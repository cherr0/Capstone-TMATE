package com.tmate.driver.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.mig35.carousellayoutmanager.CarouselLayoutManager;
import com.mig35.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.mig35.carousellayoutmanager.CenterScrollListener;
import com.skt.Tmap.TMapView;
import com.tmate.driver.R;
import com.tmate.driver.adapter.WaitingAdapter;
import com.tmate.driver.data.Waiting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WaitingActivity extends AppCompatActivity {
    Button btn_drive_stop;
    private Dialog dialog;
    private Button matching_btn_refusal;
    private Button matching_btn_accept;
    private int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 100;
    private TMapView tMapView = null;
    private ArrayList<Waiting> arrayList;
    private WaitingAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("l7xx4fac78a5b9bf445db00bb99ae2708cee");

        // 리사이클러뷰 모션레이아웃
        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_waiting);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new CenterScrollListener());

        arrayList = new ArrayList<>();

        adapter = new WaitingAdapter();
        recyclerView.setAdapter(adapter);


        btn_drive_stop = findViewById(R.id.btn_drive_stop);
        btn_drive_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getData();
    }

//    public void showDialog(){
//        dialog.show();
//
//    }
//    public void checkPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   // 마시멜로우 이상일 경우
//            if (!Settings.canDrawOverlays(this)) {              // 다른앱 위에 그리기 체크
//                Uri uri = Uri.fromParts("package", getPackageName(), null);
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri);
//                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
//            } else {
//                startMain();
//            }
//        } else {
//            startMain();
//        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.M)
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
//            if (!Settings.canDrawOverlays(this)) {
//                finish();
//            } else {
//                startMain();
//            }
//        }
//    }
//    void startMain() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            Intent intent = new Intent(this, driving_overlay.class);
//            intent.putExtra("경로",3);
//            startForegroundService(intent);
//        } else {
//            startService(new Intent(this, driving_overlay.class));
//        }
//        finish();
//    }

    private void getData() {
        List<String> merchant_uid = Arrays.asList(
                "동승",
                "일반",
                "동승"
        );
        List<String> tv_distance = Arrays.asList(
                "0.6KM",
                "1KM",
                "2KM"
        );
        List<String> personnel = Arrays.asList(
                "2인",
                "1인",
                "3인"
        );
        List<String> matching_s_place = Arrays.asList(
                "영진전문대학교",
                "경산 대평그린빌",
                "성서 청구타운."
        );
        List<String> matching_e_place = Arrays.asList(
                "경북대학교 북문",
                "영진전문대학교",
                "영진전문대학교"
        );

        for (int i = 0; i < merchant_uid.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            Waiting data = new Waiting();
            data.setMerchant_uid(merchant_uid.get(i));
            data.setTv_distance(tv_distance.get(i));
            data.setPersonnel(personnel.get(i));
            data.setMatching_s_place(matching_s_place.get(i));
            data.setMatching_e_place(matching_e_place.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }
}