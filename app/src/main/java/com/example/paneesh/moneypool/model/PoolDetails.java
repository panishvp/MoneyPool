package com.example.paneesh.moneypool.model;

import java.io.Serializable;
import java.sql.Date;

public class PoolDetails implements Serializable {

    private int poolId;
    private int poolAdminId;
    private String poolName;
    private int poolStrength;
    private int poolDuration;
    private int poolCurrentCounter;
    private double poolIndividualShare;
    private double poolMonthlyTakeAway;
    private Date poolStartDate;
    private Date poolEndDate;
    private int poolMeetUpDate;
    private int poolDepositDate;
    private int poolLateFeeCharge;
    private String rules;

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }



    public PoolDetails() {

    }

    public int getPoolId() {
        return poolId;
    }

    public void setPoolId(int poolId) {
        this.poolId = poolId;
    }

    public int getPoolAdminId() {
        return poolAdminId;
    }

    public void setPoolAdminId(int poolAdminId) {
        this.poolAdminId = poolAdminId;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public int getPoolStrength() {
        return poolStrength;
    }

    public void setPoolStrength(int poolStrength) {
        this.poolStrength = poolStrength;
    }

    public int getPoolDuration() {
        return poolDuration;
    }

    public void setPoolDuration(int poolDuration) {
        this.poolDuration = poolDuration;
    }

    public int getPoolCurrentCounter() {
        return poolCurrentCounter;
    }

    public void setPoolCurrentCounter(int poolCurrentCounter) {
        this.poolCurrentCounter = poolCurrentCounter;
    }

    public double getPoolIndividualShare() {
        return poolIndividualShare;
    }

    public void setPoolIndividualShare(double poolIndividualShare) {
        this.poolIndividualShare = poolIndividualShare;
    }

    public double getPoolMonthlyTakeAway() {
        return poolMonthlyTakeAway;
    }

    public void setPoolMonthlyTakeAway(double poolMonthlyTakeAway) {
        this.poolMonthlyTakeAway = poolMonthlyTakeAway;
    }

    public Date getPoolStartDate() {
        return poolStartDate;
    }

    public void setPoolStartDate(Date poolStartDate) {
        this.poolStartDate = poolStartDate;
    }

    public Date getPoolEndDate() {
        return poolEndDate;
    }

    public void setPoolEndDate(Date poolEndDate) {
        this.poolEndDate = poolEndDate;
    }

    public int getPoolMeetUpDate() {
        return poolMeetUpDate;
    }

    public void setPoolMeetUpDate(int poolMeetUpDate) {
        this.poolMeetUpDate = poolMeetUpDate;
    }

    public int getPoolDepositDate() {
        return poolDepositDate;
    }

    public void setPoolDepositDate(int poolDepositDate) {
        this.poolDepositDate = poolDepositDate;
    }

    public int getPoolLateFeeCharge() {
        return poolLateFeeCharge;
    }

    public void setPoolLateFeeCharge(int poolLateFeeCharge) {
        this.poolLateFeeCharge = poolLateFeeCharge;
    }
}
