package com.frogs42.cookbook.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frogs42.cookbook.R;
import com.frogs42.cookbook.adapters.MainPagerAdapter;

public class MainPagerFragment extends Fragment {

    public static final int FAVOURITE_RECIPES_VIEW = 0;
    public static final int ALL_RECIPES_VIEW = 1;
    public static final int ACTIVE_RECIPES_VIEW = 2;

    private View mView;
    private ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView != null)
            return mView;

        mView = inflater.inflate(R.layout.fragment_main, null);

        mViewPager = (ViewPager) mView.findViewById(R.id.main_pager);
        mViewPager.setAdapter(new MainPagerAdapter(getActivity()));
        mViewPager.setCurrentItem(ALL_RECIPES_VIEW);

        return mView;
    }
}
