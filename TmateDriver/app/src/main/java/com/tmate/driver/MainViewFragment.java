package com.tmate.driver;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class MainViewFragment extends Fragment {
    private View view;
    private Button dv_st;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_view, container, false);

        dv_st = view.findViewById(R.id.dv_st);
        dv_st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WaitingActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }
}