package com.risenb.witness.beans;

import java.util.List;

public class NonTaskDetailBean {
    /**
     * address : 北京市北京市丰台区左安门外南方庄1号
     * image : ["http://www.adexmall.net//Public/Admin_picture/2017-02-23/20170223180037229728.jpg","http://www.adexmall.net//Public/Admin_picture/2017-02-23/20170223180037651411.jpg","http://www.adexmall.net//Public/Admin_picture/2017-02-23/20170223180037338261.jpg"]
     * price : 13.00
     * remainTime : 166250
     * validity : 172800
     * taskId : 3283
     * longitude : 116.4430342665148
     * latitude : 39.86251877781435
     * state : 3
     * modeltype : 3
     * ScheduleName : 100条任务
     * trainid : 0
     * other : {"remark":"","mediatype":"车内"}
     * page : 3
     * istrain : 0
     */

    private String address;
    private String price;
    private int remainTime;
    private String validity;
    private String taskId;
    private String longitude;
    private String latitude;
    private String state;
    private String modeltype;
    private String ScheduleName;
    private String trainid;
    private OtherBean other;
    private int page;
    private int istrain;
    private List<String> image;

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

    public int getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(int remainTime) {
        this.remainTime = remainTime;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getModeltype() {
        return modeltype;
    }

    public void setModeltype(String modeltype) {
        this.modeltype = modeltype;
    }

    public String getScheduleName() {
        return ScheduleName;
    }

    public void setScheduleName(String ScheduleName) {
        this.ScheduleName = ScheduleName;
    }

    public String getTrainid() {
        return trainid;
    }

    public void setTrainid(String trainid) {
        this.trainid = trainid;
    }

    public OtherBean getOther() {
        return other;
    }

    public void setOther(OtherBean other) {
        this.other = other;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getIstrain() {
        return istrain;
    }

    public void setIstrain(int istrain) {
        this.istrain = istrain;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public static class OtherBean {
        /**
         * remark :
         * mediatype : 车内
         */

        private String remark;
        private String mediatype;

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getMediatype() {
            return mediatype;
        }

        public void setMediatype(String mediatype) {
            this.mediatype = mediatype;
        }
    }
}
