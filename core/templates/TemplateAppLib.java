package core.templates;

import core.utilities.Browser;
import core.webwidgets.Link;

/**
 * Standard Visiant Selenium Application Object Library Template file.
 * <P>
 * This class is the template for creating standard Application Object Library file at Visiant for Selenium automation. An Application Object
 * Library file defines and declares UI objects and methods for specific web-based applications.
 */
public class TemplateAppLib {
	/**
	 * Application Library files can contain String Constants to store information specific to a particular test web page.
	 * See example string declared below.
	 * // TODO: add your application specific property variables and values here
	 */
	public static final String visiantWebPage = "http://http://www.ikasystems.com/";
	
	public static final String contactUsText = "Transform the way you do business";

	/**
	 * // TODO: place test object description here
	 * Declares and returns a UI object for a specific
	 * Application Under Test (AUT)
	 * <P>
	 * This is an example Link object declaration for the About link on the Visiant website homepage. You define the name
	 * of the object, using the Visiant common automation naming standards. Then supply the Selenium Locator for the object declaration (i.e.
	 * "//a[contains(.,'Contact Us')]") You can find the object Locator information using the Selenium IDE tool or other open source
	 * tools such as Firebugs, xPathChecker, Web Element Locator etc. Make sure to create the object using the appropriate class
	 * type (i.e TextField for text fields, Link for links, Button for buttons, etc) for the object you are defining.
	 * <P>
	 * // TODO: change return test object info
	 *
	 * @return web UI object of specified type and definition
	 */
	public static Link lnkAbout() {
		return new Link("//span[contains(.,'About')]");
	}

	
	/**
	 * This is an example link object declaration for "Contact Us" link on the Visiant Worldwide website homepage.
	 * <P>
	 * // TODO: change return test object info
	 *
	 * @return web UI object of specified type and definition
	 */
	public static Link lnkContactUs() {
		return new Link("//a[contains(.,'Contact Us')]");
	}
	
	
	/**
	 * // TODO: place application specific method description here
	 * This is an example application specific method that loads the web page
	 */
	public static void loadVisiant() {
		Browser.loadURL(visiantWebPage);
	}

	/**
	 * // TODO: place application specific method description here
	 * This is an example application specific method that enters the contact us page and verifies information on that page
	 * <P>
	 *
	 * @param textToVerify search text to enter into Visiant search text field
	 */
	public static void contactVisiant(final String textToVerify) {
		lnkContactUs().click(); // click Contact Us link
		Browser.validateTextExists(contactUsText);
	}
}
