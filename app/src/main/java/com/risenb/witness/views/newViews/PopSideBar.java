package com.risenb.witness.views.newViews;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.risenb.witness.utils.newUtils.MUtils;
import com.risenb.witness.utils.newUtils.SortUtils;

import static android.view.Window.ID_ANDROID_CONTENT;

public class PopSideBar {
    private PopupWindow popupWindow;
    private View showView;
    private TextView tv_pop_friend_sb;

    public PopSideBar(Activity activity) {
        this.showView = activity.getWindow().getDecorView().findViewById(ID_ANDROID_CONTENT);
        View view = LayoutInflater.from(activity).inflate(SortUtils.getSrtUtils().getPopSideBar(), (ViewGroup) null);
        this.popupWindow = new PopupWindow(view, -1, -1);
        this.showAsDropDown();
        this.tv_pop_friend_sb = (TextView) view.findViewById(SortUtils.getSrtUtils().getTvPop());
        int w = View.MeasureSpec.makeMeasureSpec(0, 0);
        int h = View.MeasureSpec.makeMeasureSpec(0, 0);
        this.tv_pop_friend_sb.measure(w, h);
        this.tv_pop_friend_sb.setWidth(this.tv_pop_friend_sb.getMeasuredHeight());
    }

    public void showAsDropDown() {
        this.popupWindow.showAtLocation(this.showView, 48, 0, 0);
        this.popupWindow.update();
    }

    public void dismiss() {
        this.popupWindow.dismiss();
    }

    public void setText(final String s) {
        MUtils.getMUtils().getHandler().post(new Runnable() {
            public void run() {
                PopSideBar.this.tv_pop_friend_sb.setText(s);
            }
        });
    }
}
