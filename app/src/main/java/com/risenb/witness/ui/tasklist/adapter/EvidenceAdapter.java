package com.risenb.witness.ui.tasklist.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.risenb.witness.R;
import com.risenb.witness.beans.MutilPartInfo;
import com.risenb.witness.beans.TaskInfoDetails;
import com.risenb.witness.utils.MyConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EvidenceAdapter extends BaseAdapter {
    private Activity mContext;
    private List<TaskInfoDetails.TaskListBean> mlist;
    private LayoutInflater mInflater;
    private Map<Integer, MutilPartInfo> list = new HashMap<>();
    private String mImageFileName;
    private String mImageFilePath;

    public EvidenceAdapter(Activity mContext, List<TaskInfoDetails.TaskListBean> list) {
        this.mContext = mContext;
        this.mlist = list;
        mInflater = LayoutInflater.from(mContext);
        // ((EvidenceFirst) mContext).setListener(this);
    }

    @Override
    public int getCount() {
        return mlist.size() > 0 ? mlist.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.evidenceoneimage, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        TaskInfoDetails.TaskListBean taskexampleinfo = mlist.get(position);
        String type = taskexampleinfo.getIsType();
        Log.e("type", type);
        if ("0".equals(type)) {
            Log.e("type", type);
            holder.mCameraPhoto.setVisibility(View.GONE);
        } else {
            holder.mRecordVideo.setVisibility(View.GONE);
        }
        // ImageLoader.getInstance().displayImage(taskexampleinfo.getExampleFile(),holder.mExamplePhoto, MyConfig.options);
        //holder.mExamplePhoto.setImageBitmap();
        //相机拍照
        holder.mCameraPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TaskObtainEvidenceTakePhoto(1);
                //  ((EvidenceFirst) mContext).TaskObtainEvidenceTakePhoto(EvidenceFirst.SYSTEM_TAKE_PHOTH_ONE, position);
                Log.e("camera", "-------" + position);

            }
        });

        //相机录像
        holder.mRecordVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  ((EvidenceFirst) mContext).CameraVideoRcord(position);
            }
        });


        if (null != list) {
            holder.mShowImage.setVisibility(View.VISIBLE);
            //Log.e("partInfo.getPosition()","-------"+partInfo.getPosition());
            // Log.e("position","-------"+position);
            MutilPartInfo info = list.get(position);
            if ((null != info && info.getPosition() == position)) {
                //
                //holder.mShowImage.setImageBitmap(dBitmap);
                if ("0".equals(type)) {
                    //  Bitmap bitmap=  getVideoThumb2(info.getVideourl());
                    Log.e("info.getVideourl()", info.getThumbnail());
                    Bitmap dBitmap = BitmapFactory.decodeFile(info.getThumbnail());
                    holder.mShowImage.setImageBitmap(dBitmap);
                    // ImageLoader.getInstance().displayImage("file://"+info.getVideourl(), holder.mShowImage, MyConfig.options);
                } else {
                    ImageLoader.getInstance().displayImage("file://" + info.getUrl(), holder.mShowImage, MyConfig.options);
                }

            } else {
                holder.mShowImage.setImageBitmap(null);
                //holder.mShowImage.setVisibility(View.GONE);
            }
        }

        return view;
    }

    public static class ViewHolder {
        private Bitmap mbitmap;
        //拍摄取证文本
        @BindView(R.id.taskevidence_image_step)
        TextView mTextContent;
        //拍摄需求
        @BindView(R.id.taskevidence_image_require)
        TextView Require;
        //例图
        @BindView(R.id.taskevidence_onlyexample)
        ImageView mExamplePhoto;
        //展示图片
        @BindView(R.id.taskevidence_show_onlyimage)
        ImageView mShowImage;
        //删除图片
        @BindView(R.id.taskevidence_delete_onlyimage)
        Button mDeleteImage;
        //照相机
        @BindView(R.id.taskevidence_image_camera)
        Button mCameraPhoto;
        @BindView(R.id.taskevidence_video_camera)
        Button mRecordVideo;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}