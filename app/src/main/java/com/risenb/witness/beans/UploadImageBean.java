package com.risenb.witness.beans;

public class UploadImageBean {
    /**
     * success : 1
     * errorMsg : 上传成功
     * data : {"file_url":"/Public/Customer_space/808268dd74c46927c25637dd53a1659f/app/image/2017-03-07/148886877320779.jpg","sort":"1"}
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
         * file_url : /Public/Customer_space/808268dd74c46927c25637dd53a1659f/app/image/2017-03-07/148886877320779.jpg
         * sort : 1
         */

        private String file_url;
        private String sort;

        public String getFile_url() {
            return file_url;
        }

        public void setFile_url(String file_url) {
            this.file_url = file_url;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }
    }

}

