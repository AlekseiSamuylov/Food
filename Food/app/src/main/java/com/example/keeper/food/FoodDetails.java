package com.example.keeper.food;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class FoodDetails extends AppCompatActivity {
    private final String TAG = "FoodDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        Log.d(TAG, "Method onCreate start.");

        SharedPreferences sPref = getSharedPreferences("appStyle", MODE_PRIVATE);
        int position = sPref.getInt("backgroundStyle", 0);
        ScrollView scrollView = (ScrollView) findViewById(R.id.foodDetailScrollView);
        if (position == 0) {
            scrollView.setBackgroundResource(R.drawable.light);
        } else {
            scrollView.setBackgroundResource(R.drawable.dark);
        }

        FoodData foodData = getIntent().getParcelableExtra(FoodData.class.getCanonicalName());

        TextView foodName = (TextView) findViewById(R.id.detailsFoodName);
        foodName.setText(foodData.getName());

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Picasso.with(this)
                .load(foodData.getImageUrl())
                .into(imageView);

        TextView textView = (TextView) findViewById(R.id.detailsText);
        textView.setText(foodData.getText());
    }
}
