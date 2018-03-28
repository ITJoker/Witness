package com.risenb.witness.utils.newUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class DianView extends View {
    private int colorTrue = -16711936;
    private int colorFalse = -65536;
    private int marginTrue = 0;
    private int marginFalse = 0;
    private boolean checked;
    private int dianSize = 10;

    public DianView(Context context) {
        super(context);
    }

    @SuppressLint({"DrawAllocation"})
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int num;
        Paint paint;
        if (this.checked) {
            num = this.dianSize - this.marginTrue;
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(this.colorTrue);
            canvas.drawCircle((float) this.dianSize, (float) this.dianSize, (float) num, paint);
        } else {
            num = this.dianSize - this.marginFalse;
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(this.colorFalse);
            canvas.drawCircle((float) this.dianSize, (float) this.dianSize, (float) num, paint);
        }

    }

    public int getColorTrue() {
        return this.colorTrue;
    }

    public void setColorTrue(int colorTrue) {
        this.colorTrue = colorTrue;
    }

    public int getColorFalse() {
        return this.colorFalse;
    }

    public void setColorFalse(int colorFalse) {
        this.colorFalse = colorFalse;
    }

    public boolean isChecked() {
        return this.checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        this.invalidate();
    }

    public int getDianSize() {
        return this.dianSize;
    }

    public void setDianSize(int dianSize) {
        this.dianSize = dianSize / 2;
    }

    public int getMarginTrue() {
        return this.marginTrue;
    }

    public void setMarginTrue(int marginTrue) {
        this.marginTrue = marginTrue;
    }

    public int getMarginFalse() {
        return this.marginFalse;
    }

    public void setMarginFalse(int marginFalse) {
        this.marginFalse = marginFalse;
    }
}
