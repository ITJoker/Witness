package com.risenb.witness.beans;

public class MoneyBean {
    private String balance ; //:余额,
    private String price ; //:提现金额,
    private String alipayusername ; //:支付宝账号,
    private String alipaytruename ; //:真实姓名

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAlipayusername() {
        return alipayusername;
    }

    public void setAlipayusername(String alipayusername) {
        this.alipayusername = alipayusername;
    }

    public String getAlipaytruename() {
        return alipaytruename;
    }

    public void setAlipaytruename(String alipaytruename) {
        this.alipaytruename = alipaytruename;
    }
}
