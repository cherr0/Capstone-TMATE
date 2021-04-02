package com.tmate.user.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.EventAdapter;
import com.tmate.user.EventData;

import com.tmate.user.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class event_cosle_item_pro extends Fragment {
    private ArrayList<EventData> arrayList;
    private View view;
    private EventAdapter eventAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;

    public static event_cosle_item_pro newInstance() {
        event_cosle_item_pro  ecip = new event_cosle_item_pro();
        return ecip;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.event_close_item_pro, container, false);

        recyclerView = view.findViewById(R.id.event_pro);

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();

        eventAdapter = new EventAdapter(arrayList);
        recyclerView.setAdapter(eventAdapter);
        getData();

        return view;
    }

    private void getData() {
        // 임의의 데이터입니다.
        List<Integer> listImage = Arrays.asList(
                R.drawable.spring_event,
                R.drawable.summer_event,
                R.drawable.fall_event,
                R.drawable.winter_event
        );
        List<String> listTitle = Arrays.asList(
                "봄 이벤트",
                "겨울 이벤트"
        );
        List<String> listSubTitle = Arrays.asList(
                "신학기 맞이 이벤트",
                "겨울방학 맞이 이벤트");
        List<String> listDate = Arrays.asList(
                "21.03.22 ~ 21.04.25",
                "21.12.03 ~ 22.01.03"
        );
        for (int i = 0; i < listTitle.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            EventData eventData = new EventData();
            eventData.setIv_event(listImage.get(i));
            eventData.setTv_title(listTitle.get(i));
            eventData.setTv_subTitle(listSubTitle.get(i));
            eventData.setTv_date(listDate.get(i));
            // 각 값이 들어간 data를 adapter에 추가합니다.
            eventAdapter.addItem(eventData);
        }
        // adapter의 값이 변경되었다는 것을 알려줍니다.
        eventAdapter.notifyDataSetChanged();
    }
}
