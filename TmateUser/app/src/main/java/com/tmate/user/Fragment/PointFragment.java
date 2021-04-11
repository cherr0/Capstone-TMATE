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

import com.tmate.user.R;
import com.tmate.user.adapter.pointAdapter;
import com.tmate.user.data.PointData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PointFragment extends Fragment {
    ArrayList<String> list;
    private pointAdapter adapter;
    TextView totalPo;
    List<String> listTime;
    List<String> listExpact;
    List<Integer> listporesult;
    List<String> listCourse;
    List<Integer> mPoint;
    private ImageView btn_back_point;
    private int po_point;

    // m_id 가져오기 위한 SharePreference 선언
    Context context;
    private static SharedPreferences pref;
    String m_id = "";

    // 레트로핏 2 -> 포인트 가져오는 HTTP 통신
    DataService dataService = new DataService();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_point,container,false);
        context = container.getContext();
        pref = context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        m_id = pref.getString("m_id", "");

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.pointRecy);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new pointAdapter();
        recyclerView.setAdapter(adapter);

        btn_back_point = v.findViewById(R.id.btn_back_point);
        btn_back_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                My_info_Fragment my_info_fragment = new My_info_Fragment();
                transaction.replace(R.id.frameLayout, my_info_fragment).commit();
            }
        });

        getData();

        totalPo = v.findViewById(R.id.totalPo);


        if (po_point != 0) {
            totalPo.setText(po_point);
        }else{
            totalPo.setText("0");
        }




        return v;
    }

    private void getData() {

        Log.d("넘어가는 회원코드", m_id);
       dataService.point.getPointList(m_id).enqueue(new Callback<List<PointData>>() {
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
                       }

                       adapter.notifyDataSetChanged();
                   }
               }
           }

           @Override
           public void onFailure(Call<List<PointData>> call, Throwable t) {
               t.printStackTrace();
           }
       });


    }
}
