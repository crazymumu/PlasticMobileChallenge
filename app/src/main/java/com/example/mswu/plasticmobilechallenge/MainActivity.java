package com.example.mswu.plasticmobilechallenge;

import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements DragViewListener.Event {
    private DrawerFragment mDrawerFragment;
    private boolean drawerOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerFragment = (DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.drawer_fragment);
    }

    @Override
    public void onBegin(View view) {

    }

    @Override
    public void onMove(View view) {
        boolean hit = hitTest(view, mDrawerFragment.getView());

        // Only update drawer when values are different to avoid unnecessary computation.
        if (hit != drawerOpened) {
            drawerOpened = hit;
            mDrawerFragment.setOpen(drawerOpened);
        }
    }

    @Override
    public void onEnd(View view) {

    }

    private boolean hitTest(View v1, View v2) {
        Rect r1 = new Rect(v1.getLeft(), v1.getTop(), v1.getRight(), v1.getBottom());
        Rect r2 = new Rect(v2.getLeft(), v2.getTop(), v2.getRight(), v2.getBottom());
        return r1.intersect(r2);
    }
}
