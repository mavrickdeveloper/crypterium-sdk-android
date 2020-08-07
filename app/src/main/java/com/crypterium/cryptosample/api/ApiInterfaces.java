package com.crypterium.cryptosample.api;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterfaces {

    @POST("v1/merchant/authorize")
    Observable<AuthResponse> auth(@Body JsonObject body);

}