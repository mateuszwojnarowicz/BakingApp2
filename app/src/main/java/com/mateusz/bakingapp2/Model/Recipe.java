package com.mateusz.bakingapp2.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Recipe implements Parcelable {

    private final String mStringName;
    private final List<Ingredient> mListIngredients;
    private final List<Step> mListSteps;
    private final int mIntServings;

    public Recipe(String stringName, List<Ingredient> listIngredients, List<Step> listSteps, int intServings) {
        this.mStringName = stringName;
        this.mListIngredients = listIngredients;
        this.mListSteps = listSteps;
        this.mIntServings = intServings;
    }

    public String getStringName() {
        return mStringName;
    }
    public List<Ingredient> getListIngredients() {
        return mListIngredients;
    }
    public List<Step> getListSteps() {
        return mListSteps;
    }
    public int getIntServings() {
        return mIntServings;
    }

    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(mStringName);
        dest.writeTypedList(mListIngredients);
        dest.writeTypedList(mListSteps);
        dest.writeInt(mIntServings);
    }

    public Recipe(Parcel parcel){
        mStringName = parcel.readString();
        mListIngredients = parcel.createTypedArrayList(Ingredient.CREATOR);
        mListSteps = parcel.createTypedArrayList(Step.CREATOR);
        mIntServings = parcel.readInt();
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>(){

        @Override
        public Recipe createFromParcel(Parcel parcel) {
            return new Recipe(parcel);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[0];
        }
    };

    public int describeContents() {
        return hashCode();
    }

}
