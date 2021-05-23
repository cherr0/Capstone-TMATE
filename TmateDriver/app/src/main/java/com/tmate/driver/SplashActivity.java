package com.tmate.driver;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;

import com.daimajia.androidanimations.library.Techniques;
import com.pharid.splash.lib.activity.AnimatedSplash;
import com.pharid.splash.lib.cnst.Flags;
import com.pharid.splash.lib.model.ConfigSplash;
import com.tmate.driver.activity.LoginActivity;
import com.tmate.driver.activity.MainViewActivity;

public class SplashActivity extends AnimatedSplash {

    String imei = "";

    @SuppressLint("HardwareIds")
    @Override
    public void initSplash(ConfigSplash configSplash) {

        /* you don't have to override every property */
        imei = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        setPreference("d_id","d1010918400420");
        setPreference("d_approve","true");

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
        // sharedperference 값 있을 때 메인뷰로 이동
        if(!(getPreferenceString("d_id").equals("")) && !getPreferenceString("d_approve").equals("")) {
            Intent intent = new Intent(getApplication(), MainViewActivity.class);
            intent.putExtra("d_id", getPreferenceString("d_id"));
            startActivity(intent);
            finish();
        } else {
            Log.d("SplashActivity","LoginActivity 실행 중");
            Intent intent = new Intent(getApplication(), LoginActivity.class);
            intent.putExtra("m_imei",imei);
            startActivity(intent);
            finish();
        }
    }

    public String getPreferenceString(String key) {
        SharedPreferences pref = getSharedPreferences("loginDriver", MODE_PRIVATE);
        return pref.getString(key, "");
    }

    // 데이터 저장 함수
    public void setPreference(String key, String value){
        SharedPreferences pref = getSharedPreferences("loginDriver", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }
}