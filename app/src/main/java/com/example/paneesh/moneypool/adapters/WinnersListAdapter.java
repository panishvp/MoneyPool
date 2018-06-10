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
import com.example.paneesh.moneypool.model.WinnerPicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class WinnersListAdapter extends RecyclerView.Adapter<WinnersListAdapter.WinnersListHolder> {

    private View mView;
    private LinearLayout linearLayout;

    private ArrayList<WinnerPicker> winnerPickerArrayList = new ArrayList<>();

    public WinnersListAdapter(ArrayList<WinnerPicker> winnerPickerArrayList) {
        this.winnerPickerArrayList = winnerPickerArrayList;
    }

    @NonNull
    @Override
    public WinnersListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_list_item, parent, false);
        WinnersListHolder winnersListHolder = new WinnersListHolder(mView);
        return winnersListHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WinnersListHolder holder, int position) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Utils.datePattern);
        holder.mWinnerId.setText(winnerPickerArrayList.get(position).getWinnerMemberID()+"");
        holder.mPickerId.setText(winnerPickerArrayList.get(position).getPickerMemberID()+"");
        holder.mPickerAmount.setText(winnerPickerArrayList.get(position).getPickerTakeawatAmt()+"");
        holder.mCurrentCounter.setText(winnerPickerArrayList.get(position).getIteration()+"");
        holder.mWinnerAmount.setText(winnerPickerArrayList.get(position).getWinnerTakeawayAmt()+"");
        if (winnerPickerArrayList.get(position).getTakeawayDate() != null){
            holder.mPaymentDate.setText(simpleDateFormat.format(winnerPickerArrayList.get(position).getTakeawayDate()));
        }else {
            holder.mPaymentDate.setText(" ");
        }
    }

    @Override
    public int getItemCount() {
        return winnerPickerArrayList.size();
    }


    public static class WinnersListHolder extends RecyclerView.ViewHolder {

        TextView mWinnerId;
        TextView mPickerId;
        TextView mCurrentCounter;
        TextView mPaymentDate;
        TextView mWinnerAmount;
        TextView mPickerAmount;
        LinearLayout linearLayout;

        public WinnersListHolder(View itemView) {
            super(itemView);
            this.mWinnerId = mWinnerId;
            this.mCurrentCounter = mCurrentCounter;
            this.mPaymentDate = mPaymentDate;
            this.mWinnerAmount = mWinnerAmount;
            this.mPickerId = mPickerId;
            this.mPickerAmount= mPickerAmount;

            mWinnerId = itemView.findViewById(R.id.tv_winner_name_item);
            mPickerId = itemView.findViewById(R.id.tv_picker_id);
            mCurrentCounter = itemView.findViewById(R.id.tv_current_cycle_item);
            mPaymentDate = itemView.findViewById(R.id.tv_take_away_date);
            mWinnerAmount = itemView.findViewById(R.id.tv_winner_take_away);
            mPickerAmount = itemView.findViewById(R.id.tv_picker_take_away);
        }

    }
}
