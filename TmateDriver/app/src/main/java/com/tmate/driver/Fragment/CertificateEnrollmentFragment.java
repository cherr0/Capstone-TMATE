package com.tmate.driver.Fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.tmate.driver.R;
import com.tmate.driver.databinding.FragmentCertificateEnrollmentBinding;

import java.io.File;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class CertificateEnrollmentFragment extends Fragment implements Validator.ValidationListener {
    private FragmentCertificateEnrollmentBinding b;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public final static int REQUEST_PERMISSIONS_REQUEST_CODE = 100;

    Bundle bundle;

    @Length(min = 11, max = 11, message = "'-' 까지 명시하여 정확히 입력바랍니다.")
    @NotEmpty(message = "자격증 번호를 입력 바랍니다.")
    EditText license_no;

    Validator validator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentCertificateEnrollmentBinding.inflate(inflater, container, false);
        View view = b.getRoot();

        if (getArguments() != null) {
            bundle = getArguments();
            Log.d("번들 넘어오는 값",bundle.toString());
        }else {
            Log.d("CEFragment.Bundle","번들 값을 받아오지 못했습니다.");
        }

        license_no = view.findViewById(R.id.license_no);
        validator = new Validator(this);//필수
        validator.setValidationListener(this); //필수

        b.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate(); //버튼 클릭 시 이벤트 발생 //필수
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
    public void onValidationSucceeded() {
        Log.i("자격증 번호", "값 저장됨 : " + license_no.getText().toString());
        bundle.putString("d_license_no", license_no.getText().toString());

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        AccountRegistrationFragment arf = new AccountRegistrationFragment();
        arf.setArguments(bundle);
        transaction.replace(R.id.fm_main, arf);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getActivity());
            if (view instanceof EditText) {
                ((EditText)view).setError(message);
            } else {
                Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
            }
        }
    }
}


