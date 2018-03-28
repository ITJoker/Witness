package com.risenb.witness.ui.tasklist;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.beans.BaseBeans;
import com.risenb.witness.beans.ExecTaskListInfo;
import com.risenb.witness.beans.UploadFileBean;
import com.risenb.witness.beans.UploadImageBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.GsonResponseHandler;
import com.risenb.witness.network.OkHttpUtils.response.RawResponseHandler;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.ui.tasklist.adapter.UncertainExecListViewCommconAdapter;
import com.risenb.witness.ui.tasklist.fragment.ExecRecentlyDistanceFragment;
import com.risenb.witness.utils.SDCardUtils;
import com.risenb.witness.utils.SharedPreferencesUtil;
import com.risenb.witness.utils.StringUtils;
import com.risenb.witness.utils.newUtils.UIManager;
import com.risenb.witness.utils.newUtils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.risenb.witness.ui.tasklist.UncertainExecEvidence.tempSelectBitmap;

/**
 * 执行中任务上刊状态
 */
public class UncertainExecTaskIssueState extends BaseUI {
    @BindView(R.id.list_view)
    ListView mListView;
    //备注
    @BindView(R.id.remarks)
    EditText remarks;

    private String taskid;
    private String modeltype;
    private int page;
    private String selectstr;
    private String isType;
    private UncertainExecListViewCommconAdapter mAdapter;

    //服务器返回图片路径
    private List<String> returnfile = new ArrayList<>();
    //图片和视频全部上传完毕
    private int allLoadfinish = 0;
    private boolean uploadTask = false;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void exit() {
        back();
    }

    @Override
    protected void setControlBasis() {
        setTitle("选择上刊状态");
    }

    @Override
    protected void prepareData() {

    }

    @Override
    public void onLoadOver() {

    }

    @Override
    protected void onInitCreate(Bundle savedInstanceState) {
        setContentView(R.layout.taskissuestate);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        taskid = bundle.getString("taskid");
        modeltype = bundle.getString("modeltype");
        isType = bundle.getString("isType");
        //当前第几页
        page = bundle.getInt("page");
        Map<String, String> params = new HashMap<>();
        params.put("taskId", taskid);
        params.put("c", MyApplication.getInstance().getC());
        params.put("modeltype", modeltype);
        params.put("page", String.valueOf(page));
        onNonTaskAccessNetWorkDetail(params);
    }

    //单选数据
    public void onNonTaskAccessNetWorkDetail(Map<String, String> params) {
        String url = this.getResources().getString(R.string.service_host_address).concat(getString(R.string.myTaskDetails));
        if (StringUtils.isSpace(url)) {
            Toast.makeText(this, "网络路径无效", Toast.LENGTH_LONG).show();
            return;
        }
        Utils.getUtils().showProgressDialog(this);
        MyOkHttp.get().post(this, url, params, new GsonResponseHandler<BaseBeans<ExecTaskListInfo>>() {
            @Override
            public void onSuccess(int statusCode, BaseBeans<ExecTaskListInfo> response) {
                ExecTaskListInfo info = response.getData();
                if (info != null) {
                    //图片和视频数据
                    mAdapter = new UncertainExecListViewCommconAdapter(UncertainExecTaskIssueState.this, info.getTaskList(), 2, info.getTaskList().get(0).getExampleFile(), info.getTaskList().get(0).getIsType(), info.getTaskid());
                    mListView.setAdapter(mAdapter);
                    mListView.measure(0, View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST));
                    ViewGroup.LayoutParams layoutParams = mListView.getLayoutParams();
                    layoutParams.height = mListView.getMeasuredHeight();
                    mListView.setLayoutParams(layoutParams);
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //记录当前选中的item
                            mAdapter.setSelectItem(position);
                            //更新UI界面
                            mAdapter.notifyDataSetInvalidated();
                        }
                    });
                    //获取未执行 提交的上刊状态 并选中
                    if (!info.getTaskList().get(0).getReturnfile().isEmpty()) {
                        String selectstr = info.getTaskList().get(0).getReturnfile().get(0);
                        if (null != selectstr && !"0".equals(selectstr)) {
                            mAdapter.setselectstr(selectstr);
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

    /**
     * 保存任务
     */
    @OnClick(R.id.pre_step)
    public void saveTaskOnClick() {
        uploadTask = false;
        submitData();
    }

    /**
     * 上传任务
     */
    @OnClick(R.id.next_step)
    public void uploadTaskOnClick() {
        uploadTask = true;
        submitData();
    }

    public void submitData() {
        try {
            selectstr = mAdapter.getSelectstr();
            if (TextUtils.isEmpty(selectstr)) {
                makeText("请选择上刊状态");
                return;
            }
            Map<String, String> params = new HashMap<>();
            params.put("taskid", taskid);
            params.put("c", MyApplication.getInstance().getC());
            params.put("modeltype", modeltype);
            params.put("page", String.valueOf(page));
            params.put("issuestate", selectstr);
            params.put("remark", remarks.getText().toString().trim());
            onNonTaskSaveNetWorkDetail(params);
        } catch (Exception e) {
            makeText("请选择上刊状态");
        }
    }

    private void onNonTaskSaveNetWorkDetail(Map<String, String> params) {
        String url = this.getResources().getString(R.string.service_host_address).concat(getString(R.string.saveTaskfilepath));
        if (StringUtils.isSpace(url)) {
            Toast.makeText(this, "网络路径无效", Toast.LENGTH_LONG).show();
            return;
        }
        Utils.getUtils().showProgressDialog(this);
        MyOkHttp.get().post(this, url, params, new GsonResponseHandler<BaseBeans>() {
            @Override
            public void onSuccess(int statusCode, BaseBeans response) {
                Utils.getUtils().dismissDialog();
                if ("1".equals(response.getSuccess())) {
                    //上传任务
                    if (uploadTask) {
                        UploadTask();
                    } else {
                        UploadHttpUrlAndSort();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }

    public void UploadTask() {
        if ("0".equals(isType)) {
            // 说明需要上传视频
            // UploadVideo();
        } else if ("1".equals(isType)) {
            // 说明需要上传照片
            String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.pictureUpload));
            Map<String, String> params = new HashMap<>();
            params.put("taskid", taskid);
            params.put("sort", UncertainExecEvidence.sort);
            Map<String, File> files = new HashMap<>();
            for (int i = 0; i < tempSelectBitmap.size(); i++) {
                // 只有经过压缩的照片才可以被上传
                if (tempSelectBitmap.get(i).isSelected()) {
                    File file = new File(tempSelectBitmap.get(i).getImagePath());
                    files.put("file", file);
                    UploadImage(url, params, files);
                }
            }
        }
    }

    public synchronized void UploadImage(String url, Map<String, String> params, final Map<String, File> files) {
        Utils.getUtils().showProgressDialog(this, "上传中");
        MyOkHttp.get().upload(this, url, params, files, new RawResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                Gson gson = new Gson();
                UploadImageBean info = gson.fromJson(response, UploadImageBean.class);
                String uploadImagePath = info.getData().getFile_url();
                returnfile.add(uploadImagePath);
                ++allLoadfinish;
                if (allLoadfinish == tempSelectBitmap.size()) {
                    Utils.getUtils().dismissDialog();
                    UploadHttpUrlAndSort();
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }

    public void UploadHttpUrlAndSort() {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.saveTaskfilepath));
        List<UploadFileBean> uploadfileBeanList = new ArrayList<>();
        Map<String, String> params = new HashMap<>();
        StringBuilder stringBuilder = new StringBuilder();
        params.put("taskid", taskid);
        params.put("c", MyApplication.getInstance().getC());
        String photoTime = SharedPreferencesUtil.getString(getApplication(), taskid.concat("PhotoTime"), "NoTime");
        params.put("phototime", photoTime);
        for (int i = 0; i < returnfile.size(); i++) {
            String path = returnfile.get(i);
            if (i != returnfile.size() - 1) {
                stringBuilder.append(path + ",");
            } else {
                stringBuilder.append(path);
            }
        }
        UploadFileBean uploadfileBean = new UploadFileBean();
        uploadfileBean.setReturnfile(stringBuilder.toString());
        if (isType.equals("1")) {
            // 说明文件类型为照片
            uploadfileBean.setType("1");
        } else if (isType.equals("0")) {
            // 说明文件类型为视频
            uploadfileBean.setType("0");
        }
        String latitude = SharedPreferencesUtil.getString(getApplication(), taskid + "Latitude", "");
        String longitude = SharedPreferencesUtil.getString(getApplication(), taskid + "Longitude", "");
        uploadfileBean.setLatitude(latitude);
        uploadfileBean.setLongtitude(longitude);
        uploadfileBean.setSort(UncertainExecEvidence.sort);
        uploadfileBeanList.add(uploadfileBean);
        BaseBeans<List<UploadFileBean>> baseBens = new BaseBeans<>();
        baseBens.setData(uploadfileBeanList);
        String jsonString = JSON.toJSONString(baseBens);
        params.put("taskJson", jsonString);
        SaveFilePathTaskAccessNetWork(url, params);
    }

    private void SaveFilePathTaskAccessNetWork(String url, Map<String, String> params) {
        String text;
        if (uploadTask) {
            text = "上传中";
        } else {
            text = "保存中";
        }
        Utils.getUtils().showProgressDialog(this, text);
        MyOkHttp.get().post(this, url, params, new GsonResponseHandler<BaseBeans>() {
            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
                makeText("网络错误");
            }

            @Override
            public void onSuccess(int statusCode, BaseBeans response) {
                Utils.getUtils().dismissDialog();
                if ("1".equals(response.getSuccess())) {
                    if (!uploadTask) {
                        ExecRecentlyDistanceFragment.isNetRefresh = true;
                        // 任务未上传调用此方法显示保存成功
                        makeText("保存成功");
                    }
                    allLoadfinish = 0;
                    if (uploadTask) {
                        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.uploadTask));
                        Map<String, String> params = new HashMap<>();
                        params.put("taskid", taskid);
                        params.put("c", MyApplication.getInstance().getC());
                        UploadTaskAccessNetWork(url, params);
                    } else {
                        UIManager.getInstance().popActivity(UncertainExecTaskIssueState.class);
                        UIManager.getInstance().popActivity(UncertainExecEvidence.class);
                    }
                }
            }
        });
    }

    private void UploadTaskAccessNetWork(String url, Map<String, String> params) {
        String text;
        if (uploadTask) {
            text = "上传中";
        } else {
            text = "保存中";
        }
        Utils.getUtils().showProgressDialog(this, text);
        MyOkHttp.get().post(this, url, params, new GsonResponseHandler<BaseBeans>() {
            @Override
            public void onFailure(int statusCode, String error_msg) {
                makeText("网络错误");
                Utils.getUtils().dismissDialog();
            }

            @Override
            public void onSuccess(int statusCode, BaseBeans response) {
                if ("1".equals(response.getSuccess())) {
                    Utils.getUtils().dismissDialog();
                    makeText("任务上传成功");
                    ExecRecentlyDistanceFragment.isNetRefresh = true;
                    // Utils.getUtils().dismissDialog();
                    // 被驳回转为已上传，控制是否可以进入被驳回页面，在新线程中保存容易造成不同步，无法再任务详情页获取到数据
                    SharedPreferencesUtil.saveBoolean(getApplication(), taskid + "Reject", false);
                    // 已上传控制任务详情页关闭
                    TaskListInfoUI.isClose = true;
                    allLoadfinish = 0;
                    UIManager.getInstance().popActivity(UncertainExecTaskIssueState.class);
                    UIManager.getInstance().popActivity(UncertainExecEvidence.class);
                    /*
                     * 删除本地文件
                     */
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            deletePicture(SDCardUtils.getSDCardPath() + MyApplication.photosCompressSaveUrl + taskid);
                            deletePicture(SDCardUtils.getSDCardPath() + MyApplication.photosSaveUrl + taskid);
                        }
                    }).start();
                }
            }
        });
    }

    public static void deletePicture(String taskIDPath) {
        while (true) {
            File dir = new File(taskIDPath);
            if (dir.exists()) {
                /*
                 * 路径正确,遍历该文件夹下的所有文件并删除
                 */
                File[] subFiles = dir.listFiles();
                for (File subFile : subFiles) {
                    // 是文件则说明是照片
                    if (subFile.isFile()) {
                        subFile.delete();
                    }
                }
            } else if (dir.isFile()) {
                // makeText("此路径为文件路径");
                return;
            } else {
                // makeText("无效路径");
                return;
            }
            dir.delete();
            return;
        }
    }
}
