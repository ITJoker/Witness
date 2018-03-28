package com.risenb.witness.ui.vip;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.risenb.witness.R;

public class ImageShower extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageshower);
        ImageView imageshower = findViewById(R.id.imageshower);
        String url = getIntent().getStringExtra("image");
        if (TextUtils.isEmpty(url)) {
            return;
        }
        ImageLoader.getInstance().displayImage(url, imageshower);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }
}
