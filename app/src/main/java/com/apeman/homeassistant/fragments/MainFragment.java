package com.apeman.homeassistant.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
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
import com.apeman.homeassistant.blynk.BlynkDoorStatus;
import com.apeman.homeassistant.blynk.BlynkRelayStatus;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.transition.MaterialFadeThrough;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    HashMap<String, String> cardValues = new HashMap<>();

    private ArrayList<CardContent> cardContentArrayList;
    private RecyclerGridAdapter adapter;

    private static final String TAG = "MainActivity";
    private static final String IN_TEMP = "insideTemperature";
    private static final String IN_HUM = "insideHumidity";
    private static final String DOOR_STATUS = "doorStatus";
    private static final String WINDOW_STATUS = "windowStatus";

    private static final int VERTICAL_ITEM_SPACE = 0;
    private static final int HORIZONTAL_ITEM_SPACE = 16;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cardContentArrayList = new ArrayList<>();
        setExitTransition(new MaterialFadeThrough());
        setEnterTransition(new MaterialFadeThrough());

        // Get data from sensors
        getData();
        getDoorStatus();
        getRelayStatus();

        // Main content
        createCardViews();

        // Some debug values
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
            getDoorStatus();
            getRelayStatus();
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
                        Log.d(TAG, "onNext() BlynkData invoked");

                        String temperature = blynkData.getTemperature();
                        String humidity = blynkData.getHumidity();

                        Log.d(TAG, "BlynkData temperature: " + temperature);
                        Log.d(TAG, "BlynkData humidity: " + humidity);


                        // Adding temperature data to temperatureValue HashMap
                        cardValues.put(IN_TEMP,
                                temperature.substring(0, 4) + "\u00B0"
                        );
                        cardValues.put(IN_HUM,
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

                        if (cardValues.get(IN_TEMP) == null
                                && cardValues.get(IN_HUM) == null) {

                            cardValues.put(IN_TEMP, "--.-" + "\u00B0");
                            cardValues.put(IN_HUM, "--.-%");

                            updateLayout();
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
                        updateLayout();
                    }
                });
    }

    private void getDoorStatus() {
        String token = getResources().getString(R.string.blynk_token);

        BlynkClient.getInstance()
                .getDoorStatus(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BlynkDoorStatus>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "onSubscribe() BlynkDoorStatus invoked");

                    }

                    @Override
                    public void onNext(@NonNull BlynkDoorStatus blynkDoorStatus) {
                        Log.d(TAG, "onNext() BlynkDoorStatus invoked");

                        String doorStatus = blynkDoorStatus.getStatus(blynkDoorStatus.getDoorStatus());
                        String windowStatus = blynkDoorStatus.getStatus(blynkDoorStatus.getWindowStatus());
                        Log.d(TAG, "door status: " + doorStatus);
                        Log.d(TAG, "window status: " + windowStatus);

//                        // TODO: get rid of hardcoded IDs
                        setObjectStatus(DOOR_STATUS, 2, doorStatus);
                        setObjectStatus(WINDOW_STATUS, 3, windowStatus);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "BlynkDoorStatus error");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete()");
                        updateLayout();
                    }
                });
    }

    private void getRelayStatus() {
        String token = getResources().getString(R.string.blynk_token);

        BlynkClient.getInstance()
                .getRelayStatus(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BlynkRelayStatus>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "onSubscribe() BlynkRelayStatus invoked");
                    }

                    @Override
                    public void onNext(@NonNull BlynkRelayStatus blynkRelayStatus) {
                        int relayFirstStatus = blynkRelayStatus.getFirstDeviceStatus();
                        int relaySecondStatus = blynkRelayStatus.getSecondDeviceStatus();


                        setRelayIndicators(relayFirstStatus, relaySecondStatus);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "BlynkRelayStatus error");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "Relay onComplete()");
                        adapter.notifyItemChanged(4);
                    }
                });
    }

    private void setRelayIndicators(int first, int second) {
        if (first == 1) {
            cardContentArrayList.get(4).setPowerFirstStatus(true);
            cardContentArrayList.get(4).setFirstIndicatorColor(R.color.light_green);
        } else if (first == 0) {
            cardContentArrayList.get(4).setPowerFirstStatus(false);
            cardContentArrayList.get(4).setFirstIndicatorColor(R.color.red);
        }

        if (second == 1) {
            cardContentArrayList.get(4).setPowerSecondStatus(true);
            cardContentArrayList.get(4).setSecondIndicatorColor(R.color.light_green);
        } else if (second == 0) {
            cardContentArrayList.get(4).setPowerSecondStatus(false);
            cardContentArrayList.get(4).setSecondIndicatorColor(R.color.red);
        }
    }

    private void setObjectStatus(String object, int id, String status) {
        if (status.equals("Open")) {
            cardValues.put(object, "Open");
            cardContentArrayList.get(id).setLineColor(R.color.light_green);

        } else {
            cardValues.put(object, "Closed");
            cardContentArrayList.get(id).setLineColor(R.color.red);
        }
    }

    /**
     * Sets values in cardContentArrayList
     * and refreshes layout to apply changes.
     */
    private void updateLayout() {
        // Set values in cardContentArrayList
        List<String> indexes = new ArrayList<>();
        Collections.addAll(indexes,
                IN_TEMP,
                IN_HUM,
                DOOR_STATUS,
                WINDOW_STATUS);

        // Update layout to apply changes
        for (int i = 0; i < adapter.getItemCount() - 1; i++) {
            cardContentArrayList.get(i).setValue(cardValues.get(indexes.get(i)));
            adapter.notifyItemChanged(i);
        }
    }

    /**
     * Temporary method to print useful information
     */
    private void debugValues() {
        Log.i(TAG, "temperature: " + cardValues.get(IN_TEMP));
        Log.i(TAG, "humidity: " + cardValues.get(IN_HUM));
        Log.i(TAG, "hashmap keys: " + cardValues.keySet());
        Log.i(TAG, "hashmap values: " + cardValues.values());
    }

    private void createCardViews() {
        // TODO: Simplify this
        // I should be awarded with an YandereDev order for this batch of horror


        // Creating new cards
        cardContentArrayList.add(new CardContent(
                R.drawable.ic_temperature_reader,
                CardContent.CardType.SENSOR
        ));

        cardContentArrayList.add(new CardContent(
                R.drawable.ic_humidity,
                CardContent.CardType.SENSOR
        ));

        cardContentArrayList.add(new CardContent(
                R.drawable.ic_sensor,
                CardContent.CardType.SENSOR
        ));

        cardContentArrayList.add(new CardContent(
                R.drawable.ic_sensor,
                CardContent.CardType.SENSOR
        ));

        cardContentArrayList.add(new CardContent(
                R.drawable.ic_power_plug,
                CardContent.CardType.RELAY
        ));


        // Setting cards parameters
        cardContentArrayList.get(0).setSensorContent(
                "Salon",
                cardValues.get(IN_TEMP),
                "Czujnik temperatury wew.",
                34
        );

        cardContentArrayList.get(1).setSensorContent(
                "Dom",
                cardValues.get(IN_HUM),
                "Wilgotność",
                34
        );

        cardContentArrayList.get(2).setSensorContent(
                "Drzwi w biurze",
                cardValues.get(DOOR_STATUS),
                "Czujnik otwarcia drzwi",
                26
        );

        cardContentArrayList.get(3).setSensorContent(
                "Okno w biurze",
                cardValues.get(WINDOW_STATUS),
                "Czujnik otwarcia okna",
                26
        );

        cardContentArrayList.get(4).setRelayContent(
                "Zasilanie",
                "Światło w biurze",
                "PlayStation 5"
        );

        // Set line colors
        cardContentArrayList.get(0).setLineColor(R.color.orange);
        cardContentArrayList.get(1).setLineColor(R.color.light_blue);

        setRelayIndicators(0, 0);
    }
}