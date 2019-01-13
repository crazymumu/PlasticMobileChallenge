package com.example.mswu.plasticmobilechallenge;

import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements DragViewListener.Event {
    private ClockFragment mClockFragment;
    private DrawerFragment mDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mClockFragment = (ClockFragment) getSupportFragmentManager().findFragmentById(R.id.clock_fragment);
        mDrawerFragment = (DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.drawer_fragment);
    }

    @Override
    public void onBegin(View view) {
        mDrawerFragment.setOpen(true);
    }

    @Override
    public void onMove(View view) {

    }

    @Override
    public void onEnd(View view) {
        final boolean hit = hitTest(view, mDrawerFragment.getView());
        final int clockFragmentBottom = (int) (mClockFragment.getView().getY()) + mClockFragment.getView().getHeight();

        if (!hit || mDrawerFragment.getView().getY() + mDrawerFragment.getPaddingHeight() > clockFragmentBottom) {
            mDrawerFragment.setOpen(false);
        }
    }

    private boolean hitTest(View v1, View v2) {
        Rect r1 = new Rect(v1.getLeft(), v1.getTop(), v1.getRight(), v1.getBottom());
        Rect r2 = new Rect(v2.getLeft(), v2.getTop(), v2.getRight(), v2.getBottom());
        return r1.intersect(r2);
    }
}
