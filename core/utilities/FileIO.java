package core.utilities;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import core.utilities.exceptions.AutomationException;
import core.utilities.exceptions.ExceptionHandler;
import core.utilities.exceptions.SetupException;

/**
 * The FileIO class contains general File IO functions.
 */
public class FileIO {

	/** Buffer size (32KB) for file manipulation methods. */
	public final static int FILE_BUFFER_SIZE = 32 * 1024;

	/**
	 * Deletes specified file or directory (if directory is specified the method
	 * will recursively delete all files and or subdirectories within the
	 * specified parent directory. (i.e. deleteFile("c:\\my test folder\\") will
	 * delete the directory "my test folder" and all files and subdirectories
	 * within that directory. Be extremely careful using this function
	 * <p>
	 *
	 * @param fileName
	 *            path and filename of file or directory to delete
	 */
	public static void deleteFile(final String fileName) {
		try {
			// Create a File object to represent the filename
			final File f = new File(fileName);

			// Make sure the file or directory exists and isn't write protected
			if (!f.exists()) {
				// errorHandler("Delete: no such file or directory: " +
				// filename);
				return;
			}

			if (!f.canWrite()) {
				// errorHandler("Delete: write protected: " + filename);
				return;
			}

			// If it is a directory, recursively delete all files in the
			// directory
			if (f.isDirectory()) {
				final String[] files = f.list();

				if (files != null && files.length > 0) {
					for (final String file : files) {
						deleteFile(f.getAbsolutePath() + File.separator + file);
					}
					// ErrorHandler("Delete: directory not empty: " + filename);
					// return;
				}
			}

			// If we passed all the tests, then attempt to delete it
			final boolean success = f.delete();

			// uncomment this to see list of deleted files
			// logScriptInfo("Deleted: " + f);

			// And throw an exception if it didn't work for some (unknown)
			// reason.
			// For example, because of a bug with Java 1.1.1 on Linux,
			// directory deletion always fails
			if (!success) {
				// errorHandler("Delete: delete failed");
				return;
			}
		} catch (final IllegalArgumentException e) {
			Log.errorHandler("Delete: delete failed");
			return;
		}
	}

	/**
	 * Creates specified directories
	 * <p>
	 *
	 * @param dirName
	 *            path and directory name to create. example -
	 *            MakeDir("c:\\First\\Second\\Third"); this will create all
	 *            three directories nested within the parent directory
	 * @return boolean true if directory was created false if it was not
	 */
	public static boolean makeDirs(final String dirName) {
		// Create a File object to represent the filename
		final File dir = new File(dirName);

		// If the directory doesn't already exists create a new directory
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				Log.errorHandler("Error creating directory: " + dirName);
				return false;
			}
		}

		return true;
	}

	/**
	 * Copies all files under source folder to destination folder. If
	 * destination folder does not exist, it will be created.
	 * <p>
	 *
	 * @param srcDir
	 *            source folder
	 * @param dstDir
	 *            destination folder
	 * @return true if success, false otherwise
	 */
	public static boolean copyDir(final String srcDir, final String dstDir) {
		return copyDir(new File(srcDir), new File(dstDir));
	}

	/**
	 * Copies all files under source folder to destination folder. If
	 * destination folder does not exist, it will be created.
	 * <p>
	 *
	 * @param srcDir
	 *            source folder
	 * @param dstDir
	 *            destination folder
	 * @return true if success, false otherwise
	 */
	public static boolean copyDir(final File srcDir, final File dstDir) {
		if (dstDir.getAbsolutePath().indexOf(srcDir.getAbsolutePath()) == 0) {
			return false;
		}
		if (srcDir.isDirectory()) {
			if (!dstDir.exists()) {
				if (!dstDir.mkdirs()) {
					Log.errorHandler(String.format("Error creating destination folder: %s", dstDir));
					return false;
				}
			}

			final String[] files = srcDir.list();
			if (files != null) {
				for (final String file : files) {
					if (copyDir(new File(srcDir, file), new File(dstDir, file)) == false) {
						return false;
					}
				}
			}
			return true;
		}
		return copyFile(srcDir, dstDir);
	}

	/**
	 * Copies one file to another without any checkings.
	 *
	 * @param fileIn
	 *            source file path and name
	 * @param fileOut
	 *            destination file path and name
	 * @return true if operation was successful, false otherwise
	 */
	public static boolean copyFile(final String fileIn, final String fileOut) {
		return copyFile(new File(fileIn), new File(fileOut), FILE_BUFFER_SIZE);
	}

	/**
	 * Copies one file to another without any checkings.
	 *
	 * @param fileIn
	 *            source file path and name
	 * @param fileOut
	 *            destination file path and name
	 * @return true if operation was successful, false otherwise
	 */
	public static boolean copyFile(final File fileIn, final File fileOut) {
		return copyFile(fileIn, fileOut, FILE_BUFFER_SIZE);
	}

	/**
	 * Copies one file to another without any checkings.
	 *
	 * @param fileIn
	 *            source file path and name
	 * @param fileOut
	 *            destination file path and name
	 * @param bufsize
	 *            buffer size
	 * @return true if operation was successful, false otherwise
	 */
	public static boolean copyFile(final String fileIn, final String fileOut, final int bufsize) {
		return copyFile(new File(fileIn), new File(fileOut), bufsize);
	}

	/**
	 * Copies one file to another without any checkings.
	 *
	 * @param fileIn
	 *            source file path and name
	 * @param fileOut
	 *            destination file path and name
	 * @param bufsize
	 *            buffer size
	 * @return true if operation was successful, false otherwise
	 */
	public static boolean copyFile(final File fileIn, final File fileOut, final int bufsize) {
		FileInputStream in = null;
		FileOutputStream out = null;
		boolean result = false;
		if (!fileIn.exists()) {
			return result;
		}
		try {
			if (!fileOut.exists()) {
				final File parent = new File(fileOut.getParent());
				if (!parent.exists()) {
					if (!parent.mkdirs()) {
						Log.errorHandler(String.format("Error creating folder: %s", parent));

						return false;
					}
				}

				if (!fileOut.createNewFile()) {
					Log.errorHandler(String.format("Error creating destination file: %s", fileOut));

					return false;
				}
			}

			in = new FileInputStream(fileIn);
			out = new FileOutputStream(fileOut);
			copyPipe(in, out, bufsize);
			result = true;
		} catch (final IOException ioex) {
		}

		finally {
			if (out != null) {
				try {
					out.close();
				} catch (final IOException ioex) {
				}
			}

			if (in != null) {
				try {
					in.close();
				} catch (final IOException ioex) {
				}
			}

		}

		return result;
	}

	/**
	 * Reads from input and writes read data to the output, until the stream
	 * ends.
	 *
	 * @param in
	 * @param out
	 * @param bufSizeHint
	 * @throws IOException
	 */
	private static void copyPipe(final InputStream in, final OutputStream out, final int bufSizeHint)
			throws IOException {
		int read = -1;
		final byte[] buf = new byte[bufSizeHint];
		while ((read = in.read(buf, 0, bufSizeHint)) >= 0) {
			out.write(buf, 0, read);
		}
		out.flush();
	}

	/**
	 * Unpacks a zip file to the target directory.
	 *
	 * @param zipFile
	 *            zip file
	 * @param destDir
	 *            destination directory
	 * @throws IOException
	 *             error
	 */
	public static void unzip(final String zipFile, final String destDir) throws IOException {
		unzip(new File(zipFile), new File(destDir));
	}

	/**
	 * Unpacks a zip file to the target directory.
	 *
	 * @param zipFile
	 *            zip file
	 * @param destDir
	 *            destination directory
	 * @throws IOException
	 *             error
	 */
	public static void unzip(final File zipFile, final File destDir) throws IOException {
		try (ZipFile zip = new ZipFile(zipFile);) {
			final Enumeration<?> en = zip.entries();
			final int bufSize = 8196;

			while (en.hasMoreElements()) {
				final ZipEntry entry = (ZipEntry) en.nextElement();
				final File file = destDir != null ? new File(destDir, entry.getName()) : new File(entry.getName());
				if (entry.isDirectory()) {
					if (!file.mkdirs()) {
						if (!file.isDirectory()) {
							throw new IOException("Error creating directory: " + file);
						}
					}
				} else {
					final File parent = file.getParentFile();
					if (parent != null && !parent.exists()) {
						if (!parent.mkdirs()) {
							if (!file.isDirectory()) {
								throw new IOException("Error creating directory: " + parent);
							}
						}
					}

					try (InputStream in = zip.getInputStream(entry);
							OutputStream out = new BufferedOutputStream(new FileOutputStream(file), bufSize);) {
						copyPipe(in, out, bufSize);
					} catch (final Exception e) {
						throw new AutomationException("Error unpacking zip file: " + zipFile, e);
					}
				}
			}
		} catch (final Exception e) {
			throw new AutomationException("Error unpacking zip file: " + zipFile, e);
		}
	}

	/**
	 * Writes information to a temporary file on disk. Use this method if you
	 * need to store key=value information to a file for later use. This can be
	 * used with the getTempVarToDisk() method.
	 * <p>
	 *
	 * @param fileName
	 *            path and file to store key=value information to
	 * @param key
	 *            the key parameter to be written to the output file
	 * @param value
	 *            the value of the key parameter to be written to the output
	 *            file
	 */
	public static void setTempVarToDisk(final String fileName, final String key, final String value) {
		try (FileOutputStream out = new FileOutputStream(fileName)) {
			final Properties settings = new Properties();

			settings.put(key, value);

			// Close out properties file
			settings.store(out, "");
		} catch (final IOException ioe) {
			Log.errorHandler("Error saving temporary variable from disk: " + fileName, ioe);
		}
	}

	/**
	 * Gets key=value information from a temporary input file on disk.
	 *
	 * @param fileName
	 *            path and file to get key=value information from
	 * @param key
	 *            the key parameter to be read-in from the input file
	 * @return the value of the key parameter
	 */
	public static String getTempVarToDisk(final String fileName, final String key) {
		return getTempVarToDisk(fileName, key, false);
	}

	/**
	 * Gets key=value information from a temporary file on disk without logging
	 * the error
	 *
	 * @param fileName
	 *            path and file to get key=value information from
	 * @param key
	 *            the key parameter to be read-in from the input file
	 * @param ignoreError
	 *            true to ignore any errors, false to raise any errors that
	 *            occur
	 * @return the value of the key parameter@param bIgnoreError
	 */
	public static String getTempVarToDisk(final String fileName, final String key, final boolean ignoreError) {
		try (FileInputStream in = new FileInputStream(fileName)) {
			final Properties settings = new Properties();
			settings.load(in);

			return settings.getProperty(key);
		} catch (final IOException ioe) {
			if (!ignoreError) {
				Log.errorHandler("Error getting temporary variable from disk: " + fileName, ioe);
			}

			return null;
		}
	}

	/**
	 * Removes specified file extension from a given file name or path name
	 *
	 * @param fileName
	 *            file name or path i.e. c:\\temp\\myfle.java
	 * @param ext
	 *            extension to be removed i.e. ".java"
	 * @return file name string without extension i.e. "c:\\temp\\myfile"
	 */
	public static String removeFileExtension(final String fileName, final String ext) {
		try {
			return Strings.replace(fileName, ext, "");
		} catch (final Exception e) {
			Log.errorHandler("Error occurred in removeFileExtension()", e);
			return "";
		}
	}

	/**
	 * Removes specified file extension from a given file name or path name
	 *
	 * @param fileName
	 *            file name or path i.e. c:\\temp\\myfle.java
	 * @return file name string without extension i.e. "c:\\temp\\myfile"
	 */
	public static String removeFileExtension(final String fileName) {
		try {
			final int i = fileName.lastIndexOf('.');
			return fileName.substring(0, i);
		} catch (final Exception e) {
			Log.errorHandler("Error occurred in removeFileExtension()", e);
			return "";
		}
	}

	/**
	 * Updates the specified file by replacing the sSearchFor string with the
	 * sReplaceWith string and re-writing the file out with updated contents
	 *
	 * @param fileName
	 *            the file to be read in and updated
	 * @param searchFor
	 *            the string to be replaced
	 * @param replaceWith
	 *            the string to be added
	 */
	public static void updateFile(final String fileName, final String searchFor, final String replaceWith) {
		updateFile(fileName, searchFor, replaceWith, null);
	}

	/**
	 * Updates the specified file by replacing the sSearchFor string with the
	 * sReplaceWith string and re-writing the file out with updated contents
	 *
	 * @param fileName
	 *            the file to be read in and updated
	 * @param searchFor
	 *            the string to be replaced
	 * @param replaceWith
	 *            the string to be added
	 * @param charset
	 *            - charset such as "UTF-8"
	 */
	public static void updateFile(final String fileName, final String searchFor, final String replaceWith,
			final String charset) {
		// display error if specified file does not exist
		final File f = new File(fileName);
		if (!f.exists()) {
			Log.errorHandler("Could not find file: " + fileName);
			return;
		}

		String out = getFileContents(fileName, charset);

		if (!out.contains(searchFor)) {
			Log.errorHandler("Could not find search string: " + searchFor + " in the file " + fileName);
			return;
		}

		// replace search string with replace string
		out = Strings.replace(out, searchFor, replaceWith);

		// write out updated file
		writeFileContents(fileName, out, charset);
	}

	/**
	 * updates the specified file by replacing the sSearchFor string with the
	 * sReplaceWith string. Use charset "UTF-8". and re-writing the file out
	 * with updated contents
	 *
	 * @param fileName
	 *            the file to be read in and updated
	 * @param searchFor
	 *            the string to be replaced
	 * @param replaceWith
	 *            the string to be added
	 */
	public static void updateFileByUTF8(final String fileName, final String searchFor, final String replaceWith) {
		updateFile(fileName, searchFor, replaceWith, "UTF-8");
	}

	/**
	 * Updates the specified property file by replacing the sSearchkey value
	 * with a sNewVal and re-writes the file with updated contents
	 *
	 * @param propFile
	 *            the property file (complete path and filename) to be read in
	 *            and updated
	 * @param searchKey
	 *            the property key to search for
	 * @param newVal
	 *            the new value for the search key property
	 */
	public static void updatePropertyFile(final String propFile, final String searchKey, final String newVal) {
		// display error if specified prop file does not exist
		final File f = new File(propFile);
		if (!f.exists()) {
			Log.errorHandler("Could not find property file: " + propFile);
			return;
		}

		String line = "";

		// cycle through property file contents line by line
		for (final String element : getFileContentsAsList(propFile)) {
			// ignore empty or null lines
			if (element == null || element.equals("")) {
				continue;
			}

			// ignore comment lines
			if (element.indexOf("#") == 0) {
				continue;
			}

			if (Pattern.compile(searchKey + "\\s*=").matcher(element).find()) {
				line = element;
				break;
			}
		}

		if (line.equals("")) {
			Log.errorHandler("Could not find property key: " + searchKey + " in property file " + propFile);
			return;
		}

		// get property file content as string
		String out = getFileContents(propFile);

		// replace original property key and value with new value
		out = Strings.replace(out, line, String.format("%s=%s", searchKey, newVal));

		// write out updated property file
		writeFileContents(propFile, out);
	}

	/**
	 * Reads specified file contents and returns file contents as a string
	 * <p>
	 *
	 * @param fileName
	 *            Path and filename of file to read
	 * @return String of specified file contents
	 */
	public static String readFile(final String fileName) {
		String file = "";
		try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
			String line;

			while ((line = in.readLine()) != null) {
				file += line + "\n";
			}
		} catch (final IOException e) {
			Log.errorHandler("Error in FileIO.readFile: ", e);
		}
		return file;
	}

	/**
	 * Reads specified file and returns file contents as a string
	 * <p>
	 *
	 * @param fileName
	 *            Path and filename of file to read
	 * @return String of specified file contents
	 */
	public static String getFileContents(final String fileName) {
		final File file = new File(fileName);

		try (FileReader in = new FileReader(file)) {
			final char c[] = new char[(int) file.length()];
			return new String(c, 0, in.read(c));
		} catch (final IOException e) {
			Log.errorHandler("Error in FileIO.getFileContents: ", e);
			return "";
		}
	}

	/**
	 * Extracts and returns specified line item from specified file
	 *
	 * @param lineItem
	 *            - partial text of line item (i.e. "Script Name = ")
	 * @param fileName
	 *            file to search for line item
	 * @return sLineItem - complete line (i.e. Script Name = MyTest1.java")
	 */
	public static String getLineFromFile(final String lineItem, final String fileName) {
		final String[] lsResults = Strings.stringToStringArray(getFileContents(fileName), Log.gsNewline);

		for (final String lsResult : lsResults) {
			if (lsResult.indexOf(lineItem) != -1) {
				return lsResult.trim();
			}
		}

		return "";
	}

	/**
	 * Returns file content as a multidimensional String Array
	 *
	 * @param fileName
	 *            Path and filename of file to read and delimiter of data within
	 *            file
	 * @param delimeter
	 *            character that separates data in the file
	 * @param rows
	 *            number of rows of data in the file
	 * @param cols
	 *            number of columns of data in the file
	 * @return String[][] returns multidimensional string array of data within
	 *         file separated by a delimiter
	 */
	public static String[][] getFileContentsAsArray(final String fileName, final String delimeter, final int rows,
			final int cols) {
		String colsArray[] = new String[cols];
		final String[][] data = new String[rows][cols];

		try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
			String line;
			for (int z = 0; z < rows; z++) {
				line = in.readLine();
				if (line != null) {
					// Get line values
					colsArray = line.split(delimeter);
					for (int j = 0; j < colsArray.length; j++) {
						// Put in Matrix
						try {
							if (!colsArray[j].isEmpty()) {
								data[z][j] = colsArray[j];
							} else {
								data[z][j] = "";
							}
						} catch (final ArrayIndexOutOfBoundsException a) {
							return data;
						}
					}
				}
			}
		} catch (final Exception e) {
			Log.errorHandler("Error in FileIO.getFileContentsAsList: ", e);
		}

		return data;
	}

	/**
	 * Returns file content as a multidimensional String Array
	 *
	 * @param fileName
	 *            Path and filename of file to read and delimiter of data within
	 *            file
	 * @param delimeter
	 *            character that separates data in the file
	 * @param rows
	 *            number of rows of data in the file
	 * @param cols
	 *            number of columns of data in the file
	 * @param returnHeader
	 *            true returns column headers and false does not return column
	 *            header info
	 * @return String[][] returns multidimensional string array of data within
	 *         file separated by a delimiter
	 */
	public static String[][] getFileContentsAsArray(final String fileName, final String delimeter, final int rows,
			final int cols, final boolean returnHeader) {

		final int r = returnHeader ? rows : rows - 1;

		String colsArray[] = new String[cols];
		final String[][] data = new String[r][cols];

		try (final BufferedReader in = new BufferedReader(new FileReader(fileName))) {
			String line;
			if (!returnHeader) {
				line = in.readLine(); // if not needed return headers - read
										// fist line before read lines with data
			}
			for (int z = 0; z < r; z++) {
				line = in.readLine();
				if (line != null) {
					// Get line values
					colsArray = line.split(delimeter);
					for (int j = 0; j < colsArray.length; j++) {
						// Put in Matrix
						try {
							data[z][j] = !colsArray[j].isEmpty() ? colsArray[j] : "";
						} catch (final ArrayIndexOutOfBoundsException a) {
							in.close();
							return data;
						}
					}
				}
			}
		} catch (final Exception e) {
			Log.errorHandler("Error in FileIO.getFileContentsAsList: ", e);
		}

		return data;
	}

	/**
	 * Returns file content as a multidimensional String Array
	 *
	 * @param fileName
	 *            Path and filename of file to read and delimiter of data within
	 *            file
	 * @param delimiter
	 *            character that separates data in the file
	 * @return returns multidimensional string array of data within file
	 *         separated by a delimiter
	 */
	public static String[][] getFileContentsAsArray(final String fileName, final String delimiter) {

		final int r = getNumberOfRowsInFile(fileName);
		final int c = getNumberOfColumnsInFile(fileName, delimiter);

		String cols[] = new String[c];
		final String[][] data = new String[r][c];

		try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
			String line;
			for (int z = 0; z < r; z++) {
				line = in.readLine();
				if (line != null) {
					// Get line values
					cols = line.split(delimiter);
					for (int j = 0; j < cols.length; j++) {
						// Put in Matrix
						try {
							if (!cols[j].isEmpty()) {
								data[z][j] = cols[j];
							} else {
								data[z][j] = "";
							}
						} catch (final ArrayIndexOutOfBoundsException a) {
							in.close();
							return data;
						}
					}
				}
			}
		} catch (final Exception e) {
			Log.errorHandler("Error in FileIO.getFileContentsAsList: ", e);
		}

		return data;
	}

	/**
	 * Get file contents from specified file by given charset.
	 *
	 * @param fileName
	 *            - absolute path of target file
	 * @param charset
	 *            - charset such as "UTF-8"
	 * @return String - content of target file or null if there is any exception
	 *         occurred
	 */
	public static String getFileContents(final String fileName, final String charset) {
		// create reader - if charset is not specified or not supported, use
		// default charset;
		InputStreamReader reader = null;
		if (charset == null) {
			try {
				reader = new InputStreamReader(new FileInputStream(fileName));
			} catch (final FileNotFoundException e) {
				Log.errorHandler("Error in FileIO.getFileContents(String filename, String charset): ", e);
				return "";
			}
		} else {
			try {
				try {
					reader = new InputStreamReader(new FileInputStream(fileName), charset);
				} catch (final FileNotFoundException e) {
					Log.errorHandler("Error in FileIO.getFileContents(String filename, String charset): ", e);
					return "";
				}
			} catch (final UnsupportedEncodingException e) {
				try {
					reader = new InputStreamReader(new FileInputStream(fileName));
				} catch (final FileNotFoundException ee) {
					Log.errorHandler(
							"Error in FileIO.getFileContents(String filename, String charset): " + ee.getMessage());
					return "";
				}
			}
		}

		// read content
		final char[] c = new char[1024];
		int count = 0;
		final StringBuilder buffer = new StringBuilder();
		try {
			while ((count = reader.read(c)) != -1) {
				buffer.append(c, 0, count);
			}
		} catch (final IOException e) {
			Log.errorHandler("Error in FileIO.getFileContents(String filename, String charset): ", e);
			return "";
		}

		// return
		try {
			reader.close();
		} catch (final Exception e2) {
			Log.errorHandler("Error in FileIO.getFileContents(String filename, String charset): ", e2);
		}

		return buffer.toString();
	}

	/**
	 * Get file contents from specified file by charset "UTF-8".
	 *
	 * @param fileName
	 *            - absolute path of target file
	 * @return String - content of target file or null if there is any exception
	 *         occurred
	 */
	public static String getFileContentsByUTF8(final String fileName) {
		return getFileContents(fileName, "UTF-8");
	}

	/**
	 * Returns File array of files contained within a given directory
	 * <P>
	 *
	 * @param dir
	 *            Directory to read
	 * @return Returns File array of files contained within a given directory
	 */
	public static File[] getDirContents(final String dir) {
		try {
			return new File(dir).listFiles();
		} catch (final Exception e) {
			Log.errorHandler("Error in FileIO.getDirContents: ", e);
			return null;
		}
	}

	/**
	 * Returns file content as a single dimensional String Array or list
	 *
	 * @param fileName
	 *            Path and filename of file to read
	 * @return file content as a String Array
	 */
	public static String[] getFileContentsAsList(final String fileName) {
		int z = 0;
		final int n = getNumberOfLinesInFile(fileName);
		final String lines[] = new String[n];

		try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = in.readLine()) != null) {
				if (z >= n) {
					break;
				}

				lines[z] = line;
				z++;
			}
		} catch (final IOException e) {
			Log.errorHandler("Error in FileIO.getFileContentsAsList: ", e);
		}

		return lines;
	}

	/**
	 * Returns file content as a List Array
	 *
	 * @param fileName
	 *            Path and filename of file to read
	 * @return file content as a List
	 */
	public static List<String> getFileContentsAsListArray(final String fileName) {
		int z = 0;
		final int n = getNumberOfLinesInFile(fileName);
		final List<String> lines = new ArrayList<>();

		try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = in.readLine()) != null) {
				if (z >= n) {
					break;
				}

				lines.add(line);
				z++;
			}
		} catch (final IOException e) {
			Log.errorHandler("Error in FileIO.getFileContentsAsList: ", e);
		}

		return lines;
	}

	/**
	 * Returns file content as a String array using a specified character set.
	 *
	 * @param fileName
	 *            absolute path of target file
	 * @param charset
	 *            charset such as "UTF-8"
	 * @return String[] array which contains file content one line by one
	 *         element
	 */
	public static String[] getFileContentsAsList(final String fileName, final String charset) {
		int z = 0;
		final int n = getNumberOfLinesInFile(fileName);
		final String lines[] = new String[n];

		// create reader - if charset is not specified or not supported, use
		// default charset;
		BufferedReader reader = null;
		if (charset == null) {
			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
			} catch (final FileNotFoundException e) {
				Log.errorHandler("Error in FileIO.getFileContents(String filename, String charset): ", e);
				return lines;
			}
		} else {
			try {
				try {
					reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), charset));
				} catch (final FileNotFoundException e) {
					Log.errorHandler("Error in FileIO.getFileContents(String filename, String charset): ", e);
					return lines;
				}
			} catch (final UnsupportedEncodingException e) {
				try {
					reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
				} catch (final FileNotFoundException ee) {
					Log.errorHandler(
							"Error in FileIO.getFileContents(String filename, String charset): " + ee.getMessage());
					return lines;
				}
			}
		}

		// read content
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				if (z >= n) {
					break;
				}
				lines[z] = line;
				z++;
			}
			reader.close();
		} catch (final IOException e) {
			Log.errorHandler("Error in FileIO.getFileContents(String filename, String charset): ", e);
			return lines;
		}

		return lines;
	}

	/**
	 * Returns file content as a String array. Use charset "UTF-8".
	 *
	 * @param fileName
	 *            absolute path of target file
	 * @return array which contains file content one line by one element
	 */
	public static String[] getFileContentsAsListByUTF8(final String fileName) {
		return getFileContentsAsList(fileName, "UTF-8");
	}

	/**
	 * Returns number of lines in a specified file
	 * <p>
	 *
	 * @param fileName
	 *            Path and filename of file to read
	 * @return int number of lines in a specified file
	 */
	public static int getNumberOfLinesInFile(final String fileName) {
		int i = 0;
		try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
			while (in.readLine() != null) {
				i++;
			}
		} catch (final IOException e) {
			Log.errorHandler("Error in FileIO.getNumberOfLinesInFile: ", e);
		}

		return i;
	}

	/**
	 * Returns number of rows in a specified file
	 * <p>
	 *
	 * @param fileName
	 *            Path and filename of file to read
	 * @return int number of rows in a specified file
	 */
	public static int getNumberOfRowsInFile(final String fileName) {
		return getNumberOfLinesInFile(fileName);
	}

	/**
	 * Returns the number of columns in a file Columns are separated by a
	 * delimiter in a specified file. For example a file containing the
	 * following data: "Fred, Steve, Tony, John" would return 4 (columns of
	 * data)
	 * <p>
	 *
	 * @param fileName
	 *            Path and filename of file to read
	 * @param delim
	 *            the data separating character
	 * @return int number of columns in a specified file
	 */
	public static int getNumberOfColumnsInFile(final String fileName, final String delim) {
		int i = 0;
		int maxCol = 0;
		try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
			String s;
			while ((s = in.readLine()) != null) {
				i = Strings.count(s, delim);
				if (i > maxCol) {
					maxCol = i;
				}
			}
		} catch (final IOException e) {
			Log.errorHandler("Error in FileIO.getNumberOfColumnsInFile: ", e);
		}

		return maxCol + 1;
	}

	/**
	 * Writes specified string content to file
	 * <p>
	 *
	 * @param fileName
	 *            path and filename of file to write string to
	 * @param contents
	 *            String to write to file
	 */
	public static void writeFileContents(final String fileName, final String contents) {
		// write specified string content to file
		try (FileWriter out = new FileWriter(fileName)) {
			out.write(contents);
		} catch (final IOException e) {
			Log.errorHandler("Error in FileIO.writeFileContents: ", e);
		}
	}

	/**
	 * Write specified string content into given file using a specified
	 * character set. If the charset is null or not supported, will use default
	 * charset.
	 *
	 * @param fileName
	 *            absolute path of file
	 * @param contents
	 *            content to be written
	 * @param charset
	 *            charset such as "UTF-8"
	 */
	public static void writeFileContents(final String fileName, final String contents, final String charset) {
		// write specified string content to file
		OutputStreamWriter out = null;

		try {
			// if charset is null, use default charset
			if (charset == null) {
				try {
					out = new OutputStreamWriter(new FileOutputStream(fileName));
				} catch (final FileNotFoundException e) {
					Log.errorHandler(
							"Error in FileIO.writeFileContents(String filename, String sContents, String charset): ",
							e);
				}
			} else {
				// if charset is specified, try use it; if it is not support,
				// use default charset
				try {
					out = new OutputStreamWriter(new FileOutputStream(fileName), charset);
				} catch (final UnsupportedEncodingException e) {
					Log.errorHandler("FileIO.writeFileContents(String filename, String sContents, String charset): '"
							+ charset + "' is NOT SUPPORTED! Default charset will be used!");
					try {
						out = new OutputStreamWriter(new FileOutputStream(fileName));
					} catch (final FileNotFoundException ee) {
						Log.errorHandler(
								"Error in FileIO.writeFileContents(String filename, String sContents, String charset): "
										+ ee.getMessage());
					}
				} catch (final FileNotFoundException e) {
					Log.errorHandler(
							"Error in FileIO.writeFileContents(String filename, String sContents, String charset): ",
							e);
				}
			}
			out.write(contents);
		} catch (final Exception e) {
			Log.errorHandler("Error in FileIO.writeFileContents(String filename, String sContents, String charset): ",
					e);
		} finally {
			try {
				out.close();
			} catch (final IOException e) {
				Log.errorHandler(
						"Error in FileIO.writeFileContents(String filename, String sContents, String charset): ", e);
			}
		}
	}

	/**
	 * Write specified string content into give file with UTF-8 encoding.
	 *
	 * @param fileName
	 *            absolute path of file
	 * @param contents
	 *            content to be written
	 */
	public static void writeFileContentsByUTF8(final String fileName, final String contents) {
		writeFileContents(fileName, contents, "UTF-8");
	}

	/**
	 * Writes specified List content to file (CSV or TXT)
	 *
	 * @param fileName
	 *            path and filename of file to write List to
	 * @param contents
	 *            List to write to file
	 * @param separator
	 *            the data delimiter i.e. "," or ";", etc.
	 */
	public static void writeListContents(final String fileName, final List<String> contents, final String separator) {
		// write specified string content to file
		try (FileWriter fw = new FileWriter(fileName); PrintWriter out = new PrintWriter(fw)) {
			// ',' divides the word into columns and prints the Header row
			for (int i = 0; i < contents.size(); i++) {
				if (i < contents.size() - 1) {
					out.print(contents.get(i) + separator);// first row first
															// column
					// out.print(",");
				} else {
					out.println(contents.get(i));
				}
			}
			out.flush();
		} catch (final IOException e) {
			Log.errorHandler("Error in writeListContents() ", e);
		}
	}

	/**
	 * Writes specified String[][] content to file (CSV or TXT) with new line
	 * and carriage return for every element
	 *
	 * @param fileName
	 *            path and filename of file to write String[][] to
	 * @param contents
	 *            String[][] to write to file
	 * @param separator
	 *            the data delimiter i.e. "," or ";", etc.
	 */
	public static void writeStringArrayContents(final String fileName, final String[][] contents,
			final String separator) {
		// write specified string content to file
		try (FileWriter fw = new FileWriter(fileName); PrintWriter out = new PrintWriter(fw)) {
			// ',' divides the word into columns and prints the Header row
			for (final String[] lsContent : contents) {
				if (lsContent != null) {
					for (final String element : lsContent) {
						if (element != null) {
							out.print(element + "\r\n");
						}
					}
				}
			}
			out.flush();
		} catch (final IOException e) {
			Log.errorHandler("Error in writeListContents() ", e);
		}
	}

	/**
	 * Appends specified List content to file (CSV or TXT)
	 *
	 * @param fileName
	 *            path and filename of file to append List to
	 * @param contents
	 *            List to append to the file
	 * @param separator
	 *            the delimiter used to separate data in the list
	 */
	public static void appendListContents(final String fileName, final List<String> contents, final String separator) {
		// write specified string content to file
		try (FileWriter fw = new FileWriter(fileName); PrintWriter out = new PrintWriter(fw)) {
			// ',' divides the word into columns and prints the Header row
			for (int i = 0; i < contents.size(); i++) {
				if (i < contents.size() - 1) {
					out.print(contents.get(i) + separator);// first row first
															// column
					// out.print(",");
				} else {
					out.println(contents.get(i));
				}
			}
			out.flush();
		} catch (final IOException e) {
			Log.errorHandler("Error in appendListToFile()", e);
		}
	}

	/**
	 * write contents to given file except .html file using UTF-8 encoding.
	 *
	 * @param fileName
	 *            name of target file
	 * @param contents
	 *            content will be appended to existing specified file content
	 */
	public static void appendStringToFile(final String fileName, final String contents) {
		try (FileOutputStream out = new FileOutputStream(fileName, true)) {
			final byte[] bytes = getbyteString(System.getProperty("line.separator") + contents, "UTF-8");
			out.write(bytes);// contentBytes
		} catch (final IOException e) {
			Log.errorHandler("Error in appendStringToFile()", e);
		}
	}

	/**
	 * print given byte array
	 *
	 * @param banner
	 *            String content
	 * @param abc
	 *            byte array
	 */
	public static void printBytes(final String banner, final byte[] abc) {
		System.out.print(banner + "=");
		for (final byte element : abc) {
			System.out.print(java.lang.Integer.toHexString(element) + "  ");
		}
		System.out.println();
	}

	/**
	 * Returns bytes of given string using specific encoding
	 *
	 * @param s
	 *            string to returns bytes from
	 * @param coding
	 *            - encoding string such as UTF-8, UTF-16, GB2312 and so on. if
	 *            coding equals null, it will use default encoding.
	 * @return bytes of given string using specific encoding
	 */
	public static byte[] getbyteString(final String s, final String coding) {
		if (s == null) {
			return null;
		}
		byte[] result;
		try {
			if (null == coding) {
				result = s.getBytes();
			} else {
				result = s.getBytes(coding);
			}
			return result;
		} catch (final Exception e) {
			result = null;
		}
		return result;
	}

	/**
	 * Returns the file type extension of a given filename or file path and
	 * name. Returns empty string if there is no file extension
	 *
	 * @param fileName
	 *            the file path and or file name in String format
	 * @return the file type extension of a given filename or file path and
	 *         name. Returns empty string if there is no file extension
	 */
	public static String getFileExtension(final String fileName) {
		if (fileName == null) {
			return "";
		}
		final String afterLastSlash = fileName.substring(fileName.lastIndexOf('/') + 1);
		final int dotIndex = afterLastSlash.indexOf('.', afterLastSlash.lastIndexOf('\\') + 1);

		return dotIndex == -1 ? "" : afterLastSlash.substring(dotIndex);
	}

	/**
	 * Returns the parent folder in a given full path. Works with either
	 * backslash or slash delimiter. Examples: c:\\test1\\blah1\\blah2\\ returns
	 * c:\\test1\\blah1 <BR>
	 * /usr/local/bin/bash returns /usr/local/bin/
	 * <p>
	 *
	 * @param path
	 *            to get the parent folder from
	 * @return The parent folder
	 */
	public static String getParentPath(final String path) {
		try {
			return Paths.get(path).toAbsolutePath().getParent() + getFileSeparator();
		} catch (final Exception e) {
			Log.errorHandler("Error occurred in getParentPath()", e);
			return "";
		}
	}

	/**
	 * This method takes a path and strips the parent path info returning only
	 * the last folder name in the given full path. Works with either backslash
	 * or slash delimiter. Examples: c:\test1\blah1\\blah2\\ returns blah2 <br>
	 * /usr/local/bin/bash returns bash
	 * <p>
	 *
	 * @param path
	 *            The full path to strip
	 * @return The final filename or directory name without path info
	 */
	public static String stripPath(final String path) {
		try {
			return new File(path).getName();
		} catch (final Exception e) {
			Log.errorHandler("Error occurred in stripPath()", e);
			return "";
		}
	}

	/**
	 * Strips trailing slash from specified path string
	 *
	 * @param path
	 *            String such as (i.e. "c:\\auto\\test\\one\\")
	 * @return the given path string without the trailing backslash i.e.
	 *         "c:\\auto\\test\\one"
	 */
	public static String stripTrailingSlash(final String path) {
		try {
			return new File(path).getAbsolutePath();
		} catch (final Exception e) {
			Log.errorHandler("Error occurred in stripTrailingSlash()", e);
			return "";
		}
	}

	/**
	 * Strips leading slash from specified path string
	 *
	 * @param path
	 *            - (i.e. "\\auto\\test\\one\\")
	 * @return given path string without the leading slashes i.e.
	 *         auto\\test\\one\\
	 */
	public static String stripLeadingSlash(final String path) {
		try {
			final String fs = getFileSeparator();

			return path.startsWith(fs) ? path.substring(fs.length()) : path;
		} catch (final Exception e) {
			Log.errorHandler("Error occurred in stripLeadingSlash()", e);
			return "";
		}
	}

	/**
	 * Returns the operating systems file separator
	 *
	 * @return the operating systems file separator i.e. "\\"
	 */
	public static String getFileSeparator() {
		return Platform.getFileSeparator();
	}

	/**
	 * Returns the top most folder in a given path. For instance, if the input
	 * is "c:\\MyPortal\\blah2\\blah3\\blah4\\", it returns "c:\\MyPortal". If
	 * the input is "\\MyPortal\\blah2\\blah3\\blah4\\" it returns "\\MyPortal"
	 * <p>
	 *
	 * @param path
	 *            The path to get the highest level folder from.
	 * @return The top most or parent part of the given path.
	 */
	public static String getTopParent(final String path) {
		try {
			return Paths.get(path).getRoot().toString() + getFileSeparator() + Paths.get(path).subpath(0, 1).toString();
		} catch (final Exception e) {
			Log.errorHandler("Error occurred in getTopParent()", e);
			return "";
		}
	}

	/**
	 * Returns the first folder in a given path. For instance, if the input is
	 * "c:\\MyPortal\\blah2\\blah3\\blah4\\", it returns "c:\\MyPortal". If the
	 * input is "\\MyPortal\\blah2\\blah3\\blah4\\" it returns "\\MyPortal"
	 * <p>
	 *
	 * @param path
	 *            The path to get the highest level folder from.
	 * @return The top most or parent part of the given path.
	 */
	public static String getFirstChildInPath(final String path) {
		return getTopParent(path);
	}

	/**
	 * Returns the root or drive from a given path. For instance, if the input
	 * is "c:\\MyPortal\\blah2\\blah3\\blah4\\", it returns "c:\\". If the input
	 * is "\\MyPortal\\blah2\\blah3\\blah4\\" it returns "\\MyPortal"
	 * <p>
	 *
	 * @param path
	 *            The path to get the root or drive from.
	 * @return the root or drive part of the given path.
	 */
	public static String getRoot(final String path) {
		try {
			return Paths.get(path).getRoot().toString() + getFileSeparator();
		} catch (final Exception e) {
			Log.errorHandler("Error occurred in getRoot()", e);
			return "";
		}
	}

	/**
	 * Returns the root or drive from a given path. For instance, if the input
	 * is "c:\\MyPortal\\blah2\\blah3\\blah4\\", it returns "c:\\". If the input
	 * is "\\MyPortal\\blah2\\blah3\\blah4\\" it returns "\\MyPortal"
	 * <p>
	 *
	 * @param path
	 *            The path to get the root or drive from.
	 * @return the root or drive part of the given path.
	 */
	public static String getDrive(final String path) {
		return getRoot(path);
	}

	/**
	 * Gets the last folder name from full directory path provided. For example,
	 * if directory passed is 'C:\folder1\folder2' OR '/folder1/folder2/', the
	 * method will return 'folder2'. If directory is 'C:\' OR 'C:', the method
	 * will return 'C:'
	 *
	 * @param directory
	 *            String representing directory path; it can include the
	 *            trailing slash, but does not have to.
	 * @return String with last directory path container. If error occurs, empty
	 *         String is returned.
	 */
	public static String getLastChildInPath(final String directory) {
		return stripPath(directory);
	}

	/**
	 * Strips off the first folder or parent folder of the given path. For
	 * instance, if the input is "c:\\MyPortal\\blah2\\blah3\\blah4", it returns
	 * "\\blah2\\blah3\\blah4".
	 *
	 * @param path
	 *            The path to use to strip the first or parent folder from
	 * @return The path with the first part stripped off.
	 */
	public static String stripTopParent(final String path) {
		try {
			final String s = getTopParent(path);

			return path.substring(path.indexOf(s) + s.length());
		} catch (final Exception e) {
			Log.errorHandler("Error occurred in stripTopParent()", e);
			return "";
		}

	}

	/**
	 * Deletes specified directories
	 * <p>
	 *
	 * @param dirName
	 *            directory name to delete. example:
	 *            "c:\\First\\Second\\Third"); this will delete the "Third"
	 *            directory and everything in it.
	 */
	public static void deleteDirs(final String dirName) {
		deleteFile(dirName);
	}

	/**
	 * Checks to see if a given file or directory exists. Returns true if it
	 * exists, false if it does not exist
	 * <p>
	 *
	 * @param fileName
	 *            the path and filename to search for
	 * @return Returns true if the file or directory exists, false if it does
	 *         not exist
	 */
	public static boolean fileExists(final String fileName) {
		return new File(fileName).exists();
	}

	/**
	 * Waits for a file to exist for a specified time if it does not exist
	 * initially. Returns true if it exists, false if it does not exist after
	 * checking for a specified time
	 * <p>
	 *
	 * @param fileName
	 *            the path and filename to search for
	 * @param maxWait
	 *            the maximum time to wait for a file to exist.
	 * @return Returns true if the file or directory exists, false if it does
	 *         not exist
	 */
	public static boolean fileExists(final String fileName, final int maxWait) {
		final File fileToCheck = new File(fileName);
		int count = 0;
		while (!fileToCheck.exists()) {
			Platform.sleep(Log.AUTOMATION_WAIT_VALUE_1);
			count++;
			if (count >= maxWait) {
				return false;
			}
		}

		return fileToCheck.exists();
	}

	/**
	 * Checks to see if a given file or directory exists. Returns true if it
	 * exists, false if it does not exist
	 * <p>
	 *
	 * @param pathName
	 *            the path and filename to search for
	 * @return true if the file or directory exists, false if it does not exist
	 */
	public static boolean pathExists(final String pathName) {
		return fileExists(pathName);
	}

	/**
	 * Checks to see if a given file or directory exists. Returns true if it
	 * exists, false if it does not exist
	 * <p>
	 *
	 * @param pathName
	 *            the path and filename to search for
	 * @return true if the file or directory exists, false if it does not exist
	 */
	public static boolean dirExists(final String pathName) {
		return fileExists(pathName);
	}

	/**
	 * This method creates an absolute path from a given path, based on a
	 * current path. For example, if current path is
	 * '/ParentFolder/folder1/folder2/' and directory passed is either
	 * '../folder2/folder3/', '../folder1/folder2/folder3/',
	 * '/ParentFolder/folder1/folder2/folder3' or
	 * 'root\\ParentFolder\\folder1\\folder2\\folder3\\', path returned is
	 * always 'root\\ParentFolder\\folder1\\folder2\\folder3\\' where root is a
	 * current local drive i.e. C: or D:.
	 *
	 * @param dir
	 *            String representing Path to be changed into absolute directory
	 * @return String value of absolute path, i.e.
	 *         'C:\\ParentFolder\\folder1\\folder2\\folder3\\' or an empty
	 *         string if the path passed is not related to the current project
	 *         path.
	 */
	public static String makeAbsoluteProjectPath(String dir) {
		final String slash = "/";
		final String backslash = "\\";
		final String currProjPath = new File(".").getAbsolutePath().replace(".", "");
		final String platformBackslash = Platform.getFileSeparator();
		String topDir = "";

		if (dir.contains(":")) {
			final String root = dir.substring(0, dir.lastIndexOf(":") + 1);
			dir = dir.replace(root, root.toUpperCase());
		}

		if (dir.contains("..")) {

			dir = dir.replace("..", "");
			topDir = getTopParent(dir) + platformBackslash;

			if (currProjPath.contains(topDir)) {
				while (currProjPath.contains(topDir)) {
					dir = stripTopParent(dir);
					topDir = getTopParent(dir) + platformBackslash;
				}

				return currProjPath + stripLeadingSlash(dir).replace(slash, platformBackslash) + platformBackslash;

			}
		} else if (dir.contains(slash)) {
			dir = dir.replace(slash, backslash);

			if (dir.contains(currProjPath.substring(currProjPath.lastIndexOf(":") + 1))) {
				return currProjPath.substring(0, currProjPath.lastIndexOf(":") + 1) + dir;
			}
		} else {
			return dir.contains(currProjPath) ? dir : "";
		}

		return "";
	}

	/**
	 * Compares two files and returns true if the files are identical or false
	 * if they are different
	 *
	 * @param fileName1
	 *            Path and filename of file1 to read ex: "D:\\1.TXT" or
	 *            "D:\\1.xml"
	 * @param fileName2
	 *            Path and filename of file2 to read ex: "D:\\1.TXT" or
	 *            "D:\\1.xml"
	 * @return boolean true if files compare , false if files do not compare
	 */
	public static boolean compareFiles(final String fileName1, final String fileName2) {
		String[] array1 = null;
		String[] array2 = null;
		try {
			array1 = getFileContentsAsList(fileName1);
			array2 = getFileContentsAsList(fileName2);
		} catch (final Exception e) {
			Log.errorHandler("Error in FileIO.compareFile: " + e.getMessage());
		}

		// Log.logDebugInfo("Comparing int array i1: " + Arrays.toString(array1)
		// + " and i2: " + Arrays.toString(array2));

		if (Arrays.deepEquals(array1, array2)) {
			// Log.logScriptInfo("Contents of both files are the same: " +
			// Strings.sDQ + filename1 + Strings.sDQ + " to " + Strings.sDQ +
			// filename2 + Strings.sDQ);
			return true;
		}

		// Log.logScriptInfo("Contents of files are NOT the same: " +
		// Strings.sDQ + filename1 + Strings.sDQ + " to " + Strings.sDQ +
		// filename2 + Strings.sDQ);
		return false;
	}

	/**
	 * Moves files or directories from source path of target path.
	 *
	 * @param sourceDir
	 *            full file or directory path to source. For example: C:\\temp\\
	 * @param targetDir
	 *            full destination path of file or folder. For example:
	 *            C:\\backup\\temp\\.
	 */
	public static void moveDir(final String sourceDir, final String targetDir) {
		try {
			Files.move(Paths.get(sourceDir), Paths.get(targetDir), StandardCopyOption.REPLACE_EXISTING,
					StandardCopyOption.ATOMIC_MOVE);
		} catch (final Exception e) {
			Log.errorHandler("Error occurred in moveDir()", e);
		}

	}

	/**
	 * Moves files or directories from source path of target path.
	 *
	 * @param sourceFile
	 *            full file path of file to be moved. For example:
	 *            C:\temp\test.txt
	 * @param targetFile
	 *            full path for file destination. For example:
	 *            C:\backup\myfile.txt.
	 */
	public static void moveFile(final String sourceFile, final String targetFile) {
		moveDir(sourceFile, targetFile);
	}

	/**
	 * Checks whether a specified directory is empty.
	 *
	 * @param dir
	 *            path to directory
	 * @return true if the directory is empty
	 */
	public static boolean isDirectoryEmpty(final String dir) {
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(dir))) {
			return !directoryStream.iterator().hasNext();
		} catch (final IOException e) {
			Log.errorHandler("Error occurred in isDirectoryExists()", e);
			return true;
		}
	}

	/**
	 * Gets last modified time of file. Example usage:
	 * FileIO.getLastModifiedTime("c:\\movetest\\Test123.txt") returns
	 * 1420757326205 Returns the modified time as a long data type but can be
	 * combined with DateTime methods to return readable dates and time Example:
	 * DateTime.getFormattedDateTime(FileIO.getLastModifiedTime("c:\\movetest\\Test123.txt"),"MM\dd\yyyy
	 * hh:mm:ss") returns 01\08\2015 05:48:46
	 *
	 * @param filePath
	 *            file path
	 * @return last modified time
	 */
	public static long getLastModifiedTime(final String filePath) {
		try {
			return Files.getLastModifiedTime(Paths.get(filePath)).toMillis();
		} catch (final Exception e) {
			Log.errorHandler("Error occurred in getLastModifiedTime()", e);
			return 0;
		}
	}

	/**
	 * returns the file size in bytes
	 *
	 * @param filePath
	 *            file to check the size of
	 * @return file size in bytes
	 */
	public static long getFileSize(final String filePath) {
		try {
			return Files.size(Paths.get(filePath));
		} catch (final Exception e) {
			Log.errorHandler("Error occurred in getFileSize()", e);
			return 0;
		}
	}

	/**
	 * Empties a directory in the given path by deleting all files, folders and
	 * nested folders within the parent folder.
	 *
	 * @param path
	 *            path of directory
	 */
	public static void clearDirectory(final Path path) {
		if (Files.exists(path)) {
			try {
				Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

					@Override
					public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs)
							throws IOException {
						Files.delete(file);
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult postVisitDirectory(final Path dir, final IOException exception)
							throws IOException {
						if (exception == null) {
							Files.delete(dir);
							return FileVisitResult.CONTINUE;
						}

						throw new SetupException(exception);
					}
				});
			} catch (final IOException exception) {
				throw new SetupException(exception);
			}
		}
	}

	/**
	 * Writes lines with specified content to file.
	 *
	 * @param filePath
	 *            path to file to write
	 * @param lines
	 *            specific content
	 */
	public static void writeFileContent(final Path filePath, final List<StringBuilder> lines) {
		try {
			Files.write(filePath, lines, StandardCharsets.UTF_8);
		} catch (final IOException exception) {
			throw new SetupException(exception);
		}
	}

	/**
	 * Writes line with specified content to file.
	 *
	 * @param filePath
	 *            path to file to write
	 * @param line
	 *            specific content
	 */
	public static void writeFileContent(final Path filePath, final String line) {
		final List<StringBuilder> lines = new ArrayList<>();
		lines.add(new StringBuilder(line));

		writeFileContent(filePath, lines);
	}

	/**
	 * Creates a file in the specified directory.
	 *
	 * @param filePath
	 *            full file path
	 * @return the file path as a Path
	 */
	public static Path fileExists(final Path filePath) {
		try {
			dirExists(filePath.getParent());

			return Files.exists(filePath) ? filePath : Files.createFile(filePath);
		} catch (final Exception exception) {
			throw new SetupException(exception);
		}
	}

	/**
	 * Ensures that the directory exists.
	 *
	 * @param dirPath
	 *            full path to dir which necessary to create.
	 */
	public static void dirExists(final Path dirPath) {
		try {
			if (!Files.exists(dirPath, LinkOption.NOFOLLOW_LINKS)) {
				Files.createDirectories(dirPath);
			}
		} catch (final Exception exception) {
			throw new SetupException(exception);
		}
	}

	/**
	 * Copies resource from jar fail to destination.
	 *
	 * @param resource
	 *            name of resource in jar file.
	 * @param destination
	 *            full path to file.
	 */
	public static void copyResource(final String resource, final String destination) {
		try (InputStream inputStream = FileIO.class.getClassLoader().getResourceAsStream(resource);
				OutputStream outputStream = new FileOutputStream(new File(destination))) {
			int readBytes;
			final byte[] buffer = new byte[4096];
			while ((readBytes = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, readBytes);
			}
		} catch (final Exception exception) {
			Log.errorHandler("Error occurred in copyResource()", exception);
		}
	}

	/**
	 * Creates a copy of file or folder.
	 *
	 * @param sourcePath
	 *            intended for copy full path of source. For example:
	 *            C:\temp.txt.
	 * @param targetPath
	 *            full path of target. For example: C:\temp.txt-backup.
	 * @return the path to the target file
	 */
	public static Path copy(final Path sourcePath, final Path targetPath) {
		return ExceptionHandler.execute(new Callable<Path>() {
			@Override
			public Path call() throws Exception {
				return Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING,
						StandardCopyOption.COPY_ATTRIBUTES, LinkOption.NOFOLLOW_LINKS);
			}
		});
	}

	/**
	 * Moves file from source path to target path.
	 *
	 * @param sourceFilePath
	 *            intended for move full path to source file. For example:
	 *            C:\temp.txt.
	 * @param targetFilePath
	 *            full target path. For example: C:\temp.txt-backup.
	 * @return the path to the target file
	 */
	public static Path move(final Path sourceFilePath, final Path targetFilePath) {
		return ExceptionHandler.execute(new Callable<Path>() {
			@Override
			public Path call() throws Exception {
				return Files.move(sourceFilePath, targetFilePath, StandardCopyOption.REPLACE_EXISTING,
						StandardCopyOption.ATOMIC_MOVE);
			}
		});
	}

	/**
	 * Writes properties to file.
	 *
	 * @param filePath
	 *            path to file to write
	 * @param properties
	 *            properties to save
	 */
	public static void writeProperties(final String filePath, final Properties properties) {
		try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
			properties.store(fileOutputStream, null);
		} catch (final IOException exception) {
			throw new SetupException(exception);
		}
	}

	/**
	 * Returns true if there is property with the specified key ignoring case.
	 *
	 * @param propertiesPath
	 *            absolute path to properties file
	 * @param key
	 *            key of seeking property
	 * @return true if property with the specified key exists
	 */
	public static boolean propertiesContainIgnoreCase(final String propertiesPath, final String key) {
		return propertiesContainIgnoreCase(Log.loadFromFile(propertiesPath), key);
	}

	/**
	 * Returns true if there is property with the specified key ignoring case.
	 *
	 * @param propertiesFile
	 *            properties file
	 * @param key
	 *            key of seeking property
	 * @return true if property with the specified key exists
	 */
	public static boolean propertiesContainIgnoreCase(final File propertiesFile, final String key) {
		return propertiesContainIgnoreCase(Log.loadFromFile(propertiesFile), key);
	}

	/**
	 * Returns true if there is property with the specified key ignoring case.
	 *
	 * @param properties
	 *            properties
	 * @param key
	 *            key of seeking property
	 * @return true if property with the specified key exists
	 */
	public static boolean propertiesContainIgnoreCase(final Properties properties, final String key) {
		if (properties.containsKey(key)) {
			return true;
		}

		for (final Object propertyKey : properties.keySet()) {
			if (propertyKey.toString().toUpperCase().equals(key.toUpperCase())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns value of the property by the specified key ignoring case.
	 *
	 * @param propertiesPath
	 *            absolute path to properties file
	 * @param key
	 *            key of the seeking property, should be existing key
	 * @throws IllegalArgumentException
	 *             if necessary key does not exist
	 * @return value of the property
	 */
	public static String propertyGetIgnoreCase(final String propertiesPath, final String key) {
		return propertyGetIgnoreCase(Log.loadFromFile(propertiesPath), key);
	}

	/**
	 * Returns value of the property by the specified key ignoring case.
	 *
	 * @param propertiesFile
	 *            properties file
	 * @param key
	 *            key of the seeking property, should be existing key
	 * @throws IllegalArgumentException
	 *             if necessary key does not exist
	 * @return value of the property
	 */
	public static String propertyGetIgnoreCase(final File propertiesFile, final String key) {
		return propertyGetIgnoreCase(Log.loadFromFile(propertiesFile), key);
	}

	/**
	 * Returns value of the property by the specified key ignoring case.
	 *
	 * @param properties
	 *            properties under search
	 * @param key
	 *            key of the seeking property, should be existing key
	 * @throws IllegalArgumentException
	 *             if necessary key does not exist
	 * @return value of the property
	 */
	public static String propertyGetIgnoreCase(final Properties properties, final String key) {
		if (properties.containsKey(key)) {
			return properties.getProperty(key);
		}

		for (final Object propertyKey : properties.keySet()) {
			if (propertyKey.toString().toUpperCase().equals(key.toUpperCase())) {
				return properties.getProperty(propertyKey.toString());
			}
		}

		throw new IllegalArgumentException(String.format("There is no such key: [%s].", key));
	}
}
