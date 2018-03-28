package com.risenb.witness.utils.newUtils;

import java.util.List;

public class Program {

    /**
     * success : 1
     * errorMsg : 返回成功
     * data : [{"depict":"人人目击","limit":true,"name":"com.risenb.witness","time":"2016-09-26"},{"depict":"城市兔","limit":true,"name":"com.adexmall.cityrabbit","time":"2017-04-12"}]
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
         * depict : 人人目击
         * limit : true
         * name : com.risenb.witness
         * time : 2016-09-26
         */

        private String depict;
        private boolean limit;
        private String name;
        private String time;

        public String getDepict() {
            return depict;
        }

        public void setDepict(String depict) {
            this.depict = depict;
        }

        public boolean isLimit() {
            return limit;
        }

        public void setLimit(boolean limit) {
            this.limit = limit;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
