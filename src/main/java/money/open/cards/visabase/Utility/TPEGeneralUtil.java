package money.open.cards.visabase.Utility;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TPEGeneralUtil {

	public static boolean isDateStarted(Date startDate) {
		boolean isStarted = false;
		if (new Date().compareTo(startDate) >= 0) {
			isStarted = true;
		}

		return isStarted;
	}

	public static boolean isDateEnded(Date endDate) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		boolean isEnded = false;
		if (new Date(cal.getTimeInMillis()).compareTo(endDate) > 0) {
			isEnded = true;
		}

		return isEnded;
	}

	// Updated By Anburaj Ravi on 09/01/2018 - get Mid-night txn time
	public static Date getTransactionDate(String localTransactionDate) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, Integer.parseInt(localTransactionDate.substring(0, 2)) - 1);
		cal.set(Calendar.DATE, Integer.parseInt(localTransactionDate.substring(2, 4)));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static Timestamp getTimeStampForTxn(String txnDate, String txnTime) throws Exception {
		Timestamp timestamp = null;
		String dateFormat = "MMddhhmmss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		try {
			Date date = simpleDateFormat.parse(txnDate + txnTime);
			timestamp = new Timestamp(date.getTime());
		} catch (Exception e) {
			throw new Exception();
		}
		return timestamp;
	}

	public static boolean isSameDay(Timestamp dbTimestamp) {
		Calendar dbCal = Calendar.getInstance();
		dbCal.setTimeInMillis(dbTimestamp.getTime());
		Calendar txnCal = Calendar.getInstance();
		return checkSameDay(dbCal, txnCal);
	}

	public static boolean checkSameDay(Calendar dbCal, Calendar txnCal) {
		boolean flag = false;
		if (dbCal.get(Calendar.YEAR) == txnCal.get(Calendar.YEAR)
				&& dbCal.get(Calendar.DAY_OF_YEAR) == txnCal.get(Calendar.DAY_OF_YEAR)) {
			flag = true;
		}
		return flag;
	}

	public static boolean isSameWeek(Timestamp dbTimestamp) {
		Calendar dbCal = Calendar.getInstance();
		dbCal.setTimeInMillis(dbTimestamp.getTime());
		Calendar txnCal = Calendar.getInstance();
		return checkSameWeek(dbCal, txnCal);
	}

	public static boolean checkSameWeek(Calendar dbCal, Calendar txnCal) {
		boolean flag = false;
		if (dbCal.get(Calendar.YEAR) == txnCal.get(Calendar.YEAR)
				&& dbCal.get(Calendar.WEEK_OF_YEAR) == txnCal.get(Calendar.WEEK_OF_YEAR)) {
			flag = true;
		}
		return flag;
	}

	public static boolean isSameMonth(Timestamp dbTimestamp) {
		Calendar dbCal = Calendar.getInstance();
		dbCal.setTimeInMillis(dbTimestamp.getTime());
		Calendar txnCal = Calendar.getInstance();
		return checkSameMonth(dbCal, txnCal);
	}

	public static boolean checkSameMonth(Calendar dbCal, Calendar txnCal) {
		boolean flag = false;
		if (dbCal.get(Calendar.YEAR) == txnCal.get(Calendar.YEAR)
				&& dbCal.get(Calendar.MONTH) == txnCal.get(Calendar.MONTH)) {
			flag = true;
		}
		return flag;
	}

	public static boolean isSameYear(Timestamp dbTimestamp) {
		Calendar dbCal = Calendar.getInstance();
		dbCal.setTimeInMillis(dbTimestamp.getTime());
		Calendar txnCal = Calendar.getInstance();
		return checkSameYear(dbCal, txnCal);
	}

	public static boolean checkSameYear(Calendar dbCal, Calendar txnCal) {
		boolean flag = false;
		if (dbCal.get(Calendar.YEAR) == txnCal.get(Calendar.YEAR)) {
			flag = true;
		}
		return flag;
	}

	static Timestamp getDBTimeStamp() {
		boolean flag = false;
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 2);
		cal.set(Calendar.MONTH, 2);
		cal.set(Calendar.YEAR, 2014);
		Timestamp t1 = new Timestamp(cal.getTimeInMillis());
		return t1;
	}

	static Timestamp getDBTimeStamp1() {
		boolean flag = false;
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, 2);
		Timestamp t1 = new Timestamp(cal.getTimeInMillis());
		return t1;
	}

	public static String convertYYToYYYY(String year) {
		Date actualDate = null;
		SimpleDateFormat yy = new SimpleDateFormat("yy");
		SimpleDateFormat yyyy = new SimpleDateFormat("yyyy");
		try {
			actualDate = yy.parse(year);
		} catch (ParseException pe) {
			// System.out.println(pe.toString());
		}
		return yyyy.format(actualDate);
	}

	static int getCycleCount(int cycleType, int countOfCycle) {
		return 0;
	}

	public static void main(String[] args) {
		System.out.println(getDE037(getDE007()));
	}

	public static boolean isValidP7(String p7) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setLenient(false);
			cal.set(Calendar.MONTH, Integer.parseInt(p7.substring(0, 2)) - 1);
			cal.set(Calendar.DATE, Integer.parseInt(p7.substring(2, 4)));
			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(p7.substring(4, 6)));
			cal.set(Calendar.MINUTE, Integer.parseInt(p7.substring(6, 8)));
			cal.set(Calendar.SECOND, Integer.parseInt(p7.substring(8, 10)));

			cal.getTime();

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}
		return false;
	}

	static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMddyyyy");

	public static Date getEffectiveDate(Date localTransactionDate) {
		String localDateString = simpleDateFormat.format(localTransactionDate);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(localDateString.substring(4)));
		cal.set(Calendar.MONTH, Integer.parseInt(localDateString.substring(0, 2)) - 1);
		cal.set(Calendar.DATE, Integer.parseInt(localDateString.substring(2, 4)));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	static SimpleDateFormat simpleDateFormatP7 = new SimpleDateFormat("MMddHHmmss");

	static SimpleDateFormat simpleDateFormatP11 = new SimpleDateFormat("hhmmss");

	static SimpleDateFormat simpleDateFormatP37 = new SimpleDateFormat("yyDDD");

	public static String getDE007() {
		simpleDateFormatP7.setTimeZone(TimeZone.getTimeZone("GMT"));
		return simpleDateFormatP7.format(new Date());
	}

	public static String getDE011() {
		simpleDateFormatP7.setTimeZone(TimeZone.getTimeZone("GMT"));
		return simpleDateFormatP11.format(new Date());
	}

	public static String getDE037(String p7) {
		simpleDateFormatP7.setTimeZone(TimeZone.getTimeZone("GMT"));
		return simpleDateFormatP37.format(new Date()).substring(1) + p7.substring(2);
	}
}
