package com.example.keeper.food;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class FoodDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        FoodData foodData = getIntent().getParcelableExtra(FoodData.class.getCanonicalName());
        System.out.println("Get data.");

        TextView foodName = (TextView) findViewById(R.id.detailsFoodName);
        foodName.setText(foodData.getName());

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        try {
            InputStream input = getAssets().open("pictures/" + foodData.getImageName());
            Drawable d = Drawable.createFromStream(input, null);


            imageView.setImageDrawable(d);
        } catch (IOException e) {
            System.out.println("Image load error.");
        }

        TextView textView = (TextView) findViewById(R.id.detailsText);
        textView.setText(foodData.getData());
    }
}
