package com.example.paneesh.moneypool.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.paneesh.moneypool.R;
import com.example.paneesh.moneypool.Utils;
import com.example.paneesh.moneypool.fragments.FragmentPickWinner;
import com.example.paneesh.moneypool.model.PoolDetails;

public class ActivityPickWinner extends AppCompatActivity {

    private Bundle bundle;
    private PoolDetails poolDetails;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_winner);
        bundle =  getIntent().getExtras();
        poolDetails  = (PoolDetails) bundle.getSerializable(Utils.poolDetailsTable);
        FragmentPickWinner fragment = new FragmentPickWinner();
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_activity_pick_winner, fragment);
        fragmentTransaction.commit();
    }
}
