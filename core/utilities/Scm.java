package core.utilities;

import java.io.File;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import net.neoremind.sshxcute.core.ConnBean;
import net.neoremind.sshxcute.core.IOptionName;
import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;
import net.neoremind.sshxcute.task.impl.ExecShellScript;

/**
 * The Scm class or Software Configuration Management class contains a library
 * of general methods to connect to, access and control remote servers. NOTE:
 * The global parameters (gsServerName, gsServerUserName, gsServerPassword,
 * gsServerPort) need to be defined under Remote Server Properties section in
 * automation.properties file as a pre-requisite.
 */
public class Scm {
	static ConnBean cb = null;
	static JSch jsch;
	static Session session = null;
	static Channel channel = null;
	static ChannelSftp sftpChannel = null;

	/**
	 * Method to connect remote server via ssh.
	 *
	 * @return sshExec returns connection details on success, else throws
	 *         exception Note: Connection credentials will be pulled from
	 *         automation.properties file
	 */
	public static SSHExec connectServer() {
		SSHExec ssh = null;
		if (!Log.AUTOMATION_SCM_REMOTE_SERVER_PORT.equals("22")) {
			SSHExec.setOption(IOptionName.SSH_PORT_NUMBER, Integer.valueOf(Log.AUTOMATION_SCM_REMOTE_SERVER_PORT));
		}
		try {
			if (cb == null) {
				cb = new ConnBean(Log.AUTOMATION_SCM_REMOTE_SERVER_NAME, Log.AUTOMATION_SCM_REMOTE_SERVER_USERNAME,
						Encrypt.decryptPassword(Log.AUTOMATION_SCM_REMOTE_SERVER_PASSWORD));
			} else {
				cb.setHost(Log.AUTOMATION_SCM_REMOTE_SERVER_NAME);
				cb.setUser(Log.AUTOMATION_SCM_REMOTE_SERVER_USERNAME);
				cb.setPassword(Encrypt.decryptPassword(Log.AUTOMATION_SCM_REMOTE_SERVER_PASSWORD));
			}
			ssh = SSHExec.getInstance(cb);

			SSHExec.setOption("PreferredAuthentications", "publickey,keyboard-interactive,password");
			if (ssh.connect()) {
				Log.logScriptInfo("Connected to server : " + Log.AUTOMATION_SCM_REMOTE_SERVER_NAME + " successfully");
			} else {
				throw new Exception("Connection failed to server " + Log.AUTOMATION_SCM_REMOTE_SERVER_NAME);
			}
		} catch (Exception e) {
			Log.errorHandler("Exception occured in connectServer()", e);
		}
		return ssh;
	}

	/**
	 * Method to execute command(s) on remote unix/linux server via ssh. The
	 * global parameters (gsServerName, gsServerUserName, gsServerPassword,
	 * gsServerPort) need to be defined under Remote Server Properties section
	 * in automation.properties file as a pre-requisite.
	 *
	 * @param sCmd
	 *            - command to execute on remote server. For multiple commands,
	 *            use comma as a delimeter.
	 */
	public static void execCmd(String sCmd) {
		SSHExec ssh = null;
		int s;
		try {
			// Convert String sCmd to array for multiple execution of commands.
			String[] sCmdArray = Strings.split(sCmd, ",");

			ssh = connectServer();
			// Loop to execute single or multiple commands
			for (s = 0; s < sCmdArray.length; s++) {
				CustomTask echo = new ExecCommand(sCmdArray[s]);

				Log.logScriptInfo(echo.getInfo());

				Result res = ssh.exec(echo);
				if (res.isSuccess) {
					Log.logScriptInfo("Return code: " + res.rc);
					// Log.logScriptInfo("sysout: " + res.sysout);
				} else {
					Log.logScriptInfo("Return code: " + res.rc);
					Log.errorHandler("Error message: " + res.error_msg);
				}
			}
		} catch (TaskExecFailException e) {
			Log.errorHandler("Error executing task in execCmd()", e);
		} catch (Exception e) {
			Log.errorHandler("Exception occured in execCmd()", e);
		} finally {
			ssh.disconnect();
			Log.logScriptInfo("Disconnected from Server : " + Log.AUTOMATION_SCM_REMOTE_SERVER_NAME + " Successfully");
		}
	}

	/**
	 * Method to execute shell script on remote server via ssh. The global
	 * parameters (gsServerName, gsServerUserName, gsServerPassword,
	 * gsServerPort) need to be defined under Remote Server Properties section
	 * in automation.properties file as a pre-requisite.
	 *
	 * @param sWorkingDir
	 *            - Specify the dir path location of shellscript file (.sh) eg:
	 *            "/export/home/ebit/bin/"
	 * @param sShellPath
	 *            - Specify the name of the .sh file to be executed eg:
	 *            "./status.sh"
	 * @param sArgs
	 *            - argument to be passed if any eg: "Status of xxx process" or
	 *            null or ""
	 * @return command arguments
	 */

	public static String execShellScript(String sWorkingDir, String sShellPath, String sArgs) {
		SSHExec ssh = null;
		try {
			CustomTask echo = new ExecShellScript(sWorkingDir, sShellPath, sArgs);
			ssh = connectServer();
			Log.logScriptInfo(echo.getInfo());

			Result res = ssh.exec(echo);
			if (res.isSuccess) {
				Log.logScriptInfo("Return code: " + res.rc);
			} else {
				Log.logScriptInfo("Return code: " + res.rc);
				Log.errorHandler("error message: " + res.error_msg);
			}
		} catch (TaskExecFailException e) {
			Log.errorHandler("Error executing task in execShellScript()", e);
		} catch (Exception e) {
			Log.errorHandler("Exception occured in execShellScript()", e);
		} finally {
			ssh.disconnect();
			Log.logScriptInfo("Disconnected from Server : " + Log.AUTOMATION_SCM_REMOTE_SERVER_NAME + " Successfully");
		}
		return sArgs;
	}

	/**
	 * Method to upload a local directory or folder from local system to remote
	 * server target path via ssh. The global parameters (gsServerName,
	 * gsServerUserName, gsServerPassword, gsServerPort) need to be defined
	 * under Remote Server Properties section in automation.properties file as a
	 * pre-requisite.
	 *
	 * @param sSourceDir
	 *            - Specify local source directory to be uploaded to server eg:
	 *            C:\\temp\\test
	 * @param sTargetDir
	 *            = Specify remote target directory where folder to be uploaded
	 *            in server eg: /home/pmtplus
	 */

	public static void uploadDirToServer(String sSourceDir, String sTargetDir) {
		SSHExec ssh = null;
		try {
			ssh = connectServer();
			ssh.uploadAllDataToServer(sSourceDir, sTargetDir);
			Log.logScriptInfo("Uploaded " + sSourceDir + " to " + sTargetDir);
		} catch (Exception e) {
			Log.errorHandler("Exception occured in uploadDirToServer()", e);
		} finally {
			ssh.disconnect();
			Log.logScriptInfo("Disconnected from Server : " + Log.AUTOMATION_SCM_REMOTE_SERVER_NAME + " Successfully");
		}
	}

	/**
	 * Method to Upload file from local system to remote server target path via
	 * ssh. The global parameters (gsServerName, gsServerUserName,
	 * gsServerPassword, gsServerPort) need to be defined under Remote Server
	 * Properties section in automation.properties file as a pre-requisite.
	 *
	 * @param sSourceFile
	 *            - Specify local source filename to be uploaded to server eg:
	 *            C:\\temp\\test\\test.java or C:\\temp\\test\\*.java
	 * @param sTargetDir
	 *            = Specify remote target directory where file to be uploaded in
	 *            server eg: /home/pmtplus
	 */

	public static void uploadFileToServer(String sSourceFile, String sTargetDir) {
		SSHExec ssh = null;
		try {
			ssh = connectServer();
			ssh.uploadSingleDataToServer(sSourceFile, sTargetDir);
			Log.logScriptInfo("Uploaded " + sSourceFile + " to " + sTargetDir);
		} catch (Exception e) {
			Log.errorHandler("Exception occured in uploadFileToServer()", e);
		} finally {
			ssh.disconnect();
			Log.logScriptInfo("Disconnected from Server : " + Log.AUTOMATION_SCM_REMOTE_SERVER_NAME + " Successfully");
		}
	}

	/**
	 * Method to connect remote server via sftp. Note: Connection credentials
	 * will be pulled from automation.properties file
	 *
	 * @throws JSchException
	 *             error
	 */
	public static void connectSftp() throws JSchException {
		jsch = new JSch();
		session = jsch.getSession(Log.AUTOMATION_SCM_REMOTE_SERVER_USERNAME, Log.AUTOMATION_SCM_REMOTE_SERVER_NAME,
				Integer.valueOf(Log.AUTOMATION_SCM_REMOTE_SERVER_PORT));
		session.setPassword(Encrypt.decryptPassword(Log.AUTOMATION_SCM_REMOTE_SERVER_PASSWORD));
		session.setConfig("StrictHostKeyChecking", "no");
		session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
		session.connect();

		channel = session.openChannel("sftp");
		channel.connect();
		Log.logScriptInfo("Connected to Server : " + Log.AUTOMATION_SCM_REMOTE_SERVER_NAME + " Successfully");

	}

	/**
	 * Method to download a file or a folder including subfolders and its files
	 * to local machine from remote server. The global parameters (gsServerName,
	 * gsServerUserName, gsServerPassword, gsServerPort) need to be defined
	 * under Remote Server Properties section in automation.properties file as a
	 * pre-requisite.
	 *
	 * @param sSourceData
	 *            - Specify local source file or folder name to be downloaded
	 *            from server with absolute source path in case locates on
	 *            different folder from current eg: "test.java" or "Myfolder"
	 * @param sTargetDir
	 *            - Specify target directory name to be created in local system
	 *            where the file or folder to be downloaded without ending slash
	 *            "/" eg: "D:/Temp"
	 * @param bIsFolder
	 *            - Specify true if the source data is a folder, else false if
	 *            it is a file.
	 * @throws SftpException
	 *             error
	 */
	public static void downloadDataFromServer(String sSourceData, String sTargetDir, Boolean bIsFolder)
			throws SftpException {
		// Create local folders if absent.
		try {
			connectSftp();
			sftpChannel = (ChannelSftp) channel;
			if (bIsFolder) {
				new File(sTargetDir).mkdirs();
				Log.logScriptInfo("Created folder : " + sTargetDir);
				sftpChannel.lcd(sTargetDir);
			}
			// Copy remote folders one by one.
			lsFile_FolderCopy(sSourceData, sTargetDir, bIsFolder); // Separated
																	// because
																	// loops
																	// itself
																	// inside
																	// for
																	// subfolders.
		} catch (Exception e) {
			Log.errorHandler("Error at : " + sTargetDir);
		} finally {
			sftpChannel.exit();
			session.disconnect();
			Log.logScriptInfo("Disconnected from Server : " + Log.AUTOMATION_SCM_REMOTE_SERVER_NAME + " Successfully");
		}
	}

	/**
	 * Method to download a file or a folder including subfolders and files to
	 * local machine from remote server. The global parameters (gsServerName,
	 * gsServerUserName, gsServerPassword, gsServerPort) need to be defined
	 * under Remote Server Properties section in automation.properties file as a
	 * pre-requisite.
	 *
	 * @param sSourceData
	 *            - Specify local source file or folder name to be downloaded
	 *            from server with absolute source path in case locates on
	 *            different folder from current eg: "test.java" or "Myfolder"
	 * @param sTargetDir
	 *            - Specify target directory name to be created in local system
	 *            where the file or folder to be downloaded without ending slash
	 *            "/" eg: "D:/Temp"
	 * @param bIsFolder
	 *            - Specify true if the source data is a folder else false if it
	 *            is a file.
	 * @throws SftpException
	 */
	@SuppressWarnings("unchecked")
	private static void lsFile_FolderCopy(String sSourceData, String sTargetDir, Boolean bIsFolder)
			throws SftpException {
		Vector<ChannelSftp.LsEntry> list = sftpChannel.ls(sSourceData); // List
																		// source
																		// directory
																		// structure.
		for (ChannelSftp.LsEntry oListItem : list) { // Iterate objects in the
														// list to get
														// file/folder names.
			if (!oListItem.getAttrs().isDir()) { // If it is a file (not a
													// directory).
				////// if (!(new File(destPath + "/" +
				////// oListItem.getFilename())).exists() ||
				////// (oListItem.getAttrs().getMTime() >
				////// Long.valueOf(new File(destPath + "/" +
				////// oListItem.getFilename()).lastModified() / (long)
				////// 1000).intValue())) { //
				////// Download only if changed later.
				if (bIsFolder) {
					new File(sTargetDir + "/" + oListItem.getFilename());
					// Grab file from source ([source filename], [destination
					// filename]).
					sftpChannel.get(sSourceData + "/" + oListItem.getFilename(),
							sTargetDir + "/" + oListItem.getFilename());
					Log.logScriptInfo("Downloaded " + sSourceData + "/" + oListItem.getFilename() + " to " + sTargetDir
							+ "/" + oListItem.getFilename());
				} else {
					sftpChannel.get(sSourceData, sTargetDir);
					Log.logScriptInfo("Downloaded " + sSourceData + " to " + sTargetDir);
				}
				////// }
			} else if (!(".".equals(oListItem.getFilename()) || "..".equals(oListItem.getFilename()))) {
				new File(sTargetDir + "/" + oListItem.getFilename()).mkdirs(); // Empty
																				// folder
																				// copy.
				Log.logScriptInfo("Created folder : " + sTargetDir + "/" + oListItem.getFilename());
				// Enter found folder on server to read its contents and create
				// locally.
				lsFile_FolderCopy(sSourceData + "/" + oListItem.getFilename(),
						sTargetDir + "/" + oListItem.getFilename(), bIsFolder);
			}
		}
	}
}
