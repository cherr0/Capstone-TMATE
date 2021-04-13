package com.tmate.user.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import com.tmate.user.databinding.ActivityMatchingMapBinding;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.skt.Tmap.util.HttpConnect.getContentFromNode;

public class MatchingMapActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {

    //바뀐 위치에 대한 좌표값 설정
    @Override
    public void onLocationChange(Location location) {
        if (m_bTrackingMode) {
            mMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
            mMapView.setIconVisibility(m_bTrackingMode);
        }
    }

    Geocoder geocoder;


    private ActivityMatchingMapBinding b;
    private Context mContext;

    //맵을 띄우기 위한 변수 들
    private TMapView mMapView = null; //맵 뷰
    private static final String mApiKey = "l7xx4fac78a5b9bf445db00bb99ae2708cee"; // 발급받은 SKT AppKey

    private int together = 0; //동승 설정
    private int togetherOption;//동승 몇명인지

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
        b= ActivityMatchingMapBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());


        //gps
        gps = new TMapGpsManager(MatchingMapActivity.this);
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
        //마커 설정
        mArrayMarkerID = new ArrayList<String>();
        mMarkerID = 0;

        initSildeMenu();



        //동승유무값 가져오기 체크
        Intent intent = getIntent();
        together = intent.getExtras().getInt("together"); /*String형*/

        //출발지, 도착지 설정완료 버튼
        b.goTogetherSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (together == 0) { //동승 일 경우
                    b.slideTitle.setText("동승할 인원을 설정해주세요");
                    b.placePage.setVisibility(View.GONE);//위치 설정 레이아웃 숨기기
                    b.togetherPage.setVisibility(View.VISIBLE); //동승설정 레이아웃 보기
                    hideKeyBoard();//키보드 숨기기

                    tMapPointStart = new TMapPoint(d1, d2); //출발지 좌표 설정
                    tMapPointEnd = new TMapPoint(d3, d4); //도착지 좌표 설정
                    drawCarPath();//자동차 경로 그리는 메서드 호출
                } else { //동승이 아닐 경우
                    tMapPointStart = new TMapPoint(d1, d2);//출발지 좌표 설정
                    tMapPointEnd = new TMapPoint(d3, d4);//도착지 좌표 설정
                    drawCarPath(); //자동차 경로 그리기
                }
            }
        });
        //동승 2명을 선택 했을 시(일반 호출 시 보이지 않을 부분이다)
        b.raDouble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togetherOption=2; //2명 탄다는 것을 알려준다
            }
        });
        //동승 3명을 선택 했을 시(일반 호출 시 보이지 않을 부분이다)
        b.raTriple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togetherOption=3; //3명 탄다는 것을 알려준다
            }
        });
        //동승 호출 호출 버튼(일반 호출 시 보이지 않을 부분이다)
        b.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBoard();
                Intent intent = new Intent(getApplicationContext(), MatchingActivity.class);
                startActivity(intent);
                finish();
            }
        });
        b.startPlace.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                start_flag=1; //출발지를 검색하는 중임을 알려준다
                findAllPoi(b.startPlace.getText().toString());
                return true;
            }
        });
        b.finishPlace.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                start_flag=3;
                findAllPoi(b.finishPlace.getText().toString());
                TMapPoint tpoint = mMapView.getCenterPoint();
                return true;
            }
        });

        mContext = this;
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
    //맵 + 버튼 클릭 시
    public void onClickZoomInBtn(View v) {
        mMapView.MapZoomIn();
    }
    //맵 - 버튼 틀릭 시
    public void onClickZoomOutBtn(View v) {
        mMapView.MapZoomOut();
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
                    totalTime = getContentFromNode(item2, "tmap:totalTime"); //총 시간 설정

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
                    int zoom = info.getTMapZoomLevel();
                    if (zoom > 12) {
                        zoom = 12;
                    }
                    //동승일 경우에만 자동차 경로 그림
                    if(together != 1) {
                        System.out.println("자동차경로 그리기 시작 진2짜");
                        mMapView.addTMapPath(polyline);
                        mMapView.setZoomLevel(zoom);
                        mMapView.setCenterPoint(info.getTMapPoint().getLongitude(), info.getTMapPoint().getLatitude());
                    }
                    setTextLevel(MESSAGE_STATE_ROUTE);
                }

            }

        });
    }
    //키보드 숨기기
    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(b.editText.getWindowToken(), 0);
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

        List<Address> list = null;
        try {
            list = geocoder.getFromLocation(
                    d1, // 위도
                    d2, // 경도
                    1); // 얻어올 값의 개수
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
        };
        //주소만 추출
        String mail = list.get(0).toString();
        int idx = mail.indexOf(":");
        String mail1 = mail.substring(idx+1);
        idx = mail1.indexOf("]");
        String mail2 = mail1.substring(0,idx);
        String result = mail2.substring(mail2.indexOf("국")+1);
        idx = result.indexOf("\"");
        result = result.substring(0,idx);

        if (list != null) { //주소를 못 찾을 경우
            if (list.size()==0) {
                Toast.makeText(getApplicationContext(), "주소를 찾지 못했습니다", Toast.LENGTH_LONG).show();
            } else { //주소를 찾을 경우
                b.startPlace.setText(result); //출발지 설정 텍스트박스에 주소
            }
        }
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
                    Toast.makeText(MatchingMapActivity.this, "위치정보 수신에 동의하지 않으시면 현재위치로 이동할 수 없습니다.", Toast.LENGTH_SHORT).show();
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

    //findAllPoi 통합검색 POI를 요청한다.
    public void findAllPoi(String strData) {
        removeMarker();
        TMapData tmapdata = new TMapData();

        tmapdata.findAllPOI(strData, new TMapData.FindAllPOIListenerCallback() {
            @Override
            public void onFindAllPOI(ArrayList<TMapPOIItem> poiItem) {
                ArrayList<String> numberList = new ArrayList<String>();
                for (int i = 0; i < poiItem.size(); i++) {
                    TMapPOIItem item = poiItem.get(i);
                    numberList.add(item.getPOIName().toString());
                }
                if (numberList.size() > 0) {
                    mArrPoiItem = poiItem;
                    items = numberList.toArray(new CharSequence[numberList.size()]);
                    setTextLevel(MESSAGE_STATE_POI);
                } else {
                    setTextLevel(MESSAGE_ERROR);
                }
            }
        });
    }
    private void showPOIListAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("POI 통합 검색");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                mPoiItem = mArrPoiItem.get(item);

                TMapMarkerItem markerItem = new TMapMarkerItem();

                String strID = String.format("marker%d", mMarkerID++);
                markerItem.setID(strID);
                markerItem.setIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.poi_dot));
                markerItem.setTMapPoint(mPoiItem.getPOIPoint());
                markerItem.setCanShowCallout(false);
                markerItem.setPosition(0.5f, 1.0f);

                //마커 넣기
                mMapView.addMarkerItem(strID, markerItem);
                mArrayMarkerID.add(strID);
                mMapView.setCenterPoint(mPoiItem.getPOIPoint().getLongitude(), mPoiItem.getPOIPoint().getLatitude());
                //출발지를 검색 할 경우
                if(start_flag==1) {
                    d1 = mPoiItem.getPOIPoint().getLatitude(); //출발지 위도
                    d2 = mPoiItem.getPOIPoint().getLongitude(); //도착지 경도
                    b.startPlace.setText(mPoiItem.getPOIName()); //출발지 이름을 출발지 검색창에 세팅
                } else { //도착지를 검색 할 경우
                    d3 = mPoiItem.getPOIPoint().getLatitude(); //도착지 위도
                    d4 = mPoiItem.getPOIPoint().getLongitude(); //도착지 경도
                    b.finishPlace.setText(mPoiItem.getPOIName()); //도착지 이름을 도착지 검색창에 세팅
                }
                hideKeyBoard(); //검색 완료 후 키보드 숨기기
            }
        });
        builder.show();
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
                    b.zoomlevelText.setText("Lv." + mMapView.getZoomLevel());
                    m_nCurrentZoomLevel = mMapView.getZoomLevel() - 3;
                    break;
                //자동차 경로 보여주는 메세지를 받았을때
                case MESSAGE_STATE_ROUTE:
                    b.routeLayout.setVisibility(View.GONE);
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

                    moneyplan = 0;
                    moneyplan = ((hour*60 + minute)*1000);

                    // 동승이 아닐 경우는 위의 값을 구한 뒤 바로 결제창으로 넘어간다
                    if(together != 0 ) {
                        Intent it = new Intent(getApplicationContext(), CallGeneralActivity.class);
                        startActivity(it);
                        finish();
                    }
                    break;
                //위치 알림창 올려달라는 메세지
                case MESSAGE_STATE_POI:
                    if (b.autoCompleteLayout.getVisibility() == View.VISIBLE) {
                        b.autoCompleteLayout.setVisibility(View.GONE);
                        arDessert.clear();
                        b.editText.setText("");
                        hideKeyBoard();
                    }
                    showPOIListAlert();
                    break;
                case MESSAGE_ERROR:
                    Toast.makeText(getApplicationContext(), "정보가 없습니다.", Toast.LENGTH_SHORT).show();
                case MESSAGE_STATE_APIKEY:
                    Toast.makeText(getApplicationContext(), "앱키 인증에 실패 했습니다.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}