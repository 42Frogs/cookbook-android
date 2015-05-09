package com.frogs42.cookbook.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by ilia on 08.05.15.
 */
public class CookBookProvider  extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DbHelper mOpenHelper;

    private static final int RECIPE = 100;
    private static final int RECIPE_INGREDIENT = 200;
    private static final int STEP = 300;
    private static final int STEP_PARENTS = 400;

    static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_RECIPE + "/#",RECIPE);
        uriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_RECIPE_INGREDIENT + "/#",RECIPE_INGREDIENT);
        uriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_STEP + "/#",STEP);
        uriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_STEP_STEP + "/#",STEP_PARENTS);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)){
            case RECIPE:
                retCursor = mOpenHelper.getReadableDatabase().query(Contract.RecipeEntry.TABLE_NAME,
                        projection,
                        Contract.RecipeEntry._ID + " = ?",
                        new String[]{uri.getPathSegments().get(1)},
                        null,
                        null,
                        sortOrder);
                break;
            case RECIPE_INGREDIENT:
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(
                        Contract.RecipeIngredientEntry.TABLE_NAME + " , " +
                                Contract.IngredientEntry.TABLE_NAME);
                retCursor = builder.query(mOpenHelper.getReadableDatabase(),
                        new String[]{Contract.IngredientEntry._ID,
                                Contract.IngredientEntry.NAME,
                                Contract.RecipeIngredientEntry.SIZE,
                                Contract.RecipeIngredientEntry.MEASURE},
                        Contract.RecipeIngredientEntry.RECIPE_ID + " = ? AND " +
                                Contract.RecipeIngredientEntry.INGREDIENT_ID + " = ?",
                        new String[]{uri.getPathSegments().get(1), Contract.IngredientEntry._ID},
                        null,
                        null,
                        sortOrder);
                break;
            case STEP:
                retCursor = mOpenHelper.getReadableDatabase().query(Contract.StepEntry.TABLE_NAME,
                        new String[]{Contract.StepEntry._ID,
                                Contract.StepEntry.DESCRIPTION,
                                Contract.StepEntry.NAME,
                                Contract.StepEntry.IMAGE_PATH,
                                Contract.StepEntry.TIMER},
                        Contract.StepEntry.RECIPE_ID + " = ?",
                        new String[]{uri.getPathSegments().get(1)},
                        null,
                        null,
                        sortOrder);
                break;
            case STEP_PARENTS:
                retCursor = mOpenHelper.getReadableDatabase().query(Contract.StepStepEntry.TABLE_NAME,
                        new String[]{Contract.StepStepEntry.PARENT},
                        Contract.StepStepEntry.CHILD + " = ?",
                        new String[]{uri.getPathSegments().get(1)},
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return Uri.EMPTY;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        return 0;
    }
}

