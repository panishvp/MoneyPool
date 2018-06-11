package com.example.paneesh.moneypool.fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.paneesh.moneypool.R;
import com.example.paneesh.moneypool.Utils;
import com.example.paneesh.moneypool.database_helper.MemberOperations;
import com.example.paneesh.moneypool.model.Member;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class FragmentDashBoard extends Fragment {

    private View mVIew;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private MemberOperations databaseHelper;
    private Member memberObject;
    private MemberOperations memberOperations;
    private float[] yData = {25.3f, 30.6f};
    private String[] xData = {"Mitch", "Jessica", "Mohammad", "Kelsey", "Sam", "Robert", "Ashley"};
    private PieChart mPiechart;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mVIew = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initUI();
        fetchDataFromDB();
        saveInSharedPrefs(memberObject.getMemberID());
        creatmPiechart();
        return mVIew;
    }

    private void fetchDataFromDB() {
        String email = mSharedPreferences.getString(Utils.memberEmail, "");
        String password = mSharedPreferences.getString(Utils.memberPassword, "");
        memberObject = databaseHelper.fetchMemberDetails(email, password);
    }

    private void saveInSharedPrefs(int memberID) {
        mEditor = mSharedPreferences.edit();
        mEditor.putInt(Utils.memberId, memberID);
        mEditor.commit();
    }


    private void initUI() {
        memberOperations = new MemberOperations(getContext());
        databaseHelper = MemberOperations.getInstance(getContext());
        mSharedPreferences = getActivity().getSharedPreferences(Utils.MyPREFERENCES, MODE_PRIVATE);
        mPiechart = mVIew.findViewById(R.id.mp_pie_chart);
    }

    private void creatmPiechart() {
        // mPiechart.setDescription("Sales by employee (In Thousands $) ");
        mPiechart.setRotationEnabled(true);
        //mPiechart.setUsePercentValues(true);
        //mPiechart.setHoleColor(Color.BLUE);
        //mPiechart.setCenterTextColor(Color.BLACK);
        mPiechart.setHoleRadius(25f);
        mPiechart.setTransparentCircleAlpha(0);
        mPiechart.setCenterText("Super Cool Chart");
        mPiechart.setCenterTextSize(10);
        //mPiechart.setDrawEntryLabels(true);
        //mPiechart.setEntryLabelTextSize(20);
        //More options just check out the documentation!

        addDataSet();
    }

    private void addDataSet() {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for (int i = 0; i < yData.length; i++) {
            yEntrys.add(new PieEntry(yData[i], i));
        }

        for (int i = 1; i < xData.length; i++) {
            xEntrys.add(xData[i]);
        }

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Employee Sales");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        if (yData[0]> yData[1]){
            colors.add(Color.RED);
        }else {
            colors.add(Color.GREEN);
        }

        pieDataSet.setColors(colors);

        //add legend to chart
        Legend legend = mPiechart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        mPiechart.setData(pieData);
        mPiechart.invalidate();
    }
}

