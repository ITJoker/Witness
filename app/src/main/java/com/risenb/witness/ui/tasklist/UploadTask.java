package com.risenb.witness.ui.tasklist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.beans.BatchUploadTaskBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.GsonResponseHandler;
import com.risenb.witness.service.BatchTaskUploadService;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.ui.tasklist.adapter.UploadTaskAdapter;

import com.risenb.witness.utils.SharedPreferencesUtil;
import com.risenb.witness.utils.StringUtils;
import com.risenb.witness.utils.newUtils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadTask extends BaseUI {
    @BindView(R.id.back)
    RelativeLayout back;

    @BindView(R.id.upload_task)
    ListView mListview;
    //全选框
    @BindView(R.id.all_selection)
    CheckBox checkBox;

    public interUpload listener;

    private UploadTaskAdapter adapter;

    //服务器拉取的任务数据
    private List<BatchUploadTaskBean.DataBean> dataBeen;

    private ArrayList<BatchUploadTaskBean.DataBean> batchImageUploadTaskList = new ArrayList<>();

    private boolean allSelectSign = false;

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
        setTitle("批量上传");
        leftVisible("全选");
        rightVisible("关闭");
        back.setVisibility(View.GONE);
    }

    @Override
    protected void prepareData() {

    }

    @Override
    public void onLoadOver() {

    }

    @Override
    protected void onInitCreate(Bundle savedInstanceState) {
        setContentView(R.layout.uploadtask);
        ButterKnife.bind(this);
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.myBatchTaskUpload));
        Map<String, String> params = new HashMap<>();
        params.put("c", MyApplication.getInstance().getC());
        params.put("pagecount", "1");
        params.put("pagesize", "10");
        params.put("longitude", SharedPreferencesUtil.getString(getApplication(), "HomeLongitude", ""));
        params.put("latitude", SharedPreferencesUtil.getString(getApplication(), "HomeLatitude", ""));
        /*params.put("type", "1");
        params.put("state", "1");*/
        onNonTaskAccessNetWorkDetail(url, params);
    }

    public void onNonTaskAccessNetWorkDetail(String url, Map<String, String> params) {
        if (StringUtils.isSpace(url)) {
            makeText("网络路径无效");
            return;
        }
        Utils.getUtils().showProgressDialog(this);
        MyOkHttp.get().post(this, url, params, new GsonResponseHandler<BatchUploadTaskBean>() {
            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }

            @Override
            public void onSuccess(int statusCode, BatchUploadTaskBean response) {
                Utils.getUtils().dismissDialog();
                dataBeen = response.getData();
                adapter = new UploadTaskAdapter(UploadTask.this, dataBeen);
                mListview.setAdapter(adapter);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        for (BatchUploadTaskBean.DataBean dataBean : dataBeen) {
                            dataBean.setCheckBox(buttonView.isChecked());
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }

    //全选
    @OnClick(R.id.tv_left)
    public void allSelected(View v) {
        if (allSelectSign) {
            allSelectSign = false;
        } else {
            allSelectSign = true;
        }
        for (BatchUploadTaskBean.DataBean dataBean : dataBeen) {
            dataBean.setCheckBox(allSelectSign);
        }
        adapter.notifyDataSetChanged();
    }

    //关闭
    @OnClick(R.id.tv_right)
    public void close(View v) {
        back();
    }

    //批量上传任务按钮
    @OnClick(R.id.upload_batchtask)
    public void OnClickBatchTask(View v) {
        //获得勾选的任务
        for (int i = 0; i < dataBeen.size(); i++) {
            if (dataBeen.get(i).isCheckBox()) {
                batchImageUploadTaskList.add(dataBeen.get(i));
            }
        }
        if (batchImageUploadTaskList.size() == 0) {
            makeText("请选择上传任务");
            return;
        }
        // 控制我的任务页在批量上传后关闭
        MyApplication.isTaskListScheduleFinish = true;
        Intent intent = new Intent(this, BatchTaskUploadService.class);
        intent.putParcelableArrayListExtra("batchImageUploadTaskList", batchImageUploadTaskList);
        startService(intent);
        finish();
    }

    public interUpload getListener() {
        return listener;
    }

    public void setListener(interUpload listener) {
        this.listener = listener;
    }
}
