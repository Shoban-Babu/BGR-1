package bg.framework.app.functional.page.reFactoring;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

import bg.framework.app.functional.entities.UserProfile;
import bg.framework.app.functional.page.common.BasePage;
import bg.framework.app.functional.util.PropertyLoader;
import bg.framework.app.functional.util.Report;

public class ServiceAndExperiencePage extends BasePage{
	private final static String SMR_FILE_NAME = "resources/reFactoring/ServiceAndExperience.Properties";
private static Properties pageProperties = new PropertyLoader(SMR_FILE_NAME).load();

	public void navigateToASV2HrSlot(UserProfile userProfile)
	{
		verifyIsElementVisibleWithXpath(pageProperties.getProperty("SAE.Book2HrASV"), "ASV 2 Hour Slots link in Account summary Page");
		verifyAndClickWithXpath(pageProperties.getProperty("SAE.Book2HrASV"), "ASV 2 Hour Slots link in Account summary Page");
		
	}
	
	public void navigateToASVStdSlot(UserProfile userProfile)
	{
		verifyIsElementVisibleWithXpath(pageProperties.getProperty("SAE.BookStdASV"), "ASV Standard Slots link in Account summary Page");
		verifyAndClickWithXpath(pageProperties.getProperty("SAE.BookStdASV"), "ASV Standard Slots link in Account summary Page");
		
	}

	//******************************Appointment Selection*********************

	public void selectThisAppointment(String strSlotType)
	{
		browser.wait(1000);
	if(strSlotType == "2Hr")
	{
		verifyAndClickWithXpath(pageProperties.getProperty("SAE.BookAvailable2Hr"), "Select this appointment");
	}
	else if(strSlotType == "Std")
	{
		verifyAndClickWithXpath(pageProperties.getProperty("SAE.BookAvailableStd"), "Select this appointment");
	}
		browser.wait(1000);

	}
	
	public void selectSlotType(String strSlotType)
	{
		if(strSlotType == "Std")
		{
		verifyAndClickWithXpath(pageProperties.getProperty("SAE.StdTab"), "Standard Slot tab in the slot table");
		}
		if(strSlotType == "2Hr")
		{
		verifyAndClickWithXpath(pageProperties.getProperty("SAE.2HrTab"), "2 Hour Slot tab in the slot table");
		}
	}
	
	public void verifyCalendarPage()
	{
		/*verifyIsTextPresent(pageProperties
				.getProperty("SAE.StdSlotsAvailable"));	*/
		verifyIsElementVisibleWithXpath(pageProperties.getProperty("SAE.BookAvailable2Hr"), "First available 2 hour slot link");
		verifyIsElementVisibleWithXpath(pageProperties.getProperty("SAE.BookAvailableStd"), "First available 2preference slot link");
		
		for (int i = 4; i > 0; i--) {
			for(int j=6;j>0;j--)
			{
			if (browser.isElementVisibleWithXpath(pageProperties.getProperty(
					"SAE.StdSlot").replace("COLS",""+j ).replace("ROWS", "" + i))) {
				verifyIsElementVisibleWithXpath(pageProperties.getProperty(
				"SAE.StdSlot").replace("COLS",""+j ).replace("ROWS", "" + i),
						"Slots in the slot table");
				
				verifyIsElementVisibleWithXpath(
						pageProperties.getProperty("SAE.Continue"),
						"Continue button");
				
				break;
			}
			}
		}
	}
	
	public void selectAnAppointmentStd(String strSlotType) {
		
		browser.wait(500);
		verifyCalendarPage();
		verifyAndClickWithXpath(pageProperties.getProperty("SAE.StdTab"), "Standard Slot tab in the slot table");
		
		String strRowNum = "0";
		if(strSlotType == "AllDay")
		{
			strRowNum = "4";
		}
		
		if(strSlotType == "AllDay")
		{
			if(browser.isTextPresent("8am - 6pm"))
			{
				Report.updateTestLog("All Day slots available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("All Day slots is not available in the page", "FAIL");
			}
			strRowNum = "4";
		}
		
		if(strSlotType == "FF")
		{
			if(browser.isTextPresent("10am - 2pm"))
			{
				Report.updateTestLog("FF slots available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("FF slots is not available in the page", "FAIL");
			}
			strRowNum = "2";
		}
		
		if(strSlotType == "EVE")
		{
			if(browser.isTextPresent("6pm - 8pm"))
			{
				Report.updateTestLog("Eve slots available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("Eve slots is not available in the page", "FAIL");
			}
			strRowNum = "5";
		}
		
		if(strSlotType == "PM")
		{
			if(browser.isTextPresent("12pm - 6pm"))
			{
				Report.updateTestLog("PM slots available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("PM slots is not available in the page", "FAIL");
			}
			strRowNum = "3";
		}
		
		
		if(strSlotType == "AM")
		{
			if(browser.isTextPresent("8am - 1pm"))
			{
				Report.updateTestLog("8am - 1pm available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("8am - 1pm is not available in the page", "FAIL");
			}
			strRowNum = "1";
		}		
		
		
		int intPanelCount = 1;
		String slotPanel = "preftab"+intPanelCount;
		for (int i = 7; i > 0; i--) {

			if (browser.isElementVisibleWithXpath(pageProperties.getProperty(
					"SAE.StdSlot").replace("PANEL", slotPanel).replace("COLS",strRowNum ).replace("ROWS", "" + i))) {
				System.out
						.println(pageProperties.getProperty(
						"SAE.StdSlot").replace("PANEL", slotPanel).replace("COLS",strRowNum ).replace("ROWS", "" + i));
				verifyIsElementVisibleWithXpath(pageProperties.getProperty(
				"SAE.StdSlot").replace("PANEL", slotPanel).replace("COLS",strRowNum ).replace("ROWS", "" + i),
						"Book an appointment page");
				verifyAndClickWithXpath(
						pageProperties.getProperty(
								"SAE.StdSlot").replace("PANEL", slotPanel).replace("COLS",strRowNum ).replace("ROWS", "" + i),
						"Select a slot from available Standard slots");
				verifyIsElementVisibleWithXpath(
						pageProperties.getProperty("SAE.Continue"),
						"Continue button");
				verifyAndClickWithXpath(
						pageProperties.getProperty("SAE.Continue"),
						"Book This Appointment");
				break;
			}
			else
			{
				if(i == 1)
				{
					verifyAndClick("prefnext","Later slots in the slot table");
					i = 7;
					intPanelCount = intPanelCount +1;
					slotPanel = "preftab"+intPanelCount;
				}
			}
		}

	}

	public void selectAnAppointment2Hr(String strSlotType) {
		browser.wait(1000);
		
		verifyIsTextPresent(pageProperties
				.getProperty("SAE.2HrSlotsAvailable"));
		verifyAndClickWithXpath(pageProperties.getProperty("SAE.2HrTab"), "2 Hour Slot tab in the slot table");
		String strRowNum = "0";
		
		
		if(strSlotType == "PMElec")
		{
			if(browser.isTextPresent("4pm - 6pm"))
			{
				Report.updateTestLog("PM slots for elec (4pm - 6pm) available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("PM slots for elec (4pm - 6pm is not available in the page", "FAIL");
			}
			strRowNum = "5";
		}
		if(strSlotType == "4to6")
		{
			if(browser.isTextPresent("4pm - 6pm"))
			{
				Report.updateTestLog("PM slots for elec (4pm - 6pm) available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("PM slots for elec (4pm - 6pm is not available in the page", "FAIL");
			}
			strRowNum = "5";
		}
		if(strSlotType == "6to8")
		{
			if(browser.isTextPresent("6pm - 8pm"))
			{
				Report.updateTestLog("6pm to 8pm slots available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("6pm to 8pm slots is not available in the page", "FAIL");
			}
			strRowNum = "6";
		}
		
		if(strSlotType == "PM2to4")
		{
			if(browser.isTextPresent("2pm - 4pm"))
			{
				Report.updateTestLog("2pm - 4pm available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("2pm - 4pm is not available in the page", "FAIL");
			}
			strRowNum = "4";
		}
		if(strSlotType == "2to4")
		{
			if(browser.isTextPresent("2pm - 4pm"))
			{
				Report.updateTestLog("2pm - 4pm available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("2pm - 4pm is not available in the page", "FAIL");
			}
			strRowNum = "4";
		}
		if(strSlotType == "12to2")
		{
			if(browser.isTextPresent("12pm - 2pm"))
			{
				Report.updateTestLog("12pm - 2pm available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("12pm - 2pm is not available in the page", "FAIL");
			}
			strRowNum = "3";
		}
		
		if(strSlotType == "10to12")
		{
			if(browser.isTextPresent("10am - 12pm"))
			{
				Report.updateTestLog("10am - 12pm available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("10am - 12pm is not available in the page", "FAIL");
			}
			strRowNum = "2";
		}
				
		if(strSlotType == "8to10")
		{
			if(browser.isTextPresent("8am - 10am"))
			{
				Report.updateTestLog("8am - 10am available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("8am - 10am is not available in the page", "FAIL");
			}
			strRowNum = "1";
		}
		
		
		
		int intPanelCount = 1;
		String slotPanel = "tab"+intPanelCount;
		for (int i = 7; i > 0; i--) {

			if (browser.isElementVisibleWithXpath(pageProperties.getProperty(
					"SAE.2HrSlot").replace("COLS",strRowNum ).replace("ROWS", "" + i))) {
				verifyIsElementVisibleWithXpath(pageProperties.getProperty(
				"SAE.2HrSlot").replace("COLS",strRowNum ).replace("ROWS", "" + i),
						"Book an appointment page");
				verifyAndClickWithXpath(
						pageProperties.getProperty(
								"SAE.2HrSlot").replace("COLS",strRowNum ).replace("ROWS", "" + i),
						"Select a slot from available 2Hr slots");
				verifyIsElementVisibleWithXpath(
						pageProperties.getProperty("SAE.Continue"),
						"Continue button");
				verifyAndClickWithXpath(
						pageProperties.getProperty("SAE.Continue"),
						"Book This Appointment");
				break;
			}
			else
			{
				if(i == 1)
				{
					verifyAndClick("next","Later slots in the slot table");
					i = 7;
					intPanelCount = intPanelCount +1;
					slotPanel = "tab"+intPanelCount;
				}
			}
		}

	}
	
	public void selectFirstAvailable2Hr()
	{
		verifyIsElementVisibleWithXpath(pageProperties.getProperty("SAE.BookAvailable2Hr"), "First available slot link for 2 hour slots link");
		verifyAndClickWithXpath(pageProperties.getProperty("SAE.BookAvailable2Hr"), "First available slot link for 2 hour slots link");
	}
	
	public void selectFirstAvailableStd()
	{
		verifyIsElementVisibleWithXpath(pageProperties.getProperty("SAE.BookAvailableStd"), "First available slot link for standard slots link");
		verifyAndClickWithXpath(pageProperties.getProperty("SAE.BookAvailableStd"), "First available slot link for standard slots link");
	}
	
	public void selectAnAppointmentReschedule(String strSlotType) {
		browser.wait(1000);
	
		String strRowNum = "0";
		if(strSlotType == "AllDay")
		{
			strRowNum = "4";
		}
		
		if(strSlotType == "AllDay")
		{
			if(browser.isTextPresent("All day 8am - 6pm"))
			{
				Report.updateTestLog("All Day slots available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("All Day slots is not available in the page", "FAIL");
			}
			strRowNum = "4";
		}
		
		if(strSlotType == "FF")
		{
			if(browser.isTextPresent("10am - 2pm"))
			{
				Report.updateTestLog("FF slots available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("FF slots is not available in the page", "FAIL");
			}
			strRowNum = "2";
		}
		
		if(strSlotType == "EVE")
		{
			if(browser.isTextPresent("6pm - 8pm"))
			{
				Report.updateTestLog("Eve slots available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("Eve slots is not available in the page", "FAIL");
			}
			strRowNum = "5";
		}
		
		if(strSlotType == "PM")
		{
			if(browser.isTextPresent("12pm - 6pm"))
			{
				Report.updateTestLog("PM slots available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("PM slots is not available in the page", "FAIL");
			}
			strRowNum = "3";
		}
		
		if(strSlotType == "PMElec")
		{
			if(browser.isTextPresent("4pm - 6pm"))
			{
				Report.updateTestLog("PM slots for elec (4pm - 6pm) available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("PM slots for elec (4pm - 6pm is not available in the page", "FAIL");
			}
			strRowNum = "5";
		}
		
		if(strSlotType == "PM2to4")
		{
			if(browser.isTextPresent("2pm - 4pm"))
			{
				Report.updateTestLog("2pm - 4pm available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("2pm - 4pm is not available in the page", "FAIL");
			}
			strRowNum = "4";
		}
		if(strSlotType == "12to2")
		{
			if(browser.isTextPresent("12pm - 2pm"))
			{
				Report.updateTestLog("12pm - 2pm available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("12pm - 2pm is not available in the page", "FAIL");
			}
			strRowNum = "3";
		}
		
		if(strSlotType == "10to12")
		{
			if(browser.isTextPresent("10am - 12pm"))
			{
				Report.updateTestLog("10am - 12pm available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("10am - 12pm is not available in the page", "FAIL");
			}
			strRowNum = "2";
		}
		
		if(strSlotType == "AM")
		{
			if(browser.isTextPresent("8am - 1pm"))
			{
				Report.updateTestLog("8am - 1pm available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("8am - 1pm is not available in the page", "FAIL");
			}
			strRowNum = "1";
		}
		if(strSlotType == "8to10")
		{
			if(browser.isTextPresent("8am - 10am"))
			{
				Report.updateTestLog("8am - 10am available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("8am - 10am is not available in the page", "FAIL");
			}
			strRowNum = "1";
		}
		
		
		
		
		for (int i = 7; i > 0; i--) {

			if (browser.isElementVisibleWithXpath(pageProperties.getProperty(
			"SAE.StdSlot").replace("COLS",strRowNum ).replace("ROWS", "" + i))) {
		verifyIsElementVisibleWithXpath(pageProperties.getProperty(
		"SAE.StdSlot").replace("COLS",strRowNum ).replace("ROWS", "" + i),
				"Book an appointment page");
		verifyAndClickWithXpath(
				pageProperties.getProperty(
						"SAE.StdSlot").replace("COLS",strRowNum ).replace("ROWS", "" + i),
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

	public void selectAnAppointment2HrWeekend(String strSlotType) {
		browser.wait(500);		
		verifyIsTextPresent(pageProperties
				.getProperty("SAE.2HrSlotsAvailable"));
		String strRowNum = "0";
		
		if(strSlotType == "8to10")
		{
			if(browser.isTextPresent("8am - 10am"))
			{
				Report.updateTestLog("8am - 10am available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("8am - 10am is not available in the page", "FAIL");
			}
			strRowNum = "1";
		}
		
		
		
		if(strSlotType == "10to12")
		{
			if(browser.isTextPresent("10am - 12pm"))
			{
				Report.updateTestLog("10am - 12pm available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("10am - 12pm is not available in the page", "FAIL");
			}
			strRowNum = "2";
		}
		
		
		
		if(strSlotType == "4pm to 6pm")
		{
			if(browser.isTextPresent("4pm to 6pm"))
			{
				Report.updateTestLog("FF slots 4pm to 6pm available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("FF slots 4pm to 6pm is not available in the page", "FAIL");
			}
			strRowNum = "5";
		}
		
		
		
		
		
		if(strSlotType == "PMElec")
		{
			if(browser.isTextPresent("4pm - 6pm"))
			{
				Report.updateTestLog("PM slots for elec (4pm - 6pm) available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("PM slots for elec (4pm - 6pm is not available in the page", "FAIL");
			}
			strRowNum = "5";
		}
		
		if(strSlotType == "PM2to4")
		{
			if(browser.isTextPresent("2pm - 4pm"))
			{
				Report.updateTestLog("2pm - 4pm available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("2pm - 4pm is not available in the page", "FAIL");
			}
			strRowNum = "4";
		}
		if(strSlotType == "12to2")
		{
			if(browser.isTextPresent("12pm - 2pm"))
			{
				Report.updateTestLog("12pm - 2pm available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("12pm - 2pm is not available in the page", "FAIL");
			}
			strRowNum = "3";
		}
		
		int rowStartNum = 0;
		int rowEndNum = 0;
		Calendar currentDate = Calendar.getInstance();
		new SimpleDateFormat("yyyy");
		int dayCount = currentDate.get(currentDate.DAY_OF_WEEK);
		switch(dayCount)
		{
		case 1:
		{
			verifyAndClick(pageProperties.getProperty("SAE.next"),"Next week");
			rowStartNum = 1;
			rowEndNum = 3;
			break;
		}
		case 2:	
		{
			rowStartNum = 4;
			rowEndNum = 6;
			break;
		}
		case 3:
		{
			rowStartNum = 3;
			rowEndNum = 5;
			break;
		
		}
		case 4:
		{
			rowStartNum = 2;
			rowEndNum = 4;
			break;
		
		}
		case 5:
		{
			rowStartNum = 1;
			rowEndNum = 3;
			break;
		
		}
		case 6:
		{
			rowStartNum = 0;
			rowEndNum = 2;
			break;
		
		}
		case 7:
		{
			rowStartNum = 0;
			rowEndNum = 1;
			break;
		
		}
		}
		
				
		for (int i = rowStartNum; i > rowEndNum; i--) {
		

			if (browser.isElementVisibleWithXpath(pageProperties.getProperty(
			"SAE.StdSlot").replace("COLS",strRowNum ).replace("ROWS", "" + i))) {
		verifyIsElementVisibleWithXpath(pageProperties.getProperty(
		"SAE.StdSlot").replace("COLS",strRowNum ).replace("ROWS", "" + i),
				"Book an appointment page");
		verifyAndClickWithXpath(
				pageProperties.getProperty(
						"SAE.StdSlot").replace("COLS",strRowNum ).replace("ROWS", "" + i),
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

	public void selectAnAppointmentStdWeekend(String strSlotType) {
		browser.wait(500);		
		verifyIsTextPresent(pageProperties
				.getProperty("SAE.2HrSlotsAvailable"));
		String strRowNum = "0";
				
				
		if(strSlotType == "AllDay")
		{
			if(browser.isTextPresent("All day 8am - 6pm"))
			{
				Report.updateTestLog("All Day slots available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("All Day slots is not available in the page", "FAIL");
			}
			strRowNum = "4";
		}
		
				
		if(strSlotType == "FF")
		{
			if(browser.isTextPresent("10am - 2pm"))
			{
				Report.updateTestLog("FF slots available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("FF slots is not available in the page", "FAIL");
			}
			strRowNum = "2";
		}
		
				
		if(strSlotType == "EVE")
		{
			if(browser.isTextPresent("6pm - 8pm"))
			{
				Report.updateTestLog("Eve slots available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("Eve slots is not available in the page", "FAIL");
			}
			strRowNum = "5";
		}
		
		if(strSlotType == "PM")
		{
			if(browser.isTextPresent("12pm - 4pm"))
			{
				Report.updateTestLog("PM slots available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("PM slots is not available in the page", "FAIL");
			}
			strRowNum = "3";
		}
		
		
		if(strSlotType == "AM")
		{
			if(browser.isTextPresent("8am - 1pm"))
			{
				Report.updateTestLog("8am - 1pm available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("8am - 1pm is not available in the page", "FAIL");
			}
			strRowNum = "1";
		}
		
		int rowStartNum = 0;
		int rowEndNum = 0;
		Calendar currentDate = Calendar.getInstance();
		new SimpleDateFormat("yyyy");
		int dayCount = currentDate.get(currentDate.DAY_OF_WEEK);
		switch(dayCount)
		{
		case 1:
		{
			verifyAndClick(pageProperties.getProperty("SAE.next"),"Next week");
			rowStartNum = 1;
			rowEndNum = 3;
			break;
		}
		case 2:	
		{
			rowStartNum = 4;
			rowEndNum = 6;
			break;
		}
		case 3:
		{
			rowStartNum = 3;
			rowEndNum = 5;
			break;
		
		}
		case 4:
		{
			rowStartNum = 2;
			rowEndNum = 4;
			break;
		
		}
		case 5:
		{
			rowStartNum = 1;
			rowEndNum = 3;
			break;
		
		}
		case 6:
		{
			rowStartNum = 0;
			rowEndNum = 2;
			break;
		
		}
		case 7:
		{
			rowStartNum = 0;
			rowEndNum = 1;
			break;
		
		}
		}
		
				
		for (int i = rowStartNum; i > rowEndNum; i--) {
		

			if (browser.isElementVisibleWithXpath(pageProperties.getProperty(
			"SAE.StdSlot").replace("COLS",strRowNum ).replace("ROWS", "" + i))) {
		verifyIsElementVisibleWithXpath(pageProperties.getProperty(
		"SAE.StdSlot").replace("COLS",strRowNum ).replace("ROWS", "" + i),
				"Book an appointment page");
		verifyAndClickWithXpath(
				pageProperties.getProperty(
						"SAE.StdSlot").replace("COLS",strRowNum ).replace("ROWS", "" + i),
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

	public void errorMessage(ArrayList<String> errList)
	{
		verifyIsTextPresent(errList.get(0));
	}

	public void selectNoAppointmentStd(String strSlotType) {
		
		browser.wait(500);
		verifyCalendarPage();
		verifyAndClickWithXpath(pageProperties.getProperty("SAE.StdTab"), "Standard Slot tab in the slot table");
		
		String strRowNum = "0";
		if(strSlotType == "AllDay")
		{
			strRowNum = "4";
		}
		
		if(strSlotType == "AllDay")
		{
			if(browser.isTextPresent("8am - 6pm"))
			{
				Report.updateTestLog("All Day slots available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("All Day slots is not available in the page", "FAIL");
			}
			strRowNum = "4";
		}
		
		if(strSlotType == "FF")
		{
			if(browser.isTextPresent("10am - 2pm"))
			{
				Report.updateTestLog("FF slots available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("FF slots is not available in the page", "FAIL");
			}
			strRowNum = "2";
		}
		
		if(strSlotType == "EVE")
		{
			if(browser.isTextPresent("6pm - 8pm"))
			{
				Report.updateTestLog("Eve slots available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("Eve slots is not available in the page", "FAIL");
			}
			strRowNum = "5";
		}
		
		if(strSlotType == "PM")
		{
			if(browser.isTextPresent("12pm - 6pm"))
			{
				Report.updateTestLog("PM slots available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("PM slots is not available in the page", "FAIL");
			}
			strRowNum = "3";
		}
		
		
		if(strSlotType == "AM")
		{
			if(browser.isTextPresent("8am - 1pm"))
			{
				Report.updateTestLog("8am - 1pm available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("8am - 1pm is not available in the page", "FAIL");
			}
			strRowNum = "1";
		}		
		
		
		int intPanelCount = 1;
		String slotPanel = "preftab"+intPanelCount;
		for (int i = 7; i > 0; i--) {

			if (browser.isElementVisibleWithXpath(pageProperties.getProperty(
					"SAE.StdSlot").replace("PANEL", slotPanel).replace("COLS",strRowNum ).replace("ROWS", "" + i))) {
				System.out
						.println(pageProperties.getProperty(
						"SAE.StdSlot").replace("PANEL", slotPanel).replace("COLS",strRowNum ).replace("ROWS", "" + i));
				verifyIsElementVisibleWithXpath(pageProperties.getProperty(
				"SAE.StdSlot").replace("PANEL", slotPanel).replace("COLS",strRowNum ).replace("ROWS", "" + i),
						"Book an appointment page");
				verifyAndClickWithXpath(
						pageProperties.getProperty(
								"SAE.StdSlot").replace("PANEL", slotPanel).replace("COLS",strRowNum ).replace("ROWS", "" + i),
						"Select a slot from available Standard slots");
				verifyIsElementVisibleWithXpath(
						pageProperties.getProperty("SAE.Continue"),
						"Continue button");
				verifyAndClickWithXpath(
						pageProperties.getProperty("SAE.Continue"),
						"Book This Appointment");
				break;
			}
			else
			{
				if(i == 1)
				{
					Report.updateTestLog("No "+strSlotType+" slot is displayed", "PASS");
				}
			}
		}		

	}

	
	
}
