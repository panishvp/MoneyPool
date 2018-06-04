package com.example.paneesh.moneypool.fragments;

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

public class FragmentRegistrationPageTwo extends Fragment {

    private View mView;
    private TextView mTextViewAlertUser;
    private EditText mAddress;
    private EditText mIban;
    private EditText mSwiftCode;
    private EditText mNominee;
    private EditText mPhoneNumber;
    private Button mRegister;
    private Member member;
    private Bundle mBundle;
    private MemberOperations memberOperations;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_registration_page_two, container,false);
        initUI();

        mBundle = getArguments();
        member = (Member) mBundle.getSerializable(Utils.memberObject);


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (validateFields()){
                    mRegister.setEnabled(true);
                    mTextViewAlertUser.setText("");
                }else {
                    mTextViewAlertUser.setText(Utils.alertUserForValidInput);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        mAddress.addTextChangedListener(textWatcher);
        mIban.addTextChangedListener(textWatcher);
        mSwiftCode.addTextChangedListener(textWatcher);
        mNominee.addTextChangedListener(textWatcher);
        mPhoneNumber.addTextChangedListener(textWatcher);


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                member.setMemberIban(mIban.getText().toString());
                member.setMemberSwiftCode(mSwiftCode.getText().toString());
                member.setMemberNominee(mNominee.getText().toString());
                member.setMemberPhoneNumber(mPhoneNumber.getText().toString());
                member.setMemberAddress(mAddress.getText().toString());
                memberOperations.insertMember(member);
                loadFragment();
            }
        });
        return  mView;
    }

    private void initUI(){
        mAddress = mView.findViewById(R.id.et_address);
        mIban = mView.findViewById(R.id.et_iban);
        mSwiftCode = mView.findViewById(R.id.et_swift_code);
        mNominee = mView.findViewById(R.id.et_nominee);
        mPhoneNumber = mView.findViewById(R.id.et_phone_number);
        mRegister = mView.findViewById(R.id.bt_register);
        mTextViewAlertUser = mView.findViewById(R.id.tv_alert_register_two);
        memberOperations = MemberOperations.getInstance(getContext());

    }

    private void loadFragment() {
        Fragment fragment = new FragmentRegistrationSuccess();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_registration_container, fragment, null);
        fragmentTransaction.commit();
    }

    private boolean validateFields(){
        if (mSwiftCode.getText().toString().trim().length() > 0 && mAddress.getText().toString().trim().length() > 0 && mIban.getText().toString().trim().length() > 0
                && mNominee.getText().toString().trim().length() > 0 && mPhoneNumber.getText().toString().trim().length() > 0){
            return true;
        }else {
            return false;
        }

    }
}
