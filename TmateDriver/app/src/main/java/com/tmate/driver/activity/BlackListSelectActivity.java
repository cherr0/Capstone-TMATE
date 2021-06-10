package com.tmate.driver.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.tmate.driver.R;

import net.colindodd.toggleimagebutton.ToggleImageButton;

import java.util.ArrayList;
import java.util.List;

public class BlackListSelectActivity extends AppCompatActivity {
    private Button btn_seat;
    private ToggleImageButton seat_one, seat_two, seat_three;
    TextView seat_one_m_name, seat_two_m_name, seat_three_m_name;
    private int blackList = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list_select);


        Intent intent = getIntent();
        int blackList = intent.getExtras().getInt("blackList");
        Log.d("blackList","뭐넘어오냐"+ blackList);



        final List<String> list = new ArrayList<String>();

        seat_one = findViewById(R.id.seat_one);
        seat_one_m_name = findViewById(R.id.seat_one_m_name);
        seat_two = findViewById(R.id.seat_two);
        seat_two_m_name = findViewById(R.id.seat_two_m_name);
        seat_three = findViewById(R.id.seat_three);
        seat_three_m_name = findViewById(R.id.seat_three_m_name);

        // 유저 이름 젤 앞으로 빼오기
        seat_one_m_name.bringToFront();
        seat_two_m_name.bringToFront();
        seat_three_m_name.bringToFront();

        seat_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seat_one_m_name.setVisibility(View.VISIBLE);
                seat_one.setChecked(true);
                seat_two.setChecked(false);
                seat_three.setChecked(false);
            }
        });
        seat_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seat_two_m_name.setVisibility(View.VISIBLE);
                seat_one.setChecked(false);
                seat_two.setChecked(true);
                seat_three.setChecked(false);
            }
        });
        seat_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seat_three_m_name.setVisibility(View.VISIBLE);
                seat_one.setChecked(false);
                seat_two.setChecked(false);
                seat_three.setChecked(true);
            }
        });

        btn_seat = findViewById(R.id.btn_seat);
        btn_seat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = new String[]{"너무 시끄러워요", "시간을 안지켜요", "술을 마신거 같아요", "목적지변경을 강요해요", "불친절해요."};
                AlertDialog.Builder dialog = new AlertDialog.Builder(BlackListSelectActivity.this);
                dialog.setTitle("항목을 선택해주세요.")
                        .setMultiChoiceItems(
                                items
                                , new boolean[]{false, false, false, false, false}
                                , new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                        if (isChecked) {
                                            Toast.makeText(BlackListSelectActivity.this
                                                    , items[which]
                                                    , Toast.LENGTH_SHORT).show();
                                            list.add(items[which]);
                                        } else {
                                            list.remove(items[which]);
                                        }
                                    }
                                }
                        )
                        .setPositiveButton("보내기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (blackList == 0) {
                                    finish();
                                } else {
                                    Intent intent1 = new Intent(BlackListSelectActivity.this, WaitingActivity.class);
                                    startActivity(intent1);
                                    finish();
                                }
                            }
                        });
                dialog.create();
                dialog.show();
            }
        });
    }
}