package com.tmate.user.ui.driving;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.tmate.user.R;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchHolder> {

    private ArrayList<TMapPOIItem> items;
    DrivingModel mViewModel;
    TextView finish;

    public SearchAdapter(ArrayList<TMapPOIItem> items, DrivingModel model, TextView finish) {
        this.mViewModel = model;
        this.items = items;
        this.finish = finish;
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_address, parent, false);
        return new SearchHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHolder holder, int position) {
        int posit = holder.getAdapterPosition();
        holder.onBind(items.get(posit));

        holder.itemView.setOnClickListener( v -> {
            Log.d("SearchAdapter", "도착지 데이터 삽입 중");
            TMapPOIItem data = items.get(posit);

            mViewModel.dispatch.setFinish_place(data.getPOIName()); // 이름 설정
            mViewModel.dispatch.setFinish_lat(data.getPOIPoint().getLatitude()); // 위도 설정
            mViewModel.dispatch.setFinish_lng(data.getPOIPoint().getLongitude()); // 경도 설정

            TMapPoint point = new TMapPoint(mViewModel.dispatch.getStart_lat(), mViewModel.dispatch.getStart_lng());
            double distance = data.getDistance(point);
            mViewModel.dispatch.setEp_distance((int) distance); // 예상 거리 설정
            mViewModel.dispatch.setAll_fare(getExpectTaxiFare(distance)); // 요금 설정

            finish.setText(data.getPOIName());

            Log.d("SearchAdapter", "출발지 주소 : " + mViewModel.dispatch.getStart_place());
            Log.d("SearchAdapter", "출발지 좌표 : " + mViewModel.dispatch.getStart_lat() + ", " + mViewModel.dispatch.getStart_lng());
            Log.d("SearchAdapter", "도착지 주소 : " + data.getPOIAddress().replace(" null",""));
            Log.d("SearchAdapter","도착지 이름 : " + data.getPOIName());
            Log.d("SearchAdapter", "도착지 좌표 : " + data.getPOIPoint());
            Log.d("SearchAdapter", "예상거리 : " + mViewModel.dispatch.getEp_distance());
            Log.d("SearchAdapter","예상 요금 : " + mViewModel.dispatch.getAll_fare());
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

    public void addItem(TMapPOIItem item) {items.add(item);}
    public void clear() {
        items.clear();
    }
}

class SearchHolder extends RecyclerView.ViewHolder {

    TextView search_name;
    TextView search_address;
    TextView finish_lat;
    TextView finish_lng;
    Button finish_button;

    void onBind(TMapPOIItem data) {
        search_name.setText(data.getPOIName());
        search_address.setText(data.getPOIAddress().replace(" null",""));
        finish_lat.setText(String.valueOf(data.getPOIPoint().getLatitude()));
        finish_lng.setText(String.valueOf(data.getPOIPoint().getLongitude()));
    }

    public SearchHolder(@NonNull View itemView) {
        super(itemView);

        search_name = (TextView) itemView.findViewById(R.id.search_name);
        search_address = (TextView) itemView.findViewById(R.id.search_address);
        finish_button = (Button) itemView.findViewById(R.id.finish_button);
        finish_lat = (TextView) itemView.findViewById(R.id.finish_lat);
        finish_lng = (TextView) itemView.findViewById(R.id.finish_lng);
    }
}
