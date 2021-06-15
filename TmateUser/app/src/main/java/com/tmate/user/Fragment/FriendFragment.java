package com.tmate.user.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.tmate.user.R;
import com.tmate.user.adapter.RequestFriendAdapter;
import com.tmate.user.adapter.friendAdapter;
import com.tmate.user.data.Approval;
import com.tmate.user.data.CardData;
import com.tmate.user.data.FriendData;
import com.tmate.user.data.Notification;
import com.tmate.user.data.RequestFriendData;
import com.tmate.user.data.UserHistroy;
import com.tmate.user.net.DataService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendFragment extends Fragment {
//    ArrayList<String> list;
    private View view;
    private friendAdapter adFriend;
    private Button btn_add;
    private RecyclerView rvFriend;
    private SwipeRefreshLayout refFriend;


    // 레트로핏
    DataService dataService = DataService.getInstance();

    // 지인 연락처 등록하기
    Call<Boolean> insertFriendPhoneNoRequest;


    // 백버튼
    ImageView btn_back_friend;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friend, container, false);

        btn_back_friend = view.findViewById(R.id.btn_back_friend);

        rvFriend = view.findViewById(R.id.rv_friend);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvFriend.setLayoutManager(linearLayoutManager);



        adFriend = new friendAdapter();
        rvFriend.setAdapter(adFriend);

        refFriend = view.findViewById(R.id.ref_friend);
        refFriend.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ArrayList<FriendData> fd = new ArrayList<>();
                fd.clear();
                adFriend.clear();
                removeScrollPullUpListener();
                getMyFriendList();

            }
        });



        // 내 친구 리스트 - 데이터서비스이용
        getMyFriendList();
        



        btn_add = view.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAndRequestPermission())
                    chooseContacts();

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
                            refFriend.setRefreshing(false);
                        }

                        adFriend.notifyDataSetChanged();
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



    /*
    * -------------------------
    * 연락쳐 가져오기
    * -------------------------
    * */
    public void chooseContacts() {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI); // 연락처 화면을 띄우기위한 인텐트 만들기
        startActivityForResult(contactPickerIntent, 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 101) {
                try {
                    Uri contactsUri = data.getData();
                    String id = contactsUri.getLastPathSegment(); // 선택한 연락처의 id 값 확인

                    getConTacts(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void getConTacts(String id) {
        Cursor cursor = null;
        String name = "";
        String phone = "";

        try {
            cursor = getActivity().getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                    null,
                    ContactsContract.Data.CONTACT_ID + "=?",
                    new String[]{id},
                    null);

            if (cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                phone = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DATA1));

                Log.d("연락처 찍히는 이름 : ", name);
                String columns[] = cursor.getColumnNames();
                for (String column : columns) {
                    int index = cursor.getColumnIndex(column);
                    String columnOutput = ("#" + index + " -> [" + column + "] " + cursor.getString(index));
                    Log.d("찍히는 출력물 : ", columnOutput);
                }

                cursor.close();

                // 여기서 DB연동
                Notification notification = new Notification();
                notification.setN_name(name);
                notification.setN_phone(phone.replace("-",""));
                notification.setM_id(getPreferenceString("m_id"));
                insertFriendPhoneNoRequest = DataService.getInstance().memberAPI.registerFriend(notification);
                insertFriendPhoneNoRequest.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.code() == 200) {
                            Toast.makeText(getContext(), "안심번호로 등록되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Boolean checkAndRequestPermission() {
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED)
            return true;

        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 100);
        return false;
    }
    private void removeScrollPullUpListener(){
        rvFriend.removeOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }
        });
    }


}