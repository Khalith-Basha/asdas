
package core.utilities.report;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import core.utilities.FileIO;

/**
 * The class represents the essence of the txt report and contains all the
 * necessary methods to generate it.
 */
public class TXTReport {
	private static Path txtReportFile;

	/**
	 * Generates TXT report and opens if it necessary.
	 *
	 * @param path
	 *            path to TXT report file.
	 */
	public TXTReport(String path) {
		txtReportFile = Paths.get(path + ".txt");
		if (!FileIO.fileExists(txtReportFile.toString())) {
			FileIO.writeFileContentsByUTF8(txtReportFile.toString(), "");
		}
	}

	/**
	 * Adds content to TXT report file.
	 *
	 * @param content
	 *            step content
	 */
	public void addStep(String content) {
		try {
			Files.write(txtReportFile, (content + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
		}
	}
}
