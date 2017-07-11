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

public class MicrosoftXML {

	private final Node sheet;
	private final String fileName;
	private final Document workbook;

	public MicrosoftXML(String fileName, String sheetName) {
		this.fileName = fileName;

		workbook = XML.parse(Paths.get(fileName),
				MicrosoftXML.class.getClassLoader().getResourceAsStream("core/tools/ExcelXMLtransform.xslt"));

		sheet = XML.getNodesByXPath(workbook,
				String.format(
						"/*/*[local-name()='Worksheet'][@*[local-name()='Name' and . = '%s']]/*[local-name()='Table']",
						sheetName))
				.item(0);
	}

	/**
	 * Returns sheet from given file as the {@link Node} object.
	 *
	 * @return {@link Node} object
	 */
	public Node getSheet() {
		return sheet;
	}

	/**
	 * Gets the row count of the specified .xml spreadsheet This is a private
	 * method called and used by the main "get" methods to support files of the
	 * .xml file type.
	 *
	 * @return int the total number of rows of data in the given spreadsheet
	 */
	public int getRowCount() {
		return Integer.parseInt(sheet.getAttributes().getNamedItem("ss:ExpandedRowCount").getTextContent());
	}

	/**
	 * Gets the row count of the specified .xml spreadsheet exclude empty rows.
	 * This is a private method called and used by the main "get" methods to
	 * support files of the .xml file type.
	 *
	 * @return int the total number of rows of data in the given spreadsheet
	 */
	public int getRowCountExcludeEmptyRows() {
		return XML.getChildNodesByXPath(sheet, "*[local-name()='Row']").getLength();
	}

	/**
	 * Gets the total column count for the given .xml spreadsheet This is a
	 * private method called and used by the main "get" methods to support files
	 * of the .xml file type.
	 *
	 * @return int the total number of columns of data in the given spreadsheet
	 */
	public int getColumnCount() {
		return Integer.parseInt(sheet.getAttributes().getNamedItem("ss:ExpandedColumnCount").getTextContent());
	}

	/**
	 * Returns all the data from a given .xml spreadsheet as a multidimensional
	 * string array This is a private method called and used by the main "get"
	 * methods to support files of the .xml file type.
	 *
	 * @param returnHeader
	 *            true to return header information in table output
	 * @return String[][] Array containing all of the data from the spreadsheet
	 *         file including header rows and columns
	 */
	public String[][] getTableData(boolean returnHeader) {
		NodeList rows = XML.getChildNodesByXPath(sheet, "*[local-name()='Row']");

		int cellsCount = getColumnCount();

		String[][] tableArray = new String[getRowCount() - ((returnHeader) ? 0 : 1)][cellsCount];

		for (int rowIndex = (returnHeader) ? 0 : 1; rowIndex < rows.getLength(); rowIndex++) {
			for (int cellIndex = 0; cellIndex < cellsCount; cellIndex++) {
				tableArray[rowIndex - ((returnHeader) ? 0 : 1)][cellIndex] = (getCellValue(rows.item(rowIndex),
						cellIndex));

			}
		}

		return tableArray;
	}

	/**
	 * Checks whether the specified sheet exists in .xlsx, .xls or .xml Excel
	 * file.
	 *
	 * @return true if the specified sheet exists in Excel file, false otherwise
	 */
	public boolean isSheetExists() {
		try {
			return sheet != null;
		} catch (Exception e) {
			throw new AutomationException(e.getMessage(), e);
		}
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
		NodeList rows = XML.getChildNodesByXPath(sheet, "*[local-name()='Row']");
		List<String> rowData = new ArrayList<>();

		int rowIndex = -1;
		int columnCount = getColumnCount();

		if (Strings.isNumber(rowValue)) {
			rowIndex = Integer.parseInt(rowValue);
		} else {
			for (int index = 0; index < rows.getLength(); index++) {
				if (getCellValue(rows.item(index), 0).equals(rowValue)) {
					rowIndex = index;
					break;
				}
			}
		}

		if (rowIndex == -1) {
			throw new AutomationException(
					String.format("Row '%s' not found in test data file: %s", rowValue, fileName));
		}

		for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
			rowData.add(getCellValue(rows.item(rowIndex), columnIndex));
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

		List<String> headers = getRowData("0");
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
		NodeList rows = XML.getChildNodesByXPath(sheet, "*[local-name()='Row']");
		List<String> columnData = new ArrayList<>();

		int columnIndex = 0;

		if (Strings.isNumber(columnValue)) {
			columnIndex = Integer.parseInt(columnValue);
		} else {
			for (int cellIndex = 0; cellIndex < getColumnCount(); cellIndex++) {
				if (XML.getChildNodesByXPath(rows.item(0),
						String.format(
								"*[local-name()='Cell'][@*[local-name()='Index' and . = '%s']]/*[local-name()='Data']",
								String.valueOf(cellIndex + 1)))
						.item(0).getTextContent().equals(columnValue)) {
					columnIndex = cellIndex;
					break;
				}
			}
		}

		Node cell = null;
		for (int rowIndex = 1; rowIndex < rows.getLength(); rowIndex++) {
			cell = XML.getChildNodesByXPath(rows.item(rowIndex),
					String.format(
							"*[local-name()='Cell'][@*[local-name()='Index' and . = '%s']]/*[local-name()='Data']",
							String.valueOf(columnIndex + 1)))
					.item(0);

			columnData.add((cell != null) ? cell.getTextContent() : "");
		}

		return columnData;
	}

	/**
	 * Gets entire content of the spreadsheet as a list of map data from .xml
	 * files.
	 *
	 * @return List of HashMap of rows content.
	 */
	public List<Map<String, String>> getSheetDataAsListOfMap() {
		try {
			Log.logDebugInfo("Test data read from: " + fileName);

			List<Map<String, String>> data = new ArrayList<>();

			List<String> columnsHeaders = getRowData("0");
			List<String> cellsValue = null;

			for (int rowIndex = 0; rowIndex < getRowCount(); rowIndex++) {
				HashMap<String, String> dataRow = new HashMap<>();
				cellsValue = getRowData(String.valueOf(rowIndex));
				for (int columnIndex = 0; columnIndex < columnsHeaders.size(); columnIndex++) {
					dataRow.put(columnsHeaders.get(columnIndex), cellsValue.get(columnIndex));
				}
				data.add(dataRow);
			}

			return data;
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during getXMLSheetDataAsListOfMap(): %s", exception.getMessage()),
					exception);
		}
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
			return XML.getChildNodesByXPath(row, String.format(
					"*[local-name()='Cell'][@*[local-name()='Index' and . = '%s']]", String.valueOf(cellIndex + 1)))
					.item(0).getTextContent();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Sets the cell content in the given .xml spreadsheet file, sheet, column
	 * and row.
	 *
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
	public void setCellValue(int rowIndex, int columnIndex, String value) {
		Node row = XML.getChildNodesByXPath(sheet, "*[local-name()='Row']").item(rowIndex);

		Node cell = XML.getChildNodesByXPath(row,
				String.format("*[local-name()='Cell'][@*[local-name()='Index' and . = '%s']]/*[local-name()='Data']",
						String.valueOf(columnIndex + 1)))
				.item(0);

		if (cell == null) {
			Node newCell = XML.createChildElement(workbook, (Element) row, "Cell");

			Node attribute = newCell.getOwnerDocument().createAttribute("ss:Index");
			attribute.setNodeValue(String.valueOf(columnIndex + 1));
			newCell.getAttributes().setNamedItem(attribute);

			Node newCellData = XML.createChildElement(workbook, (Element) newCell, "Data");

			attribute = newCellData.getOwnerDocument().createAttribute("ss:Type");
			attribute.setNodeValue("String");
			newCellData.getAttributes().setNamedItem(attribute);

			row.insertBefore(newCell,
					XML.getChildNodesByXPath(row,
							String.format("*[local-name()='Cell'][@*[local-name()='Index' and . > '%s']]",
									String.valueOf(columnIndex + 1)))
							.item(0));

			newCellData.setTextContent(value);
			System.out.println(newCellData.getTextContent());

		} else {
			Node attribute = cell.getOwnerDocument().createAttribute("ss:Type");
			attribute.setNodeValue("String");
			cell.getAttributes().setNamedItem(attribute);

			cell.setTextContent(value);
		}

		XML.saveToFile(workbook, Paths.get(fileName));
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
		Node row = XML.getChildNodesByXPath(sheet, "*[local-name()='Row']").item(rowIndex);

		Node cell = null;

		for (int cellIndex = 0; cellIndex < cells.size(); cellIndex++) {
			cell = XML.getChildNodesByXPath(row,
					String.format(
							"*[local-name()='Cell'][@*[local-name()='Index' and . = '%s']]/*[local-name()='Data']",
							String.valueOf(cellIndex + 1)))
					.item(0);

			if (cell == null) {
				Node newCell = XML.createChildElement(workbook, (Element) row, "Cell");

				Node attribute = newCell.getOwnerDocument().createAttribute("ss:Index");
				attribute.setNodeValue(String.valueOf(cellIndex + 1));
				newCell.getAttributes().setNamedItem(attribute);

				Node newCellData = XML.createChildElement(workbook, (Element) newCell, "Data");

				attribute = newCellData.getOwnerDocument().createAttribute("ss:Type");
				attribute.setNodeValue("String");
				newCellData.getAttributes().setNamedItem(attribute);

				row.insertBefore(newCell,
						XML.getChildNodesByXPath(row,
								String.format("*[local-name()='Cell'][@*[local-name()='Index' and . > '%s']]",
										String.valueOf(cellIndex + 1)))
								.item(0));

				newCellData.setTextContent(cells.get(cellIndex));
			} else {
				Node attribute = cell.getOwnerDocument().createAttribute("ss:Type");
				attribute.setNodeValue("String");
				cell.getAttributes().setNamedItem(attribute);

				cell.setTextContent(cells.get(cellIndex));
			}
		}

		XML.saveToFile(workbook, Paths.get(fileName));
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
		Node sheetAttribute = sheet.getOwnerDocument().createAttribute("ss:ExpandedRowCount");
		sheetAttribute.setNodeValue(
				String.valueOf(sheet.getAttributes().getNamedItem("ss:ExpandedRowCount").getTextContent()) + 1);
		sheet.getAttributes().setNamedItem(sheetAttribute);

		Node row = XML.getChildNodesByXPath(sheet, "*[local-name()='Row']").item(rowIndex);

		Node newRow = XML.createChildElement(workbook, (Element) row, "Row");

		Node cell = null;
		Node cellData = null;

		for (int cellIndex = 0; cellIndex < cells.size(); cellIndex++) {
			cell = XML.createChildElement(workbook, (Element) newRow, "Cell");
			Node attribute = cell.getOwnerDocument().createAttribute("ss:Index");
			attribute.setNodeValue(String.valueOf(cellIndex + 1));
			cell.getAttributes().setNamedItem(attribute);

			cellData = XML.createChildElement(workbook, (Element) cell, "Data");

			attribute = cellData.getOwnerDocument().createAttribute("ss:Type");
			attribute.setNodeValue("String");
			cellData.getAttributes().setNamedItem(attribute);
			cellData.setTextContent(cells.get(cellIndex));

			newRow.appendChild(cell);
		}

		sheet.insertBefore(newRow, row);

		XML.saveToFile(workbook, Paths.get(fileName));
	}
}
