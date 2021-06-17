package com.tmate.user.ui.driving;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.skt.Tmap.util.HttpConnect.getContentFromNode;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.tmate.user.Activity.MainViewActivity;
import com.tmate.user.R;
import com.tmate.user.common.PermissionManager;
import com.tmate.user.data.Dispatch;
import com.tmate.user.data.PointData;
import com.tmate.user.data.SubscriptionRes;
import com.tmate.user.databinding.FragmentDriverWaitingBinding;
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
    Call<SubscriptionRes> subscriptionRequest;
    boolean isRunning;

    // 포인트 사용 부분 Request
    Call<Boolean> insertUsePointRequest;

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(DrivingModel.class);
        b = FragmentDriverWaitingBinding.inflate(getLayoutInflater());
        geocoder = new Geocoder(getActivity());//지오코더
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clickListenerApply();

        // 인텐트 dp_id 값 받아보고 없다면 이전 단계 거쳐서 온 것이기에 mViewModel 값 사용
        dp_id = mViewModel.dispatch.getDp_id();
        Log.d("DriverWaitingFragment","dp_id : " + dp_id);
        dispatchRequest(dp_id);

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
                            e.printStackTrace();
                        }
                    }
                }
                //줌 설정
                TMapInfo info = mMapView.getDisplayTMapInfo(polyline.getLinePoint());
//                int zoom = info.getTMapZoomLevel();
//                if (Integer.parseInt(totalDistance) > 1500) {
//                    zoom = 14;
//                }else if (Integer.parseInt(totalDistance) > 1000) {
//                    zoom = 16;
//                }
                
                mMapView.addTMapPath(polyline);
//                mMapView.setZoomLevel(zoom);
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
                    Dispatch dispatch = response.body();
                    mViewModel.dispatch = dispatch;
                    Log.d("DriverWaitingFragment","가져온 배차 정보 : " + dispatch.toString());
                    String d_id = mViewModel.dispatch.getD_id();
                    driverPhoneNo = d_id.substring(2, 5) + "-" + d_id.substring(5,9) + "-" + d_id.substring(9,13);
                    tMapPointStart = new TMapPoint(dispatch.getM_lat(), dispatch.getM_lng());
                    tMapPointEnd = new TMapPoint(dispatch.getStart_lat(), dispatch.getStart_lng());
                    modelDataBinding();
                    mapSetting(); // 지도 옵션 셋팅 및 활성화
                }
            }
            @Override
            public void onFailure(Call<Dispatch> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    // 카카오 정기결제 진행
    private void kakaoSubscription(Dispatch dispatch) {
        Map<String,String> map = new HashMap<>();

        map.put("cid","TCSUBSCRIP");
        // 결제 수단
        map.put("sid", getPreferenceString("sid")); // 이후 결제 정보 값 받아올 수 있으면 적용
        // 기사 코드
        map.put("partner_order_id", dispatch.getD_id());
        // 돈내는 사람
        map.put("partner_user_id", getPreferenceString("m_id"));
        map.put("item_name","택시 기본 요금 선결제");
        map.put("quantity","1");
        // 조건문 처리 -> 동승 1/n , 일반은 그대로
        together = mViewModel.together;
        int payment= 3300 / Integer.parseInt(together);
        mViewModel.payCash = payment;
        map.put("total_amount",String.valueOf(payment));
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
                    mViewModel.use_cash = payment;
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

    /* -----------------------
            가내수공업 메서드
       ----------------------- */
    // 뷰에 데이터 연결
    private void modelDataBinding() {
        b.carNo.setText(mViewModel.dispatch.getCar_no()); // 기사 차 번호
        b.infoStartPlace.setText(mViewModel.dispatch.getStart_place()); // 출발지
        b.infoFinishPlace.setText(mViewModel.dispatch.getFinish_place()); // 도착지

        b.infoTogether.setText(mViewModel.together); // 현재 인원
        b.maxPeople.setText(mViewModel.dispatch.getDp_id().substring(18)); // 최대 인원

    }

    // 클릭 리스너 관리
    private void clickListenerApply() {
        b.driverCall.setOnClickListener(v -> {
            Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+driverPhoneNo));
            startActivity(mIntent.addFlags(FLAG_ACTIVITY_NEW_TASK));
        });
    }

    public String getPreferenceString(String key) {
        SharedPreferences pref = getActivity().getSharedPreferences("loginUser", getActivity().MODE_PRIVATE);
        return pref.getString(key, "");
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
                        Log.d("DriverWaitingFragment", "넘어오는 기사 위치 정보 : " + dispatch.toString());

                        switch (dispatch.getDp_status()) {
                            case "3" : // 탑승 대기 중
                                tMapPointStart  = new TMapPoint(dispatch.getM_lat(), dispatch.getM_lng());
                                drawCarPath();
                                Log.d("DriverWaitingFragment", "맵 그리는 중");
                                b.dpStatus.setText("탑승 대기중");
                                isRunning = true;
                                break;
                            case "4": // 탑승 완료
                                isRunning = false;
                                // 탑승완료 될 경우 다음 레이아웃으로 이동
                                kakaoSubscription(mViewModel.dispatch);
                                insertUsePoint();
                                NavController controller = Navigation.findNavController(activity, R.id.nav_host_fragment);
                                controller.navigate(R.id.action_driverWaitingFragment_to_driverMovingFragment);
                                break;
                            case "6":
                                isRunning = false;
                                // 노쇼인 경우 MainViewActivity 이동
                                Intent intent = new Intent(activity, MainViewActivity.class);
                                startActivity(intent);
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

    // 사용한 포인트 DB 연동
    public void insertUsePoint() {
        PointData pointData = new PointData();
        pointData.setM_id(getPreferenceString("m_id"));
        pointData.setPo_course("포인트사용");
        pointData.setPo_exact("1");
        pointData.setPo_result(mViewModel.use_point);

        insertUsePointRequest = DataService.getInstance().memberAPI.registerPoint(pointData);
        insertUsePointRequest.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.code() == 200) {
                    Toast.makeText(getContext(), mViewModel.use_point+"P 가 사용되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(curDispatchRequest != null) curDispatchRequest.cancel();
        if(getDriverRequest != null) getDriverRequest.cancel();
        if(subscriptionRequest != null) subscriptionRequest.cancel();
        if(isRunning) isRunning = false;
    }
}