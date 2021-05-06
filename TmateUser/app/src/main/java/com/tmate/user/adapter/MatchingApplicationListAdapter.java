package com.tmate.user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.R;
import com.tmate.user.data.MatchingApplicationList;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MatchingApplicationListAdapter extends RecyclerView.Adapter<MatchingApplicationListHolder> {
    ArrayList<MatchingApplicationList> items = new ArrayList<>();

    @NonNull
    @Override
    public MatchingApplicationListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.matching_application_list_item, parent, false);
        return new MatchingApplicationListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchingApplicationListHolder holder, int position) {
        holder.onBind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public void addItem(MatchingApplicationList data) {
        items.add(data);
    }
}

class MatchingApplicationListHolder extends RecyclerView.ViewHolder {
    CircleImageView application_m_profile;
    TextView application_m_name;
    TextView application_read;
    TextView application_personnel;
    TextView application_start_place;
    TextView application_end_place;
    TextView application_merchant_uid;

    public void onBind(MatchingApplicationList data) {
        application_merchant_uid.setText(data.getApplication_merchant_uid());
        application_m_name.setText(data.getApplication_m_name());
        application_read.setText(data.getApplication_read());
        application_personnel.setText(data.getApplication_personnel());
        application_start_place.setText(data.getApplication_h_s_place());
        application_end_place.setText(data.getApplication_h_f_place());
    }

    public MatchingApplicationListHolder(@NonNull View itemView) {
        super(itemView);
        this.application_m_profile=(CircleImageView) itemView.findViewById(R.id.application_m_profile);
        this.application_m_name = (TextView) itemView.findViewById(R.id.application_m_name);
        this.application_read = (TextView) itemView.findViewById(R.id.application_read);
        this.application_personnel = (TextView) itemView.findViewById(R.id.application_personnel);
        this.application_start_place = (TextView) itemView.findViewById(R.id.application_start_place);
        this.application_end_place = (TextView) itemView.findViewById(R.id.application_end_place);
        this.application_merchant_uid = (TextView) itemView.findViewById(R.id.application_merchant_uid);
    }
}
