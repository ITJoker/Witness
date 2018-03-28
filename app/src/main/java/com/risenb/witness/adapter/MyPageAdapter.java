package com.risenb.witness.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.risenb.witness.beans.ImageItem;
import com.risenb.witness.utils.MyConfig;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

public class MyPageAdapter extends PagerAdapter {
    private ArrayList<ImageItem> tempSelectBitmap;
    private Context context;
    private ArrayList<View> listViews;

    private int size;

    public MyPageAdapter(ArrayList<View> listViews, ArrayList<ImageItem> tempSelectBitmap, Context context) {
        this.listViews = listViews;
        this.tempSelectBitmap = tempSelectBitmap;
        this.context = context;
        size = listViews == null ? 0 : listViews.size();
    }

    public void setListViews(ArrayList<View> listViews) {
        this.listViews = listViews;
        size = listViews == null ? 0 : listViews.size();
    }

    public int getCount() {
        return size;
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView(listViews.get(arg1 % size));
    }

    public void finishUpdate(View arg0) {
    }

    public Object instantiateItem(View arg0, int arg1) {
        try {
            PhotoView photoView = (PhotoView) listViews.get(arg1 % size);
            if (tempSelectBitmap.get(arg1).getImagePath().contains("http://")) {
                /*ImageLoader.getInstance().displayImage(tempSelectBitmap.get(arg1).getImagePath(), photoView, MyConfig.options);*/
                Glide.with(context)
                        .load(tempSelectBitmap.get(arg1).getImagePath())
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(photoView);
            } else {
                /*ImageLoader.getInstance().displayImage("file://" + tempSelectBitmap.get(arg1).getImagePath(), photoView, MyConfig.options);*/
                Glide.with(context)
                        .load("file://" + tempSelectBitmap.get(arg1).getImagePath())
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(photoView);
            }
            ((ViewPager) arg0).addView(photoView, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listViews.get(arg1 % size);
    }

    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
}
