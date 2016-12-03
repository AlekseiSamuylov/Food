package com.example.keeper.food;

import android.os.Parcel;
import android.os.Parcelable;

public class FoodData implements Parcelable {
    private String name;
    private String imagePath;
    private String text;

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
        this.imagePath = imageName;
        this.text = data;
    }

    protected FoodData(Parcel in) {
        name = in.readString();
        imagePath = in.readString();
        text = in.readString();
    }

    public String getName () {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getText() {
        return text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imagePath);
        dest.writeString(text);
    }

    @Override
    public String toString () {
        return "Name: " + name + ", imagePath: " + imagePath + ", text: " + text;
    }
}
