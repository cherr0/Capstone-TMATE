package com.tmate.driver.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tmate.driver.R;
import com.tmate.driver.activity.MainViewActivity;
import com.tmate.driver.adapter.CarAdapter;
import com.tmate.driver.data.Car;
import com.tmate.driver.databinding.FragmentCarBinding;
import com.tmate.driver.net.DataService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarFragment  extends Fragment {
    private FragmentCarBinding b;

    private CarAdapter adapter;
    Car car = new Car();
    Call<List<Car>> request;
    Call<Boolean> selectRequest;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentCarBinding.inflate(inflater, container, false);
        View view = b.getRoot();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        b.carList.setLayoutManager(linearLayoutManager);

        adapter = new CarAdapter();
        b.carList.setAdapter(adapter);

        getData(); //차량 리스트

        // 차량 추가 화면으로 이동
        b.carAdd.setOnClickListener(v -> {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            CarAddFragment carAddFragment = new CarAddFragment();
            MainViewActivity.navbarFlag = 4;
            transaction.replace(R.id.frame, carAddFragment).commit();
        });

        // 차량 선택
        b.goCarAdd.setOnClickListener(v -> {
            select();
        });
        return view;
    }

    private void getData() {
        request = DataService.getInstance().driver.driverCarList(getPreferenceString("d_id"));
        request.enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if (response.code() == 200) {
                    List<Car> list = response.body();
                    Log.i("CarFragment","car List : " + list);
                    for(Car car : list) {
                        adapter.addItem(car);
                    }
                    // adapter의 값이 변경되었다는 것을 알려줍니다.
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "차량 정보를 불러오지 못하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void select() {
        selectRequest = DataService.getInstance().driver.selectCar(getPreferenceString("d_id"), getPreferenceString("carNo"));
        Log.d("CarFragment", "d_id : " + getPreferenceString("d_id"));
        Log.d("CarFragment", "carNo : " + getPreferenceString("carNo"));
        selectRequest.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Log.d("CarFragment", "코드 : " + response.code());
                Log.d("CarFragment", "바디 : " + response.body());
                if (response.code() == 200) {
                    Intent intent = new Intent(getActivity(), MainViewActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                t.printStackTrace();
            }
        });
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
        if (selectRequest != null) selectRequest.cancel();
    }
}
