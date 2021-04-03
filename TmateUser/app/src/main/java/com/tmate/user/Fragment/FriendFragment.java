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
import com.tmate.user.data.FriendData;
import com.tmate.user.data.Notification;
import com.tmate.user.data.RequestFriendData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendFragment extends Fragment {
    ArrayList<String> list;
    private View view;
    private friendAdapter adpater;
    private RequestFriendAdapter adapter2;
    private Button btn_add;

    // 레트로핏
    DataService dataService = new DataService();

    // 백버튼
    ImageView btn_back_friend;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friend, container, false);

        btn_back_friend = view.findViewById(R.id.btn_back_friend);

        RecyclerView recyclerView = view.findViewById(R.id.rv_friend);
        RecyclerView recyclerView2 = view.findViewById(R.id.rv_request);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        recyclerView2.setLayoutManager(linearLayoutManager2);

        adpater = new friendAdapter();
        recyclerView.setAdapter(adpater);

        adapter2 = new RequestFriendAdapter();
        recyclerView2.setAdapter(adapter2);

        getData();
        getData2();


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

        return view;
    }

    // 나에게 요청한 지인 리스트
    private void getData2() {

        dataService.select.myApprovalList(getPreferenceString("m_id")).enqueue(new Callback<List<Approval>>() {
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
                            adapter2.addItem(data);


                        }

                        adapter2.notifyDataSetChanged();

//                        List<String> listName2 = Arrays.asList(
//                                "이황",
//                                "박한수"
//                        );
//                        List<String> listPhone2 = Arrays.asList(
//                                "010-6248-0673",
//                                "010-5408-9299"
//                        );

//                        for (int i = 0; i < listName2.size(); i++) {
//                            // 각 List의 값들을 data 객체에 set 해줍니다.
//                            RequestFriendData data = new RequestFriendData();
//                            data.setTv_name2(listName2.get(i));
//                            data.setTv_phone(listPhone2.get(i));
//                            // 각 값이 들어간 data를 adapter에 추가합니다.
//                            adapter2.addItem(data);
//
//                        }


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
    private void getData(){

        dataService.select.friendByUser(getPreferenceString("m_id")).enqueue(new Callback<List<Notification>>() {
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
                            adpater.addItem(friendData);
                        }

                        adpater.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {

            }
        });

//        List<String> listName = Arrays.asList(
//                "이황",
//                "박한수"
//        );
//        List<String> listPhone = Arrays.asList(
//                "010-6248-0673",
//                "010-5408-9872"
//        );
//
//        for (int i = 0; i < listName.size(); i++) {
//            // 각 List의 값들을 data 객체에 set 해줍니다.
//            FriendData data = new FriendData();
//            data.setTv_name(listName.get(i));
//            data.setTv_phone(listPhone.get(i));
//            // 각 값이 들어간 data를 adapter에 추가합니다.
//            adpater.addItem(data);
//
//        }
    }

    // getPreferenceString
    public String getPreferenceString(String key) {
        SharedPreferences pref = getActivity().getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }
}