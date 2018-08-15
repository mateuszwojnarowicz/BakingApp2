package com.mateusz.bakingapp2.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {

    private final double mDoubleQuantity;
    private final String mStringMeasure;
    private final String mStringIngredient;

    public Ingredient(double stringShortDescription,String stringDescription, String stringVideoURL){
        this.mDoubleQuantity=stringShortDescription;
        this.mStringMeasure =stringDescription;
        this.mStringIngredient =stringVideoURL;
    }

    public double getDoubleQuantity(){
        return mDoubleQuantity;
    }
    public String getStringMeasure() {
        return mStringMeasure;
    }
    public String getStringIngredient() {
        return mStringIngredient;
    }

    public void writeToParcel(Parcel dest, int flags){
        dest.writeDouble(mDoubleQuantity);
        dest.writeString(mStringMeasure);
        dest.writeString(mStringIngredient);
    }

    public Ingredient(Parcel parcel){
        mDoubleQuantity = parcel.readDouble();
        mStringMeasure = parcel.readString();
        mStringIngredient = parcel.readString();
    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>(){

        @Override
        public Ingredient createFromParcel(Parcel parcel) {
            return new Ingredient(parcel);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[0];
        }
    };

    public int describeContents() {
        return hashCode();
    }

}
