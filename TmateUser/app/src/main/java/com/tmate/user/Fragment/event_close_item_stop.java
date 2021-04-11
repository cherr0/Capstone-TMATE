package com.tmate.user.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.adapter.EventAdapter;
import com.tmate.user.data.EventData;
import com.tmate.user.R;
import com.tmate.user.data.EventDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class event_close_item_stop extends Fragment {
    private ArrayList<EventData> arrayList;
    private View view;
    private EventAdapter eventAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;

    CommonService commonService = new CommonService();

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

        commonService.event.getFinishEventList().enqueue(new Callback<List<EventDTO>>() {
            @Override
            public void onResponse(Call<List<EventDTO>> call, Response<List<EventDTO>> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        List<EventDTO> eventDTOList = response.body();
                        Log.d("종료된 이벤트 잘넘오나용", eventDTOList.toString());
                        for (int i = 0; i < eventDTOList.size(); i++) {
                            EventData eventData = new EventData();
                            eventData.setIv_event(R.drawable.tmateevent);
                            switch (eventDTOList.get(i).getBd_id().substring(0,2)) {
                                case "wi":
                                    eventData.setTv_title("겨울 이벤트");
                                    break;
                                case "sp":
                                    eventData.setTv_title("봄 이벤트");
                                    break;
                                case "sm":
                                    eventData.setTv_title("여름 이벤트");
                                    break;
                                case "fa":
                                    eventData.setTv_title("가을 이벤트");
                                    break;
                            }
                            // 날짜 작업
                            String st = eventDTOList.get(i).getBd_s_date().toString();
                            String et = eventDTOList.get(i).getBd_e_date().toString();
                            eventData.setTv_date(st.substring(2,4)+"."+st.substring(5,7)+"."+st.substring(8,10)+"~"+et.substring(2,4)+"."+et.substring(5,7)+"."+et.substring(8,10));
                            eventData.setTv_subTitle(eventDTOList.get(i).getBd_title());
                            // 이벤트 상세화면을 위한 이벤트
                            eventData.setTv_bd_id(eventDTOList.get(i).getBd_id());
                            eventAdapter.addItem(eventData);
                        }
                        eventAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<EventDTO>> call, Throwable t) {

            }
        });


    }
}
