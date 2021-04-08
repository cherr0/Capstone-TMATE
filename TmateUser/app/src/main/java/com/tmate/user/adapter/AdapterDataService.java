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

    private String BASE_URL = "http://ec2-52-79-142-104.ap-northeast-2.compute.amazonaws.com:8080/member/";
//    private String BASE_URL = "http://172.26.1.230:9090/member/";

    Retrofit retrofitClient = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(new OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public AdapterUpdateAPI update = retrofitClient.create(AdapterUpdateAPI.class);

    public AdapterDeleteAPI delete = retrofitClient.create(AdapterDeleteAPI.class);

    public CardCrudAPI crudAPI = retrofitClient.create(CardCrudAPI.class);
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

interface CardCrudAPI {

    @DELETE("remove/{customer_uid}")
    Call<Boolean> removeCard(@Path("customer_uid") String customer_uid);

    @PUT("updaterep/{customer_uid}/{m_id}")
    Call<Boolean> modifyRep(@Path("customer_uid") String customer_id, @Path("m_id") String m_id);

}

