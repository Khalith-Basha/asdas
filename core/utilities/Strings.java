package core.utilities;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contains library of generic, useful string functions
 */
public class Strings {

	/**
	 * Global String convenience variables to simplify use of characters that
	 * require escape sequencing in Java i.e. instead of having to remember the
	 * escape char sequence you can use: sDQ + "My Test" + sDQ
	 */
	public static String sDQ = "\""; // variable to simplify use of inserting
										// double quote char in strings
	public static String sSQ = "\'"; // variable to simplify use of inserting
										// single quote char in strings
	public static String sNL = "\n"; // variable to simplify use of inserting
										// next line char in strings
	public static String sTab = "\t"; // variable to simplify use of inserting a
										// tab in a text string.
	public static String sCR = "\r"; // variable to simplify use of inserting a
										// carriage return in the text at this
										// point.

	public static final String LINE_SEPARATOR = "\\r?\\n";

	/**
	 * Removes whitespace from a specified string
	 * <p>
	 *
	 * @param s
	 *            string to remove whitespace from
	 * @return string based on original specified string except without
	 *         whitespace (i.e. string "I went out" is returned as "Iwentout"
	 */

	public static String removeWhiteSpace(final String s) {
		final StringTokenizer st = new StringTokenizer(s);
		final StringBuilder str = new StringBuilder();

		for (int i = 0; i < st.countTokens(); i++) {
			str.append(st.nextToken().trim());
		}

		return str.toString();
	}

	/**
	 * Removes invalid characters from a specified string
	 * <p>
	 *
	 * @param s
	 *            string to remove invalid characters from
	 * @param invalidChars
	 *            list of invalid characters to remove from the original string
	 *            (s) i.e.
	 *            {@code "_","-","@","#","$","%","^","&","*","(",")","+","=","!"}
	 *            example:
	 *            {@code removeInvalidChars("%I$ went &out@",lsInvalidChars)}
	 *            using lsInvalidChars list
	 *            {@code "_","-","@","#","$","%","^","&","*","(",")","+","=","!"}
	 * @return string based on the originally specified string except without
	 *         specified invalid characters
	 *         {@code (i.e. string "%I$ went &out@" is returned as "I went out")}
	 */
	public static String removeInvalidChars(final String s, final String[] invalidChars) {
		String output = s;

		for (final String invalidChar : invalidChars) {
			output = replace(output, invalidChar, "");
		}

		return output;
	}

	/**
	 * Converts a specified string to a string array using default space
	 * delimeter
	 * <p>
	 *
	 * @param s
	 *            string to convert to string array
	 * @return the string array (i.e. string "one two three four" is returned as
	 *         String [] = {"one","two","three","four"}
	 */
	public static String[] stringToStringArray(final String s) {
		return stringToStringArray(s, " ");
	}

	/**
	 * Converts a specified string to a string array using specified delimiter
	 * <p>
	 *
	 * @param s
	 *            string to convert to string array
	 * @param delim
	 *            the delimiter to use to separate the data in the source string
	 * @return string array (i.e. string "one, two, three, four," is returned as
	 *         String [] = {"one","two","three","four"}
	 */

	public static String[] stringToStringArray(final String s, final String delim) {
		return s.split(delim);
	}

	/**
	 * Strips whitespace character (160) from output
	 *
	 * @param s
	 *            String to strip ascii 160 characters from
	 * @return the original string stripped of any ascii 160 characters
	 */

	public static String stripWSChar(final String s) {
		final StringBuilder sb = new StringBuilder();

		for (int x = 0; x < s.length(); x++) {
			// System.out.println("Character: " + s.charAt(x));
			final char c = s.charAt(x);
			// System.out.println(c + " ASCII Value: " + i);
			if (c != 160) {
				sb.append(c);
			}
		}

		return sb.toString();
	}

	/**
	 * Searches for text in a list of text and returns the matching lines in a
	 * String
	 * <p>
	 *
	 * @param searchContent
	 *            String array to search. Tip: use GetFileContentsAsList(String
	 *            filename) method to search entire file
	 * @param searchStr
	 *            String to search for;
	 * @return String of extracted text lines from the searched content that
	 *         match the specified search string criteria
	 */
	public static String getLines(final String[] searchContent, final String searchStr) {
		String match = "";

		// find matches
		for (final String line : searchContent) {
			if (line != null) {
				if (line.indexOf(searchStr) != -1) {
					match = match + line + Platform.gsNewline;
				}
			}
		}

		return match;
	}

	/**
	 * Searches for text in a list of text and returns the matching line as a
	 * String
	 * <p>
	 *
	 * @param searchContent
	 *            String array to search. Tip: use GetFileContentsAsList(String
	 *            filename) method to search entire file
	 * @param searchStr
	 *            String to search for;
	 * @return String of extracted text lines from the searched content that
	 *         match the specified search string criteria
	 */
	public static String getLineFromList(final List<String> searchContent, final String searchStr) {
		// find matches
		for (final String line : searchContent) {
			if (line != null) {
				if (line.indexOf(searchStr) != -1) {
					return line;
				}
			}
		}

		return "";
	}

	/**
	 * Searches for lines that match search strings and returns the matching
	 * lines
	 * <p>
	 *
	 * @param searchContent
	 *            String array to search. Tip: use GetFileContentsAsList(String
	 *            filename) method to search entire file
	 * @param searchStr
	 *            String to search for;
	 * @return String of extracted text lines from the searched content that
	 *         match the specified search string criteria
	 */
	public static List<String> getLinesFromList(final List<String> searchContent, final String searchStr) {
		final List<String> matches = new ArrayList<>();

		// find matches
		for (final String line : searchContent) {
			if (line != null) {
				if (line.indexOf(searchStr) != -1) {
					matches.add(line);
				}
			}
		}

		return matches;
	}

	/**
	 * Extracts and returns specified line item from specified block of text
	 *
	 * @param lineItem
	 *            - partial text of line item (i.e. "Script Name = ")
	 * @param content
	 *            - text block
	 * @return sLineItem - complete line (i.e. Script Name = MyTest1.java")
	 */
	public static String getLineFromString(final String lineItem, final String content) {

		final String[] results = Strings.stringToStringArray(content, Log.gsNewline);

		for (final String result : results) {
			if (result.indexOf(lineItem) != -1) {
				return result.trim();
			}
		}

		return "";
	}
	
	
	/**
	 * Extracts and returns a block of lines based on a given start line and end line of a specified block of text
	 *
	 * @param startLine partial text of line item (i.e. "Script Name = ") that is the first line of the paragraph you want to extract 
	 * @param endLine partial text of line item (i.e. "Script Elapsed Time =") that is the last line of the paragraph you want to extract
	 * @param source the original text source to analyze
	 * @return String array containing block of lines  
	 */
	public static String getLinesFromString(final String startLine, String endLine, final String source) {

		final String[] results = Strings.stringToStringArray(source, Log.gsNewline);
		String output= "";
		boolean found = false;
		
		for (final String result : results) {
			if (result.contains(startLine)) {
				 output = output + result + Strings.sNL;
				 found=true;
				 }
			if(found && !result.contains(endLine)){
				 output = output + result + Strings.sNL;
			}
			
			if(result.contains(endLine)){
				output = output + result + Strings.sNL;
				return output;
			}
		}

		return output;
	}
	

	/**
	 * Sorts data in string array. Ignores case.
	 *
	 * @param s
	 *            String Array of unsorted data
	 * @param ascOrDesc
	 *            true for Ascending or false for descending order
	 * @return String array data in sorted order
	 */
	public static String[] sortIgnoreCase(final String[] s, final boolean ascOrDesc) {
		int cnt;
		String temp;
		for (int i = 0; i < s.length; i++) {
			cnt = 0;
			for (int j = 0; j < s.length - i - 1; j++) {
				if (ascOrDesc) {
					if (s[j].compareToIgnoreCase(s[j + 1]) > 0) {
						temp = s[j];
						s[j] = s[j + 1];
						s[j + 1] = temp;
						cnt++;
					}
				} else {
					if (s[j].compareToIgnoreCase(s[j + 1]) <= 0) {
						temp = s[j];
						s[j] = s[j + 1];
						s[j + 1] = temp;
						cnt++;
					}
				}
			}
			if (cnt == 0) {
				continue;
			}
		}

		return s;
	}

	/**
	 * Sorts data in string array. Case sensitive.
	 *
	 * @param s
	 *            String Array of unsorted data
	 * @param ascOrDesc
	 *            true for Ascending or false for descending order
	 * @return String array data in sorted order
	 */
	public static String[] sort(final String[] s, final boolean ascOrDesc) {
		int cnt;
		String temp;
		for (int i = 0; i < s.length; i++) {
			cnt = 0;
			for (int j = 0; j < s.length - i - 1; j++) {
				if (ascOrDesc) {
					if (s[j].compareTo(s[j + 1]) > 0) {
						temp = s[j];
						s[j] = s[j + 1];
						s[j + 1] = temp;
						cnt++;
					}
				} else {
					if (s[j].compareTo(s[j + 1]) <= 0) {
						temp = s[j];
						s[j] = s[j + 1];
						s[j + 1] = temp;
						cnt++;
					}
				}
			}
			if (cnt == 0) {
				continue;
			}
		}
		return s;
	}

	// ---------------------------------------------------------------- replace

	/**
	 * Replaces the occurrences of a certain pattern in a string with a
	 * replacement String. This is the fastest replace function known to author.
	 *
	 * @param s
	 *            the string to be inspected
	 * @param sub
	 *            the string pattern to be replaced
	 * @param with
	 *            the string that should go where the pattern was
	 * @return the string with the replacements done
	 */
	public static String replace(final String s, final String sub, final String with) {
		if (s == null || sub == null || with == null) {
			return s;
		}
		int c = 0;
		int i = s.indexOf(sub, c);
		if (i == -1) {
			return s;
		}
		final StringBuilder buf = new StringBuilder(s.length() + with.length());
		do {
			buf.append(s.substring(c, i));
			buf.append(with);
			c = i + sub.length();
		} while ((i = s.indexOf(sub, c)) != -1);
		if (c < s.length()) {
			buf.append(s.substring(c, s.length()));
		}

		return buf.toString();
	}

	/**
	 * Character replacement in a string. All occurrencies of a character will
	 * be replaces.
	 *
	 * @param s
	 *            input string
	 * @param sub
	 *            character to replace
	 * @param with
	 *            character to replace with
	 * @return string with replaced characters
	 */
	public static String replace(final String s, final char sub, final char with) {
		if (s == null) {
			return s;
		}
		final char[] str = s.toCharArray();
		for (int i = 0; i < str.length; i++) {
			if (str[i] == sub) {
				str[i] = with;
			}
		}

		return new String(str);
	}

	/**
	 * Replaces the very first occurance of a substring with suplied string.
	 *
	 * @param s
	 *            source string
	 * @param sub
	 *            substring to replace
	 * @param with
	 *            substring to replace with
	 * @return modified source string
	 */
	public static String replaceFirst(final String s, final String sub, final String with) {
		if (s == null || sub == null || with == null) {
			return s;
		}
		final int i = s.indexOf(sub);
		if (i == -1) {
			return s;
		}

		return s.substring(0, i) + with + s.substring(i + sub.length());
	}

	/**
	 * Replaces the very first occurence of a character in a String.
	 *
	 * @param s
	 *            string
	 * @param sub
	 *            char to replace
	 * @param with
	 *            char to replace with
	 * @return modified string
	 */
	public static String replaceFirst(final String s, final char sub, final char with) {
		if (s == null) {
			return s;
		}
		final char[] str = s.toCharArray();
		for (int i = 0; i < str.length; i++) {
			if (str[i] == sub) {
				str[i] = with;
				break;
			}
		}

		return new String(str);
	}

	/**
	 * Replaces the very last occurance of a substring with suplied string.
	 *
	 * @param s
	 *            source string
	 * @param sub
	 *            substring to replace
	 * @param with
	 *            substring to replace with
	 * @return modified source string
	 */
	public static String replaceLast(final String s, final String sub, final String with) {
		if (s == null || sub == null || with == null) {
			return s;
		}
		final int i = s.lastIndexOf(sub);
		if (i == -1) {
			return s;
		}

		return s.substring(0, i) + with + s.substring(i + sub.length());
	}

	/**
	 * Replaces the very last occurrence of a character in a String.
	 *
	 * @param s
	 *            string to modify
	 * @param sub
	 *            char to replace
	 * @param with
	 *            char to replace with
	 * @return modified string
	 */
	public static String replaceLast(final String s, final char sub, final char with) {
		if (s == null) {
			return s;
		}
		final char[] str = s.toCharArray();
		for (int i = str.length - 1; i >= 0; i--) {
			if (str[i] == sub) {
				str[i] = with;
				break;
			}
		}

		return new String(str);
	}

	/**
	 * Determines if a string is empty. If string is NULL, it returns true.
	 *
	 * @param s
	 *            source string
	 * @return true if string is empty or null.
	 */
	public static boolean isEmpty(final String s) {
		return isStringEmpty(s);
	}

	/**
	 * Set the maximum length of the string. If string is longer, it will be
	 * shorten.
	 *
	 * @param s
	 *            source string
	 * @param len
	 *            max number of characters in string
	 * @return string with length no more then specified
	 */
	public static String setMaxLength(String s, final int len) {
		if (s == null) {
			return s;
		}
		if (s.length() > len) {
			s = s.substring(0, len);
		}

		return s;
	}

	/**
	 * Converts an object to a String. If object is <code>null</code> it will be
	 * not converted.
	 *
	 * @param obj
	 *            object to convert to string
	 * @return string created from the object or <code>null</code>
	 */
	public static String toString(final Object obj) {
		return (obj == null) ? null : obj.toString();
	}

	/**
	 * Converts an object to a String. If object is <code>null</code> a empty
	 * string is returned.
	 *
	 * @param obj
	 *            object to convert to string
	 * @return string created from the object
	 */
	public static String toNotNullString(final Object obj) {
		return (obj == null) ? "" : obj.toString();
	}

	// ---------------------------------------------------------------- split

	/**
	 * Splits a string in several parts (tokens) that are separated by
	 * delimeter. Delimeter is <b>always</b> surrounded by two strings! If there
	 * is no content between two delimeters, empty string will be returned for
	 * that token. Therefore, the length of the returned array will always be:
	 * #delimeters + 1.<br>
	 * <br>
	 * Method is much, much faster then regexp <code>String.split()</code>, and
	 * a bit faster then <code>StringTokenizer</code>.
	 *
	 * @param src
	 *            string to split
	 * @param delimeter
	 *            split delimeter
	 * @return array of split strings
	 */
	public static String[] split(final String src, final String delimeter) {
		if (src == null) {
			return null;
		}

		if (delimeter == null) {
			return new String[] { src };
		}

		final int maxparts = src.length() / delimeter.length() + 2; // one more
																	// for the
																	// last
		final int[] positions = new int[maxparts];
		final int dellen = delimeter.length();

		int i = 0, j = 0;
		int count = 0;
		positions[0] = -dellen;

		while ((i = src.indexOf(delimeter, j)) != -1) {
			count++;
			positions[count] = i;
			j = i + dellen;
		}
		count++;
		positions[count] = src.length();

		final String[] result = new String[count];

		for (i = 0; i < count; i++) {
			result[i] = src.substring(positions[i] + dellen, positions[i + 1]);
		}

		return result;
	}

	// ---------------------------------------------------------------- ignore
	// cases

	/**
	 * Finds first index of a substring in the given source string with ignored
	 * case.
	 *
	 * @param src
	 *            source string for examination
	 * @param subS
	 *            substring to find
	 * @return index of founded substring or -1 if substring is not found
	 * @see #indexOfIgnoreCase(String, String, int)
	 */
	public static int indexOfIgnoreCase(final String src, final String subS) {
		return indexOfIgnoreCase(src, subS, 0);
	}

	/**
	 * Finds first index of a substring in the given source string with ignored
	 * case. This seems to be the fastest way doing this, with common string
	 * length and content (of course, with no use of Boyer-Mayer type of
	 * algorithms). Other implementations are slower: getting char array first,
	 * lower casing the source string, using String.regionMatch etc.
	 *
	 * @param src
	 *            source string for examination
	 * @param subS
	 *            substring to find
	 * @param startIndex
	 *            starting index from where search begins
	 * @return index of founded substring or -1 if substring is not found
	 */
	public static int indexOfIgnoreCase(final String src, final String subS, final int startIndex) {
		final String sub = subS.toLowerCase();
		final int sublen = sub.length();

		for (int i = startIndex; i < src.length() - sublen + 1; i++) {
			int j = 0;
			while (j < sublen) {
				final char source = Character.toLowerCase(src.charAt(i + j));
				if (sub.charAt(j) != source) {
					break;
				}
				j++;
			}
			if (j == sublen) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Finds last index of a substring in the given source string with ignored
	 * case.
	 *
	 * @param s
	 *            source string
	 * @param subS
	 *            substring to find
	 * @return last index of founded substring or -1 if substring is not found
	 * @see #indexOfIgnoreCase(String, String, int)
	 * @see #lastIndexOfIgnoreCase(String, String, int)
	 */
	public static int lastIndexOfIgnoreCase(final String s, final String subS) {
		return lastIndexOfIgnoreCase(s, subS, 0);
	}

	/**
	 * Finds last index of a substring in the given source string with ignored
	 * case.
	 *
	 * @param src
	 *            source string for examination
	 * @param subS
	 *            substring to find
	 * @param startIndex
	 *            starting index from where search begins
	 * @return last index of founded substring or -1 if substring is not found
	 * @see #indexOfIgnoreCase(String, String, int)
	 */
	public static int lastIndexOfIgnoreCase(final String src, final String subS, final int startIndex) {
		final String sub = subS.toLowerCase();
		final int sublen = sub.length();

		for (int i = src.length() - sublen; i >= startIndex; i--) {
			int j = 0;
			while (j < sublen) {
				final char source = Character.toLowerCase(src.charAt(i + j));
				if (sub.charAt(j) != source) {
					break;
				}
				j++;
			}
			if (j == sublen) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Tests if this string starts with the specified prefix with ignored case.
	 *
	 * @param src
	 *            source string to test
	 * @param subS
	 *            starting substring
	 * @return <code>true</code> if the character sequence represented by the
	 *         argument is a prefix of the character sequence represented by
	 *         this string; <code>false</code> otherwise.
	 */
	public static boolean startsWithIgnoreCase(final String src, final String subS) {
		return startsWithIgnoreCase(src, subS, 0);
	}

	/**
	 * Tests if this string starts with the specified prefix with ignored case
	 * and with the specified prefix beginning a specified index.
	 *
	 * @param src
	 *            source string to test
	 * @param subS
	 *            starting substring
	 * @param startIndex
	 *            index from where to test
	 * @return <code>true</code> if the character sequence represented by the
	 *         argument is a prefix of the character sequence represented by
	 *         this string; <code>false</code> otherwise.
	 */
	public static boolean startsWithIgnoreCase(final String src, final String subS, final int startIndex) {
		final String sub = subS.toLowerCase();
		final int sublen = sub.length();

		if (startIndex + sublen > src.length()) {
			return false;
		}
		int j = 0;
		int i = startIndex;
		while (j < sublen) {
			final char source = Character.toLowerCase(src.charAt(i));
			if (sub.charAt(j) != source) {
				return false;
			}
			j++;
			i++;
		}

		return true;
	}

	/**
	 * Tests if this string ends with the specified suffix.
	 *
	 * @param src
	 *            String to test
	 * @param subS
	 *            suffix
	 * @return <code>true</code> if the character sequence represented by the
	 *         argument is a suffix of the character sequence represented by
	 *         this object; <code>false</code> otherwise.
	 */
	public static boolean endsWithIgnoreCase(final String src, final String subS) {
		final String sub = subS.toLowerCase();
		final int sublen = sub.length();
		int j = 0;
		int i = src.length() - sublen;

		if (i < 0) {
			return false;
		}

		while (j < sublen) {
			final char source = Character.toLowerCase(src.charAt(i));
			if (sub.charAt(j) != source) {
				return false;
			}
			j++;
			i++;
		}

		return true;
	}

	// ---------------------------------------------------------------- wildcard
	// match

	/**
	 * Checks whether a string matches a given wildcard pattern. Possible
	 * patterns allow to match single characters ('?') or any count of
	 * characters ('*'). Wildcard characters can be escaped (by an '\').
	 * <p>
	 * This method uses recursive matching, as in linux or windows. regexp works
	 * the same. This method is very fast, comparing to similar implementations.
	 *
	 * @param s
	 *            input string
	 * @param pattern
	 *            pattern to match
	 * @return <code>true</code> if string matches the pattern, otherwise
	 *         <code>fasle</code>
	 */
	public static boolean isMatch(final String s, final String pattern) {
		return isMatch(s, pattern, 0, 0);
	}

	/**
	 * Internal matching recursive function.
	 *
	 * @param s
	 *            source string
	 * @param pattern
	 *            text to find match
	 * @param stringStartNdx
	 *            starting index value for source string
	 * @param patternStartNdx
	 *            starting index value for pattern string
	 * @return true if match, false otherwise
	 */
	public static boolean isMatch(final String s, final String pattern, final int stringStartNdx,
			final int patternStartNdx) {
		int pNdx = patternStartNdx;
		int sNdx = stringStartNdx;
		final int pLen = pattern.length();
		final int sLen = s.length();
		boolean nextIsNotWildcard = false;

		while (true) {
			// check if end of string and/or pattern occured
			if (sNdx >= sLen) { // end of string still may have pending '*' in
								// pattern
				while (pNdx < pLen && pattern.charAt(pNdx) == '*') {
					pNdx++;
				}
				return pNdx >= pLen;
			}
			if (pNdx >= pLen) { // end pf pattern, but not end of the string
				return false;
			}
			final char p = pattern.charAt(pNdx); // pattern char

			// perform logic
			if (!nextIsNotWildcard) {

				if (p == '\\') {
					pNdx++;
					nextIsNotWildcard = true;
					continue;
				}
				if (p == '?') {
					sNdx++;
					pNdx++;
					continue;
				}
				if (p == '*') {
					char pnext = 0; // next pattern char
					if (pNdx + 1 < pLen) {
						pnext = pattern.charAt(pNdx + 1);
					}
					if (pnext == '*') { // double '*' have the same effect as
										// one '*'
						pNdx++;
						continue;
					}
					int i;
					pNdx++;

					// find recursively if there is any substring from the end
					// of the
					// line that matches the rest of the pattern !!!
					for (i = s.length(); i >= sNdx; i--) {
						if (isMatch(s, pattern, i, pNdx)) {
							return true;
						}
					}
					return false;
				}
			} else {
				nextIsNotWildcard = false;
			}

			// check if pattern char and string char are equals
			if (p != s.charAt(sNdx)) {
				return false;
			}

			// everything matches for now, continue
			sNdx++;
			pNdx++;
		}
	}

	// ---------------------------------------------------------------- count
	// substrings

	/**
	 * Count substring occurences in a source string.
	 *
	 * @param source
	 *            source string
	 * @param sub
	 *            substring to count
	 * @return number of substring occurences
	 */
	public static int count(final String source, final String sub) {
		int count = 0;
		int i = 0, j = 0;

		while (true) {
			i = source.indexOf(sub, j);
			if (i == -1) {
				break;
			}
			count++;
			j = i + sub.length();
		}

		return count;
	}

	/**
	 * Repeats a specified string a specified number of times
	 *
	 * @param s
	 *            string to repeat
	 * @param iRepeat
	 *            number of times to repeat string. repeat("*",7); would return
	 *            "*******"
	 * @return text string containing the repeated characters
	 */
	public static String repeat(final String s, final int iRepeat) {
		final StringBuilder buf = new StringBuilder();

		for (int i = 0; i < iRepeat; i++) {
			buf.append(s);
		}

		return buf.toString();
	}

	/**
	 * Returns specified string in proper case
	 *
	 * @param s
	 *            string to convert to proper case (first character is
	 *            initialized)
	 * @return specified string in proper case
	 */
	public static String toProperCase(final String s) {
		final String initChar = s.substring(0, 1); // get first char
		final String capitalizedChar = initChar.toUpperCase(); // capitalize the
																// first char

		return Strings.replaceFirst(s, initChar, capitalizedChar); // return the
																	// string in
																	// proper
																	// case
	}

	/**
	 * Returns the result of a text check on a string-- checks for strings being
	 * equal; no VP performed
	 *
	 * @param firstString
	 *            string being checked in
	 * @param secondString
	 *            pattern being checked for
	 * @return true if strings equal, false if not
	 */
	public static boolean doStringsMatch(final String firstString, final String secondString) {
		return firstString.equals(secondString);
	}

	/**
	 * Returns the result of a text check on a string- checks for pattern
	 * occurring in a string; no VP perf.
	 *
	 * @param searchString
	 *            string being checked in
	 * @param substring
	 *            pattern being checked for
	 * @return true if present, false if not
	 */
	public static boolean isSubstring(final String searchString, final String substring) {
		return Pattern.compile(substring).matcher(searchString).matches();
	}

	/**
	 * Returns the string with the space characters "fixed"-- this was coded for
	 * difficulties with weird spaces screwing up string matching
	 *
	 * @param s
	 *            string being fixed
	 * @return string with fixed space characters
	 */
	public static String fixString(final String s) {
		String t = "";

		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == 160) {
				t += (char) 32;
			} else {
				t += s.charAt(i);
			}
		}

		return t;
	}

	/**
	 * Prints the character values for 2 strings
	 *
	 * @param s1
	 *            1st string
	 * @param s2
	 *            2nd string
	 */
	public static void printCharVals(final String s1, final String s2) {
		for (int i = 0; i < s1.length(); i++) {
			System.out.print("'" + s1.charAt(i) + "' == '" + (int) s1.charAt(i) + "'");
			System.out.print(":");
			System.out.println("'" + (int) s2.charAt(i) + "' == '" + s2.charAt(i) + "'");
		}
	}

	/**
	 * Prints the content of the given String array to the console. For example:
	 * String[] args = {"One", "Two", "Three"} call to printArray(args); results
	 * in the following console output One Two Three
	 * <p>
	 *
	 * @param array
	 *            string array to print content of
	 */
	public static void printArray(final String[] array) {
		for (final String item : array) {
			Log.logScriptInfo(item);
		}
	}

	/**
	 * Searches the input array for an occurrence of the search pattern
	 *
	 * @param pattern
	 *            the string pattern to search for
	 * @param screenContents
	 *            the String array to search in
	 * @return boolean, true if found, false if not found
	 */
	public static boolean findPattern(final String pattern, final String[] screenContents) {
		final boolean found = false;
		// Regex re = new Regex(pattern);

		final Pattern p = Pattern.compile(pattern);

		// search for a pattern in the screen contents
		for (final String screenContent : screenContents) {
			if (screenContent != null) {
				if (p.matcher(screenContent).matches()) {
					// re.matches(screenContents[i]))
					return true;
				}
			}
		}
		return found;
	}

	/**
	 * Searches the input array for an occurrence of the search pattern
	 *
	 * @param pattern
	 *            the string pattern to search for
	 * @param screenContents
	 *            the String to search in
	 * @return boolean, true if found, false if not found
	 */
	public static boolean findPattern(final String pattern, final String screenContents) {
		if (pattern == null || screenContents == null) {
			return false;
		}

		return Pattern.compile(pattern).matcher(screenContents).matches();

		// return new Regex(pattern).matches(screenContents);
	}

	/**
	 * Searches the input array for an occurrence of the search pattern
	 *
	 * @param pattern
	 *            the string pattern to search for
	 * @param screenContents
	 *            the String array to search in
	 * @return index of the first occurrence of the search pattern, or -1
	 */
	public static int findPatternRow(final String pattern, final String[] screenContents) {
		final int row = -1;
		final Pattern p = Pattern.compile(pattern);
		// return p.matcher(screenContents[i]).matches();

		// Regex re = new Regex(pattern);
		// search for a pattern in the screen contents
		for (int i = 0; i < screenContents.length; ++i) {
			if (screenContents[i] != null) {
				if (p.matcher(screenContents[i]).matches()) {
					return i;
				}
			}
		}

		return row;
	}

	/**
	 * Searches the input array for an occurrence of the search pattern
	 *
	 * @param pattern
	 *            the string pattern to search for
	 * @param fromIndex
	 *            the array index from which to start the search
	 * @param screenContents
	 *            the String array to search in
	 * @return index of the first occurrence of the search pattern after the
	 *         index
	 */
	public static int findPatternRow(final String pattern, final int fromIndex, final String[] screenContents) {
		final int row = -1;
		// Regex re = new Regex(pattern);
		final Pattern p = Pattern.compile(pattern);

		// search for a pattern in the screen contents
		for (int i = fromIndex; i < screenContents.length; ++i) {
			if (screenContents[i] != null) {
				if (p.matcher(screenContents[i]).matches()) {
					return i;
				}
			}
		}

		return row;
	}

	/**
	 * Searches the input array for an occurrence of the search string
	 *
	 * @param s
	 *            the substring to search for
	 * @param screenContents
	 *            the String array to search in
	 * @return boolean, true if found, false if not found
	 */
	public static boolean findString(final String s, final String[] screenContents) {
		final boolean found = false;
		// search for a string in the screen contents
		for (final String screenContent : screenContents) {
			if (screenContent != null) {
				if (screenContent.contains(s)) {
					return true;
				}
			}
		}

		return found;
	}

	/**
	 * Searches the input array for an occurrence of the search string
	 *
	 * @param s
	 *            the substring to search for
	 * @param screenContents
	 *            the String to search in
	 * @return boolean, true if found, false if not found
	 */
	public static boolean findString(final String s, final String screenContents) {
		return (s == null || screenContents == null) ? false : screenContents.indexOf(s) != -1;
	}

	/**
	 * Searches the input array for an occurrence of the search string
	 *
	 * @param s
	 *            the substring to search for
	 * @param screenContents
	 *            the String array to search in
	 * @return int index of the first occurrence of the search string
	 */
	public static int findStringRow(final String s, final String[] screenContents) {
		final int row = -1;
		// search for a string in the screen contents
		for (int i = 0; i < screenContents.length; ++i) {
			if (screenContents[i] != null) {
				if (screenContents[i].contains(s)) {
					return i;
				}
			}
		}

		return row;
	}

	/**
	 * Searches the input array for an occurrence of the search string after the
	 * index
	 *
	 * @param s
	 *            the substring to search for
	 * @param fromIndex
	 *            the array index from which to start the search
	 * @param screenContents
	 *            the String array to search in
	 * @return int index of the first occurrence of the search string after the
	 *         index
	 */
	public static int findStringRow(final String s, final int fromIndex, final String[] screenContents) {
		final int row = -1;
		// search for a string in the screen contents
		for (int i = fromIndex; i < screenContents.length; ++i) {
			if (screenContents[i] != null) {
				if (screenContents[i].contains(s)) {
					return i;
				}
			}
		}

		return row;
	}

	/**
	 * a method that returns the position of a string sText in a array of string
	 * asText. -1 is returned if not found.
	 *
	 * @param content
	 *            List of string to search.
	 * @param searchFor
	 *            The string to search for.
	 * @return the index of the row in array asText that starts with sText.
	 *         Returns -1 if not found.
	 */
	public static int findStringRowBeginning(final String[] content, final String searchFor) {
		String fromList = "";

		int length = searchFor.length();

		for (int i = 0; i < content.length; i++) {
			if (content[i].length() >= length) {
				fromList = content[i].substring(0, length);
				if (fromList.equalsIgnoreCase(searchFor)) {
					return i;
				}
			}
		}

		return -1;
	}

	/**
	 * a method that returns the position of a string sText in a array of string
	 * asText. -1 is returned if not found.
	 * <p>
	 *
	 * @param content
	 *            List of string to search.
	 * @param searchFor
	 *            The string to search for.
	 * @param startAt
	 *            int of the row to start at
	 * @return the index of the row in asText that starts with sText. Returns -1
	 *         if not found.
	 */
	public static int findStringRowBeginningStartAt(final String[] content, final String searchFor, final int startAt) {
		String fromList = "";
		int length = searchFor.length();

		for (int i = startAt; i < content.length; i++) {
			if (content[i].length() >= length) {
				fromList = content[i].substring(0, length);
				if (fromList.equalsIgnoreCase(searchFor)) {
					return i;
				}
			}
		}

		return -1;
	}

	/**
	 * This method performs multiple replacements in the same string.
	 *
	 * @param ht
	 *            A hashtable whose keys are the strings to be searched for and
	 *            replaced, and whose values are the replacements strings.
	 * @param s
	 *            The source string to search.
	 * @return The original source string with all replacements completed.
	 */

	public static String replace(final Hashtable<?, ?> ht, final String s) {
		final Enumeration<?> enumr = ht.keys();
		String newstring = s;

		while (enumr.hasMoreElements()) {
			final String from = (String) enumr.nextElement();
			final String to = (String) ht.get(from);
			newstring = replace(newstring, from, to);
		}

		return newstring;
	}

	/**
	 * Performs multiple replacements in multiple strings.
	 *
	 * @param ht
	 *            A hashtable whose keys are the strings to be searched for and
	 *            replaced, and whose values are the replacements strings.
	 * @param s
	 *            The source strings (in an array) to search.
	 * @return The original source strings with all replacements completed.
	 */
	public static String[] replaceAll(final Hashtable<?, ?> ht, final String[] s) {
		final String[] newstring = s;

		for (int i = 0; i < s.length; i++) {
			newstring[i] = replace(ht, s[i]);
		}

		return newstring;
	}

	/**
	 * Converts a string array to a String
	 *
	 * @param s
	 *            string array to convert
	 * @return array as a String
	 */
	public static String arrayToString(final String[] s) {
		final StringBuilder out = new StringBuilder();

		if (s.length > 0) {
			out.append(s[0]);
			for (int i = 1; i < s.length; i++) {
				out.append(System.getProperty("line.separator"));
				out.append(s[i]);
			}
		}

		return out.toString();
	}

	/**
	 * Converts a multidimensional string array to a String
	 *
	 * @param arr
	 *            multidimensional string array to convert
	 * @return multidimensional array as a String
	 */
	public static String arrayToString(final String[][] arr) {
		final StringBuilder builder = new StringBuilder();

		for (final String[] s : arr) {
			builder.append(arrayToString(s));
		}

		return builder.toString();
	}

	/**
	 * Judge if specified string comprises of number characters.<br>
	 * <ul>
	 * <li>integer: 1000
	 * <li>long: 922337203685477580L
	 * <li>double: 1000d or 1000.35D
	 * <li>float: 1000f or 1000.2F
	 * <li>science number: 34E5 or 23.5684e10
	 * </ul>
	 *
	 * @param s
	 *            source string to test
	 * @return true, if given string is number string, else false
	 */
	public static boolean isNumber(final String s) {
		if (s == null || s.trim().length() == 0) {
			return false;
		}

		int pointCount = 0;
		int count = 0;

		for (int index = 0, length = s.length(); index < length; index++) {
			final char c = s.charAt(index);
			// negative
			if (c == '-') {
				if (index != 0) {
					return false;
				}
			}
			// can not put 0 at first position, if it is not 0
			if (c == '0') {
				if (index == 0 && s.length() > 1) {
					return false;
				}
			}
			// decimal fraction - can not contain more than one radix point
			if (c == '.') {
				if (pointCount == 0) {
					pointCount++;
					continue;
				}
				return false;
			}
			// E count
			if (c == 'E' || c == 'e') {
				// exponent can not be decimal fraction
				final int idx1 = s.indexOf('.');
				final int idx2 = s.indexOf(c);

				if (idx2 < idx1) {
					return false;
				}
				// E/e can not appear at start or end or more than one time
				if (index == 0 || index == length - 1) {
					return false;
				} else if (count == 0) {
					count++;
					continue;
				} else {
					return false;
				}
			}
			// postfix of float, long, and double
			if (c == 'F' || c == 'f' || c == 'D' || c == 'd') {
				return index == length - 1;
			}
			if (c == 'L' || c == 'l') {
				return index == length - 1 && pointCount == 0;
			}
			if (!isNumberChar(c)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * judge if given character is number character.
	 *
	 * @param c
	 * @return true, if given character is number character; else, false
	 */
	private static boolean isNumberChar(final char c) {
		return c >= '0' && c <= '9';
	}

	/**
	 * Returns int type value of a given string parameter if the given string is
	 * a numeric value. if the conversion fails Log.errorHandler() called
	 * <p>
	 *
	 * @param value
	 *            String number value
	 * @return int type value of string parameter value
	 */
	public static int getNumber(final String value) {
		int returnNumber = 0;
		String number = "";

		for (int j = 0; j < value.length(); j++) {
			final char sCharValue = value.charAt(j);

			if (isNumberChar(sCharValue)) {
				number = number + sCharValue;
			}
		}

		if (!number.isEmpty()) {
			returnNumber = Integer.parseInt(number);
		} else {
			Log.errorHandler("Invalid Numeric Value: " + value);
		}

		return returnNumber;
	}

	/**
	 * After replacing white space in strings, see if the target string contains
	 * the substring. This is helpful when some web applications format test
	 * with non breaking spaces.
	 *
	 * @param target
	 *            the string to search
	 * @param sub
	 *            the substring to find within the target string
	 * @return true is the target string contains the sub string
	 */
	public static boolean contains(String target, String sub) {
		target = replaceWhiteSpace(target);
		sub = replaceWhiteSpace(sub);

		return target.contains(sub);
	}

	/**
	 * Replaces the white space character (\u00a0, 160) with space character
	 *
	 * @param s
	 *            string to replace white space
	 * @return Reformatted string with whitespace character stripped from the
	 *         original string
	 */
	public static String replaceWhiteSpace(final String s) {
		final StringBuilder sb = new StringBuilder();

		for (int x = 0; x < s.length(); x++) {
			// System.out.println("Character: " + s.charAt(x));
			final char c = s.charAt(x);
			// System.out.println(c + " ASCII Value: " + i);

			if (c != 160) {
				sb.append(c);
			} else {
				sb.append(' ');
			}

		}

		return sb.toString();
	}

	/**
	 * removeDuplicates - removes duplicate line items from a string array list
	 * and returns a string array list of all unique line items
	 *
	 * @param input
	 *            - String[] array to remove duplicates from
	 * @return String[] array with duplicates removed
	 */
	public static String[] removeDuplicates(final String[] input) {
		final HashSet<String> set = new HashSet<>();
		final ArrayList<String> ret = new ArrayList<>();

		for (int i = input.length - 1; i >= 0; i--) {
			final String currString = input[i].toLowerCase();
			final char currStringArray[] = currString.toCharArray();
			Arrays.sort(currStringArray);
			final String sortedString = Arrays.toString(currStringArray);

			if (!set.contains(sortedString)) {
				ret.add(input[i]);
				set.add(sortedString);
			}
		}
		Collections.reverse(ret);

		return ret.toArray(new String[ret.size()]);
	}

	/**
	 * removes duplicate characters from a given string and returns a string of
	 * all unique characters
	 *
	 * @param text
	 *            - string to remove duplicates from
	 * @return original string with duplicates removed
	 */
	public static String removeDuplicateChars(final String text) {
		final StringBuilder out = new StringBuilder();

		for (int i = 0; i < text.length(); i++) {
			if (text.indexOf(i) == text.lastIndexOf(i)) {
				if (out.indexOf(text.substring(i, i + 1)) == -1) {
					out.append(text.substring(i, i + 1));
				}
			}
		}

		return out.toString();
	}

	/**
	 * removeElementsFromList - Removes elements from an original list within a
	 * String Array and returns the original list as a String array without
	 * elements specified in the elementsToRemove argument. For example, if the
	 * origList contains {"one","two,"three","four"} and the elementsToReove
	 * list contains {"two","three"} the result of this method will be a String
	 * array list containing {"one", "four"}
	 *
	 * @param origList
	 *            Original list to remove items from
	 * @param elementsToRemove
	 *            - items within a string array to remove from the original list
	 * @return contents of the original list without the elements specified in
	 *         the elementsToReove list
	 */
	public static String[] removeElementsFromList(final String[] origList, final String[] elementsToRemove) {
		final List<String> oList = new ArrayList<>(Arrays.asList(origList));
		final List<String> rList = new ArrayList<>(Arrays.asList(elementsToRemove));

		for (int i = 0; i < oList.size(); i++) {
			for (final String aRList : rList) {
				if (oList.get(i).contains(aRList)) {
					oList.remove(i);
				}
			}
		}

		return stringToStringArray(oList.toString(), ",");
	}

	/**
	 * Converts the string to a collection of strings using specified delimiter.
	 *
	 * @param inputString
	 *            convertible string
	 * @param delimiter
	 *            specified delimiter
	 * @return string list
	 */
	public static List<String> stringToList(final String inputString, final String delimiter) {
		return Arrays.asList(inputString.split(delimiter));
	}

	/**
	 * Converts the string to a collection of strings using line separator as
	 * delimiter.
	 *
	 * @param inputString
	 *            convertible string
	 * @return string list
	 */
	public static List<String> stringToList(final String inputString) {
		return Arrays.asList(inputString.split(LINE_SEPARATOR));
	}

	/**
	 * Interprets string as map with the "=" sign as key-value delimiter. All
	 * leading and trailing white spaces for key and value will be trimmed.
	 *
	 * @param inputString
	 *            input string
	 * @return Map object
	 */
	public static Map<String, String> stringToMap(final String inputString) {
		final Map<String, String> map = new HashMap<>();

		for (final String line : stringToList(inputString)) {
			final Entry<String, String> entry = stringToMapEntry(line);
			map.put(entry.getKey(), entry.getValue());
		}

		return map;
	}

	/**
	 * Interprets string as key-value pair with "=" sign as key-value delimiter.
	 * All leading and trailing white spaces for key and value will be trimmed.
	 *
	 * @param inputString
	 *            input string
	 * @return SimpleEntry object
	 */
	public static Entry<String, String> stringToMapEntry(final String inputString) {
		final String KEY_VALUE_DELIMITER = "=";
		final Pattern pattern = Pattern.compile("(.+?)" + KEY_VALUE_DELIMITER + "(.*)");
		final Matcher matcher = pattern.matcher(inputString);

		if (!matcher.find()) {
			throw new IllegalArgumentException(
					String.format("Line \"%s\" can not be interpreted as key-value pair with \"%s\" delimiter.",
							inputString, KEY_VALUE_DELIMITER));
		}

		final int KEY_INDEX = 1;
		final int VALUE_INDEX = 2;

		return new AbstractMap.SimpleEntry<>(matcher.group(KEY_INDEX).trim(), matcher.group(VALUE_INDEX).trim());
	}

	/**
	 * Returns true if string is not empty and false if otherwise. This method
	 * should be renamed after removing deprecated method
	 * {@link #isEmpty(String)}
	 *
	 * @param inputString
	 *            testable string
	 * @return true if string is not empty
	 */
	public static boolean isStringNotEmpty(final String inputString) {
		return !isStringEmpty(inputString);
	}

	/**
	 * Returns true if length of string = 0 or string is empty, otherwise false.
	 * This method should be renamed after removing deprecated method
	 * {@link #isEmpty(String)}
	 *
	 * @param inputString
	 *            checkable string
	 * @return true if length of string = 0 or string is empty
	 */
	public static boolean isStringEmpty(final String inputString) {
		return inputString == null || inputString.isEmpty();
	}

	/**
	 * Returns true if and only if this item of string array equals ignore case
	 * the specified line content.
	 *
	 * @param arr
	 *            source array of strings
	 * @param line
	 *            specified string
	 * @return true if item of array equals ignore case the line content, false
	 *         otherwise
	 */
	public static boolean containsIgnoreCase(final String[] arr, final String line) {
		for (final String item : arr) {
			if (item.equalsIgnoreCase(line)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns a true/false if the text is found in the source string - case
	 * insensitive
	 *
	 * @param source
	 *            the source text
	 * @param text
	 *            string to search for on the page
	 * @return true/false if the text is found
	 */
	public static boolean containsIgnoreCase(final String source, final String text) {
		try {
			if (Strings.indexOfIgnoreCase(source, text) <= -1) {
				return false;
			}

			return true;
		} catch (final Exception e) {
			return false;
		}
	}

	/**
	 * Returns a new String composed of copies of the String elements joined
	 * together with a copy of the specified delimiter.
	 *
	 * @param separator
	 *            a string that is used to separate each of the elements in the
	 *            resulting String
	 * @param data
	 *            an Array of strings that will have its elements joined
	 *            together.
	 * @return a new String composed of copies of the String elements joined
	 *         together with a copy of the specified delimiter
	 */
	public static String join(final String separator, final String[] data) {
		final StringBuilder sb = new StringBuilder();
		boolean first = true;

		for (final String item : data) {
			if (!first || (first = false)) {
				sb.append(separator);
			}
			sb.append(item);
		}
		return sb.toString();
	}
}
