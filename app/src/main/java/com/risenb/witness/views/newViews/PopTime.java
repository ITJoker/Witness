package com.risenb.witness.views.newViews;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.risenb.witness.utils.newUtils.OnDateCallback;
import com.risenb.witness.utils.newUtils.SeletTimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.Window.ID_ANDROID_CONTENT;

public class PopTime extends PopupWindow implements OnDateCallback, View.OnClickListener {
    private OnDateCallback onDateCallback;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private String nowDate;
    private long timestamps;

    public static void getSeleTime(Activity activity, String pattern, OnDateCallback onDateCallback) {
        new PopTime(activity, pattern, 0L, onDateCallback);
    }

    public static void getSeleTime(Activity activity, String pattern, long timestamps, OnDateCallback onDateCallback) {
        new PopTime(activity, pattern, timestamps, onDateCallback);
    }

    @SuppressLint({"SimpleDateFormat"})
    public static void getSeleTime(Activity activity, String pattern, String dateStr, OnDateCallback onDateCallback) {
        try {
            SimpleDateFormat e = new SimpleDateFormat(pattern);
            Date date = e.parse(dateStr);
            new PopTime(activity, pattern, date.getTime(), onDateCallback);
        } catch (ParseException var6) {
            new PopTime(activity, pattern, 0L, onDateCallback);
        }

    }

    @SuppressLint({"SimpleDateFormat"})
    public static void getSeleTime(Activity activity, String pattern, int year, int month, int day, int hour, int minute, OnDateCallback onDateCallback) {
        try {
            SimpleDateFormat e = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String time = year + "-" + month + "-" + day + " " + hour + ":" + minute;
            Date date = e.parse(time);
            new PopTime(activity, pattern, date.getTime(), onDateCallback);
        } catch (ParseException var11) {
            new PopTime(activity, pattern, 0L, onDateCallback);
        }

    }

    private PopTime(Activity activity, String pattern, long timestamps, OnDateCallback onDateCallback) {
        super(activity);
        this.onDateCallback = onDateCallback;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService("layout_inflater");
        View mMenuView = inflater.inflate(SeletTimeUtils.getSeletTimeUtils().getLayoutID(), null);
        TextView tv_pop_time_cancel = (TextView) mMenuView.findViewById(SeletTimeUtils.getSeletTimeUtils().getCancelID());
        tv_pop_time_cancel.setOnClickListener(this);
        TextView tv_pop_time_submit = (TextView) mMenuView.findViewById(SeletTimeUtils.getSeletTimeUtils().getSubmitID());
        tv_pop_time_submit.setOnClickListener(this);
        MyDatePicker dp_pop_time = (MyDatePicker) mMenuView.findViewById(SeletTimeUtils.getSeletTimeUtils().getDpID());
        dp_pop_time.setPattern(pattern);
        dp_pop_time.setOnDateCallback(this);
        dp_pop_time.setTimestamps(timestamps);
        this.setContentView(mMenuView);
        this.setWidth(-1);
        this.setHeight(-2);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0);
        this.setBackgroundDrawable(dw);
        this.setSoftInputMode(16);
        View showView = activity.getWindow().getDecorView().findViewById(ID_ANDROID_CONTENT);
        this.showAtLocation(showView, 81, 0, 0);
        this.update();
    }

    public void onClick(View v) {
        this.dismiss();
        if (v.getId() == SeletTimeUtils.getSeletTimeUtils().getSubmitID()) {
            this.onDateCallback.onDateCallback(this.year, this.month, this.day, this.hour, this.minute, this.nowDate, this.timestamps);
        }

    }

    public void onDateCallback(int year, int month, int day, int hour, int minute, String nowDate, long timestamps) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.nowDate = nowDate;
        this.timestamps = timestamps;
    }
}
