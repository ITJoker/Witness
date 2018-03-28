package com.risenb.witness.utils.newUtils;

import android.support.v4.view.ViewPager;
import android.widget.Button;

import com.risenb.witness.views.newViews.ShowImageView;

public class ShowBean {
    private Button btn;
    private ViewPager viewPager;
    private ShowImageView[] sivArr;
    private int position;

    public ShowBean() {
    }

    public Button getBtn() {
        return this.btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    public ViewPager getViewPager() {
        return this.viewPager;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public ShowImageView getShowImageView() {
        return this.sivArr[this.position];
    }

    public ShowImageView[] getSivArr() {
        return this.sivArr;
    }

    public void setSivArr(ShowImageView[] sivArr) {
        this.sivArr = sivArr;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
