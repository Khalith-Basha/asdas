package core.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * The FTPUtil class contains FTP functions to connect to and upload and download files form FTP servers.
 */
public class FTPUtil {

	// Creating FTP Client instance
	static FTPClient ftp = null;

	static boolean bFTPConnect = false, bFTPDisconnect = true;
	static boolean bResponse = false, bRecursive = false;
	static int iReply = -1, iProcessCntr = 0;
	static long[] dirInfo = null;

	/**
	 * This method will establish the connection to the FTP Server
	 * 
	 * @throws Exception
	 *
	 */
	private static void ftpConnect() throws Exception {
		try {
			if (!bFTPConnect) {
				ftp = new FTPClient();
				ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
				ftp.connect(Log.AUTOMATION_FTP_SERVER_NAME, Integer.valueOf(Log.AUTOMATION_FTP_SERVER_PORTAL));
				iReply = ftp.getReplyCode();
				if (!FTPReply.isPositiveCompletion(iReply)) {
					ftp.disconnect();
					throw new Exception("Exception in connecting to FTP Server");
				}
				ftp.login(Log.AUTOMATION_FTP_SERVER_USERNAME,
						Encrypt.decryptPassword(Log.AUTOMATION_FTP_SERVER_PASSWORD));
				ftp.setFileType(FTP.BINARY_FILE_TYPE);
				ftp.enterLocalPassiveMode();
				if (ftp.isConnected()) {
					bFTPConnect = true;
					Log.logScriptInfo("Connection to ftp server: " + Log.AUTOMATION_FTP_SERVER_NAME + " successful.");
				} else {
					Log.errorHandler("Connection to ftp server: " + Log.AUTOMATION_FTP_SERVER_NAME + " failed.");
				}
			}
		} catch (Exception e) {
			Log.errorHandler("Exception occured in ftpConnect()", e);
			Log.verifyFatal(true, bFTPConnect, "Connection to FTP Server failed");
		}
	}

	/**
	 * Checks for specified file existence after automatically connecting to a
	 * FTP Server using credentials set in FTP Remote Server Properties section
	 * of automation.properties and performs disconnection from FTP server
	 * finally.
	 *
	 * @param sFilePath
	 *            Absolute file path to check for existence in FTP server eg:
	 *            "/FTPTest/SubDir/rl.txt"
	 * @return True if success, else false
	 * @throws Exception
	 *             error
	 * 
	 * @see "checkDirectoryExists()"
	 */
	public static boolean checkFileExists(String sFilePath) throws Exception {
		bResponse = false;
		ftpConnect();
		try (InputStream inputStream = ftp.retrieveFileStream(sFilePath)) {
			iReply = ftp.getReplyCode();
			if (inputStream == null || iReply == 550) {
				Log.errorHandler("Check and found file: " + sFilePath + " does not exists.");
				bResponse = false;
			} else {
				Log.logScriptInfo("Check and found file: " + sFilePath + " exists.");
				bResponse = true;
			}
		} catch (Exception e) {
			Log.errorHandler("Exception occured in checkFileExists()", e);
		} finally {
			ftpDisconnect();
		}
		return bResponse;
	}

	/**
	 * Checks for specified directory existence after automatically connecting
	 * to a FTP Server using credentials set in FTP Remote Server Properties
	 * section of automation.properties and performs disconnection from FTP
	 * server finally.
	 *
	 * @param sDirPath
	 *            Remote directory absolute path eg: /Upload
	 * @return True if success, else false
	 * 
	 * @see "checkFileExists()"
	 */
	public static boolean checkDirectoryExists(String sDirPath) {
		try {
			ftpConnect();
			bResponse = ftp.changeWorkingDirectory(sDirPath);
			if (bResponse) {
				Log.logScriptInfo("Check and found directory: " + sDirPath + " exists.");
			} else {
				Log.logScriptInfo("Check and found directory: " + sDirPath + " does not exists.");
			}
		} catch (Exception e) {
			Log.errorHandler("Exception occured in checkDirectoryExists()", e);
		} finally {
			ftpDisconnect();
		}
		return bResponse;
	}

	/**
	 * Returns directory path string of the current working directory of FTP
	 * server after automatically connecting to a FTP Server using credentials
	 * set in FTP Remote Server Properties section of automation.properties and
	 * performs disconnection from FTP server finally.
	 *
	 * @return The pathname of the current working directory. If it cannot be
	 *         obtained, returns null
	 * 
	 * @see "changeDirectory()"
	 */
	public static String printWorkingDirectory() {
		String sResponse = null;
		try {
			ftpConnect();
			sResponse = ftp.printWorkingDirectory();
			if (!sResponse.isEmpty()) {
				Log.logScriptInfo("Present working directory: " + sResponse);
			} else {
				Log.errorHandler("Present working directory: " + sResponse);
			}
		} catch (Exception e) {
			Log.errorHandler("Exception occured in printWorkingDirectory()", e);
		} finally {
			ftpDisconnect();
		}
		return sResponse;
	}

	/**
	 * Change to specified directory path after automatically connecting to a
	 * FTP Server using credentials set in FTP Remote Server Properties section
	 * of automation.properties and performs disconnection from FTP server
	 * finally.
	 *
	 * @param sDirPath
	 *            Remote directory absolute path eg: /Upload
	 * @return True if success, else false
	 * 
	 * @see "printWorkingDirectory()"
	 */
	public static boolean changeDirectory(String sDirPath) {
		try {
			ftpConnect();
			bResponse = ftp.changeWorkingDirectory(sDirPath);
			if (bResponse) {
				Log.logScriptInfo("Change directory to: " + sDirPath + " successful.");
			} else {
				Log.errorHandler("Change directory to: " + sDirPath + " failed.");
			}
		} catch (Exception e) {
			Log.errorHandler("Exception occured in changeDirectory()", e);
		} finally {
			ftpDisconnect();
		}
		return bResponse;
	}

	/**
	 * Creates a single or nested directory structure after automatically
	 * connecting to a FTP Server using credentials set in FTP Remote Server
	 * Properties section of automation.properties and performs disconnection
	 * from FTP server finally.
	 *
	 * @param sDirPath
	 *            Remote directory path for single directory eg: /Upload Remote
	 *            directories path for nested directories eg:
	 *            /Upload/myproject/ftpdata
	 * @return True if success, else false
	 */
	public static boolean createDirectories(String sDirPath) {
		try {
			bResponse = false;
			ftpConnect();
			String[] pathElements = sDirPath.split("/");
			if (pathElements != null && pathElements.length > 0) {
				for (String sSingleDir : pathElements) {
					// Change working directory to root if sSingleDir is null
					// for root(/) due to delimeter usage
					if (sSingleDir.equals("")) {
						ftp.changeWorkingDirectory("/");
						continue;
					}
					// Create directory
					if (!ftp.changeWorkingDirectory(sSingleDir)) {
						bResponse = ftp.makeDirectory(sSingleDir);
						if (bResponse) {
							Log.logScriptInfo("Create directory: " + sDirPath + " successful.");
							// Change present working directory to current
							// created directory
							ftp.changeWorkingDirectory(sSingleDir);
						} else {
							Log.errorHandler("Create directory: " + sDirPath + " failed.");
						}
					} else {
						bResponse = true;
					}
				}
			}
		} catch (Exception e) {
			Log.errorHandler("Exception occurred in createDirectories()", e);
		} finally {
			ftpDisconnect();
		}
		return bResponse;
	}

	/**
	 * Uploads specified file to a destination folder after automatically
	 * connecting to a FTP Server using credentials set in FTP Remote Server
	 * Properties section of automation.properties and performs disconnection
	 * from FTP server finally.
	 *
	 * @param sLocalFilePath
	 *            Absolute or relative local source directory path including
	 *            filename with extension eg: C:\\upload\\uploadtest.txt
	 * @param sFilename
	 *            Destination filename with extention to be uploaded as eg:
	 *            uploadtestfile.txt
	 * @param sRemoteFilePath
	 *            Destination host directory with Absolute path eg: /FTPTest/
	 * @return True if success, else false
	 * @see "uploadDirectory(), uploadOnlyDirectoryStructure()"
	 */
	public static boolean uploadFile(String sLocalFilePath, String sFilename, String sRemoteFilePath) {
		try {
			bResponse = false;
			// Check if source file path exists
			if (!FileIO.fileExists(sLocalFilePath)) {
				Log.errorHandler("Local Source File: " + sLocalFilePath + " does not exists to upload.");
			} else {
				ftpConnect();
				InputStream input = new FileInputStream(new File(sLocalFilePath));
				if (sFilename.isEmpty()) {
					bResponse = ftp.storeFile(sRemoteFilePath, input);
				} else {
					bResponse = ftp.storeFile(sRemoteFilePath + "/" + sFilename, input);
				}
				input.close();
				if (bResponse) {
					Log.logScriptInfo(
							"Upload file: " + sLocalFilePath + " to " + sRemoteFilePath + sFilename + " successful.");
				} else {
					Log.errorHandler(
							"Upload file: " + sLocalFilePath + " to " + sRemoteFilePath + sFilename + " failed.");
				}
			}
		} catch (Exception e) {
			Log.errorHandler("Exception occured in uploadFile()", e);
		} finally {
			ftpDisconnect();
		}
		return bResponse;
	}

	/**
	 * Uploads entire directory including nested sub directories and files to a
	 * destination folder in FTP server after automatically connecting to a FTP
	 * Server using credentials set in FTP Remote Server Properties section of
	 * automation.properties and performs disconnection from FTP server finally.
	 *
	 * @param sLocalParentDir
	 *            Path of the local directory being uploaded eg:
	 *            C:\\upload\\TestDir
	 * @param sRemoteDirPath
	 *            Path of the destination directory on the server eg:
	 *            /upload/TestDir.
	 * @param sRemoteParentDir
	 *            Path of the parent directory of the current directory on the
	 *            server (used by recursive calls) eg: by default leave it null
	 *            as "".
	 * @return True if success, else false
	 * @see "uploadFile(), uploadOnlyDirectoryStructure()"
	 */
	public static boolean uploadDirectory(String sLocalParentDir, String sRemoteDirPath, String sRemoteParentDir) {
		try {
			bResponse = false;
			if (!FileIO.dirExists(sLocalParentDir)) {
				Log.errorHandler("Local Source Directory: " + sLocalParentDir + " does not exists to upload.");
			}
			ftpConnect();
			File fLocalDir = new File(sLocalParentDir);
			File[] faSubFiles = fLocalDir.listFiles();
			if (faSubFiles != null && faSubFiles.length > 0) {
				for (File item : faSubFiles) {
					String sRemoteFilePath = sRemoteDirPath + "/" + sRemoteParentDir + "/" + item.getName();
					if (sRemoteParentDir.equals("")) {
						sRemoteFilePath = sRemoteDirPath + "/" + item.getName();
					}

					if (item.isFile()) {
						// upload the file
						String sLocalFilePath = item.getAbsolutePath();
						bFTPDisconnect = false;
						bResponse = uploadFile(sLocalFilePath, "", sRemoteFilePath);
					} else {
						// Create directory if does not exists
						if (!ftp.changeWorkingDirectory(sRemoteFilePath)) {
							bResponse = ftp.makeDirectory(sRemoteFilePath);
							if (bResponse) {
								Log.logScriptInfo("Create directory: " + sRemoteFilePath + " successful.");
							} else {
								Log.errorHandler("Create directory: " + sRemoteFilePath + " failed.");
							}
						} else {
							Log.logScriptInfo("Create directory: " + sRemoteFilePath + " already exists.");
							bResponse = true;
						}

						// upload the sub directory
						String sParent = sRemoteParentDir + "/" + item.getName();
						if (sRemoteParentDir.equals("")) {
							sParent = item.getName();
						}

						sLocalParentDir = item.getAbsolutePath();
						bFTPDisconnect = false;
						bResponse = uploadDirectory(sLocalParentDir, sRemoteDirPath, sParent);
					}
				}
			}
		} catch (Exception e) {
			Log.errorHandler("Exception occurred in uploadDirectory()", e);
		} finally {
			bFTPDisconnect = true;
			ftpDisconnect();
		}
		return bResponse;
	}

	/**
	 * Uploads only directory structure including sub directories (excluding
	 * files in all directories of the path) to a destination folder in FTP
	 * server after automatically connecting to a FTP Server using credentials
	 * set in FTP Remote Server Properties section of automation.properties and
	 * performs disconnection from FTP server finally.
	 *
	 * @param sLocalParentDir
	 *            Path of the local directory to be uploaded eg:
	 *            C:\\upload\\TestDir
	 * @param sRemoteDirPath
	 *            Path of the destination directory on the server eg:
	 *            /upload/TestDir.
	 * @param sRemoteParentDir
	 *            Path of the parent directory of the current directory on the
	 *            server (used by recursive calls) eg: by default leave it null
	 *            as "".
	 * @return True if success, else false
	 * @see "uploadFile(), uploadDirectory()"
	 */
	public static boolean uploadOnlyDirectoryStructure(String sLocalParentDir, String sRemoteDirPath,
			String sRemoteParentDir) {
		try {
			bResponse = false;
			if (!FileIO.dirExists(sLocalParentDir)) {
				Log.errorHandler("Local Source Directory: " + sLocalParentDir + " does not exists to upload.");
			}
			ftpConnect();
			// Verify and create destination parent directory if does not exists
			bFTPDisconnect = false;
			createDirectories(sRemoteDirPath);

			// Create directories structure only
			File fLocalDir = new File(sLocalParentDir);
			File[] faSubFiles = fLocalDir.listFiles();
			if (faSubFiles != null && faSubFiles.length > 0) {
				for (File item : faSubFiles) {
					String sRemoteFilePath = sRemoteDirPath + "/" + sRemoteParentDir + "/" + item.getName();
					if (sRemoteParentDir.equals("")) {
						sRemoteFilePath = sRemoteDirPath + "/" + item.getName();
					}

					if (item.isDirectory()) {
						// Create directory if does not exists
						if (!ftp.changeWorkingDirectory(sRemoteFilePath)) {
							bResponse = ftp.makeDirectory(sRemoteFilePath);
							if (bResponse) {
								Log.logScriptInfo("Create directory structure: " + sRemoteFilePath + " successful.");
							} else {
								Log.logScriptInfo("Create directory structure: " + sRemoteFilePath + " failed.");
							}
						} else {
							Log.logScriptInfo("Create directory: " + sRemoteFilePath + " already exists.");
							bResponse = true;
						}

						// upload the sub directory
						String sParent = sRemoteParentDir + "/" + item.getName();
						if (sRemoteParentDir.equals("")) {
							sParent = item.getName();
						}

						sLocalParentDir = item.getAbsolutePath();
						bFTPDisconnect = false;
						bResponse = uploadOnlyDirectoryStructure(sLocalParentDir, sRemoteDirPath, sParent);
					}
				}
			}
		} catch (Exception e) {
			Log.errorHandler("Exception occurred in uploadOnlyDirectory()", e);
		} finally {
			bFTPDisconnect = true;
			ftpDisconnect();
		}
		return bResponse;
	}

	/**
	 * Downloads file from FTP Server path to local destination folder after
	 * automatically connecting to a FTP Server using credentials set in FTP
	 * Remote Server Properties section of automation.properties and performs
	 * disconnection from FTP server finally.
	 *
	 * @param sRemoteFileFullName
	 *            Absolute source directory path from remote server including
	 *            filename with extension eg: /FTPTest/downloadtest.txt
	 * @param sDestination
	 *            Absolute destination path in local system including filename
	 *            with extension eg: C:/download/downloadtest.txt
	 * @return True if success, else false.
	 * @see "downloadFiles_Directories(), downloadOnlyDirectoryStructure()"
	 */

	public static boolean downloadFile(String sRemoteFileFullName, String sDestination) {
		try {
			bResponse = false;

			ftpConnect();
			File fLocalFile = new File(sDestination);
			File parentDir = fLocalFile.getParentFile();
			if (!parentDir.exists()) {
				// boolean
				// bCreated=FileIO.makeDirs(FileUtils.readFileToString(parentDir));
				boolean bCreated = parentDir.mkdirs();
				if (bCreated)
					Log.logScriptInfo("Create directory: " + parentDir + " successful.");
				else
					Log.logScriptInfo("Create directory: " + parentDir + " failed.");
			}
			FileOutputStream fos = new FileOutputStream(sDestination);
			bResponse = ftp.retrieveFile(sRemoteFileFullName, fos);
			if (bResponse) {
				Log.logScriptInfo("Download file: " + sRemoteFileFullName + " to " + sDestination + " successful.");
			} else {
				Log.errorHandler("Download file: " + sRemoteFileFullName + " to " + sDestination + " failed.");
			}
			fos.close();
		} catch (Exception e) {
			bFTPDisconnect = true;
			Log.errorHandler("Exception occured in downloadFile()", e);
		} finally {
			ftpDisconnect();

		}
		return bResponse;
	}

	/**
	 * Calculates and returns total number of sub directories, files and size of
	 * specified remote directory from FTP server after automatically connecting
	 * using credentials set in FTP Remote Server Properties section of
	 * automation.properties and performs disconnection from FTP server finally.
	 * 
	 * @param sRemoteParentPath
	 *            Path of the remote directory to calculate. Some of the valid
	 *            examples are: 1."/FTPTest" 2."/FTPTest/ftptestfile.txt"
	 *            3."/FTPTest/*.txt" 4."/FTPTest/rl*.txt" 5."/FTPTest/?l*.txt"
	 *            6."/FTPTest/*.txt,/FTPTest/SubDir/*.txt"
	 * @param sRemoteSubDir
	 *            Default to be null to download the entire sub directories and
	 *            files, alternatively, can be a sub directory name if only this
	 *            directory to be downloaded.
	 * @return An array of long numbers in which: - the 1st number is total
	 *         directories. - the 2nd number is total files. - the 3rd number is
	 *         total size.
	 * 
	 * @see "getFileSize()"
	 */
	public static long[] getDirectoryInfo(String sRemoteParentPath, String sRemoteSubDir) {
		String sParentDir;
		long[] info = new long[3];
		long totalSize = 0;
		int totalDirs = 0;
		int totalFiles = 0;

		try {
			FTPFile[] subFiles = null;
			String[] sDnldFiles = Strings.split(sRemoteParentPath, ",");
			ftpConnect();
			for (String sDnldFile : sDnldFiles) {
				File fLocalFile = new File(sDnldFile);
				int iSlashCount = Strings.count(sDnldFile, "/");
				if (iSlashCount > 1) {
					sParentDir = Strings.replace(fLocalFile.getParent(), "\\", "/");
				} else {
					sParentDir = Strings.replace(fLocalFile.getPath(), "\\", "/");
				}

				// sDirToList = fParentDir;
				if (!sRemoteSubDir.equals("")) {
					sParentDir += "/" + sRemoteSubDir;
				}

				if (iSlashCount > 1) {
					subFiles = ftp.listFiles(sDnldFile);
				} else {
					subFiles = ftp.listFiles(sParentDir);
				}
				// Get initial processing count of folders and files from remote
				// parent path
				if (!bRecursive) {
					iReply = subFiles.length;
					bRecursive = true;
				}

				if (subFiles != null && subFiles.length > 0) {
					for (FTPFile aFile : subFiles) {
						String currentFileName = aFile.getName();
						if (currentFileName.equals(".") || currentFileName.equals("..")) {
							// skip parent directory and the directory itself
							continue;
						}
						iReply--;
						if (aFile.isDirectory()) {
							totalDirs++;
							bFTPDisconnect = false;
							long[] subDirInfo = getDirectoryInfo(sParentDir, currentFileName);
							// bFTPDisconnect = true;
							totalDirs += subDirInfo[0];
							totalFiles += subDirInfo[1];
							totalSize += subDirInfo[2];
						} else {
							totalSize += aFile.getSize();
							totalFiles++;
						}
					}
				}
				info[0] = totalDirs;
				info[1] = totalFiles;
				info[2] = totalSize;
			}
			if (iReply <= 0) {
				bFTPDisconnect = true;
			}
		} catch (Exception e) {
			Log.errorHandler("Exception occurred in calculateDirectoryInfo()", e);
		} finally {
			ftpDisconnect();
		}
		return info;
	}

	/**
	 * Download structure of a specified directory from a FTP server after
	 * automatically connecting to a FTP Server using credentials set in FTP
	 * Remote Server Properties section of automation.properties and performs
	 * disconnection from FTP server finally.
	 *
	 * @param sRemoteParentPath
	 *            Path of the remote directory to download. eg: /FTPTest
	 * 
	 * @param sRemoteSubDir
	 *            Default to be null to download the entire sub directories if
	 *            exists, alternatively, can be a sub directory name if only
	 *            this directory to be downloaded.
	 * @param sLocalDir
	 *            Path of directory where the whole remote directory structure
	 *            will be downloaded and saved in local client system eg:
	 *            C:/download/FTPTest
	 * @see "downloadFile(), downloadFiles_Directories()"
	 */
	public static void downloadOnlyDirectoryStructure(String sRemoteParentPath, String sRemoteSubDir,
			String sLocalDir) {
		// Get directory information of sRemoteParentPath to set the
		// bFTPDisconnect flag to true for initiation of disconnecting from
		// server
		if (dirInfo == null) {
			bFTPDisconnect = false;
			iProcessCntr = 0;
			dirInfo = getDirectoryInfo(sRemoteParentPath, sRemoteSubDir);
		}

		String fParentDir = sRemoteParentPath;
		FTPFile[] subFiles = null;

		String[] sDnldFiles = Strings.split(sRemoteParentPath, ",");
		try {
			ftpConnect();

			for (String sDnldFile : sDnldFiles) {
				File fLocalFile = new File(sDnldFile);
				int iSlashCount = Strings.count(sDnldFile, "/");
				if (iSlashCount > 1) {
					fParentDir = Strings.replace(fLocalFile.getParent(), "\\", "/");
				} else {
					fParentDir = Strings.replace(fLocalFile.getPath(), "\\", "/");
				}

				// sDirToList = fParentDir;
				if (!sRemoteSubDir.equals("")) {
					fParentDir += "/" + sRemoteSubDir;
				}

				if (iSlashCount > 1) {
					subFiles = ftp.listDirectories(sDnldFile);
				} else {
					subFiles = ftp.listDirectories(fParentDir);
				}

				if (subFiles != null && subFiles.length > 0) {
					for (FTPFile aFile : subFiles) {
						String currentFileName = aFile.getName();
						if (currentFileName.equals(".") || currentFileName.equals("..")) {
							// skip parent directory and the directory itself
							continue;
						}

						String newDirPath = sLocalDir + fParentDir + File.separator + currentFileName;

						if (aFile.isDirectory()) {
							// create the directory in local directory path
							File newDir = new File(newDirPath);
							if (!newDir.exists()) {
								boolean bCreated = newDir.mkdirs();
								if (bCreated) {
									Log.logScriptInfo("Create directory: " + newDir + " successful.");
								} else {
									Log.errorHandler("Create directory: " + newDir + " failed.");
								}
							}
							iProcessCntr++;
							// download the sub directory
							downloadOnlyDirectoryStructure(fParentDir, currentFileName, sLocalDir);
						}
					}
				}
			}
			// Set the FTP Server Disconnection
			if (iProcessCntr == dirInfo[0]) {
				bFTPDisconnect = true;
				iProcessCntr = 0;
			}
		} catch (Exception e) {
			Log.errorHandler("Exception occurred in downloadOnlyDirectoryStructure()", e);
		} finally {
			ftpDisconnect();
		}
	}

	/**
	 * Downloads specified directory including nested sub directories and files
	 * from FTP server after automatically connecting to a FTP Server using
	 * credentials set in FTP Remote Server Properties section of
	 * automation.properties and performs disconnection from FTP server finally.
	 *
	 * @param sRemoteParentPath
	 *            Path of the parent directory to download. Some of the valid
	 *            examples are: 1."/FTPTest" 2."/FTPTest/ftptestfile.txt"
	 *            3."/FTPTest/*.txt" 4."/FTPTest/rl*.txt" 5."/FTPTest/?l*.txt"
	 *            6."/FTPTest/*.txt,/FTPTest/SubDir/*.txt"
	 * @param sRemoteSubDir
	 *            Default to be null to download the entire sub directories and
	 *            files, alternatively, can be a sub directory name if only this
	 *            directory to be downloaded.
	 * @param sLocalDir
	 *            Path of directory where the whole remote directory will be
	 *            downloaded and saved in local client system eg:
	 *            C:/download/FTPTest
	 * @see "downloadFile(), downloadOnlyDirectoryStructure()"
	 */

	public static void downloadFiles_Directories(String sRemoteParentPath, String sRemoteSubDir, String sLocalDir) {
		try {
			ftpConnect();
			// Get directory information of sRemoteParentPath to set the
			// bFTPDisconnect flag to true for initiation of disconnecting from
			// server
			if (bFTPDisconnect) {
				bFTPDisconnect = false;
				iProcessCntr = 0;
				dirInfo = getDirectoryInfo(sRemoteParentPath, sRemoteSubDir);
			}

			String fParentDir;
			FTPFile[] subFiles = null;

			String[] sDnldFiles = Strings.split(sRemoteParentPath, ",");

			for (String sDnldFile : sDnldFiles) {
				File fLocalFile = new File(sDnldFile);
				int iSlashCount = Strings.count(sDnldFile, "/");
				if (iSlashCount > 1) {
					fParentDir = Strings.replace(fLocalFile.getParent(), "\\", "/");
				} else {
					fParentDir = Strings.replace(fLocalFile.getPath(), "\\", "/");
				}

				// sDirToList = fParentDir;
				if (!sRemoteSubDir.equals("")) {
					fParentDir += "/" + sRemoteSubDir;
				}

				if (iSlashCount > 1) {
					subFiles = ftp.listFiles(sDnldFile);
				} else {
					subFiles = ftp.listFiles(fParentDir);
				}

				if (subFiles != null && subFiles.length > 0) {
					for (FTPFile aFile : subFiles) {
						String currentFileName = aFile.getName();
						if (currentFileName.equals(".") || currentFileName.equals("..")) {
							// skip parent directory and the directory itself
							continue;
						}
						String filePath = fParentDir + "/" + currentFileName;

						String newDirPath = sLocalDir + fParentDir + File.separator + currentFileName;
						File newDir = new File(newDirPath);
						if (aFile.isDirectory()) {
							// create the directory in local directory path
							// File newDir = new File(newDirPath);
							if (!newDir.exists()) {
								boolean bCreated = newDir.mkdirs();
								if (bCreated) {
									Log.logScriptInfo("Create directory: " + newDir + " successful.");
								} else {
									Log.errorHandler("Create directory: " + newDir + " failed.");
								}
							}
							// download the sub directory
							downloadFiles_Directories(fParentDir, currentFileName, sLocalDir);
						} else {
							// download the file
							bFTPDisconnect = false;
							iProcessCntr++;
							downloadFile(filePath, newDir.toString());
						}
					}
				}
				// Set the FTP Server Disconnection
				if (iProcessCntr == dirInfo[1]) {
					bFTPDisconnect = true;
					iProcessCntr = 0;
					dirInfo = null;
				}
			}

		} catch (Exception e) {
			Log.errorHandler("Exception occurred in downloadDirectory()", e);
		} finally {
			ftpDisconnect();
		}
	}

	/**
	 * List file or directory from FTP server after automatically connecting to
	 * a FTP Server using credentials set in FTP Remote Server Properties
	 * section of automation.properties and performs disconnection from FTP
	 * server finally.
	 *
	 * @param sDirectory
	 *            FTP server directory path from root eg: /Exports
	 * @param sFilename
	 *            Filename for specific listing, null value as "" for listing
	 *            all files
	 * @return True if success, else false.
	 * @see "listFile_Dir_Recursive()"
	 */
	public static boolean listFile_Directory(String sDirectory, String sFilename) {
		try {
			bResponse = false;
			ftpConnect();
			String sDirToList = sDirectory;
			if (!sFilename.equals("")) {
				sDirToList += "/" + sFilename;
			}

			FTPFile[] subFiles = ftp.listFiles(sDirToList);

			if (subFiles != null && subFiles.length > 0) {
				for (FTPFile aFile : subFiles) {
					String sCurrentFileName = aFile.getName();
					if (sCurrentFileName.equals(".") || sCurrentFileName.equals("..")) {
						// skip parent directory and the directory itself
						continue;
					}
					String sFilePath = sDirectory;
					if (!sCurrentFileName.equals("")) {
						sFilePath = sDirectory + "/" + sCurrentFileName;
					}

					if (aFile.isDirectory()) {
						// list the sub directory
						Log.logScriptInfo("List dir: " + sFilePath + " successful.");
					} else {
						// list the file
						Log.logScriptInfo("List file: " + sFilePath + " successful.");
						if (sCurrentFileName.equals(sFilename))
							break;
					}
					bResponse = true;
				}
			} else {
				Log.errorHandler("List dir/file: " + sDirectory + "/" + sFilename + " not present in source path.");
			}
		} catch (Exception e) {
			Log.errorHandler("Exception occured in listFile_Directory()", e);
		} finally {
			ftpDisconnect();
		}
		return bResponse;
	}

	/**
	 * List files and directories recursively on a FTP server after
	 * automatically connecting to a FTP Server using credentials set in FTP
	 * Remote Server Properties section of automation.properties and performs
	 * disconnection from FTP server finally.
	 *
	 * @param sParentDir
	 *            Directory path to be listed eg: "/FTPTest/SubDir"
	 * @param sCurrentDir
	 *            Leave as null (Default), can also be a sub directory of parent
	 *            directory to be listed
	 * @see "listFile_Directory()"
	 */
	public static void listFile_Dir_Recursive(String sParentDir, String sCurrentDir) {
		try {
			ftpConnect();
			String sDirToList = sParentDir;
			if (!sCurrentDir.equals("")) {
				sDirToList += "/" + sCurrentDir;
			}

			FTPFile[] subFiles = ftp.listFiles(sDirToList);

			if (subFiles != null && subFiles.length > 0) {
				for (FTPFile aFile : subFiles) {
					String sCurrentFileName = aFile.getName();
					if (sCurrentFileName.equals(".") || sCurrentFileName.equals("..")) {
						// skip parent directory and the directory itself
						continue;
					}
					String sFilePath = sParentDir + "/" + sCurrentDir + "/" + sCurrentFileName;
					if (sCurrentDir.equals("")) {
						sFilePath = sParentDir + "/" + sCurrentFileName;
					}

					if (aFile.isDirectory()) {
						// list the sub directory
						bFTPDisconnect = false;
						Log.logScriptInfo("List dir: " + sFilePath + " successful.");
						listFile_Dir_Recursive(sDirToList, sCurrentFileName);
						bFTPDisconnect = true;
					} else {
						// list the file
						Log.logScriptInfo("List file: " + sFilePath + " successful.");
					}
				}
			}
		} catch (Exception e) {
			bFTPDisconnect = true;
			Log.errorHandler("Exception occured in listFile_Dir_Recursives()", e);
		} finally {
			ftpDisconnect();
		}
	}

	/**
	 * Deletes a single file from FTP server after automatically connecting to a
	 * FTP Server using credentials set in FTP Remote Server Properties section
	 * of automation.properties and performs disconnection from FTP server
	 * finally.
	 *
	 * @param sFilename
	 *            Absolute path including filename with extension eg:
	 *            /FTPTest/testfile.txt
	 * @return True if success, else false
	 * @see "deleteMultipleFiles()"
	 */
	public static boolean deleteSingleFile(String sFilename) {
		try {
			ftpConnect();
			bResponse = ftp.deleteFile(sFilename);
			if (bResponse) {
				Log.logScriptInfo("Delete file: " + sFilename + " successful.");
			} else {
				Log.errorHandler("Delete file: " + sFilename + " failed.");
			}
		} catch (Exception e) {
			Log.errorHandler("Exception occured in deleteSingleFile()", e);
		} finally {
			ftpDisconnect();
		}
		return bResponse;
	}

	/**
	 * Deletes multiple files from FTP server after automatically connecting to
	 * a FTP Server using credentials set in FTP Remote Server Properties
	 * section of automation.properties and performs disconnection from FTP
	 * server finally.
	 *
	 * @param sRemoteDirPath
	 *            FTP Server Remote directory from where files to be deleted eg:
	 *            /FTPTest
	 * @param sFilename
	 *            Absolute path with regular expression eg: "/FTPTest/*.txt"
	 * @return True if success, else false.
	 * @see "deleteSingleFile()"
	 */
	public static boolean deleteMultipleFiles(String sRemoteDirPath, String sFilename) {
		try {
			bResponse = false;
			ftpConnect();
			FTPFile[] files = ftp.listFiles(sRemoteDirPath + "/" + sFilename);
			if (files != null && files.length > 0) {
				for (FTPFile file : files) {
					String sRemoteFilePath = sRemoteDirPath + "/" + file.getName();
					bFTPDisconnect = false;
					bResponse = deleteSingleFile(sRemoteFilePath);
				}
			} else {
				Log.errorHandler("File: " + sRemoteDirPath + "/" + sFilename + " does not exists to delete.");
			}
		} catch (Exception e) {
			Log.errorHandler("Exception occured in deleteMultipleFiles()", e);
		} finally {
			bFTPDisconnect = true; // Allow to disconnect
			ftpDisconnect();
		}
		return bResponse;
	}

	/**
	 * Removes an empty directory after automatically connecting to a FTP Server
	 * using credentials set in FTP Remote Server Properties section of
	 * automation.properties and performs disconnection from FTP server finally.
	 * 
	 * @param sRemoveDirPath
	 *            FTP Server Directory path to be removed eg: "/FTPTest/SubDir"
	 * @return True if success, else false.
	 * @see "removeDirectory()"
	 */
	public static boolean removeEmptyDirectory(String sRemoveDirPath) {
		try {
			bResponse = false;
			ftpConnect();
			bResponse = ftp.removeDirectory(sRemoveDirPath);
			if (bResponse) {
				Log.logScriptInfo("Remove directory: " + sRemoveDirPath + " successful.");
			} else {
				Log.errorHandler("Remove directory: " + sRemoveDirPath + " failed.");
			}
		} catch (Exception e) {
			Log.errorHandler("Exception occured in removeEmptyDirectory()", e);
		} finally {
			ftpDisconnect();
		}
		return bResponse;
	}

	/**
	 * Removes non-empty directory by delete all its sub directories and files
	 * recursively after automatically connecting to a FTP Server using
	 * credentials set in FTP Remote Server Properties section of
	 * automation.properties and performs disconnection from FTP server finally.
	 *
	 * @param sRemoteParentDir
	 *            FTP server directory path to be removed eg: "/FTPTest/SubDir"
	 * @param sRemoteSubDir
	 *            Initialize as null (default) to remove the entire sub
	 *            directories and its files, can be also a sub directory of
	 *            parent directory if only this directory to be removed
	 * @see "removeEmptyDirectory()"
	 */
	public static void removeDirectory(String sRemoteParentDir, String sRemoteSubDir) {
		try {
			ftpConnect();
			String sDirToList = sRemoteParentDir;
			if (!sRemoteSubDir.equals("")) {
				sDirToList += "/" + sRemoteSubDir;
			}

			FTPFile[] subFiles = ftp.listFiles(sDirToList);

			if (subFiles != null && subFiles.length > 0) {
				for (FTPFile aFile : subFiles) {
					String sCurrentFileName = aFile.getName();
					if (sCurrentFileName.equals(".") || sCurrentFileName.equals("..")) {
						// skip parent directory and the directory itself
						continue;
					}
					String sFilePath = sRemoteParentDir + "/" + sRemoteSubDir + "/" + sCurrentFileName;
					if (sRemoteSubDir.equals("")) {
						sFilePath = sRemoteParentDir + "/" + sCurrentFileName;
					}

					if (aFile.isDirectory()) {
						// remove the sub directory
						removeDirectory(sDirToList, sCurrentFileName);
					} else {
						// delete the file
						bFTPDisconnect = false;
						deleteSingleFile(sFilePath);
					}
				}
			}
			// finally, remove the directory itself
			bFTPDisconnect = false;
			removeEmptyDirectory(sDirToList);
			if (sRemoteParentDir.equals(sDirToList))
				bFTPDisconnect = true;
		} catch (Exception e) {
			Log.errorHandler("Exception occured in removeDirectory()", e);
		} finally {
			ftpDisconnect();
		}
	}

	/**
	 * Renames file or directory in FTP Server after automatically connecting to
	 * a FTP Server using credentials set in FTP Remote Server Properties
	 * section of automation.properties and performs disconnection from FTP
	 * server finally.
	 *
	 * @param sSource
	 *            FTP server path/name of the file/directory to rename. eg:
	 *            /FTPTest/SubDir
	 * @param sDestination
	 *            Path/name of the file/directory how we want to rename.
	 *            eg:"/FTPTest/ChildDir"
	 * 
	 * @return True if success, else false
	 */
	public static boolean renameFile_Directory(String sSource, String sDestination) {
		bResponse = false;
		try {
			ftpConnect();
			bResponse = ftp.rename(sSource, sDestination);
			if (bResponse) {
				Log.logScriptInfo("Rename: " + sSource + " to " + sDestination + " successful.");
			} else {
				Log.errorHandler("Rename: " + sSource + " to " + sDestination + " failed.");
			}
		} catch (Exception e) {
			Log.errorHandler("Exception occured in renameFile_Directory()", e);
		} finally {
			ftpDisconnect();
		}
		return bResponse;
	}

	/**
	 * Get file size of a remote file from FTP Server after automatically
	 * connecting to a FTP Server using credentials set in FTP Remote Server
	 * Properties section of automation.properties and performs disconnection
	 * from FTP server finally.
	 * 
	 * @param sSourceFile
	 *            FTP server path/name of the file to get size. eg:
	 *            /FTPTest/iemgpo.txt
	 * @param sUnitType
	 *            Initialize as null (default) to get the size in bytes, other
	 *            valid options are "KB", "MB", "GB" or "TB".
	 * @return A double variable which will contains the file size value in
	 *         specified unit types.
	 * 
	 * @see "getDirectoryInfo(), getFile_DateTimeStamp()"
	 */
	public static double getFileSize(String sSourceFile, String sUnitType) {
		bResponse = false;
		long lSize = 0;
		double fFileSize = 0;
		sUnitType = sUnitType.isEmpty() ? "BYTES" : sUnitType;
		try {
			ftpConnect();
			FTPFile[] files = ftp.listFiles(sSourceFile);
			for (FTPFile aFile : files) {
				lSize = aFile.getSize();
			}
			DecimalFormat df = new DecimalFormat("#.##");
			fFileSize = lSize;
			if (sUnitType.toUpperCase().equals("BYTES")) {
				Log.logScriptInfo("File size: " + sSourceFile + " is " + lSize + " bytes.");
			} else {
				if (sUnitType.toUpperCase().equals("KB")) {
					fFileSize = Math.round(fFileSize / 1024);
					Log.logScriptInfo("File size: " + sSourceFile + " is " + Math.round(fFileSize) + " KB.");
				} else {
					if (sUnitType.toUpperCase().equals("MB")) {
						fFileSize = Double.valueOf(df.format(((fFileSize / 1024) / 1024)));
						Log.logScriptInfo("File size: " + sSourceFile + " is " + fFileSize + " MB.");
					} else {
						if (sUnitType.toUpperCase().equals("GB")) {
							fFileSize = Double.valueOf(df.format((((fFileSize / 1024) / 1024) / 1024)));
							Log.logScriptInfo("File size: " + sSourceFile + " is " + fFileSize + " GB.");
						} else {
							if (sUnitType.toUpperCase().equals("TB")) {
								fFileSize = Double.valueOf(df.format(((((fFileSize / 1024) / 1024) / 1024) / 1024)));
								Log.logScriptInfo("File size: " + sSourceFile + " is " + fFileSize + " TB.");
							} else {
								Log.errorHandler("Unit Type is invalid, Valid types are BYTES, KB, MB, GB or TB.");
							}
						}
					}
				}
			}
		} catch (Exception e) {
			Log.errorHandler("Exception occured in getFileSize()", e);
		} finally {
			ftpDisconnect();
		}
		return fFileSize;
	}

	/**
	 * Helps to get the date and time stamp of a file from FTP Server after
	 * automatically connecting to a FTP Server using credentials set in FTP
	 * Remote Server Properties section of automation.properties and performs
	 * disconnection from FTP server finally.
	 * 
	 * @param sSourceFile
	 *            FTP server path/name of the file to get date and time stamp
	 * @return Date and time stamp string
	 * @see "getDirectoryInfo(), getFileSize()"
	 */
	public static String getFile_DateTimeStamp(String sSourceFile) {
		bResponse = false;
		String lSize = null;
		try {
			ftpConnect();
			FTPFile files = ftp.mdtmFile(sSourceFile);
			String[] reply = Strings.split(files.toString(), " ");
			lSize = reply[0];
		} catch (Exception e) {
			Log.errorHandler("Exception occured in getFile_DateTimeStamp()", e);
		} finally {
			ftpDisconnect();
		}
		return lSize;
	}

	/**
	 * Disconnect from FTP Server
	 * 
	 */
	private static void ftpDisconnect() {
		if (bFTPConnect && bFTPDisconnect) {
			try {
				bResponse = ftp.logout();
				ftp.disconnect();
				bFTPConnect = false;
				if (bResponse)
					Log.logScriptInfo(
							"Disconnection from ftp server: " + Log.AUTOMATION_FTP_SERVER_NAME + " successful.");
				else
					Log.errorHandler("Disconnection from ftp server: " + Log.AUTOMATION_FTP_SERVER_NAME + " failed.");
			} catch (IOException e) {
				Log.errorHandler("IOException occured in ftpDisconnect()", e);
			}
		}
	}
}
