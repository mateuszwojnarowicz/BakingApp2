package com.mateusz.bakingapp2.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Step implements Parcelable {

    private final int mIntId;
    private final String mStringShortDescription;
    private final String mStringDescription;
    private final String mStringVideoURL;
    private final String mStringThumbnailURL;

    public Step(int id, String stringShortDescription,String stringDescription, String stringVideoURL, String stringThumbnailUrl){
        this.mIntId=id;
        this.mStringShortDescription=stringShortDescription;
        this.mStringDescription=stringDescription;
        this.mStringVideoURL=stringVideoURL;
        this.mStringThumbnailURL=stringThumbnailUrl;
    }

    public int getIntId() {
        return mIntId;
    }
    public String getStringShortDescription(){
        return mStringShortDescription;
    }
    public String getStringDescription() {
        return mStringDescription;
    }
    public String getStringVideoURL() {
        return mStringVideoURL;
    }
    public String getStringThumbnailURL() {
        return mStringThumbnailURL;
    }

    public void writeToParcel(Parcel dest, int flags){
        dest.writeInt(mIntId);
        dest.writeString(mStringShortDescription);
        dest.writeString(mStringDescription);
        if(mStringVideoURL!=null)dest.writeString(mStringVideoURL);
        if(mStringThumbnailURL!=null)dest.writeString(mStringThumbnailURL);
    }

    public Step(Parcel parcel){
        mIntId = parcel.readInt();
        mStringShortDescription = parcel.readString();
        mStringDescription = parcel.readString();
        mStringVideoURL = parcel.readString();
        mStringThumbnailURL = parcel.readString();
    }

    public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>(){

        @Override
        public Step createFromParcel(Parcel parcel) {
            return new Step(parcel);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[0];
        }
    };

    public int describeContents() {
        return hashCode();
    }

}
