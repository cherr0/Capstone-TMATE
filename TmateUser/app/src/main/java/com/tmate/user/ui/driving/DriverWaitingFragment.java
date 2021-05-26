package com.tmate.user.ui.driving;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.skt.Tmap.util.HttpConnect.getContentFromNode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapInfo;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;
import com.tmate.user.R;
import com.tmate.user.common.PermissionManager;
import com.tmate.user.data.Dispatch;
import com.tmate.user.databinding.FragmentDriverWaitingBinding;
import com.tmate.user.net.DataService;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverWaitingFragment extends Fragment implements TMapGpsManager.onLocationChangedCallback {

    FragmentDriverWaitingBinding b;
    DrivingModel mViewModel;

    String dp_id; // 배차 코드
    String driverPhoneNo; // 기사 번호
    String together; // 동승인원

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


    Handler handler;
    Positioning positioning;

    Call<Dispatch> curDispatchRequest; // 상세 이용정보 가져오기
    Call<Dispatch> getDriverRequest; // 기사 위치 가져오는 메서드
    boolean isRunning;

    
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

        // 쓰레드 상태
        handler = new Handler();
        positioning = new Positioning(requireActivity());
        isRunning = true;

        Thread thread = new Thread(() -> {
            try {
                while (isRunning) {
                    handler.post(positioning);
                    b.geocodingLayout.setVisibility(View.GONE);
                    Thread.sleep(2000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        isRunning = true;
        thread.start();
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
                            e.printStackTrace();
                        }
                    }

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
                    Toast.makeText(getActivity(), "스레드 실행 중", Toast.LENGTH_SHORT).show();
                }
                //줌 설정
                TMapInfo info = mMapView.getDisplayTMapInfo(polyline.getLinePoint());
                mMapView.addTMapPath(polyline);
                mMapView.setZoomLevel(16);
                mMapView.setCenterPoint(info.getTMapPoint().getLongitude(), info.getTMapPoint().getLatitude());
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
                    Log.d("DriverWaitingFragment","가져온 배차 정보 : " + mViewModel.dispatch.toString());
                    mViewModel.together = mViewModel.dispatch.getDp_id().substring(18);
                    String d_id = mViewModel.dispatch.getD_id();
                    driverPhoneNo = d_id.substring(2, 5) + "-" + d_id.substring(5,9) + "-" + d_id.substring(9,13);
                    together = mViewModel.dispatch.getDp_id().substring(18);
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

        // 만나는 시간
        if(mViewModel.together.equals("1")) {
            b.meetTime.setText("동승 시 이용");
        }else {
            b.meetTime.setText(mViewModel.dispatch.getMeet_time().toString());
        }

    }

    // 클릭 리스너 관리
    private void clickListenerApply() {
        b.driverCall.setOnClickListener(v -> {
            Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+driverPhoneNo));
            startActivity(mIntent.addFlags(FLAG_ACTIVITY_NEW_TASK));
        });
    }

    // 택시 기사 위치 실시간으로 가져오는 내부 쓰레드 클래스
    public class Positioning implements Runnable {

        Activity activity;

        public Positioning(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void run() {
            getDriverRequest = DataService.getInstance().matchAPI.getDriverPosition(dp_id);
            getDriverRequest.enqueue(new Callback<Dispatch>() {
                @Override
                public void onResponse(Call<Dispatch> call, Response<Dispatch> response) {
                    if (response.code() == 200 && response.body() != null) {
                        Dispatch dispatch = response.body();
                        Log.d("DriverWaitingFragment", "넘어오는 기사 위치 정보" + dispatch.toString());
                        Snackbar.make(mMapView, "계속하여 기사 위치를 가져옵니다.", Snackbar.LENGTH_SHORT).show();
                        tMapPointStart = new TMapPoint(dispatch.getM_lat(), dispatch.getM_lng());

                        switch (dispatch.getDp_status()) {
                            case "3": // 탑승 대기 중
                                tMapPointEnd = new TMapPoint(dispatch.getStart_lat(), dispatch.getStart_lng());
                                b.dpStatus.setText("탑승 대기중");
                                drawCarPath();
                                isRunning = true;
                                break;
                            case "4": // 탑승 완료
                                isRunning = false;
                                // 탑승완료 될 경우 다음 레이아웃으로 이동
                                NavController controller = Navigation.findNavController(activity, R.id.nav_host_fragment);
                                controller.navigate(R.id.action_driverWaitingFragment_to_driverMovingFragment);
                                /*
                                    운행 중으로 넘어가기전 선결제 진행
                                 */
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(curDispatchRequest != null) curDispatchRequest.cancel();
        if(getDriverRequest != null) getDriverRequest.cancel();
    }
}