package com.example.keeper.food;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
