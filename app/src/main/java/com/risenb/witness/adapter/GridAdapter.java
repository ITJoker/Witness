package com.risenb.witness.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.risenb.witness.R;
import com.risenb.witness.beans.ImageItem;

import java.io.File;
import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {
    private ArrayList<ImageItem> tempSelectBitmap;
    private Context context;
    private LayoutInflater inflater;
    private boolean shape;
    private int maxNumber = 30;

    public boolean isShape() {
        return shape;
    }

    public void setShape(boolean shape) {
        this.shape = shape;
    }

    public GridAdapter(Context context, ArrayList<ImageItem> tempSelectBitmap) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.tempSelectBitmap = tempSelectBitmap;
    }

    public int getCount() {
        if (tempSelectBitmap.size() >= maxNumber) {
            return maxNumber;
        }
        return (tempSelectBitmap.size() + 1);
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gridview_item, parent, false);
            holder = new ViewHolder();
            holder.image = convertView.findViewById(R.id.item_grida_image);
            // holder.delete_imageView= (ImageView) convertView.findViewById(R.id.delete_imageview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int size = tempSelectBitmap.size();
        /*
         * position:0 ~ tempSelectBitmap.size() -1;
         * size:1 ~ tempSelectBitmap.size()
         */
        if (position == size) {
            holder.image.setScaleType(ScaleType.FIT_XY);
            Glide.with(context)
                    .load(R.drawable.icon_taskdetails_add_picture)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.image);
            /*holder.image.setImageBitmap(BitmapFactory.decodeResource(convertView.getResources(), R.drawable.icon_addpic_unfocused));*/
            if (position >= maxNumber) {
                holder.image.setVisibility(View.GONE);
            }
        } else {
            holder.image.setScaleType(ScaleType.FIT_XY);
            Uri uri;
            File file = new File(tempSelectBitmap.get(position).getImagePath());
            uri = Uri.fromFile(file);
            try {
                Glide.with(context)
                        .load(uri)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(holder.image);
                /*Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
                holder.image.setImageBitmap(bitmap);*/
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return convertView;
    }

    public class ViewHolder {
        public ImageView image;
        public ImageView delete_imageView;
    }

    public void setPhotoNumbers(int maxNum) {
        maxNumber = maxNum;
    }
}
