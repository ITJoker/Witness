package com.risenb.witness.ui.tasklist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.beans.BaseBeans;
import com.risenb.witness.beans.ExecTaskInfo;
import com.risenb.witness.beans.MultimediaInfo;
import com.risenb.witness.beans.TaskInfoDetails;
import com.risenb.witness.beans.TaskProblemBean;
import com.risenb.witness.beans.UploadImageBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.GsonResponseHandler;
import com.risenb.witness.network.OkHttpUtils.response.RawResponseHandler;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.ui.tasklist.adapter.ListparentsAdapter;
import com.risenb.witness.ui.tasklist.fragment.ExecutingTaskFragment;
import com.risenb.witness.ui.tasklist.fragment.NonExecutionTaskFragment;
import com.risenb.witness.utils.ConvertUtils;
import com.risenb.witness.utils.FileUtils;
import com.risenb.witness.utils.MyConfig;
import com.risenb.witness.utils.SDCardUtils;
import com.risenb.witness.utils.SharedPreferencesUtil;
import com.risenb.witness.utils.StringUtils;
import com.risenb.witness.utils.newUtils.UIManager;
import com.risenb.witness.utils.newUtils.Utils;
import com.risenb.witness.views.newViews.MyListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LastObtainEvidence extends BaseUI {
    public static final int SYSTEM_TAKE_PHOTH_LAST = 10115;
    public static final int SYSTEM_IMAGE_CROP_LAST = 10116;

    @BindView(R.id.list_parent_MyListView)
    MyListView mListView;
    @BindView(R.id.linearLayout_last_image)
    RelativeLayout mRelativeLayoutImage;
    //示例图片
    @BindView(R.id.last_image_example)
    ImageView mImageViewExample;
    @BindView(R.id.tasklastobtain_camera)
    Button TmLastakeCamera;

    @BindView(R.id.obtainEvidence_last_image)
    ImageView mLastShowImage;

    @BindView(R.id.task_save_info)
    Button mSave_Task;

    private String mImageFileName;
    private String mImageFilePath;
    private Uri cropUri = null;

    private String mVidieoRcordName;
    private String mVidieoRcordPath;

    private TaskInfoDetails.TaskListBean mlastevidenceinfos;
    private ExecTaskInfo.TaskListBean execlast;

    private String situation;
    private String taskid;
    private StringBuilder textstr;
    private ListparentsAdapter mAdapter;
    private List<TaskProblemBean> data;

    private String mRadioStr;
    private String mRadioStrid;
    private String mCheckBoxStr;
    private String mCheckBoxStrStrid;
    private String mEditStr;
    private String mEditStrid = "";
    private String ImagePath;
    private MultimediaInfo mMultimediaInfo;

    private String marks;
    private String mExampleIMage;
    private String Returnfile;
    private List<ExecTaskInfo.AnswerBean> answerinfo;
    private HashMap<Integer, Boolean> isSelected;
    private String isSelectedId;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        setContentView(R.layout.lastobtainevidence);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        //taskid = getIntent().getStringExtra("taskid");
        Bundle bundle = getIntent().getExtras();
        textstr = new StringBuilder();
        taskid = bundle.getString("taskid");
//        situation=bundle.getString("situation");
        marks = bundle.getString("marks");
        if (ExecutingTaskFragment.EXECUTIONINGFRAGMENT.equals(marks)) {
            execlast = (ExecTaskInfo.TaskListBean) bundle.getSerializable("lastevidence");//最后取证信息
            Log.e("execlast", execlast.toString());
            mExampleIMage = execlast.getExampleFile().get(0);//示例图片
            Returnfile = execlast.getReturnfile().get(0);
        }
        if (NonExecutionTaskFragment.NONEXECUTIONFRAGMENT.equals(bundle.getString(marks))) {
            mlastevidenceinfos = (TaskInfoDetails.TaskListBean) bundle.getSerializable("lastevidence");//最后取证信息
            // mExampleIMage=  mlastevidenceinfos.getExampleFile().get(0);//示例图片
        }
        if (null != mExampleIMage) {
            ImageLoader.getInstance().displayImage(mExampleIMage, mImageViewExample, MyConfig.options);
        }
        if (null != mLastShowImage) {
            mRelativeLayoutImage.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(Returnfile, mLastShowImage, MyConfig.options);
        }
    }

    @Override
    protected void setControlBasis() {

    }

    @Override
    protected void prepareData() {
//        String mNonTaskId =getIntent().getStringExtra(NonExecutionTaskFragment.NONEXECUTIONFRAGMENTTASKID);
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.problem));
        Map<String, String> param = new HashMap<>(2);
        param.put("c", "3c2d4defd9c22732fe960c48f6ac5436");
        param.put("taskId", taskid);
        onNonTaskAccessNetWorkDetail(url, param);
    }

    public void onNonTaskAccessNetWorkDetail(String url, Map<String, String> param) {
        if (StringUtils.isSpace(url)) {
            Toast.makeText(this, "网络路径无效", Toast.LENGTH_LONG).show();
            return;
        }
        Utils.getUtils().showProgressDialog(this);
        MyOkHttp.get().post(this, url, param, new GsonResponseHandler<BaseBeans<List<TaskProblemBean>>>() {
            @Override
            public void onSuccess(int statusCode, BaseBeans<List<TaskProblemBean>> response) {
                data = response.getData();
                Log.e("DataBean", data.toString());
                mAdapter = new ListparentsAdapter(data, getActivity());
                mListView.setAdapter(mAdapter);
                if (null != answerinfo) {
                    mAdapter.setList(answerinfo);
                }
                Utils.getUtils().dismissDialog();
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }

    @Override
    public void onLoadOver() {

    }

    @OnClick(R.id.tasklastobtain_camera)
    public void OnClickTakeCameraPhoto(View view) {
        TaskObtainEvidenceTakePhoto();
    }

    @OnClick(R.id.task_save_info)
    public void OnClickSaveTask() {
        uploadimage();
        //onSaveTaskAccessNetWork();
    }

    public void TaskObtainEvidenceTakePhoto() {
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
            startActivityForResult(intent, SYSTEM_TAKE_PHOTH_LAST);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveMsgMainThread(EventMessage.Mesage event) {
        // Toast.makeText(this,event.message,Toast.LENGTH_SHORT).show();
        mRadioStr = event.message;
        mRadioStrid = event.id;
        //Log.e("Mesage",event.message);
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
            Log.e("mEditStrid", mEditStrid);
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveMsgMainThread(EventMessage.MesageCheckBox event) {
        // Toast.makeText(this,event.message,Toast.LENGTH_SHORT).show();
        if (null != event.isSelected) {
            isSelected = event.isSelected;
            isSelectedId = event.id;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void receiveMsgMainThread(EventMessage.MesageMultimediaInfo event) {
        // Toast.makeText(this,event.message,Toast.LENGTH_SHORT).show();
        mMultimediaInfo = event.info;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void receiveMsgMainThread(EventMessage.MesageAnswer event) {
        answerinfo = event.info;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SYSTEM_TAKE_PHOTH_LAST:
                takephotoresult();
                break;
            case SYSTEM_IMAGE_CROP_LAST:
                onCropImageResult();
                break;
        }
    }

    public void takephotoresult() {
        File file = new File(mImageFilePath + mImageFileName);
        Uri uri = Uri.fromFile(file);
        String uCorpImageName = "Corp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        File corpFile = new File(mImageFilePath + uCorpImageName);
        cropUri = Uri.fromFile(corpFile);
        int height = ConvertUtils.dp2px(this, 300);
        cropImageUri(uri, 500, 500, cropUri);
    }

    private void cropImageUri(Uri uri, int outputX, int outputY, Uri cropuri) {
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
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, SYSTEM_IMAGE_CROP_LAST);
    }


    public void onCropImageResult() {
        if (cropUri != null) {
            Bitmap dBitmap = BitmapFactory.decodeFile(cropUri.getPath());
            //Drawable drawable = new BitmapDrawable(dBitmap);
            Log.e("cropUri.getPath()", cropUri.getPath());
            mRelativeLayoutImage.setVisibility(View.VISIBLE);
            mLastShowImage.setImageBitmap(dBitmap);
            ImagePath = cropUri.getPath();
//            String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.pictureUpload));
//            Map<String, File> fileParam =new HashMap<>(2);
//            File file = new File(cropUri.getPath());
//            fileParam.put("fileone",file);
//            Map<String, String> params = new HashMap<>(1);
//            if(null==taskid){
//                makeText("无效任务id");
//                taskid="0";
//            }
//            params.put("taskid", taskid);
//            onLastNetWorkImageUpload(url,params,fileParam);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onSaveTaskAccessNetWork(String mImagePath) {
        List<String> mProblem = new ArrayList<>();
        List<Boolean> select = new ArrayList<>();
        List<Integer> position = new ArrayList<>();
//        HashMap<Integer, Boolean>   result=  mAdapter.getResult();
        // CheckBoxState resultState = mAdapter.getCheckBoxStateresult();
        if (null != isSelected) {
            //  HashMap<Integer, Boolean> result = isSelected.getIsSelected();
            Iterator<Map.Entry<Integer, Boolean>> iterator = isSelected.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, Boolean> entry = iterator.next();

                boolean value = entry.getValue();
                if (value) {
                    select.add(value);
                    position.add(entry.getKey());
                }

                // Log.e("HashMap",entry.getKey()+"");
                Log.e("HashMap", entry.getKey() + "-----" + entry.getValue() + "");
            }
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getType().equals("2")) {
                    mProblem = data.get(i).getProblem();
                }
                Log.e("mProblem", mProblem.toString());
            }
        }
        for (int k = 0; k < position.size(); k++) {
            Log.e("多选问题", mProblem.get(position.get(k)));
            if (k == position.size() - 1) {
                textstr.append(mProblem.get(position.get(k)));
            } else {
                textstr.append(mProblem.get(position.get(k)) + ",");
            }
        }

        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.saveTask));
        Map<String, String> param = new HashMap<>();
        param.put("c", application.getC());
        param.put("taskid", taskid);
        if (null != mImagePath) {
            param.put("returnFile", mImagePath);
        }

        param.put("longitude", SharedPreferencesUtil.getString(getApplication(), "HomeLongitude", ""));
        param.put("latitude", SharedPreferencesUtil.getString(getApplication(), "HomeLatitude", ""));
        param.put("type", "1");
        param.put("sort", "5");

        // 多选
        if (null == isSelected) {
            param.put("testid1", "0");
            param.put("answer1", "0");
        } else {
            param.put("testid1", isSelectedId);
            if (null != textstr) {
                Log.e("answer1", textstr.toString());
                param.put("answer1", textstr.toString());
            }
        }

        //填空
        if ("".equals(mEditStrid)) {
            param.put("testid2", "0");
            param.put("answer2", "0");
        } else {
            param.put("testid2", mEditStrid);
            param.put("answer2", mEditStr);
            Log.e("testid2", mEditStrid);
            Log.e("answer2", mEditStr);
        }
        //单选
        if (null == mRadioStrid) {
            param.put("testid3", "0");
            param.put("answer3", "0");
        } else {
            param.put("testid3", mRadioStrid);
            param.put("answer3", mRadioStr);
            Log.e("testid3", mRadioStrid);
            Log.e("answer3", mRadioStr);
        }

        if (StringUtils.isSpace(url)) {

            Toast.makeText(this, "网络路径无效", Toast.LENGTH_LONG).show();
            return;
        }
        Utils.getUtils().showProgressDialog(this);

        MyOkHttp.get().post(this, url, param, new GsonResponseHandler<BaseBeans>() {
            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }

            @Override
            public void onSuccess(int statusCode, BaseBeans response) {
                String data = response.getSuccess();
                if ("1".equals(data)) {
                    UIManager.getInstance().popActivity(TaskObtainEvidence.class);
                    UIManager.getInstance().popActivity(TaskInfoSituation.class);
                    finish();
                } else {
                    makeText("错误");
                }
                Utils.getUtils().dismissDialog();
            }
        });
        textstr.delete(0, textstr.length());
    }

    public void uploadimage() {
        String videourl = getResources().getString(R.string.service_host_address).concat(getString(R.string.pictureUpload));
        Map<String, File> filevideo = new HashMap<>();
        if (null != ImagePath) {
            File file = new File(ImagePath);
            filevideo.put("lastimage", file);
            Map<String, String> params1 = new HashMap<>();
            params1.put("taskid", taskid);
            MyOkHttp.get().upload(this, videourl, params1, filevideo, new RawResponseHandler() {

                @Override
                public void onFailure(int statusCode, String error_msg) {

                }

                @Override
                public void onSuccess(int statusCode, String response) {
                    Gson gson = new Gson();
                    UploadImageBean info = gson.fromJson(response, UploadImageBean.class);
                    String uploadImagePath = info.getData().getFile_url();
                    Log.e("uploadImagePath", uploadImagePath);
                    onSaveTaskAccessNetWork(uploadImagePath);
                }
            });
        } else {
            onSaveTaskAccessNetWork(null);
        }
    }

}
