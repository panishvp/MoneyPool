package com.example.paneesh.moneypool.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.paneesh.moneypool.R;

public class FragmentUpdateDetails extends Fragment {

    private View mView;
    private Spinner mSpinnerUpdateField;
    private LinearLayout mLinearLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_update_details, container, false);
        initUI();
        setSpinner();
        return mView;
    }

    private void initUI(){
        mSpinnerUpdateField = mView.findViewById(R.id.sp_update_member);
        mLinearLayout = mView.findViewById(R.id.ll_update_dialog);
    }

    private void setSpinner(){
        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(mView.getContext(), R.array.member_details,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mSpinnerUpdateField.setAdapter(adapter);
        mSpinnerUpdateField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mSpinnerUpdateField.getSelectedItem().toString().equals("Select")){
                    mLinearLayout.setVisibility(View.INVISIBLE);
                }else{
                    mLinearLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
