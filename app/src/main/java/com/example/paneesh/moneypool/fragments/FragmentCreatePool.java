package com.example.paneesh.moneypool.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paneesh.moneypool.DateCalculationsUtil;
import com.example.paneesh.moneypool.R;
import com.example.paneesh.moneypool.Utils;
import com.example.paneesh.moneypool.database_helper.MemberOperations;
import com.example.paneesh.moneypool.model.Member;
import com.example.paneesh.moneypool.model.PoolDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class FragmentCreatePool extends Fragment {

    private View mView;
    private TextView mEndDate;
    private TextView mMonthlyTakeAway;
    private EditText mPoolName;
    private EditText mPoolStrength;
    private EditText mPoolDuration;
    private EditText mPoolIndividualShare;
    private EditText mPoolStartDate;
    private EditText mPoolDepositDate;
    private EditText mPoolMeetUpDate;
    private EditText mPoolLateFee;
    private Button mRegisterPool;
    private java.sql.Date endDateSql;
    private java.sql.Date startDateSql;
    private SharedPreferences mSharedPreferences;
    private MemberOperations databaseHelper;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fargment_create_pool, container, false);
        initUI();

        TextWatcher textWatcher = null;


        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isDateValid() && validateFields()) {
                    mRegisterPool.setEnabled(true);
                    mEndDate.setText(calculateEndDate(mPoolStartDate.getText().toString()));
                    mMonthlyTakeAway.setText(calaulateMonthlytakeAway());
                } else {
                    mRegisterPool.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        mPoolStartDate.addTextChangedListener(textWatcher);
        mPoolName.addTextChangedListener(textWatcher);
        mPoolStrength.addTextChangedListener(textWatcher);
        mPoolIndividualShare.addTextChangedListener(textWatcher);
        mPoolMeetUpDate.addTextChangedListener(textWatcher);
        mPoolDepositDate.addTextChangedListener(textWatcher);
        mPoolLateFee.addTextChangedListener(textWatcher);

        mRegisterPool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            createPoolinDb();
            }
        });

        return mView;
    }

    private void initUI() {
        mPoolName = mView.findViewById(R.id.et_create_pool_name);
        mPoolDuration = mView.findViewById(R.id.et_create_pool_duration);
        mPoolStrength = mView.findViewById(R.id.et_create_pool_strength);
        mPoolIndividualShare = mView.findViewById(R.id.et_create_pool_individual_share);
        mPoolStartDate = mView.findViewById(R.id.et_create_pool_start_date);
        mPoolDepositDate = mView.findViewById(R.id.et_create_pool_deposit_date);
        mPoolMeetUpDate = mView.findViewById(R.id.et_create_pool_meet_up_date);
        mPoolLateFee = mView.findViewById(R.id.et_create_pool_late_fee);
        mEndDate = mView.findViewById(R.id.tv_create_pool_end_date);
        mMonthlyTakeAway = mView.findViewById(R.id.tv_create_pool_takeaway);
        mRegisterPool = mView.findViewById(R.id.bt_create_pool);
        mSharedPreferences = getActivity().getSharedPreferences(Utils.MyPREFERENCES, MODE_PRIVATE);
        databaseHelper = MemberOperations.getInstance(getContext());
    }


    private boolean isDateValid() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Utils.datePattern);
        simpleDateFormat.setLenient(false);
        try {
            Date date = simpleDateFormat.parse(mPoolStartDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @NonNull
    private String calculateEndDate(String date) {
        Date startDate = DateCalculationsUtil.stringToDateParse(date);
        startDateSql = new java.sql.Date(startDate.getTime());
        String duration = mPoolDuration.getText().toString();

        Date endDate = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Utils.datePattern);
        endDate = DateCalculationsUtil.addMonth(startDate, Integer.parseInt(duration));
         endDateSql = new java.sql.Date(endDate.getTime());

        return endDateSql.toString();
    }

    private boolean validateFields() {
        if (mPoolName.getText().toString().trim().isEmpty() || mPoolDuration.getText().toString().isEmpty() || mPoolStrength.getText().toString().isEmpty() ||
                mPoolIndividualShare.getText().toString().isEmpty() || mPoolMeetUpDate.getText().toString().isEmpty() || mPoolDepositDate.getText().toString().isEmpty() || mPoolLateFee.getText().toString().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private String calaulateMonthlytakeAway() {
        int individualShare = Integer.parseInt(mPoolIndividualShare.getText().toString());
        int strength = Integer.parseInt(mPoolStrength.getText().toString());
        return String.valueOf((individualShare * strength));
    }

    private void createPoolinDb() {
        PoolDetails mPoolDetails = new PoolDetails();
        mPoolDetails.setPoolAdminId(mSharedPreferences.getInt(Utils.memberId, 0));
        mPoolDetails.setPoolName( mPoolName.getText().toString());
        mPoolDetails.setPoolStrength(Integer.parseInt(mPoolStrength.getText().toString()));
        mPoolDetails.setPoolCurrentCounter(-1);
        mPoolDetails.setPoolDuration(Integer.parseInt(mPoolDuration.getText().toString()));
        mPoolDetails.setPoolStartDate(startDateSql);
        mPoolDetails.setPoolEndDate(endDateSql);
        mPoolDetails.setPoolDepositDate(Integer.parseInt(mPoolDepositDate.getText().toString()));
        mPoolDetails.setPoolMeetUpDate(Integer.parseInt(mPoolMeetUpDate.getText().toString()));
        mPoolDetails.setPoolMonthlyTakeAway(Integer.parseInt(mMonthlyTakeAway.getText().toString()));
        mPoolDetails.setPoolIndividualShare(Double.parseDouble(mPoolIndividualShare.getText().toString()));
        mPoolDetails.setPoolLateFeeCharge(Integer.parseInt(mPoolLateFee.getText().toString()));

        databaseHelper.insertPool(mPoolDetails);
        Toast.makeText(getContext(), "DataInserted", Toast.LENGTH_SHORT).show();
    }
}
