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

public class event_close_item_stop extends Fragment {
    private ArrayList<EventData> arrayList;
    private View view;
    private EventAdapter eventAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;

    public static event_close_item_stop newInstance() {
        event_close_item_stop ecis = new event_close_item_stop();
        return ecis;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.event_close_itemp_stop, container, false);

        recyclerView = view.findViewById(R.id.event_stop);

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
                R.drawable.summer_event,
                R.drawable.fall_event
        );
        List<String> listTitle = Arrays.asList(
                "여름 이벤트",
                "가을 이벤트"
        );
        List<String> listSubTitle = Arrays.asList(
                "여름방학 맞이 이벤트",
                "식욕의 계절 이벤트");
        List<String> listDate = Arrays.asList(
                "20.07.06 ~ 20.08.06",
                "20.09.09 ~ 20.10.29"
        );

        for (int i = 0; i < listTitle.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            EventData data = new EventData();
            data.setIv_event(listImage.get(i));
            data.setTv_title(listTitle.get(i));
            data.setTv_subTitle(listSubTitle.get(i));
            data.setTv_date(listDate.get(i));

            // adapter의 값이 변경되었다는 것을 알려줍니다.
            eventAdapter.notifyDataSetChanged();
        }
    }
}
