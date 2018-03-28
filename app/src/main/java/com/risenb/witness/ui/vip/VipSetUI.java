package com.risenb.witness.ui.vip;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.risenb.witness.utils.newUtils.Utils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.risenb.witness.R;
import com.risenb.witness.pop.VipSetClearPop;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.utils.DataCleanManager;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

@ContentView(R.layout.vipset)
public class VipSetUI extends BaseUI {
    @ViewInject(R.id.back)
    private RelativeLayout back;

    private VipSetClearPop vipSetClearPop;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("设置");
    }

    @Override
    protected void prepareData() {

    }

    @Override
    public void onLoadOver() {

    }

    //清楚缓存
    @OnClick(R.id.vipset_clearcache_ll)
    private void clearCache(View view) {
        vipSetClearPop = new VipSetClearPop(view, this, R.layout.vipsetclearpop);
        vipSetClearPop.showAsDropDown(view);
        vipSetClearPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.vipsetclearpop_ok_tv:
                        DataCleanManager.cleanInternalCache(getActivity());
                        vipSetClearPop.dismiss();
                        makeText("清除缓存成功");
                        break;
                }
            }
        });
    }

    //退出登录
    @OnClick(R.id.vipset_userout_tv)
    private void vipset_userout_tv(View view) {
        if (TextUtils.isEmpty(application.getC())) {
            makeText("请登录");
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(VipSetUI.this);
            builder.setMessage("确认退出吗?");
            builder.setTitle("提示");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    application.setC("");
                    JPushInterface.setAlias(VipSetUI.this, "", new TagAliasCallback() {
                        @Override
                        public void gotResult(int responseCode, String s, Set<String> set) {
                            Utils.getUtils().dismissDialog();
                        }
                    });
                    VipSetUI.this.finish();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }

    //消息推送
    @OnClick(R.id.vipset_messge_ll)
    private void messge(View view) {
        startActivity(new Intent(this, VipSetMessgeUI.class));
    }

    //更改密码
    @OnClick(R.id.vipinfo_changepass_ll)
    private void changePass(View view) {
        startActivity(new Intent(this, VipChangePassUI.class));
    }

    //关于
    @OnClick(R.id.vipset_about_ll)
    private void about(View view) {
        startActivity(new Intent(this, VipSetAboutUI.class));
    }
}
