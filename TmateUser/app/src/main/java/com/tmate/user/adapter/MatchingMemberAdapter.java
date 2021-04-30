package com.tmate.user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.R;
import com.tmate.user.data.MatchingMember;

import java.util.ArrayList;

public class MatchingMemberAdapter extends RecyclerView.Adapter<MatchingMemberHolder> {
    ArrayList<MatchingMember> items = new ArrayList<>();

    @NonNull
    @Override
    public MatchingMemberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.matching_member_item, parent, false);
        return new MatchingMemberHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchingMemberHolder holder, int position) {
        holder.onBind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public void addItem(MatchingMember data) {
        items.add(data);
    }
}

class MatchingMemberHolder extends RecyclerView.ViewHolder {
    TextView m_id;
    TextView m_name;
    TextView m_birth;
    TextView m_t_use;
    TextView like;
    TextView dislike;
    TextView m_count;

    public void onBind(MatchingMember data) {
        m_id.setText(data.getM_id());
        m_name.setText(data.getM_name());
        m_birth.setText(data.getM_birth());
        m_t_use.setText(data.getM_t_use());
        like.setText(data.getLike());
        dislike.setText(data.getDislike());
        m_count.setText(data.getM_count());
    }

    public MatchingMemberHolder(@NonNull View itemView) {
        super(itemView);
        m_id = itemView.findViewById(R.id.member_m_id);
        m_name =itemView.findViewById(R.id.member_m_name);
        m_birth =itemView.findViewById(R.id.member_m_birth);
        m_t_use =itemView.findViewById(R.id.member_m_t_use);
        like =itemView.findViewById(R.id.member_r_code_like);
        dislike =itemView.findViewById(R.id.member_r_code_dislike);
        m_count = itemView.findViewById(R.id.member_m_count);
    }
}
