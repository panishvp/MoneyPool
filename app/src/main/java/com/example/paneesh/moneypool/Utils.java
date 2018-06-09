package com.example.paneesh.moneypool;

public class Utils {

    public static String EmailRegex = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";
    public static String validEmail = "Please Enter a Valid Email";
    public static String validPassword = "Password must be atleast 5 characters long";
    public static final String MyPREFERENCES = "MyPrefs" ;

    public static String memberObject = "Member";

    public static String databaseName = "MoneyPool";
    public static String memberTable = "member";
    public static String poolDetailsTable = "pool_details";
    public static String poolTransactions = "pool_transactions";

    public static String memberId = "member_id";
    public static String memberFirstName = "member_first_name";
    public static String memberLastName = "member_last_name";
    public static String memberEmail = "member_email";
    public static String memberPassword = "member_password";
    public static String memberAddress = "member_address";
    public static String memberIban = "member_iban";
    public static String memberSwift = "member_swift";
    public static String memberNominee = "member_nominee";
    public static String memberPhoneNumber = "member_phone_number";


    public static String poolId = "pool_id";
    public static String poolAdminId= "pool_admin_id";
    public static String poolName = "pool_name";
    public static String poolStrength = "pool_strength";
    public static String poolDuration = "pool_duration";
    public static String poolCurrentCounter = "pool_current_counter";
    public static String poolIndividualShare = "pool_individual_share";
    public static String pdPoolMonthlyTakeAway = "pool_monthly_take_away";
    public static String poolStartDate = "pool_start_date";
    public static String poolEndDate= "pool_end_date";
    public static String poolMeetUp = "pool_meetup_date";
    public static String poolDepositDate = "pool_deposit_date";
    public static String poolLateFees = "pool_late_fee_charge";
    public static String poolWinnerFlag = "pool_winner_flag";
    public static String memberPayementDate = "pool_payment_date";
    public static String poolDelayFlag = "pool_delay_flag";
    public static String poolDelayPaymentAmt = "pool_delay_payment_amount";
    public static String poolPickerFlag = "pool_picker_flag";
    public static String poolPickerTakeawayDate = "pool_takeaway_date";
    public static String ptPoolTakeAwayAmount = "pool_take_away_amount";
    public static String pool_individual_monthly_share = "pool_individual_monthly_share";


    public static String alertUserIsPresent = "The Entered Email is already Registered \n Please Register with a different Email Id";
    public static String alertUserForValidInput = "Please fill the fields with Valid Input";

    public static  String datePattern = "dd-mm-yyyy";
}
