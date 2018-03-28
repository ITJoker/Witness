package com.risenb.witness.views.newViews;

public class NumericWheelAdapter implements WheelAdapter {
    public static final int DEFAULT_MAX_VALUE = 9;
    private static final int DEFAULT_MIN_VALUE = 0;
    private int minValue;
    private int maxValue;
    private String format;
    private String unit;

    public NumericWheelAdapter() {
        this(0, 9);
    }

    public NumericWheelAdapter(int minValue, int maxValue) {
        this(minValue, maxValue, (String) null);
    }

    public NumericWheelAdapter(int minValue, int maxValue, String format) {
        this.unit = "";
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.format = format;
    }

    public NumericWheelAdapter(int minValue, int maxValue, String format, String unit) {
        this.unit = "";
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.format = format;
        this.unit = unit;
    }

    public String getItem(int index) {
        if (index >= 0 && index < this.getItemsCount()) {
            int value = this.minValue + index;
            return this.format != null ? String.format(this.format, new Object[]{Integer.valueOf(value)}) + this.unit : value + this.unit;
        } else {
            return null;
        }
    }

    public int getItemsCount() {
        return this.maxValue - this.minValue + 1;
    }

    public int getMaximumLength() {
        int max = Math.max(Math.abs(this.maxValue), Math.abs(this.minValue));
        int maxLen = Integer.toString(max).length();
        if (this.minValue < 0) {
            ++maxLen;
        }

        return maxLen;
    }
}
