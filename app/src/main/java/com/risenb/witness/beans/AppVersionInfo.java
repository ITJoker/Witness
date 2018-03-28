package com.risenb.witness.beans;

public class AppVersionInfo {
    /**
     * success : 1
     * errorMsg : 成功
     * data : {"versionName":"1.2","versionCode":4,"apkDownloadUrl":"http://www.adexmall.net/Public/AdexMall.apk","apkDescriptionInfo":"批量上传"}
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
         * versionName : 1.2
         * versionCode : 4
         * apkDownloadUrl : http://www.adexmall.net/Public/AdexMall.apk
         * apkDescriptionInfo : 批量上传
         */

        private String versionName;
        private int versionCode;
        private String apkDownloadUrl;
        private String apkDescriptionInfo;

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getApkDownloadUrl() {
            return apkDownloadUrl;
        }

        public void setApkDownloadUrl(String apkDownloadUrl) {
            this.apkDownloadUrl = apkDownloadUrl;
        }

        public String getApkDescriptionInfo() {
            return apkDescriptionInfo;
        }

        public void setApkDescriptionInfo(String apkDescriptionInfo) {
            this.apkDescriptionInfo = apkDescriptionInfo;
        }
    }

}
