package com.risenb.witness.enums;

import android.widget.RadioButton;

import com.risenb.witness.R;
import com.risenb.witness.ui.tasklist.TaskListUI;
import com.risenb.witness.ui.home.HomeUI;
import com.risenb.witness.ui.vip.VipUI;

public enum EnumTAB {
    TAB1("tab1", R.id.rb_tab_1, R.drawable.tab_1, "我的任务", TaskListUI.class),
    TAB2("tab2", R.id.rb_tab_2, R.drawable.tab_2, "任务大厅", HomeUI.class),
    TAB3("tab3", R.id.rb_tab_3, R.drawable.tab_3, "个人中心", VipUI.class);

    private int id;
    private int drawable;
    private String tag;
    private String title;
    private Class<?> clazz;
    private RadioButton radioButton;

    EnumTAB(String tag, int id, int drawable, String title, Class<?> clazz) {
        this.tag = tag;
        this.id = id;
        this.drawable = drawable;
        this.title = title;
        this.clazz = clazz;
    }

    public int getId() {
        return id;
    }

    public int getDrawable() {
        return drawable;
    }

    public String getTag() {
        return tag;
    }

    public String getTitle() {
        return title;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setRadioButton(RadioButton radioButton) {
        this.radioButton = radioButton;
    }

    public RadioButton getRadioButton() {
        return radioButton;
    }
}
