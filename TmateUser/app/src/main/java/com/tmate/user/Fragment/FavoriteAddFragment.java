package com.tmate.user.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;
import com.tmate.user.R;
import com.tmate.user.common.Common;
import com.tmate.user.common.LogManager;
import com.tmate.user.common.PermissionManager;
import com.tmate.user.databinding.FragmentFavoritesAddBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class FavoriteAddFragment extends Fragment {

    //바뀐 위치에 대한 좌표값 설정(자신의 위치)
    //프래그먼트로 변경시 잘 작동되지 않음
    /*@Override
    public void onLocationChange(Location location) {
        if (m_bTrackingMode) {
            mMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
            mMapView.setIconVisibility(m_bTrackingMode);
        }
    }*/

    Geocoder geocoder;

    private FragmentFavoritesAddBinding b;
    private Context mContext;

    //맵을 띄우기 위한 변수 들
    private TMapView mMapView = null; //맵 뷰
    private static final String mApiKey = "l7xx4fac78a5b9bf445db00bb99ae2708cee"; // 발급받은 SKT AppKey

    double d1; // 위도
    double d2; // 경도

    //gps 및 경로 그리기 위한 변수들
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getContext();
        geocoder = new Geocoder(mContext);//지오코더
        b= FragmentFavoritesAddBinding.inflate(getLayoutInflater());
        View view = b.getRoot();
        //gps
        gps = new TMapGpsManager(getContext());
        mPermissionManager = new PermissionManager();
        //맵 화면에 띄우기
        mMapView = new TMapView(getContext());
        mMapView.setSKTMapApiKey(mApiKey);
        mMapView = new TMapView(getContext());
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


        //위치 검색
        b.searchBookmark.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                findAllPoi(b.searchBookmark.getText().toString());
                return true;
            }
        });
        //추가하기 버튼 클릭 시
        b.addBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                favoritesFragment fF = new favoritesFragment();
                transaction.replace(R.id.frameLayout, fF).commit();
                onDestroyView();
            }
        });
        //맵 + 버튼 클릭 시
        b.zoomPlusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapView.MapZoomIn();
            }
        });
        //맵 - 버튼 틀릭 시
        b.zoomMinusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapView.MapZoomOut();
            }
        });
        //뒤로가기 버튼
        b.btnBackFavoritesadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                favoritesFragment favoritesFragment = new favoritesFragment();
                transaction.replace(R.id.frameLayout, favoritesFragment).commit();
            }
        });


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
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
        // 롱 클릭 이벤트 설정
        mMapView.setOnLongClickListenerCallback(new TMapView.OnLongClickListenerCallback() {
            @Override
            public void onLongPressEvent(ArrayList arrayList, ArrayList arrayList1, TMapPoint tMapPoint) {
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
                //주소 글자만 추출
                String mail = list.get(0).toString();
                int idx = mail.indexOf(":");
                String mail1 = mail.substring(idx+1);
                idx = mail1.indexOf("]");
                String mail2 = mail1.substring(0,idx);
                String result = mail2.substring(mail2.indexOf("국")+1);
                idx = result.indexOf("\"");
                result = result.substring(0,idx);

                //주소 찾았는지 확인
                if (list != null) {
                    if (list.size()==0) {
                        Toast.makeText(getContext(), "주소를 못찾았습니다..", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
                        b.searchBookmark.setText(result);
                    }
                }
            }
        });


    }


    //zoom레벨을 설정한다
    public void setZoomLevel() {
        final String[] arrString = getResources().getStringArray(R.array.a_zoomlevel);
        AlertDialog dlg = new AlertDialog.Builder(getContext()).setIcon(R.drawable.tmark).setTitle("Select Zoom Level")
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
        Common.showAlertDialog(getContext(), "", "현재 Zoom Level : " + Integer.toString(nCurrentZoomLevel));
    }
    //키보드 숨기기
    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(b.editText.getWindowToken(), 0);
    }

    //현재 위치 버튼을 눌렀을때 현재 위치로 이동하고 포인트를 찍어주는 메소드
    /*public void onClickLocationBtn(View v) {
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
                Toast.makeText(getContext(), "주소를 찾지 못했습니다", Toast.LENGTH_LONG).show();
            } else { //주소를 찾을 경우
                Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();            }
        }
    }*/


    // setTrackingMode 화면중심을 단말의 현재위치로 이동시켜주는 트래킹모드로 설정한다.
    /*public void setTrackingMode(boolean isShow) {
        mMapView.setTrackingMode(isShow);
        if (isShow) {//gps 버튼을 켰을때
            mPermissionManager.request(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, new PermissionManager.PermissionListener() {
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
                    Toast.makeText(getContext(), "위치정보 수신에 동의하지 않으시면 현재위치로 이동할 수 없습니다.", Toast.LENGTH_SHORT).show();
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
    }*/

    //getLocationPoint 현재위치로 표시될 좌표의 위도, 경도를 반환한다.
    public void getLocationPoint() {
        TMapPoint point = mMapView.getLocationPoint();

        double Latitude = point.getLatitude();
        double Longitude = point.getLongitude();

        LogManager.printLog("Latitude " + Latitude + " Longitude " + Longitude);

        String strResult = String.format("Latitude = %f Longitude = %f", Latitude, Longitude);

        Common.showAlertDialog(getContext(), "", strResult);
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
    public void onPause() {
        super.onPause();
        // 화면 꺼지면 위치정보 수신 종료
        if (gps != null) {
            gps.CloseGps();
        }
        //b.locationBtn.setBackgroundResource(R.drawable.location_btn);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                d1 = mPoiItem.getPOIPoint().getLatitude(); // 위도
                d2 = mPoiItem.getPOIPoint().getLongitude(); // 경도
                b.searchBookmark.setText(mPoiItem.getPOIName()); //장소명을 출발지 검색창에 세팅

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
                case MESSAGE_STATE_ZOOM:
                    m_nCurrentZoomLevel = mMapView.getZoomLevel() - 3;
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
                    Toast.makeText(getContext(), "정보가 없습니다.", Toast.LENGTH_SHORT).show();
                case MESSAGE_STATE_APIKEY:
                    Toast.makeText(getContext(), "앱키 인증에 실패 했습니다.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}