package com.risenb.witness.utils.newUtils;

public class SeletTimeUtils {
    private static SeletTimeUtils seletTimeUtils;
    private int wheelVal;
    private int wheelBg;
    private int layoutID;
    private int cancelID;
    private int submitID;
    private int dpID;

    public SeletTimeUtils() {
    }

    public static SeletTimeUtils getSeletTimeUtils() {
        if(seletTimeUtils == null) {
            seletTimeUtils = new SeletTimeUtils();
        }

        return seletTimeUtils;
    }

    public void info(int wheelVal, int wheelBg, int layoutID, int cancelID, int submitID, int dpID) {
        this.wheelVal = wheelVal;
        this.wheelBg = wheelBg;
        this.layoutID = layoutID;
        this.cancelID = cancelID;
        this.submitID = submitID;
        this.dpID = dpID;
    }

    public int getWheelVal() {
        return this.wheelVal;
    }

    public int getWheelBg() {
        return this.wheelBg;
    }

    public int getLayoutID() {
        return this.layoutID;
    }

    public int getCancelID() {
        return this.cancelID;
    }

    public int getSubmitID() {
        return this.submitID;
    }

    public int getDpID() {
        return this.dpID;
    }

    public void setWheelVal(int wheelVal) {
        this.wheelVal = wheelVal;
    }
}
