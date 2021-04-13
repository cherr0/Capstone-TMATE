package com.tmate.driver.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.driver.R;
import com.tmate.driver.adapter.BlacklistManagementAdapter;
import com.tmate.driver.data.BlacklistManagementData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Blacklist_managementFragment extends Fragment {
    private BlacklistManagementAdapter adapter;

    private ArrayList<BlacklistManagementData> arrayList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_blacklist_management,container,false);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.black_list_rv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();

        adapter = new BlacklistManagementAdapter();
        recyclerView.setAdapter(adapter);

        getData();
        return v;

    }

    private void getData() {
        List<String> black_name = Arrays.asList(
                "김진수",
                "장원준",
                "박한수",
                "허시현"
        );
        List<String> black_date = Arrays.asList(
                "21/03/26",
                "21/03/24",
                "21/03/21",
                "21/03/18"
        );
        List<String> blakc_content = Arrays.asList(
                "너무 시끄러워요",
                "운전 방해를 합니다",
                "오랜시간 나타나지 않습니다.",
                "인격모독을 합니다."
        );

        for (int i = 0; i < black_name.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            BlacklistManagementData blackManageData = new BlacklistManagementData();
            blackManageData.setBlack_name(black_name.get(i));
            blackManageData.setBlack_date(black_date.get(i));
            blackManageData.setBlack_content(blakc_content.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(blackManageData);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }
}