package com.risenb.witness.utils.newUtils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

public class GetImagePath {
    private static GetImagePath imagePath;

    public static GetImagePath getImagePath() {
        if(imagePath == null) {
            imagePath = new GetImagePath();
        }

        return imagePath;
    }

    public String getPath(String path) {
        return this.getPath(Uri.parse(path));
    }

    @SuppressLint({"NewApi"})
    public String getPath(Uri uri) {
        if(Build.VERSION.SDK_INT < 19) {
            return uri.getPath();
        } else {
            boolean isKitKat = Build.VERSION.SDK_INT >= 19;
            if(isKitKat && DocumentsContract.isDocumentUri(MUtils.getMUtils().getApplication(), uri)) {
                String docId;
                String[] split;
                String type;
                if(this.isExternalStorageDocument(uri)) {
                    docId = DocumentsContract.getDocumentId(uri);
                    split = docId.split(":");
                    type = split[0];
                    if("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                } else {
                    if(this.isDownloadsDocument(uri)) {
                        docId = DocumentsContract.getDocumentId(uri);
                        Uri split1 = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId).longValue());
                        return this.getDataColumn(MUtils.getMUtils().getApplication(), split1, null, null);
                    }

                    if(this.isMediaDocument(uri)) {
                        docId = DocumentsContract.getDocumentId(uri);
                        split = docId.split(":");
                        type = split[0];
                        Uri contentUri = null;
                        if("image".equals(type)) {
                            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        } else if("video".equals(type)) {
                            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        } else if("audio".equals(type)) {
                            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        }

                        String selection = "_id=?";
                        String[] selectionArgs = new String[]{split[1]};
                        return this.getDataColumn(MUtils.getMUtils().getApplication(), contentUri, "_id=?", selectionArgs);
                    }
                }
            } else {
                if("content".equalsIgnoreCase(uri.getScheme())) {
                    if(this.isGooglePhotosUri(uri)) {
                        return uri.getLastPathSegment();
                    }

                    return this.getDataColumn(MUtils.getMUtils().getApplication(), uri, null, null);
                }

                if("file".equalsIgnoreCase(uri.getScheme())) {
                    return uri.getPath();
                }
            }

            return null;
        }
    }

    private String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = "_data";
        String[] projection = new String[]{"_data"};

        String var9;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if(cursor == null || !cursor.moveToFirst()) {
                return null;
            }

            int index = cursor.getColumnIndexOrThrow("_data");
            var9 = cursor.getString(index);
        } finally {
            if(cursor != null) {
                cursor.close();
            }

        }

        return var9;
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
