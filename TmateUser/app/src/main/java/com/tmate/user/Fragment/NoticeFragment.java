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

import com.tmate.user.NoticeAdapter;
import com.tmate.user.NoticeData;
import com.tmate.user.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class NoticeFragment extends Fragment {


    ArrayList<String> list;

    private NoticeAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_notice,container,false);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.notice_recy);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new NoticeAdapter();
        recyclerView.setAdapter(adapter);


        getData();
        return v;
    }

    private void getData() {
        // 임의의 데이터입니다.
        List<String> listTitle = Arrays.asList(
                "2021년 주의사항 안전수칙 ",
                "T 메이트 서비스 이용약관 개정 안내 ",
                "2021년에도 함께해요. T 메이트 택시 안전 이동 수칙",
                "T 메이트, 2021년 신규 오픈 대구에서 시작합니다."
        );
        List<String> listDate = Arrays.asList(
                "21.03.22",
                "21.03.14",
                "21.02.28",
                "21.01.16"
        );

        for (int i = 0; i < listTitle.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            NoticeData data = new NoticeData();
            data.setNotice_title(listTitle.get(i));
            data.setNotice_date(listDate.get(i));


            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }
}