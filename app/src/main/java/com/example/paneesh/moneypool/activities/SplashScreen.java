package com.example.paneesh.moneypool.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paneesh.moneypool.R;
import com.example.paneesh.moneypool.Utils;
import com.example.paneesh.moneypool.database_helper.MemberOperations;

public class SplashScreen extends AppCompatActivity {

    private ImageView mLogo;
    private TextView appName;
    private Animation fromTop;
    private Animation fromBottom;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private MemberOperations memberOperations;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mLogo = findViewById(R.id.iv_splash);
        appName = findViewById(R.id.tv_app_name);
        fromTop = AnimationUtils.loadAnimation(this,R.anim.fromtop);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);
        mLogo.setAnimation(fromTop);
        appName.setAnimation(fromBottom);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkForLogin();
            }
        }, 5000);

    }

    private void checkForLogin(){
        mSharedPreferences = getSharedPreferences(Utils.MyPREFERENCES, MODE_PRIVATE);
        memberOperations = new MemberOperations(this);
        if (mSharedPreferences.getBoolean(Utils.isLoggedIN, false)){
            String email = mSharedPreferences.getString(Utils.memberEmail, "");
            String password = mSharedPreferences.getString(Utils.memberPassword, "");
           isLoggedIn(email, password);
        }else {
            navigateToLoginScreen();
        }
    }

    private void isLoggedIn(String email, String password){

        Intent intent;
        if (memberOperations.loginMember(email, password)){
            saveInSharedPrefs(email, password);
            intent = new Intent(SplashScreen.this, LandingPage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else {
            navigateToLoginScreen();
        }
    }

    private void saveInSharedPrefs(String stringEmail, String password){
        mSharedPreferences = getSharedPreferences(Utils.MyPREFERENCES, MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.putString(Utils.memberEmail,stringEmail);
        mEditor.putString(Utils.memberPassword, password);
        mEditor.putBoolean(Utils.isLoggedIN, true);
        mEditor.commit();
    }

    private void navigateToLoginScreen(){
       Intent intent = new Intent(SplashScreen.this, LoginScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
