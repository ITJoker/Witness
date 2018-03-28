package com.risenb.witness.ui.tasklist;

import android.content.Intent;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.adapter.HomeScreenBrandsAdapter;
import com.risenb.witness.adapter.HomeScreenCitysAdapter;
import com.risenb.witness.adapter.HomeScreenMediasAdapter;
import com.risenb.witness.beans.BaseBeans;
import com.risenb.witness.beans.HomeScreenCityBean;
import com.risenb.witness.beans.HomeScreenCompanyBean;
import com.risenb.witness.beans.HomeScreenMediatypeBean;
import com.risenb.witness.beans.ScreenStateBeans;
import com.risenb.witness.beans.TaskListBean;
import com.risenb.witness.beans.WBaseBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.GsonResponseHandler;
import com.risenb.witness.network.OkHttpUtils.response.JsonResponseHandler;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.ui.tasklist.fragment.CommonTaskUploadedFragment;
import com.risenb.witness.ui.tasklist.fragment.ExecMoneyAtMostFragment;
import com.risenb.witness.ui.tasklist.fragment.ExecRecentlyDistanceFragment;
import com.risenb.witness.ui.tasklist.fragment.ExecWillExpireFragment;
import com.risenb.witness.ui.tasklist.fragment.ExecutingTaskFragment;
import com.risenb.witness.ui.tasklist.fragment.NonExecutionTaskFragment;
import com.risenb.witness.ui.home.HomeScreenCityUI;
import com.risenb.witness.ui.home.HomeScreenMeitiUI;
import com.risenb.witness.ui.home.HomeScreenPinpaiUI;
import com.risenb.witness.utils.SharedPreferencesUtil;
import com.risenb.witness.utils.StringUtils;
import com.risenb.witness.utils.newUtils.Utils;
import com.risenb.witness.views.newViews.MyGridView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.util.ConvertUtils;

import static com.risenb.witness.ui.tasklist.fragment.RecentlyDistanceFragment.distanceQueryText;
import static com.risenb.witness.ui.tasklist.fragment.RecentlyDistanceFragment.searchAddressList;
import static com.risenb.witness.ui.tasklist.fragment.RecentlyDistanceFragment.distanceClickSign;
import static com.risenb.witness.ui.tasklist.fragment.RecentlyDistanceFragment.distanceCityId;
import static com.risenb.witness.ui.tasklist.fragment.RecentlyDistanceFragment.distanceCompanyId;
import static com.risenb.witness.ui.tasklist.fragment.RecentlyDistanceFragment.distanceMediaId;

import static com.risenb.witness.ui.tasklist.fragment.MoneyAtMostFragment.moneyQueryText;
import static com.risenb.witness.ui.tasklist.fragment.MoneyAtMostFragment.moneySearchAddressList;
import static com.risenb.witness.ui.tasklist.fragment.MoneyAtMostFragment.moneyClickSign;
import static com.risenb.witness.ui.tasklist.fragment.MoneyAtMostFragment.moneyCityId;
import static com.risenb.witness.ui.tasklist.fragment.MoneyAtMostFragment.moneyCompanyId;
import static com.risenb.witness.ui.tasklist.fragment.MoneyAtMostFragment.moneyMediaId;

import static com.risenb.witness.ui.tasklist.fragment.WillExpireFragment.willExpireQueryText;
import static com.risenb.witness.ui.tasklist.fragment.WillExpireFragment.willExpireSearchAddressList;
import static com.risenb.witness.ui.tasklist.fragment.WillExpireFragment.willExpireClickSign;
import static com.risenb.witness.ui.tasklist.fragment.WillExpireFragment.willExpireCityId;
import static com.risenb.witness.ui.tasklist.fragment.WillExpireFragment.willExpireCompanyId;
import static com.risenb.witness.ui.tasklist.fragment.WillExpireFragment.willExpireMediaId;

import static com.risenb.witness.ui.tasklist.fragment.CommonTaskUploadedFragment.completedUploadQueryText;
import static com.risenb.witness.ui.tasklist.fragment.CommonTaskUploadedFragment.taskLists;
import static com.risenb.witness.ui.tasklist.fragment.CommonTaskUploadedFragment.completedUploadCityId;
import static com.risenb.witness.ui.tasklist.fragment.CommonTaskUploadedFragment.completedUploadCompanyId;
import static com.risenb.witness.ui.tasklist.fragment.CommonTaskUploadedFragment.completedUploadMediaId;
import static com.risenb.witness.ui.tasklist.fragment.CommonTaskUploadedFragment.completedUploadStartTime;
import static com.risenb.witness.ui.tasklist.fragment.CommonTaskUploadedFragment.completedUploadEndTime;

@ContentView(R.layout.homescreen)
public class TaskListScreenUI extends BaseUI {
    @ViewInject(R.id.homescreen_view)
    private View homescreen_view;
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

    @ViewInject(R.id.search_time_start_to_end)
    private LinearLayout searchTimeStartToEnd;
    @ViewInject(R.id.search_start_time)
    private Button searchStartTime;
    @ViewInject(R.id.search_end_time)
    private Button searchEndTime;

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
    private String searchContent;

    private List<ScreenStateBeans> list;
    private String taskState;
    private String query = "";
    private String newQueryText = "";
    private int index;
    private String startTime;
    private String endTime;

    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void setControlBasis() {
        if (!TextUtils.isEmpty(application.getTaskList())) {
            list = JSONArray.parseArray(application.getTaskList(), ScreenStateBeans.class);
        }
        homeScreenCityGridAdapter = new HomeScreenCitysAdapter();
        homeScreenMediaGridAdapter = new HomeScreenMediasAdapter();
        homeScreenBrandGridAdapter = new HomeScreenBrandsAdapter();
        homescreen__city_gv.setAdapter(homeScreenCityGridAdapter);
        homescreen_media_gv.setAdapter(homeScreenMediaGridAdapter);
        homescreen_brand_gv.setAdapter(homeScreenBrandGridAdapter);
        city();
        mediatype();
        company();
        taskState = getIntent().getStringExtra("taskState");
        if ("CompletedUpload".equals(taskState)) {
            searchTimeStartToEnd.setVisibility(View.VISIBLE);
        }
        index = getIntent().getIntExtra("index", -1);
    }

    private int Cityps = -1;
    private boolean Citypss = false;
    private int Mediaps = -1;
    private boolean Mediapss = false;
    private int Brandps = -1;
    private boolean Brandpss = false;

    @Override
    protected void prepareData() {
        mSearchView.setVisibility(View.GONE);
        homeScreenCityGridAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (homeScreenCityBeen.get(position).getCityname().equals("全部")) {
                    startActivityForResult(new Intent(TaskListScreenUI.this, HomeScreenCityUI.class), 1);
                } else {
                    if (Cityps == position) {
                        if (Citypss) {
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
        homeScreenMediaGridAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (homeScreenMediatypeBeen.get(position).getMedianame().equals("全部")) {
                    startActivityForResult(new Intent(TaskListScreenUI.this, HomeScreenMeitiUI.class), 2);
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
        homeScreenBrandGridAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (homeScreenCompanyBeen.get(position).getCompanyname().equals("全部")) {
                    startActivityForResult(new Intent(TaskListScreenUI.this, HomeScreenPinpaiUI.class), 3);
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

        homescreen_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*
         * 监听搜索框文字内容改变事件
         */
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                newQueryText = newQuery;
            }
        });

        /*
         * 监听菜单按键
         */
        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                if (!TextUtils.isEmpty(newQueryText)) {
                    searchOperation(newQueryText);
                } else {
                    makeText("请输入关键字进行搜索");
                }
            }
        });

        /*
         * 菜单最左边按键监听事件
         */
        mSearchView.setOnLeftMenuClickListener(new FloatingSearchView.OnLeftMenuClickListener() {
            @Override
            public void onMenuOpened() {

            }

            @Override
            public void onMenuClosed() {

            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                // 搜索结果被点击
            }

            @Override
            public void onSearchAction(String currentQuery) {
                searchOperation(currentQuery);
            }
        });

        homescreen_ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskSearch();
            }
        });
    }

    private void searchOperation(String currentQuery) {
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.myTaskList));
        Map<String, String> param = new HashMap<>();
        param.put("c", MyApplication.getInstance().getC());
        param.put("pagecount", "1");
        param.put("pagesize", "10");
        if (moneyClickSign) {
            param.put("sort", "1");
        } else if (willExpireClickSign) {
            param.put("sort", "3");
        } else if (distanceClickSign) {
            param.put("sort", "2");
        }
        param.put("longitude", SharedPreferencesUtil.getString(getApplication(), "HomeLongitude", ""));
        param.put("latitude", SharedPreferencesUtil.getString(getApplication(), "HomeLatitude", ""));
        param.put("query", currentQuery);
        if (taskState.equals(NonExecutionTaskFragment.NONEXECUTIONFRAGMENT)) {
            param.put("type", "0");
        } else if (taskState.equals(ExecutingTaskFragment.EXECUTIONINGFRAGMENT)) {
            param.put("type", "1");
        } else if (taskState.equals(CommonTaskUploadedFragment.COMUPLOADFRAGMENT)) {
            param.put("type", "2");
        } else if (taskState.equals(RejectTaskFragment.REJECTASKFRAGEMNTFLAG)) {

        }
        query = currentQuery;
        onQueryTextNetWork(url, param);
    }

    /**
     * 添加时间范围字段儿辅助搜索
     */
    public void onYearMonthDayStartPicker(View view) {
        DatePickerShow(searchStartTime);
    }

    public void onYearMonthDayEndPicker(View view) {
        DatePickerShow(searchEndTime);
    }

    private void DatePickerShow(final Button searchTime) {
        Calendar calendar = Calendar.getInstance();
        // 取得系统日期:
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        // 取得系统时间
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        final DatePicker picker = new DatePicker(this);
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(ConvertUtils.toPx(this, 20));
        picker.setRangeStart(2017, 1, 1);
        picker.setRangeEnd(year, month, day);
        picker.setSelectedItem(year, month, day);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                searchTime.setText(year + "-" + month + "-" + day);
            }
        });

        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();
    }

    public void taskSearch() {
        searchContent = searchET.getText().toString().trim();
        startTime = searchStartTime.getText().toString().trim();
        endTime = searchEndTime.getText().toString().trim();
        String url = getResources().getString(R.string.service_host_address).concat(getString(R.string.taskListSearch));
        Map<String, String> param = new HashMap<>();
        param.put("c", MyApplication.getInstance().getC());
        if (!TextUtils.isEmpty(cityId)) {
            param.put("CityId", cityId);
        }
        if (!TextUtils.isEmpty(mediaId)) {
            param.put("MediaTreeID", mediaId);
        }
        if (!TextUtils.isEmpty(companyId)) {
            param.put("CompanyId", companyId);
        }
        param.put("pagecount", "1");
        param.put("pagesize", "10");
        if (moneyClickSign) {
            param.put("sort", "1");
        } else if (willExpireClickSign) {
            param.put("sort", "3");
        } else if (distanceClickSign) {
            param.put("sort", "2");
        }
        param.put("longitude", SharedPreferencesUtil.getString(getApplication(), "HomeLongitude", ""));
        param.put("latitude", SharedPreferencesUtil.getString(getApplication(), "HomeLatitude", ""));
        param.put("query", searchContent);
        if (taskState.equals(NonExecutionTaskFragment.NONEXECUTIONFRAGMENT)) {
            param.put("state", "0");
        } else if (taskState.equals(ExecutingTaskFragment.EXECUTIONINGFRAGMENT)) {
            param.put("state", "1");
        } else if (taskState.equals(CommonTaskUploadedFragment.COMUPLOADFRAGMENT)) {
            param.put("state", "2");
        } else if (taskState.equals(RejectTaskFragment.REJECTASKFRAGEMNTFLAG)) {
            param.put("state", "3");
        }
        if (TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
            makeText("请选择起始时间");
            return;
        } else if (!TextUtils.isEmpty(startTime) && TextUtils.isEmpty(endTime)) {
            makeText("请选择结束时间");
            return;
        }
        param.put("starttime", startTime);
        param.put("endtime", endTime);
        onQueryTextNetWork(url, param);
    }

    public void onQueryTextNetWork(String url, Map<String, String> param) {
        if (StringUtils.isSpace(url)) {
            Toast.makeText(this, "网络路径无效", Toast.LENGTH_LONG).show();
            return;
        }
        Utils.getUtils().showProgressDialog(this);
        MyOkHttp.get().post(getApplication(), url, param, new GsonResponseHandler<BaseBeans<List<TaskListBean.TaskList>>>() {
            @Override
            public void onSuccess(int statusCode, BaseBeans<List<TaskListBean.TaskList>> response) {
                Utils.getUtils().dismissDialog();
                if (response.getData().size() > 0) {
                    if (taskState.equals(NonExecutionTaskFragment.NONEXECUTIONFRAGMENT)) {
                        // 说明未执行中已经搜索过数据，在执行中需要重新刷新页面
                        SharedPreferencesUtil.saveBoolean(getApplication(), "NonExecution", true);
                        if (moneyClickSign) {
                            // moneyQueryText = query;
                            moneyQueryText = searchContent;
                            moneyCityId = cityId;
                            moneyCompanyId = companyId;
                            moneyMediaId = mediaId;
                            moneySearchAddressList.clear();
                            moneySearchAddressList.addAll(response.getData());
                        } else if (willExpireClickSign) {
                            // willExpireQueryText = query;
                            willExpireQueryText = searchContent;
                            willExpireCityId = cityId;
                            willExpireCompanyId = companyId;
                            willExpireMediaId = mediaId;
                            willExpireSearchAddressList.clear();
                            willExpireSearchAddressList.addAll(response.getData());
                        } else if (distanceClickSign) {
                            // distanceQueryText = query;
                            distanceQueryText = searchContent;
                            distanceCityId = cityId;
                            distanceCompanyId = companyId;
                            distanceMediaId = mediaId;
                            searchAddressList.clear();
                            searchAddressList.addAll(response.getData());
                        }
                    } else if (taskState.equals(ExecutingTaskFragment.EXECUTIONINGFRAGMENT)) {
                        // 说明执行中已经搜索过数据，在未执行中需要重新刷新页面
                        SharedPreferencesUtil.saveBoolean(getApplication(), "Executioning", true);
                        if (ExecMoneyAtMostFragment.moneyClickSign) {
                            // ExecMoneyAtMostFragment.moneyQueryText = query;
                            ExecMoneyAtMostFragment.moneyQueryText = searchContent;
                            ExecMoneyAtMostFragment.moneyCityId = cityId;
                            ExecMoneyAtMostFragment.moneyCompanyId = companyId;
                            ExecMoneyAtMostFragment.moneyMediaId = mediaId;
                            ExecMoneyAtMostFragment.moneySearchAddressList.clear();
                            ExecMoneyAtMostFragment.moneySearchAddressList.addAll(response.getData());
                        } else if (ExecWillExpireFragment.willExpireClickSign) {
                            // ExecWillExpireFragment.willExpireQueryText = query;
                            ExecWillExpireFragment.willExpireQueryText = searchContent;
                            ExecWillExpireFragment.willExpireCityId = cityId;
                            ExecWillExpireFragment.willExpireCompanyId = companyId;
                            ExecWillExpireFragment.willExpireMediaId = mediaId;
                            ExecWillExpireFragment.willExpireSearchAddressList.clear();
                            ExecWillExpireFragment.willExpireSearchAddressList.addAll(response.getData());
                        } else if (ExecRecentlyDistanceFragment.distanceClickSign) {
                            // ExecRecentlyDistanceFragment.distanceQueryText = query;
                            ExecRecentlyDistanceFragment.distanceQueryText = searchContent;
                            ExecRecentlyDistanceFragment.distanceCityId = cityId;
                            ExecRecentlyDistanceFragment.distanceCompanyId = companyId;
                            ExecRecentlyDistanceFragment.distanceMediaId = mediaId;
                            ExecRecentlyDistanceFragment.searchAddressList.clear();
                            ExecRecentlyDistanceFragment.searchAddressList.addAll(response.getData());
                        }
                    } else if (taskState.equals(CommonTaskUploadedFragment.COMUPLOADFRAGMENT)) {
                        SharedPreferencesUtil.saveBoolean(getApplication(), "CompletedUpload", true);
                        completedUploadQueryText = searchContent;
                        completedUploadCityId = cityId;
                        completedUploadCompanyId = companyId;
                        completedUploadMediaId = mediaId;
                        completedUploadStartTime = startTime;
                        completedUploadEndTime = endTime;
                        taskLists.clear();
                        taskLists.addAll(response.getData());
                    }
                    finish();
                } else {
                    makeText(response.getErrorMsg());
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
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
        application.setTaskList(JSON.toJSONString(list));
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

    private void mediatype() {
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
