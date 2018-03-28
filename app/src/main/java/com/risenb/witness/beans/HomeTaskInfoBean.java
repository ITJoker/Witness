package com.risenb.witness.beans;

import java.util.List;

public class HomeTaskInfoBean {
    /**
     * address : 北京市北京市丰台区左安门外南方庄1号
     * longitude : 116.4430342665148
     * image : ["http://www.adexmall.net//Public/Admin_picture/2017-02-23/20170223180037229728.jpg","http://www.adexmall.net//Public/Admin_picture/2017-02-23/20170223180037651411.jpg","http://www.adexmall.net//Public/Admin_picture/2017-02-23/20170223180037338261.jpg"]
     * state : 2
     * modeltype : 3
     * ScheduleName : 100条任务
     * trainid : 0
     * price : 13.00
     * other : {"mediatype":"车内","remark":""}
     * validity : 172800
     * latitude : 39.86251877781435
     * istrain : 0
     * page : 3
     * remainTime : 172575
     * taskId : 3284
     */

    private String address;
    private String longitude;
    private String state;
    private String modeltype;
    private String ScheduleName;
    private String trainid;
    private String price;
    private OtherBean other;
    private String validity;
    private String latitude;
    private int istrain;
    private int page;
    private int remainTime;
    private String taskId;
    private List<String> image;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public OtherBean getOther() {
        return other;
    }

    public void setOther(OtherBean other) {
        this.other = other;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getIstrain() {
        return istrain;
    }

    public void setIstrain(int istrain) {
        this.istrain = istrain;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(int remainTime) {
        this.remainTime = remainTime;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public static class OtherBean {
        /**
         * mediatype : 车内
         * remark :
         */

        private String mediatype;
        private String remark;

        public String getMediatype() {
            return mediatype;
        }

        public void setMediatype(String mediatype) {
            this.mediatype = mediatype;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
