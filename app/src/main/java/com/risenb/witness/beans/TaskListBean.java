package com.risenb.witness.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.ArrayList;
import java.util.List;

public class TaskListBean {
    /**
     * success : 1
     * errorMsg : 返回成功
     * data : [{"address":"北京市北京市朝阳区工体北路工人体育场","distance":7.18,"price":"0.00","validity":"0","remainTime":0,"taskId":"3453","image":"http://www.adexmall.net//Public/Customer_picture/2017-02-22/58ad3cfa4c668.png","longitude":"116.4538452592425","latitude":"39.93670502614944","execution":"1","require":[{"text":"百条任务测试"}],"watermark_taskid":"0","watermark_meshtime":"0","watermark_systemtime":"0","watermark_xydata":"0"},{"address":"北京市北京市朝阳区工体北路工人体育场","distance":7.18,"price":"0.00","validity":"0","remainTime":0,"taskId":"3452","image":"http://www.adexmall.net//Public/Customer_picture/2017-02-22/58ad3cfa4c668.png","longitude":"116.4538452592425","latitude":"39.93670502614944","execution":"1","require":[{"text":"百条任务测试"}],"watermark_taskid":"0","watermark_meshtime":"0","watermark_systemtime":"0","watermark_xydata":"0"},{"address":"北京市北京市朝阳区工体北路工人体育场","distance":7.18,"price":"0.00","validity":"0","remainTime":0,"taskId":"3454","image":"http://www.adexmall.net//Public/Customer_picture/2017-02-22/58ad3cfa4c668.png","longitude":"116.4538452592425","latitude":"39.93670502614944","execution":"1","require":[{"text":"百条任务测试"}],"watermark_taskid":"0","watermark_meshtime":"0","watermark_systemtime":"0","watermark_xydata":"0"},{"address":"北京市北京市东城区工体北路东四十条桥东","distance":7.55,"price":"0.00","validity":"0","remainTime":0,"taskId":"3451","image":"http://www.adexmall.net//Public/Customer_picture/2017-02-22/58ad3cfa4c668.png","longitude":"116.4620377713579","latitude":"39.93952476538706","execution":"1","require":[{"text":"百条任务测试"}],"watermark_taskid":"0","watermark_meshtime":"0","watermark_systemtime":"0","watermark_xydata":"0"},{"address":"北京市北京市朝阳区新东路东大桥路口北","distance":8.93,"price":"0.00","validity":"0","remainTime":0,"taskId":"3455","image":"http://www.adexmall.net//Public/Customer_picture/2017-02-22/58ad3cfa4c668.png","longitude":"116.4569626093284","latitude":"39.95215097536553","execution":"1","require":[{"text":"百条任务测试"}],"watermark_taskid":"0","watermark_meshtime":"0","watermark_systemtime":"0","watermark_xydata":"0"},{"address":"北京市北京市朝阳区朝阳北路管庄路口往东200米龙湖长楹天街西区","distance":9.09,"price":"0.00","validity":"0","remainTime":0,"taskId":"3416","image":"http://www.adexmall.net//Public/Customer_picture/2017-02-22/58ad3cfa4c668.png","longitude":"116.5216948910808","latitude":"39.95895316640668","execution":"1","require":[{"text":"百条任务测试"}],"watermark_taskid":"0","watermark_meshtime":"0","watermark_systemtime":"0","watermark_xydata":"0"},{"address":"北京市北京市朝阳区朝阳北路管庄路口往东200米龙湖长楹天街西区","distance":9.09,"price":"0.00","validity":"0","remainTime":0,"taskId":"3464","image":"http://www.adexmall.net//Public/Customer_picture/2017-02-22/58ad3cfa4c668.png","longitude":"116.5216948910808","latitude":"39.95895316640668","execution":"1","require":[{"text":"百条任务测试"}],"watermark_taskid":"0","watermark_meshtime":"0","watermark_systemtime":"0","watermark_xydata":"0"},{"address":"北京市北京市朝阳区亮马桥路燕莎桥东","distance":10.11,"price":"0.00","validity":"0","remainTime":0,"taskId":"3424","image":"http://www.adexmall.net//Public/Customer_picture/2017-02-22/58ad3cfa4c668.png","longitude":"116.4926519126464","latitude":"39.96348862158496","execution":"1","require":[{"text":"百条任务测试"}],"watermark_taskid":"0","watermark_meshtime":"0","watermark_systemtime":"0","watermark_xydata":"0"},{"address":"北京市北京市朝阳区亮马桥路燕莎桥东","distance":10.11,"price":"0.00","validity":"0","remainTime":0,"taskId":"3456","image":"http://www.adexmall.net//Public/Customer_picture/2017-02-22/58ad3cfa4c668.png","longitude":"116.4926519126464","latitude":"39.96348862158496","execution":"1","require":[{"text":"百条任务测试"}],"watermark_taskid":"0","watermark_meshtime":"0","watermark_systemtime":"0","watermark_xydata":"0"},{"address":"北京市北京市朝阳区安定路安贞里","distance":11.2,"price":"0.00","validity":"0","remainTime":0,"taskId":"3427","image":"http://www.adexmall.net//Public/Customer_picture/2017-02-22/58ad3cfa4c668.png","longitude":"116.4102144752839","latitude":"39.97791301525762","execution":"1","require":[{"text":"百条任务测试"}],"watermark_taskid":"0","watermark_meshtime":"0","watermark_systemtime":"0","watermark_xydata":"0"}]
     */

    private int success;
    private String errorMsg;
    private List<TaskList> data;

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

    public List<TaskList> getData() {
        return data;
    }

    public void setData(List<TaskList> data) {
        this.data = data;
    }

    public static class TaskList implements SearchSuggestion {
        /**
         * address : 北京市北京市朝阳区工体北路工人体育场
         * distance : 7.18
         * price : 0.00
         * validity : 0
         * remainTime : 0
         * taskId : 3453
         * image : http://www.adexmall.net//Public/Customer_picture/2017-02-22/58ad3cfa4c668.png
         * longitude : 116.4538452592425
         * latitude : 39.93670502614944
         * execution : 1
         * require : [{"百条任务测试"}]
         * watermark_taskid : 0
         * watermark_meshtime : 0
         * watermark_systemtime : 0
         * watermark_xydata : 0
         * city : 广东省深圳市宝安区
         * note : 无
         * endtime : 2018-01-01
         */

        private String title;
        private String address;
        private String distance;
        private String price;
        private String validity;
        private int remainTime;
        private String taskId;
        private String image;
        private String longitude;
        private String latitude;
        private String state;
        private String uploadtime;
        private String fixedTaskState;
        private String execution;
        private String watermark_taskid;
        private String watermark_meshtime;
        private String watermark_systemtime;
        private String watermark_xydata;
        private ArrayList<String> require;
        private String shootTpye;
        private String city;
        private String note;
        private String endtime;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
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

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getUploadtime() {
            return uploadtime;
        }

        public void setUploadtime(String uploadtime) {
            this.uploadtime = uploadtime;
        }

        public String getFixedTaskState() {
            return fixedTaskState;
        }

        public void setFixedTaskState(String fixedTaskState) {
            this.fixedTaskState = fixedTaskState;
        }

        public String getExecution() {
            return execution;
        }

        public void setExecution(String execution) {
            this.execution = execution;
        }

        public String getWatermark_taskid() {
            return watermark_taskid;
        }

        public void setWatermark_taskid(String watermark_taskid) {
            this.watermark_taskid = watermark_taskid;
        }

        public String getWatermark_meshtime() {
            return watermark_meshtime;
        }

        public void setWatermark_meshtime(String watermark_meshtime) {
            this.watermark_meshtime = watermark_meshtime;
        }

        public String getWatermark_systemtime() {
            return watermark_systemtime;
        }

        public void setWatermark_systemtime(String watermark_systemtime) {
            this.watermark_systemtime = watermark_systemtime;
        }

        public String getWatermark_xydata() {
            return watermark_xydata;
        }

        public void setWatermark_xydata(String watermark_xydata) {
            this.watermark_xydata = watermark_xydata;
        }

        public ArrayList<String> getRequire() {
            return require;
        }

        public void setRequire(ArrayList<String> require) {
            this.require = require;
        }

        public String getShootTpye() {
            return shootTpye;
        }

        public void setShootTpye(String shootTpye) {
            this.shootTpye = shootTpye;
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

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        @Override
        public String getBody() {
            return address;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(title);
            dest.writeString(address);
            dest.writeString(distance);
            dest.writeString(price);
            dest.writeString(validity);
            dest.writeString(taskId);
            dest.writeString(image);
            dest.writeString(longitude);
            dest.writeString(latitude);
            dest.writeInt(remainTime);
            dest.writeString(state);
            dest.writeString(uploadtime);
            dest.writeString(fixedTaskState);
            dest.writeString(execution);
            dest.writeString(watermark_taskid);
            dest.writeString(watermark_meshtime);
            dest.writeString(watermark_systemtime);
            dest.writeString(watermark_xydata);
            dest.writeStringList(require);
            dest.writeString(shootTpye);
            dest.writeString(city);
            dest.writeString(note);
            dest.writeString(endtime);
        }

        public static final Parcelable.Creator<TaskList> CREATOR = new Parcelable.Creator<TaskList>() {
            @Override
            public TaskList createFromParcel(Parcel source) {
                TaskList taskList = new TaskList();
                taskList.title = source.readString();
                taskList.address = source.readString();
                taskList.distance = source.readString();
                taskList.price = source.readString();
                taskList.validity = source.readString();
                taskList.taskId = source.readString();
                taskList.image = source.readString();
                taskList.longitude = source.readString();
                taskList.latitude = source.readString();
                taskList.remainTime = source.readInt();
                taskList.state = source.readString();
                taskList.uploadtime = source.readString();
                taskList.fixedTaskState = source.readString();
                taskList.execution = source.readString();
                taskList.watermark_taskid = source.readString();
                taskList.watermark_meshtime = source.readString();
                taskList.watermark_systemtime = source.readString();
                taskList.watermark_xydata = source.readString();
                taskList.require = source.createStringArrayList();
                taskList.shootTpye = source.readString();
                taskList.city = source.readString();
                taskList.note = source.readString();
                taskList.endtime = source.readString();
                return taskList;
            }

            @Override
            public TaskList[] newArray(int size) {
                return new TaskList[size];
            }
        };
    }
}
