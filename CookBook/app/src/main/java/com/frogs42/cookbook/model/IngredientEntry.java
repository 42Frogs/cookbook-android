package com.frogs42.cookbook.model;

/**
 * Class to represent ingredient entry in recipe.
 */
public class IngredientEntry {

    private Ingredient mIngredient;

    private int mAmount = 0;
    private String mMeasure = "";

    public Ingredient getIngredient() {
        return mIngredient;
    }

    public IngredientEntry setIngredient(Ingredient ingredient) {
        mIngredient = ingredient;
        return this;
    }

    public int getAmount() {
        return mAmount;
    }

    public IngredientEntry setAmount(int amount) {
        mAmount = amount;
        return this;
    }

    public String getMeasure() {
        return mMeasure;
    }

    public IngredientEntry setMeasure(String measure) {
        mMeasure = measure;
        return this;
    }
}
