package com.risenb.witness.ui.tasklist.PlayVideo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.risenb.witness.R;

public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    /*
     *初始化界面:setContentview()和findViewById()
     */
    protected abstract void initView();

    /*
     *初始化数据
     */
    protected abstract void initData();

    /*
     *初始化监听器
     */
    protected abstract void initListener();

    /*
     *子类共享
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            default:
                processClick(v);
                break;
        }
    }

    /*
     * 处理点击事件
     */
    protected abstract void processClick(View v);
}
