package com.example.keeper.food;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadDataFromInternetService extends Service {
    private ExecutorService es;
    private final String TAG = "LoadDataFromInternetSer";

    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Method onCreate start.");
        es = Executors.newFixedThreadPool(1);
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Method onDestroy start.");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Method onStartCommand start.");

        PendingIntent pendingIntent = intent.getParcelableExtra("pendingIntent");
        LoadData mr = new LoadData(startId, pendingIntent);
        es.execute(mr);

        return super.onStartCommand(intent, flags, startId);
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    private class LoadData implements Runnable {
        private int startId;
        private PendingIntent pendingIntent;
        private final String url = "http://www.calorizator.ru/product/fruit";

        public LoadData(int startId, PendingIntent pendingIntent) {
            Log.d(TAG, "LoadData start.");
            this.startId = startId;
            this.pendingIntent = pendingIntent;
        }

        public void run() {
            Log.d(TAG, "Start thread.");

            try {
                ArrayList<FoodData> list = getDataFromInternet();

                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("serviceResultArray", list);
                pendingIntent.send(LoadDataFromInternetService.this, FoodList.SERVICE_FINISH, intent);
            } catch (PendingIntent.CanceledException e) {
                Log.e(TAG, e.getMessage());
            }

            Log.d(TAG, "Finish thread with start id = " + startId + ", stopSelfResult("
                    + startId + ") = " + stopSelfResult(startId));
        }

        private ArrayList<FoodData> getDataFromInternet() {
            Log.d(TAG, "Method getDataFromInternet start.");
            ArrayList<FoodData> list = new ArrayList<>();

            try {
                Document mainDocument = Jsoup.connect(url).get();

                Elements elements = mainDocument.select("td.views-field-title");
                int count = 10;
                for (int i = 0; i < elements.size() && i < count; i++) {
                    Element element = elements.get(i);

                    String foodDetailsLink = "http://www.calorizator.ru/" +  element.children().first().attr("href");
                    String foodName = element.children().first().html();

                    Document detailsDocument = Jsoup.connect(foodDetailsLink).get();
                    Element detailsElement = detailsDocument.select("div.node-content").get(0);
                    String imageUrl = detailsElement.children().first().children().first().children().first().children().first().attr("href");
                    String information = detailsElement.children().get(2).html();

                    list.add(new FoodData(foodName, imageUrl, information));
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }

            Log.d(TAG, "Load data from internet finish.");
            return list;
        }
    }
}
