package bg.framework.app.functional.page.reFactoring;

import java.util.Properties;


import java.util.Random;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import bg.framework.app.functional.page.common.BasePage;
import bg.framework.app.functional.page.selfServe.PersonalDetailsPage;
import bg.framework.app.functional.util.OnlineDBConnector;
import bg.framework.app.functional.util.PropertyLoader;
import bg.framework.app.functional.util.Report;
import bg.framework.app.functional.action.sales.AcquisitionAction;
import bg.framework.app.functional.common.ApplicationConfig;
import bg.framework.app.functional.entities.Acquisition;
import bg.framework.app.functional.entities.UserProfile;

public class EshopPage extends BasePage {
	
	private final static String FILE_NAME = "resources/reFactoring/EshopPage.properties";
	private static Properties pageProperties = new PropertyLoader(FILE_NAME).load();
	private static String orderDate;
	private static String Fuel;
	private static String SalesType;
	private static String CustomerType;
	private static String LeadData;
    private static boolean MarketingConsentFlag;
    UserProfile userProfile;

	
	public EshopPage(String Fuel){
		this.Fuel = Fuel; 
	}
	
	public EshopPage(String Fuel,String SalesType,String CustomerType){
		this.SalesType = SalesType;
		this.Fuel = Fuel;
		this.CustomerType = CustomerType;
	}
	
	public EshopPage(){
		
	}
	
	public void navigateToGasandElectricityPage(){
		browser.wait(3000);
		//verifyAndClickWithLinkText(pageProperties.getProperty("EshopPage.GasElectricity"), "Gas and electricity");
		browser.open(ApplicationConfig.APP_BG_URL+"/products-and-services/gas-and-electricity.html");
	}
	
	public void navigateToOurTariffsPage(){
		browser.wait(5000);
		verifyAndClickWithLinkText(pageProperties.getProperty("EshopPage.OurTariffs"), "Our tariffs");
		//verifyAndClickWithXpath(pageProperties.getProperty("EshopPage.OurTariffsXpath"), "Our Tariffs");
	}
	
	public void selectTariff(String Tariff){
		browser.wait(3000);
		verifyAndClickWithLinkText(pageProperties.getProperty("EshopPage."+Tariff), Tariff+" ");
	}
	
	public void selectFuel(String Fuel){
		browser.wait(2000);
		//Selecting for Esmart
		if(SalesType.equalsIgnoreCase("Esmart")){
			if(browser.isElementVisibleWithXpath(pageProperties.getProperty("EshopPage.EsmartAddon"+Fuel))){
				verifyAndClickWithXpath(pageProperties.getProperty("EshopPage.EsmartAddon"+Fuel), Fuel+" Addon Selected");	
			}
			else if(browser.isElementVisibleWithXpath(pageProperties.getProperty("EshopPage.EsmartAddon"+Fuel+"1"))){
				verifyAndClickWithXpath(pageProperties.getProperty("EshopPage.EsmartAddon"+Fuel+"1"), Fuel+" Addon Selected");
			}
		}
		
		//Select the switch now button
		if(browser.isElementVisibleWithXpath(pageProperties.getProperty("EshopPage."+Fuel)))
		{
			verifyAndClickWithXpath(pageProperties.getProperty("EshopPage."+Fuel), Fuel+" Fuel");
		}
		else if(browser.isElementVisibleWithXpath(pageProperties.getProperty("EshopPage."+Fuel+"2"))){
			verifyAndClickWithXpath(pageProperties.getProperty("EshopPage."+Fuel+"2"), Fuel+" Fuel");
		}
		else if(Fuel == "Dual")
		{
			browser.clickWithXpath(pageProperties.getProperty("EshopPage."+Fuel)+"1");
			Report.updateTestLog("Dual Fuel Clicked", "Pass");
		}
	}
	
	
	public void fillYourOrderPageDetails(Acquisition acquisition,String AddrSelect,String JourneyType){
		if(SalesType.equalsIgnoreCase("Eshop")){
			fillYourOrderDetailsEshop(acquisition,AddrSelect,JourneyType);
		}
		else if(SalesType.equalsIgnoreCase("Esmart")){
			fillYourOrderDetailsEsmart(acquisition,AddrSelect,JourneyType);
		}
		
		ContinueButton("YourOrder");
	}
	
	public void fillYourOrderDetailsEshop(Acquisition acquisition,String AddrSelect,String JourneyType){
		selectGasSupplier(acquisition);
		selectElectricitySupplier(acquisition);
		selectJourneyType(JourneyType);
		selectAddressType(AddrSelect);
	}
	
	public void fillYourOrderDetailsEsmart(Acquisition acquisition,String AddrSelect, String JourneyType){
		selectGasSupplier(acquisition);
		selectElectricitySupplier(acquisition);
		selectJourneyType(JourneyType);
		selectAddressType(AddrSelect);
	}
	
	public void selectJourneyType(String JourneyType){
		if(JourneyType.equalsIgnoreCase("ForcedLogin")){
			//Select Gas Supplier British Gas
			if(browser.isElementVisible(pageProperties.getProperty("EshopPage.selectGasSupplier"))){
				verifyAndSelectDropDownBox(pageProperties.getProperty("EshopPage.selectGasSupplier"), "Gas Supplier", "British Gas");
			}
			
			//Enter Email and Password
			/*if(browser.isElementVisibleWithXpath(pageProperties.getProperty("EshopPage.YourOrderEmail"))){
				verifyAndInputByXpath(, name, value)
			}*/
			
		}
		else if(JourneyType.equalsIgnoreCase("ForcedRegistration")){
			
		}
	}
	
	public void selectAddressType(String AddrSelect){
		if(AddrSelect.equalsIgnoreCase("DiffAddr")){
				verifyAndClickWithXpath(pageProperties.getProperty("EshopPage.selectDifferentAddress"), "Different Supply Address");
				Report.updateTestLog("Different supply Address selected successfully", "Pass");
				clickAndVerifyRMRYourOrderPage();
		}
		else if(AddrSelect.equalsIgnoreCase("AnonymousAddr")){
			clickAndVerifyRMRYourOrderPage();
		}
		else if(AddrSelect.equalsIgnoreCase("SameAddr") && SalesType.equalsIgnoreCase("Eshop")){
			clickAndVerifyRMRYourOrderPage();
		}
	}
	
	/*public void fillYourOrderPageDetails(Acquisition acquisition,String Fuel, String TariffFuel){
		selectGasSupplier(acquisition);
		selectElectricitySupplier(acquisition);
		selectDualAddress(Fuel);
		clickAndVerifyRMRYourOrderPage(TariffFuel);
		ContinueButton("YourOrder");
	}*/
	
	
	public void clickAndVerifyRMRYourOrderPage(){
		//click the checkbox for amazon voucher
			verifyAndClickWithXpath(pageProperties.getProperty("EshopPage.YourOrderPageAmazonVoucherCkeckbox"), "Amazon voucher checkbox");
			Report.updateTestLog("Amazon voucher option clicked successfully", "Pass");
	}
	
	public void selectDualAddress(String Fuel){
		if(Fuel.equalsIgnoreCase("dual")){
			if(browser.isElementVisibleWithXpath(pageProperties.getProperty("EshopPage.selectDifferentAddress"))){
				verifyAndClickWithXpath(pageProperties.getProperty("EshopPage.selectDifferentAddress"), "Different Supply Address");
				Report.updateTestLog("Different supply Address selected successfully", "Pass");
			}
		}
	}
	
	public void selectGasSupplier(Acquisition acquisition){
		if(browser.isElementVisible(pageProperties.getProperty("EshopPage.selectGasSupplier"))){
			verifyAndSelectDropDownBox(pageProperties.getProperty("EshopPage.selectGasSupplier"), "Gas Supplier", acquisition.getGasSupplier());
		}
	}
	
	public void selectElectricitySupplier(Acquisition acquisition){
		if(browser.isElementVisible(pageProperties.getProperty("EshopPage.selectElectricitySupplier"))){
			verifyAndSelectDropDownBox(pageProperties.getProperty("EshopPage.selectElectricitySupplier"), "Electricity Supplier", acquisition.getElecSupplier());	
		}
	}
	
	public void ContinueButton(String ContinueType){
		if(ContinueType == "YourOrder" || ContinueType == "PersonalDetails" || ContinueType == "CurrentMeterDetails" || ContinueType=="PaymentPage"){
			verifyAndClickWithXpath(pageProperties.getProperty("EshopPage.Continue"), "Continue button");
		}
		
	}
	
	public void enterPersonalDetails(Acquisition acquisition, UserProfile userProfile,String AddrSelect,String JourneyType){
		if(SalesType.equalsIgnoreCase("Eshop")){
			enterCustomerDetails(acquisition,userProfile);
			enterSupplyAddress(acquisition);
			enterMonthsInfo(acquisition);
			verifyRMRIncentive(Fuel);
		}
		else if(SalesType.equalsIgnoreCase("Esmart")){
			enterPersonalDetailsEsmart(acquisition,userProfile,AddrSelect,JourneyType);
		}
		
		ContinueButton("PersonalDetails");
	}
	
	public void enterPersonalDetailsEsmart(Acquisition acquisition,UserProfile userProfile,String AddrSelect,String JourneyType){
		if(AddrSelect.equalsIgnoreCase("AnonymousAddr")||AddrSelect.equalsIgnoreCase("DiffAddr")){
			enterCustomerDetails(acquisition,userProfile);
			enterSupplyAddress(acquisition);
			enterMonthsInfo(acquisition);
			verifyRMRIncentive(Fuel);
		}
		else if(AddrSelect.equalsIgnoreCase("SameAddr")){
			clickAndVerifyRMRYourOrderPage();
			enterCustomerDetails(acquisition,userProfile);
		}
	}
	
	public void verifyRMRIncentive(String Fuel){
		String AmazonVoucher;
		AmazonVoucher = browser.getTextByXpath(pageProperties.getProperty("EshopPage.AmazonVoucher"));
		if(Fuel.equalsIgnoreCase("Dual")){
			if(AmazonVoucher.contains("20")){
				Report.updateTestLog(AmazonVoucher + "is present in the application page", "Pass");
			}
			else{
				Report.updateTestLog("Amazon voucher mismatch, The actual text present was "+AmazonVoucher, "Pass");
			}
		}
		else{
			if(AmazonVoucher.contains("10")){
				Report.updateTestLog(AmazonVoucher + "is present in the application page", "Pass");
			}
			else{
				Report.updateTestLog("Amazon voucher mismatch, The actual text present was "+AmazonVoucher, "Fail");
			}
		}		
	}
	
	public void enterCustomerDetails(Acquisition acquisition, UserProfile userProfile){
		//select title
		if(browser.isElementVisible(pageProperties.getProperty("EshopPage.Title"))){
			verifyAndSelectDropDownBox(pageProperties.getProperty("EshopPage.Title"), "Title ", acquisition.getTitle());
			Report.updateTestLog("Title Field entered successfully", "Pass");
		}
		
		
		//select first name
		if(browser.isElementVisibleWithXpath(pageProperties.getProperty("EshopPage.FirstName"))){
			verifyAndInputByXpath(pageProperties.getProperty("EshopPage.FirstName"), "First Name", acquisition.getFirstName());
			Report.updateTestLog("Firstname entered successfully", "Pass");
		}
		
		//select Sur Name
		if(browser.isElementVisibleWithXpath(pageProperties.getProperty("EshopPage.SurName"))){
			verifyAndInputByXpath(pageProperties.getProperty("EshopPage.SurName"), "Sur Name", acquisition.getLastName());
			Report.updateTestLog("surname entered successfully", "Pass");
		}
		
		
		//Enter date of Birth
		if(browser.isElementVisible(pageProperties.getProperty("EshopPage.Day"))){
			verifyAndSelectDropDownBox(pageProperties.getProperty("EshopPage.Day"), "date  ", acquisition.getDay());
			verifyAndSelectDropDownBox(pageProperties.getProperty("EshopPage.Month"), "Month  ", acquisition.getMonth());
			verifyAndSelectDropDownBox(pageProperties.getProperty("EshopPage.Year"), "Year  ", acquisition.getYear());
			Report.updateTestLog("Date of birth details entered successfully", "Pass");
		}
		
		
		//Enter Telephone Details
		if(browser.isElementVisibleWithXpath(pageProperties.getProperty("EshopPage.TelephoneNumber"))){
			verifyAndInputByXpath(pageProperties.getProperty("EshopPage.TelephoneNumber"), "Telephone Number ", acquisition.getMobileNumber());
			verifyAndSelectDropDownBox(pageProperties.getProperty("EshopPage.TelephoneType"), "telephone type ", acquisition.getTelephonetype());
			Report.updateTestLog("Telephone details entered successfully", "Pass");
		}
		
		
		//enter Email Details
		if(browser.isElementVisibleWithXpath(pageProperties.getProperty("EshopPage.EmailAddress"))){
			if(CustomerType.equalsIgnoreCase("anonymouscust")){
				verifyAndInputByXpath(pageProperties.getProperty("EshopPage.EmailAddress"), "Email Address", new Random().nextInt(1000)+"Centrica_digital@bgdigitaltest.co.uk");
			}
			else{
				verifyAndInputByXpath(pageProperties.getProperty("EshopPage.EmailAddress"), "Email Address", userProfile.getEmail());
				Report.updateTestLog("Email ID entered successfully", "Pass");
			}
		}
		if(browser.isElementVisible(pageProperties.getProperty("EshopPage.EmailType"))){
			verifyAndSelectDropDownBox(pageProperties.getProperty("EshopPage.EmailType"), "Email type ", acquisition.getEmailtype());
			Report.updateTestLog("Email Type selected successfully", "Pass");
		}
	
		//Esmart Add Anonymous Password
		if(SalesType.equalsIgnoreCase("Esmart")){
			if(CustomerType.equalsIgnoreCase("AnonymousCust")){
				verifyAndInputByXpath(pageProperties.getProperty("EshopPage.EsmartPassword"), "Enter password", "password12");
				verifyAndInputByXpath(pageProperties.getProperty("EshopPage.EsmartConfirmPassword"), "Enter password", "password12");
				Report.updateTestLog("Password Details entered for anonymous customer", "Pass");
			}
			else{
				Report.updateTestLog("Password Details not entered for anonymous customer", "Pass");
			}
		}
		
		
	}
	
	public void enterSupplyAddress(Acquisition acquisition){
		
		//Enter Address Details
		if(browser.isElementVisibleWithXpath(pageProperties.getProperty("EshopPage.PostCode"))){
			verifyAndInputByXpath(pageProperties.getProperty("EshopPage.PostCode"), "PostCode ", acquisition.getPostcode());
			verifyAndClickWithXpath(pageProperties.getProperty("EshopPage.SearchAddress"), "Search Address");
			browser.wait(8000);
			//Select Address
			selectPopulatedAddress();
		}
		
	}
	
	
	public void enterMonthsInfo(Acquisition acquisition){
		//Select  Month and Year Lived
		if(browser.isElementVisible(pageProperties.getProperty("EshopPage.YearLived"))){
			verifyAndSelectDropDownBox(pageProperties.getProperty("EshopPage.YearLived"), "Year Lived ", acquisition.getYearsLived());
			verifyAndSelectDropDownBox(pageProperties.getProperty("EshopPage.MonthLived"), "Month Lived ", acquisition.getMonthsLived());
		}
	}
	
	public void enterCurrentMeterDetails(Acquisition acquisition, String Fuel){
		//Select Meter Type
		browser.wait(3000);
		if(Fuel.contains("Gas")){
			if(SalesType.equalsIgnoreCase("Eshop")){
				verifyAndSelectDropDownBox(pageProperties.getProperty("EshopPage.selecGastMeterType"), "Gas Meter Type ", acquisition.getCurrentGasSupplier());	
			}
			verifyAndClickWithXpath(pageProperties.getProperty("EshopPage.GasSmartMeterEnabled"), "Gas Smart Meter Disabled");
		}
		else if(Fuel.contains("Electricity")){
			verifyAndSelectDropDownBox(pageProperties.getProperty("EshopPage.selecElectMeterType"), "Elec Meter Type ", acquisition.getCurrentElecSupplier());
			verifyAndClickWithXpath(pageProperties.getProperty("EshopPage.ElecSmartMeterEnabled"), "Elec Smart Meter Disabled");
		}
		else if(Fuel.contains("Dual")){
			//Select Meter for gas
			if(browser.isElementVisible(pageProperties.getProperty("EshopPage.selecGastMeterType"))){
				verifyAndSelectDropDownBox(pageProperties.getProperty("EshopPage.selecGastMeterType"), "Gas Meter Type ", acquisition.getCurrentGasSupplier());
				verifyAndClickWithXpath(pageProperties.getProperty("EshopPage.GasSmartMeterEnabled"), "Gas Smart Meter Disabled");
			}
			
			//Select meter for electricity
			if(browser.isElementVisible(pageProperties.getProperty("EshopPage.selecElectMeterType"))){
				verifyAndSelectDropDownBox(pageProperties.getProperty("EshopPage.selecElectMeterType"), "Elec Meter Type ", acquisition.getCurrentElecSupplier());
				verifyAndClickWithXpath(pageProperties.getProperty("EshopPage.ElecSmartMeterEnabled"), "Elec Smart Meter Disabled");
			}
			
		}
		
		
				
		//Meter Enabled
		
		//verify RMR incentive
		verifyRMRIncentive(Fuel);
		
		//click continue
		ContinueButton("CurrentMeterDetails");
	}
	
	public void enterPaymentDetails(Acquisition acquisition){
		//select PaymentMode
		verifyAndClickWithXpath(pageProperties.getProperty("EshopPage.MonthlyFixedDirectDebit"), "Monthly Fixed Direct Debit");
		
		//Enter Back Account Details
		verifyAndInputByXpath(pageProperties.getProperty("EshopPage.BankAccountNumber"), "Bank account number ", acquisition.getPaymentAccountNumber());
		verifyAndInputByXpath(pageProperties.getProperty("EshopPage.SortCode1"), "SortCode 1 ", acquisition.getSortCode1());
		verifyAndInputByXpath(pageProperties.getProperty("EshopPage.SortCode2"), "SortCode 2 ", acquisition.getSortCode2());
		verifyAndInputByXpath(pageProperties.getProperty("EshopPage.SortCode3"), "SortCode 3 ", acquisition.getSortCode3());
		verifyAndInputByXpath(pageProperties.getProperty("EshopPage.AccountHolderName"), "AccountHolderName ", acquisition.getAccountName());
		verifyAndSelectDropDownBox(pageProperties.getProperty("EshopPage.PaymentDate"), "Payment date ", acquisition.getPayDay());
		
		
		//Enter Gas Details
		if(browser.isElementVisible(pageProperties.getProperty("EshopPage.GasBillPeriod"))){
			verifyAndSelectDropDownBox(pageProperties.getProperty("EshopPage.GasBillPeriod"), "Bill Period", "Monthly");
		}
		
		if(browser.isElementVisibleWithXpath(pageProperties.getProperty("EshopPage.GasBillAmount"))){
			verifyAndInputByXpath(pageProperties.getProperty("EshopPage.GasBillAmount"), "Bill Amount", "4");
		}
		
		//Enter Electricity Details
		if(browser.isElementVisible(pageProperties.getProperty("EshopPage.ElectricityBillPeriod"))){
			verifyAndSelectDropDownBox(pageProperties.getProperty("EshopPage.ElectricityBillPeriod"), "Bill Period", "Monthly");
		}
		
		if(browser.isElementVisibleWithXpath(pageProperties.getProperty("EshopPage.ElectricityBillAmount"))){
			verifyAndInputByXpath(pageProperties.getProperty("EshopPage.ElectricityBillAmount"), "Bill Amount", "4");
		}
		
		//verify RMR Incentive
		verifyRMRIncentive(Fuel);
		
		//Click Continue
		ContinueButton("PaymentPage");
	}
	
	
	public void enterReviewOrderDetails(){
		//orderDate=new OnlineDBConnector().DBsysdate();
		//Check Special Needs
		verifySpecialNeeds();
		//select Terms and conditions 
		verifyAndClickWithXpath(pageProperties.getProperty("EshopPage.TermsandConditions"), "Terms and Conditions");
	    // select marketing consent preference 
		if (browser.isElementVisibleWithXpath(pageProperties.getProperty("EshopPage.newMarketingConsent"))){
        	//MarketingConsentFlag = true;
        //selectMarketingConsent();//calling MarketingConsent method
        }
		//verify RMR incentive
		verifyRMRIncentive(Fuel);
		
		//Click Place Order
		verifyAndClickWithXpath(pageProperties.getProperty("EshopPage.PlaceOrder"), "Place Order");
		
	}
	
	public void verifySpecialNeeds(){
		//Click the special needs
		if(browser.isElementVisibleWithXpath(pageProperties.getProperty("EshopPage.SpecialNeeds"))){
			verifyAndClickWithXpath(pageProperties.getProperty("EshopPage.SpecialNeeds"),"Special Needs");
			
			//Select the special needs
			verifyAndClickWithXpath(pageProperties.getProperty("EshopPage.AdultOver60"), "Adult over 60 Checkbox");
			verifyAndClickWithXpath(pageProperties.getProperty("EshopPage.WheelChair"), "WheelChair Checkbox");
			
		}
		else{
			Report.updateTestLog("Special Needs Object not found in the application page", "Fail");
		}
		
		
	}
	
	//Selecting Marketing Consent which are to be OptOut
    public void selectMarketingConsent(){
    	userProfile = PersonalDetailsPage.userProfileStat;//calling static userProfile from PersonalDetails page
    	 if(userProfile.getLetterConsent().equalsIgnoreCase("yes")){
    		 verifyAndClickWithXpath(pageProperties.getProperty("EshopPage.LetterConsent"), "Letter consent check box");
    	 }
    	 if(userProfile.getEmailConsent().equalsIgnoreCase("yes")){
    		 verifyAndClickWithXpath(pageProperties.getProperty("EshopPage.EmailConsent"), "Email consent check box"); 
    	 }
    	 if(userProfile.getLandlineConsent().equalsIgnoreCase("yes")){
    		 verifyAndClickWithXpath(pageProperties.getProperty("EshopPage.LandlineConsent"), "Landline consent check box");
    	 }
    	 if(userProfile.getMobileConsent().equalsIgnoreCase("yes")){
    		 verifyAndClickWithXpath(pageProperties.getProperty("EshopPage.MobileConsent"), "Mobile consent check box");
    	 }
    	 if(userProfile.getTextMessageConsent().equalsIgnoreCase("yes")){
    		 verifyAndClickWithXpath(pageProperties.getProperty("EshopPage.TextMessageConsent"), "TextMessage consent check box");
    	 }
    	 Report.updateTestLog("MarketingConsent Screenshot","WARN");
    	    		
    }

    //Verifying which Marketing Consent are OptOut and OptIn  
    public void verifyMarketingConsent(){
    	userProfile = PersonalDetailsPage.userProfileStat;
    	LeadData = new OnlineDBConnector().getEshopLeadData(orderDate);
	if(MarketingConsentFlag == true)
   	 if(userProfile.getLetterConsent().equalsIgnoreCase("yes")){
   		 if(LeadData.contains("<MarketingChannelPreference MarketingChannel=\"Letter\" OptIn=\"N\"/>")){
   			Report.updateTestLog("Letter Consent is Optout", "PASS");
   		}
   		else{
   			Report.updateTestLog("Letter Consent is not Optout", "FAIL");
   		}
    }
   	 else if(!userProfile.getLetterConsent().equalsIgnoreCase("yes")){
   		if(LeadData.contains("<MarketingChannelPreference MarketingChannel=\"Letter\" OptIn=\"Y\"/>")){
   			Report.updateTestLog("Letter Consent is Optin", "PASS");
   		}
   		else{
   			Report.updateTestLog("Letter Consent is not Optin", "FAIL");
   		}
   	 }
   	 if(userProfile.getEmailConsent().equalsIgnoreCase("yes")){
   		 if(LeadData.contains("<MarketingChannelPreference MarketingChannel=\"Email\" OptIn=\"N\"/>")){
   			Report.updateTestLog("Email Consent is Optout", "PASS");
   		}
   		else{
   			Report.updateTestLog("Email Consent is not Optout", "FAIL");
   		}
    }
   	 else if(!userProfile.getEmailConsent().equalsIgnoreCase("yes")){
   		if(LeadData.contains("<MarketingChannelPreference MarketingChannel=\"Email\" OptIn=\"Y\"/>")){
   			Report.updateTestLog("Email Consent is Optin", "PASS");
   		}
   		else{
   			Report.updateTestLog("Email Consent is not Optin", "FAIL");
   		}
   	 }
   	if(userProfile.getLandlineConsent().equalsIgnoreCase("yes")){
  		 if(LeadData.contains("<MarketingChannelPreference MarketingChannel=\"Landline\" OptIn=\"N\"/>")){
  			Report.updateTestLog("LandLine Consent is Optout", "PASS");
  		}
  		else{
  			Report.updateTestLog("LandLine Consent is not Optout", "FAIL");
  		}
   }
  	 else if(!userProfile.getLandlineConsent().equalsIgnoreCase("yes")){
  		if(LeadData.contains("<MarketingChannelPreference MarketingChannel=\"Landline\" OptIn=\"Y\"/>")){
  			System.out.println("");
  			Report.updateTestLog("LandLine Consent is Optin", "PASS");
  		}
  		else{
  			Report.updateTestLog("LandLine Consent is not Optin", "FAIL");
  		}
  	 }
   	if(userProfile.getMobileConsent().equalsIgnoreCase("yes")){
 		 if(LeadData.contains("<MarketingChannelPreference MarketingChannel=\"Mobile\" OptIn=\"N\"/>")){
 			Report.updateTestLog("Mobile Consent is Optout", "PASS");
 		}
 		else{
 			Report.updateTestLog("Mobile Consent is not Optout", "FAIL");
 		}
  }
 	 else if(!userProfile.getMobileConsent().equalsIgnoreCase("yes")){
 		if(LeadData.contains("<MarketingChannelPreference MarketingChannel=\"Mobile\" OptIn=\"Y\"/>")){
 			Report.updateTestLog("Mobile Consent is Optin", "PASS");
 		}
 		else{
 			Report.updateTestLog("Mobile Consent is not Optin", "FAIL");
 		}
 	 }
   	if(userProfile.getTextMessageConsent().equalsIgnoreCase("yes")){
		 if(LeadData.contains("<MarketingChannelPreference MarketingChannel=\"SMS/MMS\" OptIn=\"N\"/>")){
			Report.updateTestLog("TextMessage Consent is Optout", "PASS");
		}
		else{
			Report.updateTestLog("TextMessage Consent is not Optout", "FAIL");
		}
 }
	 else if(!userProfile.getTextMessageConsent().equalsIgnoreCase("yes")){
		if(LeadData.contains("<MarketingChannelPreference MarketingChannel=\"SMS/MMS\" OptIn=\"Y\"/>")){
			Report.updateTestLog("TextMessage Consent is Optin", "PASS");
		}
		else{
			Report.updateTestLog("TextMessage Consent is not Optin", "FAIL");
		}
	 }
  	
   	 
   }
	public void verifyThankyouPage(String Fuel){
		if(browser.isElementVisibleWithXpath(pageProperties.getProperty("EshopPage.PlaceOrder"))){
			if(browser.getTextByXpath(pageProperties.getProperty("EshopPage.PlaceOrder")).contains(Fuel)){
				Report.updateTestLog(browser.getTextByXpath(pageProperties.getProperty("EshopPage.PlaceOrder")) + "Text Displayed successfully", "Pass");
			}
			else{
				Report.updateTestLog("Confirmation page text error "+browser.getTextByXpath(pageProperties.getProperty("EshopPage.PlaceOrder")) , "Fail");
			}
		}
	}
	
	public void verifyEshopLeadType(){
		String leadtype=new OnlineDBConnector().getEshopLeadType(orderDate);
	   	System.out.println(leadtype);
	   	String strAcqLead = "ENERGY_ACQ";
	   	String strConvLead = "ENERGY_CONV";
	   	if (leadtype.equals(strAcqLead)){
	   		Report.updateTestLog("Lead Type Generated for Acquisition is " + leadtype, "DONE");
	   		}
	   	else if(leadtype.contains(strConvLead)){
	   		Report.updateTestLog("Lead Type Generated for Conversion is " + leadtype, "DONE");
	   	}
	   	else{
	   	Report.updateTestLog("Lead Type not as expected, Lead Type Generated is :" + leadtype, "FAIL");
	   	}  
	   	if(MarketingConsentFlag == true){
		   	verifyMarketingConsent();
		   	}
	}
	
	
	public void verifyEshopLeadID(){
		String leadid=new OnlineDBConnector().getEshopLeadID(orderDate);
	   	Report.updateTestLog("Lead ID Generated is "+leadid, "DONE");
	   	
	}
	
	public void backToHomepage(){
		verifyAndClickWithXpath(pageProperties.getProperty("EshopPage.BackToHomePage"), "Home Page");
	}
	
	public void logout(){
		verifyAndClickWithXpath(pageProperties.getProperty("EshopPage.logout"), "logout");
		browser.wait(20000);
	}
	
	public void fillConversionYourOrderPageDetails(Acquisition acquisition){
		selectGasSupplier(acquisition);
		selectElectricitySupplier(acquisition);
		clickAndVerifyRMRYourOrderPage();
		ContinueButton("YourOrder");
	}
	
	public void selectPopulatedAddress(){
		browser.wait(8000);
		if(browser.isElementVisibleWithXpath(pageProperties.getProperty("EshopPage.ChangeAddress"))){
			Report.updateTestLog("Address Prepopulated", "Pass");
		}
		else if(browser.isElementVisibleWithXpath(pageProperties.getProperty("EshopPage.PopulatedAddress"))){
			Select select = new Select(browser.getElementByXpath(pageProperties.getProperty("EshopPage.PopulatedAddress")));
			select.selectByIndex(1);
			Report.updateTestLog("Address Selected from Dropdown", "Pass");
		}
		else{
			Report.updateTestLog("Address not getting populated from Dropdown", "Fail");
		}	
	}
	
	public void fillPartAcqAndPartConversionYourOrderPageDetails(Acquisition acquisition){
		selectGasSupplier(acquisition);
		selectElectricitySupplier(acquisition);
		selectPopulatedAddress();
		if(SalesType.equalsIgnoreCase("Eshop")){
			clickAndVerifyRMRYourOrderPage();
		}
		
		ContinueButton("YourOrder");
	}

	public void verifyErrorMessage() {
		if(browser.isElementVisibleWithXpath(pageProperties.getProperty("EshopPage.ErrorMessageTOU"))){
			Report.updateTestLog("Error Message is displayed", "Pass");}
		else{
			Report.updateTestLog("Error Message is NOT displayed", "Fail");
		}
	}
}
