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
import com.risenb.witness.beans.LastTaskBean;
import com.risenb.witness.beans.UploadFileBean;
import com.risenb.witness.beans.UploadImageBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.GsonResponseHandler;
import com.risenb.witness.network.OkHttpUtils.response.RawResponseHandler;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.ui.tasklist.adapter.UncertainTaskIssueStateAdapter;
import com.risenb.witness.ui.tasklist.fragment.RecentlyDistanceFragment;
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

import static com.risenb.witness.ui.tasklist.UncertainEvidence.tempSelectBitmap;

/**
 * 未执行任务上刊状态
 */
public class UncertainTaskIssueState extends BaseUI {
    @BindView(R.id.list_view)
    ListView mListView;
    //备注
    @BindView(R.id.remarks)
    EditText remarks;

    private String taskid;
    //这个是 未执行 上刊状态 的 Activity 的Adapter
    private UncertainTaskIssueStateAdapter mAdapter;
    private String modeltype;
    private int page;
    private String isType;

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
        //当前第几页
        page = bundle.getInt("page");
        isType = bundle.getString("isType");
        Map<String, String> params = new HashMap<>();
        params.put("taskId", taskid);
        params.put("c", MyApplication.getInstance().getC());
        params.put("modeltype", modeltype);
        params.put("page", String.valueOf(page));
        onNonTaskAccessNetWorkDetail(params);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 记录当前选中的item
                mAdapter.setSelectItem(position);
            }
        });
    }

    //上刊状态单选项数据
    public void onNonTaskAccessNetWorkDetail(Map<String, String> params) {
        String url = this.getResources().getString(R.string.service_host_address).concat(getString(R.string.myTaskDetails));
        if (StringUtils.isSpace(url)) {
            Toast.makeText(this, "网络路径无效", Toast.LENGTH_LONG).show();
            return;
        }
        Utils.getUtils().showProgressDialog(this);
        MyOkHttp.get().post(this, url, params, new GsonResponseHandler<BaseBeans<LastTaskBean>>() {
            @Override
            public void onSuccess(int statusCode, BaseBeans<LastTaskBean> response) {
                Utils.getUtils().dismissDialog();
                LastTaskBean info = response.getData();
                if (null != info) {
                    //图片和视频数据
                    mAdapter = new UncertainTaskIssueStateAdapter(UncertainTaskIssueState.this, info.getTaskList(), 2, info.getTaskList().get(0).getExampleFile(), info.getTaskList().get(0).getIsType(), info.getTaskid());
                    mListView.setAdapter(mAdapter);
                    mListView.measure(0, View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST));
                    ViewGroup.LayoutParams layoutParams = mListView.getLayoutParams();
                    layoutParams.height = mListView.getMeasuredHeight();
                    mListView.setLayoutParams(layoutParams);
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }

    @OnClick(R.id.pre_step)
    public void SaveTaskOnClick(View view) {
        uploadTask = false;
        submitData();
    }

    @OnClick(R.id.next_step)
    public void uploadTaskOnClick() {
        uploadTask = true;
        submitData();
    }

    public void submitData() {
        String selectStr = mAdapter.getSelectstr();
        if (TextUtils.isEmpty(selectStr)) {
            makeText("请选择上刊状态");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("taskid", taskid);
        params.put("c", MyApplication.getInstance().getC());
        params.put("issuestate", selectStr);
        params.put("remark", remarks.getText().toString().trim());
        onNonTaskSaveNetWorkDetail(params);
    }

    private void onNonTaskSaveNetWorkDetail(Map<String, String> params) {
        String url = this.getResources().getString(R.string.service_host_address).concat(getString(R.string.saveTaskfilepath));
        if (StringUtils.isSpace(url)) {
            makeText("无效路径");
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
        if (isType.equals("0")) {
            // 说明需要上传视频
            // UploadVideo();
        } else if (isType.equals("1")) {
            // 说明需要上传照片
            String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.pictureUpload));
            Map<String, String> params = new HashMap<>();
            params.put("taskid", taskid);
            params.put("sort", UncertainEvidence.sort);
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
                    // 说明图片已经全部上传
                    Utils.getUtils().dismissDialog();
                    UploadHttpUrlAndSort();
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                makeText("传输中断，请点击继续上传");
                Utils.getUtils().dismissDialog();
            }
        });
    }

    public void UploadHttpUrlAndSort() {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.saveTaskfilepath));
        List<UploadFileBean> uploadfileBeanList = new ArrayList<>();
        Map<String, String> params1 = new HashMap<>();
        StringBuilder stringBuilder = new StringBuilder();
        params1.put("taskid", taskid);
        params1.put("c", MyApplication.getInstance().getC());
        String photoTime = SharedPreferencesUtil.getString(getApplication(), taskid.concat("PhotoTime"), "NoTime");
        params1.put("phototime", photoTime);
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
        uploadfileBean.setSort(UncertainEvidence.sort);
        uploadfileBeanList.add(uploadfileBean);
        BaseBeans<List<UploadFileBean>> baseBens = new BaseBeans<>();
        baseBens.setData(uploadfileBeanList);
        String jsontsr = JSON.toJSONString(baseBens);
        params1.put("taskJson", jsontsr);
        SaveFilePathTaskAccessNetWork(url, params1);
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
                    RecentlyDistanceFragment.isNetRefresh = true;
                    if (!uploadTask) {
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
                        UIManager.getInstance().popActivity(UncertainTaskIssueState.class);
                        UIManager.getInstance().popActivity(UncertainEvidence.class);
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
                Utils.getUtils().dismissDialog();
                makeText("网络错误");
            }

            @Override
            public void onSuccess(int statusCode, BaseBeans response) {
                String success = response.getSuccess();
                if ("1".equals(success)) {
                    // 已上传控制任务详情页关闭
                    TaskListInfoUI.isClose = true;
                    Utils.getUtils().dismissDialog();
                    makeText("任务上传成功");
                    allLoadfinish = 0;
                    finish();
                    UIManager.getInstance().popActivity(UncertainEvidence.class);
                    UIManager.getInstance().popActivity(UncertainTaskIssueState.class);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MyApplication.deletePicture(SDCardUtils.getSDCardPath() + MyApplication.photosCompressSaveUrl + taskid);
                            MyApplication.deletePicture(SDCardUtils.getSDCardPath() + MyApplication.photosSaveUrl + taskid);
                        }
                    }).start();
                }
            }
        });
    }
}
