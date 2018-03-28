package com.risenb.witness.beans;

public class RadioButtonInfo {
    private String RaidotextContent;
    private boolean selected;
    private String id;

    public String getRaidotextContent() {
        return RaidotextContent;
    }

    public void setRaidotextContent(String raidotextContent) {
        RaidotextContent = raidotextContent;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RadioButtonInfo{" +
                "RaidotextContent='" + RaidotextContent + '\'' +
                ", selected=" + selected +
                ", id='" + id + '\'' +
                '}';
    }
}
