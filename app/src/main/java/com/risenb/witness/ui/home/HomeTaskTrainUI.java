package com.risenb.witness.ui.home;

import android.view.View;
import android.widget.RelativeLayout;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.risenb.witness.R;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.utils.newUtils.UIManager;

@ContentView(R.layout.hometasktrain)
public class HomeTaskTrainUI extends BaseUI {
    @ViewInject(R.id.back)
    private RelativeLayout back;

    @Override
    protected void back() {
        UIManager.getInstance().popActivity(getClass());
    }

    @Override
    protected void setControlBasis() {
        setTitle("培训");
    }

    @Override
    protected void prepareData() {
        // Intent intent = new Intent(getActivity(), ZUI.class);
        // startActivity(intent);
    }

    @Override
    public void onLoadOver() {

    }

    @OnClick(R.id.back)
    private void test(View v) {

    }
}
