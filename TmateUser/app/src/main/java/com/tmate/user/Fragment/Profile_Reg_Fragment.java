package com.tmate.user.Fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.tmate.user.Activity.MainViewActivity;
import com.tmate.user.R;
import com.tmate.user.net.DataService;

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
    private EditText et_house;
    private EditText et_email;
    private CircleImageView circleImageView;
    Map<String, String> map = new HashMap<>();

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public final static int REQUEST_PERMISSIONS_REQUEST_CODE = 100;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.fragment_profile__reg_, container, false);

        et_house = (EditText) rootview.findViewById(R.id.et_house);
        et_email = (EditText) rootview.findViewById(R.id.et_email);

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

                if (et_house != null) {
                    map.put("m_house", et_house.getText().toString());
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

}
