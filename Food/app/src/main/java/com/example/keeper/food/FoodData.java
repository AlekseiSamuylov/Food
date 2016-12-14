package com.example.keeper.food;

import android.os.Parcel;
import android.os.Parcelable;

public class FoodData implements Parcelable {
    private String name;
    private String imageUrl;
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
        this.imageUrl = imageName;
        this.text = data;
    }

    protected FoodData(Parcel in) {
        name = in.readString();
        imageUrl = in.readString();
        text = in.readString();
    }

    public String getName () {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
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
        dest.writeString(imageUrl);
        dest.writeString(text);
    }

    @Override
    public String toString () {
        return "Name: " + name + ", imageUrl: " + imageUrl + ", text: " + text;
    }
}
