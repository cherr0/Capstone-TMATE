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

import com.tmate.user.data.EventData;
import com.tmate.user.Activity.EventDetailActivity;
import com.tmate.user.R;
import com.tmate.user.data.EventDTO;
import com.tmate.user.net.DataService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventAdapter extends RecyclerView.Adapter<EventHolder> {

    private ArrayList<EventData> arrayList;

    DataService dataService = DataService.getInstance();

    public EventAdapter(ArrayList<EventData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_event_item, parent, false);
        return new EventHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventHolder holder, int position) {
        holder.iv_event.setImageResource(arrayList.get(position).getIv_event());
        holder.tv_title.setText(arrayList.get(position).getTv_title());
        holder.tv_subTitle.setText(arrayList.get(position).getTv_subTitle());
        holder.tv_date.setText(arrayList.get(position).getTv_date());
        holder.tv_bd_id.setText(arrayList.get(position).getTv_bd_id());


        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("클릭된 이벤트 코드", arrayList.get(position).getTv_bd_id());
                Intent intent = new Intent(v.getContext(), EventDetailActivity.class);


                dataService.commonAPI.readEvent(arrayList.get(position).getTv_bd_id()).enqueue(new Callback<EventDTO>() {
                    @Override
                    public void onResponse(Call<EventDTO> call, Response<EventDTO> response) {
                        if (response.isSuccessful()) {
                            if (response.code() == 200) {
                                EventDTO eventDTO = response.body();
                                intent.putExtra("tv_ev_title", eventDTO.getBd_title());
                                Log.d("넘어오는 URL 주소", eventDTO.getImgURL());
                                String st = eventDTO.getBd_s_date().toString();
                                String et = eventDTO.getBd_e_date().toString();
                                intent.putExtra("tv_ev_duration", st.substring(2, 4) + "." + st.substring(5, 7) + "." + st.substring(8, 10) + "~" + et.substring(2, 4) + "." + et.substring(5, 7) + "." + et.substring(8, 10));
                                intent.putExtra("imgURL", eventDTO.getImgURL());
                                v.getContext().startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<EventDTO> call, Throwable t) {
                        t.printStackTrace();
                    }
                });


            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public void addItem(EventData eventData) {
        arrayList.add(eventData);
    }

    public void addItem(EventAdapter eventAdapter) {
    }

}

class EventHolder extends RecyclerView.ViewHolder {

    ImageView iv_event;
    TextView tv_title;
    TextView tv_date;
    TextView tv_subTitle;
    TextView tv_bd_id;

    public EventHolder(View itemView){
        super(itemView);

        this.iv_event = (ImageView) itemView.findViewById(R.id.iv_event);
        this.tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        this.tv_subTitle = (TextView) itemView.findViewById(R.id.tv_subTitle);
        this.tv_date = (TextView) itemView.findViewById(R.id.tv_date);
        this.tv_bd_id = (TextView) itemView.findViewById(R.id.tv_bd_id);
    }
}