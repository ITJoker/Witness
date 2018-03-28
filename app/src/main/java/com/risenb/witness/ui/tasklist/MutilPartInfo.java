package com.risenb.witness.ui.tasklist;

import java.util.List;

public class MutilPartInfo {
    private String title;
    private String isType;
    private List<CheckBoxInfo> checkBoxInfo;

    public MutilPartInfo() {
    }

    public List<CheckBoxInfo> getCheckBoxInfo() {
        return checkBoxInfo;
    }

    public void setCheckBoxInfo(List<CheckBoxInfo> checkBoxInfo) {
        this.checkBoxInfo = checkBoxInfo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsType() {
        return isType;
    }

    public void setIsType(String isType) {
        this.isType = isType;
    }
}
