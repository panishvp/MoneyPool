package com.example.paneesh.moneypool.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paneesh.moneypool.R;
import com.example.paneesh.moneypool.Utils;
import com.example.paneesh.moneypool.database_helper.MemberOperations;
import com.example.paneesh.moneypool.model.PoolDetails;
import com.example.paneesh.moneypool.model.PoolTransactions;

import static android.content.Context.MODE_PRIVATE;

public class FragmentJoinPoolDetails extends Fragment {

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
    private SharedPreferences mSharedPrefs;
    private MemberOperations dataBaseHelper;
    private PoolDetails poolDetails;
    private PoolTransactions poolTransactions;
    private Button mButtonJoinPool;
    private Button mButtonSearchAnotherPool;
    private int poolId;
    private int memberId;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_pool_details, container, false);
        initUI();
        Bundle bundle = getArguments();
        final int poolId = Integer.parseInt(bundle.getString(Utils.poolId));
        poolDetails = dataBaseHelper.fetchPoolDetails(poolId);
        displayPoolDetails(poolDetails);
        mButtonSearchAnotherPool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadJoinPoolFragment();
            }
        });

        mButtonJoinPool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             poolTransactions.setPoolMemberId(memberId);
             poolTransactions.setPoolId(poolId);
             if (validateMemberAndPool(memberId, poolId)){
                 dataBaseHelper.enrollMember(poolTransactions);
             }

            }
        });
        return mView;
    }

    private void initUI(){
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
        mTextViewPoolLateFee = mView.findViewById(R.id.tv_pool_late_fee);
        mSharedPrefs = getActivity().getSharedPreferences(Utils.MyPREFERENCES, MODE_PRIVATE);
        poolId = mSharedPrefs.getInt(Utils.poolId, 0);
        memberId = mSharedPrefs.getInt(Utils.memberId, 0);
        poolTransactions = new PoolTransactions();
        poolDetails = new PoolDetails();
        dataBaseHelper = MemberOperations.getInstance(getContext());
        mButtonJoinPool = mView.findViewById(R.id.bt__join_pool);
        mButtonSearchAnotherPool = mView.findViewById(R.id.bt_deny_join_pool);
    }


    private void displayPoolDetails(PoolDetails poolDetails){

        mTextViewPoolName.setText(poolDetails.getPoolName());
        mTextViewPoolId.setText(String.valueOf(poolDetails.getPoolId()));
        mTextViewPoolDuration.setText(String.valueOf(poolDetails.getPoolDuration()));
        mTextViewPoolStrength.setText(String.valueOf(poolDetails.getPoolStrength()));
        mTextViewPoolCurrentCount.setText(String.valueOf(poolDetails.getPoolCurrentCounter()));
        mTextViewPoolIndividualShare.setText(String.valueOf(poolDetails.getPoolIndividualShare()));
        mTextViewPoolMonthlyTakeAway.setText(String.valueOf(poolDetails.getPoolMonthlyTakeAway()));
        mTextViewPoolStartDate.setText(String.valueOf(poolDetails.getPoolStartDate()));
        mTextViewPoolEndDate.setText(String.valueOf(poolDetails.getPoolEndDate()));
        mTextViewPoolMeetUpDate.setText(poolDetails.getPoolMeetUpDate()+" of Every Month");
        mTextViewPoolDepositDate.setText(poolDetails.getPoolDepositDate()+" of Every Month");
        mTextViewPoolLateFee.setText(poolDetails.getPoolLateFeeCharge()+"%");
    }

    private void loadJoinPoolFragment() {
        FragmentJoinPools fragmentJoinPools = new FragmentJoinPools();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_member_home, fragmentJoinPools);
        fragmentTransaction.commit();
    }

    private boolean validateMemberAndPool(int memberId, int poolid){
        boolean status = false;
        if (dataBaseHelper.isValidPoolJoin(poolid)){
            if (!dataBaseHelper.isMemberInThepool(memberId, poolid)){
                status = true;
            }else {
                Toast.makeText(getContext(), "You have already enrolled in this Pool ", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getContext(), "Sorry The Pool Is already Full ", Toast.LENGTH_SHORT).show();
        }
        return status;
    }
}
