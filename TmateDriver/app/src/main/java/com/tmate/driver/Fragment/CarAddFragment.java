package com.tmate.driver.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.tmate.driver.R;
import com.tmate.driver.adapter.SpinnerCarColorAdapter;
import com.tmate.driver.data.Car;
import com.tmate.driver.databinding.FragmentCarAddBinding;
import com.tmate.driver.net.DataService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarAddFragment extends Fragment {
    private FragmentCarAddBinding b;
    int [] spinnerImages;
    String car_color;
    String car_kind;
    Call<Boolean> carAddRequest;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        b = FragmentCarAddBinding.inflate(inflater, container, false);
        View view = b.getRoot();

        // 차량 색상 리스트
        spinnerImages  = new int[]{
                R.drawable.white,
                R.drawable.black,
                R.drawable.gray,
                R.drawable.white,
                R.drawable.orange,
                R.drawable.yellow
        };

        // 어댑터 연결
        SpinnerCarColorAdapter adapter = new SpinnerCarColorAdapter(getActivity(), spinnerImages);
        b.carAddColor.setAdapter(adapter);

        // 차량 색상 아이템 선택 리스너
        b.carAddColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                position = b.carAddColor.getSelectedItemPosition();

                switch (position) {
                    case 1 :
                        Toast.makeText(getActivity(),"1번",Toast.LENGTH_SHORT).show();
                        car_color = "black";
                        break;
                    case 2 :
                        Toast.makeText(getActivity(),"2번",Toast.LENGTH_SHORT).show();
                        car_color = "gray";
                        break;
                    case 3 :
                        Toast.makeText(getActivity(),"3번",Toast.LENGTH_SHORT).show();
                        car_color = "white";
                        break;
                    case 4 :
                        Toast.makeText(getActivity(), "4번", Toast.LENGTH_SHORT).show();
                        car_color = "orange";
                        break;
                    case 5:
                        Toast.makeText(getActivity(), "5번", Toast.LENGTH_SHORT).show();
                        car_color = "yellow";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView parent) {

            }
        });

        //차량 분류 선택
        String carKind = b.carAddKind.getSelectedItem().toString();

        switch (carKind) {
            case "소형" :
                car_kind = "0";
                break;
            case "중형" :
                car_kind = "1";
                break;
            case "대형" :
                car_kind = "2";
                break;
        }



        // 차량 등록
        b.btnCarAdd.setOnClickListener(v -> {
            String carModel = b.carAddModel.getText().toString();
            String carNo = b.carAddNum.getText().toString();
            Car car = new Car();
            car.setCar_model(carModel);
            car.setCar_no(carNo);
            car.setCar_color(car_color);
            car.setCar_kind(car_kind);
            car.setM_id(getPreferenceString("d_id"));
            carAddRequest = DataService.getInstance().driver.insertCar(car);
            carAddRequest.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    Log.d("CarAddFragment", "코드 : " + response.code());
                    Log.d("CarAddFragment", "바디 : " + response.body());
                    if (response.code() == 200 && response.body() != null) {
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        CarFragment carFragment = new CarFragment();
                        transaction.replace(R.id.frame, carFragment).commit();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        });



        return view;
    }

    public String getPreferenceString(String key) {
        SharedPreferences pref = getActivity().getSharedPreferences("loginDriver", getActivity().MODE_PRIVATE);
        return pref.getString(key, "");
    }

    // 데이터 저장 함수
    public void setPreference(String key, String value) {
        SharedPreferences pref = getActivity().getSharedPreferences("loginDriver", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (carAddRequest != null) carAddRequest.cancel();
    }
}
