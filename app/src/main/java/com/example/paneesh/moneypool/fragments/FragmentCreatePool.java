package com.example.paneesh.moneypool.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paneesh.moneypool.DateCalculationsUtil;
import com.example.paneesh.moneypool.R;
import com.example.paneesh.moneypool.Utils;
import com.example.paneesh.moneypool.activities.LandingPage;
import com.example.paneesh.moneypool.database_helper.MemberOperations;
import com.example.paneesh.moneypool.model.PoolDetails;
import com.example.paneesh.moneypool.model.PoolTransactions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class FragmentCreatePool extends Fragment {

    private View mView;
    private TextView mEndDate;
    private EditText mMonthlyTakeAway;
    private EditText mPoolName;
    private EditText mPoolStrength;
    private EditText mPoolDuration;
    private EditText mPoolIndividualShare;
    private TextView mPoolStartDate;
    private EditText mPoolDepositDate;
    private EditText mPoolMeetUpDate;
    private EditText mPoolLateFee;
    private Button mRegisterPool;
    private java.sql.Date endDateSql;
    private java.sql.Date startDateSql;
    private SharedPreferences mSharedPreferences;
    private MemberOperations databaseHelper;
    private PoolDetails mPoolDetails;
    private PoolTransactions poolTransactions;
    private AlertDialog.Builder alertdialogBuilder;
    private AlertDialog alertDialog;
    private DateCalculationsUtil dateCalculationsUtil;
    private DatePickerDialog.OnDateSetListener dateSetListener;


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
                if (validateFields() && isDateValid(mPoolStartDate.getText().toString())) {
                    mRegisterPool.setEnabled(true);
                    mEndDate.setText(calculateEndDate(mPoolStartDate.getText().toString()));
                    mMonthlyTakeAway.setText(calaulateMonthlytakeAway());
                    mPoolStrength.setText(mPoolDuration.getText().toString());
                } else {
                    mRegisterPool.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        mPoolStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,dateSetListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1 ;
                mPoolStartDate.setText(dayOfMonth+"-"+month+"-"+year);
            }
        };


        //mPoolStartDate.addTextChangedListener(textWatcher);
        mPoolName.addTextChangedListener(textWatcher);
        mPoolIndividualShare.addTextChangedListener(textWatcher);
        mPoolMeetUpDate.addTextChangedListener(textWatcher);
        mPoolDepositDate.addTextChangedListener(textWatcher);
        mPoolLateFee.addTextChangedListener(textWatcher);
        mPoolStartDate.addTextChangedListener(textWatcher);


        mRegisterPool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askUserToEnroll();
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
        mMonthlyTakeAway = mView.findViewById(R.id.et_create_pool_takeaway);
        mRegisterPool = mView.findViewById(R.id.bt_create_pool);
        mSharedPreferences = getActivity().getSharedPreferences(Utils.MyPREFERENCES, MODE_PRIVATE);
        databaseHelper = MemberOperations.getInstance(getContext());
        dateCalculationsUtil = new DateCalculationsUtil();
    }


    @NonNull
    private String calculateEndDate(String date) {
        Date startDate = dateCalculationsUtil.stringToDateParse(date);
        startDateSql = new java.sql.Date(startDate.getTime());
        String duration = mPoolDuration.getText().toString();

        Date endDate = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Utils.datePattern);
        endDate = dateCalculationsUtil.addMonth(startDate, Integer.parseInt(duration));
        endDateSql = new java.sql.Date(endDate.getTime());

        return simpleDateFormat.format(endDateSql);
    }

    private boolean validateFields() {
        if (mPoolName.getText().toString().trim().isEmpty() || mPoolDuration.getText().toString().isEmpty() ||
                mPoolIndividualShare.getText().toString().isEmpty() || mPoolMeetUpDate.getText().toString().isEmpty() || mPoolDepositDate.getText().toString().isEmpty()
                || mPoolLateFee.getText().toString().isEmpty() ) {
            return false;
        } else {
            return true;
        }
    }

    private String calaulateMonthlytakeAway() {
        int individualShare = Integer.parseInt(mPoolIndividualShare.getText().toString());
        int strength = Integer.parseInt(mPoolDuration.getText().toString());
        return String.valueOf((individualShare * strength));
    }

    private void createPoolinDb() {
        mPoolDetails = new PoolDetails();
        mPoolDetails.setPoolAdminId(mSharedPreferences.getInt(Utils.memberId, 0));
        mPoolDetails.setPoolName(mPoolName.getText().toString());
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
    }

    private void askUserToEnroll() {
        AlertDialog.Builder alertdialogBuilder = new AlertDialog.Builder(getActivity());
        alertdialogBuilder.setMessage("Do you want to enroll in this Pool as a Member");
        alertdialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createPoolinDb();
                enrollAdmin(mSharedPreferences.getInt(Utils.memberId, 0), databaseHelper.getPoolID(mPoolDetails));
            }
        });
        alertdialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createPoolinDb();
            }
        });
        AlertDialog alertDialog = alertdialogBuilder.create();
        alertDialog.show();
    }



    private boolean validateMemberAndPool(int memberId, int poolid) {
        boolean status = false;
        if (databaseHelper.isValidPoolJoin(poolid)) {
            if (!databaseHelper.isMemberInThepool(memberId, poolid)) {
                status = true;
            } else {
                Toast.makeText(getContext(), "You have already enrolled in this Pool ", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Sorry The Pool Is already Full ", Toast.LENGTH_SHORT).show();
        }
        return status;
    }

    private void enrollAdmin(int memberId, int poolId) {
        if (validateMemberAndPool(memberId, poolId)) {
            poolTransactions = new PoolTransactions();
            poolTransactions.setPoolId(poolId);
            poolTransactions.setPoolMemberId(memberId);
            alertUser();
        }
    }

    private void alertUser() {
        alertdialogBuilder = new AlertDialog.Builder(getActivity());
        alertdialogBuilder.setMessage("Terms and Conditions of the Pool");
        alertdialogBuilder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHelper.enrollMember(poolTransactions);
                enrollmentSuccess();
            }
        });
        alertdialogBuilder.setNegativeButton("Don't Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog = alertdialogBuilder.create();
        alertDialog.show();
    }

    private void enrollmentSuccess() {
        alertdialogBuilder = new AlertDialog.Builder(getActivity());
        alertdialogBuilder.setMessage("Congragulations! \n You are Successfully Enrolled");
        alertdialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseHelper.enrollMember(poolTransactions);
                Intent intent = new Intent(getContext(), LandingPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        alertDialog = alertdialogBuilder.create();
        alertDialog.show();
    }

    private boolean isDateValid(String date){
       boolean status = false;
        SimpleDateFormat sdfrmt = new SimpleDateFormat(Utils.datePattern);
        try {
            if (sdfrmt.parse(date).before(new Date())){
                status = false;
            }else {
                status = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return status;
    }


}