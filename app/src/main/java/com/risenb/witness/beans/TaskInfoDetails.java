package com.risenb.witness.beans;

import java.io.Serializable;
import java.util.List;

public class TaskInfoDetails implements Serializable{
    /**
     * address : 北京市北京市朝阳区国贸建外SOHO111任务id 4
     * price : 70
     * validity : 1233787
     * remainTime : -319417
     * Modeltype : 1
     * taskid : 4
     * taskList : [{"isType":"1","taskRemark":"拍清楚","exampleFile":"http://yingwei.4pole.cn/Public/Customer_picture/2016-10-26/5810270e6c8cb.jpg","sort":"1","taskId":"4"},{"isType":"0","taskRemark":null,"exampleFile":"http://yingwei.4pole.cn/Public/Customer_picture/2016-10-26/5810270e6c8cb.jpg","sort":"2","taskId":"4"},{"isType":"1","taskRemark":"拍清楚","exampleFile":"http://yingwei.4pole.cn/Public/Customer_picture/2016-10-26/5810270e6c8cb.jpg","sort":"3","taskId":"4"}]
     */

    private String address;
    private String price;
    private String validity;
    private int remainTime;
    private String Modeltype;
    private String taskid;
    /**
     * isType : 1
     * taskRemark : 拍清楚
     * exampleFile : http://yingwei.4pole.cn/Public/Customer_picture/2016-10-26/5810270e6c8cb.jpg
     * sort : 1
     * taskId : 4
     */

    private List<TaskListBean> taskList;

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

    public int getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(int remainTime) {
        this.remainTime = remainTime;
    }

    public String getModeltype() {
        return Modeltype;
    }

    public void setModeltype(String Modeltype) {
        this.Modeltype = Modeltype;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public List<TaskListBean> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<TaskListBean> taskList) {
        this.taskList = taskList;
    }

    public static class TaskListBean {
        private String isType;
        private String taskRemark;
        private List<String> exampleFile;
        private String sort;
        private String taskId;

        public String getIsType() {
            return isType;
        }

        public void setIsType(String isType) {
            this.isType = isType;
        }

        public String getTaskRemark() {
            return taskRemark;
        }

        public void setTaskRemark(String taskRemark) {
            this.taskRemark = taskRemark;
        }

        public List<String> getExampleFile() {
            return exampleFile;
        }

        public void setExampleFile(List<String> exampleFile) {
            this.exampleFile = exampleFile;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        @Override
        public String toString() {
            return "TaskListBean{" +
                    "isType='" + isType + '\'' +
                    ", taskRemark='" + taskRemark + '\'' +
                    ", exampleFile='" + exampleFile + '\'' +
                    ", sort='" + sort + '\'' +
                    ", taskId='" + taskId + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TaskInfoDetails{" +
                "address='" + address + '\'' +
                ", price='" + price + '\'' +
                ", validity='" + validity + '\'' +
                ", remainTime=" + remainTime +
                ", Modeltype='" + Modeltype + '\'' +
                ", taskid='" + taskid + '\'' +
                ", taskList=" + taskList +
                '}';
    }
}
