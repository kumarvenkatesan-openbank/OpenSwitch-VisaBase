package money.open.cards.visabase.constants;

public interface TPEConstants {
	String SCHEMA_DEFAULT = "DEF";
	int MASTERCARD = 1;
	int VISA = 2;
	int RUPAY = 3;
	int HOST = 6;
	int POSTILION = 5;
	int MERCURY = 4;

	String CVV_SUCCESS = "2";
	String CVV_FAILED = "1";
	String CVV2_SUCCESS = "2";
	String CVV2_FAILED = "1";
	String CVV2_FAILED_VISA = "N";
	String CVV2_SUCCESS_VISA = "M";
	String CVV2_NOT_PROCESSED_VISA = "P";
	String CVV1_FAILED_VISA = "Y";

	int mini_statement_count = 10;
	String RUPAY_SIGNON = "001";
	String RUPAY_SIGNOFF = "001";
	String RUPAY_ECHO = "201";

	public static final int CARD_STATUS_INACTIVE = 0;

	public static final int CARD_STATUS_DAMAGED = 4;

	public static final int CARD_STATUS_LOST_AND_STOLEN = 2;

	public static final int CARD_STATUS_BLOCKED = 3;

	public static final int CARD_STATUS_SUSPEND = 9;
	public static final int CARD_STATUS_HOTLIST = 11;
	public static final int CARD_STATUS_EXPIRED = 7;
	public static final int CARD_ISSUED_STATUS = 1;

	double NO_AUTHlIMIT_CHAECK_FOR_AMOUNT = 0;
	public static final String IssuerAuthenticationData = "91";
	String PREAUTH_VALID_FLAG = "Y";
	String PREAUTH_INVALID_FLAG = "N";
	String PREAUTH_NOT_EXPIRED = "N";
	String PREAUTH_EXPIRED = "Y";
	String PREAUTH_COMPLETION = "Y";
	String PREAUTH_RELEASED = "R";
	String PREAUTH_NOT_COMPLETED = "N";
	String PREAUTH_ORIGINAL = "O";

	// VISA Constatnts
	String LEDGER_BALANCE = "01";
	String CURRENT_BALANCE = "02";
	public static final int CONTINUE = 1;
	public static final int DECLINE = 2;
	public static final int DECLINE_AND_CAPTURECARD = 3;
	public static final int REFER_CARDISSUER = 4;
	public static final String TXN_APPROVED = "00";
	public static final String TXN_PARTIALY_APPROVED = "10";
	int ARQC_VERIFIED_SUCCESS = 2;
	int ARQC_VERIFIED_FAILED = 1;

	String SMS_SIGNON = "0081";
	String DMS_SIGNON = "0071";
	String SIGNOFF_93 = "802";
	String SMS_SIGNOFF = "0082";
	String DMS_SIGNOFF = "0072";
	String ECHO_TEST = "0301";
	String LOGON_93 = "801";
	String HOST_SIGNON = "001";
	String HOST_SIGNON_POSTILION = "001";
	String VISA_SIGNON = "0071";
	String VISA_SIGNON_ADVICE = "0078";
	String POSTILION_KEY_EXCHANGE = "101";
	String BANKSERV_CUTOVER = "201";
	String BANKSERV_KEY_EXCHANGE = "161";
	String BANKSERV_INIT_KEY_EXCHANGE = "162";
	String HOST_SIGNOFF = "002";
	String HOST_SIGNOFF_POSTILION = "002";
	String VISA_SIGNOFF1 = "0072";
	String VISA_SIGNOFF2 = "0082";
	String HOST_ECHO = "0301";
	String HOST_ECHO_MERCURY = "301";
	String HOST_ECHO_POSTILION = "301";
	String KEY_EXCHANGE = "0163";
	String KEY_EXCHANGE_MERCURY_161 = "161";
	String KEY_EXCHANGE_MERCURY_162 = "162";
	String DO_NOT_HONOR = "005";
	String FORMAT_ERROR = "006";
	String VISA_ISSUER_SIGNOFF = "091";
	String NMM_APPROVED = "00";
	String NMM_SIGNON_APPROVED_93 = "800";
	String APPROVED_TXN = "00";
	String ECHO_93 = "831";

	String credit = "CR";
	String debit = "DR";
	String notApplicable = "NA";
	int ACTIVE_RECORD = 1;
	int CARD_LEVEL = 1;
	int ACCOUNT_LEVEL = 2;
	int PURSE_LEVEL = 1;
	int INITIAL_USAGE_LEVEL = 1;

	int PRIMARY_ACCOUNT_OPEN = 3;
	int SECONDARY_ACCOUNT_OPEN = 8;
	String KEY_FORMAT = "11";

	int CARD_LEVEL_CFG = 4;
	int CARD_PRG_PARAM_LEVEL = 3;
	int CARD_PROGRAM_LEVEL = 2;
	int PROGRAM_LEVEL = 1;
	int TRANSACTION_FEES = 5;
	int CVV_CHECK_REQUIRED = 1;
	int ALL_MODE = 1;
	int SELECTED_MODE = 2;
	int PIN_UN_BLOCKED = 1;
	int PIN_BLOCKED = 2;

	public static final String KMS_PVK_KEYS = "PVK";
	public static final String KMS_PPK_KEYS = "PPK";
	public static final String KMS_CVK_KEYS = "CVK";

	public static final String KMS_IMKAC = "MK-AC";
	public static final String KMS_IMKMAC = "MK-SMI";
	public static final String KMS_IMKENC = "MK-SMC";
	int MCHIP4_IAD_LENGTH = 17;
	public static final int CVN_FIVE = 5;
	public static final int CVN_SIX = 6;
	public static final int CVN_TEN = 10;
	public static final int CVN_ELEVEN = 11;
	public static final int CVN_TWELVE = 12;
	public static final int CVN_THIRTEEN = 13;
	public static final int CVN_EIGHTEN = 18;
	public static final int CVN_SIXTEN = 16;
	public static final int CVN_SEVENTEN = 17;
	public static final int CVN_TWENTYONE = 21;
	public static final int CVN_TWENTYTWO = 22;
	public static final int CVN_THIRTYFOUR = 34;

	String SUB_FIID_IDENTIFICATION = "SF";
	String FIID_IDENTIFICATION = "FI";
	String DEFAULT_SCHEMA = "CMS";
	public static final int USAGE_STATUS = 2;
	public static final String KMS_TRANSACTION_KEYS = "IMKAC','IMKMAC','IMKENC', 'ZPK' ,'PVK', 'PPK','CVK";

	String impsWalletToWallet = "01";
	String impsWalletToAccount = "02";
	String eCash = "99";

	int loyaltyCoolingId = 4;

	/*
	 * Constant File Value from 112 added for IDFC
	 */

	int BASE24 = 6;
	String TXN_NAME = "Card Activation";
	int CVV_NOT_PROCESSED = 3;

	int LOG_FLAG_RESET = 0;

	int ISSUER_SCRIPT_SENT = 1;

	String MC_SIGNON = "001";
	String MC_SIGNOFF = "002";
	String MC_ECHO = "270";
	String MC_KEY_EXCHANGE = "161";
	String RUPAY_KEY_EXCHANGE = "184";

	public static final int PURCHASE_ONLY_TXN = 1;

	public static final int CARD_STATUS_ACTIVE = 1;
	public static final int ISSUER_SCRIPT_PIN_CHANGE = 1;
	public static final int ISSUER_SCRIPT_PIN_UNBLOCK = 2;
	public static final int ISSUER_SCRIPT_APPLICATION_BLOCK = 3;
	public static final int ISSUER_SCRIPT_APPLICATION_UNBLOCK = 4;
	public static final int ISSUER_SCRIPT_CARD_BLOCK = 5;
	public static final int ISSUER_SCRIPT_UPDATE_RECORD = 6;

	public static final int ACCOUNT_SPENDINGS = 1;
	public static final int ACCOUNT_GLOBAL_SERVICE = 22;
	public static final String GLOBAL_SERVICE_ID = "FFFE";
	public static final String EMPTY_SERVICE_ID = "0000";
	public static final String GLOBAL_SERVICE_BLANK_AMOUNT = "0000000000";
	public static final String SERVICE_CREATION_IDENTIFIER = "SRVCR";
	public static final String SERVICE_TRANSACTION_IDENTIFIER = "SRVTR";

	// Added by Balachandrasekhar.P for SMS and Email alerts

	public static final int ATM_NOTIFICATION_MODULE_ID = 23;
	public static final int POS_NOTIFICATION_MODULE_ID = 24;
	public static final int CASH_BACK_NOTIFICATION_MODULE_ID = 25;
	public static final int CASH_ADVANCE_NOTIFICATION_MODULE_ID = 26;
	public static final int ECOMM_NOTIFICATION_MODULE_ID = 27;
	public static final int ATM_PINCHANGE_NOTIFICATION_MODULE_ID = 28;

	public static final int ATM_REVERSAL_NOTIFICATION_MODULE_ID = 29;
	public static final int POS_REVERSAL_NOTIFICATION_MODULE_ID = 30;
	public static final int CASH_BACK_REVERSAL_NOTIFICATION_MODULE_ID = 31;
	public static final int CASH_ADVANCE_REVERSAL_NOTIFICATION_MODULE_ID = 32;
	public static final int ECOMM_REVERSAL_NOTIFICATION_MODULE_ID = 33;
	public static final int ATM_REVERSAL_PINCHANGE_NOTIFICATION_MODULE_ID = 34;
	public static final int PIN_BLOCK_NOTIFICATION_MODULE_ID = 54;

	public static final int SMS_VALUE = 1;
	public static final int EMAIL_VALUE = 2;
	public static final int SMS_AND_EMAIL = 3;
	public static final int BANK_CODE = 0;
	public static final String TRANSACTION_TYPE = "B2B";
	public static final String SBI = "SBI";
	public static final String INR_CURRENCY_CODE = "356";
	public static final String ZAR_CURRENCY_CODE = "710";
	public static final int SMS_EMAIL_NOTIFICATION_ON = 3;
	public static final String COMM_TXN_CODE = "Txn";
	public static final String QSPARC_PAYMENT_MODE_ACCOUNT = "01";
	public static final String QSPARC_PAYMENT_MODE_CASH = "02";

	public static final String TERMINAL_CAPABLITY_MODE_EMV = "5";
	public static final String POS_ENTRY_MODE_MAGSTRIPE = "02";
	public static final String RUPAY_IAD_FORMAT_VERSION_A = "A";
	public static final String RUPAY_IAD_FORMAT_VERSION_B = "B";
	public static final String EMPTY_ISSUER_DATA = "00000000";
	public static final String ECI_INDICATOR_FIVE = "5";
	public static final String ECI_INDICATOR_SEVEN = "7";
	public static final String CAVV_RESULT_CODE_SUCCESS = "2";
	public static final String CVV2_VERIFICATION_EMPTY = " ";
	public static final String CVV2_VERIFICATION_NOT_PERFORMED = "N";
	public static final String CVV2_VERIFICATION_SUCCESS = "Y";
	public static final String CVV2_VERIFICATION_DECLINE = "D";
	
	public static final String MASTERCARD_CVV2_VERIFICATION_SUCCESS = "M";
	public static final String MASTERCARD_CVV2_VERIFICATION_DECLINE = "N";
	public static final String MASTERCARD_CVV2_VERIFICATION_NOT_PERFORMED = "X";
	

	public static final String CVV2_Verification_Failure_Msg = "CVV2 Verification Failed";
	public static final String ARQC_Verification_Failure_Msg = "ARQC Verification Failed";
	public static final String CVV_Verification_VISA_Failure_Msg = "CVV Verification fails from VISA";
	public static final String CVV2_Verification_VISA_Failure_Msg = "CVV2 Verification failed in VISA";
	public static final String CVV_Verification_Failure_Msg = "CVV Verification fails";
	public static final String Track_data_NP_Failure_Msg = "Track2 Data not Present For CP Txn";
	public static final String Selected_acct_Failure_Msg = "Selected account is not available";
	public static final String EMV_Data_Not_present_Failure_Msg = "EMV Data not present in request";
	public static final String Mag_Txn_not_supported_Failure_Msg = "Magstripe txn is not supported for EMV cards";
	public static final String Chip_Txn_not_supported_For_Magstripe = "Chip txn is not supported for Magstripe cards";
	public static final String AVS_Verification_failed = "AVS Verification failed";
	public static final String Card_status_updation_failed = "Unable to update Card status";
	public static final String Secure_3D_verification_failed = "3D Secure Verification failed";
	public static final String Key_importion_fails = "Key Import Operation fails";
	public static final String New_old_Pin_verify_fails = "New PIN and Old PIN are same";
	public static final String Imk_keys_not_configured = "IMKac Key is not configured";
	public static final String Pin_offset_updation_failed = "Unable to update Pin Offset";
	public static final String Expiry_date_not_available = "Expiry date not found";
	public static final String Card_Status_Inactive = "CARD_STATUS_INACTIVE";
	public static final String Card_Status_Damaged = "CARD_STATUS_DAMAGED";
	public static final String Card_Status_Suspend = "CARD_STATUS_SUSPEND";
	public static final String Card_Not_Issued = "CARD_NOT_ISSUED";
	public static final String Invalid_CAVV_Status = "Invalid CAVV Status";
	public static final String Invalid_CAVV_INR_Status = "Invalid CAVV Status for INR Card";
	public static final String Invalid_ECI_Indicator = "Invalid ECI Indicator";
	public static final String Invalid_CVV2_Not_present = "CVV2 not present";

	public static final int SIGN_ON_VALUE = 1;
	public static final int SIGN_OF_VALUE = 0;
	public static final String BASE_24 = "B1";
	public static final String POSTILION_INTERCHANGE = "R1";
	public static final String VISA_INTERCHANGE = "VI";
	public static final String MASTERCARD_INTERCHANGE = "M1";
	public static final String BASE24_INTERCHANGE = "B1";
	public static final String RAKBANKSwitch = "RAK";
	public static final String MIPSwitch = "MIP";
	public static final String UAESwitch = "UAE";
	public static final String GCCSwitch = "GCC";
	public static final String MERCURYCARD_INTERCHANGE = "C1";

	String SETTLEMENT_COMPLETE = "C";
	String SETTLEMENT_OPEN = "O";
	String PARTIAL_SETTLEMENT = "P";
	String EXPIRED_SETTLED = "E";
	int SETTLEMENT_EXPIRY_TIME_PERIOD = 1;

	// Added by Ayesha for Partial and Full Reversal
	String Full_Reversal = "R";
	String Partial_Reversal = "PR";
	int CARD_STATUS_CANCELLED = 16;
	String Card_Status_Cancelled = "CARD_STATUS_CANCELLED";
	String Declined_FEE_STATUS_FLAG = "O";

	// Added by Ayesha for Ecommerce Transactions
	public static final String ECI_INDICATOR_POSTILION_92 = "92";
	public static final String ECI_INDICATOR_POSTILION_94 = "94";
	public static final String ECI_INDICATOR_VISA_05 = "05";
	public static final String ECI_INDICATOR_VISA_07 = "07";
	public static final String ECI_INDICATOR_MASTER_CARD_05 = "24";
	public static final String ECI_INDICATOR_MASTER_CARD_07 = "91";
	public static final String ECOMM_TRAN_CODE = "EC";
	public static final String CVV2_Valid_Postilion = "M";
	public static final String CVV2_Invalid_Postilion = "N";
	public static final String CVV2_Unable_to_Perform_Postilion = "P";
	public static final String CDM_Process_code_1 = "30";
	public static final String CDM_Process_code_2 = "31";
	public static final String FEES = "FE";
	public static final String FEES_REMARKS = " SRV CHG";
	public static final String SERVICE_TAX = "SV";
	public static final String SERVICE_TAX_REMARKS = " VAT";
	public static final String DECLINE_REMARKS = " DECLINE";

	int FULL_HUNTING_MODE = 1;
	int NO_HUNTING_MODE = 0;
	int PARTIAL_HUNTING_MODE = 2;
	String MARKUP_RATE = "MR";
	String MARKUP_RATE_REMARKS = " MARKUP CHG";
	int PURSELEVEL_DIFF = 2;
	String MASTERCARD_SESSIONACTIVATION = "081";
	String MASTERCARD_SIGNON1 = "061";
	String MASTERCARD_SIGNON2 = "065";
	String MASTERCARD_SIGNOFF1 = "062";
	String MASTERCARD_SIGNOFF2 = "066";
	String MASTERCARD_ECHO = "270";
	String MASTERCARD_KEYEXCHANGE1 = "161";
	String MASTERCARD_KEYEXCHANGE2 = "162";
	String P25_COMPLETION_ADVICE = "06";

	String FORGOT_PIN = "P";
	int USAGE_STATUS_IN_ACTIVE = 1;
	int USAGE_STATUS_ACTIVE = 2;

	String CURRENCY_CONVERSION_MODE_OBC = "2";
	String CURRENCY_CONVERSION_MODE_INHOUSE = "1";
	String SINGLE_PURSE_MODE = "1";
	String MULIT_PURSE_MODE = "2";

	String SINGLE_CURRRENCY_MODE = "1";
	String MULIT_CURRENCY_MODE = "2";

	String CARD_TRANSFER_TXN_CODE = "CT";
	String WALLET_TRANSFER_TXN_NAME = "Wallet To Wallet Transfer";
	String WALLET_TRANSFER_DR = "W2W-DR";
	String WALLET_TRANSFER_CR = "W2W-CR";
	String SLASH = "/";
	String HYPHEN = "-";
	int INTERNATION_TARANSCATION_INDICATOR = 2;
	int DOMESTIC_TARANSCATION_INDICATOR = 1;

	// Toggle Error description

	String TOGGLE_SUCCESS = "TRANSACTION SUCCESS";
	String TOGGLE_SAFE_MODE_PROTECTION_ENABLED = "SAFE MODE PROTECTION ENABLED";
	String TOGGLE_SAFE_MODE_AMOUNT_EXCEEDS = "SAFE MODE AMOUNT EXCEEDS";
	String TOGGLE_INTERNATIONAL_TRANSACTION_BlOCKED = "INTERNATIONAL TRANSACTIONS BLOCKED";
	String TOGGLE_CARD_BLOCKED = "CARD BLOCKED";
	String TOGGLE_COUNTRY_BLOCKED = "COUNTRY BLOCKED";
	String TOGGLE_CURRENCY_BLOCKED = "CURRENCY BLOCKED";
	String TOGGLE_DELIVERY_CHANNEL_BLOCKED = "DELIVERY CHANNEL BLOCKED";
	String TOGGLE_MERCHANT_CATAGORY_BLOCKED = "MERCHANT CATEGORY BLOCKED";
	String TOGGLE_TRANSACTION_TYPE_BLOCKED = "TRANSACTION TYPE BLOCKED";
	String TOGGLE_LOCATION_BLOCKED = "LOCATION BLOCKED";
	String TOGGLE_TRANSACTION_TYPE_MISCONFIGURATION = "TRANSACTION TYPE MISCONFIGURATION";
	String TOGGLE_TRANSACTION_TYPE_AMOUNT_EXCEEDS_LIMIT = "TRANSACTION AMOUNT EXCEEDS LIMIT";
	String TOGGLE_UNSUPPORTED_TRANSACTION_TYPE = "UNSUPPORTED TRANSACTION";
	String TOGGLE_CARD_DETAILS_NOT_AVAILABLE = "CARD DETAILS NOT AVAILABLE";
	String TOGGLE_CURRENCY_DETAILS_NOT_AVAILABLE = "CURRENCY DETAILS NOT AVAILABLE";
	String TOGGLE_BIN_DETAILS_NOT_AVAILABLE = "BIN DETAILS NOT AVAILABLE";
	String TOGGLE_INST_DETAILS_NOT_AVAILABLE = "INSTITUTION DETAILS NOT AVAILABLE";
	String TOGGLE_MODIFIED_BILLING_AMT_NT_AVL = "MODIFIED BILLING AMOUNT_NOT AVAILABLE";
	String TOGGLE_CARD_INACTIVE = "CARD INACTIVE";
	String TOGGLE_CARD_CLOSED = "CARD CLOSED";
	String TOGGLE_UNABLE_TO_PROCESS = "UNABLE TO PROCESS";
	String TOGGLE_UNABLE_TO_INSERT = "UNABLE TO INSERT";

	// POSTILION Error Constants
	String EXCEED_MAXIMUM_CARD_LIMIT = "28";
	String EXCEED_MONTHLY_WITHDRAWAL_LIMIT = "17";
	String EXCEED_MONTHLY_WITHDRAWAL_COUNT_LIMIT = "18";
	String EXCEED_YEARLY_WITHDRAWAL_LIMIT = "19";
	String EXCEED_YEARLY_WITHDRAWAL_COUNT_LIMIT = "20";
	String EXCEED_WEEKLY_WITHDRAWAL_LIMIT = "21";
	String EXCEED_WEEKLY_WITHDRAWAL_COUNT_LIMIT = "22";
	String BELOW_MINIMUM_AMOUNT = "23";
	String EXCEED_MAXIMUM_AMOUNT = "24";
	String Wallet_Expiry_date_not_available = "Wallet_Expiry date not found";
	String WALLET_EXPIRY_FORMAT = "yyyy-MM-dd";


}
