package bg.framework.app.functional.page.reFactoring;

import java.util.List;
import java.util.Properties;

import org.openqa.selenium.support.ui.Select;

import bg.framework.app.functional.common.ApplicationConfig;
import bg.framework.app.functional.entities.UserProfile;
import bg.framework.app.functional.page.common.BasePage;
import bg.framework.app.functional.util.PropertyLoader;
import bg.framework.app.functional.util.Report;
import bg.framework.app.functional.util.RobotSendKeys;

public class ViewBillPage extends BasePage{
	private final static String File_AccPage = "resources/ReFactoring/ViewBill.properties";
	private static final String Bill = null;
	private static Properties pageProperties = new PropertyLoader(File_AccPage).load();
	static int billsCount;
	static String BillDate;

	public void viewbillfordual() {
		List<String> Accounts = browser.getFromDropBox("id", pageProperties.getProperty("ViewBill.AccountsType"));
		for (int x = 0; x < Accounts.size(); x++) {
			browser.selectfromDropBox("id",pageProperties.getProperty("ViewBill.AccountsType"), Accounts.get(x));
			Report.updateTestLog(Accounts.get(x)+ " is Selected", "PASS");
			verifyAndClickWithXpath(pageProperties.getProperty("ViewBill.Go1"), "Go");
			browser.wait(2000);
			navigateToBillHistory();
			verifyAndViewBills();
			viewDifferentStatements();
		}
	}   
	public void clickSeeYourStatementLink(){
		browser.wait(2000);
		String link=browser.getTextByXpath(pageProperties.getProperty("ViewBill.SeeYourLatestStatement"));
		verifyAndClickWithXpath(pageProperties.getProperty("ViewBill.SeeYourLatestStatement"), link);
		verifyPageTitle("View bill/statement");
		Report.updateTestLog("We are able to view the latest Bill/Statement", "PASS");
	}
	public void navigateToBillHistory(){

		browser.wait(2000);
		verifyAndClickWithXpath(pageProperties.getProperty("ViewBill.History"), "BillingHistory");

	}
	public void navigateToDeeplink(UserProfile userProfile){
		String ACC_NUM=userProfile.getAccNumber();

		browser.open(ApplicationConfig.APP_BG_URL+"/View-Bill/Bill-Details/?accountNumber="+ACC_NUM);

	}
	public void verifyViewBillPage(){
		verifyIsTextPresent( "Bill summary");
		browser.wait(2000);
		Report.updateTestLog("Bill summary present in the application","Pass");
	}

	public void verifyAndViewBills(){
		if(browser.isElementVisibleWithXpath(pageProperties.getProperty("ViewBill.BillTable"))){
			Report.updateTestLog("The Bill History Table is present in the application", "PASS");


			billsCount = browser.getChildElementsCountByXpath(pageProperties.getProperty("ViewBill.BillTable"));
			if(browser.isElementVisibleWithXpath(pageProperties.getProperty("ViewBill.ViewBills").replace("COUNT",""+1)));
			{
				Report.updateTestLog("The Latest bill is present in the application", "PASS");
				verifyAndClickWithXpath(pageProperties.getProperty("ViewBill.ViewBills").replace("COUNT",""+1), "latest Bill");

			}
		}
		else if(browser.isElementVisibleWithXpath(pageProperties.getProperty("ViewBill.BillUnavailable")))
		{
			Report.updateTestLog("The Bill History Table is not available for the account", "PASS");
		}

	}

	public void viewDifferentStatements(){

		for(int i=1;i<=billsCount;i++)
		{
			BillDate=browser.getTextByXpath(pageProperties.getProperty("ViewBill.SelectDifferentStatement")
					.replace("BILLNO",""+i));
			verifyAndSelectDropDownBox(pageProperties.getProperty("ViewBill.ViewDifferentStatement"), "Different Statements",BillDate);
			verifyAndClickWithXpath(pageProperties.getProperty("ViewBill.Go"), "Go");
			verifyAndClickWithXpath(pageProperties.getProperty("ViewBill.DownloadStatement"),"Download Link For Bill");

			browser.wait(1000);
			//TODO saveBills();
			browser.wait(2000);
			RobotSendKeys.typeEscape();
			browser.wait(2000);
			NavigatingToLinks();
		}
	}  
	public void NavigatingToLinks()
	{
		browser.wait(3000);
		if(browser.isElementVisibleWithXpath(pageProperties.getProperty("ViewBill.ViewYourPaymentHistoryLink")))
		{
			browser.wait(2000);
			String Link=browser.getTextByXpath(pageProperties.getProperty("ViewBill.ViewYourPaymentHistoryLink"));
			verifyAndClickWithXpath(pageProperties.getProperty("ViewBill.ViewYourPaymentHistoryLink"), BillDate, (Link + " Link"));
			browser.wait(2000);	
			if(Link.equalsIgnoreCase("Make a payment"))
				verifyPageTitle("Make a payment");
			else
				verifyPageTitle("Payment history");
			browser.browserBack();
		}
		browser.wait(2000);
		verifyAndClickWithXpath(pageProperties.getProperty("ViewBill.ViewYourEnergyUsageLink"), BillDate, "View Your Energy Usage Link");
		browser.wait(2000);	
		verifyPageTitle("Energy usage");
		browser.browserBack();
		browser.wait(2000);	
		verifyAndClickWithXpath(pageProperties.getProperty("ViewBill.MeterReadHistoryLink"), BillDate, "Meter Read History Link");
		browser.wait(2000);	
		verifyPageTitle("Meter read history");
		browser.browserBack();
		browser.wait(2000);
		if(browser.isElementVisibleWithXpath(pageProperties.getProperty("ViewBill.RequestCopyBillLink"))){
			verifyAndClickWithXpath(pageProperties.getProperty("ViewBill.RequestCopyBillLink"), BillDate, "Request Copy Bill Link");
		}
	}


	public void saveBills()
	{
		RobotSendKeys.typeenter();
		browser.wait(5000);

		RobotSendKeys.altS();
		browser.wait(5000);
		RobotSendKeys.typeenter();
		RobotSendKeys.altY();
		browser.wait(5000);
		RobotSendKeys.releseShift();
		browser.wait(5000);
	}

	public void logout() {
		if (browser.isElementVisibleWithXpath(pageProperties.getProperty("ViewBill.Logout"))){
			Report.updateTestLog("View Bill Page", "WARN");
			browser.clickWithXpath(pageProperties.getProperty("ViewBill.Logout"));
			System.out.println("Selected logout");

		}
	}
	public void navigatetoElectricity() {
		Select select = new Select(browser.getElementByXpath(pageProperties.getProperty("ViewBill.AcctType")));
		select.selectByIndex(1);
		verifyAndClickWithXpath(pageProperties.getProperty("ViewBill.Go1"), "Go");
		Report.updateTestLog("Electricity Selected from Dropdown", "Pass");
	}
	
	/*-----------------------------------------------------SMB Phase 2--------------------------------------------------*/
	
	public void clickViewLatestBill(){
		verifyAndClickWithXpath(pageProperties.getProperty("ViewBill.ViewLatestBillLink"), "View Latest Bill Link");
	}
	
	public void verifySMBPromotionalMsgVBill(){
		if(browser.isElementVisibleWithXpath(pageProperties.getProperty("ViewBill.SMBPromotionalMsg"))){
			Report.updateTestLog("SMB Promotional Msg exixts in the View Bill Page ", "PASS");
		}else{
			Report.updateTestLog("SMB Promotional Msg does not exixts in the View Bill Page", "FAIL");
		}
		
	}
	
}


