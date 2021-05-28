package com.tmate.user.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.R;
import com.tmate.user.adapter.MatchingMemberAdapter;
import com.tmate.user.adapter.TogetherRequestAdapter;
import com.tmate.user.data.History;
import com.tmate.user.data.MatchingMember;
import com.tmate.user.data.TogetherRequest;
import com.tmate.user.databinding.FragmentMatchingDetailBinding;
import com.tmate.user.net.DataService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MatchingDetailFragment extends Fragment {
    private View view;
    private Button btn_match;
    private FragmentMatchingDetailBinding b;
    private TogetherRequestAdapter requestAdapter;
    private MatchingMemberAdapter memberAdapter;
    private ArrayList<MatchingMember> memberList;
    private ArrayList<TogetherRequest> requestList;
    Call<List<TogetherRequest>> request;

    private Dialog dialog;
    private Bundle bundle;

    private String m_id;
    private String list_m_id;
    private String merchant_uid;


    private int to_seat;

    Bundle toBundle = new Bundle();

    DataService dataService = DataService.getInstance();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b= FragmentMatchingDetailBinding.inflate(getLayoutInflater());
        view = b.getRoot();
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_together_info);

        if (getArguments() != null) {
            bundle = getArguments();

            merchant_uid = bundle.getString("merchant_uid");
            m_id = bundle.getString("m_id");

            Log.d("merchant_uid" , merchant_uid);
        }
        // 동승 멤버 확인 어뎁터
        RecyclerView memberRecyclerView = view.findViewById(R.id.matching_member_rv);

        LinearLayoutManager memberLinearLayoutManager = new LinearLayoutManager(getContext());
        memberRecyclerView.setLayoutManager(memberLinearLayoutManager);

        memberList = new ArrayList<>();

        memberAdapter = new MatchingMemberAdapter();
        memberRecyclerView.setAdapter(memberAdapter);

        // 동승 요청 확인 어뎁터
        RecyclerView requestRecyclerView = view.findViewById(R.id.together_request_rv);

        LinearLayoutManager requestLinearLayoutManager = new LinearLayoutManager(getContext());
        requestRecyclerView.setLayoutManager(requestLinearLayoutManager);

        requestList = new ArrayList<>();

        requestAdapter = new TogetherRequestAdapter();
        requestRecyclerView.setAdapter(requestAdapter);

        getMemberData();
        getRequestData();



        dataService.matchAPI.getMatchingDetail(merchant_uid,m_id).enqueue(new Callback<History>() {
            @Override
            public void onResponse(Call<History> call, Response<History> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        History history = response.body();
                        Log.d("잘받아오나요?", history.toString());

                        b.tvMName.setText(history.getM_name());
                        b.mfMerchantUid.setText(history.getMerchant_uid());
                        b.mfMid.setText(history.getM_id());
                        list_m_id = history.getM_id();
                        b.cgStartPlace.setText(history.getH_s_place());
                        b.mfHSLttd.setText(String.valueOf(history.getH_s_lttd()));
                        b.mfHSLngtd.setText(String.valueOf(history.getH_s_lngtd()));

                        b.cgEndPlace.setText(history.getH_f_place());
                        b.mfHFLttd.setText(String.valueOf(history.getH_f_lttd()));
                        b.mfHFLngtd.setText(String.valueOf(history.getH_f_lngtd()));

                        b.hEpFare.setText(history.getH_ep_fare()+"원");
                        b.hEpDistance.setText(history.getH_ep_distance());
                        b.hEpTime.setText(history.getH_ep_time());

                    }
                }
            }

            @Override
            public void onFailure(Call<History> call, Throwable t) {
                t.printStackTrace();
            }
        });

        if(getPreferenceString("m_id").equals(bundle.getString("m_id")) ) {
            b.clBtnMember.setVisibility(View.VISIBLE);
            b.clBtnTogetherRequest.setVisibility(View.VISIBLE);
            b.btnMatch.setText("삭제하기");

        } else {
            b.clBtnMember.setVisibility(View.INVISIBLE);
            b.clBtnTogetherRequest.setVisibility(View.INVISIBLE);
            b.btnMatch.setText("동승하기");
        }


        // 동승자 정보 보기 -> Bundle로 merchant_uid 넘김
        b.btnMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toBundle.putString("merchant_uid",merchant_uid);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                MatchingMemberFragment matchingMemberFragment = new MatchingMemberFragment();
                matchingMemberFragment.setArguments(toBundle);
                transaction.replace(R.id.fm_matching, matchingMemberFragment);
                transaction.commit();
            }
        });

        // 동승 신청 정보 보기 -> Bundle로 merchant_uid 넘김
        b.btnMemberRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toBundle.putString("merchant_uid",merchant_uid);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                TogetherRequestFragment togetherRequestFragment = new TogetherRequestFragment();
                togetherRequestFragment.setArguments(toBundle);
                transaction.replace(R.id.fm_matching, togetherRequestFragment);
                transaction.commit();
            }
        });

        b.btnMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (b.btnMatch.getText().toString().equals("동승하기")) {
                    // 동승 신청 시 좌석 선택으로 넘어간다.
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("merchant_uid", merchant_uid);
                    bundle1.putString("list_m_id",list_m_id);

                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    MatchingSeatFragment matchingSeatFragment = new MatchingSeatFragment();
                    matchingSeatFragment.setArguments(bundle1);
                    transaction.replace(R.id.fm_matching, matchingSeatFragment);
                    transaction.commit();
                }

                if (b.btnMatch.getText().toString().equals("삭제하기")) {
                    dataService.matchAPI.removeMatchingByMaster(merchant_uid).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.code() == 200) {
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                CallFragment callFragment = new CallFragment();
                                transaction.replace(R.id.fm_matching, callFragment);
                                transaction.commit();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }



            }
        });

        return view;
    }
    public void showDialog(){
        dialog.show();

        TextView exit = dialog.findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    // 데이터 불러오기 함수
    public String getPreferenceString(String key){
        SharedPreferences pref = getActivity().getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    private void getMemberData() {
        List<String> m_name = Arrays.asList(
                "김진수",
                "장원준",
                "강병현"
        );
        List<String> m_birth = Arrays.asList(
                "30대",
                "20대",
                "20대"
        );
        List<String> m_t_use = Arrays.asList(
                "14",
                "32",
                "27"
        );

        for (int i = 0; i < m_name.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            MatchingMember MatchingMember = new MatchingMember();
            MatchingMember.setM_name(m_name.get(i));
            MatchingMember.setM_birth(m_birth.get(i));
            MatchingMember.setM_t_use(m_t_use.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            memberAdapter.addItem(MatchingMember);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        memberAdapter.notifyDataSetChanged();
    }

    private void getRequestData() {

        request = DataService.getInstance().matchAPI.getTogetherRequest(merchant_uid);

        request.enqueue(new Callback<List<TogetherRequest>>() {
            @Override
            public void onResponse(Call<List<TogetherRequest>> call, Response<List<TogetherRequest>> response) {
                if (response.code() == 200) {
                    List<TogetherRequest> list = response.body();
                    Log.i("넘어오는 리스트", list.toString());
                    for (int i = 0; i < list.size(); i++) {

                        TogetherRequest togetherRequest = new TogetherRequest();
                        togetherRequest.setMerchant_uid(list.get(i).getMerchant_uid());
                        togetherRequest.setM_n_use(list.get(i).getM_n_use());
                        togetherRequest.setM_t_use(list.get(i).getM_t_use());
                        togetherRequest.setId(list.get(i).getId());
                        togetherRequest.setM_name(list.get(i).getM_name());
                        togetherRequest.setM_birth(list.get(i).getM_birth());

                        requestAdapter.addItem(togetherRequest);
                    }

                    requestAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<TogetherRequest>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


}