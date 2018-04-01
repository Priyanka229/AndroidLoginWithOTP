package com.example.noone.curofy.network;

import com.google.gson.JsonObject;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface RetrofitService {
    @POST("generate_otp.json")
    @FormUrlEncoded
    Call<JsonObject> sendOTP(@FieldMap Map<String, String> map);

    @POST("login_app.json")
    @FormUrlEncoded
    Call<JsonObject> verifyOTP(@FieldMap Map<String , String> map);
}
