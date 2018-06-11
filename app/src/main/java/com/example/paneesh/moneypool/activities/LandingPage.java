package com.example.paneesh.moneypool.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.paneesh.moneypool.AndroidDatabaseManager;
import com.example.paneesh.moneypool.R;
import com.example.paneesh.moneypool.Utils;
import com.example.paneesh.moneypool.fragments.FragmentHome;
import com.example.paneesh.moneypool.fragments.FragmentMemberProfile;
import com.example.paneesh.moneypool.fragments.FragmentUpdateDetails;

public class LandingPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initUI();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initUI() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        FragmentMemberProfile fragmentMemberProfile = new FragmentMemberProfile();
        replaceFragment(fragmentMemberProfile);
        mNavigationView.setNavigationItemSelectedListener(this);
        mSharedPreferences = getSharedPreferences(Utils.MyPREFERENCES, MODE_PRIVATE);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finishAffinity();
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

            case  R.id.nav_manage:
                mEditor = mSharedPreferences.edit();
                mEditor.clear();
                mEditor.commit();
                Intent intent = new Intent(LandingPage.this, LoginScreen.class);
                startActivity(intent);
                finishAffinity();
                break;


            default:
              /*  fragment = new FragmentMemberProfile();
                replaceFragment(fragment);
                break;
*/
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
