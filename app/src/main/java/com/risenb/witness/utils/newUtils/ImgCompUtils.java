package com.risenb.witness.utils.newUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImgCompUtils {
    private static ImgCompUtils imgCompUtils;
    private String rootPath;

    public ImgCompUtils() {
    }

    public static ImgCompUtils getImgCompUtils() {
        if(imgCompUtils == null) {
            imgCompUtils = new ImgCompUtils();
        }

        return imgCompUtils;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath + "compress/";
        File file = new File(this.rootPath);
        if(!file.exists()) {
            file.mkdirs();
        }

    }

    public String compress(String path) {
        return this.compress(path, 800000.0D);
    }

    public String compress(String path, double max) {
        if(max < 1.0D) {
            return "";
        } else {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, opts);
            double sqrt = Math.sqrt((double)(opts.outWidth * opts.outHeight) / max);
            opts.inSampleSize = (int)sqrt;
            opts.inSampleSize = Math.max(opts.inSampleSize, 1);
            opts.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeFile(path, opts);
            Bitmap smallBitmap = this.small(bitmap, max);
            return this.saveBitmap(smallBitmap);
        }
    }

    private Bitmap small(Bitmap bitmap, double max) {
        float sqrt = (float)Math.sqrt(max / (double)(bitmap.getWidth() * bitmap.getHeight()));
        sqrt -= 0.001F;
        if(sqrt > 1.0F) {
            return bitmap;
        } else {
            Matrix matrix = new Matrix();
            matrix.postScale(sqrt, sqrt);
            Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return newBitmap;
        }
    }

    private String saveBitmap(Bitmap bitmap) {
        String path = this.rootPath + System.currentTimeMillis() + ".jpg";
        FileOutputStream fOut = null;

        try {
            fOut = new FileOutputStream(path);
        } catch (FileNotFoundException var7) {
            var7.printStackTrace();
            return "";
        }

        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fOut);

        try {
            fOut.flush();
        } catch (IOException var6) {
            var6.printStackTrace();
            return "";
        }

        try {
            fOut.close();
        } catch (IOException var5) {
            var5.printStackTrace();
            return "";
        }

        bitmap.recycle();
        return path;
    }

    public int maxSize(int num, int n) {
        return num > n?this.maxSize(num, n * 2):n;
    }
}
