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

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

public class MemberOperations extends SQLiteOpenHelper {

    private static MemberOperations memberOperationsInstance = null;
    private SQLiteDatabase db;

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
        Member member = new Member();
        Cursor cursor = db.rawQuery("Select * from " + Utils.memberTable + " where " + Utils.memberEmail + " = ? And " + Utils.memberPassword + " = ?", new String[]{email, password});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Member fetchMemberDetails(String email, String password) {

        db = this.getWritableDatabase();
        Member member = new Member();
        Cursor cursor = db.rawQuery("Select * from " + Utils.memberTable + " where " + Utils.memberEmail + " = ? And " + Utils.memberPassword + " = ?", new String[]{email, password});

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
        Cursor cursor = db.rawQuery("Select * From " + Utils.memberTable + " Where " + Utils.memberEmail + " = ?", new String[]{email});
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
        contentValues.put(Utils.poolMonthlyTakeAway, poolDetails.getPoolMonthlyTakeAway());
        Date date = poolDetails.getPoolStartDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Utils.datePattern);
        String dateString = simpleDateFormat.format(date);
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
        Cursor cursor = db.rawQuery("Select * from " + Utils.poolDetailsTable + " where " + Utils.poolId + " = " + poolID, null);

        if (cursor.moveToFirst()) {
            pool.setPoolId(cursor.getInt(0));
            pool.setPoolAdminId(cursor.getInt(1));
            pool.setPoolName(cursor.getString(2));
            pool.setPoolDuration(cursor.getInt(3));
            pool.setPoolStrength(cursor.getInt(4));
            pool.setPoolCurrentCounter(cursor.getInt(5));
            pool.setPoolIndividualShare(cursor.getDouble(6));
            pool.setPoolMonthlyTakeAway(cursor.getDouble(7));
            pool.setPoolStartDate(DateCalculationsUtil.stringToSQLDate(cursor.getString(8)));
            pool.setPoolEndDate(DateCalculationsUtil.stringToSQLDate(cursor.getString(9)));
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
        Cursor cursor = db.rawQuery("Select count(*) From " + Utils.poolTransactions + " Where " + Utils.poolId + " = " + poolId + " and "
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
        Cursor cursor = db.rawQuery("select count(*) from " + Utils.poolTransactions + " " +
                "where " + Utils.poolId + " = " + poolID + " and " + Utils.poolCurrentCounter + " =-1 " +
                "and " + Utils.memberPayementDate + " is null ", null);
        cursor.moveToFirst();
        numberOfMembersRegistered = cursor.getInt(0);

        return numberOfMembersRegistered;
    }

    private int getStrenghtOfPool(int poolID) {
        int strength = 0;

        db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select " + Utils.poolStrength + " from " + Utils.poolDetailsTable + " where " + Utils.poolId + " = " + poolID, null);

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


    public ArrayList<PoolDetails> fetchMypools(int memberID){
        ArrayList<PoolDetails> myPools = new ArrayList<>();

        /*select pd.PoolID,pd.PoolName
            from pooltransactions pt
              JOIN
              pooldetails pd
              ON pt.PoolID = pd.PoolID
            where pt.MemberID = 8 and pt.WinnerFlag = 99 and pt.CurrentCounter = -1*/

        db = getWritableDatabase();
        Cursor cursor = db.rawQuery(" select pd." + Utils.poolId + ",pd." +Utils.poolName + " from " + Utils.poolTransactions + " pt" + " JOIN " + Utils.poolDetailsTable  + " pd" +
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
        Cursor cursor = db.rawQuery("select " + Utils.poolCurrentCounter + " from " + Utils.poolDetailsTable + " where " + Utils.poolId + " = " + poolID, null);

        countFetched = cursor.getInt(1);

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

        Cursor cursor = db.rawQuery("Select * from " + Utils.poolDetailsTable + " where " + Utils.poolAdminId + " = " + adminId, null);


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
                pool.setPoolStartDate(DateCalculationsUtil.stringToSQLDate(cursor.getString(8)));
                pool.setPoolEndDate(DateCalculationsUtil.stringToSQLDate(cursor.getString(9)));
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
        Cursor cursor = db.rawQuery("select count(*) from " + Utils.poolTransactions + " where " + Utils.poolId + " = " + poolID + " and " + Utils.poolCurrentCounter + "!=1 and" + Utils.memberPayementDate + "is not null", null);

        numberOfTransactions = cursor.getInt(1);
        return numberOfTransactions;
    }


    public boolean isValidPoolAdd(int poolID) {
        boolean valid;
        int numberOfTransactions = getNumberOfTransaction(poolID);
        int strength = getStrenghtOfPool(poolID);

        if (numberOfTransactions < (strength * strength)) valid = true;
        else valid = false;
        return valid;
    }


    public boolean isValidAdmin(int poolID, int adminID) {
        boolean status = false;
        //select count(*) from pooldetails where PoolID = ? AND  PoolAdminMemberID = ?

        db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from " + Utils.poolDetailsTable + " where " + Utils.poolId + " = " + poolID + " and " + Utils.poolAdminId + "=" + adminID, null);

        if (cursor.getInt(1) == 1) status = true;
        else status = false;

        return status;
    }


    public boolean isValidMember(int poolID, int memberID) {
        boolean status;
        //select count(*) from pooltransactions " +
        //where PoolID = ? AND  MemberID = ? AND CurrentCounter = -1 and WinnerFlag = 99 ");

        db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from " + Utils.poolTransactions + " where "
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
            Cursor cursor = db.rawQuery("select count(*) from " + Utils.poolTransactions + " where "
                    + Utils.poolId + " = " + poolID + " and " + Utils.poolCurrentCounter + "=" + counter + " and " + Utils.memberPayementDate + " is not null ", null);

            payerCount = cursor.getInt(1);
        } else {
            db = getWritableDatabase();
            Cursor cursor = db.rawQuery("select count(*) from " + Utils.poolTransactions + " where "
                    + Utils.poolId + " = " + poolID + " and " + Utils.poolCurrentCounter + "=" + counter, null);

            payerCount = cursor.getInt(1);
        }

        return payerCount;
    }

    private static boolean isDelay(PoolDetails activePool) {

        int depositDay = activePool.getPoolDepositDate();
        int currentCounter = activePool.getPoolCurrentCounter();
        Date startDate = activePool.getPoolStartDate();

        java.util.Date monthStartDate = DateCalculationsUtil.addMonth(startDate, currentCounter);

        java.util.Date depositDate = DateCalculationsUtil.addDay(monthStartDate, depositDay);

        java.util.Date currentDate = new java.util.Date();

        if (currentDate.after(depositDate)) return true;
        else return false;

    }

    private void updateDelayPayments(PoolDetails activePool, int memberID) {
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

    private int updateCurrentCounter(PoolDetails activePool) {
        int oldCounter = activePool.getPoolCurrentCounter();
        int newCounter;

        if (oldCounter == -1) {
            newCounter = 1;
        } else {
            newCounter = ++oldCounter;
        }
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

        double individualContri = activePool.getPoolIndividualShare();

        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Utils.poolId, activePool.getPoolId());
        contentValues.put(Utils.memberId, memeberID);
        contentValues.put(Utils.poolCurrentCounter, activePool.getPoolCurrentCounter());
        contentValues.put(Utils.poolIndividualShare, activePool.getPoolIndividualShare());
        contentValues.put(Utils.memberPayementDate, String.valueOf(currentDate));

        db.insert(Utils.poolTransactions, null, contentValues);

    }

    public int makePaymentForMember(PoolDetails activePool, int MemeberID) {


        int strenght = getStrenghtOfPool(activePool.getPoolId());
        int currentMaxCounter = getCounterFromPoolDetails(activePool.getPoolId());
        int payersCount = groupPaymentDoneCount(activePool.getPoolId(), currentMaxCounter);

        System.out.println("[DEBUG](makePaymentForMember)Strenght : " + strenght);
        System.out.println("[DEBUG](makePaymentForMember)Current Counter : " + currentMaxCounter);
        System.out.println("[DEBUG](makePaymentForMember)Payer Count :" + payersCount);

        if (isDelay(activePool))
            updateDelayPayments(activePool, MemeberID);
        else {
            if ((currentMaxCounter + 1) <= strenght) {
                if ((payersCount == strenght)) {

                    currentMaxCounter = updateCurrentCounter(activePool);
                    activePool.setPoolCurrentCounter(currentMaxCounter);
                    System.out.println("[DEBUG](makePaymentForMember)Updated current counter:" + currentMaxCounter);
                }
                insertNewBlankTransaction(activePool, MemeberID);
                System.out.println("[DEBUG](makePaymentForMember)Transaction added");
            }
            System.out.println("[DEBUG](makePaymentForMember)Pool is Complete");
        }
        return currentMaxCounter;
    }

    private int getNumberOfWinners(int poolID) {
        int numberOfWinners = 0;
        //select count(*) from pooltransactions where PoolID = ? and CurrentCounter !=-1 and WinnerFlag = 1");
        db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from " + Utils.poolTransactions + " where "
                + Utils.poolId + " = " + poolID + " and " + Utils.poolCurrentCounter + "!=-1 and " + Utils.poolWinnerFlag + " = 1 ", null);

        numberOfWinners = cursor.getInt(1);
        return numberOfWinners;
    }


    public boolean isValidPickWinner(PoolDetails activePool) {
        boolean valid;
        int numberOfWinners = getNumberOfWinners(activePool.getPoolId());

        //"select count(*) from pooltransactions where PoolID = ? and CurrentCounter =? and PickerFlag = 1");
        db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from " + Utils.poolTransactions + " where "
                + Utils.poolId + " = " + activePool.getPoolId() + " and " + Utils.poolCurrentCounter + "=" + activePool.getPoolCurrentCounter() + Utils.poolWinnerFlag + " = 1 ", null);

        int checkIfWinner = cursor.getInt(1);

        if ((numberOfWinners < activePool.getPoolStrength()) & (checkIfWinner == 0)) valid = true;
        else valid = false;
        return valid;

    }

    private Collection<Integer> getPoolMembers(PoolDetails activePool) {

        Collection<Integer> allPoolMembers = new ArrayList<>();

        //select MemberID from pooltransactions where PoolID = ? AND CurrentCounter = ?");
        db = getWritableDatabase();
        Cursor cursor = db.rawQuery(" select " + Utils.memberId + " from " + Utils.poolTransactions + " where "
                + Utils.poolId + " = " + activePool.getPoolId() + " and " + Utils.poolCurrentCounter + " =-1 ", null);

        while (cursor.moveToNext()) {
            allPoolMembers.add(cursor.getInt(1));
        }

        return allPoolMembers;
    }

    private Collection<Integer> getPoolMembersWhoPaid(PoolDetails activePool) {

        Collection<Integer> allPoolMembers = new ArrayList<>();

        //select MemberID from pooltransactions where PoolID = ? AND CurrentCounter = ?");
        db = getWritableDatabase();
        Cursor cursor = db.rawQuery(" select " + Utils.memberId + " from " + Utils.poolTransactions + " where "
                + Utils.poolId + " = " + activePool.getPoolId() + " and " + Utils.poolCurrentCounter + " = " + activePool.getPoolCurrentCounter(), null);

        while (cursor.moveToNext()) {
            allPoolMembers.add(cursor.getInt(1));
        }

        return allPoolMembers;
    }

    private Collection<Integer> getPoolMembersRemainingToPay(PoolDetails activePool) {
        Collection<Integer> allPoolMembers = getPoolMembers(activePool);
        Collection<Integer> poolMembersWhoPaid = getPoolMembersWhoPaid(activePool);

        allPoolMembers.removeAll(poolMembersWhoPaid);

        return allPoolMembers;
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
            contentValues.put(Utils.poolIndividualShare, activePool.getPoolIndividualShare());

            db.insert(Utils.poolTransactions, null, contentValues);

        }

    }

    private Collection<Integer> getPoolMembersWhoWon(PoolDetails activePool) {

        Collection<Integer> allPoolMembers = new ArrayList<>();

        //select MemberID from pooltransactions where PoolID = ? AND CurrentCounter = ?");
        db = getWritableDatabase();
        Cursor cursor = db.rawQuery(" select " + Utils.memberId + " from " + Utils.poolTransactions + " where "
                + Utils.poolId + " = " + activePool.getPoolId() + " and " + Utils.poolPickerFlag + " = 1", null);

        while (cursor.moveToNext()) {
            allPoolMembers.add(cursor.getInt(1));
        }

        return allPoolMembers;
    }

    private Collection<Integer> getPoolMemberRemainingToWin(PoolDetails activePool) {
        Collection<Integer> allPoolMembers = getPoolMembers(activePool);
        Collection<Integer> poolMembersWhoWon = getPoolMembersWhoWon(activePool);

        allPoolMembers.removeAll(poolMembersWhoWon);

        return allPoolMembers;
    }

    private void addWinner(PoolDetails activePool) {
        ArrayList<Integer> poolMembersRemainingToWin = (ArrayList<Integer>) getPoolMemberRemainingToWin(activePool);

        Random rand = new Random();
        int randomElement = poolMembersRemainingToWin.get(rand.nextInt(poolMembersRemainingToWin.size()));

        System.out.println("*******____CONGRATULATIONS Member ID " + randomElement + ",You are Winner for " + activePool.getPoolCurrentCounter() + " iteration____*********");
        long millis = System.currentTimeMillis();
        java.sql.Date currentDate = new java.sql.Date(millis);

        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Utils.poolMonthlyTakeAway, activePool.getPoolMonthlyTakeAway());
        contentValues.put(Utils.poolPickerTakeawayDate, String.valueOf(currentDate));
        contentValues.put(Utils.poolWinnerFlag, 1);
        db.update(Utils.poolTransactions, contentValues, Utils.poolId + " = " + activePool.getPoolId() + " and "
                + Utils.poolCurrentCounter + " = " + activePool.getPoolCurrentCounter() + " and " + Utils.memberId + " = " + randomElement, null);

        db = this.getWritableDatabase();
        ContentValues restContentValues = new ContentValues();
        restContentValues.put(Utils.poolWinnerFlag, 0);
        db.update(Utils.poolTransactions, contentValues, Utils.poolId + " = " + activePool.getPoolId() + " and "
                + Utils.poolCurrentCounter + " = " + activePool.getPoolCurrentCounter() + " and " + Utils.poolWinnerFlag + " != 1", null);


    }

    public void pickWinnerForCurrentMonth(PoolDetails activePool) {
        int strength = getStrenghtOfPool(activePool.getPoolId());
        int payerCount = groupPaymentDoneCount(activePool.getPoolId(), activePool.getPoolCurrentCounter());

        if (payerCount < strength) {
            fillUpBlankTransaction(activePool);
            addWinner(activePool);
        } else if (payerCount == strength) {
            addWinner(activePool);
        }
    }


    private Collection<Integer> getPoolMemberForCurrentCycle(PoolDetails activePool){
        Collection<Integer> poolMemberWhoWonForCurrentCycle = new ArrayList<>();
            //select MemberID from pooltransactions WHERE PoolID = ? AND WinnerFlag = ? and CurrentCounter = ?");
        db = getWritableDatabase();
        Cursor cursor = db.rawQuery(" select " + Utils.memberId + " from " + Utils.poolTransactions + " where "
                + Utils.poolId + " = " + activePool.getPoolId() + " and " + Utils.poolCurrentCounter + " = " + activePool.getPoolCurrentCounter() + " and " + Utils.poolWinnerFlag + " = 1 ",null);

        while(cursor.moveToNext()){
            poolMemberWhoWonForCurrentCycle.add(cursor.getInt(1));
        }
        return poolMemberWhoWonForCurrentCycle;
    }

    public Collection<Integer> printPoolMemberRemainingToWin(PoolDetails activePool){
        Collection<Integer> allPoolMembers = getPoolMembers(activePool);
        Collection<Integer> poolMembersWhoWon = getPoolMembersWhoWon(activePool);
        Collection<Integer> poolMemberWhoWonForCurrentCycle = getPoolMemberForCurrentCycle(activePool);
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
            contentValues.put(Utils.poolMonthlyTakeAway,pickerTakeaway);
            contentValues.put(Utils.poolPickerTakeawayDate, String.valueOf(currentDate));
            contentValues.put(Utils.poolPickerFlag,1);
            db.update(Utils.poolTransactions,contentValues,Utils.poolId+ " = " +activePool.getPoolId() + " and "
                    +Utils.poolCurrentCounter + " = " + activePool.getPoolCurrentCounter() + " and " + Utils.memberId + " = " + pickerMemberID  ,null);

            //UPDATE pooltransactions set TakeawayAmount = ?,TakeawayDate = ? where PoolID = ? and CurrentCounter = ? and WinnerFlag = ?");

            db = this.getWritableDatabase();
            ContentValues restContentValues = new ContentValues();
            contentValues.put(Utils.poolMonthlyTakeAway,winnerTakeaway);
            contentValues.put(Utils.poolPickerTakeawayDate, String.valueOf(currentDate));

            db.update(Utils.poolTransactions,contentValues,Utils.poolId+ " = " +activePool.getPoolId() + " and "
                    +Utils.poolCurrentCounter + " = " + activePool.getPoolCurrentCounter() + " and " + Utils.poolWinnerFlag + " = 1" ,null);

    }

}//end


