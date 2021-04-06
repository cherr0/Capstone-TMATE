package com.tmate.user.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tmate.user.R;

public class FavoritesAddFragment extends Fragment {
    private View view;
    private ImageView btn_back_favoritesadd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_favorites_add, container, false);

        btn_back_favoritesadd = view.findViewById(R.id.btn_back_favoritesadd);
        btn_back_favoritesadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                FavoritesFragment favoritesFragment = new FavoritesFragment();
                transaction.replace(R.id.frameLayout, favoritesFragment).commit();
            }
        });

        return view;
    }
}

