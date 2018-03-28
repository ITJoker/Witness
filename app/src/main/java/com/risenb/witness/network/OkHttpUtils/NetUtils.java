package com.risenb.witness.network.OkHttpUtils;

import com.risenb.witness.beans.BaseBean;
import com.risenb.witness.utils.newNetwork.MOKHttpUtils;
import com.risenb.witness.utils.newNetwork.NetMethod;

/**
 * 联网二次封装
 */
public class NetUtils extends MOKHttpUtils<BaseBean> {
    private static NetUtils netUtils;

    public static NetUtils getNetUtils() {
        if (netUtils == null) {
            netUtils = new NetUtils();
        }
        return netUtils;
    }

    public NetUtils() {
        super(BaseBean.class, NetMethod.POST, 10000, 10000, 10000);
    }
}
