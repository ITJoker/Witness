package com.risenb.witness.beans;

import java.util.List;

public class UploadTaskDetail {
    private String address;
    private String price;
    private String validity;
    private String remainTime;
    private String issuestate;
    /**
     * isType : 1
     * taskremark : 拍摄重点侧面拍摄
     * exampleFile : ["url-pic示例"]
     * taskId : 2
     * sort : 1
     */

    private List<TestListBean> testList;
    /**
     * returnfile : url
     * sort : 1
     */

    private List<UserfileBean> userfile;
    /**
     * test : 请选择漏刊范围1111
     * testid : 1
     * type : 1
     * solution : ["答案1"]
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

    public String getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(String remainTime) {
        this.remainTime = remainTime;
    }

    public String getIssuestate() {
        return issuestate;
    }

    public void setIssuestate(String issuestate) {
        this.issuestate = issuestate;
    }

    public List<TestListBean> getTestList() {
        return testList;
    }

    public void setTestList(List<TestListBean> testList) {
        this.testList = testList;
    }

    public List<UserfileBean> getUserfile() {
        return userfile;
    }

    public void setUserfile(List<UserfileBean> userfile) {
        this.userfile = userfile;
    }

    public List<AnswerBean> getAnswer() {
        return answer;
    }

    public void setAnswer(List<AnswerBean> answer) {
        this.answer = answer;
    }

    public static class TestListBean {
        private int isType;
        private String taskremark;
        private int taskId;
        private int sort;
        private List<String> exampleFile;

        public int getIsType() {
            return isType;
        }

        public void setIsType(int isType) {
            this.isType = isType;
        }

        public String getTaskremark() {
            return taskremark;
        }

        public void setTaskremark(String taskremark) {
            this.taskremark = taskremark;
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

        public List<String> getExampleFile() {
            return exampleFile;
        }

        public void setExampleFile(List<String> exampleFile) {
            this.exampleFile = exampleFile;
        }

        @Override
        public String toString() {
            return "TestListBean{" +
                    "isType=" + isType +
                    ", taskremark='" + taskremark + '\'' +
                    ", taskId=" + taskId +
                    ", sort=" + sort +
                    ", exampleFile=" + exampleFile +
                    '}';
        }
    }

    public static class UserfileBean {
        private String returnfile;
        private String sort;

        public String getReturnfile() {
            return returnfile;
        }

        public void setReturnfile(String returnfile) {
            this.returnfile = returnfile;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        @Override
        public String toString() {
            return "UserfileBean{" +
                    "returnfile='" + returnfile + '\'' +
                    ", sort='" + sort + '\'' +
                    '}';
        }
    }

    public static class AnswerBean {
        private String test;
        private int testid;
        private int type;
        private List<String> solution;

        public String getTest() {
            return test;
        }

        public void setTest(String test) {
            this.test = test;
        }

        public int getTestid() {
            return testid;
        }

        public void setTestid(int testid) {
            this.testid = testid;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
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
                    "test='" + test + '\'' +
                    ", testid=" + testid +
                    ", type=" + type +
                    ", solution=" + solution +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UploadTaskDetail{" +
                "address='" + address + '\'' +
                ", price='" + price + '\'' +
                ", validity='" + validity + '\'' +
                ", remainTime='" + remainTime + '\'' +
                ", issuestate='" + issuestate + '\'' +
                ", testList=" + testList +
                ", userfile=" + userfile +
                ", answer=" + answer +
                '}';
    }
}
