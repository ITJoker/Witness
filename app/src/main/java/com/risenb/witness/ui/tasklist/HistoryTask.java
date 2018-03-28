package com.risenb.witness.ui.tasklist;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.beans.BaseBeans;
import com.risenb.witness.beans.HistoryCompleted;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.GsonResponseHandler;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.ui.tasklist.adapter.HistoryAdapter;
import com.risenb.witness.utils.newUtils.Utils;
import com.risenb.witness.views.citypicker.CountDownTimerView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryTask extends BaseUI {
    @BindView(R.id.history_task_listview)
    ListView mHistoryTaskListView;

    private TextView mHistoryAddress;
    private TextView mHistoryState;
    private TextView mHistoryRemark;
    private TextView mHistoryPrice;
    private CountDownTimerView mHistoryTimerView;
    private HistoryAdapter adapter;
    private String taskid;

    private boolean settledTaskSign;

    private TextView mSelectRadio;
    private TextView mSelectCheckBox;
    private TextView mFillTheBlankSpaces;

    private TextView mHistorySelect;
    private TextView mHistoryLouKan;
    private TextView mHistorySuggest;

    private LinearLayout mlayout_select_radio;
    private LinearLayout mlayout_select_checkbox;
    private LinearLayout mlayout_fill_the_blank_spaces;

    private TextView mHistoryAdv;
    private LinearLayout mLayout_adv_conditionm;

    private String modeltype;
    private View mHistoryFooterView;
    private LinearLayout countdownTimerLayout;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("拍摄详情");
    }

    @Override
    protected void onInitCreate(Bundle savedInstanceState) {
        setContentView(R.layout.history_task_layout);
        ButterKnife.bind(this);
        taskid = getIntent().getStringExtra("taskid");
        modeltype = getIntent().getStringExtra("modeltype");
        settledTaskSign = getIntent().getBooleanExtra("settledTask", false);
        String note = getIntent().getStringExtra("note");

        if (TextUtils.isEmpty(taskid)) {
            /*
             * Todo 以后需依据专业或非专业人员判断是否可再次上传被驳回任务
             */
        }
        View mHistoryHeadView = LayoutInflater.from(this).inflate(R.layout.histotytask_head_layout, null, false);
        LinearLayout rejectReasonLinearLayout = mHistoryHeadView.findViewById(R.id.reject_reason_ll);
        rejectReasonLinearLayout.setVisibility(View.GONE);
        View rejectReasonLineView = mHistoryHeadView.findViewById(R.id.reject_reason_line_view);
        rejectReasonLineView.setVisibility(View.GONE);
        TextView taskMarkWord = mHistoryHeadView.findViewById(R.id.task_mark_tv);
        if(!TextUtils.isEmpty(note)) {
            taskMarkWord.setText("备注：".concat(note));
        } else {
            taskMarkWord.setText("备注：无");
        }
        mHistoryAddress = mHistoryHeadView.findViewById(R.id.historytask_adrress);
        mHistoryState = mHistoryHeadView.findViewById(R.id.historytask_state);
        mHistoryRemark = mHistoryHeadView.findViewById(R.id.historytask_remark);
        mHistoryPrice = mHistoryHeadView.findViewById(R.id.historytask_price);
        countdownTimerLayout = mHistoryHeadView.findViewById(R.id.countdownTimerLayout);
        mHistoryTimerView = mHistoryHeadView.findViewById(R.id.historytask_countdown);
        LinearLayout uploaded_task_price_and_countdown_ll = mHistoryHeadView.findViewById(R.id.uploaded_task_price_and_countdown_ll);
        if ("3".equals(getIntent().getStringExtra("execution"))) {
            //执行方式(1.专业人员、2.客户检测员、3.奖池)
            uploaded_task_price_and_countdown_ll.setVisibility(View.VISIBLE);
        } else {
            uploaded_task_price_and_countdown_ll.setVisibility(View.GONE);
        }
        mHistoryTaskListView.addHeaderView(mHistoryHeadView);

        mHistoryFooterView = LayoutInflater.from(this).inflate(R.layout.historytask_footerview, null, false);
        mLayout_adv_conditionm = mHistoryFooterView.findViewById(R.id.layout_adv_condition);
        mHistoryAdv = mHistoryFooterView.findViewById(R.id.adv_camera_condition);

        mlayout_select_radio = mHistoryFooterView.findViewById(R.id.layout_select_radio);
        mlayout_select_checkbox = mHistoryFooterView.findViewById(R.id.layout_select_checkbox);
        mlayout_fill_the_blank_spaces = mHistoryFooterView.findViewById(R.id.layout_fill_the_blank_spaces);

        mSelectRadio = mHistoryFooterView.findViewById(R.id.questions_select_radio);
        mSelectCheckBox = mHistoryFooterView.findViewById(R.id.questions_select_checkbox);
        mFillTheBlankSpaces = mHistoryFooterView.findViewById(R.id.fill_the_blank_spaces);
        //单选
        mHistorySelect = mHistoryFooterView.findViewById(R.id.history_task_selet);
        //多选
        mHistoryLouKan = mHistoryFooterView.findViewById(R.id.history_task_range);
        //填空题
        mHistorySuggest = mHistoryFooterView.findViewById(R.id.history_task_suggestion);
        /*mHistoryTaskListView.addFooterView(mHistoryFooterView);*/
        RequestNetWork();
    }

    private void RequestNetWork() {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.fixedTaskUploadedDetails));
        Map<String, String> params = new HashMap<>();
        params.put("taskId", taskid);
        params.put("c", MyApplication.getInstance().getC());
        if (settledTaskSign) {
            countdownTimerLayout.setVisibility(View.GONE);
            mHistoryTaskListView.removeFooterView(mHistoryFooterView);
        } else {
            url = getResources().getString(R.string.service_host_address).concat(getString(R.string.newScheduleUploadedDetails));
            params.put("modeltype", modeltype);
            params.put("page", "0");
        }
        onHistoryTaskAccessNetWorkDetail(url, params);
    }

    @Override
    protected void prepareData() {

    }

    @Override
    public void onLoadOver() {

    }

    private void onHistoryTaskAccessNetWorkDetail(String url, Map<String, String> params) {
        Utils.getUtils().showProgressDialog(this);
        MyOkHttp.get().post(HistoryTask.this, url, params, new GsonResponseHandler<BaseBeans<HistoryCompleted>>() {
            @Override
            public void onSuccess(int statusCode, BaseBeans<HistoryCompleted> response) {
                Utils.getUtils().dismissDialog();
                HistoryCompleted info = response.getData();
                if (!TextUtils.isEmpty(info.getTitle())) {
                    mHistoryAddress.setText(info.getTitle());
                } else {
                    mHistoryAddress.setText(info.getAddress());
                }

                if (!TextUtils.isEmpty(info.getIssuestate())) {
                    mHistoryState.setText("上刊状态：".concat(info.getIssuestate()));
                } else {
                    mHistoryState.setText("上刊状态：无");
                }

                if (!TextUtils.isEmpty(info.getScheduleRemark())) {
                    mHistoryRemark.setText("上刊备注：".concat(info.getScheduleRemark()));
                } else {
                    mHistoryRemark.setText("上刊备注：无");
                }
                mHistoryPrice.setText("￥".concat(info.getPrice()));
                //mHistoryTimerView.setTime();
                adapter = new HistoryAdapter(HistoryTask.this, info.getTaskList());
                adapter.showAddress(info.getAddress());
                adapter.showRemark(info.getRemark());
                mHistoryTaskListView.setAdapter(adapter);
                if (info.getTaskList().size() > 1) {
                    mHistoryAdv.setText(info.getTaskList().get(0).getReturnfile().get(0));
                }
                mHistoryFooterView.measure(0, 0);
                ViewGroup.LayoutParams layoutParams = mHistoryFooterView.getLayoutParams();
                layoutParams.height = mHistoryFooterView.getMeasuredHeight();
                mHistoryFooterView.setLayoutParams(layoutParams);
                /*for (int i = 0; i < info.getTaskList().size(); i++) {
                    String type = info.getTaskList().get(i).getIsType();
                    if ("2".equals(type)) {
                        //上刊状态
                        mLayout_adv_conditionm.setVisibility(View.VISIBLE);
                        mHistoryAdv.setText(info.getTaskList().get(i).getReturnfile().get(0));
                    } else {
                        mLayout_adv_conditionm.setVisibility(View.GONE);
                    }
                    if ("3".equals(type)) {
                        //单选类型
                        mlayout_select_radio.setVisibility(View.VISIBLE);
                        mSelectRadio.setText(info.getTaskList().get(i).getTaskRemark());//问题
                        if (null != info.getTaskList().get(i).getReturnfile() && info.getTaskList().get(i).getReturnfile().size() > 0) {
                            mHistorySelect.setText(info.getTaskList().get(i).getReturnfile().get(0));//答案
                        } else {
                            mHistorySelect.setText("无");
                        }
                    } else {
                        mlayout_select_radio.setVisibility(View.GONE);
                    }
                    if ("4".equals(type)) {
                        //多选
                        StringBuilder sb = new StringBuilder();
                        mlayout_select_checkbox.setVisibility(View.VISIBLE);
                        mHistoryLouKan.setText(info.getTaskList().get(i).getTaskRemark());
                        if (null != info.getTaskList().get(i).getReturnfile() && info.getTaskList().get(i).getReturnfile().size() > 0) {
                            for (int j = 0; j < info.getTaskList().get(i).getReturnfile().size(); j++) {
                                if (j == info.getTaskList().get(i).getReturnfile().size() - 1) {
                                    sb.append(info.getTaskList().get(i).getReturnfile().get(j));
                                } else {
                                    sb.append(info.getTaskList().get(i).getReturnfile().get(j) + " ");
                                }
                            }
                        }
                        if (sb.length() > 0) {
                            mSelectCheckBox.setText(sb.toString());
                        } else {
                            mSelectCheckBox.setText("无");
                        }
                    } else {
                        mlayout_select_checkbox.setVisibility(View.GONE);
                    }
                    if ("5".equals(type)) {
                        //多选
                        mlayout_fill_the_blank_spaces.setVisibility(View.VISIBLE);
                        mFillTheBlankSpaces.setText(info.getTaskList().get(i).getTaskRemark());
                        if (null != info.getTaskList().get(i).getReturnfile() && info.getTaskList().get(i).getReturnfile().size() > 0) {
                            mHistorySuggest.setText(info.getTaskList().get(i).getReturnfile().get(0));
                        } else {
                            mHistorySuggest.setText("检测首选检测中心，首批通过CMA认证及北京市建委备案单位，面向社会提供公平公正检测服务");
                        }
                    } else {
                        mlayout_fill_the_blank_spaces.setVisibility(View.GONE);
                    }
                }*/
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }
}
