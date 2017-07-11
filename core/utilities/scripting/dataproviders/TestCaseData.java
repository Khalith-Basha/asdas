package core.utilities.scripting.dataproviders;

/**
 * The class represents the data returned from the data provider.
 */
public class TestCaseData {
	
	
		
	private String search;
	private String result;
	
	public TestCaseData(Object[][] data){
	      //this.search=search;
	      //this.result=result;
	   }
		
	   
	   public TestCaseData(String search, String result){
	      this.search=search;
	      this.result=result;
	   }
	   
	   public String getSearch() {
	      return search;
	   }
	   
	   public String getResult() {
	      return result;
	   }
	   
	   
	 
	
		
	/**
	 * Converts cell value to boolean type.
	 *
	 * @param columnName column name
	 * @return boolean value
	 */
	public boolean getBoolean(String columnName) {
		return Boolean.parseBoolean(get(columnName));
	}

	/**
	 * Converts cell value to int type.
	 *
	 * @param columnName column name
	 * @return int value
	 */
	public int getInt(String columnName) {
		return Integer.parseInt(get(columnName));
	}

	
	
	public int size() {
		return this.size();
	}

	public boolean isEmpty() {
		return this.isEmpty();
	}

	public String get(String columnName) {
		return this.get(columnName);
	}

	

	
}
