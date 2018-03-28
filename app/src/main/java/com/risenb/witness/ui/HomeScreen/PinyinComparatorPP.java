package com.risenb.witness.ui.HomeScreen;

import com.risenb.witness.beans.HomeScreenCompanyBean;

import java.util.Comparator;

public class PinyinComparatorPP implements Comparator<HomeScreenCompanyBean> {

	public int compare(HomeScreenCompanyBean o1, HomeScreenCompanyBean o2) {
		if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#") || o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
