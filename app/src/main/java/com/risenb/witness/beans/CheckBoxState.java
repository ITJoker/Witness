package com.risenb.witness.beans;

import java.util.HashMap;

public class CheckBoxState {

    private HashMap<Integer, Boolean> isSelected;
    private String id;

    public CheckBoxState(){}
    public CheckBoxState(HashMap<Integer, Boolean> isSelected, String id) {
        this.isSelected = isSelected;
        this.id = id;
    }

    public HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        this.isSelected = isSelected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
