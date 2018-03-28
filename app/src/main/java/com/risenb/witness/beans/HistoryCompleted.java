package com.risenb.witness.beans;

import java.util.List;

public class HistoryCompleted {
    /**
     * address : 53174eac5e0253174eac5e02671d9633533a56fd8d385efa5916android4  SOHO14efb52a1id 4
     * price : 80.00
     * validity : 259200
     * modeltype : 1
     * taskid : 4
     * remainTime : 11085
     * taskList : [{"isType":"2","taskRemark":null,"exampleFile":["826f597d","6f0f520a","5b8c6574","4e0d548b5730"],"sort":"1","returnfile":["0"]},{"isType":"1","taskRemark":"56fe72475fc5987b59275c0f4e0081f4","exampleFile":["http://yingwei.4pole.cn/Public/Customer_picture/2016-10-26/5810270e6c8cb.jpg"],"sort":"5","returnfile":[]},{"isType":"1","taskRemark":"56fe72475bf99f50","exampleFile":["http://yingwei.4pole.cn/Public/Customer_picture/2016-10-26/5810270e6c8cb.jpg"],"sort":"6","returnfile":[]},{"testId":"1","taskRemark":"4f60591a59274e86ff1f","isType":"3","exampleFile":["155c81","165c81","175c81"],"returnfile":[]},{"testId":"2","taskRemark":"4f60662f8c01ff1f","isType":"5","exampleFile":[],"returnfile":[]},{"testId":"4","taskRemark":"4f606bd54e1a51e05e744e86ff1f","isType":"4","exampleFile":["55e74","65e74","75e74"],"returnfile":[]}]
     */

    private String title;
    private String address;
    private String execution;
    private String price;
    private String ScheduleRemark;
    private String issuestate;
    private String validity;
    private String modeltype;
    private String taskid;
    private int remainTime;
    private String remark;
    private List<TaskListBean> taskList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getExecution() {
        return execution;
    }

    public void setExecution(String execution) {
        this.execution = execution;
    }

    public String getScheduleRemark() {
        return ScheduleRemark;
    }

    public void setScheduleRemark(String scheduleRemark) {
        ScheduleRemark = scheduleRemark;
    }

    public String getIssuestate() {
        return issuestate;
    }

    public void setIssuestate(String issuestate) {
        this.issuestate = issuestate;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<TaskListBean> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<TaskListBean> taskList) {
        this.taskList = taskList;
    }

    public static class TaskListBean {
        /**
         * isType : 2
         * taskRemark : null
         * exampleFile : ["826f597d","6f0f520a","5b8c6574","4e0d548b5730"]
         * sort : 1
         * returnfile : ["0"]
         */
        private String isType;
        private String taskRemark;
        private String sort;
        private List<String> exampleFile;
        private List<String> returnfile;

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
    }
}
