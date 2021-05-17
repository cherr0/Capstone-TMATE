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
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tmate.user.adapter.FavoritesAdapter;
import com.tmate.user.data.CardData;
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
    private RecyclerView favRecy;
    private LinearLayoutManager linearLayoutManager;
    private TextView btn_add;
    private FavoriteAddFragment favoriteAddFragment;
    private ImageView btn_back_favorites;
    private LinearLayout favorites_linear;
    private SwipeRefreshLayout refFav;

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

        favRecy = (RecyclerView) rootView.findViewById(R.id.favorites_rv);
        linearLayoutManager= new LinearLayoutManager(getContext());
        favRecy.setLayoutManager(linearLayoutManager);

       arrayList = new ArrayList<>();

        favoritesAdapter = new FavoritesAdapter(arrayList);
        favRecy.setAdapter(favoritesAdapter);

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
        refFav = rootView.findViewById(R.id.ref_fav);
        refFav.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                arrayList = new ArrayList<>();
                arrayList.clear();
                favoritesAdapter.clear();
                favremoveScrollPullUpListener(); //스와이프와(위에서 아래로 댕기는 새로고침) 리사이클러뷰 포지션 리스너를 같이 쓰면 에러가나므로 리사이클러뷰 리스너는 잠시 끊어주고 새로고침후 다시연결해준다.
                getData(); // 디비에서 값을 다시불러온다.
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
                        //카드 새로고침 후 로딩중 아이콘 지우기
                        refFav.setRefreshing(false);

                    }
                }
            }

            @Override
            public void onFailure(Call<List<FavoritesData>> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }
    private void favremoveScrollPullUpListener(){
        favRecy.removeOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }
        });
    }


}
