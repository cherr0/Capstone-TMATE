package com.tmate.driver.Fragment;

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

import com.tmate.driver.R;
import com.tmate.driver.adapter.HistoryAdapter;
import com.tmate.driver.data.DriverHistory;
import com.tmate.driver.data.HistoryData;
import com.tmate.driver.net.DataService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HistoryFragment extends Fragment {

    private HistoryAdapter adapter;

    private ArrayList<HistoryData> arrayList;

    Call<List<DriverHistory>> historyRequest;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_history,container,false);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.history_card);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();

        adapter = new HistoryAdapter();
        recyclerView.setAdapter(adapter);

        getData();
        return v;

    }

    private void getData() {

        historyRequest = DataService.getInstance().driver.driveHistoryList(getPreferenceString("d_id"));
        historyRequest.enqueue(new Callback<List<DriverHistory>>() {
            @Override
            public void onResponse(Call<List<DriverHistory>> call, Response<List<DriverHistory>> response) {
                if(response.code() == 200 && response.body() != null) {

                    for(DriverHistory data : response.body()) {
                        Log.d("HistoryFragment", "운행이력 : " + data);
                        adapter.addItem(data);
                    }
                    // adapter의 값이 변경되었다는 것을 알려줍니다.
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<DriverHistory>> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }

    public String getPreferenceString(String key) {
        SharedPreferences pref = getActivity().getSharedPreferences("loginDriver", getActivity().MODE_PRIVATE);
        return pref.getString(key, "");
    }

}