package com.apeman.homeassistant.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apeman.homeassistant.CardContent;
import com.apeman.homeassistant.R;
import com.apeman.homeassistant.blynk.BlynkClient;
import com.apeman.homeassistant.blynk.BlynkData;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    HashMap<String, String> temperatureValues = new HashMap<>();

    private ArrayList<CardContent> cardContentArrayList;
    private RecyclerGridAdapter adapter;

    private static final String TAG = "MainActivity";
    private static final String IN_TEMP = "insideTemperature";
    private static final String IN_HUM = "insideHumidity";

    private static final int VERTICAL_ITEM_SPACE = 0;
    private static final int HORIZONTAL_ITEM_SPACE = 16;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get data from sensors
        getData();

        cardContentArrayList = new ArrayList<>();

        // Creating new cards
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

        debugValues();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // Setting the RecyclerView and app bar
        RecyclerView recyclerView = view.findViewById(R.id.cardsDeck);
        MaterialToolbar topAppBar = new MaterialToolbar(view.findViewById(R.id.topAppBar).getContext());

        // Setting MaterialToolbar to be support action bar
        ((AppCompatActivity) requireActivity()).setSupportActionBar(topAppBar);

        adapter = new RecyclerGridAdapter(cardContentArrayList, getContext());

        // Customizing RecyclerView to use grid features. Splitting it to 2 columns
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        // Spacing between cards
        recyclerView.addItemDecoration(
                new RecyclerGridAdapter.VerticalSpaceItemDecoration(
                        VERTICAL_ITEM_SPACE,
                        HORIZONTAL_ITEM_SPACE)
        );

        recyclerView.setAdapter(adapter);

        // Refresh button
        ActionMenuItemView refresh = view.findViewById(R.id.refresh);
        refresh.setOnClickListener(refreshView -> {
            refresh.animate().rotation(360.0f).setDuration(200).start();
            getData();
        });

        return view;
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

                    /**
                     * Gets information retrieved from Blynk datastreams
                     * and adds that data to sufficient data strucutres
                     *
                     * @param blynkData: object of BlynkData containing information
                     *                 retrieved from Blynk datastreams
                     */
                    @Override
                    public void onNext(@NonNull BlynkData blynkData) {
                        Log.d(TAG, "onNext() invoked");

                        String temperature = blynkData.getTemperature();
                        String humidity = blynkData.getHumidity();
                        Log.d(TAG, "BlynkData temperature: " + temperature);
                        Log.d(TAG, "BlynkData humidity: " + humidity);

                        // Adding temperature data to temperatureValue HashMap
                        temperatureValues.put(IN_TEMP,
                                temperature.substring(0, 4) + "\u00B0"
                        );
                        temperatureValues.put(IN_HUM,
                                humidity.substring(0, 4) + "%"
                        );
                    }

                    /**
                     * Catches connection error. If connection error appear on cold app start
                     * fill the temperatureValues HashMap with "--.-" fillers to notify user
                     * that there are a connection error and data has not been retrieved.
                     *
                     * @param e: Throwable object containing error StackTrace
                     */
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError()");

                        // debug, get rid of it before deploying it anywhere
                        e.printStackTrace();
                        debugValues();

                        if (temperatureValues.get(IN_TEMP) == null
                                && temperatureValues.get(IN_HUM) == null) {

                            temperatureValues.put(IN_TEMP, "--.-" + "\u00B0");
                            temperatureValues.put(IN_HUM, "--.-%");

                            updateTemperatures();
                        }

                        // temporary
                        Toast.makeText(getContext(), "Błąd połączenia", Toast.LENGTH_LONG).show();
                    }

                    /**
                     * Updates cards in layout on transaction completed
                     */
                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete()");
                        updateTemperatures();
                    }
                });
    }

    /**
     * Sets temperature values in cardContentArrayList
     * and refreshes layout to apply changes.
     */
    private void updateTemperatures() {
        // Set values in cardContentArrayList
        cardContentArrayList.get(0).setValue(temperatureValues.get(IN_TEMP));
        cardContentArrayList.get(1).setValue(temperatureValues.get(IN_HUM));

        // Update layout to apply changes
        adapter.notifyItemChanged(0);
        adapter.notifyItemChanged(1);
    }

    /**
     * Temporary method to print useful information
     */
    private void debugValues() {
        Log.i(TAG, "temperature: " + temperatureValues.get(IN_TEMP));
        Log.i(TAG, "humidity: " + temperatureValues.get(IN_HUM));
        Log.i(TAG, "hashmap keys: " + temperatureValues.keySet());
        Log.i(TAG, "hashmap values: " + temperatureValues.values());
    }
}