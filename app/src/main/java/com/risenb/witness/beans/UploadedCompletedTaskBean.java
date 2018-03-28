package com.risenb.witness.beans;

import java.util.List;

public class UploadedCompletedTaskBean {
    /**
     * success : 1
     * errorMsg : 返回成功
     * data : {"address":"北京市北京市丰台区保利欣苑万望园路9号1号楼1单元侯梯厅1","execution":"2","price":"0.00","returnfile":["/Public/Customer_space/a68f8c27b375b0a7d4cc1d2c637b6478/app/image/2017-12-04/151235273468968.jpg","/Public/Customer_space/a68f8c27b375b0a7d4cc1d2c637b6478/app/image/2017-12-04/151235273488258.jpg"],"ScheduleRemark":"经典款短裤","issuestate":"夜间灯不亮"}
     */

    private int success;
    private String errorMsg;
    private DataBean data;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * address : 北京市北京市丰台区保利欣苑万望园路9号1号楼1单元侯梯厅1
         * execution : 2
         * price : 0.00
         * returnfile : ["/Public/Customer_space/a68f8c27b375b0a7d4cc1d2c637b6478/app/image/2017-12-04/151235273468968.jpg","/Public/Customer_space/a68f8c27b375b0a7d4cc1d2c637b6478/app/image/2017-12-04/151235273488258.jpg"]
         * ScheduleRemark : 经典款短裤
         * issuestate : 夜间灯不亮
         */

        private String address;
        private String execution;
        private String price;
        private String ScheduleRemark;
        private String issuestate;
        private List<String> returnfile;

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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getScheduleRemark() {
            return ScheduleRemark;
        }

        public void setScheduleRemark(String ScheduleRemark) {
            this.ScheduleRemark = ScheduleRemark;
        }

        public String getIssuestate() {
            return issuestate;
        }

        public void setIssuestate(String issuestate) {
            this.issuestate = issuestate;
        }

        public List<String> getReturnfile() {
            return returnfile;
        }

        public void setReturnfile(List<String> returnfile) {
            this.returnfile = returnfile;
        }
    }
}
