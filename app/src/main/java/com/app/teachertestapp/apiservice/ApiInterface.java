package com.app.teachertestapp.apiservice;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("jsonmobile.asmx/Alerts?StudID=STUG115310009&&MacID=")
    Call<String> getMessageList();
}
