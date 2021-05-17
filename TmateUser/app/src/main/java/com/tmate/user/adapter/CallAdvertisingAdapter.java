package com.tmate.user.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tmate.user.Fragment.CallEventFragment1;
import com.tmate.user.Fragment.CallEventFragment2;
import com.tmate.user.Fragment.CallEventFragment3;
import com.tmate.user.Fragment.CallEventFragment4;
import com.tmate.user.Fragment.CallEventFragment5;
import com.tmate.user.Fragment.event_close_item_pro;
import com.tmate.user.Fragment.event_close_item_stop;

public class CallAdvertisingAdapter extends FragmentPagerAdapter {
    public CallAdvertisingAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0 :
                CallEventFragment1 cef1 = new CallEventFragment1();
                return cef1;
            case 1 :
                CallEventFragment2 cef2 = new CallEventFragment2();
                return cef2;
            case 2 :
                CallEventFragment3 cef3 = new CallEventFragment3();
                return cef3;
            case 3 :
                CallEventFragment4 cef4 = new CallEventFragment4();
                return cef4;
            case 4 :
                CallEventFragment5 cef5 = new CallEventFragment5();
                return cef5;
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
