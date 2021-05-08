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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tmate.user.adapter.NoticeAdapter;
import com.tmate.user.R;
import com.tmate.user.data.CardData;
import com.tmate.user.data.Notice;
import com.tmate.user.net.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NoticeFragment extends Fragment {

    DataService dataService = DataService.getInstance();

    private ArrayList<Notice> arrayList;
    private NoticeAdapter noticeAdapter;
    private SwipeRefreshLayout refNotice;
    private RecyclerView recyclerView;

    private ImageView btn_back_notice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_notice,container,false);

        recyclerView = (RecyclerView) v.findViewById(R.id.notice_recy);

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
        refNotice = v.findViewById(R.id.ref_notice);
        refNotice.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arrayList = new ArrayList<>();
                arrayList.clear();
                noticeAdapter.clear();
                removeScrollPullUpListener(); //스와이프와(위에서 아래로 댕기는 새로고침) 리사이클러뷰 포지션 리스너를 같이 쓰면 에러가나므로 리사이클러뷰 리스너는 잠시 끊어주고 새로고침후 다시연결해준다.
                getData(); // 디비에서 값을 다시불러온다.

            }
        });

        getData();
        return v;
    }

    private void getData() {

        dataService.commonAPI.getNoticeList().enqueue(new Callback<List<Notice>>() {
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
                        //카드 새로고침 후 로딩중 아이콘 지우기
                        refNotice.setRefreshing(false);

                    }
                }
            }

            @Override
            public void onFailure(Call<List<Notice>> call, Throwable t) {
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