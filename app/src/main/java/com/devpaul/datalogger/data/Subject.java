package com.devpaul.datalogger.data;

import com.devpaul.datalogger.utils.IdGenerator;

/**
 * Created by Pauly D on 3/14/2015.
 */
public class Subject {

    private long id;
    private int mNumber;
    private int mHeight;
    private int mWeight;
    private int mAge;
    private String mCategory;
    private String mGender;
    private String doneStudies;

    public Subject() {
        id = IdGenerator.generateId();
    }

    public Subject(long id) {
        this.id = id;
    }

    public String getGender() {
        return this.mGender;
    }

    public void setGender(String gen) {
        this.mGender = gen;
    }

    public long getId() {
        return this.id;
    }

    public int getNumber() {
        return this.mNumber;
    }

    public void setNumber(int num) {
        this.mNumber = num;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    public int getWeight() {
        return mWeight;
    }

    public void setWeight(int mWeight) {
        this.mWeight = mWeight;
    }

    public int getAge() {
        return mAge;
    }

    public void setAge(int mAge) {
        this.mAge = mAge;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public String getDoneStudies() {
        return this.doneStudies;
    }

    public void setDoneStudies(String studies) {
        this.doneStudies = studies;
    }

}
