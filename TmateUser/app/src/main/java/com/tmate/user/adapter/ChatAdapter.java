package com.tmate.user.adapter;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.tmate.user.R;
import com.tmate.user.data.ChatData;

import java.util.List;
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private List<ChatData> chatList;
    private String name;
    private String mProfileUid = "박한수"; //내 uid

    public ChatAdapter(List<ChatData> chatData, String name) {
        chatList = chatData;
        this.name = name;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_item2, parent, false);
                return new ChatHolder2(view);
            case 1:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_item, parent, false);
                return new ChatHolder(view);
        }
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item, parent, false);
        return new ChatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ChatData chat = chatList.get(position);

        if (chat.getName().equals(this.name)) { //내 메세지인 경우
            ChatHolder2 holder2 = (ChatHolder2) holder;
            holder2.msgTime.setText(chat.getTime());
            holder2.msgText.setText(chat.getMsg());
        } else {
            ChatHolder holder1 = (ChatHolder) holder;
            holder1.nameText.setText(chat.getName());
            holder1.msgTime.setText(chat.getTime());
            holder1.msgText.setText(chat.getMsg());
        }
    }
    @Override
    public int getItemViewType(int position) {
        ChatData chatMessage = chatList.get(position);
        Log.d("FFFFFF", "겟아이템뷰타입 : " + chatMessage.getName());
        Log.d("FFFFFF", "겟아이템뷰타입 내 토큰 : " + mProfileUid);
        if (chatMessage.getName().equals(mProfileUid)) {
            return 0;
        } else {
            return 1;
        }
    }
    @Override
    public int getItemCount() {
        return chatList == null ? 0: chatList.size();
    }
    class ChatHolder extends RecyclerView.ViewHolder {
        public TextView nameText;
        public TextView msgText;
        public View rootView;
        public LinearLayout msgLinear;
        public TextView msgTime;
        public LinearLayout msgLinear_second;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);
            msgLinear = itemView.findViewById(R.id.msgLinear);
            nameText = itemView.findViewById(R.id.chat_name_text);
            msgText = itemView.findViewById(R.id.msgText);
            msgTime = itemView.findViewById(R.id.msgTime);
            msgLinear_second = itemView.findViewById(R.id.msgLinear_second);
            rootView = itemView;
            itemView.setEnabled(true);
            itemView.setClickable(true);
        }
    }
    class ChatHolder2 extends RecyclerView.ViewHolder {
        public TextView nameText;
        public TextView msgText;
        public View rootView;
        public LinearLayout msgLinear;
        public TextView msgTime;
        public LinearLayout msgLinear_second;

        public ChatHolder2(@NonNull View itemView) {
            super(itemView);
            msgLinear = itemView.findViewById(R.id.msgLinear2);
            nameText = itemView.findViewById(R.id.chat_name_text2);
            msgText = itemView.findViewById(R.id.msgText2);
            msgTime = itemView.findViewById(R.id.msgTime2);
            msgLinear_second = itemView.findViewById(R.id.msgLinear_second2);
            rootView = itemView;
            itemView.setEnabled(true);
            itemView.setClickable(true);
        }
    }


    public void addChat(ChatData chat) {
        chatList.add(chat);
        notifyItemInserted(chatList.size()-1);
    }

    //한꺼번에 추가해주고싶을떄
    public void addItems(List<ChatData> chatList) {
        this.chatList = chatList;
    }

    //아이템 전부 삭제
    public void clear() {
        chatList.clear();
    }



    }

