package com.risenb.witness.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.risenb.witness.R;
import com.risenb.witness.beans.BannerBean;
import com.risenb.witness.beans.HomeTaskInfoBean;
import com.risenb.witness.beans.HomeTaskInfoStateBean;
import com.risenb.witness.beans.WBaseBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.JsonResponseHandler;
import com.risenb.witness.pop.RobTaskFailPop;
import com.risenb.witness.pop.RobTaskLoginPop;
import com.risenb.witness.pop.RobTaskSuccessPop;
import com.risenb.witness.pop.RobTaskTrainPop;
import com.risenb.witness.pop.UserOutPop;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.ui.TabUI;
import com.risenb.witness.ui.tasklist.BannerView;
import com.risenb.witness.ui.tasklist.TaskListInfoUI;
import com.risenb.witness.ui.login.LoginUI;
import com.risenb.witness.ui.tasklist.UncertainEvidence;
import com.risenb.witness.utils.TimesUtils;
import com.risenb.witness.utils.newUtils.BannerUtils;
import com.risenb.witness.utils.newUtils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.hometaskinfo)
public class HomeTaskInfoUI extends BaseUI {
    /*@ViewInject(R.id.hometaskinfo_down_timer)
    private CountDownTimerView hometaskinfo_down_timer;*/
    @ViewInject(R.id.hometaskinfo_pb)
    private ProgressBar hometaskinfo_pb;

    private List<BannerBean> list = new ArrayList<>();

    @ViewInject(R.id.vp_banner)
    private ViewPager vp_banner;

    @ViewInject(R.id.ll_banner)
    private LinearLayout ll_banner;

    @ViewInject(R.id.tv_banner)
    private TextView tv_banner;

    @ViewInject(R.id.back)
    private RelativeLayout back;

    @ViewInject(R.id.hometaskinfo_title_tv)
    private TextView hometaskinfo_title_tv;
    @ViewInject(R.id.hometaskinfo_address_tv)
    private TextView hometaskinfo_address_tv;
    @ViewInject(R.id.hometaskinfo_price_tv)
    private TextView hometaskinfo_price_tv;

    private RobTaskSuccessPop robTaskSuccessPop;
    private RobTaskFailPop robTaskFailPop;
    private RobTaskLoginPop robTaskLoginPop;
    private RobTaskTrainPop robTaskTrainPop;

    private String taskid;
    private String trainid;

    private int istrain;

    public int info = 0;
    private String address = "";
    private String modelType;
    private int page;

    @Override
    protected void back() {
        if (info == 1) {
            Intent intent = new Intent(getActivity(), TabUI.class);
            intent.putExtra("type", 2);
            startActivity(intent);
        } else {
            finish();
        }
    }

    @Override
    protected void setControlBasis() {
        setTitle("任务详情");
        /*rightVisible("预览");*/
        rightVisible("");
        taskid = getIntent().getStringExtra("taskId");
        taskDetails();
    }

    @Override
    protected void prepareData() {

    }

    private void taskDetails() {
        Utils.getUtils().showProgressDialog(getActivity());
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.taskDetails));
        Map<String, String> param = new HashMap<>();
        param.put("taskId", taskid);
        if (!TextUtils.isEmpty(application.getC())) {
            param.put("c", application.getC());
        }
        MyOkHttp.get().post(getActivity(), url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                if (wBaseBean.getSuccess().equals("1")) {
                    HomeTaskInfoBean homeTaskInfoBean = com.alibaba.fastjson.JSONObject.parseObject(wBaseBean.getData(), HomeTaskInfoBean.class);
                    trainid = homeTaskInfoBean.getTrainid();
                    hometaskinfo_title_tv.setText(homeTaskInfoBean.getScheduleName());
                    hometaskinfo_address_tv.setText(homeTaskInfoBean.getAddress());

                    // daojishi(String.valueOf(homeTaskInfoBean.getRemainTime() * 1000));

                    address = homeTaskInfoBean.getAddress();
                    modelType = homeTaskInfoBean.getModeltype();
                    page = homeTaskInfoBean.getPage();
                    hometaskinfo_pb.setMax(Integer.valueOf(homeTaskInfoBean.getValidity()));
                    hometaskinfo_pb.setProgress(Integer.valueOf(homeTaskInfoBean.getRemainTime()));
                    hometaskinfo_price_tv.setText("￥".concat(homeTaskInfoBean.getPrice()));
                    istrain = homeTaskInfoBean.getIstrain();
                    List<String> imageList = homeTaskInfoBean.getImage();
                    if (imageList != null) {
                        for (String imgUrl : imageList) {
                            BannerBean bannerBean = new BannerBean();
                            bannerBean.setBannerImg(imgUrl);
                            list.add(bannerBean);
                        }
                    }
                    setBanener();
                } else {
                    if (wBaseBean.getErrorMsg().equals("登录异常")) {
                        errorLogin();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }

    private void setBanener() {
        BannerUtils<BannerBean> bannerUtils = new BannerUtils<>();
        bannerUtils.setActivity(getActivity());
        bannerUtils.setViewPager(vp_banner);
        bannerUtils.setDianGroup(ll_banner);
        bannerUtils.setTextView(tv_banner);
        bannerUtils.setList(list);
        bannerUtils.setColorTrue(getResources().getColor(R.color.main_green));
        bannerUtils.setColorFalse(getResources().getColor(R.color.colorGray));
        /*
         * ▲▲▲setDefaultImg()与setBaseBannerView()为重点，不然无法进行图片轮播
         */
        bannerUtils.setDefaultImg(R.drawable.default_image);
        bannerUtils.setBaseBannerView(new BannerView(getApplication(), list));
        bannerUtils.info();
        bannerUtils.start();
    }

    @Override
    public void onLoadOver() {

    }

    @OnClick(R.id.back)
    private void test(View v) {

    }

    /**
     * 预览
     */
    @OnClick(R.id.ll_right)
    private void ll_right(View v) {
        startActivity(new Intent(this, TaskInfoPreviewUI.class).putExtra("taskid", taskid));
    }

    private void daojishi(String tiem) {
        String time = TimesUtils.timeDifference(tiem);
        String[] split = time.split(",");
        int dd = Integer.valueOf(split[0]);
        int hh = Integer.valueOf(split[1]);
        int mm = Integer.valueOf(split[2]);
        int ss = Integer.valueOf(split[3]);
        /*if (dd >= 0 && hh >= 0 && mm >= 0 && ss >= 0) {
            hometaskinfo_down_timer.setTime(dd, hh, mm, ss);
        } else {
            hometaskinfo_down_timer.setTime(0, 0, 0, 0);
        }
        // 开始倒计时
        hometaskinfo_down_timer.start();*/
    }

    /**
     * 抢任务
     */
    @OnClick(R.id.hometaskinfo_qrw_tv)
    private void robHomePodTask(final View view) {
        if (TextUtils.isEmpty(application.getC())) {
            robTaskLoginPop = new RobTaskLoginPop(view, this, R.layout.robtaskloginpop);
            robTaskLoginPop.showAsDropDownInstance();
            robTaskLoginPop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.robtaskloginpop_ok_tv:
                            startActivity(new Intent(HomeTaskInfoUI.this, LoginUI.class));
                            robTaskLoginPop.dismiss();
                            break;
                    }
                }
            });
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.ImageloadingDialogStyle);
            dialog.setTitle("用户提示");
            dialog.setMessage("如果任务未得到执行会影响用户信誉，确定获取任务？");
            dialog.setCancelable(false);
            dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    robTaskOperation(view);
                }
            });

            dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            //设置对话框消失监听事件
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    private void robTaskOperation(final View view) {
        info = 1;
        if (istrain == 0) {
            robTaskTrainPop = new RobTaskTrainPop(view, this, R.layout.robtasktrainpop);
            robTaskTrainPop.showAsDropDownInstance();
            robTaskTrainPop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.robtasktrainpop_ok_tv:
                            Intent intent = new Intent(HomeTaskInfoUI.this, TaskInfoTrainUI.class);
                            intent.putExtra("id", trainid);
                            startActivityForResult(intent, 1);
                            robTaskTrainPop.dismiss();
                            break;
                    }
                }
            });
        } else {
            Utils.getUtils().showProgressDialog(this);
            String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.lootTask));
            Map<String, String> param = new HashMap<>();
            param.put("c", application.getC());
            param.put("taskId", taskid);
            MyOkHttp.get().post(getActivity(), url, param, new JsonResponseHandler() {
                @Override
                public void onSuccess(int statusCode, JSONObject response) {
                    Utils.getUtils().dismissDialog();
                    WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                    if (wBaseBean.getSuccess().equals("1")) {
                        HomeTaskInfoStateBean homeTaskInfoStateBean = com.alibaba.fastjson.JSONObject.parseObject(wBaseBean.getData(), HomeTaskInfoStateBean.class);
                        if (homeTaskInfoStateBean.getState() == 1) {
                            // 抢到任务
                            robTaskSuccessPop = new RobTaskSuccessPop(view, getActivity(), R.layout.robtasksuccesspop);
                            robTaskSuccessPop.showAsDropDownInstance();
                            robTaskSuccessPop.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    switch (view.getId()) {
                                        case R.id.robtasksuccess_qx_tv:
                                            Intent intent = new Intent(getActivity(), TabUI.class);
                                            intent.putExtra("type", 2);
                                            startActivity(intent);
                                            robTaskSuccessPop.dismiss();
                                            break;
                                        case R.id.robtasksuccess_ok_tv:
                                            Intent startTaskIntent = new Intent(getActivity(), UncertainEvidence.class);
                                            startTaskIntent.putExtra("mark", "NonExecution");
                                            startTaskIntent.putExtra("taskid", taskid);
                                            /*startTaskIntent.putExtra("robbedTaskSign", info);*/
                                            startTaskIntent.putExtra("address", address);
                                            startTaskIntent.putExtra("modeltype", modelType);
                                            startTaskIntent.putExtra("currentpage", page);
                                            startActivity(startTaskIntent);
                                            robTaskSuccessPop.dismiss();
                                            /*
                                             * 抢任务刷新HomeUI列表需在此处作修改
                                             */
                                            finish();
                                            break;
                                    }
                                }
                            });
                        } else if (homeTaskInfoStateBean.getState() == 2) {
                            // 任务被抢
                            robTaskFailPop = new RobTaskFailPop(view, getActivity(), R.layout.robtaskfailpop);
                            robTaskFailPop.showAsDropDownInstance();
                            robTaskFailPop.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    switch (view.getId()) {
                                        case R.id.robtaskfailpop_jixu_tv:
                                            Intent intent = new Intent(getActivity(), TabUI.class);
                                            intent.putExtra("type", 2);
                                            startActivity(intent);
                                            robTaskFailPop.dismiss();
                                            break;
                                    }
                                }
                            });
                        }
                    } else {
                        if (wBaseBean.getErrorMsg().equals("登录异常")) {
                            errorLogin();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, String error_msg) {
                    Utils.getUtils().dismissDialog();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 1) {
                // 培训页返回Code
                istrain = 1;
            }
        }
    }
}
