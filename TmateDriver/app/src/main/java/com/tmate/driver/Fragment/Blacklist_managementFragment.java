package com.tmate.driver.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.driver.R;
import com.tmate.driver.adapter.BlacklistManagementAdapter;
import com.tmate.driver.data.JoinBan;
import com.tmate.driver.net.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Blacklist_managementFragment extends Fragment {
    private BlacklistManagementAdapter adapter;

    private List<JoinBan> blacklist;
    private Call<List<JoinBan>> request;
    private String d_id;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_blacklist_management,container,false);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.black_list_rv);
        d_id = getPreferenceString("d_id");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new BlacklistManagementAdapter();
        recyclerView.setAdapter(adapter);

        getData();
        return v;

    }

    private void getData() {
        request = DataService.getInstance().driver.getBlacklist(d_id);
        request.enqueue(new Callback<List<JoinBan>>() {
            @Override
            public void onResponse(Call<List<JoinBan>> call, Response<List<JoinBan>> response) {
                if(response.code() == 200) {
                    blacklist = response.body();
                    Log.i("BlacklistFragment","blacklist: " + blacklist);
                    for(JoinBan ban : blacklist) {
                        ban.setD_id(d_id);
                        adapter.addItem(ban);
                    }
                    // adapter의 값이 변경되었다는 것을 알려줍니다.
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<JoinBan>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        });



    }

    public String getPreferenceString(String key) {
        SharedPreferences pref = getActivity().getSharedPreferences("loginDriver", getActivity().MODE_PRIVATE);
        return pref.getString(key, "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        request.cancel();
    }
}