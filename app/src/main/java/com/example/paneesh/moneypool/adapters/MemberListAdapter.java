package com.example.paneesh.moneypool.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.paneesh.moneypool.R;
import com.example.paneesh.moneypool.model.Member;

import java.util.ArrayList;

public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.MemberListHolder> {

    private View mView;
    private ArrayList<Member> memberListOfPool = new ArrayList<>();

    public MemberListAdapter(ArrayList<Member> memberListOfPool) {
        this.memberListOfPool = memberListOfPool;
    }

    @NonNull
    @Override
    public MemberListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_list_of_pool, parent, false);
        MemberListAdapter.MemberListHolder memberListHolder = new MemberListAdapter.MemberListHolder(mView);
        return memberListHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MemberListHolder holder, int position) {
        holder.memberID.setText(memberListOfPool.get(position).getMemberID() + "");
        holder.memberName.setText(memberListOfPool.get(position).getMemberFirstName() + "");
    }

    @Override
    public int getItemCount() {
        return memberListOfPool.size();
    }

    public static class MemberListHolder extends RecyclerView.ViewHolder {

        TextView memberName;
        TextView memberID;

        public MemberListHolder(View itemView) {
            super(itemView);
            this.memberID = memberID;
            this.memberName = memberName;

            memberID = itemView.findViewById(R.id.textViewMemberID);
            memberName = itemView.findViewById(R.id.textViewMemberName);
        }
    }
}
