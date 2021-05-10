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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.tmate.user.R;
import com.tmate.user.adapter.CardAdapter;
import com.tmate.user.data.CardData;
import com.tmate.user.net.DataService;
import com.tmate.user.net.KakaopayWebviewActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class card_management  extends Fragment {
    Button button;
    LinearLayout nocard;
    ArrayList<String> list;

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

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.cardlist_recy);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new CardAdapter();
        recyclerView.setAdapter(adapter);
        getData();
        button = v.findViewById(R.id.cardAdd);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), KakaopayWebviewActivity.class);
                startActivity(intent);
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
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CardData>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}