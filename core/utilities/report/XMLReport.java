
package core.utilities.report;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import core.utilities.XML;
import core.utilities.exceptions.SetupException;

/**
 * The class represents methods for generating xml report file.
 */
public class XMLReport {
	private final Document document;
	private final Path xmlFilePath;

	/**
	 * The class represents specific tags name for XML report file.
	 */
	public static class Tags {
		public final static String ROOT = "REPORT";
		public final static String SUITE = "SUITE";
		public final static String SCRIPT = "SCRIPT";
		public final static String GROUP = "GROUP";
		public final static String TEST_CASE = "TESTCASE";

		public final static String BEFORE = "BEFORE";
		public final static String AFTER = "AFTER";

		public final static String STEP = "STEP";

		public final static String WARNING = "WARN";
		public final static String SKIP = "SKIP";

		public final static String NAME = "NAME";
		public final static String STATISTIC = "STATISTIC";

		public final static String TEST_CASE_LINK = "LINK";

		public final static String TEST_CASE_COMMENTS = "COMMENTS";
		public final static String TEST_CASE_SKIP_COMMENTS = "SKIP_COMMENTS";
		public final static String TEST_CASE_FAIL_COMMENTS = "FAIL_COMMENTS";
	}

	/**
	 * The class represent node attributes.
	 */
	public static class Attributes {
		public final static String ID = "id";
		public final static String STATUS = "status";
		public final static String SCREENSHOT = "screenshot";
	}

	public XMLReport(String fileName) {
		xmlFilePath = Paths.get(fileName + ".xml");

		if (!Files.exists(xmlFilePath)) {
			try {
				Files.createFile(xmlFilePath);
			} catch (Exception e) {
				throw new SetupException(e);
			}
		}

		document = XML.generateDocumentBuilder().getDOMImplementation().createDocument("http://www.w3.org/1999/xml",
				XMLReport.Tags.ROOT, null);

		save();
	}

	public Path getFilePath() {
		return xmlFilePath;
	}

	/**
	 * Adds node for step with screenshot.
	 *
	 * @param idScenario
	 *            scenario id
	 * @param stepDescription
	 *            step information
	 * @param screenshotFileName
	 *            filename for screenshot
	 */
	public void addStep(String idScenario, String stepDescription, String screenshotFileName) {
		if (screenshotFileName == null) {
			XML.addChild(document, idScenario, Tags.STEP, stepDescription);
		} else {
			XML.addChild(document, idScenario, Tags.STEP, stepDescription, Attributes.SCREENSHOT, screenshotFileName);
		}

		save();
	}

	/**
	 * Adds child node tag "STATISTIC" to element by element id.
	 *
	 * @param elementId
	 *            element id
	 * @param value
	 *            statistic information
	 */
	public void addStatistic(String elementId, String value) {
		XML.addChild(document, elementId, Tags.STATISTIC, value);

		save();
	}

	/**
	 * Adds pass attribute "status" to element by element id.
	 *
	 * @param elementId
	 *            element id
	 */
	public void addPassStatus(String elementId) {
		addStatus(elementId, TestStatuses.PASS);
	}

	/**
	 * Adds fail attribute "status" to element by element id.
	 *
	 * @param elementId
	 *            element id
	 */
	public void addFailStatus(String elementId) {
		addStatus(elementId, TestStatuses.FAIL);
	}

	/**
	 * Adds warning attribute "status" to element by element id.
	 *
	 * @param elementId
	 *            element id
	 */
	public void addWarningStatus(String elementId) {
		addStatus(elementId, Tags.WARNING);
	}

	/**
	 * Adds skip attribute "status" to element by element id.
	 *
	 * @param elementId
	 *            element id
	 */
	public void addSkipStatus(String elementId) {
		addStatus(elementId, Tags.SKIP);
	}

	/**
	 * Adds attribute "status" to element by element id.
	 *
	 * @param elementId
	 *            element id
	 * @param status
	 *            status name
	 */
	private void addStatus(String elementId, String status) {
		XML.setAttribute(document, elementId, Attributes.STATUS, status);

		save();
	}

	public Document save() {
		XML.saveToFile(document, xmlFilePath);

		return document;
	}

	/**
	 * Creates element in DOM structure with attribute id and child node with
	 * tag "NAME".
	 *
	 * @param parentId
	 *            parent element id
	 * @param elementName
	 *            element name
	 * @param name
	 *            text value in child element with tag "NAME"
	 * @param id
	 *            element id
	 */
	public void addElement(String parentId, String elementName, String name, String id) {
		Element parent = parentId.equals(Tags.ROOT) ? document.getDocumentElement() : document.getElementById(parentId);

		Element child = XML.createChildElement(document, parent, elementName);
		child.setAttributeNode(XML.createAttribute(document, Attributes.ID, id));
		child.setIdAttribute(Attributes.ID, true);

		child.appendChild(XML.createNode(document, Tags.NAME, name));

		save();
	}

	/**
	 * Adds node for test-case link.
	 *
	 * @param idTestCase
	 *            test-case id
	 * @param testCaseLink
	 *            link to test case information and steps
	 */
	public void addTestCaseLink(String idTestCase, String testCaseLink) {
		XML.addChild(document, idTestCase, Tags.TEST_CASE_LINK, testCaseLink);

		save();
	}

	/**
	 * Adds node for test-case fail comments.
	 *
	 * @param idTestCase
	 *            test-case id
	 * @param comments
	 *            test case information
	 */
	public void addTestCaseFailComments(String idTestCase, String comments) {
		addTestCaseComments(idTestCase, Tags.TEST_CASE_FAIL_COMMENTS, comments);
	}

	/**
	 * Adds node for test-case skip comments.
	 *
	 * @param idTestCase
	 *            test-case id
	 * @param comments
	 *            skip test case information
	 */
	public void addTestCaseSkipComments(String idTestCase, String comments) {
		addTestCaseComments(idTestCase, Tags.TEST_CASE_SKIP_COMMENTS, comments);
	}

	/**
	 * Adds node for test-case comments.
	 *
	 * @param idTestCase
	 *            test-case id
	 * @param comments
	 *            test case information
	 */
	public void addTestCaseComments(String idTestCase, String comments) {
		addTestCaseComments(idTestCase, Tags.TEST_CASE_COMMENTS, comments);
	}

	/**
	 * Adds node for test-case comments.
	 *
	 * @param idTestCase
	 *            test-case id
	 * @param tag
	 *            tag for node
	 * @param comments
	 *            test case information
	 */
	private void addTestCaseComments(String idTestCase, String tag, String comments) {
		XML.addChild(document, idTestCase, tag, comments);

		save();
	}

	/**
	 * Checks tag exists by name.
	 *
	 * @param tagName
	 *            tag name
	 * @return true - if tag exists, false - if not
	 */
	public boolean isExists(String tagName) {
		return XML.isExists(document, tagName);
	}
}
