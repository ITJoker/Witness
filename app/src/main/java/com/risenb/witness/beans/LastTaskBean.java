package com.risenb.witness.beans;

import com.risenb.witness.ui.tasklist.CheckBoxInfo;

import java.util.List;

public class LastTaskBean {
    /**
     * address : 北京市北京市朝阳区国贸建外SOHO111任务id 4
     * price : 70
     * validity : 1233787
     * remainTime : -691319
     * Modeltype : 1
     * taskid : 4
     * taskList : [{"testId":"8","taskRemark":"你多大了填空？","isType":"5","exampleFile":[""]},{"isType":"1","taskRemark":"拍清楚","exampleFile":["http://yingwei.4pole.cn/Public/Customer_picture/2016-10-26/5810270e6c8cb.jpg"],"sort":"5"},{"testId":"9","taskRemark":"你多大了多选？","isType":"4","exampleFile":["15岁","16岁","17岁"]},{"testId":"10","taskRemark":"你多大了单选？","isType":"3","exampleFile":["15岁","16岁","17岁"]}]
     */

    private String address;
    private String price;
    private String validity;
    private int remainTime;
    private String Modeltype;
    private String taskid;
    /**
     * testId : 8
     * taskRemark : 你多大了填空？
     * isType : 5
     * exampleFile : [""]
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
        private String testId;
        private String taskRemark;
        private String isType;
        private List<String> exampleFile;
        private List<CheckBoxInfo> checkBoxInfos;
        private List<RadioButtonInfo> radioButtonInfos;
        private String sort;
        private List<String> returnfile ;

        public String getTestId() {
            return testId;
        }

        public void setTestId(String testId) {
            this.testId = testId;
        }

        public String getTaskRemark() {
            return taskRemark;
        }

        public void setTaskRemark(String taskRemark) {
            this.taskRemark = taskRemark;
        }

        public String getIsType() {
            return isType;
        }

        public void setIsType(String isType) {
            this.isType = isType;
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

        public List<CheckBoxInfo> getCheckBoxInfos() {
            return checkBoxInfos;
        }

        public void setCheckBoxInfos(List<CheckBoxInfo> checkBoxInfos) {
            this.checkBoxInfos = checkBoxInfos;
        }

        public List<RadioButtonInfo> getRadioButtonInfos() {
            return radioButtonInfos;
        }

        public void setRadioButtonInfos(List<RadioButtonInfo> radioButtonInfos) {
            this.radioButtonInfos = radioButtonInfos;
        }

        public List<String> getReturnfile() {
            return returnfile;
        }

        public void setReturnfile(List<String> returnfile) {
            this.returnfile = returnfile;
        }

        @Override
        public String toString() {
            return "TaskListBean{" +
                    "testId='" + testId + '\'' +
                    ", taskRemark='" + taskRemark + '\'' +
                    ", isType='" + isType + '\'' +
                    ", exampleFile=" + exampleFile +
                    ", checkBoxInfos=" + checkBoxInfos +
                    ", radioButtonInfos=" + radioButtonInfos +
                    ", sort='" + sort + '\'' +
                    ", returnfile=" + returnfile +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LastTaskBean{" +
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
