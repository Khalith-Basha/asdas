
package core.utilities.report;

import java.io.File;
//import java.io.FileInputStream;
import java.io.IOException;
//import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
//import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Properties;

//import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import core.utilities.FileIO;
import core.utilities.Log;
import core.utilities.Strings;
//import core.utilities.exceptions.AutomationException;
import core.utilities.report.AutomationReport.Section;
import core.utilities.report.XMLReport.Tags;

public class HTMLReport {
	private static Document doc = null;
	private static File htmlReportFile;

	/**
	 * Generates HTML report and opens if it necessary.
	 *
	 * @param path
	 *            path to HTML report file.
	 */
	public HTMLReport(final String path) {
		try {
			htmlReportFile = new File(path + ".htm");

			FileIO.copyResource("core/tools/template.html",
					Paths.get(Log.AUTOMATION_TEST_RESULTS_PATH).resolve(htmlReportFile.getName()).toString());

			doc = Jsoup.parse(htmlReportFile, "UTF-8");
			doc.select("#configInfo").first().html(generateConfigInfo());
			save();
		} catch (final IOException e) {

		}
	}

	/**
	 * Sets HTML report title.
	 *
	 * @param title
	 *            of HTML report
	 */
	public void setTitle(String title) {
		title = String.format("Automation report for %s", title);
		doc.title(title);
		final Element header = doc.select("#header").first();
		if (header.html().isEmpty()) {
			header.html(String.format("<div>%s</div>", title));
			save();
		}
	}

	/**
	 * Saves {@link Document} object.
	 */
	private void save() {
		try {
			Log.AUTOMATION_SUITE_RESULT_FILENAME = htmlReportFile.getPath();
			Files.write(Paths.get(htmlReportFile.getPath()), doc.outerHtml().getBytes(StandardCharsets.UTF_8));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds menu item.
	 *
	 * @param parentId
	 *            - id of the parent element
	 * @param type
	 *            - type of element. {@link Tags}
	 * @param caption
	 *            - menu caption
	 * @param id
	 *            - id element
	 */
	public void addMenu(final String parentId, final String type, final String caption, final String id) {
		Element menuContainer = doc.select("#menu .container").first();
		String html = "";

		if (parentId != null) {
			menuContainer = menuContainer.select(String.format("li#%s", parentId)).first();

			if (menuContainer != null) {
				if (menuContainer.select("ul").size() == 0) {
					menuContainer.append("<ul class='container'></ul>");
				}

				if (menuContainer.hasClass("expandLeaf")) {
					menuContainer.removeClass("expandLeaf");
				}

				if (!menuContainer.hasClass("expandClosed")) {
					menuContainer.addClass("expandClosed");
				}

				if (!type.equals(Tags.TEST_CASE)) {
					menuContainer = menuContainer.select("ul").first();
					if (menuContainer != null) {
						final Element last = menuContainer.select("> li[class*='isLast']").first();

						if (last != null) {
							last.removeClass("isLast");
						}

						html = String.format("<li id='%s' class='node %s expandLeaf isLast'>", id, type.toLowerCase());

						html += "<div class='content'>" + "<div class='expand'></div>" + "<div class='caption'>"
								+ "<i class='fail_icon'></i>" + "<div class='caption_text' title='" + caption + "'>"
								+ caption + "</div>"
								+ "<span class='status' onmouseover='showStat(event)' onmouseout='hideStat(event)'><span style='color:black'>33</span>"
								+ "</span></div></div></li>";

						menuContainer.append(html);
					}
				} else {
					menuContainer = menuContainer.select("ul").first();

					if (menuContainer != null) {
						final Element last = menuContainer.select("> li[class*='isLast']").first();

						if (last != null) {
							last.removeClass("isLast");
						}

						html = String.format("<li id='%s' class='node testcase expandLeaf isLast'>", id);
						html += "<div class='expand'></div><div class='content'>" + "<div class='caption' title='"
								+ caption + "'><div class='caption_text'>" + caption + "</div><span class='status'>"
								+ "</span></div></div></li>";

						menuContainer.append(html);
					}
				}
			}
		} else {
			html = String.format("<li id='%s' class='node %s isRoot'>", id, type.toLowerCase());

			html += "<div class='content'>" + "<div class='expand'></div>" + "<div class='caption'>"
					+ "<i class='fail_icon'></i>" + "<div class='caption_text' title='" + caption + "'>" + caption
					+ "</div>"
					+ "<span class='status' onmouseover='showStat(event)' onmouseout='hideStat(event)'></span></div>"
					+ "</div></li>";

			menuContainer.append(html);
		}

		save();
	}

	/**
	 * Updates status of element in menu.
	 *
	 * @param id
	 *            - id menu item
	 * @param status
	 *            - status
	 */
	public void updateStatus(final String id, final String status) {
		final Element menuItem = doc.select(String.format("li#%s", id)).first();
		Element element;

		if (menuItem != null) {
			if (menuItem.hasClass("suite") || menuItem.hasClass("script") || menuItem.hasClass("group")) {
				element = menuItem.select("i").first();
				if (element != null) {
					element.addClass(status.toLowerCase() + "_icon");
				}

			} else {
				element = menuItem.select("span").first();
				if (element != null) {
					element.addClass(status.toLowerCase());
					element.appendText(status);
				}
				element = menuItem.select(".caption_text").first();
				if (element != null && !status.equals(TestStatuses.PASS)) {
					element.addClass("status " + status.toLowerCase());
				}
			}
		}

		save();
	}

	/**
	 * Adds container for statistic.
	 *
	 * @param id
	 *            - menu item id
	 */
	public void addStatisticContainer(final String id) {
		final Element statisticContainer = doc.select("#statistic").first();

		if (statisticContainer.select(String.format("div#Stat%s.statistic", id)).first() == null) {
			statisticContainer
					.append(String.format("<div id='Stat%s' class='statistic' style='display: none'></div>", id));
		}

		save();
	}

	/**
	 * Adds step into statistic conainer.
	 *
	 * @param id
	 *            - id of menu item
	 * @param step
	 *            - step description
	 * @param imagePath
	 *            - path to screenshot
	 * @param section
	 *            - {@link Section}
	 * @param essence
	 *            - {@link Tags}
	 */
	public void addStep(final String id, String step, final String imagePath, final Section section, String essence) {
		Element statisticContainer = doc.select("#statistic").first();

		essence = essence != null ? essence.substring(0, 1).toUpperCase() + essence.substring(1).toLowerCase() : "";

		statisticContainer = statisticContainer.select(String.format("div#Stat%s.statistic", id)).first();
		String screenShot = "";

		step = step.replace("\n", "</br>");

		if (step.contains("- FAIL -") || step.contains("- ERROR -")) {
			step = String.format("<span style='color:red'>%s</span>", step);
			if (imagePath != null) {
				screenShot = generateImageTag(imagePath);
			}
		} else {
			if (imagePath != null) {
				screenShot = String.format(
						"<label style='color:#adb4b8' onClick='openModal(event)' imagePath='%s'>&nbsp;&nbsp;view screenshot</label>",
						generateImageSrc(imagePath));
			}
		}

		if (section == Section.CURRENT) {
			if (statisticContainer.select("ul.steps").first() == null) {
				statisticContainer.append(
						"</br><span class='caption steps'>Script Information:<ul class='testinfo'></ul><ul class='steps'></ul></span>");
						//"</br><span class='caption steps'>Steps:<ul class='testinfo'></ul><ul class='steps'></ul></span>");
			}

			statisticContainer = statisticContainer.select("span.steps ul.steps").first();
		} else if (section == Section.BEFORETEST) {
			Element before = statisticContainer.select("span.before").first();

			if (before == null) {
				final String beforeUL = String.format("<span class='caption before'>%s and Setup information:</span>",
						essence);
				final Element spanSteps = statisticContainer.select("span.steps").first();
				if (spanSteps != null) {
					spanSteps.before(beforeUL);
				} else {
					statisticContainer.append(beforeUL);
				}
			}
			before = statisticContainer.select("span.before").first();

			if (statisticContainer.select("ul.before").first() == null && before != null) {
				before.after("<ul class='before'></ul>");
			}

			statisticContainer = statisticContainer.select("ul.before").first();
		} else if (section == Section.AFTERTEST) {
			Element after = statisticContainer.select("span.after").first();
			if (after == null) {
				statisticContainer.append(
						String.format("<span class='caption after'>Teardown %s:</span>", essence.toLowerCase()));
			}
			after = statisticContainer.select("span.after").first();
			if (after.select("ul.after").first() == null) {
				statisticContainer.append("<ul class='after'></ul>");
			}

			statisticContainer = statisticContainer.select("ul.after").first();
		}

		if (statisticContainer != null) {
			statisticContainer.append(String.format("<li>%s%s</li>", step, screenShot));
		}

		save();
	}

	// /**
	// * Encodes image to Base64 String.
	// *
	// * @param fileName path to file
	// * @return encoded image in Base64 String
	// */
	// private static String encodeToString(final Path fileName) {
	// final File file = new File(fileName.toString());
	// byte[] bytes = null;
	// try (InputStream is = new FileInputStream(file)) {
	//
	// final long length = file.length();
	// bytes = new byte[(int) length];
	// int offset = 0;
	// int numRead = 0;
	// while (offset < bytes.length && (numRead = is.read(bytes, offset,
	// bytes.length - offset)) >= 0) {
	// offset += numRead;
	// }
	//
	// if (offset < bytes.length) {
	// throw new AutomationException("Could not completely read file " +
	// file.getName());
	// }
	// } catch (final Exception exception) {
	// throw new AutomationException("Could not encode image to Base64 String",
	// exception);
	// }
	//
	// return String.format("data:image/jpeg;base64,%s",
	// Base64.encodeBase64String(bytes));
	// }

	/**
	 * Generates tag for captured image.
	 *
	 * @param imagePath
	 *            path to image
	 * @return path to image
	 */
	private String generateImageTag(String imagePath) {
		String content = "<div style='height:430px;'>";

		content += "<a onClick='openImage(arguments[0])'>";
		content += "<img src='" + generateImageSrc(imagePath)
				+ "' style='max-width:100%; max-height:100%;cursor: pointer'>";

		return content += "</a></div></br>";
	}

	/**
	 * Returns HTML string with automation and system properties.
	 *
	 * @return formatted HTML string
	 */
	private static String generateConfigInfo() {
		final StringBuffer info = new StringBuffer();

		info.append("<div class='tabs'>");
		info.append(
				"<input id='tab1' type='radio' name='tabs' checked><label for='tab1' title='automation'>automation</label>");
		info.append("<input id='tab2' type='radio' name='tabs'><label for='tab2' title='system'>system</label>");
		info.append("<input id='tab3' type='radio' name='tabs'><label for='tab3' title='debug'>debug</label>");
		info.append("<section id='content1'>");
		info.append(getAutomationProperies());
		info.append("</section><section id='content2'>");
		info.append(getSystemProperies());
		info.append("</section><section id='content3'>");
		info.append(getDebugInfo());
		info.append("</section></div>");

		return info.toString();
	}

	/**
	 * Gets automation properties.
	 *
	 * @return formatted HTML string
	 */
	private static String getAutomationProperies() {
		final StringBuffer automationProperties = new StringBuffer();

		automationProperties.append("<ul>");
		// automationProperties.append(addLI("#****************************************************"));
		// automationProperties.append(addLI("#Test Tool Properties"));
		// automationProperties.append(addLI("#****************************************************"));
		// automationProperties.append("</br>");
		// //automationProperties.append(addLI("#SilkCentral Integration"));
		// //automationProperties.append(addLI(String.format("gbAutomationSilkCentral=%s",
		// checkPropertyValue(Log.gbAutomationSilkCentral))));
		// automationProperties.append("</br>");
		automationProperties.append(addLI("#****************************************************"));
		automationProperties.append(addLI("#General Automation Path, Logging, Browser and Time out Properties"));
		automationProperties.append(addLI("#****************************************************"));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#Local Automation directory"));
		automationProperties.append(addLI(String.format("AUTOMATION_TEST_PROJECT_PATH=%s",
				checkPropertyValue(Log.AUTOMATION_TEST_PROJECT_PATH))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#Test data and support document directory"));
		automationProperties.append(addLI(
				String.format("AUTOMATION_TEST_DATA_PATH=%s", checkPropertyValue(Log.AUTOMATION_TEST_DATA_PATH))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#Result log directory"));
		automationProperties.append(addLI(String.format("AUTOMATION_TEST_RESULTS_PATH=%s",
				checkPropertyValue(Log.AUTOMATION_TEST_RESULTS_PATH))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#Test Browser Name"));
		automationProperties.append(
				addLI(String.format("AUTOMATION_TEST_BROWSER=%s", checkPropertyValue(Log.AUTOMATION_TEST_BROWSER))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#Close Test Browsers upon test script completion"));
		automationProperties.append(addLI(String.format("AUTOMATION_CLOSE_BROWSER_UPON_COMPLETION=%s",
				checkPropertyValue(Log.AUTOMATION_CLOSE_BROWSER_UPON_COMPLETION))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#Delete all files in results folder before start script"));
		automationProperties.append(
				addLI(String.format("AUTOMATION_CLEAR_RESULTS=%s", checkPropertyValue(Log.AUTOMATION_CLEAR_RESULTS))));
		automationProperties.append("</br>");
		automationProperties
				.append(addLI("#Automatically open HTML Result log in your default browser when script completes"));
		automationProperties.append(addLI(String.format("AUTOMATION_OPEN_RESULT_REPORT_AFTER_SCRIPT_COMPLETION=%s",
				checkPropertyValue(Log.AUTOMATION_OPEN_RESULT_REPORT_AFTER_SCRIPT_COMPLETION))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#Global Sleep (wait) values in seconds"));
		automationProperties.append(
				addLI(String.format("AUTOMATION_WAIT_VALUE_1=%s", checkPropertyValue(Log.AUTOMATION_WAIT_VALUE_1))));
		automationProperties.append(
				addLI(String.format("AUTOMATION_WAIT_VALUE_2=%s", checkPropertyValue(Log.AUTOMATION_WAIT_VALUE_2))));
		automationProperties.append(
				addLI(String.format("AUTOMATION_WAIT_VALUE_5=%s", checkPropertyValue(Log.AUTOMATION_WAIT_VALUE_5))));
		automationProperties.append(
				addLI(String.format("AUTOMATION_WAIT_VALUE_10=%s", checkPropertyValue(Log.AUTOMATION_WAIT_VALUE_10))));
		automationProperties.append(
				addLI(String.format("AUTOMATION_WAIT_VALUE_15=%s", checkPropertyValue(Log.AUTOMATION_WAIT_VALUE_15))));
		automationProperties.append(addLI(
				String.format("<li>AUTOMATION_WAIT_VALUE_30=%s", checkPropertyValue(Log.AUTOMATION_WAIT_VALUE_30))));
		automationProperties.append(
				addLI(String.format("AUTOMATION_WAIT_VALUE_60=%s", checkPropertyValue(Log.AUTOMATION_WAIT_VALUE_60))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#****************************************************"));
		automationProperties.append(addLI("#Logging Properties"));
		automationProperties.append(addLI("#****************************************************"));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#Result log file suffix"));

		automationProperties.append("</br>");
		automationProperties.append(addLI("#Result log error image suffix"));
		automationProperties.append(addLI(String.format("AUTOMATION_RESULT_ERROR_IMAGE_SUFFIX=%s",
				checkPropertyValue(Log.AUTOMATION_RESULT_ERROR_IMAGE_SUFFIX))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#Log Type. There 9 different log output formats."));
		automationProperties
				.append(addLI(String.format("AUTOMATION_LOG_TYPE=%s", checkPropertyValue(Log.AUTOMATION_LOG_TYPE))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#Capture Stack Trace upon failure"));
		automationProperties.append(
				addLI(String.format("AUTOMATION_STACK_TRACE=%s", checkPropertyValue(Log.AUTOMATION_STACK_TRACE))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#Capture Screen Image upon failure"));
		automationProperties.append(addLI(String.format("AUTOMATION_ERROR_IMAGE_CAPTURE=%s",
				checkPropertyValue(Log.AUTOMATION_ERROR_IMAGE_CAPTURE))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#Capture a Screen Image during every testcase"));
		automationProperties.append(
				addLI(String.format("AUTOMATION_IMAGE_CAPTURE=%s", checkPropertyValue(Log.AUTOMATION_IMAGE_CAPTURE))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#Enable JUnit fails upon each testcase failure"));
		automationProperties.append(addLI(String.format("AUTOMATION_JUNIT_FAIL_ENABLE=%s",
				checkPropertyValue(Log.AUTOMATION_JUNIT_FAIL_ENABLE))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#Test Level"));
		automationProperties.append(
				addLI(String.format("AUTOMATION_TEST_LEVEL=%s", checkPropertyValue(Log.AUTOMATION_TEST_LEVEL))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#Default Result Viewer Application"));
		automationProperties.append(addLI(String.format("AUTOMATION_RESULT_VIEWER_APP=%s",
				checkPropertyValue(Log.AUTOMATION_RESULT_VIEWER_APP))));
		automationProperties.append("</br>");
		automationProperties
				.append(addLI("#Global boolean to update test case data in runtime before starting of each test case"));
		automationProperties.append(addLI("#Number of test cases must be static and known before execution."));
		automationProperties.append(addLI(String.format("AUTOMATION_RUNTIME_UPDATE_DATA=%s",
				checkPropertyValue(Log.AUTOMATION_RUNTIME_UPDATE_DATA))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#Global boolean to capture browser window"));
		automationProperties
				.append(addLI("#If variable is false then entire screen (primary desktop) will be captured"));
		automationProperties.append(addLI(
				String.format("AUTOMATION_BROWSER_CAPTURE=%s", checkPropertyValue(Log.AUTOMATION_BROWSER_CAPTURE))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#****************************************************"));
		automationProperties.append(addLI("#Browser Profile Properties"));
		automationProperties.append(addLI("#****************************************************"));
		automationProperties.append("</br>");
		automationProperties
				.append(addLI("#Browser profiles allow you to customize your browsers options. To use use a browser"));
		automationProperties.append(addLI(
				"#profile file enter the name of the profile file here. Leave empty if the default browser is what you want."));
		automationProperties.append(addLI(
				String.format("AUTOMATION_BROWSER_PROFILE=%s", checkPropertyValue(Log.AUTOMATION_BROWSER_PROFILE))));
		automationProperties.append("</br>");
		automationProperties.append(
				addLI("#Browser Path. leaving this property empty tells automation to use the default browser path"));
		automationProperties.append(
				addLI("#If you are using a Opera or require an alternate browser directory, you can set it here"));
		automationProperties.append(
				addLI(String.format("AUTOMATION_BROWSER_PATH=%s", checkPropertyValue(Log.AUTOMATION_BROWSER_PATH))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#Chrome Command line commands"));
		automationProperties.append(addLI(String.format("AUTOMATION_CHROME_COMMANDLINE=%s",
				checkPropertyValue(Log.AUTOMATION_CHROME_COMMANDLINE))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#****************************************************"));
		automationProperties.append(addLI("#Remote Server Properties"));
		automationProperties.append(addLI("#****************************************************"));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#Remote Server Name or IP Address"));
		automationProperties.append(addLI(String.format("AUTOMATION_SCM_REMOTE_SERVER_NAME=%s",
				checkPropertyValue(Log.AUTOMATION_SCM_REMOTE_SERVER_NAME))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#Remote Server User Name"));
		automationProperties.append(addLI(String.format("AUTOMATION_SCM_REMOTE_SERVER_USERNAME=%s",
				checkPropertyValue(Log.AUTOMATION_SCM_REMOTE_SERVER_USERNAME))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#Remote Server Password"));
		automationProperties.append(addLI(String.format("AUTOMATION_SCM_REMOTE_SERVER_PASSWORD=%s",
				checkPropertyValue(Log.AUTOMATION_SCM_REMOTE_SERVER_PASSWORD))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#Remote Server Port (Default value is 22)"));
		automationProperties.append(addLI(String.format("AUTOMATION_SCM_REMOTE_SERVER_PORT=%s",
				checkPropertyValue(Log.AUTOMATION_SCM_REMOTE_SERVER_PORT))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#****************************************************"));
		automationProperties.append(addLI("#FTP Remote Server Properties"));
		automationProperties.append(addLI("#****************************************************"));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#FTP Server Name or IP Address"));
		automationProperties.append(addLI(
				String.format("AUTOMATION_FTP_SERVER_NAME=%s", checkPropertyValue(Log.AUTOMATION_FTP_SERVER_NAME))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#FTP Server User Name"));
		automationProperties.append(addLI(String.format("AUTOMATION_FTP_SERVER_USERNAME=%s",
				checkPropertyValue(Log.AUTOMATION_FTP_SERVER_USERNAME))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#FTP Server Password"));
		automationProperties.append(addLI(String.format("AUTOMATION_FTP_SERVER_PASSWORD=%s",
				checkPropertyValue(Log.AUTOMATION_FTP_SERVER_PASSWORD))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#FTP Server Port"));
		automationProperties.append(addLI(String.format("AUTOMATION_FTP_SERVER_PORTAL=%s",
				checkPropertyValue(Log.AUTOMATION_FTP_SERVER_PORTAL))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#****************************************************"));
		automationProperties.append(addLI("#Database Server Properties"));
		automationProperties.append(addLI("#****************************************************"));
		automationProperties.append("</br>");
		automationProperties.append(addLI(
				"#Database System Name (SQLServer). Valid options are : ORACLE or MYSQL or MSSQL or SQLSERVER or DB2 or SYBASE"));
		automationProperties.append(String.format("AUTOMATION_DATABASE_SERVER_SYSTEM=%s",
				checkPropertyValue(Log.AUTOMATION_DATABASE_SERVER_SYSTEM)));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#Database Server Name or IP Address"));
		automationProperties.append(String.format("AUTOMATION_DATABASE_SERVER_NAME=%s",
				checkPropertyValue(Log.AUTOMATION_DATABASE_SERVER_NAME)));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#Database Server Instance Name"));
		automationProperties.append(String.format("AUTOMATION_DATABASE_SERVER_INSTANCE_NAME=%s",
				checkPropertyValue(Log.AUTOMATION_DATABASE_SERVER_INSTANCE_NAME)));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#Database Server User Name"));
		automationProperties.append(String.format("AUTOMATION_DATABASE_SERVER_USERNAME=%s",
				checkPropertyValue(Log.AUTOMATION_DATABASE_SERVER_USERNAME)));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#Database Server Password"));
		automationProperties.append(String.format("AUTOMATION_DATABASE_SERVER_PASSWORD=%s",
				checkPropertyValue(Log.AUTOMATION_DATABASE_SERVER_PASSWORD)));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#Database Server Port"));
		automationProperties.append(String.format("AUTOMATION_DATABASE_SERVER_PORT=%s",
				checkPropertyValue(Log.AUTOMATION_DATABASE_SERVER_PORT)));
		automationProperties.append("</br>");
		// automationProperties.append(addLI("#****************************************************"));
		// automationProperties.append(addLI("#SilkMobile Properties"));
		// automationProperties.append(addLI("#****************************************************"));
		// automationProperties.append("</br>");
		// automationProperties
		// .append(addLI(String.format("gsAutomationSilkMobilePath=%s",
		// checkPropertyValue(Log.gsAutomationSilkMobilePath))));
		// automationProperties
		// .append(addLI(String.format("gsAutomationSilkMobileHost=%s",
		// checkPropertyValue(Log.gsAutomationSilkMobileHost))));
		// automationProperties
		// .append(addLI(String.format("giAutomationSilkMobilePort=%s",
		// checkPropertyValue(Log.giAutomationSilkMobilePort))));
		// automationProperties.append(addLI(String.format("gAppsDirectoryPath=%s",
		// checkPropertyValue(Log.gAppsDirectoryPath))));
		// automationProperties.append("</br>");
		// automationProperties.append(addLI("#****************************************************"));
		// automationProperties.append(addLI("#Silk4J Properties"));
		// automationProperties.append(addLI("#****************************************************"));
		// automationProperties.append("</br>");
		// automationProperties.append(addLI("#Silk4J engine type WEB, DESKTOP
		// or MOBILE"));
		// automationProperties.append(addLI(String.format("silk4jEngineType=%s",
		// checkPropertyValue(Log.silk4jEngineType))));
		// automationProperties.append("</br>");
		// automationProperties.append(addLI("#Silk4J Common variable for
		// indetifing application"));
		// automationProperties.append(addLI(String.format("silk4jLocator=%s",
		// checkPropertyValue(Log.silk4jLocator))));
		// automationProperties.append("</br>");
		// automationProperties.append(addLI("#-------------------------"));
		// automationProperties.append(addLI("#Silk4J Desktop variables"));
		// automationProperties.append(addLI("#-------------------------"));
		// automationProperties.append(addLI(String.format("silk4jExecutible=%s",
		// checkPropertyValue(Log.silk4jExecutible))));
		// automationProperties
		// .append(addLI(String.format("silk4jCommandLineArguments=%s",
		// checkPropertyValue(Log.silk4jCommandLineArguments))));
		// automationProperties.append(addLI("#-------------------------"));
		// automationProperties.append(addLI("#Silk4J Browser variables"));
		// automationProperties.append(addLI("#-------------------------"));
		// automationProperties.append(addLI(String.format("silk4jURL=%s",
		// checkPropertyValue(Log.silk4jURL))));
		// automationProperties.append(addLI("#-------------------------"));
		// automationProperties.append(addLI("#Silk4J Mobile variables"));
		// automationProperties.append(addLI("#-------------------------"));
		// automationProperties
		// .append(addLI(String.format("silk4jMobileDevicePlatform=%s",
		// checkPropertyValue(Log.silk4jMobileDevicePlatform))));
		// automationProperties.append(addLI(String.format("silk4jMobileDeviceName=%s",
		// checkPropertyValue(Log.silk4jMobileDeviceName))));
		// automationProperties.append(addLI(String.format("silk4jApplicationName=%s",
		// checkPropertyValue(Log.silk4jApplicationName))));
		// automationProperties.append("</br>");
		// automationProperties.append(addLI("#****************************************************"));
		// automationProperties.append(addLI("#Test Server/User Properties"));
		// automationProperties.append(addLI("#****************************************************"));
		// automationProperties.append("</br>");
		// automationProperties.append(addLI("#Test Server URL"));
		// automationProperties.append(addLI(String.format("gsAutomationBaseURL=%s",
		// checkPropertyValue(Log.gsAutomationBaseURL))));
		// automationProperties.append("</br>");
		// automationProperties.append(addLI("#Test user name"));
		// automationProperties.append(addLI(String.format("gsAutomationUsername=%s",
		// checkPropertyValue(Log.gsAutomationUsername))));
		// automationProperties.append("</br>");
		// automationProperties.append(addLI("#Test user password"));
		// automationProperties.append(addLI(String.format("gsAutomationPassword=%s",
		// checkPropertyValue(Log.gsAutomationPassword))));
		// automationProperties.append("</br>");

		automationProperties.append("</br>");
		automationProperties.append(addLI("#****************************************************"));
		automationProperties.append(addLI("#BrowserStack Properties"));
		automationProperties.append(addLI("#****************************************************"));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#Enable BrowserStack"));
		automationProperties.append(addLI(String.format("AUTOMATION_BROWSERSTACK_ENABLE=%s",
				checkPropertyValue(Log.AUTOMATION_BROWSERSTACK_ENABLE))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#BrowserStack License key"));
		automationProperties.append(addLI(String.format("AUTOMATION_BROWSERSTACK_LICENSE_KEY=%s",
				checkPropertyValue(Log.AUTOMATION_BROWSERSTACK_LICENSE_KEY))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#BrowserStack License User"));
		automationProperties.append(addLI(String.format("AUTOMATION_BROWSERSTACK_LICENSE_USER=%s",
				checkPropertyValue(Log.AUTOMATION_BROWSERSTACK_LICENSE_USER))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#BrowserStack Operating System"));
		automationProperties.append(addLI(
				String.format("AUTOMATION_BROWSERSTACK_OS=%s", checkPropertyValue(Log.AUTOMATION_BROWSERSTACK_OS))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#BrowserStack Operating System Version"));
		automationProperties.append(addLI(String.format("AUTOMATION_BROWSERSTACK_OS_VERSION=%s",
				checkPropertyValue(Log.AUTOMATION_BROWSERSTACK_OS_VERSION))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#BrowserStack Screen Resolution"));
		automationProperties.append(addLI(String.format("AUTOMATION_BROWSERSTACK_SCREEN_RESOLUTION=%s",
				checkPropertyValue(Log.AUTOMATION_BROWSERSTACK_SCREEN_RESOLUTION))));
		automationProperties.append("</br>");
		automationProperties.append(addLI("#BrowserStack Browser Version"));
		automationProperties.append(addLI(String.format("AUTOMATION_BROWSERSTACK_BROWSER_VERSION=%s",
				checkPropertyValue(Log.AUTOMATION_BROWSERSTACK_BROWSER_VERSION))));
		automationProperties.append("</br>");

		automationProperties.append("</br>");
		automationProperties.append(addLI("#****************************************************"));
		automationProperties.append(addLI("#BandWidth limit Properties"));
		automationProperties.append(addLI("#****************************************************"));
		automationProperties.append("</br>");
		automationProperties.append(addLI(
				String.format("AUTOMATION_BANDWIDTH_LIMIT=%s", checkPropertyValue(Log.AUTOMATION_BANDWIDTH_LIMIT))));
		automationProperties.append("</br>");
		automationProperties.append(addLI(String.format("AUTOMATION_BANDWIDTH_LIMIT_READ=%s",
				checkPropertyValue(Log.AUTOMATION_BANDWIDTH_LIMIT_READ))));
		automationProperties.append("</br>");
		automationProperties.append(addLI(String.format("AUTOMATION_BANDWIDTH_LIMIT_WRITE=%s",
				checkPropertyValue(Log.AUTOMATION_BANDWIDTH_LIMIT_WRITE))));
		automationProperties.append("</br>");

		// automationProperties.append(addLI("#****************************************************"));
		// automationProperties.append(addLI("#Test Base-State properties"));
		// automationProperties.append(addLI("#****************************************************"));
		// automationProperties.append("</br>");
		// automationProperties.append(addLI("#Browser Base State Caption"));
		// automationProperties.append(
		// addLI(String.format("gsAutomationBrowserBaseStatePage=%s",
		// checkPropertyValue(Log.gsAutomationBrowserBaseStatePage))));
		// automationProperties.append("</br>");
		// automationProperties.append(addLI("#Base state setting"));
		// automationProperties.append(addLI(String.format("giAutomationBaseState=%s",
		// checkPropertyValue(Log.giAutomationBaseState))));
		// automationProperties.append("</br>");
		// automationProperties.append(addLI("#****************************************************"));
		// automationProperties.append(addLI("#Generic array variable anyone can
		// use to store global variables for any purpose."));
		// automationProperties.append(addLI("#****************************************************"));
		// automationProperties.append("</br>");
		// automationProperties.append(addLI(String.format("glsAutomationArgs=%s",
		// checkPropertyValue(Log.glsAutomationArgs))));

		automationProperties.append("</ul>");
		return automationProperties.toString();
	}

	/**
	 * Gets automation properties.
	 *
	 * @return formatted HTML string
	 */
	private static String getSystemProperies() {
		final StringBuffer systemProperties = new StringBuffer();

		final Properties properies = System.getProperties();
		final Enumeration<?> keys = properies.keys();
		systemProperties.append("<ul>");

		while (keys.hasMoreElements()) {
			final String key = (String) keys.nextElement();
			final String value = (String) properies.get(key);
			systemProperties.append(addLI(String.format("%s: %s", key, value)));
		}

		systemProperties.append("</ul>");

		return systemProperties.toString();
	}

	/**
	 * Gets debug information.
	 *
	 * @return formatted HTML string
	 */
	private static String getDebugInfo() {
		final StringBuffer debugInfo = new StringBuffer();

		debugInfo.append("<ul>");
		debugInfo.append(addLI("Last step:<span id='lastStep'></span>"));

		final String fileName = htmlReportFile.getName().substring(0, htmlReportFile.getName().lastIndexOf("."));

		debugInfo.append(addLI(String.format("<a href='%s.txt' target='_blank'>TXT report</a>", fileName)));
		debugInfo.append(addLI(String.format("<a href='%s.xml' target='_blank'>XML report</a>", fileName)));
		debugInfo.append(addLI(String.format("<a href='%s-JUnit.xml' target='_blank'>JUNIT report</a>", fileName)));
		debugInfo.append("</ul>");

		return debugInfo.toString();
	}

	/**
	 * Generates HTML 'li' tag,
	 *
	 * @param text
	 *            - content
	 * @return formatted HTML string
	 */
	private static String addLI(final String text) {
		return String.format("<li>%s</li>", text);
	}

	private static String checkPropertyValue(final Object value) {
		return value == null ? "" : String.valueOf(value);
	}

	private static String generateImageSrc(String imagePath) {
		if (imagePath != null) {
			// if ((System.getProperty(Log.SCTM_TEST_RESULTS_DIR) != null &&
			// System.getProperty(Log.AUTOMATION_AUTO_PATH) != null ||
			// Log.gbAutomationSilkCentral) ||
			// System.getProperty(Log.EXECUTION_TOOL) != null) {
			// return encodeToString(Paths.get(imagePath));
			// }

			return Paths.get(imagePath).getFileName().toString();
		}

		return "";
	}

	public void addResultStatistic(final String id, final String testEssence, final String statistic) {
		final String caption = String.format("<span class='caption stat'>%s Results:</span>",
				testEssence.substring(0, 1).toUpperCase() + testEssence.substring(1).toLowerCase());

		final StringBuffer scenarioStatistic = new StringBuffer();
		scenarioStatistic
				.append("<ul><li>******************************************************************************</li>");

		for (final String contentItem : Strings.stringToList(statistic.replace(Strings.LINE_SEPARATOR, "</br>"),
				Strings.LINE_SEPARATOR)) {
			scenarioStatistic.append(String.format("<li>%s</li>", contentItem));
		}
		scenarioStatistic
				.append("<li>******************************************************************************</li></ul>");

		doc.select(String.format("div#Stat%s.statistic", id)).first().append(caption + scenarioStatistic);

		save();
	}

	/**
	 * Adds test case link,
	 *
	 * @param id
	 *            - id of test case menu item
	 * @param link
	 *            - link
	 */
	public void addTestCaseLink(final String id, final String link) {
		final Element stat = doc.select(String.format("div#Stat%s.statistic .caption.steps ul.testinfo", id)).first();

		if (stat != null) {
			Element testCaseLink = stat.select("#testCaseLink").first();
			if (testCaseLink == null) {
				stat.append("<li id='testCaseLink'></li>");
			}

			testCaseLink = stat.select("#testCaseLink").first().html(link);
			save();
		}
	}

	/**
	 * Adds test case comments.
	 *
	 * @param id
	 *            - id of test case menu item
	 * @param comments
	 *            - comments
	 */
	public void addTestCaseComments(final String id, final String comments) {
		final Element stat = doc.select(String.format("div#Stat%s.statistic .caption.steps ul.testinfo", id)).first();

		if (stat != null) {
			Element testCaseLink = stat.select("#testCaseComments").first();
			if (testCaseLink == null) {
				stat.append("<li id='testCaseComments'></li>");
			}

			testCaseLink = stat.select("#testCaseComments").first().html(comments);
			save();
		}
	}

	/**
	 * Adds test case fail comments.
	 *
	 * @param id
	 *            - id of test case menu item
	 * @param comments
	 *            - fail comments
	 */
	public void addTestCaseFailComments(final String id, final String comments) {
		final Element stat = doc.select(String.format("div#Stat%s.statistic .caption.steps ul.testinfo", id)).first();

		if (stat != null) {
			Element testCaseLink = stat.select("#testCaseFailComments").first();
			if (testCaseLink == null) {
				stat.append("<li id='testCaseFailComments'></li>");
			}

			testCaseLink = stat.select("#testCaseFailComments").first().html(comments);
			save();
		}
	}

	/**
	 * Adds test case skip comments.
	 *
	 * @param id
	 *            - id of test case menu item
	 * @param comments
	 *            - skip comments
	 */
	public void addTestCaseSkipComments(final String id, final String comments) {
		final Element stat = doc.select(String.format("div#Stat%s.statistic .caption.steps ul.testinfo", id)).first();

		if (stat != null) {
			Element testCaseLink = stat.select("#testCaseSkipComments").first();
			if (testCaseLink == null) {
				stat.append("<li id='testCaseSkipComments'></li>");
			}

			testCaseLink = stat.select("#testCaseSkipComments").first().html(comments);
			save();
		}
	}

	/**
	 * Updates last step info.
	 *
	 * @param step
	 *            info
	 */
	public void updateLastStep(final String step) {
		doc.select("#lastStep").first().html(step);
		save();
	}

	/**
	 * Returns HTML report {@link File} object.
	 *
	 * @return {@link File}
	 */
	public File getHTMLReportFile() {
		return htmlReportFile;
	}
}
