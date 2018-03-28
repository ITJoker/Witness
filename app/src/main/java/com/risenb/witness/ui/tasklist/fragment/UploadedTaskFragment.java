package com.risenb.witness.ui.tasklist.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.risenb.witness.R;
import com.risenb.witness.ui.tasklist.adapter.ExecFragmentPagerAdapter;
import com.risenb.witness.utils.CommonUtils;
import com.risenb.witness.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 已上传fragment
 */
public class UploadedTaskFragment extends Fragment {
    protected Activity mActivity;
    public static final String COMUPLOADFRAGMENT = "CompletedUpload";
    @BindView(R.id.tab_backgroundone)
    View mTabBackgroundone;

    //排期任务
    @BindView(R.id.text_common_task)
    TextView mCommonTaskText;
    //固定任务
    @BindView(R.id.text_settled_task)
    TextView SettledTaskText;

    @BindView(R.id.uploaded_viewpager)
    ViewPager mUploadedViewPager;

    private ExecFragmentPagerAdapter mExecFragmentPagerAdapter;
    private List<Fragment> mFragmentList;

    private TextView[] tabs;
    private int currentIndex;

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
        mFragmentList = new ArrayList<>();
        CommonTaskUploadedFragment mCommonTaskUploadedFragment = CommonTaskUploadedFragment.newInstance(COMUPLOADFRAGMENT);
        SettledTasksUploadedFragment mSettledTasksUploadedFragment = SettledTasksUploadedFragment.newInstance(COMUPLOADFRAGMENT);
        mFragmentList.add(mCommonTaskUploadedFragment);
        mFragmentList.add(mSettledTasksUploadedFragment);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.uploadedtaskfragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        tabs = new TextView[2];
        tabs[0] = mCommonTaskText;
        tabs[1] = SettledTaskText;
        mExecFragmentPagerAdapter = new ExecFragmentPagerAdapter(getChildFragmentManager(), mFragmentList);
        mUploadedViewPager.setAdapter(mExecFragmentPagerAdapter);
        initListener();
        return view;
    }

    private void initListener() {
        mUploadedViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                DisplayMetrics metrics = new DisplayMetrics();
                mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int num = CommonUtils.dip2px(mActivity, 0);
                int width2 = mTabBackgroundone.getWidth() + num;
                int w = (int) (positionOffset * width2) + position * width2;
                ViewHelper.setTranslationX(mTabBackgroundone, w);
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

    @OnClick(R.id.text_common_task)
    public void OnClickRecentlyDistance(View view) {
        onTabClick(view);
    }

    @OnClick(R.id.text_settled_task)
    public void OnClickMoneyAtMost(View view) {
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
            case R.id.text_common_task:
                currentIndex = 0;
                tabColorChange(currentIndex);
                break;
            case R.id.text_settled_task:
                currentIndex = 1;
                tabColorChange(currentIndex);
                break;
        }
        tabs[currentIndex].setSelected(true);
        mUploadedViewPager.setCurrentItem(currentIndex, true);
    }

    public void tabColorChange(int currentIndex) {
        if (currentIndex == 0) {
            mCommonTaskText.setTextColor(getResources().getColor(R.color.main_green));
            SettledTaskText.setTextColor(getResources().getColor(R.color.main_gray));
        } else if (currentIndex == 1) {
            mCommonTaskText.setTextColor(getResources().getColor(R.color.main_gray));
            SettledTaskText.setTextColor(getResources().getColor(R.color.main_green));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        SharedPreferencesUtil.saveBoolean(getContext(), "CompletedUpload", false);
    }
}
