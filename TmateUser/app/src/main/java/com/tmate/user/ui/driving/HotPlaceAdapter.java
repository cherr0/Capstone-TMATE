package com.tmate.user.ui.driving;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.tmate.user.R;
import com.tmate.user.data.FavoritesData;
import com.tmate.user.data.Place;
import com.tmate.user.net.DataService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotPlaceAdapter extends RecyclerView.Adapter<HotplaceHolder>{

    private ArrayList<Place> items;
    DrivingModel mViewModel;
    TextView finish;
    Context context;

    FavoritesData bookmark;

    Call<Boolean> insertRequest;
    Call<Boolean> deleteRequest;

    public HotPlaceAdapter(ArrayList<Place> items, DrivingModel model, TextView finish) {
        this.items = items;
        this.mViewModel = model;
        this.finish = finish;
    }

    @NonNull
    @Override
    public HotplaceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_address, parent, false);
        context = parent.getContext();
        return new HotplaceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotplaceHolder holder, int position) {
        int posit = holder.getAdapterPosition();
        Log.d("HotPlaceAdapter","바인딩 되는 중");
        holder.onBind(items.get(posit));

        holder.itemView.setOnClickListener((v) -> {
            Log.d("HotPlaceAdapter", "도착지 데이터 가져오는 중");
            Place data = items.get(posit);

            mViewModel.dispatch.setFinish_place(data.getPl_name());
            mViewModel.dispatch.setFinish_lat(data.getPl_lat());
            mViewModel.dispatch.setFinish_lng(data.getPl_lng());

            TMapPoint startPoint = new TMapPoint(mViewModel.dispatch.getStart_lat(), mViewModel.dispatch.getStart_lng());
            TMapPoint finishPoint = new TMapPoint(data.getPl_lat(),data.getPl_lng());

            TMapPolyLine polyLine = new TMapPolyLine();
            polyLine.addLinePoint(startPoint);
            polyLine.addLinePoint(finishPoint);
            double distance = polyLine.getDistance();
            mViewModel.dispatch.setEp_distance((int) distance); // 예상 거리 설정
            mViewModel.dispatch.setAll_fare(getExpectTaxiFare(distance)); // 요금 설정
            mViewModel.pl_id = data.getPl_id();

            finish.setText(data.getPl_name());

            Log.d("HotPlaceAdapter", "출발지 주소 : " + mViewModel.dispatch.getStart_place());
            Log.d("HotPlaceAdapter", "출발지 좌표 : " + mViewModel.dispatch.getStart_lat() + ", " + mViewModel.dispatch.getStart_lng());
            Log.d("HotPlaceAdapter", "도착지 주소 : " + data.getPl_address().replace(" null",""));
            Log.d("HotPlaceAdapter","도착지 이름 : " + data.getPl_name());
            Log.d("HotPlaceAdapter", "도착지 좌표 : " + finish);
            Log.d("HotPlaceAdapter", "예상거리 : " + mViewModel.dispatch.getEp_distance());
            Log.d("HotPlaceAdapter","예상 요금 : " + mViewModel.dispatch.getAll_fare());
        });

        holder.favorite_btn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Place data = items.get(posit);
            bookmark = new FavoritesData();
            bookmark.setM_id(mViewModel.dispatch.getM_id());
            bookmark.setBm_lat(data.getPl_lat());
            bookmark.setBm_lng(data.getPl_lng());
            bookmark.setBm_name(data.getPl_name());
            holder.favorite_btn.setEnabled(false);
            if(isChecked) {
                insertRequest = DataService.getInstance().memberAPI.insertBookmark(bookmark);
                insertRequest.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(response.code() == 200 && response.body() != null) {
                            Toast.makeText(context, "즐겨찾기 등록 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            holder.favorite_btn.setEnabled(true);
                        }
                    }
                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            } else {
                deleteRequest = DataService.getInstance().memberAPI.deleteBookmark(bookmark.getBm_name(), bookmark.getM_id());
                deleteRequest.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(response.code() == 200 && response.body() != null) {
                            Toast.makeText(context, "즐겨찾기 삭제 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            holder.favorite_btn.setEnabled(true);
                        }
                    }
                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });
    }

    // 택시 요금 계산 로직
    private int getExpectTaxiFare(double totalDistance) {
        int pay = 3300; // 기본 요금

        // 입력된 거리에서 2000을 빼준다.
        int i = (int) totalDistance - 2000;
        if (i < 0) {
            return pay;
        }else{
            /*
             * 대구시 보니깐 144m에 150원씩 추가한다.
             * */
            pay = pay + (i / 144) * 150;
            return pay;
        }
    }

    @Override
    public int getItemCount() {
        return (null != items ? items.size() : 0);
    }

    public void addItem(Place data) { items.add(data); }
    public void clear() {
        items.clear();
    }


}

class HotplaceHolder extends RecyclerView.ViewHolder {

    TextView search_name;
    TextView search_address;
    TextView finish_lat;
    TextView finish_lng;
    ToggleButton favorite_btn;

    void onBind(Place data) {
        search_name.setText(data.getPl_name());
        search_address.setText(data.getPl_address());
        finish_lat.setText(String.valueOf(data.getPl_lat()));
        finish_lng.setText(String.valueOf(data.getPl_lng()));
    }

    public HotplaceHolder(@NonNull View itemView) {
        super(itemView);

        search_name = (TextView) itemView.findViewById(R.id.search_name);
        search_address = (TextView) itemView.findViewById(R.id.search_address);
        finish_lat = (TextView) itemView.findViewById(R.id.finish_lat);
        finish_lng = (TextView) itemView.findViewById(R.id.finish_lng);
        favorite_btn = (ToggleButton) itemView.findViewById(R.id.favorite_btn);

    }
}