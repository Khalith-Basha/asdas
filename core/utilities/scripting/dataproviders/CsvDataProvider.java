package core.utilities.scripting.dataproviders;

import core.utilities.Excel;

/**
 * Class represents provider of data from csv file.
 */
public class CsvDataProvider {
	
	/**
	 * Processes data from external csv file for test script.
	 * 
	 * @param file to read in
	 * @return Object that contains all data from csv file
	 */
	public static Object[][] getCSVData(String file){
		
		final String csvFileName = FindFile.findFile(file);
		
		Object[][] table = Excel.getTableArray(csvFileName, "", false);
		
		return(table);
	}
}
