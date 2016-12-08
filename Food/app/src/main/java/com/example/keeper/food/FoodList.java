package com.example.keeper.food;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class FoodList extends Activity {
    private List<FoodData> foodList;
    private final String TAG = "FoodList";
    private final String localDBPath = "LocalJsonDB/data.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        foodList = getFoodList();

        PersonAdapter adapter = new PersonAdapter(foodList);
        recyclerView.setAdapter(adapter);

        SearchView sv = (SearchView) findViewById(R.id.search);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "In change text listener new text: \"" + newText + "\"");
                List<FoodData> list = new ArrayList<FoodData>();
                for (FoodData data : foodList) {
                    if (data.getName().toUpperCase().contains(newText.toUpperCase())) {
                        list.add(data);
                    }
                }
                recyclerView.setAdapter(new PersonAdapter(list));
                return false;
            }
        });
        Log.d(TAG, "Finish onCreate method");
    }

    private List<FoodData> getFoodListFromSqlDB () {
        List<FoodData> foodList = new ArrayList<>();



        return foodList;
    }

    private List<FoodData> getFoodList () {
        Log.d(TAG, "Start method getFoodList");
        List<FoodData> foodList = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(localDBPath)));
            String data = reader.readLine();
            reader.close();

            Gson gson = new Gson();
            Type itemListType = new TypeToken<List<FoodData>>() {}.getType();
            foodList = gson.fromJson(data, itemListType);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return foodList;
        }
        Log.d(TAG, "Finish method getFoodList");
        return foodList;
    }

    private class PersonHolder extends RecyclerView.ViewHolder {
        private TextView foodNameTextView;

        public PersonHolder(View itemView) {
            super(itemView);
            foodNameTextView = (TextView) itemView.findViewById(R.id.foodName);
        }

        public void bindCrime(String foodName) {
            foodNameTextView.setText(foodName);
        }
    }

    private class PersonAdapter extends RecyclerView.Adapter<PersonHolder> {
        private List<FoodData> foodDataList;

        public PersonAdapter(List<FoodData> foodDataList) {
            this.foodDataList = foodDataList;
        }

        @Override
        public PersonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater li = getLayoutInflater();
            View view = li.inflate(R.layout.list_item, parent, false);
            return new PersonHolder(view);
        }

        @Override
        public void onBindViewHolder(PersonHolder holder, int position) {
            final FoodData foodData = foodDataList.get(position);
            holder.bindCrime(foodData.getName());

            holder.foodNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Click on item with text: " + ((TextView) v).getText());
                    Intent intent = new Intent(FoodList.this, FoodDetails.class);
                    intent.putExtra(FoodData.class.getCanonicalName(), foodData);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return foodDataList.size();
        }
    }
}
