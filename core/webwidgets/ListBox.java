package core.webwidgets;

import java.util.ArrayList;

import core.utilities.Log;
import core.utilities.SeleniumCore;

/**
 * The ListBox class is a wrapper class to allow centralized control of common
 * web widget list objects and methods.
 */
public class ListBox extends WebWidget {
	/**
	 * Default constructor for ListBox object.
	 *
	 * @param id
	 *            the object identifier locator
	 */
	public ListBox(String id) {
		super(id, "ListBox");
	}

	/**
	 * ListBox constructor to override object name with sLabel variable.
	 *
	 * @param id
	 *            locator ID object identifier
	 * @param label
	 *            the label to use in replace of the default label
	 */
	public ListBox(String id, String label) {
		super(id, "ListBox", label);
	}

	/**
	 * Selects an item in a listbox object.
	 *
	 * @param item
	 *            item to select in the listbox
	 */
	public void select(String item) {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("Select item \"%s\" in %s", item, widgetInfo));
			SeleniumCore.getBrowser().select(sLocator, item);
		} else {
			Log.errorHandler(String.format("Error occurred selecting %s in %s: %s with locator of \"%s\"", item,
					sClassName, sIdentifier, sLocator));
		}
	}

	/**
	 * Selects an item in a listbox object.
	 *
	 * @param item
	 *            item to select in the listbox
	 */
	public void selectItem(String item) {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("Select item \"%s\" in %s", item, widgetInfo));
			SeleniumCore.getBrowser().selectByText(sLocator, item);
		} else {
			Log.errorHandler(String.format("Error occurred selecting %s in %s: %s with locator of \"%s\"", item,
					sClassName, sIdentifier, sLocator));
		}
	}

	/**
	 * Gets option value (value attribute) for selected option in the specified
	 * select element.
	 *
	 * @return the selected option value in the specified select drop-down
	 */
	public String getSelectedValue() {
		return SeleniumCore.getBrowser().getSelectedValue(sLocator);
	}

	/**
	 * Gets text of the selected item in a listbox object.
	 *
	 * @return selected item text from listbox element
	 */
	public String getSelectedItemText() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			return SeleniumCore.getBrowser().getSelectedItemText(sLocator);
		}

		Log.errorHandler(getWidgetNotFoundMessage());
		return "";
	}

	/**
	 * Gets value from text field type widget.
	 *
	 * @return returns Attribute value of calling object
	 */
	public String getValue() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			return SeleniumCore.getBrowser().getValue(sLocator);
		}

		Log.errorHandler(getWidgetNotFoundMessage());
		return "";
	}

	/**
	 * Checks if extjs combo box has any values or is null.
	 *
	 * @return true when combo box has any values or false when combo box has no
	 *         values
	 */
	public boolean isPopulated() {
		// if combo box has n items return true; otherwise, return false
		if (countXpaths() > 0) {
			return true;
		}

		return false;
	}

	/**
	 * Counts how many nodes match the specified xpath.
	 *
	 * @return the number of nodes that match the specified xpath, used to count
	 *         number of rows in combobox
	 */
	@Override
	public int countXpaths() {
		return SeleniumCore.getBrowser().getXpathCount(sLocator).intValue();
	}

	/**
	 * Gets the list values from the calling listbox or dropdown.
	 *
	 * @return the list of values
	 */
	public ArrayList<String> getListValues() {
		ArrayList<String> optionList = new ArrayList<>();
		int i = 1;
		try {
			// Because sLocator has the Location of the ListBox and not the
			// elements in it, hence "option" word has to be appended
			sLocator = sLocator + "//option";
			while (SeleniumCore.getBrowser().isElementPresent(sLocator + "[" + i + "]")) {
				String option = SeleniumCore.getBrowser().getText(sLocator + "[" + i + "]");
				// Log.logScriptInfo(option); // We need not record all the
				// values of the list drop down in Log
				optionList.add(option);
				i++;
			}
		} catch (Exception e) {
		}

		return optionList;
	}

	/**
	 * Gets the item count from the calling listbox or dropdown.
	 *
	 * @return the count of items in the calling listbox or dropdown
	 */
	public int getListItemCount() {
		return getListValues().size();
	}

	/**
	 * Returns all items in a specified listbox or dropdown list as a List
	 * Array.
	 *
	 * @return all items in a specified listbox or dropdown list as a List Array
	 */
	public ArrayList<String> getListItems() {
		ArrayList<String> optionList = new ArrayList<>();
		int i = 1;
		try {
			while (SeleniumCore.getBrowser().isElementPresent(sLocator + "[" + i + "]")) {
				String option = SeleniumCore.getBrowser().getText(sLocator + "[" + i + "]");
				// Log.logScriptInfo(option);
				optionList.add(option);
				i++;
			}
		} catch (Exception e) {
		}
		return optionList;
	}

	/**
	 * Selection performed by label value.
	 *
	 * @param label
	 *            the label used to locate the select option
	 */
	public void selectByLabel(String label) {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("Select item \"%s\" in %s", label, widgetInfo));
			try {
				SeleniumCore.getBrowser().selectByLabel(sLocator, label);
			} catch (Exception e) {
				Log.errorHandler(String.format("Error occurred selecting %s in %s: %s with locator of \"%s\" %s", label,
						sClassName, sIdentifier, sLocator, e.getMessage()));
			}
		} else {
			Log.errorHandler(String.format("Error occurred selecting %s in %s: %s with locator of \"%s\"", label,
					sClassName, sIdentifier, sLocator));
		}
	}

	/**
	 * Selection performed by partial label value.
	 *
	 * @param label
	 *            the label used to locate the select option
	 */
	public void selectByPartialLabel(String label) {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("Select item \"%s\" in %s", label, widgetInfo));
			try {
				SeleniumCore.getBrowser().selectByPartialLabel(sLocator, label);

			} catch (Exception e) {
				Log.errorHandler(String.format("Error occurred selecting %s in %s: %s with locator of \"%s\" %s", label,
						sClassName, sIdentifier, sLocator, e.getMessage()));
			}
		} else {
			Log.errorHandler(String.format("Error occurred selecting %s in %s: %s with locator of \"%s\"", label,
					sClassName, sIdentifier, sLocator));
		}
	}

	/**
	 * Selection performed by Index value.
	 *
	 * @param index
	 *            the Index value used to locate the select option
	 */
	public void selectByIndex(String index) {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("Select item \"%s\" in %s", index, widgetInfo));
			try {
				SeleniumCore.getBrowser().selectByIndex(sLocator, Integer.parseInt(index));
			} catch (Exception e) {
				Log.errorHandler(String.format("Error occurred selecting %s in %s: %s with locator of \"%s\" %s", index,
						sClassName, sIdentifier, sLocator, e.getMessage()));
			}
		} else {
			Log.errorHandler(String.format("Error occurred selecting %s in %s: %s with locator of \"%s\"", index,
					sClassName, sIdentifier, sLocator));
		}
	}

	/**
	 * Gets list item at specified index.
	 *
	 * @param index
	 *            is the Index value used to locate the list option
	 * @return list option as String
	 */
	public String get(int index) {
		if (SeleniumCore.getBrowser().exists(this.sLocator)) {
			sLocator = sLocator + "//option";
			try {
				return SeleniumCore.getBrowser().getText(sLocator + "[" + index + "]");

			} catch (Exception e) {
				Log.errorHandler(String.format(
						"Error occurred getting List option for the number %s item in the %s: %s with locator of \"%s\" %s",
						index, sClassName, sIdentifier, sLocator, e.getMessage()));

				return "";
			}
		}

		return "";
	}

	/**
	 * DeSelection performed by label value.
	 *
	 * @param label
	 *            the label used to locate the select option
	 */
	public void deSelectByLabel(String label) {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("Deselect item \"%s\" in %s", label, widgetInfo));
			try {
				SeleniumCore.getBrowser().deSelectByLabel(sLocator, label);
			} catch (Exception e) {
				Log.errorHandler(String.format("Error occurred deselecting %s in %s: %s with locator of \"%s\"", label,
						sClassName, sIdentifier, sLocator), e);
			}
		} else {
			Log.errorHandler(String.format("Error occurred deselecting %s in %s: %s with locator of \"%s\"", label,
					sClassName, sIdentifier, sLocator));
		}
	}

	/**
	 * DeSelection performed by partial label value.
	 *
	 * @param label
	 *            the label used to locate the select option
	 */
	public void deSelectByPartialLabel(String label) {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("Deselect item \"%s\" in %s", label, widgetInfo));
			try {
				SeleniumCore.getBrowser().deSelectByPartialLabel(sLocator, label);
			} catch (Exception e) {
				Log.errorHandler(String.format("Error occurred deselecting %s in %s: %s with locator of \"%s\"", label,
						sClassName, sIdentifier, sLocator), e);
			}
		} else {
			Log.errorHandler(String.format("Error occurred deselecting %s in %s: %s with locator of \"%s\"", label,
					sClassName, sIdentifier, sLocator));
		}
	}

	/**
	 * DeSelection performed by Index value of the desired list option.
	 *
	 * @param index
	 *            Index value of the desired list option to select
	 */
	public void deSelectByIndex(int index) {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("Deselect item \"%s\" in %s", index, widgetInfo));
			try {
				SeleniumCore.getBrowser().deSelectByIndex(sLocator, index);
			} catch (Exception e) {
				Log.errorHandler(String.format("Error occurred deselecting %s in %s: %s with locator of \"%s\"", index,
						sClassName, sIdentifier, sLocator), e);
			}
		} else {
			Log.errorHandler(String.format("Error occurred deselecting %s in %s: %s with locator of \"%s\"", index,
					sClassName, sIdentifier, sLocator));
		}
	}

	/**
	 * DeSelection performed by value of the desired list option.
	 *
	 * @param value
	 *            value of the desired list option to select
	 */
	public void deSelectByValue(String value) {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("Deselect item \"%s\" in %s", value, widgetInfo));
			try {
				SeleniumCore.getBrowser().deSelectByValue(sLocator, value);
			} catch (Exception e) {
				Log.errorHandler(String.format("Error occurred deselecting %s in %s: %s with locator of \"%s\"", value,
						sClassName, sIdentifier, sLocator), e);
			}
		} else {
			Log.errorHandler(String.format("Error occurred deselecting %s in %s: %s with locator of \"%s\"", value,
					sClassName, sIdentifier, sLocator));
		}
	}

	/**
	 * DeSelection of all items in list.
	 */
	public void deSelectAll() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("Deselect all items in the list: %s", widgetInfo));
			try {
				SeleniumCore.getBrowser().deSelectAll(sLocator);
			} catch (Exception e) {
				Log.errorHandler(
						String.format("Error occurred deselecting all items in the %s: %s with locator of \"%s\"",
								sClassName, sIdentifier, sLocator),
						e);
			}
		} else {
			Log.errorHandler(String.format("Error occurred deselecting all items in the %s: %s with locator of \"%s\"",
					sClassName, sIdentifier, sLocator));
		}
	}

	/**
	 * Gets text of the multiple selected items in a multi-select listbox
	 * object.
	 *
	 * @return a string list of selected items from the specified multi-select
	 *         listbox.
	 */
	public ArrayList<String> getAllSelectedOptions() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			try {
				return SeleniumCore.getBrowser().getAllSelectedOptions(sLocator);
			} catch (Exception e) {
				Log.errorHandler(
						String.format("Error occurred getting all selected items in the %s: %s with locator of \"%s\"",
								sClassName, sIdentifier, sLocator),
						e);
				return null;
			}
		}

		Log.errorHandler(String.format("Error could not find object %s: %s with locator of \"%s\"", sClassName,
				sIdentifier, sLocator));
		return null;
	}
}
