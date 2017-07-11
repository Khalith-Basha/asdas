package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import settings.Common;

public class LogOutPage {

public static WebDriver driver;
	
	@FindBy(xpath="//a[contains(text(),'Logout')]")
	private static WebElement logOutBtn;
	
	public LogOutPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(this.driver, this);
	}
	
	public static boolean isUserOnApplication(){
		return Common.isElementDisplayed(logOutBtn);
	}
	
	public static void clickOnLogOutBtn(){
		Common.explicitWait_Clickable(logOutBtn, 60).click();
	}
	
	

}
