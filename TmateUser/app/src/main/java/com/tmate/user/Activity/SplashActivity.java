package com.tmate.user.Activity;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.provider.Settings;

import com.daimajia.androidanimations.library.Techniques;
import com.pharid.splash.lib.activity.AnimatedSplash;
import com.pharid.splash.lib.cnst.Flags;
import com.pharid.splash.lib.model.ConfigSplash;
import com.tmate.user.R;

public class SplashActivity extends AnimatedSplash {

    String imei = "";


    @SuppressLint("HardwareIds")
    @Override
    public void initSplash(ConfigSplash configSplash) {

        /* you don't have to override every property */


        imei = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

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
        Intent intent = new Intent(getApplication(), LoginActivity.class);
        intent.putExtra("m_imei", imei);
        startActivity(intent);
        SplashActivity.this.finish();
    }
}