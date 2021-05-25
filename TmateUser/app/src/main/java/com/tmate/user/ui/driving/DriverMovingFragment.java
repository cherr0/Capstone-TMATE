package com.tmate.user.ui.driving;

import static com.skt.Tmap.util.HttpConnect.getContentFromNode;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapInfo;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;
import com.tmate.user.Activity.CarDrivingActivity;
import com.tmate.user.R;
import com.tmate.user.common.PermissionManager;
import com.tmate.user.databinding.FragmentDriverMovingBinding;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DriverMovingFragment extends Fragment implements TMapGpsManager.onLocationChangedCallback {

    FragmentDriverMovingBinding b;
    private DrivingModel mViewModel;


    //DB에 들어갈 거리와 시간에 대한 변수들
    private String totalDistance = null;//총 거리
    private String totalTime = null; // 총 시간

    // 지도 관련 변수
    private TMapView mMapView = null; //맵 뷰
    private boolean m_bTrackingMode = false; // geofencing type save
    PermissionManager mPermissionManager = null; //권한 요청(GPS)
    TMapGpsManager gps = null; //gps
    Geocoder geocoder;
    TMapPoint tMapPointStart; //출발지 위,경도를 담은 좌표
    TMapPoint tMapPointEnd; //도착지 위,경도를 담은 좌표




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentDriverMovingBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(requireActivity()).get(DrivingModel.class);
        geocoder = new Geocoder(getActivity());//지오코더
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapSetting();
    }

    //권한 요청
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionManager.setResponse(requestCode, grantResults);
    }


    /* ------------------------
          지도 관련 메서드
      ------------------------- */
    // 바뀐 위치에 대한 좌표값 설정
    @Override
    public void onLocationChange(Location location) {
        if (m_bTrackingMode) {
            mMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
            mMapView.setIconVisibility(m_bTrackingMode);
        }
    }

    private void mapSetting() {
        //gps
        gps = new TMapGpsManager(getActivity());
        mPermissionManager = new PermissionManager();
        //맵 화면에 띄우기
        mMapView = new TMapView(getActivity());
        mMapView.setSKTMapApiKey(DrivingModel.mApiKey);
        b.mapviewLayout.addView(mMapView);
    }

    //자동차 경로 그리기
    public void drawCarPath() {
        findPathDataAllType(TMapData.TMapPathType.CAR_PATH);//자동차경로 그리기 호출
    }
    //자동차 경로 그리기
    private void findPathDataAllType(final TMapData.TMapPathType type) {
        totalDistance = null; // 총 거리(km)
        totalTime = null; // 총 시간

        TMapPoint point1 = tMapPointStart; //출발지 좌표 설정
        TMapPoint point2 = tMapPointEnd; //도착지 좌표 설정

        TMapData tmapdata = new TMapData();

        tmapdata.findPathDataAllType(type, point1, point2, doc -> {
            TMapPolyLine polyline = new TMapPolyLine();
            polyline.setLineWidth(10);
            if (doc != null) {
                NodeList list = doc.getElementsByTagName("Document");
                Element item2 = (Element) list.item(0);
                totalDistance = getContentFromNode(item2, "tmap:totalDistance"); //총 거리설정
                Log.d("DriverMovingFragment", "총 거리 : " + totalDistance);
                totalTime = getContentFromNode(item2, "tmap:totalTime"); //총 시간 설정
                Log.d("DriverMovingFragment", "총 시간 : " + totalTime);

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
                mMapView.addTMapPath(polyline);
                mMapView.setZoomLevel(zoom);
                mMapView.setCenterPoint(info.getTMapPoint().getLongitude(), info.getTMapPoint().getLatitude());
            }

        });
    }
}