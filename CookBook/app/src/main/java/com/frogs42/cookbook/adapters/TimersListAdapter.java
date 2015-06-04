package com.frogs42.cookbook.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.frogs42.cookbook.R;
import com.frogs42.cookbook.utils.CookTimer;
import com.frogs42.cookbook.utils.TimersManager;


public class TimersListAdapter extends BaseAdapter implements TimersManager.DataListener {

    private Context mContext;

    public TimersListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return TimersManager.getTimersCount();
    }

    @Override
    public Object getItem(int position) {
        return TimersManager.getTimer(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_list_timer, null);
        }

        CookTimer timer = TimersManager.getTimer(position);

        TextView titleText = (TextView)convertView.findViewById(R.id.title);
        titleText.setText(String.format("%s - %d", timer.getTitle(), timer.getRemainingSeconds()));

        return convertView;
    }

    @Override
    public void onDataChanged(CookTimer caller) {
        notifyDataSetChanged();
    }

    @Override
    public void onTimerFinished(CookTimer caller) {
        notifyDataSetChanged();
    }
}
