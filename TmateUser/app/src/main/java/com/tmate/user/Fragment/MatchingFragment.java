package com.tmate.user.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tmate.user.R;
import com.tmate.user.adapter.MatchingAdapter;
import com.tmate.user.data.History;
import com.tmate.user.data.MatchingData;
import com.tmate.user.databinding.FragmentMatchingBinding;
import com.tmate.user.net.DataService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MatchingFragment extends Fragment {
    private ArrayList<History> arrayList;
    private View view;
    private MatchingAdapter matchingAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private Button btn_add;

    // 레트로핏 이용
//    MatchDataService dataService = new MatchDataService();
    DataService dataService = DataService.getInstance();

    // 거리 순으로 나오게 할것이기 때문에 기준으로 인하여 필요한 값들이다.
    String slttd;
    String slngtd;
    String flttd;
    String flngtd;
    int to_max = 0;
    String h_s_place;
    String h_f_place;
    String h_ep_fare;
    String h_ep_time;
    String h_ep_distance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_matching, container, false);


        recyclerView = view.findViewById(R.id.rv_matching);

        //
//intent.putExtra("slttd", String.valueOf(tMapPointStart.getLatitude()));
//        intent.putExtra("slngtd", String.valueOf(tMapPointStart.getLongitude()));
//        intent.putExtra("flttd", String.valueOf(tMapPointEnd.getLatitude()));
//        intent.putExtra("flngtd", String.valueOf(tMapPointEnd.getLongitude()));
//        intent.putExtra("to_max", together);
//        intent.putExtra("h_s_place", h_s_place);
//        intent.putExtra("h_f_place", h_f_place);
//        intent.putExtra("h_ep_fare",totalFare);
//        intent.putExtra("h_ep_time",totalTime);
//        intent.putExra("h_ep_distance",totalDistance);


        if (getArguments() != null) {

            slttd = getArguments().getString("slttd");
            slngtd = getArguments().getString("slngtd");
            flttd = getArguments().getString("flttd");
            flngtd = getArguments().getString("flngtd");
            to_max = getArguments().getInt("to_max");
            h_s_place = getArguments().getString("h_s_place");
            h_f_place = getArguments().getString("h_f_place");
            h_ep_fare = getArguments().getString("h_ep_fare");
            h_ep_time = getArguments().getString("h_ep_time");
            h_ep_distance = getArguments().getString("h_ep_distance");

            Log.d("넘어오는 출발지 위도 : ", slttd);
            Log.d("넘어오는 출발지 경도 : ", slngtd);
            Log.d("넘어오는 도착지 위도 : ", flttd);
            Log.d("넘어오는 도착지 경도 : ", flngtd);
            Log.i("MatchingFragment 동승인원", String.valueOf(to_max));
            Log.d("MF 출발지 장 : ", h_s_place);
            Log.d("MF 도착지 장소 : ", h_f_place);
            Log.d("예상 시간 : ", h_ep_time);
            Log.d("예상 요금 : ", h_ep_fare);
            Log.d("예상 거리 : ", h_ep_distance);


        }


        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.refresh_layout);

        matchingAdapter = new MatchingAdapter(arrayList);
        recyclerView.setAdapter(matchingAdapter);
        getMatchingList();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arrayList.clear();
                getMatchingList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        btn_add = view.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = getArguments();
                Log.d("Bundle 값 잇나 확인 : ", bundle.getString("h_s_place"));


                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                MatchingSeatFragment matchingEnrollmentFragment = new MatchingSeatFragment();
                matchingEnrollmentFragment.setArguments(bundle);
                transaction.replace(R.id.fm_matching_activity, matchingEnrollmentFragment);
                transaction.commit();
            }
        });
        return view;
    }

    private void getMatchingList() {

        dataService.matchAPI.getMatchingList(slttd, slngtd, flttd, flngtd).enqueue(new Callback<List<History>>() {
            @Override
            public void onResponse(Call<List<History>> call, Response<List<History>> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        List<History> list = response.body();
                        Log.d("넘어오는 기준리스트", list.toString());
                        for (int i = 0; i < list.size(); i++) {
                            History history = new History();

                            // 매칭방 코드
                            history.setMerchant_uid(list.get(i).getMerchant_uid());

                            // 매칭 만든사람 방 이름 & 회원 코드
                            history.setM_id(list.get(i).getM_id());
                            history.setM_name(list.get(i).getM_name());

                            // 출발지 위도 경도 설정
                            history.setH_s_lttd(list.get(i).getH_s_lttd());
                            history.setH_s_lngtd(list.get(i).getH_s_lngtd());

                            // 도착지 위도 경도 설정
                            history.setH_f_lttd(list.get(i).getH_f_lttd());
                            history.setH_f_lngtd(list.get(i).getH_f_lngtd());

                            // 출발지 거리, 도착지 거리
                            history.setDistance1(list.get(i).getDistance1());
                            history.setDistance2(list.get(i).getDistance2());

                            // 장소명 설정
                            history.setH_s_place(list.get(i).getH_s_place());
                            history.setH_f_place(list.get(i).getH_f_place());

                            // 현재 인원
                            history.setTo_people(list.get(i).getTo_people());

                            // 최대 인원수 설정
                            history.setTo_max(list.get(i).getTo_max());


                            matchingAdapter.addItem(history);
                        }
                        matchingAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<History>> call, Throwable t) {
                t.printStackTrace();
            }
        });


        }
}