package com.example.keeper.food;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "MainMenu";
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button buttonFood = (Button) findViewById(R.id.buttonFood);
        buttonFood.setText(R.string.food_list_button_text);
        buttonFood.setOnClickListener(this);

        Button buttonSettings = (Button) findViewById(R.id.buttonSettings);
        buttonSettings.setText(R.string.setting_button_text);
        buttonSettings.setOnClickListener(this);

        SharedPreferences sPref = getSharedPreferences("appStyle", MODE_PRIVATE);
        int position = sPref.getInt("backgroundStyle", 0);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_main_menu);
        if (position == 0) {
            relativeLayout.setBackgroundResource(R.drawable.light);
            buttonFood.setBackgroundResource(R.color.colorItemsBackgroundLight);
            buttonSettings.setBackgroundResource(R.color.colorItemsBackgroundLight);
        } else {
            relativeLayout.setBackgroundResource(R.drawable.dark);
            buttonFood.setBackgroundResource(R.color.colorItemsBackgroundDark);
            buttonSettings.setBackgroundResource(R.color.colorItemsBackgroundDark);
        }
        Log.d(TAG, "Finish onCreate method");
    }

    @Override
    public void onClick (View v) {
        Log.d(TAG, "Start onClick method");
        Intent intent;
        switch (v.getId()) {
            case R.id.buttonFood:
                Log.d(TAG, "On button \"food\" click");
                intent = new Intent(this, FoodList.class);
                startActivity(intent);
                break;
            case R.id.buttonSettings:
                Log.d(TAG, "On button \"settings\" click");
                intent = new Intent(this, SettingsMenu.class);
                startActivity(intent);
                break;
        }
    }
}
