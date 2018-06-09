package com.example.paneesh.moneypool.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.paneesh.moneypool.R;
import com.example.paneesh.moneypool.Utils;
import com.example.paneesh.moneypool.adapters.PoolPaymentHistoryAdapter;
import com.example.paneesh.moneypool.database_helper.MemberOperations;
import com.example.paneesh.moneypool.model.PoolDetails;
import com.example.paneesh.moneypool.model.PoolTransactions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class FragmentAdminPoolDetails extends Fragment {

    private View mView;
    private TextView mTextViewPoolName;
    private TextView mTextViewPoolId;
    private TextView mTextViewPoolDuration;
    private TextView mTextViewPoolStrength;
    private TextView mTextViewPoolCurrentCount;
    private TextView mTextViewPoolIndividualShare;
    private TextView mTextViewPoolMonthlyTakeAway;
    private TextView mTextViewPoolStartDate;
    private TextView mTextViewPoolEndDate;
    private TextView mTextViewPoolMeetUpDate;
    private TextView mTextViewPoolDepositDate;
    private TextView mTextViewPoolLateFee;
    private Button mButtonJoinPool;
    private Button mButtonPickWinner;
    private Button mButtonSearchAnotherPool;
    private Button mButtonRecordPayment;
    private SharedPreferences mSharedPrefs;
    private MemberOperations dataBaseHelper;
    private PoolDetails poolDetails;
    private int adminId;
    private RecyclerView paymentHistoryRecyclerView;
    private PoolPaymentHistoryAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private LinearLayout linearLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_pool_details, container, false);
        initUI();
        Bundle bundle = getArguments();
        int poolId = Integer.parseInt(bundle.getString(Utils.poolId));
        poolDetails = dataBaseHelper.fetchPoolDetails(poolId);
        displayPoolDetails(poolDetails);
        mButtonRecordPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentRecordPayment fragmentRecordPayment = new FragmentRecordPayment();
                loadFragmentPayment(fragmentRecordPayment);
            }
        });

        mButtonPickWinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentPickWinner fragmentPickWinner = new FragmentPickWinner();
                loadFragmentPayment(fragmentPickWinner);
            }
        });
        setRecyclerView();

        return mView;
    }


    private void initUI() {
        mTextViewPoolName = mView.findViewById(R.id.tv_pool_name);
        mTextViewPoolId = mView.findViewById(R.id.tv_pool_id);
        mTextViewPoolDuration = mView.findViewById(R.id.tv_pool_duration);
        mTextViewPoolStrength = mView.findViewById(R.id.tv_pool_strength);
        mTextViewPoolCurrentCount = mView.findViewById(R.id.tv_pool_current_counter);
        mTextViewPoolIndividualShare = mView.findViewById(R.id.tv_pool_individual_share);
        mTextViewPoolMonthlyTakeAway = mView.findViewById(R.id.tv_pool_monthly_takeaway);
        mTextViewPoolStartDate = mView.findViewById(R.id.tv_pool_start_date);
        mTextViewPoolEndDate = mView.findViewById(R.id.tv_pool_end_date);
        mTextViewPoolMeetUpDate = mView.findViewById(R.id.tv_pool_meetup);
        mTextViewPoolDepositDate = mView.findViewById(R.id.tv_pool_deposite_date);
        mButtonRecordPayment = mView.findViewById(R.id.bt_record_payment);
        mButtonRecordPayment.setVisibility(View.VISIBLE);
        mButtonPickWinner = mView.findViewById(R.id.bt_pick_winner_fragment);
        mButtonPickWinner.setVisibility(View.VISIBLE);
        mTextViewPoolLateFee = mView.findViewById(R.id.tv_pool_late_fee);
        mSharedPrefs = getActivity().getSharedPreferences(Utils.MyPREFERENCES, MODE_PRIVATE);
        adminId = mSharedPrefs.getInt(Utils.poolId, 0);
        poolDetails = new PoolDetails();
        dataBaseHelper = MemberOperations.getInstance(getContext());
        mButtonJoinPool = mView.findViewById(R.id.bt__join_pool);
        mButtonJoinPool.setVisibility(View.GONE);
        mButtonSearchAnotherPool = mView.findViewById(R.id.bt_deny_join_pool);
        mButtonSearchAnotherPool.setVisibility(View.GONE);
        paymentHistoryRecyclerView = mView.findViewById(R.id.rv_payment_history);
        linearLayout = mView.findViewById(R.id.ll_admin_pool_transactions);
    }


    private void displayPoolDetails(PoolDetails poolDetails) {

        mTextViewPoolName.setText(poolDetails.getPoolName());
        mTextViewPoolId.setText(String.valueOf(poolDetails.getPoolId()));
        mTextViewPoolDuration.setText(String.valueOf(poolDetails.getPoolDuration()));
        mTextViewPoolStrength.setText(String.valueOf(poolDetails.getPoolStrength()));
        mTextViewPoolCurrentCount.setText(String.valueOf(poolDetails.getPoolCurrentCounter()));
        mTextViewPoolIndividualShare.setText(String.valueOf(poolDetails.getPoolIndividualShare()));
        mTextViewPoolMonthlyTakeAway.setText(String.valueOf(poolDetails.getPoolMonthlyTakeAway()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Utils.datePattern);
        mTextViewPoolStartDate.setText(simpleDateFormat.format(poolDetails.getPoolStartDate()));
        mTextViewPoolEndDate.setText(simpleDateFormat.format(poolDetails.getPoolEndDate()));
        mTextViewPoolMeetUpDate.setText(poolDetails.getPoolMeetUpDate() + " of Every Month");
        mTextViewPoolDepositDate.setText(poolDetails.getPoolDepositDate() + " of Every Month");
        mTextViewPoolLateFee.setText(poolDetails.getPoolLateFeeCharge() + "%");
    }

    private void loadFragmentPayment(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Utils.poolDetailsTable, poolDetails);
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_pool_transactions_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }

    private void setRecyclerView() {

        ArrayList<PoolTransactions> poolTransactionsList = dataBaseHelper.getPoolTransactions(poolDetails.getPoolId());
        if (poolTransactionsList.size() > 0){

            layoutManager = new LinearLayoutManager(getContext());
            paymentHistoryRecyclerView.setLayoutManager(layoutManager);
            adapter = new PoolPaymentHistoryAdapter(poolTransactionsList);
            paymentHistoryRecyclerView.setAdapter(adapter);
            linearLayout.setVisibility(View.VISIBLE);
        }
    }
}
