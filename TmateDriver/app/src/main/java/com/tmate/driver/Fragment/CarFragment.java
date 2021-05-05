package com.tmate.driver.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

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

    Call<List<Car>> request;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentCarBinding.inflate(inflater, container, false);
        View v = b.getRoot();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        b.carList.setLayoutManager(linearLayoutManager);

        adapter = new CarAdapter();
        b.carList.setAdapter(adapter);

        getData();
        return v;
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

    public String getPreferenceString(String key) {
        SharedPreferences pref = getActivity().getSharedPreferences("loginDriver", getActivity().MODE_PRIVATE);
        return pref.getString(key, "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        request.cancel();
    }
}
