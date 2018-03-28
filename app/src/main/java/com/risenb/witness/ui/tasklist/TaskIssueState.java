package com.risenb.witness.ui.tasklist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.beans.BaseBeans;
import com.risenb.witness.beans.LastTaskBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.GsonResponseHandler;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.ui.tasklist.adapter.TaskIssueStateAdapter;
import com.risenb.witness.utils.StringUtils;
import com.risenb.witness.utils.newUtils.UIManager;
import com.risenb.witness.utils.newUtils.Utils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 上刊状态
 */
public class TaskIssueState extends BaseUI {
    @BindView(R.id.list_view)
    ListView mListView;
    //上一步
    @BindView(R.id.pre_step)
    Button preStep;
    //下一步
    @BindView(R.id.next_step)
    Button nextStep;

    private String taskid;
    private TaskIssueStateAdapter mAdapter;//这个是 未取证 上刊状态 的 Activity 的Adapter
    private String selectstr;
    private int page;
    private String modeltype;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("上刊状态");
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
        page = bundle.getInt("page");
        modeltype = bundle.getString("modeltype");
        Map<String, String> params = new HashMap<>();
        params.put("taskId", taskid);
        params.put("c", MyApplication.getInstance().getC());
        params.put("modeltype", modeltype);
        params.put("page", String.valueOf(page));
        //params.put("")
        Log.e("taskid", taskid);
        Log.e("getC()", MyApplication.getInstance().getC());
        Log.e("modeltype", modeltype);
        Log.e("page", page + "");
        onNonTaskAccessNetWorkDetail(params);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // textsituation= textanwer.get(position);
                // Toast.makeText(TaskInfoSituation.this,textsituation,Toast.LENGTH_SHORT).show();
                Log.e("mAdapter", position + "");
                mAdapter.setSelectItem(position); // 记录当前选中的item
                // mAdapter.notifyDataSetInvalidated();    //更新UI界面
            }
        });
    }

    //单选数据
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
                // TaskInfoDetails info = response.getData();
                LastTaskBean info = response.getData();
                if (null != info) {
                    //图片和视频 数据
                    // mAdapter = new ListViewCommconAdapter(TaskIssueState.this,info.getTaskList().get(0).getExampleFile(),info.getTaskList().get(0).getIsType());
                    mAdapter = new TaskIssueStateAdapter(TaskIssueState.this, info.getTaskList(), 2, info.getTaskList().get(0).getExampleFile(), info.getTaskList().get(0).getIsType());
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

    @OnClick(R.id.pre_step)
    public void PreStep() {
        finish();
    }

    @OnClick(R.id.next_step)
    public void NextStep() {
        selectstr = mAdapter.getSelectstr();
        if (null == selectstr) {
            makeText("请选择上刊状态");
            return;
        }
        Log.e("selectstr", selectstr);
        Map<String, String> params = new HashMap<>();
        params.put("taskid", taskid);
        params.put("c", MyApplication.getInstance().getC());
        params.put("modeltype", "1");
        params.put("page", "2");
        params.put("issuestate", selectstr);
        onNonTaskSaveNetWorkDetail(params);
//        Intent intent = new Intent(TaskIssueState.this, LastTaskSaveAndUpLoad.class);
//        Bundle bundle=new Bundle();
//        bundle.putString("taskid",taskid);
//        intent.putExtras(bundle);
//        startActivity(intent);
    }

    private void onNonTaskSaveNetWorkDetail(Map<String, String> params) {
        String url = this.getResources().getString(R.string.service_host_address).concat(getString(R.string.saveTaskfilepath));
        if (StringUtils.isSpace(url)) {
            Toast.makeText(this, "网络路径无效", Toast.LENGTH_SHORT).show();
            return;
        }
        Utils.getUtils().showProgressDialog(this);
        Log.e("taskid", taskid + "");
        MyOkHttp.get().post(this, url, params, new GsonResponseHandler<BaseBeans>() {
            @Override
            public void onSuccess(int statusCode, BaseBeans response) {
                String success = response.getSuccess();
                Log.e("success", success);
                if ("1".equals(success)) {
                    Utils.getUtils().dismissDialog();
                    //数据提交成功跳转
                    Intent intent = new Intent(TaskIssueState.this, LastTaskSaveAndUpLoad.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("taskid", taskid);
                    bundle.putString("issuestate", selectstr);
                    bundle.putInt("page", ++page);
                    bundle.putString("modeltype", modeltype);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    --page;
                    UIManager.getInstance().pushActivity(TaskIssueState.this);
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }

}
