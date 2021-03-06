package com.apeman.homeassistant.blynk;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BlynkService {

    /**
     * This method allows user to retrieve data from virtual pins using GET method.
     *
     * @param token Blynk.cloud token
     * @param a as "V0" does not take any parameters, it should be always null
     * @param b as "V1" does not take any parameters, it should be always null
     * @return Observable<BlynkData>
     */
    @GET("get")
    Observable<BlynkData> retrieveData(
            @Query("token") String token,
            @Query("V0") String a,
            @Query("V1") String b,
            @Query("V2") String c
    );


    /**
     * Allows user to get information if doors are open or closed
     * (defined by V2 virtualPin value)
     *
     * @param token Blynk.cloud token
     * @param a as "V2" does not take any parameters, it should be always null
     * @return Observable<BlynkData>
     */
    @GET("get")
    Observable<BlynkDoorStatus> getDoorStatus(
            @Query("token") String token,
            @Query("V2") String a,
            @Query("V3") String b
    );

    /**
     * Gets relay status - used to inform user if device is powered on or off
     * @param token Blynk.cloud token
     * @param a additional parameter, should be always null
     * @param b additional parameter, should be always null
     * @return Observable<BlynkRelayStatus>
     */
    @GET("get")
    Observable<BlynkRelayStatus> getRelayStatus(
            @Query("token") String token,
            @Query("V4") String a,
            @Query("V5") String b
    );

    /**
     * Updates first relay status on Blynk platform.
     * @param token Blynk.cloud token
     * @param mode Sets relay to powered on (1) or powered off (0)
     * @return Observable<BlynkRelayStatus>
     */
    @GET("update")
    Call<Void> updateFirstRelay(
            @Query("token") String token,
            @Query("V4") int mode
     );


    /**
     * Updates second relay status on Blynk platform.
     * @param token Blynk.cloud token
     * @param mode Sets relay to powered on (1) or powered off (0)
     * @return Observable<BlynkRelayStatus>
     */
    @GET("update")
    Call<Void> updateSecondRelay(
            @Query("token") String token,
            @Query("V5") int mode
    );
}
