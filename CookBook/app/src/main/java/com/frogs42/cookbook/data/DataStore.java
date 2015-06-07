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
    private ArrayList<Recipe> mActiveRecipes = new ArrayList<>();


    private DataStore(Context context) {
        mContext = context;
    }

    public static void init(Context context) {
        if (sInstance == null)
            sInstance = new DataStore(context);
    }

    public static void terminate() {
        sInstance = null;
    }

    public static ArrayList<Recipe> getRecipesList() {
        assert sInstance != null : "DataStore must be initialized first";

        if (sInstance.mRecipesList == null)
            sInstance.mRecipesList = DbAdapter.getRecipesList(sInstance.mContext);

        return sInstance.mRecipesList;
    }

    public static ArrayList<Recipe> getFavouriteRecipesList() {
        assert sInstance != null : "DataStore must be initialized first";

        if (sInstance.mFavouriteRecipesList == null)
            sInstance.mFavouriteRecipesList = DbAdapter.getFavoriteRecipesList(sInstance.mContext);

        return sInstance.mFavouriteRecipesList;
    }

    public static ArrayList<Recipe> getActiveRecipesList() {
        assert sInstance != null : "DataStore must be initialized first";

        return sInstance.mActiveRecipes;
    }

    public static Progress getProgress(Recipe recipe){
        assert sInstance != null : "DataStore must be initialized first";

        return sInstance.mActiveRecipesMap.get(recipe);
    }

    public static void addRecipe(Recipe recipe) {
        assert sInstance != null : "DataStore must be initialized first";

        // TODO: save to DB
        sInstance.mRecipesList.add(recipe);
        EventsManager.dispatchEvent(GlobalEvents.EVENT_RECIPE_ADDED, new RecipeHolder(recipe));
    }

    public static void removeRecipe(Recipe recipe) {
        assert sInstance != null : "DataStore must be initialized first";

        // TODO: delete from DB
        sInstance.mRecipesList.remove(recipe);
        EventsManager.dispatchEvent(GlobalEvents.EVENT_RECIPE_DELETED, new RecipeHolder(recipe));
    }

    public static void makeFavourite(Recipe recipe) {
        assert sInstance != null : "DataStore must be initialized first";

        DbAdapter.updateFavorite(sInstance.mContext, recipe.getId(), true);
        recipe.setFavorite(true);
        sInstance.mFavouriteRecipesList.add(recipe);
        EventsManager.dispatchEvent(GlobalEvents.EVENT_RECIPE_BECOME_FAVOURITE, new RecipeHolder(recipe));
    }

    public static void makeNonFavourite(Recipe recipe) {
        assert sInstance != null : "DataStore must be initialized first";

        DbAdapter.updateFavorite(sInstance.mContext, recipe.getId(), false);
        recipe.setFavorite(false);
        sInstance.mFavouriteRecipesList.remove(recipe);
        EventsManager.dispatchEvent(GlobalEvents.EVENT_RECIPE_BECOME_NON_FAVOURITE, new RecipeHolder(recipe));
    }

    public static void onStartCooking(Recipe recipe, Progress progress) {
        assert sInstance != null : "DataStore must be initialized first";

        sInstance.mActiveRecipesMap.put(recipe, progress);
        sInstance.mActiveRecipes.add(recipe);
        EventsManager.dispatchEvent(GlobalEvents.EVENT_RECIPE_COOKING_STARTED, new RecipeHolder(recipe));
    }

    public static void onFinishCooking(Recipe recipe) {
        assert sInstance != null : "DataStore must be initialized first";

        sInstance.mActiveRecipesMap.remove(recipe);
        sInstance.mActiveRecipes.remove(recipe);
        EventsManager.dispatchEvent(GlobalEvents.EVENT_RECIPE_COOKING_FINISHED, new RecipeHolder(recipe));
    }
}
