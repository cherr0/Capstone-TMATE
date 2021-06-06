package com.tmate.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.tmate.user.R;
import com.tmate.user.data.CardData;

import java.util.ArrayList;
import java.util.Random;

public class PaymentAdapter extends PagerAdapter {

    private final Random random = new Random();
    private int mSize;
    private Context mContext;
    private ArrayList<Integer> imageList;
    private CardData cardData;

    public PaymentAdapter(Context context, ArrayList<Integer> imageList) {
        this.mContext = context;
        this.imageList = imageList;
    }

    @Override public int getCount() {
        return imageList.size();
    }

    @Override public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View) object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull @Override public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.payment_card, null);

        //카드 이미지
        ImageView imageView = view.findViewById(R.id.payment_card_image);
        imageView.setImageResource(imageList.get(position));

        //카드 별칭
//        TextView textView = view.findViewById(R.id.payment_pay_alias);
//        textView.setText(cardData.getPay_alias());

        container.addView(view);
        return view;
    }


}
