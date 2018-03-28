package com.risenb.witness.beans;

public class MessageState {
    /**
     * success : 1
     * errorMsg : 返回成功
     * data : 1
     */

    private int success;
    private String errorMsg;
    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
