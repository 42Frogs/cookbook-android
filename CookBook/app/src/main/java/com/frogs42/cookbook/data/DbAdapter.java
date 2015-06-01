package com.frogs42.cookbook.data;

import android.content.Context;
import android.database.Cursor;

import com.frogs42.cookbook.model.Ingredient;
import com.frogs42.cookbook.model.IngredientEntry;
import com.frogs42.cookbook.model.Recipe;
import com.frogs42.cookbook.model.RecipeStep;

/**
 * Created by ilia on 08.05.15.
 */
public class DbAdapter {

    public static Recipe getRecipe(Context context, int recipe_id){
        Recipe recipe = new Recipe().setId(recipe_id);

        Cursor recipeCursor = context.getContentResolver().query(Contract.RecipeEntry.buildUri(recipe_id), null, null, null, null);
        if(recipeCursor != null && recipeCursor.moveToFirst()) {
            recipe.setTitle(recipeCursor.getString(recipeCursor.getColumnIndex(Contract.RecipeEntry.NAME)))
                    .setDescription(recipeCursor.getString(recipeCursor.getColumnIndex(Contract.RecipeEntry.DESCRIPTION)))
                    .setIcoPath(recipeCursor.getString(recipeCursor.getColumnIndex(Contract.RecipeEntry.IMAGE_PATH)));
            recipeCursor.close();
        }else return null;

        Cursor ingredientsCursor = context.getContentResolver().query(Contract.RecipeIngredientEntry.buildUri(recipe_id),null,null,null,null);
        if(ingredientsCursor != null) {
            for (int i = 0; i < ingredientsCursor.getCount(); i++) {
                ingredientsCursor.moveToPosition(i);
                recipe.addIngredient(new IngredientEntry()
                            .setIngredient(new Ingredient()
                                    .setId(ingredientsCursor.getInt(ingredientsCursor.getColumnIndex(Contract.IngredientEntry._ID)))
                                    .setName(ingredientsCursor.getString(ingredientsCursor.getColumnIndex(Contract.IngredientEntry.NAME))))
                            .setAmount(ingredientsCursor.getInt(ingredientsCursor.getColumnIndex(Contract.RecipeIngredientEntry.SIZE)))
                            .setMeasure(ingredientsCursor.getString(ingredientsCursor.getColumnIndex(Contract.RecipeIngredientEntry.MEASURE))));
            }
            ingredientsCursor.close();
        }

        Cursor stepsCursor = context.getContentResolver().query(Contract.StepEntry.buildUri(recipe_id),null,null,null,null);
        if(stepsCursor != null) {
            for (int i = 0; i < stepsCursor.getCount(); i++) {
                stepsCursor.moveToPosition(i);
                RecipeStep recipeStep = new RecipeStep();
                int id = stepsCursor.getInt(stepsCursor.getColumnIndex(Contract.StepEntry._ID));
                recipeStep.setId(id)
                        .setTitle(stepsCursor.getString(stepsCursor.getColumnIndex(Contract.StepEntry.NAME)))
                        .setDescription(stepsCursor.getString(stepsCursor.getColumnIndex(Contract.StepEntry.DESCRIPTION)))
                        .setIcoPath(stepsCursor.getString(stepsCursor.getColumnIndex(Contract.StepEntry.IMAGE_PATH)))
                        .setDurationInSeconds(stepsCursor.getInt(stepsCursor.getColumnIndex(Contract.StepEntry.TIMER)));

                recipe.addRecipeStep(recipeStep);
            }
            stepsCursor.close();
        }

        for(RecipeStep i : recipe.getRecipeSteps()) {
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
        return recipe;
    }
}
