package com.risenb.witness.beans;

public class CompletedAnalysis<T> {

    /**
     * success : 1
     * errorMsg : 提交成功
     * data : []
     */

    private int success;
    private String errorMsg;
    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
