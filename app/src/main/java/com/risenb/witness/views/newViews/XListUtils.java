package com.risenb.witness.views.newViews;

public class XListUtils {
    private static XListUtils xListUtils;
    public int xlistview_header_content;
    public int xlistview_header_time;
    public int xlistview_header;
    public int xlistview_header_arrow;
    public int xlistview_header_hint_textview;
    public int xlistview_header_progressbar;

    public XListUtils() {
    }

    public static XListUtils getXListUtils() {
        if(xListUtils == null) {
            xListUtils = new XListUtils();
        }

        return xListUtils;
    }

    public void info(int xlistview_header_content, int xlistview_header_time, int xlistview_header, int xlistview_header_arrow, int xlistview_header_hint_textview, int xlistview_header_progressbar) {
        this.xlistview_header_content = xlistview_header_content;
        this.xlistview_header_time = xlistview_header_time;
        this.xlistview_header = xlistview_header;
        this.xlistview_header_arrow = xlistview_header_arrow;
        this.xlistview_header_hint_textview = xlistview_header_hint_textview;
        this.xlistview_header_progressbar = xlistview_header_progressbar;
    }
}
