package com.risenb.witness.beans;

import java.io.Serializable;

public class UploadVidoeTask  implements Serializable{
    private  String sort;
    private String vidoepath;

    public UploadVidoeTask(){

    }

    public UploadVidoeTask(String sort, String vidoepath) {
        this.sort = sort;
        this.vidoepath = vidoepath;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getVidoepath() {
        return vidoepath;
    }

    public void setVidoepath(String vidoepath) {
        this.vidoepath = vidoepath;
    }

    @Override
    public String toString() {
        return "UploadVidoeTask{" +
                "sort='" + sort + '\'' +
                ", vidoepath='" + vidoepath + '\'' +
                '}';
    }
}
