package com.example.paneesh.moneypool.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.paneesh.moneypool.R;
import com.example.paneesh.moneypool.Utils;
import com.example.paneesh.moneypool.database_helper.MemberOperations;
import com.example.paneesh.moneypool.model.PoolDetails;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentAuction extends Fragment {

    private View mView;
    private TextView mTextViewWinnerName;
    private Button buttonAcceptAuction;
    private Button denyAuction;
    private Spinner spinnerBidders;
    private EditText editTextAuctionPercentage;
    private Button buttonAddBidder;
    private LinearLayout linearLayout;
    private MemberOperations memberOperations;
    private PoolDetails poolDetails;
    private ProgressDialog progressDialog;
    private int memberId;
    private AlertDialog.Builder alertdialogBuilder;
    private AlertDialog alertDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_auction, container, false);
        initUI();
        setProgressDialog();
        buttonAcceptAuction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.VISIBLE);
            }
        });

        denyAuction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memberOperations.updatePickerFlagForWinner(poolDetails, memberId);
                pickerAdded("Transaction for " + memberId + " has been Updated Successfully");
            }
        });

        buttonAddBidder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memberOperations.addPicker(poolDetails, Integer.parseInt(spinnerBidders.getSelectedItem().toString()), Double.parseDouble(editTextAuctionPercentage.getText().toString()));
                pickerAdded("Transaction for " + spinnerBidders.getSelectedItem().toString() + " has been Updated Successfully");
            }
        });
        setSpinner();
        return mView;
    }

    private void initUI() {
        Bundle bundle = getArguments();
        memberId = bundle.getInt(Utils.memberId);
        poolDetails = (PoolDetails) bundle.getSerializable(Utils.poolDetailsTable);
        mTextViewWinnerName = mView.findViewById(R.id.tv_winner_name);
        buttonAcceptAuction = mView.findViewById(R.id.bt_accept_auction);
        denyAuction = mView.findViewById(R.id.bt_deney_auction);
        spinnerBidders = mView.findViewById(R.id.sp_auction_list);
        editTextAuctionPercentage = mView.findViewById(R.id.et_auction_percentage);
        buttonAddBidder = mView.findViewById(R.id.bt_add_picker);
        linearLayout = mView.findViewById(R.id.ll_auction_segment);
        memberOperations = MemberOperations.getInstance(getActivity());
        setWinnerName(memberId);
    }

    private void setWinnerName(int memberId) {
        mTextViewWinnerName.setText(memberOperations.getMemberName(memberId));
        if (poolDetails.getPoolCurrentCounter() == poolDetails.getPoolStrength()) {
            buttonAcceptAuction.setEnabled(false);
        }
    }

    private void setSpinner() {
        ArrayList<Integer> memberIdList = new ArrayList<>();
        memberIdList = memberOperations.printPoolMemberRemainingToWin(poolDetails);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_spinner_item, memberIdList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerBidders.setAdapter(adapter);
    }

    private void setProgressDialog() {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Picking the Winner");

        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                progressDialog.dismiss(); // when the task active then close the dialog
                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
            }
        }, 5000);
        progressDialog.setIcon(R.drawable.rolling_dice);
        progressDialog.show();
    }

    private void loadAdminPoolDetailsFragment() {
        FragmentAdminPoolDetails fragmentAdminPoolDetails = new FragmentAdminPoolDetails();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_pool_transactions_container, fragmentAdminPoolDetails);
        fragmentTransaction.commit();
    }

    private void pickerAdded(String message) {
        alertdialogBuilder = new AlertDialog.Builder(getActivity());
        alertdialogBuilder.setMessage(message);
        alertdialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadAdminPoolDetailsFragment();
            }
        });
        alertDialog = alertdialogBuilder.create();
        alertDialog.show();
    }

}
