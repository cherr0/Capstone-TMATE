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
    String dp_id;
    DrivingModel mViewModel;


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

        RecyclerView recyclerView = view.findViewById(R.id.matching_recycle);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        mViewModel =  new ViewModelProvider(requireActivity()).get(DrivingModel.class);

        arrayList = new ArrayList<>();
        adapter = new MatchingDetailAdapter(mViewModel);
        recyclerView.setAdapter(adapter);


            widgetInfoInitialize();




        // 여기서 버튼 이벤트 추가 : 호출하기 (결제화면으로 넘어가기), 동승하기(의자화면으로 넘어가기)
        NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        b.btnMatch.setOnClickListener(v -> {
            if (b.btnMatch.getText().toString().equals("호출하기")) {
                // 호출하기로 넘어가쥬
                controller.navigate(R.id.action_matchingDetailFragment_to_paymentInformationFragment);
            }else{
                // 넘어가쥬
                controller.navigate(R.id.action_global_matchingSeatFragment);
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

    // 이용 상세정보
    public void widgetInfoInitialize() {

        dp_id = mViewModel.dispatch.getDp_id();

        getMasterDispatchDetailInfo = DataService.getInstance().matchAPI.readCurrentDispatch(dp_id);
        getMasterDispatchDetailInfo.enqueue(new Callback<Dispatch>() {
            @Override
            public void onResponse(Call<Dispatch> call, Response<Dispatch> response) {
                if (response.code() == 200) {
                    Dispatch dispatch = response.body();
                    String user = dispatch.getM_id();
                    Log.d("MatchingDetail : ", m_id + ":" + user);
                    b.fmdCurPeople.setText(String.valueOf(dispatch.getCur_people()));
                    b.fmdMaxPeople.setText(dispatch.getDp_id().substring(18));
                    b.mfDpId.setText(dispatch.getDp_id());
                    b.cgStartPlace.setText(dispatch.getStart_place());
                    b.cgEndPlace.setText(dispatch.getFinish_place());
                    b.hEpFare.setText(mViewModel.dispatch.getAll_fare()+"");
                    b.hEpDistance.setText((float)(mViewModel.dispatch.getEp_distance())/1000+"");
                    b.tvMName.setText(dispatch.getM_name());
                    b.mBirth.setText(String.valueOf(dispatch.getM_birth()));

                    if (user.equals(m_id)) {
                        b.btnMatch.setText("호출하기");
                        getData();
                    }else{
                        b.btnMatch.setText("동승하기");
                    }
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


}