package com.tmate.user.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tmate.user.R;
import com.tmate.user.adapter.CardAdapter;
import com.tmate.user.data.CardData;
import com.tmate.user.net.DataService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class card_management  extends Fragment {
    SwipeRefreshLayout refCard;
    Button button;
    LinearLayout nocard;
    ArrayList<String> list;
    RecyclerView recyclerView;
    private static SharedPreferences pref;
    Context context;
    private String m_id;


    DataService dataService = DataService.getInstance();

    private ImageView btn_back_cardManagement;
    private CardAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.card_management,container,false);


        context = container.getContext();
        pref = context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        m_id = pref.getString("m_id", "");

        recyclerView = (RecyclerView) v.findViewById(R.id.cardlist_recy);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new CardAdapter();
        recyclerView.setAdapter(adapter);
        getData();
        button = v.findViewById(R.id.cardAdd);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                card_management_push cpf = new card_management_push();
                transaction.replace(R.id.frameLayout, cpf).commit();
            }
        });
        btn_back_cardManagement = v.findViewById(R.id.btn_back_cardManagement);
        btn_back_cardManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                My_info_Fragment my_info_fragment = new My_info_Fragment();
                transaction.replace(R.id.frameLayout, my_info_fragment).commit();
            }
        });
        refCard = v.findViewById(R.id.ref_card);
        refCard.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ArrayList<CardData> cardList = new ArrayList<>();
                cardList.clear();
                adapter.clear();
                removeScrollPullUpListener(); //스와이프와(위에서 아래로 댕기는 새로고침) 리사이클러뷰 포지션 리스너를 같이 쓰면 에러가나므로 리사이클러뷰 리스너는 잠시 끊어주고 새로고침후 다시연결해준다.
                getData(); // 디비에서 값을 다시불러온다.

            }
        });
        return v;
    }

    private void getData() {
        dataService.memberAPI.getUserCard(m_id).enqueue(new Callback<List<CardData>>() {
            @Override
            public void onResponse(Call<List<CardData>> call, Response<List<CardData>> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        List<CardData> list = response.body();
                        Log.d("넘어오는 카드 정보 : " ,list.toString());
                        for (int i = 0; i < list.size(); i++) {
                            CardData data = new CardData();
                            data.setPay_company(list.get(i).getPay_company());
                            data.setCredit_no(list.get(i).getCredit_no());
                            data.setCustomer_uid(list.get(i).getCustomer_uid());
                            data.setPay_rep(list.get(i).getPay_rep());

                            adapter.addItem(data);
                        }

                        adapter.notifyDataSetChanged();
                        //카드 새로고침 후 로딩중 아이콘 지우기
                        refCard.setRefreshing(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CardData>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    private void removeScrollPullUpListener(){
        recyclerView.removeOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }
        });
    }
}