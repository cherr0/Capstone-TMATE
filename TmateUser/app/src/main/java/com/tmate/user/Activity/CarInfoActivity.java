package com.tmate.user.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapInfo;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;
import com.tmate.user.Fragment.MatchingFragment;
import com.tmate.user.R;
import com.tmate.user.common.Common;
import com.tmate.user.common.PermissionManager;
import com.tmate.user.data.Dispatch;
import com.tmate.user.data.Member;
import com.tmate.user.databinding.ActivityCarInfoBinding;
import com.tmate.user.databinding.ActivityMatchingMapBinding;
import com.tmate.user.net.DataService;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.skt.Tmap.util.HttpConnect.getContentFromNode;

public class CarInfoActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {

    // 화면 위젯 변수
    TextView car_no;
    TextView h_status;
    TextView h_s_place;
    TextView h_f_place;
    TextView h_people;
    TextView meet_time;
    TextView h_ep_time;
    TextView amount;


    // 레트로핏 사용 부분
    Call<Dispatch> request;
    String driverPhoneNo;

    // 쓰레드 관련
    String dp_id2;

    Call<Dispatch> request2;
    boolean isRunning;
    Handler handler2;
    Positioning positioning;

    //바뀐 위치에 대한 좌표값 설정
    @Override
    public void onLocationChange(Location location) {
        if (m_bTrackingMode) {
            mMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
            mMapView.setIconVisibility(m_bTrackingMode);
        }
    }

    Geocoder geocoder;

    // 기사 정보 초기화



    private ActivityCarInfoBinding b;
    private Context mContext;

    //맵을 띄우기 위한 변수 들
    private TMapView mMapView = null; //맵 뷰
    private static final String mApiKey = "l7xx4fac78a5b9bf445db00bb99ae2708cee"; // 발급받은 SKT AppKey

    //DB에 들어갈 거리와 시간에 대한 변수들
    private String totalDistance = null;//총 거리
    private String totalTime = null; // 총 시간

    //출발지 도착지에 대한 변수들
    TMapPoint tMapPointStart;//출발지 위,경도를 담은 좌표
    TMapPoint tMapPointEnd;//도착지 위,경도를 담은 좌표
    double d1; //출발지 위도
    double d2; //출발지 경도
    double d3; //도착지 위도
    double d4; //도착지 경도
    int start_flag =0;//출발지를 검색하는 중인지 도착지를 검색하는 중인지 알 수 있는 플래그이다
    private int moneyplan = 0;// 도착지에 도착하는 시간에따른 예상 가격
    private String time = null;//예상 도착 시간
    private Double km;//출발지에서 도착지까지의 거리


    //gps 및 경로 그리기 위한 변수들
    private boolean m_bTrackingMode = false; // geofencing type save

    PermissionManager mPermissionManager = null; //권한 요청(GPS)
    TMapGpsManager gps = null; //gps

    //마커에 대한 변수들
    private ArrayList<TMapPOIItem> mArrPoiItem = null;
    private TMapPOIItem mPoiItem = null;
    private CharSequence[] items = null;
    ArrayList<String> mArrayMarkerID;
    private static int mMarkerID;


    private int m_nCurrentZoomLevel = 0;//줌 레벨에 대한 변수

    //핸들러 요청에 대한 변수
    public static final int MESSAGE_STATE_ZOOM = 1;//줌 레벨 요청
    public static final int MESSAGE_STATE_POI = 3; //위치리스트 요청
    public static final int MESSAGE_STATE_ROUTE = 5; //경로에 대한 정보 요청
    public static final int MESSAGE_STATE_APIKEY = 6; //api키가 잘못됐거나 오류가 났을때의 요청
    public static final int MESSAGE_ERROR = 7; //정보가 없을때의 요청

    //위치 리스트
    ArrayList<String> arDessert = new ArrayList<String>();

    //권한 요청
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionManager.setResponse(requestCode, grantResults);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getApplicationContext();
        geocoder = new Geocoder(mContext);//지오코더
        b= ActivityCarInfoBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());


        //gps
        gps = new TMapGpsManager(CarInfoActivity.this);
        mPermissionManager = new PermissionManager();
        //맵 화면에 띄우기
        mMapView = new TMapView(this);
        mMapView.setSKTMapApiKey(mApiKey);
        mMapView = new TMapView(this);
        b.mapviewLayout.addView(mMapView);
        //맵 초기화면 설정
        mMapView.setZoomLevel(16);
        mMapView.setBufferStep(3);
        mMapView.setSKTMapApiKey(mApiKey);
        m_nCurrentZoomLevel = -1;
        mMapView.setIconVisibility(true);//현재위치로 표시될 아이콘을 표시할지 여부를 설정
//        setGps(); //시작하자마자 자신의 위치가 보이게 한다

        //마커 설정
        mArrayMarkerID = new ArrayList<String>();
        mMarkerID = 0;

        dataInitWidget();

        // 쓰레드 상태
        handler2 = new Handler();
        positioning = new Positioning();
        isRunning = true;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isRunning) {
                        handler.post(positioning);
                        Thread.sleep(2000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        isRunning = true;
        thread.start();

        initSildeMenu();

        b.complete.setOnClickListener(v -> {
            Intent intent = new Intent(this,CarDrivingActivity.class);
            intent.putExtra("출발지위도",35.888836);
            intent.putExtra("출발지경도",128.6102996);
            intent.putExtra("도착지위도",35.8686835);
            intent.putExtra("도착지경도",128.5989036);
            startActivity(intent);
            finish();
        });
        b.driverCall.setOnClickListener(v -> {
            Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+driverPhoneNo));
            startActivity(mIntent.addFlags(FLAG_ACTIVITY_NEW_TASK));
        });
        mContext = this;
    }

    // 택시 기사 위치 실시간으로 가져오는 내브 쓰레드 클래스
    public class Positioning implements Runnable {
        @Override
        public void run() {
            request2 = DataService.getInstance().matchAPI.getDriverPosition(dp_id2);
            request2.enqueue(new Callback<Dispatch>() {
                @Override
                public void onResponse(Call<Dispatch> call, Response<Dispatch> response) {
                    if (response.code() == 200 && response.body() != null) {
                        Dispatch dispatch = response.body();
                        Log.d("넘어오는 기사 정보", dispatch.toString());
                        Toast.makeText(mContext, "계속하여 기사 위치를 가져옵니다.", Toast.LENGTH_SHORT).show();
                        tMapPointStart = new TMapPoint(dispatch.getM_lat(), dispatch.getM_lng());

                        switch (dispatch.getDp_status()) {
                            case "3":
                                tMapPointEnd = new TMapPoint(dispatch.getStart_lat(), dispatch.getStart_lng());
                                h_status.setText("탑승 대기중");
                                drawCarPath();
                                isRunning = true;
                                break;
                            case "4":
                                tMapPointEnd = new TMapPoint(dispatch.getFinish_lat(), dispatch.getFinish_lng());
                                h_status.setText("탑승중");
                                drawCarPath();
                                isRunning = true;
                                break;
                            case "5":
                                isRunning = false;
                                break;
                        }

                    }
                }

                @Override
                public void onFailure(Call<Dispatch> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }



    // 레트로핏 관련 화면 UI 초기화 메서드
    public void initWidget() {
        car_no = findViewById(R.id.car_no);
        h_status = findViewById(R.id.h_status);
        h_s_place = findViewById(R.id.h_s_place);
        h_f_place = findViewById(R.id.h_f_place);
        h_people = findViewById(R.id.h_people);
        meet_time = findViewById(R.id.meet_time);
        h_ep_time = findViewById(R.id.h_ep_time);
        amount = findViewById(R.id.amount);
    }

    public void dataInitWidget() {
        initWidget();
        Intent intent =getIntent();
        String dp_id = intent.getStringExtra("dp_id");
        Log.d("찍히는 이용정보코드 : ", dp_id);
        request = DataService.getInstance().matchAPI.readCurrentDispatch(dp_id);
        request.enqueue(new Callback<Dispatch>() {
            @Override
            public void onResponse(Call<Dispatch> call, Response<Dispatch> response) {
                if (response.code() == 200 && response.body() != null) {
                    Log.d("넘어오는 호출 정보 : ", response.body().toString());
                    Dispatch dispatch = response.body();
                    String d_id = dispatch.getD_id();
                    driverPhoneNo = d_id.substring(2, 5) + "-" + d_id.substring(5,9) + "-" + d_id.substring(9,13);
                    Log.d("기사 전화번호 : ", driverPhoneNo);
                    // 일반과 동승(2,3)
                    switch (dispatch.getDp_id().substring(18)) {
                        case "1":
                            h_people.setText("1명");
                            meet_time.setText("동승이용시");
                            break;
                        case  "2":
                            h_people.setText("2명");
                            meet_time.setText("보류");
                            break;
                        case "3":
                            h_people.setText("3명");
                            meet_time.setText("보류");
                            break;
                    }


                    dp_id2 = dispatch.getDp_id();
                    car_no.setText(dispatch.getCar_no());
                    h_s_place.setText(dispatch.getStart_place());
                    h_f_place.setText(dispatch.getFinish_place());
                    h_ep_time.setText("보류");
                    amount.setText("보류");


                    // 탑승 대기중일때 기사위치에서 출발지
                    if(dispatch.getDp_status().equals("3")) {
                        h_status.setText("탑승완료");
                        d1 = dispatch.getM_lat();
                        d2 = dispatch.getM_lng();
                        d3 = dispatch.getStart_lat();
                        d4 = dispatch.getStart_lng();
                    }

                    // 탑승중일때 기사위치에서 도착지
                    if (dispatch.getDp_status().equals("4")) {
                        h_status.setText("탑승중");
                        d1 = dispatch.getM_lat();
                        d2 = dispatch.getM_lng();
                        d3 = dispatch.getFinish_lat();
                        d4 = dispatch.getFinish_lng();
                    }

                    tMapPointStart = new TMapPoint(d1, d2); //출발지 좌표 설정
                    tMapPointEnd = new TMapPoint(d3, d4); //도착지 좌표 설정

                    drawCarPath();

                }
            }

            @Override
            public void onFailure(Call<Dispatch> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void initSildeMenu() {
        //api키 정상적인지 체크
        mMapView.setOnApiKeyListener(new TMapView.OnApiKeyListenerCallback() {
            //api키가 정상적일 떄 줌레벨을 세팅한다
            @Override
            public void SKTMapApikeySucceed() {
                setTextLevel(MESSAGE_STATE_ZOOM);
            }
            //api키가 비정상적일때 에러 메시지를 부른다
            @Override
            public void SKTMapApikeyFailed(String errorMsg) {
                setTextLevel(MESSAGE_STATE_APIKEY);
            }
        });

        //줌 스크롤 시
        mMapView.setOnDisableScrollWithZoomLevelListener(new TMapView.OnDisableScrollWithZoomLevelCallback() {
            //줌 레벨을 다시 세팅하여 보여준다
            @Override
            public void onDisableScrollWithZoomLevelEvent(float zoom, TMapPoint centerPoint) {
                setTextLevel(MESSAGE_STATE_ZOOM);
            }
        });

    }
    //zoom레벨을 설정한다
    public void setZoomLevel() {
        final String[] arrString = getResources().getStringArray(R.array.a_zoomlevel);
        AlertDialog dlg = new AlertDialog.Builder(this).setIcon(R.drawable.tmark).setTitle("Select Zoom Level")
                .setSingleChoiceItems(R.array.a_zoomlevel, m_nCurrentZoomLevel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        dialog.dismiss();
                        mMapView.setZoomLevel(Integer.parseInt(arrString[item]));
                        m_nCurrentZoomLevel = item;
                    }
                }).show();
    }

    //getZoomLevel 현재 줌의 레벨을 가지고 온다.
    public void getZoomLevel() {
        int nCurrentZoomLevel = mMapView.getZoomLevel();
        Common.showAlertDialog(this, "", "현재 Zoom Level : " + Integer.toString(nCurrentZoomLevel));
    }
    //자동차 경로 그리기
    public void drawCarPath() {
        Log.d("여기 들어오는가 ? ", d1 + ":" + d2 + ":" + d3 + ":" + d4);
        findPathDataAllType(TMapData.TMapPathType.CAR_PATH);//자동차경로 그리기 호출
    }
    //자동차 경로 그리기
    private void findPathDataAllType(final TMapData.TMapPathType type) {
        removeMarker();
        totalDistance = null; // 총 거리(km)
        totalTime = null; // 총 시간

        TMapPoint point1 = tMapPointStart; //출발지 좌표 설정
        TMapPoint point2 = tMapPointEnd; //도착지 좌표 설정

        TMapData tmapdata = new TMapData();

        tmapdata.findPathDataAllType(type, point1, point2, new TMapData.FindPathDataAllListenerCallback() {
            @Override
            public void onFindPathDataAll(Document doc) {
                TMapPolyLine polyline = new TMapPolyLine();
                polyline.setLineWidth(10);
                if (doc != null) {
                    NodeList list = doc.getElementsByTagName("Document");
                    Element item2 = (Element) list.item(0);
                    totalDistance = getContentFromNode(item2, "tmap:totalDistance"); //총 거리설정
                    Log.d("총 거리 : ", totalDistance);
                    totalTime = getContentFromNode(item2, "tmap:totalTime"); //총 시간 설정
                    Log.d("총 시간 : ", totalTime);

                    NodeList list2 = doc.getElementsByTagName("LineString");

                    for (int i = 0; i < list2.getLength(); i++) {
                        Element item = (Element) list2.item(i);
                        String str = getContentFromNode(item, "coordinates");
                        if (str == null) {
                            continue;
                        }

                        String[] str2 = str.split(" ");
                        for (int k = 0; k < str2.length; k++) {
                            try {
                                String[] str3 = str2[k].split(",");
                                TMapPoint point = new TMapPoint(Double.parseDouble(str3[1]), Double.parseDouble(str3[0]));
                                polyline.addLinePoint(point);
                            } catch (Exception e) {

                            }
                        }
                    }
                    //줌 설정
                    TMapInfo info = mMapView.getDisplayTMapInfo(polyline.getLinePoint());

                    mMapView.addTMapPath(polyline);
//                    mMapView.setZoomLevel(16);
                    mMapView.setCenterPoint(info.getTMapPoint().getLongitude(), info.getTMapPoint().getLatitude());
                    setTextLevel(MESSAGE_STATE_ROUTE);
                }

            }

        });
    }

    //현재 위치 버튼을 눌렀을때 현재 위치로 이동하고 포인트를 찍어주는 메소드
    public void onClickLocationBtn(View v) {
        m_bTrackingMode = !m_bTrackingMode;
        if (m_bTrackingMode) {
            mMapView.setIconVisibility(m_bTrackingMode);
        }
        setTrackingMode(m_bTrackingMode); //트래킹모드(화면 중심을 현재위치로 이동) 설정
        TMapPoint tpoint = mMapView.getCenterPoint();
        d1 = tpoint.getLatitude(); //출발지 위도
        d2 = tpoint.getLongitude(); //출발지 경도
    }
    // setTrackingMode 화면중심을 단말의 현재위치로 이동시켜주는 트래킹모드로 설정한다.
    public void setTrackingMode(boolean isShow) {
        mMapView.setTrackingMode(isShow);
        if (isShow) {//gps 버튼을 켰을때
            mPermissionManager.request(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, new PermissionManager.PermissionListener() {
                @Override
                public void granted() {
                    if (gps != null) {
                        gps.setMinTime(1000);
                        gps.setMinDistance(5);
                        gps.setProvider(gps.GPS_PROVIDER);
                        gps.OpenGps();
                        gps.setProvider(gps.NETWORK_PROVIDER);
                        gps.OpenGps();
                    }
                }
                @Override
                public void denied() {
                    Toast.makeText(CarInfoActivity.this, "위치정보 수신에 동의하지 않으시면 현재위치로 이동할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            });
            b.locationBtn.setBackgroundResource(R.drawable.location_btn_sel);
            mMapView.setCenterPoint(mMapView.getLocationPoint().getLongitude(), mMapView.getLocationPoint().getLatitude());
        } else { // gps 버튼을 껐을때
            if (gps != null) {
                gps.CloseGps();
            }
            b.locationBtn.setBackgroundResource(R.drawable.location_btn);
        }
    }

    //getLocationPoint 현재위치로 표시될 좌표의 위도, 경도를 반환한다.
    public void getLocationPoint() {
        TMapPoint point = mMapView.getLocationPoint();

        double Latitude = point.getLatitude();
        double Longitude = point.getLongitude();

        String strResult = String.format("Latitude = %f Longitude = %f", Latitude, Longitude);

        Common.showAlertDialog(this, "", strResult);
    }
    //현재 표시된 마커들을 지운다
    public void removeMarker() {
        mArrPoiItem = null;
        mPoiItem = null;
        items = null;
        if (mArrayMarkerID.size() <= 0)
            return;
        String strMarkerID = null;
        for (int i = 0; i < mArrayMarkerID.size(); i++) {
            strMarkerID = mArrayMarkerID.get(i);
            mMapView.removeMarkerItem(strMarkerID);
        }

        mArrayMarkerID.clear();
        mMarkerID = 0;
    }
    @Override
    protected void onPause() {
        super.onPause();
        // 화면 꺼지면 위치정보 수신 종료
        if (gps != null) {
            gps.CloseGps();
        }
        b.locationBtn.setBackgroundResource(R.drawable.location_btn);
    }

    //요청에 따른 핸들러
    private void setTextLevel(final int what) {
        new Thread() {
            public void run() {
                Message msg = handler.obtainMessage(what);
                handler.sendMessage(msg);
            }
        }.start();
    }
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //지도의 줌 레벨을 변경했을때
                case MESSAGE_STATE_ZOOM:
                    m_nCurrentZoomLevel = mMapView.getZoomLevel() - 3;
                    break;
                //자동차 경로 보여주는 메세지를 받았을때
                case MESSAGE_STATE_ROUTE:
                    b.geocodingLayout.setVisibility(View.GONE);

                    int totalSec = Integer.parseInt(totalTime);
                    int day = totalSec / (60 * 60 * 24);
                    int hour = (totalSec - day * 60 * 60 * 24) / (60 * 60);
                    int minute = (totalSec - day * 60 * 60 * 24 - hour * 3600) / 60;

                    String time = null;
                    if (hour > 0) {
                        time = hour + "시간 " + minute + "분";
                    } else {
                        time = minute + "분 ";
                    }
                    km = null;
                    km = Double.parseDouble(totalDistance) / 1000; //거리(km기준)

                    //b.predictionDistance.setText(km.toString());
                    //b.predictionTime.setText(time);
                    //b.drivinginfo.setVisibility(View.VISIBLE); //예상 금액,거리, 시간 보기
                    break;
                case MESSAGE_ERROR:
                    Toast.makeText(getApplicationContext(), "정보가 없습니다.", Toast.LENGTH_SHORT).show();
                case MESSAGE_STATE_APIKEY:
                    Toast.makeText(getApplicationContext(), "앱키 인증에 실패 했습니다.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        request.cancel();
        request2.cancel();
        isRunning = false;
    }


}