package com.example.paneesh.moneypool.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.paneesh.moneypool.R;
import com.example.paneesh.moneypool.Utils;
import com.example.paneesh.moneypool.model.PoolTransactions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PoolPaymentHistoryAdapter extends RecyclerView.Adapter<PoolPaymentHistoryAdapter.PaymentHistoryViewHolder> {

    private View mView;
    private LinearLayout linearLayout;

    private ArrayList<PoolTransactions> poolTransactionsArrayList = new ArrayList<>();

    public PoolPaymentHistoryAdapter(ArrayList<PoolTransactions> poolTransactionsArrayList) {
        this.poolTransactionsArrayList = poolTransactionsArrayList;
    }

    @NonNull
    @Override
    public PaymentHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_history_list_item, parent, false);
        PaymentHistoryViewHolder historyViewHolder = new PaymentHistoryViewHolder(mView);
        return historyViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PaymentHistoryViewHolder holder, int position) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Utils.datePattern);
        holder.mMemberId.setText(poolTransactionsArrayList.get(position).getPoolMemberId()+"");
        holder.mCurrentCounter.setText(poolTransactionsArrayList.get(position).getPoolCurrentCounter()+" ");
        holder.mAmount.setText(String.valueOf(poolTransactionsArrayList.get(position).getPoolIndividualShare()));
        if (poolTransactionsArrayList.get(position).getPoolPaymentDate() != null){
            holder.mPaymentDate.setText(simpleDateFormat.format(poolTransactionsArrayList.get(position).getPoolPaymentDate())+"");
        }else {
            holder.mPaymentDate.setText("Not Paid");
        }

    }

    @Override
    public int getItemCount() {
        return poolTransactionsArrayList.size();
    }

    public static class PaymentHistoryViewHolder extends RecyclerView.ViewHolder {

        TextView mMemberId;
        TextView mCurrentCounter;
        TextView mPaymentDate;
        TextView mAmount;
        LinearLayout linearLayout;

        public PaymentHistoryViewHolder(View itemView) {
            super(itemView);
            this.mMemberId = mMemberId;
            this.mCurrentCounter = mCurrentCounter;
            this.mPaymentDate = mPaymentDate;
            this.mAmount = mAmount;

            linearLayout = itemView.findViewById(R.id.ll_payment_adapter);
            mMemberId = itemView.findViewById(R.id.tv_payment_member_id);
            mCurrentCounter = itemView.findViewById(R.id.tv_payment_current_counter);
            mPaymentDate = itemView.findViewById(R.id.tv_payment_date);
            mAmount = itemView.findViewById(R.id.tv_payment_amount);
        }

    }
}
