package com.example.keeper.food;

import android.app.Activity;
import android.app.PendingIntent;
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
import android.widget.LinearLayout;
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
    private RecyclerView recyclerView;
    private final String TAG = "FoodList";
    private final String firsRunDataLocation = "LocalJsonDB/data.txt";
    private final int GET_DATA_FROM_INTERNET_CODE = 1;
    public static final int SERVICE_FINISH = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        SharedPreferences sPref = getSharedPreferences("appStyle", MODE_PRIVATE);
        int position = sPref.getInt("backgroundStyle", 0);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.foodListLinearLayout);
        if (position == 0) {
            linearLayout.setBackgroundResource(R.drawable.light);
        } else {
            linearLayout.setBackgroundResource(R.drawable.dark);
        }

        dbHelper = new DBHelper(this);
        foodList = getData();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
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
        Log.d(TAG, "Method getData start.");

        List<FoodData> list = new ArrayList<>();
        try {
            SharedPreferences preferences = getPreferences(MODE_PRIVATE);
            boolean firsRun = preferences.getBoolean("firstRun", true);
            if (firsRun) {
                PendingIntent pendingIntent = createPendingResult(GET_DATA_FROM_INTERNET_CODE, new Intent(), 0);
                Intent intent = new Intent(this, LoadDataFromInternetService.class);
                intent.putExtra("pendingIntent", pendingIntent);
                startService(intent);

                list = getFoodListFromJson();
                addDataToSQL(list);

                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("firstRun", false);
                editor.commit();
            } else {
                list = getFoodListFromSQL();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return list;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "Method onActivityResult start. " +
                "requestCode = " + requestCode + ", resultCode = " + resultCode + ".");

        if (resultCode == SERVICE_FINISH) {
            Log.d(TAG, "Service finish.");

            List<FoodData> list = data.getParcelableArrayListExtra("serviceResultArray");
            addDataToSQL(list);
            foodList.addAll(list);

            PersonAdapter adapter = new PersonAdapter(foodList);
            recyclerView.setAdapter(adapter);
        } else {
            Log.d(TAG, "Service don't finish... What?");
        }
    }

    private List<FoodData> getFoodListFromJson () {
        Log.d(TAG, "Method getFoodListFromJson start.");

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
        Log.d(TAG, "Method getFoodListFromSQL start.");

        List<FoodData> list = new ArrayList<>();
        Task task = new Task();
        Pair<List<FoodData>, Boolean> pair = new Pair<>(null, false);

        task.execute(pair);
        try {
            list = task.get();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return list;
    }

    private void addDataToSQL (List<FoodData> list) {
        Log.d(TAG, "Method AddDataToSQL start.");

        Task mt = new Task();
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

    class Task extends AsyncTask<Pair<List<FoodData>, Boolean>, Void, List<FoodData>> {
        @Override
        protected List<FoodData> doInBackground(Pair<List<FoodData>, Boolean>... pairs) {
            Log.d(TAG, "In class Task: method doInBackground start.");
            List<FoodData> list = new ArrayList<>();
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            boolean firsRun = pairs[0].second;
            if (firsRun) {
                Log.d(TAG, "In class Task: first run.");
                ContentValues cv = new ContentValues();
                for (FoodData data : pairs[0].first) {
                    cv.put("name", data.getName());
                    cv.put("image_url", data.getImageUrl());
                    cv.put("information", data.getText());
                    db.insert("food", null, cv);
                    cv.clear();
                }
            } else {
                Log.d(TAG, "In class Task: don't first run.");
                Cursor cursor = db.query("food", null, null, null, null, null, null);

                if (cursor.moveToFirst()) {
                    Log.d(TAG, "In class Task: start read data from SQL.");
                    int nameColumnIndex = cursor.getColumnIndex("name");
                    int pathColumnIndex = cursor.getColumnIndex("image_url");
                    int textColumnIndex = cursor.getColumnIndex("information");

                    do {
                        String name = cursor.getString(nameColumnIndex);
                        String imageUrl = cursor.getString(pathColumnIndex);
                        String text = cursor.getString(textColumnIndex);

                        list.add(new FoodData(name, imageUrl, text));
                    } while (cursor.moveToNext());
                } else {
                    Log.d(TAG, "In class Task: in SQL no data.");
                }

                cursor.close();
            }

            db.close();
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
                    + "image_url text,"
                    + "information text"
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
