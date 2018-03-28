package com.risenb.witness.ui.tasklist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.beans.BaseBeans;
import com.risenb.witness.beans.ExecTaskListInfo;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.GsonResponseHandler;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.ui.tasklist.adapter.ExecListViewCommconAdapter;
import com.risenb.witness.utils.StringUtils;
import com.risenb.witness.utils.newUtils.UIManager;
import com.risenb.witness.utils.newUtils.Utils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 执行中任务上刊状态
 */
public class ExecTaskIssueState extends BaseUI {
    @BindView(R.id.list_view)
    ListView mListView;
    //上一步
    @BindView(R.id.pre_step)
    Button preStep;
    //下一步
    @BindView(R.id.next_step)
    Button nextStep;
    //备注
    @BindView(R.id.remarks)
    EditText remarks;

    private String taskid;
    private ExecListViewCommconAdapter mAdapter;
    private String modeltype;
    private int page;
    private String selectstr;
    private ExecTaskListInfo info;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
         setTitle("执行中");
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
        modeltype =bundle.getString("modeltype");
        page =bundle.getInt("page");
        Map<String, String> params = new HashMap<>();
        params.put("taskId", taskid);
        params.put("c",MyApplication.getInstance().getC());
        params.put("modeltype",modeltype);
        params.put("page",String.valueOf(page));

        onNonTaskAccessNetWorkDetail(params);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("mAdapter",position+"");
                mAdapter.setSelectItem(position); // 记录当前选中的item
//                mAdapter.notifyDataSetInvalidated();    //更新UI界面
            }
        });

    }

    //单选数据
    public void onNonTaskAccessNetWorkDetail( Map<String, String> params) {
        String url = this.getResources().getString(R.string.service_host_address).concat(getString(R.string.UploadedDetails));
        if (StringUtils.isSpace(url)) {
            Toast.makeText(this, "网络路径无效", Toast.LENGTH_LONG).show();
            return;
        }

        Utils.getUtils().showProgressDialog(this);
        Log.e("taskid", taskid + "");
        MyOkHttp.get().post(this, url, params, new GsonResponseHandler<BaseBeans<ExecTaskListInfo>>() {
            @Override
            public void onSuccess(int statusCode, BaseBeans<ExecTaskListInfo> response) {
               // TaskInfoDetails info = response.getData();
                info = response.getData();
                Log.e("TaskInfoDetails", info.toString());
                if(null!= info){

                    //图片和视频 数据
                   // mAdapter = new ListViewCommconAdapter(TaskIssueState.this,info.getTaskList().get(0).getExampleFile(),info.getTaskList().get(0).getIsType());
                    mAdapter = new ExecListViewCommconAdapter(ExecTaskIssueState.this, info.getTaskList(),2, info.getTaskList().get(0).getExampleFile(), info.getTaskList().get(0).getIsType());
                    mListView.setAdapter(mAdapter);
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            mAdapter.setSelectItem(position); // 记录当前选中的item
                            mAdapter.notifyDataSetInvalidated();    //更新UI界面
                        }
                    });
                    //获取未执行提交的上刊状态并选中
                    if(!info.getTaskList().get(0).getReturnfile().isEmpty()){
                    String selectstr=  info.getTaskList().get(0).getReturnfile().get(0);
                    if(null!=selectstr&&!"0".equals(selectstr)){
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

    @OnClick(R.id.pre_step)
    public void PreStep(){
        finish();
    }

    @OnClick(R.id.next_step)
    public void NextStep(){
        selectstr = mAdapter.getSelectstr();
        if(null== selectstr){
            makeText("请选择上刊状态");
           return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("taskid", taskid);
        params.put("c", MyApplication.getInstance().getC());
        params.put("modeltype",modeltype);
        params.put("page",String.valueOf(page));
        params.put("issuestate", selectstr);

        onNonTaskSaveNetWorkDetail(params);
    }

    private void onNonTaskSaveNetWorkDetail( Map<String, String> params) {
        String url = this.getResources().getString(R.string.service_host_address).concat(getString(R.string.saveTaskfilepath));
        if (StringUtils.isSpace(url)) {
            Toast.makeText(this, "网络路径无效", Toast.LENGTH_LONG).show();
            return;
        }


        Utils.getUtils().showProgressDialog(this);
        Log.e("taskid", taskid + "");
        MyOkHttp.get().post(this, url, params, new GsonResponseHandler<BaseBeans>() {
            @Override
            public void onSuccess(int statusCode, BaseBeans response) {
                String success = response.getSuccess();
                Log.e("success",success);
                if("1".equals(success)){
                    Utils.getUtils().dismissDialog();
                    //数据提交成功跳转
                    Intent intent = new Intent(ExecTaskIssueState.this, ExecLastTaskSaveAndUpLoad.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("modeltype", modeltype);
                    bundle.putString("taskid",taskid);
                    bundle.putString("issuestate",selectstr);
                    bundle.putInt("page",++page);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    --page;
                    UIManager.getInstance().pushActivity(ExecTaskIssueState.this);
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }
}
