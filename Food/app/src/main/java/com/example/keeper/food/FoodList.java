package com.example.keeper.food;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class FoodList extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<FoodData> foodNames = new ArrayList<>();
        addFood(foodNames);

        PersonAdapter adapter = new PersonAdapter(foodNames);
        recyclerView.setAdapter(adapter);
    }

    private void addFood (List<FoodData> foodNames) {
        foodNames.add(new FoodData("Апельсин", "orange.png", "Какой-то текст1"));
        foodNames.add(new FoodData("Картошка", "potato.png", "Какой-то текст2"));
        foodNames.add(new FoodData("Киви", "kiwi.png", "Какой-то текст3"));
        foodNames.add(new FoodData("Чай", "tea.png", "Какой-то текст4"));
        foodNames.add(new FoodData("Яблоко", "apple.png", "Какой-то текст5"));
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
