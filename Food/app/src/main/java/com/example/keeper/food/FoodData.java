package com.example.keeper.food;

import android.os.Parcel;
import android.os.Parcelable;

public class FoodData implements Parcelable {
    private String name;
    private String imageName;
    private String data;

    public static final Creator<FoodData> CREATOR = new Creator<FoodData>() {
        @Override
        public FoodData createFromParcel(Parcel in) {
            return new FoodData(in);
        }

        @Override
        public FoodData[] newArray(int size) {
            return new FoodData[size];
        }
    };

    public FoodData (String name, String imageName, String data) {
        this.name = name;
        this.imageName = imageName;
        this.data = data;
    }

    protected FoodData(Parcel in) {
        name = in.readString();
        imageName = in.readString();
        data = in.readString();
    }

    public String getName () {
        return name;
    }

    public String getImageName () {
        return imageName;
    }

    public String getData () {
        return data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imageName);
        dest.writeString(data);
    }
}
