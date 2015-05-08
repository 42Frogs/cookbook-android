package com.frogs42.cookbook.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ilia on 07.05.15.
 */
public class Contract {
    public static final String CONTENT_AUTHORITY = "com.frog42.cookbook";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_RECIPE = "recipe";
    public static final String PATH_INGREDIENT = "ingredient";
    public static final String PATH_RECIPE_INGREDIENT = "recipe_ingredient";
    public static final String PATH_CATEGORY = "category";
    public static final String PATH_STEP = "step";
    public static final String PATH_STEP_STEP = "step_step";

    public static final class RecipeEntry implements BaseColumns {
        public static final String TABLE_NAME = PATH_RECIPE;

        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String IMAGE_PATH = "image_path";
        public static final String FAVORITE = "favorite";
        public static final String CATEGORY_ID = "category_id";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPE).build();

        public static Uri buildUri(int id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class IngredientEntry implements BaseColumns {
        public static final String TABLE_NAME = PATH_INGREDIENT;

        public static final String NAME = "name";
    }

    public static final class RecipeIngredientEntry {
        public static final String TABLE_NAME = PATH_RECIPE_INGREDIENT;

        public static final String RECIPE_ID = "recipe_id";
        public static final String INGREDIENT_ID = "ingredient_id";
        public static final String SIZE = "size";
        public static final String MEASURE = "measure";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPE_INGREDIENT).build();

        public static Uri buildUri(int id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class StepEntry implements BaseColumns{
        public static final String TABLE_NAME = PATH_STEP;

        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String RECIPE_ID = "recipe_id";
        public static final String TIMER = "timer";
        public static final String IMAGE_PATH = "image_path";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STEP).build();

        public static Uri buildUri(int id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class StepStepEntry{
        public static final String TABLE_NAME = PATH_STEP_STEP;

        public static final String PARENT = "parent";
        public static final String CHILD = "child";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STEP_STEP).build();

        public static Uri buildUri(int id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class CategoryEntry implements BaseColumns{
        public static final String TABLE_NAME = PATH_CATEGORY;

        public static final String NAME = "name";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CATEGORY).build();

        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
