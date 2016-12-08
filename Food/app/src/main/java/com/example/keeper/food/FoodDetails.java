package com.example.keeper.food;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class FoodDetails extends AppCompatActivity {
    private final String TAG = "FoodDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        FoodData foodData = getIntent().getParcelableExtra(FoodData.class.getCanonicalName());

        TextView foodName = (TextView) findViewById(R.id.detailsFoodName);
        foodName.setText(foodData.getName());

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        try {
            InputStream input = getAssets().open(foodData.getImagePath());
            Drawable d = Drawable.createFromStream(input, null);


            imageView.setImageDrawable(d);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        TextView textView = (TextView) findViewById(R.id.detailsText);
        textView.setText(foodData.getText());
    }
}
