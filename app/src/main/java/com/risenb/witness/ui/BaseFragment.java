package com.risenb.witness.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.risenb.witness.MyApplication;
import com.risenb.witness.R;
import com.risenb.witness.pop.UserOutPop;
import com.risenb.witness.ui.login.LoginUI;
import com.risenb.witness.utils.newUtils.UIManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 成员变量:编译看左边(父类),运行看左边(父类)
 * 成员方法:编译看左边(父类),运行看右边(子类),动态绑定
 * 静态方法:编译看左边(父类),运行看左边(父类)(静态和类相关,不算重写,所以访问还是左边的)
 * 只有非静态的成员方法,编译看左边,运行看右边
 */
public abstract class BaseFragment extends Fragment {
    //子类亦需要使用,使用protected修饰
    protected Activity mActivity;
    @BindView(R.id.title)
    TextView fragmentTitle;
    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;

    /**
     * 创建Fragment的View时调用,返回控件越快越好,最好不要在此方法中对控件做数据展示操作
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*
         * 获取当前Fragment所在的宿主Activity,实为MainUI对象,将其作为Context使用
		 */
        mActivity = getActivity();
        View view = initView();
        ButterKnife.bind(this, view);
        return view;
    }

    /**
     * 子类必须实现返回View的方法
     */
    public abstract View initView();

    /**
     * onActivityCreated():位于Fragment生命周期中onCreateView()之后,可在此方法中对数据进行操作
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*if (isVisible) {
            initData();
        } else {
            MyApplication.handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initData();
                }
            }, 6000);
        }*/
        initData();
    }

    /**
     * 子类可实现也可不实现,看自身需求
     */
    public void initData() {

    }

    /**
     * 设置标题
     */
    public void setFragmentTitle(String title) {
        if (fragmentTitle != null) {
            fragmentTitle.setText(title);
        }
    }

    protected void loginError() {
        MyApplication.getInstance().setC("");
        final UserOutPop userOutPop = new UserOutPop(fragmentTitle, getActivity(), R.layout.useroutpop);
        userOutPop.showAsDropDownInstance();
        userOutPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.useroutpop_qx_tv:
                        userOutPop.dismiss();
                        UIManager.getInstance().popAllActivity();
                        break;
                    case R.id.useroutpop_ok_tv:
                        startActivity(new Intent(getActivity(), LoginUI.class));
                        userOutPop.dismiss();
                        break;
                }
            }
        });
    }

    /**
     * 判断当前Fragment是否可见取消预加载
     * adapter中的每个fragment切换的时候都会被调用，如果是切换到当前页，那么isVisibleToUser==true，否则为false
     * viewpager监听切换tab事件，tab切换一次，执行一次setUserVisibleHint()方法
     * setUserVisibleHint()在fragment所有生命周期之前，无论viewpager是在activity哪个生命周期里初始化
     * activity生命周期和fragment生命周期时序并不是按序来的，也就是说fragment的onCreate方法时序并不一定在activity的onCreate方法之后
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
            /*onVisible();*/
        } else {
            isVisible = false;
			/*onInvisible();*/
        }
    }

	/*protected abstract void onVisible();

	protected abstract void onInvisible();*/

    /**
     * 此方法只是关闭软键盘
     */
    protected void hintKbTwo() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive() && getActivity().getCurrentFocus() != null) {
            if (getActivity().getCurrentFocus().getWindowToken() != null) {
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}