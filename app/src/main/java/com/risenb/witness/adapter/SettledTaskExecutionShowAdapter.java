package com.risenb.witness.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.beans.ImageItem;
import com.risenb.witness.beans.VideoInfo;
import com.risenb.witness.beans.VideoOrPhotoItemSign;
import com.risenb.witness.ui.tasklist.GalleryActivity;
import com.risenb.witness.ui.tasklist.PlayVideo.PlayVideoActivity;
import com.risenb.witness.ui.tasklist.PlayVideo.VideoPlayerActivity;
import com.risenb.witness.utils.FileUtils;
import com.risenb.witness.utils.SDCardUtils;

import java.io.File;
import java.util.ArrayList;

import static com.risenb.witness.ui.home.SettledTaskExecutionUI.SYSTEM_PLAY_VIDEO_CAMERA;
import static com.risenb.witness.ui.home.SettledTaskExecutionUI.SYSTEM_TAKE_PHOTO_CAMERA;

public class SettledTaskExecutionShowAdapter extends BaseAdapter {
    private ArrayList<VideoOrPhotoItemSign> tempSelectBitmap;
    private Activity activity;
    private LayoutInflater inflater;
    private boolean shape;
    private int maxNumber = 30;
    private String fixedID;
    public String photoFilePath;
    public String photoFileName;

    public boolean isShape() {
        return shape;
    }

    public void setShape(boolean shape) {
        this.shape = shape;
    }

    public SettledTaskExecutionShowAdapter(Activity activity, ArrayList<VideoOrPhotoItemSign> tempSelectBitmap, String fixedID) {
        this.activity = activity;
        this.tempSelectBitmap = tempSelectBitmap;
        this.fixedID = fixedID;
        inflater = LayoutInflater.from(activity);
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
            holder.linearLayout = convertView.findViewById(R.id.item_grida_image_ll);
            holder.addPhotoView = convertView.findViewById(R.id.item_grida_left_image);
            holder.addVideoView = convertView.findViewById(R.id.item_grida_right_image);
            holder.videoClickView = convertView.findViewById(R.id.item_grid_video_click);
            holder.deleteImageView = convertView.findViewById(R.id.delete_imageview);
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
            holder.image.setVisibility(View.INVISIBLE);
            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.videoClickView.setVisibility(View.GONE);
            holder.deleteImageView.setVisibility(View.GONE);

            holder.addPhotoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TaskObtainEvidenceTakePhoto();
                }
            });

            holder.addVideoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int videoDataNumber = 0;
                    for (VideoOrPhotoItemSign videoOrPhotoItemSign : tempSelectBitmap) {
                        if ("video".equals(videoOrPhotoItemSign.sign)) {
                            videoDataNumber++;
                        }
                    }
                    if (videoDataNumber >= 3) {
                        Toast.makeText(activity.getApplicationContext(), "视频拍摄个数最多为3个", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent(activity, PlayVideoActivity.class);
                    intent.putExtra("taskid", fixedID);
                    intent.putExtra("videoSavePath", "system/files/witness/");
                    activity.startActivityForResult(intent, SYSTEM_PLAY_VIDEO_CAMERA);
                }
            });

            if (position >= maxNumber) {
                holder.image.setVisibility(View.GONE);
            }
        } else {
            holder.linearLayout.setVisibility(View.GONE);
            holder.image.setVisibility(View.VISIBLE);
            holder.deleteImageView.setVisibility(View.VISIBLE);
            if ("photo".equals(tempSelectBitmap.get(position).sign)) {
                holder.videoClickView.setVisibility(View.GONE);
                final ImageItem imageItem = (ImageItem) tempSelectBitmap.get(position);
                holder.linearLayout.setVisibility(View.GONE);
                File file = new File(imageItem.getImagePath());
                Uri uri = Uri.fromFile(file);
                Glide.with(parent.getContext())
                        .load(uri)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(holder.image);

                /*Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
                holder.image.setImageBitmap(bitmap);*/

                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<ImageItem> photoInfoList = new ArrayList<>();
                        photoInfoList.add(imageItem);
                        Intent intent = new Intent(activity, GalleryActivity.class);
                        intent.putExtra("settledTaskSign", true);
                        intent.putParcelableArrayListExtra("tempSelectBitmap", photoInfoList);
                        intent.putExtra("position", "1");
                        intent.putExtra("ID", 0);
                        activity.startActivity(intent);
                    }
                });

                /*
                 * 删除照片
                 */
                holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            deletePromptDialog(position, imageItem);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(activity.getApplicationContext(), "文件删除错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else if ("video".equals(tempSelectBitmap.get(position).sign)) {
                holder.videoClickView.setVisibility(View.VISIBLE);
                final VideoInfo videoInfo = (VideoInfo) tempSelectBitmap.get(position);
                if (videoInfo.getPath() != null) {
                    File file = new File(videoInfo.getPath());
                    Uri uri = Uri.fromFile(file);
                    Glide.with(activity.getApplication())
                            .load(uri)
                            /*.skipMemoryCache(true)*/
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(holder.image);
                }

                holder.videoClickView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<VideoInfo> videoInfoList = new ArrayList<>();
                        videoInfoList.add(videoInfo);
                        Intent intent = new Intent(activity, VideoPlayerActivity.class);
                        // 传递整个列表给VideoPlayerActivity
                        intent.putExtra("videoInfoList", videoInfoList);
                        // 传递当前点击的位置
                        intent.putExtra("position", 0);
                        activity.startActivity(intent);
                    }
                });

                /*
                 * 删除视频
                 */
                holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            deletePromptDialog(position, videoInfo);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(activity.getApplicationContext(), "文件删除错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        return convertView;
    }

    private void deletePromptDialog(final int position, final VideoOrPhotoItemSign videoOrPhotoItemSign) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity, R.style.ImageloadingDialogStyle);
        dialog.setTitle("人人提示");
        dialog.setMessage("确定删除数据?");
        dialog.setIcon(R.drawable.ic_launcher);
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tempSelectBitmap.remove(position);
                if ("video".equals(videoOrPhotoItemSign.sign)) {
                    VideoInfo videoInfo = (VideoInfo) videoOrPhotoItemSign;
                    new File(videoInfo.getPath()).delete();
                } else if ("photo".equals(videoOrPhotoItemSign.sign)) {
                    ImageItem imageItem = (ImageItem) videoOrPhotoItemSign;
                    new File(imageItem.getImagePath()).delete();
                }
                notifyDataSetChanged();
            }
        });

        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //设置对话框消失监听事件
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    class ViewHolder {
        private ImageView image;
        private ImageView deleteImageView;
        private LinearLayout linearLayout;
        private ImageView addPhotoView;
        private ImageView addVideoView;
        private ImageView videoClickView;
    }

    public void setPhotoNumbers(int maxNum) {
        maxNumber = maxNum;
    }

    /*
     * 照片拍摄
     */
    public void TaskObtainEvidenceTakePhoto() {
        if (getImageSavePath()) {
            photoFileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (Build.VERSION.SDK_INT >= 24) {
                File imageDir = new File(Environment.getExternalStorageDirectory(), "system/files/witness/");
                if (!imageDir.exists()) {
                    imageDir.mkdirs();
                }
                File imageFilePath = new File(imageDir + "/" + fixedID + "/", photoFileName);
                Uri uriForFile = FileProvider.getUriForFile(activity.getApplication(), "com.risenb.witness.fileprovider", imageFilePath);
                if (uriForFile != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
                    // 授予目录临时共享权限
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
            } else {
                File file = new File(photoFilePath + photoFileName);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            }

            activity.startActivityForResult(intent, SYSTEM_TAKE_PHOTO_CAMERA);
            Toast.makeText(activity.getApplication(), "请横屏拍摄", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean getImageSavePath() {
        photoFilePath = SDCardUtils.getSDCardPath() + MyApplication.settledTaskPhotosSaveUrl + fixedID + "/";
        return FileUtils.createOrExistsDir(photoFilePath);
    }

}
