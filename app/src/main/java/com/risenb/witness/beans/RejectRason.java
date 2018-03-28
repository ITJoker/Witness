package com.risenb.witness.beans;

public class RejectRason {
    /**
     * rejectInfo : 驳回原因
     */
    private String rejectInfo;

    public String getRejectInfo() {
        return rejectInfo;
    }

    public void setRejectInfo(String rejectInfo) {
        this.rejectInfo = rejectInfo;
    }

    @Override
    public String toString() {
        return "RejectRason{" +
                "rejectInfo='" + rejectInfo + '\'' +
                '}';
    }
}
