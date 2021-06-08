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
import com.tmate.user.data.Data;
import com.tmate.user.data.Dispatch;
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

    Call<List<Dispatch>> historyRequest;

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


        historyRequest = DataService.getInstance().memberAPI.selectHistory(m_id);
        historyRequest.enqueue(new Callback<List<Dispatch>>() {
            @Override
            public void onResponse(Call<List<Dispatch>> call, Response<List<Dispatch>> response) {
                if(response.code() == 200 && response.body() != null) {
                    List<Dispatch> histroyList = response.body();
                    Log.d("넘어오는 이용내역 리스트", histroyList.toString());
                    for(Dispatch data : histroyList) {
                        adapter.addItem(data);
                    }

                    adapter.notifyDataSetChanged();
                    //카드 새로고침 후 로딩중 아이콘 지우기
                    refHis.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<Dispatch>> call, Throwable t) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(historyRequest != null) historyRequest.cancel();
    }
}
