package com.risenb.witness.ui.vip.Wallet;

import com.lidroid.xutils.view.annotation.ContentView;
import com.risenb.witness.R;
import com.risenb.witness.ui.BaseUI;
import com.risenb.witness.utils.newUtils.UIManager;

@ContentView(R.layout.moneyrule)
public class MoneyRuleUI extends BaseUI {
    @Override
    protected void back() {
        UIManager.getInstance().popActivity(getClass());
    }

    @Override
    protected void exit() {
        back();
    }

    @Override
    protected void setControlBasis() {
        setTitle("取现规则");
    }

    @Override
    protected void prepareData() {

    }

    @Override
    public void onLoadOver() {

    }
}
