package com.risenb.witness.ui.tasklist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.pop.UserOutPop;
import com.risenb.witness.ui.login.LoginUI;

import org.xutils.x;

public abstract class BaseLazyFragment extends Fragment {
    protected boolean isVisible = false;
    private boolean isInitView = false;
    private boolean isFirstLoad = true;
    protected View mRootView;
    public MyApplication mApplication;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        x.view().inject(this, inflater, container);
        mApplication = MyApplication.getInstance();
        if (mRootView == null) {
            mRootView = initView(inflater, container, savedInstanceState);
        }
        isInitView = true;
        lazyLoadData();
        return mRootView;
    }

    protected abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            isVisible = true;
            lazyLoadData();
        } else {
            isVisible = false;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void lazyLoadData() {
        if (isFirstLoad) {
        } else {
        }
        if (!isFirstLoad || !isVisible || !isInitView) {
            return;
        }
        initData();
        isFirstLoad = false;
    }

    protected abstract void initData();

    /**
     * 登录错误
     */
    protected void errorLogin() {
        MyApplication.getInstance().setC("");
        final UserOutPop userOutPop = new UserOutPop(mRootView, getActivity(), R.layout.useroutpop);
        userOutPop.showAsDropDownInstance();
        userOutPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.useroutpop_qx_tv:
                        userOutPop.dismiss();
                        break;
                    case R.id.useroutpop_ok_tv:
                        startActivity(new Intent(getActivity(), LoginUI.class));
                        userOutPop.dismiss();
                        break;
                }
            }
        });
    }
}
