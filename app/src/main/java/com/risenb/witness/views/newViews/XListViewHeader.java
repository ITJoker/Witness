package com.risenb.witness.views.newViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class XListViewHeader extends LinearLayout {
    private LinearLayout mContainer;
    private ImageView mArrowImageView;
    private ProgressBar mProgressBar;
    private TextView mHintTextView;
    private int mState = 0;
    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;
    private final int ROTATE_ANIM_DURATION = 180;
    public static final int STATE_NORMAL = 0;
    public static final int STATE_READY = 1;
    public static final int STATE_REFRESHING = 2;

    public XListViewHeader(Context context) {
        super(context);
        this.initView(context);
    }

    public XListViewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }

    private void initView(Context context) {
        LayoutParams lp = new LayoutParams(-1, 0);
        this.mContainer = (LinearLayout) LayoutInflater.from(context).inflate(XListUtils.getXListUtils().xlistview_header, (ViewGroup)null);
        this.addView(this.mContainer, lp);
        this.setGravity(80);
        this.mArrowImageView = (ImageView)this.findViewById(XListUtils.getXListUtils().xlistview_header_arrow);
        this.mHintTextView = (TextView)this.findViewById(XListUtils.getXListUtils().xlistview_header_hint_textview);
        this.mProgressBar = (ProgressBar)this.findViewById(XListUtils.getXListUtils().xlistview_header_progressbar);
        this.mRotateUpAnim = new RotateAnimation(0.0F, -180.0F, 1, 0.5F, 1, 0.5F);
        this.mRotateUpAnim.setDuration(180L);
        this.mRotateUpAnim.setFillAfter(true);
        this.mRotateDownAnim = new RotateAnimation(-180.0F, 0.0F, 1, 0.5F, 1, 0.5F);
        this.mRotateDownAnim.setDuration(180L);
        this.mRotateDownAnim.setFillAfter(true);
    }

    public void setState(int state) {
        if(state != this.mState) {
            if(state == 2) {
                this.mArrowImageView.clearAnimation();
                this.mArrowImageView.setVisibility(4);
                this.mProgressBar.setVisibility(0);
            } else {
                this.mArrowImageView.setVisibility(0);
                this.mProgressBar.setVisibility(4);
            }

            switch(state) {
                case 0:
                    if(this.mState == 1) {
                        this.mArrowImageView.startAnimation(this.mRotateDownAnim);
                    }

                    if(this.mState == 2) {
                        this.mArrowImageView.clearAnimation();
                    }

                    this.mHintTextView.setText("下拉刷新");
                    break;
                case 1:
                    if(this.mState != 1) {
                        this.mArrowImageView.clearAnimation();
                        this.mArrowImageView.startAnimation(this.mRotateUpAnim);
                        this.mHintTextView.setText("松开刷新数据");
                    }
                    break;
                case 2:
                    this.mHintTextView.setText("正在加载");
            }

            this.mState = state;
        }
    }

    public void setVisiableHeight(int height) {
        if(height < 0) {
            height = 0;
        }

        LayoutParams lp = (LayoutParams)this.mContainer.getLayoutParams();
        lp.height = height;
        this.mContainer.setLayoutParams(lp);
    }

    public int getVisiableHeight() {
        return this.mContainer.getHeight();
    }
}
