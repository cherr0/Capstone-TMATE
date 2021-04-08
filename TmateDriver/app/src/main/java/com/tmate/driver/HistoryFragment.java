package com.tmate.driver;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HistoryFragment extends Fragment {

    private HistoryAdapter adapter;

    private ArrayList<HistoryData> arrayList;


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
        List<String> hDate = Arrays.asList(
                "21/03/26",
                "21/03/24",
                "21/03/21",
                "21/03/18"
        );
        List<String> hTogether = Arrays.asList(
                "일반",
                "일반",
                "동승",
                "일반"
        );
        List<String> re_amt = Arrays.asList(
                "3500원",
                "5300원",
                "4100원",
                "3200원"
        );
        List<String> history_username = Arrays.asList(
                "강병현",
                "박한수",
                "허시현",
                "하창현"
        );
        List<String> hstart = Arrays.asList(
                "영진전문대학교 정문",
                "경북대학교 북문",
                "계명대학교 동문",
                "이월드"
        );
        List<String> hfinish = Arrays.asList(
                "경북대학교 북문",
                "2.28공원",
                "성서청구타운",
                "영진전문대학교 후문"
        );
        List<String> htime = Arrays.asList(
                "03:28 - 03:33",
                "21:38 - 22:06",
                "00:46 - 00:58",
                "07:13 - 07:52"
        );

        for (int i = 0; i < hDate.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            HistoryData historyData = new HistoryData();
            historyData.setHdate(hDate.get(i));
            historyData.setHtogether(hTogether.get(i));
            historyData.setRe_amt(re_amt.get(i));
            historyData.setHistory_username(history_username.get(i));
            historyData.setHstart(hstart.get(i));
            historyData.setHfinish(hfinish.get(i));
            historyData.setHtime(htime.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(historyData);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }

}