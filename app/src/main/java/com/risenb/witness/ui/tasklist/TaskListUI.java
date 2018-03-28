package com.risenb.witness.ui.tasklist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.beans.AllTaskCount;
import com.risenb.witness.beans.BaseBeans;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.GsonResponseHandler;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.ui.tasklist.fragment.ExecutingTaskFragment;
import com.risenb.witness.ui.tasklist.fragment.NonExecutionTaskFragment;
import com.risenb.witness.ui.tasklist.fragment.UploadedTaskFragment;
import com.risenb.witness.ui.login.LoginUI;
import com.risenb.witness.utils.NetWorksUtils;
import com.risenb.witness.utils.sardar.Compressor;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 我的任务首页
 */
public class TaskListUI extends BaseUI {
    @BindView(R.id.tasklist_wei_content)
    TextView tasklist_wei_content;
    @BindView(R.id.tasklist_zhong_content)
    TextView tasklist_zhong_content;
    @BindView(R.id.tasklist_shang_content)
    TextView tasklist_shang_content;
    @BindView(R.id.tasklist_bohui_content)
    TextView tasklist_bohui_content;
    @BindView(R.id.tasklist_overtime_content)
    TextView tasklist_overtime_content;

    @BindView(R.id.back)
    RelativeLayout back;

    @BindView(R.id.text_non_execution_task)
    TextView mTextNonExecutionTask;
    @BindView(R.id.non_prompt_task)
    ImageView mNonPromptTask;
    @BindView(R.id.layout_non_execution_task)
    RelativeLayout mLayoutNonExecutionTask;
    @BindView(R.id.text_executioning_task)
    TextView mTextExecutioningTask;
    @BindView(R.id.executioning_task)
    ImageView mExecutioningTask;
    @BindView(R.id.layout_executioning_task)
    RelativeLayout mLayoutExecutioningTask;
    @BindView(R.id.text_have_execution_task)
    TextView mTextHaveExecutionTask;
    @BindView(R.id.have_execution_task)
    ImageView mHaveExecutionTask;
    @BindView(R.id.layout_have_execution_task)
    RelativeLayout mLayoutHaveExecutionTask;
    @BindView(R.id.text_passivity_task)
    TextView mTextPassivityTask;
    @BindView(R.id.passivity_task)
    ImageView mPassivityTask;
    @BindView(R.id.layout_passivity_task)
    RelativeLayout mLayoutPassivityTask;
    private Unbinder unbinder;

    @BindView(R.id.taskList_circular_fill_able_loaders)
    FrameLayout circularFillAbleLoaders;

    public static Handler handler;

    private String mNonExecTsk;
    private String mRejectTask;
    public Compressor com;
    private UploadFinishReceiver uploadFinishReceiver;

    @Override
    protected void back() {
        exit();
    }

    @Override
    protected void onCreate() {
        setContentView(R.layout.tasklist);
        unbinder = ButterKnife.bind(this);
        circularFillAbleLoaders.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        if (handler == null) {
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 1:
                            circularFillAbleLoaders.setVisibility(View.GONE);
                            break;
                    }
                    super.handleMessage(msg);
                }
            };
        }
        registerUploadFinishReceiver();
    }

    @Override
    protected void setControlBasis() {
        backGone();
        setTitle("我的任务");
        /*rightVisible("已过期");*/
    }

    @Override
    protected void prepareData() {

    }

    private void onAccessNetWork() {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.checkOverdue));
        Map<String, String> param = new HashMap<>();
        param.put("c", MyApplication.getInstance().getC());
        param.put("type", "0");
        MyOkHttp.get().post(getApplication(), url, param, new GsonResponseHandler<BaseBeans>() {
            @Override
            public void onSuccess(int statusCode, BaseBeans response) {
                if ("1".equals(response.getSuccess())) {
                    getTaskCount();
                } else {
                    if ("登录异常".equals(response.getErrorMsg())) {
                        if (!application.getOne())
                            errorLogin();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {

            }
        });
    }

    private void getTaskCount() {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.taskCount));
        Map<String, String> param = new HashMap<>();
        param.put("c", MyApplication.getInstance().getC());
        MyOkHttp.get().post(this, url, param, new GsonResponseHandler<BaseBeans<AllTaskCount>>() {
            @Override
            public void onFailure(int statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(int statusCode, BaseBeans response) {
                if ("1".equals(response.getSuccess())) {
                    AllTaskCount taskCount = (AllTaskCount) response.getData();
                    tasklist_wei_content.setText(taskCount.getUnfilledCount());
                    tasklist_zhong_content.setText(taskCount.getActiveCount());
                    tasklist_shang_content.setText(taskCount.getUploadedCount());
                    tasklist_bohui_content.setText(taskCount.getOutOfCount());
                    tasklist_overtime_content.setText(taskCount.getGiveUpCount());
                    // 执行中任务
                    // String mExecutionTask = taskCount.getActiveCount();
                    // 被驳回的
                    mRejectTask = taskCount.getOutOfCount();
                    // 未执行的任务
                    mNonExecTsk = taskCount.getUnfilledCount();
                    // 已经上传
                    // String mCompletedUploadTask =taskCount.getUploadedCount();
                    String nonTask = taskCount.getIs_read();

                    if ("1".equals(nonTask)) {
                        mNonPromptTask.setVisibility(View.VISIBLE);
                    } else {
                        mNonPromptTask.setVisibility(View.GONE);
                    }
                    if ("1".equals(taskCount.getReject_is_read())) {
                        mPassivityTask.setVisibility(View.VISIBLE);
                    } else {
                        mPassivityTask.setVisibility(View.GONE);
                    }
                    String RejectTask = taskCount.getReject_is_read();
                    if ("1".equals(RejectTask)) {
                        mPassivityTask.setVisibility(View.VISIBLE);
                    } else {
                        mPassivityTask.setVisibility(View.GONE);
                    }
                } else {
                    if ("登录异常".equals(response.getErrorMsg())) {
                        errorLogin();
                    }
                }
            }
        });
    }

    @Override
    public void onLoadOver() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!NetWorksUtils.isNetworkAvailable(this)) {
            Toast.makeText(getApplicationContext(), "当前没有可用网络", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isEmpty(MyApplication.getInstance().getC())) {
            onAccessNetWork();
        } else {
            /*startActivity(new Intent(this, LoginUI.class));*/
            tasklist_wei_content.setText("0");
            tasklist_zhong_content.setText("0");
            tasklist_shang_content.setText("0");
            tasklist_bohui_content.setText("0");
            tasklist_overtime_content.setText("0");
        }
    }

    //已过期
    @OnClick(R.id.tv_right)
    public void tv_right(View view) {
        if (!TextUtils.isEmpty(MyApplication.getInstance().getC())) {
            startActivity(new Intent(this, OverdueTaskinfo.class));
        } else {
            startActivity(new Intent(this, LoginUI.class));
        }
    }

    //未被执行任务
    @OnClick(R.id.layout_non_execution_task)
    public void OnClickNonExecutionTask(View view) {
        if (!TextUtils.isEmpty(MyApplication.getInstance().getC())) {
            lunchActivity(0);
        } else {
            startActivity(new Intent(this, LoginUI.class));
        }
    }

    //执行中任务
    @OnClick(R.id.layout_executioning_task)
    public void OnClickExecutingTask(View view) {
        if (MyApplication.isTaskListScheduleFinish) {
            circularFillAbleLoaders.setVisibility(View.VISIBLE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message message = Message.obtain();
                    message.what = 1;
                    handler.sendMessageDelayed(message, 2000);
                }
            }).start();
            return;
        }
        if (!TextUtils.isEmpty(MyApplication.getInstance().getC())) {
            lunchActivity(1);
        } else {
            startActivity(new Intent(this, LoginUI.class));
        }
    }

    //已上传任务
    @OnClick(R.id.layout_have_execution_task)
    public void OnClickHaveExecutionTask(View view) {
        if (!TextUtils.isEmpty(MyApplication.getInstance().getC())) {
            lunchActivity(2);
        } else {
            startActivity(new Intent(this, LoginUI.class));
        }
    }

    //被驳回任务
    @OnClick(R.id.layout_passivity_task)
    public void OnClickPassivityTask(View view) {
        if (!TextUtils.isEmpty(MyApplication.getInstance().getC())) {
            lunchActivity(3);
        } else {
            startActivity(new Intent(this, LoginUI.class));
        }
    }

    //已过期任务
    @OnClick(R.id.layout_overtime_task)
    public void OnClickOvertimeTask(View view) {
        if (!TextUtils.isEmpty(MyApplication.getInstance().getC())) {
            lunchActivity(4);
        } else {
            startActivity(new Intent(this, LoginUI.class));
        }
    }

    public void lunchActivity(int index) {
        if (index >= 0 && index <= 3) {
            Intent intent = new Intent(getActivity(), TaskListSchedule.class);
            if (index == 0) {
                intent.putExtra("index", 0);
                intent.putExtra("nonexectask", mNonExecTsk);
                intent.putExtra("taskState", NonExecutionTaskFragment.NONEXECUTIONFRAGMENT);
            } else if (index == 1) {
                intent.putExtra("index", 1);
                intent.putExtra("taskState", ExecutingTaskFragment.EXECUTIONINGFRAGMENT);
            } else if (index == 2) {
                intent.putExtra("index", 2);
                intent.putExtra("taskState", UploadedTaskFragment.COMUPLOADFRAGMENT);
            } else if (index == 3) {
                intent.putExtra("index", 3);
                intent.putExtra("taskState", RejectTaskFragment.REJECTASKFRAGEMNTFLAG);
                intent.putExtra("rejecttask", mRejectTask);
            }
            startActivity(intent);
        } else if (index == 4) {
            startActivity(new Intent(this, OverdueTaskinfo.class));
        }
    }

    /**
     * 动态注册广播接收者
     */
    public class UploadFinishReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!TextUtils.isEmpty(MyApplication.getInstance().getC())) {
                onAccessNetWork();
            } else {
                startActivity(new Intent(TaskListUI.this, LoginUI.class));
            }
        }
    }

    public void registerUploadFinishReceiver() {
        uploadFinishReceiver = new UploadFinishReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction("BatchTaskUploadToComplete");
        registerReceiver(uploadFinishReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(uploadFinishReceiver);
    }

    @Override
    protected void onDetachedDestroy() {
        unbinder.unbind();
    }
}
