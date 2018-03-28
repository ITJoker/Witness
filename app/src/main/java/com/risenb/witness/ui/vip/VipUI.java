package com.risenb.witness.ui.vip;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.risenb.witness.R;
import com.risenb.witness.beans.MessageState;
import com.risenb.witness.beans.PersonageBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.GsonResponseHandler;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.ui.login.LoginUI;
import com.risenb.witness.ui.vip.Wallet.WalletUI;
import com.risenb.witness.utils.MyConfig;
import com.risenb.witness.utils.NetWorksUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 个人中心
 */
@ContentView(R.layout.vip)
public class VipUI extends BaseUI {
    @ViewInject(R.id.title_line)
    View title_line;

    @ViewInject(R.id.vip_usericon_iv)
    private ImageView vip_usericon_iv;
    @ViewInject(R.id.vip_nickName_tv)
    private TextView vip_nickName_tv;
    @ViewInject(R.id.vip_user_tel_tv)
    private TextView vip_user_tel_tv;
    @ViewInject(R.id.user_money_number_tv)
    private TextView user_money_number_tv;
    @ViewInject(R.id.vipset_service_tel_tv)
    private TextView vipset_service_tel_tv;
    // 是否有未读消息
    @ViewInject(R.id.messageState)
    private ImageView messageState;

    private PersonageBean.DataBean data;
    private String serviceTel;

    final int REQUEST_CODE_ASK_CALL_PHONE_PERMISSIONS = 10100;

    @Override
    protected void back() {
        exit();
    }

    @Override
    protected void setControlBasis() {
        backGone();
        setTitle("个人中心");
        /*setTitleBackground(73, 75, 90);*/
        /*setTitleDrawableBackground(R.drawable.vip_background);*/
        /*setTitleDrawableBackground(R.drawable.vip_background_one);*/
        title_line.setVisibility(View.GONE);
    }

    @Override
    protected void prepareData() {
        if (!NetWorksUtils.isNetworkAvailable(VipUI.this)) {
            makeText("当前没有可用网络");
        }
        /*if (TextUtils.isEmpty(application.getC())) {
            rightVisible("登录");
        } else {
            rightVisible("");
        }*/
        messageState();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        messageState();
    }

    /**
     * 未读消息状态
     */
    public void messageState() {
        /*Utils.getUtils().showProgressDialog(getActivity());*/
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.messageState));
        Map<String, String> param = new HashMap<>();
        param.put("c", application.getC());
        MyOkHttp.get().post(getApplication(), url, param, new GsonResponseHandler<MessageState>() {
            @Override
            public void onFailure(int statusCode, String error_msg) {
                /*Utils.getUtils().dismissDialog();*/
            }

            @Override
            public void onSuccess(int statusCode, MessageState response) {
                /*Utils.getUtils().dismissDialog();*/
                if (response != null || 1 == response.getSuccess()) {
                    if (Integer.parseInt(response.getData()) > 0) {
                        messageState.setVisibility(View.VISIBLE);
                    } else {
                        messageState.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!NetWorksUtils.isNetworkAvailable(VipUI.this)) {
            makeText("当前没有可用网络");
            return;
        }
        if (TextUtils.isEmpty(application.getC())) {
            vip_nickName_tv.setText("");
            vip_user_tel_tv.setText("");
            user_money_number_tv.setText("");
            vipset_service_tel_tv.setText("");
            ImageLoader.getInstance().displayImage("", vip_usericon_iv, MyConfig.optionss);
            /*rightVisible("登录");*/
        } else {
            rightVisible("");
            personalCenter();
        }
    }

    private void personalCenter() {
        /*Utils.getUtils().showProgressDialog(getActivity());*/
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.personalPersonage));
        Map<String, String> param = new HashMap<>();
        param.put("c", application.getC());
        MyOkHttp.get().post(this, url, param, new GsonResponseHandler<PersonageBean>() {
            @Override
            public void onSuccess(int statusCode, PersonageBean response) {
                /*Utils.getUtils().dismissDialog();*/
                if (response.getSuccess() == 1) {
                    if (response.getData() != null) {
                        data = response.getData();
                        ImageLoader.getInstance().displayImage(data.getHeadPic(), vip_usericon_iv, MyConfig.options);
                        vip_nickName_tv.setText(data.getNickName());
                        vip_user_tel_tv.setText(data.getUserTel());
                        application.setTel(data.getUserTel());
                        user_money_number_tv.setText(data.getBalance().concat(" 元"));
                        serviceTel = data.getTel();
                        if (!TextUtils.isEmpty(serviceTel))
                            vipset_service_tel_tv.setText(serviceTel);
                    }
                } else {
                    application.setC("");
                    onResume();
                    if ("登录异常".equals(response.getErrorMsg())) {
                        if (!application.getOne())
                            errorLogin();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                /*Utils.getUtils().dismissDialog();*/
                errorLogin();
            }
        });

    }

    @Override
    public void onLoadOver() {

    }

    /*//大头像
    @OnClick(R.id.vip_usericon_iv)
    private void vip_usericon_iv(View view) {
        if (!NetWorksUtils.isNetworkAvailable(VipUI.this)) {
            makeText("当前没有可用网络");
            return;
        }
        if (!TextUtils.isEmpty(application.getC()) && data != null) {
            startActivity(new Intent(this, ImageShower.class).putExtra("image", data.getHeadPic()));
        } else {
            startActivity(new Intent(this, LoginUI.class));
        }
    }*/

    //个人信息
    @OnClick(R.id.vip_user_info_ll)
    private void vip_nickName_tv(View view) {
        if (!NetWorksUtils.isNetworkAvailable(VipUI.this)) {
            makeText("当前没有可用网络");
            return;
        }
        if (!TextUtils.isEmpty(application.getC())) {
            startActivity(new Intent(this, VipInfoUI.class));
        } else {
            startActivity(new Intent(this, LoginUI.class));
        }
    }

    //设置
    @OnClick(R.id.vip_set_ll)
    private void vipSet(View view) {
        if (!NetWorksUtils.isNetworkAvailable(VipUI.this)) {
            makeText("当前没有可用网络");
            return;
        }
        if (!TextUtils.isEmpty(application.getC())) {
            startActivity(new Intent(this, VipSetUI.class));
        } else {
            startActivity(new Intent(this, LoginUI.class));
        }
    }

    //消息
    @OnClick(R.id.vip_messge_ll)
    private void vipMessage(View view) {
        if (!NetWorksUtils.isNetworkAvailable(VipUI.this)) {
            makeText("当前没有可用网络");
            return;
        }
        if (!TextUtils.isEmpty(application.getC())) {
            startActivity(new Intent(this, VipMessageUI.class));
        } else {
            startActivity(new Intent(this, LoginUI.class));
        }
    }

    //成绩单
    @OnClick(R.id.vip_report_ll)
    private void VipReport(View view) {
        if (!NetWorksUtils.isNetworkAvailable(VipUI.this)) {
            makeText("当前没有可用网络");
            return;
        }
        if (!TextUtils.isEmpty(application.getC())) {
            startActivity(new Intent(this, VipReportUI.class));
        } else {
            startActivity(new Intent(this, LoginUI.class));
        }
    }

    //钱包
    @OnClick(R.id.vip_wallet_ll)
    private void VipWallet(View view) {
        if (!NetWorksUtils.isNetworkAvailable(VipUI.this)) {
            makeText("当前没有可用网络");
            return;
        }
        if (!TextUtils.isEmpty(application.getC())) {
            startActivity(new Intent(this, WalletUI.class));
        } else {
            startActivity(new Intent(this, LoginUI.class));
        }
    }

    //专属认证
    @OnClick(R.id.vip_authentication_ll)
    private void VipAuthentication(View view) {
        if (!NetWorksUtils.isNetworkAvailable(VipUI.this)) {
            makeText("当前没有可用网络");
            return;
        }
        if (!TextUtils.isEmpty(application.getC())) {
            startActivity(new Intent(this, ExclusiveAuthenticationUI.class));
        } else {
            startActivity(new Intent(this, LoginUI.class));
        }
    }

    //登录
    @OnClick(R.id.tv_right)
    private void tv_right(View view) {
        if (!NetWorksUtils.isNetworkAvailable(VipUI.this)) {
            makeText("当前没有可用网络");
            return;
        }
        if (TextUtils.isEmpty(application.getC())) {
            startActivity(new Intent(this, LoginUI.class));
        }
    }

    //帮助文档
    @OnClick(R.id.vipset_document_ll)
    private void vipDocument(View view) {
        startActivity(new Intent(this, VipSetDocumentUI.class));
    }

    //新手
    @OnClick(R.id.vipset_news_ll)
    private void vipNews(View view) {
        startActivity(new Intent(this, VipNewsUI.class));
    }

    //意见反馈
    @OnClick(R.id.vipset_feedback_ll)
    private void vipFeedback(View view) {
        if (!TextUtils.isEmpty(application.getC())) {
            startActivity(new Intent(this, VipSetFeedbackUI.class));
        } else {
            startActivity(new Intent(this, LoginUI.class));
        }
    }

    //客服电话
    @OnClick(R.id.vipset_service_tel_ll)
    private void vipCallServiceTel(View view) {
        callPhoneNumber();
//        // 检查是否获得了权限（Android6.0运行时权限）
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                // 没有获得授权，申请授权
//                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
//                 /*
//                  * 如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
//                  * 如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
//                  * 如果设备策略禁止应用拥有这条权限, 这个方法也返回false
//                  */
//
//                    // 跳转到应用设置界面，让用户手动授权
//                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                    Uri uri = Uri.fromParts("package", getPackageName(), null);
//                    intent.setData(uri);
//                    startActivity(intent);
//                } else {
//                    // 直接请求授权
//                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_ASK_CALL_PHONE_PERMISSIONS);
//                }
//            } else {
//                // 已经获得授权，可以打电话
//                callPhoneNumber();
//            }
//        } else {
//            callPhoneNumber();
//        }
    }

//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_CODE_ASK_CALL_PHONE_PERMISSIONS: {
//                if (permissions[0].equals(Manifest.permission.CALL_PHONE) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // 权限获取成功
//                    callPhoneNumber();
//                } else {
//                    makeText("授权失败");
//                }
//                break;
//            }
//        }
//    }

    private void callPhoneNumber() {
        if (!TextUtils.isEmpty(serviceTel)) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + serviceTel));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            startActivity(new Intent(this, LoginUI.class));
        }
    }
}
