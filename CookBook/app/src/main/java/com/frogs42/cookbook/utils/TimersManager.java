package com.frogs42.cookbook.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;

public class TimersManager implements CookTimer.CookTimerListener {

    private static TimersManager sInstance;

    public interface DataListener {
        public void onDataChanged();
    }

    private Context mContext;

    private ArrayList<CookTimer> mTimersContainer;
    private ArrayList<DataListener> mDataListeners;

    private TimersManager(Context context) {
        mContext = context;
        mTimersContainer = new ArrayList<>();
        mDataListeners = new ArrayList<>();
    }

    public static void init(Context context) {
        if (sInstance == null)
            sInstance = new TimersManager(context);
    }

    public static void terminate() {
        if (sInstance != null)
            sInstance = null;
    }

    public static void addTimer(String title, int seconds) {
        CookTimer timer = new CookTimer(sInstance.mContext, title, seconds);
        timer.setListener(sInstance);
        sInstance.mTimersContainer.add(timer);
    }

    @Override
    public void onTick(CookTimer caller) {
        notifyDataChanged();
    }

    @Override
    public void onFinish(final CookTimer caller) {
        // TODO: add hiring popup window
        mTimersContainer.remove(caller);
        notifyDataChanged();
    }

    public static void addDataListener(DataListener listener) {
        if (!sInstance.mDataListeners.contains(listener))
            sInstance.mDataListeners.add(listener);
    }

    public static void removeDataListener(DataListener listener) {
        if (sInstance.mDataListeners.contains(listener))
            sInstance.mDataListeners.remove(listener);
    }

    private void notifyDataChanged() {
        for (DataListener listener: mDataListeners)
            if (listener != null)
                listener.onDataChanged();
    }

    public static int getTimersCount() {
        return sInstance.mTimersContainer.size();
    }

    public static CookTimer getTimer(int index) {
        return sInstance.mTimersContainer.get(index);
    }
}
