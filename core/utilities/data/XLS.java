package core.utilities.data;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import core.utilities.Log;
import core.utilities.Strings;
import core.utilities.exceptions.AutomationException;
import core.utilities.exceptions.SetupException;

public class XLS {

	private final String fileName;
	private final String sheetName;
	private Sheet sheet;
	private final Workbook workbook;

	public XLS(String fileName, String sheetName) {
		this.fileName = fileName;
		this.sheetName = sheetName;

		try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(new File(fileName)))) {

			workbook = WorkbookFactory.create(inputStream);
			workbook.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);

			if (isSheetExists()) {
				sheet = getSheet();
			}
		} catch (InvalidFormatException | IOException exception) {
			throw new SetupException("Check the file name with the test data: " + fileName, exception);
		}
	}

	public Workbook getWorkbook() {
		return workbook;
	}

	/**
	 * Returns the cell content of the given .xlsx or .xls spreadsheet file,
	 * sheet, column and row This is a private method called and used by the
	 * main "get" methods to support files of the .xlsx or .xls file type.
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
	public String getCellContent(int columnIndex, int rowIndex) {
		try {
			return getCellValue(sheet.getRow(rowIndex).getCell(columnIndex));
		} catch (Exception e) {
			Log.errorHandler("Error getting spreadsheet cell content", e);

			return "";
		}
	}

	/**
	 * Gets the value of a cell depending on the cell type of and converts to
	 * string.
	 *
	 * @param cell
	 *            cell in a row of a spreadsheet
	 * @return cell value as string
	 */
	public String getCellValue(Cell cell) {
		String value = "";

		DataFormatter dataFormatter = new DataFormatter();

		FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();

		CellValue cellValue = evaluator.evaluate(cell);
		if (cellValue != null) {
			switch (cellValue.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				value = dataFormatter.formatCellValue(cell, evaluator);
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				value = String.valueOf(cellValue.getBooleanValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				break;
			case Cell.CELL_TYPE_BLANK:
				break;
			case Cell.CELL_TYPE_ERROR:
				break;
			case Cell.CELL_TYPE_STRING:
				value = cellValue.getStringValue();
				break;
			default:
				value = cellValue.getStringValue();
				break;
			}
		}

		return value;
	}

	/**
	 * Returns all the data from a given .xlsx or .xls spreadsheet as a
	 * multidimensional string array This is a private method called and used by
	 * the main "get" methods to support files of the .xlsx or .xls file type.
	 *
	 * @param returnHeader
	 *            true to return header information in table output
	 * @return String[][] Array containing all of the data from the spreadsheet
	 *         file including header rows and columns
	 */
	public String[][] getTableData(boolean returnHeader) {
		try {
			int maxRows = getRowCount();
			int maxCols = getColumnCount();

			String[][] tableArray = new String[maxRows - ((returnHeader) ? 0 : 1)][maxCols];

			Row row = null;

			for (int rowIndex = (returnHeader) ? 0 : 1; rowIndex <= maxRows; rowIndex++) {
				row = sheet.getRow(rowIndex);
				if (checkRowIsNotNull(row)) {
					for (int col = 0; col < maxCols; col++) {
						tableArray[rowIndex - ((returnHeader) ? 0 : 1)][col] = getCellValue(row.getCell(col));
					}
				}
			}

			return tableArray;
		} catch (Exception exception) {
			Log.errorHandler("Error getting table data", exception);

			return new String[0][0];
		}
	}

	/**
	 * Gets numbers of rows from given {@link Sheet}.
	 *
	 * @return number of real rows count
	 */
	public int getRowCount() {
		return sheet.getPhysicalNumberOfRows();
	}

	/**
	 * Gets numbers of rows from given {@link Sheet} exclude empty.
	 *
	 * @return number of real rows count
	 */
	public int getRowCountExcludeEmptyRows() {
		int rowCount = 0;
		if (sheet != null) {
			for (Row row : sheet) {
				if (checkRowIsNotNull(row)) {
					rowCount++;
				}
			}
		}
		return rowCount;
	}

	/**
	 * Checks is given {@link Row} is not null or blank.
	 *
	 * @param row
	 *            {@link Row} object
	 * @return true - if row is null or blank
	 */
	private Boolean checkRowIsNotNull(Row row) {
		int cellsCount = 0;
		if (row != null) {
			cellsCount = row.getPhysicalNumberOfCells();
			Iterator<Cell> cellIterator = row.cellIterator();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
					cellsCount--;
				}
			}
		}

		return cellsCount != 0;
	}

	/**
	 * Gets the total column count for the given .xlsx or .xls spreadsheet This
	 * is a private method called and used by the main "get" methods to support
	 * files of the .xlsx or .xls file type.
	 *
	 * @return int the total number of columns of data in the given spreadsheet
	 */
	public int getColumnCount() {
		try {
			return sheet.getRow(0).getPhysicalNumberOfCells();

		} catch (Exception e) {
			Log.errorHandler("Error getting maximum column", e);

			return 0;
		}
	}

	/**
	 * Returns column data from the specified column header or column number and
	 * given .xlsx or .xls spreadsheet as a List of String This is a private
	 * method called and used by the main "get" methods to support files of the
	 * .xlsx or .xls file type.
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
			List<String> colData = new ArrayList<>();

			Cell cell = sheet.getRow(0).getCell(0); // get column header

			int columnIndex;

			if (Strings.isNumber(columnValue)) {
				columnIndex = Integer.parseInt(columnValue);
			} else {
				columnIndex = findCell(columnValue).getColumnIndex();
			}

			// get column data
			for (int cellNumber = 1; cellNumber < getRowCount(); cellNumber++) {
				cell = sheet.getRow(cellNumber).getCell(columnIndex);

				if (cell != null) {
					colData.add(getCellValue(cell));
				}
			}

			return colData;
		} catch (Exception e) {
			Log.errorHandler("Error getting col data", e);

			return null;
		}
	}

	/**
	 * Return {#link Cell} object by value.
	 *
	 * @param cellValue
	 *            value of cell
	 */
	private Cell findCell(String cellValue) {
		for (Row row : sheet) {
			for (Cell cell : row) {
				if (cellValue.equals(getCellValue(cell))) {
					return cell;
				}
			}
		}

		return null;
	}

	/**
	 * Gets entire content of the spreadsheet as a list of map data from .xls or
	 * .xlsx files.
	 *
	 * @return List of HashMap of rows content.
	 */
	public List<Map<String, String>> getSheetDataAsListOfMap() {
		try {
			Log.logDebugInfo("Test data read from: " + fileName);

			List<Map<String, String>> data = new ArrayList<>();
			List<String> columnsHeadersRow = new ArrayList<>();

			for (Cell cell : sheet.getRow(0)) {
				columnsHeadersRow.add(getCellValue(cell));
			}

			Cell cell = null;
			String cellValue = null;

			for (int rowIndex = 0; rowIndex < getRowCount(); rowIndex++) {
				Map<String, String> dataRow = new HashMap<>();

				for (int columnIndex = 0; columnIndex < columnsHeadersRow.size(); columnIndex++) {
					cell = sheet.getRow(rowIndex).getCell(columnIndex);

					cellValue = getCellValue(cell);

					if (cell.getColumnIndex() < columnsHeadersRow.size()) {
						dataRow.put(columnsHeadersRow.get(cell.getColumnIndex()), cellValue);
					}
				}

				data.add(dataRow);
			}

			return data;
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during getSheetDataAsListOfMap(): %s", exception.getMessage()),
					exception);
		}
	}

	/**
	 * Returns row data from the specified row header name or row number and
	 * given .xlsx or .xls spreadsheet as a List of String This is a private
	 * method called and used by the main "get" methods to support files of the
	 * .xlsx or .xls file type.
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

			Row row = null;

			// find row to get data from
			if (Strings.isNumber(rowValue)) {
				try {
					rowIndex = Integer.parseInt(rowValue);
				} catch (Exception numTooLarge) {
					// row number too large or exception handling rowValue as an
					// Integer
					// so treat rowValue as a string
					// added to support ssb team that uses long account numbers
					// as row headers
					rowIndex = findCell(rowValue).getRowIndex();
				}
			} else {
				Cell cell = findCell(rowValue);
				if (cell != null) {
					rowIndex = cell.getRowIndex();
				}
			}

			try {
				row = sheet.getRow(rowIndex);
			} catch (Exception e) {

			}

			if (rowIndex == -1 || row == null) {
				throw new AutomationException(
						String.format("Row '%s' not found in test data file: %s", rowValue, fileName));
			}

			List<String> rowData = new ArrayList<>();

			// get entire row data
			for (int cellNumber = 0; cellNumber < row.getLastCellNum(); cellNumber++) {
				rowData.add(getCellValue(sheet.getRow(rowIndex).getCell(cellNumber)));
			}

			return rowData;
		} catch (Exception e) {
			Log.errorHandler("Error getting row data", e);

			return null;
		}
	}

	/**
	 * This method is used to retrieve the row data as a hash map from .xlsx or
	 * .xls spreadsheets This is a private method called and used by the main
	 * "get" methods to support files of the .xlsx or .xls file type. The
	 * returned data is in HashMap or key=value format i.e. {"First
	 * Name"="Tony", "Last Name"="Johnson"}
	 *
	 * @param rowValue
	 *            the row header name or row number to return the data from. For
	 *            Example, row header name can be something like "Account
	 *            Number" or it can be a specified row number expressed as a
	 *            string like this "5". Using a row number such as "5" will get
	 *            data from the 6th row in the spreadsheet as the first row is
	 *            equal to 0. For example in the spreadsheet row 1=0, 2=1, 3=2,
	 *            4=3, 5=4 and 6=5.
	 * @return Row data as HashMap or key=value format i.e. {"First
	 *         Name"="Tony", "Last Name"="Johnson"} from the specified row
	 *         header or row number and given spreadsheet as a List of String
	 */
	public Map<String, String> getRowDataAsMap(String rowValue) {
		try {
			int rowIndex = 0;
			Map<String, String> rowData = new HashMap<>();

			Row row = sheet.getRow(rowIndex);

			List<String> headers = getHeaders();

			// find row to get data from
			if (Strings.isNumber(rowValue)) {
				try {
					rowIndex = Integer.parseInt(rowValue);
					row = sheet.getRow(rowIndex);
				} catch (Exception numTooLarge) {
					// row number too large or exception handling rowValue as an
					// Integer
					// so treat rowValue as a string
					// added to support ssb team that uses long account numbers
					// as row headers
					row = findCell(rowValue).getRow();
				}
			} else {
				row = findCell(rowValue).getRow();
			}

			// get row data as map
			for (int cellNumber = 0; cellNumber < row.getLastCellNum(); cellNumber++) {
				try {
					rowData.put(headers.get(cellNumber).trim(), getCellValue(row.getCell(cellNumber)));
				} catch (Exception e) {
					rowData.put(String.valueOf(headers.get(cellNumber).trim()), "");
				}
			}

			return rowData;
		} catch (Exception e) {
			Log.errorHandler("Error getting row data", e);

			return null;
		}
	}

	/**
	 * Returns row headers from given spreadsheet as a List of String.
	 *
	 * @return List of String with row headers
	 */
	public List<String> getHeaders() {
		return getRowData("0");
	}

	private Sheet getSheet() {
		return workbook.getSheet(sheetName);
	}

	/**
	 * Checks whether the specified sheet exists in .xlsx, .xls or .xml Excel
	 * file.
	 *
	 * @return true if the specified sheet exists in Excel file, false otherwise
	 */
	public boolean isSheetExists() {
		try {
			return getSheet() != null;
		} catch (Exception e) {
			throw new AutomationException(
					String.format("Sheet name '%s' doesn't exists in the test-data file %s", sheetName, fileName), e);
		}
	}

	/**
	 * Sets the cell content in the given .xlsx or .xls spreadsheet file, sheet,
	 * column and row.
	 *
	 * @param rowIndex
	 *            the row number to set the cell content to (i.e. 0 = the first
	 *            row, 1= the second row, 2= 3rd column)
	 * @param columnIndex
	 *            the index or number of the column to set the cell content to
	 *            (i.e. 0 = the first column, 1= the second column, 2= 3rd
	 *            column)
	 * @param value
	 *            to set
	 */
	public void setCellValue(int rowIndex, int columnIndex, String value) {
		sheet.getRow(rowIndex).getCell(columnIndex).setCellValue(value);

		saveWorkbook();
	}

	/**
	 * Updates the row with cell content in the given spreadsheet file, sheet
	 *
	 * @param rowIndex
	 *            the row number to update the row content in (i.e. 0 = the
	 *            first row, 1= the second row, 2= 3rd column)
	 * @param cells
	 *            the {@link List} of cells values
	 */
	public void updateRowData(int rowIndex, List<String> cells) {
		Row row = sheet.getRow(rowIndex);

		for (int cellIndex = 0; cellIndex < row.getPhysicalNumberOfCells(); cellIndex++) {
			row.getCell(cellIndex).setCellValue(cells.get(cellIndex));
		}

		saveWorkbook();
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
	 */
	public void insertRowDataAfter(int rowIndex, List<String> cells) {
		// Shift number of rows down
		sheet.shiftRows(rowIndex, sheet.getPhysicalNumberOfRows(), 1);

		Row row = sheet.getRow(rowIndex);

		for (int cellIndex = 0; cellIndex < cells.size(); cellIndex++) {
			row.getCell(cellIndex).setCellValue(cells.get(cellIndex));
		}

		saveWorkbook();
	}

	/**
	 * Saves {@link Workbook} object to file.
	 */
	private void saveWorkbook() {
		try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
			workbook.write(fileOutputStream);
		} catch (IOException exception) {
			throw new SetupException(String.format("Can't write data to excel file: %s", fileName), exception);
		}
	}
}
