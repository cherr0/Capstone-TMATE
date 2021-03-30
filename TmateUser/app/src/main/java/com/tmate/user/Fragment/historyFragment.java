package com.tmate.user.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.R;
import com.tmate.user.data.UserHistroy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class historyFragment extends Fragment {
    ArrayList<String> list;

    DataService dataService = new DataService();
    private historyAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.history,container,false);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.hcard);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new historyAdapter();
        recyclerView.setAdapter(adapter);


        getData();
        return v;
    }

    private void getData() {
 
        String m_id = getPreferenceString("m_id");

        dataService.select.selectHistory(m_id).enqueue(new Callback<List<UserHistroy>>() {
            @Override
            public void onResponse(Call<List<UserHistroy>> call, Response<List<UserHistroy>> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        List<UserHistroy> histroyList = response.body();
                        Log.d("넘어오는 이용내역 리스트", histroyList.toString());
                        for (int i = 0; i < histroyList.size(); i++) {
                            Data data = new Data();
                            data.setCarinfo(histroyList.get(i).getCar_no()+" | "+histroyList.get(i).getCar_model());
                            data.setDrivername(histroyList.get(i).getM_name());
                            data.setStart(histroyList.get(i).getH_s_place());
                            data.setFinish(histroyList.get(i).getH_f_place());
                            data.setTime(histroyList.get(i).getH_s_time().toString().substring(10,16)+" - "+histroyList.get(i).getH_e_time().toString().substring(10,16));
                            data.setDate(histroyList.get(i).getMerchant_uid().substring(9,11)+"/"+histroyList.get(i).getMerchant_uid().substring(11,13)+"/"+histroyList.get(i).getMerchant_uid().substring(13,15));
                            switch (histroyList.get(i).getMerchant_uid().substring(27)) {
                                case "0":
                                    data.setTogether("동승");
                                    break;

                                case "1":
                                    data.setTogether("일반");
                                    break;
                            }

                            adapter.addItem(data);
                        }

                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<UserHistroy>> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }


    // getPreferenceString
    public String getPreferenceString(String key) {
        SharedPreferences pref = getActivity().getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }
}
