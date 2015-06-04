package com.frogs42.cookbook.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.frogs42.cookbook.R;
import com.frogs42.cookbook.adapters.TimersListAdapter;
import com.frogs42.cookbook.utils.TimersManager;

public class TimersListFragment extends Fragment {

    private View mView;
    private ListView mTimersListView;
    private TimersListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView != null)
            return mView;

        mView = inflater.inflate(R.layout.fragment_timers_list, null);

        mTimersListView = (ListView)mView.findViewById(R.id.timers_list);
        mAdapter = new TimersListAdapter(getActivity());
        TimersManager.addDataListener(mAdapter);
        mTimersListView.setAdapter(mAdapter);

        TimersManager.addTimer(5, 5);
        TimersManager.addTimer(10, 10);
        TimersManager.addTimer(15, 15);

        return mView;
    }

    @Override
    public void onDestroy() {
        TimersManager.removeDataListener(mAdapter);
        super.onDestroy();
    }
}
