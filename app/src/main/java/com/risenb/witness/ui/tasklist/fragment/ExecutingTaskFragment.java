package com.risenb.witness.ui.tasklist.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.beans.BaseBeans;
import com.risenb.witness.beans.TaskListBean;
import com.risenb.witness.network.OkHttpUtils.MyOkHttp;
import com.risenb.witness.network.OkHttpUtils.response.GsonResponseHandler;
import com.risenb.witness.ui.tasklist.adapter.ExecFragmentPagerAdapter;
import com.risenb.witness.utils.CommonUtils;
import com.risenb.witness.utils.SharedPreferencesUtil;
import com.risenb.witness.utils.newUtils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 执行中fragment
 */
public class ExecutingTaskFragment extends Fragment {
    protected Activity mActivity;
    public static final String EXECUTIONINGFRAGMENT = "Executioning";
    @BindView(R.id.tab_backgroundone)
    View mTabBackgroundone;
    @BindView(R.id.tab_backgroundtwo)
    View mTabBackgroundtwo;
    @BindView(R.id.tab_backgroundthree)
    View mTabBackgroundthree;

    //最近距离
    @BindView(R.id.text_recentlydistance)
    TextView mRecentlyDistance;
    //金额最高
    @BindView(R.id.text_moneyatmost)
    TextView mMoneyAtMost;
    //即将到期
    @BindView(R.id.text_willexpire)
    TextView mWillExpire;

    @BindView(R.id.executioning_viewpager)
    ViewPager mExeingViewPager;

    private ExecFragmentPagerAdapter mExecFragmentPagerAdapter;
    private List<Fragment> mFragmentList;

    private TextView[] tabs;
    private int currentIndex;

    private double type;
    private ExecRecentlyDistanceFragment mRecentlyDistanceFragment;
    private ExecMoneyAtMostFragment mMoneyAtMostFragment;
    private ExecWillExpireFragment mWillExpireFragment;
    private Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mExecFragmentPagerAdapter != null) {
            mExecFragmentPagerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 通过add添加Fragment会触发Fragment生命周期，hide和show不会触发生命周期
        mFragmentList = new ArrayList<>(3);
        mRecentlyDistanceFragment = ExecRecentlyDistanceFragment.newInstance(EXECUTIONINGFRAGMENT);
        mMoneyAtMostFragment = ExecMoneyAtMostFragment.newInstance(EXECUTIONINGFRAGMENT);
        mWillExpireFragment = ExecWillExpireFragment.newInstance(EXECUTIONINGFRAGMENT);
        mFragmentList.add(mRecentlyDistanceFragment);
        mFragmentList.add(mMoneyAtMostFragment);
        mFragmentList.add(mWillExpireFragment);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exeingtaskfragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        tabs = new TextView[3];
        tabs[0] = mRecentlyDistance;
        tabs[1] = mMoneyAtMost;
        tabs[2] = mWillExpire;
        mExecFragmentPagerAdapter = new ExecFragmentPagerAdapter(getChildFragmentManager(), mFragmentList);
        mExeingViewPager.setAdapter(mExecFragmentPagerAdapter);
        initListener();
        return view;
    }

    private void initListener() {
        mExeingViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                DisplayMetrics metrics = new DisplayMetrics();
                mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int num = CommonUtils.dip2px(mActivity, 0);
                int width2 = mTabBackgroundone.getWidth() + num;
                int w = (int) (positionOffset * width2) + position * width2;
                ViewHelper.setTranslationX(mTabBackgroundone, w);
                if (position ==0 ) {
                    // 初始Fragment为RecentlyDistanceFragment
                    mRecentlyDistanceFragment.distanceClickSign = true;
                }
            }

            @Override
            public void onPageSelected(int position) {
                mTabBackgroundone.clearAnimation();
                tabs[currentIndex].setSelected(false);
                tabs[position].setSelected(true);
                currentIndex = position;
                tabColorChange(currentIndex);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick(R.id.text_recentlydistance)
    public void OnClickRecentlyDistance(View view) {
        onTabClick(view);
    }

    @OnClick(R.id.text_moneyatmost)
    public void OnClickMoneyAtMost(View view) {
        onTabClick(view);
    }

    @OnClick(R.id.text_willexpire)
    public void OnClickWillExpire(View view) {
        onTabClick(view);
    }

    // TabView的点击事件
    public void onTabClick(View v) {
        if (v.isSelected()) {
            return;
        }
        tabs[currentIndex].setSelected(false);
        int id = v.getId();
        switch (id) {
            case R.id.text_recentlydistance:
                currentIndex = 0;
                tabColorChange(currentIndex);
                break;
            case R.id.text_moneyatmost:
                currentIndex = 1;
                tabColorChange(currentIndex);
                break;
            case R.id.text_willexpire:
                currentIndex = 2;
                tabColorChange(currentIndex);
                break;
        }
        tabs[currentIndex].setSelected(true);
        mExeingViewPager.setCurrentItem(currentIndex, true);
    }

    public void tabColorChange(int currentIndex) {
        if (currentIndex == 0) {
            mRecentlyDistanceFragment.distanceClickSign = true;
            mMoneyAtMostFragment.moneyClickSign = false;
            mWillExpireFragment.willExpireClickSign = false;
            mRecentlyDistance.setTextColor(getResources().getColor(R.color.main_green));
            mMoneyAtMost.setTextColor(getResources().getColor(R.color.main_gray));
            mWillExpire.setTextColor(getResources().getColor(R.color.main_gray));
        } else if (currentIndex == 1) {
            mRecentlyDistanceFragment.distanceClickSign = false;
            mMoneyAtMostFragment.moneyClickSign = true;
            mWillExpireFragment.willExpireClickSign = false;
            mRecentlyDistance.setTextColor(getResources().getColor(R.color.main_gray));
            mMoneyAtMost.setTextColor(getResources().getColor(R.color.main_green));
            mWillExpire.setTextColor(getResources().getColor(R.color.main_gray));
        } else if (currentIndex == 2) {
            mRecentlyDistanceFragment.distanceClickSign = false;
            mMoneyAtMostFragment.moneyClickSign = false;
            mWillExpireFragment.willExpireClickSign = true;
            mRecentlyDistance.setTextColor(getResources().getColor(R.color.main_gray));
            mMoneyAtMost.setTextColor(getResources().getColor(R.color.main_gray));
            mWillExpire.setTextColor(getResources().getColor(R.color.main_green));
        }
    }

    public void taskSearch(String cityId, String mediaId, String companyId, int index) {
        Utils.getUtils().showProgressDialog(getActivity());
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
//        param.put("pagecount", String.valueOf(page));
//        param.put("pagesize", "10");
        //最外层fragment
        if (index == 0) {
            //未执行
            param.put("state", String.valueOf(index));
        } else if (index == 1) {
            //执行中
            param.put("state", String.valueOf(index));
        } else if (index == 2) {

        } else if (index == 3) {

        }
        //最内层3个fragment
        if (type == 0) {
            //根据距离最近排序
            param.put("sort", "2");
        } else if (type == 1) {
            //根据金额最高排序
            param.put("sort", "1");
        } else if (type == 2) {
            //即将到期排序
            param.put("sort", "3");
        }
        param.put("longitude", SharedPreferencesUtil.getString(getContext(), "HomeLongitude", ""));
        param.put("latitude", SharedPreferencesUtil.getString(getContext(), "HomeLatitude", ""));
        MyOkHttp.get().post(getActivity(), url, param, new GsonResponseHandler<BaseBeans<List<TaskListBean.TaskList>>>() {
            @Override
            public void onFailure(int statusCode, String error_msg) {
                Utils.getUtils().dismissDialog();
            }

            @Override
            public void onSuccess(int statusCode, BaseBeans<List<TaskListBean.TaskList>> response) {
                MyApplication.handler.postDelayed(new Runnable() {
                    public void run() {
                        Utils.getUtils().dismissDialog();
                    }
                }, 600);

                List<TaskListBean.TaskList> data = response.getData();
                if (type == 0) {
                    mRecentlyDistanceFragment.setData(data);
                } else if (type == 1) {
                    mMoneyAtMostFragment.setData(data);
                } else if (type == 2) {
                    mWillExpireFragment.setData(data);
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        SharedPreferencesUtil.saveBoolean(getContext(), "Executioning", false);
    }
}
