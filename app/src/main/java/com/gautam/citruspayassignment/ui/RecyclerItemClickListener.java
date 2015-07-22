package com.gautam.citruspayassignment.ui;

/**
 * Created by Gautam on 05/07/15.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    GestureDetector mGestureDetector;

    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View mChildView = view.findChildViewUnder(e.getX(), e.getY());
        if (mChildView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(mChildView, view.getChildPosition(mChildView));
        }
        return false;
    }

    @Override public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }

    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        // do nothing
    }
}