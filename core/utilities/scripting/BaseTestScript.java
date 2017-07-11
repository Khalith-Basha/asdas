package core.utilities.scripting;


import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;

import core.utilities.Browser;
import core.utilities.DateTime;
import core.utilities.FileIO;
import core.utilities.Log;
import core.utilities.Platform;
import core.utilities.Strings;


@Listeners(core.utilities.scripting.BaseTestScript.class)

/**
 * This is the Base Test Script. All Scripts should extend this script
 * <p>
 */
public class BaseTestScript implements ITestListener, ISuiteListener, IInvokedMethodListener {
	
	int count=0; //count test cases
	
	
	
	/**
	 *  This belongs to ITestListener and will execute before starting of Test set/batch 
	 */
	public void onStart(ITestContext arg0) {
	}
	
	/**
	 *  This belongs to ISuiteListener and will execute before the Suite start
	 */
	public void onStart(ISuite arg0) {
		Log.AUTOMATION_SCRIPT_NAME = arg0.getName();		
		Log.AUTOMATION_SCRIPT_DESCRIPTION = "Test Suite";
		Log.AUTOMATION_SCRIPT_AUTHOR = "Tony Venditti";
		Log.AUTOMATION_SCRIPT_TEST_AREA = FileIO.getParentPath(Platform.getCurrentProjectPath() + Log.AUTOMATION_SCRIPT_NAME.replace(".", Platform.getFileSeparator()));

		// initialize for Suite run
		Log.initializeSuiteStats();
		
	}
	 
	/**
	 *  This belongs to ISuiteListener and will execute, once the Suite is finished
	 */
	public void onFinish(ISuite arg0) {
		
		Log.logSuiteStats();
		
		//Copy results files to alternate location - if directory 
		if (Log.AUTOMATION_COPY_RESULTS_TO != null) {
			try {
				if (!Log.AUTOMATION_COPY_RESULTS_TO.isEmpty()) {
					String append_date_folder = DateTime.getCurrentDate("yyyy-MM-dd")+FileIO.getFileSeparator();
					FileIO.copyDir(Log.AUTOMATION_TEST_RESULTS_PATH, Log.AUTOMATION_COPY_RESULTS_TO + append_date_folder);
					Log.logScriptInfo("Results have been copied from : " + Strings.sDQ +Log.AUTOMATION_TEST_RESULTS_PATH + Strings.sDQ +" to: " + Strings.sDQ + Log.AUTOMATION_COPY_RESULTS_TO + append_date_folder+ Strings.sDQ );
				}
				} catch (final Exception e) {
					Log.errorHandler("Error copying results", e);
				}
			}
		
		
		//Close browser if left open by failure and the close browser global parameter is set to true 
		if (Log.AUTOMATION_CLOSE_BROWSER_UPON_COMPLETION == true) {
			try {
				Browser.close();
				}
				catch (final Exception e) {
					Log.errorHandler("Error closing browser.", e);
				}
			}
		
		
		//check for pass - fail status of suite for Jenkins
		try{
			
		if (FileIO.getFileContents(Log.AUTOMATION_SUITE_RESULT_FILENAME).contains(Log.AUTOMATION_FAIL_MARKER)){
			System.exit(1);
		}
		}
		catch(Exception e){
			
		}
		
		
		
	}
	 
	/**
	 *  This belongs to ITestListener and will execute, once the Test set/batch is finished
	 */
	public void onFinish(ITestContext arg0) {
		
	}
	 
	/**
	 *  This belongs to ITestListener and will execute only when the test is pass
	 */
	public void onTestSuccess(ITestResult arg0) {
	}
	 
	/**
	 *  This belongs to ITestListener and will execute only on the event of fail test
	 */
	public void onTestFailure(ITestResult arg0) {
		//Log.errorHandler("Error occurred in:" + arg0.getTestName() + " - " + arg0.getThrowable());
	}
	 
	/**
	 * This belongs to ITestListener and will execute before the test case starts (@Test)
	 */
	public void onTestStart(ITestResult arg0) {
			String testname = arg0.getName();
		
			if(arg0.getMethod().getDescription() != null){
				testname = arg0.getMethod().getDescription();
			}
			
			count++;  //testcase counter
			
			String params=null;
			if (arg0.getParameters().length != 0 && arg0.getParameters()!=null) {
				for (Object parameter : arg0.getParameters()) {
	 				params += parameter.toString() + ",";
				}
			if (params.startsWith("null")){params=params.substring(4);}
			if (params.endsWith(",")){params=params.substring(0,params.length()-1);}
			Log.startTestCase(testname + " - " + count  + " - {" + params + "}");
			}
			else{
				Log.startTestCase(testname);
			}	
		}
	 
	
	
	/**
	 *  This belongs to ITestListener and will execute only if any of the main test(@Test) get skipped
	 */
	public void onTestSkipped(ITestResult arg0) {
		
	}
	 
	/**
	 * After test
	 */
	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
	}
	
	/**
	 * Before method
	 */
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
	}

	/**
	 * After method
	 */
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		
	}
	
	/**
	 * This gets executed after every test case
	 * 
	 * @throws Exception upon failure
	 */
	@AfterMethod
	public void endTest() throws Exception {
		Log.finishTestCase();
	}
	
	
	/**
	 * This function ends the test, calculates and logs the test results and terminates the automation
	 * 
	 * @throws Exception upon failure
	 */
	@AfterClass(alwaysRun=true)
	public void tearDownBase() throws Exception {
		Log.terminate(); //Gather metrics, report results and terminate automation
	}

	
}

