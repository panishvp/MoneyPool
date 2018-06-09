package com.example.paneesh.moneypool.database_helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.paneesh.moneypool.DateCalculationsUtil;
import com.example.paneesh.moneypool.Utils;
import com.example.paneesh.moneypool.model.Member;
import com.example.paneesh.moneypool.model.PoolDetails;
import com.example.paneesh.moneypool.model.PoolTransactions;
import com.example.paneesh.moneypool.model.WinnerPicker;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MemberOperations extends SQLiteOpenHelper {

    private static MemberOperations memberOperationsInstance = null;
    private SQLiteDatabase db;
    private Cursor cursor;
    private DateCalculationsUtil dateCalculationsUtil = new DateCalculationsUtil();


    public static MemberOperations getInstance(Context context){
        if(memberOperationsInstance == null){
            memberOperationsInstance = new MemberOperations(context);
        }
        return memberOperationsInstance;
    }

    private MemberOperations(Context context) {
        super(context, Utils.databaseName, null, 8);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + Utils.memberTable + " " +
                "( member_id  INTEGER  unique primary key AUTOINCREMENT," +
                " member_first_name varchar(45)," +
                " member_last_name varchar(45), " +
                "member_email varchar(45) not null, " +
                "member_password varchar(45) not null," +
                "member_address varchar(45), " +
                "member_iban varchar(45), " +
                "member_swift varchar(45), " +
                "member_nominee varchar(45), " +
                "member_phone_number varchar(45) )");

        db.execSQL("create table " + Utils.poolDetailsTable +
                " ( pool_id  INTEGER unique primary key AUTOINCREMENT," +
                " pool_admin_id INTEGER not null," +
                " pool_name varchar(45) not null," +
                " pool_duration INTEGER not null, " +
                "pool_strength INTEGER, " +
                "pool_current_counter INTEGER not null," +
                " pool_individual_share double not null," +
                " pool_monthly_take_away double," +
                " pool_start_date datetime not null," +
                " pool_end_date datetime," +
                " pool_meetup_date INTEGER not null," +
                " pool_deposit_date INTEGER not null," +
                " pool_late_fee_charge INTEGER not null)");

        db.execSQL("create table " + Utils.poolTransactions +
                "(uid INTEGER unique primary key AUTOINCREMENT," +
                " pool_id INTEGER not null," +
                " member_id INTEGER not null," +
                " pool_current_counter INTEGER default '-1' not null," +
                " pool_individual_monthly_share double," +
                " pool_winner_flag INTEGER default '99', " +
                "pool_delay_flag INTEGER default '0'," +
                " pool_delay_payment_amount double default '0', " +
                "pool_take_away_amount double, " +
                "pool_payment_date date, " +
                "pool_takeaway_date date," +
                " pool_picker_flag INTEGER default '0', " +
                "auction_takeaway double," +
                "constraint PoolTransactions_pooldetails_PoolID_fk" +
                "  foreign key (pool_id) references poolDetailsTable (pool_id)," +
                "  constraint PoolTransactions_member_member_id_fk" +
                "  foreign key (member_id) references memberTable (member_id))"


        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Utils.memberTable);
        db.execSQL("DROP TABLE IF EXISTS " + Utils.poolDetailsTable);
        db.execSQL("DROP TABLE IF EXISTS " + Utils.poolTransactions);
        onCreate(db);
    }

    public void insertMember(Member member) {

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

    public boolean loginMember(String email, String password) {
        db = this.getWritableDatabase();
          cursor = db.rawQuery("Select * from " + Utils.memberTable + " where " + Utils.memberEmail + " = ? And " + Utils.memberPassword + " = ?", new String[]{email, password});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Member fetchMemberDetails(String email, String password) {

        db = this.getWritableDatabase();
        Member member = new Member();
          cursor = db.rawQuery("Select * from " + Utils.memberTable + " where " + Utils.memberEmail + " = ? And " + Utils.memberPassword + " = ?", new String[]{email, password});

        if (cursor.moveToFirst()) {
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

        } else {

        }
        return member;
    }

    public boolean isMemberPresent(String email) {
        db = getWritableDatabase();
          cursor = db.rawQuery("Select * From " + Utils.memberTable + " Where " + Utils.memberEmail + " = ?", new String[]{email});
        int count = cursor.getCount();

        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void insertPool(PoolDetails poolDetails) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Utils.poolAdminId, poolDetails.getPoolAdminId());
        contentValues.put(Utils.poolName, poolDetails.getPoolName());
        contentValues.put(Utils.poolDuration, poolDetails.getPoolDuration());
        contentValues.put(Utils.poolStrength, poolDetails.getPoolStrength());
        contentValues.put(Utils.poolCurrentCounter, poolDetails.getPoolCurrentCounter());
        contentValues.put(Utils.poolIndividualShare, poolDetails.getPoolIndividualShare());
        contentValues.put(Utils.pdPoolMonthlyTakeAway, poolDetails.getPoolMonthlyTakeAway());
        String dateString = dateToString(poolDetails.getPoolStartDate());
        contentValues.put(Utils.poolStartDate, dateString);
        contentValues.put(Utils.poolEndDate, dateString);
        contentValues.put(Utils.poolMeetUp, poolDetails.getPoolMeetUpDate());
        contentValues.put(Utils.poolDepositDate, poolDetails.getPoolDepositDate());
        contentValues.put(Utils.poolLateFees, poolDetails.getPoolLateFeeCharge());
        db.insert(Utils.poolDetailsTable, null, contentValues);
    }

    public PoolDetails fetchPoolDetails(int poolID) {
        db = this.getWritableDatabase();
        PoolDetails pool = new PoolDetails();
          cursor = db.rawQuery("Select * from " + Utils.poolDetailsTable + " where " + Utils.poolId + " = " + poolID, null);

        if (cursor.moveToFirst()) {
            pool.setPoolId(cursor.getInt(0));
            pool.setPoolAdminId(cursor.getInt(1));
            pool.setPoolName(cursor.getString(2));
            pool.setPoolDuration(cursor.getInt(3));
            pool.setPoolStrength(cursor.getInt(4));
            pool.setPoolCurrentCounter(cursor.getInt(5));
            pool.setPoolIndividualShare(cursor.getDouble(6));
            pool.setPoolMonthlyTakeAway(cursor.getDouble(7));
            pool.setPoolStartDate(dateCalculationsUtil.stringToSQLDate(cursor.getString(8)));
            pool.setPoolEndDate(dateCalculationsUtil.stringToSQLDate(cursor.getString(9)));
            pool.setPoolMeetUpDate(cursor.getInt(10));
            pool.setPoolDepositDate(cursor.getInt(11));
            pool.setPoolLateFeeCharge(cursor.getInt(12));

        } else {

        }
        return pool;
    }

    public boolean isMemberInThepool(int memberId, int poolId) {

        boolean status = false;

        db = getWritableDatabase();
          cursor = db.rawQuery("Select count(*) From " + Utils.poolTransactions + " Where " + Utils.poolId + " = " + poolId + " and "
                + Utils.poolCurrentCounter + "= -1 and " + Utils.poolWinnerFlag + "= 99 and " + Utils.memberId + "= " + memberId, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        if (count != 0) {
            status = true;
        } else {
            status = false;
        }

        return status;
    }

    private int getCountRegisteredMembers(int poolID) {
        int numberOfMembersRegistered = 0;

        db = getWritableDatabase();
          cursor = db.rawQuery("select count(*) from " + Utils.poolTransactions + " " +
                "where " + Utils.poolId + " = " + poolID + " and " + Utils.poolCurrentCounter + " =-1 " +
                "and " + Utils.memberPayementDate + " is null ", null);
        cursor.moveToFirst();
        numberOfMembersRegistered = cursor.getInt(0);

        return numberOfMembersRegistered;
    }

    private int getStrenghtOfPool(int poolID) {
        int strength = 0;

        db = getWritableDatabase();
          cursor = db.rawQuery("select " + Utils.poolStrength + " from " + Utils.poolDetailsTable + " where " + Utils.poolId + " = " + poolID, null);

        cursor.moveToFirst();
        strength = cursor.getInt(0);

        return strength;
    }

    public boolean isValidPoolJoin(int poolID) {
        boolean valid;
        int numberOfMembersRegistered = getCountRegisteredMembers(poolID);
        int strength = getStrenghtOfPool(poolID);

        if (numberOfMembersRegistered < strength) valid = true;
        else valid = false;
        return valid;
    }

    public void enrollMember(PoolTransactions enrollMember) {
        if (!(isMemberInThepool(enrollMember.getPoolMemberId(), enrollMember.getPoolId())) & isValidPoolJoin(enrollMember.getPoolId())) {

            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(Utils.poolId, enrollMember.getPoolId());
            contentValues.put(Utils.memberId, enrollMember.getPoolMemberId());
            contentValues.put(Utils.poolCurrentCounter, -1);
            contentValues.put(Utils.poolWinnerFlag, 99);

            db.insert(Utils.poolTransactions, null, contentValues);

        }
    }

    public boolean isValidPool(int poolId){
        boolean isValid = false;
        db = this.getWritableDatabase();
        cursor = db.rawQuery("Select * from "+Utils.poolDetailsTable+" where "+Utils.poolId+" = "+poolId, null);
        if (cursor.moveToFirst()){
            isValid = true;
        }
        return isValid;
    }


    public ArrayList<PoolDetails> fetchMypools(int memberID){
        ArrayList<PoolDetails> myPools = new ArrayList<>();

        db = getWritableDatabase();
          cursor = db.rawQuery(" select pd." + Utils.poolId + ",pd." +Utils.poolName + " from " + Utils.poolTransactions + " pt" + " JOIN " + Utils.poolDetailsTable  + " pd" +
                " ON " + "pt." +Utils.poolId + "= pd." + Utils.poolId +
                " where "
                +"pt."+ Utils.memberId + " = " + memberID + " and " +"pt."+ Utils.poolWinnerFlag + " = 99"+ " and " +"pt."+ Utils.poolCurrentCounter + " = -1" , null);

        while (cursor.moveToNext()) {
            PoolDetails poolDetails = new PoolDetails();
            poolDetails.setPoolId(cursor.getInt(0));
            poolDetails.setPoolName(cursor.getString(1));
            myPools.add(poolDetails);
        }

        return myPools ;
    }

    public void updateMemberDetails(String columnName, String value, int memberId) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(columnName, value);
        db.update(Utils.memberTable, contentValues, Utils.memberId + "= " + memberId, null);
    }


    public void updatePoolName(String newPoolName, int poolID) {

        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Utils.poolName, newPoolName);
        db.update(Utils.poolDetailsTable, contentValues, Utils.poolId + " = " + poolID, null);

    }

    private int getCounterFromPoolDetails(int poolID) {
        int countFetched = 0;

        db = getWritableDatabase();
          cursor = db.rawQuery("select " + Utils.poolCurrentCounter + " from " + Utils.poolDetailsTable + " where " + Utils.poolId + " = " + poolID, null);

        countFetched = cursor.getInt(0);

        return countFetched;
    }


    public void updateDuration(int newDuration, int poolID) {
        int currentCounter = getCounterFromPoolDetails(poolID);

        if (currentCounter == -1) {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(Utils.poolDuration, newDuration);
            db.update(Utils.poolDetailsTable, contentValues, Utils.poolId + " = " + poolID, null);

        }
    }

    public void updateIndividualShare(double newShare, int poolID) {
        int currentCounter = getCounterFromPoolDetails(poolID);
        if (currentCounter == -1) {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(Utils.poolIndividualShare, newShare);
            db.update(Utils.poolDetailsTable, contentValues, Utils.poolId + " = " + poolID, null);

        }
    }

    public void updateStartDate(String newStartDate, int poolID) {
        int currentCounter = getCounterFromPoolDetails(poolID);
        if (currentCounter == -1) {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(Utils.poolStartDate, newStartDate);
            db.update(Utils.poolDetailsTable, contentValues, Utils.poolId + " = " + poolID, null);

        }
    }

    public void updateMeetupDate(int newMeetupDate, int poolID) {
        int currentCounter = getCounterFromPoolDetails(poolID);
        if (currentCounter == -1) {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(Utils.poolMeetUp, newMeetupDate);
            db.update(Utils.poolDetailsTable, contentValues, Utils.poolId + " = " + poolID, null);

        }
    }

    public void updateDepositDate(int newDepositDate, int poolID) {
        int currentCounter = getCounterFromPoolDetails(poolID);
        if (currentCounter == -1) {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(Utils.poolDepositDate, newDepositDate);
            db.update(Utils.poolDetailsTable, contentValues, Utils.poolId + " = " + poolID, null);

        }
    }


    public void updateLateFeeCharge(double newLateFeeCharge, int poolID) {
        int currentCounter = getCounterFromPoolDetails(poolID);
        if (currentCounter == -1) {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(Utils.poolLateFees, newLateFeeCharge);
            db.update(Utils.poolDetailsTable, contentValues, Utils.poolId + " = " + poolID, null);

        }
    }


    public ArrayList<PoolDetails> getAdminPools(int adminId) {
        ArrayList<PoolDetails> poolDetailsArrayList = new ArrayList<>();
        db = this.getWritableDatabase();

          cursor = db.rawQuery("Select * from " + Utils.poolDetailsTable + " where " + Utils.poolAdminId + " = " + adminId, null);


        if(cursor.moveToFirst()){

           do {
                PoolDetails pool = new PoolDetails();
                pool.setPoolId(cursor.getInt(0));
                pool.setPoolAdminId(cursor.getInt(1));
                pool.setPoolName(cursor.getString(2));
                pool.setPoolDuration(cursor.getInt(3));
                pool.setPoolStrength(cursor.getInt(4));
                pool.setPoolCurrentCounter(cursor.getInt(5));
                pool.setPoolIndividualShare(cursor.getDouble(6));
                pool.setPoolMonthlyTakeAway(cursor.getDouble(7));
                pool.setPoolStartDate(dateCalculationsUtil.stringToSQLDate(cursor.getString(8)));
                pool.setPoolEndDate(dateCalculationsUtil.stringToSQLDate(cursor.getString(9)));
                pool.setPoolMeetUpDate(cursor.getInt(10));
                pool.setPoolDepositDate(cursor.getInt(11));
                pool.setPoolLateFeeCharge(cursor.getInt(12));
                poolDetailsArrayList.add(pool);

            } while (cursor.moveToNext());

        }



        return poolDetailsArrayList;
    }


    /*__________________________________________________________________Pool Admin methods____________________________________________________________________________________*/

    private int getNumberOfTransaction(int poolID) {
        int numberOfTransactions = 0;

        db = getWritableDatabase();
          cursor = db.rawQuery("select count(*) from " + Utils.poolTransactions + " where " + Utils.poolId + " = " + poolID + " and " + Utils.poolCurrentCounter + "!=1 and " + Utils.memberPayementDate + " is not null", null);

        cursor.moveToFirst();
        numberOfTransactions = cursor.getInt(0);
        return numberOfTransactions;
    }


    public boolean isValidPoolAdd(int poolID) {
        boolean valid;
        int numberOfTransactions = getNumberOfTransaction(poolID);
        int strength = getStrenghtOfPool(poolID);

        if ((numberOfTransactions+1) <= (strength * strength)) valid = true;
        else valid = false;
        return valid;
    }



    public boolean isValidAdmin(int poolID, int adminID) {
        boolean status = false;
        //select count(*) from pooldetails where PoolID = ? AND  PoolAdminMemberID = ?

        db = getWritableDatabase();
          cursor = db.rawQuery("select count(*) from " + Utils.poolDetailsTable + " where " + Utils.poolId + " = " + poolID + " and " + Utils.poolAdminId + "=" + adminID, null);

        if (cursor.getInt(1) == 1) status = true;
        else status = false;

        return status;
    }


    public boolean isValidMember(int poolID, int memberID) {
        boolean status;
        //select count(*) from pooltransactions " +
        //where PoolID = ? AND  MemberID = ? AND CurrentCounter = -1 and WinnerFlag = 99 ");

        db = getWritableDatabase();
          cursor = db.rawQuery("select count(*) from " + Utils.poolTransactions + " where "
                + Utils.poolId + " = " + poolID + " and " + Utils.memberId + "=" + memberID + " and " + Utils.poolCurrentCounter + "=-1 and " + Utils.poolWinnerFlag + "=99", null);

        if (cursor.getInt(1) == 1) status = true;
        else status = false;

        return status;
    }

    private int groupPaymentDoneCount(int poolID, int counter) {
        // will give count of people made payment for that Months cycle
        int payerCount = 0;

        /*select count(MemberID) from pooltransactions " +
                    "where PoolID = ? and CurrentCounter = ? and PaymentDate is not null */
        if (counter != -1) {
            db = getWritableDatabase();
              cursor = db.rawQuery("select count(*) from " + Utils.poolTransactions + " where "
                    + Utils.poolId + " = " + poolID + " and " + Utils.poolCurrentCounter + "=" + counter + " and " + Utils.memberPayementDate + " is not null ", null);
            cursor.moveToFirst();

            payerCount = cursor.getInt(0);
        } else {
            db = getReadableDatabase();
              cursor = db.rawQuery("select count(*) from " + Utils.poolTransactions + " where "
                    + Utils.poolId + " = " + poolID + " and " + Utils.poolCurrentCounter + "=" + counter, null);

            cursor.moveToFirst();
            payerCount = cursor.getInt(0);
        }

        return payerCount;
    }

    public  boolean isDelay(PoolDetails activePool) {

        int depositDay = activePool.getPoolDepositDate();
        int currentCounter = activePool.getPoolCurrentCounter();
        Date startDate = activePool.getPoolStartDate();

        java.util.Date monthStartDate = dateCalculationsUtil.addMonth(startDate, currentCounter);

        java.util.Date depositDate = dateCalculationsUtil.addDay(monthStartDate, depositDay);

        java.util.Date currentDate = new java.util.Date();

        if (currentDate.after(depositDate)) return true;
        else return false;

    }



    public void updateDelayPayments(PoolDetails activePool, int memberID) {
        java.util.Date tempDate = new java.util.Date();
        Date currentDate = new Date(tempDate.getTime());

        double individualContribution = activePool.getPoolIndividualShare();
        double lateFeeCharge = activePool.getPoolLateFeeCharge();

        double latePaymentAmount = (lateFeeCharge / 100) * individualContribution;

        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Utils.poolDelayFlag, 1);
        contentValues.put(Utils.poolDelayPaymentAmt, latePaymentAmount);
        contentValues.put(Utils.memberPayementDate, String.valueOf(currentDate));

        db.update(Utils.poolTransactions, contentValues, Utils.poolId + " = " + activePool.getPoolId() + " and "
                + Utils.memberId + " = " + memberID + " and " + Utils.poolCurrentCounter + " = " + activePool.getPoolCurrentCounter() + " and " + Utils.memberPayementDate + " is null ", null);

    }

    public int updateCurrentCounter(PoolDetails activePool) {
        int oldCounter = activePool.getPoolCurrentCounter();
        int newCounter;

        if (oldCounter == -1) {
            newCounter = 1;
        } else {
            newCounter = ++oldCounter;
        }
        activePool.setPoolCurrentCounter(newCounter);
        setCounterInPooldetails(activePool);
        return newCounter;
    }

    private void setCounterInPooldetails(PoolDetails activePool) {

        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Utils.poolCurrentCounter, activePool.getPoolCurrentCounter());
        db.update(Utils.poolDetailsTable, contentValues, Utils.poolId + " = " + activePool.getPoolId(), null);

    }


    private void insertNewBlankTransaction(PoolDetails activePool, int memeberID) {

        long millis = System.currentTimeMillis();
        java.sql.Date currentDate = new java.sql.Date(millis);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Utils.datePattern);
        String dateString = simpleDateFormat.format(currentDate);
        double individualContri = activePool.getPoolIndividualShare();

        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Utils.poolId, activePool.getPoolId());
        contentValues.put(Utils.memberId, memeberID);
        contentValues.put(Utils.poolCurrentCounter, activePool.getPoolCurrentCounter());
        contentValues.put(Utils.pool_individual_monthly_share, activePool.getPoolIndividualShare());
        contentValues.put(Utils.memberPayementDate, dateString);

        db.insert(Utils.poolTransactions, null, contentValues);

    }

    public int makePaymentForMember(PoolDetails activePool, int MemeberID) {


        int strenght = getStrenghtOfPool(activePool.getPoolId());
        int currentMaxCounter = activePool.getPoolCurrentCounter();
        int payersCount = groupPaymentDoneCount(activePool.getPoolId(), currentMaxCounter);
        int paymentSuccess = 0;

        //TODO replace isDelay(activePool)
        if (false){
            updateDelayPayments(activePool, MemeberID);
            paymentSuccess = 1;
        } else {
            if ((currentMaxCounter <= strenght) & (isValidPoolAdd(activePool.getPoolId())) ) {
                if ((payersCount == strenght)) {

                    currentMaxCounter = updateCurrentCounter(activePool);
                    activePool.setPoolCurrentCounter(currentMaxCounter);
                    System.out.println("[DEBUG](makePaymentForMember)Updated current counter:" + currentMaxCounter);
                }
                insertNewBlankTransaction(activePool, MemeberID);
                System.out.println("[DEBUG](makePaymentForMember)Transaction added");
                paymentSuccess = 2;
            }else {
                System.out.println("[DEBUG](makePaymentForMember)Pool is Complete");
                paymentSuccess = 3;
            }

        }
        return paymentSuccess;
    }

    private int getNumberOfWinners(int poolID) {
        int numberOfWinners = 0;
        //select count(*) from pooltransactions where PoolID = ? and CurrentCounter !=-1 and WinnerFlag = 1");
        db = getWritableDatabase();
          cursor = db.rawQuery("select count(*) from " + Utils.poolTransactions + " where "
                + Utils.poolId + " = " + poolID + " and " + Utils.poolCurrentCounter + "!=-1 and " + Utils.poolWinnerFlag + " = 1 ", null);
        cursor.moveToFirst();
        numberOfWinners = cursor.getInt(0);
        return numberOfWinners;
    }


    public boolean isValidPickWinner(PoolDetails activePool) {
        boolean valid;
        int numberOfWinners = getNumberOfWinners(activePool.getPoolId());

        //"select count(*) from pooltransactions where PoolID = ? and CurrentCounter =? and PickerFlag = 1");
        db = getWritableDatabase();
          cursor = db.rawQuery("select count(*) from " + Utils.poolTransactions + " where "
                + Utils.poolId + " = " + activePool.getPoolId() + " and " + Utils.poolCurrentCounter + "=" + activePool.getPoolCurrentCounter() +" and "+ Utils.poolWinnerFlag + " = 1 ", null);
        cursor.moveToFirst();
        int checkIfWinner = cursor.getInt(0);

        if ((numberOfWinners < activePool.getPoolStrength()) & (checkIfWinner == 0)) valid = true;
        else valid = false;
        return valid;

    }

    private ArrayList<Integer> getPoolMembers(PoolDetails activePool) {

        ArrayList<Integer> allPoolMembers = new ArrayList<>();

        //select MemberID from pooltransactions where PoolID = ? AND CurrentCounter = ?");
        db = getWritableDatabase();
          cursor = db.rawQuery(" select " + Utils.memberId + " from " + Utils.poolTransactions + " where "
                + Utils.poolId + " = " + activePool.getPoolId() + " and " + Utils.poolCurrentCounter + " =-1 ", null);

        while (cursor.moveToNext()) {
            allPoolMembers.add(cursor.getInt(0));
        }

        return allPoolMembers;
    }

    private ArrayList<Integer> getPoolMembersWhoPaid(PoolDetails activePool) {

        ArrayList<Integer> allPoolMembers = new ArrayList<>();

        db = getWritableDatabase();
          cursor = db.rawQuery(" select " + Utils.memberId + " from " + Utils.poolTransactions + " where "
                + Utils.poolId + " = " + activePool.getPoolId() + " and " + Utils.poolCurrentCounter + " = " + activePool.getPoolCurrentCounter()+" and "+ Utils.memberPayementDate + " is not null ", null);

        while (cursor.moveToNext()) {
            allPoolMembers.add(cursor.getInt(0));
        }

        return allPoolMembers;
    }

    public ArrayList<Integer> getPoolMembersRemainingToPay(PoolDetails activePool) {
        ArrayList<Integer> allPoolMembers = getPoolMembers(activePool);
        ArrayList<Integer> tempAllMembers = getPoolMembers(activePool);
        ArrayList<Integer> poolMembersWhoPaid = getPoolMembersWhoPaid(activePool);

        allPoolMembers.removeAll(poolMembersWhoPaid);

        if (allPoolMembers.isEmpty()) return tempAllMembers;
        else return allPoolMembers;

    }

    private void fillUpBlankTransaction(PoolDetails activePool) {

        ArrayList<Integer> membersRemainingToPay = (ArrayList<Integer>) getPoolMembersRemainingToPay(activePool);

        for (int i = 0; i < membersRemainingToPay.size(); i++) {
            //INSERT into pooltransactions " +"(PoolID, MemberID, CurrentCounter, IndividualMonthlyShare) " +"VALUE (?,?,?,?)");

            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(Utils.poolId, activePool.getPoolId());
            contentValues.put(Utils.memberId, membersRemainingToPay.get(i));
            contentValues.put(Utils.poolCurrentCounter, activePool.getPoolCurrentCounter());
            contentValues.put(Utils.pool_individual_monthly_share, activePool.getPoolIndividualShare());

            db.insert(Utils.poolTransactions, null, contentValues);

        }

    }

    private  ArrayList<Integer> getPoolMembersWhoWon(PoolDetails activePool) {

         ArrayList<Integer> allPoolMembers = new ArrayList<>();

        //select MemberID from pooltransactions where PoolID = ? AND CurrentCounter = ?");
        db = getWritableDatabase();
          cursor = db.rawQuery(" select " + Utils.memberId + " from " + Utils.poolTransactions + " where "
                + Utils.poolId + " = " + activePool.getPoolId() + " and " + Utils.poolPickerFlag + " = 1", null);

        while (cursor.moveToNext()) {
            allPoolMembers.add(cursor.getInt(0));
        }

        return allPoolMembers;
    }

    private ArrayList<Integer> getPoolMemberRemainingToWin(PoolDetails activePool) {
        ArrayList<Integer> allPoolMembers = getPoolMembers(activePool);
        ArrayList<Integer> poolMembersWhoWon = getPoolMembersWhoWon(activePool);

        allPoolMembers.removeAll(poolMembersWhoWon);

        return allPoolMembers;
    }

    public int  addWinner(PoolDetails activePool) {
        ArrayList<Integer> poolMembersRemainingToWin = (ArrayList<Integer>) getPoolMemberRemainingToWin(activePool);

        Random rand = new Random();
        int randomElement = poolMembersRemainingToWin.get(rand.nextInt(poolMembersRemainingToWin.size()));

        System.out.println("*******____CONGRATULATIONS Member ID " + randomElement + ",You are Winner for " + activePool.getPoolCurrentCounter() + " iteration____*********");
        long millis = System.currentTimeMillis();
        java.sql.Date currentDate = new java.sql.Date(millis);

        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Utils.datePattern);
        contentValues.put(Utils.ptPoolTakeAwayAmount, activePool.getPoolMonthlyTakeAway());
        contentValues.put(Utils.poolPickerTakeawayDate, simpleDateFormat.format(currentDate));
        contentValues.put(Utils.poolWinnerFlag, 1);
        db.update(Utils.poolTransactions, contentValues, Utils.poolId + " = " + activePool.getPoolId() + " and "
                + Utils.poolCurrentCounter + " = " + activePool.getPoolCurrentCounter() + " and " + Utils.memberId + " = " + randomElement, null);

        db = this.getWritableDatabase();
        ContentValues restContentValues = new ContentValues();
        restContentValues.put(Utils.poolWinnerFlag, 0);
        db.update(Utils.poolTransactions, restContentValues, Utils.poolId + " = " + activePool.getPoolId() + " and "
                + Utils.poolCurrentCounter + " = " + activePool.getPoolCurrentCounter() + " and " + Utils.poolWinnerFlag + " != 1", null);

        return randomElement;
    }

    public int pickWinnerForCurrentMonth(PoolDetails activePool) {
        int strength = getStrenghtOfPool(activePool.getPoolId());
        int payerCount = groupPaymentDoneCount(activePool.getPoolId(), activePool.getPoolCurrentCounter());
        int winnerMemberId = 0;

        if (payerCount < strength) {
            fillUpBlankTransaction(activePool);
            winnerMemberId =    addWinner(activePool);
        } else if (payerCount == strength) {
            winnerMemberId = addWinner(activePool);
        }

        return winnerMemberId;
    }


    private  ArrayList<Integer> getPoolMemberForCurrentCycle(PoolDetails activePool){
         ArrayList<Integer> poolMemberWhoWonForCurrentCycle = new ArrayList<>();
            //select MemberID from pooltransactions WHERE PoolID = ? AND WinnerFlag = ? and CurrentCounter = ?");
        db = getWritableDatabase();
          cursor = db.rawQuery(" select " + Utils.memberId + " from " + Utils.poolTransactions + " where "
                + Utils.poolId + " = " + activePool.getPoolId() + " and " + Utils.poolCurrentCounter + " = " + activePool.getPoolCurrentCounter() + " and " + Utils.poolWinnerFlag + " = 1 ",null);

        while(cursor.moveToNext()){
            poolMemberWhoWonForCurrentCycle.add(cursor.getInt(0));
        }
        return poolMemberWhoWonForCurrentCycle;
    }

    public  ArrayList<Integer> printPoolMemberRemainingToWin(PoolDetails activePool){
         ArrayList<Integer> allPoolMembers = getPoolMembers(activePool);
         ArrayList<Integer> poolMembersWhoWon = getPoolMembersWhoWon(activePool);
         ArrayList<Integer> poolMemberWhoWonForCurrentCycle = getPoolMemberForCurrentCycle(activePool);
        allPoolMembers.removeAll(poolMembersWhoWon);
        allPoolMembers.removeAll(poolMemberWhoWonForCurrentCycle);

        return allPoolMembers;

    }

    public void addPicker(PoolDetails activePool ,int pickerMemberID,double auctionPercent) {
        long millis=System.currentTimeMillis();
        java.sql.Date currentDate = new java.sql.Date(millis);

        double takeAwayAmount = activePool.getPoolMonthlyTakeAway();
        double winnerTakeaway = (auctionPercent*takeAwayAmount)/100;
        double pickerTakeaway = ((100-auctionPercent)*takeAwayAmount)/100;

            //UPDATE pooltransactions set PickerFlag = 1,TakeawayAmount = ? ,TakeawayDate = ? where PoolID = ? and CurrentCounter = ? and MemberID = ?");

            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(Utils.pdPoolMonthlyTakeAway,pickerTakeaway);
            contentValues.put(Utils.poolPickerTakeawayDate, String.valueOf(currentDate));
            contentValues.put(Utils.poolPickerFlag,1);
            db.update(Utils.poolTransactions,contentValues,Utils.poolId+ " = " +activePool.getPoolId() + " and "
                    +Utils.poolCurrentCounter + " = " + activePool.getPoolCurrentCounter() + " and " + Utils.memberId + " = " + pickerMemberID  ,null);

            //UPDATE pooltransactions set TakeawayAmount = ?,TakeawayDate = ? where PoolID = ? and CurrentCounter = ? and WinnerFlag = ?");

            db = this.getWritableDatabase();
            ContentValues restContentValues = new ContentValues();
            contentValues.put(Utils.pdPoolMonthlyTakeAway,winnerTakeaway);
            contentValues.put(Utils.poolPickerTakeawayDate, String.valueOf(currentDate));

            db.update(Utils.poolTransactions,contentValues,Utils.poolId+ " = " +activePool.getPoolId() + " and "
                    +Utils.poolCurrentCounter + " = " + activePool.getPoolCurrentCounter() + " and " + Utils.poolWinnerFlag + " = 1" ,null);

    }


    public int getPoolID(PoolDetails newPool){
        int poolID = 0;

            db = getWritableDatabase();
            cursor = db.rawQuery("select "+ Utils.poolId + "  from " + Utils.poolDetailsTable + " where "
                    + Utils.poolName + "='" + newPool.getPoolName() + "' and  " + Utils.poolDuration + " = " + newPool.getPoolDuration()+ " and  "
                    + Utils.poolStrength + "=" + newPool.getPoolStrength() + " and  " + Utils.poolIndividualShare + " = " + newPool.getPoolIndividualShare()+ " and  "
                    + Utils.pdPoolMonthlyTakeAway + "=" + newPool.getPoolMonthlyTakeAway() + " and  " + Utils.poolMeetUp + " = " + newPool.getPoolMeetUpDate()+ " and  "
                    + Utils.poolDepositDate + "=" + newPool.getPoolDepositDate() + " and  " + Utils.poolLateFees + " = " + newPool.getPoolLateFeeCharge()+ " and  "
                    + Utils.poolStartDate + "=" + newPool.getPoolStartDate() + " and  " + Utils.poolEndDate + " = " + newPool.getPoolEndDate()+ " and  "
                    + Utils.poolAdminId + "=" + newPool.getPoolAdminId(), null);

            poolID = cursor.getInt(1);


        return poolID;
    }

    public ArrayList<PoolTransactions> getPoolTransactions(int poolID){

        ArrayList<PoolTransactions> allPoolMembersTransactions = new ArrayList<>();

        db = getWritableDatabase();
        cursor = db.rawQuery(" select " + Utils.memberId + "," + Utils.poolCurrentCounter  + "," +Utils.pool_individual_monthly_share + ","
                + Utils.memberPayementDate  + " from " + Utils.poolTransactions + " where "
                + Utils.poolId + " = " + poolID + " and " + Utils.poolCurrentCounter + " != -1 " , null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    PoolTransactions rtPool = new PoolTransactions();
                    rtPool.setPoolMemberId(cursor.getInt(0));
                    rtPool.setPoolCurrentCounter(cursor.getInt(1));
                    rtPool.setPoolIndividualShare(cursor.getDouble(2));
                    rtPool.setPoolPaymentDate(dateCalculationsUtil.stringToSQLDate(cursor.getString(3)));

                    allPoolMembersTransactions.add(rtPool);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return allPoolMembersTransactions;

    }

    private List<WinnerPicker> getWinnerPickerHistory(int poolID){
        ArrayList<WinnerPicker> allPoolMembersTransactions = new ArrayList<>();

        db = getWritableDatabase();
        cursor = db.rawQuery("select ptp." + Utils.poolCurrentCounter + ",ptw." + Utils.memberId + ",ptp." +Utils.memberId+ ",ptp." +
                Utils.poolPickerTakeawayDate + ",ptw." +Utils.ptPoolTakeAwayAmount+ ",ptp." + Utils.ptPoolTakeAwayAmount
                + "from" + Utils.poolTransactions + "as ptw JOIN" +Utils.poolTransactions + "as ptp ON ptp." + Utils.poolId + "=ptw"
                + Utils.poolId + "where ptw." + Utils.poolWinnerFlag + "=1 and ptp." + Utils.poolPickerFlag + " = 1 and ptp."
                + Utils.poolId + "=" +poolID+ "ORDER BY ptp." + Utils.poolCurrentCounter, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    // Set agents information in model.
                    WinnerPicker rtPool = new WinnerPicker();
                    rtPool.setIteration(cursor.getInt(0));
                    rtPool.setWinnerMemberID(cursor.getInt(1));
                    rtPool.setPickerMemberID(cursor.getInt(2));
                    rtPool.setTakeawayDate(dateCalculationsUtil.stringToSQLDate(cursor.getString(3)));
                    rtPool.setWinnerTakeawayAmt(cursor.getInt(4));
                    rtPool.setPickerTakeawatAmt(cursor.getInt(5));

                    allPoolMembersTransactions.add(rtPool);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return allPoolMembersTransactions;
    }


    private String dateToString(java.util.Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Utils.datePattern);
        String dateString = simpleDateFormat.format(date);
        return dateString;
    }



    private String getMemberName (int memberID){

        db = getWritableDatabase();
        cursor = db.rawQuery("select " + Utils.memberFirstName + " from " + Utils.memberTable + " where " + Utils.memberId + " = " + memberID, null);

        cursor.moveToFirst();
        String memberName = cursor.getString(0);

        return memberName;


    }




}//end


