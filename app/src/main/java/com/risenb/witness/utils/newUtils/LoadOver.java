package com.risenb.witness.utils.newUtils;

import android.app.Activity;
import android.view.View;
import android.view.ViewTreeObserver;

import static android.view.Window.ID_ANDROID_CONTENT;

public class LoadOver {
    private boolean isLoadOver;

    public LoadOver(Activity activity, final OnLoadOver onLoadOver) {
        if(LimitConfig.getLimitConfig().isLimit()) {
            this.isLoadOver = false;
            ViewTreeObserver vto = activity.getWindow().getDecorView().findViewById(ID_ANDROID_CONTENT).getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    if(!LoadOver.this.isLoadOver) {
                        LoadOver.this.isLoadOver = true;
                        MUtils.getMUtils().getHandler().post(new Runnable() {
                            public void run() {
                                if(onLoadOver != null) {
                                    onLoadOver.onLoadOver();
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    public LoadOver(View view, final OnLoadOver onLoadOver) {
        if(LimitConfig.getLimitConfig().isLimit()) {
            this.isLoadOver = false;
            ViewTreeObserver vto = view.findViewById(ID_ANDROID_CONTENT).getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    if(!LoadOver.this.isLoadOver) {
                        LoadOver.this.isLoadOver = true;
                        MUtils.getMUtils().getHandler().post(new Runnable() {
                            public void run() {
                                if(onLoadOver != null) {
                                    onLoadOver.onLoadOver();
                                }
                            }
                        });
                    }
                }
            });
        }
    }
}
