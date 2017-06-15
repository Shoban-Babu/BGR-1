package bg.framework.app.functional.page.reFactoring;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import bg.framework.app.functional.entities.UserProfile;
import bg.framework.app.functional.page.common.BasePage;
import bg.framework.app.functional.util.OnlineDBConnector;
import bg.framework.app.functional.util.PropertyLoader;
import bg.framework.app.functional.util.Report;

import bg.framework.common.functional.UIDriver;
import bg.framework.common.functional.WebDriverProvider;

public class IBPage extends BasePage{
	private final static String SMR_FILE_NAME = "resources/reFactoring/IB.properties";
	private static Properties pageProperties = new PropertyLoader(SMR_FILE_NAME).load();
	
	
	
	String[] lstFaultItems = new String[3];
	
	public void navigateToIdentifyFault(int intFlag,UserProfile userProfile)
	{
		browser.wait(500);
       
		verifyIsTextPresent(pageProperties
				.getProperty("ASVIBSelectAppliance.HeadText"));
		verifyIsTextPresent(pageProperties
				.getProperty("ASVIBSelectAppliance.SelectText"));
		verifyIsTextPresent(pageProperties
				.getProperty("ASVIBSelectAppliance.SelectSlot"));		
		/*verifyIsTextPresent(pageProperties
				.getProperty("ASVIBSelectAppliance.EmergencyText"));*/		
		/*verifyIsTextPresent(pageProperties
				.getProperty("ASVIBSelectAppliance.AwaitingSelection"));	*/	
		lstFaultItems = clickFaultItemCheck(userProfile);
		verifyIsTextPresent(pageProperties
				.getProperty("ASVIBSelectAppliance.FaultyApplianceText"));
		if(browser.isElementVisibleWithXpath(pageProperties.getProperty("ASVIBSelectAppliance.Continue")))
		{
		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("ASVIBSelectAppliance.Continue"),
				"Continue Button");
		verifyAndClickWithXpath(
				pageProperties.getProperty("ASVIBSelectAppliance.Continue"),
				"Continue");
		}
	}

	public void verifyFaultPage()
	{
		verifyIsTextPresent(pageProperties
				.getProperty("ASVIBSelectAppliance.HeadText"));
		verifyIsTextPresent(pageProperties
				.getProperty("ASVIBSelectAppliance.SelectText"));
		verifyIsTextPresent(pageProperties
				.getProperty("ASVIBSelectAppliance.SelectSlot"));		
		verifyIsTextPresent(pageProperties
				.getProperty("ASVIBSelectAppliance.EmergencyText"));		
		/*verifyIsTextPresent(pageProperties
				.getProperty("ASVIBSelectAppliance.AwaitingSelection"));*/			
		verifyIsTextPresent(pageProperties
				.getProperty("ASVIBSelectAppliance.FaultyApplianceText"));	
	}
	
	public void navigateToPriority()
	{
		
		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("ASVIBSelectAppliance.Continue"),
				"Continue Button");
		
		verifyAndClickWithXpath(
				pageProperties.getProperty("ASVIBSelectAppliance.Continue"),
				"Continue");
	}
	
	public void clickHomeAppliance() {

		verifyAndClickWithXpath(
				pageProperties.getProperty("ASVIBAccountSummary.homeAppliance")
						+ "/li[1]/a", "Home Electricals");

	}

	public String[] clickFaultItemCheck(UserProfile userProfile) {
		try {

			int itemCount = 3;
			int faultCount = 0;
			String strTemp = "";
			verifyIsElementVisibleWithXpath(
					pageProperties.getProperty("ASVIBSelectAppliance.ContinueDis"),
					"Continue Disabled Button");

			while (browser.isElementVisibleWithXpath(pageProperties
					.getProperty("ASVIBSelectAppliance.ContinueDis")) == true) 
			{			

				
				if (browser.isElementVisibleWithXpath(pageProperties.getProperty("ASVIBAccountSummary.faultItem")
						.replace("3", "" + itemCount)
						.replace("0", "" + faultCount)) == false) {
					itemCount = 3;
					faultCount = faultCount + 1;
					int catCount = faultCount+1;
					if(faultCount==1)
					{
						catCount=1;						
						browser.clickWithXpath(
								pageProperties.getProperty("ASVIBIdentifyFault.Category").replace("[i]", "["+catCount+"]"));
					}
					else
					{					
					browser.clickWithXpath(
							pageProperties.getProperty("ASVIBIdentifyFault.Category").replace("[i]", "["+catCount+"]"));
					}
				}			
				System.out.println("==========="+pageProperties.getProperty("ASVIBAccountSummary.faultItem")
						.replace("3", "" + itemCount)
						.replace("0", "" + faultCount));
				browser.clickWithXpath(
						pageProperties.getProperty("ASVIBAccountSummary.faultItem")
								.replace("3", "" + itemCount)
								.replace("0", "" + faultCount));
				
				itemCount++;
				
				
				if (browser.isElementVisibleWithXpath(pageProperties
						.getProperty("ASVIBSelectAppliance.ContinueDis")) == false) {
					Report.updateTestLog(
							"" + browser.getTextByXpath(pageProperties.getProperty("IBFault.SelectedText")) + " appliance is clicked", "DONE");
					userProfile.setFirstName(browser.getTextByXpath(pageProperties.getProperty("IBFault.SelectedText")));
				} else {
					Report.updateTestLog("" + strTemp
							+ " appliance is disabled", "DONE");
				}

			}
			// lstFaultItems.add(strTemp);
			if (lstFaultItems[0] == null) {
				lstFaultItems[0] = strTemp;
			} else if (lstFaultItems[1] == null) {
				lstFaultItems[1] = strTemp;
			} else if (lstFaultItems[2] == null) {
				lstFaultItems[2] = strTemp;
			}

			browser.wait(1000);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return lstFaultItems;
	}
	
	public void clickFaultCategory(String strCategory,UserProfile userProfile)
	{
		int itemCount = 3;
		int faultCount = 0;
		String strTemp = "";
		if(browser.isTextPresent(strCategory))
		{
			for(int i = 1 ; i<= 3; i++)
			{			
				//System.out.println("*********"+browser.getTextByXpath(pageProperties.getProperty("ASVIBIdentifyFault.Category").replace("li[i]", "li["+i+"]")));
				if(browser.getTextByXpath(pageProperties.getProperty("ASVIBIdentifyFault.Category").replace("li[i]", "li["+i+"]")).contains(strCategory))
				{
					System.out.println("------------"+pageProperties.getProperty("ASVIBAccountSummary.faultItem").replace("3", "2")									
							.replace("0", ""+(i-1)));
					verifyAndClickWithXpath(pageProperties.getProperty("ASVIBIdentifyFault.Category").replace("li[i]", "li["+i+"]"), "Category");
					verifyAndClickWithXpath(
							pageProperties.getProperty("ASVIBAccountSummary.faultItem").replace("3", "2")									
									.replace("0", ""+(i-1)),"Fault Item");
					
				/*browser.wait(1000);
				if (browser.isElementVisibleWithXpath(pageProperties.getProperty("ASVIBAccountSummary.faultItem")
						.replace("3", "" + itemCount)
						.replace("0", "" + faultCount)) == false) {
					itemCount = 3;
					faultCount = faultCount + 1;
					int catCount = faultCount+1;
					if(faultCount==1)
					{
						catCount=1;						
						browser.clickWithXpath(
								pageProperties.getProperty("ASVIBIdentifyFault.Category").replace("[i]", "["+catCount+"]"));
					}
					else
					{					
					browser.clickWithXpath(
							pageProperties.getProperty("ASVIBIdentifyFault.Category").replace("[i]", "["+catCount+"]"));
					}
				}			
				System.out.println("==========="+pageProperties.getProperty("ASVIBAccountSummary.faultItem")
						.replace("3", "" + itemCount)
						.replace("0", "" + faultCount));
				browser.clickWithXpath(
						pageProperties.getProperty("ASVIBAccountSummary.faultItem")
								.replace("3", "" + itemCount)
								.replace("0", "" + faultCount));
				
				itemCount++;
				
				
				if (browser.isElementVisibleWithXpath(pageProperties
						.getProperty("ASVIBSelectAppliance.ContinueDis")) == false) {
					Report.updateTestLog(
							"" + browser.getTextByXpath(pageProperties.getProperty("IBFault.SelectedText")) + " appliance is clicked", "DONE");
					userProfile.setFirstName(browser.getTextByXpath(pageProperties.getProperty("IBFault.SelectedText")));
				} else {
					Report.updateTestLog("" + strTemp
							+ " appliance is disabled", "DONE");
				}
				break;
				}
				
				}*/
					break;
				}
			}
		}		
		verifyIsTextPresent(pageProperties
				.getProperty("ASVIBSelectAppliance.FaultyApplianceText"));
		if(browser.isElementVisibleWithXpath(pageProperties.getProperty("ASVIBSelectAppliance.Continue")))
		{
		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("ASVIBSelectAppliance.Continue"),
				"Continue Button");
		verifyAndClickWithXpath(
				pageProperties.getProperty("ASVIBSelectAppliance.Continue"),
				"Continue");
		}
		
	}
	
	public String runVBS(UserProfile userProfile,String strType)
	{
		 String meterread = null;
		 String retVal = null;
		if(strType == "Priority")
		{
		 try {
		     
			 //File fileR = new File("C:\\Documents and Settings\\!raghavp2\\Desktop\\Banner\\CUSTID.txt");
			 //File fileP = new File("C:\\Documents and Settings\\!raghavp2\\Desktop\\Banner\\PREMID.txt");
         	 File fileR = new File("C:\\Wmis.edp\\CUSTID.txt");
			 File fileP = new File("C:\\Wmis.edp\\PREMID.txt");
             FileWriter fw = new FileWriter(fileR);
             FileWriter fwP = new FileWriter(fileP);
             //FileUtils.copyDirectory(arg0, arg1)
             File current = new File(".");
             String strPath =  current.getCanonicalPath();
             File fileRCopy = new File(strPath+"\\Test_wmis_Prior.vbs");
			 File filePCopy = new File("C:\\Test_wmis_Prior.vbs");
             
             FileUtils.copyFileToDirectory(fileRCopy, filePCopy);
             fw.write("0"+userProfile.getAccNumber().substring(0,8));
             fwP.write(userProfile.getAccNumber().substring(8, 15));
             fw.close();
             fwP.close();
             
	            Runtime.getRuntime().exec("wscript C:\\Test_wmis_Prior.vbs");
	            
	            browser.wait(90000);
	            File fileSVBCopy = new File(strPath+"\\Close.vbs");
				 File fileDVBCopy = new File("C:\\Close.vbs");
				
				 FileUtils.copyFileToDirectory(fileSVBCopy, fileDVBCopy);
				 
	            Runtime.getRuntime().exec("wscript C:\\Close.vbs");
	            
	            
	            
	            //File file1 = new File("C:\\Documents and Settings\\!raghavp2\\Desktop\\Banner\\Priority.txt");
	            File file1 = new File("C:\\Wmis.edp\\Priority.txt");
	            FileReader fr = new FileReader(file1);
	             BufferedReader br = new BufferedReader(fr);
	             meterread = br.readLine();
	             System.out.println("=================> "+meterread.trim());
	             System.out.println("LENGTH > "+meterread.length());
	             br.close();
	             retVal = meterread.trim();
	            
	        } catch (Exception i) {
	            System.err.println("" + i.toString());
	        }
		}
		if(strType == "Preference")
		{
			try
			{
			Runtime.getRuntime().exec("wscript C:\\Test_wmis.vbs");
            
            browser.wait(90000);
            
            Runtime.getRuntime().exec("wscript C:\\Close.vbs");
            //File file1 = new File("C:\\Documents and Settings\\!raghavp2\\Desktop\\Banner\\Text1.txt");
            File file1 = new File("C:\\Text1.txt");
            FileReader fr = new FileReader(file1);
             BufferedReader br = new BufferedReader(fr);
             meterread = br.readLine();
             System.out.println("================= > "+meterread.trim());
             System.out.println("LENGTH > "+meterread.length());
             
             br.close();
             retVal = meterread.trim();
			}
			catch (Exception i) 
			{
	            System.err.println("" + i.toString());
	        }
			
		}
		if(strType == "ASV")
		{
		 try {
		     
			 //File fileR = new File("C:\\Documents and Settings\\!raghavp2\\Desktop\\Banner\\CUSTID.txt");
			 //File fileP = new File("C:\\Documents and Settings\\!raghavp2\\Desktop\\Banner\\PREMID.txt");
         	 File fileR = new File("C:\\Wmis.edp\\CUSTID.txt");
			 File fileP = new File("C:\\Wmis.edp\\PREMID.txt");
			 
             FileWriter fw = new FileWriter(fileR);
             FileWriter fwP = new FileWriter(fileP);
             //FileUtils.copyDirectory(arg0, arg1)
             String strPath = System.getProperty("user.dir");
             System.out.println("--------------------"+strPath);
             strPath = strPath + "\\src\\resources\\Wmis";
             /*File current = new File(".");
             String strPath =  current.getCanonicalPath();*/
             /*File fileRCopy = new File(strPath+"\\Test_wmis_ASV.vbs");
			 //File filePCopy = new File("C:\\Test_wmis_ASV.vbs");
			 File fileRWmis = new File(strPath+"\\ASV.ebm");
			 File filePWmis = new File("C:\\Program Files\\Attachmate\\EXTRA!\\Macros\\ENU\\ASV.ebm");
			 File fileSVBCopy = new File(strPath+"\\Close.vbs");
			 //File fileDVBCopy = new File("C:\\Close.vbs");
			 File fileSSesCopy = new File(strPath+"\\Wmis.edp");
			 //File fileDSesCopy = new File("C:\\Wmis.edp");
*/			
			/* FileUtils.copyFileToDirectory(fileSSesCopy, fileDSesCopy);
			 FileUtils.copyFileToDirectory(fileSVBCopy, fileDVBCopy);
			 FileUtils.copyFileToDirectory(fileRWmis, filePWmis);
             FileUtils.copyFileToDirectory(fileRCopy, filePCopy);  */           
             
             
             fw.write("0"+userProfile.getAccNumber().substring(0,8));
             fwP.write(userProfile.getAccNumber().substring(8, 15));
             fw.close();
             fwP.close();
             String strAsvVbs = strPath+"\\Test_wmis_ASV.vbs";
             System.out.println("--------------------"+strAsvVbs);
             Runtime.getRuntime().exec("wscript C:\\Wmis\\Test_wmis_ASV.vbs");
	           // Runtime.getRuntime().exec("wscript C:\\Test_wmis_ASV.vbs");
	            
	            browser.wait(90000);
	           
				 
	           // Runtime.getRuntime().exec("wscript C:\\Wmis\\Close.vbs");
	            
	            
	            
	            //File file1 = new File("C:\\Documents and Settings\\!raghavp2\\Desktop\\Banner\\Priority.txt");
	            File file1 = new File("C:\\Wmis.edp\\Priority.txt");
	            FileReader fr = new FileReader(file1);
	             BufferedReader br = new BufferedReader(fr);
	             meterread = br.readLine();
	             System.out.println("=================> "+meterread.trim());
	             System.out.println("LENGTH > "+meterread.length());
	             br.close();
	             retVal = meterread.trim();
	            
	        } catch (Exception i) {
	            System.err.println("" + i.toString());
	        }
		}
		
		return retVal;
		
	}

	public void navigatePriorityPage(int intFlag)
	{

		browser.wait(500);
		if(intFlag == 0)
		{
			verifyIsElementVisibleWithXpath(
					pageProperties.getProperty("ASVIBIdentifyFault.PopUpContinue"),
					"Continue button");
			verifyAndClickWithXpath(
					pageProperties.getProperty("ASVIBIdentifyFault.PopUpContinue"),
					"Continue Identify Fault");
			browser.wait(1000);
			verifyPriorityPage("2");
			verifyAndClickWithXpath(pageProperties.getProperty("IBPriority.No"), "No priority");
		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("IBIdentifyFault.Continue"),
				"Continue button");
		verifyAndClickWithXpath(
				pageProperties.getProperty("IBIdentifyFault.Continue"),
				"Continue");
		}
		if(intFlag == 1)
		{
			verifyIsElementVisibleWithXpath(
					pageProperties.getProperty("ASVIBIdentifyFault.PopUpContinue"),
					"Continue button");
			verifyAndClickWithXpath(
					pageProperties.getProperty("ASVIBIdentifyFault.PopUpContinue"),
					"Continue Identify Fault");
			browser.wait(1000);
			verifyPriorityPage("2");
			verifyAndClickWithXpath(pageProperties.getProperty("IBPriority.Yes"), "Yes priority");
			
			verifyIsElementVisibleWithXpath(
					pageProperties.getProperty("IBIdentifyFault.Continue"),
					"Continue button");
			verifyAndClickWithXpath(
					pageProperties.getProperty("IBIdentifyFault.Continue"),"Continue");
			
			
		}
		if(intFlag == 2)
		{
			
			browser.wait(1000);
			verifyPriorityPage("2");
			verifyAndClickWithXpath(pageProperties.getProperty("IBPriority.Yes"), "Yes priority");
			
			verifyIsElementVisibleWithXpath(
					pageProperties.getProperty("IBIdentifyFault.Continue"),
					"Continue button");
			verifyAndClickWithXpath(
					pageProperties.getProperty("IBIdentifyFault.Continue"),"Continue");
			
			
		}
		if(intFlag == 3)
		{
			
			browser.wait(1000);
			verifyIsElementVisibleWithXpath(
					pageProperties.getProperty("ASVIBIdentifyFault.PopUpContinue"),
					"Continue button");
			verifyAndClickWithXpath(
					pageProperties.getProperty("ASVIBIdentifyFault.PopUpContinue"),
					"Continue Identify Fault");
			browser.wait(1000);
		}
		
		if(intFlag == 4)
		{
			
			browser.wait(1000);
			verifyIsElementVisibleWithXpath(
					pageProperties.getProperty("ASVIBIdentifyFault.RescheduleContinue"),
					"Continue button");
			verifyAndClickWithXpath(
					pageProperties.getProperty("ASVIBIdentifyFault.RescheduleContinue"),
					"Continue Identify Fault");
			browser.wait(1000);
		}
		if(intFlag == 5)
		{
			
			browser.wait(1000);
			verifyPriorityPage("2");
			verifyAndClickWithXpath(pageProperties.getProperty("IBPriority.No"), "No priority");
		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("IBIdentifyFault.Continue"),
				"Continue button");
		verifyAndClickWithXpath(
				pageProperties.getProperty("IBIdentifyFault.Continue"),
				"Continue");
		}
	}
	
	public void verifyPriorityPage(String intFlag)
	{
		if(intFlag == "1a")
		{
		verifyIsTextPresent(pageProperties.getProperty("ASVIBSelectAppliance.HeadText"));
		verifyIsTextPresent(pageProperties.getProperty("IBPriority.Question1"));
		verifyIsElementVisibleWithXpath(pageProperties.getProperty("IBPriority.Yes"), "Yes priority");
		verifyIsElementVisibleWithXpath(pageProperties.getProperty("IBPriority.No"), "No priority");
		}
		
		if(intFlag == "2")
		{
		verifyIsTextPresent(pageProperties.getProperty("ASVIBSelectAppliance.HeadText"));		
		verifyIsElementVisibleWithXpath(pageProperties.getProperty("IBPriority.Yes"), "Yes priority");
		verifyIsElementVisibleWithXpath(pageProperties.getProperty("IBPriority.No"), "No priority");
		}
		
		if(intFlag == "1b")
		{
		verifyIsTextPresent(pageProperties.getProperty("ASVIBSelectAppliance.HeadText"));
		verifyIsTextPresent(pageProperties.getProperty("IBPriority.Question2"));
		verifyIsTextPresent(pageProperties.getProperty("IBPriority.Question3"));
		verifyIsElementVisibleWithXpath(pageProperties.getProperty("IBPriority.Yes"), "Yes priority");
		verifyIsElementVisibleWithXpath(pageProperties.getProperty("IBPriority.No"), "No priority");
		}
		
		if(intFlag == "0" )
		{
			if(browser.isElementVisibleWithXpath(pageProperties.getProperty("IBPriority.Yes")))
			{
				Report.updateTestLog("Priority Page is loaded", "FAIL");
			}
			else
			{
				Report.updateTestLog("Priority Page is not loaded", "PASS");
				verifyIsTextPresent(pageProperties
						.getProperty("ASVIBContactDetails.YourAppointmentText"));
				verifyIsTextPresent("Review details", "Review Details Page");
				
				if(browser.isElementVisibleWithXpath(pageProperties.getProperty("ASVIBReview.GAC")))
				{
						verifyIsElementVisibleWithXpath(
						pageProperties.getProperty("ASVIBReview.GAC"), "GAC link");
				}
				if(browser.isElementVisibleWithXpath(pageProperties.getProperty("ASVIBReview.COD")))
				{
				verifyIsElementVisibleWithXpath(
						pageProperties.getProperty("ASVIBReview.COD"), "COD");
				}
				verifyIsElementVisibleWithXpath(
						pageProperties.getProperty("ASVIBReview.MobileNumberRadio"),
						"Mobile Number radio button");
				verifyAndClickWithXpath(
						pageProperties.getProperty("ASVIBReview.HomeNumberRadio"),
						"Mobile Number");
				verifyIsElementVisibleWithXpath(
						pageProperties.getProperty("ASVIBReview.HomeNumberRadio"),
						"Mobile Number radio button");
				verifyIsElementVisibleWithXpath(
						pageProperties.getProperty("ASVIBReview.MobileNumberText"),
						"Mobile Number textbox");
				if(browser.isElementVisibleWithXpath(pageProperties.getProperty("ASVIBReview.MobileNumberText")))
						{
					Report.updateTestLog("Contact Details Page is loaded", "PASS");
						}
				else
				{
					Report.updateTestLog("Contact Details Page is not loaded", "PASS");
				}
				
			}
			
		}
	}
	
	public void changeAppointment(int intOption) {
		browser.wait(1000);
		if (intOption == 1) {

			verifyIsElementVisibleWithXpath(
					pageProperties.getProperty("ASVIBReview.PopUpKeepAppointment"),
					"PopUp KeepAppointment button");
			verifyAndClickWithXpath(
					pageProperties.getProperty("ASVIBReview.PopUpKeepAppointment"),
					"Keep Appointment");
		}
		if (intOption == 2) {
			verifyIsElementVisibleWithXpath(
					pageProperties.getProperty("ASVIBReview.PopUpChangeAppointment"),
					"PopUp Change Appointment button");
			verifyAndClickWithXpath(
					pageProperties.getProperty("ASVIBReview.PopUpChangeAppointment"),
					"Change Appointment");
		}
	}
	
	public void selectThisAppointment()
	{
		browser.wait(1000);
		verifyAndClickWithXpath(pageProperties.getProperty("IBBookAppointment.SelectAppointment"), "Select this appointment");
		browser.wait(1000);
	}
	
	public void selectAnAppointment(String strSlotType) {
		browser.wait(1000);
		/*verifyIsTextPresent(pageProperties
				.getProperty("ASVIBBookAppointment.MessageText"));*/
		verifyIsTextPresent(pageProperties
				.getProperty("ASVIBBookAppointment.SlotsAvailable"));
		/*verifyIsTextPresent(pageProperties
				.getProperty("ASVIBBookAppointment.DifferentAppointment"));*/
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
					"ASVIBBookAppointment.LastAppointment").replace("COLS",strRowNum ).replace("ROWS", "" + i))) {
				verifyIsElementVisibleWithXpath(pageProperties.getProperty(
				"ASVIBBookAppointment.LastAppointment").replace("COLS",strRowNum ).replace("ROWS", "" + i),
						"Book an appointment page");
				verifyAndClickWithXpath(
						pageProperties.getProperty(
								"ASVIBBookAppointment.LastAppointment").replace("COLS",strRowNum ).replace("ROWS", "" + i),
						"Select a slot from available slots");
				verifyIsElementVisibleWithXpath(
						pageProperties.getProperty("ASVIBBookAppointment.Continue"),
						"Continue button");
				verifyAndClickWithXpath(
						pageProperties.getProperty("ASVIBBookAppointment.Continue"),
						"Book This Appointment");
				break;
			}
		}

	}
	
	public void selectAnAppointmentReschedule(String strSlotType) {
		browser.wait(1000);
		/*verifyIsTextPresent(pageProperties
				.getProperty("ASVIBBookAppointment.MessageText"));*/
		verifyIsTextPresent(pageProperties
				.getProperty("ASVIBBookAppointment.SlotsAvailable"));
		/*verifyIsTextPresent(pageProperties
				.getProperty("ASVIBBookAppointment.DifferentAppointment"));*/
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
					"ASVIBBookAppointment.LastAppointment").replace("COLS", "" + i).replace("ROWS", strRowNum))) {
				verifyIsElementVisibleWithXpath(pageProperties.getProperty(
						"ASVIBBookAppointment.BookThisAPpointmentHeader"),
						"Book an appointment page");
				verifyAndClickWithXpath(
						pageProperties.getProperty(
								"ASVIBBookAppointment.LastAppointment").replace("COLS", "" + i).replace("ROWS", strRowNum),
						"Select a slot from available slots");
				verifyIsElementVisibleWithXpath(
						pageProperties.getProperty("ASVIBBookAppointment.Continue"),
						"Continue button");
				verifyAndClickWithXpath(
						pageProperties.getProperty("ASVIBBookAppointment.Continue"),
						"Book This Appointment");
				break;
			}
		}

	}
	
	public void confirmationTotalChk(UserProfile userProfile)
	{
		//if(browser.getTextByXpath(pageProperties.getProperty("ASVIBConfirmation.TotalAmt")).trim().contains(userProfile.getTitle()))
		browser.wait(1000);
			if(browser.isTextPresent(userProfile.getTitle().trim()))
		{
			Report.updateTestLog("Total amount is present and is "+userProfile.getTitle()+" same as in review page", "PASS");
		}
		else
		{
			Report.updateTestLog("Total amount is present and is "+userProfile.getTitle()+" not same as in review page", "FAIL");
		}
	}
	
	public void verifySlot(String strSlotType)
	{
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
			
		}
	}
	
	public void selectNoAppointment(String strSlotType) {
		browser.wait(1000);
		/*verifyIsTextPresent(pageProperties
				.getProperty("ASVIBBookAppointment.MessageText"));*/
		/*verifyIsTextPresent(pageProperties
				.getProperty("ASVIBBookAppointment.SlotsAvailable"));*/
		/*verifyIsTextPresent(pageProperties
				.getProperty("ASVIBBookAppointment.DifferentAppointment"));*/
		String strRowNum = "0";
		if(strSlotType == "AllDay")
		{
			strRowNum = "4";
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
		
		if(strSlotType == "AllDay")
		{
			if(browser.isTextPresent("All day 8am - 6pm"))
			{
				Report.updateTestLog("All Day slots available in the page", "FAIL");
			}
			else
			{
				Report.updateTestLog("All Day slots is not available in the page", "PASS");
			}
			strRowNum = "4";
		}
		
		if(strSlotType == "FF")
		{
			if(browser.isTextPresent("10am - 2pm"))
			{
				Report.updateTestLog("FF slots available in the page", "FAIL");
			}
			else
			{
				Report.updateTestLog("FF slots is not available in the page", "PASS");
			}
			strRowNum = "2";
		}
		
		if(strSlotType == "EVE")
		{
			if(browser.isTextPresent("Evening 6pm - 8pm"))
			{
				Report.updateTestLog("EVE slots available in the page", "FAIL");
			}
			else
			{
				Report.updateTestLog("EVE slots is not available in the page", "PASS");
			}
			strRowNum = "5";
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
		
		for (int i = 7; i > 0; i--) {

			if (browser.isElementVisibleWithXpath(pageProperties.getProperty(
					"ASVIBBookAppointment.LastAppointment").replace("COLS", strRowNum).replace("ROWS",""+i))) {
				Report.updateTestLog( strSlotType+ " slots available in the page", "FAIL");
				break;
			}
			if(i == 1)
			{
				Report.updateTestLog( strSlotType+ " slots are not available in the page", "PASS");
			}
		}

	}
	
	@SuppressWarnings("static-access")
	public void selectAnAppointmentWeekend(String strSlotType) {
		browser.wait(500);
		
		/*verifyIsTextPresent(pageProperties
				.getProperty("ASVIBBookAppointment.MessageText"));*/
		
		verifyIsTextPresent(pageProperties
				.getProperty("ASVIBBookAppointment.SlotsAvailable"));
		
		verifyIsTextPresent(pageProperties
				.getProperty("ASVIBBookAppointment.DifferentAppointment"));
		
		String strRowNum = "0";
		if(strSlotType == "AllDay")
		{
			strRowNum = "4";
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
			verifyAndClick(pageProperties.getProperty("IBBookAppointment.next"),"Next week");
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
		
		
	
		
		/*for (int i = rowStartNum; i > rowEndNum; i--) {

			if (browser.isElementVisibleWithXpath(pageProperties.getProperty(
					"ASVIBBookAppointment.LastAppointment").replace("COLS", "" + i).replace("ROWS", strRowNum))) {
				verifyIsElementVisibleWithXpath(pageProperties.getProperty(
						"ASVIBBookAppointment.BookThisAPpointmentHeader"),
						"Book an appointment page");
				verifyAndClickWithXpath(
						pageProperties.getProperty(
								"ASVIBBookAppointment.LastAppointment").replace("COLS", "" + i).replace("ROWS", strRowNum),
						"Select a slot from available slots");
				verifyIsElementVisibleWithXpath(
						pageProperties.getProperty("ASVIBBookAppointment.Continue"),
						"Continue button");
				verifyAndClickWithXpath(
						pageProperties.getProperty("ASVIBBookAppointment.Continue"),
						"Book This Appointment");
				break;
			}
		}*/
		
		for (int i = rowStartNum; i > rowEndNum; i--) {
			verifyAndClickWithXpath(pageProperties.getProperty("FastTrack.BookLastSlot").replace("calendarRow", "calendarRow"+i).replace("COL", ""+4), "All Day Slots");
			if (browser.isElementVisibleWithXpath(pageProperties.getProperty(
					"ASVIBBookAppointment.LastAppointment").replace("calendarRow", "calendarRow"+i).replace("COL", strRowNum))) {
				verifyIsElementVisibleWithXpath(pageProperties.getProperty(
						"ASVIBBookAppointment.BookThisAPpointmentHeader"),
						"Book an appointment page");
				verifyAndClickWithXpath(
						pageProperties.getProperty(
								"ASVIBBookAppointment.LastAppointment").replace("calendarRow", "calendarRow"+i).replace("COL", strRowNum),
						"Select a slot from available slots");
				verifyIsElementVisibleWithXpath(
						pageProperties.getProperty("ASVIBBookAppointment.Continue"),
						"Continue button");
				verifyAndClickWithXpath(
						pageProperties.getProperty("ASVIBBookAppointment.Continue"),
						"Book This Appointment");
				break;
			}
		}

	}
	
	@SuppressWarnings("static-access")
	public void selectNoSlotWeekEndBankHoliday(String strSlotType)
	{
		String strRowNum = "0";
		if(strSlotType == "AllDay")
		{
			strRowNum = "4";
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
			verifyAndClick(pageProperties.getProperty("IBBookAppointment.next"),"Next week");
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
		
		
	
		
		/*for (int i = rowStartNum; i > rowEndNum; i--) {

			if (browser.isElementVisibleWithXpath(pageProperties.getProperty(
					"ASVIBBookAppointment.LastAppointment").replace("COLS", "" + i).replace("ROWS", strRowNum))) {
				verifyIsElementVisibleWithXpath(pageProperties.getProperty(
						"ASVIBBookAppointment.BookThisAPpointmentHeader"),
						"Book an appointment page");
				verifyAndClickWithXpath(
						pageProperties.getProperty(
								"ASVIBBookAppointment.LastAppointment").replace("COLS", "" + i).replace("ROWS", strRowNum),
						"Select a slot from available slots");
				verifyIsElementVisibleWithXpath(
						pageProperties.getProperty("ASVIBBookAppointment.Continue"),
						"Continue button");
				verifyAndClickWithXpath(
						pageProperties.getProperty("ASVIBBookAppointment.Continue"),
						"Book This Appointment");
				break;
			}
		}*/
		
		for (int i = rowStartNum; i > rowEndNum; i--) {
			verifyAndClickWithXpath(pageProperties.getProperty("FastTrack.BookLastSlot").replace("calendarRow", "calendarRow"+i).replace("COL", ""+4), "All Day Slots");
			if (browser.isElementVisibleWithXpath(pageProperties.getProperty(
					"ASVIBBookAppointment.LastAppointment").replace("calendarRow", "calendarRow"+i).replace("COL", strRowNum))) {
				Report.updateTestLog("slot not available", "PASS");
			}
			else
			{
				Report.updateTestLog("slot not available", "FAIL");
			}
				
		

	}
	}
	
	public void selectAnyAppointment()
	{
		verifyCalendarPage();
		for (int i = 7; i > 0; i--) {

			if (browser.isElementVisibleWithXpath(pageProperties.getProperty(
					"ASVIBBookAppointment.LastAppointment").replace("COLS", "" + i))) {
				/*verifyIsElementVisibleWithXpath(pageProperties.getProperty(
						"ASVIBBookAppointment.LastAppointmentHeader"),
						"Book an appointment page");*/
				verifyIsTextPresent("Book your appointment");
				verifyAndClickWithXpath(
						pageProperties.getProperty(
								"ASVIBBookAppointment.LastAppointment").replace(
								"COLS", "" + i),
						"Select a slot from available slots");
				verifyIsElementVisibleWithXpath(
						pageProperties.getProperty("ASVIBBookAppointment.Continue"),
						"Continue button");
				verifyAndClickWithXpath(
						pageProperties.getProperty("ASVIBBookAppointment.Continue"),
						"Book This Appointment");
				break;
			}
		}
	}
	
	public void verifyCalendarPage()
	{
		browser.wait(1000);
		/*verifyIsTextPresent(pageProperties
				.getProperty("ASVIBBookAppointment.MessageText"));*/
		/*verifyIsTextPresent(pageProperties
				.getProperty("ASVIBBookAppointment.SlotsAvailable"));*/
		/*verifyIsTextPresent(pageProperties
				.getProperty("ASVIBBookAppointment.DifferentAppointment"));*/
	}
	
	public void navigateToCalendar()
	{
		browser.wait(1000);
		/*verifyIsTextPresent(pageProperties
				.getProperty("ASVIBBookAppointment.MessageText"));*/
		verifyIsTextPresent(pageProperties
				.getProperty("ASVIBBookAppointment.SlotsAvailable"));
		verifyIsTextPresent(pageProperties
				.getProperty("ASVIBBookAppointment.DifferentAppointment"));
	}
	
	@SuppressWarnings("static-access")
	public void verifyNoWeekendSlot(String strSlotType)
	{
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
			strRowNum = "3";
		}
		int rowStartNum = 0;
		int rowEndNum = 0;
		Calendar currentDate = Calendar.getInstance();
		int dayCount = currentDate.get(currentDate.DAY_OF_WEEK);
		switch(dayCount)
		{
		case 1:
		{
			rowStartNum = 7;
			rowEndNum = 6;
			break;
		}
		case 2:	
		{
			rowStartNum = 7;
			rowEndNum = 5;
			break;
		}
		case 3:
		{
			rowStartNum = 6;
			rowEndNum = 4;
			break;
		
		}
		case 4:
		{
			rowStartNum = 5;
			rowEndNum = 3;
			break;
		
		}
		case 5:
		{
			rowStartNum = 4;
			rowEndNum = 2;
			break;
		
		}
		case 6:
		{
			rowStartNum = 3;
			rowEndNum = 1;
			break;
		
		}
		case 7:
		{
			rowStartNum = 1;
			rowEndNum = 0;
			break;
		
		}
		}
		for (int i = rowStartNum; i > rowEndNum; i--) {

			if (browser.isElementVisibleWithXpath(pageProperties.getProperty(
					"ASVIBBookAppointment.LastAppointment").replace("COLS", "" + i).replace("ROWS", strRowNum))) {
				Report.updateTestLog(strSlotType+" are available in the weekend day", "FAIL");
				break;
			}
			else
			{
				Report.updateTestLog("Evening slots are not available in the weekend day", "PASS");
			}
		}
	}
	
	public void selectAnAppointmentWeekDay(String strSlotType) {
		browser.wait(1000);
		/*verifyIsTextPresent(pageProperties
				.getProperty("ASVIBBookAppointment.MessageText"));*/
		verifyIsTextPresent(pageProperties
				.getProperty("ASVIBBookAppointment.SlotsAvailable"));
		/*verifyIsTextPresent(pageProperties
				.getProperty("ASVIBBookAppointment.DifferentAppointment"));*/
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
		
		if(strSlotType == "2HR8to10")
		{
			if(browser.isTextPresent("8am - 10am"))
			{
				Report.updateTestLog("2Hr slots 8am - 10am available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("2Hr slots 8am - 10am is not available in the page", "FAIL");
			}
			strRowNum = "1";
		}
		
		if(strSlotType == "2HR4to6")
		{
			if(browser.isTextPresent("2pm - 4pm"))
			{
				Report.updateTestLog("2Hr slots 2pm - 4pm available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("2Hr slots 2pm - 4pm is not available in the page", "FAIL");
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
		
		/*int rowStartNum = 0;
		int rowEndNum = 0;
		Calendar currentDate = Calendar.getInstance();
		int dayCount = currentDate.get(currentDate.DAY_OF_WEEK);
		switch(dayCount)
		{
		case 1:
		{
			rowStartNum = 6;
			rowEndNum = 1;
			break;
		}
		case 2:	
		{
			rowStartNum = 5;
			rowEndNum = 0;
			break;
		}
		case 3:
		{
			rowStartNum = 4;
			rowEndNum = 0;
			break;
		
		}
		case 4:
		{
			rowStartNum = 3;
			rowEndNum = 0;
			break;
		
		}
		case 5:
		{
			rowStartNum = 2;
			rowEndNum = 0;
			break;
		
		}
		case 6:
		{
			rowStartNum = 7;
			rowEndNum = 3;
			break;
		
		}
		case 7:
		{
			rowStartNum = 7;
			rowEndNum = 2;
			break;
		
		}
		}*/
		
		
		for (int i = 7; i > 0; i--) {

			if (browser.isElementVisibleWithXpath(pageProperties.getProperty(
			"ASVIBBookAppointment.LastAppointment").replace("COLS",strRowNum ).replace("ROWS", "" + i))) {
				verifyIsElementVisibleWithXpath(pageProperties.getProperty(
				"ASVIBBookAppointment.LastAppointment").replace("COLS",strRowNum ).replace("ROWS", "" + i),
						"Book an appointment page");
				verifyAndClickWithXpath(
						pageProperties.getProperty(
								"ASVIBBookAppointment.LastAppointment").replace("COLS",strRowNum ).replace("ROWS", "" + i),
						"Select a slot from available slots");
				verifyIsElementVisibleWithXpath(
						pageProperties.getProperty("ASVIBBookAppointment.Continue"),
						"Continue button");
				verifyAndClickWithXpath(
						pageProperties.getProperty("ASVIBBookAppointment.Continue"),
						"Book This Appointment");
				break;
			}
		}

	}
	
	public void reviewDetailsPage(UserProfile userProfile) {

		
		verifyIsTextPresent(pageProperties
				.getProperty("ASVIBContactDetails.YourAppointmentText"));
		verifyIsTextPresent("Review details", "Review Details Page");
		
		if(browser.isElementVisibleWithXpath(pageProperties.getProperty("ASVIBReview.GAC")))
		{
				verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("ASVIBReview.GAC"), "GAC link");
		}
		if(browser.isElementVisibleWithXpath(pageProperties.getProperty("ASVIBReview.COD")))
		{
		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("ASVIBReview.COD"), "COD");
		}
		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("ASVIBReview.MobileNumberRadio"),
				"Mobile Number radio button");
		verifyAndClickWithXpath(
				pageProperties.getProperty("ASVIBReview.HomeNumberRadio"),
				"Mobile Number");
		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("ASVIBReview.HomeNumberRadio"),
				"Mobile Number radio button");
		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("ASVIBReview.MobileNumberText"),
				"Mobile Number textbox");

		
		verifyAndClickWithXpath(
				pageProperties.getProperty("ASVIBReview.MobileNumberRadio"),
				"Mobile Number");
		verifyAndInputByXpath(
				pageProperties.getProperty("ASVIBReview.MobileNumberText"),
				"Mobile Number Text", userProfile.getMobileNumber());
		selectedAppChk(userProfile);
		if(browser.isElementVisibleWithXpath(pageProperties.getProperty("ASVIBReview.TotalAmt")))
		{
		userProfile.setTitle(browser.getTextByXpath(pageProperties.getProperty("ASVIBReview.TotalAmt")));
		}
		else
			if(browser.isElementVisibleWithXpath(pageProperties.getProperty("ASVIBReview.TotalAmt1")))
			{
			userProfile.setTitle(browser.getTextByXpath(pageProperties.getProperty("ASVIBReview.TotalAmt1")));
			}
	}
	
public void reviewDetailsPageLL(UserProfile userProfile) {

		
		verifyIsTextPresent(pageProperties
				.getProperty("ASVIBContactDetails.YourAppointmentText"));
		verifyIsTextPresent("Review details", "Review Details Page");
		
		if(browser.isElementVisibleWithXpath(pageProperties.getProperty("ASVIBReview.GAC")))
		{
				verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("ASVIBReview.GAC"), "GAC link");
		}
		if(browser.isElementVisibleWithXpath(pageProperties.getProperty("ASVIBReview.COD")))
		{
		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("ASVIBReview.COD"), "COD");
		}
		
	
		verifyIsElementVisibleWithXpath(".//*[@id='telePhoneNumber']","Telephone number Section");
		
		verifyAndInputByXpath(
				".//*[@id='telePhoneNumber']",
				"Best Contact Number Text", userProfile.getMobileNumber());
		selectedAppChk(userProfile);
		if(browser.isElementVisibleWithXpath(pageProperties.getProperty("ASVIBReview.TotalAmt")))
		{
		userProfile.setTitle(browser.getTextByXpath(pageProperties.getProperty("ASVIBReview.TotalAmt")));
		}
		else
			if(browser.isElementVisibleWithXpath(pageProperties.getProperty("ASVIBReview.TotalAmt1")))
			{
			userProfile.setTitle(browser.getTextByXpath(pageProperties.getProperty("ASVIBReview.TotalAmt1")));
			}
	}
	
	public void verifyErrorReviewPage(UserProfile userProfile,ArrayList<String> errList)
	{
		String[] strInput = {"","abcd","abcd12345#","#a2rg56hg65b","1111111111111111111","1234567890","012345678a"
				,"1987456321","078965412*0"};
		for(int i = 0; i < strInput.length ; i++)
		{
				verifyIsElementVisibleWithXpath(
						pageProperties.getProperty("ASVIBReview.MobileNumberRadio"),
						"Mobile Number radio button");
				verifyAndClickWithXpath(
						pageProperties.getProperty("ASVIBReview.MobileNumberRadio"),
						"Mobile Number radio button");
				verifyAndInputByXpath(
						pageProperties.getProperty("ASVIBReview.MobileNumberText"),
						"Mobile Number Text", strInput[i]);
				verifyIsElementVisibleWithXpath(
						pageProperties.getProperty("ASVIBReview.Continue"),
						"Continue button");

				verifyAndClickWithXpath(pageProperties.getProperty("ASVIBReview.Continue"),
						"Confirm");
			new RegistrationPage().verifyErrorMsg(browser.getTextByXpath("//*[@class='error']/ul[1]/li[1]")
					,errList.get(0),"Mobile Number");
			
			
			}
		for(int i = 0; i < strInput.length ; i++)
		{
				verifyIsElementVisibleWithXpath(
						pageProperties.getProperty("ASVIBReview.HomeNumberRadio"),
						"Home Number radio button");
				verifyAndClickWithXpath(
						pageProperties.getProperty("ASVIBReview.HomeNumberRadio"),
						"Home Number radio button");
				verifyAndInputByXpath(
						pageProperties.getProperty("ASVIBReview.HomeNumberText"),
						"Mobile Number Text", strInput[i]);
				verifyIsElementVisibleWithXpath(
						pageProperties.getProperty("ASVIBReview.Continue"),
						"Continue button");

				verifyAndClickWithXpath(pageProperties.getProperty("ASVIBReview.Continue"),
						"Confirm");
			new RegistrationPage().verifyErrorMsg(browser.getTextByXpath("//*[@class='error']/ul[1]/li[1]")
					,errList.get(1),"Mobile Number");
			
			
			}
			
		
	}
	
	public void verifyReviewComponents(UserProfile userProfile)
	{
		verifyIsElementVisibleById(
				pageProperties.getProperty("ASVIBPayment.CardType"),
				"Payment card type");
		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("ASVIBPayment.CardName"),
				"Payment card Name");
		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("ASVIBPayment.cardNumber"),
				"Payment card number");
		verifyIsElementVisibleById(
				pageProperties.getProperty("ASVIBPayment.cardStartMonth"),
				"Payment card start month field");
		verifyIsElementVisibleById(
				pageProperties.getProperty("ASVIBPayment.cardStartYear"),
				"Payment card start year field");
		verifyIsElementVisibleById(
				pageProperties.getProperty("ASVIBPayment.cardExpiryMonth"),
				"Payment card end month field");
		verifyIsElementVisibleById(
				pageProperties.getProperty("ASVIBPayment.cardExpiryYear"),
				"Payment card end year field");
		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("ASVIBPayment.cardCVV"),
				"Payment card CVV field");
		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("ASVIBPayment.submitButton"),
				"Payment submit field");
		if(browser.getTextByXpath(pageProperties.getProperty("ASVIBPayment.TotalAmountOverlay")).trim().equalsIgnoreCase(userProfile.getTitle()))
		{
			Report.updateTestLog("The total amount is "+userProfile.getTitle()+" same as in Review page", "PASS");
		}
		else
		{
			Report.updateTestLog("The total amount is "+userProfile.getTitle()+" not same as in Review page", "FAIL");
		}
	}

	public void errorPayIterate(int c,ArrayList<String> errList)
	{
		ArrayList<String> tempList = errList;
		String strTemp;
		for( int a = 1 ; a <= c ; a++)
		{
			strTemp = null;
			System.out.println("************************************************"+a);
			
			for(int b=0;b<errList.size();b++)
			{
				//System.out.println("*****************************************ACTUAL*******"+browser.getTextByXpath("//div[@id='error']/ul/li[1]".replace("1", ""+a)).trim());
				//System.out.println("*EXPECTED***********************************************"+tempList.get(b));
				if (browser.getTextByXpath("//div[@id='error']/ul/li[1]".replace("1", ""+a)).trim().contains(tempList.get(b).trim())) {
		             Report.updateTestLog("Displayed Error Message Validation For Payment details Is  :" + browser.getTextByXpath("//div[@id='error']/ul/li[1] ".replace("1", ""+a)).trim()
		            		 , "PASS");	
		             strTemp = tempList.get(b);
		             //tempList.set(b, "****************************Already used this error");
		         } else
		         {
		        	 if(b == errList.size()-1 && strTemp == null)
		        	 {
		             Report.updateTestLog("Expected Error Message Validation Was Not Displayed For Payment details", "FAIL");
		             System.out.println("*****************************************ACTUAL*******"+browser.getTextByXpath("//div[@id='error']/ul/li[1]".replace("1", ""+a)).trim());
						System.out.println("*EXPECTED***********************************************"+tempList.get(b));
						
		        	 }
		         }		
			}
		}
	}
	
	public void submitPay()
	{
		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("ASVIBPayment.submitButton"),
				"Payment submit field");
		verifyAndClickWithXpath(
				pageProperties.getProperty("ASVIBPayment.submitButton"),
				"Submit Button");
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> errorListAdd()
	{
		browser.wait(500);
		ArrayList<String> clone = (ArrayList<String>)cloneErrList.clone();
		ArrayList<String> errList = clone;
		/*errList.add("Card type : Please select a card type.");
	    errList.add("Card holder name : Please enter the name as it appears on your card.");
	    errList.add("Card number : Please enter the card number exactly as it appears on your card.");
	    errList.add("Card expiry date : Please enter the expiry date as it appears on your card.");
	    errList.add("Card security number : A required field has not been completed.");		    
	    errList.add("Card start date : Please enter the start date as it appears on your card.");	
	    errList.add("Issue number : Please enter a valid issue number.");
	    errList.add("Card security number : This should contain numbers only. This is a 3 digit security code which is printed on the reverse of your card, at the end of the signature panel.");
		*/return errList;
	}
	
	public void firstIterate(UserProfile userProfile,ArrayList<String> errList,String strCardType)
	{
		ArrayList<String> lstTemp = errList;
		verifyReviewComponents(userProfile);
		submitPay();
		
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(5, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		browser.wait(1000);
		verifyReviewComponents(userProfile);
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.CardType"), "Card Type",
				strCardType);
		submitPay();
		lstTemp.remove(0);
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(4, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());

		verifyReviewComponents(userProfile);
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.CardType"), "Card Type",
				"Select");
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.CardName"),
				"Card Name", "Test");
		submitPay();
		lstTemp.remove(1);
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(4, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		
		verifyReviewComponents(userProfile);		
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.CardName"),
				"Card Name", "");
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardNumber"),
				"Card Number", "4539791001730106");
		submitPay();
		lstTemp.remove(2);
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(4, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyReviewComponents(userProfile);	
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardNumber"),
				"Card Number", "");
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardStartMonth"),
				"Start Month", "11");
		submitPay();
	
	
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(6, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyReviewComponents(userProfile);	
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardStartMonth"),
				"Start Month", "Select");
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardStartYear"),
				"Card Start Year", "2008");
		submitPay();
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(6, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyReviewComponents(userProfile);	
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardStartYear"),
				"Card Start Year", "Select");
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryMonth"),
				"Card Expiry Month", "3");
		submitPay();
		
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(5, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyReviewComponents(userProfile);	
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryYear"),
				"Card Expiry Year", "2014");
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryMonth"),
				"Card Expiry Month", "Select");
		submitPay();
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(5, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyReviewComponents(userProfile);
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryYear"),
				"Card Expiry Year", "Select");
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardCVV"),
				"Card CVV Number", "123");
		submitPay();
		lstTemp.remove(4);
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(4, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		
		verifyReviewComponents(userProfile);
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardCVV"),
				"Card CVV Number", "");
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardIssueNumber"),
				"Card issue Number", "9");
		submitPay();
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(5, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardIssueNumber"),
				"Card issue Number", "");
	
	}
	
	public void secondIterate(UserProfile userProfile,ArrayList<String> errList,String strCardType)
	{			
		ArrayList<String> lstTemp = errList;
		verifyReviewComponents(userProfile);
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.CardType"), "Card Type",
				strCardType);
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.CardName"),
				"Card Name", "Test");
		submitPay();
		lstTemp.remove(0);
		lstTemp.remove(0);
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(3, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		
		verifyReviewComponents(userProfile);		
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.CardName"),
				"Card Name", "");
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardNumber"),
				"Card Number", "4539791001730106");
		submitPay();
		lstTemp.remove(0);
		lstTemp.remove(1);
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(3, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyReviewComponents(userProfile);	
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardNumber"),
				"Card Number", "");
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardStartMonth"),
				"Start Month", "11");
		submitPay();
		lstTemp.remove(0);				
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(5, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyReviewComponents(userProfile);	
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardStartMonth"),
				"Start Month", "Select");
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardStartYear"),
				"Card Start Year", "2008");
		submitPay();
		lstTemp.remove(0);				
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(5, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyReviewComponents(userProfile);	
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardStartYear"),
				"Card Start Year", "Select");
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryMonth"),
				"Card Expiry Month", "3");
		submitPay();
		lstTemp.remove(0);		
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(4, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyReviewComponents(userProfile);	
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryYear"),
				"Card Expiry Year", "2014");
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryMonth"),
				"Card Expiry Month", "Select");
		submitPay();
		lstTemp.remove(0);		
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(4, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyReviewComponents(userProfile);
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryYear"),
				"Card Expiry Year", "Select");
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardCVV"),
				"Card CVV Number", "123");
		submitPay();
		lstTemp.remove(0);
		lstTemp.remove(3);
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(3, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		
		verifyReviewComponents(userProfile);
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardCVV"),
				"Card CVV Number", "");	
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.CardType"), "Card Type",
				"Select");
	}
	

	public void thirdIterate(UserProfile userProfile,ArrayList<String> errList,String strCardType)
	{			
		ArrayList<String> lstTemp = errList;
		verifyReviewComponents(userProfile);
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.CardType"), "Card Type",
				strCardType);
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.CardName"),
				"Card Name", "Test");
		submitPay();
		lstTemp.remove(1);	
		lstTemp.remove(0);
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(3, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		
		verifyReviewComponents(userProfile);		
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.CardType"), "Card Type",
				"Select");
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardNumber"),
				"Card Number", "4539791001730106");
		submitPay();
		lstTemp.remove(2);
		lstTemp.remove(1);	
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(3, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyReviewComponents(userProfile);	
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardNumber"),
				"Card Number", "");
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardStartMonth"),
				"Start Month", "11");
		submitPay();
		lstTemp.remove(1);	
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(5, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyReviewComponents(userProfile);	
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardStartMonth"),
				"Start Month", "Select");
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardStartYear"),
				"Card Start Year", "2008");
		submitPay();
		lstTemp.remove(1);	
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(5, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyReviewComponents(userProfile);	
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardStartYear"),
				"Card Start Year", "Select");
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryMonth"),
				"Card Expiry Month", "3");
		submitPay();
		lstTemp.remove(1);
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(4, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyReviewComponents(userProfile);	
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryYear"),
				"Card Expiry Year", "2014");
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryMonth"),
				"Card Expiry Month", "Select");
		submitPay();
		lstTemp.remove(1);
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(4, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyReviewComponents(userProfile);
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryYear"),
				"Card Expiry Year", "Select");
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardCVV"),
				"Card CVV Number", "123");
		submitPay();
		lstTemp.remove(4);
		lstTemp.remove(1);
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(3, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		
		verifyReviewComponents(userProfile);
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardCVV"),
				"Card CVV Number", "");
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardIssueNumber"),
				"Card issue Number", "9");
		submitPay();
		lstTemp.remove(1);		
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(4, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.CardName"),
				"Card Name", "");
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardIssueNumber"),
				"Card issue Number", "");
	}
	
	
	
	public void fourthIterate(UserProfile userProfile,ArrayList<String> errList,String strCardType)
	{			
		ArrayList<String> lstTemp = errList;
		
		verifyReviewComponents(userProfile);
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.CardType"), "Card Type",
				strCardType);
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardNumber"),
				"Card Number", "4539791001730106");
		submitPay();
		lstTemp.remove(2);	
		lstTemp.remove(0);
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(3, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());		
	
		
		verifyReviewComponents(userProfile);
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.CardType"), "Card Type",
				"Select");
		
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardStartMonth"),
				"Start Month", "11");
		submitPay();
		lstTemp.remove(2);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(5, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyReviewComponents(userProfile);	
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardStartMonth"),
				"Start Month", "Select");
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardStartYear"),
				"Card Start Year", "2008");
		submitPay();
		lstTemp.remove(2);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(5, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyReviewComponents(userProfile);	
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardStartYear"),
				"Card Start Year", "Select");
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryMonth"),
				"Card Expiry Month", "3");
		submitPay();
		lstTemp.remove(2);
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(4, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyReviewComponents(userProfile);	
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryYear"),
				"Card Expiry Year", "2014");
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryMonth"),
				"Card Expiry Month", "Select");
		submitPay();
		lstTemp.remove(2);
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(4, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyReviewComponents(userProfile);
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryYear"),
				"Card Expiry Year", "Select");
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardCVV"),
				"Card CVV Number", "123");
		submitPay();
		lstTemp.remove(4);
		lstTemp.remove(2);
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(3, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		
		verifyReviewComponents(userProfile);
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardCVV"),
				"Card CVV Number", "");
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardIssueNumber"),
				"Card issue Number", "9");
		submitPay();
		lstTemp.remove(2);
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(4, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardNumber"),
				"Card Number", "");
	}
	

	public void fifthIterate(UserProfile userProfile,ArrayList<String> errList,String strCardType)
	{			
		ArrayList<String> lstTemp = errorListAdd();
		
		
		
		verifyReviewComponents(userProfile);	
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardStartMonth"),
				"Start Month", "11");
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardStartYear"),
				"Card Start Year", "2008");
		submitPay();	
		
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(5, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		
		verifyReviewComponents(userProfile);	
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardStartYear"),
				"Card Start Year", "Select");
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryMonth"),
				"Card Expiry Month", "3");
		submitPay();
		
		
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(6, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyReviewComponents(userProfile);	
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryYear"),
				"Card Expiry Year", "2014");
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryMonth"),
				"Card Expiry Month", "Select");
		submitPay();
		
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(6, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyReviewComponents(userProfile);
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryYear"),
				"Card Expiry Year", "Select");
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardCVV"),
				"Card CVV Number", "123");
		submitPay();
		lstTemp.remove(4);		
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(5, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		
		verifyReviewComponents(userProfile);
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardCVV"),
				"Card CVV Number", "");
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardIssueNumber"),
				"Card issue Number", "9");
		submitPay();
		
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(6, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardStartMonth"),
				"Start Month", "Select");
	}
	
	
	

	
	public void sixthIterate(UserProfile userProfile,ArrayList<String> errList,String strCardType)
	{			
		ArrayList<String> lstTemp = errorListAdd();
		
				
		verifyReviewComponents(userProfile);	
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardStartMonth"),
				"Start Month", "11");
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardStartYear"),
				"Card Start Year", "2008");
		submitPay();
		lstTemp.remove(2);
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(4, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyReviewComponents(userProfile);	
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardStartMonth"),
				"Card Start Year", "Select");
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryMonth"),
				"Card Expiry Month", "3");
		submitPay();
		lstTemp.remove(2);
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(4, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyReviewComponents(userProfile);	
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryYear"),
				"Card Expiry Year", "2014");
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryMonth"),
				"Card Expiry Month", "Select");
		submitPay();
		lstTemp.remove(2);
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(4, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyReviewComponents(userProfile);
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryYear"),
				"Card Expiry Year", "Select");
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardCVV"),
				"Card CVV Number", "123");
		submitPay();
		lstTemp.remove(4);
		lstTemp.remove(2);
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(3, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		
		verifyReviewComponents(userProfile);
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardCVV"),
				"Card CVV Number", "");
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardIssueNumber"),
				"Card issue Number", "9");
		submitPay();
		lstTemp.remove(2);
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(4, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardStartYear"),
				"Card Number", "");
	}
	

	public void seventhIterate(UserProfile userProfile,ArrayList<String> errList,String strCardType)
	{			
		ArrayList<String> lstTemp = errorListAdd();
		
		verifyReviewComponents(userProfile);	
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryYear"),
				"Card Expiry Year", "2014");
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryMonth"),
				"Card Expiry Month", "11");
		submitPay();
		lstTemp.remove(3);
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(4, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyReviewComponents(userProfile);
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryYear"),
				"Card Expiry Year", "Select");
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardCVV"),
				"Card CVV Number", "123");
		submitPay();
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(5, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		
		verifyReviewComponents(userProfile);
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardCVV"),
				"Card CVV Number", "");
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardIssueNumber"),
				"Card issue Number", "9");
		submitPay();
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(5, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryMonth"),
				"Start Month", "Select");
	}
	public void eighthIterate(UserProfile userProfile,ArrayList<String> errList,String strCardType)
	{			
		ArrayList<String> lstTemp = errorListAdd();
		
		verifyReviewComponents(userProfile);	
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryYear"),
				"Card Expiry Year", "2014");
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryMonth"),
				"Card Expiry Month", "11");
		submitPay();
		lstTemp.remove(3);
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(4, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyReviewComponents(userProfile);
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryMonth"),
				"Card Expiry Year", "Select");
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardCVV"),
				"Card CVV Number", "123");
		submitPay();
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(5, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		
		verifyReviewComponents(userProfile);
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardCVV"),
				"Card CVV Number", "");
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardIssueNumber"),
				"Card issue Number", "9");
		submitPay();
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(5, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryYear"),
				"Start Month", "Select");
	}
	
	public void ninthIterate(UserProfile userProfile,ArrayList<String> errList,String strCardType)
	{			
		ArrayList<String> lstTemp = errorListAdd();
		

		verifyReviewComponents(userProfile);
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardCVV"),
				"Card CVV Number", "123");
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardIssueNumber"),
				"Card issue Number", "9");
		submitPay();
		lstTemp.remove(4);
		lstTemp.remove(lstTemp.size()-3);
		lstTemp.remove(lstTemp.size()-2);
		lstTemp.remove(lstTemp.size()-1);
		errorPayIterate(5, lstTemp);
		lstTemp.clear();
		lstTemp.addAll(errorListAdd());
		
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardCVV"),
				"Start Month", "Select");
	}
	
	
	
	public void navigateToPaymentPage(UserProfile userProfile)
	{
		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("ASVIBReview.COD"), "COD");
		verifyAndClickWithXpath(pageProperties.getProperty("ASVIBReview.COD"),
				"Add COD");
		userProfile.setTitle(browser.getTextByXpath(pageProperties.getProperty("ASVIBReview.TotalAmt")));
		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("ASVIBReview.Continue"),
				"Continue button ");
		verifyAndClickWithXpath(pageProperties.getProperty("ASVIBReview.Continue"),
				"Confirm");
		browser.wait(3000);
		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("ASVIBPayment.PopUp"), "Payment popup");

		verifyAndClickWithXpath(pageProperties.getProperty("ASVIBPayment.PopUp"),
				"Payment Popup");
		browser.wait(1000);		
		
	
	}
	ArrayList<String> cloneErrList;
	@SuppressWarnings("unchecked")
	public void verifyErrorPaymentPage(UserProfile userProfile,ArrayList<String> errList)
	{
		ArrayList<String> clone1 = (ArrayList<String>)errList.clone();
		cloneErrList =  clone1;
		String[] strInput = {"","ab8cd","abcd12345#","#a2rg56hg65b","1111111111111111111","1234567890","012345678a"
				,"198745ab6321","078965412*0"};
		navigateToPaymentPage(userProfile);
		ArrayList<String> strCardTypes = browser.getFromDropBox("XPath", pageProperties.getProperty("ASVIBPayment.CardType"));
		browser.wait(1000);
		for(int i = 1;i<strCardTypes.size();i++)
		{
		firstIterate(userProfile, errList,strCardTypes.get(i));
		secondIterate(userProfile, errList,strCardTypes.get(i));
		thirdIterate(userProfile, errList,strCardTypes.get(i));
		fourthIterate(userProfile, errList,strCardTypes.get(i));
		fifthIterate(userProfile, errList,strCardTypes.get(i));
		sixthIterate(userProfile, errList, strCardTypes.get(i));
		seventhIterate(userProfile, errList, strCardTypes.get(i));
		eighthIterate(userProfile, errList, strCardTypes.get(i));
		ninthIterate(userProfile, errList, strCardTypes.get(i));
		}
		/*verifyReviewComponents();
		verifyAndClickWithXpath(
				pageProperties.getProperty("ASVIBPayment.submitButton"),
				"Submit Button");*/

		browser.wait(3000);
		ArrayList lstTemp = errorListAdd();
		for(int i = 0; i < strInput.length ; i++)
		{
			verifyAndInputByXpath(
					pageProperties.getProperty("ASVIBPayment.CardName"),
					"Payment card Name",strInput[i]);
			
			verifyAndClickWithXpath(
					pageProperties.getProperty("ASVIBPayment.submitButton"),
					"Payment submit field");
			
			lstTemp.remove(lstTemp.size()-3);
			lstTemp.remove(lstTemp.size()-2);
			lstTemp.remove(lstTemp.size()-1);
			errorPayIterate(5, lstTemp);
			lstTemp.clear();
			lstTemp.addAll(errorListAdd());
			
			verifyAndInputByXpath(
					pageProperties.getProperty("ASVIBPayment.cardNumber"),
					"Payment card number",strInput[i]);
			verifyAndClickWithXpath(
					pageProperties.getProperty("ASVIBPayment.submitButton"),
					"Payment submit field");
			lstTemp.remove(lstTemp.size()-3);
			lstTemp.remove(lstTemp.size()-2);
			lstTemp.remove(lstTemp.size()-1);
			errorPayIterate(5, lstTemp);
			lstTemp.clear();
			lstTemp.addAll(errorListAdd());
			
			verifyAndInputByXpath(
					pageProperties.getProperty("ASVIBPayment.cardCVV"),
					"Payment card CVV field",strInput[i]);			
			verifyAndClickWithXpath(
					pageProperties.getProperty("ASVIBPayment.submitButton"),
					"Payment submit field");			
			lstTemp.remove(lstTemp.size()-3);
			lstTemp.remove(lstTemp.size()-2);
			lstTemp.remove(lstTemp.size()-1);
			errorPayIterate(5, lstTemp);
			lstTemp.clear();
			lstTemp.addAll(errorListAdd());
		}
		
			
		
	}
	
	
	public void selectedAppChk(UserProfile userProfile)
	{
		if(browser.isTextPresent(userProfile.getFirstName()))
		{
			Report.updateTestLog("Selected Appliance "+userProfile.getFirstName()+" is present", "PASS");
		}
		else
		{
			Report.updateTestLog("Selected Appliance "+userProfile.getFirstName()+" is not present", "FAIL");
		}
		/*if(browser.isElementVisibleWithXpath(pageProperties.getProperty("ASVIBBookAppointment.FirstSlotDate")))
		{
			System.out.println("--------"+userProfile.getLastName().substring(38));
		if(browser.getTextByXpath(pageProperties.getProperty("ASVIBBookAppointment.FirstSlotDate")).trim().contains(userProfile.getLastName())
				||browser.getTextByXpath(pageProperties.getProperty("ASVIBBookAppointment.FirstSlotDate")).trim().contains(userProfile.getLastName().substring(38)))
		{
			Report.updateTestLog("Same first slot is present as in account summary page "+userProfile.getLastName(), "PASS");
		}
		else
		{
			Report.updateTestLog("Same first slot date "+browser.getTextByXpath(pageProperties.getProperty("ASVIBBookAppointment.FirstSlotDate"))
					+"is not present ,as in account summary page "+userProfile.getLastName(), "FAIL");
		}
		}*/
	}
	
	public void navigateToConfirmation() {
		
		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("ASVIBReview.Continue"),
				"Continue button");

		verifyAndClickWithXpath(pageProperties.getProperty("ASVIBReview.Continue"),
				"Confirm");
		browser.wait(500);
		
		if (browser.isElementVisibleWithXpath(pageProperties
				.getProperty("ASVIBPayment.PopUp"))) {
			verifyIsElementVisibleWithXpath(
					pageProperties.getProperty("ASVIBPayment.PopUp"), "popup");
			Report.updateTestLog("Payment popup is displayed", "Done");
			payment();
		}
		verifyConfirmationPage();
	}
	public void payment() {
		browser.wait(1000);
		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("ASVIBPayment.PopUp"), "Payment popup");

		verifyAndClickWithXpath(pageProperties.getProperty("ASVIBPayment.PopUp"),
				"Payment Popup");
		browser.wait(1000);
		verifyIsElementVisibleById(
				pageProperties.getProperty("ASVIBPayment.CardType"),
				"Payment card type");
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.CardType"), "Card Type",
				"Visa Debit");

		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("ASVIBPayment.CardName"),
				"Payment card Name");
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.CardName"),
				"Card Name", "Test");

		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("ASVIBPayment.cardNumber"),
				"Payment card number");
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardNumber"),
				"Card Number", "4539791001730106");

		verifyIsElementVisibleById(
				pageProperties.getProperty("ASVIBPayment.cardStartMonth"),
				"Payment card start month field");

		/*verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardStartMonth"),
				"Start Month", "11");*/
		verifyIsElementVisibleById(
				pageProperties.getProperty("ASVIBPayment.cardStartYear"),
				"Payment card start year field");
		/*verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardStartYear"),
				"Card Start Year", "2008");*/

		verifyIsElementVisibleById(
				pageProperties.getProperty("ASVIBPayment.cardExpiryMonth"),
				"Payment card end month field");
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryMonth"),
				"Card Expiry Month", "Mar");

		verifyIsElementVisibleById(
				pageProperties.getProperty("ASVIBPayment.cardExpiryYear"),
				"Payment card end year field");
		verifyAndSelectDropDownBox(
				pageProperties.getProperty("ASVIBPayment.cardExpiryYear"),
				"Card Expiry Year", "2014");

		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("ASVIBPayment.cardCVV"),
				"Payment card CVV field");
		verifyAndInputByXpath(pageProperties.getProperty("ASVIBPayment.cardCVV"),
				"Card CVV Number", "123");

		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("ASVIBPayment.submitButton"),
				"Payment submit field");
		verifyAndClickWithXpath(
				pageProperties.getProperty("ASVIBPayment.submitButton"),
				"Submit Button");

		browser.wait(3000);

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

	public void navigateToReview()
	{
		browser.wait(1000);
		/*verifyIsTextPresent(pageProperties
				.getProperty("ASVIBBookAppointment.MessageText"));*/
		verifyIsTextPresent(pageProperties
				.getProperty("ASVIBBookAppointment.SlotsAvailable"));
		verifyIsTextPresent(pageProperties
				.getProperty("ASVIBBookAppointment.DifferentAppointment"));
		verifyIsElementVisibleWithXpath(
				pageProperties.getProperty("ASVIBBookAppointment.Continue"),
				"Continue button");
		verifyAndClickWithXpath(
				pageProperties.getProperty("ASVIBBookAppointment.Continue"),
				"Book This Appointment");
	}
	
	public void verifyConfirmationPage() {
	
		verifyIsTextPresent(
				pageProperties.getProperty("ASVIBConfirmation.LongMessageText"),
				"Text:We'll send a reminder nearer the time of your booking, and the engineer will call you on the day to let you know when they're on their way");
		verifyIsTextPresent(
				pageProperties.getProperty("ASVIBConfirmation.TrackCancelChange"),
				"TrackCancelChange link");
		

		/*if (browser.getTextByXpath(pageProperties.getProperty("ASVIBFASV.ConfirmationHeader")).contains(
				"Your annual service is booked")
				|| browser.getTextByXpath(pageProperties.getProperty("ASVIBFASV.ConfirmationHeader")).contains(
						"Your appointment has been rescheduled")|| browser.getTextByXpath(pageProperties.getProperty("ASVIBFASV.ConfirmationHeader")).contains(
								"Your engineer's visit is booked")) {
			Report.updateTestLog("Confirmation page is loaded", "PASS");
		} else {
			Report.updateTestLog("Confirmation page is not loaded", "FAIL");

		}*/
		if (browser.isTextPresent(
		"Your annual service is booked"))
		{
			Report.updateTestLog("Confirmation page is loaded", "PASS");
		}
		else if( browser.isTextPresent(
				"Your appointment has been rescheduled"))
		{
			Report.updateTestLog("Confirmation page is loaded", "PASS");
		}
		else if( browser.isTextPresent(
						"Your engineer's visit is booked")) {
	Report.updateTestLog("Confirmation page is loaded", "PASS");
}
		else if(browser.isTextPresent(pageProperties.getProperty("ASVIBConfirmation.TrackCancelChange")))
		{
			Report.updateTestLog("Confirmation page is loaded", "PASS");
		}
				else {
	Report.updateTestLog("Confirmation page is not loaded", "FAIL");

}
		
		
	}
	
	public void verifyAddress(UserProfile userProfile)
	{
		/*List<String> lstAddr =  new SiebelDataBase().getAddress(userProfile.getAccNumber());
		Iterator iteAddr = lstAddr.iterator();
		String strIteAddress = "";
		while(iteAddr.hasNext())
		{
			try
			{
			strIteAddress = iteAddr.next().toString();
			System.out.println("================{Address}=========================>>>"+strIteAddress);
			verifyIsTextPresent(strIteAddress);
			}
			catch(Exception ex)
			{
				
			}
		}*/
		
		verifyIsTextPresent(userProfile.getHomeNumber(),"House Number");
		//verifyIsTextPresent(userProfile.getStreet(), "Street Name");
		//verifyIsTextPresent(userProfile.getLocalArea(), "Local Area");
		//verifyIsTextPresent(userProfile.getCity(),"City");
		//verifyIsTextPresent(userProfile.getCountry(),"Country");
	}

	public void accountSummaryChange()
	{
		browser.wait(1000);
		verifyIsTextPresent(
				pageProperties.getProperty("ASVIBAccountSummary.Change"),
				"Change Appointment link");
		verifyAndClickWithLinkText(
				pageProperties.getProperty("ASVIBAccountSummary.Change"),
				"Change Appointment");
	}

	public void verifyEmergencyMessage(int intflag)
	{
		if(intflag == 0)
		{
		verifyIsTextPresent("Urgent situation");

		verifyIsTextPresent("If you, a member of your household, or your property are at immediate risk");
		
		if(verifyIsTextPresent("If you smell burning, or if there is smoke coming from your fuseboard, lights or sockets"))
		{
			verifyIsTextPresent("If you smell burning, or if there is smoke coming from your fuseboard, lights or sockets");
		}
		verifyIsTextPresent("please call 999 immediately");
		}
		if(intflag == 4)
		{
			verifyIsTextPresent("At immediate risk?");
			verifyIsTextPresent("If you or your propery is at immediate risk");	
		}

	}

	public void firstAvailableSlot(UserProfile userProfile)
	{
		userProfile.setLastName(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.FirstSlot")));
	}
	public void verifyAccountOverview(String strStatus)
	{
		if(strStatus == "Ideal")
		{		
		//verifyIsTextPresent(pageProperties.getProperty("AccountOverview.VerifyFirstAvailableText"),"First available time slot text");
		checkDateFormat();
		//verifyIsTextPresent(pageProperties.getProperty("AccountOverview.verifyBookThisApp"), "Book this appointment link");
		//verifyIsTextPresent(pageProperties.getProperty("AccountOverview.VerifyviewAllSlot"), "View all available slots link");
		//verifyIsTextPresent(pageProperties.getProperty("AccountOverview.VerifyInfoText"), "Information Text");
		//verifyIsTextPresent(pageProperties.getProperty("AccountOverview.VerifyASVinfoText"), "Information Text");
			
		}
		if(strStatus == "EVE")
		{
			if(browser.isTextPresent("6pm - 8pm"))
			{
				Report.updateTestLog("Eve slots 6pm - 8pm available in the page", "PASS");
			}
			else
			{
				Report.updateTestLog("Eve slots 6pm - 8pm is not available in the page", "FAIL");
			}			
		}
		
		if(strStatus == "Comp")
		{
			browser.wait(getWaitTime());
			
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.YourAppointmentSection")).equalsIgnoreCase("Your appointment"))
			{
				Report.updateTestLog("Your Appointment Section Is Present", "PASS");
			}
			else
			{
				Report.updateTestLog("Your Appointment Section Is Not Present", "FAIL");
			}
			
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.BookedStatusScetion")).equalsIgnoreCase("Your appointment status"))
			{
				Report.updateTestLog("Your appointment status Section Is Present", "PASS");
			}
			else
			{
				Report.updateTestLog("Your appointment status Section Is Not Present", "FAIL");
			}
			if(browser.isElementVisibleWithXpath(pageProperties.getProperty("ASVIBAccountSummary.CompletedStatus")))
			{
				Report.updateTestLog("Completed appointment status Section Is Present", "PASS");
			}
			else
			{
				Report.updateTestLog("Completed appointment status Section Is Not Present", "FAIL");
			}
			
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.AllDone")).equalsIgnoreCase("All done"))
			{
				Report.updateTestLog("All Done status Section Is Present", "PASS");
			}
			else
			{
				Report.updateTestLog("All Done status Section Is Not Present", "FAIL");
			}
			verifyIsElementVisibleWithXpath(pageProperties.getProperty("ASVIBAccountSummary.CalendarImg"),"Calendar Image");
			verifyIsTextPresent(pageProperties.getProperty("ASVIBContactDetails.YourAppointmentText"));
			checkDateFormat();
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step1")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.booked")))
			{
				Report.updateTestLog("Step 1 contains "+pageProperties.getProperty("ASVIBAccountSummary.booked"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 1 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.booked"), "FAIL");
			}
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step2")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.preparing")))
			{
				Report.updateTestLog("Step 2 contains "+pageProperties.getProperty("ASVIBAccountSummary.preparing"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 2 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.preparing"), "FAIL");
			}
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step3")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.Engineeronway")))
			{
				Report.updateTestLog("Step 3 contains "+pageProperties.getProperty("ASVIBAccountSummary.Engineeronway"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 3 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.Engineeronway"), "FAIL");
			}
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step4")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.alldone")))
			{
				Report.updateTestLog("Step 4 contains "+pageProperties.getProperty("ASVIBAccountSummary.alldone"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 4 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.alldone"), "FAIL");
			}
			
			
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.Cancel"));
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.Change"));
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.AppointmentBookedFor"));
			
			
		}
		
		if(strStatus == "Site")
		{
			browser.wait(getWaitTime());
			
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.YourAppointmentSection")).equalsIgnoreCase("Your appointment"))
			{
				Report.updateTestLog("Your Appointment Section Is Present", "PASS");
			}
			else
			{
				Report.updateTestLog("Your Appointment Section Is Not Present", "FAIL");
			}
			
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.BookedStatusScetion")).equalsIgnoreCase("Your appointment status"))
			{
				Report.updateTestLog("Your appointment status Section Is Present", "PASS");
			}
			else
			{
				Report.updateTestLog("Your appointment status Section Is Not Present", "FAIL");
			}
			if(browser.isElementVisibleWithXpath(pageProperties.getProperty("ASVIBAccountSummary.Site")))
			{
				Report.updateTestLog("Site status Section Is Present", "PASS");
			}
			else
			{
				Report.updateTestLog("Site status Section Is Not Present", "FAIL");
			}
			
			verifyIsElementVisibleWithXpath(pageProperties.getProperty("ASVIBAccountSummary.CalendarImg"),"Calendar Image");
			verifyIsTextPresent(pageProperties.getProperty("ASVIBContactDetails.YourAppointmentText"));
			checkDateFormat();
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step1")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.comingtoday")))
			{
				Report.updateTestLog("Step 1 contains "+pageProperties.getProperty("ASVIBAccountSummary.booked"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 1 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.booked"), "FAIL");
			}
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step2")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.preparing")))
			{
				Report.updateTestLog("Step 2 contains "+pageProperties.getProperty("ASVIBAccountSummary.preparing"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 2 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.preparing"), "FAIL");
			}
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step3")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.Engineeronway")))
			{
				Report.updateTestLog("Step 3 contains "+pageProperties.getProperty("ASVIBAccountSummary.Engineeronway"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 3 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.Engineeronway"), "FAIL");
			}
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step4")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.alldone")))
			{
				Report.updateTestLog("Step 4 contains "+pageProperties.getProperty("ASVIBAccountSummary.alldone"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 4 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.alldone"), "FAIL");
			}
			
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.AppointmentBookedFor"));
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.Status")).contains("Engineer on the way"))
			{
				Report.updateTestLog("Engineer on the way status is Present in the status section", "PASS");
			}
			else
			{
				Report.updateTestLog("Engineer on the way status is not Present in the status section", "FAIL");
			}
			
		}
		
		if(strStatus == "Route")
		{
			browser.wait(getWaitTime());
			
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.YourAppointmentSection")).equalsIgnoreCase("Your appointment"))
			{
				Report.updateTestLog("Your Appointment Section Is Present", "PASS");
			}
			else
			{
				Report.updateTestLog("Your Appointment Section Is Not Present", "FAIL");
			}
			
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.BookedStatusScetion")).equalsIgnoreCase("Your appointment status"))
			{
				Report.updateTestLog("Your appointment status Section Is Present", "PASS");
			}
			else
			{
				Report.updateTestLog("Your appointment status Section Is Not Present", "FAIL");
			}
			if(browser.isElementVisibleWithXpath(pageProperties.getProperty("ASVIBAccountSummary.CompletedStatus")))
			{
				Report.updateTestLog("Completed appointment status Section Is Present", "PASS");
			}
			else
			{
				Report.updateTestLog("Completed appointment status Section Is Not Present", "FAIL");
			}
			
			verifyIsElementVisibleWithXpath(pageProperties.getProperty("ASVIBAccountSummary.CalendarImg"),"Calendar Image");
			verifyIsTextPresent(pageProperties.getProperty("ASVIBContactDetails.YourAppointmentText"));
			checkDateFormat();
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step1")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.comingtoday")))
			{
				Report.updateTestLog("Step 1 contains "+pageProperties.getProperty("ASVIBAccountSummary.booked"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 1 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.booked"), "FAIL");
			}
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step2")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.preparing")))
			{
				Report.updateTestLog("Step 2 contains "+pageProperties.getProperty("ASVIBAccountSummary.preparing"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 2 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.preparing"), "FAIL");
			}
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step3")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.Engineeronway")))
			{
				Report.updateTestLog("Step 3 contains "+pageProperties.getProperty("ASVIBAccountSummary.Engineeronway"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 3 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.Engineeronway"), "FAIL");
			}
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step4")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.alldone")))
			{
				Report.updateTestLog("Step 4 contains "+pageProperties.getProperty("ASVIBAccountSummary.alldone"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 4 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.alldone"), "FAIL");
			}
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.Cancel"));
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.Engineeronway"));
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.Change"));
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.AppointmentBookedFor"));
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.Status")).contains("Engineer on the way"))
			{
				Report.updateTestLog("Engineer on the way status is Present in the status section", "PASS");
			}
			else
			{
				Report.updateTestLog("Engineer on the way status is not Present in the status section", "FAIL");
			}
		}
		if(strStatus == "Pend")
		{
			browser.wait(getWaitTime());
			
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.YourAppointmentSection")).equalsIgnoreCase("Your appointment"))
			{
				Report.updateTestLog("Your Appointment Section Is Present", "PASS");
			}
			else
			{
				Report.updateTestLog("Your Appointment Section Is Not Present", "FAIL");
			}
			
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.BookedStatusScetion")).equalsIgnoreCase("Your appointment status"))
			{
				Report.updateTestLog("Your appointment status Section Is Present", "PASS");
			}
			else
			{
				Report.updateTestLog("Your appointment status Section Is Not Present", "FAIL");
			}
			if(browser.isElementVisibleWithXpath(pageProperties.getProperty("ASVIBAccountSummary.Pend")))
			{
				Report.updateTestLog("Pending appointment status Section Is Present", "PASS");
			}
			else
			{
				Report.updateTestLog("Pending appointment status Section Is Not Present", "FAIL");
			}
			
			verifyIsElementVisibleWithXpath(pageProperties.getProperty("ASVIBAccountSummary.CalendarImg"),"Calendar Image");
			verifyIsTextPresent(pageProperties.getProperty("ASVIBContactDetails.YourAppointmentText"));
			checkDateFormat();
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step1")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.comingtoday")))
			{
				Report.updateTestLog("Step 1 contains "+pageProperties.getProperty("ASVIBAccountSummary.booked"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 1 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.booked"), "FAIL");
			}
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step2")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.preparing")))
			{
				Report.updateTestLog("Step 2 contains "+pageProperties.getProperty("ASVIBAccountSummary.preparing"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 2 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.preparing"), "FAIL");
			}
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step3")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.Engineeronway")))
			{
				Report.updateTestLog("Step 3 contains "+pageProperties.getProperty("ASVIBAccountSummary.Engineeronway"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 3 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.Engineeronway"), "FAIL");
			}
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step4")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.alldone")))
			{
				Report.updateTestLog("Step 4 contains "+pageProperties.getProperty("ASVIBAccountSummary.alldone"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 4 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.alldone"), "FAIL");
			}
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.Cancel"));
			
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.Change"));
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.AppointmentBookedFor"));
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.Status")).contains("We're coming today"))
			{
				Report.updateTestLog("We're coming today status is Present in the status section", "PASS");
			}
			else
			{
				Report.updateTestLog("We're coming today status is not Present in the status section", "FAIL");
			}		
			
		}
		
		if(strStatus == "DISP")
		{
			browser.wait(getWaitTime());
			
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.YourAppointmentSection")).equalsIgnoreCase("Your appointment"))
			{
				Report.updateTestLog("Your Appointment Section Is Present", "PASS");
			}
			else
			{
				Report.updateTestLog("Your Appointment Section Is Not Present", "FAIL");
			}
			
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.BookedStatusScetion")).equalsIgnoreCase("Your appointment status"))
			{
				Report.updateTestLog("Your appointment status Section Is Present", "PASS");
			}
			else
			{
				Report.updateTestLog("Your appointment status Section Is Not Present", "FAIL");
			}
			if(browser.isElementVisibleWithXpath(pageProperties.getProperty("ASVIBAccountSummary.Disp")))
			{
				Report.updateTestLog("Disp appointment status Section Is Present", "PASS");
			}
			else
			{
				Report.updateTestLog("Disp appointment status Section Is Not Present", "FAIL");
			}
			
			verifyIsElementVisibleWithXpath(pageProperties.getProperty("ASVIBAccountSummary.CalendarImg"),"Calendar Image");
			verifyIsTextPresent(pageProperties.getProperty("ASVIBContactDetails.YourAppointmentText"));
			checkDateFormat();
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step1")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.comingtoday")))
			{
				Report.updateTestLog("Step 1 contains "+pageProperties.getProperty("ASVIBAccountSummary.booked"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 1 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.booked"), "FAIL");
			}
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step2")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.preparing")))
			{
				Report.updateTestLog("Step 2 contains "+pageProperties.getProperty("ASVIBAccountSummary.preparing"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 2 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.preparing"), "FAIL");
			}
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step3")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.Engineeronway")))
			{
				Report.updateTestLog("Step 3 contains "+pageProperties.getProperty("ASVIBAccountSummary.Engineeronway"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 3 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.Engineeronway"), "FAIL");
			}
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step4")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.alldone")))
			{
				Report.updateTestLog("Step 4 contains "+pageProperties.getProperty("ASVIBAccountSummary.alldone"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 4 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.alldone"), "FAIL");
			}
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.Cancel"));
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.Engineeronway"));
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.Change"));
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.AppointmentBookedFor"));
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.Status")).contains("Engineer preparing"))
			{
				Report.updateTestLog("Engineer Preparing status is Present in the status section", "PASS");
			}
			else
			{
				Report.updateTestLog("Engineer Preparing status is not Present in the status section", "FAIL");
			}		
			
		}
		
		if(strStatus == "Hold")
		{
			browser.wait(getWaitTime());
			
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.YourAppointmentSection")).equalsIgnoreCase("Your appointment"))
			{
				Report.updateTestLog("Your Appointment Section Is Present", "PASS");
			}
			else
			{
				Report.updateTestLog("Your Appointment Section Is Not Present", "FAIL");
			}
			
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.BookedStatusScetion")).equalsIgnoreCase("Your appointment status"))
			{
				Report.updateTestLog("Your appointment status Section Is Present", "PASS");
			}
			else
			{
				Report.updateTestLog("Your appointment status Section Is Not Present", "FAIL");
			}
			if(browser.isElementVisibleWithXpath(pageProperties.getProperty("ASVIBAccountSummary.Hold")))
			{
				Report.updateTestLog("Hold appointment status Section Is Present", "PASS");
			}
			else
			{
				Report.updateTestLog("Hold appointment status Section Is Not Present", "FAIL");
			}
			
			verifyIsElementVisibleWithXpath(pageProperties.getProperty("ASVIBAccountSummary.CalendarImg"),"Calendar Image");
			verifyIsTextPresent(pageProperties.getProperty("ASVIBContactDetails.YourAppointmentText"));
			checkDateFormat();
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step1")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.comingtoday")))
			{
				Report.updateTestLog("Step 1 contains "+pageProperties.getProperty("ASVIBAccountSummary.booked"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 1 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.booked"), "FAIL");
			}
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step2")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.preparing")))
			{
				Report.updateTestLog("Step 2 contains "+pageProperties.getProperty("ASVIBAccountSummary.preparing"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 2 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.preparing"), "FAIL");
			}
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step3")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.Engineeronway")))
			{
				Report.updateTestLog("Step 3 contains "+pageProperties.getProperty("ASVIBAccountSummary.Engineeronway"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 3 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.Engineeronway"), "FAIL");
			}
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step4")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.alldone")))
			{
				Report.updateTestLog("Step 4 contains "+pageProperties.getProperty("ASVIBAccountSummary.alldone"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 4 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.alldone"), "FAIL");
			}
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.Cancel"));
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.preparing"));
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.Change"));
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.AppointmentBookedFor"));
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.Status")).contains("Engineer preparing"))
			{
				Report.updateTestLog("Engineer Preparing status is Present in the status section", "PASS");
			}
			else
			{
				Report.updateTestLog("Engineer Preparing status is not Present in the status section", "FAIL");
			}		
			
		}
		
		if(strStatus == "Sched")
		{
			browser.wait(getWaitTime());
			
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.YourAppointmentSection")).equalsIgnoreCase("Your appointment"))
			{
				Report.updateTestLog("Your Appointment Section Is Present", "PASS");
			}
			else
			{
				Report.updateTestLog("Your Appointment Section Is Not Present", "FAIL");
			}
			
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.BookedStatusScetion")).equalsIgnoreCase("Your appointment status"))
			{
				Report.updateTestLog("Your appointment status Section Is Present", "PASS");
			}
			else
			{
				Report.updateTestLog("Your appointment status Section Is Not Present", "FAIL");
			}
			if(browser.isElementVisibleWithXpath(pageProperties.getProperty("ASVIBAccountSummary.Sched")))
			{
				Report.updateTestLog("Completed appointment status Section Is Present", "PASS");
			}
			else
			{
				Report.updateTestLog("Completed appointment status Section Is Not Present", "FAIL");
			}
			
			verifyIsElementVisibleWithXpath(pageProperties.getProperty("ASVIBAccountSummary.CalendarImg"),"Calendar Image");
			verifyIsTextPresent(pageProperties.getProperty("ASVIBContactDetails.YourAppointmentText"));
			checkDateFormat();
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step1")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.booked")))
			{
				Report.updateTestLog("Step 1 contains "+pageProperties.getProperty("ASVIBAccountSummary.booked"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 1 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.booked"), "FAIL");
			}
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step2")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.preparing")))
			{
				Report.updateTestLog("Step 2 contains "+pageProperties.getProperty("ASVIBAccountSummary.preparing"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 2 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.preparing"), "FAIL");
			}
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step3")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.Engineeronway")))
			{
				Report.updateTestLog("Step 3 contains "+pageProperties.getProperty("ASVIBAccountSummary.Engineeronway"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 3 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.Engineeronway"), "FAIL");
			}
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step4")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.alldone")))
			{
				Report.updateTestLog("Step 4 contains "+pageProperties.getProperty("ASVIBAccountSummary.alldone"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 4 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.alldone"), "FAIL");
			}
			
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.Cancel"));
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.Engineeronway"));
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.Change"));
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.AppointmentBookedFor"));
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.Status")).contains("You've booked"))
			{
				Report.updateTestLog(" You've Booked status is Present in the status section", "PASS");
			}
			else
			{
				Report.updateTestLog(" You've Booked status is not Present in the status section", "FAIL");
			}		
			
		}
		
		if(strStatus == "NA1" || strStatus == "NA2" || strStatus == "NAW" )
		{
			browser.wait(getWaitTime());
			
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.YourAppointmentSection")).equalsIgnoreCase("Your appointment"))
			{
				Report.updateTestLog("Your Appointment Section Is Present", "PASS");
			}
			else
			{
				Report.updateTestLog("Your Appointment Section Is Not Present", "FAIL");
			}
			
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.BookedStatusScetion")).equalsIgnoreCase("Your appointment status"))
			{
				Report.updateTestLog("Your appointment status Section Is Present", "PASS");
			}
			else
			{
				Report.updateTestLog("Your appointment status Section Is Not Present", "FAIL");
			}
			if(browser.isElementVisibleWithXpath(pageProperties.getProperty("ASVIBAccountSummary.na1")))
			{
				Report.updateTestLog("Missed appointment status Section Is Present", "PASS");
			}
			else
			{
				Report.updateTestLog("Missed appointment status Section Is Not Present", "FAIL");
			}			

			verifyIsElementVisibleWithXpath(pageProperties.getProperty("ASVIBAccountSummary.CalendarImg"),"Calendar Image");
			verifyIsTextPresent(pageProperties.getProperty("ASVIBContactDetails.YourAppointmentText"));
			checkDateFormat();
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step1")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.comingtoday")))
			{
				Report.updateTestLog("Step 1 contains "+pageProperties.getProperty("ASVIBAccountSummary.booked"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 1 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.booked"), "FAIL");
			}
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step2")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.preparing")))
			{
				Report.updateTestLog("Step 2 contains "+pageProperties.getProperty("ASVIBAccountSummary.preparing"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 2 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.preparing"), "FAIL");
			}
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step3")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.Engineeronway")))
			{
				Report.updateTestLog("Step 3 contains "+pageProperties.getProperty("ASVIBAccountSummary.Engineeronway"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 3 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.Engineeronway"), "FAIL");
			}
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step4")).equalsIgnoreCase(pageProperties.getProperty("ASVIBConfirmation.Missed")))
			{
				Report.updateTestLog("Step 4 contains "+pageProperties.getProperty("ASVIBAccountSummary.alldone"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 4 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.alldone"), "FAIL");
			}
			
			
			
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.Change"));
			verifyIsTextPresent(pageProperties.getProperty("ASVIBConfirmation.BeBack"));
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.AppointmentBookedFor"));
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.Status")).contains("missed"))
			{
				Report.updateTestLog(pageProperties.getProperty("ASVIBConfirmation.Missed")+" status is Present in the status section", "PASS");
			}
			else
			{
				Report.updateTestLog(pageProperties.getProperty("ASVIBConfirmation.Missed")+" status is not Present in the status section", "FAIL");
			}		
			
		}
		
		if(strStatus == "ALLOC")
		{
			browser.wait(getWaitTime());
			
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.YourAppointmentSection")).equalsIgnoreCase("Your appointment"))
			{
				Report.updateTestLog("Your Appointment Section Is Present", "PASS");
			}
			else
			{
				Report.updateTestLog("Your Appointment Section Is Not Present", "FAIL");
			}
			
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.BookedStatusScetion")).equalsIgnoreCase("Your appointment status"))
			{
				Report.updateTestLog("Your appointment status Section Is Present", "PASS");
			}
			else
			{
				Report.updateTestLog("Your appointment status Section Is Not Present", "FAIL");
			}
			if(browser.isElementVisibleWithXpath(pageProperties.getProperty("ASVIBAccountSummary.Alloc")))
			{
				Report.updateTestLog("Alloc appointment status Section Is Present", "PASS");
			}
			else
			{
				Report.updateTestLog("Alloc appointment status Section Is Not Present", "FAIL");
			}			

			verifyIsElementVisibleWithXpath(pageProperties.getProperty("ASVIBAccountSummary.CalendarImg"),"Calendar Image");
			verifyIsTextPresent(pageProperties.getProperty("ASVIBContactDetails.YourAppointmentText"));
			checkDateFormat();
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step1")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.comingtoday")))
			{
				Report.updateTestLog("Step 1 contains "+pageProperties.getProperty("ASVIBAccountSummary.booked"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 1 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.booked"), "FAIL");
			}
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step2")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.preparing")))
			{
				Report.updateTestLog("Step 2 contains "+pageProperties.getProperty("ASVIBAccountSummary.preparing"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 2 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.preparing"), "FAIL");
			}
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step3")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.Engineeronway")))
			{
				Report.updateTestLog("Step 3 contains "+pageProperties.getProperty("ASVIBAccountSummary.Engineeronway"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 3 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.Engineeronway"), "FAIL");
			}
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.step4")).equalsIgnoreCase(pageProperties.getProperty("ASVIBAccountSummary.alldone")))
			{
				Report.updateTestLog("Step 4 contains "+pageProperties.getProperty("ASVIBAccountSummary.alldone"), "PASS");
			}
			else
			{
				Report.updateTestLog("Step 4 does not contain "+pageProperties.getProperty("ASVIBAccountSummary.alldone"), "FAIL");
			}
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.Cancel"));
			verifyIsTextPresent(pageProperties.getProperty("ASVIBConfirmation.ComingToday"));
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.Engineeronway"));
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.Change"));
			verifyIsTextPresent(pageProperties.getProperty("ASVIBConfirmation.BeBack"));
			verifyIsTextPresent(pageProperties.getProperty("ASVIBConfirmation.AllDone"));
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.AppointmentBookedFor"));
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.Status")).contains(pageProperties.getProperty("ASVIBConfirmation.BeBack")))
			{
				Report.updateTestLog(pageProperties.getProperty("ASVIBConfirmation.BeBack")+" status is Present in the status section", "PASS");
			}
			else
			{
				Report.updateTestLog(pageProperties.getProperty("ASVIBConfirmation.BeBack")+" status is not Present in the status section", "FAIL");
			}		
			
		}
		
		if(strStatus == "Booked")
		{
			browser.wait(getWaitTime());
			
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.YourAppointmentSection")).equalsIgnoreCase("Your appointment"))
			{
				Report.updateTestLog("Your Appointment Section Is Present", "PASS");
			}
			else
			{
				Report.updateTestLog("Your Appointment Section Is Not Present", "FAIL");
			}
			
			if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.BookedStatusScetion")).equalsIgnoreCase("Your appointment status"))
			{
				Report.updateTestLog("Your appointment status Section Is Present", "PASS");
			}
			else
			{
				Report.updateTestLog("Your appointment status Section Is Not Present", "FAIL");
			}
			
			verifyIsElementVisibleWithXpath(pageProperties.getProperty("ASVIBAccountSummary.CalendarImg"),"Calendar Image");
			verifyIsTextPresent(pageProperties.getProperty("ASVIBContactDetails.YourAppointmentText"));
			checkDateFormat();
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.Cancel"));
			
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.Change"));
			
			verifyIsTextPresent(pageProperties.getProperty("ASVIBAccountSummary.AppointmentBookedFor"));
				
			
		}
	}
	

	@SuppressWarnings("static-access")
	public void checkDateFormat()
{
	Calendar currentDate = Calendar.getInstance();
	SimpleDateFormat oDateFormat = new SimpleDateFormat("MMM yyyy");
	for (int i = 0; i <= 100; i++) {
		String oStrDate = "";
		if(i>0)
		{
		currentDate.add(Calendar.DATE, 1);
		}
		oStrDate = oDateFormat.format(currentDate.getTime());
		String dayName[] = new DateFormatSymbols().getWeekdays();
		String FinalDate = dayName[currentDate.get(currentDate.DAY_OF_WEEK)]
				.toString()
				+ " "
				+ currentDate.get(currentDate.DAY_OF_MONTH)
				+ " "
				+ oStrDate;
		
		/*String FinalDate = 	currentDate.get(currentDate.DAY_OF_MONTH)
		           				+ " "
		           				+ oStrDate;*/
		System.out.println("-----------------------"+FinalDate);
		if (browser.isTextPresent(FinalDate)) {
			Report.updateTestLog(
					"The date format is verified and the date " + FinalDate
							+ " is present", "PASS");
			break;
			
		} /*else {
			if (i == 100) {
				Report.updateTestLog(
						"The date format is verified and the correct date is not present",
						"FAIL");
			}
		}*/

	}

}

	public void clickChangeAppointment()
	{
		verifyIsTextPresent(pageProperties.getProperty("ASVIBBookAppointment.ChangeAppointment"), "Change Appointment link");
		verifyAndClickWithLinkText(pageProperties.getProperty("ASVIBBookAppointment.ChangeAppointment"), "Change Appointment");
	}
	
	public void clickChangeAppliance()
	{
		verifyIsTextPresent(pageProperties.getProperty("ASVIBBookAppointment.ChangeAppliance"), "Change Appointment link");
		verifyAndClickWithLinkText(pageProperties.getProperty("ASVIBBookAppointment.ChangeAppliance"), "Change Appliance");
	}
	
	public void checkCalendarPage()
	{
		if(
		verifyIsTextPresent(pageProperties
				.getProperty("ASVIBBookAppointment.SlotsAvailable")) ||
		verifyIsTextPresent(pageProperties
				.getProperty("ASVIBBookAppointment.DifferentAppointment")))
		{
			Report.updateTestLog("Calendar Page is loaded", "PASS");
		}
		else
		{
			Report.updateTestLog("Calendar Page is not loaded", "FAIL");
		}
		
	}

	public void checkFaultItemPage()
	{
		browser.wait(3000);

		if(verifyIsTextPresent(pageProperties
				.getProperty("ASVIBSelectAppliance.HeadText")) ||
		verifyIsTextPresent(pageProperties
				.getProperty("ASVIBSelectAppliance.SelectText")) ||
		verifyIsTextPresent(pageProperties
				.getProperty("ASVIBSelectAppliance.SelectSlot")) ||		
		verifyIsTextPresent(pageProperties
				.getProperty("ASVIBSelectAppliance.EmergencyText")) ||		
		verifyIsTextPresent(pageProperties
				.getProperty("ASVIBSelectAppliance.AwaitingSelection")))
		{
			Report.updateTestLog("Fault Appliance page is loaded", "PASS");
		}
		else
		{
			Report.updateTestLog("Fault Appliance Page is not loaded", "FAIL");
		}
	}

	public void checkBothAppointments(String strApp1, String strApp2)
	{
		verifyIsElementVisibleWithXpath(pageProperties.getProperty("ASVIBAccountSummary.YourAppSection1"), "section 1");
		verifyIsElementVisibleWithXpath(pageProperties.getProperty("ASVIBAccountSummary.YourAppSection2"), "section 2");
		if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.YourAppSection1")).contains(strApp1))
		{
			Report.updateTestLog("Section contains "+strApp1+" as first selected IB", "PASS");
		}
		else
		{
			Report.updateTestLog("Section does not contains "+strApp1+" as first selected IB", "FAIL");
		}
		
		if(browser.getTextByXpath(pageProperties.getProperty("ASVIBAccountSummary.YourAppSection2")).contains(strApp2))
		{
			Report.updateTestLog("Section contains "+strApp2+" as second selected IB", "PASS");
		}
		else
		{
			Report.updateTestLog("Section does not contains "+strApp2+" as second selected IB", "FAIL");
		}
	}
	
	public void navigateToAccountSummary2(UserProfile userProfile)
	{
		  verifyAndClickWithXpath((pageProperties.getProperty(
	                "AccountOverviewPage.ManageAccountXPath").replace("USERACCOUNTNUMBER",
	                		userProfile.getBgsAccount())), "Account summary");
	}

	public void verifyLinkNavigation(String strType)
	{
		browser.wait(6000);
		if (strType.equalsIgnoreCase("ASV")) {
			verifyIsTextPresent(
					pageProperties
							.getProperty("ASVIBAccountSummary.ViewAllAppointments"),
					"Book an appointment");
			verifyAndClickWithLinkText(
					pageProperties
							.getProperty("ASVIBAccountSummary.ViewAllAppointments"),
					"Book An Appointment");
			browser.wait(2000);
			if(
			verifyIsTextPresent(pageProperties
					.getProperty("ASVIBBookAppointment.SlotsAvailable")) ||
			verifyIsTextPresent(pageProperties
					.getProperty("ASVIBBookAppointment.DifferentAppointment")) == true)
			{
				Report.updateTestLog("Successfully navigated to the faulty calendar page", "PASS");
			}
			else
			{
				Report.updateTestLog("Did not navigate to the calendar page", "Fail");
		    }
			
		} else if (strType.equalsIgnoreCase("IB")) {
			verifyIsTextPresent(
					pageProperties.getProperty("ASVIBAccountSummary.BookAnEngineer"),
					"Book An Engineer");
			verifyAndClickWithLinkText(
					pageProperties.getProperty("ASVIBAccountSummary.BookAnEngineer"),
					"Book AN Engineer");
			browser.wait(2000);
			if(verifyIsTextPresent(pageProperties.getProperty("ASVIBSelectAppliance.HeadText")) &&
					verifyIsTextPresent(pageProperties.getProperty("ASVIBSelectAppliance.SelectText")) &&
							verifyIsTextPresent(pageProperties.getProperty("ASVIBSelectAppliance.SelectSlot")) == true)
					{
							Report.updateTestLog("Successfully navigated to the faulty appliance page", "PASS");
					}
					else
					{
						Report.updateTestLog("Did not navigate to the faulty appliance page", "Fail");
				    }
		}
		else if (strType.equalsIgnoreCase("AccOverviewASV"))
		{
			verifyIsTextPresent(pageProperties
					.getProperty("ASVIBAccountSummary.BookAnAppointment"));
			verifyIsTextPresent(pageProperties
					.getProperty("ASVIBAccountOverviewPage.BookAnEngineer"));
			verifyAndClickWithLinkText(
					pageProperties
							.getProperty("ASVIBAccountSummary.BookAnAppointment"),"Book An Appointment");
			browser.wait(2000);
			if(
			verifyIsTextPresent(pageProperties
					.getProperty("ASVIBBookAppointment.SlotsAvailable")) &&
			verifyIsTextPresent(pageProperties
					.getProperty("ASVIBBookAppointment.DifferentAppointment")) == true)
			{
				Report.updateTestLog("Successfully navigated to the faulty calendar page", "PASS");
			}
			else
			{
				Report.updateTestLog("Did not navigate to the faulty calendar page", "Fail");
		    }
		}
		else if (strType.equalsIgnoreCase("AccOverviewIB"))
		{
			verifyIsTextPresent(pageProperties
					.getProperty("ASVIBAccountSummary.BookAnAppointment"));
			verifyIsTextPresent(pageProperties
					.getProperty("ASVIBAccountOverviewPage.BookAnEngineer"));
			verifyAndClickWithLinkText(
					pageProperties
							.getProperty("ASVIBAccountSummary.BookAnEngineer"),"Book An Appointment");
			browser.wait(2000);
			if(verifyIsTextPresent(pageProperties.getProperty("ASVIBSelectAppliance.HeadText")) &&
			verifyIsTextPresent(pageProperties.getProperty("ASVIBSelectAppliance.SelectText")) &&
					verifyIsTextPresent(pageProperties.getProperty("ASVIBSelectAppliance.SelectSlot")) == true)
			{
					Report.updateTestLog("Successfully navigated to the faulty appliance page", "PASS");
			}
			else
			{
				Report.updateTestLog("Did not navigate to the faulty appliance page", "Fail");
		    }
		}
		browser.wait(2000);
	}
	
	public void navigateBack()
	{
		browser.browserBack();
		browser.wait(2000);
	}

	public void selectFirstAvailableSlot()
	{
		verifyAndClickWithXpath(pageProperties.getProperty("ASVIBBookAppointment.BookThisAppointment"), "Book this appointment");
	}
	
	public void verifyEmailConfirmationStatus(UserProfile userProfile)
	{		
		String strRetVal = new OnlineDBConnector().verifyASVEmailConfirmationStatus(userProfile.getAccNumber().substring(8, 15));
		if(strRetVal.equalsIgnoreCase("Sent"))
		{
			Report.updateTestLog("The status is present as Sent", "PASS");
		}
		else
		{
			Report.updateTestLog("The status is present as "+strRetVal, "FAIL");
		}
		
	}
	public void verifyEmailConfirmation(UserProfile userProfile,String strTransactionType)
	{		
		String[] strRetVal = new OnlineDBConnector().verifyASVEmailConfirmation(userProfile.getAccNumber().substring(8, 15));
		
		if(strRetVal.length <1)
		{
			Report.updateTestLog("The Audit data in the ASV_TA_AUDIT_DETAILS table is not available", "FAIL");
		}
		else
		{
			for(int i=0;i<6;i++)
			{
				if(i == 0)
				{
					Report.updateTestLog("The Audit ID in the ASV_TA_AUDIT_DETAILS table is : <b>"+strRetVal[i].toString(), "PASS");
				}
				if(i == 1)
				{
					if(strRetVal[i].toString().equalsIgnoreCase("Customer"))
					{
					Report.updateTestLog("The User Type in the ASV_TA_AUDIT_DETAILS table is : <b>"+strRetVal[i].toString(), "PASS");
					}
					else
					{
						Report.updateTestLog("The User Type in the ASV_TA_AUDIT_DETAILS table is : <b>"+strRetVal[i].toString()+"Customer", "Fail");
					}
				}
				if(i == 2)
				{
					if(strRetVal[i].toString().equalsIgnoreCase(strTransactionType))
					{
					Report.updateTestLog("The Transaction Type in the ASV_TA_AUDIT_DETAILS table is : <b>"+strRetVal[i].toString(), "PASS");
					}
					else
					{
						Report.updateTestLog("The Transaction Type in the ASV_TA_AUDIT_DETAILS table is : <b>"+strRetVal[i].toString()+" Insead of "+strTransactionType, "PASS");
					}
				}
				
				if(i == 3)
				{
					if(!strRetVal[i].toString().isEmpty())
					{
					Report.updateTestLog("The Audit Time Stamp in the ASV_TA_AUDIT_DETAILS table is : <b>"+strRetVal[i].toString(), "PASS");
					}
					else
					{
						Report.updateTestLog("The Audit Time Stamp in the ASV_TA_AUDIT_DETAILS table is : ", "FAIL");
					}
				}
				
				if(i == 4)
				{
					if(!strRetVal[i].toString().isEmpty())
					{
					Report.updateTestLog("The AUDIT_DETAILS in the ASV_TA_AUDIT_DETAILS table is : <b>"+strRetVal[i].toString(), "PASS");
					}
					else
					{
						Report.updateTestLog("The AUDIT_DETAILS in the ASV_TA_AUDIT_DETAILS table is : ", "FAIL");
					}
				}
				if(i == 5)
				{
					if(!strRetVal[i].toString().isEmpty())
					{
					Report.updateTestLog("The PREMISE_NUMBER in the ASV_TA_AUDIT_DETAILS table is : <b>"+strRetVal[i].toString(), "PASS");
					}
					else
					{
						Report.updateTestLog("The PREMISE_NUMBER in the ASV_TA_AUDIT_DETAILS table is : ", "FAIL");
					}
				}
			}
		}		
		
	}

	public void testHornors()
	{
		//String strParent = browser.getWindowTitle();
		//browser.openTab("http://ss000075/cgi-bin/hestia/WMSHTH01.cgi");
		//String strChild = browser.getWindowTitle();
		//browser.switchWindowToPrev(strChild);
		browser.wait(5000);
verifyAndInputByXpath("//*[@name='user']","UserName","wmis");	
verifyAndInputByXpath("//*[@name='pw']","Password","wmis");
verifyAndInputByXpath("//*[@name='db']","DataBase Name","IJPWMS05");

//		*[@value="Login"]

//		*[@name="topic"]	STATUS_UPDATE_WR


//		*[@name="DOMAIN"]
//		*[@name="AREA"]	2  (Patch code)
//		*[@name="WR"]
//		*[@name="NEWSTATUS"]	ALLOC
//		*[@value="Now"]
//		*[@value="Generate"]
//browser.switchWindowToPrev(strParent);
//browser.switchWindowToPrev("Book an engineer to fix a fault");
	}

	public void clickCancel()
	{
		browser.wait(1000);
		verifyIsTextPresent("Cancel", "Cancel link");
		verifyAndClickWithLinkText("Cancel", "Cancel link");
	}
	public void clickBack()
	{
		verifyIsTextPresent("Back", "Back link");
		verifyAndClickWithLinkText("Back", "Cancel link");
	}


}
