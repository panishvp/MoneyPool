package com.example.paneesh.moneypool.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.paneesh.moneypool.R;
import com.example.paneesh.moneypool.Utils;
import com.example.paneesh.moneypool.database_helper.MemberOperations;
import com.example.paneesh.moneypool.model.PoolDetails;

public class FragmentPickWinner extends Fragment {


    private View mView;
    private Button mButtonPickWinner;
    private Bundle mBundle;
    private PoolDetails poolDetails;
    private MemberOperations memberOperations;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_pick_winner, container, false);
        initUI();
        mButtonPickWinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (memberOperations.isValidPickWinner(poolDetails)){
                pickWinner();
            }
            else
                Toast.makeText(getContext(), "Cannot Choose winner",Toast.LENGTH_SHORT).show();
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

    private void pickWinner(){
        int winner = memberOperations.pickWinnerForCurrentMonth(poolDetails);
        Bundle bundle = new Bundle();
        bundle.putInt(Utils.memberId, winner);
        bundle.putSerializable(Utils.poolDetailsTable, poolDetails);
        FragmentAuction fragment = new FragmentAuction();
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_pool_transactions_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}
