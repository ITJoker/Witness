package com.risenb.witness.utils.newUtils;

import android.view.MotionEvent;
import android.view.View;

import com.risenb.witness.views.newViews.ShowImageView;

public class ShowUtils {
    private static ShowUtils showUtils;
    private boolean blShow;
    private MotionEvent downEvent;
    private float rawX = 0.0F;
    private float rawY = 0.0F;
    private long exitTime = 0L;

    public static ShowUtils getShowUtils() {
        if(showUtils == null) {
            showUtils = new ShowUtils();
        }

        return showUtils;
    }

    public void setOnTouchListener(final ShowBean bean) {
        final int num = Utils.getUtils().dip2px(3.0F);
        bean.getBtn().setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, final MotionEvent event) {
                switch(event.getAction() & 255) {
                    case 0:
                        ShowUtils.this.downEvent = event;
                        ShowUtils.this.rawX = ShowUtils.this.downEvent.getRawX();
                        ShowUtils.this.rawY = ShowUtils.this.downEvent.getRawY();
                        if(bean.getShowImageView().isFrameFit()) {
                            ShowUtils.this.blShow = false;
                        } else {
                            ShowUtils.this.blShow = true;
                        }
                        break;
                    case 1:
                        if(System.currentTimeMillis() - ShowUtils.this.exitTime > 500L) {
                            if(Math.abs(ShowUtils.this.rawX - event.getRawX()) <= (float)num && Math.abs(ShowUtils.this.rawY - event.getRawY()) <= (float)num) {
                                ShowUtils.this.exitTime = System.currentTimeMillis();
                            } else {
                                ShowUtils.this.exitTime = 0L;
                            }
                        } else if(bean.getShowImageView().isFrameFit()) {
                            for(int i = 0; i < 10; ++i) {
                                bean.getShowImageView().setFrameBig(event);
                            }

                            bean.getShowImageView().up();
                        } else {
                            bean.getShowImageView().setFrameFitView();
                        }
                        break;
                    case 5:
                        ShowUtils.this.blShow = true;
                        ShowUtils.this.exitTime = 0L;
                }

                if(ShowUtils.this.blShow) {
                    if((event.getAction() & 255) == 5) {
                        bean.getShowImageView().onTouchEvent(ShowUtils.this.downEvent);
                    }

                    bean.getShowImageView().onTouchEvent(event);
                    if((event.getAction() & 255) == 5) {
                        event.setAction(1);
                        bean.getViewPager().onTouchEvent(event);
                    }
                } else {
                    bean.getViewPager().onTouchEvent(event);
                }

                bean.getShowImageView().setOnShowCallback(new ShowImageView.ShowCallback() {
                    public void onShowCallback(boolean bl) {
                        ShowUtils.this.blShow = false;
                        event.setAction(1);
                        bean.getShowImageView().onTouchEvent(event);
                        event.setAction(0);
                        bean.getViewPager().onTouchEvent(event);
                    }
                });
                return false;
            }
        });
    }
}
