package com.risenb.witness.ui.tasklist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.beans.BaseBeans;
import com.risenb.witness.beans.ExecTaskInfo;
import com.risenb.witness.beans.MultimediaInfo;
import com.risenb.witness.beans.TaskInfoDetails;
import com.risenb.witness.beans.UploadImageBean;
import com.risenb.witness.beans.UploadTaskDetail;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.GsonResponseHandler;
import com.risenb.witness.network.OkHttpUtils.response.RawResponseHandler;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.ui.tasklist.fragment.ExecutingTaskFragment;
import com.risenb.witness.ui.tasklist.fragment.NonExecutionTaskFragment;
import com.risenb.witness.ui.tasklist.fragment.UploadedTaskFragment;
import com.risenb.witness.ui.tasklist.mediarecorder.MediaRecorderActivity;
import com.risenb.witness.utils.ConvertUtils;
import com.risenb.witness.utils.FileUtils;
import com.risenb.witness.utils.MyConfig;
import com.risenb.witness.utils.SDCardUtils;
import com.risenb.witness.utils.SharedPreferencesUtil;
import com.risenb.witness.utils.StringUtils;
import com.risenb.witness.utils.newUtils.UIManager;
import com.risenb.witness.utils.newUtils.Utils;
import com.risenb.witness.views.citypicker.CountDownTimerView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.risenb.witness.beans.TaskInfoDetails.TaskListBean;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaskObtainEvidence extends BaseUI {
    public static final int SYSTEM_TAKE_PHOTH_ONE = 1011;
    public static final int SYSTEM_TAKE_PHOTH_SECOND = 1012;
    public static final int SYSTEM_TAKE_PHOTH_THREE = 1013;
    public static final int SYSTEM_IMAGE_CROP_ONE = 1001;
    public static final int SYSTEM_IMAGE_CROP_SECOND = 1002;
    public static final int SYSTEM_IMAGE_CROP_THREE = 1003;

    public static final int VIDEO_CAPTURE_ONE = 2001;
    //地址
    @BindView(R.id.taskobtainevi_adrress)
    TextView mTaskobtaineviAdrress;
    //价格
    @BindView(R.id.taskobtain_price)
    TextView mTaskobtainPrice;
    //失效倒计时
    @BindView(R.id.takobtain_countdown)
    CountDownTimerView mTakobtainCountdown;

    @BindView(R.id.taskobtain_progressbar)
    ProgressBar mTaskobtainProgressbar;


    //第一张示例图片
    @BindView(R.id.image_example_imageone)
    ImageView mTaskobtainExampleImageOne;
    //第一个 示例的要求
    @BindView(R.id.taskobtain_xiaoquone)
    TextView mTaskObtainRequestOne;

    @BindView(R.id.taskobtain_image_deletetwo)
    Button mTaskobtainImageDeletetwo;
    //第一个拍照按钮
    @BindView(R.id.taskobtain_cameratwo)
    Button mTaskobtainCameratwo;
    //第一个拍照录像
    @BindView(R.id.taskobtain_recordvideoone)
    Button mTaskobtainRecordvideoone;

    //第二个 示例的要求
    @BindView(R.id.taskobtain_xiaoqutwo)
    TextView mTaskObtainRequestSecond;
    @BindView(R.id.image_example_imagetwo)
    ImageView mTaskobtainExampleImageSecond;


    @BindView(R.id.taskobtain_image_deletethree)
    Button mTaskobtainImageDeletethree;

    //第二个拍照按钮
    @BindView(R.id.taskobtain_camera_three)
    Button mTaskobtainCameraThree;
    //第二个录像按钮
    @BindView(R.id.taskobtain_recordvideothree)
    Button mTaskobtainRecordvideothree;
    //第二个 拍摄的图片显示
    @BindView(R.id.obtainEvidence_show_imagetwo)
    ImageView mShowImageView_two;

    //取证3
    // //第三个 示例的要求
    @BindView(R.id.taskobtain_xiaoquthree)
    TextView mTaskObtainRequestThree;

    @BindView(R.id.taskobtain_image_deleteone)
    Button mTaskobtainImageDeleteone;

    //第三个 拍照按钮
    @BindView(R.id.taskobtain_camera)
    Button taskobtainCamera;
    //第三个录像按钮
    @BindView(R.id.taskobtain_recordvideotwo)
    Button mTaskobtainRecordvideotwo;

    @BindView(R.id.image_example_imagethree)
    ImageView mTaskobtainExampleImageThree;


    @BindView(R.id.obtainEvidence_show_imagethree)
    ImageView mImageshowTwo;

    @BindView(R.id.task_next_step)
    Button mTaskNextStep;

    @BindView(R.id.linearLayout_show_imageone)
    RelativeLayout mTask_Show_Image_One;

    @BindView(R.id.linearLayout_show_imagetwo)
    RelativeLayout mTask_Show_Image_Two;

    @BindView(R.id.linearLayout_show_imagethree)
    RelativeLayout mTask_Show_Image_Three;

    //最底层文字提示
    @BindView(R.id.completed_upload_mode)
    LinearLayout mCompletedUploadMode;

    //漏刊范围
    @BindView(R.id.completed_upload_range)
    TextView mTaskLouKanRange;

    private String marks;
    private String taskid;
    @BindView(R.id.obtainEvidence_show_imageone)
    ImageView mImageshowOne;

    private String mImageFileName;
    private String mImageFilePath;

    private String mVidieoRcordName;
    private String mVidieoRcordPath;

    private Uri cropUri = null;

    private Uri mVideoUri = null;

    private int screenWidth;
    private int screenHeight;

    List<String> filepath = new ArrayList<>();
    int uploadfinish = 0;

    private String videopath;
    private List<String> videopatlist = new ArrayList<>();
    private TaskListBean infos;
    private TaskListBean lastevidengce;

    private String uploadImagePath;
    private String videoPath;
    private List<String> mUploadImagePathlist = new ArrayList<>();

    private List<String> returnImagelist = new ArrayList<>();
    private List<String> position = new ArrayList<>();
    private String mVideoPosition;//视频的位置
    private String mImagePosition;//图片位置
    private String mImageOnePosition;//图片位置
    private String mImageTwoPosition;//图片位置
    private boolean isVideo;  //  是否包含视频
    private boolean modifyfile = false; //是否修改
    private boolean modifyvideo = false; //视频

    private ExecTaskInfo.TaskListBean execinfo;//执行中上刊数据
    private ExecTaskInfo.TaskListBean execlast;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {

    }

    @Override
    protected void onCreate() {
        super.onCreate();
        setContentView(R.layout.taskobtainevidence);
        ButterKnife.bind(this);
        marks = getIntent().getStringExtra("marks");
        taskid = getIntent().getStringExtra("taskid");
        if (marks.equals(NonExecutionTaskFragment.NONEXECUTIONFRAGMENT)) {
            String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.myTaskDetails));
            Map<String, String> params = new HashMap<>();
            params.put("taskId", taskid);
            params.put("c", "3c2d4defd9c22732fe960c48f6ac5436");
            onNonTaskAccessNetWorkDetail(url, params);
        } else if (marks.equals(ExecutingTaskFragment.EXECUTIONINGFRAGMENT)) {
            mTask_Show_Image_One.setVisibility(View.VISIBLE);
            mTask_Show_Image_Two.setVisibility(View.VISIBLE);
            mTask_Show_Image_Three.setVisibility(View.VISIBLE);
            String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.UploadedDetails));
            Map<String, String> mExecparams = new HashMap<>();
            mExecparams.put("taskId", taskid);
            mExecparams.put("c", "3c2d4defd9c22732fe960c48f6ac5436");
            ExecTaskAccessNetWork(url, mExecparams);
        } else if (marks.equals(UploadedTaskFragment.COMUPLOADFRAGMENT)) {
            String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.UploadedDetails));
            Map<String, String> params = new HashMap<>();
            params.put("taskid", taskid);
            params.put("c", "3c2d4defd9c22732fe960c48f6ac5436");
            onNonTaskAccessNetWorkDetail(url, params, null);
        }

        WindowManager wm1 = this.getWindowManager();
        screenWidth = wm1.getDefaultDisplay().getWidth();
        screenHeight = wm1.getDefaultDisplay().getHeight();
        UIManager.getInstance().pushActivity(this);
    }

    @Override
    protected void prepareData() {

    }

    @Override
    public void onLoadOver() {

    }

    //删除第一张照片
    @OnClick(R.id.taskobtain_image_deletetwo)
    public void deleteShowCameraImage(View view) {
        if (filepath.size() > 0) {
            filepath.remove(0);
        }

        Drawable mDrawable = mImageshowOne.getDrawable();
        if (mDrawable != null && mDrawable instanceof BitmapDrawable) {
            Bitmap bmp = ((BitmapDrawable) mDrawable).getBitmap();
            bmp.recycle();
            mImageshowOne.setImageBitmap(null);
        }
        if (mDrawable != null) {
            mDrawable.setCallback(null);
        }
        mTask_Show_Image_One.setVisibility(View.GONE);
    }

    @OnClick(R.id.task_next_step)
    public void OnClickTaskNext() {
        if (marks.equals(ExecutingTaskFragment.EXECUTIONINGFRAGMENT)) {
            UploadFile();
        } else if (marks.equals(NonExecutionTaskFragment.NONEXECUTIONFRAGMENT)) {
            UploadFile();
        }
    }

    public void UploadFile() {
        if (filepath.size() >= 0 && !filepath.isEmpty()) {
            String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.pictureUpload));
            for (int i = 0; i < filepath.size(); i++) {
                Map<String, File> files = new HashMap<>();
                File file = new File(filepath.get(i));
                files.put("file" + i, file);
                Map<String, String> params = new HashMap<>();
                params.put("taskid", taskid);
                UploadImage(url, params, files);
            }
        } else {
            Intent intent = new Intent(this, TaskInfoSituation.class);
            Bundle bundle = new Bundle();
            if (marks.equals(ExecutingTaskFragment.EXECUTIONINGFRAGMENT)) {
                bundle.putSerializable("param", execinfo);
                bundle.putSerializable("lastevidence", execlast);
                bundle.putString("marks", ExecutingTaskFragment.EXECUTIONINGFRAGMENT);
            }
            if (marks.equals(NonExecutionTaskFragment.NONEXECUTIONFRAGMENT)) {
                // bundle.putSerializable("param",infos);
                // bundle.putSerializable("lastevidence",lastevidengce);
                bundle.putString("marks", NonExecutionTaskFragment.NONEXECUTIONFRAGMENT);
            }
            bundle.putString("taskid", taskid);

            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    //第一个 拍照按钮
    @OnClick(R.id.taskobtain_cameratwo)
    public void OnClickFirstButtonTakePhoto(View view) {
        TaskObtainEvidenceTakePhoto(SYSTEM_TAKE_PHOTH_ONE);
    }

    public void TaskObtainEvidenceTakePhoto(int flag) {
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

    //录像
    @OnClick(R.id.taskobtain_recordvideothree)
    public void OnClickTwoVideoRecord(View view) {
        CameraVideoRcord();
    }

    //第三个拍照按钮
    @OnClick(R.id.taskobtain_camera)
    public void OnClickThreeButtonTakePhoto(View view) {
        TaskObtainEvidenceTakePhoto(SYSTEM_TAKE_PHOTH_THREE);
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
        switch (requestCode) {
            case SYSTEM_TAKE_PHOTH_ONE:
                takephotoresult(SYSTEM_IMAGE_CROP_ONE);
                break;
            case SYSTEM_TAKE_PHOTH_SECOND:
                break;
            case SYSTEM_TAKE_PHOTH_THREE:
                takephotoresult(SYSTEM_IMAGE_CROP_THREE);
                break;
            case SYSTEM_IMAGE_CROP_ONE:
                onCropImageResult(SYSTEM_IMAGE_CROP_ONE);
                break;
            case SYSTEM_IMAGE_CROP_SECOND:
                break;
            case SYSTEM_IMAGE_CROP_THREE:
                onCropImageResult(SYSTEM_IMAGE_CROP_THREE);
                break;
            case VIDEO_CAPTURE_ONE:
                if (null != data) {
                    modifyvideo = true;
                    videopath = data.getStringExtra("result");
                    if (videopatlist.size() > 0 && !videopatlist.isEmpty()) {
                        videopatlist.clear();
                        videopatlist.add(videopath);
                    } else {
                        videopatlist.add(videopath);
                    }
                    Log.e("videopath", videopath);
                    Bitmap bitmap = getVideoThumb2(videopath);
                    mTask_Show_Image_Two.setVisibility(View.VISIBLE);
                    mShowImageView_two.setImageBitmap(bitmap);
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

    public void onCropImageResult(int flag) {
        if (cropUri != null) {
            Bitmap dBitmap = BitmapFactory.decodeFile(cropUri.getPath());
            //Drawable drawable = new BitmapDrawable(dBitmap);
            if (flag == SYSTEM_IMAGE_CROP_ONE) {
                Log.e("cropUri.getPath()", cropUri.getPath());
                mTask_Show_Image_One.setVisibility(View.VISIBLE);
                mImageshowOne.setImageBitmap(dBitmap);
                //  boolean flagfile= FileUtils.isFile(cropUri.getPath());
                modifyfile = true;
                if (filepath.size() > 0 && !filepath.isEmpty()) {
                    filepath.clear();
                    filepath.add(0, cropUri.getPath());
                } else {
                    filepath.add(0, cropUri.getPath());
                }
                Log.e("path0", filepath.get(0) + "");
            }

            if (flag == SYSTEM_IMAGE_CROP_SECOND) {

            }

            if (flag == SYSTEM_IMAGE_CROP_THREE) {
                mTask_Show_Image_Three.setVisibility(View.VISIBLE);
                mImageshowTwo.setImageBitmap(dBitmap);
                boolean flagfile = FileUtils.isFile(cropUri.getPath());
                modifyfile = true;
                if (filepath.size() >= 2 && !filepath.isEmpty()) {
                    filepath.remove(1);
                    filepath.add(1, cropUri.getPath());
                } else {
                    filepath.add(1, cropUri.getPath());
                }
                Log.e("path1", filepath.get(1) + "");
            }
        }
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

    public void onNonTaskAccessNetWorkDetail(String url, Map<String, String> params) {
        if (StringUtils.isSpace(url)) {
            Toast.makeText(this, "无效网址", Toast.LENGTH_LONG).show();
            return;
        }

        Utils.getUtils().showProgressDialog(this);
        Log.e("taskid", taskid + "");
        MyOkHttp.get().post(this, url, params, new GsonResponseHandler<BaseBeans<TaskInfoDetails>>() {
            @Override
            public void onSuccess(int statusCode, BaseBeans<TaskInfoDetails> response) {
                TaskInfoDetails info = response.getData();
                Log.e("TaskInfoDetails", info.toString());
                // mTask_Show_Image_One.setVisibility(View.VISIBLE);
                // mTask_Show_Image_Two.setVisibility(View.VISIBLE);
                //mTask_Show_Image_Three.setVisibility(View.VISIBLE);
                // mCompletedUploadMode.setVisibility(View.VISIBLE);
                mTaskobtaineviAdrress.setText(info.getAddress());
                mTaskobtainPrice.setText("￥" + info.getPrice());
                String mValidityTime = info.getValidity();
                formatTime(Long.valueOf(mValidityTime));
                // String mInfoModeltype = info.getModeltype();//跳转模版值
                //   String mIsType = info.getTaskList().get(0).getIsType();
                for (int k = 0; k < info.getTaskList().size(); k++) {
                    if (info.getTaskList().get(k).getIsType().equals("0")) {
                        break;
                    }
                }

                for (int i = 0; i < info.getTaskList().size(); i++) {
                    String mSort = info.getTaskList().get(i).getSort();
                    if (mSort.equals("4")) {
                        infos = info.getTaskList().get(i);
                    }
                    if (mSort.equals("5")) {
                        lastevidengce = info.getTaskList().get(i);
                    }
                    if (mSort.equals("1")) {
                        mTaskObtainRequestOne.setText(info.getTaskList().get(i).getTaskRemark());//拍摄需求
                        //  ImageLoader.getInstance().displayImage(info.getTaskList().get(0).getExampleFile().get(0),mTaskobtainExampleImageOne, MyConfig.options);
                        if (info.getTaskList().get(i).getIsType().equals("0")) {
                            mTaskobtainCameratwo.setVisibility(View.GONE);
                            mVideoPosition = mSort;
                            isVideo = true;
                        } else if (info.getTaskList().get(i).getIsType().equals("1")) {
                            mTaskobtainCameratwo.setVisibility(View.VISIBLE);
                            //  mImageOnePosition=mSort;
                            position.add(mSort);
                        }
                    } else if (mSort.equals("2")) {
                        mTaskObtainRequestSecond.setText(info.getTaskList().get(i).getTaskRemark());//拍摄需求
                        // ImageLoader.getInstance().displayImage(info.getTaskList().get(0).getExampleFile().get(0),mTaskobtainExampleImageSecond, MyConfig.options);
                        if (info.getTaskList().get(i).getIsType().equals("0")) {
                            mTaskobtainCameraThree.setVisibility(View.GONE);
                            mVideoPosition = mSort;
                            isVideo = true;
                        } else if (info.getTaskList().get(i).getIsType().equals("1")) {
                            position.add(mSort);
                        }
                    } else if (mSort.equals("3")) {
                        mTaskObtainRequestThree.setText(info.getTaskList().get(i).getTaskRemark());//拍摄需求
                        // ImageLoader.getInstance().displayImage(info.getTaskList().get(0).getExampleFile().get(0),mTaskobtainExampleImageThree, MyConfig.options);
                        if (info.getTaskList().get(i).getIsType().equals("0")) {
                            taskobtainCamera.setVisibility(View.GONE);
                            mVideoPosition = mSort;
                            isVideo = true;
                        } else if (info.getTaskList().get(i).getIsType().equals("1")) {
                            position.add(mSort);
                        }
                    }
                }
                Utils.getUtils().dismissDialog();
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }


    public synchronized void onNonTaskAccessNetWorkDetail(String url, Map<String, String> params, final Map<String, File> files) {
        if (StringUtils.isSpace(url)) {
            Toast.makeText(this, "网络路径无效", Toast.LENGTH_LONG).show();
            return;
        }
        Utils.getUtils().showProgressDialog(this);

        if (files.isEmpty()) {
            MyOkHttp.get().post(TaskObtainEvidence.this, url, params, new GsonResponseHandler<BaseBeans<UploadTaskDetail>>() {
                @Override
                public void onFailure(int statusCode, String error_msg) {
                    Utils.getUtils().dismissDialog();
                }

                @Override
                public void onSuccess(int statusCode, BaseBeans<UploadTaskDetail> response) {
                    Utils.getUtils().dismissDialog();
                    UploadTaskDetail info = response.getData();

                    int flag1 = info.getTestList().get(0).getIsType();
                    mTaskObtainRequestOne.setText(info.getTestList().get(0).getTaskremark());
                    //示例图 1
                    ImageLoader.getInstance().displayImage(info.getTestList().get(0).getExampleFile().get(0), mTaskobtainExampleImageOne, MyConfig.options);
                    if (flag1 != 0) {
                        //拍摄的图片1
                        // if(){}
                        //mImageshowOne.setImageBitmap();
                        mTaskobtainCameratwo.setVisibility(View.VISIBLE);

                    } else {
                        mTaskobtainCameratwo.setVisibility(View.GONE);
                        // mTaskobtainRecordvideoone
                        //视频
                    }

                    //取证2
                    //第二个 拍摄要求
                    int flag2 = info.getTestList().get(1).getIsType();
                    mTaskObtainRequestSecond.setText(info.getTestList().get(1).getTaskremark());
                    //示例图 2
                    //mTaskobtainExampleImageSecond.setImageBitmap();
                    if (flag2 != 0) {
                        //拍摄的图片2
                        //mShowImageView_two.setImageBitmap();
                    } else {
                        //视频
                    }

                    //取证3
                    //第三个 拍摄要求
                    int flag3 = info.getTestList().get(2).getIsType();
                    mTaskObtainRequestThree.setText(info.getTestList().get(2).getTaskremark());
                    if (flag3 != 0) {
                        //拍摄的图片3
                    } else {
                        //视频
                    }
                }
            });
        } else {

        }
    }

    public synchronized void UploadImage(String url, Map<String, String> params, final Map<String, File> files) {
        Utils.getUtils().showProgressDialog(this);
        if (modifyfile) {
            MyOkHttp.get().upload(TaskObtainEvidence.this, url, params, files, new RawResponseHandler() {
                @Override
                public void onSuccess(int statusCode, String response) {
                    Log.e("response", response.toString());
                    Gson gson = new Gson();
                    UploadImageBean info = gson.fromJson(response, UploadImageBean.class);
                    uploadImagePath = info.getData().getFile_url();
                    mUploadImagePathlist.add(uploadImagePath);
                    returnImagelist.add(uploadImagePath);
                    Log.e("UploadImageBean", uploadImagePath.toString());
                    uploadfinish++;
                    Log.e("filepath.size()", filepath.size() + "");
                    Log.e("Imageuploadfinish", uploadfinish + "");
                    modifyfile = false;
                    if (uploadfinish == filepath.size()) {
                        if (isVideo) {
                            if (videopatlist.size() >= 1 && null != videopatlist.get(0) && !videopatlist.isEmpty()) {
                                if (modifyvideo) {
                                    UploadVideo();
                                }
                            }
                        } else {
                            MultimediaInfo multimediaInfo = new MultimediaInfo();
                            Intent intent = new Intent(TaskObtainEvidence.this, TaskInfoSituation.class);
                            Bundle bundle = new Bundle();
                            if (mUploadImagePathlist.size() > 0) {
                                multimediaInfo.setUploadImagePath(mUploadImagePathlist);
                            }
                            bundle.putString("taskid", taskid);
                            intent.putExtras(bundle);
                            EventBus.getDefault().postSticky(new EventMessage.MesageMultimediaInfo(multimediaInfo));
                            startActivity(intent);
                            uploadfinish = 0;
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, String error_msg) {
                    Utils.getUtils().dismissDialog();
                }
            });
        } else {
            if (modifyvideo) {
                if (null != videopath && !videopath.equals("")) {
                    UploadVideo();
                }
            } else {
                MultimediaInfo multimediaInfo = new MultimediaInfo();
                Intent intent = new Intent(TaskObtainEvidence.this, TaskInfoSituation.class);
                Bundle bundle = new Bundle();
                if (mUploadImagePathlist.size() > 0) {
                    multimediaInfo.setUploadImagePath(mUploadImagePathlist);
                }

                if (null != videoPath) {
                    multimediaInfo.setVideoPath(videopatlist.get(0));
                }

                if (marks.equals(ExecutingTaskFragment.EXECUTIONINGFRAGMENT)) {
                    bundle.putSerializable("param", execinfo);
                    bundle.putSerializable("lastevidence", execlast);
                    bundle.putString("marks", ExecutingTaskFragment.EXECUTIONINGFRAGMENT);
                }
                if (marks.equals(NonExecutionTaskFragment.NONEXECUTIONFRAGMENT)) {
                    bundle.putString("marks", NonExecutionTaskFragment.NONEXECUTIONFRAGMENT);
                }
                bundle.putString("taskid", taskid);
                intent.putExtras(bundle);
                EventBus.getDefault().postSticky(new EventMessage.MesageMultimediaInfo(multimediaInfo));
                uploadfinish = 0;
                modifyfile = false;
                modifyvideo = false;
                startActivity(intent);
            }
        }
    }

    public void UploadVideo() {
        String videourl = getResources().getString(R.string.service_host_address).concat(getString(R.string.videoUpload));
        Map<String, File> filevideo = new HashMap<>();
        File file = new File(videopatlist.get(0));
        filevideo.put("cao", file);
        Map<String, String> params1 = new HashMap<>();
        params1.put("taskid", taskid);
        Utils.getUtils().showProgressDialog(this);
        MyOkHttp.get().upload(TaskObtainEvidence.this, videourl, params1, filevideo, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                Log.e("response", response.toString());
                Utils.getUtils().dismissDialog();
                Gson gson = new Gson();
                UploadImageBean info = gson.fromJson(response, UploadImageBean.class);
                videoPath = info.getData().getFile_url();
                returnImagelist.add(videoPath);
                Log.e("UploadvideoPath", videoPath.toString());
                Log.e("videopath.size()", filepath.size() + "");
                Log.e("videopathuploadfinish", uploadfinish + "");
                String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.saveTaskfilepath));
                Map<String, String> params1 = new HashMap<>();
                params1.put("taskid", taskid);
                params1.put("c", application.getC());
                if (position.size() >= 3) {
                    params1.put("returnFile1", returnImagelist.get(0));
                    params1.put("longtitude1", SharedPreferencesUtil.getString(getApplication(), "HomeLongitude", ""));
                    params1.put("latitude1", SharedPreferencesUtil.getString(getApplication(), "HomeLatitude", ""));
                    params1.put("type1", "1");
                    params1.put("sort1", position.get(0));

                    params1.put("returnFile2", returnImagelist.get(1));
                    params1.put("longtitude2", SharedPreferencesUtil.getString(getApplication(), "HomeLongitude", ""));
                    params1.put("latitude2", SharedPreferencesUtil.getString(getApplication(), "HomeLatitude", ""));
                    params1.put("type2", "1");
                    params1.put("sort2", position.get(1));

                    params1.put("returnFile3", returnImagelist.get(2));
                    params1.put("longtitude3", SharedPreferencesUtil.getString(getApplication(), "HomeLongitude", ""));
                    params1.put("latitude3", SharedPreferencesUtil.getString(getApplication(), "HomeLatitude", ""));
                    params1.put("type3", "0");
                    params1.put("sort3", position.get(2));
                } else {
                    params1.put("returnFile1", returnImagelist.get(0));
                    params1.put("longtitude1", SharedPreferencesUtil.getString(getApplication(), "HomeLongitude", ""));
                    params1.put("latitude1", SharedPreferencesUtil.getString(getApplication(), "HomeLatitude", ""));
                    params1.put("type1", "1");
                    params1.put("sort1", position.get(0));

                    params1.put("returnFile2", videoPath);
                    params1.put("longtitude2", SharedPreferencesUtil.getString(getApplication(), "HomeLongitude", ""));
                    params1.put("latitude2", SharedPreferencesUtil.getString(getApplication(), "HomeLatitude", ""));
                    params1.put("sort2", mVideoPosition);
                    params1.put("type2", "0");

                    params1.put("returnFile3", returnImagelist.get(1));
                    params1.put("longtitude3", SharedPreferencesUtil.getString(getApplication(), "HomeLongitude", ""));
                    params1.put("latitude3", SharedPreferencesUtil.getString(getApplication(), "HomeLatitude", ""));
                    params1.put("sort3", position.get(1));
                    params1.put("type3", "1");
                }
                SaveFilePathTaskAccessNetWork(url, params1);
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Log.e("error_msg", error_msg);
                Utils.getUtils().dismissDialog();
            }
        });
    }

    public void jump() {
        Utils.getUtils().dismissDialog();
        Intent intent = new Intent(this, TaskInfoSituation.class);
        intent.putExtra("taskid", taskid);
        startActivity(intent);
    }

    // 视频保存路径
    public boolean getVideoSavePath() {
        String mSDCardPath = SDCardUtils.getSDCardPath();
        mVidieoRcordPath = mSDCardPath + MyApplication.videosSaveUrl;
        boolean exists = FileUtils.createOrExistsDir(mVidieoRcordPath);
        Log.e("mSDCardPath", mSDCardPath);
        Log.e("mImageFilePath", mImageFilePath);
        Log.e("exists", exists + "");
        return exists;
    }


    public void CameraVideoRcord() {
        Intent intent = new Intent(this, MediaRecorderActivity.class);
        startActivityForResult(intent, VIDEO_CAPTURE_ONE);
    }

    public static Bitmap getVideoThumb2(String path) {
        return getVideoThumb2(path, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
    }

    public static Bitmap getVideoThumb2(String path, int kind) {
        return ThumbnailUtils.createVideoThumbnail(path, kind);
    }

    public String formatTime(long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        String strDay = day < 10 ? "0" + day : "" + day; //天
        String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
        String strSecond = second < 10 ? "0" + second : "" + second;//秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;//毫秒
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;
        mTakobtainCountdown.setTime(Integer.valueOf(strDay), Integer.valueOf(strHour), Integer.valueOf(strMinute), Integer.valueOf(strSecond));
        mTakobtainCountdown.start();
        return strMinute + " 分钟 " + strSecond + " 秒";
    }


    public void ExecTaskAccessNetWork(String url, Map<String, String> params) {
        MyOkHttp.get().post(this, url, params, new GsonResponseHandler<BaseBeans<ExecTaskInfo>>() {
            @Override
            public void onFailure(int statusCode, String error_msg) {
                Log.e("error_msg", error_msg);
            }

            @Override
            public void onSuccess(int statusCode, BaseBeans<ExecTaskInfo> response) {
                ExecTaskInfo info = response.getData();
                List<ExecTaskInfo.AnswerBean> answerinfo = info.getAnswer();
                if (null != answerinfo && answerinfo.size() > 0) {
                    EventBus.getDefault().postSticky(new EventMessage.MesageAnswer(answerinfo));
                }

                for (int i = 0; i < info.getTaskList().size(); i++) {
                    String mSort = info.getTaskList().get(i).getSort();
                    if (mSort.equals("4")) {
                        execinfo = info.getTaskList().get(i);
                    }

                    if (mSort.equals("5")) {
                        execlast = info.getTaskList().get(i);
                    }
                    if (mSort.equals("1")) {
                        mTaskObtainRequestOne.setText(info.getTaskList().get(i).getTaskRemark());//拍摄需求
                        ImageLoader.getInstance().displayImage(info.getTaskList().get(i).getExampleFile().get(0), mTaskobtainExampleImageOne, MyConfig.options);
                        if (info.getTaskList().get(i).getIsType().equals("0")) {
                            isVideo = true;
                            mVideoPosition = mSort;
                            mTaskobtainCameratwo.setVisibility(View.GONE);
                        } else if (info.getTaskList().get(i).getIsType().equals("1")) {
                            mTaskobtainCameratwo.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(info.getTaskList().get(i).getReturnfile().get(0), mImageshowOne, MyConfig.options);
                            position.add(mSort);
                        }
                    } else if (mSort.equals("2")) {
                        mTaskObtainRequestSecond.setText(info.getTaskList().get(i).getTaskRemark());//拍摄需求
                        ImageLoader.getInstance().displayImage(info.getTaskList().get(i).getExampleFile().get(0), mTaskobtainExampleImageSecond, MyConfig.options);
                        if (info.getTaskList().get(i).getIsType().equals("0")) {
                            isVideo = true;
                            mVideoPosition = mSort;
                            mTaskobtainCameraThree.setVisibility(View.GONE);
                            ImageLoader.getInstance().displayImage(info.getTaskList().get(i).getReturnfile().get(0), mShowImageView_two, MyConfig.options);
                        } else if (info.getTaskList().get(i).getIsType().equals("1")) {
                            ImageLoader.getInstance().displayImage(info.getTaskList().get(i).getReturnfile().get(0), mShowImageView_two, MyConfig.options);
                            position.add(mSort);
                        }
                    } else if (mSort.equals("3")) {
                        mTaskObtainRequestThree.setText(info.getTaskList().get(i).getTaskRemark());//拍摄需求
                        ImageLoader.getInstance().displayImage(info.getTaskList().get(i).getExampleFile().get(0), mTaskobtainExampleImageThree, MyConfig.options);
                        if (info.getTaskList().get(i).getIsType().equals("0")) {
                            isVideo = true;
                            mVideoPosition = mSort;
                            taskobtainCamera.setVisibility(View.GONE);
                        } else if (info.getTaskList().get(i).getIsType().equals("1")) {
                            ImageLoader.getInstance().displayImage(info.getTaskList().get(i).getReturnfile().get(0), mImageshowTwo, MyConfig.options);
                            position.add(mSort);
                        }
                    }
                }
            }
        });
    }

    public void SaveFilePathTaskAccessNetWork(String url, Map<String, String> params) {
        MyOkHttp.get().post(this, url, params, new GsonResponseHandler<BaseBeans>() {
            @Override
            public void onFailure(int statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(int statusCode, BaseBeans response) {
                String flag = response.getSuccess();
                Log.e("flag", response.getErrorMsg());
                Log.e("flag", flag + "");
                if (null != flag && "1".equals(flag)) {
                    MultimediaInfo multimediaInfo = new MultimediaInfo();
                    Intent intent = new Intent(TaskObtainEvidence.this, TaskInfoSituation.class);
                    Bundle bundle = new Bundle();
                    if (mUploadImagePathlist.size() > 0) {
                        multimediaInfo.setUploadImagePath(mUploadImagePathlist);
                    }

                    if (null != videoPath) {
                        //  bundle.putStringArrayList("videopath",videoPath);
                        multimediaInfo.setVideoPath(videopatlist.get(0));
                    }
                    if (marks.equals(ExecutingTaskFragment.EXECUTIONINGFRAGMENT)) {
                        bundle.putSerializable("param", execinfo);
                        bundle.putSerializable("lastevidence", execlast);
                        bundle.putString("marks", ExecutingTaskFragment.EXECUTIONINGFRAGMENT);
                    }
                    if (marks.equals(NonExecutionTaskFragment.NONEXECUTIONFRAGMENT)) {
                        bundle.putString("marks", NonExecutionTaskFragment.NONEXECUTIONFRAGMENT);
                    }

                    bundle.putString("taskid", taskid);

                    intent.putExtras(bundle);
                    EventBus.getDefault().postSticky(new EventMessage.MesageMultimediaInfo(multimediaInfo));
                    uploadfinish = 0;
                    modifyvideo = false;
                    startActivity(intent);
                }
            }
        });
    }
}
