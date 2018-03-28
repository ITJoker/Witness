package com.risenb.witness.beans;

public class ResultBean {
    /**
     * success : 1
     * errorMsg : 提交成功
     */

    private int success;
    private String errorMsg;

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
}
