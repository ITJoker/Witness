package com.risenb.witness.pop;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.risenb.witness.R;

/**
 * 描述：通用设置
 */
public class PopIco implements OnClickListener {
    private PopupWindow popupWindow;
    private View v;
    /**
     * 回调接口
     */
    private OnClickListener onClickListener;

    /**
     * @param v       点击的控件
     * @param context
     */
    public PopIco(View v, Context context) {
        this.v = v;

        View view = LayoutInflater.from(context).inflate(R.layout.pop_ico, null);
        // 设置popwindow弹出大小
        /*popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);*/
        popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        // 弹出popwindow
        context = null;

        RelativeLayout rl_pop_ico_back = view.findViewById(R.id.rl_pop_ico_back);
        TextView tv_pop_ico_camera = view.findViewById(R.id.tv_pop_ico_camera);
        TextView tv_pop_ico_photo = view.findViewById(R.id.tv_pop_ico_photo);
        TextView tv_pop_ico_cancel = view.findViewById(R.id.tv_pop_ico_cancel);

        rl_pop_ico_back.setOnClickListener(this);
        tv_pop_ico_camera.setOnClickListener(this);
        tv_pop_ico_photo.setOnClickListener(this);
        tv_pop_ico_cancel.setOnClickListener(this);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    /**
     * 下拉式 弹出 pop菜单 parent 右下角
     */
    public void showAsDropDown() {
        // 这个是为了点击“返回Back”也能使其消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置弹出位置
        popupWindow.showAtLocation(v, Gravity.TOP, 0, 0);
        // 刷新状态
        popupWindow.update();
    }

    /**
     * 隐藏菜单
     */
    public void dismiss() {
        popupWindow.dismiss();
    }

    @Override
    public void onClick(View v) {
        popupWindow.dismiss();
        if (v.getId() == R.id.rl_back)
            return;
        if (onClickListener != null) {
            onClickListener.onClick(v);
        }
    }
}
