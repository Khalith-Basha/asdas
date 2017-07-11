package core.utilities.scripting.dataproviders;

import java.io.File;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import core.utilities.FileIO;
import core.utilities.Log;

public class FindFile {
	private static String datafile;

	/**
	 * Finds file in the directory and returns first match.
	 *
	 * @param filename - filename or part of the path.
	 *        For example: "transfers.xls", "android/phone/transfers.xls", "android\\phone\\transfers.xls",
	 *        "transfers", "android/tablet/transfers"
	 * @return full path
	 */
	public static String findFile(String filename) {
		// check to see if filename exists. If so then no need to go any further
		File f = new File(filename);
		if (f.exists()) {
			// Log.logScriptInfo("Accessing testdata file: " + filename);
			return filename;
		}

		// if filename does NOT exists then lets search for it within the global testdata folder
		Path startDir = Paths.get(Log.AUTOMATION_TEST_DATA_PATH);

		// if the filename does not specify a file extension then use the extension identified in the global automation properties file
		if (FileIO.getFileExtension(filename).isEmpty()) {
			filename = filename + Log.AUTOMATION_SPREADSHEET_SUFFIX;
		}

		if (!filename.startsWith("/")) {
			filename = "/" + filename;
		}

		// Find the filename within the global testdata folder
		final Pattern p = Pattern.compile(filename.replace("\\\\", ".").replace("\\", ".").replace("/", "."));

		FileVisitor<Path> matcherVisitor = new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attribs) {
				Matcher matcher = p.matcher(file.toString());
				if (matcher.find()) {
					datafile = file.toString();
					return FileVisitResult.TERMINATE;
				}
				return FileVisitResult.CONTINUE;
			}
		};

		try {
			Files.walkFileTree(startDir, matcherVisitor);
			if (datafile == null) {
				Log.errorHandler(
						"Error finding testdata file: " + filename + " within testdata folder: " + Log.AUTOMATION_TEST_DATA_PATH);
			} else {
				// Log.logScriptInfo("Accessing testdata file: " + datafile);
			}

			return datafile;

		} catch (Exception exception) {
			Log.errorHandler("Error finding testdata file: " + datafile);
			return datafile;
		}
	}


}
