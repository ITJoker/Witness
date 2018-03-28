package com.risenb.witness.ui.home;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nineoldandroids.view.ViewHelper;
import com.risenb.witness.NotificationsUtils;
import com.risenb.witness.R;
import com.risenb.witness.beans.HomeDistanceFrgBean;
import com.risenb.witness.beans.WBaseBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.JsonResponseHandler;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.utils.CommonUtils;
import com.risenb.witness.utils.SDCardUtils;
import com.risenb.witness.utils.SharedPreferencesUtil;
import com.risenb.witness.utils.newUtils.Utils;
import com.risenb.witness.utils.sardar.JPushUtil;
import com.risenb.witness.views.newViews.ZoomControlView;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * 任务大厅
 */
@ContentView(R.layout.home)
public class HomeUI extends BaseUI {
    public static String address = "";

    public static int type = 1;

    private ViewPager viewPager;
    private View tab;
    private TextView[] tabs;
    private int currentIndex;
    private SettledTaskFragment settledTaskFragment;
    public static HomeDistanceFragment homeDistanceFragment;
    public static HomePriceFragment homePriceFragment;

    private TextView message_tv0;
    private TextView message_tv1;
    private TextView message_tv2;

    @ViewInject(R.id.bmapView)
    private MapView mMapView;

    @ViewInject(R.id.ZoomControls)
    private ZoomControlView ZoomControls;

    private BaiduMap mBaiduMap;

    public LocationClient mLocationClient;
    public BDLocationListener myListener = new MyListener();

    public static boolean isForeground = false;

    private boolean flags = false;

    private String cityId;
    private String mediaId;
    private String companyId;
    public static int info = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        registerMessageReceiver();
    }

    @Override
    protected void back() {
        exit();
    }

    @Override
    protected void setControlBasis() {
        setTitle("任务大厅");
        backGone();
        leftVisible(R.drawable.home_titleleft);
        rightVisible(R.drawable.ic_search);
    }

    @Override
    protected void prepareData() {
        // 开启定位图层
        initLocation();
        try {
            File file = new File(SDCardUtils.getSDCardPath() + "Download/" + "AdexMall.apk");
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initNotification();
    }

    private void initNotification() {
        if (!NotificationsUtils.isNotificationEnabled(getApplicationContext())) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.AlertDialog);
            dialog.setMessage("请前往设置打开通知，以便接收消息");
            dialog.setCancelable(false);
            dialog.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {
                    NotificationsUtils.requestPermission(getActivity(), 0);
                }
            });
            dialog.setNeutralButton("取消", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public void initLocation() {
        Utils.getUtils().showProgressDialog(getActivity());
        mBaiduMap = mMapView.getMap();
        mMapView.showZoomControls(false);
        // 普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        ZoomControls.setMapView(mMapView);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        // 打开gps
        option.setOpenGps(true);
        // 设置坐标类型
        option.setCoorType("bd09ll");
        option.setScanSpan(1000 * 60 * 5);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            String getLatitude = String.valueOf(location.getLatitude());
            SharedPreferencesUtil.saveString(getApplication(), "HomeLatitude", getLatitude);
            String getLongitude = String.valueOf(location.getLongitude());
            SharedPreferencesUtil.saveString(getApplication(), "HomeLongitude", getLongitude);
            address = location.getAddrStr();
            if (!flags && !getLatitude.equals("") && !getLongitude.equals("")) {
                // 加载布局
                initView();
                flags = true;
            }
            application.setCity(location.getCity());
        }

        public void onReceivePoi(BDLocation poiLocation) {

        }
    }

    private void initView() {
        message_tv0 = findViewById(R.id.message_tv0);
        message_tv1 = findViewById(R.id.message_tv1);
        message_tv2 = findViewById(R.id.message_tv2);
        viewPager = findViewById(R.id.viewPager);
        tab = findViewById(R.id.message_tab0);
        tabs = new TextView[3];
        tabs[0] = message_tv0;
        tabs[1] = message_tv1;
        tabs[2] = message_tv2;
        List<Fragment> list = new ArrayList<>();
        settledTaskFragment = new SettledTaskFragment();
        homeDistanceFragment = new HomeDistanceFragment();
        homePriceFragment = new HomePriceFragment();
        // 固定任务
        list.add(settledTaskFragment);
        // 距离最近
        list.add(homeDistanceFragment);
        // 金额最高
        list.add(homePriceFragment);
        viewPager.setAdapter(new MessagePageAdapter(getSupportFragmentManager(), list));
        // viewPager的滑动事件
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                tab.setBackgroundResource(R.color.main_green);
                tab.clearAnimation();
                tabs[currentIndex].setSelected(false);
                tabs[arg0].setSelected(true);
                currentIndex = arg0;
                if (currentIndex == 0) {
                    message_tv0.setTextColor(getResources().getColor(R.color.main_green));
                    message_tv1.setTextColor(getResources().getColor(R.color.main_gray));
                    message_tv2.setTextColor(getResources().getColor(R.color.main_gray));
                    type = 1;
                } else if (currentIndex == 1) {
                    message_tv0.setTextColor(getResources().getColor(R.color.main_gray));
                    message_tv1.setTextColor(getResources().getColor(R.color.main_green));
                    message_tv2.setTextColor(getResources().getColor(R.color.main_gray));
                    type = 2;
                } else if (currentIndex == 2) {
                    message_tv0.setTextColor(getResources().getColor(R.color.main_gray));
                    message_tv1.setTextColor(getResources().getColor(R.color.main_gray));
                    message_tv2.setTextColor(getResources().getColor(R.color.main_green));
                    type = 3;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                /*
                 * arg0:当前页面,及你点击滑动的页面
                 * arg1:当前页面偏移的百分比
                 * arg2:当前页面偏移的像素位置
                 */
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int num = CommonUtils.dip2px(HomeUI.this, 0);
                int width2 = tab.getWidth() + num;
                int w = (int) (arg1 * width2) + arg0 * width2;
                ViewHelper.setTranslationX(tab, w);
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        currentIndex = 1;
        viewPager.setCurrentItem(currentIndex);
        tabs[currentIndex].setSelected(true);
    }

    // TabView的点击事件
    public void onTabClick(View v) {
        if (v.isSelected()) {
            return;
        }
        tabs[currentIndex].setSelected(false);
        int id = v.getId();
        switch (id) {
            case R.id.message_tv0:
                currentIndex = 0;
                message_tv0.setTextColor(getResources().getColor(R.color.main_green));
                message_tv1.setTextColor(getResources().getColor(R.color.main_gray));
                message_tv2.setTextColor(getResources().getColor(R.color.main_gray));
                type = 1;
                break;
            case R.id.message_tv1:
                currentIndex = 1;
                message_tv0.setTextColor(getResources().getColor(R.color.main_gray));
                message_tv1.setTextColor(getResources().getColor(R.color.main_green));
                message_tv2.setTextColor(getResources().getColor(R.color.main_gray));
                type = 2;
                break;
            case R.id.message_tv2:
                currentIndex = 2;
                message_tv0.setTextColor(getResources().getColor(R.color.main_gray));
                message_tv1.setTextColor(getResources().getColor(R.color.main_gray));
                message_tv2.setTextColor(getResources().getColor(R.color.white));
                type = 3;
                break;
            case R.id.back:
                finish();
                break;
        }
        tabs[currentIndex].setSelected(true);
        viewPager.setCurrentItem(currentIndex, true);
    }

    @Override
    public void onLoadOver() {

    }

    /**
     * 筛选
     */
    @OnClick(R.id.iv_right)
    private void iv_right(View view) {
        startActivityForResult(new Intent(this, HomeScreenUI.class), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 1) {
                cityId = data.getStringExtra("cityId");
                mediaId = data.getStringExtra("mediaId");
                companyId = data.getStringExtra("companyId");
                if (!TextUtils.isEmpty(cityId) || !TextUtils.isEmpty(mediaId) || !TextUtils.isEmpty(companyId)) {
                    taskSearch();
                } else {
                    if (type == 1) {
                        taskList(1, "2");
                    } else {
                        taskList(1, "1");
                    }
                }
            }
        } else if (requestCode == 2) {
            if (resultCode == 2) {
                if (type == 1) {
                    taskList(1, "2");
                } else {
                    taskList(1, "1");
                }
                info = 1;
            }
        } /*else if (requestCode == 0) {
            if (!NotificationsUtils.isNotificationEnabled(getApplicationContext())) {
                NotificationsUtils.requestPermission(getActivity(), 0);
            }
        }*/
    }

    private void taskList(final int page, final String sort) {
        Utils.getUtils().showProgressDialog(getActivity());
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.taskList));
        Map<String, String> param = new HashMap<>();
        param.put("pagecount", String.valueOf(page));
        param.put("pagesize", "10");
        param.put("sort", sort);
        param.put("longitude", SharedPreferencesUtil.getString(getApplication(), "HomeLongitude", ""));
        param.put("latitude", SharedPreferencesUtil.getString(getApplication(), "HomeLatitude", ""));
        MyOkHttp.get().post(getActivity(), url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                if ("1".equals(wBaseBean.getSuccess())) {
                    List<HomeDistanceFrgBean> homeDistanceFrgBeen = JSONArray.parseArray(wBaseBean.getData(), HomeDistanceFrgBean.class);
                    if (type == 1) {
                        homeDistanceFragment.setData(homeDistanceFrgBeen);
                    } else {
                        homePriceFragment.setData(homeDistanceFrgBeen);
                    }
                } else {
                    if (wBaseBean.getErrorMsg().equals("登录异常")) {
                        if (!application.getOne())
                            errorLogin();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }

    public void taskSearch() {
        Utils.getUtils().showProgressDialog(getActivity());
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.taskSearch));
        Map<String, String> param = new HashMap<>();
        if (!TextUtils.isEmpty(cityId)) {
            param.put("CityId", cityId);
        }
        if (!TextUtils.isEmpty(mediaId)) {
            param.put("MediaTreeID", mediaId);
        }
        if (!TextUtils.isEmpty(companyId)) {
            param.put("CompanyId", companyId);
        }
//        param.put("pagecount", String.valueOf(page));
//        param.put("pagesize", "10");
        if (type == 1) {
            param.put("sort", "2");
        } else {
            param.put("sort", "1");
        }
        param.put("longitude", SharedPreferencesUtil.getString(getApplication(), "HomeLongitude", ""));
        param.put("latitude", SharedPreferencesUtil.getString(getApplication(), "HomeLatitude", ""));
        MyOkHttp.get().post(getApplication(), url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                if ("1".equals(wBaseBean.getSuccess())) {
                    List<HomeDistanceFrgBean> homeDistanceFrgBeen = JSONArray.parseArray(wBaseBean.getData(), HomeDistanceFrgBean.class);
                    if (type == 1) {
                        homeDistanceFragment.setData(homeDistanceFrgBeen);
                    } else {
                        homePriceFragment.setData(homeDistanceFrgBeen);
                    }
                } else {
                    if (wBaseBean.getErrorMsg().equals("登录异常")) {
                        errorLogin();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }

    /**
     * 接收广播
     */
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String message = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + message + "\n");
                if (!JPushUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                notifyShow(message);
            }
        }
    }

    /**
     * 设置通知提示方式 - 基础属性
     */
    private void setStyleBasic() {
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(HomeUI.this);
        builder.statusBarDrawable = R.drawable.ic_launcher;
        //设置为点击后自动消失
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;
        //设置为铃声（ Notification.DEFAULT_SOUND）或者震动（ Notification.DEFAULT_VIBRATE）
        builder.notificationDefaults = Notification.DEFAULT_SOUND;
        builder.developerArg0 = "";
        JPushInterface.setPushNotificationBuilder(1, builder);
        Toast.makeText(getApplication(), "Basic Builder - 1", Toast.LENGTH_SHORT).show();
    }

    /**
     * 设置通知栏样式 - 定义通知栏Layout
     */
    private void setStyleCustom(String message) {
        // NotificationManager manager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
        CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(HomeUI.this, R.layout.customer_notitfication_layout, R.id.icon, R.id.title, R.id.text);
        builder.layoutIconDrawable = R.drawable.ic_launcher;
        builder.developerArg0 = message;
        JPushInterface.setDefaultPushNotificationBuilder(builder);
        JPushInterface.setPushNotificationBuilder(2, builder);
        Toast.makeText(HomeUI.this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 通知栏
     */
    private void notifyShow(String message) {
        NotificationManager manager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
        // 使用notification
        // 使用广播或者通知进行内容的显示
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplication());
        builder.setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(JPushInterface.EXTRA_TITLE);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        manager.notify(1, builder.build());
    }

    /**
     * 地图
     */
    @OnClick(R.id.iv_left)
    private void iv_left(View view) {
        startActivity(new Intent(this, HomeMapsUI.class).putExtra("type", "1"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isForeground = false;
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMessageReceiver);
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mLocationClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
    }
}
