package com.risenb.witness.ui.vip;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.adapter.AuthenticationFragmentsPagerAdapter;
import com.risenb.witness.beans.BaseBeans;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.GsonResponseHandler;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.utils.newUtils.UIManager;
import com.risenb.witness.utils.newUtils.Utils;
import com.risenb.witness.views.newViews.NoTouchScrollViewPager;

import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.activity_exclusive_authentication_ui)
public class ExclusiveAuthenticationUI extends BaseUI implements View.OnClickListener {
    @ViewInject(R.id.authentication_media_owner_tv)
    TextView authentication_media_owner_tv;
    @ViewInject(R.id.authentication_media_owner_view)
    View authentication_media_owner_view;
    @ViewInject(R.id.authentication_administrator_tv)
    TextView authentication_administrator_tv;
    @ViewInject(R.id.authentication_administrator_view)
    View authentication_administrator_view;
    @ViewInject(R.id.authentication_code_et)
    EditText authentication_code_et;
    @ViewInject(R.id.authenticate_tv)
    TextView authenticate_tv;
    @ViewInject(R.id.authentication_vp)
    NoTouchScrollViewPager authentication_vp;
    private AuthenticationFragmentsPagerAdapter authenticationFragmentsPagerAdapter = new AuthenticationFragmentsPagerAdapter(getSupportFragmentManager());
    private AfterMediaOwnerAuthenticationListener afterMediaOwnerAuthenticationListener;
    private AfterAdministratorAuthenticationListener afterAdministratorAuthenticationListener;

    @Override
    protected void back() {
        UIManager.getInstance().popActivity(getClass());
    }

    @Override
    protected void exit() {
        back();
    }

    @Override
    protected void setControlBasis() {
        setTitle("专属认证");
    }

    @Override
    protected void prepareData() {
        authentication_media_owner_tv.setOnClickListener(this);
        authentication_administrator_tv.setOnClickListener(this);
        authenticate_tv.setOnClickListener(this);
        authentication_vp.setAdapter(authenticationFragmentsPagerAdapter);
        authentication_vp.setCurrentItem(0);
    }

    @Override
    public void onLoadOver() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.authentication_media_owner_tv:
                authentication_vp.setCurrentItem(0);
                selectBackgroundColor(authentication_media_owner_tv, authentication_administrator_tv);
                authentication_media_owner_view.setVisibility(View.VISIBLE);
                authentication_administrator_view.setVisibility(View.GONE);
                break;
            case R.id.authentication_administrator_tv:
                authentication_vp.setCurrentItem(1);
                selectBackgroundColor(authentication_administrator_tv, authentication_media_owner_tv);
                authentication_media_owner_view.setVisibility(View.GONE);
                authentication_administrator_view.setVisibility(View.VISIBLE);
                break;
            case R.id.authenticate_tv:
                authenticationOperation();
                break;
        }
    }

    private void authenticationOperation() {
        hintKbTwo();
        if (!TextUtils.isEmpty(authentication_code_et.getText().toString().trim())) {
            String url = "";
            if (authentication_vp.getCurrentItem() == 0) {
                //媒体主端认证
                url = getString(R.string.service_host_address).concat(getString(R.string.ApproveTwo));
            } else if (authentication_vp.getCurrentItem() == 1) {
                //管理员端认证
                url = getString(R.string.service_host_address).concat(getString(R.string.ApproveOne));
            }
            Map<String, String> param = new HashMap<>();
            param.put("c", MyApplication.getInstance().getC());
            param.put("ApproveCode", authentication_code_et.getText().toString().trim());
            Utils.getUtils().showProgressDialog(getActivity(), "认证中");
            MyOkHttp.get().post(getApplicationContext(), url, param, new GsonResponseHandler<BaseBeans>() {
                @Override
                public void onSuccess(int statusCode, BaseBeans response) {
                    Utils.getUtils().dismissDialog();
                    if ("1".equals(response.getSuccess())) {
                        if (authentication_vp.getCurrentItem() == 0 && afterMediaOwnerAuthenticationListener != null) {
                            afterMediaOwnerAuthenticationListener.updateMediaOwnerView();
                        } else if (authentication_vp.getCurrentItem() == 1 && afterAdministratorAuthenticationListener != null) {
                            afterAdministratorAuthenticationListener.updateAdministratorView();
                        }
                        authentication_code_et.setText("");
                    }
                    makeText(response.getErrorMsg());
                }

                @Override
                public void onFailure(int statusCode, String error_msg) {
                    Utils.getUtils().dismissDialog();
                }
            });
        } else {
            makeText("认证码不可为空");
        }
    }

    private void selectBackgroundColor(TextView one, TextView two) {
        one.setTextColor(getResources().getColor(R.color.main_green));
        two.setTextColor(getResources().getColor(R.color.main_gray));
    }

    public void setAfterMediaOwnerAuthenticationListener(AfterMediaOwnerAuthenticationListener afterMediaOwnerAuthenticationListener) {
        this.afterMediaOwnerAuthenticationListener = afterMediaOwnerAuthenticationListener;
    }

    public void setAfterAdministratorAuthenticationListener(AfterAdministratorAuthenticationListener afterAdministratorAuthenticationListener) {
        this.afterAdministratorAuthenticationListener = afterAdministratorAuthenticationListener;
    }
}
