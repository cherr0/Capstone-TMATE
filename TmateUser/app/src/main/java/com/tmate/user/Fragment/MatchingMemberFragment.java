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
import com.tmate.user.adapter.MatchingMemberAdapter;
import com.tmate.user.data.MatchingMember;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MatchingMemberFragment extends Fragment {
    private MatchingMemberAdapter adapter;

    private ArrayList<MatchingMember> arrayList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_matching_member,container,false);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.matching_member_rv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();

        adapter = new MatchingMemberAdapter();
        recyclerView.setAdapter(adapter);

        getData();
        return v;

    }

    private void getData() {
        List<String> m_name = Arrays.asList(
                "김진수",
                "장원준",
                "강병현"
        );
        List<String> m_birth = Arrays.asList(
                "30대",
                "20대",
                "20대"
        );
        List<String> m_t_use = Arrays.asList(
                "14",
                "32",
                "27"
        );
        List<String> like = Arrays.asList(
                "2",
                "22",
                "24"
        );
        List<String> dislike = Arrays.asList(
                "22",
                "9",
                "2"
        );
        List<String> m_count = Arrays.asList(
                "3",
                "0",
                "1"
        );


        for (int i = 0; i < m_name.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            MatchingMember MatchingMember = new MatchingMember();
            MatchingMember.setM_name(m_name.get(i));
            MatchingMember.setM_birth(m_birth.get(i));
            MatchingMember.setM_t_use(m_t_use.get(i));
            MatchingMember.setLike(like.get(i));
            MatchingMember.setDislike(dislike.get(i));
            MatchingMember.setM_count(dislike.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(MatchingMember);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }
}