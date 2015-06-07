package com.frogs42.cookbook.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ListView;

import com.frogs42.cookbook.R;
import com.frogs42.cookbook.data.DataStore;
import com.frogs42.cookbook.fragments.MainPagerFragment;
import com.frogs42.cookbook.utils.EventsManager;

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
        View view = View.inflate(mContext, R.layout.pager_main_item, null);
        ((ViewPager) collection).addView(view);
        ListView recipesList = (ListView) view.findViewById(R.id.recipes_list);
        switch (position) {
            case MainPagerFragment.FAVOURITE_RECIPES_VIEW: {
                RecipesListAdapter adapter = new RecipesListAdapter(mContext, DataStore.getFavouriteRecipesList());
                EventsManager.addHandler(DataStore.EVENT_RECIPE_BECOME_FAVOURITE, adapter);
                EventsManager.addHandler(DataStore.EVENT_RECIPE_BECOME_NON_FAVOURITE, adapter);
                recipesList.setAdapter(adapter);
                break;
            }
            case MainPagerFragment.ACTIVE_RECIPES_VIEW: {
                RecipesListAdapter adapter = new RecipesListAdapter(mContext, DataStore.getActiveRecipesList());
                EventsManager.addHandler(DataStore.EVENT_RECIPE_COOKING_STARTED, adapter);
                EventsManager.addHandler(DataStore.EVENT_RECIPE_COOKING_FINISHED, adapter);
                recipesList.setAdapter(adapter);
                break;
            }
            case MainPagerFragment.ALL_RECIPES_VIEW: {
                RecipesListAdapter adapter = new RecipesListAdapter(mContext, DataStore.getRecipesList());
                EventsManager.addHandler(DataStore.EVENT_RECIPE_ADDED, adapter);
                EventsManager.addHandler(DataStore.EVENT_RECIPE_DELETED, adapter);
                recipesList.setAdapter(adapter);
                break;
            }
        }
        return view;
    }

    @Override
    public void destroyItem(View collection, int position, Object view) {
        ListView recipesList = (ListView) ((View) view).findViewById(R.id.recipes_list);
        EventsManager.unsubscribeAll((RecipesListAdapter) recipesList.getAdapter());
        ((ViewPager) collection).removeView((View) view);
    }


    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view.equals(o);
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
