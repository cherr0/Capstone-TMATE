package com.tmate.user.Fragment;

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

import com.tmate.user.MatchingFragment;
import com.tmate.user.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CallFragment extends Fragment {
    ArrayList<String> list;
    private View view;
    private LinearLayout Ll_together;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_call, container, false);

        Ll_together = view.findViewById(R.id.Ll_together);
        Ll_together.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                MatchingFragment matchingFragment = new MatchingFragment();
                transaction.replace(R.id.frameLayout, matchingFragment);
                transaction.commit();
            }
        });
        return view;
    }
}