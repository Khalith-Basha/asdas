package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import settings.Common;
import utility.Property;

public class LoginPage {
	
	public static WebDriver driver;
	
	@FindBy(id="sso_username")
	private static WebElement usernameField;
	
	@FindBy(id="sso_password")
	private static WebElement passwordField;
	
	@FindBy(id="sso_submit")
	private static WebElement submitBtn;
	
	@FindBy(xpath="//img[contains(@src,'GlobalNavLogo')]")
	private static WebElement globalApplicationLogo;

	public LoginPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(this.driver, this);
	}
	
	public static boolean isUserOnLoginPage(){
		return Common.isElementDisplayed(usernameField);
	}

	public static void enterUsername() {
		Common.setText(usernameField, Property.getProperty("UserName"));
	}

	public static void enterPassword() {
		Common.setText(passwordField, Property.getProperty("Password"));
	}
	
	public static void clickOnSubmitBtn(){
		Common.explicitWait_Clickable(submitBtn, 60).click();
	}
	
	public static boolean isUserOnHomePage(){
		return Common.isElementDisplayed(globalApplicationLogo);
	}

	
}



