package com.risenb.witness.beans;

public class HomeMapsMakerBeans {

    private String taskId; //"taskId":"2",
    private String price; //      "price":"50",
    private String remainTime; //       "remainTime":"2",
    private String address; //       "address":"北京市朝阳区见外SOHO",
    private String validity; //       "validity":"2"

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(String remainTime) {
        this.remainTime = remainTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

}
