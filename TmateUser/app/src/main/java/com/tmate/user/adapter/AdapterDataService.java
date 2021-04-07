package com.tmate.user.adapter;


import com.tmate.user.data.Notification;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class AdapterDataService {

    private String BASE_URL = "http://10.0.2.2:9090/member/";
//    private String BASE_URL = "http://172.26.1.230:9090/member/";

    Retrofit retrofitClient = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(new OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public AdapterUpdateAPI update = retrofitClient.create(AdapterUpdateAPI.class);

    public AdapterDeleteAPI delete = retrofitClient.create(AdapterDeleteAPI.class);
}

interface AdapterUpdateAPI{

    @PUT("agree/{m_id}")
    Call<Boolean> agreeAppro(@Path("m_id") String m_id, @Body Notification notification);

    @PUT("updatestat")
    Call<Boolean> modifyStat(@Body Notification notification);
}

interface AdapterDeleteAPI {

    @DELETE("removeAppro/{id}/{m_id}")
    Call<Boolean> removeApproval(@Path("id") String id, @Path("m_id") String m_id);
}

