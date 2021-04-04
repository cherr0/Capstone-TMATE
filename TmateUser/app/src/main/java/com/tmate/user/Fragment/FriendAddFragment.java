package com.tmate.user.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tmate.user.R;
import com.tmate.user.data.Approval;
import com.tmate.user.data.Member;

import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendAddFragment extends Fragment {
    private View view;

    EditText editText;
    TextView search_name, search_gender, search_birth, search_phone;
    Button search_btn, btn_approval;
    ConstraintLayout constraintLayout;

    ImageView btn_back_friendadd;

    Approval approval;

    DataService dataService = new DataService();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_friend_add, container, false);

        // 백버튼
        btn_back_friendadd = view.findViewById(R.id.btn_back_friendadd);
        
        
        // 데이터 전송 관련
        editText = (EditText) view.findViewById(R.id.editText);
        search_name = (TextView) view.findViewById(R.id.search_name);
        search_gender = (TextView) view.findViewById(R.id.search_gender);
        search_birth = (TextView) view.findViewById(R.id.search_birth);
        search_phone = (TextView) view.findViewById(R.id.search_phone);
        search_btn = (Button) view.findViewById(R.id.search_btn);
        btn_approval = (Button) view.findViewById(R.id.btn_approval);
        constraintLayout = view.findViewById(R.id.constraintLayoutf);

        // 처음 화면에서 안보여준다.
        constraintLayout.setVisibility(View.INVISIBLE);


        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNo = editText.getText().toString();

                if (phoneNo.length() == 0) {
                    Toast.makeText(getActivity(), "전화번호를 입력하세요", Toast.LENGTH_LONG).show();
                    return;
                }


                dataService.select.searchMembers(phoneNo).enqueue(new Callback<List<Member>>() {
                    @Override
                    public void onResponse(Call<List<Member>> call, Response<List<Member>> response) {
                        if (response.isSuccessful()) {
                            if (response.code() == 200) {
                                Log.d("넘어오는 멤버 정보 : ", response.body().toString());
                                List<Member> memberList = response.body();

                                if (memberList != null) {

                                    // approval 초기화
                                    approval = new Approval();
                                    approval.setId(getPreferenceString("m_id"));
                                    approval.setName(getPreferenceString("m_name"));
                                    approval.setM_id(memberList.get(0).getM_id());

                                    // Response값 화면에 적용
                                    search_name.setText(memberList.get(0).getM_name());
                                    search_birth.setText(memberList.get(0).getM_birth().toString().substring(0,10));
                                    switch (memberList.get(0).getM_id().substring(1,1)) {
                                        case "1": case "3":
                                            search_gender.setText("남");
                                            break;
                                        case "2": case "4":
                                            search_gender.setText("여");
                                            break;
                                    }
                                    search_phone.setText(memberList.get(0).getM_id().substring(2,13));

                                    constraintLayout.setVisibility(View.VISIBLE);
                                }

                                else {
                                    Toast.makeText(getActivity(), "검색 결과가 없습니다.", Toast.LENGTH_LONG).show();

                                }

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Member>> call, Throwable t) {
                            t.printStackTrace();
                    }
                });




            }
        });

        btn_approval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataService.insert.approvalFriend(approval).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.isSuccessful()) {
                            if (response.code() == 200) {
                                // 초기화
                                editText.setText("");
                                // 콘스트래인트도 비움
                                constraintLayout.setVisibility(View.INVISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });
        
        // 백버튼 활성화
        btn_back_friendadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                FriendFragment friendFragment = new FriendFragment();
                transaction.replace(R.id.frameLayout, friendFragment).commit();
            }
        });


        return view;
    }

    // getPreferenceString
    public String getPreferenceString(String key) {
        SharedPreferences pref = getActivity().getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }
}