package com.risenb.witness.beans;

public class VipSetAboutBean {
    /**
     * success : 0
     * errorMsg : 已是最新版本
     * data : {"androidVersion":"1.8.1","company":"上海广漾网络科技有限公司","qqCrowd":"308920964","servicePhone":"021-61480617","QRCodeImageUrl":"http://www.adexmall.net//Public/APP/appxiazai.png","serviceTime":"9:00-12:00  13:00-18:00"}
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
         * androidVersion : 1.8.1
         * company : 上海广漾网络科技有限公司
         * qqCrowd : 308920964
         * servicePhone : 021-61480617
         * QRCodeImageUrl : http://www.adexmall.net//Public/APP/appxiazai.png
         * serviceTime : 9:00-12:00  13:00-18:00
         */

        private String androidVersion;
        private String company;
        private String qqCrowd;
        private String servicePhone;
        private String QRCodeImageUrl;
        private String serviceTime;

        public String getAndroidVersion() {
            return androidVersion;
        }

        public void setAndroidVersion(String androidVersion) {
            this.androidVersion = androidVersion;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getQqCrowd() {
            return qqCrowd;
        }

        public void setQqCrowd(String qqCrowd) {
            this.qqCrowd = qqCrowd;
        }

        public String getServicePhone() {
            return servicePhone;
        }

        public void setServicePhone(String servicePhone) {
            this.servicePhone = servicePhone;
        }

        public String getQRCodeImageUrl() {
            return QRCodeImageUrl;
        }

        public void setQRCodeImageUrl(String QRCodeImageUrl) {
            this.QRCodeImageUrl = QRCodeImageUrl;
        }

        public String getServiceTime() {
            return serviceTime;
        }

        public void setServiceTime(String serviceTime) {
            this.serviceTime = serviceTime;
        }
    }
}
