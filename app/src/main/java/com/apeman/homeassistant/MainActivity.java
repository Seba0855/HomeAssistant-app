package com.apeman.homeassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
    private static final int VERTICAL_ITEM_SPACE = 0;
    private static final int HORIZONTAL_ITEM_SPACE = 16;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.cardsDeck);

        RequestWizard.getInstance(this.getApplicationContext()).getRequestQueue();
        RequestWizard.getInstance(this).addToRequestQueue(sendRequest());

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

        RecyclerGridAdapter adapter = new RecyclerGridAdapter(cardContentArrayList, this);

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

            RequestWizard.getInstance(this).addToRequestQueue(sendRequest());

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

    public StringRequest sendRequest() {
        String uri = getResources().getString(R.string.get_temperature_URL);

        return new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse wywołane");
                try {
                    Log.d(TAG, "chce stworzyć json object");
                    JSONObject jsonObject = new JSONObject(response);

                    // Assigning those values by using loop would cause problems in the future
                    String tmp = jsonObject.getString("V0");
                    tmp = tmp.substring(0, tmp.length() - 2) + "\u00B0";
                    temperatureValues.put("insideTemperature", tmp);
                    Log.d(TAG, "V0: " + jsonObject.getString("V0"));

                    tmp = jsonObject.getString("V1");
                    tmp = tmp.substring(0, tmp.length() - 2) + "%";
                    temperatureValues.put("insideHumidity", tmp);
                    Log.d(TAG, "V1: " + jsonObject.getString("V1"));

//                        Log.i(TAG, "temperatura: " + temperatureValues.get("insideTemperature"));
//                        Log.i(TAG, "wilgotność: " + temperatureValues.get("insideHumidity"));
//                        Log.i(TAG, "klucze: " + temperatureValues.keySet());
//                        Log.i(TAG, "wartosci: " + temperatureValues.values());
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
    }
}