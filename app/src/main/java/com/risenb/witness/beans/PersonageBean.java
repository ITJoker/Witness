package com.risenb.witness.beans;

public class PersonageBean {
    /**
     * success : 1
     * errorMsg : 成功返回
     * data : {"nickName":"差不多先生","headPic":"http://www.adexmall.net//Public/upload/app/headPic/2018-03-08/5aa0e179eba3c.png","userTel":"18647468021","balance":"0.00","tel":"010-58611189"}
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
         * nickName : 差不多先生
         * headPic : http://www.adexmall.net//Public/upload/app/headPic/2018-03-08/5aa0e179eba3c.png
         * userTel : 18647468021
         * balance : 0.00
         * tel : 010-58611189
         */

        private String nickName;
        private String headPic;
        private String userTel;
        private String balance;
        private String tel;

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getHeadPic() {
            return headPic;
        }

        public void setHeadPic(String headPic) {
            this.headPic = headPic;
        }

        public String getUserTel() {
            return userTel;
        }

        public void setUserTel(String userTel) {
            this.userTel = userTel;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }
    }
}
