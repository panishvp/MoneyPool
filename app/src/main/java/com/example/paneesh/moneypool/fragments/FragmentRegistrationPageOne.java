package com.example.paneesh.moneypool.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paneesh.moneypool.R;
import com.example.paneesh.moneypool.Utils;
import com.example.paneesh.moneypool.database_helper.MemberOperations;
import com.example.paneesh.moneypool.model.Member;

import java.io.Serializable;

public class FragmentRegistrationPageOne extends Fragment {

    private View mView;
    private TextView mTextViewAlertUser;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmail;
    private EditText mPassword;
    private Button mNext;
    private Member member;
    private MemberOperations databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_registration_page_one, container, false);
        initUI();
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (databaseHelper.isMemberPresent(mEmail.getText().toString())){
                    alertUserIsPresent();
                }else {
                    loadFragment();
                }

            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (validateEmail() && validatePassword() && validateNameFields()){
                    mNext.setEnabled(true);
                    mTextViewAlertUser.setText("");
                }else {
                    mNext.setEnabled(false);
                    mTextViewAlertUser.setText(Utils.alertUserForValidInput);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        mFirstName.addTextChangedListener(textWatcher);
        mLastName.addTextChangedListener(textWatcher);
        mEmail.addTextChangedListener(textWatcher);
        mPassword.addTextChangedListener(textWatcher);


        return mView;
    }

    private void initUI() {
        mFirstName = mView.findViewById(R.id.et_first_name);
        mLastName = mView.findViewById(R.id.et_last_name);
        mEmail = mView.findViewById(R.id.et_register_email);
        mPassword = mView.findViewById(R.id.et_register_password);
        mNext = mView.findViewById(R.id.bt_register_next);
        member = new Member();
        databaseHelper = MemberOperations.getInstance(getContext());
        mTextViewAlertUser= mView.findViewById(R.id.tv_alert_register_one);
    }


    private void loadFragment() {
        Fragment fragment = new FragmentRegistrationPageTwo();
        member.setMemberFirstName(mFirstName.getText().toString());
        member.setMemberLastName(mLastName.getText().toString());
        member.setMemberEmail(mEmail.getText().toString());
        member.setMemberPassword(mPassword.getText().toString());
        Bundle bundle = new Bundle();
        bundle.putSerializable(Utils.memberObject, (Serializable) member);
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_registration_container, fragment, null);
        fragmentTransaction.commit();
    }

    private boolean validateEmail(){
        if (mEmail.getText().toString().trim().matches(Utils.EmailRegex)){
            return true;
        }else{
            return false;
        }
    }

    private boolean validatePassword(){
        if ( mPassword.getText().toString().trim().length() > 4){
            return true;
        }else{
            return false;
        }
    }

    private boolean validateNameFields(){
        if (mFirstName.getText().toString().trim().length() > 0 && mLastName.getText().toString().trim().length() > 0){
            return true;
        }else {
            return false;
        }
    }


    private void alertUserIsPresent(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage(Utils.alertUserIsPresent).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
