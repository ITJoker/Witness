package com.risenb.witness.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class BatchUploadTaskBean {
    /**
     * success : 1
     * errorMsg : 成功返回
     * data : [{"address":"北京市北京市朝阳区北京市朝阳区金台路17号","distance":5.67,"price":"5.00","validity":"172800","remainTime":-5262,"taskId":"4008","image":"http://www.adexmall.net//Public/Customer_picture/2017-03-05/58bb78b406772.png","longitude":"116.4770763667819","latitude":"39.92280998570679","modeltype":"3","is_type":"1"},{"address":"北京市北京市朝阳区北京市朝阳区兆丰街","distance":5.83,"price":"5.00","validity":"172800","remainTime":-5262,"taskId":"4009","image":"http://www.adexmall.net//Public/Customer_picture/2017-03-05/58bb78b406772.png","longitude":"116.4667442265858","latitude":"39.92402538397164","modeltype":"3","is_type":"1"}]
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

    public static class DataBean implements Parcelable {
        /**
         * address : 北京市北京市朝阳区北京市朝阳区金台路17号
         * distance : 5.67
         * price : 5.00
         * validity : 172800
         * remainTime : -5262
         * taskId : 4008
         * image : http://www.adexmall.net//Public/Customer_picture/2017-03-05/58bb78b406772.png
         * longitude : 116.4770763667819
         * latitude : 39.92280998570679
         * modeltype : 3
         * is_type : 1
         */

        private String address;
        private double distance;
        private String price;
        private String validity;
        private int remainTime;
        private String taskId;
        private String image;
        private String longitude;
        private String latitude;
        private String modeltype;
        private String is_type;
        private boolean isCheckBox;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
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

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getModeltype() {
            return modeltype;
        }

        public void setModeltype(String modeltype) {
            this.modeltype = modeltype;
        }

        public String getIs_type() {
            return is_type;
        }

        public void setIs_type(String is_type) {
            this.is_type = is_type;
        }

        public boolean isCheckBox() {
            return isCheckBox;
        }

        public void setCheckBox(boolean checkBox) {
            isCheckBox = checkBox;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(address);
            dest.writeDouble(distance);
            dest.writeString(price);
            dest.writeString(validity);
            dest.writeInt(remainTime);
            dest.writeString(taskId);
            dest.writeString(image);
            dest.writeString(longitude);
            dest.writeString(latitude);
            dest.writeString(modeltype);
            dest.writeString(is_type);
            dest.writeBooleanArray(new boolean[]{isCheckBox});
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            /**
             * 供外部类反序列化本类数组使用
             */
            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }

            /**
             * 从Parcel中读取数据
             */
            @Override
            public DataBean createFromParcel(Parcel source) {
                DataBean dataBean = new DataBean();
                dataBean.address = source.readString();
                dataBean.distance = source.readDouble();
                dataBean.price = source.readString();
                dataBean.validity = source.readString();
                dataBean.remainTime = source.readInt();
                dataBean.taskId = source.readString();
                dataBean.image = source.readString();
                dataBean.longitude = source.readString();
                dataBean.latitude = source.readString();
                dataBean.modeltype = source.readString();
                dataBean.is_type = source.readString();
                boolean[] checkBox = new boolean[1];
                source.readBooleanArray(checkBox);
                dataBean.isCheckBox = checkBox[0];
                return dataBean;
            }
        };
    }
}
