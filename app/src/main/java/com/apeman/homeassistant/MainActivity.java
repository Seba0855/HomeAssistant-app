package com.apeman.homeassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.widget.GridView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    HashMap<String, String> temperatureValues = new HashMap<>();
    private RecyclerView recyclerView;
    private ArrayList<CardContent> cardContentArrayList;

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.cardsDeck);

        RequestWizard requestWizard = new RequestWizard(this, temperatureValues);
        requestWizard.sendRequest();

        Log.i(TAG, "temperatura: " + temperatureValues.get("insideTemperature"));
        Log.i(TAG, "wilgotność: " + temperatureValues.get("insideHumidity"));
        Log.i(TAG, "klucze: " + temperatureValues.keySet());
        Log.i(TAG, "wartosci: " + temperatureValues.values());

        // Create new ArrayList to store card objects
        cardContentArrayList = new ArrayList<>();

        cardContentArrayList.add(new CardContent(
                temperatureValues.get("insideTemperature"),
                "Czujnik temperatury wew."
        ));
        cardContentArrayList.add(new CardContent(
                temperatureValues.get("insideHumidity"),
                "Wilgotność"
        ));

        RecyclerGridAdapter adapter = new RecyclerGridAdapter(cardContentArrayList, this);

        // Setting grid layout manager to implement grid view, displaying 2 columns
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "Utworzył się view");
        //recyclerView.setForegroundGravity(Gravity.CENTER);


        ActionMenuItemView refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(view -> {
            // it rotates only once, fix it
            refresh.animate().rotation(360.0f).setDuration(200).start();
            requestWizard.sendRequest();
            cardContentArrayList.get(0).setValue(temperatureValues.get("insideTemperature"));
            cardContentArrayList.get(1).setValue(temperatureValues.get("insideHumidity"));
            Log.i(TAG, "temperatura: " + temperatureValues.get("insideTemperature"));
            Log.i(TAG, "wilgotność: " + temperatureValues.get("insideHumidity"));
            Log.i(TAG, "klucze: " + temperatureValues.keySet());
            Log.i(TAG, "wartosci: " + temperatureValues.values());
            Log.i(TAG, "temperatura z arraya: " + cardContentArrayList.get(0).getValue());
            adapter.notifyItemChanged(0);
            adapter.notifyItemChanged(1);
        });
    }

    public void sendRequest() {
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        RequestQueue requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        String uri = getResources().getString(R.string.get_temperature_URL);


        Log.e(TAG, "PRÓBA UTWORZENIA STRING REQUESTA");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    // Assigning those values by using loop would cause problems in the future
                    temperatureValues.put("insideTemperature", jsonObject.getString("V0"));
                    Log.d(TAG, "V0: " + jsonObject.getString("V0"));
                    temperatureValues.put("insideHumidity", jsonObject.getString("V1"));
                    Log.d(TAG, "V1: " + jsonObject.getString("V1"));

                } catch (JSONException e) {
                    Log.e(TAG, "chuij nie wyszło");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "wystąpił błąd: " + error);
            }
        });
        requestQueue.add(stringRequest);
    }
}