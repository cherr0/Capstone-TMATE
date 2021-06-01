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
import com.tmate.user.adapter.MatchingDetailAdapter;
import com.tmate.user.adapter.MatchingMemberAdapter;
import com.tmate.user.adapter.TogetherRequestAdapter;
import com.tmate.user.data.History;
import com.tmate.user.data.MatchingDetailData;
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
    private Dialog dialog;
    private Bundle bundle;
    private String m_id;
    private String list_m_id;
    private String merchant_uid;
    private String id_num;
    private MatchingDetailAdapter adapter;
    private MatchingDetailData matchingDetailData;
    private ArrayList<MatchingDetailData> arrayList;


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

        RecyclerView recyclerView = view.findViewById(R.id.matching_recycle);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();
        adapter = new MatchingDetailAdapter();
        recyclerView.setAdapter(adapter);

        getData();

        if (getArguments() != null) {
            bundle = getArguments();

            merchant_uid = bundle.getString("merchant_uid");
            m_id = bundle.getString("m_id");

            Log.d("merchant_uid" , merchant_uid);
        }

//        dataService.matchAPI.getMatchingDetail(merchant_uid,m_id).enqueue(new Callback<History>() {
//            @Override
//            public void onResponse(Call<History> call, Response<History> response) {
//                if (response.isSuccessful()) {
//                    if (response.code() == 200) {
//                        History history = response.body();
//                        Log.d("잘받아오나요?", history.toString());
//
//                        b.tvMName.setText(history.getM_name());
//                        b.mfMerchantUid.setText(history.getMerchant_uid());
//                        b.mfMid.setText(history.getM_id());
//                        list_m_id = history.getM_id();
//                        b.cgStartPlace.setText(history.getH_s_place());
//                        b.mfHSLttd.setText(String.valueOf(history.getH_s_lttd()));
//                        b.mfHSLngtd.setText(String.valueOf(history.getH_s_lngtd()));
//
//                        b.cgEndPlace.setText(history.getH_f_place());
//                        b.mfHFLttd.setText(String.valueOf(history.getH_f_lttd()));
//                        b.mfHFLngtd.setText(String.valueOf(history.getH_f_lngtd()));
//
//                        b.hEpFare.setText(history.getH_ep_fare()+"원");
//                        b.hEpDistance.setText(history.getH_ep_distance());
//                        b.hEpTime.setText(history.getH_ep_time());
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<History> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });
//
//        if(getPreferenceString("m_id").equals(bundle.getString("m_id")) ) {
////            b.clBtnMember.setVisibility(View.VISIBLE);
////            b.clBtnTogetherRequest.setVisibility(View.VISIBLE);
//            b.btnMatch.setText("삭제하기");
//
//        } else {
////            b.clBtnMember.setVisibility(View.INVISIBLE);
////            b.clBtnTogetherRequest.setVisibility(View.INVISIBLE);
//            b.btnMatch.setText("동승하기");
//        }
//
//
////         동승자 정보 보기 -> Bundle로 merchant_uid 넘김
//        b.btnMember.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toBundle.putString("merchant_uid",merchant_uid);
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                MatchingMemberFragment matchingMemberFragment = new MatchingMemberFragment();
//                matchingMemberFragment.setArguments(toBundle);
//                transaction.replace(R.id.fm_matching, matchingMemberFragment);
//                transaction.commit();
//            }
//        });
//
////         동승 신청 정보 보기 -> Bundle로 merchant_uid 넘김
//        b.btnMemberRequest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toBundle.putString("merchant_uid",merchant_uid);
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                TogetherRequestFragment togetherRequestFragment = new TogetherRequestFragment();
//                togetherRequestFragment.setArguments(toBundle);
//                transaction.replace(R.id.fm_matching, togetherRequestFragment);
//                transaction.commit();
//            }
//        });
//
//        b.btnMatch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (b.btnMatch.getText().toString().equals("동승하기")) {
//                    // 동승 신청 시 좌석 선택으로 넘어간다.
//                    Bundle bundle1 = new Bundle();
//                    bundle1.putString("merchant_uid", merchant_uid);
//                    bundle1.putString("list_m_id",list_m_id);
//
//                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                    MatchingSeatFragment matchingSeatFragment = new MatchingSeatFragment();
//                    matchingSeatFragment.setArguments(bundle1);
//                    transaction.replace(R.id.fm_matching, matchingSeatFragment);
//                    transaction.commit();
//                }
//
//                if (b.btnMatch.getText().toString().equals("삭제하기")) {
//                    dataService.matchAPI.removeMatchingByMaster(merchant_uid).enqueue(new Callback<Boolean>() {
//                        @Override
//                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
//                            if (response.code() == 200) {
//                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                                CallFragment callFragment = new CallFragment();
//                                transaction.replace(R.id.fm_matching, callFragment);
//                                transaction.commit();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<Boolean> call, Throwable t) {
//                            t.printStackTrace();
//                        }
//                    });
//                }
//            }
//        });
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

     public void getData() {
        List<String> id_num = Arrays.asList(
                "1",
                "2"
        );
        List<String> situation = Arrays.asList(
                "동승자 정보",
                "동승요청 현황"
        );

        for (int i = 0; i < id_num.size(); i++) {
            matchingDetailData = new MatchingDetailData();
            matchingDetailData.setId_num(id_num.get(i));
            matchingDetailData.setSituation(situation.get(i));

            adapter.addItem(matchingDetailData);
        }

        adapter.notifyDataSetChanged();
    }
}