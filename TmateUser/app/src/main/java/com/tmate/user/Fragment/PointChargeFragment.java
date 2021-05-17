package com.tmate.user.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.R;
import com.tmate.user.adapter.PointChargeAdapter;
import com.tmate.user.adapter.PointChargeDecoration;
import com.tmate.user.adapter.historyAdapter;

import java.util.ArrayList;

public class PointChargeFragment extends Fragment {

    private RecyclerView recyclerView;
    private PointChargeAdapter adapter;
    private ImageView backButton;
    View v;

    public static TextView chargePoint;
    public static TextView allPoint;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_point_charge, container, false);
        init();
        chargePoint = (TextView) v.findViewById(R.id.charge_point);
        allPoint = (TextView) v.findViewById(R.id.all_point);

        backButton = (ImageView) v.findViewById(R.id.btn_back_call);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                CallFragment cf = new CallFragment();
                transaction.replace(R.id.frameLayout, cf).commit();
            }
        });

        return v;

    }

    private void init() {

        recyclerView = v.findViewById(R.id.re_point_charge);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<String> itemList = new ArrayList<>();
        itemList.add("1000");
        itemList.add("3000");
        itemList.add("5000 ");
        itemList.add("10000");
        itemList.add("100000");




        adapter = new PointChargeAdapter(getContext(),itemList);
        recyclerView.setAdapter(adapter);


        PointChargeDecoration decoration = new PointChargeDecoration();
        recyclerView.addItemDecoration(decoration);
    }

}
