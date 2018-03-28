package com.risenb.witness.beans;

import java.util.List;

public class Tasklist {
    private List<String> exampleFile; //"exampleFile":Array[1],
    private int taskId; //        "taskId":2,
    private int sort; //       "sort":1,
    private int isType; //      "isType":1,
    private String longitude; //       "longitude":"40.45",
    private String latitude; //     "latitude":"110.23",
    private String taskRemark; //      "taskRemark":"拍清晰"

    public List<String> getExampleFile() {
        return exampleFile;
    }

    public void setExampleFile(List<String> exampleFile) {
        this.exampleFile = exampleFile;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getIsType() {
        return isType;
    }

    public void setIsType(int isType) {
        this.isType = isType;
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

    public String getTaskRemark() {
        return taskRemark;
    }

    public void setTaskRemark(String taskRemark) {
        this.taskRemark = taskRemark;
    }

    @Override
    public String toString() {
        return "Tasklist{" +
                "exampleFile=" + exampleFile +
                ", taskId=" + taskId +
                ", sort=" + sort +
                ", isType=" + isType +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", taskRemark='" + taskRemark + '\'' +
                '}';
    }
}
