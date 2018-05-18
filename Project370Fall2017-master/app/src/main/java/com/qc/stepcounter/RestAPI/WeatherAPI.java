package com.qc.stepcounter.RestAPI;

import retrofit2.Call;
import retrofit2.http.GET;



public interface WeatherAPI {

    @GET("40.743235, -73.941886")
    Call<GetCurrently> getResponse();
}
