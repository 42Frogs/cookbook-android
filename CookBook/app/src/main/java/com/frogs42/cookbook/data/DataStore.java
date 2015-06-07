package com.frogs42.cookbook.data;

import android.content.ContentValues;
import android.content.Context;

import com.frogs42.cookbook.model.Recipe;
import com.frogs42.cookbook.model.RecipeHolder;
import com.frogs42.cookbook.utils.EventsManager;
import com.frogs42.cookbook.utils.GlobalEvents;

import java.util.ArrayList;

/**
 * Main class for data storing and manipulations
 */
public class DataStore {

    private  static DataStore sInstance;

    private Context mContext;

    private ArrayList<Recipe> mRecipesList;
    private ArrayList<Recipe> mFavouriteRecipesList;
    private ArrayList<Recipe> mActiveRecipesList = new ArrayList<>();


    private DataStore(Context context) {
        mContext = context;

        mRecipesList = DbAdapter.getRecipesList(mContext);

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
        // TODO: make it ok)!
        sInstance.mFavouriteRecipesList = DbAdapter.getRecipesList(sInstance.mContext);
        for (int i = sInstance.mFavouriteRecipesList.size() - 1; i >= 0; --i) {
            if (!sInstance.mFavouriteRecipesList.get(i).isFavorite()) {
                sInstance.mFavouriteRecipesList.remove(i);
            }
        }

        return sInstance.mFavouriteRecipesList;
    }

    public static ArrayList<Recipe> getActiveRecipesList() {
        return sInstance.mActiveRecipesList;
    }

    public static void addRecipe(Recipe recipe) {
        // TODO: save to DB
        sInstance.mRecipesList.add(recipe);
        EventsManager.dispatchEvent(GlobalEvents.EVENT_RECIPE_ADDED, new RecipeHolder(recipe));
    }

    public static void removeRecipe(Recipe recipe) {
        // TODO: delete from DB
        sInstance.mRecipesList.remove(recipe);
        EventsManager.dispatchEvent(GlobalEvents.EVENT_RECIPE_DELETED, new RecipeHolder(recipe));
    }

    public static void makeFavourite(Recipe recipe) {
        ContentValues values = new ContentValues();
        values.put(Contract.RecipeEntry._ID, recipe.getId());
        values.put(Contract.RecipeEntry.FAVORITE, true);
        sInstance.mContext.getContentResolver().update(
                Contract.RecipeEntry.buildUri(recipe.getId()),
                values, Contract.RecipeEntry._ID + "= ?",
                new String[]{Long.toString(recipe.getId())});

        recipe.setFavorite(true);
        sInstance.mFavouriteRecipesList.add(recipe);
        EventsManager.dispatchEvent(GlobalEvents.EVENT_RECIPE_BECOME_FAVOURITE, new RecipeHolder(recipe));
    }

    public static void makeNonFavourite(Recipe recipe) {
        ContentValues values = new ContentValues();
        values.put(Contract.RecipeEntry._ID, recipe.getId());
        values.put(Contract.RecipeEntry.FAVORITE, false);
        sInstance.mContext.getContentResolver().update(
                Contract.RecipeEntry.buildUri(recipe.getId()),
                values, Contract.RecipeEntry._ID + "= ?",
                new String[]{Long.toString(recipe.getId())});

        recipe.setFavorite(false);
        sInstance.mFavouriteRecipesList.remove(recipe);
        EventsManager.dispatchEvent(GlobalEvents.EVENT_RECIPE_BECOME_NON_FAVOURITE, new RecipeHolder(recipe));
    }

    public static void onStartCooking(Recipe recipe) {
        sInstance.mActiveRecipesList.add(recipe);
        EventsManager.dispatchEvent(GlobalEvents.EVENT_RECIPE_COOKING_STARTED, new RecipeHolder(recipe));
    }

    public static void onFinishCooking(Recipe recipe) {
        sInstance.mActiveRecipesList.remove(recipe);
        EventsManager.dispatchEvent(GlobalEvents.EVENT_RECIPE_COOKING_FINISHED, new RecipeHolder(recipe));
    }
}
