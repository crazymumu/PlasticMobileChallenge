package com.example.mswu.plasticmobilechallenge;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DrawerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DrawerFragment extends Fragment {
    // Arguments
    private static final String sArgOpen = "sArgOpen";
    private static final String sArgHeight = "sArgHeight";

    private boolean mOpened = false;
    private int mHeight = 150;
    private int mPaddingHeight = Integer.MIN_VALUE;

    public DrawerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param open State of the drawer.
     * @param height Drawer height.
     * @return A new instance of fragment DrawerFragment.
     */
    public static DrawerFragment newInstance(boolean open, int height) {
        DrawerFragment fragment = new DrawerFragment();
        Bundle args = new Bundle();
        args.putBoolean(sArgOpen, open);
        args.putInt(sArgHeight, height);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mOpened = getArguments().getBoolean(sArgOpen);
            mHeight = getArguments().getInt(sArgHeight);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_drawer, container, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleOpen();
            }
        });

        return view;
    }

    public int getPaddingHeight() {
        return mPaddingHeight;
    }

    public void toggleOpen() {
        setOpen(!mOpened);
    }

    public void setOpen(boolean open) {
        mOpened = open;
        final ViewGroup.LayoutParams params = getView().getLayoutParams();

        if (mPaddingHeight == Integer.MIN_VALUE) {
            mPaddingHeight = params.height;
        }

        int height;

        if (open) {
            height = dpToPx(mHeight + mPaddingHeight);
        } else {
            height = mPaddingHeight;
        }

        ValueAnimator animator = ValueAnimator.ofInt(getView().getMeasuredHeight(), height);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                params.height = (int) valueAnimator.getAnimatedValue();
                getView().setLayoutParams(params);
            }
        });
        animator.setDuration(100);
        animator.start();
    }

    private int dpToPx(int dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics()));
    }
}
