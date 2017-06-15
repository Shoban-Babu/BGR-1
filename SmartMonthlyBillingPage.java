package bg.framework.app.functional.page.reFactoring;

import java.util.ArrayList;
import java.util.Properties;

import bg.framework.app.functional.common.ApplicationConfig;
import bg.framework.app.functional.page.common.BasePage;
import bg.framework.app.functional.util.PropertyLoader;
import bg.framework.app.functional.util.Report;

public class SmartMonthlyBillingPage extends BasePage
{
private final static String FILE_NAME = "resources/ReFactoring/BGAcquisition.properties";
private static Properties pageProperties = new PropertyLoader(FILE_NAME).load();

	public void navigateToSMB(){
	//browser.open("http://10.224.70.104/content/britishgas/youraccount/smart-monthly-billing/smblandingpage.html");
    browser.open("http://10.224.70.104/smartmonthlybilling");
	//verifyAndClickWithXpath(pageProperties.getProperty("SMB.SMBLink"), "Link");
	}
	
	public void navigateToSMBnegativetemp(ArrayList<String> errList){
		browser.open("http://10.224.70.83/content/britishgas/youraccount/smart-monthly-billing/smblandingpage.html");
		verifyAndClickWithXpath(pageProperties.getProperty("SMB.SMBLink"), "Link");		
		//verifyIsElementVisibleById(pageProperties.getProperty("SMB.SelectAddress"), "Address drop down box");
    	
		if (!browser.isElementVisible(pageProperties.getProperty("SMB.SelectAddress"))) {
   		   	Report.updateTestLog("SMB link is not displayed" , "PASS");
   	    } else {
   	        Report.updateTestLog("SMB link is displayed", "FAIL");
   	    }
		new RegistrationPage().verifyErrorMsg(browser.getTextByXpath("//*[@class='error']/ul[1]")
				,errList.get(0),"Registration Page Error");
		}
		
	public void verifySMBLink()	{
		if(browser.isTextPresent("SmartMonthlyBilling")){
			Report.updateTestLog("SMB Link is Present", "FAIL");}
		else{
			Report.updateTestLog("SMB Link is not Present", "PASS");}	
	}


public void navigateToManageAccount(ArrayList<String> errList){
	verifyAndClickWithXpath(pageProperties.getProperty("SMB.SMBLink"), "Link");		
	//verifyIsElementVisibleById(pageProperties.getProperty("SMB.SelectAddress"), "Address drop down box");
	

}
	public void verifyTrilliantErrorMsg(){
		if (browser.isElementVisibleWithXpath(pageProperties.getProperty("SMB.TrillianErrorMessage"))) {
			Report.updateTestLog("error screen", "WARN");
			String errrorText=browser.getTextByXpath(pageProperties.getProperty("SMB.TrillianErrorMessage"));
			System.out.println("error text "+errrorText);
		   	Report.updateTestLog("Error Message is dispalyed: "+errrorText , "PASS");
		   	
	    } else if
	        /*Report.updateTestLog("Error Message is not dispalyed", "FAIL");
	        Report.updateTestLog("error screen", "WARN");
	    }*/
		(browser.isElementVisibleWithXpath(pageProperties.getProperty("SMB.ElecAccountIneligibleError"))){
			String errrorText=browser.getTextByXpath(pageProperties.getProperty("SMB.ElecAccountIneligibleError"));
			Report.updateTestLog("error screen for one account", "WARN");
			System.out.println("error text "+errrorText);
			Report.updateTestLog("Error Message is dispalyed for one account: "+errrorText , "PASS");
		} else {
			Report.updateTestLog("Error Message is not dispalyed", "FAIL");
			Report.updateTestLog("error screen ", "WARN");
			}
		}

}