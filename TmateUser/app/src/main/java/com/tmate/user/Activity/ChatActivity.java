package com.tmate.user.Activity;

import android.graphics.Color;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tmate.user.R;
import com.tmate.user.adapter.ChatAdapter;
import com.tmate.user.data.ChatData;
import com.tmate.user.databinding.ActivityChatBinding;
import com.tmate.user.databinding.ActivityMatchingMapBinding;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity implements TextWatcher {
    private RecyclerView.Adapter adapter;
    private ChatAdapter chatAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ChatData> chatList;
    private String nickname = "박한수";
    private DatabaseReference myRef;
    long now = System.currentTimeMillis();
    private ActivityChatBinding b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b= ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        b.chatText.addTextChangedListener(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ChatData chat = snapshot.getValue(ChatData.class);
                ((ChatAdapter) adapter).addChat(chat);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        b.recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        b.recyclerView.setLayoutManager(layoutManager);
        chatList = new ArrayList<>();
        adapter = new ChatAdapter(chatList, nickname);
        b.recyclerView.setAdapter(adapter);
        // 새로운 글이 추가되면 제일 하단으로 포지션 이동
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = adapter.getItemCount();
                LinearLayoutManager layoutManager = (LinearLayoutManager) b.recyclerView.getLayoutManager();
                int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    b.recyclerView.scrollToPosition(positionStart);
                }
            }
        });
        // 키보드 올라올 때 RecyclerView의 위치를 마지막 포지션으로 이동
        b.recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    v.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            b.recyclerView.smoothScrollToPosition(adapter.getItemCount());
                        }
                    }, 100);
                }
            }
        });
    }
    int beforLength;
    @Override
    public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
        beforLength = s.length();
        b.sendBtn.setBackgroundColor(Color.BLUE);
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }


    //edit 삭제 수정
    @Override
    public void afterTextChanged(Editable s) {
        int currentLength=s.length();
        if(beforLength==0 && currentLength >0){
            b.sendBtn.setClickable(true);
            b.sendBtn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    String msg = b.chatText.getText().toString();
                    if (msg != null) {
                        ChatData chat = new ChatData();
                        chat.setName(nickname);
                        chat.setMsg(msg);
                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
                        chat.setTime(sdf.format(new Date()));
                        b.chatText.setText("");
                        myRef.push().setValue(chat);
                        b.sendBtn.setBackgroundColor(Color.GRAY);
                    }
                }
            });
        }
        else if(beforLength>0 && currentLength==0){
            b.sendBtn.setClickable(false);
            b.sendBtn.setOnClickListener(null);


        }
    }
}