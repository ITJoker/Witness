package com.risenb.witness.beans;

import java.util.List;

public class TaskInfoPreviewBean {
    private String modeltype;
    private String price; //"price":"50",
    private int remainTime; //      "remainTime":"2",
    private String address; //      "address":"北京市朝阳区见外SOHO",
    private String validity; //      "validity":"2",
    private List<Tasklist> tasklist; //"tasklist":

    public String getModeltype() {
        return modeltype;
    }

    public void setModeltype(String modeltype) {
        this.modeltype = modeltype;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(int remainTime) {
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

    public List<Tasklist> getTasklist() {
        return tasklist;
    }

    public void setTasklist(List<Tasklist> tasklist) {
        this.tasklist = tasklist;
    }

    @Override
    public String toString() {
        return "TaskInfoPreviewBean{" +
                "price='" + price + '\'' +
                ", remainTime='" + remainTime + '\'' +
                ", address='" + address + '\'' +
                ", validity='" + validity + '\'' +
                ", tasklist=" + tasklist +
                '}';
    }
}
