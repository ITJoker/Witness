package com.risenb.witness.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TabHost.TabSpec;

import com.risenb.witness.R;
import com.risenb.witness.enums.EnumTAB;
import com.risenb.witness.utils.newUtils.Utils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：导航
 */
public class TabUI extends TabActivity implements OnClickListener {
    private List<ImageView> ims = new ArrayList<>();
    private List<ImageView> tabIms = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tab);

        ImageView tab1_true_iv = findViewById(R.id.tab1_true_iv);
        ImageView tab2_true_iv = findViewById(R.id.tab2_true_iv);
        ImageView tab3_true_iv = findViewById(R.id.tab3_true_iv);
        ImageView tab1_prompttrue_iv = findViewById(R.id.tab1_prompttrue_iv);
        ImageView tab2_prompttrue_iv = findViewById(R.id.tab2_prompttrue_iv);
        ImageView tab3_prompttrue_iv = findViewById(R.id.tab3_prompttrue_iv);

        ims.add(tab1_true_iv);
        ims.add(tab2_true_iv);
        ims.add(tab3_true_iv);
        tabIms.add(tab1_prompttrue_iv);
        tabIms.add(tab2_prompttrue_iv);
        tabIms.add(tab3_prompttrue_iv);

        TabSpec spec;
        Intent intent;
        Drawable drawable;

        int right = Utils.getUtils().getDimen(this, R.dimen.dm040);
        int bottom = Utils.getUtils().getDimen(this, R.dimen.dm040);

        EnumTAB[] enumArr = EnumTAB.values();
        for (int i = 0; i < enumArr.length; i++) {
            /*
             * 循环初始化TaskListUI/HomeUI/VipUI
             */
            // 设置按键点击事件及文字
            enumArr[i].setRadioButton((RadioButton) findViewById(enumArr[i].getId()));
            enumArr[i].getRadioButton().setOnClickListener(this);
            enumArr[i].getRadioButton().setText(enumArr[i].getTitle());
            // 设置按键背景图片
            drawable = getResources().getDrawable(enumArr[i].getDrawable());
            drawable.setBounds(0, 0, right, bottom);
            enumArr[i].getRadioButton().setCompoundDrawables(null, drawable, null, null);
            // 通过反射获取UI页面并通过Intent进行开启初始化数据，然后依次将3个UI页面的添加到ArrayList<TabSpec>中，TabSpec为指示器说明
            intent = new Intent().setClass(this, enumArr[i].getClazz());
            spec = getTabHost().newTabSpec(enumArr[i].getTag()).setIndicator(enumArr[i].getTag()).setContent(intent);
            getTabHost().addTab(spec);
        }
        int type = getIntent().getIntExtra("type", -1);
        if (type > 0) {
            setCurrentTabByTag(enumArr[type - 1]);
            /*ims.get(type - 1).setVisibility(View.VISIBLE);*/
        }
    }

    public void setTabIms(final int i) {
        tabIms.get(i).setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        EnumTAB[] enumArr = EnumTAB.values();
        for (int i = 0; i < enumArr.length; i++) {
            if (enumArr[i].getId() == v.getId()) {
                /*ims.get(i).setVisibility(View.VISIBLE);*/
                setCurrentTabByTag(enumArr[i]);
            } else {
                /*ims.get(i).setVisibility(View.INVISIBLE);*/

                //改变之前方式
                enumArr[i].getRadioButton().setTextColor(getResources().getColor(R.color.main_gray));
            }
        }
    }

    public void setCurrentTabByTag(EnumTAB enumTab) {
        EnumTAB[] enumArr = EnumTAB.values();
        for (int i = 0; i < enumArr.length; i++) {
            /*enumArr[i].getRadioButton().setChecked(enumArr[i] == enumTab);*/

            //改变之前方式
            if (enumArr[i] == enumTab) {
                enumArr[i].getRadioButton().setChecked(true);
                enumArr[i].getRadioButton().setText(enumArr[i].getTitle());
                enumArr[i].getRadioButton().setTextColor(getResources().getColor(R.color.main_green));
            } else {
                enumArr[i].getRadioButton().setTextColor(getResources().getColor(R.color.main_gray));
            }

        }
        getTabHost().setCurrentTabByTag(enumTab.getTag());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
}