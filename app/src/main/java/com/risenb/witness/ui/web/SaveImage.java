package com.risenb.witness.ui.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;

/***
 * 功能：用线程保存图片
 */
class SaveImage extends AsyncTask<String, Void, String>
{
    private FragmentActivity activity;
    private String imgurl = "";

    public SaveImage(FragmentActivity activity, String imgurl)
    {
        this.activity = activity;
        this.imgurl = imgurl;
    }

    public String getImgurl()
    {
        return imgurl;
    }

    public void setImgurl(String imgurl)
    {
        this.imgurl = imgurl;
    }

    public void setActivity(FragmentActivity activity)
    {
        this.activity = activity;
    }

    public FragmentActivity getActivity()
    {
        return activity;
    }

    @Override
    protected String doInBackground(String... params)
    {
        String result = "";
        String sdcard = Environment.getExternalStorageDirectory().toString();
        try
        {
            File file = new File(sdcard + "/DCIM/newdai");
            if (!file.exists())
            {
                file.mkdirs();
            }
            // int idx = imgurl.lastIndexOf(".");
            // String ext = imgurl.substring(idx);
            file = new File(sdcard + "/DCIM/newdai/" + new Date().getTime() + ".png");
            InputStream inputStream = null;
            URL url = new URL(imgurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(20000);
            if (conn.getResponseCode() == 200)
            {
                inputStream = conn.getInputStream();
            }
            byte[] buffer = new byte[4096];
            int len = 0;
            FileOutputStream outStream = new FileOutputStream(file);
            while ((len = inputStream.read(buffer)) != -1)
            {
                outStream.write(buffer, 0, len);
            }
            outStream.close();
            result = file.getAbsolutePath();
        }
        catch (Exception e)
        {
            result = "保存失败！" + e.getLocalizedMessage();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result)
    {
      
        MediaScannerConnection.scanFile(getActivity(), new String[] { result }, null, null);
    }
}
