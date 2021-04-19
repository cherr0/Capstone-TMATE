package com.tmate.user.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tmate.user.Activity.CallGeneralActivity;
import com.tmate.user.Activity.MatchingActivity;
import com.tmate.user.Activity.MatchingMapActivity;
import com.tmate.user.R;
import com.tmate.user.databinding.FragmentMatchingSeatBinding;


public class MatchingSeatFragment extends Fragment {
    private View view;
    private int togetherAdd = 1;
    private FragmentMatchingSeatBinding b;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentMatchingSeatBinding.inflate(getLayoutInflater());
        view = b.getRoot();

        Bundle bundle = getArguments();  //만들고 있는 방의 좌석 체크인지 이미 만든 방의 좌석체크인지 확인
        if(bundle != null){
            togetherAdd = bundle.getInt("togetherAdd"); //Name 받기.
            System.out.println(togetherAdd); //확인
        }
        //자리 선택 완료 후 버튼클릭이벤트
        b.btnSeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(togetherAdd == 0) {        //매칭방 만들때의 경로(자리 선택 후 매칭리스트로 이동)
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    MatchingFragment mf = new MatchingFragment();
                    transaction.replace(R.id.fm_matching_activity, mf);
                    transaction.commit();
                } else { //이미 만들어진 매칭방에서 들어왔을때의 경로(결제화면으로 이동)
                    Intent intent = new Intent(getContext(),CallGeneralActivity.class);
                    startActivity(intent);
                }
            }
        });
        //자리 1번을 선택할 경우
        b.seatOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.seatOne.setChecked(true);
                b.seatTwo.setChecked(false);
                b.seatThree.setChecked(false);
            }
        });
        //자리 2를 선택할 경우
        b.seatTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.seatTwo.setChecked(true);
                b.seatOne.setChecked(false);
                b.seatThree.setChecked(false);
            }
        });
        //자리 3을 선택할 경우
        b.seatThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.seatThree.setChecked(true);
                b.seatTwo.setChecked(false);
                b.seatOne.setChecked(false);
            }
        });
        return view;
    }

}