package com.example.paneesh.moneypool.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.paneesh.moneypool.R;
import com.example.paneesh.moneypool.Utils;
import com.example.paneesh.moneypool.database_helper.MemberOperations;
import com.example.paneesh.moneypool.model.PoolDetails;

import java.util.ArrayList;

public class FragmentRecordPayment extends Fragment {


    private View mView;
    private Spinner mSpinner;
    private MemberOperations memberOperations;
    private Bundle mBundle;
    private Button mButtonSavePayment;
    private PoolDetails poolDetails;
    private AlertDialog.Builder alertdialogBuilder;
    private AlertDialog alertDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_record_payment, container, false);
        initUI();
        mButtonSavePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (memberOperations.getCountRegisteredMembers(poolDetails.getPoolId()) == poolDetails.getPoolStrength()){
                   recordPayment();
               }else {
                   poolIsNotFull("Pool is not full, There are members who are yet to join!");
               }

            }
        });
        return mView;
    }

    private void initUI(){
        mSpinner = mView.findViewById(R.id.sp_record_payment);
        memberOperations = MemberOperations.getInstance(getContext());
        mBundle = getArguments();
        mButtonSavePayment = mView.findViewById(R.id.bt_save_payment);
        poolDetails = (PoolDetails) mBundle.getSerializable(Utils.poolDetailsTable);
        setSpinner();

    }

    private void setSpinner(){
        ArrayList<Integer> memberIdList = new ArrayList<>();
        memberIdList = memberOperations.getPoolMembersRemainingToPay(poolDetails);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getContext(),android.R.layout.simple_spinner_item, memberIdList );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mSpinner.setAdapter(adapter);
    }

    private void displayMessage(int code){
        switch (code){
            case 1: paymentSuccess("Transaction Updated ");
            break;
            case  2: paymentSuccess("Transaction added");
            break;
            case 3: paymentSuccess("Pool is Completed");
            break;
            case 4: paymentSuccess("Pick winner for Current Cycle first");
                break;
            default: paymentSuccess(" test 123");
            break;
        }
    }

    private void paymentSuccess(String message) {
        alertdialogBuilder = new AlertDialog.Builder(getActivity());
        alertdialogBuilder.setMessage(message);
        alertdialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               setSpinner();
            }
        });
        alertDialog = alertdialogBuilder.create();
        alertDialog.show();
    }


    private void recordPayment(){
        int code = 0;
        code  =  memberOperations.makePaymentForMember(poolDetails, Integer.parseInt(mSpinner.getSelectedItem().toString()));
        displayMessage(code);
    }

    private void poolIsNotFull(String message) {
        alertdialogBuilder = new AlertDialog.Builder(getActivity());
        alertdialogBuilder.setMessage(message);
        alertdialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog = alertdialogBuilder.create();
        alertDialog.show();
    }

}
