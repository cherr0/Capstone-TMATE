package com.tmate.user.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

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
import com.tmate.user.data.Attend;
import com.tmate.user.data.Dispatch;
import com.tmate.user.data.History;
import com.tmate.user.data.Together;
import com.tmate.user.databinding.FragmentMatchingSeatBinding;
import com.tmate.user.net.DataService;
import com.tmate.user.ui.driving.DrivingModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MatchingSeatFragment extends Fragment {
    private View view;
    private int togetherAdd = 0;
    private FragmentMatchingSeatBinding b;

    private DrivingModel mViewModel;

    Call<Boolean> request;
    Call<List<Together>> request2;
    Call<Boolean> request3;

    Bundle bundle;
//    History history;
//    Together together;

    // 동승 참가 하는 것
    Dispatch dispatch;
    Attend attend;

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

        mViewModel = new ViewModelProvider(requireActivity()).get(DrivingModel.class);

        // 새로 추가하기 - 리스트
        if(mViewModel.dispatch.getDp_id() == null) {
            Log.d("만약 널이라면","여기가 찍히겠");
            bundle = getArguments();



            //자리 선택 완료 후 버튼클릭이벤트 --> Dispatch 하고 Attend들어가는거
            b.btnSeat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //매칭방 만들때의 경로(자리 선택 후 매칭리스트로 이동)
                        Log.d("찍히긴 찍히냐", "반응은 하냐? 좀 해라 궁금하다 왜 안되냐");
                        initializeObjects(bundle);

                        Map<String, Object> hashmap = new HashMap<>();

                        hashmap.put("dispatch", dispatch);
                        hashmap.put("attend", attend);



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

                    
                }
            });

            seatClickEvent();
        }
        // 기존의 방에서 좌석 선택
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

                dispatch = new Dispatch();
                attend = new Attend();
                
                
                String m_id = getActivity().getSharedPreferences("loginUser", Context.MODE_PRIVATE).getString("m_id", "");


                dispatch.setM_id(m_id);

                dispatch.setStart_place(mViewModel.dispatch.getStart_place());

                dispatch.setStart_lat(mViewModel.dispatch.getStart_lat());
                dispatch.setStart_lng(mViewModel.dispatch.getStart_lng());


                dispatch.setFinish_place(mViewModel.dispatch.getFinish_place());

                dispatch.setFinish_lat(mViewModel.dispatch.getFinish_lat());
                dispatch.setFinish_lng(mViewModel.dispatch.getFinish_lng());


                dispatch.setAll_fare(mViewModel.dispatch.getAll_fare());
                dispatch.setEp_time(mViewModel.dispatch.getEp_time());
                dispatch.setEp_distance(mViewModel.dispatch.getEp_distance());



                attend.setM_id(m_id);
                attend.setAt_status("1");
                attend.setSeat(to_seat);
                attend.setAmount(mViewModel.dispatch.getAll_fare());





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