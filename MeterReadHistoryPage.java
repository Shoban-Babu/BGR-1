package bg.framework.app.functional.page.reFactoring;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import bg.framework.app.functional.common.ApplicationConfig;
import bg.framework.app.functional.entities.UserProfile;
import bg.framework.app.functional.page.common.BasePage;
import bg.framework.app.functional.util.PropertyLoader;
import bg.framework.app.functional.util.Report;
import bg.framework.app.functional.util.RunQTP;
import bg.framework.app.functional.util.SiebelDataBase;
import bg.framework.app.functional.entities.UserProfile;
import org.openqa.selenium.support.ui.Select;


public class MeterReadHistoryPage extends BasePage {
	private final static String File_AccPage = "resources/ReFactoring/MeterReadHistory.properties";
	private final static String File_AccPage1 = "resources/selfServe/BGAccountOverview.properties";
	private static Properties pageProperties = null; 
	private static Properties AccPageProperties = null;
	{
		pageProperties = new PropertyLoader(File_AccPage).load();
		AccPageProperties = new PropertyLoader(File_AccPage1).load();
	}
	public MeterReadHistoryPage()
	{
	}

	public void navigateToMeterReadHistory() {
		verifyAndClickWithXpath(pageProperties.getProperty("MeterReadHistory.EnergyUsagemain"), "Energy Usage");
		verifyAndClickWithXpath(pageProperties.getProperty("MeterReadHistory.MeterReadHistorymain"), "Meter Read History");
	}
	public void verifylinks() {
		verifyPageTitle("Meter read history");
		if(ApplicationConfig.BRAND.equalsIgnoreCase("BG")){
			ArrayList<String> link = new ArrayList<String>();
			link.add("MeterReadHistory.Payments");
			link.add("MeterReadHistory.Billing");
			link.add("MeterReadHistory.EnergyUsage");
			link.add("MeterReadHistory.MeterReadHistory");
			link.add("MeterReadHistory.Compareyourusage");
			link.add("MeterReadHistory.Predictnextbill");
			link.add("MeterReadHistory.midata");
			String s = link.toString();
			System.out.println(s);
			for (String i : link) {
				System.out.println(i);
				verifyLink(i);
			}
		}
	}
	private void verifyLink(String link) {
		String linkName = link.substring(link.indexOf('.') + 1, link.length());
		if (browser.isElementVisibleWithXpath(pageProperties.getProperty(link))) {
			Report.updateTestLog(linkName + " Link Exist", "PASS");
		} else {
			Report.updateTestLog(linkName + " Link not Exist", "FAIL");
		}
	}
	public void logout() {
		if (browser.isElementVisibleWithXpath(pageProperties.getProperty("MeterReadHistory.Logout"))){
			Report.updateTestLog("Meter Read History Page", "WARN");
			browser.clickWithXpath(pageProperties.getProperty("MeterReadHistory.Logout"));
			System.out.println("Selected logout");

		}
	}
	public void verifyMeterReadTable(String accountNumber, String fuel) {
		System.out.println("RUN QTP");
		RunQTP runQTP = new RunQTP();

		runQTP.runQTP("BGRegression\\src\\bg\\framework\\app\\functional\\util\\vbsScripts\\MeterRead.vbs", accountNumber);

		browser.wait(15000);
		String date =null;
		String browserDate=null, elecdate, elecmeter, gasdate, gasmeter, energy;
		String meterread = null;
		String browserMR1=null, browserMR=null;
		String[] ElecDate, ElecMeter, GasDate, GasMeter;
		if(fuel.equals("Electricity"))
		{				   			    
			try {
				File file = new File("C:\\SAPData\\meterread1.txt");
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				br.readLine();
				br.readLine();
				br.readLine();
				energy=br.readLine();
				if(energy.equals("Electricity"))
				{
					Report.updateTestLog("Energy Type - Electricity", "PASS");
					elecdate=br.readLine();
					elecmeter=br.readLine();
					ElecDate =elecdate.split(";");
					ElecMeter=elecmeter.split(";");
					for(int i=1;i< ElecDate.length;i++)
					{
						browserDate= (browser.getTextByXpath(pageProperties.getProperty("MeterReadHistory.Date").replace("NUM",""+i)));
						browserMR1=(browser.getTextByXpath(pageProperties.getProperty("MeterReadHistory.MR").replace("NUM",""+i)));

						for(int a=0;a<browserMR1.length();a++)
						{
							if(!(browserMR1.charAt(a)=='0'))
							{
								browserMR=browserMR1.substring(a,browserMR1.length());
								break; 
							}
						}
						if(browserMR1.equals("00000"))
							browserMR="0";
						if(ElecDate[i].equals(browserDate) && ElecMeter[i].equals(browserMR))
						{
							Report.updateTestLog("Date  "+ ElecDate[i] + " Meter Read  " + ElecMeter[i]+ " are displayed correctly", "PASS");
						}
						else
						{
							Report.updateTestLog("Date  "+ ElecDate[i] + " Meter Read  " + ElecMeter[i] + " are not displayed correctly", "FAIL");
						}

					}
				}			    
				br.close();
			}
			catch (IOException e) {
				System.out.println("bad"+e);
			}
		}
		if(fuel.equals("Gas"))
		{		
			try {
				File file = new File("C:\\SAPData\\meterread1.txt");
				System.out.println("reached the File");
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				br.readLine();
				br.readLine();
				br.readLine();
				energy=br.readLine();
				if(energy.equals("Gas"))
				{
					Report.updateTestLog("Energy Type - GAS", "PASS");
					gasdate=br.readLine();
					gasmeter=br.readLine();
					GasDate =gasdate.split(";");
					GasMeter=gasmeter.split(";");
					for(int i=1;i<GasDate.length;i++)
					{
						browser.wait(3000);
						browserDate= (browser.getTextByXpath(pageProperties.getProperty("MeterReadHistory.Date").replace("NUM",""+i)));
						browserMR1=(browser.getTextByXpath(pageProperties.getProperty("MeterReadHistory.MR").replace("NUM",""+i)));

						for(int a=0;a<browserMR1.length();a++)
						{
							if(!(browserMR1.charAt(a)=='0'))
							{
								browserMR=browserMR1.substring(a,browserMR1.length());
								break; 
							}
						}
						if(browserMR1.equals("00000"))
							browserMR="0";
						if(GasDate[i].equals(browserDate) && GasMeter[i].equals(browserMR))
						{
							Report.updateTestLog("Date  "+ GasDate[i] + " Meter Read  " + GasMeter[i]+ " are displayed correctly", "PASS");
						}
						else
						{
							Report.updateTestLog("Date  "+ GasDate[i] + " Meter Read  " + GasMeter[i] + " are not displayed correctly", "FAIL");
						}

					}
				}			    
				br.close();
			}
			catch (IOException e) {
				System.out.println("bad"+e);
			}
		}
		if(fuel.equals("JI"))
		{				   			    
			try {
				File file = new File("C:\\SAPData\\meterread1.txt");
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				br.readLine();
				br.readLine();
				br.readLine();
				energy=br.readLine();
				if(energy.equals("Gas"))
				{
					gasdate=br.readLine();
					gasmeter=br.readLine();
					GasDate =gasdate.split(";");
					GasMeter=gasmeter.split(";");
					Report.updateTestLog("Energy Type - GAS", "PASS");
					for(int i=1;i< GasDate.length;i++)
					{
						browserDate= (browser.getTextByXpath(pageProperties.getProperty("MeterReadHistory.Date").replace("NUM",""+i)));
						browserMR1=(browser.getTextByXpath(pageProperties.getProperty("MeterReadHistory.MR").replace("NUM",""+i)));

						for(int a=0;a<browserMR1.length();a++){
							if(!(browserMR1.charAt(a)=='0')){
								browserMR=browserMR1.substring(a,browserMR1.length());
								break; }
						}
						if(browserMR1.equals("00000"))
							browserMR="0";
						if(GasDate[i].equals(browserDate) && GasMeter[i].equals(browserMR))
						{
							Report.updateTestLog("Date  "+ GasDate[i] + " Meter Read  " + GasMeter[i]+ " are displayed correctly", "PASS");
						}
						else
						{
							Report.updateTestLog("Date  "+ GasDate[i] + " Meter Read  " + GasMeter[i] + " are not displayed correctly", "FAIL");
						}

					}
				}
				br.readLine();
				Select select = new Select(browser.getElementByXpath(pageProperties.getProperty("MeterReadHistory.drop")));
				select.selectByIndex(1);
				browser.wait(3000);
				energy=br.readLine();
				if(energy.equals("Electricity"))
				{
					elecdate=br.readLine();
					elecmeter=br.readLine();
					ElecDate =elecdate.split(";");
					ElecMeter=elecmeter.split(";");
					Report.updateTestLog("Energy Type - ELECTRICITY", "PASS");
					for(int i=1;i< ElecDate.length;i++)
					{
						browserDate= (browser.getTextByXpath(pageProperties.getProperty("MeterReadHistory.Date").replace("NUM",""+i)));
						browserMR1=(browser.getTextByXpath(pageProperties.getProperty("MeterReadHistory.MR").replace("NUM",""+i)));

						for(int a=0;a<browserMR1.length();a++){
							if(!(browserMR1.charAt(a)=='0')){
								browserMR=browserMR1.substring(a,browserMR1.length());
								break; }
						}
						if(browserMR1.equals("00000"))
							browserMR="0";
						if(ElecDate[i].equals(browserDate) && ElecMeter[i].equals(browserMR))
						{
							Report.updateTestLog("Date  "+ ElecDate[i] + " Meter Read  " + ElecMeter[i]+ " are displayed correctly", "PASS");
						}
						else
						{
							Report.updateTestLog("Date  "+ ElecDate[i] + " Meter Read  " + ElecMeter[i] + " are not displayed correctly", "FAIL");
						}

					}
				}
				br.close();
			}
			catch (IOException e) {
				System.out.println("bad"+e);
			}
		}

	}

	public void navigatetoElec() {
		Select select = new Select(browser.getElementByXpath(pageProperties.getProperty("MeterReadHistory.Dropdown")));
		select.selectByIndex(1);
		verifyAndClickWithXpath(pageProperties.getProperty("MeterReadHistory.Go"), "Go");		
	}
}

	
