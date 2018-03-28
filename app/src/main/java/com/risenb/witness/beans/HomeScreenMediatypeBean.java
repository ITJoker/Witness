package com.risenb.witness.beans;

public class HomeScreenMediatypeBean {

    private String medianame; //"medianame":"北京",
    private String mediaid; //     "cityid":1
    private String  sortLetters;

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public String getMedianame() {
        return medianame;
    }

    public void setMedianame(String medianame) {
        this.medianame = medianame;
    }

    public String getMediaid() {
        return mediaid;
    }

    public void setMediaid(String mediaid) {
        this.mediaid = mediaid;
    }
}
