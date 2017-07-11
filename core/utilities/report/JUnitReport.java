
package core.utilities.report;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.GregorianCalendar;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import core.utilities.Log;
import core.utilities.XML;
import core.utilities.exceptions.SetupException;

public class JUnitReport {
	private Document document;
	private final Path xmlFilePath;

	private Element testSuite = null;

	private String currentTest;

	private boolean setFail = false;
	private String errorMessage = "";
	private Exception exception;

	private int result = 0;
	private int error = 0;

	private static ArrayDeque<Element> suite = new ArrayDeque<>();

	private class Tags {
		private static final String TEST_SUITE = "testsuite";
		private static final String TEST_CASE = "testcase";
		private static final String TEST_FAILURE = "failure";
	}

	private class Attr {
		private static final String NAME = "name";
		private static final String CLASS_NAME = "classname";
		private static final String ERRORS = "errors";
		private static final String TIME = "time";
		private static final String TYPE = "type";
		private static final String HOSTNAME = "hostname";
		private static final String SKIPPED = "skipped";
		private static final String TESTS = "tests";
		private static final String FAILURES = "failures";
		private static final String TIMESTEMP = "timestamp";
	}

	/**
	 * Constructor for JUnitReport object.
	 *
	 * @param fileName
	 *            path to report file.
	 */
	public JUnitReport(String fileName) {
		xmlFilePath = Paths.get(Paths.get(fileName).getParent().toString(),
				Paths.get(fileName).getFileName() + "-JUnit.xml");

		if (!Files.exists(xmlFilePath)) {
			try {
				Files.createFile(xmlFilePath);
			} catch (Exception e) {
				throw new SetupException(e);
			}
		}

		document = XML.generateDocumentBuilder().getDOMImplementation().createDocument(null, null, null);
	}

	/**
	 * Returns current test script(suite) name.
	 *
	 * @return test suite name
	 */
	public Element getTestSuite() {
		return testSuite;
	}

	/**
	 * Sets information about script(suite).
	 *
	 * @param name
	 *            test script(suite) name.
	 */
	public void testSuite(String name) {
		testSuite = document.createElement(Tags.TEST_SUITE);
		testSuite.setAttribute(Attr.ERRORS, "0");
		testSuite.setAttribute(Attr.HOSTNAME, Log.getLocalClientName());
		testSuite.setAttribute(Attr.SKIPPED, "0");
		testSuite.setAttribute(Attr.FAILURES, "0");
		testSuite.setAttribute(Attr.TESTS, "0");
		testSuite.setAttribute(Attr.NAME, name);
		testSuite.setAttribute(Attr.TIMESTEMP,
				new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(GregorianCalendar.getInstance().getTime()));
		document.appendChild(testSuite);
	}

	public void testSuite(String name, Element parentSuite) {
		suite.add(testSuite);

		Element childSuite = document.createElement(Tags.TEST_SUITE);
		childSuite.setAttribute(Attr.ERRORS, "0");
		childSuite.setAttribute(Attr.HOSTNAME, Log.getLocalClientName());
		childSuite.setAttribute(Attr.SKIPPED, "0");
		childSuite.setAttribute(Attr.FAILURES, "0");
		childSuite.setAttribute(Attr.TESTS, "0");
		childSuite.setAttribute(Attr.NAME, name);
		childSuite.setAttribute(Attr.TIMESTEMP,
				new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(GregorianCalendar.getInstance().getTime()));
		parentSuite.appendChild(childSuite);

		testSuite = childSuite;
	}

	/**
	 * Sets information about test case.
	 *
	 * @param description
	 *            test case description(name)
	 */
	public void startTest(String description) {
		setSetFail(false);
		currentTest = description;
	}

	/**
	 * Sets exception.
	 *
	 * @param exception
	 *            error
	 */
	public void setException(Exception exception) {
		this.exception = exception;
	}

	/**
	 * Checks if test is fail.
	 *
	 * @return true - if test is fail.
	 */
	public boolean isSetFail() {
		return setFail;
	}

	/**
	 * Sets test fail.
	 *
	 * @param setFail
	 *            failure
	 */
	public void setSetFail(boolean setFail) {
		this.setFail = setFail;
	}

	/**
	 * Writes test case execution results.
	 *
	 * @param elapsedTime
	 *            time spending for execution test case
	 */
	public void stopTest(String elapsedTime) {
		Element testresult = document.createElement(Tags.TEST_CASE);

		testresult.setAttribute(Attr.CLASS_NAME, testSuite.getAttribute(Attr.NAME));
		testresult.setAttribute(Attr.NAME, currentTest);

		if (isSetFail()) {
			Element failure = document.createElement(Tags.TEST_FAILURE);
			failure.setAttribute(Attr.TYPE, errorMessage);

			if (exception != null) {
				failure.setTextContent(exception.toString());
			}

			testresult.appendChild(failure);

			error++;

			testSuite.setAttributeNode(XML.createAttribute(document, Attr.FAILURES, String.valueOf(error)));
		}

		testresult.setAttributeNode(XML.createAttribute(document, Attr.TIME, String.valueOf(elapsedTime)));

		testSuite.appendChild(testresult);

		result++;

		testSuite.setAttributeNode(XML.createAttribute(document, Attr.TESTS, String.valueOf(result)));
	}

	/**
	 * Sets error message.
	 *
	 * @param errorMessage
	 *            error message
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * Saves report into the file.
	 *
	 * @param elapsedTime
	 *            time elapsed
	 */
	public void finishSuite(String elapsedTime) {
		testSuite.setAttributeNode(XML.createAttribute(document, Attr.TIME, elapsedTime));

		if (!suite.isEmpty()) {
			testSuite = suite.getLast();
			suite.removeLast();
		}

		XML.saveToFile(document, xmlFilePath);
	}
}
