
package core.extjswidgets;

import java.util.ArrayList;

import org.kopitubruk.util.json.JSONParser;

import core.extjswidgets.utils.ExtJSHelper;
import core.utilities.Log;
import core.utilities.exceptions.AutomationException;

/**
 * The ExtJsList class is a wrapper class to allow centralized control of common
 * Ext Js web widget objects and methods.
 */
public class ExtJSList extends ExtJSWidget {

	/**
	 * List constructor to override object name with label variable.
	 *
	 * @param locator
	 *            locator ID object identifier
	 * @param label
	 *            the label to use in replace of the default label
	 */
	public ExtJSList(String locator, String label) {
		super(locator, label);
	}

	/**
	 * Default constructor for List object.
	 *
	 * @param locator
	 *            the object identifier locator
	 */
	public ExtJSList(String locator) {
		super(locator);
	}

	/**
	 * Selects row in the List by index.
	 *
	 * @param rowIndex
	 *            row number starting with 0
	 * @param keepExisting
	 *            true to retain existing selections
	 * @throws AutomationException
	 *             if could not select row
	 */
	public void selectByIndex(int rowIndex, boolean keepExisting) {
		Log.logScriptInfo(String.format("Select row #%s in %s", rowIndex, widgetInfo));

		try {
			getJsExecutor()
					.executeScript(String.format("Ext.getCmp('%s').select(%s, %s)", getId(), rowIndex, keepExisting));
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during selectByIndex(): %s", exception.getMessage()), exception);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Selects row in the List by value.
	 *
	 * @param value
	 *            text in row
	 * @param keepExisting
	 *            true to retain existing selections
	 * @throws AutomationException
	 *             if could not select row
	 */
	public void selectByValue(String value, boolean keepExisting) {
		Log.logScriptInfo(String.format("Selects row '%s' in %s", value, widgetInfo));
		String deSelectCmd = "";

		try {
			if (ExtJSHelper.getExtJsMajorVersion() == 3) {
				deSelectCmd = String.format("v.select(r, %s)", keepExisting);
			} else {
				deSelectCmd = String.format("v.getSelectionModel().select(r, %s)", keepExisting);
			}

			getJsExecutor().executeScript(String.format(
					"(function(){var v=Ext.getCmp('%s');var s=v.store; var r=s.findBy(function(record){var k=Object.keys(record.data);for(var key in k){if(record.data[k[key]]==='%s')return true}});%s})()",
					getId(), value, deSelectCmd));
		} catch (Exception exception) {
			throw new AutomationException(String.format("Error occurred during select(): %s", exception.getMessage()),
					exception);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * DeSelects row in the List.
	 *
	 * @param rowIndex
	 *            row number starting with 0
	 * @throws AutomationException
	 *             if could not select row
	 */
	public void deSelectByIndex(int rowIndex) {
		Log.logScriptInfo(String.format("DeSelects row #%s in %s", rowIndex, widgetInfo));

		try {
			getJsExecutor().executeScript(String.format("Ext.getCmp('%s').deselect(%s)", getId(), rowIndex));
		} catch (Exception exception) {
			throw new AutomationException(String.format("Error occurred during deselect(): %s", exception.getMessage()),
					exception);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * DeSelects row in the List by value.
	 *
	 * @param value
	 *            value of the desired list option to deSelect
	 * @throws AutomationException
	 *             if could not select row
	 */
	public void deSelectByValue(String value) {
		Log.logScriptInfo(String.format("DeSelects row '%s' in %s", value, widgetInfo));
		String deSelectCmd = "";

		try {
			if (ExtJSHelper.getExtJsMajorVersion() == 3) {
				deSelectCmd = "v.deselect(r)";
			} else {
				deSelectCmd = "v.getSelectionModel().deselect(r)";
			}

			getJsExecutor().executeScript(String.format(
					"(function(){var v=Ext.getCmp('%s');var s=v.store; var r=s.findBy(function(record){var k=Object.keys(record.data);for(var key in k){if(record.data[k[key]]==='%s')return true}});%s})()",
					getId(), escapeValue(value), deSelectCmd));
		} catch (Exception exception) {
			throw new AutomationException(String.format("Error occurred during deselect(): %s", exception.getMessage()),
					exception);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * DeSelects of all items in the List.
	 *
	 * @throws AutomationException
	 *             if could not select row
	 */
	public void deSelectAll() {
		Log.logScriptInfo(String.format("DeSelects of all items in the %s", widgetInfo));

		try {

			if (ExtJSHelper.getExtJsMajorVersion() == 3) {
				getJsExecutor().executeScript(String.format(
						"(function(){var l=Ext.getCmp('%s');var s=l.getSelectedIndexes();for(var i=0;i<s.length;i++){l.deselect(s[i])}})()",
						getId()));
			} else {
				getJsExecutor()
						.executeScript(String.format("Ext.getCmp('%s').getSelectionModel().deselectAll()", getId()));
			}
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during deselectAll(): %s", exception.getMessage()), exception);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Gets selected indexes in the List.
	 *
	 * @return list of selected items
	 * @throws AutomationException
	 *             if could not get selected indexes
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<String> getSelectedIndexes() {
		try {
			String jsFunction = "";

			if (ExtJSHelper.getExtJsMajorVersion() == 3) {
				jsFunction = String.format("return Ext.encode(Ext.getCmp('%s').getSelectedIndexes().map(String))",
						getId());
			} else {
				jsFunction = String.format(
						"return Ext.encode((function(){var a=new Array();var l=Ext.getCmp('%s');var s=l.getStore();l.getSelectionModel().selected.each(function(r){a.push(s.indexOf(r)+'')});return a.sort()})())",
						getId());
			}

			return (ArrayList<String>) JSONParser.parseJSON((String) getJsExecutor().executeScript(jsFunction));

		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during getSelectedIndexes(): %s", exception.getMessage()), exception);
		}
	}

	/**
	 * Gets text of the multiple selected items in a multi-select List object.
	 *
	 * @return a string list of selected items from the specified multi-select
	 *         List.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<String> getSelected() {
		try {
			String jsFunction = "";

			if (ExtJSHelper.getExtJsMajorVersion() == 3) {
				jsFunction = String.format(
						"return Ext.encode((function(){var a=new Array();var v=Ext.getCmp('%s');v.getSelectedIndexes().forEach(function(i){var r=new Array();var dr=v.store.getAt(i).data; for(var d in dr){r.push(dr[d])}a.push(r)});return a})())",
						getId());
			} else {
				jsFunction = String.format(
						"return Ext.encode((function(){var a=new Array();var v=Ext.getCmp('%s');v.getSelectionModel().selected.items.forEach(function(i){var r=new Array();var dr=i.data; for(var d in dr){r.push(dr[d])}a.push(r)});return a})())",
						getId());
			}

			return (ArrayList<String>) JSONParser.parseJSON((String) getJsExecutor().executeScript(jsFunction));

		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during getSelected(): %s", exception.getMessage()), exception);
		}
	}

	/**
	 * Gets the item count in the List.
	 *
	 * @return the count of items in the List
	 */
	public int getListItemCount() {
		try {
			return ((Long) getJsExecutor()
					.executeScript(String.format("return Ext.getCmp('%s').store.data.length", getId()))).intValue();
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during checking row selected: %s", exception.getMessage()),
					exception);
		}
	}

	/**
	 * Checks row selected in the List.
	 *
	 * @param rowIndex
	 *            row number starting with 0
	 * @return true - if row selected
	 * @throws AutomationException
	 *             if could not get selected indexes
	 */
	public Boolean isSelected(int rowIndex) {
		try {
			return (Boolean) getJsExecutor()
					.executeScript(String.format("return Ext.getCmp('%s').isSelected(%s)", getId(), rowIndex));

		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during checking row selected: %s", exception.getMessage()),
					exception);
		}
	}

	/**
	 * Selects row in the List.
	 *
	 * @param start
	 *            the index of the first node in the range
	 * @param end
	 *            the index of the last node in the range
	 * @param keepExisting
	 *            true to retain existing selections
	 * @throws AutomationException
	 *             if could not select row
	 */
	public void selectRange(int start, int end, boolean keepExisting) {
		Log.logScriptInfo(String.format("Select range (%s, %s) in %s", start, end, widgetInfo));

		try {
			getJsExecutor().executeScript(String.format("Ext.getCmp('%s').selectRange(%s, %s, %s)", getId(), start - 1,
					end - 1, keepExisting));
		} catch (Exception exception) {
			throw new AutomationException(String.format("Error occurred during select(): %s", exception.getMessage()),
					exception);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

}
