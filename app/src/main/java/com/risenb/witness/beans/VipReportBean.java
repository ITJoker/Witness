package com.risenb.witness.beans;

public class VipReportBean {
//    RegisterTime:注册天数,
//    CouponNumber:领取的卡卷数量,
//    TaskCount:任务完成的个数,
//    GetIntegral:已领取积分,
//    GetBonus:已领取奖金

    private String TaskCount; //"taskcount":3,
    private String GetBonus;//      "getbonus":50,
    private String RegisterTime;//    "registertime":1477640492,
    private String GetIntegral;//   "getintegral":12,
    private String CouponNumber;//  "couponnumber":5、

    public String getTaskCount() {
        return TaskCount;
    }

    public void setTaskCount(String taskCount) {
        TaskCount = taskCount;
    }

    public String getGetBonus() {
        return GetBonus;
    }

    public void setGetBonus(String getBonus) {
        GetBonus = getBonus;
    }

    public String getRegisterTime() {
        return RegisterTime;
    }

    public void setRegisterTime(String registerTime) {
        RegisterTime = registerTime;
    }

    public String getGetIntegral() {
        return GetIntegral;
    }

    public void setGetIntegral(String getIntegral) {
        GetIntegral = getIntegral;
    }

    public String getCouponNumber() {
        return CouponNumber;
    }

    public void setCouponNumber(String couponNumber) {
        CouponNumber = couponNumber;
    }
}
