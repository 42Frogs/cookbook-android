package com.frogs42.cookbook;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.frogs42.cookbook.data.Contract;
import com.frogs42.cookbook.data.DbAdapter;
import com.frogs42.cookbook.fragments.CookingFragment;
import com.frogs42.cookbook.fragments.TimersListFragment;
import com.frogs42.cookbook.model.IngredientEntry;
import com.frogs42.cookbook.model.Recipe;
import com.frogs42.cookbook.model.RecipeStep;
import com.frogs42.cookbook.utils.EventsManager;
import com.frogs42.cookbook.utils.TimersManager;


public class MainActivity extends ActionBarActivity {

    Recipe recipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init helpers
        EventsManager.init(this);
        TimersManager.init(this);

        recipe = DbAdapter.getRecipe(this, 1);
        if(recipe != null) {
            Log.e("name", recipe.getTitle());
            for(IngredientEntry i : recipe.getIngredients())
                Log.e("ingredient",i.getIngredient().getName() + " " + i.getAmount() + " " + i.getMeasure());
            for(RecipeStep step : recipe.getRecipeSteps()) {
                Log.e("step", step.getTitle() + " ");
                for(RecipeStep s : step.getParentSteps())
                    Log.e("parent",s.getTitle());
            }
        }else Log.e("recipe","is null");
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        TimersManager.terminate();
        EventsManager.terminate();
        super.onDestroy();
    }
}
