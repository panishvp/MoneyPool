package com.example.paneesh.moneypool.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.Toast;

import com.example.paneesh.moneypool.R;
import com.example.paneesh.moneypool.Utils;
import com.example.paneesh.moneypool.database_helper.MemberOperations;
import com.example.paneesh.moneypool.model.Member;

import static android.content.Context.MODE_PRIVATE;

public class FragmentUpdateDetails extends Fragment {

    private View mView;
    private Spinner mSpinnerUpdateField;
    private LinearLayout mLinearLayout;
    private TextView mTextFieldToUpdate;
    private TextView mTextViewDataOfField;
    private EditText mEditTextVakueToUpdate;
    private EditText mEditTextOldPassword;
    private MemberOperations databaseOperations;
    private SharedPreferences mSharedPreferences;
    private TextView mTextViewInformUser;
    private String columnName;
    private Button mButtonUpdate;
    private Member member;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_update_details, container, false);
        initUI();
        setSpinner();
        mButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditTextVakueToUpdate.getText().length() > 4) {
                        updateMemberDetailsInDb(mEditTextVakueToUpdate.getText().toString());
                    FragmentHome fragmentHome = new FragmentHome();
                    replaceFragment(fragmentHome);

                } else {
                    Toast.makeText(getContext(), Utils.alertUserForValidInput, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return mView;
    }

    private void initUI() {
        mSpinnerUpdateField = mView.findViewById(R.id.sp_update_member);
        mLinearLayout = mView.findViewById(R.id.ll_update_dialog);
        databaseOperations = new MemberOperations(getContext());
        mTextFieldToUpdate = mView.findViewById(R.id.tv_selected_field_to_update);
        mTextViewDataOfField = mView.findViewById(R.id.tv_data_from_db);
        mSharedPreferences = getActivity().getSharedPreferences(Utils.MyPREFERENCES, MODE_PRIVATE);
        mButtonUpdate = mView.findViewById(R.id.bt_update_member_details);
        mTextViewInformUser = mView.findViewById(R.id.tv_inform_user_update);
        mEditTextOldPassword = mView.findViewById(R.id.et_old_password);
        mEditTextVakueToUpdate = mView.findViewById(R.id.et_update_value);
        member = new Member();
        member = databaseOperations.fetchMemberDetails(mSharedPreferences.getString(Utils.memberEmail, ""), mSharedPreferences.getString(Utils.memberPassword, ""));
    }

    private void setSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mView.getContext(), R.array.member_details, android.R.layout.simple_spinner_item);
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
            case "Address":
                mTextViewDataOfField.setText(member.getMemberAddress());
                mEditTextOldPassword.setVisibility(View.GONE);
                columnName = Utils.memberAddress;
                break;
            case "Iban":
                mTextViewDataOfField.setText(member.getMemberIban());
                mEditTextOldPassword.setVisibility(View.GONE);
                columnName = Utils.memberIban;
                break;
            case "SwiftCode":
                mTextViewDataOfField.setText(member.getMemberSwiftCode());
                mEditTextOldPassword.setVisibility(View.GONE);
                columnName = Utils.memberSwift;
                break;
            case "Phone Number":
                mTextViewDataOfField.setText(member.getMemberPhoneNumber());
                mEditTextOldPassword.setVisibility(View.GONE);
                columnName = Utils.memberPhoneNumber;
                break;
            case "Nominee":
                mTextViewDataOfField.setText(member.getMemberNominee());
                mEditTextOldPassword.setVisibility(View.GONE);
                columnName = Utils.memberNominee;
                break;
            case "Password":
                mTextViewDataOfField.setText("******");
                mEditTextOldPassword.setVisibility(View.VISIBLE);
                columnName = Utils.memberPassword;
                mTextViewInformUser.setText("Please Enter Your Old and New Password");
                break;
        }
    }

    private void updateMemberDetailsInDb( String valueToUpdate){

        if (columnName.equals("Password")){
            verifyPassword(mEditTextOldPassword.getText().toString(), valueToUpdate);
        }else {

            databaseOperations.updateMemberDetails(columnName, mEditTextVakueToUpdate.getText().toString(),mSharedPreferences.getInt(Utils.memberId, 0) );
            Toast.makeText(getActivity(), "Data Updated", Toast.LENGTH_SHORT).show();
        }
    }

    private void verifyPassword(String oldPassword, String newPassword){
        if (oldPassword.length()> 4){
            if (oldPassword.equals(mSharedPreferences.getString(Utils.memberPassword, ""))){
                databaseOperations.updateMemberDetails(columnName, newPassword, mSharedPreferences.getInt(Utils.memberId, 0));
                Toast.makeText(getActivity(), "Data Updated", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getActivity(), "the Old Password you entered is wrong", Toast.LENGTH_SHORT).show();
            }
        }else {

        }
    }


    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_member_home, fragment);
        fragmentTransaction.commit();
    }

}
