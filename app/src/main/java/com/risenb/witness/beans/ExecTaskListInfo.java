package com.risenb.witness.beans;

import com.risenb.witness.ui.tasklist.CheckBoxInfo;

import java.util.List;

public class ExecTaskListInfo {
    /**
     * address : 北京市北京市朝阳区国贸建外SOHO111任务id 4
     * price : 70
     * validity : 1233787
     * modeltype : 1
     * taskid : 4
     * remainTime : -857965
     * taskList : [{"isType":"1","taskRemark":"拍清楚","exampleFile":["http://yingwei.4pole.cn/Public/Customer_picture/2016-10-26/5810270e6c8cb.jpg"],"sort":"1","returnfile":["http://yingwei.4pole.cn/Public/Customer_space//app/image/2016-12-01/148057862720349.jpg/Public/Customer_space//app/image/2016-12-01/148057862720349.jpg"]},{"isType":"0","taskRemark":"拍清楚拍清楚","exampleFile":["http://yingwei.4pole.cn/Public/Customer_picture/2016-10-26/5810270e6c8cb.jpg"],"sort":"2","returnfile":["http://yingwei.4pole.cn/Public/upload/app/2016-12-01/8e662908f5075472c63cc08f3c430a83.jpg"]},{"isType":"1","taskRemark":"拍清楚","exampleFile":["http://yingwei.4pole.cn/Public/Customer_picture/2016-10-26/5810270e6c8cb.jpg"],"sort":"3","returnfile":["http://yingwei.4pole.cn/Public/Customer_space//app/image/2016-12-01/148057862896113.jpg/Public/Customer_space//app/image/2016-12-01/148057862896113.jpg"]}]
     */

    private String address;
    private String price;
    private String validity;
    private String modeltype;
    private String taskid;
    private int remainTime;
    /**
     * isType : 1
     * taskRemark : 拍清楚
     * exampleFile : ["http://yingwei.4pole.cn/Public/Customer_picture/2016-10-26/5810270e6c8cb.jpg"]
     * sort : 1
     * returnfile : ["http://yingwei.4pole.cn/Public/Customer_space//app/image/2016-12-01/148057862720349.jpg/Public/Customer_space//app/image/2016-12-01/148057862720349.jpg"]
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

    public String getModeltype() {
        return modeltype;
    }

    public void setModeltype(String modeltype) {
        this.modeltype = modeltype;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public int getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(int remainTime) {
        this.remainTime = remainTime;
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
        private String sort;
        private List<String> exampleFile;
        private List<String> returnfile;
        private List<CheckBoxInfo> checkBoxInfos;
        private List<RadioButtonInfo> radioButtonInfos;

        private String testId;

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

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public List<String> getExampleFile() {
            return exampleFile;
        }

        public void setExampleFile(List<String> exampleFile) {
            this.exampleFile = exampleFile;
        }

        public List<String> getReturnfile() {
            return returnfile;
        }

        public void setReturnfile(List<String> returnfile) {
            this.returnfile = returnfile;
        }

        public List<CheckBoxInfo> getCheckBoxInfos() {
            return checkBoxInfos;
        }

        public void setCheckBoxInfos(List<CheckBoxInfo> checkBoxInfos) {
            this.checkBoxInfos = checkBoxInfos;
        }

        public String getTestId() {
            return testId;
        }

        public void setTestId(String testId) {
            this.testId = testId;
        }

        public List<RadioButtonInfo> getRadioButtonInfos() {
            return radioButtonInfos;
        }

        public void setRadioButtonInfos(List<RadioButtonInfo> radioButtonInfos) {
            this.radioButtonInfos = radioButtonInfos;
        }
    }
}
