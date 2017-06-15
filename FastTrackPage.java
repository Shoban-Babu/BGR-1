package bg.framework.app.functional.page.reFactoring;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;

import bg.framework.app.functional.entities.UserProfile;
import bg.framework.app.functional.page.common.BasePage;
import bg.framework.app.functional.util.OnlineDBConnector;
import bg.framework.app.functional.util.PropertyLoader;
import bg.framework.app.functional.util.Report;
import bg.framework.app.functional.util.SiebelDataBase;
import bg.framework.common.functional.UIDriver;
import bg.framework.common.functional.WebDriverProvider;

public class FastTrackPage extends BasePage{
	private final static String SMR_FILE_NAME = "resources/reFactoring/FastTrack.properties";
	private static Properties pageProperties = new PropertyLoader(SMR_FILE_NAME).load();
	
		
	public void fastTrackASVLogin(UserProfile userProfile)
	{
		verifyAndInputByXpath(pageProperties.getProperty("FastTrack.UserID"), "Unique Customer number field",userProfile.getBgsAccount());
		verifyAndInputByXpath(pageProperties.getProperty("FastTrack.PostCode"), "Postcode field",userProfile.getaddr());
		verifyAndClickWithXpath(pageProperties.getProperty("FastTrack.Continue"),"Continue object");
	}
	
	public void fastTrackConfirmAddressDetails(UserProfile userProfile)
	{		
		verifyAndClickWithXpath(pageProperties.getProperty("FastTrack.ConfirmContinue"), "Continue Button");
	}
	
	public void verifyNA2Message()
	{		
		verifyIsTextPresent(pageProperties.getProperty("FastTrack.NA2Head")); 
		verifyIsTextPresent(pageProperties.getProperty("FastTrack.NA2Message")); 
		verifyIsTextPresent(pageProperties.getProperty("FastTrack.Register")); 
		verifyIsTextPresent(pageProperties.getProperty("FastTrack.Login")); 
	}
	
	public void verifyNAWMessage()
	{		
		verifyIsTextPresent(pageProperties.getProperty("FastTrack.NA2Head")); 
		verifyIsTextPresent(pageProperties.getProperty("FastTrack.NA2Message")); 
		verifyIsTextPresent(pageProperties.getProperty("FastTrack.Register")); 
		verifyIsTextPresent(pageProperties.getProperty("FastTrack.Login")); 
	}
	
	public void fastTrackSelectAppointment(String strSlotType)
	{
		//verifyIsTextPresent(pageProperties.getProperty("FastTrack.BookPage"),"Choose an appointment page");
				verifyIsTextPresent(pageProperties.getProperty("FastTrack.BookPageSub"),"Choose an appointment page");
				
				
				if(strSlotType.equalsIgnoreCase("ALLDAY"))
				{
					if(browser.isTextPresent("All Day"))
					{
						Report.updateTestLog("All Day slots available in the page", "PASS");
					}
					else
					{
						Report.updateTestLog("All Day slots is not available in the page", "FAIL");
					}
						//verifyIsElementVisibleWithXpath(pageProperties.getProperty("FastTrack.BookLastSlot").replace("COL", ""+7).replace("ROW", ""+5),"All Day slots");
				
								int i=7;
								while(browser.isElementVisibleWithXpath(pageProperties.getProperty("FastTrack.BookContinue"))== false)
								{
									
								if(browser.isElementVisibleWithXpath(pageProperties.getProperty("FastTrack.BookLastSlot").replace("ROW", ""+i).replace("COL", ""+4)))
								{
									verifyAndClickWithXpath(pageProperties.getProperty("FastTrack.BookLastSlot").replace("ROW", ""+i).replace("COL", ""+4), "All Day Slots");
								}
								browser.wait(500);
								i = i - 1;
								if(i == 0)
								{
									break;
								}
								}
				}
				if(strSlotType.equalsIgnoreCase("FF"))
				{
					//verifyIsElementVisibleWithXpath(pageProperties.getProperty("FastTrack.BookLastSlot").replace("COL", ""+7).replace("ROW", ""+),"All Day slots");
						if(browser.isTextPresent("10am - 2pm"))
						{
							Report.updateTestLog("FF slots available in the page", "PASS");
						}
						else
						{
							Report.updateTestLog("FF slots is not available in the page", "FAIL");
						}
								int i=7;
								while(browser.isElementVisibleWithXpath(pageProperties.getProperty("FastTrack.BookContinue"))== false)
								{
									
									/*if(browser.isElementVisibleWithXpath(pageProperties.getProperty("FastTrack.BookLastSlot").replace("calendarRow", "calendarRow"+i).replace("COL", ""+2)))
									{
										verifyAndClickWithXpath(pageProperties.getProperty("FastTrack.BookLastSlot").replace("calendarRow", "calendarRow"+i).replace("COL", ""+2), "FF Day Slots");
									}*/
									
									if(browser.isElementVisibleWithXpath(pageProperties.getProperty("FastTrack.BookLastSlot").replace("ROW", ""+i).replace("COL", ""+2)))
									{
										verifyAndClickWithXpath(pageProperties.getProperty("FastTrack.BookLastSlot").replace("ROW", ""+i).replace("COL", ""+2), "FF Day Slots");
									}
								browser.wait(500);
								i = i - 1;
								if(i == 0)
								{
									
									break;
								}
								}
				}
								verifyAndClickWithXpath(pageProperties.getProperty("FastTrack.BookContinue"),"Continue button");
	}
	
	public void fastTrackReviewPageDetails(UserProfile userProfile)
	{
		verifyIsTextPresent(pageProperties.getProperty("FastTrack.ReviewPage"),"Review Details Page text");
		verifyIsTextPresent(pageProperties.getProperty("FastTrack.ReviewPage1"),"Review Details Page text ");
		verifyIsElementVisibleWithXpath(pageProperties.getProperty("FastTrack.ReviewEmail"),"Email Field");
		verifyAndInputByXpath(pageProperties.getProperty("FastTrack.ReviewEmail"),"Email Field","Kingston.SamSelwyn@cognizant.com");
		verifyIsElementVisibleWithXpath(pageProperties.getProperty("FastTrack.ReviewConfirmEmail"),"Confirm Email Field");
		verifyAndInputByXpath(pageProperties.getProperty("FastTrack.ReviewConfirmEmail"),"Confirm Email Field","Kingston.SamSelwyn@cognizant.com");
				verifyIsElementVisibleWithXpath(pageProperties.getProperty("FastTrack.ReviewContactNumber"),"Contact Number Field");
				verifyAndInputByXpath(pageProperties.getProperty("FastTrack.ReviewContactNumber"),"Contact Number Field",userProfile.getMobileNumber());
						
	}
	
	public void addCOD(UserProfile userProfile) {
		browser.wait(1000);
		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("FastTrack.COD"), "COD");
		verifyAndClickWithXpath(pageProperties.getProperty("FastTrack.COD"),
				"Add COD");
		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("FastTrack.ReviewContinue"),
				"Continue button ");
		verifyAndClickWithXpath(pageProperties.getProperty("FastTrack.ReviewContinue"),
				"Confirm");

		browser.wait(5000);

		payment();

		browser.wait(1000);

		if (browser.getTextByXpath(".//*[@id='h1']").contains(
				"Your annual service is booked")
				|| browser.getTextByXpath(".//*[@id='h1']").contains(
						"Your appointment has been rescheduled")) {
			Report.updateTestLog("Confirmation page is loaded", "PASS");
		} else {
			Report.updateTestLog("Confirmation page is not loaded", "FAIL");

		}
	}
	
	public void payment() {
		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("FastTrack.PopUp"), "Payment popup");

		verifyAndClickWithXpath(pageProperties.getProperty("FastTrack.PopUp"),
				"Payment Popup");
		browser.wait(5000);
		verifyIsElementVisibleById(
				pageProperties.getProperty("FastTrack.CardType"),
				"Payment card type");
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("FastTrack.CardType"), "Card Type",
				"Visa Debit");

		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("FastTrack.CardName"),
				"Payment card Name");
		verifyAndInputByXpath(pageProperties.getProperty("FastTrack.CardName"),
				"Card Name", "Test");

		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("FastTrack.cardNumber"),
				"Payment card number");
		verifyAndInputByXpath(pageProperties.getProperty("FastTrack.cardNumber"),
				"Card Number", "4539791001730106");

		verifyIsElementVisibleById(
				pageProperties.getProperty("FastTrack.cardStartMonth"),
				"Payment card start month field");

		verifyAndSelectDropDownBox(
				pageProperties.getProperty("FastTrack.cardStartMonth"),
				"Start Month", "11");
		verifyIsElementVisibleById(
				pageProperties.getProperty("FastTrack.cardStartYear"),
				"Payment card start year field");
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("FastTrack.cardStartYear"),
				"Card Start Year", "2008");

		verifyIsElementVisibleById(
				pageProperties.getProperty("FastTrack.cardExpiryMonth"),
				"Payment card end month field");
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("FastTrack.cardExpiryMonth"),
				"Card Expiry Month", "3");

		verifyIsElementVisibleById(
				pageProperties.getProperty("FastTrack.cardExpiryYear"),
				"Payment card end year field");
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("FastTrack.cardExpiryYear"),
				"Card Expiry Year", "2014");

		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("FastTrack.cardCVV"),
				"Payment card CVV field");
		verifyAndInputByXpath(pageProperties.getProperty("FastTrack.cardCVV"),
				"Card CVV Number", "123");

		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("FastTrack.submitButton"),
				"Payment submit field");
		verifyAndClickWithXpath(
				pageProperties.getProperty("FastTrack.submitButton"),
				"Submit Button");

		browser.wait(10000);

		UIDriver driver = WebDriverProvider.getCurrentDriver();

		driver.switchTo().frame(
				driver.findElement(By.xpath(".//*[@id='message']/iframe")));
		verifyIsElementVisibleWithXpath(
				"html/body/form/table/tbody/tr/td/table/tbody/tr[4]/td/input",
				"Security Check page");
		verifyIsElementVisibleWithXpath(
				"html/body/form/table/tbody/tr/td/table/tbody/tr[4]/td/input",
				"Security Check submit field");
		verifyAndClickWithXpath(
				"html/body/form/table/tbody/tr/td/table/tbody/tr[4]/td/input",
				"Security check submit button");
		Alert alert =  driver.switchTo().alert();
		alert.accept();
		browser.swtichToDefaultContent();
	}
	
	public void fastTrackNavigateToConfirmation()
	{
		verifyIsElementVisibleWithXpath(pageProperties.getProperty("FastTrack.ReviewContinue"),"Continue Button Field");
		verifyAndClickWithXpath(pageProperties.getProperty("FastTrack.ReviewContinue"),"Continue Button Field");
		
	}
	
	public void addGAC() {
		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("FastTrack.GAC"), "GAC link");
		browser.wait(1000);
		verifyAndClickWithXpath(pageProperties.getProperty("FastTrack.GAC"), "GAC");

		/*verifyIsTextPresent(
				pageProperties.getProperty("FastTrack.GACaddedLink"),
				"Request Call Back");*/
	}
	
	public void fastTrackConfirmationPage()
	{
		verifyIsTextPresent(pageProperties.getProperty("FastTrack.ConfirmationPage"),"Confirmation Page text");
		verifyIsTextPresent(pageProperties.getProperty("FastTrack.ConfirmationPage1"),"Confirmation Page text");
		verifyIsTextPresent(pageProperties.getProperty("FastTrack.ConfirmationPage2"),"Confirmation Page text");
		verifyIsTextPresent(pageProperties.getProperty("FastTrack.ConfirmationPrint"),"Print Page Link");
		verifyIsTextPresent(pageProperties.getProperty("FastTrack.ConfirmationTrackAppointment"),"Track changes appointment link");				
		verifyAndClickWithLinkText(pageProperties.getProperty("FastTrack.ConfirmationTrackAppointment"),"Track Appointment");
	}

	public void loginUser(UserProfile userProfile) {
		/*final String expectedEmailAdd = new OnlineDBConnector()
				.getUserEmail(userProfile.getUCRN());*/
		verifyAndInputById(pageProperties.getProperty("FastTrack.NewEmail"),
				"Email Id", userProfile.getEmail());
		verifyAndInputById(pageProperties.getProperty("FastTrack.NewPassword"),
				"Password", userProfile.getPassword());
		// browser.clickWithClass(pageProperties.getProperty("LoginPage.LoginSubmitClass"));
		browser.clickWithXpath(pageProperties
				.getProperty("FastTrack.NewLoginSubmitXpath"));
		browser.wait(getWaitTime());
		//continueToMyAccount();
	}
	
	public void navigateToAccountSummaryPage(UserProfile userProfile) {
		browser.wait(8000);
		
		verifyAndClickWithXpath(
				(pageProperties.getProperty("FastTrack.ManageAccountXPath").replace(
						"USERACCOUNTNUMBER", userProfile.getAccNumber())),
				"Account summary");
		browser.wait(1000);		

	}
	
	public void verifyWorkReq(String strWorkReq) {
		browser.wait(8000);
		
		verifyIsElementVisibleWithXpath("//a[contains(@href,'"+strWorkReq+"')]","Work Request Number From WMIS");
				
		browser.wait(1000);		

	}
	
	public void accountSummarycancelAppointment() {
		browser.wait(1000);
		
		
		verifyAndClickWithLinkText(
				pageProperties.getProperty("FastTrack.Cancel"),
				"Cancel link");
		int i =1;
		while (browser.isElementVisibleWithXpath(pageProperties
				.getProperty("FastTrack.Confirm")) == false) {
			
			verifyAndClickWithLinkText(
					pageProperties.getProperty("FastTrack.Cancel"),
					"Cancel link");
			browser.wait(2000);
			i++;
			if(i>5)
			{
				Report.updateTestLog("No Cancel appointment link", "FAIL");
				break;
			}
		}
		confirmCancelAppointment();
	}
	
	public void confirmCancelAppointment() {
		browser.wait(2000);
		
		verifyAndClickWithXpath(
				pageProperties.getProperty("FastTrack.Confirm"),
				"Confirm Booking");

		browser.wait(2000);

	

		verifyAndClickWithLinkText(
				pageProperties.getProperty("FastTrack.Return"),
				"Return to Account Summary");
	}

	public void verifyBookedSlotPage() {
		browser.wait(2000);
		verifyIsTextPresent(pageProperties.getProperty("FastTrack.AlreadyBookedInfoMessage"));
		verifyIsTextPresent(pageProperties.getProperty("FastTrack.Register"));
		verifyIsTextPresent(pageProperties.getProperty("FastTrack.Login"));		
		verifyAndClickWithLinkText(pageProperties.getProperty("FastTrack.Register"), "Register Now");
		Report.updateTestLog("Screenshot of register page", "WARN");
		browser.browserBack();
		verifyAndClickWithLinkText(pageProperties.getProperty("FastTrack.Login"), "Login");
		Report.updateTestLog("Screenshot of Login Page", "WARN");
	}

}
