package com.frogs42.cookbook.data;

import android.content.Context;

import com.frogs42.cookbook.model.Recipe;
import com.frogs42.cookbook.model.RecipeHolder;
import com.frogs42.cookbook.utils.EventsManager;

import java.util.ArrayList;

/**
 * Main class for data storing and manipulations
 */
public class DataStore {

    public static final String EVENT_RECIPE_ADDED = "event_recipe_added";
    public static final String EVENT_RECIPE_DELETED = "event_recipe_deleted";
    public static final String EVENT_RECIPE_BECOME_FAVOURITE = "event_recipe_become_favourite";
    public static final String EVENT_RECIPE_BECOME_NON_FAVOURITE = "event_recipe_become_non_favourite";
    public static final String EVENT_RECIPE_COOKING_STARTED = "event_recipe_cooking_started";
    public static final String EVENT_RECIPE_COOKING_FINISHED = "event_recipe_cooking_finished";


    private  static DataStore sInstance;

    private Context mContext;

    private ArrayList<Recipe> mRecipesList;
    private ArrayList<Recipe> mFavouriteRecipesList;
    private ArrayList<Recipe> mActiveRecipesList = new ArrayList<>();


    private DataStore(Context context) {
        mContext = context;

        mRecipesList = DbAdapter.getRecipesList(mContext);
        // TODO: change to favourites query
        mFavouriteRecipesList = DbAdapter.getRecipesList(mContext);

    }

    public static void init(Context context) {
        if (sInstance == null)
            sInstance = new DataStore(context);
    }

    public static void terminate() {
        sInstance = null;
    }

    public static ArrayList<Recipe> getRecipesList() {
        return sInstance.mRecipesList;
    }

    public static ArrayList<Recipe> getFavouriteRecipesList() {
        return sInstance.mFavouriteRecipesList;
    }

    public static ArrayList<Recipe> getActiveRecipesList() {
        return sInstance.mActiveRecipesList;
    }

    public static void addRecipe(Recipe recipe) {
        // TODO: save to DB
        sInstance.mRecipesList.add(recipe);
        EventsManager.dispatchEvent(EVENT_RECIPE_ADDED, new RecipeHolder(recipe));
    }

    public static void removeRecipe(Recipe recipe) {
        // TODO: delete from DB
        sInstance.mRecipesList.remove(recipe);
        EventsManager.dispatchEvent(EVENT_RECIPE_DELETED, new RecipeHolder(recipe));
    }

    public static void makeFavourite(Recipe recipe) {
        // TODO: make favourite
        sInstance.mFavouriteRecipesList.add(recipe);
        EventsManager.dispatchEvent(EVENT_RECIPE_BECOME_FAVOURITE, new RecipeHolder(recipe));
    }

    public static void makeNonFavourite(Recipe recipe) {
        // TODO: make non favourite
        sInstance.mFavouriteRecipesList.remove(recipe);
        EventsManager.dispatchEvent(EVENT_RECIPE_BECOME_NON_FAVOURITE, new RecipeHolder(recipe));
    }
}
