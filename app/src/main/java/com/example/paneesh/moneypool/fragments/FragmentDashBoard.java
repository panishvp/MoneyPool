package com.example.paneesh.moneypool.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.paneesh.moneypool.R;

public class FragmentDashBoard extends Fragment {

    private View mVIew;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mVIew = inflater.inflate(R.layout.fragment_dashboard, container, false);
        return mVIew;
    }
}
