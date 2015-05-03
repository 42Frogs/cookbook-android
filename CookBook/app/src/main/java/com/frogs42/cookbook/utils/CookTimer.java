package com.frogs42.cookbook.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;

public class CookTimer {

    public static final String LOG_TAG = "CookTimer";

    public interface CookTimerListener {
        public void onTick(CookTimer caller);
        public void onFinish(CookTimer caller);
    }

    private Context mContext;

    private String mTitle;
    private CountDownTimer mInnerTimer;
    private CookTimerListener mTimerListener;

    private int mRemainingSeconds;

    public CookTimer(Context context, String title, int seconds) {
        mContext = context;
        mTitle = title;
        mRemainingSeconds = seconds;
        mInnerTimer = new CountDownTimer(seconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                --mRemainingSeconds;
                Handler handler = new Handler(mContext.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mTimerListener != null)
                            mTimerListener.onTick(CookTimer.this);
                    }
                });
            }

            @Override
            public void onFinish() {
                Handler handler = new Handler(mContext.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mTimerListener != null) {
                            mTimerListener.onFinish(CookTimer.this);
                        }
                    }
                });
            }
        }.start();
    }

    public String getTitle() {
        return mTitle;
    }

    public void setListener(CookTimerListener listener) {
        mTimerListener = listener;
    }

    public void removeListener(CookTimerListener listener) {
        if (mTimerListener == listener)
            mTimerListener = null;
    }

    public void cancel() {
        mInnerTimer.cancel();
    }

    public int getRemainingSeconds() {
        return mRemainingSeconds;
    }
}
