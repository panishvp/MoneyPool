package com.example.paneesh.moneypool.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.paneesh.moneypool.R;
import com.example.paneesh.moneypool.adapters.CustomAdapter;

public class FragmentMemberProfile extends Fragment {

    private View mView;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.content_main, container, false);
        initUI();
        return mView;
    }

    private void initUI() {
        mTabLayout = mView.findViewById(R.id.tl_main);
        mViewPager = mView.findViewById(R.id.vp_main);
        setViewPager();
    }

    private void setViewPager() {
        CustomAdapter adapter = new CustomAdapter(getFragmentManager(), getContext());
        mViewPager.setAdapter(adapter);
        setTabLayout();
        mTabLayout.setupWithViewPager(mViewPager, true);
    }

    private void setTabLayout() {
        String[] headers = getResources().getStringArray(R.array.tab_headers);
        for (int i = 0; i < headers.length; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(headers[i]));
        }
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
