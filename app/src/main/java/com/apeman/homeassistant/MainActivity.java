package com.apeman.homeassistant;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    HashMap<String, String> temperatureValues = new HashMap<>();

    private MaterialToolbar topAppBar;
    private RecyclerView recyclerView;
    private ArrayList<CardContent> cardContentArrayList;
    private RecyclerGridAdapter adapter;

    private static final String TAG = "MainActivity";
    private static final String IN_TEMP = "insideTemperature";
    private static final String IN_HUM = "insideHumidity";

    private static final int VERTICAL_ITEM_SPACE = 0;
    private static final int HORIZONTAL_ITEM_SPACE = 16;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.cardsDeck);
        topAppBar = new MaterialToolbar(findViewById(R.id.topAppBar).getContext());
        setSupportActionBar(topAppBar);

        getData();

        Log.i(TAG, "temperature: " + temperatureValues.get(IN_TEMP));
        Log.i(TAG, "humidity: " + temperatureValues.get(IN_HUM));
        Log.i(TAG, "hashmap keys: " + temperatureValues.keySet());
        Log.i(TAG, "hashmap values: " + temperatureValues.values());

        // Create new ArrayList to store card objects
        cardContentArrayList = new ArrayList<>();

        cardContentArrayList.add(new CardContent(
                "Salon",
                temperatureValues.get(IN_TEMP),
                R.color.orange,
                "Czujnik temperatury wew."
        ));
        cardContentArrayList.add(new CardContent(
                "Dom",
                temperatureValues.get(IN_HUM),
                R.color.light_blue,
                "Wilgotność"
        ));

        adapter = new RecyclerGridAdapter(cardContentArrayList, this);

        // Setting grid layout manager to implement grid view, displaying 2 columns
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(
                new RecyclerGridAdapter.VerticalSpaceItemDecoration(
                        VERTICAL_ITEM_SPACE,
                        HORIZONTAL_ITEM_SPACE)
        );

        recyclerView.setAdapter(adapter);


        ActionMenuItemView refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(view -> {
            // it rotates only once, fix it
            refresh.animate().rotation(360.0f).setDuration(200).start();
            getData();
        });
    }

    /**
     * getData() gets temperature and humidity data from blynk datastream
     *
     * onNext(): downloading data and inserting it into HashMap
     * onError(): if there is connection error, make Toast with proper message. If there was already
     *            no connection on app cold start, use -.-- as value indicator.
     * onComplete(): updates layout to refresh data
     */
    private void getData() {
        String token = getResources().getString(R.string.blynk_token);

        BlynkClient.getInstance()
                .retrieveData(token)
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
                        temperatureValues.put(IN_TEMP,
                                temperature.substring(0, 4) + "\u00B0"
                        );
                        temperatureValues.put(IN_HUM,
                                humidity.substring(0, 4) + "%"
                        );
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError()");
                        e.printStackTrace();
                        Log.i(TAG, "temperature: " + temperatureValues.get(IN_TEMP));
                        Log.i(TAG, "humidity: " + temperatureValues.get(IN_HUM));
                        Log.i(TAG, "hashmap keys: " + temperatureValues.keySet());
                        Log.i(TAG, "hashmap values: " + temperatureValues.values());

                        if (temperatureValues.get(IN_TEMP) == null
                                && temperatureValues.get(IN_HUM) == null) {

                            temperatureValues.put(IN_TEMP, "--.-" + "\u00B0");
                            temperatureValues.put(IN_HUM, "--.-%");

                            cardContentArrayList.get(0).setValue(temperatureValues.get(IN_TEMP));
                            cardContentArrayList.get(1).setValue(temperatureValues.get(IN_HUM));

                            adapter.notifyItemChanged(0);
                            adapter.notifyItemChanged(1);
                        }

                        // temporary
                        Toast.makeText(getApplicationContext(), "Błąd połączenia", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete()");
                        cardContentArrayList.get(0).setValue(temperatureValues.get(IN_TEMP));
                        cardContentArrayList.get(1).setValue(temperatureValues.get(IN_HUM));
                        adapter.notifyItemChanged(0);
                        adapter.notifyItemChanged(1);
                    }
                });
    }

    private NavigationBarView.OnItemSelectedListener navListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@androidx.annotation.NonNull MenuItem item) {
            // Cannot use switch statement as resource identifiers are not final

            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.homenav) {
                selectedFragment = new MainFragment();
            } else if (item.getItemId() == R.id.wykresy) {
                selectedFragment = new ChartFragment();
            } else if (item.getItemId() == R.id.ustawienia) {
                selectedFragment = new SettingsFragment();
            }

//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace()
            return true;
        }
    };
}