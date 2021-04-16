package com.tmate.user.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.tmate.user.Activity.MainViewActivity;
import com.tmate.user.R;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Profile_Reg_Fragment extends Fragment {

    private static final String LOG_TAG = "회원정보";

    DataService dataService = new DataService();
    private Button btn_submit;
    private EditText et_house;
    private EditText et_email;
    Map<String, String> map = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.fragment_profile__reg_, container, false);

        et_house = (EditText) rootview.findViewById(R.id.et_house);
        et_email = (EditText) rootview.findViewById(R.id.et_email);

        if (getArguments() != null) {
            map.put("m_id", getArguments().getString("m_id"));
            map.put("m_name", getArguments().getString("m_name"));
            map.put("m_birth", getArguments().getString("m_birth"));
            map.put("m_imei", getArguments().getString("m_imei"));
        }

        btn_submit = rootview.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_house != null) {
                    map.put("m_house", et_house.getText().toString());
                }

                if (et_email != null) {
                    map.put("m_email", et_email.getText().toString());
                }

                Log.d(LOG_TAG, map.values().toString());

                dataService.profile.insertOne(map).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(@NonNull  Call<Boolean> call, @NonNull Response<Boolean> response) {
                        if (response.isSuccessful()) {
                            Log.d("response", response.body().toString());
                            Intent intent = new Intent(getActivity(), MainViewActivity.class);
                            intent.putExtra("m_id", map.get("m_id"));
                            intent.putExtra("m_name", map.get("m_name"));
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<Boolean> call, Throwable t) {
                        t.printStackTrace();
                    }
                });


            }
        });
        return rootview;
    }

}
