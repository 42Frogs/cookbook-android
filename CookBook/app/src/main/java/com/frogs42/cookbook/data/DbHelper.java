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
                StepEntry.DESCRIPTION + " varchar(255) NOT NULL, " +
                StepEntry.RECIPE_ID + " bigint(19) NOT NULL, " +
                StepEntry.TIMER + " integer(10), " +
                StepEntry.NAME + " varchar(255) NOT NULL, " +
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
        for(String s : getBorschSQL())
            sqLiteDatabase.execSQL(s);
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

    private String[] getBorschSQL(){
        return new String[]{
                "INSERT INTO recipe VALUES(1,'Борщ с курицей','Национальное украинское блюдо','file:///android_asset/borsch_done.jpg',0,1);",
                "INSERT INTO ingredient VALUES(1,'Свекла');",
                "INSERT INTO ingredient VALUES(2,'Курица');",
                "INSERT INTO ingredient VALUES(3,'Картофель');",
                "INSERT INTO ingredient VALUES(4,'Белокочанная капуста');",
                "INSERT INTO ingredient VALUES(5,'Лук репчатый');",
                "INSERT INTO ingredient VALUES(6,'Морковь');",
                "INSERT INTO ingredient VALUES(7,'Чеснок');",
                "INSERT INTO ingredient VALUES(8,'Сметана');",
                "INSERT INTO ingredient VALUES(9,'Томатная паста');",
                "INSERT INTO recipe_ingredient VALUES(1,1,1,'шт');",
                "INSERT INTO recipe_ingredient VALUES(1,2,600,'г');",
                "INSERT INTO recipe_ingredient VALUES(1,3,4,'шт');",
                "INSERT INTO recipe_ingredient VALUES(1,4,200,'г');",
                "INSERT INTO recipe_ingredient VALUES(1,5,1,'шт');",
                "INSERT INTO recipe_ingredient VALUES(1,6,2,'шт');",
                "INSERT INTO recipe_ingredient VALUES(1,7,4,'зубка');",
                "INSERT INTO recipe_ingredient VALUES(1,8,0,'по вкусу');",
                "INSERT INTO recipe_ingredient VALUES(1,9,1,'столовая ложка');",
                "INSERT INTO category VALUES(1,'Первое');",
                "INSERT INTO step VALUES(1,'Курицу разморозить',1,NULL,'Курицу разморозить','file:///android_asset/chicken.jpg');",
                "INSERT INTO step VALUES(2,'Нарезать картошку',1,NULL,'Нарезать картошку','file:///android_asset/potato.jpg');",
                "INSERT INTO step VALUES(3,'Капусту нашинковать',1,NULL,'Капусту нашинковать','file:///android_asset/cabbage.jpg');",
                "INSERT INTO step VALUES(4,'Свеклу натереть на терке',1,NULL,'Свеклу (крупную) натереть на терке','file:///android_asset/beet.jpg');",
                "INSERT INTO step VALUES(5,'Морковь натереть на терке',1,NULL,'Морковь натереть на терке','file:///android_asset/carrot.jpg');",
                "INSERT INTO step VALUES(6,'Лук мелко порезать',1,NULL,'Лук мелко порезать','file:///android_asset/onion.jpg');",
                "INSERT INTO step VALUES(7,'Раздавить чеснок',1,NULL,'Раздавить чеснок','file:///android_asset/garlic.jpg');",
                "INSERT INTO step VALUES(8,'Курицу разделить на части и поставить варить. Посолить бульон. Ждать, пока закипит',1,600,'Курицу разделить на части и поставить варить. Посолить бульон. Ждать, пока закипит','file:///android_asset/bouillon.jpeg');",
                "INSERT INTO step VALUES(9,'Добавить картошку',1,NULL,'Добавить картошку','file:///android_asset/potato_to_b.png');",
                "INSERT INTO step VALUES(10,'Капусту добавить в бульон',1,NULL,'Капусту добавить в бульон','file:///android_asset/cabbage_to_b.png');",
                "INSERT INTO step VALUES(11,'Обжарить в подсолнечном масле 5 минут',1,300,'Обжарить в подсолнечном масле 5 минут','file:///android_asset/obzharit-sveklu-morkov-i-luk.jpg');",
                "INSERT INTO step VALUES(12,' Ждать пока картошка станет мягкой',1,300,' Ждать пока картошка станет мягкой','file:///android_asset/chilledchickenbroth.jpg');",
                "INSERT INTO step VALUES(13,'Вложить томатную пасту',1,NULL,'Вложить томатную пасту','file:///android_asset/tomato.jpg');",
                "INSERT INTO step VALUES(14,' Как только картошка стала мягкой, добавить заправку',1,NULL,' Как только картошка стала мягкой, добавить заправку','file:///android_asset/borsch.jpg');",
                "INSERT INTO step VALUES(15,'Оставить еще на 3–5 минут',1,240,'Оставить еще на 3–5 минут','file:///android_asset/borsch_almost_done.jpg');",
                "INSERT INTO step VALUES(16,' При подаче на стол добавить сметаны, можно украсить зеленью',1,NULL,' При подаче на стол добавить сметаны, можно украсить зеленью','file:///android_asset/borsch_done.jpg');",
                "INSERT INTO step_step VALUES(1,8);",
                "INSERT INTO step_step VALUES(2,9);",
                "INSERT INTO step_step VALUES(3,10);",
                "INSERT INTO step_step VALUES(4,11);",
                "INSERT INTO step_step VALUES(5,11);",
                "INSERT INTO step_step VALUES(6,11);",
                "INSERT INTO step_step VALUES(7,11);",
                "INSERT INTO step_step VALUES(9,12);",
                "INSERT INTO step_step VALUES(11,13);",
                "INSERT INTO step_step VALUES(8,9);",
                "INSERT INTO step_step VALUES(12,14);",
                "INSERT INTO step_step VALUES(13,14);",
                "INSERT INTO step_step VALUES(14,15);",
                "INSERT INTO step_step VALUES(15,16);",
                "INSERT INTO step_step VALUES(8,10);",
                "INSERT INTO recipe VALUES(2,'Суп харчо',NULL,NULL,0,1);",
                "INSERT INTO recipe VALUES(3,'Марокканский суп',NULL,NULL,0,1);",
                "INSERT INTO recipe VALUES(4,'Суп с чечевицей и болгарским перцем',NULL,NULL,0,1);",
                "INSERT INTO recipe VALUES(5,'Холодный суп а-ля окрошка',NULL,NULL,0,1);",
                "INSERT INTO recipe VALUES(6,'Суп-пюре из чечевицы',NULL,NULL,0,1);",
                "INSERT INTO recipe VALUES(7,'Солянка',NULL,NULL,0,1);",
                "INSERT INTO recipe VALUES(8,'Томатный суп из морепродуктов',NULL,NULL,0,1);",
                "INSERT INTO recipe VALUES(9,'Тыквенный суп с имбирем',NULL,NULL,0,1);",
                "INSERT INTO recipe VALUES(10,'Суп-пюре из баклажанов',NULL,NULL,0,1);",
                "INSERT INTO recipe VALUES(11,'Гороховый суп',NULL,NULL,0,1);",
                "INSERT INTO recipe VALUES(12,'Суп с фрикадельками',NULL,NULL,0,1);"

        };
    }
}
