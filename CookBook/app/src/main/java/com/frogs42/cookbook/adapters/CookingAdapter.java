package com.frogs42.cookbook.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frogs42.cookbook.R;
import com.frogs42.cookbook.model.Recipe;
import com.frogs42.cookbook.model.RecipeStep;
import com.frogs42.cookbook.utils.CookTimer;
import com.frogs42.cookbook.utils.TimersManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by ilia on 16.05.15.
 */
public class CookingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements SwipeableItemAdapter<CookingAdapter.StepsViewHolder>, TimersManager.DataListener {

    private static Progress progress;
    private Recipe recipe;
    private final static int RUNNING = 0;
    private final static int AVAILABLE = 1;
    private final static int UNAVAILABLE = 2;
    private final static int COMPLETED = 3;

    private final int TYPE_HEADER = 0;
    private final int TYPE_STEP = 1;

    private static int expandedPosition = -1;

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

        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return progress.getStep(position).getId();
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
        if(progress.getStatus(step).equals(AVAILABLE) || progress.getStatus(step).equals(RUNNING))
            ((StepsViewHolder) holder).name.setTextAppearance(mContext,R.style.available_step);
        if(progress.getStatus(step).equals(UNAVAILABLE))
            ((StepsViewHolder) holder).name.setTextAppearance(mContext,R.style.unavailable_step);

        ((StepsViewHolder) holder).description.setText(step.getDescription());

        if (position == expandedPosition)
            ((StepsViewHolder) holder).description.setVisibility(View.VISIBLE);
         else
            ((StepsViewHolder) holder).description.setVisibility(View.GONE);


        ((StepsViewHolder) holder).setSwipeItemSlideAmount(0);
    }

    @Override
    public void onSetSwipeBackground(StepsViewHolder holder, int position, int type) {
        int bgRes = 0;
//        Log.e("background",String.valueOf(position) + " " + String.valueOf(holder.recipeStep.getDurationInSeconds()));
        if(type == RecyclerViewSwipeManager.DRAWABLE_SWIPE_LEFT_BACKGROUND) {
            if(holder.recipeStep.getDurationInSeconds() > 0)
                bgRes = R.drawable.swipe_timer_background;
            else
                bgRes = R.drawable.swipe_done_background;
        }
        if(type == RecyclerViewSwipeManager.DRAWABLE_SWIPE_RIGHT_BACKGROUND) {
            if(holder.recipeStep.getDurationInSeconds() > 0)
                bgRes = R.drawable.swipe_timer_right_background;
            else
                bgRes = R.drawable.swipe_done_right_background;
        }
        holder.itemView.setBackgroundResource(bgRes);
    }

    @Override
    public int onGetSwipeReactionType(StepsViewHolder holder, int position, int x, int y) {
        if(progress.getStatus(progress.getStep(position)).equals(AVAILABLE) ||
                progress.getStatus(progress.getStep(position)).equals(RUNNING))
            return RecyclerViewSwipeManager.REACTION_CAN_SWIPE_BOTH;
        else
            return RecyclerViewSwipeManager.REACTION_CAN_NOT_SWIPE_BOTH_WITH_RUBBER_BAND_EFFECT;
    }

    @Override
    public int onSwipeItem(StepsViewHolder holder, int position, int result) {
        if(result == RecyclerViewSwipeManager.RESULT_SWIPED_LEFT || result == RecyclerViewSwipeManager.RESULT_SWIPED_RIGHT) {
            if(holder.recipeStep.getDurationInSeconds() > 0) {
                RecipeStep step = progress.getStep(position);
                if(!progress.getStatus(step).equals(RUNNING)) {
                    TimersManager.addTimer(step.getId(), 5);//step.getDurationInSeconds());
                    progress.setStatus(step, RUNNING);
                    progress.move(step, progress.getStepsCount(RUNNING) - 1);
                    return RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_DEFAULT;
                }else{
                    showDialog(step);
                    return RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_DEFAULT;
                }
            }
            return RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_REMOVE_ITEM;
        }
        return RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_DEFAULT;
    }

    @Override
    public void onPerformAfterSwipeReaction(StepsViewHolder holder, int position, int result, int reaction) {
        if (reaction == RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_REMOVE_ITEM) {

            if(expandedPosition == position) expandedPosition = -1;
            if(expandedPosition > position) expandedPosition--;

            completeStep(progress.getStep(position));
        }
    }

    @Override
    public void onDataChanged(CookTimer caller){
        Log.e("timer",String.valueOf(caller.getID()) + String.valueOf(caller.getRemainingSeconds()));
    }

    @Override
    public void onTimerFinished(CookTimer caller){
        for(int i = 0; i < progress.getStepsCount(); i++) {
            RecipeStep step = progress.getStep(i);
            if (step.getId() == caller.getID()) {
                completeStep(step);
            }
        }
        Log.e("timer",String.valueOf(caller.getID()) + String.valueOf(caller.getRemainingSeconds()));
    }

    private void showDialog(final RecipeStep step) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.is_finished)
                .setCancelable(true)
                .setPositiveButton(R.string.yep, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TimersManager.removeTimer(step.getId());
                        completeStep(step);
                    }
                })
                .setNegativeButton(R.string.nope, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
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

    private void completeStep(RecipeStep step){
        progress.setStatus(step, COMPLETED);
        adapter.unlockStep();
        progress.move(step, progress.getNonCompletedCount());
    }

    public static class StepsViewHolder extends AbstractSwipeableItemViewHolder {
        public TextView name;
        public TextView description;
        public RecipeStep recipeStep;
        public ViewGroup mContainer;

        public StepsViewHolder(View view){
            super(view);
            mContainer = (ViewGroup) view.findViewById(R.id.container);
            name = (TextView) view.findViewById(R.id.name);
            description = (TextView) view.findViewById(R.id.description);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentPosition = progress.indexOf(recipeStep);
                    int prevPosition = expandedPosition;

                    if(prevPosition == currentPosition) {
                        description.setVisibility(View.GONE);
                        expandedPosition = -1;
                        adapter.notifyItemChanged(prevPosition);
                    }
                    else{
                        expandedPosition = currentPosition;
                        if(prevPosition > 0)
                            adapter.notifyItemChanged(prevPosition);
                        adapter.notifyItemChanged(expandedPosition);
                    }

                }
            });
        }

        @Override
        public View getSwipeableContainerView() {
            return mContainer;
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

        public int getStepsCount(){
            return  stepList.size();
        }

        public int getStepsCount(Integer status){
            int count = 0;
            for(Integer i: statusList)
                if(i.equals(status))
                    count++;
            return count;
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
