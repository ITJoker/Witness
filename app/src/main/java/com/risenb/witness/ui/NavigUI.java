package com.risenb.witness.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.view.Window;
import android.view.WindowManager;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.app.AlertDialog.Builder;

import com.lidroid.xutils.view.annotation.ContentView;
import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.beans.AppVersionInfo;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.DownloadResponseHandler;
import com.risenb.witness.network.OkHttpUtils.response.GsonResponseHandler;
import com.risenb.witness.utils.FileUtils;
import com.risenb.witness.utils.NetWorksUtils;
import com.risenb.witness.utils.SDCardUtils;
import com.risenb.witness.utils.newUtils.MUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * 导航页
 */
@ContentView(R.layout.navig)
public class NavigUI extends BaseUI {
    private ProgressDialog progressDialog;
    private String installUrl = SDCardUtils.getSDCardPath() + "Download/" + "AdexMall.apk";

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void onCreate() {
        // 去掉标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 去掉信息栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void setControlBasis() {
        backGone();
        setTitle("导航页");
        initGPS();
    }

    @Override
    protected void prepareData() {

    }

    @Override
    public void onLoadOver() {

    }

    /**
     * 监听GPS
     */
    private void initGPS() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // 判断GPS模块是否开启，如果没有则开启
        if (!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("人人提示");
            dialog.setMessage("请打开GPS，以便更好的使用");
            dialog.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // 转到手机设置界面，用户设置GPS
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            // 设置完成后返回到原来的界面
                            startActivityForResult(intent, 0);
                        }
                    });
            dialog.setNeutralButton("取消", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                    finish();
                }
            });
            dialog.setCancelable(false);
            dialog.show();
        } else {
            MUtils.getMUtils().getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!NetWorksUtils.isNetworkAvailable(NavigUI.this)) {
                        makeText("当前无可用网络");
                    }
                    if (!isFinishing()) {
                        // 当前页面未关闭
                        // enterGuideUI();
                        update();
                    }
                }
            }, 300);
        }
    }

    /**
     * 联网获取服务器APK版本号
     */
    private void update() {
        String updateVersionUrl = getResources().getString(R.string.service_host_address).concat(getString(R.string.updateVersion));
        Map<String, String> param = new HashMap<>();
        param.put("c", MyApplication.getInstance().getC());
        MyOkHttp.get().post(getApplication(), updateVersionUrl, param, new GsonResponseHandler<AppVersionInfo>() {
            @Override
            public void onFailure(int statusCode, String error_msg) {
                makeText("获取新版本号信息失败");
                enterGuideUI();
            }

            @Override
            public void onSuccess(int statusCode, AppVersionInfo response) {
                if (response.getSuccess() == 1) {
                    try {
                        PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                        String versionName = packageInfo.versionName;
                        int versionCode = packageInfo.versionCode;
                        if (!versionName.equals(response.getData().getVersionName()) && versionCode < response.getData().getVersionCode()) {
                            showUpdateDialog(response.getData().getVersionName(), response.getData().getApkDescriptionInfo(), response.getData().getApkDownloadUrl());
                        } else {
                            // 无需更新版本
                            enterGuideUI();
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 更新APK对话框
     */
    private void showUpdateDialog(String newVersionName, String apkDescriptionInfo, final String apkDownloadUrl) {
        AlertDialog.Builder dialog = new Builder(this, R.style.ImageloadingDialogStyle);
        dialog.setTitle("最新版本：" + newVersionName);
        dialog.setMessage(apkDescriptionInfo);
        dialog.setIcon(R.drawable.ic_launcher);
        dialog.setCancelable(false);
        dialog.setPositiveButton("立即更新", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (new File(installUrl).exists()) {
                    installAPK();
                } else {
                    downloadApk(apkDownloadUrl);
                }
            }
        });

        dialog.setNegativeButton("以后再说", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                enterGuideUI();
            }
        });

        //设置对话框消失监听事件
        dialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                enterGuideUI();
            }
        });
        dialog.show();
    }

    /**
     * 下载最新版本的APK
     */
    protected void downloadApk(String apkDownloadUrl) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            showProgressDialog();
            // String downloadDir = Environment.getExternalStorageDirectory() + "AdexMallDownload/";
            String downloadDir = SDCardUtils.getSDCardPath() + "Download/";
            FileUtils.createOrExistsDir(downloadDir);
            MyOkHttp.get().download(getApplication(), apkDownloadUrl, downloadDir, "AdexMall.apk", new DownloadResponseHandler() {
                @Override
                public void onFinish(File download_file) {
                    progressDialog.dismiss();
                    installAPK();
                }

                @Override
                public void onProgress(long currentBytes, long totalBytes) {
                    progressDialog.setMax((int) totalBytes / 1024 / 1024);
                    progressDialog.setProgress((int) currentBytes / 1024 / 1024);
                }

                @Override
                public void onFailure(String error_msg) {
                    progressDialog.dismiss();
                    makeText("下载失败");
                    enterGuideUI();
                }
            });
        } else {
            makeText("没有可用的SD卡");
            enterGuideUI();
        }
    }

    /**
     * 下载进度条对话框
     */
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /**
     * 安装APK文件
     */
    protected void installAPK() {
        /*
         *  <intent-filter>
         <action android:name="android.intent.action.VIEW" />
         <category android:name="android.intent.category.DEFAULT" />
         <data android:scheme="content" /> content:// 内容提供者
         <data android:scheme="file" />
         <data android:mimeType="application/vnd.android.package-archive" />
         </intent-filter>
         */
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        Uri uri;
        File file = new File(installUrl);
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(getApplicationContext(), "com.risenb.witness.fileprovider", file);
            /*
             * ▲▲▲★★★对目标应用临时授权该Uri所代表的文件▲▲▲★★★
             */
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            initGPS();
        } else if (requestCode == 1) {
            try {
                File file = new File(SDCardUtils.getSDCardPath() + "Download/" + "AdexMall.apk");
                if (file.exists()) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            enterGuideUI();
        }
    }

    private void enterGuideUI() {
        Intent intent = new Intent(getActivity(), GuideUI.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
}
