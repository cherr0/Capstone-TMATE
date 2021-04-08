package com.tmate.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapData.AutoCompleteListenerCallback;
import com.skt.Tmap.TMapData.FindAllPOIListenerCallback;
import com.skt.Tmap.TMapData.OnResponseCodeInfoCallback;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapGpsManager.onLocationChangedCallback;
import com.skt.Tmap.TMapInfo;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;
import com.tmate.user.common.Common;
import com.tmate.user.common.LogManager;
import com.tmate.user.common.PermissionManager;
import com.tmate.user.databinding.ActivityMatchingMapBinding;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import static com.skt.Tmap.util.HttpConnect.getContentFromNode;

public class MatchingMapActivity extends AppCompatActivity implements onLocationChangedCallback{
    private ActivityMatchingMapBinding b;
    double d1;
    double d2;
    double d3;
    double d4;
    int start_flag =0;
    int together_option = 0;

    @Override
    public void onLocationChange(Location location) {
        LogManager.printLog("onLocationChange " + location.getLatitude() + " " + location.getLongitude() + " " + location.getSpeed() + " " + location.getAccuracy());

        if (m_bTrackingMode) {
            mMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
            mMapView.setIconVisibility(m_bTrackingMode);
        }
    }

    /* slide menu */
    private double rGoX;
    private double rGoY;
    //16.09.26 KMY [ 화면중심좌표 메뉴 수정 ]
    public static final int MESSAGE_STATE_ZOOM = 1;
    public static final int MESSAGE_STATE_GEOCODING = 2;
    public static final int MESSAGE_STATE_POI = 3;
    public static final int MESSAGE_STATE_ROUTE = 5;
    public static final int MESSAGE_STATE_APIKEY = 6;
    public static final int MESSAGE_ERROR = 7;
    public static final int MESSAGE_STATE_POI_AUTO = 8;
    private TMapView mMapView = null;
    private Context mContext;
    private static final String mApiKey = "l7xx4fac78a5b9bf445db00bb99ae2708cee"; // 발급받은 SKT AppKey
    private int m_nCurrentZoomLevel = 0;
    // geofencing type save
    private boolean m_bTrackingMode = false;
    //16.09.26 KMY [ 화면중심좌표 메뉴 수정 ]
    ArrayList<String> mArrayID;
    ArrayList<String> mArrayLineID;
    private static int mLineID;
    ArrayList<String> mArrayMarkerID;
    private static int mMarkerID;
    ArrayList<String> mArrayGeofenchingID;
    TMapGpsManager gps = null;
    PermissionManager mPermissionManager = null;

    private final MatchingFragment mf = new MatchingFragment();
    private String totalDistance = null;
    private String totalTime = null;
    private String totalFare = null;
    private ArrayList<TMapPOIItem> mArrPoiItem = null;
    private TMapPOIItem mPoiItem = null;
    private CharSequence[] items = null;
    private String oldBAddress = null;
    private String newAddress = null;
    double Latitude2;
    double Longitude2;
    final Geocoder geocoder = new Geocoder(this);
    private final String reverseLabelID = "ReverseLabel";

    TMapPoint tMapPointStart;
    TMapPoint tMapPointEnd;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionManager.setResponse(requestCode, grantResults);
    }
    int together;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b= ActivityMatchingMapBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        Intent intent = getIntent();
        together = intent.getExtras().getInt("together"); /*String형*/


        b.change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (together == 0) {
                    b.slideTitle.setText("동승할 인원을 설정해주세요");
                    b.placePage.setVisibility(View.GONE);
                    b.togetherPage.setVisibility(View.VISIBLE);
                    hideKeyBoard();

                    tMapPointStart = new TMapPoint(d1, d2);
                    tMapPointEnd = new TMapPoint(d3, d4);
                    drawCarPath();
                } else {
                    tMapPointStart = new TMapPoint(d1, d2);
                    tMapPointEnd = new TMapPoint(d3, d4);
                    drawCarPath();
                }
            }
        });

        b.raDouble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                together_option=2;
            }
        });
        b.raTriple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                together_option=3;
            }
        });
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
                start_flag=1;
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
        gps = new TMapGpsManager(MatchingMapActivity.this);
        mPermissionManager = new PermissionManager();
        //RelativeLayout mMainRelativeLayout = (RelativeLayout) findViewById(R.id.mapview_layout);
        mMapView = new TMapView(this);
        b.mapviewLayout.addView(mMapView);
        mMapView.setZoomLevel(16);
        mMapView.setBufferStep(3);
        mMapView.setSKTMapApiKey(mApiKey);

        initSildeMenu();

        m_nCurrentZoomLevel = -1;
        totalDistance = null;
        totalTime = null;
        totalFare = null;
        mArrPoiItem = null;
        mPoiItem = null;
        items = null;
        oldBAddress = null;
        newAddress = null;


        mArrayID = new ArrayList<String>();


        mArrayLineID = new ArrayList<String>();
        mLineID = 0;

        mArrayMarkerID = new ArrayList<String>();
        mMarkerID = 0;

        mArrayGeofenchingID = new ArrayList<String>();

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.point);
        mMapView.setIcon(bitmap);

        m_bTrackingMode = false;
        setTrackingMode(false);
    }
    public void onClickZoomInBtn(View v) {
        mapZoomIn();
    }

    public void onClickZoomOutBtn(View v) {
        mapZoomOut();
    }

    private void initMap() {
        mMapView.setZoomLevel(16);
        m_bTrackingMode = false;
        setTrackingMode(false);
        mMapView.setTileType(mMapView.TILETYPE_HDTILE);


        removeMarker();

        mMapView.setCompassMode(false);
        mMapView.setSightVisible(false);


        mMapView.removeMarkerItem2(reverseLabelID);

        m_nCurrentZoomLevel = -1;

        totalDistance = null;
        totalTime = null;
        totalFare = null;
        mArrPoiItem = null;
        mPoiItem = null;
        items = null;
        oldBAddress = null;
        newAddress = null;
    }

    private void initSildeMenu() {
        // init ui

        mMapView.setOnApiKeyListener(new TMapView.OnApiKeyListenerCallback() {
            @Override
            public void SKTMapApikeySucceed() {
                setTextLevel(MESSAGE_STATE_ZOOM);
            }

            @Override
            public void SKTMapApikeyFailed(String errorMsg) {
                setTextLevel(MESSAGE_STATE_APIKEY);
            }
        });

        mMapView.setOnDisableScrollWithZoomLevelListener(new TMapView.OnDisableScrollWithZoomLevelCallback() {
            @Override
            public void onDisableScrollWithZoomLevelEvent(float zoom, TMapPoint centerPoint) {
                setTextLevel(MESSAGE_STATE_ZOOM);
            }
        });

        mMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
            @Override
            public boolean onPressUpEvent(ArrayList<TMapMarkerItem> markerlist, ArrayList<TMapPOIItem> poilist, TMapPoint point, PointF pointf) {
                return false;
            }

            @Override
            public boolean onPressEvent(ArrayList<TMapMarkerItem> markerlist, ArrayList<TMapPOIItem> poilist, TMapPoint point, PointF pointf) {
                if (m_bTrackingMode) {
                    m_bTrackingMode = false;
                    setTrackingMode(m_bTrackingMode);
                }
                return true;
            }
        });

        mMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
            @Override
            public void onCalloutRightButton(TMapMarkerItem markerItem) {
                String strMessage = "";
                strMessage = "ID: " + markerItem.getID() + " " + "Title " + markerItem.getCalloutTitle();
                Common.showAlertDialog(MatchingMapActivity.this, "Callout Right Button", strMessage);
            }
        });

        Adapter = new ArrayAdapter<String>(this, R.layout.list_layout, arDessert);

        b.editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String strData = s.toString();
                TMapData tmapdata = new TMapData();
                tmapdata.autoComplete(strData, new AutoCompleteListenerCallback() {
                    @Override
                    public void onAutoComplete(ArrayList<String> poiItem) {
                        if (poiItem == null && poiItem.size() < 0) {
                            setTextLevel(MESSAGE_ERROR);
                            return;
                        }
                        arDessert.clear();

                        for (int i = 0; i < poiItem.size(); i++) {
                            arDessert.add(poiItem.get(i));
                        }
                        setTextLevel(MESSAGE_STATE_POI_AUTO);
                    }
                });
            }
        });

        TMapData mapData = new TMapData();
        mapData.setResponseCodeInfoCallBack(new OnResponseCodeInfoCallback() {

            @Override
            public void responseCodeInfo(String apiName, int resCode, String url) {
//				Log.d("test", ">>>>>>>>>>>apiName = " + apiName + " / " + resCode + " / " + url);
            }
        });
    }
    public void drawCarPath() {
        findPathDataAllType(TMapData.TMapPathType.CAR_PATH);
    }
    private void findPathDataAllType(final TMapData.TMapPathType type) {
        totalDistance = null;
        totalTime = null;
        totalFare = null;

        TMapPoint point1 = tMapPointStart;
        TMapPoint point2 = tMapPointEnd;

        TMapData tmapdata = new TMapData();

        tmapdata.findPathDataAllType(type, point1, point2, new TMapData.FindPathDataAllListenerCallback() {
            @Override
            public void onFindPathDataAll(Document doc) {
                TMapPolyLine polyline = new TMapPolyLine();
                polyline.setLineWidth(10);
                if (doc != null) {
                    NodeList list = doc.getElementsByTagName("Document");
                    Element item2 = (Element) list.item(0);
                    totalDistance = getContentFromNode(item2, "tmap:totalDistance");
                    totalTime = getContentFromNode(item2, "tmap:totalTime");
                    if (type == TMapData.TMapPathType.CAR_PATH) {
                        totalFare = getContentFromNode(item2, "tmap:totalFare");
                    }

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

                    TMapInfo info = mMapView.getDisplayTMapInfo(polyline.getLinePoint());
                    int zoom = info.getTMapZoomLevel();
                    if (zoom > 12) {
                        zoom = 12;
                    }

                    if(together == 0) {
                        mMapView.addTMapPath(polyline);
                        mMapView.setZoomLevel(zoom);
                        mMapView.setCenterPoint(info.getTMapPoint().getLongitude(), info.getTMapPoint().getLatitude());
                    }
                    setTextLevel(MESSAGE_STATE_ROUTE);
                }
            }
        });
    }

    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(b.editText.getWindowToken(), 0);
    }

    ArrayList<String> arDessert = new ArrayList<String>();
    ArrayAdapter<String> Adapter;
    ListView list;


    private String name = null;
    private String address = null;

    private double distance;
    private int moneyplan;
    private String movetime;

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_ZOOM:
                    b.zoomlevelText.setText("Lv." + mMapView.getZoomLevel());
                    m_nCurrentZoomLevel = mMapView.getZoomLevel() - 3;
                    break;
                case MESSAGE_STATE_GEOCODING:
                    if (name != null) {
                        b.geocodingLayout.setVisibility(View.VISIBLE);
                        b.routeLayout.setVisibility(View.GONE);

                        b.geocodingTitle.setText(name);
                        b.geocodingSub.setText(address);
                    } else {
                        //16.09.26 KMY [ 화면중심좌표 메뉴 수정 ]
                        b.geocodingLayout.setVisibility(View.GONE);
                        b.routeLayout.setVisibility(View.GONE);

                    }
                    break;
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

                    double km = Double.parseDouble(totalDistance) / 1000;

                    distance = km;
                    movetime = time;
                    moneyplan = ((hour*60 + minute)*1000);

                    if(together != 0 ) {
                        Intent it = new Intent(getApplicationContext(), CallGeneralActivity.class);
                        startActivity(it);
                        finish();
                    }
/*                    if (totalFare != null) {
                        routeFare.setVisibility(View.VISIBLE);
                        routeFare.setText("유료도로 요금 : " + totalFare + "원");
                    } else {
                        routeFare.setVisibility(View.GONE);
                    }*/
                    break;
                case MESSAGE_STATE_POI:
                    if (b.autoCompleteLayout.getVisibility() == View.VISIBLE) {
                        b.autoCompleteLayout.setVisibility(View.GONE);
                        arDessert.clear();
                        b.editText.setText("");
                        hideKeyBoard();
                    }
                    showPOIListAlert();
                    break;
                case MESSAGE_STATE_POI_AUTO:
                    Adapter.notifyDataSetChanged();
                    break;
                case MESSAGE_STATE_APIKEY:
                    Toast.makeText(getApplicationContext(), "앱키 인증에 실패 했습니다.", Toast.LENGTH_SHORT).show();
                    break;

                case MESSAGE_ERROR:
                    Toast.makeText(getApplicationContext(), "정보가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void setTextLevel(final int what) {
        new Thread() {
            public void run() {
                Message msg = handler.obtainMessage(what);
                handler.sendMessage(msg);
            }
        }.start();
    }
    public void onClickLocationBtn(View v) {
        m_bTrackingMode = !m_bTrackingMode;
        if (m_bTrackingMode) {
            mMapView.setIconVisibility(m_bTrackingMode);
        }
        setTrackingMode(m_bTrackingMode);
        TMapPoint tpoint = mMapView.getCenterPoint();
        d1 = tpoint.getLatitude();
        d2 = tpoint.getLongitude();

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
        String mail = list.get(0).toString();

        int idx = mail.indexOf(":");
        String mail1 = mail.substring(idx+1);

        idx = mail1.indexOf("]");
        String mail2 = mail1.substring(0,idx);

        if (list != null) {
            if (list.size()==0) {
                Toast.makeText(getApplicationContext(), "못찾음..", Toast.LENGTH_LONG).show();
            } else {
                b.startPlace.setText(mail2);
            }
        }
    }
    /**
     * mapZoomIn 지도를 한단계 확대한다.
     */
    public void mapZoomIn() {
        mMapView.MapZoomIn();
    }
    /**
     * mapZoomOut 지도를 한단계 축소한다.
     */
    public void mapZoomOut() {
        mMapView.MapZoomOut();
    }

    /**
     * getZoomLevel 현재 줌의 레벨을 가지고 온다.
     */
    public void getZoomLevel() {
        int nCurrentZoomLevel = mMapView.getZoomLevel();
        Common.showAlertDialog(this, "", "현재 Zoom Level : " + Integer.toString(nCurrentZoomLevel));
    }

    /**
     * setZoomLevel Zoom Level을 설정한다.
     */
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
    /**
     * getLocationPoint 현재위치로 표시될 좌표의 위도, 경도를 반환한다.
     */
    public void getLocationPoint() {
        TMapPoint point = mMapView.getLocationPoint();

        double Latitude = point.getLatitude();
        double Longitude = point.getLongitude();

        Latitude2 = Latitude;
        Longitude2 = Longitude;

        LogManager.printLog("Latitude " + Latitude + " Longitude " + Longitude);

        String strResult = String.format("Latitude = %f Longitude = %f", Latitude, Longitude);

        Common.showAlertDialog(this, "", strResult);
    }

    /**
     * setTrackingMode 화면중심을 단말의 현재위치로 이동시켜주는 트래킹모드로 설정한다.
     */
    public void setTrackingMode(boolean isShow) {
        mMapView.setTrackingMode(isShow);
        if (isShow) {
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
        } else {
            if (gps != null) {
                gps.CloseGps();
            }
            b.locationBtn.setBackgroundResource(R.drawable.location_btn);
        }
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
    /**
     * findAllPoi 통합검색 POI를 요청한다.
     */
    public void findAllPoi(String strData) {
        removeMarker();
        TMapData tmapdata = new TMapData();

        tmapdata.findAllPOI(strData, new FindAllPOIListenerCallback() {
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

                mMapView.addMarkerItem(strID, markerItem);
                mArrayMarkerID.add(strID);
                mMapView.setCenterPoint(mPoiItem.getPOIPoint().getLongitude(), mPoiItem.getPOIPoint().getLatitude());
                rGoX = mPoiItem.getPOIPoint().getLongitude();
                rGoY = mPoiItem.getPOIPoint().getLatitude();
                if(start_flag==1) {
                    d1 = rGoY;
                    d2 = rGoX;
                    b.startPlace.setText(mPoiItem.getPOIName());
                } else {
                    d3 = rGoY;
                    d4 = rGoX;
                    b.finishPlace.setText(mPoiItem.getPOIName());
                }
                hideKeyBoard();
            }
        });
        builder.show();
    }
}