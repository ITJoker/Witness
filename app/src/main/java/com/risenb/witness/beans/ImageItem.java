package com.risenb.witness.beans;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class ImageItem extends VideoOrPhotoItemSign implements Parcelable {
    private String imageId;
    private String thumbnailPath;
    private String imagePath;
    private Bitmap bitmap;
    private boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageId);
        dest.writeString(thumbnailPath);
        dest.writeString(imagePath);
        dest.writeParcelable(bitmap, flags);
        dest.writeBooleanArray(new boolean[]{isSelected});
    }

    public static final Creator<ImageItem> CREATOR = new Creator<ImageItem>() {
        /**
         * 供外部类反序列化本类数组使用
         */
        @Override
        public ImageItem[] newArray(int size) {
            return new ImageItem[size];
        }

        /**
         * 从Parcel中读取数据
         */
        @Override
        public ImageItem createFromParcel(Parcel source) {
            ImageItem imageItem = new ImageItem();
            imageItem.imageId = source.readString();
            imageItem.thumbnailPath = source.readString();
            imageItem.imagePath = source.readString();
            imageItem.bitmap = source.readParcelable(Bitmap.class.getClassLoader());
            boolean[] selected = new boolean[1];
            source.readBooleanArray(selected);
            imageItem.isSelected = selected[0];
            return imageItem;
        }
    };
}
