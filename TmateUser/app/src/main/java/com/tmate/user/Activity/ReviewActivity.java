package com.tmate.user.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.Fragment.historyFragment;
import com.tmate.user.R;
import com.tmate.user.adapter.ReviewAdapter;
import com.tmate.user.data.ReviewData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {
    private ReviewAdapter adapter;
    Button reviewFinish;
    Button btnDriverReview;
    Dialog dialog;
    TextView reviewDriverName;
    ImageView reviewDriverFinish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        // 리사이클러뷰에 LinearLayoutManager 객체 지정.

        RecyclerView recyclerView = findViewById(R.id.re_re_together);
        LinearLayoutManager manager = new LinearLayoutManager(getApplication());
        recyclerView.setLayoutManager(manager); // LayoutManager 등록
        adapter = new ReviewAdapter();
        recyclerView.setAdapter(adapter);

        getData();

        reviewFinish = findViewById(R.id.review_finish);
        reviewFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        reviewDriverFinish = findViewById(R.id.review_driver_finish);
        reviewDriverName = findViewById(R.id.review_driver_name);
        btnDriverReview = findViewById(R.id.btn_driver_review);
        btnDriverReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(ReviewActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_review);
                ImageView good = dialog.findViewById(R.id.r_good);
                dialog.show();

                AlertDialog.Builder builder = new AlertDialog.Builder(ReviewActivity.this);
                final ArrayList<String> selectedItems= new ArrayList<String>();
                TextView titleMName = dialog.findViewById(R.id.title_m_name);
                titleMName.setText(reviewDriverName.getText());
                TextView contentMName = dialog.findViewById(R.id.content_m_name);
                contentMName.setText(reviewDriverName.getText());
                good.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        final String[] result = getResources().getStringArray(R.array.driver_good_review);
                        builder.setTitle("좋아요 한 사유를 선택해주세요");
                        builder.setMultiChoiceItems(R.array.driver_good_review, null, new DialogInterface.OnMultiChoiceClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int pos, boolean isChecked)
                            {
                                if(isChecked == true) // Checked 상태일 때 추가
                                {
                                    try {
                                        selectedItems.add(result[pos]);
                                    } catch (IndexOutOfBoundsException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else
                                {
                                    try {
                                        selectedItems.remove(result[pos]);
                                    } catch (IndexOutOfBoundsException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                        builder.setPositiveButton("완료", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(selectedItems != null && !selectedItems.isEmpty()) {
                                    reviewDriverFinish.setImageResource(R.drawable.review_checked);
                                }
                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });
                ImageView bad = dialog.findViewById(R.id.r_bad);
                bad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        final String[] result = getResources().getStringArray(R.array.member_bad_review);
                        builder.setTitle("싫어요 한 사유를 선택해주세요");
                        builder.setMultiChoiceItems(R.array.member_bad_review, null, new DialogInterface.OnMultiChoiceClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int pos, boolean isChecked)
                            {
                                if(isChecked == true) // Checked 상태일 때 추가
                                {
                                    try {
                                        selectedItems.add(result[pos]);
                                    } catch (IndexOutOfBoundsException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else
                                {
                                    try {
                                        selectedItems.remove(result[pos]);
                                    } catch (IndexOutOfBoundsException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                        builder.setPositiveButton("완료", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(selectedItems != null && !selectedItems.isEmpty()) {
                                    reviewDriverFinish.setImageResource(R.drawable.review_checked);
                                }
                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });

            }
        });
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