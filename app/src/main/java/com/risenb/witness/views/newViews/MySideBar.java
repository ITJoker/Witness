package com.risenb.witness.views.newViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MySideBar extends View {
    public static String[] LETTER = new String[]{"*", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    private MySideBar.OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    private int choose = -1;
    private Paint paint = new Paint();
    private Context context;
    private boolean showBkg = false;

    public MySideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public MySideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public MySideBar(Context context) {
        super(context);
        this.context = context;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.showBkg) {
            canvas.drawColor(Color.parseColor("#40000000"));
        }

        float scale = this.context.getResources().getDisplayMetrics().density;
        int dp = (int) (12.0F * scale + 0.5F);
        int height = this.getHeight();
        int width = this.getWidth();
        int singleHeight = height / LETTER.length;

        for (int i = 0; i < LETTER.length; ++i) {
            this.paint.setColor(-7829368);
            this.paint.setTypeface(Typeface.DEFAULT_BOLD);
            this.paint.setAntiAlias(true);
            this.paint.setTextSize((float) dp);
            if (i == this.choose) {
                this.paint.setColor(Color.parseColor("#525252"));
                this.paint.setFakeBoldText(true);
            }

            float xPos = (float) (width / 2) - this.paint.measureText(LETTER[i]) / 2.0F;
            float yPos = (float) (singleHeight * i + singleHeight);
            canvas.drawText(LETTER[i], xPos, yPos, this.paint);
            this.paint.reset();
        }

    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float y = event.getY();
        int oldChoose = this.choose;
        MySideBar.OnTouchingLetterChangedListener listener = this.onTouchingLetterChangedListener;
        int c = (int) (y / (float) this.getHeight() * (float) LETTER.length);
        switch (action) {
            case 0:
                this.showBkg = true;
                if (oldChoose != c && listener != null && c >= 0 && c < LETTER.length) {
                    if (listener != null) {
                        listener.onTouchingLetterChanged(LETTER[c]);
                        listener.onTouchingLetterChanged(c);
                    }

                    this.choose = c;
                    this.invalidate();
                }
                break;
            case 1:
            case 3:
                this.showBkg = false;
                this.choose = -1;
                this.invalidate();
                if (listener != null) {
                    listener.onTouchingLetterUP();
                }
                break;
            case 2:
                if (oldChoose != c && listener != null && c >= 0 && c < LETTER.length) {
                    if (listener != null) {
                        listener.onTouchingLetterChanged(LETTER[c]);
                        listener.onTouchingLetterChanged(c);
                    }

                    this.choose = c;
                    this.invalidate();
                }
        }

        return true;
    }

    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setOnTouchingLetterChangedListener(MySideBar.OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String var1);

        void onTouchingLetterChanged(int var1);

        void onTouchingLetterUP();
    }
}
