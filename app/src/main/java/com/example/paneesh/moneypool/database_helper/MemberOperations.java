package com.example.paneesh.moneypool.database_helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.paneesh.moneypool.Utils;
import com.example.paneesh.moneypool.model.Member;
import com.example.paneesh.moneypool.model.PoolDetails;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class MemberOperations  extends SQLiteOpenHelper{

    private SQLiteDatabase db;


    public MemberOperations(Context context) {
        super(context, Utils.databaseName, null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table "+Utils.memberTable+" ( member_id  int primary key, member_first_name varchar(45), member_last_name varchar(45), member_email varchar(45), member_password varchar(45)," +
                "member_address varchar(45), member_iban varchar(45), member_swift varchar(45), member_nominee varchar(45), member_phone_number varchar(45) )");
        db.execSQL("create table "+Utils.poolDetailsTable +" ( pool_id  int primary key, pool_admin_id int, pool_name varchar(45), pool_duration int, pool_strength int, " +
                "pool_current_counter int, pool_individual_share double, pool_monthly_takeaway double, pool_start_date datetime, pool_end_date datetime, pool_meetup_date int, pool_deposit_date int, pool_late_fee_charge int )");
        db.execSQL("create table "+Utils.poolTransactions+"(uid int primary key, pool_id int, member_id int, pool_current_counter int, pool_individual_monthly_share double, pool_winner_flag int, " +
                "pool_delay_flag int, pool_delay_payment_amount double, pool_take_away_amount double, pool_payment_date date, pool_takeaway_date date, pool_picker_flag int, auction_takeaway double)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Utils.memberTable);
        onCreate(db);
    }

    public void insertMember(Member member){

         db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Utils.memberFirstName, member.getMemberFirstName());
        contentValues.put(Utils.memberLastName, member.getMemberLastName());
        contentValues.put(Utils.memberEmail, member.getMemberEmail());
        contentValues.put(Utils.memberPassword, member.getMemberPassword());
        contentValues.put(Utils.memberAddress, member.getMemberAddress());
        contentValues.put(Utils.memberIban, member.getMemberIban());
        contentValues.put(Utils.memberSwift, member.getMemberSwiftCode());
        contentValues.put(Utils.memberNominee, member.getMemberNominee());
        contentValues.put(Utils.memberPhoneNumber, member.getMemberPhoneNumber());
        db.insert(Utils.memberTable, null, contentValues);
    }

    public boolean loginMember(String email, String password){
         db = this.getWritableDatabase();
        Member member = new Member();
        Cursor cursor = db.rawQuery("Select * from "+Utils.memberTable+" where "+Utils.memberEmail+" = ? And "+Utils.memberPassword+" = ?", new String[]{email, password});
        if (cursor.getCount()> 0){
            return true;
        }else{
            return false;
        }
    }

    public Member fetchMemberDetails(String email, String password){

        db = this.getWritableDatabase();
        Member member = new Member();
        Cursor cursor = db.rawQuery("Select * from "+Utils.memberTable+" where "+Utils.memberEmail+" = ? And "+Utils.memberPassword+" = ?", new String[]{email, password});

        if (cursor.moveToFirst()){
            member.setMemberID(cursor.getInt(0));
            member.setMemberFirstName(cursor.getString(1));
            member.setMemberLastName(cursor.getString(2));
            member.setMemberEmail(cursor.getString(3));
            member.setMemberPassword(cursor.getString(4));
            member.setMemberAddress(cursor.getString(5));
            member.setMemberIban(cursor.getString(6));
            member.setMemberSwiftCode(cursor.getString(7));
            member.setMemberNominee(cursor.getString(8));
            member.setMemberPhoneNumber(cursor.getString(9));

        }else{

        }
        return member;
    }

    public boolean isMemberPresent(String email){
        db = getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * From "+Utils.memberTable+" Where "+Utils.memberEmail+" = ?", new String[]{email});
        int count = cursor.getCount();

        if (count>0){
            return true;
        }else {
            return false;
        }
    }

    public void  insertPool(PoolDetails poolDetails){
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Utils.poolAdminId, poolDetails.getPoolAdminId());
        contentValues.put(Utils.poolName, poolDetails.getPoolName());
        contentValues.put(Utils.poolDuration, poolDetails.getPoolDuration());
        contentValues.put(Utils.poolStrength, poolDetails.getPoolStrength());
        contentValues.put(Utils.poolCurrentCounter, poolDetails.getPoolCurrentCounter());
        contentValues.put(Utils.poolIndividualShare, poolDetails.getPoolIndividualShare());
        contentValues.put(Utils.poolMonthlyTakeAway, poolDetails.getPoolMonthlyTakeAway());
        Date date = poolDetails.getPoolStartDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Utils.datePattern);
        String dateString = simpleDateFormat.format(date);
        contentValues.put(Utils.poolStartDate, dateString);
        contentValues.put(Utils.poolEndDate,dateString);
        contentValues.put(Utils.poolMeetUp, poolDetails.getPoolMeetUpDate());
        contentValues.put(Utils.poolDepositDate, poolDetails.getPoolDepositDate());
        contentValues.put(Utils.poolLateFees, poolDetails.getPoolLateFeeCharge());
        db.insert(Utils.poolDetailsTable, null, contentValues);
    }

}
