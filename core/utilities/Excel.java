package core.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.NodeList;

import core.utilities.data.CSV;
import core.utilities.data.DataXML;
import core.utilities.data.MicrosoftXML;
import core.utilities.data.XLS;
import core.utilities.exceptions.AutomationException;
import core.utilities.scripting.dataproviders.FindFile;

/**
 * The Excel class contains Excel spreadsheet functions to read and write data to and from Excel files.
 */
public class Excel {

	/**
	 * Sets the cell content in the given spreadsheet file, sheet, column and
	 * row.
	 *
	 * @param fileName
	 *            name of the spreadsheet file to get set content to. The
	 *            spreadsheet can be of the .xlsx, .xls, .xml or .csv file types
	 * @param sheetName
	 *            name of the sheet within the given spreadsheet file to set the
	 *            cell content to
	 * @param rowIndex
	 *            the row number to set the cell content to (i.e. 0 = the first
	 *            row, 1= the second row, 2= 3rd column)
	 * @param columnIndex
	 *            the index or number of the column to set the cell content to
	 *            (i.e. 0 = the first column, 1= the second column, 2= 3rd
	 *            column)
	 * @param value
	 *            value to set
	 */
	public static void setCellValue(String fileName, String sheetName, int rowIndex, int columnIndex, String value) {
		try {
			fileName = getCompletePathToExcelFile(fileName);

			Log.logDebugInfo("Set cell value in file: " + fileName);

			if (isCSV(fileName)) { // if .csv format
				new CSV(fileName).setCellValue(rowIndex, columnIndex, value);
			} else if (isXLS(fileName) || isXLSX(fileName)) { // if .xlsx or
																// .xls format
				new XLS(fileName, sheetName).setCellValue(rowIndex, columnIndex, value);
			} else if (isMicrosoftXML(fileName)) { // if .xml format
				new MicrosoftXML(fileName, sheetName).setCellValue(rowIndex, columnIndex, value);
			} else if (isDataXML(fileName)) { // if .xml format
				new DataXML(fileName).setCellValue(rowIndex, columnIndex, value);
			} else {
				Log.errorHandler(String.format("Error setting cell value: wrong file extension - %s",
						FileIO.getFileExtension(fileName)));

			}

		} catch (Exception exception) {
			throw new AutomationException(String.format("Error set cell value: %s", exception.getMessage()), exception);
		}
	}

	/**
	 * Updates the row with cell content in the given spreadsheet file, sheet
	 *
	 * @param fileName
	 *            name of the spreadsheet file to update row content in. The
	 *            spreadsheet can be of the .xlsx, .xls, .xml or .csv file types
	 * @param sheetName
	 *            name of the sheet within the given spreadsheet file to update
	 *            the row content in
	 * @param rowIndex
	 *            the row number to update the row content in (i.e. 0 = the
	 *            first row, 1= the second row, 2= 3rd column)
	 * @param cells
	 *            the {@link List} of cells values
	 */
	public static void updateRowData(String fileName, String sheetName, int rowIndex, List<String> cells) {
		try {
			fileName = getCompletePathToExcelFile(fileName);

			Log.logDebugInfo("Insert row into file: " + fileName);

			if (isCSV(fileName)) { // if .csv format
				new CSV(fileName).updateRowData(rowIndex, cells);
			} else if (isXLS(fileName) || isXLSX(fileName)) { // if .xlsx or
																// .xls format
				new XLS(fileName, sheetName).updateRowData(rowIndex, cells);
			} else if (isMicrosoftXML(fileName)) { // if .xml format
				new MicrosoftXML(fileName, sheetName).updateRowData(rowIndex, cells);
			} else if (isDataXML(fileName)) { // if .xml format
				new DataXML(fileName).updateRowData(rowIndex, cells);
			} else {
				Log.errorHandler(String.format("Error inserting row: wrong file extension - %s",
						FileIO.getFileExtension(fileName)));

			}

		} catch (Exception exception) {
			throw new AutomationException(String.format("Error insert row: %s", exception.getMessage()), exception);
		}
	}

	/**
	 * Insert the row with cell content in the given spreadsheet file after
	 * necessary row.
	 *
	 * @param fileName
	 *            name of the spreadsheet file to update row content in. The
	 *            spreadsheet can be of the .xlsx, .xls, .xml or .csv file types
	 * @param sheetName
	 *            name of the sheet within the given spreadsheet file to update
	 *            the row content in
	 * @param rowIndex
	 *            the row number after what needed to insert new row(i.e. 0 =
	 *            the first row, 1= the second row, 2= 3rd column)
	 * @param cells
	 *            the {@link List} of cells values
	 */
	public static void insertRowDataAfter(String fileName, String sheetName, int rowIndex, List<String> cells) {
		try {
			fileName = getCompletePathToExcelFile(fileName);

			Log.logDebugInfo("Insert row into file: " + fileName);

			if (isCSV(fileName)) { // if .csv format
				new CSV(fileName).insertRowDataAfter(rowIndex, cells);
			} else if (isXLS(fileName) || isXLSX(fileName)) { // if .xlsx or
																// .xls format
				new XLS(fileName, sheetName).insertRowDataAfter(rowIndex, cells);
			} else if (isMicrosoftXML(fileName)) { // if .xml format
				new MicrosoftXML(fileName, sheetName).insertRowDataAfter(rowIndex, cells);
			} else if (isDataXML(fileName)) { // if .xml format
				new DataXML(fileName).insertRowDataAfter(rowIndex, cells);
			} else {
				Log.errorHandler(String.format("Error inserting row: wrong file extension - %s",
						FileIO.getFileExtension(fileName)));

			}

		} catch (Exception exception) {
			throw new AutomationException(String.format("Error insert row: %s", exception.getMessage()), exception);
		}
	}

	/**
	 * Returns the cell content of the given spreadsheet file, sheet, column and
	 * row.
	 *
	 * @param fileName
	 *            name of the spreadsheet file to get cell content from. The
	 *            spreadsheet can be of the .xls, .xlsx, .xml or .csv file
	 *            types.
	 * @param sheetName
	 *            name of the sheet within the given spreadsheet file to get the
	 *            cell content from
	 * @param columnIndex
	 *            the index or number of the column to get the cell content from
	 *            (i.e. 0 = the first column, 1= the second column, 2= 3rd
	 *            column)
	 * @param rowIndex
	 *            the row number to get the cell content from (i.e. 0 = the
	 *            first row, 1= the second row, 2= 3rd column)
	 * @return String containing the text content of the cell (of the given
	 *         excel, sheet, column and row)
	 */
	public static String getCellContent(String fileName, String sheetName, int columnIndex, int rowIndex) {
		try {
			fileName = getCompletePathToExcelFile(fileName);

			Log.logDebugInfo("Test data read from: " + fileName);

			// if .xlsx or .xls format
			if (isXLS(fileName) || isXLSX(fileName)) {
				return new XLS(fileName, sheetName).getCellContent(columnIndex, rowIndex);
			} else if (isCSV(fileName)) {
				return new CSV(fileName).getCellContent(rowIndex, columnIndex);
			} else if (isMicrosoftXML(fileName)) {
				return new MicrosoftXML(fileName, sheetName).getCellContent(columnIndex, rowIndex);
			} else if (isDataXML(fileName)) { // if .xml format
				return new DataXML(fileName).getCellContent(columnIndex, rowIndex);
			} else {
				// Get the first sheet
				return "";
			}
		} catch (Exception e) {
			Log.errorHandler("Error getting the spreadsheet cell content", e);

			return "";
		}
	}

	/**
	 * Gets the total column count for the given spreadsheet.
	 *
	 * @param fileName
	 *            the full path name for the spreadsheet to get the column count
	 *            from. The spreadsheet can be of the .xls, .xlsx, .xml or .csv
	 *            file types.
	 * @param sheetName
	 *            the sheet name where the column data resides.
	 * @return int the total number of columns of data in the given spreadsheet
	 */
	public static int getColumnCount(String fileName, String sheetName) {
		try {
			fileName = getCompletePathToExcelFile(fileName);

			Log.logDebugInfo("Test data read from: " + fileName);

			// if .xlsx or .xls format
			if (isXLS(fileName) || isXLSX(fileName)) {
				return new XLS(fileName, sheetName).getColumnCount();
			} else if (isCSV(fileName)) { // if .csv format
				return new CSV(fileName).getColumnCount();
			} else if (isMicrosoftXML(fileName)) { // if .xml format
				return new MicrosoftXML(fileName, sheetName).getColumnCount();
			} else if (isDataXML(fileName)) { // if .xml format
				return new DataXML(fileName).getColumnCount();
			} else {
				Log.errorHandler(String.format("Error getting row count: wrong file extension - %s",
						FileIO.getFileExtension(fileName)));

				return 0;
			}

		} catch (Exception e) {
			Log.errorHandler("Error getting the column count", e);

			return 0;
		}
	}

	/**
	 * Returns column data from the specified column header or column number and
	 * given spreadsheet as a List of Strings
	 *
	 * @param fileName
	 *            The spreadsheet filename to get column data from. Can be an
	 *            .xls, .xlsx, .xml or .csv spreadsheet type
	 * @param sheetName
	 *            the sheet name where the column data resides.
	 * @param columnValue
	 *            the Column header name or column number to return the data
	 *            from. For Example, column header name can be something like
	 *            "Account Number" or it can be a specified column number
	 *            expressed as a string like this "5". Using a column number
	 *            such as "5" will get data from the 6th column in the
	 *            spreadsheet as the first column is equal to 0. For example in
	 *            the spreadsheet column A=0, B=1, C=2, D=3, E=4 and D=5.
	 * @return Column data (excluding the actual column header) from the
	 *         specified column header or column number and given spreadsheet as
	 *         a List of String
	 */
	public static List<String> getColumnData(String fileName, String sheetName, String columnValue) {
		try {
			fileName = getCompletePathToExcelFile(fileName);

			Log.logDebugInfo("Test data read from: " + fileName);

			// if .xlsx or .xls format
			if (isXLS(fileName) || isXLSX(fileName)) {
				return new XLS(fileName, sheetName).getColumnData(columnValue);
			} else if (isCSV(fileName)) { // if .csv format
				return new CSV(fileName).getColumnData(columnValue);
			} else if (isMicrosoftXML(fileName)) { // if .xml format
				return new MicrosoftXML(fileName, sheetName).getColumnData(columnValue);
			} else if (isDataXML(fileName)) { // if .xml format
				return new DataXML(fileName).getColumnData(columnValue);
			} else {
				return null;
			}
		} catch (Exception e) {
			Log.errorHandler("Error getting the spreadsheet column data", e);

			return null;
		}
	}

	/**
	 * Returns row headers from given spreadsheet as a List of Strings.
	 *
	 * @param fileName
	 *            The spreadsheet filename to get row headers from. Can be an
	 *            .xls, .xlsx, .xml or .csv spreadsheet type
	 * @param sheetName
	 *            the sheet name where the row headers resides.
	 * @return List of Strings with row headers
	 */
	public static List<String> getHeaders(String fileName, String sheetName) {
		return getRowData(fileName, sheetName, "0");
	}

	/**
	 * Gets the row count of the specified spreadsheet.
	 *
	 * @param fileName
	 *            the full path name for the spreadsheet to get the row count
	 *            from. The spreadsheet can be of the .xls, .xlsx, .csv or .xml
	 *            file types.
	 * @param sheetName
	 *            the sheet name where the row data resides.
	 * @return the total number of rows of data in the given spreadsheet
	 */
	public static int getRowCount(String fileName, String sheetName) {
		try {
			fileName = getCompletePathToExcelFile(fileName);

			Log.logDebugInfo("Test data read from: " + fileName);

			if (isCSV(fileName)) {
				return new CSV(fileName).getRowCount();
			} else if (isXLS(fileName) || isXLSX(fileName)) { // if .xlsx or
																// .xls format
				return new XLS(fileName, sheetName).getRowCount();
			} else if (isMicrosoftXML(fileName)) {
				return new MicrosoftXML(fileName, sheetName).getRowCount();
			} else if (isDataXML(fileName)) { // if .xml format
				return new DataXML(fileName).getRowCount();
			} else {
				Log.errorHandler(String.format("Error getting row count: wrong file extension - %s",
						FileIO.getFileExtension(fileName)));

				return 0;
			}

		} catch (Exception exception) {
			throw new AutomationException(String.format("Error getting row count: %s", exception.getMessage()),
					exception);
		}
	}

	/**
	 * Gets the row count of the specified spreadsheet exclude empty rows.
	 *
	 * @param fileName
	 *            the full path name for the spreadsheet to get the row count
	 *            from. The spreadsheet can be of the .xls, .xlsx, .csv or .xml
	 *            file types.
	 * @param sheetName
	 *            the sheet name where the row data resides.
	 * @return the total number of rows of data in the given spreadsheet
	 */
	public static int getRowCountExcludeEmptyRows(String fileName, String sheetName) {
		try {
			fileName = getCompletePathToExcelFile(fileName);

			Log.logDebugInfo("Test data read from: " + fileName);

			if (isCSV(fileName)) {
				return new CSV(fileName).getRowCountExcludeEmptyRows();
			} else if (isXLS(fileName) || isXLSX(fileName)) { // if .xlsx or
																// .xls format
				return new XLS(fileName, sheetName).getRowCountExcludeEmptyRows();
			} else if (isMicrosoftXML(fileName)) {
				return new MicrosoftXML(fileName, sheetName).getRowCountExcludeEmptyRows();
			} else if (isDataXML(fileName)) { // if .xml format
				return new DataXML(fileName).getRowCountExcludeEmptyRows();
			} else {
				Log.errorHandler(String.format("Error getting row count: wrong file extension - %s",
						FileIO.getFileExtension(fileName)));

				return 0;
			}

		} catch (Exception exception) {
			throw new AutomationException(String.format("Error getting row count: %s", exception.getMessage()),
					exception);
		}
	}

	/**
	 * Returns row data from the specified row header name or row number and
	 * given spreadsheet as a List of Strings.
	 *
	 * @param fileName
	 *            The spreadsheet filename to get row data from. Can be an .xls,
	 *            .xlsx, .xml or .csv spreadsheet type
	 * @param sheetName
	 *            the sheet name where the row data resides.
	 * @param rowValue
	 *            the row header name or row number to return the data from. For
	 *            Example, row header name can be something like "Account
	 *            Number" or it can be a specified row number expressed as a
	 *            string like this "5". Using a row number such as "5" will get
	 *            data from the 6th row in the spreadsheet as the first row is
	 *            equal to 0. For example in the spreadsheet row 1=0, 2=1, 3=2,
	 *            4=3, 5=4 and 6=5.
	 * @return Row data (excluding the row header) from the specified row header
	 *         or row number and given spreadsheet as a List of String
	 */
	public static List<String> getRowData(String fileName, String sheetName, String rowValue) {
		try {
			fileName = getCompletePathToExcelFile(fileName);

			Log.logDebugInfo("Test data read from: " + fileName);

			// if .xlsx or .xls format
			if (isXLS(fileName) || isXLSX(fileName)) {
				return new XLS(fileName, sheetName).getRowData(rowValue);
			} else if (isCSV(fileName)) { // if .csv format
				return new CSV(fileName).getRowData(rowValue);
			} else if (isMicrosoftXML(fileName)) { // if .xml format
				return new MicrosoftXML(fileName, sheetName).getRowData(rowValue);
			} else if (isDataXML(fileName)) { // if .xml format
				return new DataXML(fileName).getRowData(rowValue);
			} else {
				return null;
			}
		} catch (Exception e) {
			Log.errorHandler("Error getting the spreadsheet row data", e);

			return null;
		}
	}

	/**
	 * This method is used to retrieve the row data as a hash map by using
	 * findCell() which gets a cell for which content matching the string passed
	 * in. The returned data is in hashmap or key=value format i.e. {"First
	 * Name"="Tony", "Last Name"="Johnson"}
	 *
	 * @param fileName
	 *            The spreadsheet filename to get row data from. Can be an .xls,
	 *            .xlsx, .xml or .csv spreadsheet type
	 * @param sheetName
	 *            the sheet name where the row data resides.
	 * @param rowValue
	 *            the row header name or row number to return the data from. For
	 *            Example, row header name can be something like "Account
	 *            Number" or it can be a specified row number expressed as a
	 *            string like this "5". Using a row number such as "5" will get
	 *            data from the 6th row in the spreadsheet as the first row is
	 *            equal to 0. For example in the spreadsheet row 1=0, 2=1, 3=2,
	 *            4=3, 5=4 and 6=5.
	 * @return Row data as hashmap or key=value format i.e. {"First
	 *         Name"="Tony", "Last Name"="Johnson"} from the specified row
	 *         header or row number and given spreadsheet as a List of Strings
	 */
	public static Map<String, String> getRowDataAsMap(String fileName, String sheetName, String rowValue) {
		try {
			fileName = getCompletePathToExcelFile(fileName);

			Log.logDebugInfo("Test data read from: " + fileName);

			// if .xlsx or .xls format
			if (isXLS(fileName) || isXLSX(fileName)) {
				return new XLS(fileName, sheetName).getRowDataAsMap(rowValue);
			} else if (isCSV(fileName)) { // if .csv format
				return new CSV(fileName).getRowDataAsMap(rowValue);
			} else if (isMicrosoftXML(fileName)) { // if .xml format
				return new MicrosoftXML(fileName, sheetName).getRowDataAsMap(rowValue);
			} else if (isDataXML(fileName)) { // if .xml format
				return new DataXML(fileName).getRowDataAsMap(rowValue);
			} else {

				return null;
			}
		} catch (Exception e) {
			Log.errorHandler("Error occurred during getXlsRowDataAsMap()", e);

			return null;
		}
	}

	/**
	 * This method is used to retrieve the row data as a hash map by using
	 * findCell() which gets a cell for which content matching the string passed
	 * in. The returned data is in hashmap or key=value format i.e. {"First
	 * Name"="Tony", "Last Name"="Johnson"}
	 *
	 * @param fileName
	 *            The spreadsheet filename to get row data from. Can be an .xls,
	 *            .xlsx, .xml or .csv spreadsheet type
	 * @param sheetName
	 *            the sheet name where the row data resides.
	 * @param rowValue
	 *            the row header name or row number to return the data from. For
	 *            Example, row header name can be something like "Account
	 *            Number" or it can be a specified row number expressed as a
	 *            string like this "5". Using a row number such as "5" will get
	 *            data from the 6th row in the spreadsheet as the first row is
	 *            equal to 0. For example in the spreadsheet row 1=0, 2=1, 3=2,
	 *            4=3, 5=4 and 6=5.
	 * @return Row data as hashmap or key=value format i.e. {"First
	 *         Name"="Tony", "Last Name"="Johnson"} from the specified row
	 *         header or row number and given spreadsheet as a List of Strings
	 */
	public static Map<String, String> getRowDataAsMap(String fileName, String sheetName, int rowValue) {
		return getRowDataAsMap(fileName, sheetName, String.valueOf(rowValue));
	}

	/**
	 * Gets entire content of the spreadsheet as a list of map data.
	 *
	 * @param fileName
	 *            the spreadsheet filename to get row data from. Can be an .xls,
	 *            .xlsx, .xml or .csv spreadsheet type
	 * @param sheetName
	 *            the sheet name where the rows data resides.
	 * @return List of HashMap of rows content.
	 */
	public static List<Map<String, String>> getSheetDataAsListOfMap(String fileName, String sheetName) {
		try {
			fileName = getCompletePathToExcelFile(fileName);

			Log.logDebugInfo("Test data read from: " + fileName);

			// if .xlsx or .xls format
			if (isXLS(fileName) || isXLSX(fileName)) {
				return new XLS(fileName, sheetName).getSheetDataAsListOfMap();
			} else if (isCSV(fileName)) { // if .csv format
				return new CSV(fileName).getDataAsListOfMap();
			} else if (isMicrosoftXML(fileName)) { // if .xml format
				return new MicrosoftXML(fileName, sheetName).getSheetDataAsListOfMap();
			} else if (isDataXML(fileName)) { // if .xml format
				return new DataXML(fileName).getDataAsListOfMap();
			} else {

				return null;
			}
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during getSheetDataAsListOfMap(): %s", exception.getMessage()),
					exception);
		}
	}

	/**
	 * Returns all the data from a given spreadsheet as a multidimensional
	 * string array
	 *
	 * @param fileName
	 *            The spreadsheet filename to get table data from. Can be an
	 *            .xls, .csv or .xlsx spreadsheet type
	 * @param sheetName
	 *            is the sheetName where the test data is present
	 * @return String[][] Array containing all of the data from the spreadsheet
	 *         file including header rows and columns
	 */
	public static String[][] getTableArray(String fileName, String sheetName) {
		return getTableArray(fileName, sheetName, true);
	}

	/**
	 * Returns all the data from a given spreadsheet as a multidimensional
	 * string array Can include or exclude column headers by specifying true or
	 * false to the bReturnheader parameter
	 *
	 * @param fileName
	 *            The spreadsheet filename to get table data from. Can be an
	 *            .xls, .xlsx, .xml or .csv spreadsheet type
	 * @param sheetName
	 *            is the sheetName where the test data is present
	 * @param returnHeader
	 *            specify true to include column headers or false to exclude
	 *            column headers
	 * @return String[][] Array containing all of the data from the spreadsheet
	 *         file
	 */
	public static String[][] getTableArray(String fileName, String sheetName, boolean returnHeader) {
		try {
			fileName = getCompletePathToExcelFile(fileName);

			Log.logDebugInfo("Test data read from: " + fileName);

			// if .xlsx or .xls format
			if (isXLS(fileName) || isXLSX(fileName)) {
				return new XLS(fileName, sheetName).getTableData(returnHeader);
			} else if (isCSV(fileName)) { // if .csv format
				return new CSV(fileName).getTableData(returnHeader);
			} else if (isMicrosoftXML(fileName)) { // if .xml format
				return new MicrosoftXML(fileName, sheetName).getTableData(returnHeader);
			} else if (isDataXML(fileName)) { // if .xml format
				return new DataXML(fileName).getTableData(returnHeader);
			} else {
				return null;
			}
		} catch (Exception e) {
			Log.errorHandler("Error occurred in getTableArray()", e);
			return null;
		}
	}

	/**
	 * Checks whether the specified sheet exists in .xlsx, .xls or .xml Excel
	 * file.
	 *
	 * @param fileName
	 *            The spreadsheet filename to get table data from. Can be an
	 *            .xls, .xlsx, .xml spreadsheet type
	 * @param sheetName
	 *            sheet name
	 * @return true if the specified sheet exists in Excel file, false otherwise
	 */
	public static boolean isSheetExists(String fileName, String sheetName) {
		try {
			fileName = getCompletePathToExcelFile(fileName);

			Log.logDebugInfo("Test data read from: " + fileName);

			// if .xlsx or .xls format
			if (isXLS(fileName) || isXLSX(fileName)) {
				return new XLS(fileName, sheetName).isSheetExists();
			} else if (isMicrosoftXML(fileName)) { // if .xml format
				return new MicrosoftXML(fileName, sheetName).isSheetExists();
			} else {
				return false;
			}

		} catch (Exception e) {
			throw new AutomationException(
					String.format("Sheet name '%s' doesn't exists in the test-data file %s", sheetName, fileName), e);
		}
	}

	/**
	 * Generates complete spreadsheet path, name and extension from a given
	 * pathname or name based on the information supplied in your
	 * automation.properties file.
	 *
	 * @param fileName
	 *            file name. For example: Transfers. This function will add the
	 *            necessary automation testdata path and the appropriate file
	 *            extension based on the information supplied in your
	 *            automation.properties file
	 * @return Returns complete path to spreadsheet file as a string
	 */
	public static String getCompletePathToExcelFile(String fileName) {
		return FindFile.findFile(fileName);
	}

	/**
	 * Checks is the given file is CSV.
	 *
	 * @param fileName
	 *            the full path name for the spreadsheet
	 * @return boolean
	 */
	private static Boolean isCSV(String fileName) {
		return FileIO.getFileExtension(fileName).equals(Log.AUTOMATION_CSV_EXT);
	}

	/**
	 * Checks is the given file is XLSX
	 *
	 * @param fileName
	 *            the full path name for the spreadsheet
	 * @return boolean
	 */
	private static Boolean isXLSX(String fileName) {
		return FileIO.getFileExtension(fileName).equals(Log.AUTOMATION_EXCEL_XLSX_EXT);
	}

	/**
	 * Checks is the given file is XML Spreadsheet 2003
	 *
	 * @param fileName
	 *            the full path name for the spreadsheet
	 * @return boolean
	 */
	private static Boolean isMicrosoftXML(String fileName) {
		NodeList element = XML.parse(Paths.get(fileName)).getElementsByTagName("Workbook");

		return (FileIO.getFileExtension(fileName).equals(Log.AUTOMATION_EXCEL_XML_EXT) && element.getLength() > 0
				&& element.item(0).getNamespaceURI().equals("urn:schemas-microsoft-com:office:spreadsheet"));
	}

	/**
	 * Checks is the given file is XML Data
	 *
	 * @param fileName
	 *            the full path name for the spreadsheet
	 * @return boolean
	 */
	private static Boolean isDataXML(String fileName) {
		NodeList element = XML.parse(Paths.get(fileName)).getElementsByTagName("data-set");

		return (FileIO.getFileExtension(fileName).equals(Log.AUTOMATION_EXCEL_XML_EXT) && element.getLength() > 0
				&& element.item(0).lookupNamespaceURI("xsi").equals("http://www.w3.org/2001/XMLSchema-instance"));
	}

	/**
	 * Checks is the given file is XLS.
	 *
	 * @param fileName
	 *            the full path name for the spreadsheet
	 * @return boolean
	 */
	private static Boolean isXLS(String fileName) {
		return FileIO.getFileExtension(fileName).equals(Log.AUTOMATION_EXCEL_XLS_EXT);
	}

	// ===================================================
	// Create and write methods for XLSX files
	// ===================================================

	/**
	 * Creates new Excel xlsx file.
	 *
	 * @param fileName
	 *            of .xlsx file to create
	 */
	public static void createXLSXFile(String fileName) {
		try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
			XSSFWorkbook workbook = new XSSFWorkbook();

			workbook.createSheet("Sheet1");
			workbook.createSheet("Sheet2");
			workbook.createSheet("Sheet3");

			workbook.write(fileOut);
			workbook.close();
		} catch (Exception e) {
			Log.errorHandler("Error creating Excel .xlsx file", e);
		}
	}

	/**
	 * Adds a new sheet to an existing Excel xlsx file or creates a new sheet in
	 * a new Excel file if file does not exist.
	 *
	 * @param fileName
	 *            the name of the excel file to create the new sheet in
	 * @param sheetName
	 *            the name of the sheet to create
	 */
	public static void createXLSXSheet(String fileName, String sheetName) {
		// read in existing excel file or create a new one
		try (XSSFWorkbook wbIn = new XSSFWorkbook(new FileInputStream(fileName));
				FileOutputStream fs = new FileOutputStream(new File(fileName))) {
			// add sheet
			wbIn.createSheet(sheetName);

			// write out
			wbIn.write(fs);
			wbIn.close();
		} catch (Exception e) {
			// create a new file

			try (FileOutputStream fileOut = new FileOutputStream(fileName); XSSFWorkbook wb = new XSSFWorkbook();) {
				wb.createSheet(sheetName);
				wb.write(fileOut);
			} catch (Exception exception) {
				Log.errorHandler("Error creating sheet in Excel .xlsx file", e);
			}
		}
	}

}
