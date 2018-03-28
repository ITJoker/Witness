package com.risenb.witness.ui.HomeScreen;

import com.risenb.witness.beans.HomeScreenCityBean;

import java.util.Comparator;

public class PinyinComparator implements Comparator<HomeScreenCityBean> {

    public int compare(HomeScreenCityBean o1, HomeScreenCityBean o2) {
        if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#") || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }

}
