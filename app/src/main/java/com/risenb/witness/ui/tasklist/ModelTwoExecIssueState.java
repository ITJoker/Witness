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
import com.risenb.witness.ui.tasklist.adapter.ModelTwoExecIssuStateAdapter;
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
public class ModelTwoExecIssueState extends BaseUI {

    @BindView(R.id.list_view)
    ListView mListView;
    //上一步
    @BindView(R.id.pre_step)
    Button preStep;
    //下一步
    @BindView(R.id.next_step)
    Button nextStep;


    private String taskid;
    private ModelTwoExecIssuStateAdapter mAdapter;//这个是 未取证 上刊状态 的 Activity 的Adapter
    private String marks;
    private String modeltype;
    private int page;
    private String totalpage;

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
        setContentView(R.layout.taskissuestate);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        taskid = bundle.getString("taskid");     //任务ID
        marks = bundle.getString("marks");        //标示
        modeltype = bundle.getString("modeltype");//模版
        page = bundle.getInt("currentpage");//当前第几页
        totalpage = bundle.getString("page");//总页数

        Map<String, String> params = new HashMap<>();
        params.put("taskId", taskid);
        params.put("c", MyApplication.getInstance().getC());
        params.put("modeltype", modeltype);
        params.put("page", String.valueOf(page));

        //params.put("")
        onNonTaskAccessNetWorkDetail(params);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //textsituation= textanwer.get(position);
                //  Toast.makeText(TaskInfoSituation.this,textsituation,Toast.LENGTH_SHORT).show();
                Log.e("mAdapter", position + "");
                mAdapter.setSelectItem(position); // 记录当前选中的item
//                mAdapter.notifyDataSetInvalidated();    //更新UI界面
            }
        });

    }

    //单选数据
    public void onNonTaskAccessNetWorkDetail(Map<String, String> params) {
        String url = this.getResources().getString(R.string.service_host_address).concat(getString(R.string.UploadedDetails));
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
                Log.e("IssueLastTaskBean", info.toString());
                if (null != info) {

                    //图片和视频 数据
                    // mAdapter = new ListViewCommconAdapter(TaskIssueState.this,info.getTaskList().get(0).getExampleFile(),info.getTaskList().get(0).getIsType());
                    mAdapter = new ModelTwoExecIssuStateAdapter(ModelTwoExecIssueState.this, info.getTaskList(), 1, info.getTaskList().get(0).getExampleFile(), info.getTaskList().get(0).getIsType());
                    mListView.setAdapter(mAdapter);
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Log.e("mAdapter", position + "");
                            mAdapter.setSelectItem(position); // 记录当前选中的item
                        }
                    });

                    //这里是 从服务器拉取选中的数据
                    if (null != info.getTaskList().get(0).getReturnfile() && info.getTaskList().get(0).getReturnfile().size() > 0) {
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

    //下一步
    @OnClick(R.id.next_step)
    public void NextStep() {
        String selectstr = mAdapter.getSelectstr();
        if (null == selectstr) {
            makeText("请选择上刊状态");
            selectstr = "良好";
        }
        Log.e("selectstr", selectstr);
        Map<String, String> params = new HashMap<>();
        params.put("taskid", taskid);
        params.put("c", MyApplication.getInstance().getC());
        params.put("issuestate", selectstr);
        Log.e("taskid", taskid);
        Log.e("c", MyApplication.getInstance().getC());
        Log.e("issuestate", selectstr);
        onNonTaskSaveNetWorkDetail(params);
    }

    private void onNonTaskSaveNetWorkDetail(Map<String, String> params) {
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
                Log.e("success", success);
                if ("1".equals(success)) {
                    Utils.getUtils().dismissDialog();
                    //数据提交成功跳转
                    Intent intent = new Intent(ModelTwoExecIssueState.this, ModelTwoExecEvidenceFirst.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("taskid", taskid);
                    bundle.putString("modeltype", modeltype);
                    bundle.putInt("page", ++page);
                    intent.putExtra("totalpage", totalpage);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    --page;
                    UIManager.getInstance().pushActivity(ModelTwoExecIssueState.this);
                    // 之后还原
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }

}
