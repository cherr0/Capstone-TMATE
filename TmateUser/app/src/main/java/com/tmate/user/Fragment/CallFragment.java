package com.tmate.user.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tmate.user.Activity.ChatActivity;
import com.tmate.user.Activity.MatchingMapActivity;
import com.tmate.user.MatchingApplicationListFragment;
import com.tmate.user.R;

import java.util.ArrayList;


public class CallFragment extends Fragment {
    ArrayList<String> list;
    private View view;
    private CardView Ll_together;
    private CardView Ll_solo;
    private int together;
    private Dialog dialog;
    private Button btn_chat;
    private TextView requset_list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_call, container, false);

        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_together);

        Ll_together = view.findViewById(R.id.together_call);
        Ll_together.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(); //동승 설정 알림창
            }
        });

        Ll_solo = view.findViewById(R.id.normal_call);
        Ll_solo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                together =1;
                Intent intent = new Intent(getContext(), MatchingMapActivity.class);
                intent.putExtra("together", together);
                startActivity(intent);

            }
        });

        btn_chat = view.findViewById(R.id.btn_chat);
        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        requset_list = view.findViewById(R.id.requset_list);
        requset_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                MatchingApplicationListFragment matchingApplicationListFragment = new MatchingApplicationListFragment();
                transaction.replace(R.id.frameLayout, matchingApplicationListFragment).commit();
            }
        });

        return view;
    }

    public void showDialog(){
        dialog.show();

        Button btn_no = dialog.findViewById(R.id.btn_double);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                together =2;
                Intent intent = new Intent(getContext(), MatchingMapActivity.class);
                intent.putExtra("together", together);
                startActivity(intent);
            }
        });
        Button btn_yes = dialog.findViewById(R.id.btn_triple);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                together =3;
                Intent intent = new Intent(getContext(), MatchingMapActivity.class);
                intent.putExtra("together", together);
                startActivity(intent);
            }
        });
    }
}