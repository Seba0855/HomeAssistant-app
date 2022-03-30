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
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();

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

        Log.i(TAG, "temperature: " + temperatureValues.get(IN_TEMP));
        Log.i(TAG, "humidity: " + temperatureValues.get(IN_HUM));
        Log.i(TAG, "hashmap keys: " + temperatureValues.keySet());
        Log.i(TAG, "hashmap values: " + temperatureValues.values());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);


        recyclerView = view.findViewById(R.id.cardsDeck);
        topAppBar = new MaterialToolbar(view.findViewById(R.id.topAppBar).getContext());
        ((AppCompatActivity) requireActivity()).setSupportActionBar(topAppBar);

        adapter = new RecyclerGridAdapter(cardContentArrayList, getContext());

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(
                new RecyclerGridAdapter.VerticalSpaceItemDecoration(
                        VERTICAL_ITEM_SPACE,
                        HORIZONTAL_ITEM_SPACE)
        );

        recyclerView.setAdapter(adapter);

        ActionMenuItemView refresh = view.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh.animate().rotation(360.0f).setDuration(200).start();
                getData();
            }
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
                        Toast.makeText(getContext(), "Błąd połączenia", Toast.LENGTH_LONG).show();
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
}