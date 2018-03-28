package com.risenb.witness.ui.tasklist;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnItemTouchListener;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class OnRecyclerItemClickListener implements OnItemTouchListener {
    private final RecyclerView recyclerView;
    private final GestureDetectorCompat mGestureDetector;

    public OnRecyclerItemClickListener(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(), new ItemTouchHelperGestureListener());
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        return false;
    }

    public abstract void onItemClick(ViewHolder viewHolder);

    public abstract void onItemLongClick(ViewHolder viewHolder);

    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {
        public boolean onSingleTapUp(MotionEvent event) throws NullPointerException{
            View child = recyclerView.findChildViewUnder(event.getX(), event.getY());
            if (child != null) {
                ViewHolder viewHolder = recyclerView.getChildViewHolder(child);
                onItemClick(viewHolder);
            }
            return true;
        }

        public void onLongPress(MotionEvent event) {
            View child = recyclerView.findChildViewUnder(event.getX(), event.getY());
            if (child != null) {
                ViewHolder viewHolder = recyclerView.getChildViewHolder(child);
                onItemLongClick(viewHolder);
            }
        }
    }
}
