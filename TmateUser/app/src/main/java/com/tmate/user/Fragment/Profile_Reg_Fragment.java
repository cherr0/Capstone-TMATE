package com.tmate.user.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.tmate.user.Activity.MainViewActivity;
import com.tmate.user.R;
import com.tmate.user.net.DataService;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Profile_Reg_Fragment extends Fragment {

    private static final String LOG_TAG = "회원정보";

    DataService dataService = DataService.getInstance();
    private Button btn_submit;
    private TextView tv_house;
    private EditText et_email;
    Map<String, String> map = new HashMap<>();
    private WebView wv_adress;
    private Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.fragment_profile__reg_, container, false);

        tv_house = (TextView) rootview.findViewById(R.id.tv_house);
        et_email = (EditText) rootview.findViewById(R.id.et_email);

        tv_house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // WebView 설정
                wv_adress = rootview.findViewById(R.id.wv_adress);
                wv_adress.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.GONE);
                // WebView 초기화
                init_webView();
                // 핸들러를 통한 JavaScript 이벤트 반응
                handler = new Handler();


            }
        });


        if (getArguments() != null) {
            map.put("m_id", getArguments().getString("m_id"));
            map.put("m_name", getArguments().getString("m_name"));
            map.put("m_birth", getArguments().getString("m_birth"));
            map.put("m_imei", getArguments().getString("m_imei"));
        }


        btn_submit = rootview.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tv_house != null) {
                    map.put("m_house", tv_house.getText().toString());
                }

                if (et_email != null) {
                    map.put("m_email", et_email.getText().toString());
                }

                Log.d(LOG_TAG, map.values().toString());

                dataService.memberAPI.insertOne(map).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(@NonNull  Call<Boolean> call, @NonNull Response<Boolean> response) {
                        if (response.isSuccessful()) {
                            Log.d("response", response.body().toString());
                            Intent intent = new Intent(getActivity(), MainViewActivity.class);
                            intent.putExtra("m_id", map.get("m_id"));
                            intent.putExtra("m_name", map.get("m_name"));
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<Boolean> call, Throwable t) {
                        t.printStackTrace();
                    }
                });


            }
        });
        return rootview;
    }
    public void init_webView() {
        // JavaScript 허용
        wv_adress.getSettings().setJavaScriptEnabled(true);
        // JavaScript의 window.open 허용
        wv_adress.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        wv_adress.addJavascriptInterface(new AndroidBridge(), "TestApp");
        // web client 를 chrome 으로 설정
        wv_adress.setWebChromeClient(new WebChromeClient());
        // webview url load. php 파일 주소
        wv_adress.loadUrl("http://tmate777.dothome.co.kr/");
    }
    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    tv_house.setText(String.format("(%s) %s %s", arg1, arg2, arg3));
                    // WebView를 초기화 하지않으면 재사용할 수 없음
                    init_webView();
                    wv_adress.setVisibility(View.GONE);
                    btn_submit.setVisibility(View.VISIBLE);

                }
            });
        }
    }

}
