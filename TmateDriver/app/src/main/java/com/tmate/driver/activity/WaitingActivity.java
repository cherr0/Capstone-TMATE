package com.tmate.driver.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mig35.carousellayoutmanager.CarouselLayoutManager;
import com.mig35.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.mig35.carousellayoutmanager.CenterScrollListener;
import com.skt.Tmap.TMapView;
import com.tmate.driver.GpsTracker;
import com.tmate.driver.R;
import com.tmate.driver.adapter.WaitingAdapter;
import com.tmate.driver.data.CallHistory;
import com.tmate.driver.data.Dispatch;
import com.tmate.driver.data.HistoryData;
import com.tmate.driver.data.Waiting;
import com.tmate.driver.net.DataService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaitingActivity extends AppCompatActivity {
    Button btn_drive_stop;
    private Dialog dialog;
    private Button matching_btn_refusal;
    private Button matching_btn_accept;
    private int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 100;
    private TMapView tMapView = null;
    private ArrayList<Waiting> arrayList;
    private WaitingAdapter adapter;
    private RecyclerView recyclerView;
    private ConstraintLayout clWait;

    // 레트로핏 관련
    private SharedPreferences pref;
    private String d_id;
    Call<Boolean> request;
    Call<List<Dispatch>> request2;

    // GpsTracker 자기 위치 가져오기
    private GpsTracker gpsTracker;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    // 쓰레드 관련
    boolean isRunning;
    Calling calling;
    Handler handler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("l7xx4fac78a5b9bf445db00bb99ae2708cee");


//        if (checkLocationServicesStatus()) {
//            checkRunTimePermission();
//        } else {
//            showDialogForLocationServiceSetting();
//        }
//
//        gpsTracker = new GpsTracker(WaitingActivity.this);
//
//        double latitude = gpsTracker.getLatitude();
//        double longitude = gpsTracker.getLongitude();
//
//        Toast.makeText(WaitingActivity.this, "현재위치 \n위도 " + latitude + "\n경도 " + longitude, Toast.LENGTH_LONG).show();
//        Log.d("나의 위도", String.valueOf(latitude));
//        Log.d("나의 경도", String.valueOf(longitude));

        handler = new Handler();
        calling = new Calling();
        isRunning = true;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isRunning) {
                        handler.post(calling);
                        Thread.sleep(2000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        isRunning = true;
        thread.start();

        pref = getSharedPreferences("loginDriver", MODE_PRIVATE);
        d_id = pref.getString("d_id", "");

        // 리사이클러뷰 모션레이아웃
        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());

        recyclerView = (RecyclerView) findViewById(R.id.rv_waiting);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new CenterScrollListener());

        clWait = findViewById(R.id.cl_waiting);

        arrayList = new ArrayList<>();

        adapter = new WaitingAdapter();
        recyclerView.setAdapter(adapter);


        btn_drive_stop = findViewById(R.id.btn_drive_stop);
        btn_drive_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request = DataService.getInstance().call.modifyStatusByD_idAndFlag(d_id, 0);
                request.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.code() == 200) {
                            isRunning = false;
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

            }
        });

    }

    // 내부 쓰레드 클래스
    public class Calling implements Runnable {
        @Override
        public void run() {
            if (checkLocationServicesStatus()) {
                checkRunTimePermission();
            } else {
                showDialogForLocationServiceSetting();
            }

            gpsTracker = new GpsTracker(WaitingActivity.this);

            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();

            Toast.makeText(WaitingActivity.this, "현재위치 \n위도 " + latitude + "\n경도 " + longitude, Toast.LENGTH_LONG).show();

            isRunning = true;

            request2 = DataService.getInstance().call.getCallInfoByPosition(latitude, longitude);
            request2.enqueue(new Callback<List<Dispatch>>() {
                @Override
                public void onResponse(Call<List<Dispatch>> call, Response<List<Dispatch>> response) {
                    if (response.code() == 200) {
                        List<Dispatch> list = response.body();

                        Log.d("넘어오는 리스트 정보 ", list.toString());

                        // list가 널이 아닐때
                        // 리스트 그림 그려준다.
                        if (!list.isEmpty()) {
                            isRunning = false;
                            clWait.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            for (int i = 0; i < list.size(); i++) {
                                CallHistory data = new CallHistory();
                                data.setMerchant_uid(list.get(i).getDp_id());
                                data.setTo_people(Integer.parseInt(list.get(i).getDp_id().substring(18)));
                                data.setDistance1(list.get(i).getDistance());
                                data.setH_s_place(list.get(i).getStart_place());
                                data.setH_f_place(list.get(i).getFinish_place());
                                data.setH_s_lttd(list.get(i).getStart_lat());
                                data.setH_s_lngtd(list.get(i).getStart_lng());
                                data.setH_f_lttd(list.get(i).getFinish_lat());
                                data.setH_f_lngtd(list.get(i).getFinish_lng());


                                adapter.addItem(data);
                            }

                            adapter.notifyDataSetChanged();
                        }
                        // list가 널일때
                        // 계속해서 검색한다.
                        else{
                            clWait.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            isRunning = true;
                        }

                    }
                }

                @Override
                public void onFailure(Call<List<Dispatch>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }


//    private void getData() {
//        List<String> merchant_uid = Arrays.asList(
//                "동승",
//                "일반",
//                "동승"
//        );
//        List<String> tv_distance = Arrays.asList(
//                "0.6KM",
//                "1KM",
//                "2KM"
//        );
//        List<String> personnel = Arrays.asList(
//                "2인",
//                "1인",
//                "3인"
//        );
//        List<String> matching_s_place = Arrays.asList(
//                "영진전문대학교",
//                "경산 대평그린빌",
//                "성서 청구타운."
//        );
//        List<String> matching_e_place = Arrays.asList(
//                "경북대학교 북문",
//                "영진전문대학교",
//                "영진전문대학교"
//        );
//        for (int i = 0; i < merchant_uid.size(); i++) {
//            // 각 List의 값들을 data 객체에 set 해줍니다.
//            Waiting data = new Waiting();
//            data.setMerchant_uid(merchant_uid.get(i));
//            data.setTv_distance(tv_distance.get(i));
//            data.setPersonnel(personnel.get(i));
//            data.setMatching_s_place(matching_s_place.get(i));
//            data.setMatching_e_place(matching_e_place.get(i));
//
//            // 각 값이 들어간 data를 adapter에 추가합니다.
//            adapter.addItem(data);
//        }
//
//        // adapter의 값이 변경되었다는 것을 알려줍니다.
//        adapter.notifyDataSetChanged();
//    }

    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if (check_result) {

                //위치 값을 가져올 수 있음
                ;
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(WaitingActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                } else {

                    Toast.makeText(WaitingActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission() {

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(WaitingActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(WaitingActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음


        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(WaitingActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(WaitingActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(WaitingActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(WaitingActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(WaitingActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(request != null) request.cancel();
        if(request2 != null) request2.cancel();
    }

    @Override
    public void onStart() {
        super.onStart();
        isRunning = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        isRunning = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isRunning = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        isRunning = false;
    }

}