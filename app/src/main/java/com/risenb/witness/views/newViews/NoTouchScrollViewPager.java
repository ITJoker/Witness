package com.risenb.witness.views.newViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoTouchScrollViewPager extends ViewPager {
    public NoTouchScrollViewPager(Context context) {
        super(context);
    }

    public NoTouchScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置ContentFragment中ViewPager的条目不可滑动,在ContentFragment和布局R.layout.fragment_content中使用全类名修改!!!
     * 当最外层的NoTouchScrollViewPager遇到Touch事件时不进行处理,并非固定不可滑动,仍可由别的控件Touch事件引发ViewPager滑动!!!
     */
    @SuppressLint("all")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // 不需要父类的onTouchEvent(),而自己不进行处理,所以不用return true去消费,直接返回false即可
        // return super.onTouchEvent(ev)
        return false;
    }

    // 不拦截子控件的事件
    /*
     * ▲▲▲★★★最外层ViewPager拦截子控件的事件,具体表现为FrameLayout中的ViewPager向左滑动会带动最外层ViewPager滑动!▲▲▲★★★
	 * ViewPager extends ViewGroup,所以会有以下3个方法由外向内逐层传递事件,具体如下:
	 * 1、dispatchKeyEvent(event):分发事件处理的方法
	 * 2、onInterceptTouchEvent(MotionEvent ev):拦截事件的方法,可拦截子控件的事件,如果最后仍没有控件处理事件,事件便会丢失!
	 * 3、onTouchEvent(MotionEvent ev):处理事件的方法
	 */
    // View只有dispatchKeyEvent(event)和onTouchEvent(MotionEvent ev)两个处理事件的方法

    /**
     * 即便onTouchEvent() { return false; }不处理事件,onInterceptTouchEvent() { return true; }拦截子控件事件后也会有移动操作!
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // true:拦截子控件事件,false:不拦截子控件事件
        return false;
    }
}