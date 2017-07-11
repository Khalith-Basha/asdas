
package core.extjswidgets;

import java.util.ArrayList;

import org.kopitubruk.util.json.JSONParser;

import core.extjswidgets.utils.ExtJSHelper;
import core.utilities.Log;
import core.utilities.SeleniumCore;
import core.utilities.exceptions.AutomationException;

/**
 * The ComboBox class is a wrapper class to allow centralized control of common
 * Ext Js web widget objects and methods.
 */
public class ExtJSComboBox extends ExtJSWidget {
	/**
	 * ComboBox constructor to override object name with label variable.
	 *
	 * @param locator
	 *            locator ID object identifier
	 * @param label
	 *            the label to use in replace of the default label
	 */
	public ExtJSComboBox(String locator, String label) {
		super(locator, label);
	}

	/**
	 * Default constructor for ComboBox object.
	 *
	 * @param locator
	 *            the object identifier locator
	 */
	public ExtJSComboBox(String locator) {
		super(locator);
	}

	/**
	 * Select option by value text.
	 *
	 * @param value
	 *            option text
	 * @throws AutomationException
	 *             if could not select item
	 */
	public void select(String value) {
		Log.logScriptInfo(String.format("Select option \"%s\" in %s", value, widgetInfo));

		if (ExtJSHelper.getExtJsMajorVersion() == 3) {
			Boolean result = (Boolean) getJsExecutor().executeScript(String.format(
					"return (function(){var c=Ext.getCmp('%s');var s=c.store;var i=s.find(c.displayField,'%s');c.selectedIndex=i;if (i>0){c.setValue('%2$s');c.fireEvent('select',c,s.getAt(i),i);return true}else{return false}})()",
					getId(), escapeValue(value)));
			if (!result) {
				throw new AutomationException(
						String.format("Failed to find ComboBox item by value text: \"%s\"", value));
			}
		} else {
			String jsFunction = String.format(
					"return Ext.getCmp('%s').findRecordByValue('%s').get(Ext.ComponentMgr.get('%s').valueField)",
					getId(), escapeValue(value), getId());

			Object componentValue = escapeValue((String) SeleniumCore.jsExecutor.executeScript(jsFunction));
			if (componentValue != null) {
				getJsExecutor().executeScript(
						String.format("(function(){var c=Ext.getCmp('%s');c.select(c.findRecordByValue('%s'))})()",
								getId(), componentValue));

				getJsExecutor().executeScript(String.format(
						"(function(){var c=Ext.getCmp('%s');var r=c.findRecordByValue('%s');if (r) {var i=c.store.indexOf(r);c.fireEvent('select',c,r,i)}})()",
						getId(), componentValue));
			} else {
				throw new AutomationException(
						String.format("Failed to find ComboBox item by value text: \"%s\"", value));
			}
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Select option by option index.
	 *
	 * @param index
	 *            option index
	 * @throws AutomationException
	 *             if could not select item
	 */
	public void select(int index) {
		Log.logScriptInfo(String.format("Select item #%s in %s", index, widgetInfo));

		try {
			if (ExtJSHelper.getExtJsMajorVersion() == 3) {
				getJsExecutor().executeScript(String.format(
						"(function(){var c=Ext.getCmp('%s');var s=c.store;c.selectedIndex=%2$s;c.setValue(s.getAt(%s).data[c.displayField]);c.fireEvent('select',c,s.getAt(%2$s),%2$s);})()",
						getId(), index));
			} else {
				getJsExecutor().executeScript(String.format(
						"(function(){var c=Ext.getCmp('%s');var s=c.store;c.select(s.getAt(%s).data[c.displayField]);c.fireEvent('select',c,s.getAt(%2$s),%2$s);})()",
						getId(), index));
			}
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during select combobox item: %s", exception.getMessage()), exception);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Gets text from text field type widget.
	 *
	 * @return String returns text value of calling object
	 * @throws AutomationException
	 *             if could not get text of combobox
	 */
	@Override
	public String getText() {
		try {
			return (String) getJsExecutor()
					.executeScript(String.format("return Ext.getCmp('%s').getRawValue()", getId()));
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during getting text: %s", exception.getMessage()), exception);
		}
	}

	/**
	 * Sets the specified value into the field. If the value finds a match, the
	 * corresponding record text will be displayed in the field. This method not
	 * fire any events.
	 *
	 * @param value
	 *            text to enter
	 * @throws AutomationException
	 *             if could not set text
	 */
	public void setText(String value) {
		Log.logScriptInfo(String.format("Set text \"%s\" in %s", value, widgetInfo));

		try {
			getJsExecutor()
					.executeScript(String.format("Ext.getCmp('%s').setValue('%s')", getId(), escapeValue(value)));
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during setting text: %s", exception.getMessage()), exception);

		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Expands the dropdown list if it is currently hidden. Fires the expand
	 * event on completion.
	 *
	 * @throws AutomationException
	 *             if could not expand
	 */
	public void expand() {
		Log.logScriptInfo(String.format("Expand %s", widgetInfo));

		try {
			getJsExecutor().executeScript(String.format("Ext.getCmp('%s').expand()", getId()));
		} catch (Exception exception) {
			throw new AutomationException(String.format("Error occurred during expanding: %s", exception.getMessage()),
					exception);
		}
	}

	/**
	 * Hides the dropdown list if it is currently expanded. Fires the collapse
	 * event on completion.
	 *
	 * @throws AutomationException
	 *             if could not collapse
	 */
	public void collapse() {
		Log.logScriptInfo(String.format("Collapse %s", widgetInfo));

		try {
			getJsExecutor().executeScript(String.format("Ext.getCmp('%s').collapse()", getId()));
		} catch (Exception exception) {
			throw new AutomationException(String.format("Error occurred during collapsing: %s", exception.getMessage()),
					exception);
		}
	}

	/**
	 * Gets list of values.
	 *
	 * @return values list
	 * @throws AutomationException
	 *             if could not get list of values
	 */
	@SuppressWarnings({ "unchecked" })
	public ArrayList<String> getListValues() {
		try {
			final String jsFunction = String.format(
					"return Ext.encode((function(){var e=new Array();var c=Ext.getCmp('%s');c.store.each(function(r){e.push(r.data[c.displayField])});return e})())",
					getId());

			return (ArrayList<String>) JSONParser.parseJSON((String) getJsExecutor().executeScript(jsFunction));
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during getting list of items: %s", exception.getMessage()),
					exception);
		}
	}

	/**
	 * Resets the current field value to the originally-loaded value and clears
	 * any validation messages
	 *
	 * @throws AutomationException
	 *             if could not reset
	 */
	public void reset() {
		try {
			getJsExecutor().executeScript(String.format("Ext.getCmp('%s').reset()", getId()));
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during getting list of items: %s", exception.getMessage()),
					exception);
		}
	}
}
