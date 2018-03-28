package com.risenb.witness.beans;

public class AllTaskCount{
    /**
     * unfilledCount : 2
     * activeCount : 1
     * uploadedCount : 0
     * outOfCount : 0
     * GiveUpCount : 0
     */

    private String unfilledCount;
    private String activeCount;
    private String uploadedCount;
    private String outOfCount;
    private String GiveUpCount;
    private String is_read;

    private String reject_is_read;

    public String getUnfilledCount() {
        return unfilledCount;
    }

    public void setUnfilledCount(String unfilledCount) {
        this.unfilledCount = unfilledCount;
    }

    public String getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(String activeCount) {
        this.activeCount = activeCount;
    }

    public String getUploadedCount() {
        return uploadedCount;
    }

    public void setUploadedCount(String uploadedCount) {
        this.uploadedCount = uploadedCount;
    }

    public String getOutOfCount() {
        return outOfCount;
    }

    public void setOutOfCount(String outOfCount) {
        this.outOfCount = outOfCount;
    }

    public String getGiveUpCount() {
        return GiveUpCount;
    }

    public void setGiveUpCount(String giveUpCount) {
        GiveUpCount = giveUpCount;
    }

    public String getIs_read() {
        return is_read;
    }

    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }

    public String getReject_is_read() {
        return reject_is_read;
    }

    public void setReject_is_read(String reject_is_read) {
        this.reject_is_read = reject_is_read;
    }
}
