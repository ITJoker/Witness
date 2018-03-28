package com.risenb.witness.beans;

public class SettledTaskDetailsBean {
    /**
     * success : 1
     * errorMsg : 成功返回
     * data : {"id":"3","fixedname":"一汽大众","price":"1","count":"10","example":"/Public/Customer_picture/2017-05-11/149448984514743.jpg"}
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
         * id : 3
         * fixedname : 一汽大众
         * price : 1
         * count : 10
         * example : /Public/Customer_picture/2017-05-11/149448984514743.jpg
         */

        private String id;
        private String fixedname;
        private String price;
        private String count;
        private String example;
        private String remark;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFixedname() {
            return fixedname;
        }

        public void setFixedname(String fixedname) {
            this.fixedname = fixedname;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getExample() {
            return example;
        }

        public void setExample(String example) {
            this.example = example;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
