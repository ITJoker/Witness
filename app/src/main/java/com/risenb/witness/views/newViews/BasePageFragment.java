package com.risenb.witness.views.newViews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.risenb.witness.utils.newUtils.BaseMenuBean;
import com.risenb.witness.utils.newUtils.LoadOver;
import com.risenb.witness.utils.newUtils.OnLoadOver;

public abstract class BasePageFragment<T extends BaseMenuBean> extends Fragment implements OnLoadOver {
    protected View view;
    protected int position;
    protected T baseMenuBean;

    public BasePageFragment(T baseMenuBean, int position) {
        this.position = position;
        this.baseMenuBean = baseMenuBean;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.loadViewLayout(inflater, container);
        ViewUtils.inject(this, this.view);
        new LoadOver(this.getActivity(), this);
        this.setControlBasis();
        this.prepareData();
        return this.view;
    }

    public void onLoadOver() {
    }

    public void startActivity(Intent intent) {
        this.getActivity().startActivity(intent);
    }

    protected void makeText(String msg) {
        Toast.makeText(this.getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    protected abstract void loadViewLayout(LayoutInflater var1, ViewGroup var2);

    protected abstract void setControlBasis();

    protected abstract void prepareData();
}
