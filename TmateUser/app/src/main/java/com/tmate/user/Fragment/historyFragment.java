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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class historyFragment extends Fragment {
    ArrayList<String> list;

    private historyAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.history,container,false);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.hcard);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new historyAdapter();
        recyclerView.setAdapter(adapter);


        getData();
        return v;
    }

    private void getData() {
        // 임의의 데이터입니다.
        List<String> listStart = Arrays.asList("대구 북구 복현동 영진전문대 정문","대구 북구 복현동 영진전문대 정문","대웅아파트","장미아파트","대구 동구 신세계백화점","대구 중구 스파크랜드");
        List<String> listTogether = Arrays.asList("일반","동승","일반","일반","동승","동승");
        List<String> listDate = Arrays.asList("19/04/11","20/04/20","20/11/12","20/12/05","20/12/07","21/03/21");
        List<String> listTime = Arrays.asList("12:32-11:48","11:32-12:47","09:28-12:01","05:48-06:02","03:16-04:00","11:29-12:00");
        List<String> listFinish = Arrays.asList("장미아파트","장미아파트","대구 북구 복현동 후문","대구 북구 복현동 정문","스파크랜드","대구대학교");
        List<String> listDrivername = Arrays.asList("김*치","박*우","김*일","배*나","하*연","김*철");
        List<String> listCarinfo = Arrays.asList("대구 32바 18062 | 쏘나타","대구 16바 84551 | 쏘나타","대구 17바 89543 | k3","대구 62바 78942 | 쏘나타","대구 64바 78132 | k3","대구 23바 75123 | k3");

        for (int i = 0; i < listStart.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            Data data = new Data();
            data.setStart(listStart.get(i));
            data.setTogether(listTogether.get(i));
            data.setDate(listDate.get(i));
            data.setTime(listTime.get(i));
            data.setFinish(listFinish.get(i));
            data.setDrivername(listDrivername.get(i));
            data.setCarinfo(listCarinfo.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }
}
