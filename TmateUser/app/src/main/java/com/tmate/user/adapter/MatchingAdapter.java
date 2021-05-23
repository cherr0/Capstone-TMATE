package com.tmate.user.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.Activity.MatchingDetailActivity;
import com.tmate.user.R;
import com.tmate.user.data.History;
import com.tmate.user.net.DataService;

import java.util.ArrayList;

public class MatchingAdapter extends RecyclerView.Adapter<MatchingHolder> {

    private ArrayList<History> items = new ArrayList<>();

    DataService dataService = DataService.getInstance();

    public MatchingAdapter(ArrayList<History> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public MatchingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matching,parent,false);
        MatchingHolder holder = new MatchingHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MatchingHolder holder, int position) {

        try {
            holder.onBind(items.get(position));
        } catch (Exception e) {
            Log.e("아무것도 안옴", e.toString());
        }

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), MatchingDetailActivity.class);
                intent.putExtra("merchant_uid",items.get(position).getMerchant_uid());
                intent.putExtra("m_id", items.get(position).getM_id());

                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != items ? items.size() : 0);
    }

    public void addItem(History matchingData) {
        items.add(matchingData);
    }
}

class MatchingHolder extends RecyclerView.ViewHolder {

    TextView matching_dp_status;
    TextView matching_item_cur_people;
    TextView tv_start;
    TextView matching_item_start_place;
    TextView tv_end;
    TextView matching_item_finish_place;
    ImageView matching_img;
    TextView matching_item_ep_time;


    TextView distance1, distance2;


    // 화면에는 보이지 않지만 값이 필요하다
    TextView tv_m_id, tv_h_s_lttd, tv_h_s_lngtd, tv_h_f_lttd, tv_h_f_lngtd, tv_merchant_uid;

    public MatchingHolder(View itemView) {
        super(itemView);

        this.matching_dp_status = (TextView) itemView.findViewById(R.id.matching_dp_status);
        this.matching_item_cur_people = (TextView) itemView.findViewById(R.id.matching_item_cur_people);
        this.tv_start = (TextView) itemView.findViewById(R.id.tv_start);
        this.matching_item_start_place = (TextView) itemView.findViewById(R.id.matching_item_start_place);
        this.tv_end = (TextView) itemView.findViewById(R.id.tv_end);
        this.matching_item_finish_place = (TextView) itemView.findViewById(R.id.matching_item_finish_place);
        this.matching_img = (ImageView) itemView.findViewById(R.id.matching_img);
        this.matching_item_ep_time = (TextView) itemView.findViewById(R.id.matching_item_ep_time);


        /*
          화면에는 보이지 않는 값
        * 이용 코드 , 회원 코드, 출발지 위도, 경도 & 도착지 위도, 경도
        * */
        this.tv_merchant_uid = (TextView) itemView.findViewById(R.id.tv_merchant_uid);
        this.tv_m_id = (TextView) itemView.findViewById(R.id.tv_m_id);
        this.tv_h_s_lttd = (TextView) itemView.findViewById(R.id.mf_h_s_lttd);
        this.tv_h_s_lngtd = (TextView) itemView.findViewById(R.id.tv_h_s_lngtd);
        this.tv_h_f_lttd = (TextView) itemView.findViewById(R.id.tv_h_f_lttd);
        this.tv_h_f_lngtd = (TextView) itemView.findViewById(R.id.tv_h_f_lngtd);
    }

    void onBind(History data) {
        // 회원 코드, 이름
        tv_m_id.setText(data.getM_id());

        // 매칭 방 코드
        tv_merchant_uid.setText(data.getMerchant_uid());

        // 내가 설정한 출발지와의 거리
        double distanceStart = data.getDistance1() *1000;
        double distanceFinish = data.getDistance2() *1000;

        if (distanceStart < 1000) {
            distance1.setText((int)distanceStart+"m");
        }else{
            distance1.setText((int)distanceStart/1000.0 + "km");
        }
        // 내가 설정한 목적지와의 거리

        if (distanceFinish < 1000) {
            distance2.setText((int)distanceFinish+"m");
        }else{
            distance2.setText((int)distanceFinish/1000.0 + "km");
        }

        // 회원 이거는 머고
        matching_item_cur_people.setText("모집인원("+data.getTo_people()+"/"+data.getTo_max()+")");

        // 출발지 , 위도, 경도
        matching_item_start_place.setText(data.getH_s_place());
        tv_h_s_lttd.setText(String.valueOf(data.getH_s_lttd()));
        tv_h_s_lngtd.setText(String.valueOf(data.getH_s_lngtd()));

        // 도착지, 위도, 경도
        matching_item_finish_place.setText(data.getH_f_place());
        tv_h_f_lttd.setText(String.valueOf(data.getH_f_lttd()));
        tv_h_f_lngtd.setText(String.valueOf(data.getH_f_lngtd()));
    }
}