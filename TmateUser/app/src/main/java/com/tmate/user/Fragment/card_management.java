package com.tmate.user.Fragment;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class card_management  extends Fragment {
    Button button;
    LinearLayout nocard;
    ArrayList<String> list;

    private ImageView btn_back_cardManagement;

    private CardAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.card_management,container,false);

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

        return v;
    }

    private void getData() {
        // 임의의 데이터입니다.
        List<Integer> listImage = Arrays.asList(
                R.drawable.woori,
                R.drawable.sinhan,
                R.drawable.nh,
                R.drawable.kb
        );
        List<String> listTitle = Arrays.asList(
                "우리은행",
                "신한은행",
                "농협",
                "국민은행"
        );

        for (int i = 0; i < listTitle.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            CardData data = new CardData();
            data.setCardmark(listImage.get(i));
            data.setBankName(listTitle.get(i));


            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }




}
