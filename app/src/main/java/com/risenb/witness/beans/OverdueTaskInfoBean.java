package com.risenb.witness.beans;

import java.util.List;

/**
 * 过期任务信息类
 */
public class OverdueTaskInfoBean {
    /**
     * success : 1
     * errorMsg : 返回成功
     * data : [{"taskid":"47862","image":"http://www.adexmall.net//Public/Customer_picture/NewLogo/1511856950.jpg","address":"宝星国际朝阳区望京东园一区107号楼110号楼11单元1号梯左","city":"北京市北京市朝阳区","note":"无","state":"5"},{"taskid":"47863","image":"http://www.adexmall.net//Public/Customer_picture/NewLogo/1511856950.jpg","address":"宝星国际朝阳区望京东园一区107号楼110号楼12单元2号梯左","city":"北京市北京市朝阳区","note":"无","state":"5"},{"taskid":"65863","image":"http://www.adexmall.net//Public/Customer_picture/NewLogo/1511849471.jpg","address":"中建国际港西区大兴区黄村兴华大街西门进口","city":"北京市北京市大兴区","note":"无","state":"6"},{"taskid":"65867","image":"http://www.adexmall.net//Public/Customer_picture/NewLogo/1511849471.jpg","address":"卡尔生活馆别墅天宝园四里别墅出口","city":"北京市北京市大兴区","note":"无","state":"6"},{"taskid":"65866","image":"http://www.adexmall.net//Public/Customer_picture/NewLogo/1511849471.jpg","address":"北空住宅小区南区大兴旧宫大门进口","city":"北京市北京市大兴区","note":"无","state":"6"}]
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
         * taskid : 47862
         * image : http://www.adexmall.net//Public/Customer_picture/NewLogo/1511856950.jpg
         * address : 宝星国际朝阳区望京东园一区107号楼110号楼11单元1号梯左
         * city : 北京市北京市朝阳区
         * note : 无
         * state : 5
         */

        private String taskid;
        private String image;
        private String address;
        private String city;
        private String note;
        private String state;

        public String getTaskid() {
            return taskid;
        }

        public void setTaskid(String taskid) {
            this.taskid = taskid;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }
}
