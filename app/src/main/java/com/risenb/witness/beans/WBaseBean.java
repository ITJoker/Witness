package com.risenb.witness.beans;

public class WBaseBean {
    private String success ; //: 1:true ;0:false; 2:备用(提示)，
    private String errorMsg; //:当有错误提示时填写,
    private String data; //:

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
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
