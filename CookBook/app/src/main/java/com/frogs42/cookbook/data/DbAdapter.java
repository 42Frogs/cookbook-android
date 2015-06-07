package com.frogs42.cookbook.data;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.frogs42.cookbook.model.Ingredient;
import com.frogs42.cookbook.model.IngredientEntry;
import com.frogs42.cookbook.model.Recipe;
import com.frogs42.cookbook.model.RecipeStep;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ilia on 08.05.15.
 */
public class DbAdapter {

    private static HashMap<Integer, Recipe> sRecipesCache = new HashMap<>();
    private static HashMap<HashMap.Entry<Integer, Integer>, IngredientEntry> sIngredientEntriesCache = new HashMap<>();
    private static HashMap<Integer, Ingredient> sIngredientsCache = new HashMap<>();
    private static HashMap<Integer, RecipeStep> sStepsCache = new HashMap<>();

    public static Recipe getRecipe(Context context, int recipe_id){
        Recipe recipe;
        Cursor recipeCursor = context.getContentResolver().query(Contract.RecipeEntry.buildUri(recipe_id), null, null, null, null);
        if(recipeCursor != null && recipeCursor.moveToFirst()) {
            recipe = getRecipeFromCursor(recipeCursor);
            recipeCursor.close();
        } else return null;

        getIngredients(context,recipe);

        getSteps(context,recipe);

        return recipe;
    }

    public static void getIngredients(Context context, Recipe recipe){
        Cursor ingredientsCursor = context.getContentResolver().query(Contract.RecipeIngredientEntry.buildUri(recipe.getId()), null, null, null, null);
        recipe.clearIngredients();
        if(ingredientsCursor != null) {
            for (int i = 0; i < ingredientsCursor.getCount(); i++) {
                ingredientsCursor.moveToPosition(i);
                int id = ingredientsCursor.getInt(ingredientsCursor.getColumnIndex(Contract.IngredientEntry._ID));
                Ingredient ingredient = getIngredientInstance(id);
                IngredientEntry ingredientEntry = getIngredientEntryInstance(recipe.getId(), id);
                recipe.addIngredient(ingredientEntry
                        .setIngredient(ingredient.setName(ingredientsCursor.getString(ingredientsCursor.getColumnIndex(Contract.IngredientEntry.NAME))))
                        .setAmount(ingredientsCursor.getInt(ingredientsCursor.getColumnIndex(Contract.RecipeIngredientEntry.SIZE)))
                        .setMeasure(ingredientsCursor.getString(ingredientsCursor.getColumnIndex(Contract.RecipeIngredientEntry.MEASURE))));
            }
            ingredientsCursor.close();
        }
    }

    public static void getSteps(Context context, Recipe recipe){
        Cursor stepsCursor = context.getContentResolver().query(Contract.StepEntry.buildUri(recipe.getId()),null,null,null,null);
        recipe.clearSteps();
        if(stepsCursor != null) {
            for (int i = 0; i < stepsCursor.getCount(); i++) {
                stepsCursor.moveToPosition(i);
                int id = stepsCursor.getInt(stepsCursor.getColumnIndex(Contract.StepEntry._ID));
                RecipeStep recipeStep = getRecipeStepInstance(id);
                recipeStep.setTitle(stepsCursor.getString(stepsCursor.getColumnIndex(Contract.StepEntry.NAME)))
                        .setDescription(stepsCursor.getString(stepsCursor.getColumnIndex(Contract.StepEntry.DESCRIPTION)))
                        .setIcoPath(stepsCursor.getString(stepsCursor.getColumnIndex(Contract.StepEntry.IMAGE_PATH)))
                        .setDurationInSeconds(stepsCursor.getInt(stepsCursor.getColumnIndex(Contract.StepEntry.TIMER)));

                recipe.addRecipeStep(recipeStep);
            }
            stepsCursor.close();
        }

        for(RecipeStep i : recipe.getRecipeSteps()) {
            i.clearParents();
            Cursor stepParents = context.getContentResolver().query(Contract.StepStepEntry.buildUri(i.getId()), null, null, null, null);
            if (stepParents != null) {
                for (int j = 0; j < stepParents.getCount(); j++) {
                    stepParents.moveToPosition(j);
                    for(RecipeStep step : recipe.getRecipeSteps())
                        if(step.getId() == stepParents.getInt(stepParents.getColumnIndex(Contract.StepStepEntry.PARENT))){
                            i.addParentStep(step);
                            break;
                        }
                }
                stepParents.close();
            }
        }
    }

    public static ArrayList<Recipe> getRecipesList(Context context) {
        ArrayList<Recipe> recipesList = new ArrayList<>();

        Cursor recipeCursor = context.getContentResolver().query(Contract.RecipeEntry.CONTENT_URI, null, null, null, null);
        if(recipeCursor != null) {
            for (int i = 0; i < recipeCursor.getCount(); i++) {
                recipeCursor.moveToPosition(i);
                Recipe recipe = getRecipeFromCursor(recipeCursor);
                
                getIngredients(context,recipe);

                getSteps(context, recipe);

                recipesList.add(recipe);
            }
            recipeCursor.close();
        }

        return recipesList;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static ArrayList<Recipe> getFavoriteRecipesList(Context context) {
        ArrayList<Recipe> favoriteRecipes = new ArrayList<>();

        Cursor recipeCursor = context.getContentResolver().query(
                Contract.RecipeEntry.CONTENT_URI,
                null,
                Contract.RecipeEntry.FAVORITE + "= ?",
                new String[]{"1"},
                null,
                null);

        if(recipeCursor != null) {
            for (int i = 0; i < recipeCursor.getCount(); i++) {
                recipeCursor.moveToPosition(i);
                Recipe recipe = getRecipeFromCursor(recipeCursor);
                favoriteRecipes.add(recipe);
            }
            recipeCursor.close();
        }

        return favoriteRecipes;
    }

    public static int updateFavorite(Context context, int recipeId, boolean isFavorite) {
        ContentValues values = new ContentValues();
        values.put(Contract.RecipeEntry._ID, recipeId);
        values.put(Contract.RecipeEntry.FAVORITE, isFavorite);
        return context.getContentResolver().update(
                Contract.RecipeEntry.buildUri(recipeId),
                values,
                Contract.RecipeEntry._ID + "= ?",
                new String[]{Long.toString(recipeId)});
    }

    private static Recipe getRecipeInstance(int id) {
        Recipe instance = sRecipesCache.get(id);
        if (instance == null) {
            instance = new Recipe().setId(id);
            sRecipesCache.put(id, instance);
        }
        return instance;
    }

    private static RecipeStep getRecipeStepInstance(int id) {
        RecipeStep instance = sStepsCache.get(id);
        if (instance == null) {
            instance = new RecipeStep().setId(id);
            sStepsCache.put(id, instance);
        }
        return instance;
    }

    private static Ingredient getIngredientInstance(int id) {
        Ingredient instance = sIngredientsCache.get(id);
        if (instance == null) {
            instance = new Ingredient().setId(id);
            sIngredientsCache.put(id, instance);
        }
        return instance;
    }

    private static IngredientEntry getIngredientEntryInstance(int recipeId, int ingredientId) {
        IngredientEntry instance = sIngredientEntriesCache.get(new HashMap.SimpleEntry<>(recipeId, ingredientId));
        if (instance == null) {
            instance = new IngredientEntry();
            sIngredientEntriesCache.put(new HashMap.SimpleEntry<>(recipeId, ingredientId), instance);
        }
        return instance;
    }

    private static Recipe getRecipeFromCursor(Cursor cursor) {
        int recipeId = (int) cursor.getLong(cursor.getColumnIndex(Contract.RecipeEntry._ID));
        Recipe recipe = getRecipeInstance(recipeId);
        return recipe.setTitle(cursor.getString(cursor.getColumnIndex(Contract.RecipeEntry.NAME)))
                .setDescription(cursor.getString(cursor.getColumnIndex(Contract.RecipeEntry.DESCRIPTION)))
                .setIcoPath(cursor.getString(cursor.getColumnIndex(Contract.RecipeEntry.IMAGE_PATH)))
                .setFavorite(cursor.getInt(cursor.getColumnIndex(Contract.RecipeEntry.FAVORITE)) != 0);
    }
}
