package com.risenb.witness.views.newViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.risenb.witness.utils.newUtils.Log;
import com.risenb.witness.utils.newUtils.Utils;

@SuppressLint("AppCompatCustomView")
public class ShowImageView extends ImageView {
    private ShowCallback onShowCallback;
    public static int drawLineWidth;
    private int screenW;
    private int screenH;
    private int down_x;
    private int down_y;
    private int menu = 20;
    private int menu_x;
    private int menu_y;
    public int down2_x;
    public int down2_y;
    private boolean isMoved;
    public static Touch touch;
    public static boolean setFrame;
    private boolean Fit = false;
    private double beforeLenght;
    private Context context;
    private int height;
    private int width;
    private int height_10;
    private int width_10;
    private double mark;
    private int markW;
    private int markH;
    private int l;
    private int t;
    private int r;
    private int b;
    private int lx;
    private int ly;
    private int rx;
    private int ry;
    private Bitmap bitmap;
    private double frameFit;

    private void info() {
        this.lx = 0;
        this.ly = 0;
        this.rx = 0;
        this.ry = 0;
    }

    public ShowImageView(Context context) {
        super(context);
        this.context = context;
    }

    public ShowImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public ShowImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public ShowImageView(Context context, int screenW, int screenH) {
        super(context);
        this.context = context;
        this.screenW = screenW;
        this.screenH = screenH;
        this.info();
    }

    public void setLeft() {
        int w = this.r - this.l;
        this.l -= w;
        this.r -= w;
        this.setFrame(this.l, this.t, this.r, this.b);
    }

    public void setRight() {
        int w = this.r - this.l;
        this.l += w;
        this.r += w;
        this.setFrame(this.l, this.t, this.r, this.b);
    }

    public void setOnShowCallback(ShowCallback onShowCallback) {
        this.onShowCallback = onShowCallback;
    }

    public void setImageBitmap(Bitmap bitmap) {
        this.info();
        this.bitmap = bitmap;
        super.setImageBitmap(bitmap);
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
        Log.e("setImageBitmap width = " + this.width + " height = " + this.height);
        this.width_10 = this.width / 10;
        this.height_10 = this.height / 10;
        touch = Touch.DRAG;
        setFrame = true;
        super.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (setFrame) {
                    setFrameFitView();
                }
            }
        });
    }

    public void setFrame2(int l, int t, int r, int b) {
        super.setFrame(l, t, r, b);
    }

    public void setFrameBig(MotionEvent event) {
        this.markW = (int) ((double) ((event.getX(0) - (float) this.getLeft()) * 10.0F) / this.mark);
        this.markH = (int) ((double) ((event.getY(0) - (float) this.getTop()) * 10.0F) / this.mark);
        this.Big();
    }

    public void setFrameBig() {
        this.markW = this.width / 2;
        this.markH = this.height / 2;
        this.Big();
        this.up();
    }

    public void setFrameSmall() {
        this.markW = this.width / 2;
        this.markH = this.height / 2;
        this.Small();
        this.up();
    }

    public void setFrameOriginal() {
        this.l = 0;
        this.t = 0;
        this.r = this.width;
        this.b = this.height;
        this.mark = 10.0D;
        this.setFrame(this.l, this.t, this.r, this.b);
    }

    public void setFrameFitWidth() {
        this.Fit = true;
        this.l = 0;
        this.r = this.screenW;
        this.t = 0;
        this.b = this.screenW * this.height / this.width;
        this.mark = (double) this.screenW * 10.0D / (double) this.width;
        Log.e("mark = " + this.mark);
        if (this.b < this.screenH) {
            this.t = (this.screenH - this.b) / 2;
            this.b += this.t;
        }

        this.setFrame(this.l, this.t, this.r, this.b);
    }

    public void setFrameFitHight() {
        this.Fit = true;
        this.l = 0;
        this.r = this.screenH * this.width / this.height;
        this.t = 0;
        this.b = this.screenH;
        this.mark = (double) this.screenH * 10.0D / (double) this.height;
        Log.e("mark = " + this.mark);
        if (this.r < this.screenW) {
            this.l = (this.screenW - this.r) / 2;
            this.r += this.l;
        }

        this.setFrame(this.l, this.t, this.r, this.b);
    }

    public void setFrameFitView() {
        this.Fit = true;
        if (this.screenH * this.width > this.screenW * this.height) {
            this.setFrameFitWidth();
        } else {
            this.setFrameFitHight();
        }

        this.frameFit = this.mark;
    }

    public boolean isFrameFit() {
        return this.frameFit == this.mark;
    }

    private boolean Big() {
        int markW2;
        int markH2;
        if (this.Fit) {
            this.Fit = false;
            markW2 = (int) this.mark;
            double var10001;
            if (markW2 >= 10) {
                var10001 = (double) (markW2 / 10 * 10 + 10);
            } else {
                ++markW2;
                var10001 = (double) markW2;
            }

            this.mark = var10001;
            markH2 = (int) ((double) this.width * this.mark / 10.0D);
            int _h = (int) ((double) this.height * this.mark / 10.0D);
            int num_x = (markH2 - (this.r - this.l)) / 2;
            int num_y = (_h - (this.b - this.t)) / 2;
            this.l -= num_x;
            this.t -= num_y;
            this.r = this.l + markH2;
            this.b = this.t + _h;
            this.setFrame(this.l, this.t, this.r, this.b);
        } else {
            if (this.mark == 60.0D) {
                return false;
            }

            if (this.mark >= 10.0D) {
                this.l -= this.markW;
                this.t -= this.markH;
                this.r += this.width - this.markW;
                this.b += this.height - this.markH;
                this.setFrame(this.l, this.t, this.r, this.b);
                this.mark += 10.0D;
            } else {
                markW2 = this.markW / 10;
                markH2 = this.markH / 10;
                this.l -= markW2;
                this.t -= markH2;
                this.r += this.width_10 - markW2;
                this.b += this.height_10 - markH2;
                this.setFrame(this.l, this.t, this.r, this.b);
                ++this.mark;
            }
        }

        return true;
    }

    private boolean Small() {
        if (this.r - this.l <= this.screenW && this.b - this.t <= this.screenH) {
            return false;
        } else {
            int markW2;
            int markH2;
            if (this.Fit) {
                this.Fit = false;
                markW2 = (int) this.mark;
                this.mark = markW2 > 10 ? (double) (markW2 / 10 * 10) : (double) markW2;
                markH2 = (int) ((double) this.width * this.mark / 10.0D);
                int _h = (int) ((double) this.height * this.mark / 10.0D);
                int num_x = (this.r - this.l - markH2) / 2;
                int num_y = (this.b - this.t - _h) / 2;
                this.l += num_x;
                this.t += num_y;
                this.r = this.l + markH2;
                this.b = this.t + _h;
            } else {
                if (this.mark == 1.0D) {
                    return false;
                }

                if (this.mark > 10.0D) {
                    this.l += this.markW;
                    this.t += this.markH;
                    this.r -= this.width - this.markW;
                    this.b -= this.height - this.markH;
                    this.mark -= 10.0D;
                } else {
                    markW2 = this.markW / 10;
                    markH2 = this.markH / 10;
                    this.l += markW2;
                    this.t += markH2;
                    this.r -= this.width_10 - markW2;
                    this.b -= this.height_10 - markH2;
                    --this.mark;
                }
            }

            if (this.r - this.l < this.screenW && this.b - this.t < this.screenH) {
                this.setFrameFitView();
                return false;
            } else {
                this.setFrame(this.l, this.t, this.r, this.b);
                return true;
            }
        }
    }

    public void setRect() {
        this.lx = 0;
        this.ly = 0;
        this.rx = 0;
        this.ry = 0;
    }

    public Rect getRect() {
        Rect rect = new Rect(this.lx, this.ly, this.rx, this.ry);
        this.drawRect(-65536, rect);
        return rect;
    }

    private void drawRect(int color, Rect rect) {
        if (rect.right != 0) {
            if (rect.bottom != 0) {
                int i;
                for (i = rect.left; i <= rect.right; ++i) {
                    this.bitmap.setPixel(i, rect.top, color);
                    this.bitmap.setPixel(i, rect.bottom, color);
                }

                for (i = rect.top; i <= rect.bottom; ++i) {
                    this.bitmap.setPixel(rect.left, i, color);
                    this.bitmap.setPixel(rect.right, i, color);
                }

            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        this.menu(event);
        this.dragZoom(event);
        return true;
    }

    public boolean menu(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                this.isMoved = true;
                this.menu_x = (int) event.getRawX();
                this.menu_y = (int) event.getRawY();
                this.down2_x = (int) ((double) (event.getX() * 10.0F) / this.mark);
                this.down2_y = (int) ((double) (event.getY() * 10.0F) / this.mark);
                break;
            case 1:
                this.isMoved = false;
                break;
            case 2:
                if (this.isMoved && (Math.abs((float) this.menu_x - event.getRawX()) > (float) (this.screenW / this.menu) || Math.abs((float) this.menu_y - event.getRawY()) > (float) (this.screenH / this.menu))) {
                    this.isMoved = false;
                }
        }

        return false;
    }

    public void dragZoom(MotionEvent event) {
        switch (event.getAction() & 255) {
            case 0:
                touch = Touch.DRAG;
                this.l = super.getLeft();
                this.t = super.getTop();
                this.r = super.getRight();
                this.b = super.getBottom();
                setFrame = false;
                Log.e("l = " + this.l + " t = " + this.t + " r = " + this.r + " b = " + this.b);
                break;
            case 1:
                this.up();
                break;
            case 5:
                touch = Touch.ZOOM;
        }

        if (touch == Touch.DRAG) {
            this.drag(event);
        } else if (touch == Touch.ZOOM) {
            this.zoom(event);
        }

    }

    public void drag(MotionEvent event) {
        switch (event.getAction() & 255) {
            case 0:
                this.down_x = (int) event.getRawX();
                this.down_y = (int) event.getRawY();
                break;
            case 2:
                if (this.t > 0 && this.b < this.screenH) {
                    super.layout(this.l + (int) event.getRawX() - this.down_x, this.t, this.r + (int) event.getRawX() - this.down_x, this.b);
                } else {
                    super.layout(this.l + (int) event.getRawX() - this.down_x, this.t + (int) event.getRawY() - this.down_y, this.r + (int) event.getRawX() - this.down_x, this.b + (int) event.getRawY() - this.down_y);
                }

                int num = Utils.dip2px(this.context, 60.0F);
                if (this.l + (int) event.getRawX() - this.down_x > 0 + num) {
                    Log.e("l = " + (this.l + (int) event.getRawX()));
                    Log.e("num = " + num);
                    if (this.onShowCallback != null) {
                        this.onShowCallback.onShowCallback(false);
                    }
                }

                if (this.r + (int) event.getRawX() - this.down_x < this.screenW - num) {
                    Log.e("r = " + (this.r + (int) event.getRawX() - this.down_x));
                    Log.e("screenW = " + (this.screenW - num));
                    if (this.onShowCallback != null) {
                        this.onShowCallback.onShowCallback(false);
                    }
                }
        }

    }

    public void zoom(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            switch (event.getAction() & 255) {
                case 0:
                case 5:
                    this.beforeLenght = this.spacing(event);
                    this.markW = (int) ((double) ((event.getX(0) + event.getX(1) - (float) this.getLeft() - (float) this.getLeft()) * 5.0F) / this.mark);
                    this.markH = (int) ((double) ((event.getY(0) + event.getY(1) - (float) this.getTop() - (float) this.getTop()) * 5.0F) / this.mark);
                    break;
                case 2:
                    if (Math.abs(event.getX(1) - event.getX(0)) <= (float) this.screenW && Math.abs(event.getY(1) - event.getY(0)) <= (float) this.screenH) {
                        double afterLenght = this.spacing(event);
                        if (afterLenght >= 10.0D) {
                            double gapLenght = afterLenght - this.beforeLenght;
                            if (Math.abs(gapLenght) >= 20.0D) {
                                if (gapLenght > 0.0D) {
                                    if (!this.Big()) {
                                        return;
                                    }
                                } else if (!this.Small()) {
                                    return;
                                }

                                this.beforeLenght = afterLenght;
                                Log.e("mark = " + this.mark);
                            }
                        }
                    }
            }

        }
    }

    public void up() {
        int disX = 0;
        int disY = 0;
        if (this.getWidth() >= this.screenW) {
            if (this.getLeft() > 0) {
                disX = this.getLeft();
                this.layout(0, this.getTop(), this.getWidth(), this.getBottom());
            } else if (this.getRight() < this.screenW) {
                disX = this.getWidth() - this.screenW + this.getLeft();
                this.layout(this.screenW - this.getWidth(), this.getTop(), this.screenW, this.getBottom());
            }
        } else if (this.getLeft() < 0) {
            disX = this.getLeft();
            this.layout(0, this.getTop(), this.getWidth(), this.getBottom());
        } else if (this.getRight() > this.screenW) {
            disX = this.getWidth() - this.screenW + this.getLeft();
            this.layout(this.screenW - this.getWidth(), this.getTop(), this.screenW, this.getBottom());
        }

        int trans;
        if (this.getHeight() >= this.screenH) {
            if (this.getTop() > 0) {
                trans = this.getTop();
                this.layout(this.getLeft(), 0, this.getRight(), 0 + this.getHeight());
                disY = trans - this.getTop();
            } else if (this.getBottom() < this.screenH) {
                disY = this.getHeight() - this.screenH + this.getTop();
                this.layout(this.getLeft(), this.screenH - this.getHeight(), this.getRight(), this.screenH);
            }
        } else if (this.getTop() < 0) {
            trans = this.getTop();
            this.layout(this.getLeft(), 0, this.getRight(), 0 + this.getHeight());
            disY = trans - this.getTop();
        } else if (this.getBottom() > this.screenH) {
            disY = this.getHeight() - this.screenH + this.getTop();
            this.layout(this.getLeft(), this.screenH - this.getHeight(), this.getRight(), this.screenH);
        }

        Log.e("disX = " + disX + " disY = " + disY);
        if (disX != 0 || disY != 0) {
            TranslateAnimation trans1 = new TranslateAnimation((float) disX, 0.0F, (float) disY, 0.0F);
            trans1.setDuration(500L);
            this.startAnimation(trans1);
        }

    }

    private double spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return Math.sqrt((double) (x * x + y * y));
    }

    static {
        touch = Touch.DRAG;
    }

    public interface ShowCallback {
        void onShowCallback(boolean var1);
    }

    static enum Touch {
        DRAG, ZOOM;

        private Touch() {
        }
    }
}
