package com.risenb.witness.beans;

public class MoneyRecordBean {
    private int withdrawtime; //:提现时间,
    private String price; //:提现金额

    public int getWithdrawTime() {
        return withdrawtime;
    }

    public void setWithdrawTime(int withdrawtime) {
        this.withdrawtime = withdrawtime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
