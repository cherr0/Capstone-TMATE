package com.tmate.user.net;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.common.api.Api;
import com.tmate.user.data.PaymentRes;
import com.tmate.user.databinding.ActivityKakaopayWebviewBinding;

import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class KakaopayWebviewActivity extends AppCompatActivity {

    ActivityKakaopayWebviewBinding b;
    final String auth = "KakaoAK e24eec29f82748733f7a2be2de93c236";

    Call<PaymentRes> readyRequest;
    Call<Boolean> insertRequest;
    HashMap<String,String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityKakaopayWebviewBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        map = requestBinding(); // Request 값 추가

        /* ------------------------------
                  Web View Settings
          ------------------------------ */
        b.kakaopayWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        b.kakaopayWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        WebSettings settings = b.kakaopayWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);    // 자바스크립트에서 새 창 실행

        settings.setSupportMultipleWindows(false);      //새 창 실행

        settings.setSupportZoom(false);                 //화면 줌
        settings.setBuiltInZoomControls(false);         //화면 확대 및 축소

        settings.setSaveFormData(true);
        b.kakaopayWebView.setHorizontalScrollBarEnabled(false);//가로 스크롤 방지
        b.kakaopayWebView.setWebChromeClient(new WebChromeClient());
        b.kakaopayWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 인텐트로 시작할 경우
                if (url != null && url.startsWith("intent://")) {
                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        startActivity(intent);
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // 디버깅 허용
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    WebView.setWebContentsDebuggingEnabled(true);
                }
                view.loadUrl(url);
                return false;
            }
        });

        Log.d("kakaopay","보내는 값 : " + map);

        readyRequest = KakaoService.getInstance().getApi().kakaoReady(auth, map);
        readyRequest.enqueue(new Callback<PaymentRes>() {
            @Override
            public void onResponse(Call<PaymentRes> call, Response<PaymentRes> response) {
                if(response.code() == 200) {
                    Log.i("pay", response.body().toString());
                    PaymentRes result = response.body();
                    Map<String, String> data = new HashMap<>();

                    data.put("tid",result.getTid());
                    data.put("partner_user_id",getPreferenceString("m_id"));
                    data.put("created_at", Long.toString(dateParser(result.getCreated_at())));

                    b.kakaopayWebView.getSettings().setAllowFileAccess(true);
                    b.kakaopayWebView.getSettings().setAllowContentAccess(true);

                    Log.d("Kakaopay","결제이력 데이터 삽입 내용 : " + data);

                    insertRequest = DataService.getInstance().paymentAPI.kakaoReady(data);
                    insertRequest.enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if(response.code() == 200) {
                                Log.d("Kakaopay","삽입 완료 여부 : " + response.body());
                            } else {
                                try {
                                    Log.d("Kakaopay", "에러 : " + response);
                                    assert response.errorBody() != null;
                                    Log.d("Kakaopay", "데이터 삽입 실패 : " + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });

                    b.kakaopayWebView.loadUrl(result.getNext_redirect_app_url());   // res 값으로 넘어온 url 실행

                }else {
                    Log.d("Kakaopay","err code : " + response);
                }
            }

            @Override
            public void onFailure(Call<PaymentRes> call, Throwable t) {
                Log.e("Kakaopay","결제 준비 항목을 불러오지 못했습니다.");
                t.printStackTrace();
            }
        });
    }

    private HashMap<String, String> requestBinding() {
        HashMap<String, String> map = new HashMap();
        map.put("cid","TCSUBSCRIP");    // 가맹점코드 (정기결제 테스트)
        map.put("partner_order_id", "Tmate");   // 판매자
        map.put("partner_user_id",getPreferenceString("m_id"));  // 회원코드
        map.put("item_name", "택시정기결제");   // 상품명
        map.put("quantity", "1");    // 상품 수량
        map.put("total_amount","0"); // 상품 총액
        map.put("tax_free_amount","0");  // 상품 비과세 금액

        map.put("approval_url", "http://ec2-52-79-142-104.ap-northeast-2.compute.amazonaws.com:8080/kakao/payment?partner_user_id=" + getPreferenceString("m_id"));     // 결제 성공 시 url
        map.put("cancel_url","http://ec2-52-79-142-104.ap-northeast-2.compute.amazonaws.com:8080");       // 결제 취소 시 url
        map.put("fail_url","http://ec2-52-79-142-104.ap-northeast-2.compute.amazonaws.com:8080");         // 결제 실패 시 url
        return map;
    }

    public Long dateParser(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDay = simpleDateFormat.parse(simpleDateFormat.format(date), new ParsePosition(0));
        Long currentLong = currentDay.getTime();
        return currentLong;
    }

    public String getPreferenceString(String key) {
        SharedPreferences pref = getSharedPreferences("loginUser", MODE_PRIVATE);
        return pref.getString(key, "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(readyRequest != null) readyRequest.cancel();
        if(insertRequest != null) insertRequest.cancel();
    }

    @Override   // 가로 세로 전환 시 처음으로 돌아가지 않도록 처리
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}