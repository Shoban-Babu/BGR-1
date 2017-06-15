package bg.framework.app.functional.page.reFactoring;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import com.jniwrapper.win32.ie.Browser;
import org.apache.james.mime4j.field.datetime.DateTime;

import bg.framework.app.functional.page.common.BasePage;
import bg.framework.app.functional.util.PropertyLoader;
import bg.framework.app.functional.util.Report;

public class LandlordPhase2Page extends BasePage{
	private final static String SMR_FILE_NAME = "resources/reFactoring/LandlordPhase2.properties";
	private static Properties pageProperties = new PropertyLoader(SMR_FILE_NAME).load();
	
	public void continueLandlord()
	{
		verifyIsElementVisibleWithXpath(pageProperties.getProperty("Landlord.PanelHead"), "Panel Heading");
		verifyIsTextPresent(pageProperties.getProperty("Landlord.PanelHeadText"),"Panel Head Text");
		verifyIsElementVisibleWithXpath(pageProperties.getProperty("Landlord.InnerPanel"), "Inner Panel Heading");
		verifyIsTextPresent(pageProperties.getProperty("Landlord.InnerPanelText"),"Inner Panel Head Text");
		verifyIsElementVisibleById(pageProperties.getProperty("Landlord.ContinueCP12"), "CP12 Continue");
		verifyAndClick(pageProperties.getProperty("Landlord.ContinueCP12"), "CP12 Continue");
	}
	
	@SuppressWarnings("static-access")
	public void continueLandlordOverlay(int intOption, String strSlotType)
	{
		browser.wait(1000);
		if(browser.isElementVisible("certificate"))
		{
		String strDate = browser.getTextByXpath("//*[@id='Gas-Certificate-overlay']/div[1]/h4");
		if(intOption == 0)
		{
			browser.wait(500);
			verifyAndClickWithXpath("//*[@id='Gas-Certificate-overlay' ]/div[4]/div/div[1]/button", "New slot");
			try {
				java.util.Date date = new SimpleDateFormat("dd MMM yyyy",Locale.ENGLISH).parse(strDate);
				Calendar caldate = Calendar.getInstance();
				caldate.setTime(date);
				Date dt1 = (Date)caldate.getTime();
				Date dt2 = new Date();
				if(dt1.compareTo(dt2)<0)
				{
					
				}
				else
				{
				caldate.add(caldate.DATE, -1);
				int intoldDate = caldate.DAY_OF_MONTH;
				
				if(browser.isTextPresent(""+intoldDate))
				{
					//int rowCount=browser.getChildElementsCountByXpath("xpath");
				//	int colCount=browser.getChildElementsCountByXpath("xpath");
					if(strSlotType == "2Hr")
					{
						verifyAndClickWithXpath(pageProperties.getProperty("SAE.2Hrtab"), "Select this appointment");
						for(int j = 0;j<7;j++)
						{
							if(browser.getTextByXpath(pageProperties.getProperty("SAE.2HrSlot").replace("ROWS", ""+j)
									.replace("COLS", "1")).contains(
									""+intoldDate))
							{
								for (int i = 7; i > 0; i--) {

									if (browser.isElementVisibleWithXpath(pageProperties.getProperty(
											"SAE.StdSlot").replace("COLS",""+i ).replace("ROWS", "" + (j-1)))) {
										verifyIsElementVisibleWithXpath(pageProperties.getProperty(
										"SAE.StdSlot").replace("COLS",""+i ).replace("ROWS", "" + (j-1)),
												"Book an appointment page");
										verifyAndClickWithXpath(
												pageProperties.getProperty(
														"SAE.StdSlot").replace("COLS",""+i ).replace("ROWS", "" + (j-1)),
												"Select a slot from available Standard slots");
										verifyIsElementVisibleWithXpath(
												pageProperties.getProperty("SAE.Continue"),
												"Continue button");
										verifyAndClickWithXpath(
												pageProperties.getProperty("SAE.Continue"),
												"Book This Appointment");
										break;
									}
								}
							}
								
									
						}
					}
					else if(strSlotType == "Std")
					{
						verifyAndClickWithXpath(pageProperties.getProperty("SAE.StdTab"), "Select this appointment");
						for(int j = 0;j<7;j++)
						{
							if(browser.getTextByXpath(pageProperties.getProperty("SAE.2HrSlot").replace("ROWS", ""+(j))
									.replace("COLS", "0")).contains(
									""+intoldDate))
							{
								for (int i = 7; i >0; i--) {

									if (browser.isElementVisibleWithXpath(pageProperties.getProperty(
											"SAE.StdSlot").replace("COLS",""+i ).replace("ROWS", "" + (j-1)))) {
										verifyIsElementVisibleWithXpath(pageProperties.getProperty(
										"SAE.StdSlot").replace("COLS",""+i ).replace("ROWS", "" + (j-1)),
												"Book an appointment page");
										verifyAndClickWithXpath(
												pageProperties.getProperty(
														"SAE.StdSlot").replace("COLS",""+i ).replace("ROWS", "" + (j-1)),
												"Select a slot from available Standard slots");
										verifyIsElementVisibleWithXpath(
												pageProperties.getProperty("SAE.Continue"),
												"Continue button");
										verifyAndClickWithXpath(
												pageProperties.getProperty("SAE.Continue"),
												"Book This Appointment");
										break;
									}
								}
							}								
									
						}
					}
						browser.wait(1000);

					}
					
				}
				}
				
			 catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(intOption == 1)
		{
			
			verifyAndClick("keepappointment", "Keep slot");
		}
		}
		else
		{
			Report.updateTestLog("Overlay is not present", "FAIL");
		}
		if(intOption == 3)
		{
			verifyAndClick("keepappointment", "Keep slot-CP12");
		}
		/*
		verifyIsElementVisibleById(pageProperties.getProperty("Landlord.PanelHead"), "Panel Heading");
		verifyIsTextPresent(pageProperties.getProperty("Landlord.PanelHeadText"),"Panel Head Text");
		verifyIsElementVisibleById(pageProperties.getProperty("Landlord.InnerPanel"), "Inner Panel Heading");
		verifyIsTextPresent(pageProperties.getProperty("Landlord.InnerPanelText"),"Inner Panel Head Text");
		verifyIsElementVisibleById(pageProperties.getProperty("Landlord.ContinueCP12"), "CP12 Continue");
		verifyAndClick(pageProperties.getProperty("Landlord.ContinueCP12"), "CP12 Continue");*/
	}
	public void verifyInstallationText()
	{
		if(browser.getTextByXpath("//li").contains("Installation"))
		{
			Report.updateTestLog("Installation text is present", "FAIL");
		}
		else
		{
			Report.updateTestLog("Installation text is not present", "PASS");
		}
	}
	
	public void verifyReviewAppText()
	{
		if(browser.isTextPresent("Boiler"))
		{
			Report.updateTestLog("Boiler text is present", "PASS");
		}		
		
		if(browser.isTextPresent("Suspected Hazardous materials"))
		{
			Report.updateTestLog("Suspected Hazardous materials is present", "PASS");
		}
		
		if(browser.isTextPresent("Warm air and Circulator"))
		{
			Report.updateTestLog("Warm air and Circulator is present", "PASS");
		}
		
		if(browser.isTextPresent("Circulator"))
		{
			Report.updateTestLog("Circulator is present", "PASS");
		}
		
		if(browser.isTextPresent("Central Heating - CHB"))
		{
			Report.updateTestLog("Central Heating - CHB is present", "PASS");
		}
		
	}
	
	public void verifyReviewAppTextHeating()
	{
		if(browser.isTextPresent("Boiler"))
		{
			Report.updateTestLog("Boiler text is present", "PASS");
		}		
		
		if(browser.isTextPresent("Central Heating - BBC"))
		{
			Report.updateTestLog("Central Heating - BBC text is present", "PASS");
		}		
		
		if(browser.isTextPresent("Water heater - CIR  "))
		{
			Report.updateTestLog("Water heater - CIR   is present", "PASS");
		}
		
		if(browser.isTextPresent("Gas cooker"))
		{
			Report.updateTestLog("Gas cooker is present", "PASS");
		}
		
		if(browser.isTextPresent("Gas Hob"))
		{
			Report.updateTestLog("Gas Hob is present", "PASS");
		}		
		
		
}
	
}
