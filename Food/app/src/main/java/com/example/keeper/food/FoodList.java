package com.example.keeper.food;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.Pair;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class FoodList extends Activity {
    private List<FoodData> foodList;
    private DBHelper dbHelper;
    private final String TAG = "FoodList";
    private final String firsRunDataLocation = "LocalJsonDB/data.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        dbHelper = new DBHelper(this);
        foodList = getData();

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

    private List<FoodData> getData () {
        Log.d(TAG, "Method gedData start");
        List<FoodData> list;
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean firsRun = preferences.getBoolean("firstRun", true);
        if (firsRun) {
            Log.d(TAG, "First run.");
            list = getFoodListFromJson();
            addDataToSQL(list);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstRun", false);
            editor.commit();
        } else {
            Log.d(TAG, "Not first run");
            list = getFoodListFromSQL();
        }

        return list;
    }

    private List<FoodData> getFoodListFromJson () {
        Log.d(TAG, "Method getFoodListFromJson start");
        List<FoodData> foodList = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(firsRunDataLocation)));
            String data = reader.readLine();
            reader.close();

            Gson gson = new Gson();
            Type itemListType = new TypeToken<List<FoodData>>() {}.getType();
            foodList = gson.fromJson(data, itemListType);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return foodList;
        }
        return foodList;
    }

    private List<FoodData> getFoodListFromSQL () {
        List<FoodData> list = new ArrayList<>();
        MyTask mt = new MyTask();

        Pair<List<FoodData>, Boolean> pair = new Pair<>(null, false);
        mt.execute(pair);
        try {
            list = mt.get();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    private void addDataToSQL (List<FoodData> list) {
        MyTask mt = new MyTask();
        Pair<List<FoodData>, Boolean> pair = new Pair<>(list, true);
        mt.execute(pair);
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

    class MyTask extends AsyncTask<Pair<List<FoodData>, Boolean>, Void, List<FoodData>> {
        @Override
        protected List<FoodData> doInBackground(Pair<List<FoodData>, Boolean>... pairs) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            List<FoodData> list = new ArrayList<>();
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            boolean firsRun = pairs[0].second;
            if (firsRun) {
                System.out.println("List size: " + pairs[0].first.size() + ", firstRun = " + firsRun);
                ContentValues cv = new ContentValues();
                for (FoodData data : pairs[0].first) {
                    cv.put("name", data.getName());
                    cv.put("image_path", data.getImagePath());
                    cv.put("information", data.getText());
                    db.insert("food", null, cv);
                    cv.clear();
                    System.out.println("To SQL: " + data.toString());
                }
            } else {
                Cursor cursor = db.query("food", null, null, null, null, null, null);

                if (cursor.moveToFirst()) {
                    int nameColumnIndex = cursor.getColumnIndex("name");
                    int pathColumnIndex = cursor.getColumnIndex("image_path");
                    int textColumnIndex = cursor.getColumnIndex("information");

                    do {
                        String name = cursor.getString(nameColumnIndex);
                        String imagePath = cursor.getString(pathColumnIndex);
                        String text = cursor.getString(textColumnIndex);

                        FoodData data = new FoodData(name, imagePath, text);
                        list.add(data);

                        System.out.println("From SQL: " + data.toString());
                    } while (cursor.moveToNext());
                } else {
                    //0 rows
                }
            }
            return list;
        }
    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, "food", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table food ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "image_path text,"
                    + "information text" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
