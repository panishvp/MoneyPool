package com.example.paneesh.moneypool.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paneesh.moneypool.R;
import com.example.paneesh.moneypool.Utils;
import com.example.paneesh.moneypool.database_helper.MemberOperations;

public class LoginScreen extends AppCompatActivity {

    private TextView mRegister;
    private EditText mEmail;
    private EditText mPassword;
    private Button mLogin;
    private String stringEmail;
    private String password;
    private TextView mTextViewAlert;
    private MemberOperations databaseOperations;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        initUI();
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginScreen.this, RegistrationScreen.class);
                startActivity(intent);
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (databaseOperations.loginMember(stringEmail, password)){
                    saveInSharedPrefs();
                    Intent intent = new Intent(LoginScreen.this, LandingPage.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Not a valid input", Toast.LENGTH_LONG).show();
                }

            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (checkValidInput()){
                    mLogin.setEnabled(true);
                    mTextViewAlert.setText("");
                }else{
                    mTextViewAlert.setText(Utils.alertUserForValidInput);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        mEmail.addTextChangedListener(textWatcher);
        mPassword.addTextChangedListener(textWatcher);
    }

    private void initUI(){
        mRegister = findViewById(R.id.tv_register);
        mEmail = findViewById(R.id.et_login_username);
        mPassword = findViewById(R.id.et_login_password);
        mLogin = findViewById(R.id.bt_login);
        mTextViewAlert = findViewById(R.id.tv_alert_login);
        databaseOperations =  MemberOperations.getInstance(this);

    }

    private boolean checkValidInput(){
        stringEmail = mEmail.getText().toString();
        password = mPassword.getText().toString();

        if (stringEmail.matches(Utils.EmailRegex) && password.length() > 4){
            return true;
        }else {
            return false;
        }
    }

    private void saveInSharedPrefs(){
        mSharedPreferences = getSharedPreferences(Utils.MyPREFERENCES, MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.putString(Utils.memberEmail,stringEmail);
        mEditor.putString(Utils.memberPassword, password);
        mEditor.commit();
    }
}