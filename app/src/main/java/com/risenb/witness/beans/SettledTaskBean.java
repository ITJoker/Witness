package com.risenb.witness.beans;

import java.util.List;

public class SettledTaskBean {
    /**
     * success : 1
     * errorMsg : 成功返回
     * data : [{"fixedid":"5","image":"http://www.adexmall.net//Public/Customer_picture/2017-03-06/58bcfb73a4a1f.jpg","fixedname":"张先生","province_name":"河北省","city_name":"衡水市","price":"100","count":"50","validity":"0","remainTime":0},{"fixedid":"4","image":"http://www.adexmall.net//Public/Customer_picture/2017-03-06/58bcfb73a4a1f.jpg","fixedname":"一汽大众","province_name":"河北省","city_name":"石家庄市","price":"10","count":"10","validity":"0","remainTime":0},{"fixedid":"3","image":"http://www.adexmall.net//Public/Customer_picture/2017-03-06/58bcfb73a4a1f.jpg","fixedname":"一汽大众","province_name":"北京市","city_name":"北京市","price":"1","count":"10","validity":"0","remainTime":0}]
     */

    private int success;
    private String errorMsg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * fixedid : 5
         * image : http://www.adexmall.net//Public/Customer_picture/2017-03-06/58bcfb73a4a1f.jpg
         * fixedname : 张先生
         * province_name : 河北省
         * city_name : 衡水市
         * price : 100
         * count : 50
         * validity : 0
         * remainTime : 0
         * isdemo : true
         */

        private String fixedid;
        private String image;
        private String fixedname;
        private String province_name;
        private String city_name;
        private String price;
        private String count;
        private String validity;
        private int remainTime;
        private String isdemo;

        public String getFixedid() {
            return fixedid;
        }

        public void setFixedid(String fixedid) {
            this.fixedid = fixedid;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getFixedname() {
            return fixedname;
        }

        public void setFixedname(String fixedname) {
            this.fixedname = fixedname;
        }

        public String getProvince_name() {
            return province_name;
        }

        public void setProvince_name(String province_name) {
            this.province_name = province_name;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getValidity() {
            return validity;
        }

        public void setValidity(String validity) {
            this.validity = validity;
        }

        public int getRemainTime() {
            return remainTime;
        }

        public void setRemainTime(int remainTime) {
            this.remainTime = remainTime;
        }

        public String getIsdemo() {
            return isdemo;
        }

        public void setIsdemo(String isdemo) {
            this.isdemo = isdemo;
        }
    }
}
