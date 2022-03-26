package com.apeman.homeassistant;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BlynkService {

    /**
     * This method allows user to retrieve data from virtual pins using GET method.
     *
     * @param token Blynk.cloud token
     * @param a as "V0" does not take any parameters, it should be always null
     * @param b as "V1" does not take any parameters, it should be always null
     * @return Observable\<BlynkData>
     */
    @GET("get")
    Observable<BlynkData> retrieveData(
            @Query("token") String token,
            @Query("V0") String a,
            @Query("V1") String b
    );
}
