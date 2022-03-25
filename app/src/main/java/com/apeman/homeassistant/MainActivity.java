package com.apeman.homeassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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

        getData();

        Log.i(TAG, "temperatura: " + temperatureValues.get("insideTemperature"));
        Log.i(TAG, "wilgotność: " + temperatureValues.get("insideHumidity"));
        Log.i(TAG, "klucze: " + temperatureValues.keySet());
        Log.i(TAG, "wartosci: " + temperatureValues.values());

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
        Log.d(TAG, "Utworzył się view");
        //recyclerView.setForegroundGravity(Gravity.CENTER);


        ActionMenuItemView refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(view -> {
            // it rotates only once, fix it
            refresh.animate().rotation(360.0f).setDuration(200).start();

            getData();

            cardContentArrayList.get(0).setValue(temperatureValues.get("insideTemperature"));
            cardContentArrayList.get(1).setValue(temperatureValues.get("insideHumidity"));

            //Log.i(TAG, "temperatura: " + temperatureValues.get("insideTemperature"));
            //Log.i(TAG, "wilgotność: " + temperatureValues.get("insideHumidity"));
            Log.i(TAG, "klucze: " + temperatureValues.keySet());
            Log.i(TAG, "wartosci: " + temperatureValues.values());
            Log.i(TAG, "temperatura z arraya: " + cardContentArrayList.get(0).getValue());

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
                        Log.d(TAG, "onSubscribe()");

                    }

                    @Override
                    public void onNext(@NonNull BlynkData blynkData) {
                        Log.d(TAG, "onNext()");
                        Log.d(TAG, "Temperature: " + blynkData.getTemperature());
                        Log.d(TAG, "Humidity: " + blynkData.getHumidity());
                        temperatureValues.put("insideTemperature", blynkData.getTemperature());
                        temperatureValues.put("insideHumidity", blynkData.getHumidity());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError()");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete()");
                    }
                });
    }


    //    public StringRequest sendRequest() {
//        String uri = getResources().getString(R.string.get_temperature_URL);
//
//        return new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d(TAG, "onResponse wywołane");
//                try {
//                    Log.d(TAG, "chce stworzyć json object");
//                    JSONObject jsonObject = new JSONObject(response);
//
//                    // Assigning those values by using loop would cause problems in the future
//                    String tmp = jsonObject.getString("V0");
//                    tmp = tmp.substring(0, tmp.length() - 2) + "\u00B0";
//                    temperatureValues.put("insideTemperature", tmp);
//                    Log.d(TAG, "V0: " + jsonObject.getString("V0"));
//
//                    tmp = jsonObject.getString("V1");
//                    tmp = tmp.substring(0, tmp.length() - 2) + "%";
//                    temperatureValues.put("insideHumidity", tmp);
//                    Log.d(TAG, "V1: " + jsonObject.getString("V1"));
//
////                        Log.i(TAG, "temperatura: " + temperatureValues.get("insideTemperature"));
////                        Log.i(TAG, "wilgotność: " + temperatureValues.get("insideHumidity"));
////                        Log.i(TAG, "klucze: " + temperatureValues.keySet());
////                        Log.i(TAG, "wartosci: " + temperatureValues.values());
//                } catch (JSONException e) {
//                    Log.e(TAG, "wystąpił błąd przy tworzeniu json object");
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "wystąpił błąd: " + error);
//                topAppBar.setTitle("Bridge • Connection error");
//            }
//        });
//    }
}