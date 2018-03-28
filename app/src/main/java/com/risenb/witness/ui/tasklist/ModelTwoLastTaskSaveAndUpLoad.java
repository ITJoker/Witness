package com.risenb.witness.ui.tasklist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.beans.BaseBeans;
import com.risenb.witness.beans.LastTaskBean;
import com.risenb.witness.beans.RadioButtonInfo;
import com.risenb.witness.beans.UploadFileBean;
import com.risenb.witness.beans.UploadImageBean;
import com.risenb.witness.beans.UploadVidoeTask;
import com.risenb.witness.beans.VideoInfo;
import com.risenb.witness.beans.anwersinfo;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.GsonResponseHandler;
import com.risenb.witness.network.OkHttpUtils.response.RawResponseHandler;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.ui.tasklist.DataBase.DaoHelper;
import com.risenb.witness.ui.tasklist.adapter.ModelTwoLastTaskCommconAdapter;
import com.risenb.witness.ui.tasklist.mediarecorder.VideoRecorderActivity;
import com.risenb.witness.ui.home.HomeUI;
import com.risenb.witness.utils.ConvertUtils;
import com.risenb.witness.utils.FileUtils;
import com.risenb.witness.utils.SDCardUtils;
import com.risenb.witness.utils.SharedPreferencesUtil;
import com.risenb.witness.utils.StringUtils;
import com.risenb.witness.utils.newUtils.UIManager;
import com.risenb.witness.utils.newUtils.Utils;
import com.risenb.witness.utils.sardar.CompressListener;
import com.risenb.witness.utils.sardar.Compressor;
import com.risenb.witness.utils.sardar.InitListener;
import com.risenb.witness.views.newViews.CircleProgressView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModelTwoLastTaskSaveAndUpLoad extends BaseUI {

    public static final int SYSTEM_TAKE_PHOTH_ONE_LAST = 10110;//拍照
    public static final int SYSTEM_IMAGE_CROP_ONE_LAST = 10010;
    //    public static final int SYSTEM_IMAGE_CROP_THREE = 1003;
    public static final int VIDEO_CAPTURE_ONE_LAST = 20010;//录像


    private String taskid;//任务id
    private ModelTwoLastTaskCommconAdapter mAdapter;
    @BindView(R.id.last_listview)
    ListView mListView;
    @BindView(R.id.last_task_save_info)
    Button mTaskSvae;
    @BindView(R.id.last_task_upload_info)
    Button mTaskUpload;

    @BindView(R.id.last_compress_upload_progress)
    CircleProgressView progressBar1;
    @BindView(R.id.last_layout_progress)
    RelativeLayout mLayoutProgress;
    @BindView(R.id.last_upload_task_text)
    TextView mUploadText;

    private InterImage listener;
    private String mEditStr;
    private String mEditStrid;
    private List<MutilPartInfo> list;
    private List<LastTaskBean.TaskListBean> taskList;

    //拍照
    private int position = -1; //父List item的位置 拍照或录像
    private String mVideoSort;  //视频
    private String mImageSort; //照片
    private String mImageFileName;
    private String mImageFilePath;
    private Uri cropUri = null;
    private Map<String, String> hashmap = new HashMap<>(); //key缩略图路劲 和 Value视频路劲
    private List<String> filepath = new ArrayList<>(); //图片本地路径
    private List<String> returnfile = new ArrayList<>();//服务器返回图片路径
    private List<String> mListreturnVideoPath = new ArrayList<>();//服务器返回视频路径
    private Map<String, String> videopathpos = new HashMap<>(); //视频位置
    private Map<Integer, com.risenb.witness.beans.MutilPartInfo> map = new HashMap<>();
    private List<Boolean> flaglist = new ArrayList<>();
    private int allLoadfinish = 0;//图片和视频全部上传完毕

    private int countcheck = 0;
    private String jsontsr; //测试问题的Json
    private Map<String, String> params1 = new HashMap<>();//最后提交数据的 集合
    private String marks;
    private String modeltype;
    private int page;
    private DaoHelper mDaoHelper;
    private List<VideoInfo> taskupload;  //存放没有上传任务
    private boolean uploadTask;

    private String cmd;
    private Compressor mCompressor;
    private int vidoetime;
    private String time2;
    private int time3;
    private Handler mHandler;

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
        setContentView(R.layout.savetaskupload);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        Bundle bundle = getIntent().getExtras();
        taskid = bundle.getString("taskid");
        marks = bundle.getString("marks");
        modeltype = bundle.getString("modeltype");
        page = bundle.getInt("page");//当前第几页
        onNonTaskAccessNetWorkDetail();
        mDaoHelper = new DaoHelper(this);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.arg1 > 0)
                    progressBar1.setProgress(msg.arg1);//更新进度条
            }
        };

        MyApplication.getInstance().setAreaAnwer("");
    }

    public void onNonTaskAccessNetWorkDetail() {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.myTaskDetails));
        if (StringUtils.isSpace(url)) {
            Toast.makeText(this, "网络路径无效", Toast.LENGTH_LONG).show();
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("taskId", taskid);
        params.put("c", MyApplication.getInstance().getC());
        params.put("modeltype", modeltype);
        params.put("page", String.valueOf(page));
        Utils.getUtils().showProgressDialog(this);
        Log.e("taskid", taskid + "");
        Log.e("modeltype", modeltype);
        Log.e("page3", page + "");
        Log.e("onNonC", MyApplication.getInstance().getC());
        MyOkHttp.get().post(this, url, params, new GsonResponseHandler<BaseBeans<LastTaskBean>>() {
            @Override
            public void onSuccess(int statusCode, BaseBeans<LastTaskBean> response) {
                final LastTaskBean info = response.getData();
                if (null != info) {
                    //图片 视频 单选 多选 填空 数据

                    //单独把多选 List<String> 那出来处理一下
                    List<CheckBoxInfo> mCheckBoxInfo;
                    HashMap<Integer, List<CheckBoxInfo>> checkboxmap = new HashMap<>();
                    CheckBoxInfo ckeboxinfo;

                    List<RadioButtonInfo> mRadioButtonInfo;
                    HashMap<Integer, List<RadioButtonInfo>> selectRadio = new HashMap<>();
                    RadioButtonInfo radioButtonInfo;

                    taskList = info.getTaskList();
                    for (int i = 0; i < taskList.size(); i++) {
                        LastTaskBean.TaskListBean bean = taskList.get(i);
                        String type = bean.getIsType();
                        if ("4".equals(type)) {
                            mCheckBoxInfo = new ArrayList<>();
                            List<String> liststr = bean.getExampleFile();
                            for (int k = 0; k < liststr.size(); k++) {
                                ckeboxinfo = new CheckBoxInfo();
                                ckeboxinfo.setTextContent(liststr.get(k));
                                ckeboxinfo.setSelected(false);
                                ckeboxinfo.setId(bean.getTestId());
                                mCheckBoxInfo.add(ckeboxinfo);
                            }
                            Log.e("mCheckBoxInfo", mCheckBoxInfo.toString());
                            checkboxmap.put(i, mCheckBoxInfo);
                        }
                        if ("3".equals(type)) {
                            mRadioButtonInfo = new ArrayList<>();
                            List<String> listradio = bean.getExampleFile();
                            for (int j = 0; j < listradio.size(); j++) {
                                radioButtonInfo = new RadioButtonInfo();
                                radioButtonInfo.setRaidotextContent(listradio.get(j));
                                radioButtonInfo.setSelected(false);
                                radioButtonInfo.setId(bean.getTestId());
                                mRadioButtonInfo.add(radioButtonInfo);
                            }
                            selectRadio.put(i, mRadioButtonInfo);
                        }
                    }

                    //    Log.e("mCheckBoxInfo", mCheckBoxInfo.toString());
                    //把处理过的 多选数据 设置进去
                    for (int i = 0; i < taskList.size(); i++) {
                        LastTaskBean.TaskListBean bean = taskList.get(i);
                        String type = bean.getIsType();
                        if ("4".equals(type)) {
                            List<CheckBoxInfo> listcheck = checkboxmap.get(i);
                            bean.setCheckBoxInfos(listcheck);
                        }
                        if ("3".equals(type)) {

                            List<RadioButtonInfo> radioselect = selectRadio.get(i);
                            bean.setRadioButtonInfos(radioselect);
                        }
                    }


                    //单选拿出来也需要单独处理
                    Log.e("taskList", taskList.toString());
                    // mAdapter = new ListViewCommconAdapter(LastTaskSaveAndUpLoad.this, info.getTaskList(),3,null,null);
                    mAdapter = new ModelTwoLastTaskCommconAdapter(ModelTwoLastTaskSaveAndUpLoad.this, info.getTaskList(), 3);
                    mListView.setAdapter(mAdapter);
                }
                Utils.getUtils().dismissDialog();
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {

                Utils.getUtils().dismissDialog();
            }
        });
    }

    //上传任务
    @OnClick(R.id.last_task_upload_info)
    public void UploadTaskOnClick(View view) {
        uploadTask = true;
        if (UploadVideoTask(uploadTask)) {
            return;
        }
        //  SaveTaskData();
    }

    //保存任务
    @OnClick(R.id.last_task_save_info)
    public void SaveTaskOnClick(View view) {
        submitData();
    }

    private void SaveTaskData() {
        if (UploadVideoTask(uploadTask)) {
            return;
        }
    }

    private boolean UploadVideoTask(final boolean flag) {
        //查询没有上传的任务
        boolean upload = false;
        taskupload = mDaoHelper.queryVideoInfoAll(taskid, "0");
        if (null != taskupload && taskupload.size() > 0) {
            upload = true;
            // 创建构建器
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // 设置参数
            builder.setTitle("请上传视频").setIcon(R.drawable.ic_launcher)
                    .setMessage("去上传视频")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CompressVideo(0);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog,
                                    int which) {
                    if (flag) {
                        makeText("请先上传视频");
                    } else {
                        submitData();
                    }
                }
            });
            builder.create().show();
        }
        return upload;
    }

    private void submitData() {
        List<anwersinfo> anwersinfolist = new ArrayList<>();
        BaseBeans<List<anwersinfo>> anwersjson = new BaseBeans<>();
        if (!"".equals(mEditStr)) {
//            mEditStr = event.message;
//            mEditStrid = event.id;
            anwersinfo areainfo = new anwersinfo();
            areainfo.setAnswer(mEditStr);
            areainfo.setTestid(mEditStrid);
            anwersinfolist.add(areainfo);
            Log.e("MesageEmpty", mEditStr + "---" + mEditStrid);
        }

        //获取多选的值
        for (int i = 0; i < taskList.size(); i++) {
            LastTaskBean.TaskListBean bean = taskList.get(i);
            String type = bean.getIsType();
            // List<CheckBoxInfo>  listboxinfo=null;
            if ("4".equals(type)) {
                List<CheckBoxInfo> listboxinfo = bean.getCheckBoxInfos();
                anwersinfo infosanwer = new anwersinfo();
                StringBuilder textstr = new StringBuilder();
                for (int k = 0; k < listboxinfo.size(); k++) {
                    CheckBoxInfo checkBoxInfo = listboxinfo.get(k);
                    infosanwer.setTestid(listboxinfo.get(k).getId());
                    if (checkBoxInfo.isSelected()) {
                        textstr.append(checkBoxInfo.getTextContent() + ",");
                        Log.e("被选中的", checkBoxInfo.getTextContent() + "---" + i + "--" + k + "--" + checkBoxInfo.isSelected());
                    }
                }
                //多选没选 textstr 会报空
                if (textstr.length() > 0) {
                    String newstr = textstr.substring(0, textstr.length() - 1);
                    infosanwer.setAnswer(newstr);
                }
                anwersinfolist.add(infosanwer);
            }

            if ("3".equals(type)) {
                List<RadioButtonInfo> listradioinfo = bean.getRadioButtonInfos();
                anwersinfo radioinfos;
                for (int j = 0; j < listradioinfo.size(); j++) {
                    radioinfos = new anwersinfo();
                    RadioButtonInfo inforadio = listradioinfo.get(j);

                    if (inforadio.isSelected()) {
                        radioinfos.setTestid(inforadio.getId());
                        radioinfos.setAnswer(inforadio.getRaidotextContent());
                        anwersinfolist.add(radioinfos);
                    }
                }
            }
            //resultmap.put(i,anwersinfolist);
        }
        // resultmap
        anwersjson.setData(anwersinfolist);
        jsontsr = JSON.toJSONString(anwersjson);
        params1.put("testJson", jsontsr);
        if (null != map && map.size() > 0) {
            for (Map.Entry<Integer, com.risenb.witness.beans.MutilPartInfo> entry : map.entrySet()) {
                int pos = entry.getKey();
                com.risenb.witness.beans.MutilPartInfo info = entry.getValue();
                Log.e("size", map.size() + "");
                Log.e("Keyvalue", "key= " + entry.getKey() + " and value= " + entry.getValue());
                Log.e("pos", pos + "---" + info.getUrl());
                String imageurl = info.getUrl();
                if (null != imageurl) {
                    boolean flagimage = info.isImagemodify();//判断图片有没有上传过
                    if (flagimage) {
                        flaglist.add(true);
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
                    }
                }

//                String  videourl=  info.getVideourl();
//                if(null==videourl){
//                    boolean flagimage= info.isImagemodify();//判断图片有没有上传过
//                    if(flagimage){
//                        flaglist.add(true);
//                        String imagesort=info.getImagesort();
//                        Log.e("UploadFilevideosort",imagesort);
//                        filepath.add(info.getUrl());
//                        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.pictureUpload));
//                        Map<String, File> files= new HashMap<>();
//                        File file = new File(info.getUrl());
//                        files.put("file", file);
//                        Map<String, String> params = new HashMap<>();
//                        params.put("taskid", taskid);
//                        params.put("sort",imagesort);
//                       UploadImage(url, params, files);
//                    }
//
//                }else{
//                    boolean flagvideo= info.isVideomodify();//视频是否上传过
//                    if(flagvideo){
//                        flaglist.add(true);
//                        String videosort=info.getVideosort();
//                        Log.e("UploadFileimagesort",videosort);
//                        Map<String, File> files= new HashMap<>();
//                        File file = new File(info.getVideourl());
//                        files.put("video", file);
//                        Map<String, String> params = new HashMap<>();
//                        params.put("taskid", taskid);
//                        params.put("sort",videosort);
//                         //UploadVideo(params, files);
//                    }

            }
        } else {
            UploadHttpUrlAndSort();
        }
//        }else{
//
//              // makeText("请取证");
//            UploadHttpUrlAndSort();
//        }
    }

    public synchronized void UploadImage(String url, Map<String, String> params, final Map<String, File> files) {

        Utils.getUtils().showProgressDialog(this);
        MyOkHttp.get().upload(this, url, params, files, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                Gson gson = new Gson();
                UploadImageBean info = gson.fromJson(response, UploadImageBean.class);
                String uploadImagePath = info.getData().getFile_url();
                Log.e("uploadImagePath", uploadImagePath);
                String imagesort = info.getData().getSort();
                Log.e("imagesort", imagesort);
                returnfile.add(uploadImagePath);
                videopathpos.put(imagesort, uploadImagePath);
                //uploadfinish++;
                allLoadfinish++;
                Utils.getUtils().dismissDialog();
                if (allLoadfinish == map.size()) {
                    UploadHttpUrlAndSort();

                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {

            }
        });
    }

//    public synchronized void UploadVideo( Map<String, String> params, final Map<String, File> files) {
//        String videourl = getResources().getString(R.string.service_host_address).concat(getString(R.string.videoUpload));
//        Utils.getUtils().showProgressDialog(this);
//        MyOkHttp.get().upload(this, videourl, params, files, new RawResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, String response) {
//                Log.e("response", response.toString());
//                Utils.getUtils().dismissDialog();
//                Gson gson = new Gson();
//                UploadImageBean info = gson.fromJson(response, UploadImageBean.class);
//                String videoPath = info.getData().getFile_url();
//                String videoSort= info.getData().getSort();
//                Log.e("videoSort",videoSort);
//                videopathpos.put(videoSort,videoPath);
//                mListreturnVideoPath.add(videoPath);
//                returnfile.add(videoPath);
//                Log.e("UploadvideoPath", videoPath.toString());
//                Log.e("videopath.size()", filepath.size() + "");
//                allLoadfinish++;
//                if(allLoadfinish==map.size()){
//                    UploadHttpUrlAndSort();
//                }
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, String error_msg) {
//                Log.e("error_msg", error_msg);
//                Utils.getUtils().dismissDialog();
//            }
//        });
//
//        // }
//    }

    public void UploadHttpUrlAndSort() {

        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.saveTaskfilepath));
        params1.put("taskid", taskid);
        params1.put("c", MyApplication.getInstance().getC());
        if (null != videopathpos && videopathpos.size() > 0) {
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
        }
        SaveFilePathTaskAccessNetWork(url, params1);

    }


    private void SaveFilePathTaskAccessNetWork(String url, Map<String, String> params) {

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
                Log.e("twolastsuccess", success);
                if ("1".equals(success)) {
                    makeText("保存成功");
                    for (Map.Entry<Integer, com.risenb.witness.beans.MutilPartInfo> entry : map.entrySet()) {
                        com.risenb.witness.beans.MutilPartInfo info = entry.getValue();
                        info.setImagemodify(false);
                        info.setVideomodify(false);
                    }

                    // uploadvideofinish=0;
                    allLoadfinish = 0;
                    Utils.getUtils().dismissDialog();
                    finish();

                    UIManager.getInstance().popActivity(ModelTwoEvidenceFirst.class);
                    UIManager.getInstance().popActivity(ModelTwoNonIssueState.class);
//                    Intent intent = new Intent(EvidenceFirst.this, TaskIssueState.class);
//                    Bundle bundle=new Bundle();
//                    bundle.putString("taskid",taskid);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
                } else {
                    Utils.getUtils().dismissDialog();
                    finish();
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveMsgMainThread(EventMessage.MesageEmpty event) {
        // Toast.makeText(this,event.message,Toast.LENGTH_SHORT).show();
        if (!"".equals(event.message)) {
            mEditStr = event.message;
            mEditStrid = event.id;
            Log.e("MesageEmpty", event.message);
            Log.e("MesageEmpty", event.id);
            Log.e("mEditStr", mEditStr);
        }
    }

    //录像
    public void CameraVideoRcord(int position, String sort) {
        this.position = position;
        this.mVideoSort = sort;
        Intent intent = new Intent(this, VideoRecorderActivity.class);
        startActivityForResult(intent, VIDEO_CAPTURE_ONE_LAST);
    }


    //拍照
    public void TaskObtainEvidenceTakePhoto(int flag, int position, String sort) {
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

    //获取 图片存储 路径
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
        switch (requestCode) {
            case SYSTEM_TAKE_PHOTH_ONE_LAST:
                takephotoresult(SYSTEM_IMAGE_CROP_ONE_LAST);
                break;
            case SYSTEM_IMAGE_CROP_ONE_LAST:
                //如果是图片直接传null
                onCropImageResult(null);
                break;
            case VIDEO_CAPTURE_ONE_LAST:
                if (null != data) {
                    String videopath = data.getStringExtra("result");
                    onCropImageResult(videopath);
                }
                break;
        }
    }

    public void takephotoresult(int flag) {
        File file = new File(mImageFilePath + mImageFileName);
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(getApplicationContext(), "com.risenb.witness.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        String uCorpImageName = "Corp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        File corpFile = new File(mImageFilePath + uCorpImageName);
        if (Build.VERSION.SDK_INT >= 24) {
            cropUri = FileProvider.getUriForFile(getApplicationContext(), "com.risenb.witness.fileprovider", corpFile);
        } else {
            cropUri = Uri.fromFile(corpFile);
        }
        int height = ConvertUtils.dp2px(this, 300);
        cropImageUri(uri, 500, 500, cropUri, flag);
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

    public void onCropImageResult(final String videopath) {
        com.risenb.witness.beans.MutilPartInfo info = new com.risenb.witness.beans.MutilPartInfo();
        info.setPosition(position);
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
            //mListVideoPath.add(videopath);
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
                        int count = mDaoHelper.insertVideoInfo(taskid, videopath, imageThumbnail, "0", String.valueOf(page), mVideoSort, null);
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
                }

            }
        }

        listener.lunchcamera(map);
        mAdapter.notifyDataSetChanged();
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
        File file = null;
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

    public void CompressVideo(final int uploadint) {
        Log.e("uploadint", uploadint + "");
        if (uploadint >= taskupload.size()) {
            submitData();
            return;
        }
        final VideoInfo videoInfo = taskupload.get(uploadint);
//        taskupload
        mLayoutProgress.setVisibility(View.VISIBLE);

        String mSDCardPath = SDCardUtils.getSDCardPath();
        String mVidieoRcordPath = mSDCardPath + MyApplication.videosSaveUrl;
        File mediaStorageDir = new File(mVidieoRcordPath, "Compressor");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("CameraSample", "failed to create directory");
                return;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        final File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "VID_" + timeStamp + ".mp4");
        //获取视频的时长
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(videoInfo.getPath());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        vidoetime = mediaPlayer.getDuration();

        mUploadText.setText("视频压缩中");
        cmd = "-y -i " + videoInfo.getPath() + " -strict -2 -vcodec libx264 -preset ultrafast -crf 24 -acodec aac -ar 44100 -ac 2 -b:a 96k -s 480x320 -aspect 16:9 " + mediaFile.getPath();
        Log.e("Compressor", cmd);
        mCompressor = new Compressor(this);
        mCompressor.loadBinary(new InitListener() {
            @Override
            public void onLoadSuccess() {
                mCompressor.execCommand(cmd, new CompressListener() {
                    @Override
                    public void onExecSuccess(String message) {
                        Log.e("onExecSuccess", message);
                        //mLayoutProgress.setVisibility(View.GONE);
                        progressBar1.setProgress(0);
                        Map<String, File> files = new HashMap<>();
                        File file = new File(mediaFile.getPath());
                        files.put("video", file);
                        Map<String, String> params = new HashMap<>();
                        params.put("taskid", taskid);
                        params.put("sort", videoInfo.getSort());
                        UploadVideo(params, files, videoInfo, uploadint);

                    }

                    @Override
                    public void onExecFail(String reason) {
                        Log.i("fail", reason);
                        mLayoutProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onExecProgress(String message) {

                        /// Log.i("progress",message);
                        if (message.contains("time=")) {
                            //截取出message中的时间
                            time2 = message.substring(message.indexOf("time=") + 5, message.indexOf("time") + 13);
                            if (!"null".equals(time2)) {
                                //把截取出的时间转int
                                int hour = Integer.valueOf(time2.substring(0, 2)) * 60 * 60 * 1000;
                                int minute = Integer.valueOf(time2.substring(3, 5)) * 60 * 1000;
                                int second = Integer.valueOf(time2.substring(6)) * 1000;
                                time3 = hour + minute + second;
                            }
                        }
                        int process = (time3 * 100 / vidoetime);
                        Message message2 = new Message();
                        message2.arg1 = process;
                        mHandler.sendMessage(message2);
                        Log.e("进度======progress", "" + process);
                        Log.i("进度======message", "" + message);
                    }
                });
            }

            @Override
            public void onLoadFail(String reason) {
                Log.i("fail", reason);
            }
        });
    }


    public synchronized void UploadVideo(Map<String, String> params, final Map<String, File> files, final VideoInfo videoInfo, final int up) {
        String videourl = getResources().getString(R.string.service_host_address).concat(getString(R.string.videoUpload));
        mUploadText.setText("视频上传中");
        MyOkHttp.get().upload(this, videourl, params, files, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                // Utils.getUtils().dismissDialog();
                Gson gson = new Gson();
                UploadImageBean info = gson.fromJson(response, UploadImageBean.class);
                int sucess = info.getSuccess();
                if (sucess == 1) {
                    mLayoutProgress.setVisibility(View.GONE);
                    String videoPath = info.getData().getFile_url();
                    String videoSort = info.getData().getSort();
                    Log.e("videoPath", videoPath);
                    Log.e("videoSort", videoSort);
                    UploadVidoeTask returnfile = new UploadVidoeTask(videoSort, videoPath);
                    CompressVideo(up + 1);
                    // list.add(returnfile);
                    //  int count=  mDaoHelper.finishVideoInfo(taskid,videoInfo.getPath(),videoInfo.getPage(),videoInfo.getSort(),"1");
                    // adapter.setPosition(position);
                    //  Log.e("finshUploadVideo",count+"");
                    //  Log.e("returnfilelist",list.toString());
                }

            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Log.e("error_msg", error_msg);
                Utils.getUtils().dismissDialog();
            }

            @Override
            public void onProgress(long currentBytes, long totalBytes) {
                Log.e("onProgress", "current/total:  " + currentBytes + "/" + totalBytes);
                int process = 0;
                if (totalBytes != 0) {
                    process = (int) (((float) currentBytes / totalBytes) * 100);
                }
                Message message = new Message();
                message.arg1 = process;
                mHandler.sendMessage(message);
            }
        });


    }

    public InterImage getListener() {
        return listener;
    }

    public void setListener(InterImage listener) {
        this.listener = listener;
    }
}
