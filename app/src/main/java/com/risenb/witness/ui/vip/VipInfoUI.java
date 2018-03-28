package com.risenb.witness.ui.vip;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.risenb.witness.MyApplication;
import com.risenb.witness.beans.BaseBeans;
import com.risenb.witness.beans.ProvinceAndCityBean;
import com.risenb.witness.beans.VipInfoBean;
import com.risenb.witness.network.OkHttpUtils.response.GsonResponseHandler;
import com.risenb.witness.ui.vip.Wallet.RealNameUI;
import com.risenb.witness.utils.newUtils.CameraCallBack;
import com.risenb.witness.utils.newUtils.ImgUtils;
import com.risenb.witness.utils.newUtils.Utils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.risenb.witness.R;
import com.risenb.witness.beans.WBaseBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.JsonResponseHandler;
import com.risenb.witness.pop.PopIco;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.utils.MyConfig;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.vipinfo)
public class VipInfoUI extends BaseUI {
    @ViewInject(R.id.vipinfo_icon_iv)
    private ImageView vipinfo_icon_iv;
    @ViewInject(R.id.vipinfo_city_show_tv)
    private TextView vipinfo_city_show_tv;
    @ViewInject(R.id.user_name_et)
    private EditText user_name_et;
    @ViewInject(R.id.user_tel_tv)
    private TextView user_tel_tv;
    @ViewInject(R.id.vipinfo_sex_show_tv)
    private TextView vipinfo_sex_show_tv;
    @ViewInject(R.id.vipinfo_real_name_show_tv)
    private TextView vipinfo_real_name_show_tv;

    private ImgUtils imgUtils;

    // 省级名称和ID
    private ArrayList<String> provinceNameList = new ArrayList<>();
    private ArrayList<String> provinceIDList = new ArrayList<>();
    // 市级名称和ID
    private ArrayList<ArrayList<String>> provinceToCityNameList = new ArrayList<>();
    private ArrayList<ArrayList<String>> provinceToCityIDList = new ArrayList<>();
    // 省级注册ID
    private String provinceID;
    // 城市注册ID
    private String cityID;
    private String sexNumber = "0";
    private int is_prove = -1;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        setTitle("我的信息");
    }

    @Override
    protected void prepareData() {
        personalCenter();
        getProvinceAndCityInfo();
        user_name_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                hintKbTwo();
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (!TextUtils.isEmpty(user_name_et.getText().toString().trim())) {
                        if (user_name_et.getText().toString().trim().length() < 2 || user_name_et.getText().toString().trim().length() > 10) {
                            makeText("请输入2-10个字");
                        } else {
                            vipChangeName();
                        }
                    } else {
                        makeText("请输入文字");
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void vipChangeName() {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.editNickName));
        Map<String, String> param = new HashMap<>();
        param.put("c", application.getC());
        param.put("nickName", user_name_et.getText().toString().trim());
        MyOkHttp.get().post(this, url, param, new GsonResponseHandler<BaseBeans>() {
            @Override
            public void onSuccess(int statusCode, BaseBeans response) {
                makeText(response.getErrorMsg());
                if (!"1".equals(response.getSuccess())) {
                    if (response.getErrorMsg().equals("登录异常")) {
                        errorLogin();
                    }
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

    private void personalCenter() {
        Utils.getUtils().showProgressDialog(this);
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.personalCenter));
        Map<String, String> param = new HashMap<>();
        param.put("c", application.getC());
        MyOkHttp.get().post(this, url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                if (wBaseBean.getSuccess().equals("1")) {
                    VipInfoBean vipInfoBean = com.alibaba.fastjson.JSONObject.parseObject(wBaseBean.getData(), VipInfoBean.class);
                    ImageLoader.getInstance().displayImage(vipInfoBean.getHeadPic(), vipinfo_icon_iv, MyConfig.options);
                    user_name_et.setText(vipInfoBean.getNickName());
                    user_tel_tv.setText(vipInfoBean.getUserTel());
                    switch (vipInfoBean.getSex()) {
                        case 1:
                            vipinfo_sex_show_tv.setText("男");
                            break;
                        case 2:
                            vipinfo_sex_show_tv.setText("女");
                            break;
                    }
                    is_prove = vipInfoBean.getIs_prove();
                    switch (is_prove) {
                        case 0:
                            vipinfo_real_name_show_tv.setText("未认证");
                            break;
                        case 1:
                            vipinfo_real_name_show_tv.setText("已认证");
                            break;
                    }
                    if (!TextUtils.isEmpty(vipInfoBean.getCity_name())) {
                        if (vipInfoBean.getProvince_name().equals(vipInfoBean.getCity_name()) || vipInfoBean.getCity_name().contains(vipInfoBean.getProvince_name())) {
                            vipinfo_city_show_tv.setText(vipInfoBean.getCity_name());
                        } else {
                            vipinfo_city_show_tv.setText(vipInfoBean.getProvince_name().concat(vipInfoBean.getCity_name()));
                        }
                    } else {
                        vipinfo_city_show_tv.setText(vipInfoBean.getProvince_name());
                    }
                } else {
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

    private void getProvinceAndCityInfo() {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.ReturnCity));
        MyOkHttp.get().post(getApplication(), url, null, new GsonResponseHandler<ProvinceAndCityBean>() {
            @Override
            public void onSuccess(int statusCode, final ProvinceAndCityBean response) {
                if (response.getSuccess() == 1) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (provinceIDList != null || provinceIDList.size() != 0) {
                                provinceIDList.clear();
                            }
                            if (provinceNameList != null || provinceNameList.size() != 0) {
                                provinceNameList.clear();
                            }
                            for (ProvinceAndCityBean.DataBean dataBean : response.getData()) {
                                provinceIDList.add(dataBean.getPid());
                                provinceNameList.add(dataBean.getProvince_name());
                                ArrayList<String> cityNameList = new ArrayList<>();
                                ArrayList<String> cityIDList = new ArrayList<>();
                                for (ProvinceAndCityBean.DataBean.CityBean cityBean : dataBean.getCity()) {
                                    cityIDList.add(cityBean.getCid());
                                    cityNameList.add(cityBean.getCity_name());
                                }
                                provinceToCityIDList.add(cityIDList);
                                provinceToCityNameList.add(cityNameList);
                            }
                        }
                    }).start();
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {

            }
        });
    }

    /**
     * 头像
     */
    @OnClick(R.id.vip_info_ll)
    private void vip_info_ll(View v) {
        Intent intentUtils = new Intent();
        // aspectX aspectY 是宽高的比例
        intentUtils.putExtra("aspectX", 1);
        intentUtils.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intentUtils.putExtra("outputX", 300);
        intentUtils.putExtra("outputY", 300);
        if (Build.VERSION.SDK_INT >= 24) {
            imgUtils = new ImgUtils(getActivity(), true, intentUtils);
        } else {
            imgUtils = new ImgUtils(this, true, intentUtils);
        }
        imgUtils.setCameraCallBack(new CameraCallBack() {
            @Override
            public void onCameraCallBack(String path) {
                Bitmap bm = BitmapFactory.decodeFile(path);
                vipinfo_icon_iv.setImageBitmap(bm);
                editHeadPic(path);
            }
        });
        PopIco popIco = new PopIco(v, this);
        popIco.showAsDropDown();
        popIco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_pop_ico_camera:
                        imgUtils.openCamera();
                        break;
                    case R.id.tv_pop_ico_photo:
                        imgUtils.openPhotoAlbum();
                        break;
                }
            }
        });

    }

    private void editHeadPic(String path) {
        Utils.getUtils().showProgressDialog(this);
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.editHeadPic));
        Map<String, String> param = new HashMap<>();
        param.put("c", application.getC());
        Map<String, File> files = new HashMap<>();
        files.put("img", new File(path));
        MyOkHttp.get().upload(this, url, param, files, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                makeText(wBaseBean.getErrorMsg());
                if (wBaseBean.getSuccess().equals("1")) {
                    /*personalCenter();*/
                } else {
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

    //更改手机号
    @OnClick(R.id.vipinfo_changephone_ll)
    private void changePhone(View view) {
        startActivity(new Intent(this, VipChangePhoneUI.class));
    }

    //选择城市
    @OnClick(R.id.vipinfo_city_select_ll)
    private void selectCity(View view) {
        if (provinceIDList.size() == 0 && provinceNameList.size() == 0) {
            makeText("获取省级信息失败");
            return;
        }
        OptionsPickerView pvOptions = new OptionsPickerView(new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                provinceID = provinceIDList.get(options1);
                if (options2 == -1) {
                    vipinfo_city_show_tv.setText(provinceNameList.get(options1));
                    cityID = "";
                } else {
                    cityID = provinceToCityIDList.get(options1).get(options2);
                    if (provinceNameList.get(options1).equals(provinceToCityNameList.get(options1).get(options2)) || provinceToCityNameList.get(options1).get(options2).contains(provinceNameList.get(options1))) {
                        vipinfo_city_show_tv.setText(provinceToCityNameList.get(options1).get(options2));
                    } else {
                        vipinfo_city_show_tv.setText(provinceNameList.get(options1).concat(provinceToCityNameList.get(options1).get(options2)));
                    }
                }
                updateProvinceAndCityData();
            }
        }));
        pvOptions.setPicker(provinceNameList, provinceToCityNameList);
        pvOptions.show();
    }

    private void updateProvinceAndCityData() {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.SubCity));
        Map<String, String> param = new HashMap<>();
        param.put("c", MyApplication.getInstance().getC());
        param.put("ProvinceId", provinceID);
        param.put("CityId", cityID);
        MyOkHttp.get().post(getApplication(), url, param, new GsonResponseHandler<BaseBeans>() {
            @Override
            public void onSuccess(int statusCode, BaseBeans response) {
                makeText(response.getErrorMsg());
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {

            }
        });
    }

    //性别选择
    @OnClick(R.id.vipinfo_sex_select_ll)
    private void selectSex(View view) {
        final ArrayList<String> sexList = new ArrayList<String>() {{
            add("男");
            add("女");
        }};
        OptionsPickerView pvOptions = new OptionsPickerView(new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                vipinfo_sex_show_tv.setText(sexList.get(options1));
                String sex = sexList.get(options1);
                switch (sex) {
                    case "男":
                        sexNumber = "1";
                        break;
                    case "女":
                        sexNumber = "2";
                        break;
                }
                updateUserSexData();
            }
        }));
        pvOptions.setPicker(sexList);
        pvOptions.show();
    }

    private void updateUserSexData() {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.personalEditSex));
        Map<String, String> param = new HashMap<>();
        param.put("c", MyApplication.getInstance().getC());
        param.put("sex", sexNumber);
        MyOkHttp.get().post(getApplication(), url, param, new GsonResponseHandler<BaseBeans>() {
            @Override
            public void onSuccess(int statusCode, BaseBeans response) {
                makeText(response.getErrorMsg());
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {

            }
        });
    }

    //实名认证
    @OnClick(R.id.vipinfo_real_name_authentication_ll)
    private void realNameAuthentication(View view) {
        startActivity(new Intent(this, RealNameUI.class).putExtra("type", is_prove));
    }

    //常用地址
    @OnClick(R.id.vipinfo_commonaddress_ll)
    private void address(View view) {
        startActivity(new Intent(this, CommonAddressUI.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imgUtils.onActivityResult(requestCode, resultCode, data);
    }
}
