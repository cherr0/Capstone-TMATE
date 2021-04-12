package com.tmate.user.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.List;
public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ChatAdapter chatAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ChatData> chatList;
    private String nickname = "강병현";
    private EditText chatText;
    private Button sendButton;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatText = findViewById(R.id.chatText);
        sendButton = findViewById(R.id.sendBtn);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = chatText.getText().toString();
                if (msg != null) {
                    ChatData chat = new ChatData();
                    chat.setName(nickname);
                    chat.setMsg(msg);
                    chatText.setText("");
                    myRef.push().setValue(chat);
                }
            }
        });
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
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        chatList = new ArrayList<>();
        adapter = new ChatAdapter(chatList, nickname);
        recyclerView.setAdapter(adapter);
        // 새로운 글이 추가되면 제일 하단으로 포지션 이동
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = adapter.getItemCount();
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    recyclerView.scrollToPosition(positionStart);
                }
            }
        });
        // 키보드 올라올 때 RecyclerView의 위치를 마지막 포지션으로 이동
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    v.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.smoothScrollToPosition(adapter.getItemCount());
                        }
                    }, 100);
                }
            }
        });
    }
}