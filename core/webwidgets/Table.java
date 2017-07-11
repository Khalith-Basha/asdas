package core.webwidgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import core.utilities.Log;
import core.utilities.SeleniumCore;
import core.utilities.exceptions.AutomationException;

/**
 * The Table class provides wrapper methods pertaining to Table objects to allow
 * centralized control of Table web widget objects and methods.
 */
public class Table extends WebWidget {
	/**
	 * Table Constructor method.
	 *
	 * @param id
	 *            the locator ID or identifier for the table object
	 */
	public Table(String id) {
		super(id, "Table");
	}

	/**
	 * Table Constructor method to override object name with sLabel variable.
	 *
	 * @param id
	 *            the locator ID or identifier for the table object
	 * @param label
	 *            name to use for the object instead of the given object name
	 */
	public Table(String id, String label) {
		super(id, "Table", label);
	}

	// =================================
	// Webdriver Table methods
	// =================================

	/**
	 * Returns row headers as a List of String.
	 *
	 * @return List of String with row headers
	 */
	public ArrayList<String> getTableHeaders() {
		ArrayList<String> headers = new ArrayList<>();
		List<WebElement> listHeaders = getElement(sLocator).findElements(By.xpath(".//th"));
		if (listHeaders.size() != 0) {
			for (WebElement header : listHeaders) {
				String colspan = header.getAttribute("colspan");
				String spanCount = (colspan != null) ? colspan : header.getAttribute("rowspan");

				if (spanCount != null) {
					for (int i = 1; i <= Integer.parseInt(spanCount); i++) {
						headers.add(header.getText());
					}
				} else {
					headers.add(header.getText());
				}
			}

			return headers;
		}

		// Log.errorHandler(String.format("Error getting headers from table: %s.
		// Check if tags <th> exsists.", sLocator));

		return null;
	}

	/**
	 * Returns the count of rows in a specified table exclude headers.
	 *
	 * @return number of rows
	 */
	public int getTableRowCount() {
		try {
			return getElement(sLocator).findElements(By.xpath(".//tr[td]")).size();
		} catch (Exception e) {
			Log.errorHandler("getTableColumnCount() failed: " + e.getMessage());
			return 0;
		}
	}

	/**
	 * Returns column data data for a specific column number as a list array
	 * exclude headers.
	 *
	 * @param index
	 *            column number starting with 1
	 * @return list of column data
	 */
	public List<String> getTableColumnData(int index) {
		try {
			List<String> rowData = new ArrayList<>();

			List<WebElement> cells = getElement(sLocator).findElements(By.xpath(String.format(".//tr//td[%s]", index)));

			for (int cellIndex = 0; cellIndex < cells.size(); cellIndex++) {
				rowData.add(cells.get(cellIndex).getText());
			}

			return rowData;
		} catch (Exception e) {
			Log.errorHandler(String.format("Could not get column data from table %s", sLocator));

			return null;
		}
	}

	/**
	 * Returns column data data for a specific column number as a list array
	 * exclude headers.
	 *
	 * @param value
	 *            column to return
	 * @return list of column data
	 */
	public List<String> getTableColumnData(String value) {
		try {
			String xpath = String.format(".//td[count((//td[. = '%s'])[1]/preceding-sibling::*) + 1]", value);

			List<String> rowData = new ArrayList<>();

			List<WebElement> cells = getElement(sLocator).findElements(By.xpath(xpath));

			for (int cellIndex = 0; cellIndex < cells.size(); cellIndex++) {
				rowData.add(cells.get(cellIndex).getText());
			}

			return rowData;
		} catch (Exception e) {
			Log.errorHandler(String.format("Could not get column data from table %s", sLocator));

			return null;
		}
	}

	/**
	 * Searches table rows on specified column to acquire link for matching item
	 * name.
	 *
	 * @param columnDataMatch
	 *            name of the link to find
	 * @param columnOnly
	 *            column location containing item name and link
	 * @return String locator for link
	 */
	public String getTableColumnLink(String columnDataMatch, int columnOnly) {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			return SeleniumCore.getBrowser().getTableColumnLink(sLocator, columnDataMatch, columnOnly);
		}

		Log.errorHandler(getWidgetNotFoundMessage());
		return null;
	}

	/**
	 * Gets string reference for a table.
	 *
	 * @return string reference for table
	 */
	public String getTable() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			return SeleniumCore.getBrowser().getTableName(sLocator);
		}

		Log.errorHandler(getWidgetNotFoundMessage());
		return "No table found";
	}

	/**
	 * Gets the text from a cell of a table based on row and column number.
	 *
	 * @param rowIndex
	 *            row number
	 * @param columnIndex
	 *            column number
	 * @return the text from the specified cell
	 */
	public String getCellValue(int rowIndex, int columnIndex) {
		return getElement(sLocator).findElement(By.xpath(String.format(".//tr[td][%s]//td[%s]", rowIndex, columnIndex)))
				.getText();
	}

	/**
	 * Counts how many nodes match the specified xpath.
	 *
	 * @return the number of nodes that match the specified xpath used to count
	 *         number of rows in table
	 */
	@Override
	public int countXpaths() {
		return SeleniumCore.getBrowser().getXpathCount(sLocator).intValue();
	}

	/**
	 * Returns the count of columns in a specified table.
	 *
	 * @return number of columns
	 */
	public int getTableColumnCount() {
		try {
			List<WebElement> cells = getElement(sLocator).findElements(By.xpath(".//tr[td][1]//td"));

			int columnCount = 0;
			for (WebElement cell : cells) {
				String colspan = cell.getAttribute("colspan");
				if (colspan != null) {
					for (int i = 1; i <= Integer.parseInt(colspan); i++) {
						columnCount++;
					}
				} else {
					columnCount++;
				}
			}
			return columnCount;
		} catch (Exception e) {
			Log.errorHandler("getTableColumnCount() failed: " + e.getMessage());
			return 0;
		}
	}

	/**
	 * Returns row data from the specified row number as a List of String.
	 *
	 * @param index
	 *            row number to return the data from.
	 * @return Row data (excluding the row header) from the specified row number
	 *         as a List of String
	 */
	public List<String> getTableRowData(int index) {
		try {
			List<String> rowData = new ArrayList<>();

			List<WebElement> cells = getElement(sLocator)
					.findElements(By.xpath(String.format(".//tr[td][%s]//td", index)));
			// get entire row data
			for (int cellIndex = 0; cellIndex < cells.size(); cellIndex++) {
				rowData.add(cells.get(cellIndex).getText());
			}

			return rowData;
		} catch (Exception e) {
			Log.errorHandler("Error getting row data", e);

			return null;
		}
	}

	/**
	 * Returns row data from the specified row header name or row number as a
	 * List of String.
	 *
	 * @param rowValue
	 *            the row header name or row number to return the data from. For
	 *            Example, row header name can be something like "Account
	 *            Number" or it can be a specified row number expressed as a
	 *            string like this "5".
	 * @return Row data (excluding the row header) from the specified row header
	 *         or row number as a List of String
	 */
	public List<String> getTableRowData(String rowValue) {
		try {
			String xpath = String.format(".//tr//*[text()='%s']//ancestor::tr//td", rowValue);

			List<String> rowData = new ArrayList<>();

			List<WebElement> cells = getElement(sLocator).findElements(By.xpath(xpath));
			// get entire row data
			for (int cellIndex = 0; cellIndex < cells.size(); cellIndex++) {
				rowData.add(cells.get(cellIndex).getText());
			}

			return rowData;
		} catch (Exception e) {
			Log.errorHandler("Error getting row data", e);

			return null;
		}
	}

	/**
	 * This method is used to retrieve the row data as a hash map. The returned
	 * data is in hashmap or key=value format i.e. {"First Name"="Tony", "Last
	 * Name"="Johnson"}
	 *
	 * @param rowIndex
	 *            the row number to return the data from.
	 * @return Row data as hashmap or key=value format i.e. {"First
	 *         Name"="Tony", "Last Name"="Johnson"} from the specified row
	 *         number and given spreadsheet as a List of Strings
	 */
	public Map<String, String> getTableRowDataAsMap(int rowIndex) {
		try {
			List<String> rowDataList = getTableRowData(rowIndex);
			ArrayList<String> headers = getTableHeaders();

			HashMap<String, String> rowData = new LinkedHashMap<>();

			for (int cellIndex = 0; cellIndex < rowDataList.size(); cellIndex++) {
				String key = ((headers != null) ? headers.get(cellIndex) : String.format("Header %s", cellIndex + 1));

				rowData.put(key, rowDataList.get(cellIndex));
			}

			return rowData;
		} catch (Exception e) {
			Log.errorHandler("Error getting row data as Map", e);

			return null;
		}
	}

	/**
	 * Returns row data from the specified row header name or row number as a
	 * List of String.
	 *
	 * @param rowValue
	 *            the row header name or row number to return the data from. For
	 *            Example, row header name can be something like "Account
	 *            Number" or it can be a specified row number expressed as a
	 *            string like this "5".
	 * @return Row data from the specified row header or row number as a List of
	 *         String
	 */
	public Map<String, String> getTableRowDataAsMap(String rowValue) {
		try {
			List<String> rowDataList = getTableRowData(rowValue);
			ArrayList<String> headers = getTableHeaders();

			HashMap<String, String> rowData = new LinkedHashMap<>();

			for (int cellIndex = 0; cellIndex < rowDataList.size(); cellIndex++) {
				String key = ((headers != null) ? headers.get(cellIndex) : String.format("Header %s", cellIndex + 1));

				rowData.put(key, rowDataList.get(cellIndex));
			}

			return rowData;
		} catch (Exception e) {
			Log.errorHandler("Error getting row data as Map", e);

			return null;
		}
	}

	/**
	 * Gets entire content of the table as a list of map.
	 *
	 * @return List of HashMap of rows content.
	 */
	public List<Map<String, String>> getTableDataAsListOfMap() {
		try {
			List<Map<String, String>> data = new ArrayList<>();

			for (int rowIndex = 1; rowIndex <= getTableRowCount(); rowIndex++) {
				data.add(getTableRowDataAsMap(rowIndex));
			}

			return data;
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during getTableDataAsListOfMap(): %s", exception.getMessage()),
					exception);
		}
	}

	/**
	 * Gets entire content of the table as a multidimensional string array.
	 *
	 * @param returnHeaders
	 *            true to return header information in table output
	 * @return Array of rows content.
	 */
	public String[][] getTableDataAsArray(Boolean returnHeaders) {
		try {
			int rowsCount = getTableRowCount();
			int columnsCount = getTableColumnCount();

			String[][] data = new String[rowsCount + (returnHeaders ? 1 : 0)][columnsCount];

			if (returnHeaders) {
				List<String> headers = getTableHeaders();

				for (int headerIndex = 0; headerIndex < columnsCount; headerIndex++) {
					data[0][headerIndex] = (headers != null) ? headers.get(headerIndex) : "";
				}
			}

			for (int rowIndex = 1; rowIndex <= rowsCount; rowIndex++) {
				List<String> rowData = getTableRowData(rowIndex);

				for (int columnIndex = 0; columnIndex < columnsCount; columnIndex++) {
					data[rowIndex - (returnHeaders ? 0 : 1)][columnIndex] = rowData.get(columnIndex);
				}
			}

			return data;
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during getTableDataAsArray(): %s", exception.getMessage()),
					exception);
		}
	}

	/**
	 * Returns {@link WebElement} object of the Table element.
	 *
	 * @param id
	 *            the locator ID or identifier for the object
	 * @return {@link WebElement} object
	 */
	private WebElement getElement(String id) {
		try {
			return SeleniumCore.getBrowser().getElement(SeleniumCore.getBrowser().convertLocatorToBy(id),
					Log.AUTOMATION_WAIT_VALUE_10);
		} catch (Exception e) {
			Log.errorHandler("Error getting WebElement object of the table element.", e);
		}

		return null;
	}
	
	
	/**
	 * Clicks on dynamically generated table item based on specified item caption.
	 * @param caption
	 *            caption of a dynamically created table item to click
	 */
	public void clickTableLink(String caption) {
		SeleniumCore.getBrowser().click("//td[contains(.,'" + caption + "')]");
		Log.logScriptInfo(String.format("Click \"%s\" object", caption));
		}
	
	
	
}
