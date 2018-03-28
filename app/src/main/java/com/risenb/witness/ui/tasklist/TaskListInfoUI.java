package com.risenb.witness.ui.tasklist;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.beans.BannerBean;
import com.risenb.witness.beans.BaseBeans;
import com.risenb.witness.beans.NonTaskDetailBean;
import com.risenb.witness.beans.RejectRason;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.GsonResponseHandler;
import com.risenb.witness.pop.RobTaskFailPop;
import com.risenb.witness.pop.VipSetClearPop;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.ui.TabUI;
import com.risenb.witness.ui.tasklist.fragment.ExecutingTaskFragment;
import com.risenb.witness.ui.tasklist.fragment.NonExecutionTaskFragment;
import com.risenb.witness.ui.tasklist.fragment.UploadedTaskFragment;
import com.risenb.witness.utils.SDCardUtils;
import com.risenb.witness.utils.SharedPreferencesUtil;
import com.risenb.witness.utils.StringUtils;
import com.risenb.witness.utils.TimesUtils;
import com.risenb.witness.utils.newUtils.BannerUtils;
import com.risenb.witness.utils.newUtils.Utils;
import com.risenb.witness.views.citypicker.CountDownTimerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaskListInfoUI extends BaseUI {
    //时间
    @BindView(R.id.task_info_countdowntimerview)
    CountDownTimerView mTaskinfoHhTv;
    //倒计时
    @BindView(R.id.nontasklistinfo_pb)
    ProgressBar nontasklistinfo_pb;

    @BindView(R.id.text_taskinfo_address)
    TextView mTaskinfoAddress;
    @BindView(R.id.taskdetailtitle)
    TextView mTaskDetailTitle;

    //价格
    @BindView(R.id.taskinfo_price)
    TextView mTaskinfoPrice;
    //开始/继续/查看/更正按键
    @BindView(R.id.taskinfo_qrw_tv)
    TextView mTaskinfoQrwTv;

    @BindView(R.id.completed_upload_title)
    LinearLayout mComletedUploadTitle;

    @BindView(R.id.Countdown_show)
    TextView CountdownShow;

    @BindView(R.id.vp_banner)
    ViewPager vp_banner;

    @ViewInject(R.id.ll_banner)
    LinearLayout ll_banner;

    @ViewInject(R.id.tv_banner)
    TextView tv_banner;

    public static boolean isClose = false;

    private String marks;
    private RobTaskFailPop robTaskFailPop;
    private VipSetClearPop vipSetClearPop;

    private String mNonTaskId;

    private String modeltype;
    private String totalpage;
    private int robbedTaskSign;

    //private Handler mHandler = new Handler()
    @Override
    protected void back() {
        if (robbedTaskSign == 1) {
            Intent intent = new Intent(getActivity(), TabUI.class);
            intent.putExtra("type", 2);
            startActivity(intent);
        }
        finish();
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        setContentView(R.layout.nontasklistinfo);
        ButterKnife.bind(this);
        marks = getIntent().getStringExtra("mark");
        robbedTaskSign = getIntent().getIntExtra("robbedTaskSign", -1);
    }

    @Override
    protected void setControlBasis() {
        if (marks.equals(NonExecutionTaskFragment.NONEXECUTIONFRAGMENT)) {
            setTitle("任务详情");
            mNonTaskId = getIntent().getStringExtra(NonExecutionTaskFragment.NONEXECUTIONFRAGMENTTASKID);
            String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.taskDetails));
            Map<String, String> param = new HashMap<>(2);
            param.put("c", MyApplication.getInstance().getC());
            param.put("taskId", mNonTaskId);
            onNonTaskAccessNetWorkDetail(url, param);
            onNonTaskAccessNetWork("1");
        } else if (marks.equals(ExecutingTaskFragment.EXECUTIONINGFRAGMENT)) {
            String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.taskDetails));
            setTitle("执行中");
            mTaskinfoQrwTv.setText("继续");
            mNonTaskId = getIntent().getStringExtra(ExecutingTaskFragment.EXECUTIONINGFRAGMENT);
            Map<String, String> param = new HashMap<>(2);
            param.put("c", MyApplication.getInstance().getC());
            param.put("taskId", mNonTaskId);
            onNonTaskAccessNetWorkDetail(url, param);
        } else if (marks.equals(UploadedTaskFragment.COMUPLOADFRAGMENT)) {
            setTitle("已上传");
            mTaskinfoQrwTv.setText("查看");
            mNonTaskId = getIntent().getStringExtra(UploadedTaskFragment.COMUPLOADFRAGMENT);
            String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.taskDetails));
            Map<String, String> param = new HashMap<>(2);
            param.put("c", MyApplication.getInstance().getC());
            param.put("taskId", mNonTaskId);
            onNonTaskAccessNetWorkDetail(url, param);
        } else if (marks.equals(RejectTaskFragment.REJECTASKFRAGEMNTFLAG)) {
            setTitle("被驳回");
            if (SharedPreferencesUtil.getBoolean(getApplication(), mNonTaskId + "Reject", false)) {
                mTaskinfoQrwTv.setText("更正");
            } else {
                mTaskinfoQrwTv.setText("查看");
            }
            mComletedUploadTitle.setVisibility(View.VISIBLE);
            mNonTaskId = getIntent().getStringExtra(RejectTaskFragment.REJECTASKFRAGEMNTFLAG);
            String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.taskDetails));
            Map<String, String> param = new HashMap<>();
            param.put("c", MyApplication.getInstance().getC());
            param.put("taskId", mNonTaskId);
            onNonTaskAccessNetWorkDetail(url, param);
            onNonTaskAccessNetWork("2");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isClose) {
            // 被驳回任务重新被上传，关闭当前页面
            finish();
        }
        // 没有上传任务，判断在被驳回中是否进行了更正操作
        if (SharedPreferencesUtil.getBoolean(getApplication(), mNonTaskId + "Reject", false)) {
            mTaskinfoQrwTv.setText("更正");
        }
    }

    @Override
    protected void prepareData() {

    }

    private void daojishi(String tiem) {
        String time = TimesUtils.timeDifference(tiem);
        String[] split = time.split(",");
        int dd = Integer.valueOf(split[0]);
        int hh = Integer.valueOf(split[1]);
        int mm = Integer.valueOf(split[2]);
        int ss = Integer.valueOf(split[3]);
        if (dd >= 0 && hh >= 0 && mm >= 0 && ss > 0) {
            mTaskinfoHhTv.setTime(dd, hh, mm, ss);
            // 开始倒计时
            mTaskinfoHhTv.start();
        } else {
            mTaskinfoHhTv.setTime(0, 0, 0, 0);
            CountdownShow.setText("任务已结束");
            mTaskinfoHhTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoadOver() {

    }

    @OnClick(R.id.back)
    public void test(View v) {

    }

    @OnClick(R.id.task_rejected_giveup)
    public void OnClickTaskRejected(View view) {
        vipSetClearPop = new VipSetClearPop(view, this, R.layout.rejecttaskgiveup);
        vipSetClearPop.showAsDropDownInstance();
        vipSetClearPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.vipsetclearpop_ok_tv:
                        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.giveuptask));
                        Map<String, String> param = new HashMap<>(2);
                        param.put("c", MyApplication.getInstance().getC());
                        param.put("taskId", mNonTaskId);
                        onGiveUpTaskNextWork(url, param);
                        vipSetClearPop.dismiss();
                        break;
                }
            }
        });
    }

    @OnClick(R.id.task_rejected_reson)
    public void OnClickTaskRejectedReason(View view) {
        robTaskFailPop = new RobTaskFailPop(view, this, R.layout.rejecttaskrson);
        robTaskFailPop.showAsDropDownInstance();
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.rejecttask));
        Map<String, String> param = new HashMap<>(2);
        param.put("c", MyApplication.getInstance().getC());
        param.put("taskId", mNonTaskId);
        onRejectTaskNextWork(url, param);
    }

    /**
     * ★★★开始取证
     */
    @OnClick(R.id.taskinfo_qrw_tv)
    public void onStartObtainEvidence(View view) {
        Intent intent = null;
        if (marks.equals(NonExecutionTaskFragment.NONEXECUTIONFRAGMENT)) {
            // ★★★未执行任务与三个模版
            if ("1".equals(modeltype)) {
                intent = new Intent(this, EvidenceFirst.class);
            } else if ("2".equals(modeltype)) {
                intent = new Intent(this, ModelTwoNonIssueState.class);
            } else {
                intent = new Intent(this, UncertainEvidence.class);
            }
        } else if (marks.equals(ExecutingTaskFragment.EXECUTIONINGFRAGMENT)) {
            // ★★★正在执行任务与三个模版
            if ("1".equals(modeltype)) {
                intent = new Intent(this, ExecEvidenceFirst.class);
            } else if ("2".equals(modeltype)) {
                intent = new Intent(this, ModelTwoExecIssueState.class);
            } else {
                intent = new Intent(this, UncertainExecEvidence.class);
            }
        } else if (marks.equals(UploadedTaskFragment.COMUPLOADFRAGMENT)) {
            // ★★★任务上传完成
            intent = new Intent(this, HistoryTask.class);
        } else if (marks.equals(RejectTaskFragment.REJECTASKFRAGEMNTFLAG)) {
            // ★★★更正任务
            if (SharedPreferencesUtil.getBoolean(getApplication(), mNonTaskId + "Reject", false)) {
                // 说明在被驳回页有过操作,直接跳转到未执行页读取数据
                intent = new Intent(this, UncertainExecEvidence.class);
            } else {
                // 跳转到被驳回页面
                intent = new Intent(this, RejectUI.class);
            }
        }
        intent.putExtra("marks", marks);
        intent.putExtra("modeltype", modeltype);
        intent.putExtra("page", totalpage);
        intent.putExtra("taskid", mNonTaskId);

        intent.putExtra("currentpage", 1);
        startActivity(intent);
    }

    public void onNonTaskAccessNetWorkDetail(String url, Map<String, String> param) {
        if (StringUtils.isSpace(url)) {
            Toast.makeText(this, "网络路径无效", Toast.LENGTH_LONG).show();
            return;
        }
        Utils.getUtils().showProgressDialog(this);

        MyOkHttp.get().post(this, url, param, new GsonResponseHandler<BaseBeans<NonTaskDetailBean>>() {
            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }

            @Override
            public void onSuccess(int statusCode, BaseBeans<NonTaskDetailBean> response) {
                NonTaskDetailBean data = response.getData();
                modeltype = data.getModeltype();
                totalpage = String.valueOf(data.getPage());
                mTaskDetailTitle.setText(data.getScheduleName());
                mTaskinfoAddress.setText(data.getAddress());
                mTaskinfoPrice.setText("￥" + data.getPrice());
                nontasklistinfo_pb.setMax(Integer.valueOf(data.getValidity()));
                nontasklistinfo_pb.setProgress(Integer.valueOf(data.getRemainTime()));
                List<String> imageList = data.getImage();
                ArrayList<BannerBean> bannerBeanArrayList = new ArrayList<>();
                for (String imgUrl : imageList) {
                    BannerBean bannerBean = new BannerBean();
                    bannerBean.setBannerImg(imgUrl);
                    bannerBeanArrayList.add(bannerBean);
                }

                BannerUtils<BannerBean> bannerUtils = new BannerUtils<>();
                bannerUtils.setActivity(getActivity());
                bannerUtils.setViewPager(vp_banner);
                bannerUtils.setDianGroup(ll_banner);
                bannerUtils.setTextView(tv_banner);
                bannerUtils.setList(bannerBeanArrayList);
                bannerUtils.setColorTrue(getResources().getColor(R.color.main_green));
                bannerUtils.setColorFalse(getResources().getColor(R.color.colorGray));
                bannerUtils.setDefaultImg(R.drawable.default_image);
                bannerUtils.setBaseBannerView(new BannerView(getApplication(), bannerBeanArrayList));
                bannerUtils.info();
                bannerUtils.start();

                daojishi(String.valueOf(data.getRemainTime() * 1000));

                Utils.getUtils().dismissDialog();
            }
        });
    }

    public void onGiveUpTaskNextWork(String url, Map<String, String> param) {
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
                Utils.getUtils().dismissDialog();
                String flag = response.getSuccess();
                if (flag.equals("1")) {
                    makeText("已放弃任务");
                    MyApplication.deletePicture(SDCardUtils.getSDCardPath() + MyApplication.photosCompressSaveUrl + mNonTaskId);
                } else {
                    makeText("放弃失败");
                }
            }
        });
    }

    /*
     * 判断任务是否已读
     */
    private void onNonTaskAccessNetWork(String type) {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.taskIsread));
        Map<String, String> param = new HashMap<>();
        param.put("taskId", mNonTaskId);
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

    public void onRejectTaskNextWork(String url, Map<String, String> param) {
        if (StringUtils.isSpace(url)) {
            Toast.makeText(this, "网络路径无效", Toast.LENGTH_LONG).show();
            return;
        }
        Utils.getUtils().showProgressDialog(this);
        MyOkHttp.get().post(this, url, param, new GsonResponseHandler<BaseBeans<RejectRason>>() {
            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }

            @Override
            public void onSuccess(int statusCode, BaseBeans<RejectRason> response) {
                Utils.getUtils().dismissDialog();
                RejectRason str = response.getData();
                robTaskFailPop.setData(str.getRejectInfo());
            }
        });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return isCosumenBackKey();
        }
        return false;
    }

    private boolean isCosumenBackKey() {
        // 这儿做返回键的控制，如果自己处理返回键逻辑就返回true，如果返回false,代表继续向下传递back事件，由系统取控制
        if (null != robTaskFailPop) {
            robTaskFailPop.dismiss();
        }
        if (null != vipSetClearPop) {
            vipSetClearPop.dismiss();
        }
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        isClose = false;
    }
}
