package com.risenb.witness.ui.tasklist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.risenb.witness.R;
import com.risenb.witness.beans.BaseBeans;
import com.risenb.witness.beans.ExecTaskInfo;
import com.risenb.witness.beans.TaskInfoDetails;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.GsonResponseHandler;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.ui.tasklist.adapter.ListViewAdapter;
import com.risenb.witness.ui.tasklist.fragment.ExecutingTaskFragment;
import com.risenb.witness.ui.tasklist.fragment.NonExecutionTaskFragment;
import com.risenb.witness.utils.StringUtils;
import com.risenb.witness.utils.newUtils.UIManager;
import com.risenb.witness.utils.newUtils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaskInfoSituation extends BaseUI {
    @BindView(R.id.list_view)
    ListView mlistView;
    @BindView(R.id.pre_step)
    Button mTaskPreStep;
    @BindView(R.id.next_step)
    Button mTaskNextStep;

    private TaskInfoDetails.TaskListBean infos;
    private TaskInfoDetails.TaskListBean mlastevidenceinfos;

    private ExecTaskInfo.TaskListBean execinfo;//执行中上刊数据
    private ExecTaskInfo.TaskListBean execlast;
    private String taskid;
    private ListViewAdapter adapter;
    private List<String> textanwer;
    private String textsituation;
    private String marks;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        setContentView(R.layout.taskinfosituation);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        marks = bundle.getString("marks");
        taskid = bundle.getString("taskid");

        //入口 执行中 模版一
        if (ExecutingTaskFragment.EXECUTIONINGFRAGMENT.equals(marks)) {
            execinfo = (ExecTaskInfo.TaskListBean) bundle.getSerializable("param"); //上刊状态
            execlast = (ExecTaskInfo.TaskListBean) bundle.getSerializable("lastevidence");//最后取证信息
            if (null != execinfo) {
                textanwer = execinfo.getExampleFile();
                Log.e("infos", execinfo.toString());
            }
        }
        //入口 未执行 模版一
        if (NonExecutionTaskFragment.NONEXECUTIONFRAGMENT.equals(marks)) {
            infos = (TaskInfoDetails.TaskListBean) bundle.getSerializable("param"); //上刊状态
            mlastevidenceinfos = (TaskInfoDetails.TaskListBean) bundle.getSerializable("lastevidence");//最后取证信息
            if (null != infos) {
                // textanwer = infos.getExampleFile();
                Log.e("infos", infos.toString());
            }
        }
        //入口未执行 模版三
        if (UncertainEvidence.MODLETHREETASK.equals(marks)) {
            infos = (TaskInfoDetails.TaskListBean) bundle.getSerializable("param"); //上刊状态
            mlastevidenceinfos = (TaskInfoDetails.TaskListBean) bundle.getSerializable("lastevidence");//最后取证信息
            if (null != infos) {
                //textanwer = infos.getExampleFile();
                Log.e("infos", infos.toString());
            }
            //模版三
        }

        if (null != textanwer) {
            adapter = new ListViewAdapter(this, textanwer);
            mlistView.setAdapter(adapter);
        }

        if (null != execinfo) {
            adapter.setselectstr(execinfo.getExampleFile().get(2));
        }

        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //textsituation= textanwer.get(position);
                //  Toast.makeText(TaskInfoSituation.this,textsituation,Toast.LENGTH_SHORT).show();
                adapter.setSelectItem(position); // 记录当前选中的item
                adapter.notifyDataSetInvalidated();    //更新UI界面
            }
        });
        UIManager.getInstance().pushActivity(this);
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

    @OnClick(R.id.pre_step)
    public void OnClickmTaskPreStep(View view) {
        finish();
    }

    @OnClick(R.id.next_step)
    public void OnClickmTaskNextStep(View view) {
        textsituation = adapter.getSelectstr();
        Toast.makeText(TaskInfoSituation.this, textsituation, Toast.LENGTH_SHORT).show();
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.saveTaskIssueState));
        Map<String, String> param = new HashMap<>(2);
        param.put("c", "3c2d4defd9c22732fe960c48f6ac5436");
        if (null == taskid) {
            makeText("任务ID 为空");
            return;
        }
        if (null == textsituation) {
            makeText("请选择");
            return;
        }
        param.put("taskId", taskid);
        param.put("issuestate", textsituation);
        onNonTaskAccessNetWorkDetail(url, param);
        if (null != textsituation) {
            Intent intent = new Intent(this, LastObtainEvidence.class);
            Bundle bundle = new Bundle();
            if (marks.equals(ExecutingTaskFragment.EXECUTIONINGFRAGMENT)) {
                bundle.putSerializable("lastevidence", execlast);
                bundle.putString("marks", ExecutingTaskFragment.EXECUTIONINGFRAGMENT);
            }
            if (marks.equals(NonExecutionTaskFragment.NONEXECUTIONFRAGMENT)) {
                //  bundle.putSerializable("lastevidence", mlastevidenceinfos);
                bundle.putString("marks", NonExecutionTaskFragment.NONEXECUTIONFRAGMENT);
            }
            bundle.putString("taskid", taskid);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            Toast.makeText(TaskInfoSituation.this, "请选择上看状态", Toast.LENGTH_SHORT).show();
        }
    }

    public void onNonTaskAccessNetWorkDetail(String url, Map<String, String> param) {
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
                } else {
                    makeText("错误");
                }
                Utils.getUtils().dismissDialog();
            }
        });
    }
}
