package com.tmate.user.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.adapter.FavoritesAdapter;
import com.tmate.user.data.FavoritesData;
import com.tmate.user.R;
import com.tmate.user.net.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class favoritesFragment extends Fragment {

    private ArrayList<FavoritesData> arrayList;
    private FavoritesAdapter favoritesAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Button btn_add;
    private FavoriteAddFragment favoriteAddFragment;
    private ImageView btn_back_favorites;
    private LinearLayout favorites_linear;

    // 레트로핏 DB  연동 설정
    Context context;
    private static SharedPreferences pref;
    DataService dataService = DataService.getInstance();
    String m_id;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
       ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_favorites, container, false);

        context = container.getContext();
        pref = context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        m_id = pref.getString("m_id", "");

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
                FavoriteAddFragment fa = new FavoriteAddFragment();
                transaction.replace(R.id.frameLayout, fa).commit();
            }
        });

        btn_back_favorites = rootView.findViewById(R.id.btn_back_favorites);
        btn_back_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                My_info_Fragment my_info_fragment = new My_info_Fragment();
                transaction.replace(R.id.frameLayout, my_info_fragment).commit();
            }
        });

        return rootView;
    }

    private void getData() {


        dataService.memberAPI.getBookmarkList(m_id).enqueue(new Callback<List<FavoritesData>>() {
            @Override
            public void onResponse(Call<List<FavoritesData>> call, Response<List<FavoritesData>> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        List<FavoritesData> list = response.body();
                        if (list != null && list.size() != 0) {
                            for (int i = 0; i < list.size(); i++) {
                                FavoritesData data = new FavoritesData();
                                data.setBm_date(list.get(i).getBm_date());
                                data.setBm_id(list.get(i).getBm_id());
                                data.setBm_name(list.get(i).getBm_name());
                                data.setBm_lttd(list.get(i).getBm_lttd());
                                data.setBm_lngtd(list.get(i).getBm_lngtd());
                                favoritesAdapter.addItem(data);
                            }
                        }else {
                            favoritesAdapter.addItem(null);
                        }

                        favoritesAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<FavoritesData>> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }


}
