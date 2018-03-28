package com.risenb.witness.beans;

public class SettledTaskPictureReturnParams {
    /**
     * success : 1
     * errorMsg : 上传成功
     * data : {"file_url":"/Public/Customer_space/a6dc185dd384120b9508d6cbeb88b649/app/image/2017-05-12/149457499110468.jpg"}
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
         * file_url : /Public/Customer_space/a6dc185dd384120b9508d6cbeb88b649/app/image/2017-05-12/149457499110468.jpg
         */

        private String file_url;

        public String getFile_url() {
            return file_url;
        }

        public void setFile_url(String file_url) {
            this.file_url = file_url;
        }
    }
}
