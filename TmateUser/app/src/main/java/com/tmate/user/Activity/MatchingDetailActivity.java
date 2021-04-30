package com.tmate.user.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.tmate.user.Fragment.MatchingDetailFragment;
import com.tmate.user.R;

public class MatchingDetailActivity extends AppCompatActivity {

    // 인텐트를 받아오기 위해 선언
    Intent intent;

    // 번들로 프래그먼트로 값을 넘겨주기 위해서 선
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_detail);

        intent = getIntent();

        // 인텐트가 널이 아니
        if (intent != null) {
            bundle = new Bundle();
            bundle.putString("merchant_uid",intent.getStringExtra("merchant_uid"));
            bundle.putString("m_id", intent.getStringExtra("m_id"));
            Log.d("이용정보에서 넘어갈때 잘뜨나요? ", intent.getStringExtra("m_id"));
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        MatchingDetailFragment matchingDetailFragment = new MatchingDetailFragment();
        matchingDetailFragment.setArguments(bundle);
        transaction.replace(R.id.fm_matching, matchingDetailFragment).commit();
    }
}