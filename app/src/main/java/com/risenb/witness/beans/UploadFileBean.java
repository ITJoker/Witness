package com.risenb.witness.beans;

public class UploadFileBean {
    /**
     * returnfile : 用户上传的文件
     * longtitude : 文件经度
     * latitude : 文件纬度
     * type : 文件类型
     * sort : 文件顺序
     */

    private String returnfile;
    private String longtitude;
    private String latitude;
    private String type;
    private String sort;

    public String getReturnfile() {
        return returnfile;
    }

    public void setReturnfile(String returnfile) {
        this.returnfile = returnfile;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
