package core.utilities.scripting.dataproviders;

import core.utilities.Excel;
import core.utilities.exceptions.SetupException;

/**
 * Class represents provider of data from excel file.
 */
public class ExcelDataProvider {
	/**
	 * Processes data from excel file for test script.
	 * 
	 * @param file to read in
	 * @param sheet to read from
	 * @return Object that contains all data from excel file
	 */
	public static Object[][] getExcelData(String file, String sheet){
		
		final String excelFileName = FindFile.findFile(file);
		final String sheetName = sheet;

		if (!Excel.isSheetExists(excelFileName, sheetName)) {
			throw new SetupException(String.format("Error: Sheet [%s] does not exist in file [%s]", sheetName, excelFileName));
		}
		
		Object[][] table = Excel.getTableArray(excelFileName, sheetName, false);
		
		return(table);	
	}
}
