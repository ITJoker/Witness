package com.risenb.witness.ui.vip.Wallet;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.risenb.witness.utils.newUtils.Utils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.risenb.witness.R;
import com.risenb.witness.beans.RealNameBean;
import com.risenb.witness.beans.RealNameBeans;
import com.risenb.witness.beans.WBaseBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.JsonResponseHandler;
import com.risenb.witness.pop.RealNamePop;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.utils.Validator;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.realname)
public class RealNameUI extends BaseUI {
    @ViewInject(R.id.realname_wai_ll)
    private LinearLayout realname_wai_ll;
    @ViewInject(R.id.realname_wancheng_ll)
    private LinearLayout realname_wancheng_ll;
    @ViewInject(R.id.realname_renzheng_ll)
    private LinearLayout realname_renzheng_ll;
    @ViewInject(R.id.realname_name_et)
    private EditText realname_name_et;
    @ViewInject(R.id.realname_code_et)
    private EditText realname_code_et;

    @ViewInject(R.id.realname_okname_tv)
    private TextView realname_okname_tv;
    @ViewInject(R.id.realname_okcode_tv)
    private TextView realname_okcode_tv;
    @ViewInject(R.id.realname_okid_tv)
    private TextView realname_okid_tv;

    private RealNamePop realNamePop;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void exit() {
        back();
    }

    @Override
    protected void setControlBasis() {
        setTitle("实名认证");
    }

    @Override
    protected void prepareData() {
        if (getIntent().getIntExtra("type", -1) == 1) {
            realname_renzheng_ll.setVisibility(View.GONE);
            realname_wai_ll.setBackgroundColor(Color.rgb(255, 255, 255));
            realname_wancheng_ll.setVisibility(View.VISIBLE);
            approveMsg();
        }
    }

    private void approveMsg() {
        Utils.getUtils().showProgressDialog(getActivity());
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.approveMsg));
        Map<String, String> param = new HashMap<>();
        param.put("c", application.getC());
        MyOkHttp.get().post(this, url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                if (wBaseBean.getSuccess().equals("1")) {
                    RealNameBeans realNameBean = com.alibaba.fastjson.JSONObject.parseObject(wBaseBean.getData(), RealNameBeans.class);
                    realname_okname_tv.setText(realNameBean.getTruename());
                    String idNumber = realNameBean.getIdnumber();
                    String startID = idNumber.substring(0, 5);
                    String lastID = idNumber.substring(idNumber.length() - 4, idNumber.length());
                    realname_okcode_tv.setText(startID.concat("*********").concat(lastID));
                    realname_okid_tv.setText(realNameBean.getUserid());
                } else {
                    makeText(wBaseBean.getErrorMsg());
                    if (wBaseBean.getErrorMsg().equals("登录异常")) {
                        errorLogin();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
                makeText(error_msg);
            }
        });

    }

    @Override
    public void onLoadOver() {

    }

    /**
     * 下一步
     */
    @OnClick(R.id.realname_next_tv)
    private void realname_next_tv(View v) {
        if (TextUtils.isEmpty(realname_name_et.getText().toString().trim())) {
            makeText("请输入姓名");
            return;
        }
        if (TextUtils.isEmpty(realname_code_et.getText().toString().trim())) {
            makeText("请输入身份证");
            return;
        }
        if (!Validator.isIDCard(realname_code_et.getText().toString().trim())) {
            makeText("请正确输入身份证");
            return;
        }

        /*
         * 隐藏软键盘
         */
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        realNamePop = new RealNamePop(v, this, R.layout.realnamepop);
        realNamePop.showAsDropDownInstance();
        realNamePop.setData(realname_name_et.getText().toString().trim(), realname_code_et.getText().toString().trim());
        realNamePop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.realname_ok_tv:
                        realNameApprove();
                        break;
                }
            }
        });
    }

    private void realNameApprove() {
        Utils.getUtils().showProgressDialog(getActivity());
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.realNameApprove));
        Map<String, String> param = new HashMap<>();
        param.put("c", application.getC());
        param.put("trueName", realname_name_et.getText().toString().trim());
        param.put("userCode", realname_code_et.getText().toString().trim());
        MyOkHttp.get().post(this, url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                if (wBaseBean.getSuccess().equals("1")) {
                    RealNameBean realNameBean = com.alibaba.fastjson.JSONObject.parseObject(wBaseBean.getData(), RealNameBean.class);
                    makeText(wBaseBean.getErrorMsg());
                    realNamePop.dismiss();
                    realname_renzheng_ll.setVisibility(View.GONE);
                    realname_wai_ll.setBackgroundColor(Color.rgb(255, 255, 255));
                    realname_wancheng_ll.setVisibility(View.VISIBLE);
                    realname_okname_tv.setText(realNameBean.getTruename());
                    realname_okcode_tv.setText(realNameBean.getUsercode());
                    realname_okid_tv.setText(realNameBean.getUserid());
                } else {
                    makeText(wBaseBean.getErrorMsg());
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
