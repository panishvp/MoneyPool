package com.example.paneesh.moneypool.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.paneesh.moneypool.R;
import com.example.paneesh.moneypool.Utils;
import com.example.paneesh.moneypool.model.PoolTransactions;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MyTransactionsListAdapter extends RecyclerView.Adapter<MyTransactionsListAdapter.MyTransactionsHolder> {


    private View mView;
    private ArrayList<PoolTransactions> poolTransactionsArrayList;

    public MyTransactionsListAdapter(ArrayList<PoolTransactions> poolTransactionsArrayList) {
        this.poolTransactionsArrayList = poolTransactionsArrayList;
    }

    @NonNull
    @Override
    public MyTransactionsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_transactions_list_item, parent, false);
        MyTransactionsHolder holder = new MyTransactionsHolder(mView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyTransactionsHolder holder, int position) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Utils.datePattern);
        holder.mTextViewIndividualShare.setText(poolTransactionsArrayList.get(position).getPoolIndividualShare()+" " );
        holder.mTextViewCurrentCounter.setText(poolTransactionsArrayList.get(position).getPoolCurrentCounter()+" ");
        holder.mTextViewPoolId.setText(poolTransactionsArrayList.get(position).getPoolId()+" ");
        if (poolTransactionsArrayList.get(position).getPoolPaymentDate() != null){
            holder.mTextViewPaymentDate.setText(simpleDateFormat.format(poolTransactionsArrayList.get(position).getPoolPaymentDate()));
        }else {
            holder.mTextViewPaymentDate.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return poolTransactionsArrayList.size();
    }

    public static  class  MyTransactionsHolder extends RecyclerView.ViewHolder{

        TextView mTextViewCurrentCounter;
        TextView mTextViewPaymentDate;
        TextView mTextViewIndividualShare;
        TextView mTextViewPoolId;

        public MyTransactionsHolder(View itemView) {
            super(itemView);

            this.mTextViewPoolId = mTextViewPoolId;
            this.mTextViewCurrentCounter = mTextViewCurrentCounter;
            this.mTextViewPaymentDate = mTextViewPaymentDate;
            this.mTextViewIndividualShare = mTextViewIndividualShare;

            mTextViewCurrentCounter = itemView.findViewById(R.id.tv_my_transactions_pool_counter);
            mTextViewIndividualShare = itemView.findViewById(R.id.tv_my_transactions_pool_individual_share);
            mTextViewPaymentDate = itemView.findViewById(R.id.tv_my_transactions_pool_payment_date);
            mTextViewPoolId = itemView.findViewById(R.id.tv_my_transactions_pool_id);

        }
    }
}
