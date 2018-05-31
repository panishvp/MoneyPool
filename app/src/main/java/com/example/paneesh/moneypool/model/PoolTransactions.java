package com.example.paneesh.moneypool.model;

import java.sql.Date;

public class PoolTransactions  {

    private int poolId;
    private int poolMemberId;
    private int poolCurrentCounter;
    private int poolWinnerFlag;
    private int poolDelayFlag;
    private double poolIndividualShare;
    private double poolTakeAway;
    private double poolDelayPaymentAmount;
    private Date poolPaymentDate;
    private Date poolTakeawayDate;
    private int poolPickerFlag;
    private  double poolAuctionTakeAway;

    public int getPoolId() {
        return poolId;
    }

    public void setPoolId(int poolId) {
        this.poolId = poolId;
    }

    public int getPoolMemberId() {
        return poolMemberId;
    }

    public void setPoolMemberId(int poolMemberId) {
        this.poolMemberId = poolMemberId;
    }

    public int getPoolCurrentCounter() {
        return poolCurrentCounter;
    }

    public void setPoolCurrentCounter(int poolCurrentCounter) {
        this.poolCurrentCounter = poolCurrentCounter;
    }

    public int getPoolWinnerFlag() {
        return poolWinnerFlag;
    }

    public void setPoolWinnerFlag(int poolWinnerFlag) {
        this.poolWinnerFlag = poolWinnerFlag;
    }

    public int getPoolDelayFlag() {
        return poolDelayFlag;
    }

    public void setPoolDelayFlag(int poolDelayFlag) {
        this.poolDelayFlag = poolDelayFlag;
    }

    public double getPoolIndividualShare() {
        return poolIndividualShare;
    }

    public void setPoolIndividualShare(double poolIndividualShare) {
        this.poolIndividualShare = poolIndividualShare;
    }

    public double getPoolTakeAway() {
        return poolTakeAway;
    }

    public void setPoolTakeAway(double poolTakeAway) {
        this.poolTakeAway = poolTakeAway;
    }

    public double getPoolDelayPaymentAmount() {
        return poolDelayPaymentAmount;
    }

    public void setPoolDelayPaymentAmount(double poolDelayPaymentAmount) {
        this.poolDelayPaymentAmount = poolDelayPaymentAmount;
    }

    public Date getPoolPaymentDate() {
        return poolPaymentDate;
    }

    public void setPoolPaymentDate(Date poolPaymentDate) {
        this.poolPaymentDate = poolPaymentDate;
    }

    public Date getPoolTakeawayDate() {
        return poolTakeawayDate;
    }

    public void setPoolTakeawayDate(Date poolTakeawayDate) {
        this.poolTakeawayDate = poolTakeawayDate;
    }

    public int getPoolPickerFlag() {
        return poolPickerFlag;
    }

    public void setPoolPickerFlag(int poolPickerFlag) {
        this.poolPickerFlag = poolPickerFlag;
    }

    public double getPoolAuctionTakeAway() {
        return poolAuctionTakeAway;
    }

    public void setPoolAuctionTakeAway(double poolAuctionTakeAway) {
        this.poolAuctionTakeAway = poolAuctionTakeAway;
    }
}
