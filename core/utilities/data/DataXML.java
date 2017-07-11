package core.utilities.data;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import core.utilities.Log;
import core.utilities.Strings;
import core.utilities.XML;
import core.utilities.exceptions.AutomationException;

public class DataXML {

	private final Node sheet;
	private final String fileName;
	private final Document workbook;

	private static class Tags {
		static final String DATA_SET = "data-set";
		static final String RECORD = "record";
	}

	/**
	 * Constructor for the DataXML class. Reads in XML file content
	 *
	 * @param fileName
	 *            file to read
	 */
	public DataXML(String fileName) {
		this.fileName = fileName;
		workbook = XML.parse(Paths.get(fileName));
		sheet = XML.getNodesByXPath(workbook, Tags.DATA_SET).item(0);
	}

	/**
	 * Gets the row count of the specified .xml spreadsheet This is a private
	 * method called and used by the main "get" methods to support files of the
	 * .xml file type.
	 *
	 * @return int the total number of rows of data in the given spreadsheet
	 */
	public int getRowCount() {
		return Integer.valueOf(XML.getChildNodesByXPath(sheet, Tags.RECORD).getLength());
	}

	/**
	 * Gets the row count of the specified .xml spreadsheet This is a private
	 * method called and used by the main "get" methods to support files of the
	 * .xml file type.
	 *
	 * @return int the total number of rows of data in the given spreadsheet
	 */
	public int getRowCountExcludeEmptyRows() {
		return Integer.valueOf(XML.getChildNodesByXPath(sheet, String.format("%s[*]", Tags.RECORD)).getLength());
	}

	/**
	 * Gets the total column count for the given .xml spreadsheet This is a
	 * private method called and used by the main "get" methods to support files
	 * of the .xml file type.
	 *
	 * @return int the total number of columns of data in the given spreadsheet
	 */
	public int getColumnCount() {
		return Integer.valueOf(XML.getChildNodesByXPath(sheet, String.format("%s[1]/*", Tags.RECORD)).getLength());
	}

	/**
	 * Returns all the data from a given .xml spreadsheet as a multidimensional
	 * string array This is a private method called and used by the main "get"
	 * methods to support files of the .xml file type.
	 *
	 * @param returnHeader
	 *            include header information in output table data
	 * @return String[][] Array containing all of the data from the spreadsheet
	 *         file including header rows and columns
	 */
	public String[][] getTableData(boolean returnHeader) {
		NodeList rows = XML.getChildNodesByXPath(sheet, Tags.RECORD);

		int cellsCount = getColumnCount();

		String[][] tableArray = new String[getRowCount() + ((returnHeader) ? 1 : 0)][cellsCount];

		List<String> headers = getHeaders();

		if (returnHeader) {
			for (int headerIndex = 0; headerIndex < headers.size(); headerIndex++) {
				tableArray[0][headerIndex] = headers.get(headerIndex);
			}
		}

		for (int rowIndex = 0; rowIndex < rows.getLength(); rowIndex++) {
			for (int cellIndex = 0; cellIndex < cellsCount; cellIndex++) {
				tableArray[rowIndex + ((returnHeader) ? 1 : 0)][cellIndex] = getCellValue(rows.item(rowIndex),
						cellIndex + 1);
			}
		}

		return tableArray;
	}

	/**
	 * Gets cell value from {@link Node} that contains cell elements.
	 *
	 * @param row
	 *            {@link Node} that contains cell elements.
	 * @param cellIndex
	 * @return cell value
	 */
	private String getCellValue(Node row, int cellIndex) {
		try {
			return XML.getChildNodesByXPath(row, String.format("*[%s]", cellIndex)).item(0).getTextContent();
		} catch (Exception e) {
			return "";
		}
	}

	public List<String> getHeaders() {
		List<String> headers = new ArrayList<>();
		NodeList headersList = XML.getChildNodesByXPath(sheet, String.format("%s[1]/*", Tags.RECORD));
		for (int i = 0; i < headersList.getLength(); i++) {
			headers.add(headersList.item(i).getLocalName());
		}
		return headers;
	}

	/**
	 * Returns row data from the specified row header name or row number and
	 * given .xml spreadsheet as a List of String This is a private method
	 * called and used by the main "get" methods to support files of the .xml
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
		if (rowValue.equals("0")) {
			return getHeaders();
		}

		List<String> rowData = new ArrayList<>();

		Node row = null;
		int rowIndex = -1;
		int columnCount = getColumnCount();

		if (Strings.isNumber(rowValue)) {
			rowIndex = Integer.parseInt(rowValue);
			row = XML.getChildNodesByXPath(sheet, Tags.RECORD).item(rowIndex - 1);
		} else {
			row = XML.getNodesByXPath(workbook, String.format("//*[text()[contains(.,'%s')]]", rowValue)).item(0)
					.getParentNode();
		}

		if (row == null) {
			throw new AutomationException(
					String.format("Row '%s' not found in test data file: %s", rowValue, fileName));
		}

		for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
			rowData.add(getCellValue(row, columnIndex));
		}

		return rowData;
	}

	/**
	 * Returns the cell content of the given .xml spreadsheet file, sheet,
	 * column and row This is a private method called and used by the main "get"
	 * methods to support files of the .xml file type.
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
			return getRowData(String.valueOf(rowIndex)).get(columnIndex);
		} catch (Exception e) {
			Log.errorHandler("Error getting spreadsheet cell content", e);

			return "";
		}
	}

	/**
	 * This method is used to retrieve the row data as a hash map from .xml
	 * spreadsheets This is a private method called and used by the main "get"
	 * methods to support files of the .xlsx or .xls file type. The returned
	 * data is in HashMap or key=value format i.e. {"First Name"="Tony", "Last
	 * Name"="Johnson"}
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
		Map<String, String> rowData = new HashMap<>();

		List<String> headers = getHeaders();
		List<String> data = getRowData(rowValue);
		for (int index = 0; index < headers.size(); index++) {
			rowData.put(headers.get(index), data.get(index));
		}

		return rowData;
	}

	/**
	 * Returns column data from the specified column header or column number and
	 * given .xml spreadsheet as a List of String This is a private method
	 * called and used by the main "get" methods to support files of the .xml
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
		String header = "";

		if (Strings.isNumber(columnValue)) {
			header = getHeaders().get(Integer.parseInt(columnValue));
		} else {
			header = XML.getNodesByXPath(workbook, String.format("//*[text()[contains(.,'%s')]]", columnValue)).item(0)
					.getLocalName();
		}

		NodeList columns = XML.getChildNodesByXPath(sheet, String.format("//*[local-name()='%s']", header));

		List<String> columnData = new ArrayList<>();

		for (int rowIndex = 0; rowIndex < columns.getLength(); rowIndex++) {
			columnData.add(columns.item(rowIndex).getTextContent());
		}

		return columnData;
	}

	/**
	 * Gets entire content as a list of map data from .xml files.
	 *
	 * @return List of HashMap of rows content.
	 */
	public List<Map<String, String>> getDataAsListOfMap() {
		try {
			List<Map<String, String>> data = new ArrayList<>();

			List<String> columnsHeaders = getHeaders();
			List<String> cellsValue = null;

			for (int rowIndex = 0; rowIndex <= getRowCount(); rowIndex++) {
				HashMap<String, String> rowData = new HashMap<>();
				cellsValue = getRowData(String.valueOf(rowIndex));
				for (int columnIndex = 0; columnIndex < columnsHeaders.size(); columnIndex++) {
					rowData.put(columnsHeaders.get(columnIndex), cellsValue.get(columnIndex));
				}
				data.add(rowData);
			}

			return data;
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during getXMLSheetDataAsListOfMap(): %s", exception.getMessage()),
					exception);
		}
	}

	/**
	 * Sets the cell content in the given .xml spreadsheet file, sheet, column
	 * and row.
	 *
	 * @param rowIndex
	 *            the row number to set the cell content to (i.e. 1 = the first
	 *            row, 2= the second row, 3= 3rd column)
	 * @param columnIndex
	 *            the index or number of the column to set the cell content to
	 *            (i.e. 0 = the first column, 1= the second column, 2= 3rd
	 *            column)
	 * @param value
	 *            value to set
	 */
	public void setCellValue(int rowIndex, int columnIndex, String value) {
		if (rowIndex == 0) {
			throw new AutomationException("Can't change header");
		}

		XML.getChildNodesByXPath(sheet, String.format("//%s[%s]//*[%s]", Tags.RECORD, rowIndex, columnIndex)).item(0)
				.setTextContent(value);

		XML.saveToFile(workbook, Paths.get(fileName));
	}

	/**
	 * Updates the row with cell content in the given spreadsheet file, sheet
	 *
	 * @param rowIndex
	 *            the row number to update the row content in (i.e. 1 = the
	 *            first row, 2= the second row, 3= 3rd column)
	 * @param cells
	 *            the {@link List} of cells values
	 */
	public void updateRowData(int rowIndex, List<String> cells) {
		Node row = XML.getChildNodesByXPath(sheet, Tags.RECORD).item(rowIndex - 1);

		for (int cellIndex = 0; cellIndex < cells.size(); cellIndex++) {
			XML.getChildNodesByXPath(row, String.format("*[%s]", cellIndex + 1)).item(0)
					.setTextContent(cells.get(cellIndex));
		}

		XML.saveToFile(workbook, Paths.get(fileName));
	}

	/**
	 * Insert the row with cell content in the given spreadsheet file after
	 * necessary row.
	 *
	 * @param rowIndex
	 *            the row number after what needed to insert new row(i.e. 1 =
	 *            the first row, 2= the second row, 3= 3rd column)
	 * @param cells
	 *            the {@link List} of cells values
	 */
	public void insertRowDataAfter(int rowIndex, List<String> cells) {

		Node row = XML.getChildNodesByXPath(sheet, Tags.RECORD).item(rowIndex);

		Node newRow = XML.createChildElement(workbook, (Element) row, Tags.RECORD);

		Node cell = null;
		List<String> headers = getHeaders();

		for (int cellIndex = 0; cellIndex < cells.size(); cellIndex++) {
			cell = XML.createChildElement(workbook, (Element) newRow, headers.get(cellIndex));

			cell.setTextContent(cells.get(cellIndex));

			newRow.appendChild(cell);
		}

		sheet.insertBefore(newRow, row);

		XML.saveToFile(workbook, Paths.get(fileName));
	}
}
