package com.tmate.user.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.R;
import com.tmate.user.adapter.MatchingDetailAdapter;
import com.tmate.user.adapter.MatchingMemberAdapter;
import com.tmate.user.adapter.TogetherRequestAdapter;
import com.tmate.user.data.Dispatch;
import com.tmate.user.data.Dv_option;
import com.tmate.user.data.History;
import com.tmate.user.data.MatchingDetailData;
import com.tmate.user.data.MatchingMember;
import com.tmate.user.data.TogetherRequest;
import com.tmate.user.databinding.FragmentMatchingDetailBinding;
import com.tmate.user.net.DataService;
import com.tmate.user.ui.driving.DrivingModel;

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
    
    private String m_id;
    SharedPreferences pref; 
    
    private MatchingDetailAdapter adapter;
    private MatchingDetailData matchingDetailData;
    private ArrayList<MatchingDetailData> arrayList;

    

    Call<Dispatch> getMasterDispatchDetailInfo;
    Call<Dispatch> getCheckingDispatchStatus;
    Call<Boolean> modifyDispatchInfo;
    Call<Boolean> removeMatchRequest;

    String dp_id;
    DrivingModel mViewModel;
    String user;

    // 쓰레드 관련
    Boolean isRunning;
    Handler handler;
    Checking checking;

    // 입장하기 위함 성별 체크
    String gender;

    // 동승 옵션 가져오기
    Call<Dv_option> getDv_optionRequest;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b= FragmentMatchingDetailBinding.inflate(getLayoutInflater());
        view = b.getRoot();
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_together_info);

        pref = getActivity().getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        m_id = pref.getString("m_id", "");
        b.tvMName.setText(getPreferenceString("m_name"));
        b.mBirth.setText(getPreferenceString("age"));

        RecyclerView recyclerView = view.findViewById(R.id.matching_recycle);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        mViewModel =  new ViewModelProvider(requireActivity()).get(DrivingModel.class);

        // 매칭상세정보 데이터 연동
        widgetInfoInitialize();

        arrayList = new ArrayList<>();
        adapter = new MatchingDetailAdapter(mViewModel);
        recyclerView.setAdapter(adapter);



        // 여기서 버튼 이벤트 추가 : 호출하기 (결제화면으로 넘어가기), 동승하기(의자화면으로 넘어가기)
        NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        b.btnMatch.setOnClickListener(v -> {
            if (b.btnMatch.getText().toString().equals("호출하기")) {

                Dispatch dispatch = new Dispatch();
                dispatch.setDp_id(dp_id);
                dispatch.setDp_status("1");

                modifyDispatchInfo = DataService.getInstance().matchAPI.modifyTogetherStatus(dispatch);
                modifyDispatchInfo.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.code() == 200) {
                            // 결제정보로 넘어가쥬
                            controller.navigate(R.id.action_matchingDetailFragment_to_paymentInformationFragment);
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }else if (b.btnMatch.getText().toString().equals("동승하기")){
                // 넘어가쥬
                if (gender.equals(getPreferenceString("gender")))
                    controller.navigate(R.id.action_global_matchingSeatFragment);
                else
                    Toast.makeText(getContext(), "동성만 이용할 수 있습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
              // 삭제하기 로직 짜야됨
                removeMatchRequest = DataService.getInstance().matchAPI.removeTogetherMatch(dp_id);
                removeMatchRequest.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.code() == 200) {
                            Toast.makeText(getContext(), "매칭이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });

        return view;
    }


    public class Checking implements Runnable {
        @Override
        public void run() {
            getCheckingDispatchStatus = DataService.getInstance().matchAPI.getCurrentDispatchBeforesuccess(dp_id);
            getCheckingDispatchStatus.enqueue(new Callback<Dispatch>() {
                @Override
                public void onResponse(Call<Dispatch> call, Response<Dispatch> response) {
                    if (response.code() == 200) {
                        Dispatch dispatch = response.body();
                        String dp_status = dispatch.getDp_status();
                        Log.d("동승자입장의DISPATCH", dispatch.toString());
                        if (dp_status.equals("1")) {
                            isRunning = false;
                            NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                            controller.navigate(R.id.action_matchingDetailFragment_to_paymentInformationFragment);
                        }else {
                            isRunning = true;
                        }
                    }
                }

                @Override
                public void onFailure(Call<Dispatch> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }


    // 데이터 불러오기 함수
    public String getPreferenceString(String key){
        SharedPreferences pref = getActivity().getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    // 이용 상세정보
    public void widgetInfoInitialize() {

        dp_id = mViewModel.dispatch.getDp_id();

        getMasterDispatchDetailInfo = DataService.getInstance().matchAPI.getCurrentDispatchBeforesuccess(dp_id);
        getMasterDispatchDetailInfo.enqueue(new Callback<Dispatch>() {
            @Override
            public void onResponse(Call<Dispatch> call, Response<Dispatch> response) {
                if (response.code() == 200) {
                    Dispatch dispatch = response.body();
                    Log.d("MatchingDetail : ", dispatch.toString());
                    user = dispatch.getM_id();
                    switch (user.substring(1, 2)) {
                        case "1":
                        case "3":
                            gender = "남";
                            break;
                        case "2":
                        case "4":
                            gender = "여";
                            break;
                    }
                    Log.d("MatchingDetail user ", user);
                    b.fmdCurPeople.setText(String.valueOf(dispatch.getCur_people()));
                    b.fmdMaxPeople.setText(dispatch.getDp_id().substring(18));
                    b.mfDpId.setText(dispatch.getDp_id());
                    b.cgStartPlace.setText(dispatch.getStart_place());
                    b.cgEndPlace.setText(dispatch.getFinish_place());
                    b.hEpFare.setText(String.valueOf(dispatch.getAll_fare()));
                    b.hEpDistance.setText(String.valueOf((float)(dispatch.getEp_distance())/1000));



                    if (user.equals(m_id)) {
                        if(b.fmdCurPeople.getText().equals(b.fmdMaxPeople.getText()))
                            b.btnMatch.setText("호출하기");
                        else
                            b.btnMatch.setText("삭제하기");
                        
                        getData();
                        
                    }else{
                        b.btnMatch.setText("동승하기");
                        handler = new Handler();
                        checking = new Checking();
                        isRunning = true;

                        Thread thread = new Thread(() -> {
                            try {
                                while (isRunning) {
                                    handler.post(checking);
                                    Thread.sleep(2000);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

                        isRunning = true;
                        thread.start();
                    }

                    getDv_optionRequest = DataService.getInstance().memberAPI.getDvOptionByM_id(user);
                    getDv_optionRequest.enqueue(new Callback<Dv_option>() {
                        @Override
                        public void onResponse(Call<Dv_option> call, Response<Dv_option> response) {
                            if (response.code() == 200) {
                                Dv_option body = response.body();

                                // 반려동물 유무
                                switch (body.getDo_animal()) {
                                    case "0":
                                        b.textView2.setText("반려동물 상관없어요");
                                        break;
                                    case "1":
                                        b.textView2.setText("반려동물 동반 싫어요");
                                        break;
                                }

                                // 짐 유무
                                switch (body.getDo_load()) {
                                    case "0":
                                        b.textView3.setText("짐 상관 없어요");
                                        break;
                                    case "1":
                                        b.textView3.setText("짐 많은것 부담스러워요");
                                        break;
                                }

                                // 유아동반 유무
                                switch (body.getDo_child()) {
                                    case "0":
                                        b.textView4.setText("유아동반 상관없어요");
                                        break;
                                    case "1":
                                        b.textView4.setText("유아동반 싫어요");
                                        break;
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<Dv_option> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Dispatch> call, Throwable t) {
                t.printStackTrace();
            }
        });
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

    @Override
    public void onStart() {
        super.onStart();
        isRunning = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        isRunning = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isRunning = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        isRunning = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        if(getCheckingDispatchStatus != null) getCheckingDispatchStatus.cancel();
        if(getMasterDispatchDetailInfo != null) getMasterDispatchDetailInfo.cancel();
    }


}