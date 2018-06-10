package com.example.paneesh.moneypool.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.paneesh.moneypool.R;
import com.example.paneesh.moneypool.fragments.FragmentAdminPool;
import com.example.paneesh.moneypool.fragments.FragmentDashBoard;
import com.example.paneesh.moneypool.fragments.FragmentHome;
import com.example.paneesh.moneypool.fragments.FragmentMyPools;


public class CustomAdapter extends android.support.v4.app.FragmentStatePagerAdapter {

    private Context mContext;

    public CustomAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        FragmentDashBoard fragmentDashBoard = new FragmentDashBoard();
        FragmentMyPools fragmentMyPools = new FragmentMyPools();
        FragmentAdminPool fragmentAdminPool = new FragmentAdminPool();
        if (position == 0) {
            return fragmentDashBoard;
        } else if (position == 1) {
            return fragmentMyPools;
        } else {
            return fragmentAdminPool;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String[] headers = mContext.getResources().getStringArray(R.array.tab_headers);
        return headers[position];
    }

    @Override
    public int getCount() {
        return 3;
    }
}
