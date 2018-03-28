package com.risenb.witness.utils.newUtils;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.risenb.witness.views.newViews.ColorArcProgressBar;

import java.util.List;

public class Utils {
    private Dialog dialog;
    private static Utils utils = null;
    private int common_waiting_dialog;
    private int tv_dialog;
    private int capb_dialog;
    private int custom_dialog_style;
    private int capbWidth;
    private Application application;
    private ColorArcProgressBar capbDialog;
    private TextView tvDialog;

    public Utils() {
    }

    public static Utils getUtils() {
        if (utils == null) {
            utils = new Utils();
        }

        return utils;
    }

    public void setCapbWidth(int capbWidth) {
        this.capbWidth = this.getDimen(capbWidth);
    }

    public int getCapbWidth() {
        return this.capbWidth;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public void info(int common_waiting_dialog, int capb_dialog, int tv_dialog, int custom_dialog_style) {
        this.common_waiting_dialog = common_waiting_dialog;
        this.capb_dialog = capb_dialog;
        this.tv_dialog = tv_dialog;
        this.custom_dialog_style = custom_dialog_style;
    }

    public boolean isNetworkConnected() {
        if (this.application == null) {
            return false;
        } else {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) this.application.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (mConnectivityManager == null) {
                return false;
            } else {
                NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
                return mNetworkInfo != null && mNetworkInfo.isAvailable();
            }
        }
    }

    public void showProgressDialog(Context context, String text) {
        this.showProgressDialog(context);
        if (this.tvDialog != null) {
            this.tvDialog.setVisibility(View.VISIBLE);
            this.tvDialog.setText(text);
        }
    }

    public void showProgressDialog(Context context) {
        if (LimitConfig.getLimitConfig().isLimit()) {
            if (context != null) {
                if (this.dialog == null || !this.dialog.isShowing()) {
                    this.dialog = new Dialog(context, this.custom_dialog_style);
                    View dialogView = View.inflate(context, this.common_waiting_dialog, null);
                    this.capbDialog = (ColorArcProgressBar) dialogView.findViewById(this.capb_dialog);
                    this.tvDialog = (TextView) dialogView.findViewById(this.tv_dialog);
                    this.dialog.setContentView(dialogView);
                    this.dialog.setCancelable(false);
                    this.dialog.show();
                }
            }
        }
    }

    public void setCurrentValues(int currentValues) {
        if (this.capbDialog != null) {
            this.capbDialog.setCurrentValues((float) currentValues);
        }

        if (this.tvDialog != null) {
            this.tvDialog.setVisibility(View.VISIBLE);
            this.tvDialog.setText(currentValues + "%");
        }
    }

    public boolean isShowDialog() {
        return this.dialog != null && this.dialog.isShowing();
    }

    public void dismissDialog() {
        if (this.dialog != null && this.dialog.isShowing()) {
            this.setCurrentValues(100);
            this.dialog.dismiss();
            this.dialog = null;
            if (this.tvDialog != null) {
                tvDialog.setVisibility(View.GONE);
            }
        }
    }

    public static int dip2px(Context context, float dpValue) {
        if (!LimitConfig.getLimitConfig().isLimit()) {
            return 0;
        } else {
            float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5F);
        }
    }

    public int dip2px(float dpValue) {
        if (!LimitConfig.getLimitConfig().isLimit()) {
            return 0;
        } else {
            float scale = this.application.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5F);
        }
    }

    public int getDimen(Context context, int dimen) {
        return !LimitConfig.getLimitConfig().isLimit() ? 0 : context.getResources().getDimensionPixelOffset(dimen);
    }

    public int getDimen(int dimen) {
        return !LimitConfig.getLimitConfig().isLimit() ? 0 : this.application.getResources().getDimensionPixelOffset(dimen);
    }

    public Bitmap toRoundCorner(Bitmap bitmap) {
        if (!LimitConfig.getLimitConfig().isLimit()) {
            return null;
        } else {
            int pSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
            Bitmap output = Bitmap.createBitmap(pSize, pSize, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, pSize, pSize);
            RectF rectF = new RectF(rect);
            float roundPx = (float) (pSize / 2);
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(-16777216);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            return output;
        }
    }

    private void dialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle("");
        builder.setMessage("");
        builder.setPositiveButton("1", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNeutralButton("2", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        builder.setNegativeButton("3", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    public static boolean isEmpty(List<?> list) {
        return list != null && list.size() > 0;
    }

    public String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception var3) {
            return null;
        }
    }

    public int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception var3) {
            return -1;
        }
    }

    public void setText(TextView tv, String text) {
        try {
            tv.setText(text);
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }

    public void setText(TextView tv, int text) {
        this.setText(tv, String.valueOf(text));
    }

    public void setText(TextView tv, double text) {
        this.setText(tv, String.valueOf(text));
    }

    public void setText(TextView tv, float text) {
        this.setText(tv, String.valueOf(text));
    }

    public String getText(TextView tv) {
        try {
            return tv.getText().toString();
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public void setText(EditText et, String text) {
        try {
            et.setText(text);
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }

    public void setText(EditText et, int text) {
        this.setText(et, String.valueOf(text));
    }

    public void setText(EditText et, double text) {
        this.setText(et, String.valueOf(text));
    }

    public void setText(EditText et, float text) {
        this.setText(et, String.valueOf(text));
    }

    public String getText(EditText et) {
        try {
            return et.getText().toString();
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static int parseInt(String text) {
        try {
            return (int) parseDouble(text);
        } catch (Exception var2) {
            var2.printStackTrace();
            return 0;
        }
    }

    public static double parseDouble(String text) {
        try {
            return Double.parseDouble(text);
        } catch (Exception var2) {
            var2.printStackTrace();
            return 0.0D;
        }
    }

    public static String replaceAll(String str, String regularExpression, String replacement) {
        StringBuffer ret;
        for (ret = new StringBuffer(); str.indexOf(regularExpression) != -1; str = str.substring(str.indexOf(regularExpression) + regularExpression.length())) {
            ret.append(str.substring(0, str.indexOf(regularExpression)));
            ret.append(replacement);
        }

        ret.append(str);
        return ret.toString();
    }
}
