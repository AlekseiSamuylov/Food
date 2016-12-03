package com.example.keeper.food;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SettingsMenu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_menu);
        
        TextView textView = (TextView) findViewById(R.id.styleSettingText);
        textView.setText(R.string.setting_style_text);

        List<String> styleTypes = new ArrayList<>();
        styleTypes.add(getString(R.string.light_style));
        styleTypes.add(getString(R.string.dark_style));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, styleTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerStyle);
        spinner.setAdapter(adapter);

        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
        int position = sPref.getInt("appStyle", 0);
        spinner.setSelection(position);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences pref = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                if (position == 0 ) {
                    editor.putInt("appStyle", 0);
                } else {
                    editor.putInt("appStyle", 1);
                }
                editor.commit();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }
}
