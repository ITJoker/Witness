package com.risenb.witness.views.newViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.risenb.witness.utils.newUtils.OnDateCallback;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyDatePicker extends LinearLayout {
    private Calendar calendar = Calendar.getInstance();
    private WheelView years;
    private WheelView months;
    private WheelView days;
    private WheelView hour;
    private WheelView minute;
    private OnDateCallback onDateCallback;
    private int num = 0;
    private String pattern;
    private OnWheelChangedListener onYearsChangedListener = new OnWheelChangedListener() {
        public void onChanged(WheelView hours, int oldValue, int newValue) {
            MyDatePicker.this.calendar.set(Calendar.YEAR, 1900 + newValue);
            MyDatePicker.this.onDateCallback.onDateCallback(MyDatePicker.this.getYear(), MyDatePicker.this.getMonth(), MyDatePicker.this.getDay(), MyDatePicker.this.getHour(), MyDatePicker.this.getMinute(), MyDatePicker.this.getDate(), MyDatePicker.this.getTimestamps());
            int maxday_of_month = MyDatePicker.this.calendar.getActualMaximum(Calendar.DATE);
            MyDatePicker.this.days.setAdapter(new NumericWheelAdapter(1, maxday_of_month, "%02d", "日"));
        }
    };
    private OnWheelChangedListener onMonthsChangedListener = new OnWheelChangedListener() {
        public void onChanged(WheelView mins, int oldValue, int newValue) {
            MyDatePicker.this.calendar.set(Calendar.MONTH, newValue);
            MyDatePicker.this.onDateCallback.onDateCallback(MyDatePicker.this.getYear(), MyDatePicker.this.getMonth(), MyDatePicker.this.getDay(), MyDatePicker.this.getHour(), MyDatePicker.this.getMinute(), MyDatePicker.this.getDate(), MyDatePicker.this.getTimestamps());
            int maxday_of_month = MyDatePicker.this.calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            MyDatePicker.this.days.setAdapter(new NumericWheelAdapter(1, maxday_of_month, "%02d", "日"));
        }
    };
    private OnWheelChangedListener onDaysChangedListener = new OnWheelChangedListener() {
        public void onChanged(WheelView mins, int oldValue, int newValue) {
            MyDatePicker.this.calendar.set(Calendar.DATE, newValue + 1);
            MyDatePicker.this.onDateCallback.onDateCallback(MyDatePicker.this.getYear(), MyDatePicker.this.getMonth(), MyDatePicker.this.getDay(), MyDatePicker.this.getHour(), MyDatePicker.this.getMinute(), MyDatePicker.this.getDate(), MyDatePicker.this.getTimestamps());
        }
    };
    private OnWheelChangedListener onHourChangedListener = new OnWheelChangedListener() {
        public void onChanged(WheelView mins, int oldValue, int newValue) {
            MyDatePicker.this.calendar.set(Calendar.HOUR_OF_DAY, newValue);
            MyDatePicker.this.onDateCallback.onDateCallback(MyDatePicker.this.getYear(), MyDatePicker.this.getMonth(), MyDatePicker.this.getDay(), MyDatePicker.this.getHour(), MyDatePicker.this.getMinute(), MyDatePicker.this.getDate(), MyDatePicker.this.getTimestamps());
        }
    };
    private OnWheelChangedListener onMinuteChangedListener = new OnWheelChangedListener() {
        public void onChanged(WheelView mins, int oldValue, int newValue) {
            MyDatePicker.this.calendar.set(Calendar.MINUTE, newValue);
            MyDatePicker.this.onDateCallback.onDateCallback(MyDatePicker.this.getYear(), MyDatePicker.this.getMonth(), MyDatePicker.this.getDay(), MyDatePicker.this.getHour(), MyDatePicker.this.getMinute(), MyDatePicker.this.getDate(), MyDatePicker.this.getTimestamps());
        }
    };

    public MyDatePicker(Context context) {
        super(context);
        this.init(context);
    }

    public MyDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    private void init(Context context) {
        LayoutParams lparams = new LayoutParams(0, -2);
        lparams.weight = 1.0F;
        this.years = new WheelView(context);
        this.years.setLayoutParams(lparams);
        this.years.setAdapter(new NumericWheelAdapter(1900, 2100, (String) null, "年"));
        this.years.setVisibleItems(5);
        this.years.addChangingListener(this.onYearsChangedListener);
        this.addView(this.years);
        this.months = new WheelView(context);
        this.months.setLayoutParams(lparams);
        this.months.setAdapter(new NumericWheelAdapter(1, 12, "%02d", "月"));
        this.months.setVisibleItems(5);
        this.months.setCyclic(true);
        this.months.addChangingListener(this.onMonthsChangedListener);
        this.addView(this.months);
        this.days = new WheelView(context);
        this.days.setLayoutParams(lparams);
        int maxday_of_month = this.calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        this.days.setAdapter(new NumericWheelAdapter(1, maxday_of_month, "%02d", "日"));
        this.days.setVisibleItems(5);
        this.days.setCyclic(true);
        this.days.addChangingListener(this.onDaysChangedListener);
        this.addView(this.days);
        this.hour = new WheelView(context);
        this.hour.setLayoutParams(lparams);
        this.hour.setAdapter(new NumericWheelAdapter(0, 23, "%02d", "时"));
        this.hour.setVisibleItems(5);
        this.hour.setCyclic(true);
        this.hour.addChangingListener(this.onHourChangedListener);
        this.addView(this.hour);
        this.minute = new WheelView(context);
        this.minute.setLayoutParams(lparams);
        this.minute.setAdapter(new NumericWheelAdapter(0, 59, "%02d", "分"));
        this.minute.setVisibleItems(5);
        this.minute.setCyclic(true);
        this.minute.addChangingListener(this.onMinuteChangedListener);
        this.addView(this.minute);
        ViewTreeObserver vto = this.minute.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                if (MyDatePicker.this.num < 10) {
                    MyDatePicker.this.num++;
                    MyDatePicker.this.years.setAdapter(new NumericWheelAdapter(1900, 2100, (String) null, "年"));
                    MyDatePicker.this.months.setAdapter(new NumericWheelAdapter(1, 12, "%02d", "月"));
                    int maxday_of_month = MyDatePicker.this.calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    MyDatePicker.this.days.setAdapter(new NumericWheelAdapter(1, maxday_of_month, "%02d", "日"));
                    MyDatePicker.this.hour.setAdapter(new NumericWheelAdapter(0, 23, "%02d", "时"));
                    MyDatePicker.this.minute.setAdapter(new NumericWheelAdapter(0, 59, "%02d", "分"));
                }

                return true;
            }
        });
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
        this.years.setVisibility(pattern.indexOf("yyyy") == -1 ? GONE : VISIBLE);
        this.months.setVisibility(pattern.indexOf("MM") == -1 ? GONE : VISIBLE);
        this.days.setVisibility(pattern.indexOf("dd") == -1 ? GONE : VISIBLE);
        this.hour.setVisibility(pattern.indexOf("HH") == -1 ? GONE : VISIBLE);
        this.minute.setVisibility(pattern.indexOf("mm") == -1 ? GONE : VISIBLE);
    }

    public void setOnDateCallback(OnDateCallback onDateCallback) {
        this.onDateCallback = onDateCallback;
    }

    @SuppressLint({"SimpleDateFormat"})
    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(this.pattern);
        return sdf.format(new Timestamp(this.getTimestamps()));
    }

    public long getTimestamps() {
        return this.calendar.getTimeInMillis();
    }

    public void setTimestamps(long timestamps) {
        if (timestamps == 0L) {
            this.calendar.setTimeInMillis(System.currentTimeMillis());
        } else {
            this.calendar.setTimeInMillis(timestamps);
        }

    }

    public void setYear(int year) {
        if (year != 0) {
            this.years.setCurrentItem(year - 1900);
        }

    }

    public int getYear() {
        return this.calendar.get(Calendar.YEAR);
    }

    public void setMonth(int month) {
        if (month != 0) {
            this.months.setCurrentItem(month - 1);
        }

    }

    public int getMonth() {
        return this.calendar.get(Calendar.MONTH) + 1;
    }

    public void setDay(int day) {
        if (day != 0) {
            this.days.setCurrentItem(day - 1);
        }

    }

    public int getDay() {
        return this.calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getHour() {
        return this.calendar.get(Calendar.HOUR_OF_DAY);
    }

    public void setHour(int hour) {
        this.hour.setCurrentItem(hour);
    }

    public int getMinute() {
        return this.calendar.get(Calendar.MINUTE);
    }

    public void setMinute(int minute) {
        this.minute.setCurrentItem(minute);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.setYear(this.getYear());
        this.setMonth(this.getMonth());
        this.setDay(this.getDay());
    }
}
