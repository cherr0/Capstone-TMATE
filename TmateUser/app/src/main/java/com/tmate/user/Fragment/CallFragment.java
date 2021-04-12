package com.tmate.user.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tmate.user.Activity.CallGeneralActivity;
import com.tmate.user.Activity.MatchingMapActivity;
import com.tmate.user.R;

import java.util.ArrayList;


public class CallFragment extends Fragment {
    ArrayList<String> list;
    private View view;
    private LinearLayout Ll_together;
    private LinearLayout Ll_solo;
    private int together;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_call, container, false);

        Ll_together = view.findViewById(R.id.Ll_together);
        Ll_together.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                together =0;
                Intent intent = new Intent(getContext(), MatchingMapActivity.class);
                intent.putExtra("together", together);
                startActivity(intent);
            }
        });

        Ll_solo = view.findViewById(R.id.Ll_solo);
        Ll_solo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                together =1;
                Intent intent = new Intent(getContext(), MatchingMapActivity.class);
                intent.putExtra("together", together);
                startActivity(intent);

            }
        });
        return view;
    }
}