package com.risenb.witness.beans;

import java.util.List;

public class MultimediaInfo {
    private List<String> uploadImagePath;
    private String videoPath;

    public MultimediaInfo(){

    }
    public MultimediaInfo(List<String> uploadImagePath, String videoPath) {
        this.uploadImagePath = uploadImagePath;
        this.videoPath = videoPath;
    }

    public List<String> getUploadImagePath() {
        return uploadImagePath;
    }

    public void setUploadImagePath(List<String> uploadImagePath) {
        this.uploadImagePath = uploadImagePath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }
}
