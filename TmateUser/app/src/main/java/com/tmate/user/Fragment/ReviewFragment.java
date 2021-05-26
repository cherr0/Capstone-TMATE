package com.tmate.user.Fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.R;
import com.tmate.user.adapter.ReviewAdapter;
import com.tmate.user.data.ReviewData;
import com.tmate.user.databinding.FragmentReviewBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReviewFragment extends Fragment {
    private FragmentReviewBinding b;
    private ReviewAdapter adapter;
    Button btnDriverReview;
    TextView reviewDriverName;
    ImageView reviewDriverFinish;
    View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentReviewBinding.inflate(getLayoutInflater());
        view = b.getRoot();

        adapter = new ReviewAdapter();

        getData();

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        b.reReTogether.setLayoutManager(manager); // LayoutManager 등록

        b.reReTogether.setAdapter(adapter);

        b.reviewFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //평가완료 후 이동
            }
        });
        b.driverLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                b.driverDislike.setChecked(false); //싫어요 off
                if(isChecked) {
                    b.driverLike.setChecked(true);
                } else if(!isChecked){
                    b.driverLike.setChecked(false);

                }
            }
        });
        b.driverDislike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                b.driverLike.setChecked(false); //좋아요 off
                if(isChecked) {
                    b.driverDislike.setChecked(true);
                } else if (!isChecked){
                    b.driverDislike.setChecked(false);
                }
            }
        });


        return view;
    }
    private void getData() {
        List<String> list_m_name = Arrays.asList("김첨치","김주역");
        List<String> list_to_seat = Arrays.asList("보조석","기사석 뒤");
        List<Integer> list_review_picture = Arrays.asList(R.drawable.poi_dot,R.drawable.tmate);

        for(int i = 0; i<list_m_name.size(); i++) {
            ReviewData data = new ReviewData();
            data.setName(list_m_name.get(i));
            data.setR_to_seat(list_to_seat.get(i));
            data.setReview_picture(list_review_picture.get(i));

            adapter.addItem(data);
            adapter.notifyDataSetChanged();
        }
    }

}