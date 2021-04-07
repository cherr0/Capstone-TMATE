package com.tmate.user.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tmate.user.R;
import com.tmate.user.data.ChatData;

import java.util.List;
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private List<ChatData> chatList;
    private String name;
    public ChatAdapter(List<ChatData> chatData, String name) {
        chatList = chatData;
        this.name = name;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(linearLayout);
        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, int position) {
        ChatData chat = chatList.get(position);
        holder.nameText.setText(chat.getName());
        holder.msgText.setText(chat.getMsg());
        if (chat.getName().equals(this.name)){
            holder.nameText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            holder.msgText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            holder.msgLinear.setGravity(Gravity.RIGHT);
        } else {
            holder.nameText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            holder.msgText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            holder.msgLinear.setGravity(Gravity.LEFT);
        }
    }
    @Override
    public int getItemCount() {
        return chatList == null ? 0: chatList.size();
    }
    public void addChat(ChatData chat) {
        chatList.add(chat);
        notifyItemInserted(chatList.size()-1);
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameText;
        public TextView msgText;
        public View rootView;
        public LinearLayout msgLinear;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            msgLinear = itemView.findViewById(R.id.msgLinear);
            nameText = itemView.findViewById(R.id.chat_name_text);
            msgText = itemView.findViewById(R.id.msgText);
            rootView = itemView;
            itemView.setEnabled(true);
            itemView.setClickable(true);
        }
    }
}