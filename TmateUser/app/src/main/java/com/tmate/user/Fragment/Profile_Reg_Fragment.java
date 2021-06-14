package com.tmate.user.Fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.tmate.user.Activity.MainViewActivity;
import com.tmate.user.R;
import com.tmate.user.net.DataService;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class Profile_Reg_Fragment extends Fragment {

    private static final String LOG_TAG = "회원정보";

    DataService dataService = DataService.getInstance();
    private Button btn_submit;
    private TextView tv_house;
    private EditText et_email;
    private CircleImageView circleImageView;
    Map<String, String> map = new HashMap<>();
    private WebView wv_adress;
    private Handler handler;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public final static int REQUEST_PERMISSIONS_REQUEST_CODE = 100;

    private Call<Boolean> request;


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
            map.put("password", getArguments().getString("password"));
        }


        btn_submit = rootview.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(v -> {

            if (tv_house != null) {
                map.put("m_house", tv_house.getText().toString());
            }

            if (et_email != null) {
                map.put("m_email", et_email.getText().toString());
            }

            Log.d(LOG_TAG, map.values().toString());

            request = dataService.memberAPI.insertOne(map);
            request.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                    Log.d("profileRegFragment", "코드 : " + response.code());
                    Log.d("profileRegFragment", "바디 : " + response.body());
                    if (response.code() == 200) {
                        Log.d("response", response.body() + "");
                        Intent intent = new Intent(getActivity(), MainViewActivity.class);
                        setPreference("m_id", map.get("m_id"));
                        setPreference("m_name", map.get("m_name"));
                        Toast.makeText(getContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }else {
                        Log.d("Final Check", response.toString());
                        Log.d("Final Check", response.message());
                    }
                }
                @Override
                public void onFailure(@NonNull Call<Boolean> call, Throwable t) {
                    t.printStackTrace();
                    Log.d("Final Check", "회원가입 데이터 전송 실패");
                }
            });


        });
        circleImageView = rootview.findViewById(R.id.circleImageView);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("사진 가져올 방법을 선택하세요.")
                        .setPositiveButton("카메라", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
                                if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                                    new AlertDialog.Builder(getContext())
                                            .setTitle("알림")
                                            .setMessage("저장소 권한이 필요합니다.")
                                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    startPermissionRequestcamera();
                                                }
                                            })
                                            .create()
                                            .show();
                                } else {
                                    Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(intentCamera, REQUEST_IMAGE_CAPTURE);
                                }
                            }
                        })
                        .setNeutralButton("갤러리", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                                    new AlertDialog.Builder(getContext())
                                            .setTitle("알림")
                                            .setMessage("저장소 권한이 필요합니다.")
                                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    startPermissionRequestphoto();
                                                }
                                            })
                                            .create()
                                            .show();
                                } else {
                                    //기기 기본 갤러리 접근
                                    Intent intent = new Intent();
                                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE); //구글 갤러리
                                    intent.setType("image/*");
                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(intent, 101);

                                }

                            }
                        })
                        .create()
                        .show();
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
          
    private void startPermissionRequestcamera() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void startPermissionRequestphoto() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Bundle extras = imageReturnedIntent.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    circleImageView.setImageBitmap(imageBitmap);

                }

                break;
            case 101:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    circleImageView.setImageURI(selectedImage);
                }
                break;
        }
    }

    // 데이터 저장 함수
    public void setPreference(String key, String value){
        SharedPreferences pref = getActivity().getSharedPreferences("loginUser", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    // 데이터 불러오기 함수
    public String getPreferenceString(String key){
        SharedPreferences pref = getActivity().getSharedPreferences("loginUser", getActivity().MODE_PRIVATE);
        return pref.getString(key, "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (request != null ) request.cancel();
    }
}
