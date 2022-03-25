package com.apeman.homeassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    HashMap<String, String> temperatureValues = new HashMap<>();

    private MaterialToolbar topAppBar;
    private RecyclerView recyclerView;
    private ArrayList<CardContent> cardContentArrayList;
    private RecyclerGridAdapter adapter;

    private static final String TAG = "MainActivity";
    private static final int VERTICAL_ITEM_SPACE = 0;
    private static final int HORIZONTAL_ITEM_SPACE = 16;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.cardsDeck);
        topAppBar = new MaterialToolbar(findViewById(R.id.topAppBar).getContext());

        Log.d(TAG, "Invoking getData() before recyclerView has been created");
        getData();

        Log.i(TAG, "temperature: " + temperatureValues.get("insideTemperature"));
        Log.i(TAG, "humidity: " + temperatureValues.get("insideHumidity"));
        Log.i(TAG, "hashmap keys: " + temperatureValues.keySet());
        Log.i(TAG, "hashmap values: " + temperatureValues.values());

        // Create new ArrayList to store card objects
        cardContentArrayList = new ArrayList<>();

        cardContentArrayList.add(new CardContent(
                "Salon",
                temperatureValues.get("insideTemperature"),
                R.color.orange,
                "Czujnik temperatury wew."
        ));
        cardContentArrayList.add(new CardContent(
                "Dom",
                temperatureValues.get("insideHumidity"),
                R.color.light_blue,
                "Wilgotność"
        ));

        adapter = new RecyclerGridAdapter(cardContentArrayList, this);

        // Setting grid layout manager to implement grid view, displaying 2 columns
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecyclerGridAdapter.VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE, HORIZONTAL_ITEM_SPACE));

        recyclerView.setAdapter(adapter);
        Log.d(TAG, "recyclerView created");
        //recyclerView.setForegroundGravity(Gravity.CENTER);


        ActionMenuItemView refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(view -> {
            // it rotates only once, fix it
            refresh.animate().rotation(360.0f).setDuration(200).start();

            Log.d(TAG, "Refresh button clicked");
            getData();

            cardContentArrayList.get(0).setValue(temperatureValues.get("insideTemperature"));
            cardContentArrayList.get(1).setValue(temperatureValues.get("insideHumidity"));

            Log.i(TAG, "temperature: " + temperatureValues.get("insideTemperature"));
            Log.i(TAG, "humidity: " + temperatureValues.get("insideHumidity"));
            Log.i(TAG, "hashmap keys: " + temperatureValues.keySet());
            Log.i(TAG, "hashmap values: " + temperatureValues.values());
            Log.i(TAG, "arraylist get(): " + cardContentArrayList.get(0).getValue());

            adapter.notifyItemChanged(0);
            adapter.notifyItemChanged(1);
        });
    }


    private void getData() {
        //String token = getResources().getString(R.string.blynk_token);

        BlynkClient.getInstance()
                .retrieveData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BlynkData>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "onSubscribe() invoked");

                    }

                    @Override
                    public void onNext(@NonNull BlynkData blynkData) {
                        Log.d(TAG, "onNext() invoked");
                        String temperature = blynkData.getTemperature();
                        String humidity = blynkData.getHumidity();

                        Log.d(TAG, "BlynkData temperature: " + temperature);
                        Log.d(TAG, "BlynkData humidity: " + humidity);
                        temperatureValues.put("insideTemperature",
                                temperature.substring(0, 4) + "\u00B0"
                        );
                        temperatureValues.put("insideHumidity",
                                humidity.substring(0, 4) + "%"
                        );
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError()");
                        e.printStackTrace();
                        if (temperatureValues.get("insideTemperature") == null
                                && temperatureValues.get("insideHumidity") == null) {
                            temperatureValues.put("insideTemperature", "--.-" + "\u00B0");
                            temperatureValues.put("insideHumidity", "--.-%");
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete()");
                        cardContentArrayList.get(0).setValue(temperatureValues.get("insideTemperature"));
                        cardContentArrayList.get(1).setValue(temperatureValues.get("insideHumidity"));
                        adapter.notifyItemChanged(0);
                        adapter.notifyItemChanged(1);
                    }
                });
    }
}