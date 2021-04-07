package com.tmate.user.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.adapter.NoticeAdapter;
import com.tmate.user.R;
import com.tmate.user.data.Notice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NoticeFragment extends Fragment {

    private CommonService commonService = new CommonService();

    private ArrayList<Notice> arrayList;
    private NoticeAdapter noticeAdapter;

    private ImageView btn_back_notice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_notice,container,false);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.notice_recy);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();

        noticeAdapter = new NoticeAdapter(arrayList);
        recyclerView.setAdapter(noticeAdapter);


        // 뒤로가기 버튼 클릭
        btn_back_notice = v.findViewById(R.id.btn_back_notice);
        btn_back_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                My_info_Fragment my_info_fragment = new My_info_Fragment();
                transaction.replace(R.id.frameLayout, my_info_fragment).commit();
            }
        });

        getData();
        return v;
    }

    private void getData() {

        commonService.notice.getNoticeList().enqueue(new Callback<List<Notice>>() {
            @Override
            public void onResponse(Call<List<Notice>> call, Response<List<Notice>> response) {

                if(response.isSuccessful()){
                    if(response.code() == 200) {
                        List<Notice> noticeList = response.body();    // 값 받아오기

                        // 어댑터에 각 아이템 추가
                        for(Notice notice : noticeList) {
                            noticeAdapter.addItem(notice);
                        }

                        noticeAdapter.notifyDataSetChanged();   // 어댑터에 값 변경 알림
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Notice>> call, Throwable t) {
                t.printStackTrace();
            }
        });


        /*
        // 이전 예제 코드
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
            NoticeData noticeData = new NoticeData();
            noticeData.setNotice_title(listTitle.get(i));
            noticeData.setNotice_date(listDate.get(i));


            // 각 값이 들어간 data를 adapter에 추가합니다.
            noticeAdapter.addItem(noticeData);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        noticeAdapter.notifyDataSetChanged();
        */
    }
}