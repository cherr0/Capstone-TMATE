package com.tmate.driver.Fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tmate.driver.R;
import com.tmate.driver.databinding.FragmentCertificateEnrollmentBinding;
import com.tmate.driver.databinding.FragmentProfileBinding;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public final static int REQUEST_PERMISSIONS_REQUEST_CODE = 100;
    private Button m_profile;
    private FragmentProfileBinding b;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentProfileBinding.inflate(inflater, container, false);
        View view = b.getRoot();


        b.mProfile.setOnClickListener(new View.OnClickListener() {
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
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Bundle extras = imageReturnedIntent.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    b.mProfile.setImageBitmap(imageBitmap);

                }

                break;
            case 101:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    b.mProfile.setImageURI(selectedImage);
                }
                break;
        }
    }
}