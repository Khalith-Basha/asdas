package web.stepdefs;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import pages.LogOutPage;
import pages.LoginPage;

public class LogOutSteps {

	@Given("^User is on the Home Screen$")
    public void user_is_on_Home_screen(){
		LoginPage.isUserOnHomePage();
    }
    
    @When("^User clicks on the logout button$")
    public void User_clicks_on_the_logout_button()  {
  	  LogOutPage.clickOnLogOutBtn();
    }
	
    @Then("^User logout is successful$")
    public void User_logout_is_successful() {
    	LoginPage.isUserOnLoginPage();
    }
}
