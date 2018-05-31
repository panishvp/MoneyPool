package com.example.paneesh.moneypool.fragments;

import android.content.Intent;
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
import android.widget.TextView;

import com.example.paneesh.moneypool.R;
import com.example.paneesh.moneypool.activities.PoolDetailsContainer;
import com.example.paneesh.moneypool.adapters.MyPoolListAdapter;
import com.example.paneesh.moneypool.model.PoolDetails;

import java.util.ArrayList;

public class FragmentAdminPool extends Fragment implements MyPoolListAdapter.onPoolItemClickListener{

    private View mView;
    private RecyclerView mMyPoolRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MyPoolListAdapter adapter;
    private ArrayList<PoolDetails> poolDetailsArrayList;
    private FloatingActionButton mFloatingActionButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_admin_pool_list, container, false);
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
                loadCreatePoolFragment();
            }
        });
        return mView;
    }

    private void initUI() {
        mMyPoolRecyclerView = mView.findViewById(R.id.rv_admin_pool_list);
        mFloatingActionButton = mView.findViewById(R.id.fab_create_pool);
    }

    private void loadData(){

        poolDetailsArrayList = new ArrayList<>();

        for (int i=1; i<=20; i++){
            poolDetailsArrayList.add(new PoolDetails());
        }
    }

    @Override
    public void onItemClick(String name) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(getContext(), PoolDetailsContainer.class);
        bundle.putString("poolName", name);
        bundle.putString("role","admin");
        intent.putExtras(bundle);
        startActivity(intent);

    }

    private void loadCreatePoolFragment(){
        FragmentCreatePool fragmentCreatePool = new FragmentCreatePool();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_member_home, fragmentCreatePool);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
