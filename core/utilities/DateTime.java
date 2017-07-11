package core.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * The DateTime class contains general date and time functions.
 */
public class DateTime {

	/** Global default date format */
	private static final ThreadLocal<DateFormat> sdf = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("MM/dd/yyyy");
		}
	};

	

	/** Global long for one day in millis */
	private static final long glOneDay = 86400000;

	/**
	 * Sets clock start time
	 * <p>
	 *
	 * @return clock start time as a long
	 */
	public static long setStartTime() {
		return System.currentTimeMillis();
	}

	/**
	 * returns long data type of elapsed time
	 * <p>
	 *
	 * @param startTime
	 *            contains the starting time as a long type
	 * @return Long containing the elapsed time between the start time specified
	 *         and the end time.
	 */
	public static long getElapsedTimeLong(final long startTime) {
		return System.currentTimeMillis() - startTime;
	}

	/**
	 * Gets elapsed time from the specified start time. Returns string in a
	 * specified format.
	 * <p>
	 *
	 * @param startTime
	 *            the start time as a long
	 * @param sFormat
	 *            Time format string i.e. "HH:mm:ss:SSS"
	 * @return A string containing the time difference between the script start
	 *         time and the script elapsed time in a specified time format.
	 */
	public static String getElapsedTime(final long startTime, final String sFormat) {
		return getFormattedDateTime(System.currentTimeMillis() - startTime, sFormat);
	}

	/**
	 * Gets elapsed time from the specified start time. Returns string in
	 * "HH:mm:ss:SSS" format.
	 * <p>
	 *
	 * @param startTime
	 *            the starting time
	 * @return A string containing the time difference between the script start
	 *         time and the script elapsed time.
	 */
	public static String getElapsedTime(final long startTime) {
		// return getFormattedDateTimeForElapsedTime(System.currentTimeMillis()
		// - startTime);
		final long stopTime = System.currentTimeMillis() - startTime;
		return new SimpleDateFormat("KK:mm:ss:SSS")
				.format(new Date(stopTime - TimeZone.getDefault().getOffset(stopTime)));
	}

	/**
	 * returns formatted String version of date supplied as long
	 * <p>
	 *
	 * @param dateTime
	 *            date and time in long data type
	 * @param dateTimeFormat
	 *            The string format you would like the date and time to be
	 *            displayed as. (i.e. "HH:mm:ss:SSS")
	 * @return String containing the formatted date and time of the given long
	 *         date and time.
	 */
	public static String getFormattedDateTime(final long dateTime, final String dateTimeFormat) {
		return new SimpleDateFormat(dateTimeFormat).format(new Date(dateTime));
	}

	/**
	 * returns formatted String version of date supplied as long
	 * <p>
	 *
	 * @param elapsedTime
	 *            date and time in long data type of the elapsed time
	 * @return String containing the formatted date time of the given long
	 *         datetime elapsed.
	 */
	public static String getFormattedDateTimeForElapsedTime(final long elapsedTime) {
		final long hr = TimeUnit.MILLISECONDS.toHours(elapsedTime);
		final long min = TimeUnit.MILLISECONDS.toMinutes(elapsedTime - TimeUnit.HOURS.toMillis(hr));
		final long sec = TimeUnit.MILLISECONDS
				.toSeconds(elapsedTime - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min));
		final long ms = TimeUnit.MILLISECONDS.toMillis(elapsedTime - TimeUnit.HOURS.toMillis(hr)
				- TimeUnit.MINUTES.toMillis(min) - TimeUnit.SECONDS.toMillis(sec));

		return String.format("%02d:%02d:%02d.%03d", hr, min, sec, ms);
	}

	/**
	 * returns a long from a specified Date String
	 * <p>
	 * Helpful in making date-based calculations
	 *
	 * @param date
	 *            date and time in String data type (i.e. "12/25/2004")
	 * @param format
	 *            The string format you would like the date and time to be
	 *            displayed as. (i.e. "HH:mm:ss:SSS")
	 * @return long containing the date time value of the specified date (i.e.
	 *         getLongFromFormattedDateTime( "09/21/1964", "MM/dd/yy" ) would
	 *         return the long value 1135486800000 which can then be used in
	 *         date-based calculations.
	 */
	public static long getLongFromFormattedDateTime(final String date, final String format) {
		return stringToDate(date, format).getTime();
	}

	/**
	 * returns a string of the current date in MMMM dd, yyyy format
	 *
	 * @return a string of the current date in MMMM dd, yyyy format
	 */
	public static String getCurrentDate() {
		// get current date
		final long currentDate = System.currentTimeMillis();

		// System.out.println(sCurrentDate);
		final String format = Integer.parseInt(getFormattedDateTime(currentDate, "dd")) <= 9 ? "MMMM d, yyyy"
				: "MMMM dd, yyyy";

		return getFormattedDateTime(currentDate, format);
	}

	/**
	 * returns a string of the current date in a specified format
	 * <p>
	 *
	 * @param format
	 *            The date format to use. (i.e. "MMMM dd, yyyy"=September
	 *            21,2006, "MMM yyyy" = "Sep 2006")
	 * @return String of the current date in the specified format
	 */
	public static String getCurrentDate(final String format) {
		return getFormattedDateTime(System.currentTimeMillis(), format);
	}

	/**
	 * returns a string of the current date plus one day in a specified format
	 *
	 * @param format
	 *            The date format to use. (i.e. "MMMM dd, yyyy"=September
	 *            21,2006, "MMM yyyy" = "Sep 2006")
	 * @return String of the current date plus one day in the specified format
	 */
	public static String getCurrentDatePlusOne(final String format) {
		// get current date plus one
		final long currentDate = System.currentTimeMillis() + glOneDay;
		return getFormattedDateTime(currentDate, format);
	}

	/**
	 * returns a string of the current date minus one day in a specified format
	 *
	 * @param format
	 *            The date format to use. (i.e. "MMMM dd, yyyy"=September
	 *            21,2006, "MMM yyyy" = "Sep 2006")
	 * @return String of the current date minus one day in the specified format
	 */
	public static String getCurrentDateMinusOne(final String format) {
		// get current date minus one
		final long currentDate = System.currentTimeMillis() - glOneDay;

		return getFormattedDateTime(currentDate, format);
	}

	/**
	 * Extracts part of a date value from a given Date in string format. ie.
	 * getDatePartValue("11/5/04","/",0) returns 11)
	 *
	 * @param date
	 *            Date in a String format (i.e. 11/5/04)
	 * @param delim
	 *            Date Delimeter. In above example would be "/"
	 * @param placement
	 *            integer containing placement of the date part. In above
	 *            example month iPlacement equal to 0 will return the month
	 *            (11). If iPlacement is set to 1 then it would return the day
	 *            (5) in the above example. If the iPlacement is set to 2 then
	 *            it return the year (04) in the above example.
	 * @return part of a date value from a given Date in string format as an int
	 */
	public static int getDatePartValue(final String date, final String delim, final int placement) {
		return Integer.parseInt(Strings.stringToStringArray(date, delim)[placement]);
	}

	/**
	 * Generates String containing unique random date-based value
	 * <p>
	 *
	 * @return a String containing a date-based unique value
	 */
	public static String genDateBasedRandVal() {
		return new SimpleDateFormat("MMddHHmmss").format(new Date());
	}

	/**
	 * Generates String containing unique random date-based value of a specified
	 * number of characters
	 * <p>
	 *
	 * @param i
	 *            is number of characters to limit output to
	 * @return a String containing a date-based unique value of a specified
	 *         length
	 */
	public static String genDateBasedRandVal(final int i) {
		// generates String containing unique random date-based value
		final String s = new SimpleDateFormat("MMddHHmmss").format(new Date());

		return s.substring(s.length() - i);
	}

	/**
	 * Returns string as Date type
	 * <p>
	 *
	 * @param date
	 *            Date Text
	 * @param format
	 *            format to display date in
	 * @return Date
	 */
	public static Date stringToDate(final String date, final String format) {
		Date dDate = null;

		try {
			final ParsePosition pos = new ParsePosition(0);
			final SimpleDateFormat sdf = new SimpleDateFormat(format);
			dDate = sdf.parse(date, pos);
		} catch (final Exception e) {
			return dDate;
		}

		return dDate;
	}

	/**
	 * Formats a date string as specified in sFormat
	 * <p>
	 *
	 * @param sDate
	 *            String Date Text i.e. "12/05/2006"
	 * @param sFormatIn
	 *            String format to display date in i.e "MM/dd/yyyy"
	 * @param sFormatOut
	 *            String sFormatOut format to display date in i.e "MMMM dd,
	 *            yyyy"
	 * @return String i.e. "December 5, 2006"
	 */
	/*
	 * public static String formatDateString(String sDate, String sFormatIn,
	 * String sFormatOut) { long lDate = getLongFromFormattedDateTime(sDate,
	 * sFormatIn); return getFormattedDateTime(lDate, sFormatOut); }
	 */

	/**
	 * Formats a date string as specified in sFormat
	 * <p>
	 *
	 * @param date
	 *            String Date Text i.e. "12/05/2006"
	 * @param formatIn
	 *            String format to display date in i.e "MM/dd/yyyy"
	 * @param formatOut
	 *            String sFormatOut format to display date in i.e "MMMM dd,
	 *            yyyy"
	 * @return String i.e. "December 5, 2006"
	 */
	public static String formatDateString(final String date, final String formatIn, final String formatOut) {
		// long lDate = getLongFromFormattedDateTime(sDate, sFormatIn);
		// return getFormattedDateTime(lDate, sFormatOut);

		// Changes made by Tony Venditti to incorporate simplicity and raise
		// exception - Start
		Date formatedDate = null;

		try {
			formatedDate = new SimpleDateFormat(formatIn).parse(date);
		} catch (final ParseException e) {
			e.printStackTrace();
		}

		final SimpleDateFormat newFormat = new SimpleDateFormat(formatOut);
		final String finalString = newFormat.format(formatedDate);

		return finalString;
	}

	/**
	 * Returns todays date in 2/23/05 format
	 * <p>
	 *
	 * @return String - todays date
	 */
	public static String getTodaysDate() {
		final DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT);

		return dateFormatter.format(new Date());
	}

	/**
	 * Returns date string value of todays date plus the specified days, months
	 * and years (i.e. if todays date is 2/23/05 then todayPlus(7,2,10) would
	 * return 4/30/2015)
	 * <p>
	 *
	 * @param days
	 *            number of days to increase
	 * @param months
	 *            number of months to increase
	 * @param years
	 *            number of years to increase
	 * @return String - calculated future date
	 */
	public static String todayPlus(final int days, final int months, final int years) {
		final String sdDate = getTodaysDate();

		// Create Calendar instance, and get current Date
		final Calendar cal = Calendar.getInstance();
		String newDate = "";

		try {
			boolean twoDigitYr = false;
			final int chA = sdDate.trim().indexOf("/");
			final int iMth = Integer.parseInt(sdDate.substring(0, chA));
			final int chB = sdDate.trim().indexOf("/", chA + 1);
			final int intDy = Integer.parseInt(sdDate.substring(chA + 1, chB));
			int iYr = Integer.parseInt(sdDate.substring(chB + 1, sdDate.length()));

			if (iYr < 100) {
				twoDigitYr = true;
				iYr = iYr + 2000;
			}
			// Set the calendar object with the Year/Month/Day info passed in
			cal.set(Calendar.MONTH, iMth - 1);
			cal.set(Calendar.YEAR, iYr);
			cal.set(Calendar.DAY_OF_MONTH, intDy);

			// Increment that field accordingly.
			cal.add(java.util.Calendar.YEAR, years);
			cal.add(java.util.Calendar.MONTH, months);
			cal.add(java.util.Calendar.DAY_OF_MONTH, days);

			// To return the new "Date", need to construct format
			// Have to increment the Month by 1 because it is zero based.
			if (twoDigitYr == true) {
				final String str = Integer.toString(cal.get(1));
				newDate = cal.get(Calendar.MONTH) + 1 + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + str;
			} else {
				newDate = cal.get(Calendar.MONTH) + 1 + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/"
						+ cal.get(Calendar.YEAR);
			}

		} catch (final Exception e) {

		}

		return newDate;
	}

	/**
	 * Returns the name of the day for the specified given date. If you specify
	 * "08/01/2014" as the sDate value the method will return "Friday"
	 * <p>
	 *
	 * @param date
	 *            String that contains a date in the "MM/dd/yyyy" format that
	 *            you want to get the Week Day name for
	 * @return String of the Week day name for the specified date
	 */
	public static String returnDayName(final String date) {
		String dateStrings[];

		if (!DateTime.isExpectedDtFormat(date, "MM/dd/yyyy")) {
			Log.errorHandler("Incorrect date format");
		}

		dateStrings = date.split("/");
		final Date newDate = new GregorianCalendar(Integer.parseInt(dateStrings[2]),
				Integer.parseInt(dateStrings[0]) - 1, Integer.parseInt(dateStrings[1])).getTime();

		return new SimpleDateFormat("EEEE").format(newDate);
	}

	/**
	 * Returns the name of the next business day from the current date.
	 * <p>
	 *
	 * @return String of the Week day name for the next business date from the
	 *         current date
	 */
	public static String getNextBusinessDay() {
		final String date = DateTime.getCurrentDatePlusOne("MM/dd/yyyy");

		Calendar.getInstance().setTime(DateTime.stringToDate(date, "MM/dd/yyyy"));
		if (returnDayName(date).equals("Sunday")) {
			return datePlusBusinessDay(1, 0, 0, sdf.get().format(DateTime.stringToDate(date, "MM/dd/yyyy")));
		} else if (returnDayName(date).equals("Saturday")) {
			return datePlusBusinessDay(2, 0, 0, sdf.get().format(DateTime.stringToDate(date, "MM/dd/yyyy")));
		} else {
			return sdf.get().format(DateTime.stringToDate(date, "MM/dd/yyyy"));
		}
	}

	/**
	 * Returns the next business day name as a string for the specified date in
	 * sDate plus the specified days, months and years
	 * <p>
	 *
	 * @param days
	 *            number of days to increase
	 * @param months
	 *            number of months to increase
	 * @param years
	 *            number of years to increase
	 * @param date
	 *            string specifying a date in the "MM/dd/yyyy" format
	 * @return String calculated next business day name
	 */
	public static String datePlusBusinessDay(final int days, final int months, final int years, final String date) {
		// Create Calendar instance, and get current Date
		final Calendar cal = Calendar.getInstance();
		String sNewDate = "";

		try {
			boolean twoDigitYr = false;
			final int chA = date.trim().indexOf("/");
			final int iMth = Integer.parseInt(date.substring(0, chA));
			final int chB = date.trim().indexOf("/", chA + 1);
			final int intDy = Integer.parseInt(date.substring(chA + 1, chB));
			int iYr = Integer.parseInt(date.substring(chB + 1, date.length()));

			if (iYr < 100) {
				twoDigitYr = true;
				iYr = iYr + 2000;
			}
			// Set the calendar object with the Year/Month/Day info passed in
			cal.set(Calendar.MONTH, iMth - 1);
			cal.set(Calendar.YEAR, iYr);
			cal.set(Calendar.DAY_OF_MONTH, intDy);

			// Increment that field accordingly.
			cal.add(java.util.Calendar.YEAR, years);
			cal.add(java.util.Calendar.MONTH, months);
			cal.add(java.util.Calendar.DAY_OF_MONTH, days);

			// To return the new "Date", need to construct format
			// Have to increment the Month by 1 because it is zero based.
			if (twoDigitYr == true) {
				final String str = Integer.toString(cal.get(1));
				// str = str.substring(2);
				// String y = "1";
				sNewDate = cal.get(Calendar.MONTH) + 1 + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + str;
			} else {
				sNewDate = cal.get(Calendar.MONTH) + 1 + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/"
						+ cal.get(Calendar.YEAR);
			}

			Calendar.getInstance().setTime(DateTime
					.stringToDate(sdf.get().format(DateTime.stringToDate(sNewDate, "MM/dd/yyyy")), "MM/dd/yyyy"));
			if (returnDayName(sdf.get().format(DateTime.stringToDate(sNewDate, "MM/dd/yyyy"))).equals("Sunday")) {
				return datePlusBusinessDay(1, 0, 0, sdf.get().format(DateTime.stringToDate(sNewDate, "MM/dd/yyyy")));
			} else if (returnDayName(sdf.get().format(DateTime.stringToDate(sNewDate, "MM/dd/yyyy")))
					.equals("Saturday")) {
				return datePlusBusinessDay(2, 0, 0, sdf.get().format(DateTime.stringToDate(sNewDate, "MM/dd/yyyy")));
			}
		} catch (final Exception e) {

		}

		return sdf.get().format(DateTime.stringToDate(sNewDate, "MM/dd/yyyy"));
	}

	/**
	 * Returns Time string value of current time plus the specified minutes
	 *
	 * @param minsToAdd
	 *            minutes to add as int type
	 * @return String calculated time
	 */
	public static String sGetCurrentTimePlusX(final int minsToAdd) {
		final String today = new Date().toString();
		// System.out.println(sToday);
		// Fri Jun 18 10:45:50 EDT 2004
		String time = today.substring(today.indexOf(":") - 2);
		// System.out.println(sTime);
		time = time.substring(0, time.indexOf(" "));
		// System.out.println(sTime);
		final String hour = time.substring(0, time.indexOf(":"));
		final String mins = time.substring(time.indexOf(":") + 1, time.lastIndexOf(":"));
		// System.out.println(sHour);
		// System.out.println(sMins);

		// System.out.println(iMins+3);
		// ok, now the %$#% tricky part...have to figure out what now + x
		// is...for now let's not worry about things like
		// midnight and value greater than 60 for the argument
		// in fact might want to change the args to be two args...hours to add
		// and mins to add
		String newHour;
		String newMins;
		final int iMins = Integer.parseInt(mins);

		if (iMins + minsToAdd > 59) {
			newMins = String.valueOf(iMins + minsToAdd - 60);
			newHour = String.valueOf(Integer.parseInt(hour) + 1);
		} else {
			newMins = String.valueOf(iMins + minsToAdd);
			newHour = hour;
		}
		// bsm 12/18/2007 problem when minutes was less than 10 was returning
		// time as 9:6 instead of 9:06
		if (newMins.length() == 1) {
			newMins = "0" + newMins;
		}

		return newHour + ":" + newMins;
	}

	/**
	 * Returns Time string value of current time plus the specified minutes in
	 * 12 hour format
	 *
	 * @param minsToAdd
	 *            minutes to add as int type
	 * @return String - calculated time
	 */
	public static String sGetCurrentTimePlusX12(final int minsToAdd) {
		final String today = new Date().toString();
		// System.out.println(sToday);
		// Fri Jun 18 10:45:50 EDT 2004
		String time = today.substring(today.indexOf(":") - 2);
		// System.out.println(sTime);
		time = time.substring(0, time.indexOf(" "));
		// System.out.println(sTime);
		// System.out.println(sHour);
		// System.out.println(sMins);
		String newMins;

		final int mins = Integer.parseInt(time.substring(time.indexOf(":") + 1, time.lastIndexOf(":")));
		int hours = Integer.parseInt(time.substring(0, time.indexOf(":")));

		// System.out.println(iMins+3);
		// ok, now the %$#% tricky part...have to figure out what now + x
		// is...for now let's not worry about things like
		// midnight and value greater than 60 for the argument
		// in fact might want to change the args to be two args...hours to add
		// and mins to add
		if (mins + minsToAdd > 59) {
			newMins = String.valueOf(mins + minsToAdd - 60);
			hours = hours + 1;
			// do this later
			// sNewHour=String.valueOf(iHours+1);
		} else {
			newMins = String.valueOf(mins + minsToAdd);
		}
		// bsm 12/18/2007 problem when minutes was less than 10 was returning
		// time as 9:6 instead of 9:06
		if (newMins.length() == 1) {
			newMins = "0" + newMins;
		}
		// hours greater than 12 need to set to regular time
		if (hours > 12) {
			hours = hours - 12;
		}

		return String.valueOf(hours) + ":" + newMins;
	}

	/**
	 * Checks the format of a given Date string and returns true if it matches
	 * the expected format.
	 * <p>
	 *
	 * @param dateToValidate
	 *            string representing a specific date f.ex. "05/25/2011",
	 *            "03/21/11 02:21 AM", "12/31"
	 * @param expectedFormat
	 *            string representing desired format date should be in Format
	 *            must consist of Java standard date format characters, f.ex.
	 *            "MM/dd/yyyy", "MM/dd/yy hh:mm a", "MM/dd"
	 * @return true if format of passed date matches the desired format false if
	 *         format of passed date does NOT match the desired format
	 */
	public static boolean isExpectedDtFormat(final String dateToValidate, final String expectedFormat) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat(expectedFormat);

		try {
			return dateFormat.format(dateFormat.parse(dateToValidate)).equals(dateToValidate);
		} catch (final ParseException e) {
			return false;
		}
	}

	/**
	 * Checks the format of a given Date string and logs an error if it does NOT
	 * match the expected format.
	 * <p>
	 *
	 * @param dateToValidate
	 *            string representing a specific date f.ex. "05/25/2011",
	 *            "03/21/11 02:21 AM", "12/31"
	 * @param expectedFormat
	 *            string representing desired format date should be in Format
	 *            must consist of Java standard date format characters, f.ex.
	 *            "MM/dd/yyyy", "MM/dd/yy hh:mm a", "MM/dd"
	 */
	public static void verifyExpectedDtFormat(final String dateToValidate, final String expectedFormat) {
		if (isExpectedDtFormat(dateToValidate, expectedFormat) == true) {
			Log.logScriptInfo(
					"The specified date, " + dateToValidate + ", is in the expected format: " + expectedFormat);
		} else {
			Log.errorHandler("The specified date, " + dateToValidate
					+ ", is NOT in the expected format. The expected format is: " + expectedFormat);
		}
	}

	/*
	 * Public Default Constructor to satisfy compilers need for a default
	 * constructor
	 */

	/**
	 * Constructor for: DateTime() class using system date for Todays date
	 */
	public DateTime() {
		this.calendar = new GregorianCalendar();
		setToday();
		setCurrentDate(this.today);
	}

	/*
	 * New Public Static Methods Start Here
	 */
	/**
	 * Returns Date object for a given String date value
	 * <p>
	 *
	 * @param date
	 *            Date String to convert to a date object formated as:
	 *            "MM/dd/yyyy"
	 * @return Date object
	 */
	public static Date convertStringDateToDate(final String date) {
		return new DateTime(date).getDate();
	}

	/**
	 * Converts Date Parameter value to String representation of: "MM/dd/yyyy"
	 *
	 * @param dateValue
	 *            Date Type value
	 * @return String representation of Date value as: MM/dd/yyyy
	 */
	public static String convertDateToString(final Date dateValue) {
		return new DateTime(dateValue).getDateAsString();
	}

	/**
	 * returns the Next Business Day date (Excludes Saturday and Sunday) as a
	 * Date object
	 * <p>
	 *
	 * @param date
	 *            Date to modify
	 * @return Next Business Day date (Excludes Saturday and Sunday)
	 */
	public static Date getNextBusinessDate(final Date date) {
		return new DateTime(date).advanceDate(1);
	}

	/**
	 * Advances the date to the next Business date plus the number of days to
	 * advance the date by. iAdvanceDays and will skip past Saturday and Sunday
	 * <p>
	 *
	 * @param date
	 *            Date to advance
	 * @param daysToAdvance
	 *            days to advance
	 * @return Next Business date advanced by iAdvanceDays and will skip past
	 *         Saturday and Sunday
	 */
	public static Date advanceTheDate(final Date date, final int daysToAdvance) {
		return new DateTime(date).advanceDate(daysToAdvance);
	}

	/**
	 * Returns the next business date on or after date advanced by
	 * iYearsToAdvance and will skip past Saturday and Sunday
	 * <p>
	 *
	 * @param date
	 *            Date to advance
	 * @param yearsToAdvance
	 *            years to advance
	 * @return Next Business date on or after date advanced by iYearsToAdvance
	 *         and will skip past Saturday and Sunday
	 */
	public static Date advanceTheDateByYears(final Date date, final int yearsToAdvance) {
		return new DateTime(date).advanceDateInYears(yearsToAdvance);
	}

	/**
	 * Returns the next non business date
	 * <p>
	 *
	 * @param date
	 *            Date to begin with
	 * @return Next Non-Business date
	 */
	public static Date getNextNonBusinessDate(final Date date) {
		return new DateTime(date).computeNextNonBusinessDate();
	}

	/**
	 * Returns the next non business Date As a String
	 *
	 * @param date
	 *            Date to test
	 * @return String representation of Next Non Business Day Date as:
	 *         MM/dd/yyyy
	 */
	public static String getNextNonBusinessDateAsString(final Date date) {
		return convertDateToString(getNextNonBusinessDate(date));
	}

	/**
	 * Returns the next business Date As a String
	 *
	 * @param date
	 *            Date to test
	 * @return String representation of Next Business Day Date as: MM/dd/yyyy
	 */
	public static String getNextBusinessDateAsString(final Date date) {
		return convertDateToString(getNextBusinessDate(date));
	}

	/**
	 * Returns current business date object from string date representation
	 *
	 * @param dateString
	 *            Date String to convert to a date object formated like:
	 *            "MM/dd/yyyy"
	 * @param format
	 *            Format string value to apply like: "MM/dd/yyyy"
	 * @return Current Business date object from string date representation
	 */
	public static Date convertStringDateToDate(final String dateString, final String format) {
		return new DateTime().stringDateToDate(dateString, format);
	}

	/**
	 * Returns the number days between a begin date and an end date as a long
	 * data type. Logs error if End Date supersedes Start Date Example: Start =
	 * 07/21/2014 End = 07/22/2014 getDaysBetween = 1
	 *
	 * @param startDate
	 *            Calendar Start Date value
	 * @param endDate
	 *            Calendar End Date value
	 * @return System days between Start and End dates
	 */
	public static long getDaysBetween(final Calendar startDate, final Calendar endDate) {
		return new DateTime().calculateDaysBetween(startDate, endDate);
	}

	/**
	 * Returns an invalid date
	 * <p>
	 *
	 * @param date
	 *            Current Date value
	 * @return Invalid date string of current month and year, with day value of
	 *         32
	 */
	public static String getAnInvalidDate(final Date date) {
		return new DateTime(date).createInvalidDate();
	}

	/**
	 * Returns true if System date day of the week matches nDayToMatch parameter
	 * value Note: Sunday is day 1 of the week
	 *
	 * @param dayToMatch
	 *            the corresponding number value for day of week i.e. 1 =
	 *            Sunday, 2= Monday , etc
	 * @return true if System date day of week matches nDayToMatch parameter
	 *         value
	 */
	public static boolean isCurrentDayOfWeekSame(final int dayToMatch) {
		return new DateTime().getCurrentDayOfWeek() == dayToMatch;
	}

	/**
	 * Returns true if system date is an even number, false if odd number
	 *
	 * @return true if System date value is an even number
	 */
	public static boolean isEvenDate() {
		return new DateTime().isDateEven();
	}

	/**
	 * Return prior business date on or before the date decremented by
	 * iDaysToDdecrement skipping past Saturday and Sunday.
	 *
	 * @param date
	 *            Current Date to decrement
	 * @param daysToDdecrement
	 *            int value for how many days to decrement the specified date by
	 * @return Prior Business date on or before date decremented by
	 *         iDaysToDdecrement, skipping past Saturday and Sunday.
	 */
	public static Date getDecrementedDate(final Date date, final int daysToDdecrement) {
		return new DateTime(date).decrementDate(daysToDdecrement);
	}

	/**
	 * Returns true if Current Date value represents last day of that month
	 *
	 * @param date
	 *            Current Date value
	 * @return true if Current Date value represents last day of that month
	 */
	public static boolean isLastDayOfMonth(final Date date) {
		return new DateTime(date).isLastdayOfMonth();
	}

	/**
	 * Returns true if the the Current Date represented as a String value
	 * represents the last day of that month
	 *
	 * @param date
	 *            String of Current Date value represented as a string as:
	 *            MM/dd/yyyy
	 * @return true if Current Date value represents last day of that month
	 */
	public static boolean isLastDayOfMonth(final String date) {
		return isLastDayOfMonth(convertStringDateToDate(date));
	}

	/**
	 * Returns a String value of ToDays date (System date) formated as:
	 * MM/dd/yyyy
	 *
	 * @return String value of ToDays date (System date) formated as: MM/dd/yyyy
	 */
	public static String getToDayAsString() {
		return new DateTime().toDay;
	}

	/**
	 * Returns a Date object for the current days date
	 *
	 * @return Date object of ToDays date (System date)
	 */
	public static Date getToDay() {
		return new DateTime().todaysDate();
	}

	/**
	 * Returns a String value of Current date object formated as: MM/dd/yyyy
	 *
	 * @param date
	 *            Date object containing current date
	 * @return String value of Current date object formated as: MM/dd/yyyy
	 */
	public static String getCurrentDateString(final Date date) {
		return new DateTime(date).sCurrentDate;
	}

	/**
	 * Get Current time based on given time zone
	 *
	 * @param timeFormat
	 *            (hh:mm or hh:mm:ss or hh:mm:ss.SSS)
	 * @param timeZone
	 *            (US/Eastern,US/Central,US/Pacific)
	 * @return String containing the current time in the specified format
	 */
	public static String getCurrentTime(final String timeFormat, final String timeZone) {
		// Specifying the format
		final DateFormat dateFormat = new SimpleDateFormat(timeFormat);

		// Setting the Timezone
		final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
		dateFormat.setTimeZone(cal.getTimeZone());

		// Picking the time value in the required Format
		return dateFormat.format(cal.getTime());
	}

	/**
	 * Get Today's day based on given time zone
	 *
	 * @param dayFormat
	 *            (EEEEEE - to represent the day in full and EEE - to return the
	 *            abbreviated day)
	 * @param timeZone
	 *            (US/Eastern,US/Central,US/Pacific)
	 * @return String containing the current date in the specified format
	 */
	public static String getTodaysDay(final String dayFormat, final String timeZone) {
		// Specifying the format
		final DateFormat requiredFormat = new SimpleDateFormat(dayFormat);

		// Setting the Timezone
		requiredFormat.setTimeZone(TimeZone.getTimeZone(timeZone));

		// Picking the day value in the required Format
		return requiredFormat.format(new Date());
	}

	/**
	 * Get Today's date based on given time zone
	 *
	 * @param dateFormat
	 *            (EEEEEE - to represent the day in full and EEE - to return the
	 *            abbreviated day)
	 * @param timeZone
	 *            (US/Eastern,US/Central,US/Pacific)
	 * @return String containing the current date in the specified format
	 */
	public static String getTodaysDate(final String dateFormat, final String timeZone) {
		// Specifying the format
		final DateFormat requiredFormat = new SimpleDateFormat(dateFormat);

		// Setting the Timezone
		requiredFormat.setTimeZone(TimeZone.getTimeZone(timeZone));

		// Picking the date value in the required Format
		return requiredFormat.format(new Date());
	}

	/**
	 * Returns a string containing the time after adding or subtracting specific
	 * hours from current time of a 24-hour clock for any Timezone. gets an
	 * input from User to specify a TimeFormat, Timezone and No of Hours to
	 * Add/Subtract from Current time of Timezone.
	 * <p>
	 * Example Usage of the Method:<BR>
	 * For e.g. Current EST Time = Current Time : 10:31<BR>
	 * sStartTime=addSubtractHrsInCurrentTime("hh:mm","US/Eastern", -2); result:
	 * 08:31 sEndTime=addSubtractHrsInCurrentTime("hh:mm","US/Eastern", 1);
	 * result: 11:31
	 *
	 * @param timeFormat
	 *            the time format to be returned i.e. "hh:mm", "hh:mm:ss"
	 * @param timeZone
	 *            i.e. "US/Eastern","US/Central","US/Pacific"
	 * @param hrs
	 *            number of hours to add or subtract i.e. 5 to add five hours or
	 *            -3 to subtract three hours from the current time
	 * @return String containing the time after adding or subtracting specific
	 *         hours from current time of a 24-hour clock for any Timezone.
	 */
	public static String addSubtractHrsInCurrentTime(final String timeFormat, final String timeZone, final int hrs) {
		// Specifying the format
		final DateFormat dateFormat = new SimpleDateFormat(timeFormat);

		// Setting the Timezone
		final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
		cal.add(Calendar.HOUR_OF_DAY, hrs);
		dateFormat.setTimeZone(cal.getTimeZone());

		// Picking the time value in the required Format
		return dateFormat.format(cal.getTime());
	}

	/**
	 * Returns First date of next month as string
	 *
	 * @param now
	 *            base date
	 * @return First date of next month as string
	 */
	public static String getFirstDayNextMonth(final Date now) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DATE, 1);

		return convertDateToString(cal.getTime());
	}

	/**
	 * Returns the last date of the given month as string
	 *
	 * @param now
	 *            base date
	 * @return Last date of this month as string
	 */
	public static String getLastDayThisMonth(final Date now) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

		return convertDateToString(cal.getTime());
	}

	/**
	 * Returns next Saturday date as a string based upon dNow
	 *
	 * @param now
	 *            Base date
	 * @return Next Saturday date based upon dNow
	 */
	public static String getNextSaturday(final Date now) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(now);

		cal.add(Calendar.DATE, 1);
		boolean bIsSaturday = Calendar.SATURDAY == cal.get(Calendar.DAY_OF_WEEK);

		while (!bIsSaturday) {
			cal.add(Calendar.DATE, 1);
			bIsSaturday = Calendar.SATURDAY == cal.get(Calendar.DAY_OF_WEEK);
		}
		return convertDateToString(cal.getTime());
	}

	/**
	 * Returns the next Sunday date as a string based upon dNow
	 *
	 * @param now
	 *            Base date
	 * @return Next Sunday date based upon dNow
	 */
	public static String getNextSunday(final Date now) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(now);

		cal.add(Calendar.DATE, 1);
		boolean bIsSunday = Calendar.SUNDAY == cal.get(Calendar.DAY_OF_WEEK);

		while (!bIsSunday) {
			cal.add(Calendar.DATE, 1);
			bIsSunday = Calendar.SUNDAY == cal.get(Calendar.DAY_OF_WEEK);
		}
		return convertDateToString(cal.getTime());
	}

	/**
	 * Returns last day of next month as string based upon dNow
	 *
	 * @param now
	 *            Base date
	 * @return Last day of next month based upon dNow as string
	 */
	public static String getLastDayNextMonth(final Date now) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

		return convertDateToString(cal.getTime());
	}

	/**
	 * Returns Date plus n number of days including Saturday and Sunday
	 *
	 * @param now
	 *            current date
	 * @param days
	 *            days to add to dNow
	 * @return dNow + iDays
	 */
	public static Date addCalendarDays(final Date now, final int days) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.DATE, days);

		return cal.getTime();
	}

	/**
	 * Returns Date plus n number of business days excluding Saturday and Sunday
	 *
	 * @param now
	 *            Date
	 * @param days
	 *            number of days to add to given date
	 * @return dNow + iDays excluding Saturday and Sunday
	 */
	public static Date addBusinessDays(final Date now, final int days) {
		int totalDays = 0;
		boolean isSunday;
		boolean isSaturday;

		final Calendar cal = Calendar.getInstance();
		cal.setTime(now);

		while (totalDays < days) {
			cal.add(Calendar.DATE, 1);

			isSunday = Calendar.SUNDAY == cal.get(Calendar.DAY_OF_WEEK);
			isSaturday = Calendar.SATURDAY == cal.get(Calendar.DAY_OF_WEEK);

			if (!(isSunday || isSaturday)) {
				totalDays++;
			}
		}

		return cal.getTime();
	}

	/**
	 * Returns past calendar date as string
	 *
	 * @param future
	 *            Future date
	 * @param days
	 *            Number of days to recede
	 * @return receded calendar date as string
	 */
	public static String getPriorCalendarDate(final Date future, int days) {
		if (days > 0) {
			days = days * -1;
		}

		final Calendar cal = Calendar.getInstance();
		cal.setTime(future);
		cal.add(Calendar.DATE, days);

		return convertDateToString(cal.getTime());
	}

	/**
	 * Returns Past business date as string
	 *
	 * @param date
	 *            starting date
	 * @return Past business date
	 */
	public static String getPastBusinessDate(final Date date) {
		return convertDateToString(getDecrementedDate(date, 1));
	}

	/**
	 * Returns prior business date as a string
	 *
	 * @param date
	 *            Base date
	 * @param daysToDecrement
	 *            days to decrease by
	 * @return Prior business date
	 */
	public static String getPriorBusinessDate(final Date date, int daysToDecrement) {
		if (daysToDecrement < 0) { // make positive for following function
			daysToDecrement = daysToDecrement * -1;
		}

		return convertDateToString(getDecrementedDate(date, daysToDecrement));
	}

	/**
	 * Returns prior Saturday date as a string
	 *
	 * @param now
	 *            Base date
	 * @return Prior Saturday date
	 */
	public static String getPriorSaturday(final Date now) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(now);

		cal.add(Calendar.DATE, -1);
		boolean bIsSaturday = Calendar.SATURDAY == cal.get(Calendar.DAY_OF_WEEK);

		while (!bIsSaturday) {
			cal.add(Calendar.DATE, -1);
			bIsSaturday = Calendar.SATURDAY == cal.get(Calendar.DAY_OF_WEEK);
		}

		return convertDateToString(cal.getTime());
	}

	/**
	 * Returns prior Sunday date as a string
	 *
	 * @param now
	 *            Base date
	 * @return Prior Sunday date
	 */
	public static String getPriorSunday(final Date now) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(now);

		cal.add(Calendar.DATE, -1);
		boolean bIsSunday = Calendar.SUNDAY == cal.get(Calendar.DAY_OF_WEEK);

		while (!bIsSunday) {
			cal.add(Calendar.DATE, -1);
			bIsSunday = Calendar.SUNDAY == cal.get(Calendar.DAY_OF_WEEK);
		}

		return convertDateToString(cal.getTime());
	}

	/**
	 * Returns next non-business date as a string
	 *
	 * @param now
	 *            base date
	 * @return Next non-business date
	 */
	public static String getPriorNonBusinessDate(final Date now) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(now);

		cal.add(Calendar.DATE, -1);
		boolean bIsSunday = Calendar.SUNDAY == cal.get(Calendar.DAY_OF_WEEK);
		boolean bIsSaturday = Calendar.SATURDAY == cal.get(Calendar.DAY_OF_WEEK);

		while (!(bIsSunday || bIsSaturday)) {
			cal.add(Calendar.DATE, -1);

			bIsSunday = Calendar.SUNDAY == cal.get(Calendar.DAY_OF_WEEK);
			bIsSaturday = Calendar.SATURDAY == cal.get(Calendar.DAY_OF_WEEK);
		}

		return convertDateToString(cal.getTime());
	}

	/**
	 * Date object of sDate
	 *
	 * @param date
	 *            base value as (MM/dd/yyyy)
	 * @return Date object of sDate
	 * @throws Exception
	 *             when conversion fails
	 */
	public static Date stringToDateFormat(final String date) throws Exception {
		return new SimpleDateFormat("MM/dd/yyyy").parse(date);
	}

	/*
	 * New Private Fields Start Here
	 */
	private final Calendar calendar;
	private Date currentDate;
	private Date today;
	private boolean isNonBusinessDay;
	private boolean isSunday;
	private boolean isLastDayOfMonth;

	private String toDay;
	private String sCurrentDate;

	/*
	 * New Private Methods Start Here
	 */
	/**
	 * Returns Date object from a given date String formated as: "MM/dd/yyyy".
	 * Logs error if invalid date string
	 * <p>
	 *
	 * @param dateString
	 *            date String formated as: "MM/dd/yyyy"
	 * @return Current Business date object from string date representation.
	 */
	private Date convertStringToDate(final String dateString) {
		Date dateValue = null;

		try {
			dateValue = new SimpleDateFormat("MM/dd/yyyy").parse(dateString);
		} catch (final Exception e) {
			Log.errorHandler("Error in convertStringToDate() " + e.getMessage());
		}

		return dateValue;
	}

	/**
	 * Sets bIsNonBusinessDay, bIsSunday, bIsSaturday, and bIsLastDayOfMonth
	 * fields based upon dCurrentDate field value
	 */
	private void setIsNonBusinessDate() {
		final boolean isSaturday = Calendar.SATURDAY == calendar.get(Calendar.DAY_OF_WEEK);
		this.isSunday = Calendar.SUNDAY == calendar.get(Calendar.DAY_OF_WEEK);

		if (isSaturday || this.isSunday) {
			this.isNonBusinessDay = true;
		} else {
			this.isNonBusinessDay = false;
		}

		isLastDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH) == calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Returns current date plus 1 day (if new date falls on Saturday or Sunday
	 * advances to Monday)
	 * <p>
	 *
	 * @param daysToAdvance
	 *            days to advance
	 * @return current date plus 1 day (if new date falls on Saturday or Sunday
	 *         advances to Monday)
	 */
	private Date advanceDate(final int daysToAdvance) {
		this.calendar.add(Calendar.DATE, daysToAdvance);
		this.setCurrentDate(calendar.getTime());

		if (this.isNonBusinessDay) {
			if (this.isSunday) {
				this.calendar.add(Calendar.DATE, 1);
			} else {
				this.calendar.add(Calendar.DATE, 2);
			}
			this.setCurrentDate(this.calendar.getTime());
		}

		return this.getDate();
	}

	/**
	 * Returns current date plus 1 year (if new date falls on Saturday or Sunday
	 * advances to Monday)
	 * <p>
	 *
	 * @param yearsToAdvance
	 *            number of years to advance current date
	 * @return current date plus 1 year (if new date falls on Saturday or Sunday
	 *         advances to Monday)
	 */
	private Date advanceDateInYears(final int yearsToAdvance) {
		this.calendar.add(Calendar.YEAR, yearsToAdvance);
		this.setCurrentDate(calendar.getTime());

		if (this.isNonBusinessDay) {
			if (this.isSunday) {
				this.calendar.add(Calendar.DATE, 1);
			} else {
				this.calendar.add(Calendar.DATE, 2);
			}
			this.setCurrentDate(this.calendar.getTime());
		}

		return this.getDate();
	}

	/**
	 * Returns the current day of the week
	 * <p>
	 *
	 * @return Calendar DAY_OF_WEEK value
	 */
	private int getCurrentDayOfWeek() {
		final Calendar calendar = new GregorianCalendar();
		final Date date = new Date();
		calendar.setTime(date);

		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * Returns the next business date on or before the date decremented by
	 * iDaysToDecrement
	 * <p>
	 *
	 * @param daysToDecrement
	 *            days to decrement
	 * @return Next Business date on or before the date decremented by
	 *         iDaysToDecrement
	 */
	private Date decrementDate(final int daysToDecrement) {
		this.calendar.add(Calendar.DATE, daysToDecrement * -1);
		this.setCurrentDate(calendar.getTime());

		if (this.isNonBusinessDay) {
			if (this.isSunday) {
				this.calendar.add(Calendar.DATE, 2 * -1);
			} else {
				this.calendar.add(Calendar.DATE, 1 * -1);
			}
			this.setCurrentDate(this.calendar.getTime());
		}
		return this.getDate();
	}

	/**
	 * Sets dToday, dCurrentDate, and sToday values to the System date
	 */
	private void setToday() {
		this.today = new Date();
		final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		toDay = dateFormat.format(this.today);
	}

	/**
	 * Sets internal Current date and Calendar to Date value parameter
	 * <p>
	 *
	 * @param dateValue
	 *            Current date value
	 */
	private void setCurrentDate(final Date dateValue) {
		this.currentDate = dateValue;
		this.calendar.setTime(this.currentDate);

		final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		sCurrentDate = dateFormat.format(this.currentDate);

		this.setIsNonBusinessDate();
	}

	/**
	 * Returns Current Date object
	 */
	private Date getDate() {
		return this.currentDate;
	}

	/**
	 * Returns System date object
	 * <p>
	 *
	 * @return System date object
	 */

	private Date todaysDate() {
		return this.today;
	}

	/**
	 * Returns String representation of Current Date
	 * <p>
	 *
	 * @return String representation of Current Date
	 */
	private String getDateAsString() {
		return new SimpleDateFormat("MM/dd/yyyy").format(this.currentDate);
	}

	/**
	 * Returns true if current date object value equals the Last Day Of Month
	 * <p>
	 *
	 * @return true if current date object value equals the Last Day Of Month
	 */
	private boolean isLastdayOfMonth() {
		return isLastDayOfMonth;
	}

	/**
	 * Returns true if date value is an even number
	 * <p>
	 *
	 * @return true if date value is an even number
	 */
	private boolean isDateEven() {
		return (this.calendar.get(Calendar.DAY_OF_MONTH) & 1) == 0;
	}

	/**
	 * Returns an invalid String date based on the current date and uses 32nd as
	 * day
	 * <p>
	 *
	 * @return an invalid String date based on the current date and uses 32nd as
	 *         day
	 */
	private String createInvalidDate() {
		final String date = new SimpleDateFormat("MM/dd/yyyy").format(this.currentDate);

		return date.substring(0, 3) + "32" + date.substring(5);
	}

	/**
	 * Returns the number of days between Start and End dates using Calendar
	 * date For example, Calendar calendarStart = new
	 * GregorianCalendar(2014,1,28,13,24,56); Calendar calendarEnd = new
	 * GregorianCalendar(2014,3,28,13,24,56); calculateDaysBetween(Calendar
	 * startDate, Calendar endDate) returns 59 (days)
	 * <p>
	 *
	 * @param startDate
	 *            Start Date value
	 * @param endDate
	 *            End Date value
	 * @return number of days between Start and End dates
	 */
	private long calculateDaysBetween(final Calendar startDate, final Calendar endDate) {
		final Calendar date = (Calendar) startDate.clone();
		long daysBetween = 0;

		try {
			if (date.after(endDate)) {
				throw new Exception("End date before Start date.");
			}

			while (date.before(endDate)) {
				date.add(Calendar.DAY_OF_MONTH, 1);
				daysBetween++;
			}
		} catch (final Exception e) {
			Log.errorHandler("Error in calculateDaysBetween(): " + e.getMessage());
		}

		return daysBetween;
	}

	/**
	 * Returns the current business date object from string date representation
	 * <p>
	 *
	 * @param dateString
	 *            Date String to convert to a date object formated like:
	 *            "MM/dd/yyyy"
	 * @param format
	 *            Format string value to apply like: "MM/dd/yyyy"
	 * @return Current Business date object from string date representation
	 */
	private Date stringDateToDate(final String dateString, final String format) {
		Date dateValue = null;

		try {
			dateValue = new SimpleDateFormat(format).parse(dateString);
		} catch (final Exception e) {
			Log.errorHandler("Error in convertStringToDate() " + e.getMessage());
		}

		return dateValue;
	}

	/**
	 * Returns the next non-business date on or after date advanced by
	 * iAdvanceDays
	 * <p>
	 *
	 * @return Next Non-Business date on or after date advanced by iAdvanceDays
	 */
	private Date computeNextNonBusinessDate() {
		final int daysToAdvance = 1;

		while (!this.isNonBusinessDay) {
			this.calendar.add(Calendar.DATE, daysToAdvance);
			this.setCurrentDate(this.calendar.getTime());
		}

		return this.getDate();
	}
	

	/**
	 * converts a given String based time value in the HH:mm:SS:sss format to
	 * seconds
	 *
	 * @param sTime
	 *            given string time value i.e. 05:32:55:244
	 * @return time in seconds for comparison purposes. The example time above
	 *         (05:32:55:244) would return 19925.244 seconds
	 */
	public static long convertTimetoSeconds(final String sTime) {
		// convert time1 to seconds
		final String sHours = sTime.substring(0, 2);
		final String sMinutes = sTime.substring(3, 5);
		final String sSeconds = sTime.substring(6, 8);
		final String sMilliseconds = sTime.substring(9, 12);

		final int hours = Strings.getNumber(sHours);
		final int minutes = Strings.getNumber(sMinutes);
		final int seconds = Strings.getNumber(sSeconds);
		final int milliseconds = Strings.getNumber(sMilliseconds);

		final long time = 60 * hours * 60 + minutes * 60 + seconds + milliseconds / 1000;
		return time;
	}
	
	
	/**
	 * Returns string date values based on the input parameter
	 * For example getDate("FUTURE") returns a future date, getDate("TODAY") returns todays date, 
	 * getDate("FIRST_DAY_LAST_MONTH") returns the first day of the month 
	 * @param dateValue contains a key value which will return the appropriate date based on the key value
	 * "TODAY", "FUTURE", "PAST","PAST_2_MONTHS","PAST_3_MONTHS","PAST_4_MONTHS","PAST_5_MONTHS","PAST_6_MONTHS","PAST_7_MONTHS",
	 * "PAST_8_MONTHS", "PAST_9_MONTHS","PAST_10_MONTHS", "PAST_11_MONTHS","FIRST_DAY_LAST_MONTH","FIRST_DAY_NEXT_MONTH",
	 * "FIRSTDAYOFNEXTMONTH","FIRST_DAY_THIS_MONTH","LAST_DAY_MONTH","FIRST_DAY_THIS_YEAR","LAST_DAY_LAST_MONTH","FUTURE_2_MONTHS",
	 * "FUTURE_3_MONTHS","FUTURE_4_MONTHS","FIRST_DAY_NEXT_YEAR"
 	 * @return date string in the following format "MM/dd/yyyy" containing the appropriate date based on the specified date input 
	 */
	public static String getDate(String dateValue) {
		return getDate(dateValue,"MM/dd/yyyy");
	}
	
	/**
	 * Returns string date values based on the input parameter
	 * For example getDate("FUTURE") returns a future date, getDate("TODAY") returns todays date, 
	 * getDate("FIRST_DAY_LAST_MONTH") returns the first day of the month 
	 * @param dateValue contains a key value which will return the appropriate date based on the key value
	 * "TODAY", "FUTURE", "PAST","PAST_2_MONTHS","PAST_3_MONTHS","PAST_4_MONTHS","PAST_5_MONTHS","PAST_6_MONTHS","PAST_7_MONTHS",
	 * "PAST_8_MONTHS", "PAST_9_MONTHS","PAST_10_MONTHS", "PAST_11_MONTHS","FIRST_DAY_LAST_MONTH","FIRST_DAY_NEXT_MONTH",
	 * "FIRSTDAYOFNEXTMONTH","FIRST_DAY_THIS_MONTH","LAST_DAY_MONTH","FIRST_DAY_THIS_YEAR","LAST_DAY_LAST_MONTH","FUTURE_2_MONTHS",
	 * "FUTURE_3_MONTHS","FUTURE_4_MONTHS","FIRST_DAY_NEXT_YEAR"
 	 * @param format date format to use i.e. "MM/dd/yyyy", "MMM-dd-yy", "MMMM dd, yyyy", "MMMM", "dd"
	 * @return date string containing the appropriate date based on the specified date input 
	 */
	public static String getDate(String dateValue, String format) {
		String newDate="";

		SimpleDateFormat dateFormat = new SimpleDateFormat(format);

		Calendar date = Calendar.getInstance();

		if (dateValue.endsWith("TODAY")){
			newDate=(dateFormat.format(date.getTime()));

		}else if(dateValue.endsWith("FUTURE")){
			date.add(Calendar.DATE , +40);
			newDate=(dateFormat.format(date.getTime()));
		}else if(dateValue.endsWith("PAST")){
			date.set(Calendar.DAY_OF_MONTH, 1);
			date.add(Calendar.MONTH, -1);
			newDate=(dateFormat.format(date.getTime()));
		} else if(dateValue.endsWith("PAST_2_MONTHS")){
			date.set(Calendar.DAY_OF_MONTH, 1);
			date.add(Calendar.MONTH, -2);
			newDate=(dateFormat.format(date.getTime()));
		}else if(dateValue.endsWith("PAST_3_MONTHS")){
			date.set(Calendar.DAY_OF_MONTH, 1);
			date.add(Calendar.MONTH, -3);
			newDate=(dateFormat.format(date.getTime()));
		}else if(dateValue.endsWith("PAST_4_MONTHS")){
			date.set(Calendar.DAY_OF_MONTH, 1);
			date.add(Calendar.MONTH, -4);
			newDate=(dateFormat.format(date.getTime()));
		}else if(dateValue.endsWith("PAST_5_MONTHS")){
			date.set(Calendar.DAY_OF_MONTH, 1);
			date.add(Calendar.MONTH, -5);
			newDate=(dateFormat.format(date.getTime()));
		}else if(dateValue.endsWith("PAST_6_MONTHS")){
			date.set(Calendar.DAY_OF_MONTH, 1);
			date.add(Calendar.MONTH, -6);
			newDate=(dateFormat.format(date.getTime()));
		}else if(dateValue.endsWith("PAST_7_MONTHS")){
			date.set(Calendar.DAY_OF_MONTH, 1);
			date.add(Calendar.MONTH, -7);
			newDate=(dateFormat.format(date.getTime()));
		}else if(dateValue.endsWith("PAST_8_MONTHS")){
			date.set(Calendar.DAY_OF_MONTH, 1);
			date.add(Calendar.MONTH, -8);
			newDate=(dateFormat.format(date.getTime()));
		}else if(dateValue.endsWith("PAST_9_MONTHS")){
			date.set(Calendar.DAY_OF_MONTH, 1);
			date.add(Calendar.MONTH, -9);
			newDate=(dateFormat.format(date.getTime()));
		}else if(dateValue.endsWith("PAST_10_MONTHS")){
			date.set(Calendar.DAY_OF_MONTH, 1);
			date.add(Calendar.MONTH, -10);
			newDate=(dateFormat.format(date.getTime()));
		}
		else if(dateValue.endsWith("PAST_11_MONTHS")){
			date.set(Calendar.DAY_OF_MONTH, 1);
			date.add(Calendar.MONTH, -11);
			newDate=(dateFormat.format(date.getTime()));
		} else if(dateValue.endsWith("FIRST_DAY_LAST_MONTH")){
			date.set(Calendar.DAY_OF_MONTH, 1);
			date.add(Calendar.MONTH, -1);
			newDate=(dateFormat.format(date.getTime()));
		}else if(dateValue.endsWith("FIRST_DAY_NEXT_MONTH")|| dateValue.endsWith("FIRSTDAYOFNEXTMONTH")){
			date.set(Calendar.DAY_OF_MONTH, 1);
			date.add(Calendar.MONTH, +1);
			newDate=(dateFormat.format(date.getTime()));
		}else if(dateValue.endsWith("FIRST_DAY_THIS_MONTH")){
			date.set(Calendar.DAY_OF_MONTH, 1);
			newDate=(dateFormat.format(date.getTime()));
		}else if(dateValue.endsWith("LAST_DAY_MONTH")){
			date.set(Calendar.DAY_OF_MONTH, date.getActualMaximum(Calendar.DAY_OF_MONTH));
			newDate=(dateFormat.format(date.getTime()));
		}else if(dateValue.endsWith("FIRST_DAY_THIS_YEAR")){
			date.set(Calendar.DAY_OF_YEAR, 1);
			newDate=(dateFormat.format(date.getTime()));
		}else if(dateValue.endsWith("LAST_DAY_LAST_MONTH")){
	    	date.set(Calendar.DAY_OF_MONTH, 1);
	    	date.add(Calendar.DAY_OF_MONTH, -1);
	    	newDate=(dateFormat.format(date.getTime()));
	    }

		else if(dateValue.endsWith("FUTURE_2_MONTHS")){
			date.set(Calendar.DAY_OF_MONTH, 1);
			date.add(Calendar.MONTH, +2);
			newDate=(dateFormat.format(date.getTime()));
		}


		else if(dateValue.endsWith("FUTURE_3_MONTHS")){
			date.set(Calendar.DAY_OF_MONTH, 1);
			date.add(Calendar.MONTH, +3);
			newDate=(dateFormat.format(date.getTime()));
		}else if(dateValue.endsWith("FUTURE_4_MONTHS")){
            date.set(Calendar.DAY_OF_MONTH, 1);
            date.add(Calendar.MONTH, +4);
            newDate=(dateFormat.format(date.getTime()));
        }else if(dateValue.endsWith("FIRST_DAY_NEXT_YEAR")){
        	date.set(Calendar.YEAR, date.get(Calendar.YEAR) + 1);
			date.set(Calendar.DAY_OF_YEAR, 1);
			newDate=(dateFormat.format(date.getTime()));
		}


		return newDate;
	}
	
	

	/*
	 * Private Constructors Start Here
	 */

	/**
	 * Constructor for: DateTime() using sDate parameter for Current date
	 *
	 * @param date
	 *            Date String to convert to a date object formated as:
	 *            "MM/dd/yyyy"
	 */
	private DateTime(final String date) {
		this();
		this.setCurrentDate(this.convertStringToDate(date));
	}

	/**
	 * Constructor for: DateTime() using dDate parameter for Current date
	 *
	 * @param date
	 *            Date object
	 */
	private DateTime(final Date date) {
		this();
		this.setCurrentDate(date);
	}

	/**
	 * Unit tests and example calls for all methods in the DateTime class
	 * library
	 */
	public static void dateTimeUnitTest() {
		final String slash = "\\";
		final String dq = "\"";
		final Calendar calendarStart = new GregorianCalendar(2014, 1, 28, 13, 24, 56);
		final Calendar calendarEnd = new GregorianCalendar(2014, 3, 28, 13, 24, 56);
		final long long_09042014_210PM = 1409854252651L; // long value for
															// approximate date
															// and time -
															// 09/04/2014 2:10pm

		// System.out.println(getLongFromFormattedDateTime("08/21/2011",
		// "MM/dd/yy"));

		Log.logBanner("DateTime class library Unit Test");
		Log.logScriptInfo("advanceTheDate(getToDay(), 10);   ======>  " + advanceTheDate(getToDay(), 10));
		Log.logScriptInfo("advanceTheDateByYears(getToDay(), 10);   ======>  " + advanceTheDateByYears(getToDay(), 10));
		Log.logScriptInfo("convertDateToString(getToDay());	======>  " + convertDateToString(getToDay()));
		Log.logScriptInfo("convertStringDateToDate(" + dq + "08" + slash + "06" + slash + "2014" + dq + ");  ======>  "
				+ convertStringDateToDate("08/06/2014"));
		Log.logScriptInfo("convertStringDateToDate(" + dq + "08" + slash + "06" + slash + "2014" + dq + ", " + dq + "MM"
				+ slash + "dd" + slash + "yyyy" + dq + ");     ======>  "
				+ convertStringDateToDate("08/06/2014", "MM/dd/yyyy"));
		Log.logScriptInfo("datePlusBusinessDay(10, 10, 10, " + dq + "08" + slash + "06" + slash + "2014" + dq
				+ ");     ======>  " + datePlusBusinessDay(10, 10, 10, "08/06/2014"));
		Log.logScriptInfo("formatDateString(" + dq + "08" + slash + "06" + slash + "2014" + dq + ", " + dq + "MM"
				+ slash + "dd" + slash + "yyyy" + dq + ", " + dq + "MMMM dd, yyyy" + dq + ");     ======>  "
				+ formatDateString("08/06/2014", "MM/dd/yyyy", "MMMM dd, yyyy"));
		Log.logScriptInfo("genDateBasedRandVal();     ======>  " + genDateBasedRandVal());
		Log.logScriptInfo("genDateBasedRandVal(6);     ======>  " + genDateBasedRandVal(6));
		Log.logScriptInfo("getAnInvalidDate(getToDay());     ======>  " + getAnInvalidDate(getToDay()));
		Log.logScriptInfo("getCurrentDate();     ======>  " + getCurrentDate());
		Log.logScriptInfo("getCurrentDate(" + dq + "MMM yyyy" + dq + ");     ======>  " + getCurrentDate("MMM yyyy"));
		Log.logScriptInfo("getCurrentDateMinusOne(" + dq + "MM" + slash + "dd" + slash + "yyyy" + dq
				+ ");     ======>  " + getCurrentDateMinusOne("MM/dd/yyyy"));
		Log.logScriptInfo("getCurrentDatePlusOne(" + dq + "MM" + slash + "dd" + slash + "yyyy" + dq + ");     ======>  "
				+ getCurrentDatePlusOne("MMMM dd, yyyy"));
		Log.logScriptInfo("getCurrentDateString(getToDay());     ======>  " + getCurrentDateString(getToDay()));
		Log.logScriptInfo("getCurrentTime(" + dq + "HH:mm" + dq + "," + dq + "US//Pacific" + dq + "); ======>"
				+ getCurrentTime("HH:mm", "US/Pacific"));
		Log.logScriptInfo("getDatePartValue(" + dq + "08" + slash + "06" + slash + "2014" + dq + ", " + slash
				+ ", 2);     ======>  " + getDatePartValue("08/06/2014", "/", 2));
		Log.logScriptInfo("getDaysBetween(calendarStart, calendarEnd);     ======>  "
				+ getDaysBetween(calendarStart, calendarEnd));
		Log.logScriptInfo("getDecrementedDate(getToDay(), 10);     ======>  " + getDecrementedDate(getToDay(), 10));
		Log.logScriptInfo("getElapsedTime(long_09042014_210PM);     ======>  " + getElapsedTime(long_09042014_210PM));
		Log.logScriptInfo("getElapsedTime(long_09042014_210PM, " + dq + "HH:mm:ss:SSS" + dq + ");     ======>  "
				+ getElapsedTime(long_09042014_210PM, "HH:mm:ss:SSS"));
		Log.logScriptInfo(
				"getElapsedTimeLong(long_09042014_210PM);     ======>  " + getElapsedTimeLong(long_09042014_210PM));
		Log.logScriptInfo("getFormattedDateTime(System.currentTimeMillis(), " + dq + "MMMM dd, yyyy HH:mm:ss:SSS" + dq
				+ ");     ======>  " + getFormattedDateTime(System.currentTimeMillis(), "MM/dd/yyyy HH:mm:ss:SSS"));
		Log.logScriptInfo("getLongFromFormattedDateTime(" + dq + "09" + slash + "21" + slash + "1964" + dq + ", " + dq
				+ "MM" + slash + "dd" + slash + "yy" + dq + ");     ======>  "
				+ getLongFromFormattedDateTime("09/21/1964", "MM/dd/yy"));
		Log.logScriptInfo("getNextBusinessDate(getToDay());     ======>  " + getNextBusinessDate(getToDay()));
		Log.logScriptInfo(
				"getNextBusinessDateAsString(getToDay());     ======>  " + getNextBusinessDateAsString(getToDay()));
		Log.logScriptInfo("getNextBusinessDay();     ======>  " + getNextBusinessDay());
		Log.logScriptInfo("getNextNonBusinessDate(getToDay());     ======>  " + getNextNonBusinessDate(getToDay()));
		Log.logScriptInfo("getNextNonBusinessDateAsString(getToDay());     ======>  "
				+ getNextNonBusinessDateAsString(getToDay()));
		Log.logScriptInfo("getToDay();     ======>  " + getToDay());
		Log.logScriptInfo("getTodaysDate(" + dq + "MMM dd, yyyy" + dq + "," + dq + "US//Pacific" + dq + "); ======> "
				+ getTodaysDate("MMM dd, yyyy", "US/Pacific"));
		Log.logScriptInfo("getTodaysDay(" + dq + "EEE" + dq + "," + dq + "US//Pacific" + dq + "); ======> "
				+ getTodaysDate("EEE", "US/Pacific"));
		Log.logScriptInfo("getToDayAsString();     ======>  " + getToDayAsString());
		Log.logScriptInfo("getTodaysDate();     ======>  " + getTodaysDate());
		Log.logScriptInfo("isCurrentDayOfWeekSame(6);     ======>  " + isCurrentDayOfWeekSame(6));
		Log.logScriptInfo("isEvenDate();     ======>  " + isEvenDate());
		Log.logScriptInfo("isExpectedDtFormat(" + dq + "08" + slash + "06" + slash + "2014" + dq + ", " + dq + "MM"
				+ slash + "dd" + dq + ");     ======>  " + isExpectedDtFormat("08/06/2014", "MM/dd"));
		Log.logScriptInfo("isLastDayOfMonth(getToDay());     ======>  " + isLastDayOfMonth(getToDay()));
		Log.logScriptInfo("isLastDayOfMonth(" + dq + "08" + slash + "06" + slash + "2014" + dq + ");     ======>  "
				+ isLastDayOfMonth("08/06/2014"));
		// Log.logScriptInfo("parseDate(" + sDQ + "08" + sSlash + "06" + sSlash
		// + "2014 12:30PM" +sDQ + "); ======> " +
		// parseDate("08-06-2014 12:30 PM"));
		Log.logScriptInfo("returnDayName(" + dq + "08" + slash + "06" + slash + "2014" + dq + ");     ======>  "
				+ returnDayName("08/06/2014"));
		Log.logScriptInfo("setStartTime();     ======>  " + setStartTime());
		Log.logScriptInfo("sGetCurrentTimePlusX(10);     ======>  " + sGetCurrentTimePlusX(10));
		Log.logScriptInfo("sGetCurrentTimePlusX12(10);     ======>  " + sGetCurrentTimePlusX12(10));
		// Log.logScriptInfo("sortDate(String[], boolean); ======> " +
		// sortDate(String[], boolean));
		Log.logScriptInfo("stringToDate(" + dq + "08" + slash + "06" + slash + "2014" + dq + ", " + dq + "MM" + slash
				+ "dd" + slash + "yy" + dq + ");     ======>  " + stringToDate("08/06/2014", "MM/dd/yy"));
		Log.logScriptInfo("todayPlus(10, 10, 10);     ======>  " + todayPlus(10, 10, 10));
		Log.logScriptInfo("verifyExpectedDtFormat(" + dq + "08" + slash + "06" + slash + "2014" + dq + ", " + dq + "MM"
				+ slash + "dd" + slash + "yyyy" + dq + ");   ======>  ");
		verifyExpectedDtFormat("08/06/2014", "MM/dd/yyyy");
		Log.startTestCase("Convert date format from yyyy-mm-dd to dd-MMM-yyyy");

		final String date1 = DateTime.formatDateString("06/12/2015", "dd/MM/yyyy", "dd.MMM.yyyy");
		Log.logScriptInfo("Formatted Date : " + date1.toUpperCase());

		final String date2 = DateTime.formatDateString("06122015", "ddMMyyyy", "dd-MMM-yyyy");
		Log.logScriptInfo("Formatted Date : " + date2.toUpperCase());

		final String date3 = DateTime.formatDateString("15.6.2012", "dd.M.yyyy", "dd MMMM yy");
		Log.logScriptInfo("Formatted Date : " + date3.toUpperCase());
		Log.logBanner("DateTime class library Unit Test - Completed");
	}
}
