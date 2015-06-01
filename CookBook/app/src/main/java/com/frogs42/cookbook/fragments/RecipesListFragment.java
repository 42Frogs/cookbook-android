package com.frogs42.cookbook.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.frogs42.cookbook.R;
import com.frogs42.cookbook.model.Recipe;

import java.util.ArrayList;

public class RecipesListFragment extends Fragment {

    private ArrayList<Recipe> mRecipesList;

    public RecipesListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_recipes_list, container, false);

        ArrayList<String> recipeStr = new ArrayList<>();
        for (int i = 0; i < mRecipesList.size(); i++) {
            recipeStr.add(mRecipesList.get(i).getTitle());
        }
        ArrayAdapter<String> mRecipeAdapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.item_list_recipe,
                R.id.recipe,
                recipeStr
        );
        ListView mRecipesListView = (ListView) mView.findViewById(R.id.recipes_list);
        mRecipesListView.setAdapter(mRecipeAdapter);
        return mView;
    }


    public RecipesListFragment setRecipesList(ArrayList<Recipe> recipesList) {
        this.mRecipesList = recipesList;
        return this;
    }
}
