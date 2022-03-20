package com.apeman.homeassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;

import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    HashMap<String, String> temperatureValues = new HashMap<>();
    GridView gridView;

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        gridView = findViewById(R.id.cardsDeck);
        gridView.setGravity(Gravity.CENTER);
        sendRequest();

        Log.i(TAG, "temperatura: " + temperatureValues.get("insideTemperature"));
        Log.i(TAG, "wilgotność: " + temperatureValues.get("insideHumidity"));
        Log.i(TAG, "klucze: " + temperatureValues.keySet());
        Log.i(TAG, "wartosci: " + temperatureValues.values());

        ArrayList<CardContent> cardContentArrayList = new ArrayList<CardContent>();
        cardContentArrayList.add(new CardContent(
                temperatureValues.get("insideTemperature"),
                "Czujnik temperatury wew."
        ));
        cardContentArrayList.add(new CardContent(
                temperatureValues.get("insideHumidity"),
                "Wilgotność"
        ));

        GridAdapter adapter = new GridAdapter(this, cardContentArrayList);
        gridView.setAdapter(adapter);


        ActionMenuItemView refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(view -> {
            // it rotates only once, fix it
            refresh.animate().rotation(360.0f).setDuration(200).start();
        });
    }

    public synchronized void sendRequest() {
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        RequestQueue requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        String uri = getResources().getString(R.string.get_temperature_URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri,
                response -> { // onResponse
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        // Assigning those values by using loop would cause problems in the future
                        temperatureValues.put("insideTemperature", jsonObject.getString("V0"));
                        Log.d(TAG, "V0: " + jsonObject.getString("V0"));
                        temperatureValues.put("insideHumidity", jsonObject.getString("V1"));
                        Log.d(TAG, "V1: " + jsonObject.getString("V1"));

                        Log.i(TAG, "temperatura: " + temperatureValues.get("insideTemperature"));
                        Log.i(TAG, "wilgotność: " + temperatureValues.get("insideHumidity"));
                        Log.i(TAG, "klucze: " + temperatureValues.keySet());
                        Log.i(TAG, "wartosci: " + temperatureValues.values());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace // onErrorResponse, use Log.e later
        );

        requestQueue.add(stringRequest);
    }
}