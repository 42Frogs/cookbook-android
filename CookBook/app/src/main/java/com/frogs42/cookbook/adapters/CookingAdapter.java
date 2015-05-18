package com.frogs42.cookbook.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frogs42.cookbook.R;
import com.frogs42.cookbook.model.Recipe;
import com.frogs42.cookbook.model.RecipeStep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by ilia on 16.05.15.
 */
public class CookingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static LinkedHashMap<RecipeStep,Integer> progress;
    private final static int AVAILABLE = 0;
    private final static int UNAVAILABLE = 1;
    private final static int COMPLETED = 2;

    private final int TYPE_HEADER = 0;
    private final int TYPE_STEP = 1;

    private Context mContext;
    private static CookingAdapter adapter;

    public CookingAdapter(Context context,Recipe recipe){
        adapter = this;
        mContext = context;
        progress = new LinkedHashMap<>();

        for(RecipeStep step : recipe.getRecipeSteps())
            progress.put(step,AVAILABLE);

        for(RecipeStep step : recipe.getRecipeSteps())
            for (RecipeStep parent : step.getParentSteps())
                if (!progress.get(parent).equals(COMPLETED)) {
                    progress.put(step, UNAVAILABLE);
                    break;
                }
        sort(progress);
    }

    private void sort(LinkedHashMap<RecipeStep,Integer> map){

        Object[] recipeSteps = map.keySet().toArray();
        for(Object step : recipeSteps)
            if(map.get(step).equals(UNAVAILABLE)) {
                Integer availability = AVAILABLE;
                for (RecipeStep parent : ((RecipeStep) step).getParentSteps())
                    if (!map.get(parent).equals(COMPLETED)) {
                        availability = UNAVAILABLE;
                        break;
                    }

                map.put((RecipeStep) step,availability);
            }

        List<LinkedHashMap.Entry<RecipeStep, Integer>> entries = new ArrayList<>(map.entrySet());
        Collections.sort(entries, new Comparator<LinkedHashMap.Entry<RecipeStep, Integer>>() {
            public int compare(LinkedHashMap.Entry<RecipeStep, Integer> a, LinkedHashMap.Entry<RecipeStep, Integer> b) {
                return a.getValue().compareTo(b.getValue());
            }
        });
        map.clear();
        for (LinkedHashMap.Entry<RecipeStep, Integer> entry : entries) {
            map.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public int getItemCount() {
        if(!progress.containsValue(COMPLETED))
            return progress.size(); //TODO add header

        int count = 0;
        Object[] recipeSteps = progress.keySet().toArray();
        for(Object step : recipeSteps)
            if(!progress.get(step).equals(COMPLETED))
                count++;

        return count;
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_STEP;   //TODO add header
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_HEADER:
                break;

            case TYPE_STEP:
                return new StepsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.step,parent,false));
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecipeStep step = (RecipeStep) progress.keySet().toArray()[position];
        ((StepsViewHolder) holder).recipeStep = step;
        ((StepsViewHolder) holder).name.setText(step.getTitle());
        if(progress.get(step).equals(AVAILABLE))
            ((StepsViewHolder) holder).name.setTextAppearance(mContext,R.style.available_step);
        if(progress.get(step).equals(UNAVAILABLE))
            ((StepsViewHolder) holder).name.setTextAppearance(mContext,R.style.unavailable_step);
    }

    public static class StepsViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public RecipeStep recipeStep;
        public StepsViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progress.put(recipeStep,COMPLETED);
                    adapter.sort(progress);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }
}
