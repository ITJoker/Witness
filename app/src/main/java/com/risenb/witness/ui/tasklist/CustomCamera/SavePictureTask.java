package com.risenb.witness.ui.tasklist.CustomCamera;

import android.os.AsyncTask;
import android.widget.Toast;

import com.risenb.witness.MyApplication;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @MainThread
 * public final AsyncTask<Params, Progress, Result> execute(Params... params) {
 * return executeOnExecutor(sDefaultExecutor, params);
 * }
 */
class SavePictureTask extends AsyncTask<byte[], String, String> {
    private SavePictureTaskCompletedListener savePictureTaskCompletedListener;
    @Override
    protected String doInBackground(byte[]... params) {
        // 获取Jpeg图片，并保存在SD卡上
        String photoSavePath = new String(params[1]);
        File pictureFile = new File(photoSavePath);
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(params[0]);
            fos.close();
        } catch (Exception e) {
            Toast.makeText(MyApplication.getInstance(), "照片保存失败", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if (savePictureTaskCompletedListener != null) {
            savePictureTaskCompletedListener.savePictureTaskCompleted();
        }
    }

    public void setSavePictureTaskCompletedListener(SavePictureTaskCompletedListener savePictureTaskCompletedListener) {
        this.savePictureTaskCompletedListener = savePictureTaskCompletedListener;
    }
}
