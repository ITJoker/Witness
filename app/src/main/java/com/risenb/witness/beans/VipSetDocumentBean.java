package com.risenb.witness.beans;

import java.util.List;

public class VipSetDocumentBean {
    private String content; //"content":"首先注册之后",
    private String title;//     "title":"如何抢任务？"
    private List<String> list;

    public void setList(List<String> list) {
        this.list = list;
    }

    public List<String> getList() {
        return list;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
