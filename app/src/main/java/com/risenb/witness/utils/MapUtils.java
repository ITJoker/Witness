package com.risenb.witness.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class MapUtils {
    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        //获取PackageManager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfoList != null) {
            for (int i = 0; i < packageInfoList.size(); i++) {
                String packName = packageInfoList.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    public static void mapGuide(Context context, String longitude, String latitude, String positionName) {
        Intent intent;
        /*
         * 百度地图
         */
        /*if (isAvilible(context, "com.baidu.BaiduMap")) {//传入指定应用包名
            try {
                //intent = Intent.getIntent("intent://map/direction?origin=latlng:34.264642646862,108.95108518068|name:我家&destination=大雁塔&mode=driving®ion=西安&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                intent = Intent.getIntent("intent://map/direction?" +
                        //"origin=latlng:"+"34.264642646862,108.95108518068&" +   //起点  此处不传值默认选择当前位置
                        "destination=latlng:" + latitude + "," + longitude + "|name:我的目的地" +        //终点
                        "&mode=driving&" +          //导航路线方式
                        "region=北京" +           //
                        "&src=人人目击#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                context.startActivity(intent); //启动调用
            } catch (URISyntaxException e) {
                Log.e("intent", e.getMessage());
            }
        } else {
            //未安装
            //market为路径，id为包名
            //显示手机上所有的market商店
            Toast.makeText(context, "您尚未安装百度地图", Toast.LENGTH_LONG).show();
            Uri uri = Uri.parse("market://details?id=com.baidu.BaiduMap");
            intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }*/

        /*
         * 高德地图
         */
        if (isAvilible(context, "com.autonavi.minimap")) {
            try {
                intent = Intent.getIntent("androidamap://navi?sourceApplication=人人目击&poiname=" + positionName + "&lat=" + latitude + "&lon=" + longitude + "&dev=0");
                context.startActivity(intent);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "您尚未安装高德地图", Toast.LENGTH_LONG).show();
            Uri uri = Uri.parse("market://details?id=com.autonavi.minimap");
            intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }

        /*
         * 谷歌地图
         */
        /*if (isAvilible(context, "com.google.android.apps.maps")) {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude + ", + Sydney +Australia");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            context.startActivity(mapIntent);
        } else {
            Toast.makeText(context, "您尚未安装谷歌地图", Toast.LENGTH_LONG).show();

            Uri uri = Uri.parse("market://details?id=com.google.android.apps.maps");
            intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }*/
    }
}
