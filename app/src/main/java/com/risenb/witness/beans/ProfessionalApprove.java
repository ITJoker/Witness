package com.risenb.witness.beans;

public class ProfessionalApprove {
    /**
     * success : 1
     * errorMsg : 已认证
     * data : {"username":"河北省管理员","appcode":"236550"}
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
         * username : 河北省管理员
         * appcode : 236550
         */

        private String username;
        private String appcode;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAppcode() {
            return appcode;
        }

        public void setAppcode(String appcode) {
            this.appcode = appcode;
        }
    }
}
