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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
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
import com.tmate.user.Activity.CallWaitingActivity;
import com.tmate.user.R;
import com.tmate.user.common.PermissionManager;
import com.tmate.user.data.Dispatch;
import com.tmate.user.databinding.FragmentDriverWaitingBinding;
import com.tmate.user.net.DataService;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverWaitingFragment extends Fragment implements TMapGpsManager.onLocationChangedCallback {

    FragmentDriverWaitingBinding b;
    DrivingModel mViewModel;

    String dp_id;

    // 지도 관련 변수
    TMapPoint tMapPointStart;//출발지 위,경도를 담은 좌표
    TMapPoint tMapPointEnd;//도착지 위,경도를 담은 좌표
    private TMapView mMapView; //맵 뷰

    //DB에 들어갈 거리와 시간에 대한 변수들
    private String totalDistance; // 총 거리
    private String totalTime; // 총 시간

    //gps 및 경로 그리기 위한 변수들
    private boolean m_bTrackingMode = false; // geofencing type save
    Geocoder geocoder;
    PermissionManager mPermissionManager; //권한 요청(GPS)
    TMapGpsManager gps; //gps

    Call<Dispatch> curDispatchRequest;

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(DrivingModel.class);
        b = FragmentDriverWaitingBinding.inflate(getLayoutInflater());
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapSetting(); // 지도 옵션 셋팅 및 활성화
        clickListenerApply();

        // 인텐트 dp_id 값 받아보고 없다면 이전 단계 거쳐서 온 것이기에 mViewModel 값 사용
        dp_id = getActivity().getIntent().getStringExtra("dp_id");
        if(dp_id == null) {
            dp_id = mViewModel.dispatch.getDp_id();
        }
        Log.d("DriverWaitingFragment","dp_id : " + dp_id);
        dispatchRequest(dp_id);
        modelDataBinding();
        drawCarPath(); // 자동차 경로 그리기
    }

    @Override
    public void onLocationChange(Location location) {
        if (m_bTrackingMode) {
            mMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
            mMapView.setIconVisibility(m_bTrackingMode);
        }
    }


    /* ----------------------
            지도 관련 메서드
       ---------------------- */
    private void mapSetting() {
        //gps
        gps = new TMapGpsManager(getActivity());
        mPermissionManager = new PermissionManager();
        geocoder = new Geocoder(getActivity());//지오코더

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
                int zoom = info.getTMapZoomLevel();
                if (zoom > 12) {
                    zoom = 12;
                }
                mMapView.addTMapPath(polyline);
                mMapView.setZoomLevel(zoom);
                mMapView.setCenterPoint(info.getTMapPoint().getLongitude(), info.getTMapPoint().getLatitude());

                // 자동차 경로 그리기
                new Thread(() -> {
                    b.geocodingLayout.setVisibility(View.GONE);

                    int totalSec = Integer.parseInt(totalTime);
                    int day = totalSec / (60 * 60 * 24);
                    int hour = (totalSec - day * 60 * 60 * 24) / (60 * 60);
                    int minute = (totalSec - day * 60 * 60 * 24 - hour * 3600) / 60;

                    String time;
                    if (hour > 0) {
                        time = hour + "시간 " + minute + "분";
                        b.arriveTime.setText(time);
                    } else {
                        time = minute + "분 ";
                        b.arriveTime.setText(time);
                    }
                    double km = Double.parseDouble(totalDistance) / 1000; // 거리 (km기준)
                    /*
                        거리도 필요할 시 나중에 로직 추가
                     */
                }).start();
            }
        });
    }

    /* ------------------------
         레트로핏 사용 메서드
       ------------------------ */
    private void dispatchRequest(String dp_id) {
        curDispatchRequest = DataService.getInstance().matchAPI.readCurrentDispatch(dp_id);
        curDispatchRequest.enqueue(new Callback<Dispatch>() {
            @Override
            public void onResponse(Call<Dispatch> call, Response<Dispatch> response) {
                if(response.code() == 200 && response.body() != null) {
                    mViewModel.dispatch = response.body();
                    mViewModel.together = Integer.parseInt(mViewModel.dispatch.getDp_id().substring(18));
                    tMapPointStart = new TMapPoint(mViewModel.dispatch.getStart_lat(),mViewModel.dispatch.getStart_lng());
                    tMapPointEnd = new TMapPoint(mViewModel.dispatch.getFinish_lat(), mViewModel.dispatch.getFinish_lng());
                }
            }
            @Override
            public void onFailure(Call<Dispatch> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /* -----------------------
            가내수공업 메서드
       ----------------------- */
    // 뷰에 데이터 연결
    private void modelDataBinding() {
        b.carNo.setText(mViewModel.dispatch.getCar_no()); // 기사 차 번호
        b.infoStartPlace.setText(mViewModel.dispatch.getStart_place()); // 출발지
        b.infoFinishPlace.setText(mViewModel.dispatch.getFinish_place()); // 도착지

        // 탑승인원
        b.infoTogether.setText(mViewModel.together);

        // 탑승상태
        switch (mViewModel.dispatch.getDp_status()) {
            case "3" : b.dpStatus.setText("탑승 대기중"); break;
            case "4" : b.dpStatus.setText("탑승중"); break;
            case "5" :
                // 탑승완료 될 경우 다음 레이아웃으로 이동
                NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                controller.navigate(R.id.action_driverWaitingFragment_to_driverMovingFragment);
        }
    }

    // 클릭 리스너 관리
    private void clickListenerApply() {
        b.complete.setOnClickListener(v -> {
            NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            controller.navigate(R.id.action_driverWaitingFragment_to_driverMovingFragment);
        });
    }
}