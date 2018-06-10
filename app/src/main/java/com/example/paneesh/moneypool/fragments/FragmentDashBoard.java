package com.example.paneesh.moneypool.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.paneesh.moneypool.R;
import com.example.paneesh.moneypool.Utils;
import com.example.paneesh.moneypool.database_helper.MemberOperations;
import com.example.paneesh.moneypool.model.Member;

import static android.content.Context.MODE_PRIVATE;

public class FragmentDashBoard extends Fragment {

    private View mVIew;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private MemberOperations databaseHelper;
    private Member memberObject;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mVIew = inflater.inflate(R.layout.fragment_dashboard, container, false);
        databaseHelper = MemberOperations.getInstance(getContext());
        mSharedPreferences = getActivity().getSharedPreferences(Utils.MyPREFERENCES, MODE_PRIVATE);
        fetchDataFromDB();
        saveInSharedPrefs(memberObject.getMemberID());
        return mVIew;
    }

    private void fetchDataFromDB(){
        String email = mSharedPreferences.getString(Utils.memberEmail,"");
        String password = mSharedPreferences.getString(Utils.memberPassword, "");
        memberObject = databaseHelper.fetchMemberDetails(email,password);
    }

    private void saveInSharedPrefs(int memberID){
        mEditor = mSharedPreferences.edit();
        mEditor.putInt(Utils.memberId,memberID);
        mEditor.commit();
    }
}
