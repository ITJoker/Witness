package com.risenb.witness.utils.newUtils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.risenb.witness.utils.newNetwork.BaseBannerBean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BannerUtils<T extends BaseBannerBean> implements MPagerAdapter.OnMPagerCallBack {
    private Activity activity;
    private ViewPager viewPager;
    private LinearLayout dianGroup;
    private TextView textView;
    private List<T> list;
    private int drawTrue = 0;
    private int drawFalse = 0;
    private int backgroundTrue = 0;
    private int backgroundFalse = 0;
    private int defaultImg;
    private AdapterView.OnItemClickListener onItemClickListener;
    private ViewPager.OnPageChangeListener onPageChangeListener;
    private int dianSize = 10;
    private int dianWidth = 10;
    private int dianHeight = 10;
    private int dianMargin = 5;
    private int maxDianNum = 10;
    private BaseBannerView baseBannerView;
    private int what = 1;
    private boolean adaptat = true;
    private boolean loop = true;
    private int colorTrue = -16711936;
    private int colorFalse = -65536;
    private int marginTrue = 0;
    private int marginFalse = 0;
    private ArrayList<DianView> dianList = new ArrayList();
    private ArrayList<ImageView> imgList = new ArrayList();

    public BannerUtils() {
    }

    public void info() {
        try {
            this.infoThrow();
        } catch (Exception var2) {
            Log.e(var2.getMessage());
        }
    }

    public void infoThrow() throws Exception {
        if (LimitConfig.getLimitConfig().isLimit()) {
            if (this.activity == null) {
                System.out.println("activity is null");
                throw new Exception("activity is null");
            } else if (this.viewPager == null) {
                System.out.println("viewPager is null");
                throw new Exception("viewPager is null");
            } else if (this.list == null) {
                System.out.println("list is null");
                throw new Exception("list is null");
            } else if (this.defaultImg == 0) {
                System.out.println("defaultImg is null");
                throw new Exception("defaultImg is null");
            } else {
                if (this.list.size() == 0) {
                    this.viewPager.setVisibility(View.GONE);
                }

                if (this.dianGroup != null) {
                    this.dianGroup.removeAllViews();
                    this.dianList.clear();
                }

                int width;
                for (width = 0; width < this.list.size() && width < this.maxDianNum; ++width) {
                    LinearLayout.LayoutParams adapter;
                    if (this.drawTrue == 0) {
                        DianView height = new DianView(this.activity);
                        height.setDianSize(this.dianSize);
                        height.setMarginTrue(this.marginTrue);
                        height.setMarginFalse(this.marginFalse);
                        height.setColorTrue(this.colorTrue);
                        height.setColorFalse(this.colorFalse);
                        adapter = new LinearLayout.LayoutParams(this.dianSize, this.dianSize);
                        adapter.setMargins(this.dianMargin, this.dianMargin, this.dianMargin, this.dianMargin);
                        height.setLayoutParams(adapter);
                        this.dianList.add(height);
                        if (this.dianGroup != null) {
                            this.dianGroup.addView(height);
                            height.invalidate();
                        }
                    } else {
                        ImageView var7 = new ImageView(this.activity);
                        var7.setScaleType(ImageView.ScaleType.FIT_XY);
                        adapter = new LinearLayout.LayoutParams(this.dianWidth, this.dianHeight);
                        adapter.setMargins(this.dianMargin, this.dianMargin, this.dianMargin, this.dianMargin);
                        var7.setLayoutParams(adapter);
                        this.imgList.add(var7);
                        if (this.dianGroup != null) {
                            this.dianGroup.addView(var7);
                        }
                    }
                }

                this.dianSelect(0);
                this.imgSelect(0);
                width = this.viewPager.getWidth();
                int var8 = this.viewPager.getHeight();
                Log.e("h=" + var8 + ",w=" + width);
                MPagerAdapter var9 = new MPagerAdapter(this.activity, this.list, this, this.defaultImg, this.baseBannerView, this.onItemClickListener, width, var8, this.loop);
                this.viewPager.setAdapter(var9);
                this.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    public void onPageSelected(int position) {
                        if (BannerUtils.this.list.size() > 1 && BannerUtils.this.loop) {
                            --position;
                            position = position >= 0 ? position : BannerUtils.this.list.size() - 1;
                            position = position < BannerUtils.this.list.size() ? position : 0;
                        }

                        try {
                            BannerUtils.this.dianSelect(position % BannerUtils.this.list.size());
                            BannerUtils.this.imgSelect(position % BannerUtils.this.list.size());
                            BannerUtils.this.backgroundSelect(position % BannerUtils.this.list.size());
                        } catch (Exception var3) {
                            ;
                        }

                        if (BannerUtils.this.onPageChangeListener != null) {
                            BannerUtils.this.onPageChangeListener.onPageSelected(position % BannerUtils.this.list.size());
                        }

                        if (BannerUtils.this.textView != null) {
                            BannerUtils.this.textView.setText(BannerUtils.this.getItem(position % BannerUtils.this.list.size()).getBannerBeanTitle());
                        }

                        if (BannerUtils.this.list.size() > 1 && BannerUtils.this.loop) {
                            if (BannerUtils.this.viewPager.getCurrentItem() == BannerUtils.this.list.size() + 1) {
                                MUtils.getMUtils().getHandler().postDelayed(new Runnable() {
                                    public void run() {
                                        BannerUtils.this.viewPager.setCurrentItem(1, false);
                                    }
                                }, 500L);
                            } else if (BannerUtils.this.viewPager.getCurrentItem() == 0) {
                                MUtils.getMUtils().getHandler().postDelayed(new Runnable() {
                                    public void run() {
                                        BannerUtils.this.viewPager.setCurrentItem(BannerUtils.this.list.size(), false);
                                    }
                                }, 500L);
                            }
                        }

                    }

                    public void onPageScrolled(int arg0, float arg1, int arg2) {
                        if (BannerUtils.this.onPageChangeListener != null) {
                            BannerUtils.this.onPageChangeListener.onPageScrolled(arg0, arg1, arg2);
                        }

                    }

                    public void onPageScrollStateChanged(int arg0) {
                        if (BannerUtils.this.onPageChangeListener != null) {
                            BannerUtils.this.onPageChangeListener.onPageScrollStateChanged(arg0);
                        }

                    }
                });
                if (this.list.size() > 1 && this.loop) {
                    this.viewPager.setCurrentItem(1);
                } else {
                    this.viewPager.setCurrentItem(0);
                }

                try {
                    Field e = ViewPager.class.getDeclaredField("mScroller");
                    e.setAccessible(true);
                    FixedSpeedScroller mScroller = new FixedSpeedScroller(this.viewPager.getContext(), new AccelerateInterpolator());
                    e.set(this.viewPager, mScroller);
                    mScroller.setmDuration(500);
                } catch (Exception var6) {
                    var6.printStackTrace();
                }

            }
        }
    }

    public T getItem(int position) {
        return this.list.get(position);
    }

    public void onMPagerCallBack(Bitmap bitmap) {
        if (this.adaptat) {
            if (bitmap != null) {
                DisplayMetrics outMetrics = new DisplayMetrics();
                this.activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
                int screenWidth = outMetrics.widthPixels;
                int height = screenWidth * bitmap.getHeight() / bitmap.getWidth();
                android.view.ViewGroup.LayoutParams layoutParams = this.viewPager.getLayoutParams();
                layoutParams.height = height;
                layoutParams.width = screenWidth;
                this.viewPager.setLayoutParams(layoutParams);
            }
        }
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public void setDianGroup(LinearLayout dianGroup) {
        this.dianGroup = dianGroup;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public void setDrawTrue(int drawTrue) {
        this.drawTrue = drawTrue;
    }

    public void setDrawFalse(int drawFalse) {
        this.drawFalse = drawFalse;
    }

    public void setDefaultImg(int defaultImg) {
        this.defaultImg = defaultImg;
    }

    public void setDianSize(int dianSize) {
        this.dianSize = dianSize;
    }

    public void setDianMargin(int dianMargin) {
        this.dianMargin = dianMargin;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    public void setBaseBannerView(BaseBannerView baseBannerView) {
        this.baseBannerView = baseBannerView;
    }

    public void setAdaptat(boolean adaptat) {
        this.adaptat = adaptat;
    }

    public void setMaxDianNum(int maxDianNum) {
        this.maxDianNum = maxDianNum;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public void setColorTrue(int colorTrue) {
        this.colorTrue = colorTrue;
    }

    public void setColorFalse(int colorFalse) {
        this.colorFalse = colorFalse;
    }

    public void setMarginTrue(int marginTrue) {
        this.marginTrue = marginTrue;
    }

    public void setMarginFalse(int marginFalse) {
        this.marginFalse = marginFalse;
    }

    public void setBackgroundTrue(int backgroundTrue) {
        this.backgroundTrue = backgroundTrue;
    }

    public void setBackgroundFalse(int backgroundFalse) {
        this.backgroundFalse = backgroundFalse;
    }

    public void start() {
        this.start(++this.what, 5000);
    }

    public void start(int time) {
        this.start(++this.what, time);
    }

    private void dianSelect(int idx) {
        if (this.dianList.size() > 0) {
            for (int i = 0; i < this.dianList.size(); ++i) {
                ((DianView) this.dianList.get(i)).setChecked(false);
            }

            ((DianView) this.dianList.get(idx)).setChecked(true);
        }

    }

    private void imgSelect(int idx) {
        if (this.drawTrue != 0) {
            if (this.imgList.size() > 0) {
                for (int i = 0; i < this.imgList.size(); ++i) {
                    ((ImageView) this.imgList.get(i)).setImageResource(this.drawFalse);
                }

                ((ImageView) this.imgList.get(idx)).setImageResource(this.drawTrue);
            }

        }
    }

    private void backgroundSelect(int idx) {
        if (this.backgroundTrue != 0) {
            if (this.imgList.size() > 0) {
                for (int i = 0; i < this.imgList.size(); ++i) {
                    ((ImageView) this.imgList.get(i)).setBackgroundResource(this.backgroundFalse);
                }

                ((ImageView) this.imgList.get(idx)).setBackgroundResource(this.backgroundTrue);
            }

        }
    }

    public void start(final int what, final int time) {
        if (this.list != null && this.list.size() >= 2) {
            MUtils.getMUtils().getHandler().postDelayed(new Runnable() {
                public void run() {
                    if (BannerUtils.this.what == what) {
                        BannerUtils.this.viewPager.setCurrentItem(BannerUtils.this.viewPager.getCurrentItem() + 1);
                        MUtils.getMUtils().getHandler().postDelayed(this, (long) time);
                    }
                }
            }, (long) time);
        }
    }

    public void cancel() {
        ++this.what;
    }
}
