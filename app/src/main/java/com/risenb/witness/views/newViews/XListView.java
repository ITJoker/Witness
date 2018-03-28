package com.risenb.witness.views.newViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.risenb.witness.utils.newUtils.MUtils;

import java.util.Calendar;
import java.util.List;

public class XListView extends ListView implements AbsListView.OnScrollListener {
    private int page = 1;
    private int maxPage = 10000;
    private float mLastY = -1.0F;
    private Scroller mScroller;
    private OnScrollListener mScrollListener;
    private XListView.IXListViewListener mListViewListener;
    private XListViewHeader mHeaderView;
    private RelativeLayout mHeaderViewContent;
    private TextView mHeaderTimeView;
    private int mHeaderViewHeight;
    private boolean mEnablePullRefresh = true;
    private boolean mPullRefreshing = false;
    private boolean mEnablePullLoad;
    private boolean mIsFooterReady = false;
    private int mScrollBack;
    private static final int SCROLLBACK_HEADER = 0;
    private static final int SCROLL_DURATION = 400;
    private static final float OFFSET_RADIO = 1.8F;
    private boolean blScroll = false;
    private Calendar calendar;
    private List<?> list;

    public XListView(Context context) {
        super(context);
        this.initWithContext(context);
    }

    public XListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initWithContext(context);
    }

    public XListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.initWithContext(context);
    }

    private void initWithContext(Context context) {
        this.mScroller = new Scroller(context, new DecelerateInterpolator());
        super.setOnScrollListener(this);
        this.mHeaderView = new XListViewHeader(context);
        this.mHeaderViewContent = (RelativeLayout)this.mHeaderView.findViewById(XListUtils.getXListUtils().xlistview_header_content);
        this.mHeaderTimeView = (TextView)this.mHeaderView.findViewById(XListUtils.getXListUtils().xlistview_header_time);
        this.addHeaderView(this.mHeaderView);
        this.mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                XListView.this.mHeaderViewHeight = XListView.this.mHeaderViewContent.getHeight();
                XListView.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    public void setAdapter(ListAdapter adapter) {
        if(!this.mIsFooterReady) {
            this.mIsFooterReady = true;
            if(this.mEnablePullLoad) {

            }
        }

        super.setAdapter(adapter);
    }

    public void setPullRefreshEnable(boolean enable) {
        this.mEnablePullRefresh = enable;
        if(!this.mEnablePullRefresh) {
            this.mHeaderViewContent.setVisibility(INVISIBLE);
        } else {
            this.mHeaderViewContent.setVisibility(VISIBLE);
        }

    }

    public void stopRefresh() {
        if(this.mPullRefreshing) {
            this.mPullRefreshing = false;
            this.resetHeaderHeight();
            this.setTime();
            MUtils.getMUtils().getHandler().postDelayed(new Runnable() {
                public void run() {
                    XListView.this.mHeaderTimeView.setVisibility(VISIBLE);
                }
            }, 500L);
        }

    }

    private void invokeOnScrolling() {
        if(this.mScrollListener instanceof XListView.OnXScrollListener) {
            XListView.OnXScrollListener l = (XListView.OnXScrollListener)this.mScrollListener;
            l.onXScrolling(this);
        }

    }

    private void updateHeaderHeight(float delta) {
        this.mHeaderView.setVisiableHeight((int)delta + this.mHeaderView.getVisiableHeight());
        if(this.mEnablePullRefresh && !this.mPullRefreshing) {
            if(this.mHeaderView.getVisiableHeight() > this.mHeaderViewHeight) {
                this.mHeaderView.setState(1);
            } else {
                this.mHeaderView.setState(0);
            }
        }

        this.setSelection(0);
    }

    private void resetHeaderHeight() {
        int height = this.mHeaderView.getVisiableHeight();
        if(height != 0) {
            if(!this.mPullRefreshing || height > this.mHeaderViewHeight) {
                int finalHeight = 0;
                if(this.mPullRefreshing && height > this.mHeaderViewHeight) {
                    finalHeight = this.mHeaderViewHeight;
                }

                this.mScrollBack = 0;
                this.mScroller.startScroll(0, height, 0, finalHeight - height, 400);
                this.invalidate();
            }
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if(this.mLastY == -1.0F) {
            this.mLastY = ev.getRawY();
        }

        switch(ev.getAction()) {
            case 0:
                this.mLastY = ev.getRawY();
                break;
            case 2:
                float deltaY = ev.getRawY() - this.mLastY;
                this.mLastY = ev.getRawY();
                if(this.getFirstVisiblePosition() == 0 && (this.mHeaderView.getVisiableHeight() > 0 || deltaY > 0.0F)) {
                    this.updateHeaderHeight(deltaY / 1.8F);
                    this.invokeOnScrolling();
                }
                break;
            default:
                this.mLastY = -1.0F;
                if(this.getFirstVisiblePosition() == 0) {
                    if(this.mEnablePullRefresh && this.mHeaderView.getVisiableHeight() > this.mHeaderViewHeight) {
                        this.mPullRefreshing = true;
                        this.mHeaderView.setState(2);
                        this.page = 1;
                        this.maxPage = 10000;
                        if(this.mListViewListener != null) {
                            this.mListViewListener.onLoad(this.page);
                        }

                        this.stopRefresh();
                    }

                    this.resetHeaderHeight();
                }
        }

        return super.onTouchEvent(ev);
    }

    public void computeScroll() {
        if(this.mScroller.computeScrollOffset()) {
            if(this.mScrollBack == 0) {
                this.mHeaderView.setVisiableHeight(this.mScroller.getCurrY());
            }

            this.postInvalidate();
            this.invokeOnScrolling();
        }

        super.computeScroll();
    }

    public void setOnScrollListener(OnScrollListener l) {
        this.mScrollListener = l;
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(this.mScrollListener != null) {
            this.mScrollListener.onScrollStateChanged(view, scrollState);
        }

    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(this.mScrollListener != null) {
            this.mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }

        if(totalItemCount > visibleItemCount) {
            if(firstVisibleItem + visibleItemCount == totalItemCount) {
                if(!this.blScroll) {
                    this.blScroll = true;
                    ++this.page;
                    if(this.page > this.maxPage) {
                        return;
                    }

                    if(this.mListViewListener != null && (this.list == null || this.list.size() > 0)) {
                        this.mListViewListener.onLoad(this.page);
                    }
                }
            } else {
                this.blScroll = false;
            }
        }

    }

    public void setList(List<?> list) {
        this.list = list;
    }

    public void setXListViewListener(XListView.IXListViewListener l) {
        this.mListViewListener = l;
        this.setTime();
        this.onLoad();
    }

    private void setTime() {
        MUtils.getMUtils().getHandler().postDelayed(new Runnable() {
            public void run() {
                XListView.this.calendar = Calendar.getInstance();
                int hour = XListView.this.calendar.get(Calendar.HOUR_OF_DAY);
                int minute = XListView.this.calendar.get(Calendar.MINUTE);
                int second = XListView.this.calendar.get(Calendar.SECOND);
                String time = String.format("%02d", new Object[]{Integer.valueOf(hour)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minute)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(second)});
                XListView.this.mHeaderTimeView.setText("上次更新时间：" + time);
            }
        }, 500L);
    }

    public void onLoad() {
        this.page = 1;
        this.maxPage = 10000;
        this.mListViewListener.onLoad(this.page);
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public interface IXListViewListener {
        void onLoad(int var1);
    }

    interface OnXScrollListener extends OnScrollListener {
        void onXScrolling(View var1);
    }
}
