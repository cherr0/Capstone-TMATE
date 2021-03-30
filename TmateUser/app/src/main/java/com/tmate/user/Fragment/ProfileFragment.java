package com.tmate.user.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tmate.user.R;
import com.tmate.user.data.Member;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {
    private View view;

    DataService dataService = new DataService();

    ImageView iv_level;

    TextView tv_name, tv_like, tv_dislike, tv_name2, tv_phone, tv_email, tv_gender, tv_birth, tv_grade,
    tv_point, tv_m_n_use, tv_m_t_use, tv_m_count;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        initWidget(view);

        findData();


        return view;
    }



    public void initWidget(View v) {

        iv_level = (ImageView) v.findViewById(R.id.iv_level);

        tv_name = (TextView) v.findViewById(R.id.tv_name);
        tv_like = v.findViewById(R.id.tv_like);
        tv_dislike = v.findViewById(R.id.tv_dislike);
        tv_name2 = v.findViewById(R.id.tv_name2);
        tv_phone = v.findViewById(R.id.tv_phone);
        tv_email = v.findViewById(R.id.tv_email);
        tv_gender = v.findViewById(R.id.tv_gender);
        tv_birth = v.findViewById(R.id.tv_birth);
        tv_grade = v.findViewById(R.id.tv_grade);
        tv_point = v.findViewById(R.id.tv_point);
        tv_m_n_use = v.findViewById(R.id.tv_m_n_use);
        tv_m_t_use = v.findViewById(R.id.tv_m_t_use);
        tv_m_count = v.findViewById(R.id.tv_m_count);

    }

    public void findData() {
        String m_id = getPreferenceString("m_id");

        dataService.select.selectProfile(m_id).enqueue(new Callback<Member>() {
            @Override
            public void onResponse(Call<Member> call, Response<Member> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
//                        Member member = response.body();


                        Member member = response.body();

                        tv_like.setText(member.getLike()+"");
                        tv_dislike.setText(member.getDislike()+"");

                        switch (member.getM_level()) {
                            case "0":
                                iv_level.setImageResource(R.drawable.slver);
                                break;

                            case "1":
                                iv_level.setImageResource(R.drawable.gold);
                                break;

                            case "2":
                                iv_level.setImageResource(R.drawable.platinum);
                                break;

                            case "3":
                                iv_level.setImageResource(R.drawable.vip);
                                break;
                        }

                        tv_name.setText(member.getM_name());
                        tv_name2.setText(member.getM_name());
                        tv_phone.setText(member.getM_id().substring(2,13));
                        tv_email.setText(member.getM_email());

                        switch (member.getM_id().substring(1, 2)) {
                            case "1": case "3":
                                tv_gender.setText("남자");
                                break;
                            case "2": case "4":
                                tv_gender.setText("여자");
                                break;
                        }
                        Log.d("넘어오는 생년월일", member.getM_birth().toString());
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//                        tv_birth.setText(sdf.format(member.getM_birth()));
                        tv_birth.setText(member.getM_birth().toString().substring(0,10));

                        tv_point.setText(member.getM_point() + "P");

                        tv_m_n_use.setText(member.getM_n_use()+"회");
                        tv_m_t_use.setText(member.getM_t_use()+"회");
                        tv_m_count.setText(member.getM_count()+"회");


                    }
                }
            }

            @Override
            public void onFailure(Call<Member> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    // getPreferenceString
    public String getPreferenceString(String key) {
        SharedPreferences pref = getActivity().getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }
}