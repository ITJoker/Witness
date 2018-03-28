package com.risenb.witness.pop;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.risenb.witness.R;
import com.risenb.witness.adapter.PopListAdapter;

import java.util.List;

/**
 * 描述：通用List设置
 */
public class PopList implements OnClickListener {
    private PopupWindow popupWindow;
    private View v;
    private PopListAdapter popListAdapter;
    private ListView lv_pop_list;
    private int id;
    private OnClickListener onClickListener;

    /**
     * @param v       点击的控件
     * @param context
     */
    public PopList(View v, Context context) {
        this.v = v;

        View view = LayoutInflater.from(context).inflate(R.layout.pop_list, null);
        // 设置popwindow弹出大小
        popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        // 弹出popwindow
        context = null;

        RelativeLayout rl_back = (RelativeLayout) view.findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);

        popListAdapter = new PopListAdapter();
        lv_pop_list = (ListView) view.findViewById(R.id.lv_pop_list);
        lv_pop_list.setAdapter(popListAdapter);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setList(List<String> list) {
        popListAdapter.setList(list);
    }

    public void setOnItemClickListener(final AdapterView.OnItemClickListener onItemClickListener) {
        lv_pop_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemClickListener.onItemClick(parent, view, position, id);
                dismiss();
            }
        });
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
        dismiss();
        if (onClickListener != null) {
            onClickListener.onClick(v);
        }
    }

}
