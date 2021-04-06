package com.tmate.driver;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tmate.driver.databinding.FragmentMainViewBinding;

public class MainViewFragment  extends Fragment {
    private FragmentMainViewBinding b;
    private View view;
    TextView tv_car_no ;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentMainViewBinding.inflate(inflater, container, false);
        View view = b.getRoot();

        b.carFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogItems();
            }
        });

        b.dvSt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WaitingActivity.class);
                startActivity(intent);
            }
        });

        tv_car_no = b.tvCarNo;
        return view;
    }
    private void showAlertDialogItems() {
        String [] cards = new String[] {"대구 11가 1111","대구 11가 1111","대구 11가 1111","대구 11가 1111"};
        AlertDialog.Builder ad = new AlertDialog.Builder(getContext());

        ad.setTitle("차량 선택");
        ad.setItems(
                cards, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv_car_no.setText(cards[which]);
                    }
                });
        ad.setNeutralButton("닫기",null).show();
    }
}