package com.tmate.user.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.tmate.user.net.KakaopayWebviewActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class card_management  extends Fragment {
    SwipeRefreshLayout refCard;
    Button button ;
    ImageView btn_card_tooltip;
    LinearLayout nocard;
    ArrayList<String> list;
    RecyclerView recyclerView;
    private static SharedPreferences pref;
    private TextView tv_toolTip;
    Context context;
    Boolean click = false;

    private String m_id;
    ArrayList<CardData> cardList;

    Call<List<CardData>> listRequest;


    private ImageView btn_back_cardManagement;
    private CardAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_management,container,false);
        cardList = new ArrayList<>();

        context = container.getContext();
        tv_toolTip = view.findViewById(R.id.tv_toolTip);
        pref = context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        m_id = pref.getString("m_id", "");

        recyclerView = (RecyclerView) view.findViewById(R.id.cardlist_recy);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new CardAdapter();
        recyclerView.setAdapter(adapter);
        getData();
        button = view.findViewById(R.id.cardAdd);
        button.setOnClickListener(new View.OnClickListener() {
            @Override // 카드 등록. 카카오페이 정기 결제 등록 웹뷰 열기
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), KakaopayWebviewActivity.class);
                startActivity(intent);
            }
        });
        btn_back_cardManagement = view.findViewById(R.id.btn_back_cardManagement);
        btn_back_cardManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                My_info_Fragment my_info_fragment = new My_info_Fragment();
                transaction.replace(R.id.frameLayout, my_info_fragment).commit();
            }
        });
        refCard = view.findViewById(R.id.ref_card);
        refCard.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cardList.clear();
                adapter.clear();
                removeScrollPullUpListener(); //스와이프와(위에서 아래로 댕기는 새로고침) 리사이클러뷰 포지션 리스너를 같이 쓰면 에러가나므로 리사이클러뷰 리스너는 잠시 끊어주고 새로고침후 다시연결해준다.
                getData(); // 디비에서 값을 다시불러온다.

            }
        });

        btn_card_tooltip = view.findViewById(R.id.btn_card_tooltip);
        btn_card_tooltip.setOnClickListener(v -> {
            if (click == false) {
                tv_toolTip.setVisibility(View.VISIBLE);
                click = true;
            } else {
                tv_toolTip.setVisibility(View.GONE);
                click = false;
            }
        });
        return view;
    }

    // 카드 리스트 가져오기
    private void getData() {
        listRequest = DataService.getInstance().memberAPI.getUserCard(m_id);
        listRequest.enqueue(new Callback<List<CardData>>() {
            @Override
            public void onResponse(Call<List<CardData>> call, Response<List<CardData>> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        List<CardData> list = response.body();
                        Log.d("넘어오는 카드 정보 : " ,list.toString());
                        for (CardData data : list) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (listRequest != null) listRequest.cancel();
//        if(adapter.deleteCardRequest != null) adapter.deleteCardRequest.cancel();
//        if(adapter.selectCardRequest != null) adapter.selectCardRequest.cancel();
        if(adapter.kakaoInactiveRequest != null) adapter.kakaoInactiveRequest.cancel();
    }
}