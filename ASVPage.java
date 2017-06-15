package bg.framework.app.functional.page.reFactoring;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import net.sf.saxon.functions.CurrentDateTime;

import bg.framework.app.functional.entities.UserProfile;
import bg.framework.app.functional.page.common.BasePage;
import bg.framework.app.functional.util.OnlineDBConnector;
import bg.framework.app.functional.util.PropertyLoader;
import bg.framework.app.functional.util.Report;
import bg.framework.app.functional.util.SiebelDataBase;

public class ASVPage extends BasePage{
	private final static String SMR_FILE_NAME = "resources/selfServe/ASVIB.properties";
	private static Properties pageProperties = new PropertyLoader(SMR_FILE_NAME)
			.load();
	
	public void checkMobileNumber(UserProfile userProfile)
	{
		if(browser.getTextByXpath(pageProperties.getProperty("ASVIBReview.MobileNumberText"))!="")
		{
			Report.updateTestLog("The Mobile Number Field is not empty", "PASS");
		}
		else
		{
			Report.updateTestLog("The Mobile Number Field is empty", "FAIL");
		}			
		if(new SiebelDataBase().getTelephoneNumber(userProfile.getUCRN()).equalsIgnoreCase(userProfile.getPhoneNumber()))
		{
			Report.updateTestLog("Mobile number is same as in the siebel Database", "PASS");
		}
		else
		{
			Report.updateTestLog("Mobile number is not same as in the siebel Database", "FAIL");
		}
	}
	
	public void checkHomeNumber(UserProfile userProfile)
	{
		verifyIsElementVisibleWithXpath(pageProperties.getProperty("ASVIBReview.HomeNumberText"), "Home Number Text Field");		
		if(browser.getTextByXpath(pageProperties.getProperty("ASVIBReview.HomeNumberText"))!="")
		{
			Report.updateTestLog("The Home Number Field is not empty", "PASS");
		}
		else
		{
			Report.updateTestLog("The Home Number Field is empty", "FAIL");
		}			
		if(new SiebelDataBase().getTelephoneNumber(userProfile.getUCRN()).equalsIgnoreCase(userProfile.getPhoneNumber()))
		{
			Report.updateTestLog("Home number is same as in the siebel Database", "PASS");
		}
		else
		{
			Report.updateTestLog("Home number is not same as in the siebel Database", "FAIL");
		}
	}
	
	public void modifyHomeNumber()
	{
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBReview.HomeNumberText"), "Home Number Text", "0123456789");
	}
	
	public void checkNewMobileNumber(UserProfile userProfile)
	{
		if(browser.getTextByXpath(pageProperties.getProperty("ASVIBReview.MobileNumberText"))!="")
		{
			Report.updateTestLog("The Mobile Number Field is not empty", "PASS");
		}
		else
		{
			Report.updateTestLog("The Mobile Number Field is empty", "FAIL");
		}			
		if(browser.getTextByXpath(pageProperties.getProperty("ASVIBReview.MobileNumberText")).equalsIgnoreCase(userProfile.getMobileNumber()))
		{
			Report.updateTestLog("Mobile number is same as entered already", "PASS");
		}
		else
		{
			Report.updateTestLog("Mobile number is not same as entered already", "FAIL");
		}
	}

	public void verifyBookingDB(UserProfile userProfile,String strType)
	{
		String strAuditDet = null;	
		Calendar currentDate = Calendar.getInstance();
		String monthname[] = new DateFormatSymbols().getMonths();
		SimpleDateFormat oDateFormat = new SimpleDateFormat("yyyy");
		
			String oStrDate = "";			
			oStrDate = oDateFormat.format(currentDate.getTime());
			String monthName[] = new DateFormatSymbols().getMonths();
			if(strType != "Payment")
			{
			String FinalDate = currentDate.get(currentDate.DAY_OF_MONTH)
								+ "-"
								+monthName[currentDate.get(currentDate.MONTH)]
								+"-"
								+ oStrDate;
		
			strAuditDet = "CUSTOMER ID : "
						+userProfile.getAccNumber().substring(0, 8)
						+", APPOINTMENT DATE: "
						+FinalDate
						+", APPOINTMENT SLOT: ";
			}
			
			if(strType == "Payment")
			{
				strAuditDet = currentDate.get(currentDate.DAY_OF_MONTH)
						+ "-"
						+monthName[currentDate.get(currentDate.MONTH)].substring(0,3)
						+"-"
						+ oStrDate.substring(0,2);
			}
			
		int retVal = new OnlineDBConnector().getAuditDetailsCount(userProfile.getAccNumber().substring(8,15), strType, strAuditDet);
		if(retVal == 0)
		{
			Report.updateTestLog("The Value is not entered in the ASV_TA_AUDIT_DETAILS", "FAIL");			
		}
		else if(retVal == 1)
		{
			Report.updateTestLog("The Value is entered in the ASV_TA_AUDIT_DETAILS", "PASS");			
		}
	}
	
}
