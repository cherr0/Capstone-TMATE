package com.tmate.driver.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.driver.R;
import com.tmate.driver.adapter.NoticeAdapter;
import com.tmate.driver.data.Notice;
import com.tmate.driver.data.NoticeData;
import com.tmate.driver.net.DataService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticeFragment extends Fragment {

    private NoticeAdapter noticeAdapter = new NoticeAdapter();

    private List<Notice> noticelist;
    private Call<List<Notice>> request;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_notice,container,false);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.notice_recy);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(noticeAdapter);


        getData();
        return v;
    }

    private void getData() {
        request = DataService.getInstance().common.getNoticeList();

        request.enqueue(new Callback<List<Notice>>() {
            @Override
            public void onResponse(Call<List<Notice>> call, Response<List<Notice>> response) {
                if(response.isSuccessful()) {
                    if(response.code() == 200) {
                        noticelist = response.body();
                        Log.i("NoticeFragment", "noticelist : " + noticelist);

                        for(Notice notice : noticelist) {
                            noticeAdapter.addItem(notice);
                        }

                        // adapter의 값이 변경되었다는 것을 알려줍니다.
                        noticeAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Notice>> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        request.cancel();
    }
}