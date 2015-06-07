package com.frogs42.cookbook.fragments;


import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.frogs42.cookbook.R;
import com.frogs42.cookbook.adapters.CookingAdapter;
import com.frogs42.cookbook.data.DataStore;
import com.frogs42.cookbook.model.Recipe;
import com.frogs42.cookbook.utils.EventsManager;
import com.frogs42.cookbook.utils.GlobalEvents;
import com.frogs42.cookbook.utils.TimersManager;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;

public class CookingFragment extends Fragment implements EventsManager.EventHandler {

    private RecyclerView mRecyclerView;
    private FloatingActionButton mFab;
    private CookingAdapter mAdapter;
    private Recipe mRecipe;
    private RecyclerViewSwipeManager mRecyclerViewSwipeManager;
    private RecyclerViewTouchActionGuardManager mRecyclerViewTouchActionGuardManager;

    public CookingFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cooking, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.cooking);
        setUpRecyclerView();

        mFab = (FloatingActionButton) rootView.findViewById(R.id.start);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.startCooking();

                Toast.makeText(getActivity(), getString(R.string.cooking_started), Toast.LENGTH_SHORT).show();
                //TODO dialog with calculator for ingredients
            }
        });

        mFab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(),getString(R.string.start_cooking),Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        if(DataStore.getActiveRecipesList().contains(mRecipe)) {
            mFab.setVisibility(View.GONE);
        }

        EventsManager.addHandler(GlobalEvents.EVENT_RECIPE_COOKING_FINISHED,this);
        EventsManager.addHandler(GlobalEvents.EVENT_RECIPE_COOKING_STARTED,this);

        return rootView;
    }

    public CookingFragment setRecipe(Recipe recipe){
        this.mRecipe = recipe;
        return this;
    }

    private void setUpRecyclerView(){
        mRecyclerViewTouchActionGuardManager = new RecyclerViewTouchActionGuardManager();
        mRecyclerViewTouchActionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(true);
        mRecyclerViewTouchActionGuardManager.setEnabled(true);
        mRecyclerViewSwipeManager = new RecyclerViewSwipeManager();

        final GeneralItemAnimator animator = new SwipeDismissItemAnimator();
        animator.setSupportsChangeAnimations(false);
        mRecyclerView.setItemAnimator(animator);

        mAdapter = new CookingAdapter(getActivity(), mRecipe);
        TimersManager.addDataListener(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mRecyclerViewSwipeManager.createWrappedAdapter(mAdapter));
        mRecyclerView.addItemDecoration(new SimpleListDividerDecorator(getActivity().
                obtainStyledAttributes(new int[]{android.R.attr.listDivider}).getDrawable(0), true));

        mRecyclerViewTouchActionGuardManager.attachRecyclerView(mRecyclerView);
        mRecyclerViewSwipeManager.attachRecyclerView(mRecyclerView);
    }

    @Override
    public void handleEvent(String eventType, Object eventData) {
        if(eventType.equals(GlobalEvents.EVENT_RECIPE_COOKING_FINISHED)) {
            mFab.setVisibility(View.VISIBLE);
        }else
            mFab.setVisibility(View.GONE);
    }
}
