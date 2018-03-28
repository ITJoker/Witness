package com.risenb.witness.beans;

import java.util.List;

public class HistoryCrashBean {
    /**
     * success : 1
     * errorMsg : 返回成功
     * data : [{"createTime":"1487262948","price":"199.00","type":"2"},{"createTime":"1487262957","price":"796.00","type":"2"},{"createTime":"1487262953","price":"398.00","type":"2"},{"createTime":"1487262937","price":"100.00","type":"2"},{"createTime":"1487262167","price":"99.00","type":"3"}]
     */

    private int success;
    private String errorMsg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * createTime : 1487262948
         * price : 199.00
         * type : 2
         */

        private String createTime;
        private String price;
        private String type;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

}
