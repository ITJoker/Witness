package com.risenb.witness.ui.home;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.risenb.witness.R;
import com.risenb.witness.adapter.HomeScreenBrandsAdapter;
import com.risenb.witness.adapter.HomeScreenCitysAdapter;
import com.risenb.witness.adapter.HomeScreenMediasAdapter;
import com.risenb.witness.beans.HomeDistanceFrgBean;
import com.risenb.witness.beans.HomeScreenCityBean;
import com.risenb.witness.beans.HomeScreenCompanyBean;
import com.risenb.witness.beans.HomeScreenMediatypeBean;
import com.risenb.witness.beans.ScreenStateBeans;
import com.risenb.witness.beans.WBaseBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.JsonResponseHandler;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.utils.SharedPreferencesUtil;
import com.risenb.witness.utils.newUtils.Utils;
import com.risenb.witness.views.newViews.MyGridView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.risenb.witness.ui.home.HomeUI.homeDistanceFragment;
import static com.risenb.witness.ui.home.HomeDistanceFragment.isHomeDistanceSearch;
import static com.risenb.witness.ui.home.HomeUI.homePriceFragment;
import static com.risenb.witness.ui.home.HomePriceFragment.isHomePriceSearch;
import static com.risenb.witness.ui.home.HomeUI.type;

@ContentView(R.layout.homescreen)
public class HomeScreenUI extends BaseUI {
    @ViewInject(R.id.homescreen__city_gv)
    private MyGridView homescreen__city_gv;
    @ViewInject(R.id.homescreen_media_gv)
    private MyGridView homescreen_media_gv;
    @ViewInject(R.id.homescreen_brand_gv)
    private MyGridView homescreen_brand_gv;
    @ViewInject(R.id.homescreen__city_tv)
    private TextView homescreen__city_tv;
    @ViewInject(R.id.homescreen__media_tv)
    private TextView homescreen__media_tv;
    @ViewInject(R.id.homescreen_brand_tv)
    private TextView homescreen_brand_tv;
    @ViewInject(R.id.homescreen_city_ll)
    private LinearLayout homescreen_city_ll;
    @ViewInject(R.id.homescreen_media_ll)
    private LinearLayout homescreen_media_ll;
    @ViewInject(R.id.homescreen_brand_ll)
    private LinearLayout homescreen_brand_ll;
    @ViewInject(R.id.homescreen_city_iv)
    private ImageView homescreen_city_iv;
    @ViewInject(R.id.homescreen_media_iv)
    private ImageView homescreen_media_iv;
    @ViewInject(R.id.homescreen_brand_iv)
    private ImageView homescreen_brand_iv;
    @ViewInject(R.id.homescreen_ok_tv)
    private TextView homescreen_ok_tv;

    @ViewInject(R.id.homeScreen_search_ev)
    private EditText searchET;
    @ViewInject(R.id.floating_search_view)
    private FloatingSearchView mSearchView;
    private String query = "";
    private String newQueryText = "";

    private HomeScreenCitysAdapter homeScreenCityGridAdapter;
    private HomeScreenMediasAdapter homeScreenMediaGridAdapter;
    private HomeScreenBrandsAdapter homeScreenBrandGridAdapter;

    private List<HomeScreenCityBean> homeScreenCityBeen;
    private List<HomeScreenMediatypeBean> homeScreenMediatypeBeen;
    private List<HomeScreenCompanyBean> homeScreenCompanyBeen;

    private boolean cityflag = false;
    private boolean mediaflag = false;
    private boolean brandflag = false;

    private String cityId = "";
    private String mediaId = "";
    private String companyId = "";
    private int page = 1;

    private List<ScreenStateBeans> list;
    private String searchContent;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        if (!TextUtils.isEmpty(application.getScreen())) {
            list = JSONArray.parseArray(application.getScreen(), ScreenStateBeans.class);
        }
        homeScreenCityGridAdapter = new HomeScreenCitysAdapter();
        homeScreenMediaGridAdapter = new HomeScreenMediasAdapter();
        homeScreenBrandGridAdapter = new HomeScreenBrandsAdapter();
        homescreen__city_gv.setAdapter(homeScreenCityGridAdapter);
        homescreen_media_gv.setAdapter(homeScreenMediaGridAdapter);
        homescreen_brand_gv.setAdapter(homeScreenBrandGridAdapter);
        city();
        mediaType();
        company();
    }

    private int Cityps = 0;
    private boolean Citypss = false;
    private int Mediaps = 0;
    private boolean Mediapss = false;
    private int Brandps = 0;
    private boolean Brandpss = false;

    @Override
    protected void prepareData() {
        mSearchView.setVisibility(View.GONE);
        homeScreenCityGridAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (homeScreenCityBeen.get(position).getCityname().equals("全部")) {
                    startActivityForResult(new Intent(HomeScreenUI.this, HomeScreenCityUI.class), 1);
                } else {
                    if (Cityps == position) {
                        if (Citypss) {
                            // position为0说明cityID为北京2
                            homescreen__city_tv.setText("");
                            cityId = "";
                            Citypss = false;
                        } else {
                            homescreen__city_tv.setText(homeScreenCityBeen.get(position).getCityname());
                            cityId = homeScreenCityBeen.get(position).getCityid();
                            Citypss = true;
                        }
                    } else {
                        homescreen__city_tv.setText(homeScreenCityBeen.get(position).getCityname());
                        cityId = homeScreenCityBeen.get(position).getCityid();
                        Citypss = true;
                    }
                    homeScreenCityGridAdapter.setInt(position);
                    Cityps = position;
                }
            }
        });
        homeScreenMediaGridAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (homeScreenMediatypeBeen.get(position).getMedianame().equals("全部")) {
                    startActivityForResult(new Intent(HomeScreenUI.this, HomeScreenMeitiUI.class), 2);
                } else {
                    if (Mediaps == position) {
                        if (Mediapss) {
                            homescreen__media_tv.setText("");
                            mediaId = "";
                            Mediapss = false;
                        } else {
                            homescreen__media_tv.setText(homeScreenMediatypeBeen.get(position).getMedianame());
                            mediaId = homeScreenMediatypeBeen.get(position).getMediaid();
                            Mediapss = true;
                        }
                    } else {
                        homescreen__media_tv.setText(homeScreenMediatypeBeen.get(position).getMedianame());
                        mediaId = homeScreenMediatypeBeen.get(position).getMediaid();
                        Mediapss = true;
                    }
                    homeScreenMediaGridAdapter.setInt(position);
                    Mediaps = position;
                }
            }
        });
        homeScreenBrandGridAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (homeScreenCompanyBeen.get(position).getCompanyname().equals("全部")) {
                    startActivityForResult(new Intent(HomeScreenUI.this, HomeScreenPinpaiUI.class), 3);
                } else {
                    if (Brandps == position) {
                        if (Brandpss) {
                            homescreen_brand_tv.setText("");
                            companyId = "";
                            Brandpss = false;
                        } else {
                            homescreen_brand_tv.setText(homeScreenCompanyBeen.get(position).getCompanyname());
                            companyId = homeScreenCompanyBeen.get(position).getCompanyid();
                            Brandpss = true;
                        }
                    } else {
                        homescreen_brand_tv.setText(homeScreenCompanyBeen.get(position).getCompanyname());
                        companyId = homeScreenCompanyBeen.get(position).getCompanyid();
                        Brandpss = true;
                    }
                    homeScreenBrandGridAdapter.setInt(position);
                    Brandps = position;
                }
            }
        });
        homescreen_ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskSearch();
            }
        });
    }

    public void taskSearch() {
        searchContent = searchET.getText().toString().trim();
        Utils.getUtils().showProgressDialog(getActivity());
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.taskSearch));
        Map<String, String> param = new HashMap<>();
        param.put("query", searchContent);
        if (!TextUtils.isEmpty(cityId)) {
            param.put("CityId", cityId);
        }
        if (!TextUtils.isEmpty(mediaId)) {
            param.put("MediaTreeID", mediaId);
        }
        if (!TextUtils.isEmpty(companyId)) {
            param.put("CompanyId", companyId);
        }
        param.put("pagecount", String.valueOf(page));
        param.put("pagesize", "10");
        if (type == 1) {
            param.put("sort", "2");
        } else {
            param.put("sort", "1");
        }
        param.put("longitude", SharedPreferencesUtil.getString(getApplication(), "HomeLongitude", ""));
        param.put("latitude", SharedPreferencesUtil.getString(getApplication(), "HomeLatitude", ""));
        MyOkHttp.get().post(getApplication(), url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                List<HomeDistanceFrgBean> homeDistanceFrgBeen = JSONArray.parseArray(wBaseBean.getData(), HomeDistanceFrgBean.class);
                if (homeDistanceFrgBeen != null && homeDistanceFrgBeen.size() == 0) {
                    makeText("未搜索到相关数据");
                }
                switch (type) {
                    case 1:
                        homeDistanceFragment.setData(homeDistanceFrgBeen);
                        isHomeDistanceSearch = true;
                        HomeDistanceFragment.cityId = cityId;
                        HomeDistanceFragment.mediaId = mediaId;
                        HomeDistanceFragment.companyId = companyId;
                        HomeDistanceFragment.searchContent = searchContent;
                        break;
                    case 2:
                        homePriceFragment.setData(homeDistanceFrgBeen);
                        isHomePriceSearch = true;
                        HomePriceFragment.cityId = cityId;
                        HomePriceFragment.mediaId = mediaId;
                        HomePriceFragment.companyId = companyId;
                        HomePriceFragment.searchContent = searchContent;
                        break;
                    default:
                        makeText("未搜索到相关数据");
                        break;
                }
                finish();
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
                makeText("没有搜索到相关数据");
            }
        });
    }

    @Override
    public void onLoadOver() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == 1) {
                homescreen__city_tv.setText(data.getStringExtra("name"));
                cityId = data.getStringExtra("id");
                homeScreenCityGridAdapter.setInt(-1);
            }
        }
        if (requestCode == 2) {
            if (resultCode == 2) {
                homescreen__media_tv.setText(data.getStringExtra("name"));
                mediaId = data.getStringExtra("id");
                homeScreenMediaGridAdapter.setInt(-1);
            }
        }
        if (requestCode == 3) {
            if (resultCode == 3) {
                homescreen_brand_tv.setText(data.getStringExtra("name"));
                companyId = data.getStringExtra("id");
                homeScreenBrandGridAdapter.setInt(-1);
            }
        }
    }

    /**
     * 返回
     */
    @OnClick(R.id.homescreen_back)
    private void test(View v) {
        finish();
    }

    /**
     * 完成
     */
    @OnClick(R.id.homescreen_ok_tv)
    private void homeScreenOK_tv(View v) {
        List<ScreenStateBeans> list = new ArrayList<>();
        ScreenStateBeans s1 = new ScreenStateBeans();
        s1.setName("城市");
        s1.setId(cityId);
        s1.setKg(cityflag);
        if (TextUtils.isEmpty(cityId)) {
            s1.setXz(false);
        } else {
            s1.setXz(true);
        }
        ScreenStateBeans s2 = new ScreenStateBeans();
        s2.setName("媒体类型");
        s2.setId(mediaId);
        s2.setKg(mediaflag);
        if (TextUtils.isEmpty(mediaId)) {
            s2.setXz(false);
        } else {
            s2.setXz(true);
        }
        ScreenStateBeans s3 = new ScreenStateBeans();
        s3.setName("品牌");
        s3.setId(companyId);
        s3.setKg(brandflag);
        if (TextUtils.isEmpty(companyId)) {
            s3.setXz(false);
        } else {
            s3.setXz(true);
        }
        list.add(s1);
        list.add(s2);
        list.add(s3);
        application.setScreen(JSON.toJSONString(list));
        Intent intent = getIntent();
        intent.putExtra("cityId", cityId);
        intent.putExtra("mediaId", mediaId);
        intent.putExtra("companyId", companyId);
        setResult(1, intent);
        finish();
    }

    /**
     * 重置
     */
    @OnClick(R.id.homescreen_reset_tv)
    private void homeScreenReset_tv(View v) {
        homescreen__city_tv.setText("");
        homescreen__media_tv.setText("");
        homescreen_brand_tv.setText("");
        homeScreenCityGridAdapter.setInt(-1);
        homeScreenMediaGridAdapter.setInt(-1);
        homeScreenBrandGridAdapter.setInt(-1);
        cityId = "";
        mediaId = "";
        companyId = "";
    }

    /**
     * 城市
     */
    @OnClick(R.id.homescreen_city_ll)
    private void homeScreenCity_ll(View v) {
        if (cityflag) {
            cityflag = false;
            homescreen_city_iv.setImageResource(R.drawable.bangzhuwendangxia);
            homeScreenCityGridAdapter.CityFlags(cityflag);
            homeScreenCityGridAdapter.notifyDataSetChanged();
        } else {
            cityflag = true;
            homescreen_city_iv.setImageResource(R.drawable.bangzhuwendangshang);
            homeScreenCityGridAdapter.CityFlags(cityflag);
            homeScreenCityGridAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 媒体类型
     */
    @OnClick(R.id.homescreen_media_ll)
    private void homeScreenMedia_ll(View v) {
        if (mediaflag) {
            mediaflag = false;
            homescreen_media_iv.setImageResource(R.drawable.bangzhuwendangxia);
            homeScreenMediaGridAdapter.MediaFlags(mediaflag);
            homeScreenMediaGridAdapter.notifyDataSetChanged();
        } else {
            mediaflag = true;
            homescreen_media_iv.setImageResource(R.drawable.bangzhuwendangshang);
            homeScreenMediaGridAdapter.MediaFlags(mediaflag);
            homeScreenMediaGridAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 品牌
     */
    @OnClick(R.id.homescreen_brand_ll)
    private void homeScreenBrand_ll(View v) {
        if (brandflag) {
            brandflag = false;
            homescreen_brand_iv.setImageResource(R.drawable.bangzhuwendangxia);
            homeScreenBrandGridAdapter.BrandFlags(brandflag);
            homeScreenBrandGridAdapter.notifyDataSetChanged();
        } else {
            brandflag = true;
            homescreen_brand_iv.setImageResource(R.drawable.bangzhuwendangshang);
            homeScreenBrandGridAdapter.BrandFlags(brandflag);
            homeScreenBrandGridAdapter.notifyDataSetChanged();
        }
    }

    private void city() {
        Utils.getUtils().showProgressDialog(getActivity());
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.city));
        Map<String, String> param = new HashMap<>();
        param.put("pagecount", "1");
        param.put("pagesize", "8");
        MyOkHttp.get().post(getActivity(), url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                homeScreenCityBeen = JSONArray.parseArray(wBaseBean.getData(), HomeScreenCityBean.class);
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getName().equals("城市") && homeScreenCityBeen != null && homeScreenCityBeen.size() > 0) {
                            if (list.get(i).isXz()) {
                                for (int j = 0; j < homeScreenCityBeen.size(); j++) {
                                    if (list.get(i).getId().equals(homeScreenCityBeen.get(j).getCityid())) {
                                        homescreen__city_tv.setText(homeScreenCityBeen.get(j).getCityname());
                                        cityId = homeScreenCityBeen.get(j).getCityid();
                                        homeScreenCityGridAdapter.setInt(j);
                                    }
                                }
                            } else {
                                homescreen__city_tv.setText("");
                                homeScreenCityGridAdapter.setInt(-1);
                            }
                            if (list.get(i).isKg()) {
                                homescreen_city_iv.setImageResource(R.drawable.bangzhuwendangshang);
                                homeScreenCityGridAdapter.CityFlags(list.get(i).isKg());
                                homeScreenCityGridAdapter.notifyDataSetChanged();
                            }
                            cityflag = list.get(i).isKg();
                        }
                    }
                } else if (homeScreenCityBeen != null && homeScreenCityBeen.size() > 0) {
                    /*homescreen__city_tv.setText(homeScreenCityBeen.get(0).getCityname());
                    cityId = homeScreenCityBeen.get(0).getCityid();
                    homeScreenCityGridAdapter.setInt(0);*/
                }
                if (homeScreenCityBeen.size() == 8) {
                    HomeScreenCityBean homeScreenCityBean = new HomeScreenCityBean();
                    homeScreenCityBean.setCityname("全部");
                    homeScreenCityBean.setCityid("1");
                    homeScreenCityBeen.add(homeScreenCityBean);
                }
                homeScreenCityGridAdapter.setList(homeScreenCityBeen);
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }

    private void mediaType() {
        Utils.getUtils().showProgressDialog(getActivity());
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.mediatype));
        Map<String, String> param = new HashMap<>();
        param.put("pagecount", "1");
        param.put("pagesize", "8");
        MyOkHttp.get().post(getActivity(), url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                homeScreenMediatypeBeen = JSONArray.parseArray(wBaseBean.getData(), HomeScreenMediatypeBean.class);
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getName().equals("媒体类型") && homeScreenMediatypeBeen != null && homeScreenMediatypeBeen.size() > 0) {
                            if (list.get(i).isXz()) {
                                for (int j = 0; j < homeScreenMediatypeBeen.size(); j++) {
                                    if (list.get(i).getId().equals(homeScreenMediatypeBeen.get(j).getMediaid())) {
                                        homescreen__media_tv.setText(homeScreenMediatypeBeen.get(j).getMedianame());
                                        mediaId = homeScreenMediatypeBeen.get(j).getMediaid();
                                        homeScreenMediaGridAdapter.setInt(j);
                                    }
                                }
                            } else {
                                homescreen__media_tv.setText("");
                                homeScreenMediaGridAdapter.setInt(-1);
                            }
                            if (list.get(i).isKg()) {
                                homescreen_media_iv.setImageResource(R.drawable.bangzhuwendangshang);
                                homeScreenMediaGridAdapter.MediaFlags(list.get(i).isKg());
                                homeScreenMediaGridAdapter.notifyDataSetChanged();
                            }
                            mediaflag = list.get(i).isKg();
                        }
                    }
                } else if (homeScreenMediatypeBeen != null && homeScreenMediatypeBeen.size() > 0) {
                    /*homescreen__media_tv.setText(homeScreenMediatypeBeen.get(0).getMedianame());
                    mediaId = homeScreenMediatypeBeen.get(0).getMediaid();
                    homeScreenMediaGridAdapter.setInt(0);*/
                }

                if (homeScreenMediatypeBeen.size() == 8) {
                    HomeScreenMediatypeBean homeScreenMediatypeBean = new HomeScreenMediatypeBean();
                    homeScreenMediatypeBean.setMedianame("全部");
                    homeScreenMediatypeBean.setMediaid("1");
                    homeScreenMediatypeBeen.add(homeScreenMediatypeBean);
                }
                homeScreenMediaGridAdapter.setList(homeScreenMediatypeBeen);
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }

    private void company() {
        Utils.getUtils().showProgressDialog(getActivity());
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.company));
        Map<String, String> param = new HashMap<>();
        param.put("pagecount", "1");
        param.put("pagesize", "8");
        MyOkHttp.get().post(getActivity(), url, param, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Utils.getUtils().dismissDialog();
                WBaseBean wBaseBean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), WBaseBean.class);
                homeScreenCompanyBeen = JSONArray.parseArray(wBaseBean.getData(), HomeScreenCompanyBean.class);
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getName().equals("品牌") && homeScreenCompanyBeen != null && homeScreenCompanyBeen.size() > 0) {
                            if (list.get(i).isXz()) {
                                for (int j = 0; j < homeScreenCompanyBeen.size(); j++) {
                                    if (list.get(i).getId().equals(homeScreenCompanyBeen.get(j).getCompanyid())) {
                                        homescreen_brand_tv.setText(homeScreenCompanyBeen.get(j).getCompanyname());
                                        companyId = homeScreenCompanyBeen.get(j).getCompanyid();
                                        homeScreenBrandGridAdapter.setInt(j);
                                    }
                                }
                            } else {
                                homescreen_brand_tv.setText("");
                                homeScreenBrandGridAdapter.setInt(-1);
                            }
                            if (list.get(i).isKg()) {
                                homescreen_brand_iv.setImageResource(R.drawable.bangzhuwendangshang);
                                homeScreenBrandGridAdapter.BrandFlags(list.get(i).isKg());
                                homeScreenBrandGridAdapter.notifyDataSetChanged();
                            }
                            brandflag = list.get(i).isKg();
                        }
                    }
                } else if (homeScreenCompanyBeen != null && homeScreenCompanyBeen.size() > 0) {
                    /*homescreen_brand_tv.setText(homeScreenCompanyBeen.get(0).getCompanyname());
                    companyId = homeScreenCompanyBeen.get(0).getCompanyid();
                    homeScreenBrandGridAdapter.setInt(0);*/
                }

                if (homeScreenCompanyBeen.size() == 8) {
                    HomeScreenCompanyBean homeScreenCompanyBean = new HomeScreenCompanyBean();
                    homeScreenCompanyBean.setCompanyname("全部");
                    homeScreenCompanyBean.setCompanyid("1");
                    homeScreenCompanyBeen.add(homeScreenCompanyBean);
                }
                homeScreenBrandGridAdapter.setList(homeScreenCompanyBeen);
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }
        });
    }

    /**
     * 关闭当前页面
     */
    @OnClick(R.id.homescreen_view)
    private void finishActivity(View view) {
        this.finish();
    }
}
