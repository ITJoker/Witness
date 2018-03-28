package com.risenb.witness.ui.tasklist;

public class CheckBoxInfo {
    private String TextContent;
    private boolean selected;
    private String id;

    public CheckBoxInfo() {

    }

    public CheckBoxInfo(String textContent, boolean selected) {
        this.TextContent = textContent;
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    public String getTextContent() {
        return TextContent;
    }

    public void setTextContent(String textContent) {
        TextContent = textContent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CheckBoxInfo{" +
                "TextContent='" + TextContent + '\'' +
                ", selected=" + selected +
                '}';
    }
}
