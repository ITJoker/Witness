package com.risenb.witness.views.newViews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.risenb.witness.utils.newUtils.Utils;

public class ColorArcProgressBar extends View {
    private int mWidth;
    private int mHeight;
    private int diameter = 300;
    private float centerX;
    private float centerY;
    private Paint allArcPaint;
    private Paint progressPaint;
    private Paint vTextPaint;
    private Paint hintPaint;
    private Paint degreePaint;
    private Paint curSpeedPaint;
    private RectF bgRect;
    private ValueAnimator progressAnimator;
    private float startAngle = 135.0F;
    private float sweepAngle = 270.0F;
    private float currentAngle = 0.0F;
    private float lastAngle;
    private int[] colors = new int[]{-16711936, -256, -65536, -65536};
    private float maxValues = 60.0F;
    private float curValues = 0.0F;
    private float bgArcWidth = (float)this.dipToPx(2.0F);
    private float progressWidth = (float)this.dipToPx(2.0F);
    private float textSize = (float)this.dipToPx(20.0F);
    private float hintSize = (float)this.dipToPx(15.0F);
    private float curSpeedSize = (float)this.dipToPx(13.0F);
    private int aniSpeed = 1000;
    private float longdegree = (float)this.dipToPx(13.0F);
    private float shortdegree = (float)this.dipToPx(5.0F);
    private final int DEGREE_PROGRESS_DISTANCE = this.dipToPx(8.0F);
    private String hintColor = "#676767";
    private String longDegreeColor = "#111111";
    private String shortDegreeColor = "#111111";
    private String bgArcColor = "#999999";
    private boolean isShowCurrentSpeed = true;
    private String hintString = "Km/h";
    private boolean isNeedTitle;
    private boolean isNeedUnit;
    private boolean isNeedDial;
    private boolean isNeedContent;
    private String titleString;
    private float k;

    public ColorArcProgressBar(Context context) {
        super(context, (AttributeSet)null);
        this.initView();
    }

    public ColorArcProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        this.initCofig(context, attrs);
        this.initView();
    }

    public ColorArcProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initCofig(context, attrs);
        this.initView();
    }

    private void initCofig(Context context, AttributeSet attrs) {
        int color1 = -16711936;
        int color2 = -16711936;
        int color3 = -16711936;
        this.colors = new int[]{color1, color2, color3, color3};
        this.sweepAngle = 360.0F;
        this.bgArcWidth = (float)this.dipToPx(2.0F);
        this.progressWidth = (float)this.dipToPx(3.0F);
        this.isNeedTitle = false;
        this.isNeedContent = false;
        this.isNeedUnit = false;
        this.isNeedDial = false;
        this.hintString = "";
        this.titleString = "";
        this.curValues = 0.0F;
        this.maxValues = 100.0F;
        this.setCurrentValues(this.curValues);
        this.setMaxValues(this.maxValues);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (int)(2.0F * this.longdegree + this.progressWidth + (float)this.diameter + (float)(2 * this.DEGREE_PROGRESS_DISTANCE));
        int height = (int)(2.0F * this.longdegree + this.progressWidth + (float)this.diameter + (float)(2 * this.DEGREE_PROGRESS_DISTANCE));
        this.setMeasuredDimension(width, height);
    }

    private void initView() {
        this.diameter = Utils.getUtils().getCapbWidth();
        this.bgRect = new RectF();
        this.bgRect.top = this.longdegree + this.progressWidth / 2.0F + (float)this.DEGREE_PROGRESS_DISTANCE;
        this.bgRect.left = this.longdegree + this.progressWidth / 2.0F + (float)this.DEGREE_PROGRESS_DISTANCE;
        this.bgRect.right = (float)this.diameter + this.longdegree + this.progressWidth / 2.0F + (float)this.DEGREE_PROGRESS_DISTANCE;
        this.bgRect.bottom = (float)this.diameter + this.longdegree + this.progressWidth / 2.0F + (float)this.DEGREE_PROGRESS_DISTANCE;
        this.centerX = (2.0F * this.longdegree + this.progressWidth + (float)this.diameter + (float)(2 * this.DEGREE_PROGRESS_DISTANCE)) / 2.0F;
        this.centerY = (2.0F * this.longdegree + this.progressWidth + (float)this.diameter + (float)(2 * this.DEGREE_PROGRESS_DISTANCE)) / 2.0F;
        this.degreePaint = new Paint();
        this.degreePaint.setColor(Color.parseColor(this.longDegreeColor));
        this.allArcPaint = new Paint();
        this.allArcPaint.setAntiAlias(true);
        this.allArcPaint.setStyle(Paint.Style.STROKE);
        this.allArcPaint.setStrokeWidth(this.bgArcWidth);
        this.allArcPaint.setColor(Color.parseColor(this.bgArcColor));
        this.allArcPaint.setStrokeCap(Paint.Cap.ROUND);
        this.progressPaint = new Paint();
        this.progressPaint.setAntiAlias(true);
        this.progressPaint.setStyle(Paint.Style.STROKE);
        this.progressPaint.setStrokeCap(Paint.Cap.ROUND);
        this.progressPaint.setStrokeWidth(this.progressWidth);
        this.progressPaint.setColor(-16711936);
        this.vTextPaint = new Paint();
        this.vTextPaint.setTextSize(this.textSize);
        this.vTextPaint.setColor(-16777216);
        this.vTextPaint.setTextAlign(Paint.Align.CENTER);
        this.hintPaint = new Paint();
        this.hintPaint.setTextSize(this.hintSize);
        this.hintPaint.setColor(Color.parseColor(this.hintColor));
        this.hintPaint.setTextAlign(Paint.Align.CENTER);
        this.curSpeedPaint = new Paint();
        this.curSpeedPaint.setTextSize(this.curSpeedSize);
        this.curSpeedPaint.setColor(Color.parseColor(this.hintColor));
        this.curSpeedPaint.setTextAlign(Paint.Align.CENTER);
    }

    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        if(this.isNeedDial) {
            for(int sweepGradient = 0; sweepGradient < 40; ++sweepGradient) {
                if(sweepGradient > 15 && sweepGradient < 25) {
                    canvas.rotate(9.0F, this.centerX, this.centerY);
                } else {
                    if(sweepGradient % 5 == 0) {
                        this.degreePaint.setStrokeWidth((float)this.dipToPx(2.0F));
                        this.degreePaint.setColor(Color.parseColor(this.longDegreeColor));
                        canvas.drawLine(this.centerX, this.centerY - (float)(this.diameter / 2) - this.progressWidth / 2.0F - (float)this.DEGREE_PROGRESS_DISTANCE, this.centerX, this.centerY - (float)(this.diameter / 2) - this.progressWidth / 2.0F - (float)this.DEGREE_PROGRESS_DISTANCE - this.longdegree, this.degreePaint);
                    } else {
                        this.degreePaint.setStrokeWidth((float)this.dipToPx(1.4F));
                        this.degreePaint.setColor(Color.parseColor(this.shortDegreeColor));
                        canvas.drawLine(this.centerX, this.centerY - (float)(this.diameter / 2) - this.progressWidth / 2.0F - (float)this.DEGREE_PROGRESS_DISTANCE - (this.longdegree - this.shortdegree) / 2.0F, this.centerX, this.centerY - (float)(this.diameter / 2) - this.progressWidth / 2.0F - (float)this.DEGREE_PROGRESS_DISTANCE - (this.longdegree - this.shortdegree) / 2.0F - this.shortdegree, this.degreePaint);
                    }

                    canvas.rotate(9.0F, this.centerX, this.centerY);
                }
            }
        }

        canvas.drawArc(this.bgRect, this.startAngle, this.sweepAngle, false, this.allArcPaint);
        SweepGradient var4 = new SweepGradient(this.centerX, this.centerY, this.colors, (float[])null);
        Matrix matrix = new Matrix();
        matrix.setRotate(130.0F, this.centerX, this.centerY);
        var4.setLocalMatrix(matrix);
        this.progressPaint.setShader(var4);
        canvas.drawArc(this.bgRect, this.startAngle, this.currentAngle, false, this.progressPaint);
        this.invalidate();
    }

    public void setMaxValues(float maxValues) {
        this.maxValues = maxValues;
        this.k = this.sweepAngle / maxValues;
    }

    public void setCurrentValues(float currentValues) {
        if(currentValues > this.maxValues) {
            currentValues = this.maxValues;
        }

        if(currentValues < 0.0F) {
            currentValues = 0.0F;
        }

        this.curValues = currentValues;
        this.lastAngle = this.currentAngle;
        this.setAnimation(this.lastAngle, currentValues * this.k, this.aniSpeed);
    }

    public void setBgArcWidth(int bgArcWidth) {
        this.bgArcWidth = (float)bgArcWidth;
    }

    public void setProgressWidth(int progressWidth) {
        this.progressWidth = (float)progressWidth;
    }

    public void setTextSize(int textSize) {
        this.textSize = (float)textSize;
    }

    public void setHintSize(int hintSize) {
        this.hintSize = (float)hintSize;
    }

    public void setUnit(String hintString) {
        this.hintString = hintString;
        this.invalidate();
    }

    public void setDiameter(int diameter) {
        this.diameter = this.dipToPx((float)diameter);
    }

    private void setAnimation(float last, float current, int length) {
        this.progressAnimator = ValueAnimator.ofFloat(new float[]{last, current});
        this.progressAnimator.setDuration((long)length);
        this.progressAnimator.setTarget(Float.valueOf(this.currentAngle));
        this.progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                ColorArcProgressBar.this.currentAngle = ((Float)animation.getAnimatedValue()).floatValue();
                ColorArcProgressBar.this.curValues = ColorArcProgressBar.this.currentAngle / ColorArcProgressBar.this.k;
            }
        });
        this.progressAnimator.start();
    }

    private int dipToPx(float dip) {
        float density = this.getContext().getResources().getDisplayMetrics().density;
        return (int)(dip * density + 0.5F * (float)(dip >= 0.0F?1:-1));
    }

    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager)this.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public void setIsShowCurrentSpeed(boolean isShowCurrentSpeed) {
        this.isShowCurrentSpeed = isShowCurrentSpeed;
    }
}
