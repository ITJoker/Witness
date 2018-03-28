package com.risenb.witness.utils.newUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.risenb.witness.utils.newNetwork.BaseBannerBean;

import java.util.ArrayList;
import java.util.List;

public class MPagerAdapter<T extends BaseBannerBean> extends PagerAdapter {
    private Context context;
    private List<View> listView = new ArrayList();
    private List<T> list;
    private MPagerAdapter.OnMPagerCallBack onMPagerCallBack;
    private AdapterView.OnItemClickListener onItemClickListener;
    private int defaultImg;
    private BaseBannerView baseBannerView;
    private int screenW;
    private int screenH;
    private boolean loop = true;
    private DisplayImageOptions displayImageOptions;

    public MPagerAdapter(Context context, List<T> list, MPagerAdapter.OnMPagerCallBack onMPagerCallBack, int defaultImg, BaseBannerView baseBannerView, AdapterView.OnItemClickListener onItemClickListener, int screenW, int screenH, boolean loop) {
        if(LimitConfig.getLimitConfig().isLimit()) {
            this.context = context;
            this.list = list;
            this.onMPagerCallBack = onMPagerCallBack;
            this.onItemClickListener = onItemClickListener;
            this.defaultImg = defaultImg;
            this.baseBannerView = baseBannerView;
            this.screenW = screenW;
            this.screenH = screenH;
            this.loop = loop;
            this.displayImageOptions = (new DisplayImageOptions.Builder()).showStubImage(defaultImg).showImageForEmptyUri(defaultImg).showImageOnFail(defaultImg).cacheInMemory(true).cacheOnDisk(true).build();
            this.getView();
        }
    }

    private void getView() {
        if(this.list.size() > 1 && this.loop) {
            this.listView.add(this.getView(this.list.size() - 1));
        }

        for(int i = 0; i < this.list.size(); ++i) {
            this.listView.add(this.getView(i));
        }

        if(this.list.size() > 1 && this.loop) {
            this.listView.add(this.getView(0));
        }

    }

    private View getView(final int i) {
        if(this.baseBannerView != null) {
            return this.baseBannerView.getView(i);
        } else {
            final ImageView imageView = new ImageView(this.context);
            String uri = this.getItem(i).getBannerBeanImage();
            if(uri != null) {
                if(uri.indexOf("://") != uri.lastIndexOf("://")) {
                    Log.e("图片URL错误" + uri);
                    uri = null;
                }

                if(uri.indexOf("_") != -1) {
                    String idx = uri.substring(uri.indexOf("://") + 3);
                    idx = idx.substring(0, idx.indexOf("/"));
                    if(idx.indexOf("_") != -1) {
                        Log.e("图片URL错误有下划线" + uri);
                        uri = null;
                    }
                }
            }

            if(TextUtils.isEmpty(uri)) {
                if(MUtils.getMUtils().getDefaultBitmapMap().get(Integer.valueOf(this.defaultImg)) == null) {
                    MUtils.getMUtils().getDefaultBitmapMap().put(Integer.valueOf(this.defaultImg), BitmapFactory.decodeResource(this.context.getResources(), this.defaultImg));
                }

                imageView.setImageBitmap((Bitmap)MUtils.getMUtils().getDefaultBitmapMap().get(Integer.valueOf(this.defaultImg)));
                this.onMPagerCallBack.onMPagerCallBack((Bitmap)MUtils.getMUtils().getDefaultBitmapMap().get(Integer.valueOf(this.defaultImg)));
            } else {
                ImageLoader.getInstance().displayImage(this.getItem(i).getBannerBeanImage(), imageView, this.displayImageOptions, new ImageLoadingListener() {
                    public void onLoadingStarted(String imageUri, View view) {
                    }

                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    }

                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        imageView.setImageBitmap(loadedImage);
                        MPagerAdapter.this.onMPagerCallBack.onMPagerCallBack(loadedImage);
                    }

                    public void onLoadingCancelled(String imageUri, View view) {
                    }
                });
            }

            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(MPagerAdapter.this.onItemClickListener != null) {
                        MPagerAdapter.this.onItemClickListener.onItemClick((AdapterView)null, v, i, (long)i);
                    }

                }
            });
            return imageView;
        }
    }

    public List<?> getBanners() {
        return this.list;
    }

    public T getItem(int position) {
        return this.list.get(position);
    }

    public int getCount() {
        return this.listView.size();
    }

    public boolean isViewFromObject(View v, Object obj) {
        return v == obj;
    }

    public Object instantiateItem(View convertView, int position) {
        ((ViewPager)convertView).removeView((View)this.listView.get(position % this.listView.size()));
        ((ViewPager)convertView).addView((View)this.listView.get(position % this.listView.size()));
        return this.listView.get(position % this.listView.size());
    }

    public void destroyItem(View container, int position, Object object) {
    }

    public interface OnMPagerCallBack {
        void onMPagerCallBack(Bitmap var1);
    }
}
