package com.risenb.witness.beans;

public class VipInfoBean {
    private String userName;
    private String nickName;
    private String userTel;
    private String headPic;
    private String Province_name;
    private String City_name;
    private int sex;
    private int is_prove;

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public String getProvince_name() {
        return Province_name;
    }

    public void setProvince_name(String province_name) {
        Province_name = province_name;
    }

    public String getCity_name() {
        return City_name;
    }

    public void setCity_name(String city_name) {
        City_name = city_name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getIs_prove() {
        return is_prove;
    }

    public void setIs_prove(int is_prove) {
        this.is_prove = is_prove;
    }
}
