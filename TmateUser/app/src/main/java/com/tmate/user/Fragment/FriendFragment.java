package com.tmate.user.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tmate.user.R;
import com.tmate.user.adapter.RequestFriendAdapter;
import com.tmate.user.adapter.friendAdapter;
import com.tmate.user.data.FriendData;
import com.tmate.user.data.RequestFriendData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FriendFragment extends Fragment {
    ArrayList<String> list;
    private View view;
    private friendAdapter adpater;
    private RequestFriendAdapter adapter2;
    private Button btn_add;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friend, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rv_friend);
        RecyclerView recyclerView2 = view.findViewById(R.id.rv_request);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        recyclerView2.setLayoutManager(linearLayoutManager2);

        adpater = new friendAdapter();
        recyclerView.setAdapter(adpater);

        adapter2 = new RequestFriendAdapter();
        recyclerView2.setAdapter(adapter2);

        getData();
        getData2();


        btn_add = view.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                FriendAddFragment friendAddFragment = new FriendAddFragment();
                transaction.replace(R.id.frameLayout, friendAddFragment).commit();
            }
        });

        return view;
    }

    private void getData2() {
        List<String> listName2 = Arrays.asList(
                "강병현",
                "박한수"
        );
        List<String> listPhone2 = Arrays.asList(
                "010-6248-0673",
                "010-5408-9299"
        );

        for (int i = 0; i < listName2.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            RequestFriendData data = new RequestFriendData();
            data.setTv_name2(listName2.get(i));
            data.setTv_phone(listPhone2.get(i));
            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter2.addItem(data);

        }
    }

    private void getData(){
        List<String> listName = Arrays.asList(
                "강병현",
                "박한수"
        );
        List<String> listPhone = Arrays.asList(
                "010-6248-0673",
                "010-5408-9299"
        );

        for (int i = 0; i < listName.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            FriendData data = new FriendData();
            data.setTv_name(listName.get(i));
            data.setTv_phone(listPhone.get(i));
            // 각 값이 들어간 data를 adapter에 추가합니다.
            adpater.addItem(data);

        }
    }
}