package com.tmate.user.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.CallGeneralActivity;
import com.tmate.user.MainViewActivity;
import com.tmate.user.MapMatchingActivity;
import com.tmate.user.MatchingFragment;
import com.tmate.user.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CallFragment extends Fragment {
    ArrayList<String> list;
    private View view;
    private LinearLayout Ll_together;
    private LinearLayout Ll_solo;
    int togehter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_call, container, false);

        Ll_together = view.findViewById(R.id.Ll_together);
        Ll_together.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togehter = 0;
                Intent intent = new Intent(getContext(), MapMatchingActivity.class);
                intent.putExtra("together",togehter);
                startActivity(intent);
            }
        });

        Ll_solo = view.findViewById(R.id.Ll_solo);
        Ll_solo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togehter = 1;
                Intent intent = new Intent(getContext(), MapMatchingActivity.class);
                intent.putExtra("together",togehter);
                startActivity(intent);
            }
        });
        return view;
    }
}