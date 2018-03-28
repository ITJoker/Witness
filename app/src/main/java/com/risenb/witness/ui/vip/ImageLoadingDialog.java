package com.risenb.witness.ui.vip;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.risenb.witness.R;

public class ImageLoadingDialog extends Dialog {
    public ImageLoadingDialog(Context context) {
        super(context, R.style.ImageloadingDialogStyle);
    }

    private ImageLoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_imageloading);
    }
}
