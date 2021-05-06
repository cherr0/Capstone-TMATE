package com.tmate.user.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.tmate.user.Activity.CallGeneralActivity;
import com.tmate.user.Activity.MatchingActivity;
import com.tmate.user.Activity.MatchingMapActivity;
import com.tmate.user.R;
import com.tmate.user.data.Approval;
import com.tmate.user.data.History;
import com.tmate.user.data.Together;
import com.tmate.user.databinding.FragmentMatchingSeatBinding;
import com.tmate.user.net.DataService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MatchingSeatFragment extends Fragment {
    private View view;
    private int togetherAdd = 0;
    private FragmentMatchingSeatBinding b;


    Call<Boolean> request;
    Call<List<Together>> request2;
    Call<Boolean> request3;

    Bundle bundle;
    History history;
    Together together;

    private int to_seat;

    /*
    * 기존 방에서 넘어올때 필요한 것들
    * */
    private String merchant_uid;
    private String list_m_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentMatchingSeatBinding.inflate(getLayoutInflater());
        view = b.getRoot();



        // 새로 추가하기 - 리스트
        if(getArguments().getString("merchant_uid") == null) {
            Log.d("만약 널이라면","여기가 찍히겠");
            bundle = getArguments();



            //자리 선택 완료 후 버튼클릭이벤트
            b.btnSeat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //매칭방 만들때의 경로(자리 선택 후 매칭리스트로 이동)
                    if(togetherAdd == 0) {

                        Log.d("찍히긴 찍히냐", "반응은 하냐? 좀 해라 궁금하다 왜 안되냐");
                        initializeObjects(bundle);

                        HashMap<String, Object> hashmap = new HashMap<>();

                        hashmap.put("history", history);
                        hashmap.put("together", together);



                        request = DataService.getInstance().matchAPI.registerMatchingRegister(hashmap);

                        request.enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                if (response.isSuccessful()) {
                                    if (response.code() == 200) {
                                        Log.d("넘어온거 보면 성공했겠죠?", String.valueOf(response.body()));
                                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                        CarInfoFragment mf = new CarInfoFragment();
                                        transaction.replace(R.id.fm_matching_activity, mf);
                                        transaction.commit();

                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });

                    } else { //이미 만들어진 매칭방에서 들어왔을때의 경로(결제화면으로 이동)
                        Intent intent = new Intent(getContext(),CallGeneralActivity.class);
                        startActivity(intent);
                    }
                }
            });

            seatClickEvent();
        }
        // 기존의 방에서 좌석 선
        else{
            Log.d("만약 널이아니라면","여기가 찍히겠");

            merchant_uid = getArguments().getString("merchant_uid");
            list_m_id = getArguments().getString("list_m_id");

            request2 = DataService.getInstance().matchAPI.getCurrentSeatNums(merchant_uid);

            request2.enqueue(new Callback<List<Together>>() {
                @Override
                public void onResponse(Call<List<Together>> call, Response<List<Together>> response) {
                    List<Together> list = response.body();

                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) {
                            switch (list.get(i).getTo_seat()) {
                                case 1:
                                    b.seatOne.setChecked(true);
                                    b.seatOne.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(getContext(), "이미 지정된 좌석입니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    break;
                                case 2:
                                    b.seatTwo.setChecked(true);
                                    b.seatTwo.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(getContext(), "이미 지정된 좌석입니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    break;
                                case 3:
                                    b.seatThree.setChecked(true);
                                    b.seatThree.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(getContext(), "이미 지정된 좌석입니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    break;
                            }
                        }
                    } // for 끝

                    seatClickEvent();

                    Approval approval = new Approval();

                    approval.setId(getActivity().getSharedPreferences("loginUser",Context.MODE_PRIVATE).getString("m_id",""));
                    approval.setName(getActivity().getSharedPreferences("loginUser", Context.MODE_PRIVATE).getString("m_name", ""));
                    approval.setTo_seat(to_seat);
                    approval.setM_id(list_m_id);
                    approval.setMerchant_uid(merchant_uid);

                    b.btnSeat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            request3 = DataService.getInstance().matchAPI.registerApply(approval);
                            request3.enqueue(new Callback<Boolean>() {
                                @Override
                                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                    // 여기서는 화면 이동 할 예정
                                    Toast.makeText(getActivity(), "신청하였습니다.", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<Boolean> call, Throwable t) {

                                }
                            });
                        }
                    });


                }

                @Override
                public void onFailure(Call<List<Together>> call, Throwable t) {

                }
            });

        }




        return view;
    }

    // 두 객체 초기화
    public void initializeObjects(Bundle bundle) {


            try {

                history = new History();
                together = new Together();
                String m_id = getActivity().getSharedPreferences("loginUser", Context.MODE_PRIVATE).getString("m_id", "");

                history.setM_id(m_id);

                history.setH_s_place(bundle.getString("h_s_place"));
                history.setH_s_lttd(Double.valueOf(bundle.getString("slttd")));
                history.setH_s_lngtd(Double.valueOf(bundle.getString("slngtd")));

                history.setH_f_place(bundle.getString("h_f_place"));
                history.setH_f_lttd(Double.valueOf(bundle.getString("flttd")));
                history.setH_f_lngtd(Double.valueOf(bundle.getString("flngtd")));

                history.setH_made_flag("0");

                history.setH_ep_fare(bundle.getString("h_ep_fare"));
                history.setH_ep_time(bundle.getString("h_ep_time"));
                history.setH_ep_distance(bundle.getString("h_ep_distance"));

                together.setM_id(m_id);
                together.setTo_max(bundle.getInt("to_max"));
                together.setTo_seat(Integer.valueOf(to_seat));

                Log.d("1.", String.valueOf(history.getH_s_lngtd()));
                Log.d("2.", String.valueOf(history.getH_s_lttd()));
                Log.d("3.", String.valueOf(history.getH_f_lngtd()));
                Log.d("4.", String.valueOf(history.getH_f_lttd()));




            } catch (NullPointerException e) {
                Log.d("아무것도 안쓴 상태", e.toString());
            }

        }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(request != null) request.cancel();
    }


    public void seatClickEvent() {


        if(!b.seatOne.isChecked()) {
            //자리 1번을 선택할 경우
            b.seatOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    b.seatOne.setChecked(true);
                    b.seatTwo.setChecked(false);
                    b.seatThree.setChecked(false);
                    to_seat = 1;
                }
            });

        }

        if(!b.seatTwo.isChecked()) {
            //자리 2를 선택할 경우
            b.seatTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    b.seatTwo.setChecked(true);
                    b.seatOne.setChecked(false);
                    b.seatThree.setChecked(false);
                    to_seat = 2;
                }
            });
        }

        if(!b.seatThree.isChecked()) {
            //자리 3을 선택할 경우
            b.seatThree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    b.seatThree.setChecked(true);
                    b.seatTwo.setChecked(false);
                    b.seatOne.setChecked(false);
                    to_seat = 3;
                }
            });
        }
    }
}