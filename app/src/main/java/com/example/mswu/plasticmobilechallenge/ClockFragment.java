package com.example.mswu.plasticmobilechallenge;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DragViewListener.Event} interface
 * to handle interaction events.
 * Use the {@link ClockFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClockFragment extends Fragment {
    // Arguments
    private static final String sArgTime = "sArgTime";

    private String mTime;

    // Fragment Callback
    private DragViewListener.Event mListener;

    // Timer Controller
    private final Handler mTimerHandler = new Handler();
    private TimerRunnable mTimerRunnable;

    // UI Elements
    private TextView mTimerTextView;

    // Animation
    private RotateAnimation mRotateAnimation;

    public ClockFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param time Time to display.
     * @return A new instance of fragment ClockFragment.
     */
    public static ClockFragment newInstance(String time) {
        ClockFragment fragment = new ClockFragment();
        Bundle args = new Bundle();
        args.putString(sArgTime, time);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTime = getArguments().getString(sArgTime);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_clock, container, false);
        final DragViewListener dragListener = new DragViewListener();
        dragListener.setDelegate(mListener);
        view.setOnTouchListener(dragListener);
        mTimerTextView = view.findViewById(R.id.time_text_view);

        if (mTime != null) {
            mTimerTextView.setText(mTime);
        }

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotation);
        view.startAnimation(animation);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mTimerRunnable = new TimerRunnable(context);
        mTimerRunnable.run();

        if (context instanceof DragViewListener.Event) {
            mListener = (DragViewListener.Event) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DragViewListener.Event");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mTimerRunnable.stop();
        mTimerHandler.removeCallbacks(mTimerRunnable);
    }

    /**
     * Fetch current time from the API and update time to UI.
     */
    private final class TimerRunnable implements Runnable {
        static final String TAG = "TimerRunnable";

        private static final String sEndpoint = "https://dateandtimeasjson.appspot.com/";

        private final RequestQueue mRequestQueue;
        private final Gson mGson = new Gson();

        TimerRunnable(Context context) {
            mRequestQueue = Volley.newRequestQueue(context);
        }

        /**
         * Start periodic task.
         */
        public void run() {
            final long interval = 1;
            final StringRequest request = new StringRequest(Request.Method.GET, sEndpoint, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    mTimerTextView.setText(deserialize(response));
                    mTimerHandler.postAtTime(mTimerRunnable, interval);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, error.toString());
                    mTimerHandler.postAtTime(mTimerRunnable, interval);
                }
            });
            request.setTag(TAG);
            mRequestQueue.add(request);
        }

        /**
         * Terminate running requests.
         */
        void stop() {
            if (mRequestQueue != null) {
                mRequestQueue.cancelAll(TAG);
            }
        }

        /**
         * Parse raw JSON string and return value in datetime field.
         *
         * @param data The raw JSON string.
         * @return Time in string on success or null on failure.
         */
        private String deserialize(String data) {
            final DateTime dateTime = mGson.fromJson(data, DateTime.class);
            return dateTime.toString();
        }
    }
}
