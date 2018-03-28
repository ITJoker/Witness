package com.risenb.witness.ui.vip;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.risenb.witness.utils.newUtils.UIManager;
import com.risenb.witness.utils.newUtils.Utils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.risenb.witness.R;
import com.risenb.witness.beans.ShareBean;
import com.risenb.witness.beans.VipReportBean;
import com.risenb.witness.beans.WBaseBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.JsonResponseHandler;
import com.risenb.witness.pop.UserOutPop;
import com.risenb.witness.pop.VipReportPop;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.ui.login.LoginUI;
import com.risenb.witness.utils.ShareUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.vipreport)
public class VipReportUI extends BaseUI implements ShareUtils.ShareResult {
    @ViewInject(R.id.back)
    private RelativeLayout back;
    @ViewInject(R.id.iv_right)
    private ImageView iv_right;
    @ViewInject(R.id.vipreport_yizhuce22)
    private TextView vipreport_yizhuce22;
    @ViewInject(R.id.vipreport_kaquan22)
    private TextView vipreport_kaquan22;
    @ViewInject(R.id.vipreport_renwuwancheng_tv)
    private TextView vipreport_renwuwancheng_tv;
    @ViewInject(R.id.vipreport_lingqujifen_tv)
    private TextView vipreport_lingqujifen_tv;
    @ViewInject(R.id.vipreport_lingqujiangjin_tv)
    private TextView vipreport_lingqujiangjin_tv;

    private VipReportPop vipReportPop;
    private ShareBean share;

    @Override
    protected void back() {
        UIManager.getInstance().popActivity(getClass());
    }

    @Override
    protected void setControlBasis() {
        setTitle("成绩单");
        rightVisible(R.drawable.share_btn_down_two);
        ShareUtils.getInstance().init();
        reportCard();
        reportShare();
    }

    @Override
    protected void prepareData() {

    }

    private void reportCard() {
        Utils.getUtils().showProgressDialog(getActivity());
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.report));
        Map<String, String> param = new HashMap<>();
        param.put("c", application.getC());
        MyOkHttp.get().post(this, url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                if (wBaseBean.getSuccess().equals("1")) {
                    VipReportBean vipReportBean = com.alibaba.fastjson.JSONObject.parseObject(wBaseBean.getData(), VipReportBean.class);
                    vipreport_yizhuce22.setText(vipReportBean.getRegisterTime());
                    vipreport_kaquan22.setText(vipReportBean.getCouponNumber());
                    vipreport_renwuwancheng_tv.setText(vipReportBean.getTaskCount());
                    vipreport_lingqujifen_tv.setText(vipReportBean.getGetIntegral());
                    if (vipReportBean.getGetBonus() != null) {
                        vipreport_lingqujiangjin_tv.setText(vipReportBean.getGetBonus());
                    }
                } else {
                    if (wBaseBean.getErrorMsg().equals("登录异常")) {
                        errorLogin();
                    }
                    makeText(wBaseBean.getErrorMsg());
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });

    }

    @Override
    public void onLoadOver() {

    }

    /**
     * 成绩单分享
     */
    public void reportShare() {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.reportShare));
        final Map<String, String> map = new HashMap<>();
        map.put("c", application.getC());
        MyOkHttp.get().post(this, url, map, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                if ("1".equals(wBaseBean.getSuccess())) {
                    share = com.alibaba.fastjson.JSONObject.parseObject(wBaseBean.getData(), ShareBean.class);
                } else {
                    makeText(wBaseBean.getErrorMsg());
                    if (wBaseBean.getErrorMsg().equals("登录异常")) {
                        errorLogin();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                makeText(error_msg);
            }
        });
    }

    //分享
    @OnClick(R.id.iv_right)
    private void iv_right(View view) {
        final String url = share.getShareurl() + "?C=" + application.getC();
        vipReportPop = new VipReportPop(view, this, R.layout.vipreportpop);
        vipReportPop.showAsDropDownInstance();
        vipReportPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.fenxiang_qq_ll:
                        ShareUtils.getInstance().share(VipReportUI.this, SHARE_MEDIA.QQ, share.getTitle(), share.getContent(), share.getImg(), url, VipReportUI.this);
                        vipReportPop.dismiss();
                        break;
                    case R.id.fenxiang_wx_ll:
                        ShareUtils.getInstance().share(VipReportUI.this, SHARE_MEDIA.WEIXIN, share.getTitle(), share.getContent(), share.getImg(), url, VipReportUI.this);
                        vipReportPop.dismiss();
                        break;
                    case R.id.fenxiang_pyq_ll:
                        ShareUtils.getInstance().share(VipReportUI.this, SHARE_MEDIA.WEIXIN_CIRCLE, share.getTitle(), share.getContent(), share.getImg(), url, VipReportUI.this);
                        vipReportPop.dismiss();
                        break;
                    case R.id.fenxiang_wb_ll:
                        ShareUtils.getInstance().share(VipReportUI.this, SHARE_MEDIA.SINA, share.getTitle(), share.getContent(), share.getImg(), url, VipReportUI.this);
                        vipReportPop.dismiss();
                        break;
                }
            }
        });
    }

    @Override
    public void success(SHARE_MEDIA platform) {
        makeText("分享成功");
    }

    @Override
    public void fail(SHARE_MEDIA platform) {
        makeText("分享失败");
    }

    @Override
    public void cancel(SHARE_MEDIA platform) {
        makeText("取消分享");
    }
}
