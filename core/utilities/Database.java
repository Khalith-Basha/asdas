package core.utilities;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * The Database class contains general database related functions.
 */
public class Database {
	/**
	 * Executes an SQL query in Oracle database from a Windows Client machine.
	 * Dependant on sqlplus a command-line utility
	 * 
	 * @param sQuery
	 *            SQL Query to execute
	 * @param sSchema
	 *            Database schema name
	 * @param sPW
	 *            Password to access DB schema
	 * @param sDBInstance
	 *            Database instance as specified in the client's tnsnames.ora,
	 *            f.ex. QA10G
	 */
	public static void runSQLCommandFromClient(String sQuery, String sSchema, String sPW, String sDBInstance) {
		runSQLCommandFromClient(sQuery, sSchema, sPW, sDBInstance, "", false);
	}

	/**
	 * Executes an SQL query in Oracle database from a Windows Client machine,
	 * Writes resultset to output file and suppress password Dependant on
	 * sqlplus a command-line utility
	 * 
	 * @param sQuery
	 *            SQL Query to execute
	 * @param sSchema
	 *            Database schema name
	 * @param sPW
	 *            Password to access DB schema
	 * @param sDBInstance
	 *            Database instance as specified in the tnsnames.ora, f.ex.
	 *            QA10G
	 * @param sOutputLog
	 *            name of the file that will record query output, f.ex.
	 *            /Temp/SQLOutput/myOutpurLog.txt; it may have file's path
	 *            specified in front of the file name to direct the file to a
	 *            desired location. If not specified, the file is created in the
	 *            active directory, f.ex. /Documents and Settings/userName
	 * @param bDBPartSupress
	 *            Print only Schema name in the result file leaving the other
	 *            details such as pwd, dbinstance etc.
	 */
	public static void runSQLCommandFromClient(String sQuery, String sSchema, String sPW, String sDBInstance,
			String sOutputLog, boolean bDBPartSupress) {

		String sPwdDBSuppress;
		String[] saQuery = new String[3];
		saQuery[0] = "cmd.exe";
		saQuery[1] = "/c";
		if (sOutputLog.isEmpty()) {
			sPwdDBSuppress = "echo " + sQuery + "|sqlplus " + sSchema;
			sQuery = "echo " + sQuery + "|sqlplus " + sSchema + "/" + sPW + "@" + sDBInstance;
		} else {
			sPwdDBSuppress = "echo " + sQuery + "|sqlplus " + sSchema + " > " + sOutputLog;
			sQuery = "echo " + sQuery + "|sqlplus " + sSchema + "/" + sPW + "@" + sDBInstance + " > " + sOutputLog;
		}
		saQuery[2] = sQuery;
		try {
			Runtime.getRuntime().exec(saQuery);
			if (!bDBPartSupress)
				Log.logScriptInfo("Execute SQL command: " + sQuery);
			else
				Log.logScriptInfo("Execute SQL command: " + sPwdDBSuppress);
		} catch (Exception e) {
			Log.errorHandler("Error in Database.runSQLCommandFromClient(): ", e);
		}
	}

	/**
	 * Executes queries from a SQL file in Oracle database from a Windows Client
	 * machine. Dependant on sqlplus a command-line utility
	 * 
	 * @param sFilename
	 *            SQL file to run. If the file is not present in the active
	 *            directory, it needs to include path in front of the name.
	 * @param sSchema
	 *            Database schema name
	 * @param sPW
	 *            Password to access DB schema
	 * @param sDBInstance
	 *            Database instance as specified in the tnsnames.ora, f.ex.
	 *            QA10GBOL
	 * @return true if executed, false on exception
	 */
	public static boolean runSQLFileFromClient(String sFilename, String sSchema, String sPW, String sDBInstance) {
		// String sCommand = "sqlplus " + sSchema + "/" + sPW + "@" +
		// sDBInstance + " @" + sFilename;
		String[] sCommand = new String[3];
		sCommand[0] = "cmd.exe";
		sCommand[1] = "/c";
		sCommand[2] = "sqlplus " + sSchema + "/" + sPW + "@" + sDBInstance + " @" + sFilename;
		try {
			// Runtime.getRuntime().exec("cmd.exe /c " + sCommand);
			Runtime.getRuntime().exec(sCommand);
			Log.logScriptInfo("Execute SQL command: " + sCommand[2]);
			return true;
		} catch (Exception e) {
			Log.errorHandler("Error in Database.runSQLFileFromClient(): ", e);
			return false;
		}
	}

	/**
	 * Executes queries from a SQL file in Oracle database from a Windows Client
	 * machine after changing the directory. It is executed from a Windows
	 * Client machine.
	 * 
	 * @param sDir
	 *            Directory from which sqlplus command is executed
	 * @param sFilename
	 *            SQL file to run
	 * @param sSchema
	 *            Database schema name
	 * @param sPW
	 *            Password to access DB schema
	 * @param sDBInstance
	 *            - Database instance as specified in the tnsnames.ora, f.ex.
	 *            QA10GBOL
	 * @return true if command is executed without error
	 */
	public static boolean runSQLFileFromClient(String sDir, String sFilename, String sSchema, String sPW,
			String sDBInstance) {
		// String sCommand = "cd " + sDir + " & sqlplus " + sSchema + "/" + sPW
		// + "@" + sDBInstance + " @" + sFilename; // i.e. cmd.exe /c cd
		// \EB_Automation\trunk\automation\eb\testdata\SQL\ResetPwsd & sqlplus
		// EBTSPOD17CSV3/password@QA10GBOL @ResetPwd_AutoUser_NoReset.sql
		String[] sCommand = new String[3];
		sCommand[0] = "cmd.exe";
		sCommand[1] = "/c";
		sCommand[2] = "cd " + sDir + " & sqlplus " + sSchema + "/" + sPW + "@" + sDBInstance + " @" + sFilename;
		try {

			// Runtime.getRuntime().exec("cmd.exe /c " + sCommand);
			Runtime.getRuntime().exec(sCommand);
			Log.logScriptInfo("Execute SQL command: " + sCommand[2]);
			return true;
		} catch (Exception e) {
			Log.errorHandler("Error in Database.runSQLFileFromClient(): ", e);
			return false;
		}
	}

	/**
	 * Executes a SQL query in MSSQL database from a Windows Client machine
	 * Dependant on sqlcmd a command-line utility
	 * 
	 * @param sQuery
	 *            SQL Query to execute
	 * @param sServer
	 *            MSSQL db server name or IP address
	 * @param sDBInstance
	 *            Database instance as specified in the tnsnames.ora, f.ex.
	 *            QA10G
	 * @param iPort
	 *            Database Port eg: 1433
	 * @param sSchema
	 *            Database schema name or userid
	 * @param sPwd
	 *            Password to access DB schema
	 * @param sOutputLog
	 *            name of the file that will record query output, f.ex.
	 *            /Temp/SQLOutput/myOutpurLog.txt; it may have file's path
	 *            specified in front of the file name to direct the file to a
	 *            desired location. If not specified, the file is created in the
	 *            active directory, f.ex. /Documents and Settings/userName
	 * @param bDBPartSupress
	 *            Print only Schema name in the result file leaving the other
	 *            details such as pwd, dbinstance etc.
	 */
	public static void runMSSQLCommandFromClient(String sQuery, String sServer, String sDBInstance, int iPort,
			String sSchema, String sPwd, String sOutputLog, boolean bDBPartSupress) {

		String sPwdDBSuppress;
		String[] saQuery = new String[3];
		saQuery[0] = "cmd.exe";
		saQuery[1] = "/c";
		if (sOutputLog.isEmpty()) {
			sPwdDBSuppress = "echo sqlcmd -S " + sServer + "\\" + sDBInstance + "," + iPort + " -U " + sSchema
					+ " -Q \"" + sQuery + "\"";
			sQuery = "echo |sqlcmd -S " + sServer + "\\" + sDBInstance + "," + iPort + " -U " + sSchema + " -P " + sPwd
					+ " -Q \"" + sQuery + "\"";
		} else {
			sPwdDBSuppress = "echo sqlcmd -S " + sServer + "\\" + sDBInstance + "," + iPort + " -U " + sSchema
					+ " -Q \"" + sQuery + "\" -o " + sOutputLog;
			sQuery = "echo |sqlcmd -S " + sServer + "\\" + sDBInstance + "," + iPort + " -U " + sSchema + " -P " + sPwd
					+ " -Q \"" + sQuery + "\" -o " + sOutputLog;

		}
		saQuery[2] = sQuery;
		try {
			Runtime.getRuntime().exec(saQuery);
			if (!bDBPartSupress)
				Log.logScriptInfo("Execute SQL command: " + sQuery);
			else
				Log.logScriptInfo("Execute SQL command: " + sPwdDBSuppress);
		} catch (Exception e) {
			Log.errorHandler("Error in Database.runMSSQLCommandFromClient(): ", e);
		}
	}

	/**
	 * This method updates values for parameters in definition file. It holds
	 * input needed for sql file execution, f.ex. values for User ID(s),
	 * Organization ID, and output log file path. The file needs to specify any
	 * parameter with corresponding value, which is needed for sql file as input
	 * to execute queries.
	 * 
	 * @param sFileName
	 *            Definition file name to update; it must include path to be
	 *            easily found, i.e. "/automation/testdata/sql/myInputFile.def"
	 *            - where automation is located in root directory of the client
	 *            machine.
	 * @param hmNewVals
	 *            HashMap that contains input to update definition file. It
	 *            includes pairs of parameter's Search Key and corresponding
	 *            Value to be assigned to the parameter, i.e. {parameter1,
	 *            value1}, {parameter2, value2}, ...
	 * @return true if file is updated is successfully
	 */
	public static boolean updateDefInputFile(String sFileName, Map<String, String> hmNewVals) {

		try {
			// display error if specified file does not exist
			File f = new File(sFileName);
			if (!f.exists()) {
				Log.errorHandler("Could not find definition file: " + sFileName);
				return false;
			}

			// get file content as string array
			String sTmp[] = FileIO.getFileContentsAsList(sFileName);

			// cycle through search keys and assign proper value to each key
			for (Map.Entry<String, String> stringStringEntry : hmNewVals.entrySet()) {
				String sMapKey = stringStringEntry.getKey();
				String sKey = sMapKey + "=";
				String sLine = "";
				String s = "";

				// cycle through file contents line by line
				for (String aSTmp : sTmp) {

					// ignore empty or null lines
					if (aSTmp == null || aSTmp.isEmpty()) {
						continue;
					}

					// ignore comment lines
					if (aSTmp.indexOf("--") == 0)
						continue;

					// assign line content to string s
					s = aSTmp;

					// compare line s to the searchkey
					if (sKey.contains(s) || s.contains(sKey)) {
						// if searchkey is found get line content and assign it
						// to sLine and break loop
						sLine = aSTmp;
						break;
					}

				}
				if (sLine.isEmpty()) {
					Log.errorHandler("Could not find key '" + sKey + "' in file '" + sFileName + "'");
					return false;
				}

				// get file content as string
				String sOut = FileIO.getFileContents(sFileName);

				// replace original key and value with new value
				sOut = Strings.replace(sOut, sLine, "define " + sKey + hmNewVals.get(sMapKey) + ";");
				Log.logDebugInfo(
						"Update file '" + sFileName + "' with line 'define " + sKey + hmNewVals.get(sMapKey) + ";'");

				// write out updated file
				FileIO.writeFileContents(sFileName, sOut);

			}
			return true;

		} catch (Exception e) {
			Log.errorHandler("Error in Database.updateDefInputFile(): ", e);
			return false;
		}
	}

	/**
	 * This method updates SQL file with proper path for input definition file,
	 * i.e. instead of calling "myInputFile.def", script would call, f.ex.
	 * "/automation/testdata/sql/myInputFile.def" This is needed when the
	 * sqlplus is executed from any directory, and sql file reads values from a
	 * definition file located in a specific directory
	 * <p>
	 * 
	 * @param sSQLFile
	 *            SQL file to update; this file calls definition file with
	 *            required input; It must include path in front of the file name
	 *            to be easily found, f.ex.
	 *            "/automation/testdata/sql/myFile.sql" - where automation is
	 *            located in root directory of the client machine.
	 * @param sSearchDefLogName
	 *            name of input definition file that is called from sql file
	 *            during sqlplus command execution; f.ex. "myInputFile.def"
	 * @param sPath
	 *            Path that needs to be in front of the definition file name;
	 *            f.ex. "/automation/testdata/sql/"
	 * @return true if file is updated without error
	 */
	public static boolean updateSQLFile(String sSQLFile, String sSearchDefLogName, String sPath) {
		try {
			// display error if specified file does not exist
			File f = new File(sSQLFile);
			if (!f.exists()) {
				Log.errorHandler("Could not find SQL file: " + sSQLFile);
				return false;
			}

			// get file content as string array
			String sTmp[] = FileIO.getFileContentsAsList(sSQLFile);

			String sLine = "";
			String s = "";

			// cycle through file contents line by line
			for (String aSTmp : sTmp) {

				// ignore empty or null lines
				if (aSTmp == null || aSTmp.isEmpty()) {
					continue;
				}

				// ignore comment lines
				if (aSTmp.indexOf("--") == 0)
					continue;

				// assign line content to string s
				s = aSTmp;

				// compare line s to the searchkey
				if (sSearchDefLogName.contains(s) || s.contains(sSearchDefLogName)) {
					// if searchkey is found get line content and assign it to
					// sLine and break loop
					sLine = aSTmp;
					break;
				}
			}

			if (sLine.isEmpty()) {
				Log.errorHandler(
						"Could not find Searched Value '" + sSearchDefLogName + "' in  file '" + sSQLFile + "'");
				return false;
			}

			// get file content as string
			String sOut = FileIO.getFileContents(sSQLFile);

			// replace original key and value with new value
			sOut = Strings.replace(sOut, sLine, "@" + sPath + sSearchDefLogName);
			Log.logDebugInfo("Update file '" + sSQLFile + "' with line '@" + sPath + sSearchDefLogName + "'");

			// write out updated file
			FileIO.writeFileContents(sSQLFile, sOut);

			return true;

		} catch (Exception e) {
			Log.errorHandler("Error in Database.updateSQLFile(): ", e);
			return false;
		}
	}

	/**
	 * This method appends specific line to specified sql file
	 * 
	 * @param sSQLFile
	 *            SQL file (including file location and extension) i.e.
	 *            "C:\testdata\sql\folder1\sSQLFileName.sql"
	 * @param lsLinesToAppend
	 *            array list to append to the sql file
	 * @return true if file is appended successfully without error
	 */
	public static boolean appendSQLFile(String sSQLFile, List<String> lsLinesToAppend) {
		try {
			String sLineToAppend = "";

			// display error if specified file does not exist
			File f = new File(sSQLFile);
			if (!f.exists()) {
				Log.errorHandler("Could not find SQL file: " + sSQLFile);
				return false;
			}

			for (String aLsLinesToAppend : lsLinesToAppend) {
				sLineToAppend = aLsLinesToAppend;
				FileIO.appendStringToFile(sSQLFile, sLineToAppend);
			}
			return true;

		} catch (Exception e) {
			Log.errorHandler("Error in Database.appendSQLFile(): ", e);
			return false;
		}
	}

	/**
	 * Gets a line from a specified database output file
	 * 
	 * @param sLogFileName
	 *            log file to search
	 * @param sSearchKey
	 *            key to search for
	 * @return line from output log file
	 */
	public static String getLineFromOutputLog(String sLogFileName, String sSearchKey) {
		try {
			// display error if specified file does not exist
			File f = new File(sLogFileName);
			if (!f.exists()) {
				Log.errorHandler("Could not find Log file: " + sLogFileName);
				return "";
			}

			// get file content as string array
			String sTmp[] = FileIO.getFileContentsAsList(sLogFileName);

			String sLine = "";
			String s = "";

			// cycle through file contents line by line
			for (String aSTmp : sTmp) {

				// ignore empty or null lines
				if (aSTmp == null || aSTmp.isEmpty()) {
					continue;
				}

				// assign line content to string s
				s = aSTmp;
				// compare line s to the search key partially
				// if (sKey.indexOf(s) != -1 || s.indexOf(sKey) != -1)
				// compare line s to search exact key
				if (sSearchKey.equals(s)) {
					// if searchkey is found get line content and assign it to
					// sLine and break loop
					sLine = aSTmp;
					break;
				}
			}

			if (sLine.isEmpty()) {
				Log.errorHandler("Could not find Searched Value '" + sSearchKey + "' in  file '" + sLogFileName + "'");
				return "";
			}

			Log.logScriptInfo("Searched Value '" + sSearchKey + "' found in file " + sLogFileName);
			return sLine;

		} catch (Exception e) {
			Log.errorHandler("Error in Database.getLineFromOutputLog(): ", e);
			return "";
		}
	}

	/**
	 * Verifies no errors occurred in database output log
	 * 
	 * @param sLogFileName
	 *            log file to verify
	 * @param sErrText
	 *            error text to flag
	 * @return true if no errors flagged, false if errors are found
	 */
	public static boolean verifySQLOutputLog(String sLogFileName, String sErrText) {
		try {
			if (FileIO.fileExists(sLogFileName)) {
				Platform.sleep(Log.AUTOMATION_WAIT_VALUE_15); // Wait for the
																// file to
																// finish
																// creating

				if (!(FileIO.getFileContents(sLogFileName)).contains(sErrText)) {
					Log.logScriptInfo("Verify SQL log '" + sLogFileName.trim() + "': command executed without errors.");
					return true;

				}

				Log.errorHandler("SQL command was not executed. Review the log for details: " + sLogFileName);
			} else {
				Log.errorHandler("Following Log File is missing: " + sLogFileName);
			}
		} catch (Exception e) {
			Log.errorHandler("Error in Database.verifySQLOutputLog(): ", e);
		}

		return false;
	}

	/**
	 * JDBC connection
	 */
	private static Connection connection;

	/**
	 * Get Connect to Database using JDBC driver Pre-requisite: The global
	 * parameters (AUTOMATION_DATABASE_SERVER_SYSTEM,
	 * AUTOMATION_DATABASE_SERVER_NAME,
	 * AUTOMATION_DATABASE_SERVER_INSTANCE_NAME,
	 * AUTOMATION_DATABASE_SERVER_USERNAME, AUTOMATION_DATABASE_SERVER_PASSWORD
	 * and AUTOMATION_DATABASE_SERVER_PORT) need to be defined and initialized
	 * with appropriate values under Database Server Properties section in
	 * automation.properties file by default. Alternatively these parameters can
	 * be re-initialized to connect to multiple database.
	 * 
	 * @throws ClassNotFoundException
	 *             error
	 * @throws SQLException
	 *             sql error
	 * @return database connection
	 */
	public static Connection connectToDatabase() throws ClassNotFoundException, SQLException {

		String sDBDriver = null;
		String sConnectionUrl = null;

		// Add Database sytem type and initialize driver and connection url.
		switch (Log.AUTOMATION_DATABASE_SERVER_SYSTEM.toUpperCase()) {

		case "ORACLE":
			sDBDriver = "oracle.jdbc.driver.OracleDriver";
			sConnectionUrl = "jdbc:oracle:thin:@" + Log.AUTOMATION_DATABASE_SERVER_NAME + ":"
					+ Log.AUTOMATION_DATABASE_SERVER_PORT + ":" + Log.AUTOMATION_DATABASE_SERVER_INSTANCE_NAME;
			break;

		case "MYSQL":
			sDBDriver = "com.mysql.jdbc.Driver";
			sConnectionUrl = "jdbc:mysql://" + Log.AUTOMATION_DATABASE_SERVER_NAME + ":"
					+ Log.AUTOMATION_DATABASE_SERVER_PORT + "/" + Log.AUTOMATION_DATABASE_SERVER_INSTANCE_NAME;
			break;

		case "SQLSERVER":
		case "MSSQL":
			sDBDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			sConnectionUrl = "jdbc:sqlserver://" + Log.AUTOMATION_DATABASE_SERVER_NAME + ":"
					+ Log.AUTOMATION_DATABASE_SERVER_PORT + ";databaseName="
					+ Log.AUTOMATION_DATABASE_SERVER_INSTANCE_NAME;
			break;

		case "DB2":
			sDBDriver = "COM.ibm.db2os390.sqlj.jdbc.DB2SQLJDriver";
			sConnectionUrl = "jdbc:db2://" + Log.AUTOMATION_DATABASE_SERVER_NAME + ":"
					+ Log.AUTOMATION_DATABASE_SERVER_PORT + "/" + Log.AUTOMATION_DATABASE_SERVER_INSTANCE_NAME;
			break;

		case "SYBASE":
			sDBDriver = "com.sybase.jdbc3.jdbc.SybDriver";
			sConnectionUrl = "jdbc:sybase:Tds:" + Log.AUTOMATION_DATABASE_SERVER_NAME + ":"
					+ Log.AUTOMATION_DATABASE_SERVER_PORT + "/" + Log.AUTOMATION_DATABASE_SERVER_INSTANCE_NAME;
			break;

		default:
			Log.errorHandler(
					"Supported Database and values in Log.AUTOMATION_DATABASE_SERVER_SYSTEM variable in automation.properties/script are : ORACLE or MYSQL or MSSQL or SQLSERVER or DB2 or SYBASE");
			break;
		}

		// Get connection to database
		try {
			Class.forName(sDBDriver);
			setConnection(DriverManager.getConnection(sConnectionUrl, Log.AUTOMATION_DATABASE_SERVER_USERNAME,
					Encrypt.decryptPassword(Log.AUTOMATION_DATABASE_SERVER_PASSWORD)));
			Log.logScriptInfo("Database connection established with " + Log.AUTOMATION_DATABASE_SERVER_INSTANCE_NAME
					+ " Instance on " + Log.AUTOMATION_DATABASE_SERVER_NAME);
		} catch (ClassNotFoundException e) {
			Log.errorHandler("Error Class Not Found in Database.connectToDatabase()", e);
		} catch (SQLException e) {
			Log.errorHandler("Error SQL Exception in Database.connectToDatabase()", e);
		}
		return getConnection();
	}

	/**
	 * Getter Connection method to connect database using JDBC
	 * 
	 * @return connection
	 */

	private static Connection getConnection() {
		return connection;
	}

	/**
	 * Setter Connection method to connect database using JDBC
	 * 
	 * @return connection
	 */

	private static Connection setConnection(Connection connection) {
		Database.connection = connection;
		return connection;
	}

	/**
	 * Close Connection to the default database
	 * 
	 * @throws SQLException
	 *             error
	 */
	public static void closeConnection() throws SQLException {
		connection.close();
		Log.logScriptInfo("Database connection closed successfully");
	}

	// /**
	// * Execute any type of sql statement
	// */
	// private void executeSql(String sqlFilePath) {
	// final class SqlExecuter extends SQLExec {
	// public SqlExecuter() {
	// Project project = new Project();
	// project.init();
	// setProject(project);
	// setTaskType("sql");
	// setTaskName("sql");
	// }
	// }
	//
	// SqlExecuter executer = new SqlExecuter();
	// executer.setSrc(new File(sqlFilePath));
	//
	// executer.setDriver(args.getDriver());
	// executer.setPassword(args.getPwd());
	// executer.setUserid(args.getUser());
	// executer.setUrl(args.getUrl());
	// executer.execute();
	// }

	/**
	 * Close Connection to the specified database
	 * 
	 * @param conn
	 *            - Connection string value returned from connectToDatabase
	 *            method
	 * @throws SQLException
	 *             error
	 */
	public static void closeConnection(Connection conn) throws SQLException {
		conn.close();
		Log.logScriptInfo("Database connection closed successfully");
	}

	/**
	 * Execute sql statements from a file Pre-requisite: 1. The global
	 * parameters (AUTOMATION_DATABASE_SERVER_SYSTEM,
	 * AUTOMATION_DATABASE_SERVER_NAME,
	 * AUTOMATION_DATABASE_SERVER_INSTANCE_NAME,
	 * AUTOMATION_DATABASE_SERVER_USERNAME, AUTOMATION_DATABASE_SERVER_PASSWORD
	 * and AUTOMATION_DATABASE_SERVER_PORT) need to be defined and initialized
	 * with appropriate values under Database Server Properties section in
	 * automation.properties file by default. Alternatively these parameters can
	 * be re-initialized to connect to multiple database. 2. User to establish
	 * connection to a database prior to calling this method in test
	 * script/library eg: Connection connName = Database.connectToDatabase();
	 * where value can be ORACLE or MYSQL or MSSQL or SQLSERVER or DB2 or
	 * SYBASE.
	 * 
	 * @param sSQLFilePath
	 *            - SQL script file name with absolute path eg:
	 *            "D:\\Temp\\TestSql.sql"
	 * @param conn
	 *            - Connection string returned while connecting DB. ie. connName
	 *            as given in eg: above.
	 * @throws SQLException
	 *             error
	 */
	public static void executeSqlFromFile(String sSQLFilePath, Connection conn) throws SQLException {

		try {
			// Set auto-commit to false
			conn.setAutoCommit(false);

			// Get sql statements from sqlFilePath
			String[] sqlToExec = FileIO.getFileContentsAsList(sSQLFilePath);

			// Get sql statements from file and execute
			for (int element = 0; element < sqlToExec.length; element++) {
				sqlToExec[element] = Strings.replace(sqlToExec[element], ";", "");
				if (sqlToExec[element].toUpperCase().startsWith("SELECT")) {
					executeDatabaseQuery(sqlToExec[element], conn);
				} else {
					executeDatabaseUpdate(sqlToExec[element], conn);
				}
			}
		} catch (Exception e) {
			Log.errorHandler("Error SQL Exception in Database.executeSql()", e);
		} finally {
			// Explicitly commit statements to apply changes
			conn.commit();
		}
	}

	/**
	 * Executes a sql insert, update or delete query in default database
	 * specified in Database Server Properties section of Automation.properties
	 * file. Pre-requisite: 1. The global parameters
	 * (AUTOMATION_DATABASE_SERVER_SYSTEM, AUTOMATION_DATABASE_SERVER_NAME,
	 * AUTOMATION_DATABASE_SERVER_INSTANCE_NAME,
	 * AUTOMATION_DATABASE_SERVER_USERNAME, AUTOMATION_DATABASE_SERVER_PASSWORD
	 * and AUTOMATION_DATABASE_SERVER_PORT) need to be defined and initialized
	 * with appropriate values under Database Server Properties section in
	 * automation.properties file by default. Alternatively these parameters can
	 * be re-initialized to connect to multiple database. 2. User to establish
	 * connection to a database prior to calling this method in test
	 * script/library eg: Connection connName = Database.connectToDatabase();
	 * where value can be ORACLE or MYSQL or MSSQL or SQLSERVER or DB2 or
	 * SYBASE.
	 * 
	 * @param sQuery
	 *            - Update query to be executed
	 * @return iUpdCount - returns updated row(s) count.
	 * @throws SQLException
	 *             error
	 */

	public static int executeDatabaseUpdate(String sQuery) throws SQLException {
		return executeDatabaseUpdate(sQuery, connection);
	}

	/**
	 * Executes a SQL insert, update or delete query in specified database
	 * Pre-requisite: 1. The global parameters
	 * (AUTOMATION_DATABASE_SERVER_SYSTEM, AUTOMATION_DATABASE_SERVER_NAME,
	 * AUTOMATION_DATABASE_SERVER_INSTANCE_NAME,
	 * AUTOMATION_DATABASE_SERVER_USERNAME, AUTOMATION_DATABASE_SERVER_PASSWORD
	 * and AUTOMATION_DATABASE_SERVER_PORT) need to be defined and initialized
	 * with appropriate values under Database Server Properties section in
	 * automation.properties file by default. Alternatively these parameters can
	 * be re-initialized to connect to multiple database. 2. User to establish
	 * connection to a database prior to calling this method in test
	 * script/library eg: Connection connName = Database.connectToDatabase();
	 * where value can be ORACLE or MYSQL or MSSQL or SQLSERVER or DB2 or
	 * SYBASE.
	 * 
	 * @param sQuery
	 *            Update query to be executed
	 * @param conn
	 *            string returned while connecting DB. ie. connName like in
	 *            example above.
	 * @return iUpdCount returns updated row(s) count.
	 * @throws SQLException
	 *             error
	 */

	public static int executeDatabaseUpdate(String sQuery, Connection conn) throws SQLException {
		int iUpdCount = 0;
		try (Statement statement = conn.createStatement()) {
			Log.logScriptInfo("Executed: " + sQuery + " in DB " + conn.getCatalog());
			statement.executeUpdate(sQuery);
			iUpdCount = statement.getUpdateCount();
			if (iUpdCount > 0)
				conn.commit();
			Log.logScriptInfo("ResultSet: " + iUpdCount + " rows affected.");
		} catch (SQLException e) {
			Log.errorHandler("Error SQL Exception in Database.executeDatabaseUpdate()", e);
		}
		return iUpdCount;
	}

	/**
	 * Executes a SQL query in default database specified in Database Server
	 * Properties section of Automation.properties file.
	 * 
	 * @param sQuery
	 *            - sql query to execute
	 * @return String rSet - returns resultset after converting as string
	 * @throws SQLException
	 *             error
	 */

	public static String executeDatabaseQuery(String sQuery) throws SQLException {
		return executeDatabaseQuery(sQuery, connection);
	}

	/**
	 * Executes a SQL query in specified database. Pre-requisite: 1. The global
	 * parameters (AUTOMATION_DATABASE_SERVER_SYSTEM,
	 * AUTOMATION_DATABASE_SERVER_NAME,
	 * AUTOMATION_DATABASE_SERVER_INSTANCE_NAME,
	 * AUTOMATION_DATABASE_SERVER_USERNAME, AUTOMATION_DATABASE_SERVER_PASSWORD
	 * and AUTOMATION_DATABASE_SERVER_PORT) need to be defined and initialized
	 * with appropriate values under Database Server Properties section in
	 * automation.properties file by default. Alternatively these parameters can
	 * be re-initialized to connect to multiple database. 2. User to establish
	 * connection to a database prior to calling this method in test
	 * script/library eg: Connection connName = Database.connectToDatabase();
	 * where value can be ORACLE or MYSQL or MSSQL or SQLSERVER or DB2 or
	 * SYBASE.
	 * 
	 * @param sQuery
	 *            - sql query to execute
	 * @param conn
	 *            connection string returned while connecting DB. ie. connName
	 *            like in example above.
	 * @return String rSet returns resultset after converting as string
	 * @throws SQLException
	 *             error
	 */
	public static String executeDatabaseQuery(String sQuery, Connection conn) throws SQLException {
		String sQueryResult = null;
		try (Statement statement = conn.createStatement(); ResultSet rSet = statement.executeQuery(sQuery)) {
			Log.logScriptInfo("Executed: " + sQuery + " in DB " + Log.AUTOMATION_DATABASE_SERVER_INSTANCE_NAME);
			sQueryResult = Database.convertResultSetAsString(rSet);
			Log.logScriptInfo("ResultSet: " + sQueryResult);
		} catch (SQLException e) {
			Log.errorHandler("Error SQL Exception Database.executeDatabaseQuery()", e);
		}
		return sQueryResult;
	}

	/**
	 * This method converts the ResultSet to String variable
	 * 
	 * @param ResultSet
	 *            This variable will have the output of the executed query
	 * @throws SQLException
	 *             error
	 */

	private static String convertResultSetAsString(ResultSet ResultSet) throws SQLException {
		StringBuilder builder = new StringBuilder();
		int columnCount = ResultSet.getMetaData().getColumnCount();
		while (ResultSet.next()) {
			for (int i = 0; i < columnCount;) {
				builder.append(ResultSet.getString(i + 1));
				if (++i < columnCount)
					builder.append(",");
			}
			builder.append("\r\n");
		}
		return builder.toString();
	}

	/**
	 * Executes a SQL query in specified database. Pre-requisite: 1. The global
	 * parameters (AUTOMATION_DATABASE_SERVER_SYSTEM,
	 * AUTOMATION_DATABASE_SERVER_NAME,
	 * AUTOMATION_DATABASE_SERVER_INSTANCE_NAME,
	 * AUTOMATION_DATABASE_SERVER_USERNAME, AUTOMATION_DATABASE_SERVER_PASSWORD
	 * and AUTOMATION_DATABASE_SERVER_PORT) need to be defined and initialized
	 * with appropriate values under Database Server Properties section in
	 * automation.properties file by default. Alternatively these parameters can
	 * be re-initialized to connect to multiple database. 2. User to establish
	 * connection to a database prior to calling this method in test
	 * script/library eg: Connection connName = Database.connectToDatabase();
	 * where value can be ORACLE or MYSQL or MSSQL or SQLSERVER or DB2 or
	 * SYBASE. This method is called especially to execute a stored procedure or
	 * to execute a query which does not retun any result set.
	 * 
	 * @param sQuery
	 *            - sql query to execute
	 * @param conn
	 *            connection string returned while connecting DB. ie. connName
	 *            like in example above.
	 * @param bln
	 *            - boolean value like true or false to be passed to call this
	 *            method
	 * @return boolean result returns true or false based on the execution of
	 *         the query
	 * @throws SQLException
	 *             error
	 */

	public static boolean executeDatabaseQuery(String sQuery, Connection conn, boolean bln) throws SQLException {
		boolean result = bln;
		try (Statement statement = conn.createStatement()) {
			result = statement.execute(sQuery);
			Log.logScriptInfo("Executed: " + sQuery + " in DB " + Log.AUTOMATION_DATABASE_SERVER_INSTANCE_NAME);
		} catch (SQLException e) {
			Log.errorHandler("Error SQL Exception Database.executeDatabaseQuery()", e);
		}
		return result;
	}

	/**
	 * Executes an array of sql statements. Pre-requisite: 1. The global
	 * parameters (AUTOMATION_DATABASE_SERVER_SYSTEM,
	 * AUTOMATION_DATABASE_SERVER_NAME,
	 * AUTOMATION_DATABASE_SERVER_INSTANCE_NAME,
	 * AUTOMATION_DATABASE_SERVER_USERNAME, AUTOMATION_DATABASE_SERVER_PASSWORD
	 * and AUTOMATION_DATABASE_SERVER_PORT) need to be defined and initialized
	 * with appropriate values under Database Server Properties section in
	 * automation.properties file by default. Alternatively these parameters can
	 * be re-initialized to connect to multiple database. 2. User to establish
	 * connection to a database prior to calling this method in test
	 * script/library eg: Connection connName = Database.connectToDatabase();
	 * where value can be ORACLE or MYSQL or MSSQL or SQLSERVER or DB2 or
	 * SYBASE.
	 * 
	 * @param sQuery
	 *            - Array of queries to be executed
	 * 
	 * @param conn
	 *            - Connection string returned while connecting DB. ie. connName
	 *            as given in eg: above.
	 * @throws SQLException
	 *             error
	 */
	public static void executeDatabaseQueryBatch(String[] sQuery, Connection conn) throws SQLException {
		try (Statement statement = conn.createStatement()) {
			// Set auto-commit to false
			conn.setAutoCommit(false);

			// Get sql statements from sQuery
			String[] sqlToExec = sQuery;

			// Add to batch
			for (String i : sqlToExec) {
				statement.addBatch(i);
			}

			// Execute the batch
			statement.executeBatch();
			Log.logScriptInfo("Executed: An array of SQL statements in batch " + " in DB "
					+ Log.AUTOMATION_DATABASE_SERVER_INSTANCE_NAME);

			// Clear the batch
			statement.clearBatch();

		} catch (Exception e) {
			Log.errorHandler("Error SQL Exception in Database.executeDatabaseQueryBatch()", e);
		} finally {
			// Explicitly commit statements to apply changes
			conn.commit();
		}

	}

}
