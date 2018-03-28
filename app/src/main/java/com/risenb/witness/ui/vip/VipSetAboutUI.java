package com.risenb.witness.ui.vip;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.risenb.witness.network.OkHttpUtils.response.GsonResponseHandler;
import com.risenb.witness.utils.newUtils.UIManager;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.risenb.witness.R;
import com.risenb.witness.beans.VipSetAboutBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.ui.BaseUI;

import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.vipsetabout)
public class VipSetAboutUI extends BaseUI {
    @ViewInject(R.id.vipsetabout_name_tv)
    private TextView vipsetabout_name_tv;
    @ViewInject(R.id.vipsetabout_version_tv)
    private TextView vipsetabout_version_tv;
    @ViewInject(R.id.vipsetabout_gx_tv)
    private TextView vipsetabout_gx_tv;
    @ViewInject(R.id.qqCrowd_tv)
    private TextView qqCrowd_tv;
    @ViewInject(R.id.servicePhone_tv)
    private TextView servicePhone_tv;
    @ViewInject(R.id.serviceTime_tv)
    private TextView serviceTime_tv;
    @ViewInject(R.id.QRCodeImageUrl_iv)
    private ImageView QRCodeImageUrl_iv;

    @Override
    protected void back() {
        UIManager.getInstance().popActivity(getClass());
    }

    @Override
    protected void setControlBasis() {
        setTitle("关于我们");
    }

    @Override
    protected void prepareData() {
        about();
        vipsetabout_version_tv.setText("版本号：".concat(getVersion()).concat("（"));
    }

    private void about() {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.about));
        Map<String, String> param = new HashMap<>();
        param.put("Version", getVersion());
        MyOkHttp.get().post(this, url, param, new GsonResponseHandler<VipSetAboutBean>() {
            @Override
            public void onSuccess(int statusCode, VipSetAboutBean response) {
                VipSetAboutBean.DataBean data = response.getData();
                vipsetabout_gx_tv.setText(response.getErrorMsg());
                if (data != null) {
                    if (data.getQqCrowd() != null)
                        qqCrowd_tv.setText("官方QQ群：".concat(data.getQqCrowd()));
                    servicePhone_tv.setText("客服电话  ：".concat(data.getServicePhone()));
                    serviceTime_tv.setText("工作时间  ：".concat(data.getServiceTime()));
                    Glide.with(getApplication())
                            .load(data.getQRCodeImageUrl())
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(QRCodeImageUrl_iv);
                    vipsetabout_name_tv.setText(data.getCompany());
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {

            }
        });

    }

    @Override
    public void onLoadOver() {

    }

    /**
     * 2  * 获取版本号
     * 3  * @return 当前应用的版本号
     * 4
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
