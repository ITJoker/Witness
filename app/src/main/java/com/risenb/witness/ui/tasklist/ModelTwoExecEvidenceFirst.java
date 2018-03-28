package com.risenb.witness.ui.tasklist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.beans.BaseBeans;
import com.risenb.witness.beans.LastTaskBean;
import com.risenb.witness.beans.MutilPartInfo;
import com.risenb.witness.beans.UploadFileBean;
import com.risenb.witness.beans.UploadImageBean;
import com.risenb.witness.beans.VideoInfo;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.GsonResponseHandler;
import com.risenb.witness.network.OkHttpUtils.response.RawResponseHandler;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.ui.tasklist.DataBase.DaoHelper;
import com.risenb.witness.ui.tasklist.adapter.ModelTwoListViewCommconAdapter;
import com.risenb.witness.ui.tasklist.mediarecorder.VideoRecorderActivity;
import com.risenb.witness.ui.home.HomeUI;
import com.risenb.witness.utils.FileUtils;
import com.risenb.witness.utils.SDCardUtils;
import com.risenb.witness.utils.SharedPreferencesUtil;
import com.risenb.witness.utils.StringUtils;
import com.risenb.witness.utils.TimesUtils;
import com.risenb.witness.utils.newUtils.UIManager;
import com.risenb.witness.utils.newUtils.Utils;
import com.risenb.witness.views.citypicker.CountDownTimerView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModelTwoExecEvidenceFirst extends BaseUI {

    public static final int SYSTEM_TAKE_PHOTH_ONE = 1011;//拍照
    public static final int SYSTEM_IMAGE_CROP_ONE = 1001;
    public static final int SYSTEM_IMAGE_CROP_THREE = 1003;
    public static final int VIDEO_CAPTURE_ONE = 2001;//录像

    private int position = -1; //父List item的位置 拍照或录像

    private String mVideoSort;
    private String mImageSort;
    private String mImageFileName;
    private String mImageFilePath;
    private Uri cropUri = null;

    private int uploadfinish = 0;//图片上传 的张数
    private int uploadvideofinish = 0;//视频上传的个数
    private int allLoadfinish = 0;//图片和视频全部上传完毕

    @BindView(R.id.listview)
    ListView mListView;

    @BindView(R.id.task_next_step)
    //地址
            TextView mTaskEvidence_Address;
    //价格
    TextView mTaskEvidence_Price;
    //计时器
    CountDownTimerView mTaskEvidence_TimeCount;
    //计时器进度条
    ProgressBar mTaskEvidence_TimePb;

    //标示
    private String marks;
    //任务ID
    private String taskid;
    //模版类型
    private String modeltype;

    public InterImage listener;
    private Map<Integer, MutilPartInfo> map = new HashMap<>();
    private List<String> filepath = new ArrayList<>(); //图片本地路径
    private List<String> returnfile = new ArrayList<>();//服务器返回图片路径
    private List<String> mListVideoPath = new ArrayList<>();//视频文件本地路径
    private List<String> mListreturnVideoPath = new ArrayList<>();//服务器返回视频路径
    private Map<String, String> hashmap = new HashMap<>(); //key缩略图路劲 和 Value视频路劲
    private Map<String, String> videopathpos = new HashMap<>(); //视频位置

    private List<Boolean> flaglist = new ArrayList<>();

    private List<CheckBoxInfo> checkBoxInfos = new ArrayList<>();
    private List<com.risenb.witness.ui.tasklist.MutilPartInfo> list;
    private ModelTwoListViewCommconAdapter mAdapter;

    private boolean isModify = false;//是否修改过
    private int page;
    private String totalpage;
    private boolean uploadmodify = false;
    private LastTaskBean info;
    private int countvideo = 0;
    private DaoHelper mDaoHelper;


    boolean flags = false;
    boolean flagss = false;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {

    }

    @Override
    protected void prepareData() {

    }

    @Override
    public void onLoadOver() {

    }

    @Override
    protected void onInitCreate(Bundle savedInstanceState) {
        setContentView(R.layout.evidencefirst);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        marks = bundle.getString("marks");
        taskid = bundle.getString("taskid");
        modeltype = bundle.getString("modeltype");
        page = bundle.getInt("page");//当前第几页
        totalpage = bundle.getString("totalpage");//总页数
        if (String.valueOf(page).equals(totalpage)) {
            //当前页 等于 总页数
        }
        View headview = LayoutInflater.from(this).inflate(R.layout.headviewevidence, null);
        mTaskEvidence_Address = (TextView) headview.findViewById(R.id.taskevidence_name_address);
        mTaskEvidence_Price = (TextView) headview.findViewById(R.id.taskevidence_price);
        mTaskEvidence_TimeCount = (CountDownTimerView) headview.findViewById(R.id.taskevidence_timecount);
        mTaskEvidence_TimePb = (ProgressBar) headview.findViewById(R.id.taskevidence_time_pb);
        mListView.addHeaderView(headview);
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.UploadedDetails));
        //String url="http://yingwei.4pole.cn/Task/myTaskDetails/?taskId=4&c=3c2d4defd9c22732fe960c48f6ac5436&modeltype=1&page=1";
        Map<String, String> params = new HashMap<>();
        params.put("taskId", taskid);
        params.put("c", MyApplication.getInstance().getC());
        params.put("modeltype", modeltype);
        params.put("page", String.valueOf(page));
        //params.put("")
        Log.e("ModelTwoEvidenceFirstC", MyApplication.getInstance().getC());
        onNonTaskAccessNetWorkDetail(url, params);
        mDaoHelper = new DaoHelper(this);
    }

    public void onNonTaskAccessNetWorkDetail(String url, Map<String, String> params) {
        if (StringUtils.isSpace(url)) {
            Toast.makeText(this, "网络路径无效", Toast.LENGTH_LONG).show();
            return;
        }
        Utils.getUtils().showProgressDialog(this);
        Log.e("taskid", taskid + "");
        MyOkHttp.get().post(this, url, params, new GsonResponseHandler<BaseBeans<LastTaskBean>>() {
            @Override
            public void onSuccess(int statusCode, BaseBeans<LastTaskBean> response) {
                info = response.getData();
                Log.e("TaskInfoDetails", info.toString());
                Utils.getUtils().dismissDialog();
                if (null != info) {
                    mTaskEvidence_Address.setText(info.getAddress());
                    mTaskEvidence_Price.setText(info.getPrice());
                    daojishi(String.valueOf(info.getRemainTime() * 1000));
                    //图片和视频 数据
                    if (null != info.getTaskList() && !info.getTaskList().isEmpty()) {
                        mAdapter = new ModelTwoListViewCommconAdapter(ModelTwoExecEvidenceFirst.this, info.getTaskList(), 2, info.getTaskList().get(0).getExampleFile(), info.getTaskList().get(0).getIsType());
                        mListView.setAdapter(mAdapter);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }

    private void daojishi(String tiem) {
        String time = TimesUtils.timeDifference(tiem);
        String[] split = time.split(",");
        int dd = Integer.valueOf(split[0]);
        int hh = Integer.valueOf(split[1]);
        int mm = Integer.valueOf(split[2]);
        int ss = Integer.valueOf(split[3]);
        if (dd >= 0 && hh >= 0 && mm >= 0 && ss >= 0) {
            mTaskEvidence_TimeCount.setTime(dd, hh, mm, ss);
        } else {
            mTaskEvidence_TimeCount.setTime(0, 0, 0, 0);
        }
        // 开始倒计时
        mTaskEvidence_TimeCount.start();
    }

    @OnClick(R.id.task_next_step)
    public void NextStep() {
//       for (int i=0;i<list.size();i++){
// //         String str=  list.get(i).getTitle();
// //           Log.e("str",str+i);
// //         for (int j=0;j<list.get(i).getCheckBoxInfo().size();j++){
// //             CheckBoxInfo  cckin=   list.get(i).getCheckBoxInfo().get(j);
// //             if(cckin.isSelected()){
// //                 Log.e("被选中的",i+"--"+j+"--"+cckin.isSelected());
// //             }
// //         }
// //       }
//
        //上传文件
        UploadFile();

//        Intent intent = new Intent(ModelTwoEvidenceFirst.this, ModelTwoLastTaskSaveAndUpLoad.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("taskid", taskid);
//        bundle.putString("marks", marks);
//        bundle.putString("modeltype", modeltype);
//        intent.putExtras(bundle);
//        startActivity(intent);
    }

    int ssss = 0;

    public void UploadFile() {

        if (null != map && map.size() > 0) {
            for (Map.Entry<Integer, MutilPartInfo> entry : map.entrySet()) {
                int pos = entry.getKey();
                MutilPartInfo info = entry.getValue();
                Log.e("size", map.size() + "");
                Log.e("Keyvalue", "key= " + entry.getKey() + " and value= " + entry.getValue());
                Log.e("pos", pos + "---" + info.getUrl());
                String type = this.info.getTaskList().get(pos).getIsType();
                if (type.equals("0")) {
                    countvideo++;
                    Log.e("countvideo", countvideo + "");
                    if (countvideo == map.size()) {
                        Intent intent = new Intent(this, ModelTwoExecLastTaskSaveAndUpLoad.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("modeltype", modeltype);
                        bundle.putString("taskid", taskid);
                        bundle.putInt("page", ++page);
                        intent.putExtras(bundle);
                        --page;
                        countvideo = 0;
                        flaglist.clear();
                        startActivity(intent);
                        Log.e("lookthis", "kankan");
                    } else {
                        continue;
                    }
                }
                String imageurl = info.getUrl();
                if (null != imageurl) {
                    boolean flagimage = info.isImagemodify();//判断图片有没有上传过
                    if (flagimage) {
                        flagss = true;
                        String imagesort = info.getImagesort();
                        Log.e("UploadFilevideosort", imagesort);
                        filepath.add(info.getUrl());
                        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.pictureUpload));
                        Map<String, File> files = new HashMap<>();
                        File file = new File(info.getUrl());
                        files.put("file", file);
                        Map<String, String> params = new HashMap<>();
                        params.put("taskid", taskid);
                        params.put("sort", imagesort);
                        UploadImage(url, params, files);
                        Log.e(" Image modify ", "图片被修改过");
                    } else {
                        ssss++;
                        if (ssss == map.size() - countvideo) {
                            Intent intent = new Intent(ModelTwoExecEvidenceFirst.this, ModelTwoExecLastTaskSaveAndUpLoad.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("taskid", taskid);
                            bundle.putString("marks", marks);
                            bundle.putString("modeltype", modeltype);
                            bundle.putInt("page", ++page);
                            intent.putExtras(bundle);
                            flaglist.clear();
                            ssss = 0;
                            startActivity(intent);
                            --page;
                            Log.e(" Not modify ", "图片全部都没有被修改过");
                        }
                    }

                } else {
                    //flags = true;
                }


//                if (null == videourl) {
//                    boolean flagimage = info.isImagemodify();//判断图片有没有上传过
//                    if (flagimage) {
//                        flaglist.add(true);
//                        String imagesort = info.getImagesort();
//                        Log.e("UploadFilevideosort", imagesort);
//                        filepath.add(info.getUrl());
//                        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.pictureUpload));
//                        Map<String, File> files = new HashMap<>();
//                        File file = new File(info.getUrl());
//                        files.put("file", file);
//                        Map<String, String> params = new HashMap<>();
//                        params.put("taskid", taskid);
//                        params.put("sort", imagesort);
//                        UploadImage(url, params, files);
//                    }
////                videopathpos.put(pos,info.getVideourl());
////
//                } else {
//                    //videopathpos.put(pos,info.getVideourl());
//                    //  String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.pictureUpload));
//                    boolean flagvideo = info.isVideomodify();//视频是否上传过
//                    if (flagvideo) {
//                        flaglist.add(true);
//                        String videosort = info.getVideosort();
//                        Log.e("UploadFileimagesort", videosort);
//                        Map<String, File> files = new HashMap<>();
//                        File file = new File(info.getVideourl());
//                        files.put("video", file);
//                        Map<String, String> params = new HashMap<>();
//                        params.put("taskid", taskid);
//                        params.put("sort", videosort);
//                        UploadVideo(params, files);
//                    }
//
//                }
            }
        } else {
            //没有 取证

            Intent intent = new Intent(ModelTwoExecEvidenceFirst.this, ModelTwoExecLastTaskSaveAndUpLoad.class);
            Bundle bundle = new Bundle();
            bundle.putString("taskid", taskid);
            bundle.putString("marks", marks);
            bundle.putString("modeltype", modeltype);
            bundle.putInt("page", ++page);
            intent.putExtras(bundle);
            startActivity(intent);
            flaglist.clear();
            --page;
            Log.e("empty", "Map 集合为空");
        }
        //makeText("请取证");
        countvideo = 0;
//        Log.e("nothing","路过看看");
//        if (flags) {
//
//            Intent intent = new Intent(ModelTwoExecEvidenceFirst.this, ModelTwoExecLastTaskSaveAndUpLoad.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("taskid", taskid);
//            bundle.putString("marks", marks);
//            bundle.putString("modeltype", modeltype);
//            bundle.putInt("page", ++page);
//            intent.putExtras(bundle);
//            flaglist.clear();
//            startActivity(intent);
//            Log.e("jump","map 集合 中有数据");
//            --page;
//        }


    }

    public synchronized void UploadImage(String url, Map<String, String> params, final Map<String, File> files) {

        Utils.getUtils().showProgressDialog(this);
        //makeText("上传图片中");
        MyOkHttp.get().upload(this, url, params, files, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                Log.e("response", response.toString());
                Gson gson = new Gson();
                UploadImageBean info = gson.fromJson(response, UploadImageBean.class);
                String uploadImagePath = info.getData().getFile_url();
                Log.e("uploadImagePath", uploadImagePath);
                String imagesort = info.getData().getSort();
                Log.e("imagesort", imagesort);
                returnfile.add(uploadImagePath);
                videopathpos.put(imagesort, uploadImagePath);
                uploadfinish++;
                allLoadfinish++;
                Utils.getUtils().dismissDialog();
                //makeText("上传图片中");
                if (allLoadfinish == flaglist.size()) {
                    UploadHttpUrlAndSort();
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {

            }
        });
    }


    public void UploadHttpUrlAndSort() {

        //  if (allLoadfinish == map.size()) {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.saveTaskfilepath));
        Map<String, String> params1 = new HashMap<>();
        params1.put("taskid", taskid);
        params1.put("c", MyApplication.getInstance().getC());
        List<UploadFileBean> json = new ArrayList<>();
        for (Map.Entry<String, String> entry1 : videopathpos.entrySet()) {
            UploadFileBean uploadfileBean = new UploadFileBean();
            String position = entry1.getKey();
            String videopath = entry1.getValue();
            Log.e("positionandpath", position + "---" + videopath);

            String prefix = videopath.substring(videopath.lastIndexOf(".") + 1);
            Log.e("prefix", prefix);
            if ("mp4".equals(prefix)) {
                uploadfileBean.setType("0");
                uploadfileBean.setSort(position);
                uploadfileBean.setReturnfile(videopath);
            } else {
                uploadfileBean.setType("1");
                uploadfileBean.setSort(position);
                uploadfileBean.setReturnfile(videopath);
            }

            uploadfileBean.setLatitude(SharedPreferencesUtil.getString(getApplication(), "HomeLatitude", ""));
            uploadfileBean.setLongtitude(SharedPreferencesUtil.getString(getApplication(), "HomeLongitude", ""));
            json.add(uploadfileBean);
        }
        BaseBeans<List<UploadFileBean>> baseBens = new BaseBeans<>();
        baseBens.setData(json);
        String jsontsr = JSON.toJSONString(baseBens);
        //String str="data:{"+jsontsr+"}";
        Log.e("jsontsr", jsontsr);
        params1.put("taskJson", jsontsr);
        SaveFilePathTaskAccessNetWork(url, params1);
        // }
    }

    private void SaveFilePathTaskAccessNetWork(String url, Map<String, String> params) {

        //makeText("正在上传");
        Utils.getUtils().showProgressDialog(this);
        MyOkHttp.get().post(this, url, params, new GsonResponseHandler<BaseBeans>() {
            @Override
            public void onFailure(int statusCode, String error_msg) {
                makeText("网络错误");
                Utils.getUtils().dismissDialog();
            }

            @Override
            public void onSuccess(int statusCode, BaseBeans response) {
                String success = response.getSuccess();
                if ("1".equals(success)) {
                    for (Map.Entry<Integer, MutilPartInfo> entry : map.entrySet()) {
                        MutilPartInfo info = entry.getValue();
                        info.setImagemodify(false);
                        info.setVideomodify(false);
                    }
                    uploadfinish = 0;
                    uploadvideofinish = 0;
                    allLoadfinish = 0;
                    Utils.getUtils().dismissDialog();

                    Intent intent = new Intent(ModelTwoExecEvidenceFirst.this, ModelTwoExecLastTaskSaveAndUpLoad.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("taskid", taskid);
                    bundle.putString("marks", marks);
                    bundle.putString("modeltype", modeltype);
                    bundle.putInt("page", ++page);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    uploadmodify = false;
                    --page;
                    flaglist.clear();
                    Log.e("norml", "正常跳转");
                    UIManager.getInstance().pushActivity(ModelTwoExecEvidenceFirst.this);
                }
            }
        });

    }

    //录像
    public void CameraVideoRcord(int position, String sort) {
        uploadmodify = true;
        this.position = position;
        this.mVideoSort = sort;
        Intent intent = new Intent(this, VideoRecorderActivity.class);
        startActivityForResult(intent, VIDEO_CAPTURE_ONE);
    }

    //拍照
    public void TaskObtainEvidenceTakePhoto(int flag, int position, String sort) {
        uploadmodify = true;
        this.position = position;
        this.mImageSort = sort;
        Log.e("TaskakePhoto", position + "");
        Log.e("LocalPosition", this.position + "");
        boolean mPathFlag = getImageSavePath();
        if (mPathFlag) {
            mImageFileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = new File(mImageFilePath + mImageFileName);
            Uri uri;
            if (Build.VERSION.SDK_INT >= 24) {
                uri = FileProvider.getUriForFile(getApplicationContext(), "com.risenb.witness.fileprovider", file);
            } else {
                uri = Uri.fromFile(file);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, flag);
        }
    }

    //删除照片
    //@OnClick(R.id.taskobtain_image_deletetwo)
    public void deleteShowCameraImage(String pos) {

        if (map.size() > 0) {
            if (map.containsKey(Integer.valueOf(pos))) {
                map.remove(pos);
                if (null != flaglist && flaglist.size() > 0) {
                    flaglist.remove(0);
                    for (int i = 0; i < flaglist.size(); i++) {
                        System.out.println("-------" + i);
                        Log.e("flaglist", flaglist.get(i) + "");
                    }
                }
            }
        }
    }

    public boolean getImageSavePath() {
        String mSDCardPath = SDCardUtils.getSDCardPath();
        mImageFilePath = mSDCardPath + MyApplication.photosSaveUrl;
        boolean exists = FileUtils.createOrExistsDir(mImageFilePath);
        Log.e("mSDCardPath", mSDCardPath);
        Log.e("mImageFilePath", mImageFilePath);
        Log.e("exists", exists + "");
        return exists;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.e("requestCode", requestCode + "");
        Log.e("resultCode", resultCode + "");
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SYSTEM_TAKE_PHOTH_ONE:
                    takephotoresult();
                    break;
                case SYSTEM_IMAGE_CROP_ONE:
                    //如果是图片直接传null
                    onCropImageResult(null);
                    break;
                case SYSTEM_IMAGE_CROP_THREE:
                    onCropImageResult(null);
                    break;
                case VIDEO_CAPTURE_ONE:
                    if (null != data) {
                        String videopath = data.getStringExtra("result");
                        onCropImageResult(videopath);
                    }
                    break;
            }
        } else {
            makeText("请拍照");
        }
    }

    public void takephotoresult() {
        File corpFile = new File(mImageFilePath + mImageFileName);
        File path = compressBitmap(corpFile.getPath());
        if (Build.VERSION.SDK_INT >= 24) {
            cropUri = FileProvider.getUriForFile(getApplicationContext(), "com.risenb.witness.fileprovider", path);
        } else {
            cropUri = Uri.fromFile(path);
        }
        onCropImageResult(null);
    }

    public File compressBitmap(String filePath) {
        // 数值越高，图片像素越低
        int inSampleSize = 3;
        BitmapFactory.Options options = new BitmapFactory.Options();
        //采样率
        options.inSampleSize = inSampleSize;

        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        if (null == bitmap) {
            Log.e("bitmap", "bitmap为空");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 把压缩后的数据存放到baos中
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        try {
            String mSDCardPath = SDCardUtils.getSDCardPath();
            String compresspath = mSDCardPath + MyApplication.photosCompressSaveUrl;
            boolean exists = FileUtils.createOrExistsDir(compresspath);
            Log.e("exists", exists + "");
            String filename = String.valueOf(System.currentTimeMillis()) + ".jpg";
            File filepath = new File(compresspath + filename);
            FileOutputStream fos = new FileOutputStream(filepath);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
            if (null != bitmap && bitmap.isRecycled()) {
                bitmap.recycle();
            }
            bitmap.recycle();
            return filepath;
        } catch (Exception e) {
            if (null != bitmap && bitmap.isRecycled()) {
                bitmap.recycle();
            }
            e.printStackTrace();

        }
        return null;
    }

    public void onCropImageResult(final String videopath) {
        MutilPartInfo info = new MutilPartInfo();
        info.setPosition(position);
        //  if (cropUri != null) {
        //Bitmap dBitmap = BitmapFactory.decodeFile(cropUri.getPath());
        //Drawable drawable = new BitmapDrawable(dBitmap);
        //   }

        if (null != videopath) {
            final String imageThumbnail = getVideoThumb2(videopath);
            Log.e("imageThumbnail", imageThumbnail);
            info.setThumbnail(imageThumbnail);
            info.setVideourl(videopath);
            info.setVideosort(mVideoSort);
            info.setVideomodify(true);
            // Log.e("VideoSort",mImageSort);
            hashmap.put(imageThumbnail, videopath);
            info.setHashmap(hashmap);
            mListVideoPath.add(videopath);
            map.put(position, info);

            //数据库操作 需要开异步
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //如果 录制视频还是有插入过数据库
                    VideoInfo videoInfo = mDaoHelper.queryVideoInfo(taskid, String.valueOf(page), mVideoSort);
                    if (null != videoInfo) {
                        Log.e("modifyidvideopath", videopath);
                        int modifyid = mDaoHelper.modifyVideoInfo(taskid, videopath, imageThumbnail, String.valueOf(page), mVideoSort);
                        Log.e("modifyid", modifyid + "");
                    } else {
                        Log.e("countvideopath", videopath);
                        int count = mDaoHelper.insertVideoInfo(taskid, videopath, imageThumbnail, "0", String.valueOf(page), mVideoSort, String.valueOf(position));
                        Log.e("mDaoHelper", count + "");
                    }
                }
            }).start();

        } else {
            if (null != cropUri) {
                String fileSize = FileUtils.getFileSize(new File(cropUri.getPath()));
                if (!"".equals(fileSize)) {
                    info.setUrl(cropUri.getPath());
                    info.setImagemodify(true);
                    Log.e("ImageSort", mImageSort);
                    info.setImagesort(mImageSort);
                    map.put(position, info);
                    flaglist.add(true);
                }
            }
        }

        listener.lunchcamera(map);
        mAdapter.notifyDataSetChanged();
    }

    private void cropImageUri(Uri uri, int outputX, int outputY, Uri cropuri, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropuri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);
    }

    public String getVideoThumb2(String path) {
        Bitmap bitmap = getVideoThumb2(path, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
        String filepath = saveImage(bitmap);
        return filepath;
    }

    public Bitmap getVideoThumb2(String path, int kind) {
        return ThumbnailUtils.createVideoThumbnail(path, kind);
    }

    public String saveImage(Bitmap bmp) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "revoeye");
        File file;
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    public InterImage getListener() {
        return listener;
    }

    public void setListener(InterImage listener) {
        this.listener = listener;
    }

}


