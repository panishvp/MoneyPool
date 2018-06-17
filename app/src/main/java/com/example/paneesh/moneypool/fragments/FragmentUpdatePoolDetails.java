package com.example.paneesh.moneypool.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import static android.content.Context.MODE_PRIVATE;

public class FragmentUpdatePoolDetails extends Fragment {

    private View mView;
    private Spinner mSpinnerUpdateField;
    private LinearLayout mLinearLayout;
    private TextView mTextFieldToUpdate;
    private TextView mTextViewDataOfField;
    private EditText mEditTextVakueToUpdate;
    private MemberOperations databaseOperations;
    private Button mButtonUpdate;
    private PoolDetails poolDetails;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_update_pool_details, container, false);
        initUI();
        setSpinner();
        if (poolDetails.getPoolCurrentCounter() == -1) {
            mButtonUpdate.setEnabled(true);
        }
        mButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditTextVakueToUpdate.getText().toString().trim().length() > 0) {
                    updatePoolDetails(mEditTextVakueToUpdate.getText().toString());
                }
            }
        });
        return mView;
    }

    private void initUI() {
        mSpinnerUpdateField = mView.findViewById(R.id.sp_update_pool_details);
        mLinearLayout = mView.findViewById(R.id.ll_update_dialog);
        databaseOperations = MemberOperations.getInstance(getContext());
        mTextFieldToUpdate = mView.findViewById(R.id.tv_selected_field_to_update_pool_details);
        mTextViewDataOfField = mView.findViewById(R.id.tv_data_from_db_pool_details);
        mButtonUpdate = mView.findViewById(R.id.bt_update_pool_details);
        mEditTextVakueToUpdate = mView.findViewById(R.id.et_update_value_pool_details);
        poolDetails = new PoolDetails();
        Bundle bundle = getArguments();
        poolDetails = (PoolDetails) bundle.getSerializable(Utils.poolDetailsTable);
    }

    private void setSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mView.getContext(), R.array.pool_details, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mSpinnerUpdateField.setAdapter(adapter);
        mSpinnerUpdateField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mSpinnerUpdateField.getSelectedItem().toString().equals("Select")) {
                    mLinearLayout.setVisibility(View.INVISIBLE);
                } else {
                    mLinearLayout.setVisibility(View.VISIBLE);
                    mTextFieldToUpdate.setText(mSpinnerUpdateField.getSelectedItem().toString());
                    collectUpdateDetails(mSpinnerUpdateField.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void collectUpdateDetails(String fieldToUpdate) {

        switch (fieldToUpdate) {
            case "PoolName":
                mTextViewDataOfField.setText(poolDetails.getPoolName());
                mEditTextVakueToUpdate.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                break;
            case "Pool Individual Share":
                mTextViewDataOfField.setText(" " + poolDetails.getPoolIndividualShare());
                mEditTextVakueToUpdate.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case "Pool Late Fee":
                mTextViewDataOfField.setText(" " + poolDetails.getPoolLateFeeCharge());
                mEditTextVakueToUpdate.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
        }

    }


    private void updatePoolDetails(String value) {
        if (mSpinnerUpdateField.getSelectedItem().toString().equals("PoolName")) {
            databaseOperations.updatePoolName(value, poolDetails.getPoolId());
            replaceFragment();
        } else if (mSpinnerUpdateField.getSelectedItem().toString().equals("Pool Individual Share")) {
            databaseOperations.updateIndividualShare(Double.parseDouble(value), poolDetails.getPoolId());
            replaceFragment();
        } else {
            databaseOperations.updateLateFeeCharge(Double.parseDouble(value), poolDetails.getPoolId());
            replaceFragment();
        }
    }

    private void replaceFragment() {
        Bundle mBundle = new Bundle();
        mBundle.putString(Utils.poolId, String.valueOf(poolDetails.getPoolId()));
        FragmentAdminPoolDetails fragment = new FragmentAdminPoolDetails();
        fragment.setArguments(mBundle);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_pool_transactions_container, fragment);
        fragmentTransaction.commit();
    }

}
