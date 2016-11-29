package com.example.keeper.food;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {
    Button buttonFood;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        buttonFood = (Button) findViewById(R.id.buttonFood);
        buttonFood.setText(R.string.food_list_button_text);
        buttonFood.setOnClickListener(this);
    }

    @Override
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.buttonFood:
                Intent intent = new Intent(this, FoodList.class);
                startActivity(intent);
                break;
        }
    }
}
