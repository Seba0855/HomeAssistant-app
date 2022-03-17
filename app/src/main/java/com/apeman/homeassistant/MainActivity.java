package com.apeman.homeassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;

import android.os.Bundle;

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

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView temp = findViewById(R.id.temperature);
        TextView humi = findViewById(R.id.humidity);

        ActionMenuItemView refresh = findViewById(R.id.refresh);

        refresh.setOnClickListener(view -> {
            refresh.animate().rotation(360.0f).setDuration(200).start();
            sendRequest(temp, humi);
        });

    }

    private void sendRequest(TextView temp, TextView humi) {
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        RequestQueue requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        String url = getResources().getString(R.string.get_temperature_URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> { // onResponse
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        temp.setText(jsonObject.getString("V0"));
                        humi.setText(jsonObject.getString("V1"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                volleyError -> temp.setText("error") // onErrorResponse
        );

        requestQueue.add(stringRequest);
    }
}