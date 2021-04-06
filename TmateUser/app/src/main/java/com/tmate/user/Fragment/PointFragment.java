package com.tmate.user.Fragment;

import android.os.Bundle;
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
import com.tmate.user.adapter.PointAdapter;
import com.tmate.user.data.PointData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PointFragment extends Fragment {
    ArrayList<String> list;
    private PointAdapter adapter;
    TextView totalPo;
    List<String> listTime;
    List<String> listExpact;
    List<Integer> listporesult;
    List<String> listCourse;
    List<Integer> mPoint;
    private ImageView btn_back_point;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_point,container,false);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.pointRecy);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new PointAdapter();
        recyclerView.setAdapter(adapter);

        btn_back_point = v.findViewById(R.id.btn_back_point);
        btn_back_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                MoreFragment more_fragment = new MoreFragment();
                transaction.replace(R.id.frameLayout, more_fragment).commit();
            }
        });

        getData();

        totalPo = v.findViewById(R.id.totalPo);


        if(mPoint.get(0) ==null) {
            totalPo.setText("0");
        } else {
            totalPo.setText(Integer.toString(mPoint.get(0)));
        }


        return v;
    }

    private void getData() {
        // 임의의 데이터입니다.
        listTime = Arrays.asList(
               "2019/05/16/ 17:22:10",
                "2021/03/21/13:15:01",
                "2021/03/20/12:35:12",
                "2021/03/19/03:03:42",
                "2021/03/18/21:17:35",
                "2021/03-15/20:07:32"
               );

        listExpact = Arrays.asList(
                "-",
                "+",
                "-",
                "+",
                "-",
                "-"
        );
        listporesult = Arrays.asList(
                10,
                20,
                30,
                100,
                50,
                60
        );

        mPoint = Arrays.asList(
                1970,
                1980,
                1960,
                1990,
                1850,
                1940
        );


        listCourse = Arrays.asList(
                "택시결제",
                "이벤트",
                "택시결제",
                "이벤트",
                "택시결제",
                "택시결제"
        );

        for (int i = 0; i < listTime.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            PointData data = new PointData();
            data.setPoTime(listTime.get(i));
            data.setPoResult(listporesult.get(i));
            data.setmPoint(mPoint.get(i));
            data.setPoExact(listExpact.get(i));
            data.setPoCourse(listCourse.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }
}
