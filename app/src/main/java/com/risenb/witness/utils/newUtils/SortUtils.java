package com.risenb.witness.utils.newUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortUtils {
    private static SortUtils srtUtils;
    private int popSideBar;
    private int tvPop;
    private boolean city = false;

    public SortUtils() {
    }

    public static SortUtils getSrtUtils() {
        if (srtUtils == null) {
            srtUtils = new SortUtils();
        }

        return srtUtils;
    }

    public void setCity(boolean city) {
        this.city = city;
    }

    public void info(int popSideBar, int tvPop) {
        this.popSideBar = popSideBar;
        this.tvPop = tvPop;
    }

    public int getPopSideBar() {
        return !LimitConfig.getLimitConfig().isLimit() ? 0 : this.popSideBar;
    }

    public int getTvPop() {
        return !LimitConfig.getLimitConfig().isLimit() ? 0 : this.tvPop;
    }

    public int[] sort(List list) {
        if (!LimitConfig.getLimitConfig().isLimit()) {
            return null;
        } else {
            int[] msb = new int[28];
            if (this.city) {
                Collections.sort(list, new Comparator<BaseSortBean>() {
                    public int compare(BaseSortBean lhs, BaseSortBean rhs) {
                        return lhs.getBaseSortBeanPYC().compareTo(rhs.getBaseSortBeanPYC());
                    }
                });
            } else {
                Collections.sort(list, new Comparator<BaseSortBean>() {
                    public int compare(BaseSortBean lhs, BaseSortBean rhs) {
                        return lhs.getBaseSortBeanPYS().compareTo(rhs.getBaseSortBeanPYS());
                    }
                });
            }

            for (int sortBean = 0; sortBean < msb.length; ++sortBean) {
                msb[sortBean] = -1;
            }

            int idx = 0;

            for (int i = 0; i < list.size(); ++i) {
                BaseSortBean var7 = (BaseSortBean) list.get(i);
                int j;
                if (i == 0 || var7.getBaseSortBeanID() != ((BaseSortBean) list.get(i - 1)).getBaseSortBeanID()) {
                    if (var7.getBaseSortBeanID() == 42) {
                        msb[0] = i;
                        idx = i;
                    } else if (var7.getBaseSortBeanID() == 35) {
                        msb[27] = i;
                        idx = i;
                    } else {
                        for (j = 0; j < var7.getBaseSortBeanID() - 64; ++j) {
                            if (msb[j] == -1) {
                                msb[j] = i;
                            }
                        }

                        msb[var7.getBaseSortBeanID() - 64] = i;
                        idx = i;
                    }
                }

                if (i == list.size() - 1) {
                    for (j = 0; j < msb.length; ++j) {
                        if (msb[j] == -1) {
                            msb[j] = idx;
                        }
                    }
                }
            }

            return msb;
        }
    }
}