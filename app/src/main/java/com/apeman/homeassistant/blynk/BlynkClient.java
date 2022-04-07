package com.apeman.homeassistant.blynk;

import androidx.annotation.NonNull;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class BlynkClient {
    private static final String BLYNK_CLOUD_URL = "https://blynk.cloud/external/api/";

    private static BlynkClient instance;
    private final BlynkService blynkService;

    private BlynkClient() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BLYNK_CLOUD_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        blynkService = retrofit.create(BlynkService.class);
    }

    public static synchronized BlynkClient getInstance() {
        if (instance == null) {
            instance = new BlynkClient();
        }
        return instance;
    }

    public Observable<BlynkData> retrieveData(@NonNull String token) {
        return blynkService.retrieveData(token, "", "", "");
    }

    public Observable<BlynkDoorStatus> getDoorStatus(@NonNull String token) {
        return blynkService.getDoorStatus(token, "", "");
    }
}
