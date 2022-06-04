package com.apeman.homeassistant.blynk;

import androidx.annotation.NonNull;

import java.io.IOException;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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

    public Observable<BlynkRelayStatus> getRelayStatus(@NonNull String token) {
        return blynkService.getRelayStatus(token, "", "");
    }

    public Observable<BlynkRelayStatus> updateFirstRelay(@NonNull String token, int mode) {
        return blynkService.updateFirstRelay(token, mode);
    }

    public Observable<BlynkRelayStatus> updateSecondRelay(@NonNull String token, int mode) {
        return blynkService.updateSecondRelay(token, mode);
    }

}
