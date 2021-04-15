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
import com.tmate.user.Activity.MatchingMapActivity;
import com.tmate.user.R;


public class MatchingSeatFragment extends Fragment {
    private View view;
    Button btnSeat;
    private int togetherAdd = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_matching_seat, container, false);

        Bundle bundle = getArguments();  //만들고 있는 방의 좌석 체크인지 이미 만든 방의 좌석체크인지 확인
        if(bundle != null){
            togetherAdd = bundle.getInt("togetherAdd"); //Name 받기.
            System.out.println(togetherAdd); //확인
        }

        btnSeat = view.findViewById(R.id.btn_seat);
        btnSeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(togetherAdd == 0) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    MatchingFragment mf = new MatchingFragment();
                    transaction.replace(R.id.fm_matching_activity, mf);
                    transaction.commit();
                }
                else {
                    Intent intent = new Intent(getContext(),CallGeneralActivity.class);
                    startActivity(intent);
                }
            }
        });
        return view;
    }
}