package bg.framework.app.functional.page.reFactoring;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import bg.framework.app.functional.entities.PriceFinder;
//import bg.framework.app.functional.entities.UnitRate;
import bg.framework.app.functional.entities.UserProfile;
import bg.framework.app.functional.page.common.BasePage;
import bg.framework.app.functional.util.PropertyLoader;
import bg.framework.app.functional.util.Report;
import bg.framework.app.functional.util.RobotSendKeys;
import bg.framework.app.functional.util.TestDataHelper;


public class UnitRatesPage extends BasePage{
	private final static String File_Name= "resources/ReFactoring//UnitRates.properties";
	private static Properties pageProperties = new PropertyLoader(File_Name).load();


	public void verifyUnitRates(UserProfile userProfile, String fuel) {
		String Gas[]={"Clear & Simple", "Smarter Enery December 2014", "Smarter Energy July 2014", "Standard"};
		String ElecSR[]={"Clear & Simple", "Standard"};
		String Elec2R[]={"Clear & Simple", "Standard"};
		String ElecTOU[]={"Day Time Discount" , "Evening & Weekend"};
		String gasmeter = null, elecmeter= null, paymentType = null, TariffElec = null, TariffGas = null, TariffElec1=null, TariffGas1=null;
		String Region=null, AccNum=null ;
		if(fuel=="Gasdual"){
			AccNum=browser.getTextByXpath(pageProperties.getProperty("UnitRates.AccNumgas")); 
		}
		else if(fuel=="Electricitydual"){
			AccNum=browser.getTextByXpath(pageProperties.getProperty("UnitRates.AccNumelec")); 
		}
		else
		{
			AccNum=browser.getTextByXpath(pageProperties.getProperty("UnitRates.AccNum")); 
		}

		if(browser.isElementVisibleWithXpath(pageProperties.getProperty("UnitRates.ELECtariff"))){
			TariffElec= browser.getTextByXpath(pageProperties.getProperty("UnitRates.ELECtariff")); 
			TariffElec=TariffElec.substring(12);
		}
		if(browser.isElementVisibleWithXpath(pageProperties.getProperty("UnitRates.GAStariff"))){
			TariffGas= browser.getTextByXpath(pageProperties.getProperty("UnitRates.GAStariff")); 
			TariffGas=TariffGas.substring(4);
		}
		String PostcodeinScreen = browser.getTextByXpath((pageProperties.getProperty("UnitRates.Postcode")).replace("NUM", AccNum));
		String[] PostCode=PostcodeinScreen.split(",");
		String PostCodeValue=PostCode[(PostCode.length)-1];
		Region = getPostcode(PostCodeValue);
		int reg=1;
		String PaymentinScreen=browser.getTextByXpath(pageProperties.getProperty("UnitRates.PaymentType")); 
		if(PaymentinScreen.contains("Credit/Debit card")){
			paymentType="QCC";
		}
		if(PaymentinScreen.contains("Direct Debit")){
			paymentType="MDD";
		}
		if(PaymentinScreen.contains("Pre Payment")){
			paymentType="PPM";
		}

		if(browser.isElementVisibleWithXpath(pageProperties.getProperty("UnitRates.GAStariff")))
		{
			if(Arrays.asList(Gas).contains(TariffGas)){
				gasmeter="Gas";
			}
		}
		if(browser.isElementVisibleWithXpath(pageProperties.getProperty("UnitRates.ELECtariff")))
		{
			if(Arrays.asList(ElecSR).contains(TariffElec)){
				elecmeter="Elec SR";
			}
			else if(Arrays.asList(Elec2R).contains(TariffElec)){
				elecmeter="Elec 2R";
			}
			else if(Arrays.asList(ElecTOU).contains(TariffElec)){
				elecmeter="Elec ToU";
			}
		}
		if(gasmeter == ("Gas"))
		{		     
			navigateToSeeUnitLink("Gas");
			browser.wait(2000);
			TariffGas1 = browser.getTextByXpath(pageProperties.getProperty("UnitRates.Tariff")); 
			TariffGas1 = TariffGas.replace(" Gas Notes", "");
			String payment = TariffGas1 + gasmeter + reg + paymentType + Region;
			PriceFinder priceFinder2 = new TestDataHelper().getPFdata(payment);
			Report.updateTestLog("Unit Rates: Tariff : " + TariffGas + ", " +
					"Meter Type :" + gasmeter + ", Reg : " + reg+ ", Payment Type :" + paymentType + ", Region :" + Region, "PASS");

			if (browser.isElementVisibleWithXpath(pageProperties.getProperty("UnitRates.GasStandingChargesElement"))) {
				String SC = priceFinder2.getSC();
				String SCAct = ceilDouble(SC, "0.00");
				String SC1 = browser.getTextByXpath(pageProperties.getProperty("UnitRates.GasStandingCharges"));
				String SCDisp = ceilDouble(SC1, "0.00");

				if (SCDisp.equalsIgnoreCase(SCAct)) {
					Report.updateTestLog("Standing Charge Verification Done :" + SCAct, "PASS");
				} else {
					Report.updateTestLog("Standing Charge Verification actual :" + SCAct + ", displayed :" + SCDisp, "FAIL");
				}
			}
			if (browser.isElementVisibleWithXpath(pageProperties.getProperty("UnitRates.GasUnitRatesElement"))) {
				String t1Act = priceFinder2.getT1();
				String tier1Act = ceilDouble(t1Act, "0.00");
				String t1Disp = browser.getTextByXpath(pageProperties.getProperty("UnitRates.GasUnitRates"));
				String tier1Disp = ceilDouble(t1Disp, "0.00");

				if (tier1Disp.equalsIgnoreCase(tier1Act)) {
					Report.updateTestLog("Unit Rate Verification Done :" + tier1Act, "PASS");
				} else {
					Report.updateTestLog("Unit Rate Verification actual :" + tier1Act + ", displayed :" + tier1Disp, "FAIL");
				}
			}
			if (browser.isElementVisibleWithXpath(pageProperties.getProperty("UnitRates.GasTier1Element"))) {
				String t1Act = priceFinder2.getT1();
				String tier1Act = ceilDouble(t1Act, "0.00");
				String t1Disp = browser.getTextByXpath(pageProperties.getProperty("UnitRates.GasTier1"));
				String tier1Disp = ceilDouble(t1Disp, "0.00");

				if (tier1Disp.equalsIgnoreCase(tier1Act)) {
					Report.updateTestLog("Tier 1 Verification Done :" + tier1Act, "PASS");
				} else {
					Report.updateTestLog("Tier 1 Verification actual :" + tier1Act + ", displayed :" + tier1Disp, "FAIL");
				}
			}
			if (browser.isElementVisibleWithXpath(pageProperties.getProperty("UnitRates.GasTier2Element"))) {
				String t2Act = priceFinder2.getT2();
				String tier2Act = ceilDouble(t2Act, "0.00");
				String t2Disp = browser.getTextByXpath(pageProperties.getProperty("UnitRates.GasTier2"));
				String tier2Disp = ceilDouble(t2Disp, "0.00");
				if (tier2Disp.equalsIgnoreCase(tier2Act)) {
					Report.updateTestLog("Tier 2 Verification Done :" + tier2Act, "PASS");
				} else {
					Report.updateTestLog("Tier 2 Verification actual :" + tier2Act + ", displayed :" + tier2Disp, "FAIL");
				}
			} 
			browser.wait(2000);
			browser.clickWithXpath(pageProperties.getProperty("UnitRates.close"));
			browser.wait(2000);        
		}

		if(elecmeter =="Elec SR")
		{		        
			navigateToSeeUnitLink("Electricity");
			browser.wait(2000);
			TariffElec1 = browser.getTextByXpath(pageProperties.getProperty("UnitRates.Tariffelec")); 
			TariffElec1 = TariffElec1.replace(" Electricity Notes", "");
			String payment = TariffElec1 + elecmeter + reg + paymentType + Region;
			PriceFinder priceFinder2 = new TestDataHelper().getPFdata(payment);
			Report.updateTestLog("Unit Rates: Tariff : " + TariffElec + ", " +
					"Meter Type :" + elecmeter + ", Reg : " + reg+ ", Payment Type :" + paymentType + ", Region :" + Region, "PASS");

			if (browser.isElementVisibleWithXpath(pageProperties.getProperty("UnitRates.ElecStandingChargesElement"))) {
				String SC = priceFinder2.getSC();
				String SCAct = ceilDouble(SC, "0.00");
				String SC1 = browser.getTextByXpath(pageProperties.getProperty("UnitRates.ElecStandingCharges"));
				String SCDisp = ceilDouble(SC1, "0.00");

				if (SCDisp.equalsIgnoreCase(SCAct)) {
					Report.updateTestLog("Standing Charge Verification Done :" + SCAct, "PASS");
				} else {
					Report.updateTestLog("Standing Charge Verification actual :" + SCAct + ", displayed :" + SCDisp, "FAIL");
				}
			}
			if (browser.isElementVisibleWithXpath(pageProperties.getProperty("UnitRates.ElecUnitRatesElement"))) {
				String t1Act = priceFinder2.getT1();
				String tier1Act = ceilDouble(t1Act, "0.00");
				String t1Disp = browser.getTextByXpath(pageProperties.getProperty("UnitRates.ElecUnitRates"));
				String tier1Disp = ceilDouble(t1Disp, "0.00");

				if (tier1Disp.equalsIgnoreCase(tier1Act)) {
					Report.updateTestLog("Unit Rate Verification Done :" + tier1Act, "PASS");
				} else {
					Report.updateTestLog("Unit Rate Verification actual :" + tier1Act + ", displayed :" + tier1Disp, "FAIL");
				}
			}
			if (browser.isElementVisibleWithXpath(pageProperties.getProperty("UnitRates.ElecTier1Element"))) {
				String t1Act = priceFinder2.getT1();
				String tier1Act = ceilDouble(t1Act, "0.00");
				String t1Disp = browser.getTextByXpath(pageProperties.getProperty("UnitRates.ElecTier1"));
				String tier1Disp = ceilDouble(t1Disp, "0.00");

				if (tier1Disp.equalsIgnoreCase(tier1Act)) {
					Report.updateTestLog("Tier 1 Verification Done :" + tier1Act, "PASS");
				} else {
					Report.updateTestLog("Tier 1 Verification actual :" + tier1Act + ", displayed :" + tier1Disp, "FAIL");
				}
			}
			if (browser.isElementVisibleWithXpath(pageProperties.getProperty("UnitRates.ElecTier2Element"))) {
				String t2Act = priceFinder2.getT2();
				String tier2Act = ceilDouble(t2Act, "0.00");
				String t2Disp = browser.getTextByXpath(pageProperties.getProperty("UnitRates.ElecTier2"));
				String tier2Disp = ceilDouble(t2Disp, "0.00");
				if (tier2Disp.equalsIgnoreCase(tier2Act)) {
					Report.updateTestLog("Tier 2 Verification Done :" + tier2Act, "PASS");
				} else {
					Report.updateTestLog("Tier 2 Verification actual :" + tier2Act + ", displayed :" + tier2Disp, "FAIL");
				}
			} 
			browser.wait(2000);
			browser.clickWithXpath(pageProperties.getProperty("UnitRates.close1"));
			browser.wait(2000);    
		}
		if((elecmeter == ("Elec 2R"))|| (elecmeter ==("Elec ToU")))
		{		        
			navigateToSeeUnitLink("Electricity");
			browser.wait(3000);
			TariffElec1 = browser.getTextByXpath(pageProperties.getProperty("UnitRates.Tariffelec")); 
			TariffElec1 = TariffElec1.replace(" Electricity Notes", "");

			for(int i=reg; i<=3; i++)
			{
				String payment = TariffElec1 + elecmeter + i + paymentType + Region;
				PriceFinder priceFinder2 = new TestDataHelper().getPFdata(payment);
				if(priceFinder2==(null)){
					continue; }
				else
				{
					Report.updateTestLog("Unit Rates: Tariff : " + TariffElec1 + ", " +
							"Meter Type :" + elecmeter + ", Reg : " + i + ", Payment Type :" + paymentType + ", Region :" + Region, "PASS");
					if (browser.isElementVisibleWithXpath(pageProperties.getProperty("UnitRates.ElecStandingChargesElement"))) {
						String SC = priceFinder2.getSC();
						String SCAct = ceilDouble(SC, "0.00");
						String SC1 = browser.getTextByXpath(pageProperties.getProperty("UnitRates.ElecStandingCharges"));
						String SCDisp = ceilDouble(SC1, "0.00");
						if (SCDisp.equalsIgnoreCase(SCAct)) {
							Report.updateTestLog("Standing Charge Verification Done :" + SCAct, "PASS");
						} else {
							Report.updateTestLog("Standing Charge Verification actual :" + SCAct + ", displayed :" + SCDisp, "FAIL");
						}
						String t1Act = priceFinder2.getT1();
						String tier1Act = ceilDouble(t1Act, "0.00");
						String t1Disp = browser.getTextByXpath(pageProperties.getProperty("UnitRates.ElecReg"+i+"Tier1"));
						String tier1Disp = ceilDouble(t1Disp, "0.00");
						if (tier1Disp.equalsIgnoreCase(tier1Act)) {
							Report.updateTestLog("Reg "+ i + " Verification Done :" + tier1Act, "PASS");
						} else {
							Report.updateTestLog("Reg "+ i + " Verification actual :" + tier1Act + ", displayed :" + tier1Disp, "FAIL");
						}
					}
				}
			}
			browser.wait(2000);
			browser.clickWithXpath(pageProperties.getProperty("UnitRates.close1"));
			browser.wait(2000);    
		}

	}

	public String getPostcode(String region1) {
		String Region = null;
		if(region1.contains("AL10 8UW"))/*AL7 4HD*/{
			Region = "Eastern";
		}
		else if(region1.contains("LE12 9LX")) {
			Region ="East Midlands";
		}
		else if(region1.contains("BR1 7YJ")) {
			Region = "London";
		}
		else if(region1.contains("CH4 7UH")) {
			Region = "ManWeb";
		}
		else if(region1.contains("B10 9JU")) {
			Region = "Midlands";
		}         
		else if(region1.contains("DH2 7GH")) {
			Region = "Northern";
		}  
		else if(region1.contains("BB2 7DF")) {
			Region = "Norweb";
		}  
		else if(region1.contains("AB14 7GH")) {
			Region = "ScotHydro";
		}  
		else if(region1.contains("DG10 7FG")) {
			Region = "ScotPower";
		}         
		else if(region1.contains("KT17 3BH"))/*BN10 7PR*/ {
			Region = "Seeboard";
		}  
		else if(region1.contains("BH7 6VC")) {
			Region = "Southern";
		}   
		else if(region1.contains("CF10 6FG")) {
			Region = "Swalec";
		}   
		else if(region1.contains("BA3 7GH")) {
			Region = "Sweb";
		}  
		else if(region1.contains("BD10 5RT")) {
			Region = "Yorkshire";
		}  
		return Region;
	}

	public void logout() {
		if (browser.isElementVisibleWithXpath(pageProperties.getProperty("UnitRates.Logout"))){
			Report.updateTestLog("Account Summary Page", "WARN");
			browser.clickWithXpath(pageProperties.getProperty("UnitRates.Logout"));
		}
	}

	public void navigateToSeeUnitLink(String Energy) {
		if(Energy.equals("Gas"))
		{
			if (browser.isElementVisibleWithXpath(pageProperties.getProperty("UnitRates.SeeunitratesGas"))){
				browser.clickWithXpath(pageProperties.getProperty("UnitRates.SeeunitratesGas"));
				Report.updateTestLog("Navigated to Unit Rates Page - Gas ", "PASS");
			}
		}

		if(Energy.equals("Electricity"))
		{
			if (browser.isElementVisibleWithXpath(pageProperties.getProperty("UnitRates.SeeunitratesElec"))){
				browser.clickWithXpath(pageProperties.getProperty("UnitRates.SeeunitratesElec"));
				Report.updateTestLog("Navigated to Unit Rates Page - Electricity ", "PASS");
			}
		}
	}
	public void navigatetoAccountoverview() {
		browser.wait(2000);
		browser.clickWithXpath(pageProperties.getProperty("UnitRates.AccountOverview"));
		browser.wait(2000);
	}

}




