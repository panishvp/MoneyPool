package com.example.paneesh.moneypool.fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.paneesh.moneypool.R;
import com.example.paneesh.moneypool.Utils;
import com.example.paneesh.moneypool.adapters.MyTransactionsListAdapter;
import com.example.paneesh.moneypool.database_helper.MemberOperations;
import com.example.paneesh.moneypool.model.Member;
import com.example.paneesh.moneypool.model.PoolTransactions;
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
    private PieChart mPiechart;
    private RecyclerView recyclerViewMyTransactions;
    private MyTransactionsListAdapter myTransactionsListAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mVIew = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initUI();
        fetchDataFromDB();
        saveInSharedPrefs(memberObject.getMemberID());
        createPieChart();
        setRecyclerView();
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
        memberOperations = MemberOperations.getInstance(getContext());
        databaseHelper = MemberOperations.getInstance(getContext());
        mSharedPreferences = getActivity().getSharedPreferences(Utils.MyPREFERENCES, MODE_PRIVATE);
        mPiechart = mVIew.findViewById(R.id.mp_pie_chart);
        recyclerViewMyTransactions = mVIew.findViewById(R.id.rv_my_transactions);
    }

    private void createPieChart() {

        mPiechart.setRotationEnabled(true);
        mPiechart.setHoleRadius(45f);
        mPiechart.setTransparentCircleAlpha(0);
        mPiechart.setCenterText("Investments \nvs\nReturns");
        mPiechart.setCenterTextSize(15);

        addDataSet();
    }

    private void addDataSet() {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();

        int getTotalInvestments = memberOperations.getTotalPaymemntsForMember(memberObject.getMemberID()) + memberOperations.getTotalLateFeeForMember(memberObject.getMemberID());
        int getTakeAwayReturns = memberOperations.getTotalTakeawayForMember(memberObject.getMemberID());

        yEntrys.add(new PieEntry((float) getTotalInvestments, "Investments"));
        yEntrys.add(new PieEntry((float) getTakeAwayReturns, "Returns"));

        System.out.println("Data " + getTotalInvestments + " " + getTakeAwayReturns);

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "");
        pieDataSet.setSliceSpace(12);
        pieDataSet.setValueTextSize(12);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#FF9A23C2"));
        if (yEntrys.get(0).getValue() > yEntrys.get(1).getValue()) {
            colors.add(Color.parseColor("#FFF42828"));
        } else {
            colors.add(Color.parseColor("#FF06B114"));
        }

        pieDataSet.setColors(colors);


        Legend l = mPiechart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);


        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        mPiechart.setData(pieData);
        mPiechart.invalidate();
    }

    private void setRecyclerView() {
        ArrayList<PoolTransactions> poolTransactionsArrayList = memberOperations.getAllMemberTransactions(memberObject.getMemberID());
        if (poolTransactionsArrayList.size() > 0) {
            myTransactionsListAdapter = new MyTransactionsListAdapter(poolTransactionsArrayList);
            layoutManager = new LinearLayoutManager(getContext());
            recyclerViewMyTransactions.setLayoutManager(layoutManager);
            recyclerViewMyTransactions.setAdapter(myTransactionsListAdapter);

        }
    }
}

