package com.tmate.user.ui.driving;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;
import com.tmate.user.R;
import com.tmate.user.adapter.FavoritesAdapter;
import com.tmate.user.common.PermissionManager;
import com.tmate.user.data.FavoritesData;
import com.tmate.user.data.Place;
import com.tmate.user.databinding.FragmentSearchPlaceBinding;
import com.tmate.user.net.DataService;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPlaceFragment extends Fragment implements View.OnClickListener {

    private DrivingModel mViewModel;
    FragmentSearchPlaceBinding b;
    Geocoder geocoder;

    private TMapView mMapView; //맵 뷰

    SearchAdapter searchAdapter;
    FavoritesAdapter favoritesAdapter;
    HotPlaceAdapter placeAdapter;
    ArrayList<TMapPOIItem> searchList;
    ArrayList<FavoritesData> bookmarkList;
    ArrayList<Place> placeList;

    Call<List<FavoritesData>> bookmarkRequest;
    Call<List<Place>> placeRequest;

    // gps 및 경로 그리기 위한 변수들
    private boolean m_bTrackingMode = false; // geofencing type save
    private PermissionManager mPermissionManager; //권한 요청(GPS)
    private TMapGpsManager gps; //gps

    @Override
    public void onStart() {
        super.onStart();
        setGps(); //시작하자마자 자신의 위치가 보이게 한다

        NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        String at_status = mViewModel.dispatch.getAt_status();
        if(at_status != null) {
            if (at_status.equals("1")) { // 참여 상태 1(참가) 인 경우 배차 상태대로 이동
                switch (mViewModel.dispatch.getDp_status()) {
                    case "0":
                        controller.navigate(R.id.action_global_matchingDetailFragment);
                        break;
                    case "2":
                        controller.navigate(R.id.action_global_callWaitingFragment);
                        break;
                    case "3":
                        controller.navigate(R.id.action_global_driverWaitingFragment);
                        break;
                    case "4":
                        controller.navigate(R.id.action_global_driverMovingFragment);
                        break;
                }
            } else { // 참여 상태 0,1,2 인 경우 이동
                controller.navigate(R.id.action_global_matchingFragment);
            }
        }
    }

    @Override // onViewCreated 가 실행되기 전 레이아웃을 구성하기 위해 실행되는 생명주기 메서드
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(DrivingModel.class);
        b = FragmentSearchPlaceBinding.inflate(getLayoutInflater());
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapSetting(); // 맵 뷰에 필요한 설정 적용
        clickListenerApply(); // 클릭 리스너 활성화

        // 리사이클러 뷰 설정
        RecyclerView.LayoutManager searchManager = new LinearLayoutManager(getContext());
        b.searchList.setLayoutManager(searchManager);
        if(mViewModel.together.equals("3")) {
            placeList = new ArrayList<>();
            placeAdapter = new HotPlaceAdapter(placeList, mViewModel, b.finishPlace);
            b.searchList.setAdapter(placeAdapter);
        } else{
            searchList = new ArrayList<>();
            searchAdapter = new SearchAdapter(searchList, mViewModel, b.finishPlace);
            b.searchList.setAdapter(searchAdapter);
        }

        RecyclerView.LayoutManager bookmarkManager = new LinearLayoutManager(getContext());
        bookmarkList = new ArrayList<>();
        favoritesAdapter = new FavoritesAdapter(bookmarkList, mViewModel, b.finishPlace);
        b.favoriteList.setLayoutManager(bookmarkManager);
        b.favoriteList.setAdapter(favoritesAdapter);


    }

    /* -------------------
          온클릭 통합 관리
       ------------------- */
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.go_together_select : // 출발, 도착지 설정 완료 버튼
                selectedPlace();
                break;
            case R.id.location_btn : // 현재 위치 클릭
                onClickLocationBtn();
                break;
            case R.id.bookmark:
                getBookmarkList();
                break;
            case R.id.search_btn:
                searchListVisible();
                break;
        }
    }

    private void searchListVisible() {
        if(b.searchList.getVisibility() == View.GONE) {
            b.searchList.setVisibility(View.VISIBLE);
            b.favoriteList.setVisibility(View.GONE);
        }
    }

    // 클릭 리스너 적용
    private void clickListenerApply() {
        b.goTogetherSelect.setOnClickListener(this);
        b.locationBtn.setOnClickListener(this);
        b.finishPlace.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if(actionId == EditorInfo.IME_ACTION_DONE){
                Log.d("SearchPlaceFragment","Done.");
                hideKeyBoard();
                handled = true;
            }
            return handled;
        });

        b.finishPlace.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(b.searchList.getVisibility() == View.GONE) {
                    b.searchList.setVisibility(View.VISIBLE);
                    b.favoriteList.setVisibility(View.GONE);
                }
                if(mViewModel.together.equals("3")){
                    if(!placeList.isEmpty()) {
                        placeAdapter.clear();
                    }
                }else {
                    if(!searchList.isEmpty())
                        searchAdapter.clear();
                }

                String text = b.finishPlace.getText().toString();
                if(mViewModel.together.equals("3")){
                    placeRequest = DataService.getInstance().memberAPI.getPlaceList();
                    placeRequest.enqueue(new Callback<List<Place>>() {
                        @Override
                        public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                            if(response.code() == 200 && response.body() != null) {
                                Log.d("SearchPlaceFragment", "placeList : " + response.body());
                                for(Place data : response.body()) {
                                    String name = data.getPl_name();
                                    if(name.contains(text)) {
                                        Log.d("SearchPlaceFragment", "데이터 들어가는 중 : " + data);
                                        placeAdapter.addItem(data);
                                    }
                                    placeAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<List<Place>> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }else {
                    TMapPoint tPoint = new TMapPoint(mViewModel.dispatch.getStart_lat(), mViewModel.dispatch.getStart_lng());
                    TMapData mapData = new TMapData();
                    mapData.findAroundKeywordPOI(tPoint, text, 200, 50, arrayList -> {
                        Log.d("SearchPlaceFragment", "검색 리스트 : " + arrayList);
                        for(TMapPOIItem item : arrayList) {
                            searchAdapter.addItem(item);
                        }
                    });

                    searchAdapter.notifyDataSetChanged();
                }


            }
        });

        b.bookmark.setOnClickListener(this);
        b.searchBtn.setOnClickListener(this);
    }

    /* ------------------------
            지도 관련 메서드
       ------------------------ */
    // 맵뷰 시작 할 시 현위치 중심으로 보여지게 함
    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            if (location != null) { //자신의 위치정보를 가져온다
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                mMapView.setLocationPoint(longitude, latitude);
                mMapView.setCenterPoint(longitude, latitude);
            }
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };


    //자신의 위치로 보여주게한다 (시작 할 경우 사용)
    public void setGps() {
        final LocationManager lm =
                (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(
                getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자(실내에선 NETWORK_PROVIDER 권장)
                1000, // 통지사이의 최소 시간간격 (miliSecond)
                1, // 통지사이의 최소 변경거리 (m)
                mLocationListener);
    }

    // setTrackingMode 화면중심을 단말의 현재위치로 이동시켜주는 트래킹모드로 설정한다.
    public void setTrackingMode(boolean isShow) {
        mMapView.setTrackingMode(isShow);
        if (isShow) {//gps 버튼을 켰을때
            mPermissionManager.request(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, new PermissionManager.PermissionListener() {
                @Override
                public void granted() {
                    if (gps != null) {
                        gps.setMinTime(1000);
                        gps.setMinDistance(5);
                        gps.setProvider(gps.GPS_PROVIDER);
                        gps.setProvider(gps.NETWORK_PROVIDER);
                        gps.OpenGps();
                    }
                }
                @Override
                public void denied() {
                    Snackbar.make(mMapView, "위치정보 수신에 동의하지 않으시면 현재위치로 이동할 수 없습니다.", Snackbar.LENGTH_SHORT).show();
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

    //현재 위치 버튼을 눌렀을때 현재 위치로 이동하고 포인트를 찍어주는 메소드
    public void onClickLocationBtn() {
        m_bTrackingMode = !m_bTrackingMode;
        if (m_bTrackingMode) {
            mMapView.setIconVisibility(m_bTrackingMode);
        }
        setTrackingMode(m_bTrackingMode); //트래킹모드(화면 중심을 현재위치로 이동) 설정
        TMapPoint tpoint = mMapView.getCenterPoint();
        TMapData tMapData = new TMapData();
        tMapData.convertGpsToAddress(tpoint.getLatitude(), tpoint.getLongitude(),
                s -> {
                    Log.d("SearchPlaceFragment", "트래킹 모드 설정");
                    mViewModel.dispatch.setStart_place(s);
                    mViewModel.dispatch.setStart_lat(tpoint.getLatitude());
                    mViewModel.dispatch.setStart_lng(tpoint.getLongitude());
                    Log.d("SearchPlaceFragment","출발지 값 : " + s + ", " + tpoint.getLatitude() + "," + tpoint.getLongitude());
                    b.startPlace.setText(mViewModel.dispatch.getStart_place());
                });
    }

    private void mapSetting() {

        //gps
        gps = new TMapGpsManager(getActivity());
        mPermissionManager = new PermissionManager();
        geocoder = new Geocoder(getActivity());//지오코더

        //맵 화면에 띄우기
        mMapView = new TMapView(getActivity());
        mMapView.setSKTMapApiKey(DrivingModel.mApiKey);
        b.mapviewLayout.addView(mMapView);

        //맵 초기화면 설정
        mMapView.setZoomLevel(16);
        mMapView.setBufferStep(3);
        mMapView.setIconVisibility(true);//현재위치로 표시될 아이콘을 표시할지 여부를 설정

        // 나중에 맵 세팅 메서드에 추가
        // api 키 정상적인지 체크
        mMapView.setOnApiKeyListener(new TMapView.OnApiKeyListenerCallback() {
            @Override
            public void SKTMapApikeySucceed() {
                Log.d("SearchPlaceFragment","앱 키 인증 성공");
            }
            @Override // api 키가 비정상적일때 에러 메시지를 부른다
            public void SKTMapApikeyFailed(String errorMsg) {
                Log.d("SearchPlaceFragment","앱 키 인증 실패");
                Snackbar.make(mMapView , "앱키 인증에 실패 했습니다.", Snackbar.LENGTH_SHORT).show();
            }
        });

        //줌 스크롤 이벤트
        mMapView.setOnDisableScrollWithZoomLevelListener((zoom, centerPoint) -> {
            TMapData tMapData = new TMapData();
            tMapData.convertGpsToAddress(centerPoint.getLatitude(), centerPoint.getLongitude(),
                    s -> {
                        Log.d("SearchPlaceFragment","줌 스크롤 이벤트 실행");
                        mViewModel.dispatch.setStart_place(s);
                        mViewModel.dispatch.setStart_lat(centerPoint.getLatitude());
                        mViewModel.dispatch.setStart_lng(centerPoint.getLongitude());
                        Log.d("SearchPlaceFragment","출발지 값 : " + s + ", " + centerPoint.getLatitude() + "," + centerPoint.getLongitude());
                        b.startPlace.setText(mViewModel.dispatch.getStart_place());
                    });

        });

        b.cursor.bringToFront(); // 중심점 아이콘 최상단으로 올리기
    }


    /* ---------------------------
             가내수공업 메서드
      --------------------------- */
    //키보드 숨기기
    private void hideKeyBoard() {
        b.finishPlace.clearFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(b.startPlace.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(b.finishPlace.getWindowToken(), 0);
    }

    // perf 가져오기
    public String getPreferenceString(String key) {
        SharedPreferences pref = getActivity().getSharedPreferences("loginUser", getActivity().MODE_PRIVATE);
        return pref.getString(key, "");
    }

    // 위치 권한 요청
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionManager.setResponse(requestCode, grantResults);
    }

    // 출발지, 목적지 설정완료 버튼
    private void selectedPlace() {
        if(b.finishPlace.getText().toString().equals("")) {
            Snackbar.make(mMapView,"도착지를 작성해주세요",Snackbar.LENGTH_SHORT).show();
            return;
        }

        Log.d("SearchPlaceFragment","dispatch 데이터");
        Log.d("SearchPlaceFragment", mViewModel.dispatch.toString());
        Log.d("SearchPlaceFragment","출발지 : " + mViewModel.dispatch.getStart_place());
        Log.d("SearchPlaceFragment", "도착지 : " + mViewModel.dispatch.getFinish_place());
        Log.d("SearchPlaceFragment", "동승 유무 : " + mViewModel.together);

        if(mViewModel.together.equals("1")) { // 일반 탑승 시
            NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            controller.navigate(R.id.action_searchPlace_to_paymentInformationFragment);
        } else { // 동승 호출 시 -> 매칭 리스트로 넘어간다.
            NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            controller.navigate(R.id.action_searchPlace_to_matchingFragment);
        }

    }

    // 즐겨찾기 가져오기
    private void getBookmarkList() {
        bookmarkList.clear();
        bookmarkRequest = DataService.getInstance().memberAPI.getBookmarkList(getPreferenceString("m_id"));
        bookmarkRequest.enqueue(new Callback<List<FavoritesData>>() {
            @Override
            public void onResponse(Call<List<FavoritesData>> call, Response<List<FavoritesData>> response) {
                if(response.code() == 200 && response.body() != null) {
                    bookmarkList.addAll(response.body());
                    favoritesAdapter.notifyDataSetChanged();
                    b.favoriteList.setVisibility(View.VISIBLE);
                    b.searchList.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<List<FavoritesData>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        // 화면 꺼지면 위치정보 수신 종료
        if (gps != null) {
            gps.CloseGps();
        }
        b.locationBtn.setBackgroundResource(R.drawable.location_btn);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(bookmarkRequest != null) bookmarkRequest.cancel();
        if(placeRequest != null) placeRequest.cancel();
    }
}