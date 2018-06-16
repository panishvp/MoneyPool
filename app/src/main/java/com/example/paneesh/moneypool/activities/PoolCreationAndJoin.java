package com.example.paneesh.moneypool.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.example.paneesh.moneypool.R;
import com.example.paneesh.moneypool.fragments.FragmentCreatePool;
import com.example.paneesh.moneypool.fragments.FragmentJoinPools;

public class PoolCreationAndJoin extends AppCompatActivity {

    private FrameLayout frameLayoutPoolCreationJoin;
    private Bundle mBundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pool_creation_join);
        initUI();
        if (mBundle.getString("purpose").equals("join")) {
            FragmentJoinPools fragmentJoinPools = new FragmentJoinPools();
            loadFragment(fragmentJoinPools);
        } else {
            FragmentCreatePool fragmentCreatePool = new FragmentCreatePool();
            loadFragment(fragmentCreatePool);
        }
    }

    private void initUI() {
        frameLayoutPoolCreationJoin = findViewById(R.id.fl_pool_creation_join);
        mBundle = getIntent().getExtras();
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_pool_creation_join, fragment);
        fragmentTransaction.commit();
    }


}
