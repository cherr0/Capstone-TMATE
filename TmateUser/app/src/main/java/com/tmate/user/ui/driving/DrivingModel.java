package com.tmate.user.ui.driving;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.tmate.user.common.PermissionManager;
import com.tmate.user.data.Dispatch;

import java.util.ArrayList;
import java.util.List;

public class DrivingModel extends ViewModel {
    public static final String mApiKey = "l7xx4fac78a5b9bf445db00bb99ae2708cee";
    public static final String auth = "KakaoAK e24eec29f82748733f7a2be2de93c236";

    public String together; //동승 설정
    public int use_point; // 사용 포인트
    public int use_cash = 0; // 현금 결제 유무

    //DB에 들어갈 거리와 시간에 대한 변수들
    public Dispatch dispatch;
    public String pl_id;

    public int payFlag = 1;  // 1: 성공, 2: 오류, 3: 취소
    public int payCash; // 지불 금액
}
