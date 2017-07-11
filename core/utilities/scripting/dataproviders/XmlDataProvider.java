package core.utilities.scripting.dataproviders;

import core.utilities.Excel;
import core.utilities.exceptions.SetupException;

/**
 * Class represents provider of data from xml file.
 */
public class XmlDataProvider {
	/**
	 * Processes data from xml file for test script.
	 *
	 * @param file xml file to get data from
	 * @param sheet xml file sheet to get data from
	 * @return content from xml data file
	 */
	public static Object[][] getXMLData(String file, String sheet){
		
		final String XMLFileName = FindFile.findFile(file);
		final String sheetName = sheet;

		if (!Excel.isSheetExists(XMLFileName, sheetName)) {
			throw new SetupException(String.format("Error: Sheet [%s] does not exist in file [%s]", sheetName, XMLFileName));
		}
		
		Object[][] table = Excel.getTableArray(XMLFileName, sheetName, false);
		
		return(table);
	}
}
