package com.risenb.witness.ui.home;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.adapter.AuthenticationDataShowAdapter;
import com.risenb.witness.beans.AuthenticationBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.GsonResponseHandler;
import com.risenb.witness.ui.BaseFragment;
import com.risenb.witness.ui.vip.AfterAdministratorAuthenticationListener;
import com.risenb.witness.ui.vip.ExclusiveAuthenticationUI;
import com.risenb.witness.utils.newUtils.Utils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class AuthenticationAdministratorFragment extends BaseFragment {
    @BindView(R.id.authentication_administrator_XRecyclerView)
    XRecyclerView authentication_administrator_XRecyclerView;

    public AuthenticationAdministratorFragment() {

    }

    @Override
    public View initView() {
        return View.inflate(getActivity(), R.layout.fragment_authentication_administrator, null);
    }

    @Override
    public void initData() {
        ExclusiveAuthenticationUI activity = (ExclusiveAuthenticationUI) getActivity();
        activity.setAfterAdministratorAuthenticationListener(new AfterAdministratorAuthenticationListener() {
            @Override
            public void updateAdministratorView() {
                getAuthenticationAdministrator();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        authentication_administrator_XRecyclerView.setLayoutManager(layoutManager);
        authentication_administrator_XRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        authentication_administrator_XRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        authentication_administrator_XRecyclerView.setArrowImageView(R.mipmap.iconfont_downgrey);
        authentication_administrator_XRecyclerView.setPullRefreshEnabled(true);
        authentication_administrator_XRecyclerView.setLoadingMoreEnabled(false);
        authentication_administrator_XRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getAuthenticationAdministrator();
            }

            @Override
            public void onLoadMore() {

            }
        });

        getAuthenticationAdministrator();
    }

    private void getAuthenticationAdministrator() {
        if (isVisible) {
            Utils.getUtils().showProgressDialog(getActivity());
        }
        String url = getString(R.string.service_host_address).concat(getString(R.string.ApproveOneList));
        Map<String, String> param = new HashMap<>();
        param.put("c", MyApplication.getInstance().getC());
        MyOkHttp.get().post(getContext(), url, param, new GsonResponseHandler<AuthenticationBean>() {
            @Override
            public void onSuccess(int statusCode, AuthenticationBean response) {
                MyApplication.handler.postDelayed(new Runnable() {
                    public void run() {
                        if (isVisible) {
                            Utils.getUtils().dismissDialog();
                        }
                    }
                }, 300);
                if (response.getSuccess() == 1) {
                    if (response.getData() != null && response.getData().size() > 0) {
                        AuthenticationDataShowAdapter authenticationDataShowAdapter = new AuthenticationDataShowAdapter(response.getData());
                        authentication_administrator_XRecyclerView.setAdapter(authenticationDataShowAdapter);
                    }
                    authentication_administrator_XRecyclerView.refreshComplete();
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }
}
