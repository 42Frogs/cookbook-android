package com.frogs42.cookbook.model;

/**
 * Model to represent recipe ingredient
 */
public class Ingredient {

    private int mId = -1;
    private String mName = "";

    public int getId() {
        return mId;
    }

    public Ingredient setId(int id) {
        mId = id;
        return this;
    }

    public String getName() {
        return mName;
    }

    public Ingredient setName(String name) {
        mName = name;
        return this;
    }
}
