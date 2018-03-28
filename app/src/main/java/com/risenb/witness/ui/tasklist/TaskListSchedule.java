package com.risenb.witness.ui.tasklist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.risenb.witness.utils.SharedPreferencesUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.jpush.android.api.JPushInterface;

/**
 * 我的任务的fragment页面
 */
public class TaskListSchedule extends BaseUI {
    //未执行
    @BindView(R.id.layout_title_non_execution)
    RelativeLayout mTitleNonExecution;
    //文本
    @BindView(R.id.title_non_execution)
    TextView mTextTitle_non_execution;
    //执行中
    @BindView(R.id.layout_title_executioning)
    RelativeLayout mTitleExecutioning;
    //文本
    @BindView(R.id.title_executioning)
    TextView mTextTitle_executioning;
    //已上传
    @BindView(R.id.layout_have_execution)
    RelativeLayout mHaveExecution;
    //文本
    @BindView(R.id.title_have_execution)
    TextView mTextTitle_have_execution;
    //已上传 提示红点
    @BindView(R.id.new_image_have_execution)
    ImageView mImageHaveExecution;
    //被驳回
    @BindView(R.id.layout_passivity_task)
    RelativeLayout mPassivityTask;
    //文本
    @BindView(R.id.title_passivity_task)
    TextView mTextTitle_passivity_task;
    //显示内容
    @BindView(R.id.main_content)
    FrameLayout mMainContent;
    //被驳回 提示红点
    @BindView(R.id.new_image_passivity_task)
    ImageView mImagePassivityTask;
    //未执行 提示红点
    @BindView(R.id.new_image_non_execution)
    ImageView mNonExecutionPassivityTask;
    // 批量上传引导
    @BindView(R.id.batch_upload_guide)
    RelativeLayout mBatchUploadGuide;

    public static int type = 1;

    private NonExecutionTaskFragment mNonFragment;
    private ExecutingTaskFragment mExeingFragment;
    private UploadedTaskFragment mComUploadFragment;
    private RejectTaskFragment mRejectTaskFragment;
    private String KEY_INDEX = "CUREENFM";
    private int index = 0;

    private Unbinder unbinder;
    private String cityId;
    private String mediaId;
    private String companyId;
    private String mNonExecTsk;
    private String mRejectTask;
    private String taskState;

    @Override
    protected void onInitCreate(Bundle savedInstanceState) {
        setContentView(R.layout.tasklistschedule);
        unbinder = ButterKnife.bind(this);
        index = getIntent().getIntExtra("index", -1);
        taskState = getIntent().getStringExtra("taskState");
        mBatchUploadGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBatchUploadGuide.setVisibility(View.GONE);
            }
        });
        if (savedInstanceState != null) {
            // “内存重启”时调用
            mNonFragment = (NonExecutionTaskFragment) getSupportFragmentManager().findFragmentByTag(NonExecutionTaskFragment.class.getName());
            mExeingFragment = (ExecutingTaskFragment) getSupportFragmentManager().findFragmentByTag(ExecutingTaskFragment.class.getName());
            mComUploadFragment = (UploadedTaskFragment) getSupportFragmentManager().findFragmentByTag(UploadedTaskFragment.class.getName());
            mRejectTaskFragment = (RejectTaskFragment) getSupportFragmentManager().findFragmentByTag(RejectTaskFragment.class.getName());
            index = savedInstanceState.getInt(KEY_INDEX);
            if (index == 0) {
                getSupportFragmentManager().beginTransaction()
                        .show(mNonFragment)
                        .hide(mExeingFragment)
                        .hide(mComUploadFragment)
                        .hide(mRejectTaskFragment)
                        .commit();
            } else if (index == 1) {
                getSupportFragmentManager().beginTransaction()
                        .show(mExeingFragment)
                        .hide(mNonFragment)
                        .hide(mComUploadFragment)
                        .hide(mRejectTaskFragment)
                        .commit();
            } else if (index == 2) {
                getSupportFragmentManager().beginTransaction()
                        .show(mComUploadFragment)
                        .hide(mExeingFragment)
                        .hide(mNonFragment)
                        .hide(mRejectTaskFragment)
                        .commit();
            } else if (index == 3) {
                getSupportFragmentManager().beginTransaction()
                        .show(mRejectTaskFragment)
                        .hide(mExeingFragment)
                        .hide(mNonFragment)
                        .hide(mComUploadFragment)
                        .commit();
            }
        } else {  // 正常时
//            msgFragment = MsgFragment.newInstance();
//            contactFragment = ContactFragment.newInstance();
//            meFragment = MeFragment.newInstance();
//
//            getFragmentManager().beginTransaction()
//                    .add(R.id.container, msgFragment, msgFragment.getClass().getName())
//                    .add(R.id.container, contactFragment, contactFragment.getClass().getName())
//                    .add(R.id,container,meFragment,meFragment.getClass().getName())
//                    .hide(contactFragment)
//                    .hide(meFragment)
//                    .commit();
            luncherFragment(index);
        }
    }

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("我的任务");
        rightVisible(R.drawable.ic_search);
        /*setTitleBackground(255, 255, 255);*/
    }

    @Override
    protected void prepareData() {
        String message = getIntent().getExtras().getString(JPushInterface.EXTRA_ALERT);
        if (!TextUtils.isEmpty(message) && message.length() > 2) {
            switch (message.substring(0, 2)) {
                case "驳回":
                    OnClickPassivitySchedule(null);
                    break;
                case "推送":
                    OnClickNonExecutionSchedule(null);
                    break;
            }
        }
    }

    @Override
    public void onLoadOver() {

    }

    //未执行
    @OnClick(R.id.layout_title_non_execution)
    public void OnClickNonExecutionSchedule(View view) {
        fragmentChange(0);
        textSizeChange(0);
        index = 0;
    }

    //执行中
    @OnClick(R.id.layout_title_executioning)
    public void OnClickExecutioningSchedule(View view) {
        fragmentChange(1);
        textSizeChange(1);
        index = 1;
    }

    //已上传
    @OnClick(R.id.layout_have_execution)
    public void OnClickHaveExecutionSchedule(View view) {
        fragmentChange(2);
        textSizeChange(2);
        index = 2;
    }

    //被驳回
    @OnClick(R.id.layout_passivity_task)
    public void OnClickPassivitySchedule(View view) {
        fragmentChange(3);
        textSizeChange(3);
        index = 3;
    }

    //搜索
    @OnClick(R.id.iv_right)
    public void OnClickSearchTaskSchedule(View view) {
        Intent intent = new Intent(this, TaskListScreenUI.class);
        switch (index) {
            case 0:
                intent.putExtra("taskState", "NonExecution");
                break;
            case 1:
                intent.putExtra("taskState", "Executioning");
                break;
            case 2:
                intent.putExtra("taskState", "CompletedUpload");
                break;
            case 3:
                intent.putExtra("taskState", "RejectTask");
                break;
            default:
                intent.putExtra("taskState", taskState);
                break;
        }
        intent.putExtra("index", index);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 1) {
                cityId = data.getStringExtra("cityId");
                mediaId = data.getStringExtra("mediaId");
                companyId = data.getStringExtra("companyId");
                if (!TextUtils.isEmpty(cityId) || !TextUtils.isEmpty(mediaId) || !TextUtils.isEmpty(companyId)) {
                    if (index == 0) {
                        mNonFragment.taskSearch(cityId, mediaId, companyId, index);
                    } else if (index == 1) {
                        mExeingFragment.taskSearch(cityId, mediaId, companyId, index);
                    }
                } else {
                }
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if ("Executioning".equals(taskState) && MyApplication.isTaskListScheduleFinish) {
            // 说明已经开始批量上传
            finish();
        } else {
            onAccessNetWork();
        }
    }

    private void onAccessNetWork() {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.taskCount));
        Map<String, String> param = new HashMap<>();
        param.put("c", MyApplication.getInstance().getC());
        MyOkHttp.get().post(this, url, param, new GsonResponseHandler<BaseBeans<AllTaskCount>>() {
            @Override
            public void onFailure(int statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(int statusCode, BaseBeans response) {
                String success = response.getSuccess();
                AllTaskCount taskCount = (AllTaskCount) response.getData();
                // String mExecutionTask = taskCount.getActiveCount(); //执行中任务
                //被驳回的
                mRejectTask = taskCount.getOutOfCount();
                //未执行的任务
                mNonExecTsk = taskCount.getUnfilledCount();
                // String mCompletedUploadTask =taskCount.getUploadedCount(); //已经上传
                String nontask = taskCount.getIs_read();
                if ("1".equals(nontask)) {
                    mNonExecutionPassivityTask.setVisibility(View.VISIBLE);
                } else {
                    mNonExecutionPassivityTask.setVisibility(View.GONE);
                }
                String RejectTask = taskCount.getReject_is_read();
                if ("1".equals(RejectTask)) {
                    mImagePassivityTask.setVisibility(View.VISIBLE);
                } else {
                    mImagePassivityTask.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_INDEX, index);
    }

    public void fragmentChange(int currentfragmentindex) {
        FragmentManager fm = getSupportFragmentManager();
        if (currentfragmentindex == 0) {
            FragmentTransaction transaction = fm.beginTransaction();
            if (mNonFragment == null) {
                mNonFragment = new NonExecutionTaskFragment();
                transaction.add(R.id.main_content, mNonFragment, mNonFragment.getClass().getName());
            }
            if (mExeingFragment != null) {
                transaction.hide(mExeingFragment);
            }
            if (mComUploadFragment != null) {
                transaction.hide(mComUploadFragment);
            }
            if (mRejectTaskFragment != null) {
                transaction.hide(mRejectTaskFragment);
            }
            transaction.show(mNonFragment);
            transaction.commit();
        }

        if (currentfragmentindex == 1) {
            FragmentTransaction transaction = fm.beginTransaction();
            if (mExeingFragment == null) {
                mExeingFragment = new ExecutingTaskFragment();
                transaction.add(R.id.main_content, mExeingFragment, mExeingFragment.getClass().getName());
            }
            if (mNonFragment != null) {
                transaction.hide(mNonFragment);
            }

            if (mComUploadFragment != null) {
                transaction.hide(mComUploadFragment);
            }

            if (mRejectTaskFragment != null) {
                transaction.hide(mRejectTaskFragment);
            }
            transaction.show(mExeingFragment);
            transaction.commit();
        }

        if (currentfragmentindex == 2) {
            FragmentTransaction transaction = fm.beginTransaction();
            if (mComUploadFragment == null) {
                mComUploadFragment = new UploadedTaskFragment();
                Bundle bundle = new Bundle();
                bundle.putString("mark", UploadedTaskFragment.COMUPLOADFRAGMENT);
                mComUploadFragment.setArguments(bundle);
                transaction.add(R.id.main_content, mComUploadFragment, mComUploadFragment.getClass().getName());
            }
            if (mNonFragment != null) {
                transaction.hide(mNonFragment);
            }
            if (mExeingFragment != null) {
                transaction.hide(mExeingFragment);
            }
            if (mRejectTaskFragment != null) {
                transaction.hide(mRejectTaskFragment);
            }
            transaction.show(mComUploadFragment);
            transaction.commit();
        }

        if (currentfragmentindex == 3) {
            FragmentTransaction transaction = fm.beginTransaction();
            if (mRejectTaskFragment == null) {
                mRejectTaskFragment = new RejectTaskFragment();
                Bundle bundle = new Bundle();
                bundle.putString("mark", RejectTaskFragment.REJECTASKFRAGEMNTFLAG);
                mRejectTaskFragment.setArguments(bundle);
                transaction.add(R.id.main_content, mRejectTaskFragment, mRejectTaskFragment.getClass().getName());
            }
            if (mNonFragment != null) {
                transaction.hide(mNonFragment);
            }
            if (mExeingFragment != null) {
                transaction.hide(mExeingFragment);
            }
            if (mComUploadFragment != null) {
                transaction.hide(mComUploadFragment);
            }
            transaction.show(mRejectTaskFragment);
            transaction.commit();
        }
    }

    public void textSizeChange(int index) {
        if (index == 0) {
            mTextTitle_non_execution.setTextSize(16);
            mTextTitle_executioning.setTextSize(16);
            mTextTitle_have_execution.setTextSize(16);
            mTextTitle_passivity_task.setTextSize(16);
            mTextTitle_non_execution.setTextColor(getResources().getColor(R.color.main_green));
            mTextTitle_executioning.setTextColor(getResources().getColor(R.color.gray));
            mTextTitle_have_execution.setTextColor(getResources().getColor(R.color.gray));
            mTextTitle_passivity_task.setTextColor(getResources().getColor(R.color.gray));
        } else if (index == 1) {
            mTextTitle_executioning.setTextSize(16);
            mTextTitle_non_execution.setTextSize(16);
            mTextTitle_have_execution.setTextSize(16);
            mTextTitle_passivity_task.setTextSize(16);
            mTextTitle_executioning.setTextColor(getResources().getColor(R.color.main_green));
            mTextTitle_non_execution.setTextColor(getResources().getColor(R.color.gray));
            mTextTitle_have_execution.setTextColor(getResources().getColor(R.color.gray));
            mTextTitle_passivity_task.setTextColor(getResources().getColor(R.color.gray));
        } else if (index == 2) {
            mTextTitle_have_execution.setTextSize(16);
            mTextTitle_non_execution.setTextSize(16);
            mTextTitle_executioning.setTextSize(16);
            mTextTitle_passivity_task.setTextSize(16);
            mTextTitle_have_execution.setTextColor(getResources().getColor(R.color.main_green));
            mTextTitle_non_execution.setTextColor(getResources().getColor(R.color.gray));
            mTextTitle_executioning.setTextColor(getResources().getColor(R.color.gray));
            mTextTitle_passivity_task.setTextColor(getResources().getColor(R.color.gray));
        } else if (index == 3) {
            mTextTitle_passivity_task.setTextSize(16);
            mTextTitle_have_execution.setTextSize(16);
            mTextTitle_non_execution.setTextSize(16);
            mTextTitle_executioning.setTextSize(16);
            mTextTitle_passivity_task.setTextColor(getResources().getColor(R.color.main_green));
            mTextTitle_have_execution.setTextColor(getResources().getColor(R.color.gray));
            mTextTitle_non_execution.setTextColor(getResources().getColor(R.color.gray));
            mTextTitle_executioning.setTextColor(getResources().getColor(R.color.gray));
        }
    }

    public void luncherFragment(int index) {
        if (index == 0) {
            if (null == mNonFragment) {
                mNonFragment = new NonExecutionTaskFragment();
                Bundle bundle = new Bundle();
                bundle.putString("mark", NonExecutionTaskFragment.NONEXECUTIONFRAGMENT);
                mNonFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.main_content, mNonFragment, mNonFragment.getClass().getName())
                        .show(mNonFragment)
                        .commit();
                textSizeChange(index);
            } else {
                getSupportFragmentManager().beginTransaction().show(mNonFragment);
            }
        } else if (index == 1) {
            if (null == mExeingFragment) {
                mExeingFragment = new ExecutingTaskFragment();
                Bundle bundle = new Bundle();
                bundle.putString("mark", ExecutingTaskFragment.EXECUTIONINGFRAGMENT);
                mExeingFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.main_content, mExeingFragment, mExeingFragment.getClass().getName())
                        .show(mExeingFragment)
                        .commit();
                textSizeChange(index);
                boolean showBatchGuideImage = SharedPreferencesUtil.getBoolean(getApplication(), "SHOW_BATCH_GUIDE_IMAGE", false);
                if (!showBatchGuideImage) {
                    // showReminderDialog();
                    mBatchUploadGuide.setVisibility(View.VISIBLE);
                    SharedPreferencesUtil.saveBoolean(getApplication(), "SHOW_BATCH_GUIDE_IMAGE", true);
                }
            } else {
                getSupportFragmentManager().beginTransaction().show(mExeingFragment);
            }
        } else if (index == 2) {
            if (null == mComUploadFragment) {
                mComUploadFragment = new UploadedTaskFragment();
                Bundle bundle = new Bundle();
                bundle.putString("mark", UploadedTaskFragment.COMUPLOADFRAGMENT);
                mComUploadFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.main_content, mComUploadFragment, mComUploadFragment.getClass().getName())
                        .show(mComUploadFragment)
                        .commit();
                textSizeChange(index);
            } else {
                getSupportFragmentManager().beginTransaction().show(mComUploadFragment);
            }
        } else if (index == 3) {
            if (null == mRejectTaskFragment) {
                mRejectTaskFragment = new RejectTaskFragment();
                Bundle bundle = new Bundle();
                bundle.putString("mark", RejectTaskFragment.REJECTASKFRAGEMNTFLAG);
                mRejectTaskFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.main_content, mRejectTaskFragment, mRejectTaskFragment.getClass().getName())
                        .show(mRejectTaskFragment)
                        .commit();
                textSizeChange(index);
            } else {
                getSupportFragmentManager().beginTransaction().show(mRejectTaskFragment);
            }
        }
    }

    @Override
    protected void onDetachedDestroy() {
        unbinder.unbind();
    }

    public void showReminderDialog() {
        final AlertDialog.Builder customizeDialog = new AlertDialog.Builder(this, R.style.ImageloadingDialogStyle);
        // final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_customize, null);
        // customizeDialog.setView(dialogView);
        customizeDialog.setTitle("操作提示");
        customizeDialog.setMessage("长按任务列表可进入批量上传页面");
        customizeDialog.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        customizeDialog.show();
    }
}
