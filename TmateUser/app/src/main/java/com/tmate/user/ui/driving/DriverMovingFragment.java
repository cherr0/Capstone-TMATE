package com.tmate.user.ui.driving;

import static com.skt.Tmap.util.HttpConnect.getContentFromNode;

import android.location.Geocoder;
import android.location.Location;
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
import com.tmate.user.data.SubscriptionRes;
import com.tmate.user.databinding.FragmentDriverMovingBinding;
import com.tmate.user.net.DataService;
import com.tmate.user.net.KakaoService;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    String dp_id; // 배차 코드

    Handler handler;
    Positioning positioning;

    Call<Dispatch> getDriverRequest;
    Call<SubscriptionRes> subscriptionRequest;
    boolean isRunning;



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
        modelDataBinding();

        // 인텐트 dp_id 값 받아보고 없다면 이전 단계 거쳐서 온 것이기에 mViewModel 값 사용
        dp_id = getActivity().getIntent().getStringExtra("dp_id");
        if(dp_id == null) {
            dp_id = mViewModel.dispatch.getDp_id();
        }
        Log.d("DriverWaitingFragment","dp_id : " + dp_id);

        // 쓰레드 상태
        handler = new Handler();
        positioning = new Positioning();
        isRunning = true;

        Thread thread = new Thread(() -> {
            try {
                while (isRunning) {
                    handler.post(positioning);
                    Thread.sleep(2000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        isRunning = true;
        thread.start();
    }

    //권한 요청
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionManager.setResponse(requestCode, grantResults);
    }

    /* ---------------------
            View 관련 메서드
       --------------------- */
    private void modelDataBinding() {
        b.moveStartPlace.setText(mViewModel.dispatch.getStart_place());
        b.moveFinishPlace.setText(mViewModel.dispatch.getFinish_place());

        if(mViewModel.together.equals("1")) {
            Log.d("DriverMovingFragment","배차 정보 : " + mViewModel.dispatch.toString());
            b.amount.setText(String.valueOf(mViewModel.dispatch.getAll_fare()));
        }else {
//            b.amount.setText();
            /*
                추후 동승 시 attend 가져와서 setText
             */
        }
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

    /*
        카카오 결제 관련
     */

    // 카카오 정기결제 진행
    private void kakaoSubscription(Dispatch dispatch) {
        Map<String,String> map = new HashMap<>();

        map.put("cid","TCSUBSCRIP");
        map.put("sid", dispatch.getSid());
        map.put("partner_order_id", dispatch.getD_id());
        map.put("partner_user_id", dispatch.getM_id());
        map.put("item_name","택시 운임 결제");
        map.put("quantity","1");
        map.put("total_amount",String.valueOf(dispatch.getAmount()));
        map.put("vat_amount","0");
        map.put("tax_free_amount","0");

        Log.d("payInfoFragemnt","map 전달 내용 : " + map);

        subscriptionRequest = KakaoService.getInstance().getApi().kakaoSubscription(DrivingModel.auth, map);
        subscriptionRequest.enqueue(new Callback<SubscriptionRes>() {
            @Override
            public void onResponse(Call<SubscriptionRes> call, Response<SubscriptionRes> response) {
                if(response.code() == 200 && response.body() != null) {
                    SubscriptionRes result = response.body();
                    Log.d("payInfoFragemnt", "받아오는 값 :" + result);

                }else {
                    try {
                        Log.d("payInfoFragemnt", "에러 : " + response);
                        assert response.errorBody() != null;
                        Log.d("payInfoFragemnt", "데이터 삽입 실패 : " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SubscriptionRes> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(getDriverRequest != null) getDriverRequest.cancel();
        if(subscriptionRequest != null) subscriptionRequest.cancel();
    }

    // 택시 기사 위치 실시간으로 가져오는 내부 쓰레드 클래스
    public class Positioning implements Runnable {
        @Override
        public void run() {
            getDriverRequest = DataService.getInstance().matchAPI.getDriverPosition(dp_id);
            getDriverRequest.enqueue(new Callback<Dispatch>() {
                @Override
                public void onResponse(Call<Dispatch> call, Response<Dispatch> response) {
                    if (response.code() == 200 && response.body() != null) {
                        Dispatch dispatch = response.body();
                        Log.d("넘어오는 기사 정보", dispatch.toString());
                        Snackbar.make(mMapView, "계속하여 기사 위치를 가져옵니다.", Snackbar.LENGTH_SHORT).show();
                        tMapPointStart = new TMapPoint(dispatch.getM_lat(), dispatch.getM_lng());

                        switch (dispatch.getDp_status()) {
                            case "4": // 운행 중
                                tMapPointEnd = new TMapPoint(dispatch.getFinish_lat(), dispatch.getFinish_lng());
                                drawCarPath();
                                isRunning = true;
                                break;
                            case "5":
                                isRunning = false;
                                // 탑승완료 될 경우 다음 레이아웃으로 이동
                                /*
                                    결제 관련 값 받아오는 api 작성해서 메서드 실행
                                 */
//                                kakaoSubscription(mViewModel.dispatch);
                                NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                                controller.navigate(R.id.action_driverMovingFragment_to_driverFinishingFragment);
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
}