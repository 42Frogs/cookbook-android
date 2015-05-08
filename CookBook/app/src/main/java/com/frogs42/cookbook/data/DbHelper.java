package com.frogs42.cookbook.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.frogs42.cookbook.data.Contract.RecipeEntry;
import com.frogs42.cookbook.data.Contract.IngredientEntry;
import com.frogs42.cookbook.data.Contract.RecipeIngredientEntry;
import com.frogs42.cookbook.data.Contract.StepEntry;
import com.frogs42.cookbook.data.Contract.StepStepEntry;
import com.frogs42.cookbook.data.Contract.CategoryEntry;

/**
 * Created by ilia on 07.05.15.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "cookbook.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_RECIPE = "CREATE TABLE " + RecipeEntry.TABLE_NAME + " (" +
                RecipeEntry._ID + " INTEGER NOT NULL, " +
                RecipeEntry.NAME + " varchar(255) NOT NULL, " +
                RecipeEntry.DESCRIPTION + " varchar(2000), " +
                RecipeEntry.IMAGE_PATH + " varchar(255), " +
                RecipeEntry.FAVORITE + " integer(1) NOT NULL, " +
                RecipeEntry.CATEGORY_ID + " bigint(19) NOT NULL, " +
                "PRIMARY KEY (" + RecipeEntry._ID + "), " +
                "FOREIGN KEY(" + RecipeEntry.CATEGORY_ID + ") REFERENCES " + CategoryEntry.TABLE_NAME + "(" + CategoryEntry._ID +"));";

        final String SQL_CREATE_INGREDIENT = "CREATE TABLE " + IngredientEntry.TABLE_NAME + " (" +
                IngredientEntry._ID + " INTEGER NOT NULL, " +
                IngredientEntry.NAME + " varchar(255) NOT NULL, " +
                "PRIMARY KEY (" + IngredientEntry._ID + "));";

        final String SQL_CREATE_RECIPE_INGREDIENT = "CREATE TABLE " + RecipeIngredientEntry.TABLE_NAME + " (" +
                RecipeIngredientEntry.RECIPE_ID + " bigint(19) NOT NULL, " +
                RecipeIngredientEntry.INGREDIENT_ID + " bigint(19) NOT NULL, " +
                RecipeIngredientEntry.SIZE + " decimal(10, 2) NOT NULL, " +
                RecipeIngredientEntry.MEASURE + " varchar(255) NOT NULL, " +
                "PRIMARY KEY (" + RecipeIngredientEntry.RECIPE_ID + ", " + RecipeIngredientEntry.INGREDIENT_ID + "), " +
                "FOREIGN KEY(" + RecipeIngredientEntry.RECIPE_ID + ") REFERENCES " + RecipeEntry.TABLE_NAME + "(" + RecipeEntry._ID + "), " +
                "FOREIGN KEY(" + RecipeIngredientEntry.INGREDIENT_ID + ") REFERENCES " + IngredientEntry.TABLE_NAME + "(" + IngredientEntry._ID + "));";
        final String SQL_CREATE_STEP = "CREATE TABLE " + StepEntry.TABLE_NAME + " (" +
                StepEntry._ID + " INTEGER NOT NULL, " +
                StepEntry.NAME + " varchar(255) NOT NULL, " +
                StepEntry.DESCRIPTION + " varchar(255) NOT NULL, " +
                StepEntry.RECIPE_ID + " bigint(19) NOT NULL, " +
                StepEntry.TIMER + " integer(10), " +
                StepEntry.IMAGE_PATH + " varchar(255), " +
                "PRIMARY KEY (" + StepEntry._ID + "), " +
                "FOREIGN KEY(" + StepEntry.RECIPE_ID + ") REFERENCES " + RecipeEntry.TABLE_NAME + "(" + RecipeEntry._ID + "));";
        final String SQL_CREATE_STEP_STEP = "CREATE TABLE " + StepStepEntry.TABLE_NAME +" (" +
                StepStepEntry.PARENT + " bigint(19) NOT NULL, " +
                StepStepEntry.CHILD + " bigint(19) NOT NULL, " +
                "PRIMARY KEY (" + StepStepEntry.PARENT +", " + StepStepEntry.CHILD + "), " +
                "FOREIGN KEY(" + StepStepEntry.PARENT + ") REFERENCES " + StepEntry.TABLE_NAME + "(" + StepEntry._ID + "), " +
                "FOREIGN KEY(" + StepStepEntry.CHILD + ") REFERENCES " + StepEntry.TABLE_NAME + "(" + StepEntry._ID + "));";
        final String SQL_CREATE_CATEGORY = "CREATE TABLE " + CategoryEntry.TABLE_NAME + " (" +
                CategoryEntry._ID + " INTEGER NOT NULL, " +
                CategoryEntry.NAME + " varchar(255) NOT NULL, " +
                "PRIMARY KEY (" + CategoryEntry._ID + "));";
        sqLiteDatabase.execSQL(SQL_CREATE_CATEGORY);
        sqLiteDatabase.execSQL(SQL_CREATE_INGREDIENT);
        sqLiteDatabase.execSQL(SQL_CREATE_STEP);
        sqLiteDatabase.execSQL(SQL_CREATE_RECIPE);
        sqLiteDatabase.execSQL(SQL_CREATE_RECIPE_INGREDIENT);
        sqLiteDatabase.execSQL(SQL_CREATE_STEP_STEP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CategoryEntry.TABLE_NAME + ";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + IngredientEntry.TABLE_NAME + ";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeEntry.TABLE_NAME + ";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeIngredientEntry.TABLE_NAME + ";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + StepStepEntry.TABLE_NAME + ";");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + StepEntry.TABLE_NAME + ";");
        onCreate(sqLiteDatabase);
    }
}
