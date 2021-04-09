package com.tmate.driver.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tmate.driver.R;

import java.util.ArrayList;
import java.util.List;

public class BlackListSelectFragment extends Fragment {
    private View view;
    private Context context;
    private Button btn_seat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_black_list_select, container, false);
        context = container.getContext();

        final List<String> list = new ArrayList<String>();

        btn_seat = view.findViewById(R.id.btn_seat);
        btn_seat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = new String[]{"너무 시끄러워요", "시간을 안지켜요", "술을 마신거 같아요", "목적지변경을 강요해요", "불친절해요."};
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("항목을 선택해주세요.")
                        .setMultiChoiceItems(
                                items
                                , new boolean[]{false, false, false, false, false}
                                , new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                        if (isChecked) {
                                            Toast.makeText(context
                                                    , items[which]
                                                    , Toast.LENGTH_SHORT).show();
                                            list.add(items[which]);
                                        } else {
                                            list.remove(items[which]);
                                        }
                                    }
                                }
                        )
                        .setPositiveButton("보내기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                dialog.create();
                dialog.show();
            }
        });
        return view;
    }
}