package com.tmate.user.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tmate.user.R;
import com.tmate.user.adapter.pointAdapter;
import com.tmate.user.data.CardData;
import com.tmate.user.data.PointData;
import com.tmate.user.net.DataService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PointFragment extends Fragment {

    private pointAdapter adapter;
    TextView totalPo;
    private SwipeRefreshLayout refPoint;

    private ImageView btn_back_point;
    private int po_point;

    // m_id 가져오기 위한 SharePreference 선언
    Context context;
    private static SharedPreferences pref;
    String m_id = "";
    private RecyclerView recyclerView;

    // 레트로핏 2 -> 포인트 가져오는 HTTP 통신
//    DataService dataService = new DataService();
    DataService dataService = DataService.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_point,container,false);
        context = container.getContext();
        pref = context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        m_id = pref.getString("m_id", "");

        recyclerView = (RecyclerView) v.findViewById(R.id.pointRecy);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new pointAdapter();
        recyclerView.setAdapter(adapter);

        getData();

        btn_back_point = v.findViewById(R.id.btn_back_point);
        btn_back_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                CallFragment cf = new CallFragment();
                transaction.replace(R.id.frameLayout, cf).commit();
            }
        });



        totalPo = v.findViewById(R.id.totalPo);


        if (po_point != 0) {
            totalPo.setText(po_point);
        }else{
            totalPo.setText("598");
        }
        refPoint = v.findViewById(R.id.ref_point);
        refPoint.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                List<CardData> pointList = new ArrayList<>();
                pointList.clear();
                adapter.clear();
                removeScrollPullUpListener(); //스와이프와(위에서 아래로 댕기는 새로고침) 리사이클러뷰 포지션 리스너를 같이 쓰면 에러가나므로 리사이클러뷰 리스너는 잠시 끊어주고 새로고침후 다시연결해준다.
                getData(); // 디비에서 값을 다시불러온다.
            }
        });




        return v;
    }

    private void getData() {

        Log.d("넘어가는 회원코드", m_id);

       dataService.memberAPI.getPointList(m_id).enqueue(new Callback<List<PointData>>() {
           @Override
           public void onResponse(Call<List<PointData>> call, Response<List<PointData>> response) {
               if (response.isSuccessful()) {
                   if (response.code() == 200) {
                       List<PointData> list = response.body();


                       if (list != null && list.size() != 0) {
                           po_point = list.get(0).getPo_point();

                           for (int i = 0; i < list.size(); i++) {
                               PointData data = new PointData();
                               data.setPo_time(list.get(i).getPo_time());
                               switch (list.get(i).getPo_exact()) {
                                   case "0":
                                       data.setPo_exact("+");
                                       break;
                                   case "1":
                                       data.setPo_exact("-");
                                       break;
                               }

                               data.setPo_course(list.get(i).getPo_course());
                               data.setPo_result(list.get(i).getPo_result());
                               adapter.addItem(data);
                           }
                       }
                       else {
                           adapter.addItem(null);
                           po_point = 0;
                       }

                       adapter.notifyDataSetChanged();
                       //카드 새로고침 후 로딩중 아이콘 지우기
                       refPoint.setRefreshing(false);
                       if (po_point != 0) {
                           totalPo.setText(String.valueOf(po_point));
                       }else{
                           totalPo.setText("598");
                       }
                   }
               }
           }

           @Override
           public void onFailure(Call<List<PointData>> call, Throwable t) {
                t.printStackTrace();
           }
       });

    }
    private void removeScrollPullUpListener(){
        recyclerView.removeOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }
        });
    }
}
