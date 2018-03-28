package com.risenb.witness.utils.newUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.risenb.witness.MyApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImgUtils {
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final String IMG_PATH_KEY = "IMG_PATH_KEY";
    private static String path = "";
    private Activity activity;
    private boolean isRound;
    private Intent intentUtils;
    private CameraCallBack cameraCallBack;

    public ImgUtils(Activity activity) {
        this(activity, false, (Intent) null);
    }

    public ImgUtils(Activity activity, Intent intentUtils) {
        this(activity, false, intentUtils);
    }

    public ImgUtils(Activity activity, boolean isRound, Intent intentUtils) {
        this.isRound = false;
        if (LimitConfig.getLimitConfig().isLimit()) {
            this.activity = activity;
            this.isRound = isRound;
            this.intentUtils = intentUtils;
        }
    }

    public static void setPath(String path) {
        ImgUtils.path = path + "ico" + File.separator;
        File file = new File(ImgUtils.path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private void trimPath() {
        MUtils.getMUtils().setShared("IMG_PATH_KEY", path + System.currentTimeMillis() + ".png");
    }

    private String getPath() {
        return MUtils.getMUtils().getShared("IMG_PATH_KEY");
    }

    public void setCameraCallBack(CameraCallBack cameraCallBack) {
        this.cameraCallBack = cameraCallBack;
    }

    public void openCamera() {
        this.trimPath();
        /*Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra("return-data", false);
        intent.putExtra("output", Uri.fromFile(new File(this.getPath())));*/
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= 24) {
            /*
             * 根据getPath()的存储路径自动匹配external-path，生成content://com.adexmall.witness.fileprovider/photosSaveUrl/1503386048998.jpg路径
             */
            Uri uriForFile = FileProvider.getUriForFile(MyApplication.getInstance(), "com.risenb.witness.fileprovider", new File(getPath()));
            if (uriForFile != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
                // 授予目录临时共享权限
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(getPath())));
        }
        this.activity.startActivityForResult(intent, 101);
    }

    public void openPhotoAlbum() {
        Intent intent = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        this.activity.startActivityForResult(intent, 102);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != 0) {
            String requestPath;
            if (requestCode == 101) {
                requestPath = this.autoRotation(this.getPath());
                if (this.intentUtils == null) {
                    if (this.cameraCallBack != null) {
                        this.cameraCallBack.onCameraCallBack(requestPath);
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= 24) {
                        // 适配android7.0 ，不能直接访问原路径
                        // 需要对intent 授权
                        Uri uriForFile = FileProvider.getUriForFile(activity, "com.risenb.witness.fileprovider", new File(requestPath));
                        this.startPhotoZoom(uriForFile);
                    } else {
                        this.startPhotoZoom(Uri.fromFile(new File(requestPath)));
                        /*// 效果相同
                        this.startPhotoZoom(Uri.parse("file:" + File.separator + File.separator + File.separator + requestPath));*/
                    }
                }
            }

            if (requestCode == 103 && this.cameraCallBack != null) {
                this.cameraCallBack.onCameraCallBack(this.getPath());
            }

            if (requestCode == 104) {
                BitmapFactory.Options requestPath1 = new BitmapFactory.Options();
                requestPath1.inPreferredConfig = Bitmap.Config.RGB_565;
                Bitmap imgPath = this.toRoundCorner(BitmapFactory.decodeFile(this.getPath(), requestPath1));
                this.SaveBitmap(imgPath);
                if (this.cameraCallBack != null) {
                    this.cameraCallBack.onCameraCallBack(this.getPath());
                }
            }

            if (data != null) {
                if (requestCode == 102) {
                    requestPath = this.selectImage(this.activity, data);
                    String imgPath1 = this.autoRotation(requestPath);
                    if (this.intentUtils == null) {
                        if (this.cameraCallBack != null) {
                            this.cameraCallBack.onCameraCallBack(imgPath1);
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= 24) {
                            // 适配android7.0 ，不能直接访问原路径
                            // 需要对intent 授权
                            Uri uriForFile = FileProvider.getUriForFile(activity, "com.risenb.witness.fileprovider", new File(imgPath1));
                            this.startPhotoZoom(uriForFile);
                        } else {
                            this.startPhotoZoom(Uri.fromFile(new File(imgPath1)));
                        }
                    }
                }
            }
        }
    }

    private Bitmap toRoundCorner(Bitmap bitmap) {
        int pixels = bitmap.getWidth() / 2;
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int color = -16777216;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        float roundPx = (float) pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-16777216);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        bitmap.recycle();
        return output;
    }

    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        /*
         * 以下两行为重点之一，解决Toast提示“无法加载此图片”的问题（适配Android7.0）
         */
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        if (Build.VERSION.SDK_INT >= 19) {
            if (Build.VERSION.SDK_INT >= 24) {
                intent.setDataAndType(uri, "image/*");
            } else {
                String url = GetImagePath.getImagePath().getPath(uri);
                intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
            }
        } else {
            intent.setDataAndType(uri, "image/*");
        }
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", this.intentUtils.getIntExtra("aspectX", 1));
        intent.putExtra("aspectY", this.intentUtils.getIntExtra("aspectY", 1));
        intent.putExtra("outputX", this.intentUtils.getIntExtra("outputX", 150));
        intent.putExtra("outputY", this.intentUtils.getIntExtra("outputY", 150));
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        this.trimPath();
        intent.putExtra("return-data", false);
        /*
         * 相机调用FileProvider.getUriForFile，重点是裁剪EXTRA_OUTPUT不用，否者在onActivityResult时，返回的resultCode为0，即取消
         */
        intent.putExtra("output", Uri.fromFile(new File(this.getPath())));
        this.activity.startActivityForResult(intent, this.isRound ? 104 : 103);
    }

    private void SaveBitmap(Bitmap bitmap) {
        this.trimPath();
        FileOutputStream fOut = null;

        try {
            fOut = new FileOutputStream(this.getPath());
        } catch (FileNotFoundException var6) {
            var6.printStackTrace();
        }

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);

        try {
            fOut.flush();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        try {
            fOut.close();
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }

    private String selectImage(Context context, Intent data) {
        Uri selectedImage = data.getData();
        if (selectedImage != null) {
            String filePathColumn = selectedImage.toString();
            String cursor = filePathColumn.substring(10, filePathColumn.length());
            if (cursor.startsWith("com.sec.android.gallery3d")) {
                Log.e("It\'s auto backup pic path:" + selectedImage.toString());
                return null;
            }
        }

        String[] filePathColumn1 = new String[]{"_data"};
        Cursor cursor1 = context.getContentResolver().query(selectedImage, filePathColumn1, (String) null, (String[]) null, (String) null);
        if (cursor1 == null) {
            return selectedImage.getPath();
        } else {
            cursor1.moveToFirst();
            int columnIndex = cursor1.getColumnIndex(filePathColumn1[0]);
            String picturePath = cursor1.getString(columnIndex);
            cursor1.close();
            return picturePath;
        }
    }

    private String autoRotation(String rotPath) {
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(rotPath);
        } catch (IOException var9) {
            var9.printStackTrace();
        }

        if (exif != null) {
            int orientation = exif.getAttributeInt("Orientation", 0);
            boolean angle = false;
            short angle1;
            switch (orientation) {
                case 3:
                    angle1 = 180;
                    break;
                case 6:
                    angle1 = 90;
                    break;
                case 8:
                    angle1 = 270;
                    break;
                default:
                    angle1 = 0;
            }

            if (angle1 != 0) {
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(rotPath, opts);
                opts.inJustDecodeBounds = false;
                opts.inPreferredConfig = Bitmap.Config.RGB_565;
                opts.inSampleSize = Math.max(opts.outWidth / 600, 1);
                Bitmap photo = BitmapFactory.decodeFile(rotPath, opts);
                Matrix matrix = new Matrix();
                matrix.postRotate((float) angle1);
                Bitmap result = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight(), matrix, true);
                photo.recycle();
                photo = null;
                this.SaveBitmap(result);
                return this.getPath();
            }
        }

        return rotPath;
    }
}
