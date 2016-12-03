package com.example.keeper.food;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {
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
    }

    @Override
    public void onClick (View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.buttonFood:
                intent = new Intent(this, FoodList.class);
                startActivity(intent);
                break;
            case R.id.buttonSettings:
                intent = new Intent(this, SettingsMenu.class);
                startActivity(intent);
                break;
        }
    }
}
