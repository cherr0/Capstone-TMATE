package com.tmate.user;

import android.os.Bundle;
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

import com.tmate.user.Fragment.MatchingAdapter;
import com.tmate.user.Fragment.MatchingData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MatchingFragment extends Fragment {
    private ArrayList<MatchingData> arrayList;
    private View view;
    private MatchingAdapter matchingAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private Button btn_add;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_matching, container, false);

        recyclerView = view.findViewById(R.id.rv_matching);

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.refresh_layout);

        matchingAdapter = new MatchingAdapter(arrayList);
        recyclerView.setAdapter(matchingAdapter);
        getData();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arrayList.clear();
                getData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        btn_add = view.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                MatchingSeatFragment matchingEnrollmentFragment = new MatchingSeatFragment();
                transaction.replace(R.id.macing_frame, matchingEnrollmentFragment);
                transaction.commit();
            }
        });
        return view;
    }

    private void getData() {
            // 임의의 데이터입니다.
            List<String> listName = Arrays.asList(
                    "박한수",
                    "강병현",
                    "김진수"
            );
            List<String> listPersonnel = Arrays.asList(
                    "(1/2)",
                    "(1/2)",
                    "(1/2)"
            );
            List<String> listStartPlace = Arrays.asList(
                    "대구광역시 북구 영진전문대학교",
                    "대구광역시 북구 대구공항",
                    "대구광역시 동구 동대구역"
            );
            List<String> listEndPlace = Arrays.asList(
                    "대구광역시 북구 영진전문대학교",
                    "대구광역시 달서구 성서청구타운",
                    "대구광역시 북구 영진전문대학교"
            );

            for (int i = 0; i < listName.size(); i++) {
                // 각 List의 값들을 data 객체에 set 해줍니다.
                MatchingData matchingData = new MatchingData();
                matchingData.setTv_matching_name(listName.get(i));
                matchingData.setTv_personnel(listPersonnel.get(i));
                matchingData.setTv_start_place(listStartPlace.get(i));
                matchingData.setTv_end_place(listEndPlace.get(i));
                // 각 값이 들어간 data를 adapter에 추가합니다.
                matchingAdapter.addItem(matchingData);
            }
            // adapter의 값이 변경되었다는 것을 알려줍니다.
        matchingAdapter.notifyDataSetChanged();
        }
}