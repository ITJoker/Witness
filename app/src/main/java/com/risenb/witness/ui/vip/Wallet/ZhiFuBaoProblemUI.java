package com.risenb.witness.ui.vip.Wallet;

import com.lidroid.xutils.view.annotation.ContentView;
import com.risenb.witness.R;
import com.risenb.witness.ui.BaseUI;

@ContentView(R.layout.zhifubaoproblem)
public class ZhiFuBaoProblemUI extends BaseUI {
    @Override
    protected void back() {
        finish();
    }

    @Override
    protected void exit() {
        back();
    }

    @Override
    protected void setControlBasis() {
        setTitle("常见问题");
    }

    @Override
    protected void prepareData() {

    }

    @Override
    public void onLoadOver() {

    }
}
