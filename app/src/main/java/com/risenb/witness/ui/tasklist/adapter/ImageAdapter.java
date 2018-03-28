package com.risenb.witness.ui.tasklist.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.utils.MyConfig;
import com.risenb.witness.utils.SDCardUtils;
import com.risenb.witness.utils.SharedPreferencesUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private String isType;
    private List<String> exampleFile;
    private String string;
    public String rejectSign = "";
    public HistoryAdapter historyAdapter;
    public String imageAdapterTaskID = "";
    private Context context;

    public ImageAdapter(List<String> exampleFile, String string, String isType) {
        this.exampleFile = exampleFile;
        this.string = string;
        this.isType = isType;
    }

    @Override
    public int getCount() {
        if ("Reject".equals(rejectSign)) {
            if (exampleFile != null) {
                if (exampleFile.size() >= 30) {
                    return 30;
                }
            } else {
                return 0;
            }
        }
        return exampleFile.size();
    }

    @Override
    public Object getItem(int position) {
        return exampleFile.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        context = parent.getContext();
        ViewHolder viewHolder;
        if ("exampleImage".equals(string)) {
            // 示例图
            /*if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.history_task_adapter_item_adapter1_item, null);
                viewHolder = new ViewHolder();
                viewHolder.imageView = convertView.findViewById(R.id.history_task_exampleimage);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            uploadedFilesShow(position, viewHolder);*/
        } else if ("cameraImage".equals(string)) {
            // 拍摄文件展示
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.history_task_adapter_item_adapter2_item, null);
                viewHolder = new ViewHolder();
                viewHolder.imageView = convertView.findViewById(R.id.history_task_cameraimage);
                viewHolder.playerVideoView = convertView.findViewById(R.id.history_player_video);
                viewHolder.deleteImage = convertView.findViewById(R.id.history_task_deleteimage);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if ("0".equals(isType)) {
                // 展示视频
                viewHolder.playerVideoView.setVisibility(View.VISIBLE);
                viewHolder.imageView.setVisibility(View.GONE);
            } else if ("1".equals(isType)) {
                // 显示图片
                viewHolder.playerVideoView.setVisibility(View.GONE);
                viewHolder.imageView.setVisibility(View.VISIBLE);

                if ("Reject".equals(rejectSign)) {
                    // 被驳回任务展示
                    if (position != exampleFile.size()) {
                        /*
                         * 索引未越界
                         */
                        uploadedFilesShow(position, viewHolder);
                    }
                } else {
                    // 已完成任务展示
                    uploadedFilesShow(position, viewHolder);
                }
            } else if (TextUtils.isEmpty(isType)) {
                // 固定任务上传数据详情展示
                uploadedFilesShow(position, viewHolder);
            }
        }
        return convertView;
    }

    private void uploadedFilesShow(int position, ViewHolder viewHolder) {
        if (!exampleFile.get(position).contains("adexmall")) {
            if (exampleFile.get(position).contains("mp4")) {
                viewHolder.imageView.setImageResource(R.drawable.video_preview_not_supported);
                /*Glide.with(context)
                        .load("http://www.adexmall.net/" + exampleFile.get(position))
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.default_image)
                        .error(R.drawable.video_preview_not_supported)
                        .into(viewHolder.imageView);*/
            } else {
                ImageLoader.getInstance().displayImage("http://www.adexmall.net/" + exampleFile.get(position), viewHolder.imageView, MyConfig.options);
                // ImageLoader.getInstance().displayImage("http://www.adexmall.net/" + exampleFile.get(position), viewHolder.imageView);
                /*Glide.with(context)
                        .load("http://www.adexmall.net/" + exampleFile.get(position))
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.vipinfousericon)
                        .error(R.drawable.video_preview_not_supported)
                        .into(viewHolder.imageView);*/
            }
        } else {
            if (exampleFile.get(position).contains("mp4")) {
                viewHolder.imageView.setImageResource(R.drawable.video_preview_not_supported);
            } else {
                ImageLoader.getInstance().displayImage(exampleFile.get(position), viewHolder.imageView, MyConfig.options);
                // ImageLoader.getInstance().displayImage(exampleFile.get(position), viewHolder.imageView);
                /*Glide.with(context)
                        .load(exampleFile.get(position))
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.vipinfousericon)
                        .error(R.drawable.video_preview_not_supported)
                        .into(viewHolder.imageView);*/
            }
        }
    }

    private void saveBitmapToFiles(ViewGroup parent) {
        if (!TextUtils.isEmpty(imageAdapterTaskID)) {
            SharedPreferencesUtil.saveBoolean(parent.getContext(), imageAdapterTaskID + "Reject", true);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < exampleFile.size(); i++) {
                    try {
                        String imageFilePath = SDCardUtils.getSDCardPath() + MyApplication.photosCompressSaveUrl + imageAdapterTaskID + "/";
                        File file = new File(imageFilePath, i + ".jpg");
                        if (file.exists()) {
                            // 文件存在
                            MyApplication.deletePicture(SDCardUtils.getSDCardPath() + MyApplication.photosCompressSaveUrl + imageAdapterTaskID);
                        } else {
                            if (!file.getParentFile().exists()) {
                                // 文件不存在则新建
                                file.getParentFile().mkdir();
                            }
                            file.createNewFile();
                        }
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        URL url = new URL("http://www.adexmall.net/" + exampleFile.get(i));
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setConnectTimeout(5000);
                        if (conn.getResponseCode() == 200) {
                            //获取服务器返回回来的流
                            InputStream inputStream = conn.getInputStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            fileOutputStream.write(byteArrayOutputStream.toByteArray());
                            fileOutputStream.flush();
                            fileOutputStream.close();
                            bitmap.recycle();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    class ViewHolder {
        ImageView imageView;
        ImageView playerVideoView;
        ImageView deleteImage;
    }
}
