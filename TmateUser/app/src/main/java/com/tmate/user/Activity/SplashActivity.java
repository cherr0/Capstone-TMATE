package com.tmate.user.Activity;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;

import com.daimajia.androidanimations.library.Techniques;
import com.pharid.splash.lib.activity.AnimatedSplash;
import com.pharid.splash.lib.cnst.Flags;
import com.pharid.splash.lib.model.ConfigSplash;
import com.tmate.user.R;
import com.tmate.user.net.DataService;

import java.sql.Timestamp;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AnimatedSplash {

    String imei = "";

    Call<String> getBirthRequest;

    @SuppressLint("HardwareIds")
    @Override
    public void initSplash(ConfigSplash configSplash) {

        /* you don't have to override every property */

        imei = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

//        setPreference("m_id", "m1010918400420");
//        setPreference("m_name", "하창현");
//        setPreference("sid", "S2905462887021313232");

        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.white); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(2000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_LEFT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_TOP); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.logo); //or any other drawable
        configSplash.setAnimLogoSplashDuration(2000); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeIn); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)


//        Customize Title


    }

    @Override
    public void animationsFinished() {
        Log.d("SplashActivity", "m_id : " + getPreferenceString("m_id"));

        // sharedpreference 값 있을떄 바로 메인뷰로 간다.
        if (!(getPreferenceString("m_id").equals(""))) {
            Intent intent = new Intent(this, MainViewActivity.class);
            intent.putExtra("m_id", getPreferenceString("m_id"));
            storeGenderByUser();
            storeAgeByUser();
            Log.d("찍히는 성별", getPreferenceString("gender"));
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(getApplication(), LoginActivity.class);
            intent.putExtra("m_imei", imei);
            startActivity(intent);
            finish();
        }

    }

    // 데이터 저장 함수
    public void setPreference(String key, String value){
        SharedPreferences pref = getSharedPreferences("loginUser", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getPreferenceString(String key) {
        SharedPreferences pref = getSharedPreferences("loginUser", MODE_PRIVATE);
        return pref.getString(key, "");
    }
    
    // 성별 SharedPreference 작업
    public void storeGenderByUser() {
        switch (getPreferenceString("m_id").substring(1, 2)) {
            case "1":
            case "3":
                setPreference("gender","남");
                break;
            case "2":
            case "4":
                setPreference("gender","여");
                break;
        }
    }
    
    // 생년 월일 작업
    public void storeAgeByUser() {
        getBirthRequest = DataService.getInstance().memberAPI.getBirth(getPreferenceString("m_id"));
        getBirthRequest.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200) {
                    int year = Calendar.getInstance().get(Calendar.YEAR);
                    Log.d("넘어오는 회원 생년월일", String.valueOf(response.body()));
                    Log.d("현재 년도 ", String.valueOf(year).substring(2));
                    int age = (year-2000) - Integer.parseInt(response.body());
                    if (age < 0)
                        age += 100;
                    Log.d("현재 이분의 나이는 : ", String.valueOf(age));

                    switch (age / 10) {
                        case 1:
                            setPreference("age","10대");
                            break;
                        case 2:
                            setPreference("age","20대");
                            break;
                        case 3:
                            setPreference("age","30대");
                            break;
                        case 4:
                            setPreference("age","40대");
                            break;
                        case 5:
                            setPreference("age","50대");
                            break;
                        case 6:
                            setPreference("age","60대");
                            break;
                        case 7:
                            setPreference("age","70대");
                            break;
                        default:
                            setPreference("age","기타");
                            break;
                    }

                    Log.d("이분의 나이대는? ", getPreferenceString("age"));

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}