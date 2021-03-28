package com.tmate.user.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.FavoritesAdapter;
import com.tmate.user.FavoritesData;
import com.tmate.user.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class favoritesFragment extends Fragment {

    private ArrayList<FavoritesData> arrayList;
    private FavoritesAdapter favoritesAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Button btn_add;
    private FavoritesAddFragment favoritesAddFragment;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
       ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_favorites, container, false);

       recyclerView = (RecyclerView) rootView.findViewById(R.id.favorites_rv);
       linearLayoutManager= new LinearLayoutManager(getActivity());
       recyclerView.setLayoutManager(linearLayoutManager);

       arrayList = new ArrayList<>();

        favoritesAdapter = new FavoritesAdapter(arrayList);
        recyclerView.setAdapter(favoritesAdapter);

        getData();


        btn_add = rootView.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                FavoritesAddFragment favoritesAddFragment = new FavoritesAddFragment();
                transaction.replace(R.id.frameLayout, favoritesAddFragment).commit();
            }
        });

        return rootView;
    }

    private void getData() {
        List<String> adress = Arrays.asList(
                "대구 북구 영진전문대",
                "대구 달서구 성서청구타운"
        );
        List<String> date = Arrays.asList(
                "2021-03-21",
                "2021-03-23"
        );

        for (int i = 0; i < adress.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            FavoritesData data = new FavoritesData();
            data.setBm_name(adress.get(i));
            data.setBm_date(date.get(i));
            // 각 값이 들어간 data를 adapter에 추가합니다.
            favoritesAdapter.addItem(data);
        }
    }
}
