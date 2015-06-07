package com.frogs42.cookbook;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.frogs42.cookbook.data.DataStore;
import com.frogs42.cookbook.data.DbAdapter;
import com.frogs42.cookbook.fragments.CookingFragment;
import com.frogs42.cookbook.fragments.MainPagerFragment;
import com.frogs42.cookbook.fragments.RecipesListFragment;
import com.frogs42.cookbook.fragments.TimersListFragment;
import com.frogs42.cookbook.model.IngredientEntry;
import com.frogs42.cookbook.model.Recipe;
import com.frogs42.cookbook.model.RecipeStep;
import com.frogs42.cookbook.utils.EventsManager;
import com.frogs42.cookbook.utils.GlobalEvents;
import com.frogs42.cookbook.utils.TimersManager;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements EventsManager.EventHandler {

    Recipe recipe;
    ArrayList<Recipe> recipesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Move to theme definition
        ActionBar bar = getSupportActionBar();
        if (bar != null)
            bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#229922")));

        // init helpers
        EventsManager.init(this);
        TimersManager.init(this);
        DataStore.init(this);

        // TODO: move to DataStore
        recipe = DbAdapter.getRecipe(this, 1);
        /*if(recipe != null) {
            Log.e("name", recipe.getTitle());
            for(IngredientEntry i : recipe.getIngredients())
                Log.e("ingredient",i.getIngredient().getName() + " " + i.getAmount() + " " + i.getMeasure());
            for(RecipeStep step : recipe.getRecipeSteps()) {
                Log.e("step", step.getTitle() + " ");
                for(RecipeStep s : step.getParentSteps())
                    Log.e("parent",s.getTitle());
            }
        }else Log.e("recipe","is null");*/

        recipesList = DbAdapter.getRecipesList(this);

        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragments_container,
                new MainPagerFragment()
        ).commit();

        EventsManager.addHandler(GlobalEvents.EVENT_RECIPE_SELECTED, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_timers) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new TimersListFragment()).commit();
            return true;
        }
        if (id == R.id.action_recipe) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new CookingFragment().setRecipe(recipe)).commit();
            return true;
        }
        if (id == R.id.action_recipes_list) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new RecipesListFragment().setRecipesList(recipesList)).commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        TimersManager.terminate();
        EventsManager.terminate();
        DataStore.terminate();
        super.onDestroy();
    }

    @Override
    public void handleEvent(String eventType, Object eventData) {
        if (GlobalEvents.EVENT_RECIPE_SELECTED.equals(eventType)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragments_container, new CookingFragment().setRecipe(recipe))
                    .addToBackStack(null)
                    .commit();
        }
    }
}
