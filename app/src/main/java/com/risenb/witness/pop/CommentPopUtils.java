package com.risenb.witness.pop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

/**
 * pop窗口基础类
 */
@SuppressLint("NewApi")
public abstract class CommentPopUtils implements OnDismissListener {
    private View v;
    protected PopupWindow popupWindow;
    protected OnClickListener onClickListener;
    protected OnItemClickListener itemClickListener;
    protected OnDismissListener onDismissListener;

    public CommentPopUtils(View v, Context context, int layout) {
        super();
        this.v = v;
        View view = LayoutInflater.from(context).inflate(layout, null);
        /*popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);*/
        popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        popupWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow.dismiss();
                if (onDismissListener != null) {
                    onDismissListener.onDismiss();
                }
            }
        });
        initLayout(view, context);
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public abstract void initLayout(View v, Context context);

    /**
     * 下拉式 弹出 pop菜单 parent 右下角
     */
    @SuppressWarnings("deprecation")
    public void showAsDropDown(View v) {
        // 这个是为了点击“返回Back”也能使其消失
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(v);
        // 刷新状态
        popupWindow.update();
    }

    @SuppressWarnings("deprecation")
    public void showPopUp(View v) {
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        int[] location = new int[2];
        v.getLocationOnScreen(location);

        popupWindow.showAtLocation(v, Gravity.BOTTOM, 20, 20);
        popupWindow.update();
    }

    /**
     * 下拉式 弹出 pop菜单 parent 右下角
     */
    @SuppressWarnings("deprecation")
    public void showAsDropDownInstance() {
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        // 使其聚集
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true); // 设置PopupWindow可触摸
        // 设置弹出位置
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        // 刷新状态
        popupWindow.update();
    }

    /**
     * 下拉式 弹出 pop菜单 parent 右下角
     */
    @SuppressWarnings("deprecation")
    public void showAtLocation() {
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置弹出位置
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        // 刷新状态
        popupWindow.update();
    }

    /**
     * 隐藏菜单
     */
    public void dismiss() {
        popupWindow.dismiss();
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void onDismiss() {

    }
}
