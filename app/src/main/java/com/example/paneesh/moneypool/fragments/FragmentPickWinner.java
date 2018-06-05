package com.example.paneesh.moneypool.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pools;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.paneesh.moneypool.R;
import com.example.paneesh.moneypool.Utils;
import com.example.paneesh.moneypool.database_helper.MemberOperations;
import com.example.paneesh.moneypool.model.Member;
import com.example.paneesh.moneypool.model.PoolDetails;

import java.util.ArrayList;
import java.util.Random;

public class FragmentPickWinner extends Fragment {


    private View mView;
    private Button mButtonPickWinner;
    private Bundle mBundle;
    private PoolDetails poolDetails;
    private MemberOperations memberOperations;
    private  ArrayList<Integer> membersList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_pick_winner, container, false);
        initUI();
        getPoolMembers();
        mButtonPickWinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            pickWinner();
            }
        });
        return mView;
    }

    private void initUI(){
        mButtonPickWinner = mView.findViewById(R.id.bt_pick_winner);
        mBundle = getArguments();
        poolDetails = (PoolDetails) mBundle.getSerializable(Utils.poolDetailsTable);
        memberOperations = MemberOperations.getInstance(getContext());
    }

    private void getPoolMembers(){
        membersList = memberOperations.printPoolMemberRemainingToWin(poolDetails);
    }

    private void pickWinner(){
        int winner = memberOperations.pickWinnerForCurrentMonth(poolDetails);
        Toast.makeText(getContext(), "Winner is "+winner,Toast.LENGTH_SHORT).show();

    }
}
