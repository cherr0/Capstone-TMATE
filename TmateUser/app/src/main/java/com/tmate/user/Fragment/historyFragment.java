package com.tmate.user.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tmate.user.R;
import com.tmate.user.adapter.historyAdapter;
import com.tmate.user.data.CardData;
import com.tmate.user.data.Data;
import com.tmate.user.data.UserHistroy;
import com.tmate.user.net.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class historyFragment extends Fragment {
    ArrayList<String> list;

//    DataService dataService = new DataService();
    DataService dataService = DataService.getInstance();

    private historyAdapter adapter;
    private ImageView btn_back_history;
    private SwipeRefreshLayout refHis;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.history,container,false);

        recyclerView = (RecyclerView) v.findViewById(R.id.hcard);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new historyAdapter();
        recyclerView.setAdapter(adapter);

        getData();

        btn_back_history = v.findViewById(R.id.btn_back_history);
        btn_back_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                My_info_Fragment my_info_fragment = new My_info_Fragment();
                fragmentTransaction.replace(R.id.frameLayout, my_info_fragment).commit();
            }
        });

        refHis = v.findViewById(R.id.ref_history);
        refHis.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ArrayList<UserHistroy> userHis = new ArrayList<>();
                userHis.clear();
                adapter.clear();
                hisremoveScrollPullUpListener(); //스와이프와(위에서 아래로 댕기는 새로고침) 리사이클러뷰 포지션 리스너를 같이 쓰면 에러가나므로 리사이클러뷰 리스너는 잠시 끊어주고 새로고침후 다시연결해준다.
                getData(); // 디비에서 값을 다시불러온다.

            }
        });

        return v;


    }

    private void getData() {
 
        String m_id = getPreferenceString("m_id");


        dataService.memberAPI.selectHistory(m_id).enqueue(new Callback<List<UserHistroy>>() {
            @Override
            public void onResponse(Call<List<UserHistroy>> call, Response<List<UserHistroy>> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        List<UserHistroy> histroyList = response.body();
                        Log.d("넘어오는 이용내역 리스트", histroyList.toString());
                        for (int i = 0; i < histroyList.size(); i++) {
                            Data data = new Data();
                            data.setMerchant_uid(histroyList.get(i).getMerchant_uid());
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
                        //카드 새로고침 후 로딩중 아이콘 지우기
                        refHis.setRefreshing(false);

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
    private void hisremoveScrollPullUpListener(){
        recyclerView.removeOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }
        });
    }

}
