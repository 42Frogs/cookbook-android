package com.frogs42.cookbook.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frogs42.cookbook.R;
import com.frogs42.cookbook.adapters.CookingAdapter;
import com.frogs42.cookbook.model.Recipe;

/**
 * A simple {@link Fragment} subclass.
 */
public class CookingFragment extends Fragment {

    private RecyclerView recyclerView;
    private Recipe recipe;

    public CookingFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cooking, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.cooking);
        setUpRecyclerView();

        return rootView;
    }

    public CookingFragment setRecipe(Recipe recipe){
        this.recipe = recipe;
        return this;
    }

    private void setUpRecyclerView(){
        CookingAdapter adapter = new CookingAdapter(getActivity(),recipe);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

}
