package core.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import core.utilities.exceptions.AutomationException;

/**
 * The Randomize class contains general randomization functions
 */
public class Randomize {
	private static final String DATE_FORMAT = "MMddHHmmss";

	/**
	 * Generates String containing unique random date and time-based value. For
	 * example, if the current date and time is 10/06/2006 11:06:52am. This
	 * method will return a string equal to "1006110652".
	 * <p>
	 *
	 * @return a String containing a date-based unique value
	 */
	public static String genDateBasedRandVal() {
		return genDateBasedonFormat(DATE_FORMAT);
	}

	/**
	 * Generates String containing unique random date-based value of a specified
	 * number of characters.
	 * <p>
	 *
	 * @param length
	 *            number of characters to limit output to
	 * @return a String containing a date-based unique value
	 */
	public static String genDateBasedRandVal(int length) {
		if (length <= 0 || length > DATE_FORMAT.length()) {
			throw new AutomationException(String.format(
					"Length parameter must be positive, non-zero and less or equal to \"%s\" string length",
					DATE_FORMAT));
		}

		// generates String containing unique random date-based value
		String s = genDateBasedRandVal();

		return s.substring(s.length() - length);
	}

	/**
	 * Generates String containing unique date-based on specified format.
	 * <p>
	 *
	 * @param format
	 *            i.e. M/d/y or MM/dd/yyyy or dd/MM/yyyy or any valid
	 *            combination(specify Date and Year in lowercase)
	 * @return a String containing a date-based on specified format
	 */
	public static String genDateBasedonFormat(String format) {
		return new SimpleDateFormat(format).format(new Date());
	}

	/**
	 * Generates a random alphanumeric string of the size specified excluding
	 * any characters in the exclude string.
	 *
	 * @param length
	 *            The length of the string that will be returned
	 * @param exclude
	 *            A case-sensitive String of characters to exclude eg("Tony")
	 * @return A random String of size length excluding any specified chars
	 * @throws AutomationException
	 *             if length parameter is negative or zero
	 */
	public static String genAlphaNumericExcluding(int length, String exclude) {
		if (length <= 0) {
			throw new AutomationException("Length parameter must be positive");
		}

		StringBuffer sb = new StringBuffer();
		boolean add = true;

		for (int n = 0; n < length; n++) {
			String next = charset[(int) (Math.random() * charset.length)];
			if (exclude.length() > 0) {

				for (int j = 0; j < exclude.length() && add; j++) {
					if (next.equals(exclude.substring(j, j + 1))) {
						add = false;
						n--;
					} else {
						add = true;
					}

				}
				if (add) {
					sb = sb.append(next);
				}
				add = true;
			} else {
				sb = sb.append(next);
			}
		}

		return sb.toString();
	}

	/**
	 * Generates a random alphanumeric string of some length between the min and
	 * max lengths specified.
	 *
	 * @param minimumLength
	 *            The minimum length of the string that will be returned
	 * @param maximumLength
	 *            The maximum length of the string that will be returned
	 * @return A random String of length no less than min and no more than max
	 * @throws AutomationException
	 *             if max or min length are negative or zero
	 */
	public static String genAlphaNumeric(int minimumLength, int maximumLength) {
		if (minimumLength <= 0 || maximumLength <= 0) {
			throw new AutomationException("Max and min lengths must be positive and non-zero");
		}

		int rndSize = generateRandomInt(minimumLength, maximumLength);

		return genAlphaNumericExcluding(rndSize, "");
	}

	/**
	 * Generates a random alpha string of some length between the min and max
	 * lengths specified.
	 *
	 * @param minimumLength
	 *            The minimum length of the string that will be returned
	 * @param maximumLength
	 *            The maximum length of the string that will be returned
	 * @return String A random String of length no less than min and no more
	 *         than max
	 * @throws AutomationException
	 *             if max or min length are negative or zero
	 */
	public static String genAlpha(int minimumLength, int maximumLength) {
		if (minimumLength <= 0 || maximumLength <= 0) {
			throw new AutomationException("Max and min lengths must be positive and non-zero");
		}

		int rndSize = generateRandomInt(minimumLength, maximumLength);

		return genAlphaNumericExcluding(rndSize, "1234567890");
	}

	/**
	 * Generates a random alphanumeric string of some length between the min and
	 * max lengths specified excluding any characters in the exclude string.
	 *
	 * @param minimumLength
	 *            The minimum length of the string that will be returned
	 * @param maximumLength
	 *            The maximum length of the string that will be returned
	 * @param exclude
	 *            A case-sensitive String of characters to exclude
	 *            eg("Johnjones")
	 * @return A random String of length no less than min and no more than max
	 *         excluding any specified chars
	 * @throws AutomationException
	 *             if max or min length are negative or zero
	 */
	public static String genAlphaNumericExcluding(int minimumLength, int maximumLength, String exclude) {
		if (minimumLength <= 0 || maximumLength <= 0) {
			throw new AutomationException("Max and min lengths must be positive and non-zero");
		}

		int rndSize = generateRandomInt(minimumLength, maximumLength);

		return genAlphaNumericExcluding(rndSize, exclude);
	}

	/**
	 * Generates a random alpha string of some length between the min and max
	 * lengths specified excluding any characters in the exclude string.
	 *
	 * @param minimumLength
	 *            The minimum length of the string that will be returned
	 * @param maximumLength
	 *            The maximum length of the string that will be returned
	 * @param exclude
	 *            A case-sensitive String of characters to exclude
	 *            eg("Johnjones")
	 * @return A random String of length no less than min and no more than max
	 *         excluding any specified chars
	 * @throws AutomationException
	 *             if max or min length are negative or zero
	 */
	public static String genAlphaExcluding(int minimumLength, int maximumLength, String exclude) {
		if (minimumLength <= 0 || maximumLength <= 0) {
			throw new AutomationException("Max and min lengths must be positive and non-zero");
		}

		int rndSize = generateRandomInt(minimumLength, maximumLength);

		return genAlphaNumericExcluding(rndSize, exclude.concat("1234567890"));
	}

	/**
	 * Generates a random numeric string of some length between the min and max
	 * lengths specified excluding any characters in the exclude string.
	 *
	 * @param minimumLength
	 *            The minimum length of the string that will be returned
	 * @param maximumLength
	 *            The maximum length of the string that will be returned
	 * @return A random String of length no less than min and no more than max
	 *         excluding any specified chars
	 * @throws AutomationException
	 *             if max or min length are negative or zero
	 */
	public static String genNumeric(int minimumLength, int maximumLength) {
		if (minimumLength <= 0 || maximumLength <= 0) {
			throw new AutomationException("Max and min lengths must be positive and non-zero");
		}

		int rndSize = generateRandomInt(minimumLength, maximumLength);

		return genAlphaNumericExcluding(rndSize, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
	}

	/**
	 * Generates a random numeric string of some length between the min and max
	 * lengths specified excluding any characters in the exclude string.
	 *
	 * @param minimumLength
	 *            The minimum length of the string that will be returned
	 * @param maximumLength
	 *            The maximum length of the string that will be returned
	 * @param exclude
	 *            A case-sensitive String of characters to exclude
	 *            eg("Johnjones")
	 * @return A random String of length no less than min and no more than max
	 *         excluding any specified chars
	 * @throws AutomationException
	 *             if max or min length are negative or zero
	 */
	public static String genNumericExcluding(int minimumLength, int maximumLength, String exclude) {
		if (minimumLength <= 0 || maximumLength <= 0) {
			throw new AutomationException("Max and min lengths must be positive and non-zero");
		}

		int rndSize = generateRandomInt(minimumLength, maximumLength);

		return genAlphaNumericExcluding(rndSize,
				exclude.concat("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
	}

	/**
	 * Generates a random integer between 0 and maximumNumber.
	 *
	 * @param maximumNumber
	 *            The highest number that will be returned, must be positive
	 * @return A random number between 0 and maximumNumber
	 * @throws AutomationException
	 *             if max value is negative or null
	 */
	public static int generateRandomInt(int maximumNumber) {
		if (maximumNumber < 0) {
			throw new AutomationException("Maximum number must be positive");
		}

		return (int) Math.round((Math.random()) * maximumNumber);
	}

	/**
	 * Generates a random integer between minimumNumber and maximumNumber.
	 *
	 * @param minimumNumber
	 *            The lowest number that will be returned
	 * @param maximumNumber
	 *            The highest number that will be returned
	 * @return A random number between minimumNumber and maximumNumber
	 */
	public static int generateRandomInt(int minimumNumber, int maximumNumber) {
		if (minimumNumber > maximumNumber) {
			throw new AutomationException("Max value must be >= min value");
		}

		return ThreadLocalRandom.current().nextInt(minimumNumber, maximumNumber + 1);
	}

	/**
	 * Generates a random integer as a String between 0 and maximumNumber.
	 *
	 * @param maximumNumber
	 *            The highest number that will be returned
	 * @return A random number between minimumNumber and maximumNumber
	 */
	public static String generateRandomIntAsString(int maximumNumber) {
		return Integer.toString(generateRandomInt(maximumNumber));
	}

	/**
	 * Generates a random integer as a String between minimumNumber and
	 * maximumNumber.
	 *
	 * @param minimumNumber
	 *            The lowest number that will be returned
	 * @param maximumNumber
	 *            The highest number that will be returned
	 * @return A random number between minimumNumber and maximumNumber
	 */
	public static String generateRandomIntAsString(int minimumNumber, int maximumNumber) {
		return Integer.toString(generateRandomInt(minimumNumber, maximumNumber));
	}

	public static void example() {
		String toExclude = "fred";
		String numExclude = "1357";
		System.out.println("genAlpha(10, 20)\t\t\t\t\t\t" + genAlpha(10, 20));
		System.out.println(
				"genAlphaExcluding(10, 20, \"" + toExclude + ")\t" + genAlphaExcluding(10, 20, toExclude) + "\n");

		System.out.println("genAlphaNumeric(10, 20)\t\t\t\t\t" + genAlphaNumeric(10, 20));
		System.out.println("genAlphaNumericExcluding(10, \"" + toExclude + ")\t"
				+ genAlphaNumericExcluding(10, 20, toExclude) + "\n");

		System.out.println("genNumeric(10, 20)\t\t\t\t\t\t" + genNumeric(10, 20));
		System.out.println(
				"genNumericExcluding(10, \"" + numExclude + ")\t\t\t" + genNumericExcluding(10, 20, numExclude) + "\n");

		System.out.println("generateRandomInt(20)\t\t\t\t\t" + generateRandomInt(20));
		System.out.println("generateRandomInt(100, 200)\t\t\t\t" + generateRandomInt(100, 200) + "\n");
	}

	/**
	 * To obtain a random string between lo and hi number of characters. String
	 * of between 10 and 40 characters, you would say: String s =
	 * Randomize.randomString(10, 40);
	 *
	 * @param lo
	 *            the minimum number of characters must be less than hi and
	 *            positive or zero
	 * @param hi
	 *            the maximum number of characters must be greater than lo and
	 *            positive and non-zero
	 * @return String the result string
	 * @throws AutomationException
	 *             if max or min values are negative or max is zero
	 */
	public static String randomString(int lo, int hi) {
		if (lo <= 0 || hi <= 0) {
			throw new AutomationException("Max and min values must be positive and non-zero");
		}

		int n = rand(lo, hi);
		char c[] = new char[n];

		for (int z = 0; z < n; z++) {
			c[z] = (char) rand('a', 'z');
		}

		return new String(c);
	}

	/**
	 * Actual random numbers are obtained using nextInt(), and then knocked down
	 * to the relevant range using the modulo ("%") operator.
	 *
	 * @param lo
	 *            min number must less than hi or equal
	 * @param hi
	 *            max number must be greater than lo or equal
	 * @return a number between lo and hi
	 * @throws AutomationException
	 *             if max value is larger than min value
	 */
	public static int rand(int lo, int hi) {
		if (lo > hi) {
			throw new AutomationException("Max value must be >= min value");
		}

		Random rd = new Random();
		int n = hi - lo + 1;
		int i = rd.nextInt() % n;

		if (i < 0) {
			i = -i;
		}

		return lo + i;
	}

	/**
	 * Array containing a list of characters used for randomization purposes.
	 */
	private static final String[] charset = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e",
			"f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
			"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"V", "W", "X", "Y", "Z" };
}
