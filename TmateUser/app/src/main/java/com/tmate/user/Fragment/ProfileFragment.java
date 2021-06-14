package com.tmate.user.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.tmate.user.R;
import com.tmate.user.data.Member;
import com.tmate.user.data.Social;
import com.tmate.user.net.DataService;

import java.security.MessageDigest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {
    private View view;
    private ImageView btn_back_profile;


    // 레트로핏2 서비스
    DataService dataService = DataService.getInstance();

    ImageView iv_level;

    TextView tv_name, tv_like, tv_dislike, tv_name2, tv_phone, tv_email, tv_gender, tv_birth, tv_grade,
            tv_point, tv_m_n_use, tv_m_t_use, tv_m_count;

    // Call<Integer> 포인트를 가져오는 리퀘스트
    Call<Integer> getUserTotalPoint;

    // 프로필 변경
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public final static int REQUEST_PERMISSIONS_REQUEST_CODE = 100;
    ImageView m_profile_img;

    // 구글 로그인 연동
    private SignInButton btn_google; // 구글 로그인 버튼
    private FirebaseAuth auth; // 파이어 베이스 인증 객체
    private GoogleApiClient googleApiClient; // 구글 API 클라이언트 객체
    private static final int REQ_SIGN_GOOGLE = 100; // 구글 로그인 결과 코드



    // 계정 연동 DB 연결을 위함
    Social social;

    // 프래그 먼트 처음 수행 되는 곳
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

//        getAppKeyHash();

        initWidget(view); // 위젯 초기화
        findData(); // 프로필 데이터 받아오기
        getTotalPoint(); // 토탈 포인트 받아오기

        //프로필 변경 관련
        m_profile_img = view.findViewById(R.id.m_profile_img);
        m_profile_img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new androidx.appcompat.app.AlertDialog.Builder(getContext())
                        .setTitle("사진 가져올 방법을 선택하세요.")
                        .setPositiveButton("카메라", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
                                if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                                    new androidx.appcompat.app.AlertDialog.Builder(getContext())
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

        // 구글 로그인 관련
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        if(googleApiClient == null || !googleApiClient.isConnected()) {
            googleApiClient = new GoogleApiClient.Builder(getContext())
                    .enableAutoManage((FragmentActivity) getActivity(), this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                    .build();
        }

        auth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체 초기화.

        btn_google = view.findViewById(R.id.btn_google);
        btn_google.setOnClickListener(new View.OnClickListener() { // 구글 로그인 버튼을 클릭했을 때 이곳을 수행.
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, REQ_SIGN_GOOGLE);
            }
        });






        // 이전 화면
        btn_back_profile = view.findViewById(R.id.btn_back_profile);
        btn_back_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                My_info_Fragment my_info_fragment = new My_info_Fragment();
                transaction.replace(R.id.frameLayout, my_info_fragment).commit();
            }
        });


        return view;
    }


    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return permissionState == PackageManager.PERMISSION_GRANTED;
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_SIGN_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            // 인증 결과가 성공적이면...
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount(); // account 라는 데이터는 구글 로그인 정보를 담고 있습니다. (닉네임, 프로필 사진Url, 이메일 주소..등)
                resultLogin(account); // 로그인 결과 수행하려는 메서드
            }
        }

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    m_profile_img.setImageBitmap(imageBitmap);

                }

                break;
            case 101:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    m_profile_img.setImageURI(selectedImage);
                }
                break;
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void resultLogin(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { // 로그인이 성공하면
                            String m_id = getPreferenceString("m_id");
                            Log.d("구글 계정 연동 이메일 정보", account.getEmail());
                            Log.d("구글 계정 연동한 유저 이름", account.getDisplayName());
                            social = new Social();
                            social.setS_email(account.getEmail());
                            social.setM_id(m_id);

                            dataService.memberAPI.socialAccount(social).enqueue(new Callback<Boolean>() {
                                @Override
                                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                    if (response.isSuccessful()) {
                                        if (response.code() == 200) {
                                            Toast.makeText(getActivity(), "구글 계정 연동 성공적으로 마쳤습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Boolean> call, Throwable t) {
                                    t.printStackTrace();
                                }
                            });


                        } else { // 로그인 실패
                            Toast.makeText(getActivity(), "연동 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void initWidget(View v) {

        iv_level = (ImageView) v.findViewById(R.id.iv_level);

        tv_name = (TextView) v.findViewById(R.id.tv_name);
        tv_like = v.findViewById(R.id.tv_like);
        tv_dislike = v.findViewById(R.id.tv_dislike);
        tv_name2 = v.findViewById(R.id.tv_name2);
        tv_phone = v.findViewById(R.id.tv_phone);
        tv_email = v.findViewById(R.id.tv_email);
        tv_gender = v.findViewById(R.id.tv_gender);
        tv_birth = v.findViewById(R.id.tv_birth);
        tv_grade = v.findViewById(R.id.tv_grade);
        tv_point = v.findViewById(R.id.tv_point);
        tv_m_n_use = v.findViewById(R.id.tv_m_n_use);
        tv_m_t_use = v.findViewById(R.id.tv_m_t_use);
        tv_m_count = v.findViewById(R.id.tv_m_count);

    }

    public void findData() {
        String m_id = getPreferenceString("m_id");

        dataService.memberAPI.selectProfile(m_id).enqueue(new Callback<Member>() {
            @Override
            public void onResponse(Call<Member> call, Response<Member> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
//                        Member member = response.body();


                        Member member = response.body();

                        tv_like.setText(member.getLike() + "");
                        tv_dislike.setText(member.getDislike() + "");

                        // 멤버 등급별 이미지 리소스
                        int normalCnt = member.getM_n_use();
                        int togetherCnt = member.getM_t_use();
                        int sumCnt = normalCnt + togetherCnt;
                        if (sumCnt < 5) {
                            iv_level.setImageResource(R.drawable.slver);
                        }
                        if (sumCnt >= 5 && sumCnt < 10) {
                            iv_level.setImageResource(R.drawable.gold);
                        }
                        if (sumCnt >= 10 && sumCnt < 20) {
                            iv_level.setImageResource(R.drawable.platinum);
                        }
                        if (sumCnt >= 20) {
                            iv_level.setImageResource(R.drawable.vip);
                        }

                        tv_name.setText(member.getM_name());
                        tv_name2.setText(member.getM_name());
                        tv_phone.setText(member.getM_id().substring(2, 13));
                        tv_email.setText(member.getM_email());

                        switch (member.getM_id().substring(1, 2)) {
                            case "1":
                            case "3":
                                tv_gender.setText("남자");
                                break;
                            case "2":
                            case "4":
                                tv_gender.setText("여자");
                                break;
                        }
                        Log.d("넘어오는 생년월일", member.getM_birth().toString());
                        tv_birth.setText(member.getM_birth().toString().substring(0, 10));
                        tv_m_n_use.setText(member.getM_n_use() + "회");
                        tv_m_t_use.setText(member.getM_t_use() + "회");
                        tv_m_count.setText(member.getM_count() + "회");
                    }
                }
            }

            @Override
            public void onFailure(Call<Member> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void getTotalPoint() {
        getUserTotalPoint = dataService.memberAPI.getUnusedPoint(getPreferenceString("m_id"));
        getUserTotalPoint.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.code() == 200 && response.body() != null) {
                    Integer point = response.body();
                    tv_point.setText(point + "P");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    // getPreferenceString
    public String getPreferenceString(String key) {
        SharedPreferences pref = getActivity().getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPause() {
        super.onPause();
        googleApiClient.stopAutoManage(requireActivity());
        googleApiClient.disconnect();
    }
}