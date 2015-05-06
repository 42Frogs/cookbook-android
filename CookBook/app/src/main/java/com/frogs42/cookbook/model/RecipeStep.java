package com.frogs42.cookbook.model;

import java.util.ArrayList;

/**
 * Model to represent one step from recipe
 */
public class RecipeStep {

    private int mId = -1;

    private String mTitle = "";
    private String mDescription = "";
    private String mIcoPath = "";

    private int mDurationInSeconds = 0;

    private ArrayList<RecipeStep> mParentSteps = new ArrayList<>();

    public int getId() {
        return mId;
    }

    public RecipeStep setId(int id) {
        mId = id;
        return this;
    }

    public String getTitle() {
        return mTitle;
    }

    public RecipeStep setTitle(String title) {
        mTitle = title;
        return this;
    }

    public String getDescription() {
        return mDescription;
    }

    public RecipeStep setDescription(String description) {
        mDescription = description;
        return this;
    }

    public String getIcoPath() {
        return mIcoPath;
    }

    public RecipeStep setIcoPath(String icoPath) {
        mIcoPath = icoPath;
        return this;
    }

    public int getDurationInSeconds() {
        return mDurationInSeconds;
    }

    public RecipeStep setDurationInSeconds(int durationInSeconds) {
        mDurationInSeconds = durationInSeconds;
        return this;
    }

    public ArrayList<RecipeStep> getParentSteps() {
        return mParentSteps;
    }

    public RecipeStep addParentStep(RecipeStep parent) {
        mParentSteps.add(parent);
        return this;
    }
}
