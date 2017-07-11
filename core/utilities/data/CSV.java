package core.utilities.data;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import core.utilities.FileIO;
import core.utilities.Log;
import core.utilities.Strings;
import core.utilities.exceptions.AutomationException;

public class CSV {
	private final String fileName;
	private final List<CSVRecord> data;

	public CSV(String fileName) throws IOException {
		this.fileName = fileName;
		this.data = CSVParser.parse(new File(fileName), StandardCharsets.UTF_8, CSVFormat.RFC4180).getRecords();
	}

	/**
	 * Gets the row count of the specified .csv spreadsheet. This is a private
	 * method called and used by the main "get" methods to support files of the
	 * .csv file type.
	 *
	 * @return int the total number of rows of data in the given spreadsheet
	 */
	public int getRowCount() {
		try {
			return data.size();
		} catch (Exception e) {
			Log.errorHandler("ERROR getting row count in CSV file: " + fileName);
			return 0;
		}
	}

	/**
	 * Gets the row count of the specified .csv spreadsheet without empty rows.
	 * This is a private method called and used by the main "get" methods to
	 * support files of the .csv file type.
	 *
	 * @return int the total number of rows of data in the given spreadsheet
	 */
	public int getRowCountExcludeEmptyRows() {
		try {
			String[][] table = getTableData(true);
			int rowCount = 0;

			for (String[] row : table) {
				if (!Strings.join("", row).isEmpty()) {
					rowCount++;
				}
			}

			return rowCount;
		} catch (Exception e) {
			Log.errorHandler("ERROR getting row count in CSV file: " + fileName);
			return 0;
		}
	}

	/**
	 * This method is used to retrieve the row data as a hash map The returned
	 * data is in hashmap or key=value format i.e. {"First Name"="Tony", "Last
	 * Name"="Johnson"} This is a private method called and used by the main
	 * "get" methods to support files of the .csv file type.
	 *
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
	 *         header or row number and given spreadsheet as a List of String
	 */
	public Map<String, String> getRowDataAsMap(String rowValue) {
		int rowIndex = 0;
		try {

			Map<String, String> rowData = new HashMap<>();

			if (Strings.isNumber(rowValue)) {
				rowIndex = Integer.parseInt(rowValue);
			} else {
				// find row to get data from
				boolean flag = false;
				for (int row = 0; row < data.size(); row++) {
					for (int col = 0; col < data.get(0).size(); col++) {
						if (data.get(row).get(col).equals(rowValue)) {
							rowIndex = row;
							flag = true;
							break;
						}
					}

					if (flag) {
						break;
					}
				}

				if (rowIndex == 0) {
					throw new AutomationException(
							String.format("Row '%s' not found in CSV file: %s", rowValue, fileName));
				}
			}

			// get data as map
			for (int cellNumber = 0; cellNumber < getColumnCount(); cellNumber++) {
				rowData.put(data.get(0).get(cellNumber), data.get(rowIndex).get(cellNumber));
			}

			return rowData;
		} catch (Exception e) {
			Log.errorHandler("Error getting row data as map from spreadsheet file: " + fileName, e);

			return null;
		}
	}

	/**
	 * Returns all the data from a given .csv spreadsheet as a multidimensional
	 * string array This is a private method called and used by the main "get"
	 * methods to support files of the .csv file type.
	 *
	 * @param returnHeaders
	 *            true to return header information in output. Specify false to
	 *            ignore and not include header information in the output
	 * @return String[][] Array containing all of the data from the spreadsheet
	 *         file including header rows and columns
	 */
	public String[][] getTableData(boolean returnHeaders) {
		try {
			int rowCount = data.size();
			int colCount = data.get(0).size();

			String[][] tableData = new String[rowCount - ((returnHeaders) ? 0 : 1)][colCount];

			for (int rowIndex = ((returnHeaders) ? 0 : 1); rowIndex < rowCount; rowIndex++) {
				for (int colIndex = 0; colIndex < colCount; colIndex++) {
					tableData[rowIndex - ((returnHeaders) ? 0 : 1)][colIndex] = data.get(rowIndex).get(colIndex);
				}
			}

			return tableData;
		} catch (Exception e) {
			Log.errorHandler("ERROR getting table data in CSV file: " + fileName, e);

			return new String[0][0];
		}
	}

	/**
	 * Gets the total column count for the given .csv spreadsheet This is a
	 * private method called and used by the main "get" methods to support files
	 * of the .csv file type.
	 *
	 * @return int the total number of columns of data in the given spreadsheet
	 */
	public int getColumnCount() {
		try {
			return data.get(0).size();
		} catch (Exception e) {
			Log.errorHandler("ERROR getting column count in CSV file: " + fileName);
			return 0;
		}
	}

	/**
	 * Returns row data from the specified row header name or row number and
	 * given .csv spreadsheet as a List of String This is a private method
	 * called and used by the main "get" methods to support files of the .csv
	 * file type.
	 *
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
	public List<String> getRowData(String rowValue) {
		try {
			int rowIndex = -1;
			List<String> outputRow = new ArrayList<>();

			if (Strings.isNumber(rowValue)) {
				rowIndex = Integer.parseInt(rowValue);
			} else {
				// find row to get data from
				boolean flag = false;
				for (int row = 0; row < data.size(); row++) {
					for (int col = 0; col < data.get(0).size(); col++) {
						if (data.get(row).get(col).equals(rowValue)) {
							rowIndex = row;
							flag = true;
							break;
						}
					}

					if (flag) {
						break;
					}
				}

				if (rowIndex == -1) {
					throw new AutomationException(
							String.format("Row '%s' not found in test data file: %s", rowValue, fileName));
				}
			}

			for (int index = 0; index < data.get(rowIndex).size(); index++) {
				outputRow.add(data.get(rowIndex).get(index));
			}

			return outputRow;

		} catch (Exception e) {
			Log.errorHandler("ERROR getting row data in CSV file: " + fileName, e);
			return null;
		}
	}

	/**
	 * Gets entire content of the spreadsheet as a list of map data from .csv
	 * files.
	 *
	 * @return List of HashMap of rows content.
	 */
	public List<Map<String, String>> getDataAsListOfMap() {
		try {
			Log.logDebugInfo("Test data read from: " + fileName);

			List<Map<String, String>> mapData = new ArrayList<>();

			String[][] dataFromCSV = getTableData(true);

			for (String[] element : dataFromCSV) {
				HashMap<String, String> dataRow = new HashMap<>();
				for (int columnIndex = 0; columnIndex < data.get(0).size(); columnIndex++) {
					dataRow.put(data.get(0).get(columnIndex), element[columnIndex]);
				}
				mapData.add(dataRow);
			}

			return mapData;
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during CSV.getDataAsListOfMap(): %s", exception.getMessage()),
					exception);
		}
	}

	/**
	 * Returns column data from the specified column header or column number and
	 * given .csv spreadsheet as a List of String This is a private method
	 * called and used by the main "get" methods to support files of the .csv
	 * file type.
	 *
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
	public List<String> getColumnData(String columnValue) {
		try {
			List<String> columnsList = new ArrayList<>();

			int columnIndex = 0;

			if (Strings.isNumber(columnValue)) {
				columnIndex = Integer.parseInt(columnValue);
			} else {
				// find row to get data from
				boolean flag = false;
				for (int row = 0; row < data.size(); row++) {
					for (int col = 0; col < data.get(0).size(); col++) {
						if (data.get(row).get(col).equals(columnValue)) {
							columnIndex = col;
							flag = true;
							break;
						}
					}

					if (flag) {
						break;
					}
				}

				if (columnIndex == 0) {
					throw new AutomationException(
							String.format("Column '%s' not found in CSV file: %s", columnValue, fileName));
				}
			}

			// get column data
			for (int rowIndex = 1; rowIndex < data.size(); rowIndex++) {
				try {
					columnsList.add(data.get(rowIndex).get(columnIndex));
				} catch (ArrayIndexOutOfBoundsException e) {
					columnsList.add("");
				}
			}

			return columnsList;
		} catch (Exception e) {
			Log.errorHandler("ERROR getting column data in CSV file: " + fileName, e);

			return null;
		}
	}

	/**
	 * Returns the cell content of the given .csv spreadsheet file, column and
	 * row This is a private method called and used by the main "get" methods to
	 * support files of the .csv file type.
	 *
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
	public String getCellContent(int rowIndex, int columnIndex) {
		try {
			return data.get(rowIndex).get(columnIndex);
		} catch (Exception e) {
			Log.errorHandler("ERROR getting cell content from CSV file: " + fileName, e);

			return null;
		}
	}

	/**
	 * Sets the cell content in the given .csv spreadsheet file, sheet, column
	 * and row.
	 *
	 * @param rowIndex
	 *            rowIndex the row number to set the cell content to (i.e. 0 =
	 *            the first row, 1= the second row, 2= 3rd column)
	 * @param columnIndex
	 *            the index or number of the column to set the cell content to
	 *            (i.e. 0 = the first column, 1= the second column, 2= 3rd
	 *            column)
	 * @param value
	 *            to set
	 */
	public void setCellValue(int rowIndex, int columnIndex, String value) {
		String[][] data = null;

		data = getTableData(true);

		data[rowIndex][columnIndex] = value;
		List<StringBuilder> lines = new ArrayList<>();

		for (String[] row : data) {
			StringBuilder line = new StringBuilder();
			for (int i = 0; i < row.length; i++) {
				line.append(row[i]);
				if (i < row.length - 1) {
					line.append(",");
				}
			}
			lines.add(line);
		}
		FileIO.writeFileContent(Paths.get(fileName), lines);
	}

	/**
	 * Updates the row with cell content in the given spreadsheet file, sheet
	 *
	 * @param rowIndex
	 *            the row number to update the row content in (i.e. 0 = the
	 *            first row, 1= the second row, 2= 3rd column)
	 * @param cells
	 *            the {@link List} of cells values
	 * @throws IOException
	 *             error
	 */
	public void updateRowData(int rowIndex, List<String> cells) throws IOException {
		String[][] data = getTableData(true);
		for (int cellIndex = 0; cellIndex < data[rowIndex].length; cellIndex++) {
			data[rowIndex][cellIndex] = cells.get(cellIndex);
		}

		List<StringBuilder> lines = new ArrayList<>();

		for (String[] row : data) {
			StringBuilder line = new StringBuilder();
			for (int i = 0; i < row.length; i++) {
				line.append(row[i]);
				if (i < row.length - 1) {
					line.append(",");
				}
			}
			lines.add(line);
		}

		FileIO.writeFileContent(Paths.get(fileName), lines);
	}

	/**
	 * Insert the row with cell content in the given spreadsheet file after
	 * necessary row.
	 *
	 * @param rowIndex
	 *            the row number after what needed to insert new row(i.e. 0 =
	 *            the first row, 1= the second row, 2= 3rd column)
	 * @param cells
	 *            the {@link List} of cells values
	 * @throws IOException
	 *             error
	 */
	public void insertRowDataAfter(int rowIndex, List<String> cells) throws IOException {
		String[][] data = getTableData(true);

		String[][] temp = new String[data.length + 1][data[0].length];

		for (int index = 0; index < data.length; index++) {
			temp[index + ((index < rowIndex) ? 0 : 1)] = data[index];
		}

		for (int cellIndex = 0; cellIndex < data[rowIndex].length; cellIndex++) {
			temp[rowIndex][cellIndex] = cells.get(cellIndex);
		}

		List<StringBuilder> lines = new ArrayList<>();

		for (String[] row : temp) {
			StringBuilder line = new StringBuilder();
			for (int i = 0; i < row.length; i++) {
				line.append(row[i]);
				if (i < row.length - 1) {
					line.append(",");
				}
			}
			lines.add(line);
		}

		FileIO.writeFileContent(Paths.get(fileName), lines);
	}
}
