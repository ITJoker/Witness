package com.risenb.witness.pop;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.risenb.witness.R;

/**
 * 描述：通用设置
 */
public class PopSet implements OnClickListener {
    private PopupWindow popupWindow;
    private View v;
    /**
     * 回调接口
     */
    private OnClickListener onClickListener;

    /**
     * @param v       点击的控件
     * @param context
     * @param title   要加载的数据
     */
    public PopSet(View v, Context context, String[] title) {
        this.v = v;

        View view = LayoutInflater.from(context).inflate(R.layout.pop_set, null);
        // 设置popwindow弹出大小
        popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        // 弹出popwindow
        context = null;

        RelativeLayout rl_back = (RelativeLayout) view.findViewById(R.id.rl_back);
        Button btn_pop_set_1 = (Button) view.findViewById(R.id.btn_pop_set_1);
        Button btn_pop_set_2 = (Button) view.findViewById(R.id.btn_pop_set_2);
        Button btn_pop_set_3 = (Button) view.findViewById(R.id.btn_pop_set_3);
        Button btn_pop_set_4 = (Button) view.findViewById(R.id.btn_pop_set_4);
        Button btn_pop_set_5 = (Button) view.findViewById(R.id.btn_pop_set_5);

        rl_back.setOnClickListener(this);
        btn_pop_set_1.setOnClickListener(this);
        btn_pop_set_2.setOnClickListener(this);
        btn_pop_set_3.setOnClickListener(this);
        btn_pop_set_4.setOnClickListener(this);
        btn_pop_set_5.setOnClickListener(this);

        Button[] bt = {btn_pop_set_1, btn_pop_set_2, btn_pop_set_3, btn_pop_set_4, btn_pop_set_5};

        for (int i = 0; i < bt.length; i++) {
            if (title.length > i)
                bt[i].setText(title[i]);
            else
                bt[i].setVisibility(View.GONE);
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    /**
     * 下拉式 弹出 pop菜单 parent 右下角
     */
    @SuppressWarnings("deprecation")
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
