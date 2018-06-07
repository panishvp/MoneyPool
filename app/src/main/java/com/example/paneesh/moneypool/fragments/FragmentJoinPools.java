package com.example.paneesh.moneypool.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paneesh.moneypool.R;
import com.example.paneesh.moneypool.Utils;
import com.example.paneesh.moneypool.database_helper.MemberOperations;
import com.example.paneesh.moneypool.model.PoolDetails;

public class FragmentJoinPools extends Fragment {

    private View mView;
    private EditText mEditTextPoolId;
    private Button mButtonJoinPool;
    private MemberOperations memberOperations;
    private PoolDetails poolDetails;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_join_pool, container, false);
        initUI();
        mButtonJoinPool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String poolId = mEditTextPoolId.getText().toString().trim();
                if (mEditTextPoolId.getText().toString().trim().length() > 0) {
                    if (memberOperations.isValidPool(Integer.parseInt(poolId))) {
                        loadJoinPoolFragment();
                    } else {
                        Toast.makeText(getContext(), "Pool doesn't exists", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Please Enter th Pool Id", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return mView;
    }

    private void initUI() {
        mEditTextPoolId = mView.findViewById(R.id.et_join_pool_id);
        mButtonJoinPool = mView.findViewById(R.id.bt_search_pool);
        memberOperations = MemberOperations.getInstance(getContext());
        poolDetails = new PoolDetails();
    }

    private void loadJoinPoolFragment() {
        Bundle bundle = new Bundle();
        bundle.putString(Utils.poolId, mEditTextPoolId.getText().toString().trim());
        FragmentJoinPoolDetails fragmentJoinPools = new FragmentJoinPoolDetails();
        fragmentJoinPools.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_pool_creation_join, fragmentJoinPools);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}
