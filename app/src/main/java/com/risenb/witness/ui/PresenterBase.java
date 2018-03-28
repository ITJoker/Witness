package com.risenb.witness.ui;

import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.risenb.witness.R;
import com.risenb.witness.MyApplication;
import com.risenb.witness.utils.newUtils.MUtils;

/**
 * Presenter 基类
 */
public abstract class PresenterBase {
    protected FragmentActivity activity;
    protected MyApplication application;

    public FragmentActivity getActivity() {
        return activity;
    }

    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
        application = (MyApplication) activity.getApplication();
    }

    protected void makeText(final String content) {
        MUtils.getMUtils().getHandler().post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected String getUrl(int id) {
        return getActivity().getResources().getString(R.string.service_host_address).concat(getActivity().getString(id));
    }
}
