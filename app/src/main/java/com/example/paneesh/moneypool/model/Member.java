package com.example.paneesh.moneypool.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Member implements Serializable {


    private int memberID;
    private String memberFirstName;
    private String memberLastName;
    private String memberEmail;
    private String memberPhoneNumber;
    private String memberNominee;
    private String memberIban;
    private String memberSwiftCode;
    private String memberAddress;
    private String memberPassword;

    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public String getMemberFirstName() {
        return memberFirstName;
    }

    public void setMemberFirstName(String memberFirstName) {
        this.memberFirstName = memberFirstName;
    }

    public String getMemberLastName() {
        return memberLastName;
    }

    public void setMemberLastName(String memberLastName) {
        this.memberLastName = memberLastName;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public String getMemberPhoneNumber() {
        return memberPhoneNumber;
    }

    public void setMemberPhoneNumber(String memberPhoneNumber) {
        this.memberPhoneNumber = memberPhoneNumber;
    }

    public String getMemberNominee() {
        return memberNominee;
    }

    public void setMemberNominee(String memberNominee) {
        this.memberNominee = memberNominee;
    }

    public String getMemberIban() {
        return memberIban;
    }

    public void setMemberIban(String memberIban) {
        this.memberIban = memberIban;
    }

    public String getMemberSwiftCode() {
        return memberSwiftCode;
    }

    public void setMemberSwiftCode(String memberSwiftCode) {
        this.memberSwiftCode = memberSwiftCode;
    }

    public String getMemberAddress() {
        return memberAddress;
    }

    public void setMemberAddress(String memberAddress) {
        this.memberAddress = memberAddress;
    }

    public String getMemberPassword() {
        return memberPassword;
    }

    public void setMemberPassword(String memberPassword) {
        this.memberPassword = memberPassword;
    }


}
