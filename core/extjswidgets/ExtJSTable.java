
package core.extjswidgets;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.kopitubruk.util.json.JSONParser;

import core.extjswidgets.utils.ExtJSHelper;
import core.utilities.Log;
import core.utilities.exceptions.AutomationException;

/**
 * The Table class is a wrapper class to allow centralized control of common Ext
 * Js web widget objects and methods.
 */
public class ExtJSTable extends ExtJSWidget {
	/**
	 * Default constructor for Table object.
	 *
	 * @param locator
	 *            the object identifier locator
	 */
	public ExtJSTable(String locator) {
		super(locator);
	}

	/**
	 * Table constructor to override object name with label variable.
	 *
	 * @param locator
	 *            locator ID object identifier
	 * @param label
	 *            the label to use in replace of the default label
	 */
	public ExtJSTable(String locator, String label) {
		super(locator, label);
	}

	/**
	 * Returns row headers as a List of String.
	 *
	 * @return List of String with row headers
	 * @throws AutomationException
	 *             if could not get Table headers
	 */
	@SuppressWarnings({ "unchecked" })
	public ArrayList<String> getTableHeaders() {
		try {
			String jsFunction = "";

			if (ExtJSHelper.getExtJsMajorVersion() == 3) {
				jsFunction = String.format(
						"return Ext.encode((function(){var e=new Array();var t=Ext.getCmp('%s').getColumnModel().getColumnsBy(function(c){return !c.hidden;});for(var i=0;i<t.length;i++){e.push(t[i].header);};return e;})())",
						getId());
			} else {
				jsFunction = String.format(
						"return Ext.encode((function(){var e=new Array();var t=Ext.getCmp('%s').headerCt.getVisibleGridColumns();for(var i=0;i<t.length;i++){e.push(t[i].text)};return e;})())",
						getId());
			}
			return (ArrayList<String>) JSONParser.parseJSON((String) getJsExecutor().executeScript(jsFunction));
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during getTableHeaders(): %s", exception.getMessage()), exception);
		}
	}

	/**
	 * Returns the count of rows in a specified table exclude headers.
	 *
	 * @param visible
	 *            - if 'true' the method return only visible row count
	 * @return number of rows
	 * @throws AutomationException
	 *             if could not get row count
	 */
	public Long getTableRowCount(Boolean visible) {
		try {
			return (Long) getJsExecutor().executeScript(String.format("return Ext.getCmp('%s').getStore().get%sCount()",
					getId(), (visible ? "" : "Total")));
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during getTableRowCount(): %s", exception.getMessage()), exception);
		}
	}

	/**
	 * Returns column data data for a specific column number as a list array
	 * exclude headers.
	 *
	 * @param header
	 *            column name
	 * @return list of column data
	 * @throws AutomationException
	 *             if could not get data from all rows in given column name
	 */
	@SuppressWarnings({ "unchecked" })
	public ArrayList<String> getTableColumnData(String header) {
		try {
			String jsFunction = "";

			if (ExtJSHelper.getExtJsMajorVersion() == 3) {
				jsFunction = String.format(
						"return Ext.encode((function(){var e=new Array();var g=Ext.ComponentMgr.get('%s');var cid=g.getColumnModel().getColumnsBy(function(c){return !c.hidden&&c.header=='%s'})[0].dataIndex;g.store.each(function(r){e.push(r.get(cid))});return e})())",
						getId(), escapeValue(header));
			} else {
				jsFunction = String.format(
						"return Ext.encode((function(){var e=new Array();var g=Ext.getCmp('%s');var cid=g.down('[text=%s]').dataIndex;g.store.each(function(r){e.push(r.get(cid))});return e})())",
						getId(), escapeValue(header));
			}

			return (ArrayList<String>) JSONParser.parseJSON((String) getJsExecutor().executeScript(jsFunction));
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during getTableColumnData(): %s", exception.getMessage()), exception);
		}
	}

	/**
	 * Returns column data data for a specific column number as a list array
	 * exclude headers.
	 *
	 * @param index
	 *            column number starting with 1
	 * @return list of column data
	 * @throws AutomationException
	 *             if could not get data from all rows in given column index
	 */
	@SuppressWarnings({ "unchecked" })
	public ArrayList<String> getTableColumnData(int index) {
		try {
			String jsFunction = "";

			if (ExtJSHelper.getExtJsMajorVersion() == 3) {
				jsFunction = String.format(
						"return Ext.encode((function(){var e=new Array();var g=Ext.ComponentMgr.get('%s');var cid=g.getColumnModel().getColumnsBy(function(c){return !c.hidden})[%d].dataIndex;g.store.each(function(record){e.push(record.get(cid))});return e})())",
						getId(), index - 1);
			} else {
				jsFunction = String.format(
						"return Ext.encode((function(){var e=new Array();var g=Ext.getCmp('%s');var cid=g.headerCt.getVisibleGridColumns()[%d].dataIndex;g.store.each(function(record){e.push(record.get(cid))});return e})())",
						getId(), index - 1);
			}

			return (ArrayList<String>) JSONParser.parseJSON((String) getJsExecutor().executeScript(jsFunction));
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during getTableColumnData(): %s", exception.getMessage()), exception);
		}
	}

	/**
	 * Returns row data from the specified row number as a List of String
	 *
	 * @param index
	 *            row number to return the data from. Starting with 1
	 * @return Row data (excluding the row header) from the specified row number
	 *         as a List of String
	 * @throws AutomationException
	 *             if could not get data from row
	 */
	@SuppressWarnings({ "unchecked" })
	public ArrayList<String> getTableRowData(int index) {
		try {
			String jsFunction = "";

			if (ExtJSHelper.getExtJsMajorVersion() == 3) {
				jsFunction = String.format(
						"return Ext.encode((function(){var e= Array();var g=Ext.ComponentMgr.get('%s');var cn=g.getColumnModel().getColumnsBy(function(c){return !c.hidden});for (var i=0;i<cn.length;i++){e.push(g.getStore().getAt(%d).get(cn[i].dataIndex)+'')};return e})())",
						getId(), index - 1);
			} else {
				jsFunction = String.format(
						"return Ext.encode((function(){var e= Array();var g=Ext.getCmp('%s');var cn=g.headerCt.getVisibleGridColumns();for (var i=0;i<cn.length;i++){e.push(g.getStore().getAt(%d).get(cn[i].dataIndex)+'')};return e})())",
						getId(), index - 1);
			}

			return (ArrayList<String>) JSONParser.parseJSON((String) getJsExecutor().executeScript(jsFunction));
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during getTableRowData(): %s", exception.getMessage()), exception);
		}
	}

	/**
	 * This method is used to retrieve the row data as a hash map. The returned
	 * data is in hashmap or key=value format i.e. {"First Name"="Tony", "Last
	 * Name"="Johnson"}
	 *
	 * @param index
	 *            the row number to return the data from. Starting with 1
	 * @return Row data as hashmap or key=value format i.e. {"First
	 *         Name"="Tony", "Last Name"="Johnson"} from the specified row
	 *         number and given spreadsheet as a List of Strings
	 * @throws AutomationException
	 *             if could not get data from row
	 */
	@SuppressWarnings({ "unchecked" })
	public LinkedHashMap<String, String> getTableRowDataAsMap(int index) {
		try {
			String jsFunction = "";

			if (ExtJSHelper.getExtJsMajorVersion() == 3) {
				jsFunction = String.format(
						"return Ext.encode((function(){var j={};var g=Ext.ComponentMgr.get('%s');var cn=g.getColumnModel().getColumnsBy(function(c){return !c.hidden});for (var i=0;i<cn.length;i++){j[cn[i].header]=g.getStore().getAt(%s).get(cn[i].dataIndex)+''};return j})())",
						getId(), index - 1);
			} else {
				jsFunction = String.format(
						"return Ext.encode((function(){var j={};var g=Ext.getCmp('%s');var cn=g.headerCt.getVisibleGridColumns();for (var i=0;i<cn.length;i++){j[cn[i].text]=g.getStore().getAt(%d).get(cn[i].dataIndex)+''};return j})())",
						getId(), index - 1);
			}

			return (LinkedHashMap<String, String>) JSONParser
					.parseJSON((String) getJsExecutor().executeScript(jsFunction));
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during getTableRowDataAsMap(): %s", exception.getMessage()),
					exception);
		}
	}

	/**
	 * Gets the text from a cell of a table based on row and column number.
	 *
	 * @param rowIndex
	 *            row number starting with 1
	 * @param columnIndex
	 *            column number starting with 1
	 * @return the text from the specified cell
	 * @throws AutomationException
	 *             if could not get cell value
	 */
	public String getCellValue(int rowIndex, int columnIndex) {
		try {
			String jsFunction = "";

			if (ExtJSHelper.getExtJsMajorVersion() == 3) {
				jsFunction = String.format("return Ext.ComponentMgr.get('%s').getView().getCell(%s,%s).textContent",
						getId(), rowIndex - 1, columnIndex - 1);
			} else {
				jsFunction = String.format(
						"return (function(){var g=Ext.getCmp('%s');return g.store.getAt(%d).get(g.headerCt.getVisibleGridColumns()[%d].dataIndex)})()",
						getId(), rowIndex - 1, columnIndex - 1);
			}

			return (String) getJsExecutor().executeScript(jsFunction);
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during getCellValue(): %s", exception.getMessage()), exception);
		}
	}

	/**
	 * Gets entire content of the table as a list of map.
	 *
	 * @return List of HashMap of rows content.
	 * @throws AutomationException
	 *             if could not get data from table
	 */
	@SuppressWarnings({ "unchecked" })
	public List<Map<String, String>> getTableDataAsListOfMap() {
		ArrayList<Map<String, String>> tableData = new ArrayList<>();
		try {
			String jsFunction = "";

			if (ExtJSHelper.getExtJsMajorVersion() == 3) {
				jsFunction = String.format(
						"return (function(){var e=new Array();var g=Ext.ComponentMgr.get('%s');var cn=g.getColumnModel().getColumnsBy(function(c){return !c.hidden});for(var i=0;i<g.getStore().getCount();i++){e.push((function(){var js={};for (var j=0;j<cn.length;j++){js[cn[j].header]=g.getStore().getAt(i).get(cn[j].dataIndex)+''};return Ext.encode(js)})())}return e})()",
						getId());
			} else {
				jsFunction = String.format(
						"return (function(){var e=new Array();var g=Ext.getCmp('%s');var cn=g.headerCt.getVisibleGridColumns();for(var i=0;i<g.getStore().getCount();i++){e.push((function(){var jj={};for (var j=0;j<cn.length;j++){jj[cn[j].text]=g.getStore().getAt(i).get(cn[j].dataIndex)+''}return Ext.encode(jj)})())}return e})()",
						getId());
			}
			ArrayList<String> arrayOfJSONRows = (ArrayList<String>) getJsExecutor().executeScript(jsFunction);

			for (String jsonRow : arrayOfJSONRows) {
				tableData.add((LinkedHashMap<String, String>) JSONParser.parseJSON(jsonRow));
			}

			return tableData;
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during getTableDataAsListOfMap(): %s", exception.getMessage()),
					exception);
		}
	}

	/**
	 * Selects row in the Table.
	 *
	 * @param rowIndex
	 *            row number starting with 1
	 * @throws AutomationException
	 *             if could not select row
	 */
	public void selectRow(int rowIndex) {
		Log.logScriptInfo(String.format("Select row #%s in %s", rowIndex, widgetInfo));

		try {
			String jsFunction = "";

			if (ExtJSHelper.getExtJsMajorVersion() == 3) {
				jsFunction = String.format("Ext.ComponentMgr.get('%s').getSelectionModel().selectRow(%s)", getId(),
						rowIndex - 1);
			} else {
				jsFunction = String.format("Ext.getCmp('%s').getSelectionModel().select(%s)", getId(), rowIndex - 1);
			}

			getJsExecutor().executeScript(jsFunction);
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during selectRow(): %s", exception.getMessage()), exception);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Clicks on cell.
	 *
	 * @param rowIndex
	 *            row number starting with 1
	 * @param columnIndex
	 *            column number starting with 1
	 * @throws AutomationException
	 *             if could not click on cell
	 */
	public void clickCell(int rowIndex, int columnIndex) {
		Log.logScriptInfo(String.format("Click on cell (%s, %s) in %s", rowIndex, columnIndex, widgetInfo));

		try {
			String jsFunction = "";

			if (ExtJSHelper.getExtJsMajorVersion() == 3) {

				jsFunction = String.format(
						"(function(){var g=Ext.ComponentMgr.get('%s');g.fireEvent('cellclick',g, %s, %s)})()", getId(),
						rowIndex - 1, columnIndex - 1);
			} else {
				jsFunction = String.format(
						"(function(){var g=Ext.getCmp('%s');var r=g.getStore().getAt(%s);var v=g.getView();var tr=v.getNode(%2$s);var td=Ext.query('td', tr)[%s];v.fireEvent('cellclick',v, td,%3$s, r, tr, %2$s)})()",
						getId(), rowIndex - 1, columnIndex - 1);
			}
			getJsExecutor().executeScript(jsFunction);
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during clickCell(): %s", exception.getMessage()), exception);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Double clicks on cell.
	 *
	 * @param rowIndex
	 *            row number starting with 1
	 * @param columnIndex
	 *            column number starting with 1
	 * @throws AutomationException
	 *             if could not double click on cell
	 */
	public void doubleClickCell(int rowIndex, int columnIndex) {
		Log.logScriptInfo(String.format("Double click on cell (%s, %s) in %s", rowIndex, columnIndex, widgetInfo));

		try {
			String jsFunction = "";

			if (ExtJSHelper.getExtJsMajorVersion() == 3) {

				jsFunction = String.format(
						"(function(){var g=Ext.ComponentMgr.get('%s');g.fireEvent('celldblclick',g, %s, %s)})()",
						getId(), rowIndex - 1, columnIndex - 1);
			} else {
				jsFunction = String.format(
						"(function(){var g=Ext.getCmp('%s');var r=g.getStore().getAt(%s);var v=g.getView();var tr=v.getNode(%2$s);var td=Ext.query('td', tr)[%s];v.fireEvent('celldblclick',v, td,%3$s, r, tr, %2$s)})()",
						getId(), rowIndex - 1, columnIndex - 1);
			}
			getJsExecutor().executeScript(jsFunction);
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during doubleClickCell(): %s", exception.getMessage()), exception);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Gets id of the ExtJs widget in the grid cell.
	 *
	 * @param rowIndex
	 *            row number starting with 1
	 * @param columnIndex
	 *            column number starting with 1
	 * @return id of the element
	 * @throws AutomationException
	 *             if could not get id of cell editor
	 */
	public String getCellEditorId(int rowIndex, int columnIndex) {
		try {
			clickCell(rowIndex, columnIndex);
			String jsFunction = "";

			if (ExtJSHelper.getExtJsMajorVersion() == 3) {
				jsFunction = String.format("return Ext.ComponentMgr.get('%s').editingPlugin.getActiveEditor().field.id",
						getId());
			} else {
				jsFunction = String.format("return Ext.getCmp('%s').getSelectionModel().grid.activeEditor.field.id",
						getId());
			}

			return (String) getJsExecutor().executeScript(jsFunction);
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during getCellEditorId(): %s", exception.getMessage()), exception);
		}
	}

	/**
	 * Gets the active page.
	 *
	 * @return number ot the active page
	 */
	public String getCellCurrentPage() {
		try {
			String jsFunction = "";

			if (ExtJSHelper.getExtJsMajorVersion() == 3) {
				jsFunction = String.format("return Ext.ComponentMgr.get('%s').getBottomToolbar().inputItem.startValue",
						getId());
			} else {
				jsFunction = String.format("return Ext.getCmp('%s').getStore().currentPage", getId());
			}

			return (String) getJsExecutor().executeScript(jsFunction);
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during getCellEditorId(): %s", exception.getMessage()), exception);
		}
	}

	/**
	 * Moves to the first page, has the same effect as clicking the 'first'
	 * button.
	 *
	 * @throws AutomationException
	 *             if could not move to the first page
	 */
	public void moveFirst() {
		Log.logScriptInfo(String.format("Move to the first page in %s", widgetInfo));

		try {
			String jsFunction = "";

			if (ExtJSHelper.getExtJsMajorVersion() == 3) {
				jsFunction = String.format("Ext.ComponentMgr.get('%s').getBottomToolbar().moveFirst()", getId());
			} else {
				jsFunction = String.format("Ext.getCmp('%s').getDockedItems('toolbar[dock=bottom]')[0].moveFirst()",
						getId());
			}
			getJsExecutor().executeScript(jsFunction);
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during moveFirst(): %s", exception.getMessage()), exception);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Moves to the last page, has the same effect as clicking the 'last'
	 * button.
	 *
	 * @throws AutomationException
	 *             if could not move to the last page
	 */
	public void moveLast() {
		Log.logScriptInfo(String.format("Move to the last page in %s", widgetInfo));

		try {
			String jsFunction = "";

			if (ExtJSHelper.getExtJsMajorVersion() == 3) {
				jsFunction = String.format("Ext.ComponentMgr.get('%s').getBottomToolbar().moveLast()", getId());
			} else {
				jsFunction = String.format("Ext.getCmp('%s').getDockedItems('toolbar[dock=bottom]')[0].moveLast()",
						getId());
			}
			getJsExecutor().executeScript(jsFunction);
		} catch (Exception exception) {
			throw new AutomationException(String.format("Error occurred during moveLast(): %s", exception.getMessage()),
					exception);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Moves to the next page, has the same effect as clicking the 'next'
	 * button.
	 *
	 * @throws AutomationException
	 *             if could not move to the next page
	 */
	public void moveNext() {
		Log.logScriptInfo(String.format("Move to the next page in %s", widgetInfo));

		try {
			String jsFunction = "";

			if (ExtJSHelper.getExtJsMajorVersion() == 3) {
				jsFunction = String.format("Ext.ComponentMgr.get('%s').getBottomToolbar().moveNext()", getId());
			} else {
				jsFunction = String.format("Ext.getCmp('%s').getDockedItems('toolbar[dock=bottom]')[0].moveNext()",
						getId());
			}
			getJsExecutor().executeScript(jsFunction);
		} catch (Exception exception) {
			throw new AutomationException(String.format("Error occurred during moveNext(): %s", exception.getMessage()),
					exception);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Moves to the previous page, has the same effect as clicking the 'next'
	 * button.
	 *
	 * @throws AutomationException
	 *             if could not move to the previous page
	 */
	public void movePrevious() {
		Log.logScriptInfo(String.format("Move to the previous page in %s", widgetInfo));

		try {
			String jsFunction = "";

			if (ExtJSHelper.getExtJsMajorVersion() == 3) {
				jsFunction = String.format("Ext.ComponentMgr.get('%s').getBottomToolbar().movePrevious()", getId());
			} else {
				jsFunction = String.format("Ext.getCmp('%s').getDockedItems('toolbar[dock=bottom]')[0].movePrevious()",
						getId());
			}
			getJsExecutor().executeScript(jsFunction);
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during movePrevious(): %s", exception.getMessage()), exception);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Changes the active page.
	 *
	 * @param pageNumber
	 *            the page to display
	 */
	public void setPage(int pageNumber) {
		Log.logScriptInfo(String.format("Chenge the active page to #%s in %s", pageNumber, widgetInfo));

		try {
			String jsFunction = "";

			if (ExtJSHelper.getExtJsMajorVersion() == 3) {
				jsFunction = String.format("Ext.ComponentMgr.get('%s').getBottomToolbar().changePage(%s)", getId(),
						pageNumber);
			} else {
				jsFunction = String.format(
						"(function(){var g=Ext.getCmp('%s'); g.getStore().currentPage=%s; g.getDockedItems('toolbar[dock=bottom]')[0].doRefresh()})()",
						getId(), pageNumber);
			}
			getJsExecutor().executeScript(jsFunction);
		} catch (Exception exception) {
			throw new AutomationException(String.format("Error occurred during setPage(): %s", exception.getMessage()),
					exception);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

}
