package com.risenb.witness.ui.HomeScreen;

import com.risenb.witness.beans.HomeScreenMediatypeBean;

import java.util.Comparator;

public class PinyinComparatorMT implements Comparator<HomeScreenMediatypeBean> {

	public int compare(HomeScreenMediatypeBean o1, HomeScreenMediatypeBean o2) {
		if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#") || o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
