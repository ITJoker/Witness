package com.risenb.witness.views.newViews;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v4.widget.ViewDragHelper.Callback;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class SwipeLayout extends FrameLayout {
    private View contentView;// 内容View
    private View deleteView;// 删除View
    private int contentWidth;// 内容的宽度
    private int contentHeight;// 内容的高度
    private int deleteWidth;// 删除View的宽度
    private ViewDragHelper viewDragHelper;
    private SwipeState mState = SwipeState.Close;// 默认是关闭的状态

    public SwipeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwipeLayout(Context context) {
        super(context);
        init();
    }

    /**
     * 定义滑动状态常量
     */
    enum SwipeState {
        Open, Close;
    }

    /**
     * 初始化方法
     */
    private void init() {
        viewDragHelper = ViewDragHelper.create(this, callback);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = getChildAt(0);
        deleteView = getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        contentWidth = contentView.getMeasuredWidth();
        contentHeight = contentView.getMeasuredHeight();
        deleteWidth = deleteView.getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // super.onLayout(changed, left, top, right, bottom);
        contentView.layout(0, 0, contentWidth, contentHeight);
        deleteView.layout(contentWidth, 0, contentWidth + deleteWidth, contentHeight);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    float downX, downY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                // 1.获取当前的x和y
                float moveX = event.getX();
                float moveY = event.getY();
                // 2.获取水平和垂直方向移动的距离
                float deltaX = moveX - downX;
                float deltaY = moveY - downY;
                // 3.根据deltaX和deltaY判断是偏向于哪个方向
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    // 说明是偏向于水平方向，此时应该请求父View不拦截事件
                    requestDisallowInterceptTouchEvent(true);
                }
                // 4.更新downX和downY
                downX = moveX;
                downY = moveY;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    private Callback callback = new Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == contentView || child == deleteView;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return deleteWidth;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == contentView) {
                if (left > 0)
                    left = 0;// 限制contentView右边
                if (left < -deleteWidth)
                    left = -deleteWidth;// 限制contentView的左边
            } else if (child == deleteView) {
                // 限制deleteView的左边
                if (left < (contentWidth - deleteWidth))
                    left = (contentWidth - deleteWidth);
                if (left > contentWidth)
                    left = contentWidth;// 限制deleteView的右边
            }
            return left;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (changedView == contentView) {
                // 手动改变deleteView的位置
                deleteView.layout(deleteView.getLeft() + dx,
                        deleteView.getTop(), deleteView.getRight() + dx,
                        deleteView.getBottom());
            } else if (changedView == deleteView) {
                // 手动改变contentView的位置
                contentView.layout(contentView.getLeft() + dx, contentView.getTop(), contentView.getRight() + dx, contentView.getBottom());
            }

            // 通过contentView的left来判断状态,并回调对应的方法
            if (contentView.getLeft() == 0 && mState != SwipeState.Close) {
                mState = SwipeState.Close;
                if (listener != null) {
                    listener.onClose(SwipeLayout.this);
                }
            } else if (contentView.getLeft() == -deleteWidth
                    && mState != SwipeState.Open) {
                mState = SwipeState.Open;
                if (listener != null) {
                    listener.onOpen(SwipeLayout.this);
                }
            } else if (contentView.getLeft() > -deleteWidth
                    && contentView.getLeft() < 0) {
                // 现在是处于中间的滑动状态
                if (mState == SwipeState.Close) {
                    if (listener != null) {
                        listener.onStartOpen(SwipeLayout.this);
                    }
                } else if (mState == SwipeState.Open) {
                    if (listener != null) {
                        listener.onStartClose(SwipeLayout.this);
                    }
                }
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (contentView.getLeft() > -deleteWidth / 2) {
                // 应该close
                close();
            } else {
                // 应该open
                open();
            }
        }
    };

    public void close() {
        viewDragHelper.smoothSlideViewTo(contentView, 0, contentView.getTop());
        ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
    }

    public void open() {
        viewDragHelper.smoothSlideViewTo(contentView, -deleteWidth,
                contentView.getTop());
        ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
    }

    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
        }
    }

    public void setOnSwipeStateChangeListener(OnSwipeStateChangeListener listener) {
        this.listener = listener;
    }

    private OnSwipeStateChangeListener listener;

    /**
     * 滑动状态改变的监听器
     */
    public interface OnSwipeStateChangeListener {
        void onClose(SwipeLayout swipeLayout);

        void onOpen(SwipeLayout swipeLayout);

        /**
         * 开始关闭
         */
        void onStartClose(SwipeLayout swipeLayout);

        /**
         * 开始打开
         */
        void onStartOpen(SwipeLayout swipeLayout);
    }
}
