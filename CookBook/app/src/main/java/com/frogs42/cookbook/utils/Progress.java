package com.frogs42.cookbook.utils;

import com.frogs42.cookbook.model.RecipeStep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by ilia on 07.06.15.
 */
public class Progress {

    public final static int RUNNING = 0;
    public final static int AVAILABLE = 1;
    public final static int UNAVAILABLE = 2;
    public final static int COMPLETED = 3;

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
