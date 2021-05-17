package com.tmate.user.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.tmate.user.R;
import com.tmate.user.adapter.RequestFriendAdapter;
import com.tmate.user.adapter.friendAdapter;
import com.tmate.user.data.Approval;
import com.tmate.user.data.CardData;
import com.tmate.user.data.FriendData;
import com.tmate.user.data.Notification;
import com.tmate.user.data.RequestFriendData;
import com.tmate.user.net.DataService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendFragment extends Fragment {
    ArrayList<String> list;
    private View view;
    private friendAdapter adFriend;
    private RequestFriendAdapter adRequest;
    private Button btn_add;
    private SwipeRefreshLayout refRequest;
    private SwipeRefreshLayout refFriend;
    private RecyclerView rvFriend;
    private RecyclerView rvRequest;

    // 레트로핏
    DataService dataService = DataService.getInstance();


    // 백버튼
    ImageView btn_back_friend;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friend, container, false);

        btn_back_friend = view.findViewById(R.id.btn_back_friend);

        rvFriend = view.findViewById(R.id.rv_friend);
        rvRequest = view.findViewById(R.id.rv_request);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvFriend.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        rvRequest.setLayoutManager(linearLayoutManager2);

        adFriend = new friendAdapter();
        rvFriend.setAdapter(adFriend);

        adRequest = new RequestFriendAdapter();
        rvRequest.setAdapter(adRequest);

        // 내 친구 리스트 - 데이터서비스이용
        getMyFriendList();
        
        // 나에게 승인 요청 리스트 - 데이터 서비스이용
        getReqFreindList();


        btn_add = view.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                FriendAddFragment friendAddFragment = new FriendAddFragment();
                transaction.replace(R.id.frameLayout, friendAddFragment).commit();
            }
        });

        btn_back_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                My_info_Fragment my_info_fragment = new My_info_Fragment();
                transaction.replace(R.id.frameLayout, my_info_fragment).commit();
            }
        });
        refRequest = view.findViewById(R.id.ref_request);
        refRequest.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ArrayList<Approval> reqFreind = new ArrayList<>();
                reqFreind.clear();
                adRequest.clear();
                requestremoveScrollPullUpListener(); //스와이프와(위에서 아래로 댕기는 새로고침) 리사이클러뷰 포지션 리스너를 같이 쓰면 에러가나므로 리사이클러뷰 리스너는 잠시 끊어주고 새로고침후 다시연결해준다.
                getReqFreindList(); // 디비에서 값을 다시불러온다.

            }
        });
        refFriend = view.findViewById(R.id.ref_friend);
        refFriend.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ArrayList<Notification> friend = new ArrayList<>();
                friend.clear();
                adFriend.clear();
                freindremoveScrollPullUpListener(); //스와이프와(위에서 아래로 댕기는 새로고침) 리사이클러뷰 포지션 리스너를 같이 쓰면 에러가나므로 리사이클러뷰 리스너는 잠시 끊어주고 새로고침후 다시연결해준다.
                getMyFriendList(); // 디비에서 값을 다시불러온다.


            }
        });

        return view;
    }

    // 나에게 요청한 지인 리스트
    private void getReqFreindList() {

        dataService.memberAPI.myApprovalList(getPreferenceString("m_id")).enqueue(new Callback<List<Approval>>() {
            @Override
            public void onResponse(Call<List<Approval>> call, Response<List<Approval>> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        List<Approval> approvalList = response.body();
                        Log.d("승인요청정보 : ", approvalList.toString());
                        RequestFriendData data = new RequestFriendData();

                        for (int i = 0; i < approvalList.size(); i++) {
                            data.setTv_id(approvalList.get(i).getId());
                            data.setTv_name2(approvalList.get(i).getName());
                            data.setTv_phone2(approvalList.get(i).getId().substring(2, 13));
                            adRequest.addItem(data);


                        }

                        adRequest.notifyDataSetChanged();
                        //카드 새로고침 후 로딩중 아이콘 지우기
                        refRequest.setRefreshing(false);

                    }
                }
            }

            @Override
            public void onFailure(Call<List<Approval>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    // 나의 지인
    private void getMyFriendList(){

        dataService.memberAPI.friendByUser(getPreferenceString("m_id")).enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        List<Notification> notificationList = response.body();
                        for (int i = 0; i < notificationList.size(); i++) {
                            FriendData friendData = new FriendData();
                            friendData.setTv_name(notificationList.get(i).getN_name());
                            friendData.setTv_phone(notificationList.get(i).getN_phone());
                            switch (notificationList.get(i).getN_whether()) {
                                case "0":
                                    friendData.setTv_flag("비활성화");
                                    break;
                                case "1":
                                    friendData.setTv_flag("활성화");
                                    break;
                            }
                            adFriend.addItem(friendData);
                        }

                        adFriend.notifyDataSetChanged();
                        //카드 새로고침 후 로딩중 아이콘 지우기
                        refFriend.setRefreshing(false);

                    }
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    // getPreferenceString
    public String getPreferenceString(String key) {
        SharedPreferences pref = getActivity().getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }
    private void requestremoveScrollPullUpListener(){
        rvRequest.removeOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }
        });
    }
    private void freindremoveScrollPullUpListener(){
        rvFriend.removeOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }
        });
    }
}