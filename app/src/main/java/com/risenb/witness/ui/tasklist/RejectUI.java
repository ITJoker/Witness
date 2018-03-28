package com.risenb.witness.ui.tasklist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.beans.BaseBeans;
import com.risenb.witness.beans.HistoryCompleted;
import com.risenb.witness.beans.RejectRason;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.GsonResponseHandler;
import com.risenb.witness.pop.RobTaskFailPop;
import com.risenb.witness.pop.VipSetClearPop;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.ui.tasklist.adapter.HistoryAdapter;
import com.risenb.witness.utils.SDCardUtils;
import com.risenb.witness.utils.SharedPreferencesUtil;
import com.risenb.witness.utils.StringUtils;
import com.risenb.witness.utils.newUtils.UIManager;
import com.risenb.witness.utils.newUtils.Utils;
import com.risenb.witness.views.citypicker.CountDownTimerView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 模板3，纯拍照任务
 */
public class RejectUI extends BaseUI {
    @BindView(R.id.history_task_listview)
    ListView mHistoryTaskListView;

    private TextView mHistoryAddress;
    private TextView mHistoryState;
    private TextView mHistoryRemark;
    private TextView mHistoryPrice;
    private CountDownTimerView mHistoryTimerView;
    private ProgressBar mProgressBar;
    private HistoryAdapter adapter;

    private String taskid;
    private String modeltype;
    private String isType;

    private LinearLayout layoutRoot;
    private TextView mCountdownTitle;
    private List<String> returnFile;
    Handler handler;
    int recorder = 0;

    private RobTaskFailPop robTaskFailPop;
    private VipSetClearPop vipSetClearPop;
    private String address;
    private String price;
    private String execution;
    private TextView historyTaskRejectReason;

    @Override
    protected void setControlBasis() {
        setTitle("驳回详情");
    }

    @Override
    protected void onInitCreate(Bundle savedInstanceState) {
        setContentView(R.layout.history_task_layout);
        ButterKnife.bind(this);
        taskid = getIntent().getStringExtra("taskid");
        modeltype = getIntent().getStringExtra("modeltype");
        address = getIntent().getStringExtra("address");
        price = getIntent().getStringExtra("price");
        execution = getIntent().getStringExtra("execution");
        String note = getIntent().getStringExtra("note");

        //顶布局
        View mHistoryHeadView = LayoutInflater.from(this).inflate(R.layout.histotytask_head_layout, null, false);
        mHistoryAddress = mHistoryHeadView.findViewById(R.id.historytask_adrress);
        /*mHistoryAddress.setVisibility(View.GONE);*/
        historyTaskRejectReason = mHistoryHeadView.findViewById(R.id.history_task_reject_reason);
        /*TextView mHistoryRejectReason = mHistoryHeadView.findViewById(R.id.historytask_reject_reason);
        mHistoryRejectReason.setVisibility(View.VISIBLE);*/
        TextView taskMarkWord = mHistoryHeadView.findViewById(R.id.task_mark_tv);
        if(!TextUtils.isEmpty(note)) {
            taskMarkWord.setText("备注：".concat(note));
        } else {
            taskMarkWord.setText("备注：无");
        }
        mHistoryState = mHistoryHeadView.findViewById(R.id.historytask_state);
        mHistoryRemark = mHistoryHeadView.findViewById(R.id.historytask_remark);
        mHistoryPrice = mHistoryHeadView.findViewById(R.id.historytask_price);
        mHistoryTimerView = mHistoryHeadView.findViewById(R.id.historytask_countdown);
        mCountdownTitle = mHistoryHeadView.findViewById(R.id.historytask_countdown_title);
        mProgressBar = mHistoryHeadView.findViewById(R.id.historytask_progressbar);
        LinearLayout uploaded_task_price_and_countdown_ll = mHistoryHeadView.findViewById(R.id.uploaded_task_price_and_countdown_ll);
        if ("3".equals(getIntent().getStringExtra("execution"))) {
            //执行方式(1.专业人员、2.客户检测员、3.奖池)
            uploaded_task_price_and_countdown_ll.setVisibility(View.VISIBLE);
        } else {
            uploaded_task_price_and_countdown_ll.setVisibility(View.GONE);
        }
        mHistoryTaskListView.addHeaderView(mHistoryHeadView);

        //底布局
        layoutRoot = new LinearLayout(this);
        AbsListView.LayoutParams layoutParamsRoot = new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutRoot.setLayoutParams(layoutParamsRoot);
        layoutRoot.setOrientation(LinearLayout.HORIZONTAL);
        Button button = getButton("更正任务", R.color.main_green, R.color.white);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (handler == null) {
                    handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            switch (msg.what) {
                                case 1:
                                    SharedPreferencesUtil.saveBoolean(getApplication(), taskid + "Reject", true);
                                    Intent intent = new Intent(RejectUI.this, UncertainExecEvidence.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("taskid", taskid);
                                    bundle.putString("modeltype", modeltype);
                                    bundle.putInt("currentpage", 1);
                                    bundle.putString("execution", execution);
                                    bundle.putString("address", address);
                                    bundle.putString("price", price);
                                    bundle.putString("waterMarkID", getIntent().getStringExtra("waterMarkID"));
                                    bundle.putString("waterMarkNT", getIntent().getStringExtra("waterMarkNT"));
                                    bundle.putString("waterMarkXY", getIntent().getStringExtra("waterMarkXY"));
                                    bundle.putString("waterMarkST", getIntent().getStringExtra("waterMarkST"));
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    UIManager.getInstance().pushActivity(RejectUI.this);
                                    Utils.getUtils().dismissDialog();
                                    finish();
                                    break;
                                case 2:
                                    Utils.getUtils().dismissDialog();
                                    makeText("照片保存本地失败");
                                    break;
                                case 3:
                                    makeText("正在保存第" + recorder + "张照片");
                                    break;
                            }
                        }
                    };
                }
                saveBitmapToFiles();
            }
        });

        Button giveUpTaskButton = getButton("放弃任务", R.color.background, R.color.main_green);
        giveUpTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickTaskRejected(v);
            }
        });

        layoutRoot.addView(giveUpTaskButton);
        layoutRoot.addView(button);
        mHistoryTaskListView.addFooterView(layoutRoot);

        RequestNetWork();
        rejectTaskReason();
        onJudgeTaskReadState("2");
    }

    @NonNull
    private Button getButton(String word, int backgroundColor, int textColor) {
        Button button = new Button(getApplication());
        LinearLayout.LayoutParams layoutParamsButton = new LinearLayout.LayoutParams(0, (int) getResources().getDimension(R.dimen.dm080));
        /*layoutParamsButton.gravity = Gravity.CENTER_HORIZONTAL;
        layoutParamsButton.topMargin = (int) getResources().getDimension(R.dimen.dm030);
        layoutParamsButton.bottomMargin = (int) getResources().getDimension(R.dimen.dm030);
        layoutParamsButton.leftMargin = (int) getResources().getDimension(R.dimen.dm090);
        layoutParamsButton.rightMargin = (int) getResources().getDimension(R.dimen.dm090);*/
        layoutParamsButton.weight = 1;
        button.setLayoutParams(layoutParamsButton);
        /*button.setBackgroundResource(R.drawable.circular_green_login_bg);*/
        button.setBackgroundColor(getResources().getColor(backgroundColor));
        button.setGravity(Gravity.CENTER);
        button.setPadding(0, 0, 0, 0);
        button.setText(word);
        button.setTextColor(getResources().getColor(textColor));
        button.setTextSize(getResources().getDimension(R.dimen.dm010));
        return button;
    }

    private void RequestNetWork() {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.newScheduleUploadedDetails));
        Map<String, String> params = new HashMap<>();
        params.put("taskId", taskid);
        params.put("c", MyApplication.getInstance().getC());
        params.put("modeltype", modeltype);
        params.put("page", "1");
        onHistoryTaskAccessNetWorkDetail(url, params);
    }

    private void onHistoryTaskAccessNetWorkDetail(String url, Map<String, String> params) {
        Utils.getUtils().showProgressDialog(this);
        MyOkHttp.get().post(getApplication(), url, params, new GsonResponseHandler<BaseBeans<HistoryCompleted>>() {
            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }

            @Override
            public void onSuccess(int statusCode, BaseBeans<HistoryCompleted> response) {
                Utils.getUtils().dismissDialog();
                HistoryCompleted data = response.getData();
                isType = data.getTaskList().get(0).getIsType();
                returnFile = data.getTaskList().get(0).getReturnfile();
                if (isType.equals("1")) {
                    /*
                     * Todo 以后需要做详细的视频/照片展示区别
                     */
                    adapter = new HistoryAdapter(getApplication(), data.getTaskList());
                    adapter.sign = "Reject";
                    adapter.historyAdapterTaskID = taskid;
                    mHistoryTaskListView.setAdapter(adapter);
                }
                mHistoryAddress.setText(data.getAddress());
                mHistoryPrice.setText("￥".concat(data.getPrice()));
                mHistoryTimerView.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);
                mCountdownTitle.setVisibility(View.GONE);

                if (!TextUtils.isEmpty(data.getIssuestate())) {
                    mHistoryState.setText("上刊状态：".concat(data.getIssuestate()));
                } else {
                    mHistoryState.setText("上刊状态：无");
                }

                if (!TextUtils.isEmpty(data.getScheduleRemark())) {
                    mHistoryRemark.setText("上刊备注：".concat(data.getScheduleRemark()));
                } else {
                    mHistoryRemark.setText("上刊备注：无");
                }

                layoutRoot.measure(0, 0);
                ViewGroup.LayoutParams layoutParams = layoutRoot.getLayoutParams();
                layoutParams.height = layoutRoot.getMeasuredHeight();
                layoutRoot.setLayoutParams(layoutParams);
            }
        });
    }

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void prepareData() {

    }

    @Override
    public void onLoadOver() {

    }

    private void saveBitmapToFiles() {
        if (TextUtils.isEmpty(taskid)) {
            makeText("任务ID错误");
        }
        if (returnFile != null && returnFile.size() > 0) {
            Utils.getUtils().showProgressDialog(RejectUI.this);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MyApplication.deletePicture(SDCardUtils.getSDCardPath() + MyApplication.photosCompressSaveUrl + taskid);
                    for (int i = 0; i < returnFile.size(); i++) {
                        try {
                            String imageFilePath = SDCardUtils.getSDCardPath() + MyApplication.photosCompressSaveUrl + taskid + "/";
                            File file = new File(imageFilePath, i + ".jpg");
                            if (file.exists()) {
                                // 文件存在
                                MyApplication.deletePicture(SDCardUtils.getSDCardPath() + MyApplication.photosCompressSaveUrl + taskid);
                            } else {
                                if (!file.getParentFile().exists()) {
                                    // 文件不存在则新建
                                    file.getParentFile().mkdir();
                                }
                                file.createNewFile();
                            }
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            URL url = new URL("http://www.adexmall.net/" + returnFile.get(i));
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setConnectTimeout(5000);
                            if (conn.getResponseCode() == 200) {
                                //获取服务器返回回来的流
                                InputStream inputStream = conn.getInputStream();
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
                                FileOutputStream fileOutputStream = new FileOutputStream(file);
                                fileOutputStream.write(byteArrayOutputStream.toByteArray());
                                fileOutputStream.flush();
                                fileOutputStream.close();
                                bitmap.recycle();
                                recorder++;
                                Message message = new Message();
                                message.what = 3;
                                handler.sendMessage(message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (recorder == returnFile.size()) {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    } else {
                        // 照片保存数量有误
                        recorder = 0;
                        Message message = new Message();
                        message.what = 2;
                        handler.sendMessage(message);
                    }
                }
            }).start();
        }
    }

    public void OnClickTaskRejected(View view) {
        vipSetClearPop = new VipSetClearPop(view, this, R.layout.rejecttaskgiveup);
        vipSetClearPop.showAsDropDownInstance();
        vipSetClearPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.vipsetclearpop_ok_tv:
                        giveUpTask();
                        vipSetClearPop.dismiss();
                        break;
                }
            }
        });
    }

    public void giveUpTask() {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.giveuptask));
        Map<String, String> param = new HashMap<>();
        param.put("c", MyApplication.getInstance().getC());
        param.put("taskId", taskid);

        MyOkHttp.get().post(this, url, param, new GsonResponseHandler<BaseBeans>() {
            @Override
            public void onFailure(int statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(int statusCode, BaseBeans response) {
                String flag = response.getSuccess();
                if (flag.equals("1")) {
                    makeText("已放弃任务");
                    MyApplication.deletePicture(SDCardUtils.getSDCardPath() + MyApplication.photosCompressSaveUrl + taskid);
                    finish();
                } else {
                    makeText("放弃失败");
                }
            }
        });
    }

    public void OnClickTaskRejectedReason(View view) {
        robTaskFailPop = new RobTaskFailPop(view, this, R.layout.rejecttaskrson);
        robTaskFailPop.showAsDropDownInstance();
        rejectTaskReason();
    }

    public void rejectTaskReason() {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.rejecttask));
        Map<String, String> param = new HashMap<>();
        param.put("c", MyApplication.getInstance().getC());
        param.put("taskId", taskid);

        MyOkHttp.get().post(this, url, param, new GsonResponseHandler<BaseBeans<RejectRason>>() {
            @Override
            public void onFailure(int statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(int statusCode, BaseBeans<RejectRason> response) {
                /*robTaskFailPop.setData(response.getData().getRejectInfo());*/
                historyTaskRejectReason.setText(response.getData().getRejectInfo());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * 判断任务状态
     */
    private void onJudgeTaskReadState(String type) {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.taskIsread));
        Map<String, String> param = new HashMap<>();
        param.put("taskId", taskid);
        param.put("c", MyApplication.getInstance().getC());
        param.put("type", type);
        MyOkHttp.get().post(this, url, param, new GsonResponseHandler<BaseBeans>() {
            @Override
            public void onFailure(int statusCode, String error_msg) {

            }

            @Override
            public void onSuccess(int statusCode, BaseBeans response) {
                String flag = response.getSuccess();
                if ("1".equals(flag)) {
                    System.out.println("任务已读");
                } else if ("2".equals(flag)) {
                    System.out.println("任务未读");
                }
            }
        });
    }
}
