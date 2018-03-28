package com.risenb.witness.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.risenb.witness.R;
import com.risenb.witness.beans.VideoInfo;
import com.risenb.witness.ui.tasklist.PlayVideo.PlayVideoActivity;
import com.risenb.witness.ui.tasklist.PlayVideo.VideoPlayerActivity;

import java.io.File;
import java.util.ArrayList;

public class GridVideoAdapter extends BaseAdapter {
    private ArrayList<VideoInfo> videoInfoList;
    private final String taskID;
    private final String marks;

    public GridVideoAdapter(ArrayList<VideoInfo> videoInfoList, String taskID, String marks) {
        this.videoInfoList = videoInfoList;
        this.taskID = taskID;
        this.marks = marks;
    }

    @Override
    public int getCount() {
        if (videoInfoList.size() >= 3) {
            return 3;
        }
        return (videoInfoList.size() + 1);
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        GridView gridView = (GridView) parent;
        final Context context = parent.getContext();
        final VideoViewHolder videoViewHolder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.gridview_item_video, null);
            videoViewHolder = new VideoViewHolder();
            videoViewHolder.relativeLayout = convertView.findViewById(R.id.item_grid_video_layout);
            videoViewHolder.videoView = convertView.findViewById(R.id.item_grid_video);
            videoViewHolder.delete_imageView = convertView.findViewById(R.id.delete_imageview);
            videoViewHolder.playVideoView = convertView.findViewById(R.id.item_grid_click);
            convertView.setTag(videoViewHolder);
        } else {
            videoViewHolder = (VideoViewHolder) convertView.getTag();
        }
        if (position == videoInfoList.size()) {
            videoViewHolder.playVideoView.setVisibility(View.GONE);
            videoViewHolder.delete_imageView.setVisibility(View.GONE);
            videoViewHolder.videoView.setImageResource(R.drawable.image_shooting);
            if (position == 3) {
                videoViewHolder.videoView.setVisibility(View.GONE);
            }
        } else {
            videoViewHolder.videoView.setVisibility(View.VISIBLE);
            videoViewHolder.delete_imageView.setVisibility(View.VISIBLE);
            // videoViewHolder.videoView.setVideoPath(videoInfoList.get(position).getPath());
            if (videoInfoList.get(position).getPath() != null) {
                File file = new File(videoInfoList.get(position).getPath());
                Uri uri = Uri.fromFile(file);
                Glide.with(context)
                        .load(uri)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(videoViewHolder.videoView);
            }
        }
        videoViewHolder.delete_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File file = new File(videoInfoList.get(position).getPath());
                    file.delete();
                    videoInfoList.remove(position);
                    Glide.get(context).clearMemory();
                    notifyDataSetChanged();
                } catch (Exception e) {
                    Toast.makeText(context, "删除文件错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        videoViewHolder.playVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    /*Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(videoInfoList.get(position).getPath()), "video/mp4");
                    context.startActivity(intent);*/
                    Intent intent = new Intent(context, VideoPlayerActivity.class);
                    // 传递整个列表给VideoPlayerActivity
                    intent.putExtra("videoInfoList", videoInfoList);
                    // 传递当前点击的位置
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(context, "请在拍摄视频后预览", Toast.LENGTH_SHORT).show();
                }
            }
        });
        videoViewHolder.videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoInfoList.size() == position) {
                    Intent intent = new Intent(context, PlayVideoActivity.class);
                    intent.putExtra("taskid", taskID);
                    intent.putExtra("marks", marks);
                    context.startActivity(intent);
                }
            }
        });
        return convertView;
    }

    private class VideoViewHolder {
        RelativeLayout relativeLayout;
        ImageView videoView;
        ImageView playVideoView;
        ImageView delete_imageView;
    }
}

