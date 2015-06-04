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
import com.frogs42.cookbook.utils.TimersManager;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;

public class CookingFragment extends Fragment {

    private RecyclerView recyclerView;
    private Recipe recipe;
    private RecyclerViewSwipeManager mRecyclerViewSwipeManager;
    private RecyclerViewTouchActionGuardManager mRecyclerViewTouchActionGuardManager;

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
        mRecyclerViewTouchActionGuardManager = new RecyclerViewTouchActionGuardManager();
        mRecyclerViewTouchActionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(true);
        mRecyclerViewTouchActionGuardManager.setEnabled(true);
        mRecyclerViewSwipeManager = new RecyclerViewSwipeManager();
        final GeneralItemAnimator animator = new SwipeDismissItemAnimator();
        animator.setSupportsChangeAnimations(false);
        recyclerView.setItemAnimator(animator);

        final CookingAdapter adapter = new CookingAdapter(getActivity(), recipe);
        TimersManager.addDataListener(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mRecyclerViewSwipeManager.createWrappedAdapter(adapter));

        mRecyclerViewTouchActionGuardManager.attachRecyclerView(recyclerView);
        mRecyclerViewSwipeManager.attachRecyclerView(recyclerView);
    }
}
