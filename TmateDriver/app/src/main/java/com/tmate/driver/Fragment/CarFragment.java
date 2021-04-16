package com.tmate.driver.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tmate.driver.adapter.CarAdapter;
import com.tmate.driver.data.CarData;
import com.tmate.driver.databinding.FragmentCarBinding;

import java.util.Arrays;
import java.util.List;

public class CarFragment  extends Fragment {
    private FragmentCarBinding b;

    private CarAdapter adapter;


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
        // 임의의 데이터입니다.
        List<String> listCarVin = Arrays.asList(
                "KMHSD81VP4U454178",
                "KNABX511BCT457894",
                "KMNSD71VP4U235828",
                "KMHEG1BP9U293919"
        );
        List<String> listCarNo = Arrays.asList(
                "87가4689",
                "62가3894",
                "19사2488",
                "68자1591"
        );
        List<String> listModel = Arrays.asList(
                "소나타",
                "소나타",
                "K5",
                "그랜져"
        );
        List<String> listCarColor = Arrays.asList(
                "검정색",
                "하얀색",
                "하얀색",
                "검정색");
        List<String> listKind = Arrays.asList(
                "중형",
                "중형",
                "중형",
                "대형");

        for (int i = 0; i < listCarVin.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            CarData data = new CarData();
            data.setCar_model(listModel.get(i));
            data.setCar_no(listCarNo.get(i));
            data.setCar_color(listCarColor.get(i));
            data.setCar_vin(listCarVin.get(i));
            data.setCar_kind(listKind.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }




}
