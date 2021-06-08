package com.tmate.user.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skt.Tmap.TMapPoint;
import com.tmate.user.data.Dispatch;
import com.tmate.user.data.FavoritesData;
import com.tmate.user.R;
import com.tmate.user.net.DataService;
import com.tmate.user.ui.driving.DrivingModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesHolder> {

    private ArrayList<FavoritesData> items;
    private DrivingModel mViewModel;
    private TextView finishPlace;

    Call<Boolean> deleteRequest;

    // 레트로핏 어뎁터 서비스
//    AdapterComService dataService = new AdapterComService();
    DataService dataService = DataService.getInstance();

    // m_id 값 불러오기
    Context context;
    private static SharedPreferences pref;
    String m_id;

    public FavoritesAdapter(ArrayList<FavoritesData> items) {
        this.items = items;
    }

    public FavoritesAdapter(ArrayList<FavoritesData> items, DrivingModel model, TextView finish) {
        this.items = items;
        this.mViewModel = model;
        this.finishPlace = finish;
    }

    @NonNull
    @Override
    public FavoritesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_item_list,parent,false);
        context = parent.getContext();
        pref = context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        m_id = pref.getString("m_id", "");
        FavoritesHolder holder = new FavoritesHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesHolder holder, int position) {

        try {
            if(items != null && items.size() != 0) {
                holder.onBind(items.get(position));
            }
        } catch (NullPointerException e) {
            Log.e("아무것도 안옴", e.toString());
        }

        holder.itemView.setTag(position);

        holder.itemView.setOnClickListener(v -> {
            FavoritesData data = items.get(position);
            mViewModel.dispatch.setFinish_place(data.getBm_name());
            mViewModel.dispatch.setFinish_lat(data.getBm_lat());
            mViewModel.dispatch.setFinish_lng(data.getBm_lng());
            finishPlace.setText(data.getBm_name());
        });


        holder.itemView.setOnLongClickListener(v -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
            builder.setTitle("즐겨찾기");
            builder.setMessage("삭제하시겠습니까");
            builder.setPositiveButton("예", (dialog, which) -> {
                deleteRequest = DataService.getInstance().memberAPI.deleteBookmark((String) holder.bm_name.getText(), mViewModel.dispatch.getM_id());
                deleteRequest.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        Toast.makeText(context, "즐겨찾기 삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            });
            builder.setNegativeButton("아니오", (dialog, which) -> dialog.cancel());
            builder.show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return (null != items ? items.size() : 0);
    }

    public void remove(int position) {
        try {
            items.remove(position);
            notifyItemRemoved(position);
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    public void addItem(FavoritesData data) {
        items.add(data);
    }
    public void clear() {
        items.clear();
    }


}
class FavoritesHolder extends RecyclerView.ViewHolder {

    LinearLayout favorites_linear;
    TextView bm_name;
    TextView bm_date;
    TextView bm_id;
    TextView bm_lat;
    TextView bm_lng;

    public FavoritesHolder(View itemView) {
        super(itemView);
        this.favorites_linear = (LinearLayout) itemView.findViewById(R.id.favorites_linear);
        this.bm_name = (TextView) itemView.findViewById(R.id.bm_name);
        this.bm_date = (TextView) itemView.findViewById(R.id.bm_date);
        this.bm_id = (TextView) itemView.findViewById(R.id.bm_id);
        this.bm_lat = (TextView) itemView.findViewById(R.id.bm_lat);
        this.bm_lng = (TextView) itemView.findViewById(R.id.bm_lng);

    }

    void onBind(FavoritesData data) {
        bm_name.setText(data.getBm_name());
        bm_date.setText(data.getBm_date().substring(0, 10));
        bm_id.setText(data.getBm_id());
        bm_lat.setText(String.valueOf(data.getBm_lat()));
        bm_lng.setText(String.valueOf(data.getBm_lng()));
        favorites_linear.setVisibility(View.VISIBLE);
    }

}
