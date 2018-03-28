package com.risenb.witness.beans;

public class CompletedTaskBean {
    // 要删除的Bean
    /**
     * address : 北京市丰台区
     * price : 50
     * validity : 2
     * remainTime : 2
     * taskId : 13
     * image : img-url
     * state : 2
     */

    private String address;
    private String price;
    private String validity;
    private String remainTime;
    private String taskId;
    private String image;
    private String state;
    private String uploadtime;

    public String getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(String uploadtime) {
        this.uploadtime = uploadtime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(String remainTime) {
        this.remainTime = remainTime;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
