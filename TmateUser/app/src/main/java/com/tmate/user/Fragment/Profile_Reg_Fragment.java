package com.tmate.user.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.tmate.user.MainViewActivity;
import com.tmate.user.R;


public class Profile_Reg_Fragment extends Fragment {

    private Button btn_submit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.fragment_profile__reg_, container, false);

        btn_submit = rootview.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainViewActivity.class);
                startActivity(intent);
            }
        });
        return rootview;
    }

}
