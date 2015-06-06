package com.frogs42.cookbook.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.frogs42.cookbook.MainActivity;
import com.frogs42.cookbook.R;
import com.frogs42.cookbook.data.Contract;
import com.frogs42.cookbook.data.DbAdapter;
import com.frogs42.cookbook.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by igor on 05.06.15.
 */
public class RecipesListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Recipe> mObjects;
    private boolean[] mFavoriteStates;

    public RecipesListAdapter(Context context) {
        mContext = context;
        mObjects = DbAdapter.getRecipesList(mContext);

        Log.v(MainActivity.class.getName(), "RecipesListAdapter, favorites from constructor:");
        mFavoriteStates = new boolean[mObjects.size()];
        for (int i = 0; i < mObjects.size() ; i++) {
            mFavoriteStates[i] = mObjects.get(i).isFavorite();
            Log.v(MainActivity.class.getName(), String.valueOf(mFavoriteStates[i]));
        }
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public Recipe getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    private class ViewHolder {
        TextView textView;
        CheckBox star;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_list_recipe, null);

            ViewHolder holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.recipe);
            holder.star = (CheckBox) convertView.findViewById(R.id.btn_star);
            holder.star.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

//                    final int position = ((ListView) parent).getPositionForView(buttonView);
                    Log.v(MainActivity.class.getName(), "This method has been called!!!");
                    if (position != ListView.INVALID_POSITION) {
                        Log.v(MainActivity.class.getName(), "Position: " + position);

                        mFavoriteStates[position] = isChecked;

                        Recipe recipe = mObjects.get(position);
                        ContentValues values = new ContentValues();
                        values.put(Contract.RecipeEntry._ID, recipe.getId());
                        values.put(Contract.RecipeEntry.FAVORITE, isChecked);
                        int count = mContext.getContentResolver().update(
                                Contract.RecipeEntry.buildUri(recipe.getId()),
                                values, Contract.RecipeEntry._ID + "= ?",
                                new String[] { Long.toString(recipe.getId())});
                        Recipe recipe2 = DbAdapter.getRecipe(mContext, recipe.getId());
                        Log.v(MainActivity.class.getName(), "From RecipesListAdapter after retrieving the same element");
                        Log.v(MainActivity.class.getName(), recipe2.getTitle());
                        Log.v(MainActivity.class.getName(), (recipe2.isFavorite() ? "True" : "False"));
                    }
                }
            });

            convertView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.star.setChecked(mFavoriteStates[position]);
        holder.textView.setText(getItem(position).getTitle());

        return convertView;
    }
}
