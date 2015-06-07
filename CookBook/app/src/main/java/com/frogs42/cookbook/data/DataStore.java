package com.frogs42.cookbook.data;

import android.content.Context;

import com.frogs42.cookbook.model.Recipe;
import com.frogs42.cookbook.model.RecipeHolder;
import com.frogs42.cookbook.utils.EventsManager;
import com.frogs42.cookbook.utils.GlobalEvents;
import com.frogs42.cookbook.utils.Progress;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Main class for data storing and manipulations
 */
public class DataStore {

    private  static DataStore sInstance;

    private Context mContext;

    private ArrayList<Recipe> mRecipesList;
    private ArrayList<Recipe> mFavouriteRecipesList;
    private HashMap<Recipe,Progress> mActiveRecipesMap = new HashMap<>();


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
        sInstance.mFavouriteRecipesList = DbAdapter.getFavoriteRecipesList(sInstance.mContext);

        return sInstance.mFavouriteRecipesList;
    }

    public static ArrayList<Recipe> getActiveRecipesList() {
        return new ArrayList<>(sInstance.mActiveRecipesMap.keySet());
    }

    public static Progress getProgress(Recipe recipe){
        return sInstance.mActiveRecipesMap.get(recipe);
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
        DbAdapter.updateFavorite(sInstance.mContext, recipe.getId(), true);
        recipe.setFavorite(true);
        sInstance.mFavouriteRecipesList.add(recipe);
        EventsManager.dispatchEvent(GlobalEvents.EVENT_RECIPE_BECOME_FAVOURITE, new RecipeHolder(recipe));
    }

    public static void makeNonFavourite(Recipe recipe) {
        DbAdapter.updateFavorite(sInstance.mContext, recipe.getId(), false);
        recipe.setFavorite(false);
        sInstance.mFavouriteRecipesList.remove(recipe);
        EventsManager.dispatchEvent(GlobalEvents.EVENT_RECIPE_BECOME_NON_FAVOURITE, new RecipeHolder(recipe));
    }

    public static void onStartCooking(Recipe recipe, Progress progress) {
        sInstance.mActiveRecipesMap.put(recipe, progress);
        EventsManager.dispatchEvent(GlobalEvents.EVENT_RECIPE_COOKING_STARTED, new RecipeHolder(recipe));
    }

    public static void onFinishCooking(Recipe recipe) {
        sInstance.mActiveRecipesMap.remove(recipe);
        EventsManager.dispatchEvent(GlobalEvents.EVENT_RECIPE_COOKING_FINISHED, new RecipeHolder(recipe));
    }
}
