package com.risenb.witness.utils;

import android.text.TextUtils;

import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.network.OkHttpUtils.NetUtils;
import com.risenb.witness.utils.newNetwork.HttpBack;
import com.risenb.witness.utils.newNetwork.ReqParams;

import java.io.File;

/**
 * 调用接口
 */
public class NetworkUtils {
    private static NetworkUtils networkUtils;
    protected MyApplication application;

    public static NetworkUtils getNetworkUtils() {
        if (networkUtils == null) {
            networkUtils = new NetworkUtils();
        }
        return networkUtils;
    }

    public void setApplication(MyApplication application) {
        this.application = application;
    }

    private String getUrl(int id) {
        return application.getResources().getString(R.string.service_host_address).concat(application.getString(id));
    }

    private ReqParams getReqParams() {
        ReqParams param = new ReqParams();
        if (!TextUtils.isEmpty(application.getC())) {
            param.addParam("c", application.getC());
        }
        return param;
    }

    /**
     * @param httpBack
     */
    public void ValidCode(final HttpBack<String> httpBack) {
        ReqParams param = getReqParams();
        param.addParam("", "");
        param.addHead("", "");
        String url = getUrl(R.string.defaulpage);
        NetUtils.getNetUtils().send(url, param, httpBack);
    }

    /**
     * @param httpBack
     */
    public void test(final HttpBack<String> httpBack) {
        File file = new File("/storage/emulated/0/DCIM/Camera/aa.jpg");
        ReqParams param = new ReqParams();
        param.addParam("c", "698");
        param.addParam("head_img", file);
        String url = "http://api.app.meiyezhipin.com/Ucenter/Users/MemberInfo.aspx";
        NetUtils.getNetUtils().send(url, param, httpBack);
    }
}
