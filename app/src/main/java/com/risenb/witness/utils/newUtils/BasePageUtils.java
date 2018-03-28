package com.risenb.witness.utils.newUtils;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

public class BasePageUtils<T extends BaseMenuBean> {
    private RadioGroup radioGroup;
    private View cursor;
    private HorizontalScrollView horizontalScrollView;
    private ViewPager viewPager;
    private List<T> list;
    private FragmentActivity activity;
    private int width;
    private int numColumns = 5;
    private int margin;
    private int radioLayoutID;
    private int radioButton00;
    private ViewPager.OnPageChangeListener onPageChangeListener;

    public BasePageUtils() {
    }

    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
    }

    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public void setRadioGroup(RadioGroup radioGroup) {
        this.radioGroup = radioGroup;
    }

    public void setCursor(View cursor) {
        this.cursor = cursor;
    }

    public void setHorizontalScrollView(HorizontalScrollView horizontalScrollView) {
        this.horizontalScrollView = horizontalScrollView;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public void setRadioLayoutID(int radioLayoutID) {
        this.radioLayoutID = radioLayoutID;
    }

    public void setRadioButton00(int radioButton00) {
        this.radioButton00 = radioButton00;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    public void info() {
        if (this.activity == null) {
            Log.e("activity == null");
        } else {
            DisplayMetrics outMetrics = new DisplayMetrics();
            this.activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
            this.width = outMetrics.widthPixels;
            LinearLayout.LayoutParams cursorParams = null;
            if (this.cursor != null) {
                cursorParams = (LinearLayout.LayoutParams) this.cursor.getLayoutParams();
                cursorParams.setMargins(this.margin, 0, 0, 0);
                this.cursor.setLayoutParams(cursorParams);
            }

            final LinearLayout.LayoutParams finalCursorParams = cursorParams;
            this.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                public void onPageScrollStateChanged(int arg0) {
                    if (BasePageUtils.this.onPageChangeListener != null) {
                        BasePageUtils.this.onPageChangeListener.onPageScrollStateChanged(arg0);
                    }

                }

                public void onPageScrolled(int position, float arg1, int arg2) {
                    if (BasePageUtils.this.onPageChangeListener != null) {
                        BasePageUtils.this.onPageChangeListener.onPageScrolled(position, arg1, arg2);
                    }

                    if (BasePageUtils.this.radioGroup != null) {
                        RadioButton left = (RadioButton) BasePageUtils.this.radioGroup.getChildAt(position);
                        if (left != null) {
                            left.setChecked(true);
                            int left1 = left.getLeft();
                            int right = left.getRight();
                            if (BasePageUtils.this.horizontalScrollView != null) {
                                BasePageUtils.this.horizontalScrollView.smoothScrollTo(left1 - BasePageUtils.this.width / 2 + (right - left1) / 2, 0);
                            }
                        }
                    }

                    if (finalCursorParams != null) {
                        int left2 = (int) ((float) BasePageUtils.this.width * ((float) position + arg1) / (float) BasePageUtils.this.numColumns + (float) BasePageUtils.this.margin);
                        finalCursorParams.setMargins(left2, 0, 0, 0);
                        BasePageUtils.this.cursor.setLayoutParams(finalCursorParams);
                    }

                }

                public void onPageSelected(int position) {
                    if (BasePageUtils.this.onPageChangeListener != null) {
                        BasePageUtils.this.onPageChangeListener.onPageSelected(position);
                    }

                }
            });
            if (this.radioGroup != null) {
                for (int baseFragmentAdapter = 0; baseFragmentAdapter < this.list.size(); ++baseFragmentAdapter) {
                    RadioButton radioButton = (RadioButton) LayoutInflater.from(this.activity).inflate(this.radioLayoutID, (ViewGroup) null);
                    radioButton.setWidth(this.width / this.numColumns);
                    radioButton.setId(this.radioButton00 + baseFragmentAdapter);
                    radioButton.setText(((BaseMenuBean) this.list.get(baseFragmentAdapter)).getBaseMenuBeanTitle());
                    radioButton.setTag(((BaseMenuBean) this.list.get(baseFragmentAdapter)).getBaseMenuBeanID());
                    this.radioGroup.addView(radioButton);
                    final int finalBaseFragmentAdapter = baseFragmentAdapter;
                    radioButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            BasePageUtils.this.viewPager.setCurrentItem(finalBaseFragmentAdapter, false);
                        }
                    });
                }

                RadioButton var7 = (RadioButton) this.radioGroup.getChildAt(0);
                var7.setChecked(true);
            }

            BaseFragmentAdapter var8 = new BaseFragmentAdapter(this.activity.getSupportFragmentManager());
            var8.setList(this.list);
            this.viewPager.setAdapter(var8);
        }
    }
}
