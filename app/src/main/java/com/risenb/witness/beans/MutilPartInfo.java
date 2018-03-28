package com.risenb.witness.beans;

import java.util.Map;

public class MutilPartInfo {
    private int position;
    private String url;
    private String videourl;
    private String Thumbnail;
    private Map<String, String> hashmap;
    private String imagesort;
    private String videosort;
    private boolean imagemodify;
    private boolean videomodify;
    private String type;

    public MutilPartInfo() {

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        Thumbnail = thumbnail;
    }

    public Map<String, String> getHashmap() {
        return hashmap;
    }

    public void setHashmap(Map<String, String> hashmap) {
        this.hashmap = hashmap;
    }

    public String getImagesort() {
        return imagesort;
    }

    public void setImagesort(String imagesort) {
        this.imagesort = imagesort;
    }

    public String getVideosort() {
        return videosort;
    }

    public void setVideosort(String videosort) {
        this.videosort = videosort;
    }

    public boolean isImagemodify() {
        return imagemodify;
    }

    public void setImagemodify(boolean imagemodify) {
        this.imagemodify = imagemodify;
    }

    public boolean isVideomodify() {
        return videomodify;
    }

    public void setVideomodify(boolean videomodify) {
        this.videomodify = videomodify;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MutilPartInfo{" +
                "position=" + position +
                ", url='" + url + '\'' +
                ", videourl='" + videourl + '\'' +
                ", Thumbnail='" + Thumbnail + '\'' +
                '}';
    }
}
