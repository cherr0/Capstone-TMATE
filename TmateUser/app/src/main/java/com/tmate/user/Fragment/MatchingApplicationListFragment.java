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

import com.tmate.user.R;
import com.tmate.user.adapter.MatchingApplicationListAdapter;
import com.tmate.user.data.MatchingApplicationList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MatchingApplicationListFragment extends Fragment {
    private MatchingApplicationListAdapter adapter;

    private ArrayList<MatchingApplicationList> arrayList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_together_request,container,false);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.together_request_rv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();

        adapter = new MatchingApplicationListAdapter();
        recyclerView.setAdapter(adapter);

        getData();
        return v;

    }

    private void getData() {
        List<String> application_m_name = Arrays.asList(
                "김진수",
                "장원준",
                "강병현",
                "허시현"
        );
        List<String> application_read = Arrays.asList(
                "읽음",
                "안읽음",
                "안읽음",
                "읽음"
        );
        List<String> application_personnel = Arrays.asList(
                "(1/3)",
                "(2/3)",
                "(1/2)",
                "(1/3)"
        );
        List<String> application_start_place = Arrays.asList(
                "영진전문대",
                "경북대학교 북문",
                "성서청구타운",
                "경산 대평그린빌"
        );
        List<String> application_end_place = Arrays.asList(
                "경북대학교 북문",
                "성서청구타운",
                "영진전문대",
                "영진전문대"
        );


        for (int i = 0; i < application_m_name.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            MatchingApplicationList matchingApplicationList = new MatchingApplicationList();
            matchingApplicationList.setApplication_m_name(application_m_name.get(i));
            matchingApplicationList.setApplication_read(application_read.get(i));
            matchingApplicationList.setApplication_personnel(application_personnel.get(i));
            matchingApplicationList.setApplication_h_s_place(application_start_place.get(i));
            matchingApplicationList.setApplication_h_f_place(application_end_place.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(matchingApplicationList);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }
}