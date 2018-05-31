package com.example.paneesh.moneypool.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.paneesh.moneypool.R;
import com.example.paneesh.moneypool.fragments.FragmentRegistrationPageOne;

public class RegistrationScreen extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_screen);
        loadFragmentPageOne();
    }


    private void loadFragmentPageOne() {
        Fragment fragment = new FragmentRegistrationPageOne();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_registration_container, fragment, null);
        fragmentTransaction.commit();
    }
}
