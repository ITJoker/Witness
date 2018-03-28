package com.risenb.witness.beans;

import java.io.Serializable;

public class VideoInfo extends VideoOrPhotoItemSign implements Serializable{
    private String title;
    private int duration;
    private String taskid;
    private String path;
    private String thumbnailpath;
    private String sort;
    private String page;
    private String isupload;
    private String position;

    public VideoInfo() {

    }

    public VideoInfo(String taskid, String path, String thumbnailpath, String sort, String page, String isupload,String position) {
        this.taskid = taskid;
        this.path = path;
        this.thumbnailpath = thumbnailpath;
        this.sort = sort;
        this.page = page;
        this.isupload = isupload;
        this.position=position;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getThumbnailpath() {
        return thumbnailpath;
    }

    public void setThumbnailpath(String thumbnailpath) {
        this.thumbnailpath = thumbnailpath;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getIsupload() {
        return isupload;
    }

    public void setIsupload(String isupload) {
        this.isupload = isupload;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
