package com.frogs42.cookbook.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.frogs42.cookbook.R;
import com.frogs42.cookbook.data.DataStore;
import com.frogs42.cookbook.fragments.MainPagerFragment;
import com.frogs42.cookbook.model.Recipe;
import com.frogs42.cookbook.model.RecipeHolder;
import com.frogs42.cookbook.utils.EventsManager;
import com.frogs42.cookbook.utils.GlobalEvents;

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
        RecipesListAdapter adapter = null;
        switch (position) {
            case MainPagerFragment.FAVOURITE_RECIPES_VIEW: {
                adapter = new RecipesListAdapter(mContext, DataStore.getFavouriteRecipesList());
                EventsManager.addHandler(GlobalEvents.EVENT_RECIPE_BECOME_FAVOURITE, adapter);
                EventsManager.addHandler(GlobalEvents.EVENT_RECIPE_BECOME_NON_FAVOURITE, adapter);
                recipesList.setAdapter(adapter);
                break;
            }
            case MainPagerFragment.ACTIVE_RECIPES_VIEW: {
                adapter = new RecipesListAdapter(mContext, DataStore.getActiveRecipesList());
                EventsManager.addHandler(GlobalEvents.EVENT_RECIPE_COOKING_STARTED, adapter);
                EventsManager.addHandler(GlobalEvents.EVENT_RECIPE_COOKING_FINISHED, adapter);
                EventsManager.addHandler(GlobalEvents.EVENT_RECIPE_BECOME_FAVOURITE, adapter);
                EventsManager.addHandler(GlobalEvents.EVENT_RECIPE_BECOME_NON_FAVOURITE, adapter);
                recipesList.setAdapter(adapter);
                break;
            }
            case MainPagerFragment.ALL_RECIPES_VIEW: {
                adapter = new RecipesListAdapter(mContext, DataStore.getRecipesList());
                EventsManager.addHandler(GlobalEvents.EVENT_RECIPE_ADDED, adapter);
                EventsManager.addHandler(GlobalEvents.EVENT_RECIPE_DELETED, adapter);
                EventsManager.addHandler(GlobalEvents.EVENT_RECIPE_BECOME_FAVOURITE, adapter);
                EventsManager.addHandler(GlobalEvents.EVENT_RECIPE_BECOME_NON_FAVOURITE, adapter);
                recipesList.setAdapter(adapter);
                break;
            }
        }

        final RecipesListAdapter finalAdapter = adapter;
        if (position == MainPagerFragment.ALL_RECIPES_VIEW || position == MainPagerFragment.FAVOURITE_RECIPES_VIEW)
            recipesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Recipe selectedRecipe = finalAdapter.getItem(position);
                    EventsManager.dispatchEvent(GlobalEvents.EVENT_RECIPE_SELECTED, new RecipeHolder(selectedRecipe));
                }
            });

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
