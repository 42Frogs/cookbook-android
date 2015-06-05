package com.frogs42.cookbook.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ListView;

import com.frogs42.cookbook.R;
import com.frogs42.cookbook.fragments.MainPagerFragment;

/**
 * Main pager adapter class
 */
public class MainPagerAdapter extends PagerAdapter {

    private Context mContext;

    public MainPagerAdapter(Context context) {
        super();
        mContext = context;
    }

    @Override
    public Object instantiateItem(View collection, int position) {
        ListView view = new ListView(mContext);
        ((ViewPager)collection).addView(view);
        return view;
    }

    @Override
    public void destroyItem(View collection, int position, Object view) {
        ((ViewPager) collection).removeView((View) view);
    }


    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return !(view == null || view.getTag() == null || o == null) && view.getTag().equals(o);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case MainPagerFragment.FAVOURITE_RECIPES_VIEW:
                return mContext.getString(R.string.favourite_recipes_view_header);
            case MainPagerFragment.ALL_RECIPES_VIEW:
                return mContext.getString(R.string.all_recipes_view_header);
            case MainPagerFragment.ACTIVE_RECIPES_VIEW:
                return mContext.getString(R.string.active_recipes_view_header);
        }
        return "";
    }
}
