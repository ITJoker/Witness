package com.risenb.witness.beans;

import java.io.Serializable;
import java.util.List;

public class ExecTaskInfo {

    /**
     * address : 北京市北京市东城区国贸建外SOHO任务id为1
     * price : 50
     * validity : 1233787
     * modeltype : 1
     * remainTime : 268908
     * issuestate :
     * taskList : [{"isType":"1","taskRemark":"图片必须大小一致","exampleFile":["http://yingwei.4pole.cn/Public/Customer_picture/2016-10-26/5810270e6c8cb.jpg"],"taskId":"1","sort":"1","returnfile":["http://yingwei.4pole.cn/Public/Customer_space/fe1585234908c3b7261fd920f1454717/app/image/2016-11-18/147945215458062.jpg"]},{"isType":"0","taskRemark":"拍清楚","exampleFile":["http://yingwei.4pole.cn/Public/Customer_picture/2016-10-26/5810270e6c8cb.jpg"],"taskId":"1","sort":"2","returnfile":["http://yingwei.4pole.cn/Public/upload/app/2016-11-18/6d266a35cde250d667d40c6dd4a2bea2.jpg"]},{"isType":"1","taskRemark":"图片对齐","exampleFile":["http://yingwei.4pole.cn/Public/Customer_picture/2016-10-26/5810270e6c8cb.jpg"],"taskId":"1","sort":"3","returnfile":["http://yingwei.4pole.cn/Public/Customer_space/fe1585234908c3b7261fd920f1454717/app/image/2016-11-18/147945215516281.jpg"]},{"isType":"2","taskRemark":null,"exampleFile":["良好","漏刊","完整","不咋地"],"taskId":"1","sort":"4","returnfile":[""]},{"isType":"1","taskRemark":"图片对齐","exampleFile":["http://yingwei.4pole.cn/Public/Customer_picture/2016-10-26/5810270e6c8cb.jpg"],"taskId":"1","sort":"5","returnfile":["http://yingwei.4pole.cn/Public/Customer_space/fe1585234908c3b7261fd920f1454717/app/image/2016-11-18/147945219166239.jpg"]}]
     * answer : [{"solution":["2"],"testid":"1","type":"1","test":"你多大了？"},{"solution":["我是我"],"testid":"2","type":"3","test":"你是谁？"},{"solution":["16岁"],"testid":"4","type":"2","test":"你毕业几年了？"}]
     */

    private String address;
    private String price;
    private String validity;
    private String modeltype;
    private int remainTime;
    private String issuestate;
    /**
     * isType : 1
     * taskRemark : 图片必须大小一致
     * exampleFile : ["http://yingwei.4pole.cn/Public/Customer_picture/2016-10-26/5810270e6c8cb.jpg"]
     * taskId : 1
     * sort : 1
     * returnfile : ["http://yingwei.4pole.cn/Public/Customer_space/fe1585234908c3b7261fd920f1454717/app/image/2016-11-18/147945215458062.jpg"]
     */

    private List<TaskListBean> taskList;
    /**
     * solution : ["2"]
     * testid : 1
     * type : 1
     * test : 你多大了？
     */

    private List<AnswerBean> answer;

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

    public int getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(int remainTime) {
        this.remainTime = remainTime;
    }

    public String getIssuestate() {
        return issuestate;
    }

    public void setIssuestate(String issuestate) {
        this.issuestate = issuestate;
    }

    public List<TaskListBean> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<TaskListBean> taskList) {
        this.taskList = taskList;
    }

    public List<AnswerBean> getAnswer() {
        return answer;
    }

    public void setAnswer(List<AnswerBean> answer) {
        this.answer = answer;
    }

    public static class TaskListBean implements Serializable{
        private String isType;
        private String taskRemark;
        private String taskId;
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

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
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

        @Override
        public String toString() {
            return "TaskListBean{" +
                    "isType='" + isType + '\'' +
                    ", taskRemark='" + taskRemark + '\'' +
                    ", taskId='" + taskId + '\'' +
                    ", sort='" + sort + '\'' +
                    ", exampleFile=" + exampleFile +
                    ", returnfile=" + returnfile +
                    '}';
        }
    }

    public static class AnswerBean {
        private String testid;
        private String type;
        private String test;
        private List<String> solution;

        public String getTestid() {
            return testid;
        }

        public void setTestid(String testid) {
            this.testid = testid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTest() {
            return test;
        }

        public void setTest(String test) {
            this.test = test;
        }

        public List<String> getSolution() {
            return solution;
        }

        public void setSolution(List<String> solution) {
            this.solution = solution;
        }

        @Override
        public String toString() {
            return "AnswerBean{" +
                    "testid='" + testid + '\'' +
                    ", type='" + type + '\'' +
                    ", test='" + test + '\'' +
                    ", solution=" + solution +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ExecTaskInfo{" +
                "address='" + address + '\'' +
                ", price='" + price + '\'' +
                ", validity='" + validity + '\'' +
                ", modeltype='" + modeltype + '\'' +
                ", remainTime=" + remainTime +
                ", issuestate='" + issuestate + '\'' +
                ", taskList=" + taskList +
                ", answer=" + answer +
                '}';
    }
}
