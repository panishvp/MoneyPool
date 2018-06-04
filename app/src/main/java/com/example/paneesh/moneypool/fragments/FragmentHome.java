package com.example.paneesh.moneypool.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.paneesh.moneypool.R;
import com.example.paneesh.moneypool.Utils;
import com.example.paneesh.moneypool.database_helper.MemberOperations;
import com.example.paneesh.moneypool.model.Member;

import static android.content.Context.MODE_PRIVATE;

public class FragmentHome extends Fragment {

    private View mView;
    private TextView mTextViewMemberName;
    private TextView mTextViewAddress;
    private TextView mTextViewIban;
    private TextView mTextViewSwiftCode;
    private TextView mTextViewNominee;
    private TextView mTextViewPhoneNumber;
    private TextView mTextViewEmail;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private MemberOperations databaseHelper;
    private Member memberObject;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        initUI();
        fetchDataFromDB();
        return mView;
    }

    private void initUI() {

        mTextViewAddress = mView.findViewById(R.id.tv_address);
        mTextViewIban = mView.findViewById(R.id.tv_iban);
        mTextViewSwiftCode = mView.findViewById(R.id.tv_swift_code);
        mTextViewNominee = mView.findViewById(R.id.tv_nominee);
        mTextViewPhoneNumber = mView.findViewById(R.id.tv_phone_number);
        mTextViewMemberName = mView.findViewById(R.id.tv_member_name);
        mTextViewEmail = mView.findViewById(R.id.tv_email);
        databaseHelper = MemberOperations.getInstance(getContext());
        mSharedPreferences = getActivity().getSharedPreferences(Utils.MyPREFERENCES, MODE_PRIVATE);

    }

    private void fetchDataFromDB(){
        String email = mSharedPreferences.getString(Utils.memberEmail,"");
        String password = mSharedPreferences.getString(Utils.memberPassword, "");
        memberObject = databaseHelper.fetchMemberDetails(email,password);
        displayData(memberObject);
    }

    private void displayData(Member member){
        mTextViewMemberName.setText(member.getMemberFirstName()+" "+member.getMemberLastName());
        mTextViewEmail.setText(member.getMemberEmail());
        mTextViewAddress.setText(member.getMemberAddress());
        mTextViewIban.setText(member.getMemberIban());
        mTextViewNominee.setText(member.getMemberNominee());
        mTextViewSwiftCode.setText(member.getMemberSwiftCode());
        mTextViewPhoneNumber.setText(member.getMemberPhoneNumber());

        saveInSharedPrefs(member.getMemberID());

    }

    private void saveInSharedPrefs(int memberID){
        mSharedPreferences = getActivity().getSharedPreferences(Utils.MyPREFERENCES, MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.putInt(Utils.memberId,memberID);
        mEditor.commit();
    }
}
