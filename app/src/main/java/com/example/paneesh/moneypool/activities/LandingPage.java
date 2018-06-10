package com.example.paneesh.moneypool.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.paneesh.moneypool.AndroidDatabaseManager;
import com.example.paneesh.moneypool.R;
import com.example.paneesh.moneypool.fragments.FragmentHome;
import com.example.paneesh.moneypool.fragments.FragmentMemberProfile;
import com.example.paneesh.moneypool.fragments.FragmentUpdateDetails;

public class LandingPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        FragmentMemberProfile fragmentMemberProfile = new FragmentMemberProfile();
        replaceFragment(fragmentMemberProfile);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.nav_camera:
                fragment = new FragmentUpdateDetails();
                replaceFragment(fragment);
                break;
            case R.id.nav_gallery:
                Intent dbmanager = new Intent(LandingPage.this,AndroidDatabaseManager.class);
                startActivity(dbmanager);
                break;

            case R.id.nav_slideshow:
            fragment = new FragmentHome();
            replaceFragment(fragment);
            break;

            default:
                fragment = new FragmentMemberProfile();
                replaceFragment(fragment);
                break;

        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_member_home, fragment);
        fragmentTransaction.commit();
    }
}
