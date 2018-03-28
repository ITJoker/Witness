package com.risenb.witness;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.risenb.witness.ui.tasklist.CustomCamera.FileUtil;
import com.risenb.witness.ui.vip.VipMessageUI;
import com.risenb.witness.utils.ErrorInfo;
import com.risenb.witness.utils.ImageLoaderUtils;
import com.risenb.witness.utils.MyConfig;
import com.risenb.witness.utils.NetworkUtils;
//import com.squareup.leakcanary.LeakCanary;
import com.risenb.witness.utils.SharedPreferencesUtil;
import com.risenb.witness.utils.newUtils.CrashHandler;
import com.risenb.witness.utils.newUtils.Log;
import com.risenb.witness.utils.newUtils.MUtils;
import com.risenb.witness.utils.newUtils.Utils;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.xutils.x;

import java.io.File;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {
    /**
     * 根目录
     */
    private String path;

    private static MyApplication instance;
    public static Handler handler = new Handler();

    public static String photosSaveUrl = "ObtainEvidenceImage/";
    public static String photosCompressSaveUrl = "ObtainEvidenceCompress/";
    public static String videosSaveUrl = "ObtainEvidenceVideo/";
    public static String settledTaskPhotosSaveUrl = "system/files/witness/";
    public static String settledTaskPhotosCompressSaveUrl = "system/files/witnessCompress/";

    private int webSize = 0;

    // 控制批量上传后关闭当前页面
    public static boolean isTaskListScheduleFinish = false;
    /*public static Typeface typeFace;*/

    public static int mScreenWidth = 0;
    public static int mScreenHeight = 0;
    private Bitmap mCameraBitmap;

    @Override
    public void onCreate() {
        super.onCreate();
        // typeFace = Typeface.createFromAsset(getAssets(),"fonts/NotoSansMonoCJKsc-Bold.otf");
        /*if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);*/
        instance = this;
        x.Ext.init(this);
        path = MUtils.getMUtils().getPath(this);
        Log.setDebug(!MyConfig.SIGN.equals(MUtils.getMUtils().getSignature()));
        Log.e(MUtils.getMUtils().getSignature());
        ImageLoaderUtils.initImageLoader(this);
        NetworkUtils.getNetworkUtils().setApplication(this);
        ErrorInfo.info();
        Utils.getUtils().setCapbWidth(R.dimen.dm088);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        UMShareAPI.get(this);
        Config.REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";
        //第三方登录相关
        //各个平台的配置，建议放在全局Application或者程序入口
        //微信
        PlatformConfig.setWeixin("wx3cd103750f6b14f9", "db7614ae2602ee777e8728156a65a117");
        //新浪微博
        PlatformConfig.setSinaWeibo("2103535502", "ae4896d24696b3a822ae73be1d75eaa4");
        //QQ
        PlatformConfig.setQQZone("1105716783", "4Gbm9CN9QHE2pA9y");
        CrashHandler.getInstance().setPath(ExceptionPath());
        // 设置开启日志,发布时请关闭日志
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        onReceive(this, new Intent());

        DisplayMetrics mDisplayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        mScreenWidth = mDisplayMetrics.widthPixels;
        mScreenHeight = (int) (mDisplayMetrics.heightPixels * 0.8);
        FileUtil.initFolder();

        taskLocation();
    }

    public Bitmap getCameraBitmap() {
        return mCameraBitmap;
    }

    public void setCameraBitmap(Bitmap mCameraBitmap) {
        if (mCameraBitmap != null) {
            recycleCameraBitmap();
        }
        this.mCameraBitmap = mCameraBitmap;
    }

    public void recycleCameraBitmap() {
        if (mCameraBitmap != null) {
            if (!mCameraBitmap.isRecycled()) {
                mCameraBitmap.recycle();
            }
            mCameraBitmap = null;
        }
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public void setWebSize(int webSize) {
        this.webSize = webSize;
    }

    public int getWebSize() {
        return webSize;
    }

    public int getWebSize(int dimen) {
        return dimen * webSize / 640;
    }

    public String getPath() {
        return path;
    }

    public String getScreen() {
        return MUtils.getMUtils().getShared("Screen");
    }

    public void setScreen(String c) {
        MUtils.getMUtils().setShared("", c);
    }

    public String getTaskList() {
        return MUtils.getMUtils().getShared("tasklist");
    }

    public void setTaskList(String c) {
        MUtils.getMUtils().setShared("tasklist", c);
    }

    public String getC() {
        return MUtils.getMUtils().getShared("c");
    }

    public void setC(String c) {
        MUtils.getMUtils().setShared("c", c);
    }

    public String getAreaAnwer() {
        return MUtils.getMUtils().getShared("anwer");
    }

    public void setAreaAnwer(String c) {
        MUtils.getMUtils().setShared("anwer", c);
    }

    public String getTel() {
        return MUtils.getMUtils().getShared("tel");
    }

    public void setTel(String tel) {
        MUtils.getMUtils().setShared("tel", tel);
    }

    public String getCity() {
        return MUtils.getMUtils().getShared("City");
    }

    public void setCity(String City) {
        MUtils.getMUtils().setShared("City", City);
    }

    public String getTaskExpireOnOff() {
        return MUtils.getMUtils().getShared("TaskExpireOnOff");
    }

    public void setTaskExpireOnOff(String TaskExpireOnOff) {
        MUtils.getMUtils().setShared("TaskExpireOnOff", TaskExpireOnOff);
    }

    public String getNightNotDisturb() {
        return MUtils.getMUtils().getShared("NightNotDisturb");
    }

    public void setNightNotDisturb(String NightNotDisturb) {
        MUtils.getMUtils().setShared("NightNotDisturb", NightNotDisturb);
    }

    public boolean getOne() {
        String tag = "one" + Utils.getUtils().getVersionCode(getApplicationContext());
        // isOne为true说明是第1次启动
        boolean isOne = "".equals(MUtils.getMUtils().getShared(tag));
        // 然后将其置为false
        MUtils.getMUtils().setShared(tag, "false");
        return isOne;
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通） 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][3578]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            System.out.println("收到了自定义消息，消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            System.out.println("收到了通知");
            // 在这里可以做些统计，或者做些其他工作
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            System.out.println("用户点击打开了通知");
            // 在这里可以自己写代码去定义用户点击后的行为
            Intent i = new Intent(this, VipMessageUI.class);
            startActivity(intent);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else {

        }
    }

    public String ExceptionPath() {
        String path = null;
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                path = "/sdcard/crash/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            }
        } catch (Exception e) {

        }
        Log.e("ExceptionPath", path);
        return path;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static void deletePicture(String taskIDPath) {
        while (true) {
            File dir = new File(taskIDPath);
            if (dir.exists()) {
                /*
                 * 路径正确,遍历该文件夹下的所有文件并删除
                 */
                File[] subFiles = dir.listFiles();
                for (File subFile : subFiles) {
                    // 是文件则说明是照片
                    if (subFile.isFile()) {
                        subFile.delete();
                    }
                }
            } else if (dir.isFile()) {
                // makeText("此路径为文件路径");
                return;
            } else {
                // makeText("无效路径");
                return;
            }
            dir.delete();
            return;
        }
    }

    private void taskLocation() {
        //LocationClient定位器必须在主线程中声明
        LocationClient locationClient = new LocationClient(getApplicationContext());
        BDLocationListener bdLocationListener = new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                //获取纬度信息
                SharedPreferencesUtil.saveString(MyApplication.getInstance(), "HomeLatitude", String.valueOf(bdLocation.getLatitude()));
                //获取经度信息
                SharedPreferencesUtil.saveString(MyApplication.getInstance(), "HomeLongitude", String.valueOf(bdLocation.getLongitude()));
            }
        };
        //注册监听函数
        locationClient.registerLocationListener(bdLocationListener);
        //设置百度地图SDK的定位方式
        LocationClientOption option = new LocationClientOption();
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系
        option.setCoorType("bd09ll");
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        int span = 0;
        option.setScanSpan(span);
        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);
        //可选，默认false,设置是否使用gps
        option.setOpenGps(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setLocationNotify(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        option.setEnableSimulateGps(false);
        //为定位器设置定位参数
        locationClient.setLocOption(option);
        //开始定位
        locationClient.start();
    }
}
