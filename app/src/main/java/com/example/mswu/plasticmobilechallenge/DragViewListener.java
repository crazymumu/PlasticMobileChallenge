package com.example.mswu.plasticmobilechallenge;

import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;

/**
 * Handle drag gesture.
 */
public class DragViewListener implements View.OnTouchListener {
    public interface Event {
        void onBegin(View view);
        void onMove(View view);
        void onEnd(View view);
    }

    private float mX, mY;
    private WeakReference<Event> mDelegate;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                final FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) v.getLayoutParams();
                mX = event.getRawX() - lParams.leftMargin;
                mY = event.getRawY() - lParams.topMargin;

                if (mDelegate != null) {
                    mDelegate.get().onBegin(v);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mDelegate != null) {
                    mDelegate.get().onEnd(v);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) v.getLayoutParams();
                layoutParams.leftMargin = (int) (event.getRawX() - mX);
                layoutParams.topMargin = (int) (event.getRawY() - mY);
                v.setLayoutParams(layoutParams);
                v.invalidate();

                if (mDelegate != null) {
                    mDelegate.get().onMove(v);
                }
                break;
            default:
                return false;
        }

        return true;
    }

    public void setDelegate(Event delegate) {
        mDelegate = new WeakReference<>(delegate);
    }
}
