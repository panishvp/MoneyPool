package com.example.paneesh.moneypool.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.paneesh.moneypool.R;
import com.example.paneesh.moneypool.fragments.FragmentAdminPoolDetails;
import com.example.paneesh.moneypool.fragments.FragmentMyPoolDetails;
import com.example.paneesh.moneypool.fragments.FragmentRegistrationPageOne;

public class PoolDetailsContainer extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pool_transactions);
        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("role").equals("member")){
            loadMemberFragment();
        }else{
            loadAdminFragment();
        }

    }

    private void loadMemberFragment(){
        Fragment fragment = new FragmentMyPoolDetails();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_pool_transactions_container, fragment, null);
        fragmentTransaction.commit();
    }

    private void loadAdminFragment(){

        Fragment fragment = new FragmentAdminPoolDetails();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_pool_transactions_container, fragment, null);
        fragmentTransaction.commit();

    }
}
