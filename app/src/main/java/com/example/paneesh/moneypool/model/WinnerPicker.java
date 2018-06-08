package com.example.paneesh.moneypool.model;

import java.io.Serializable;
import java.util.Date;

public class WinnerPicker implements Serializable {
    private int iteration;
    private int winnerMemberID;
    private int pickerMemberID;
    private Date takeawayDate;
    private double winnerTakeawayAmt;
    private double pickerTakeawatAmt;

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public int getWinnerMemberID() {
        return winnerMemberID;
    }

    public void setWinnerMemberID(int winnerMemberID) {
        this.winnerMemberID = winnerMemberID;
    }

    public int getPickerMemberID() {
        return pickerMemberID;
    }

    public void setPickerMemberID(int pickerMemberID) {
        this.pickerMemberID = pickerMemberID;
    }

    public Date getTakeawayDate() {
        return takeawayDate;
    }

    public void setTakeawayDate(Date takeawayDate) {
        this.takeawayDate = takeawayDate;
    }

    public double getWinnerTakeawayAmt() {
        return winnerTakeawayAmt;
    }

    public void setWinnerTakeawayAmt(double winnerTakeawayAmt) {
        this.winnerTakeawayAmt = winnerTakeawayAmt;
    }

    public double getPickerTakeawatAmt() {
        return pickerTakeawatAmt;
    }

    public void setPickerTakeawatAmt(double pickerTakeawatAmt) {
        this.pickerTakeawatAmt = pickerTakeawatAmt;
    }


}
