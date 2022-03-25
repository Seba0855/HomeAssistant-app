package com.apeman.homeassistant;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BlynkService {

    @GET("external/api/get?token=d-6DGP7qHZ3ILwCWh3PUJNnI8LScfhqs&V0&V1")
    Observable<BlynkData> retrieveData();
//@Query("token") String token, @Query("virtualPin") String virtualPin
}
