package com.frogs42.cookbook.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.frogs42.cookbook.R;

import java.util.ArrayList;

public class TimersManager implements CookTimer.CookTimerListener {

    private static TimersManager sInstance;

    public interface DataListener {
        public void onDataChanged(CookTimer caller);
        void onTimerFinished(CookTimer timer);
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

    public static void addTimer(int id, int seconds) {
        CookTimer timer = new CookTimer(sInstance.mContext, id, seconds);
        timer.setListener(sInstance);
        sInstance.mTimersContainer.add(timer);
    }

    public static void removeTimer(int id) {
        for(CookTimer timer : sInstance.mTimersContainer)
            if(timer.getID() == id){
                sInstance.mTimersContainer.remove(timer);
                sInstance.notifyDataChanged(timer);
                timer.removeListener(sInstance);
                timer.cancel();
            }

    }

    @Override
    public void onTick(CookTimer caller) {
        notifyDataChanged(caller);
    }

    @Override
    public void onFinish(final CookTimer caller) {
//        notifyDataChanged(caller);
        showTimerFinishedPopup(caller);
    }

    public static void addDataListener(DataListener listener) {
        if (!sInstance.mDataListeners.contains(listener))
            sInstance.mDataListeners.add(listener);
    }

    public static void removeDataListener(DataListener listener) {
        if (sInstance.mDataListeners.contains(listener))
            sInstance.mDataListeners.remove(listener);
    }

    private void notifyDataChanged(CookTimer caller) {
        for (DataListener listener: mDataListeners)
            if (listener != null)
                listener.onDataChanged(caller);
    }

    private void notifyTimerFinished(CookTimer caller) {
        for (DataListener listener: mDataListeners)
            if (listener != null)
                listener.onTimerFinished(caller);
    }

    public static int getTimersCount() {
        return sInstance.mTimersContainer.size();
    }

    public static CookTimer getTimer(int index) {
        return sInstance.mTimersContainer.get(index);
    }

    public static int getRemainingTime(int id){
        for(CookTimer timer : sInstance.mTimersContainer)
            if(timer.getID() == id)
                return timer.getRemainingSeconds();
        return 0;
    }

    private void showTimerFinishedPopup(final CookTimer timer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.timer_done_dialog_title)
                .setMessage(timer.getTitle())
                .setCancelable(false)
                .setPositiveButton(R.string.timer_done_dialog_button_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTimersContainer.remove(timer);
                        notifyTimerFinished(timer);
                        timer.removeListener(sInstance);
                    }
                })
                .setNegativeButton(R.string.timer_postpone_dialog_button_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        timer.addTime(5);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
