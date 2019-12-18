package com.mffa.dev.issbid.Api;

import com.mffa.dev.issbid.Model.WeatherResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServices {
    @GET("weather")
    Observable<WeatherResponse> getWeatherbyLatLang(@Query("lat") String lat,
                                                    @Query("lon") String lng,
                                                    @Query("appid") String appid,
                                                    @Query("units") String unit);
}
