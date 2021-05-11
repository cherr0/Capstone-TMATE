package com.tmate.user.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tmate.user.R;
import com.tmate.user.data.History;
import com.tmate.user.net.DataService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallGeneralActivity extends AppCompatActivity {

    Button btn_fare;
    private ImageView btn_back_general;
    Button btn_fare1;

    // CallGeneralActivity 위젯들
    private TextView cg_m_name;

    // 출발지
    private TextView cg_start_place;
    private TextView cg_h_s_lttd;
    private TextView cg_h_s_lngtd;

    // 도착지
    private TextView cg_end_place;
    private TextView cg_h_f_lttd;
    private TextView cg_h_f_lngtd;

    // 예상 요금
    private TextView fare2;


    // 일반 호출 설정시 넘어온다.
    Intent intent;
    
    // 회원코드, 이름 넘겨주기 위한 변수
    private String m_id;
    private String m_name;
    
    // SharedPrefernce
    private SharedPreferences pref;

    // 레트로핏 -> 리퀘스트
    Call<String> request;
    History history;

    // 다음 화면으로 넘어갈 때 필요한것
    String h_s_place;
    String h_f_place;
    String slttd;
    String slngtd;
    String flttd;
    String flngtd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_general);


//        intent.putExtra("h_ep_time",time); // 예상 시간
//        intent.putExtra("h_ep_distance",km); // 예상 거리



        pref = getSharedPreferences("loginUser", MODE_PRIVATE);
        m_id = pref.getString("m_id", "");
        m_name = pref.getString("m_name", "");

        // 위젯 초기화
        initWidget();

        if(getIntent() != null){

            intent = getIntent();
            Log.d("CallGeneralActivity", "인텐트 넘어오는 값 : " + intent);

            history = new History();

            // 값 설정 해준다.
            Log.d("CallGeneralActivity", "이름 : " + getSharedPreferences("loginUser", MODE_PRIVATE).getString("m_name", ""));
            cg_m_name.setText(m_name);

            h_s_place = intent.getStringExtra("h_s_place");
            h_f_place = intent.getStringExtra("h_f_place");
            slttd = intent.getStringExtra("slttd");
            slngtd = intent.getStringExtra("slngtd");
            flttd = intent.getStringExtra("flttd");
            flngtd = intent.getStringExtra("flngtd");

            Log.d("CallGeneralActivity", slttd + ":" + slngtd + ":" + flngtd + ":" + flttd);

            cg_start_place.setText(h_s_place);
            cg_h_s_lttd.setText(slttd);
            cg_h_s_lngtd.setText(slngtd);
            cg_end_place.setText(h_f_place);
            cg_h_f_lttd.setText(flttd);
            cg_h_f_lngtd.setText(flngtd);
            fare2.setText(intent.getStringExtra("h_ep_fare"));

            history.setM_id(m_id);
            history.setH_s_place(h_s_place);
            history.setH_s_lttd(Double.parseDouble(slttd));
            history.setH_s_lngtd(Double.parseDouble(slngtd));
            history.setH_f_place(h_f_place);
            history.setH_f_lttd(Double.parseDouble(flttd));
            history.setH_f_lngtd(Double.parseDouble(flngtd));
            history.setH_made_flag("0");
            history.setH_ep_fare(intent.getStringExtra("h_ep_fare"));
            history.setH_ep_distance(intent.getStringExtra("h_ep_distance"));
            history.setH_ep_time(intent.getStringExtra("h_ep_time"));



        }



        btn_fare = findViewById(R.id.btn_fare);
        btn_fare1 = findViewById(R.id.btn_fare2);

        // 자동 결제
        btn_fare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(CallGeneralActivity.this, PaymentSuccessActivity.class);
                /*
                * 카드 정보 같이 넘겨주어야 한다.
                * */

                startActivity(intent3);
            }
        });

        // 만나서 결제
        btn_fare1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent2 = new Intent(CallGeneralActivity.this, CallWaitingActivity.class);

                Intent intent2 = new Intent(CallGeneralActivity.this, CallWaitingActivity.class);

                intent2.putExtra("h_s_place",h_s_place);
                intent2.putExtra("h_f_place", h_f_place);
                intent2.putExtra("h_s_lttd", slttd);
                intent2.putExtra("h_s_lngtd", slngtd);
                intent2.putExtra("h_f_lttd", flttd);
                intent2.putExtra("h_f_lngtd", flngtd);
                intent2.putExtra("m_name", m_name);
                intent2.putExtra("m_id",m_id);
                
                /*
                * 삽입 실행한 다음 넘긴다. -> 레트로핏 이용
                * */


                Intent finalIntent = intent2;

                request = DataService.getInstance().matchAPI.registerNormalMatching(history);
                request.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.code() == 200) {
                            String merchant_uid = response.body();
                            finalIntent.putExtra("merchant_uid", merchant_uid);

                            Toast.makeText(CallGeneralActivity.this, "호출을 시작합니다.", Toast.LENGTH_LONG).show();

                            if (merchant_uid != null && merchant_uid.length() != 0) {
                                Log.d("값 찍히는지 보자 :  이용코드 : ", merchant_uid);
                                startActivity(finalIntent);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        t.printStackTrace();
                    }
                });


            }
        });

        btn_back_general = findViewById(R.id.btn_back_general);
        btn_back_general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initWidget(){

        cg_m_name = findViewById(R.id.cg_m_name);
        cg_start_place = findViewById(R.id.cg_start_place);
        cg_h_s_lttd = findViewById(R.id.cg_h_s_lttd);
        cg_h_s_lngtd = findViewById(R.id.cg_h_s_lngtd);
        cg_end_place = findViewById(R.id.cg_end_place);
        cg_h_f_lttd = findViewById(R.id.cg_h_f_lttd);
        cg_h_f_lngtd = findViewById(R.id.cg_h_f_lngtd);
        fare2 = findViewById(R.id.fare2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        request.cancel();
    }
}