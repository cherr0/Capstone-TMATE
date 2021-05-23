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

    public int together = 0; //동승 설정

    //DB에 들어갈 거리와 시간에 대한 변수들
    public Dispatch dispatch;

    public MutableLiveData<String> start = new MutableLiveData<>();
}
