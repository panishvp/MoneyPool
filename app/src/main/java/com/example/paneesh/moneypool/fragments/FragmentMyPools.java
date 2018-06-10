package com.example.paneesh.moneypool.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.paneesh.moneypool.R;
import com.example.paneesh.moneypool.Utils;
import com.example.paneesh.moneypool.activities.PoolCreationAndJoin;
import com.example.paneesh.moneypool.activities.PoolDetailsContainer;
import com.example.paneesh.moneypool.adapters.MyPoolListAdapter;
import com.example.paneesh.moneypool.database_helper.MemberOperations;
import com.example.paneesh.moneypool.model.PoolDetails;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class FragmentMyPools extends Fragment implements MyPoolListAdapter.onPoolItemClickListener {

    private View mView;
    private RecyclerView mMyPoolRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MyPoolListAdapter adapter;
    private ArrayList<PoolDetails> poolDetailsArrayList;
    private FloatingActionButton mFloatingActionButton;
    private MemberOperations memberOperations;
    private SharedPreferences mSharedPrefs;
    private TextView mTextViewNoPoolsAlert;
    private TextView mTextViewCreatePool;
    private LinearLayout mLinearLayout;
    private Intent intent;
    private Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_my_pools_list, container, false);
        initUI();
        loadData();
        layoutManager = new LinearLayoutManager(getContext());
        mMyPoolRecyclerView.setLayoutManager(layoutManager);

        adapter = new MyPoolListAdapter(poolDetailsArrayList);
        adapter.setOnItemClickListener(this);
        mMyPoolRecyclerView.setAdapter(adapter);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadJoinPoolFragment();
            }
        });

        return mView;
    }

    private void initUI() {
        mMyPoolRecyclerView = mView.findViewById(R.id.rv_my_pool_list);
        mFloatingActionButton = mView.findViewById(R.id.fab_join_pool);
        memberOperations = MemberOperations.getInstance(getContext());
        mTextViewNoPoolsAlert = mView.findViewById(R.id.tv_no_pools_alert);
        mTextViewCreatePool = mView.findViewById(R.id.tv_join_new_pool);
        mLinearLayout = mView.findViewById(R.id.ll_no_pools_alert);

    }

    private void loadData() {
        mSharedPrefs = getActivity().getSharedPreferences(Utils.MyPREFERENCES, MODE_PRIVATE);
        int memberId = mSharedPrefs.getInt(Utils.memberId, 0);
        poolDetailsArrayList = memberOperations.fetchMypools(memberId);
        if (poolDetailsArrayList.size() == 0){
            mTextViewNoPoolsAlert.setText(Utils.joinPoolsAlert);
            mTextViewCreatePool.setText(Utils.joinNewPool);
        }else {
            mLinearLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(String name) {
        bundle = new Bundle();
        bundle.putString(Utils.poolId, name);
        bundle.putString("role", "member");
        intent = new Intent(getContext(), PoolDetailsContainer.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void loadJoinPoolFragment() {

        intent = new Intent(getContext(), PoolCreationAndJoin.class);
        bundle = new Bundle();
        bundle.putString("purpose", "join");
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    public void onResume() {
        super.onResume();
        loadData();
        adapter.notifyDataSetChanged();
    }


}
