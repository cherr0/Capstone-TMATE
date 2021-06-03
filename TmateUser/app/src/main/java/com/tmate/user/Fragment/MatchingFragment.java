package com.tmate.user.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tmate.user.R;
import com.tmate.user.adapter.MatchingAdapter;
import com.tmate.user.data.Attend;
import com.tmate.user.data.Dispatch;
import com.tmate.user.data.History;
import com.tmate.user.net.DataService;
import com.tmate.user.ui.driving.DrivingModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MatchingFragment extends Fragment {

    // view 모델
    private DrivingModel mViewModel;

    private ArrayList<Dispatch> arrayList;
    private View view;
    private MatchingAdapter matchingAdapter;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView;
    private Button btn_add;
    TextView tooltip;

    // 레트로핏 이용
    Call<List<History>> matchingRequest;

    Call<List<Dispatch>> getMatchListRequest;

    // 거리 순으로 나오게 할것이기 때문에 기준으로 인하여 필요한 값들이다.
    double s_lat;
    double s_lng;
    double f_lat;
    double f_lng;
    int to_max = 0;
    String h_s_place;
    String h_f_place;
    String h_ep_fare;
    String h_ep_time;
    String h_ep_distance;

    TextView matching_start_place;
    TextView matching_finish_place;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_matching, container, false);

        // view모델 삽입
        mViewModel = new ViewModelProvider(requireActivity()).get(DrivingModel.class);

        recyclerView = view.findViewById(R.id.rv_matching);

        matching_start_place = view.findViewById(R.id.matching_start_place);
        matching_finish_place = view.findViewById(R.id.matching_finish_place);

        Log.d("한번찍어보자 출위도", String.valueOf(mViewModel.dispatch.getStart_lat()));
        Log.d("한번찍어보자 출경도", String.valueOf(mViewModel.dispatch.getStart_lng()));
        Log.d("한번찍어보자 도위도", String.valueOf(mViewModel.dispatch.getFinish_lat()));
        Log.d("한번찍어보자 도경도", String.valueOf(mViewModel.dispatch.getFinish_lng()));

        s_lat = mViewModel.dispatch.getStart_lat();
        s_lng = mViewModel.dispatch.getStart_lng();
        f_lat = mViewModel.dispatch.getFinish_lat();
        f_lng = mViewModel.dispatch.getFinish_lng();

        matching_start_place.setText(mViewModel.dispatch.getStart_place());
        matching_finish_place.setText(mViewModel.dispatch.getFinish_place());

        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        arrayList = new ArrayList<>();

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.refresh_layout);

        matchingAdapter = new MatchingAdapter(arrayList,requireActivity(),mViewModel);
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
                // 좌석 선택
                NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                controller.navigate(R.id.action_matchingFragment_to_matchingSeatFragment);

            }
        });
        return view;
    }

    private void getMatchingList() {

        matchingRequest = DataService.getInstance().matchAPI.getMatchingList(String.valueOf(s_lat), String.valueOf(s_lng), String.valueOf(f_lat), String.valueOf(f_lng));

        getMatchListRequest = DataService.getInstance().matchAPI.getTogetherList(s_lat, s_lng, f_lat, f_lng);
        getMatchListRequest.enqueue(new Callback<List<Dispatch>>() {
            @Override
            public void onResponse(Call<List<Dispatch>> call, Response<List<Dispatch>> response) {
                if (response.code() == 200 && response.body() != null) {
                    List<Dispatch> list = response.body();
                    Log.d("MatchingFragment", list.toString());

                    for (int i = 0; i < list.size(); i++) {
                        Dispatch dispatch = new Dispatch();


                        // 매칭방 코드
                        dispatch.setDp_id(list.get(i).getDp_id());

                        // 출발지 위도 경도 설정
                        dispatch.setStart_lat(list.get(i).getStart_lat());
                        dispatch.setStart_lng(list.get(i).getStart_lng());

                        // 도착지 위도 경도 설정
                        dispatch.setFinish_lat(list.get(i).getFinish_lat());
                        dispatch.setFinish_lng(list.get(i).getFinish_lng());


                        // 장소명 설정
                        dispatch.setStart_place(list.get(i).getStart_place());
                        dispatch.setFinish_place(list.get(i).getFinish_place());

                        // 현재 인원
                        dispatch.setCur_people(list.get(i).getCur_people());

                        matchingAdapter.addItem(dispatch);
                    }
                    matchingAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Dispatch>> call, Throwable t) {
                t.printStackTrace();
            }
        });



        }

    @Override
    public void onDestroy() {
        super.onDestroy();
        matchingRequest.cancel();
    }
}