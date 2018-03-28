package com.risenb.witness.ui.home;

import android.content.Intent;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ZoomControls;

import com.alibaba.fastjson.JSONArray;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.risenb.witness.R;
import com.risenb.witness.beans.DWBean;
import com.risenb.witness.beans.HomeMapsMakerBeans;
import com.risenb.witness.beans.OftensiteBean;
import com.risenb.witness.beans.WBaseBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.JsonResponseHandler;
import com.risenb.witness.pop.HomeMapsMakerPop;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.ui.vip.HomeSearchUI;
import com.risenb.witness.utils.newUtils.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.homemaps)
public class HomeMapsUI extends BaseUI {
    @ViewInject(R.id.bmapView)
    private MapView mMapView;

    private BaiduMap mBaiduMap;

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    private HomeMapsMakerPop homeMapsMakerPop;

    private String homeLongitude;
    private String homeLatitude;

    private String companyLongitude;
    private String companyLatitude;

    @Override
    protected void onCreate() {
        super.onCreate();
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
    }

    protected void back() {
        Intent intent = getIntent();
        setResult(2, intent);
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("地图");
        Intent intent = getIntent();
        if (intent.getStringExtra("type").equals("3")) {
            setLatLng(intent.getStringExtra("Latitude"), intent.getStringExtra("Longitude"));
        } else {
            // 开启定位图层
            initLocation();
        }
        if (!TextUtils.isEmpty(application.getC())) {
            oftensite();
        }
    }

    @Override
    protected void prepareData() {

    }

    private void initLocation() {
        if (mBaiduMap == null) {
            mBaiduMap = mMapView.getMap();
            final UiSettings uiSettings = mBaiduMap.getUiSettings();
            mBaiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
                @Override
                public void onTouch(MotionEvent motionEvent) {
                    boolean scrollGesturesEnabled = uiSettings.isScrollGesturesEnabled();
                    System.out.println(scrollGesturesEnabled + "");
                    uiSettings.setScrollGesturesEnabled(true);
                }
            });
            /*//关闭一切手势操作
            uiSettings.setAllGesturesEnabled(false);
            //屏蔽双指下拉时变成3D地图
            uiSettings.setOverlookingGesturesEnabled(false);
            //屏蔽旋转
            uiSettings.setRotateGesturesEnabled(true);
            //获取是否允许缩放手势返回:是否允许缩放手势
            uiSettings.setZoomGesturesEnabled(false);
            //地图平移
            uiSettings.setScrollGesturesEnabled(false);*/
        }
        mMapView.showZoomControls(false);
        // 将底图标注设置为隐藏，方法如下：
        mBaiduMap.showMapPoi(true);
        // 隐藏logo
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
        //地图上比例尺
        mMapView.showScaleControl(false);
        // 普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(500);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // mapView销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            if (String.valueOf(location.getLongitude()).equals("4.9E-324") || String.valueOf(location.getLatitude()).equals("4.9E-324")) {
                setLatLng("116.403972", "39.915046");
                makeText("无法获取您的位置信息，请重新定位");
            } else {
                taskMap(location.getLongitude(), location.getLatitude());
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {

        }
    }

    private void taskMap(final double longitude, final double latitude) {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.taskMap));
        Map<String, String> param = new HashMap<>();
        param.put("longtitude", String.valueOf(longitude));
        param.put("latitude", String.valueOf(latitude));
        MyOkHttp.get().post(getActivity(), url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                List<DWBean> dwBeen = JSONArray.parseArray(wBaseBean.getData(), DWBean.class);
                sign(dwBeen);
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {

            }
        });
    }

    @Override
    public void onLoadOver() {

    }

    @OnClick(R.id.homemaps_back_ll)
    private void homeMaps_back_ll(View v) {
        back();
    }

    /**
     * 搜索
     */
    @OnClick(R.id.homemaps_sousuo_ll)
    private void homeMapsSearch_ll(View v) {

    }

    /**
     * 搜索框
     */
    @OnClick(R.id.homemaps_sousuo_et)
    private void homeMapsSearch_et(View view) {
        Intent intent = new Intent(getActivity(), HomeSearchUI.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 1) {
                setLatLng(data.getStringExtra("latitude"), data.getStringExtra("longitude"));
            }
        }
    }

    /**
     * 当前位置
     */
    @OnClick(R.id.maps_dangqian_iv)
    private void mapsNowLocation_iv(View v) {
        initLocation();
    }

    /**
     * 家位置
     */
    @OnClick(R.id.maps_home_iv)
    private void maps_home_iv(View v) {
        if (TextUtils.isEmpty(application.getC())) {
            makeText("请登录");
            return;
        }
        if (!TextUtils.isEmpty(homeLatitude) && !TextUtils.isEmpty(homeLongitude)) {
            setLatLng(homeLatitude, homeLongitude);
        } else {
            makeText("请前往个人中心设置");
        }
    }

    /**
     * 公司位置
     */
    @OnClick(R.id.maps_gongsi_iv)
    private void mapsCompany_iv(View v) {
        if (TextUtils.isEmpty(application.getC())) {
            makeText("请登录");
            return;
        }
        if (!TextUtils.isEmpty(companyLatitude) && !TextUtils.isEmpty(companyLongitude)) {
            setLatLng(companyLatitude, companyLongitude);
        } else {
            makeText("请前往个人中心设置");
        }
    }

    private void setLatLng(String Latitude, String Longitude) {
        if (mBaiduMap == null) {
            mBaiduMap = mMapView.getMap();
        }
        //设定中心点坐标companyLongitude
        LatLng latLng = new LatLng(Double.valueOf(Latitude), Double.valueOf(Longitude));
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(latLng)
                .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        taskMap(Double.valueOf(Longitude), Double.valueOf(Latitude));
    }

    private void sign(List<DWBean> dwBeen) {
        if (mBaiduMap == null) {
            mBaiduMap = mMapView.getMap();
        }
        // 隐藏缩放按钮
        mMapView.showZoomControls(false);
        for (int i = 0; i < dwBeen.size(); i++) {
            // 定义Maker坐标点
            LatLng point = new LatLng(Double.valueOf(dwBeen.get(i).getLatitude()), Double.valueOf(dwBeen.get(i)
                    .getLongtitude()));
            if (i != 0) {
                if (Double.valueOf(dwBeen.get(i).getLatitude()) == Double.valueOf(dwBeen.get(i - 1).getLatitude()) && Double.valueOf(dwBeen.get(i)
                        .getLongtitude()) == Double.valueOf(dwBeen.get(i - 1).getLongtitude())) {
                    continue;
                }
            }
            // 构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.baidumapmaker_false);
            // 构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
            // 在地图上添加Marker，并显示
            mBaiduMap.addOverlay(option);
        }
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker arg0) {
                mapCotent(String.valueOf(arg0.getPosition().longitude), String.valueOf(arg0.getPosition().latitude));
                return false;
            }
        });
    }

    private void mapCotent(final String longitude, final String latitude) {
        Utils.getUtils().showProgressDialog(getActivity());
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.mapCotent));
        Map<String, String> param = new HashMap<>();
        param.put("longitude", longitude);
        param.put("latitude", latitude);
        MyOkHttp.get().post(getActivity(), url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                List<HomeMapsMakerBeans> homeMapsMakerBeanses = JSONArray.parseArray(wBaseBean.getData(), HomeMapsMakerBeans.class);
                homeMapsMakerPop = new HomeMapsMakerPop(mMapView, HomeMapsUI.this, R.layout.homemapsmaker);
                homeMapsMakerPop.showAsDropDownInstance();
                homeMapsMakerPop.setData(homeMapsMakerBeanses);
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });

    }

    private void oftensite() {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.oftensite));
        Map<String, String> param = new HashMap<>();
        param.put("c", application.getC());
        MyOkHttp.get().post(this, url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                OftensiteBean oftensite = com.alibaba.fastjson.JSONObject.parseObject(wBaseBean.getData(), OftensiteBean.class);
                if (wBaseBean.getSuccess().equals("0")) {
                    if (wBaseBean.getErrorMsg().equals("登录异常")) {
                        errorLogin();
                    }
                } else if (wBaseBean.getSuccess().equals("1")) {
                    if (!TextUtils.isEmpty(oftensite.getHomelatitude()) && !TextUtils.isEmpty(oftensite.getHomelongitude())) {
                        homeLatitude = oftensite.getHomelatitude();
                        homeLongitude = oftensite.getHomelongitude();
                    }
                    if (!TextUtils.isEmpty(oftensite.getCompanylatitude()) && !TextUtils.isEmpty(oftensite.getCompanylongitude())) {
                        companyLatitude = oftensite.getCompanylatitude();
                        companyLongitude = oftensite.getCompanylongitude();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mLocationClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}
