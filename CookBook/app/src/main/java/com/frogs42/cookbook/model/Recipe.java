package com.frogs42.cookbook.model;

import java.util.ArrayList;

/**
 * Main model to represent recipe instance.
 */
public class Recipe {

    private int mId = -1;

    private String mTitle = "";
    private String mDescription = "";
    private String mIcoPath = "";
    private boolean mFavorite = false;

    private ArrayList<IngredientEntry> mIngredients = new ArrayList<>();
    private ArrayList<RecipeStep> mRecipeSteps = new ArrayList<>();

    public int getId() {
        return mId;
    }

    public Recipe setId(int id) {
        mId = id;
        return this;
    }

    public String getTitle() {
        return mTitle;
    }

    public Recipe setTitle(String title) {
        mTitle = title;
        return this;
    }

    public String getDescription() {
        return mDescription;
    }

    public Recipe setDescription(String description) {
        mDescription = description;
        return this;
    }

    public String getIcoPath() {
        return mIcoPath;
    }

    public Recipe setIcoPath(String icoPath) {
        mIcoPath = icoPath;
        return this;
    }

    public ArrayList<RecipeStep> getRecipeSteps() {
        return mRecipeSteps;
    }

    public Recipe addRecipeStep(RecipeStep recipeStep) {
        mRecipeSteps.add(recipeStep);
        return this;
    }

    public ArrayList<IngredientEntry> getIngredients() {
        return mIngredients;
    }

    public Recipe addIngredient(IngredientEntry ingredient) {
        mIngredients.add(ingredient);
        return this;
    }

    public boolean isFavorite() {
        return mFavorite;
    }

    public Recipe setFavorite(boolean favorite) {
        mFavorite = favorite;
        return this;
    }
}
