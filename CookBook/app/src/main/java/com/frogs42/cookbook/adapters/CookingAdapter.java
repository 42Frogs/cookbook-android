package com.frogs42.cookbook.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

/**
 * Created by ilia on 16.05.15.
 */
public class CookingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static Progress progress;
    private Recipe recipe;
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
        progress = new Progress(recipe.getRecipeSteps());
        this.recipe = recipe;

        for(RecipeStep step : recipe.getRecipeSteps()) {
            boolean parent_completed = true;
            for (RecipeStep parent : step.getParentSteps())
                if (!progress.getStatus(parent).equals(COMPLETED)) {
                    parent_completed = false;
                    break;
                }
            if(parent_completed) progress.addStatus(AVAILABLE);
            else progress.addStatus(UNAVAILABLE);
        }
        progress.sort();
    }

    private void unlockStep(){
        for(RecipeStep step : recipe.getRecipeSteps())
            if(progress.getStatus(step).equals(UNAVAILABLE)) {
                Integer availability = AVAILABLE;
                int parent_position = progress.getNonCompletedCount();
                for (RecipeStep parent : step.getParentSteps()) {
                    if (!progress.getStatus(parent).equals(COMPLETED)) {
                        availability = UNAVAILABLE;
                        break;
                    }
                    int current_index = progress.indexOf(parent);
                    if (current_index <= parent_position)
                        parent_position = current_index;
                }

                if(availability.equals(AVAILABLE)) {
                    progress.setStatus(step, availability);
                    if(parent_position >= 0)
                        progress.move(step, parent_position);
                }
            }
    }

//    private boolean hasCommonChild(RecipeStep a, RecipeStep b){
//        Object[] steps = progress.keySet().toArray();
//        for(Object step : steps) {
//            ArrayList<RecipeStep> parents = ((RecipeStep) step).getParentSteps();
//            if (parents.contains(a) && parents.contains(b))
//                return true;
//        }
//        return false;
//    }

    @Override
    public int getItemCount() {
        int count = progress.getNonCompletedCount();
        //count++;  //TODO add header
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
//        Log.e("onBind",String.valueOf(position));
        RecipeStep step = progress.getStep(position);
        ((StepsViewHolder) holder).recipeStep = step;
        ((StepsViewHolder) holder).name.setText(step.getTitle());
//        Log.e("step",step.getTitle() + " " + String.valueOf(progress.getStatus(step)));
        if(progress.getStatus(step).equals(AVAILABLE))
            ((StepsViewHolder) holder).name.setTextAppearance(mContext,R.style.available_step);
        if(progress.getStatus(step).equals(UNAVAILABLE))
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
                    progress.setStatus(recipeStep, COMPLETED);
                    adapter.unlockStep();
                    progress.move(recipeStep, progress.getNonCompletedCount());
                }
            });
        }


    }

    private class Progress{
        private ArrayList<RecipeStep> stepList;
        private ArrayList<Integer> statusList;

        public Progress(ArrayList<RecipeStep> steps){
            stepList = new ArrayList<>(steps);
            statusList = new ArrayList<>();
        }

        public RecipeStep getStep(int position){
            return stepList.get(position);
        }

        public Integer getStatus(RecipeStep step){
            return statusList.get(stepList.indexOf(step));
        }

        public void setStatus(RecipeStep step, Integer status){
            statusList.set(stepList.indexOf(step), status);
        }

        public void addStatus(Integer status){
            this.statusList.add(status);
        }

        public int getNonCompletedCount(){
            int count = 0;
            for(Integer i: statusList)
                if(!i.equals(COMPLETED)) count++;
            return count;
        }

        public int indexOf(RecipeStep step){
            return stepList.indexOf(step);
        }

        public void move(RecipeStep step, int to){
            int from = stepList.indexOf(step);
            stepList.remove(from);
            stepList.add(to, step);

            Integer status = statusList.get(from);
            statusList.remove(from);
            statusList.add(to, status);

            if(!status.equals(COMPLETED)) {
                adapter.notifyItemMoved(from, to);
                adapter.notifyItemChanged(to);
            }
            else
                adapter.notifyItemRemoved(from);
        }

        public void sort(){
            ArrayList<RecipeStep> tempList = stepList;
            Collections.sort(tempList, new Comparator<RecipeStep>() {
                public int compare(RecipeStep a, RecipeStep b) {
                    return statusList.get(stepList.indexOf(a)).compareTo(statusList.get(stepList.indexOf(b)));
                }
            });
            Collections.sort(statusList);
            stepList = tempList;
        }
    }
}
