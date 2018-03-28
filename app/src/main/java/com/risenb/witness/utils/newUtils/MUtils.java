package com.risenb.witness.utils.newUtils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.risenb.witness.views.newViews.XListUtils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MUtils {
    private static MUtils nUtils;
    private Application application;
    private Handler handler;
    private Class<?> claLayout;
    private Class<?> claID;
    private Class<?> claStyle;
    private Class<?> claDrawable;
    private Class<?> claXml;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor edit;
    @SuppressLint({"UseSparseArrays"})
    private Map<Integer, Bitmap> defaultBitmapMap = new HashMap();
    private int widthPixels;
    private int heightPixels;
    private float density;
    private int densityDpi;
    private String path = null;
    private String rootDirectory = null;
    private String strPath = null;

    public MUtils() {
    }

    public static MUtils getMUtils() {
        if(nUtils == null) {
            nUtils = new MUtils();
        }

        return nUtils;
    }

    public Handler getHandler() {
        return this.handler;
    }

    public Application getApplication() {
        return this.application;
    }

    public String getRootDirectory() {
        return this.rootDirectory;
    }

    public String getPath(Application application) {
        this.application = application;
        LimitConfig.KEY = application.getPackageName();
        String dataPath = "Android" + File.separator + "data" + File.separator + application.getPackageName() + File.separator + "cache" + File.separator;
        this.sharedPreferences = application.getSharedPreferences(application.getPackageName(), 0);
        this.edit = this.sharedPreferences.edit();
        this.handler = new Handler();
        Utils.getUtils().setApplication(application);
        LimitConfig.getLimitConfig().info();
        if(Build.VERSION.RELEASE.indexOf("4.3") == 0) {
            this.path = this.getDiskCacheDir();
        }

        if(TextUtils.isEmpty(this.path)) {
            List e = this.getExtSDCardPath();

            for(int i = 0; i < e.size(); ++i) {
                this.path = e.get(i) + File.separator + dataPath;
                File file = new File(this.path);
                if(!file.exists()) {
                    file.mkdirs();
                }

                if(file.exists()) {
                    break;
                }
            }
        }

        if(TextUtils.isEmpty(this.path)) {
            this.path = this.getDiskCacheDir();
        }

        try {
            this.info();
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        if(!TextUtils.isEmpty(this.path)) {
            this.rootDirectory = this.path.replace(dataPath, "");
        }

        return this.path;
    }

    private List<String> getExtSDCardPath() {
        ArrayList lResult = new ArrayList();

        try {
            Runtime e = Runtime.getRuntime();
            Process proc = e.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String line;
            while((line = br.readLine()) != null) {
                if(line.contains("extSdCard")) {
                    String[] arr = line.split(" ");
                    String path = arr[1];
                    File file = new File(path);
                    if(file.isDirectory()) {
                        lResult.add(path);
                    }
                }
            }
            isr.close();
        } catch (Exception var11) {
        }

        return lResult;
    }

    public String getSignature() {
        String str = "";

        try {
            PackageManager e = this.application.getPackageManager();
            StringBuilder builder = new StringBuilder();
            PackageInfo packageInfo = e.getPackageInfo(this.application.getPackageName(), 64);
            Signature[] signatures = packageInfo.signatures;
            Signature[] var6 = signatures;
            int var7 = signatures.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                Signature signature = var6[var8];
                builder.append(signature.toCharsString());
            }

            str = builder.toString();
        } catch (PackageManager.NameNotFoundException var10) {
            var10.printStackTrace();
        }

        return str;
    }

    private void info() throws Exception {
        if(LimitConfig.getLimitConfig().isLimit()) {
            this.strPath = this.path + "/str/";
            File file = new File(this.strPath);
            if(!file.exists()) {
                file.mkdirs();
            }

            ImgUtils.setPath(this.path);
            CrashHandler.getInstance().setPath(this.path);
            ImgCompUtils.getImgCompUtils().setRootPath(this.path);
            this.claLayout = this.infoForName(".R$layout");
            this.claID = this.infoForName(".R$id");
            this.claStyle = this.infoForName(".R$style");
            this.claDrawable = this.infoForName(".R$drawable");
            this.claXml = this.infoForName(".R$xml");
            Utils.getUtils().info(this.getLayout("common_waiting_dialog"), this.getID("capb_dialog"), this.getID("tv_dialog"), this.getStyle("custom_dialog_style"));
            SeletTimeUtils.getSeletTimeUtils().info(this.getDrawable("wheel_val"), this.getDrawable("wheel_bg"), this.getLayout("pop_time"), this.getID("tv_pop_time_cancel"), this.getID("tv_pop_time_submit"), this.getID("dp_pop_time"));
            SortUtils.getSrtUtils().info(this.getLayout("pop_side_bar"), this.getID("tv_side_bar"));
            XListUtils.getXListUtils().info(this.getID("xlistview_header_content"), this.getID("xlistview_header_time"), this.getLayout("xlistview_header"), this.getID("xlistview_header_arrow"), this.getID("xlistview_header_hint_textview"), this.getID("xlistview_header_progressbar"));
            IDcardUtils.getIdCardUtils().info(this.getXml("symbols"));
        }
    }

    private Class<?> infoForName(String className) {
        try {
            return Class.forName(this.application.getPackageName() + className);
        } catch (Exception var3) {
            System.out.println(className + " == null");
            return null;
        }
    }

    private int getLayout(String key) {
        if(this.claLayout == null) {
            System.out.println("claLayout == null");
            return 0;
        } else {
            try {
                return this.claLayout.getField(key).getInt("");
            } catch (Exception var3) {
                System.out.println(key + " == null");
                return 0;
            }
        }
    }

    private int getID(String key) {
        if(this.claID == null) {
            System.out.println("claID == null");
            return 0;
        } else {
            try {
                return this.claID.getField(key).getInt("");
            } catch (Exception var3) {
                System.out.println(key + " == null");
                return 0;
            }
        }
    }

    private int getStyle(String key) {
        if(this.claStyle == null) {
            System.out.println("claStyle == null");
            return 0;
        } else {
            try {
                return this.claStyle.getField(key).getInt("");
            } catch (Exception var3) {
                System.out.println(key + " == null");
                return 0;
            }
        }
    }

    private int getDrawable(String key) {
        if(this.claDrawable == null) {
            System.out.println("claDrawable == null");
            return 0;
        } else {
            try {
                return this.claDrawable.getField(key).getInt("");
            } catch (Exception var3) {
                System.out.println(key + " == null");
                return 0;
            }
        }
    }

    private int getXml(String key) {
        if(this.claXml == null) {
            System.out.println("claXml == null");
            return 0;
        } else {
            try {
                return this.claXml.getField(key).getInt("");
            } catch (Exception var3) {
                System.out.println(key + " == null");
                return 0;
            }
        }
    }

    public void setShared(String key, String value) {
        this.edit.putString(key, value).commit();
    }

    public String getShared(String key) {
        return this.getShared(key, "");
    }

    public String getShared(String key, String value) {
        return this.sharedPreferences.getString(key, value);
    }

    public void saveMem(String key, String value) {
        this.edit.putString(key, value).commit();
    }

    public String getMem(String key) {
        return this.getMem(key, "");
    }

    public String getMem(String key, String value) {
        return this.sharedPreferences.getString(key, value);
    }

    public void saveSD(String key, String value) {
        try {
            byte[] e = value.getBytes("GBK");
            FileOutputStream fos = new FileOutputStream(this.strPath + key, false);
            DataOutputStream out = new DataOutputStream(fos);
            out.write(e);
            out.close();
        } catch (IOException var6) {
            ;
        }

    }

    public String getSD(String key) {
        return this.getSD(key, "");
    }

    public String getSD(String key, String value) {
        try {
            FileInputStream fis = new FileInputStream(this.strPath + key);
            DataInputStream dis = new DataInputStream(fis);
            byte[] e = new byte[dis.available()];
            dis.read(e);
            dis.close();
            String result = new String(e, "GBK");
            return result;
        } catch (Exception var7) {
            return value;
        }
    }

    public String getDiskCacheDir() {
        String cachePath = null;
        File cacheDir;
        if("mounted".equals(Environment.getExternalStorageState())) {
            cacheDir = this.application.getExternalCacheDir();
            if(cacheDir != null) {
                cachePath = cacheDir.getPath();
            }
        }

        if(cachePath == null) {
            cacheDir = this.application.getCacheDir();
            if(cacheDir != null && cacheDir.exists()) {
                cachePath = cacheDir.getPath();
            }
        }

        return cachePath + File.separator;
    }

    public void machineInformation() {
        DisplayMetrics metric = new DisplayMetrics();
        ((WindowManager)this.application.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metric);
        this.widthPixels = metric.widthPixels;
        this.heightPixels = metric.heightPixels;
        this.density = metric.density;
        this.densityDpi = metric.densityDpi;
        Log.mem();
        Log.e("W = " + this.widthPixels + " H = " + this.heightPixels + " DENSITY = " + this.density + " DENSITYDPI = " + this.densityDpi + " VERSION = " + Build.VERSION.RELEASE);
    }

    public int getWidthPixels() {
        return this.widthPixels;
    }

    public int getHeightPixels() {
        return this.heightPixels;
    }

    public float getDensity() {
        return this.density;
    }

    public int getDensityDpi() {
        return this.densityDpi;
    }

    public Map<Integer, Bitmap> getDefaultBitmapMap() {
        return this.defaultBitmapMap;
    }
}
