package com.frogs42.cookbook.model;

/**
 * Class to hold recipe in events
 */
public class RecipeHolder {
    private Recipe mRecipe;

    public RecipeHolder(Recipe recipe) {
        mRecipe = recipe;
    }

    public Recipe getRecipe() {
        return mRecipe;
    }
}
