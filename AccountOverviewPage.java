package bg.framework.app.functional.page.reFactoring;

import bg.framework.app.functional.common.ApplicationConfig;
import bg.framework.app.functional.entities.UserProfile;
import bg.framework.app.functional.page.common.BasePage;
import bg.framework.app.functional.page.common.GlobalErrorMessages;
import bg.framework.app.functional.util.OnlineDBConnector;
import bg.framework.app.functional.util.PropertyLoader;
import bg.framework.app.functional.util.Report;
import bg.framework.app.functional.util.SiebelDataBase;
import org.openqa.selenium.NoSuchElementException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class AccountOverviewPage extends BasePage {
	
	private final static String File_AccPage = "resources/ReFactoring/AccountOverview.properties";
    private static Properties AccPageProperties = new PropertyLoader(File_AccPage).load();
    final String Pass_Str = "PASS";
    private static String orderDate;
    final String Fail_Str = "FAIL";
    
    public AccountOverviewPage() {

    }

// Verifying the values in the Bread Crumb in Account Overview Page

    public void verifyingForgotEmailBreadCrumb() {
        final String strBreadCrumbText = browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.BreadCrumbID"));
        Report.updateTestLog("Displayed Bread Crumb Is  :" + strBreadCrumbText,Pass_Str);
    }

 // Verifying whether appropriate text is displayed in Account Overview Page once after logging with appropriate User Credentials
    
    public void verifyingMyAccount(UserProfile userProfile) {
    	//accountOverviewTitleVerification();
    	//accountOverviewLogInUserValidation(userProfile);
    	//validateAccOverviewContents(userProfile);
//        final String strMyAccText = browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.MyAccountID"));
//        System.out.println(strMyAccText);
//        if (strMyAccText.trim().equals("Your accounts")){
//        	Report.updateTestLog("Expected Text was displayed : "+strMyAccText, Pass_Str);
//        }
//        else{
//        	Report.updateTestLog("Expected Text was not displayed", Fail_Str);
       // }
    }
    private void accountOverviewTitleVerification(){
    	browser.wait(2000);
    	String accountOverview= browser.getTextByXpath("//title");
    	if(accountOverview.trim().contains("Account Overview")){
    		Report.updateTestLog("Account overview page displayed ", Pass_Str);
    	}else{
    		Report.updateTestLog("Account overview page not displayed ", Fail_Str);
    	}
    }
    public void AccountOverViewVerification(UserProfile userProfile){
    	//accountOverviewTitleVerification();
    	verifyPageTitle("Account overview");
    	loggeduserVerification(userProfile.getUCRN());
    	crosscellVerification();
    }
    
// Verify whether logged in User's Last Name is displayed in Account Overview Page by validating against the Siebel database 
//  for the appropriate UCRN
    
    public void accountOverviewLogInUserValidation(UserProfile userProfile){
    	//verifyingForgotEmailBreadCrumb();
    	if(browser.isElementVisibleWithXpath(AccPageProperties.getProperty("AccountOverviewPage.PaperlessOverlay"))){
        	verifyBenefitsOfPaperlessOverlay();
        	}

    	Report.updateTestLog("Account Overview Page", "WARN");
    	if (ApplicationConfig.BRAND.equalsIgnoreCase("bg")){
    	verifyPageTitle("Account overview");
    	}
    	else{
    		verifyPageTitle("Sainsbury's Energy - Your Account Overview");	
    	}
    	Report.takeScreenshot("Account Overview Page", "WARN");
    	verifyingMyAccount(userProfile);
    	loggeduserVerification(userProfile.getUCRN());    	
   	    String AccNumber = userProfile.getAccNumber();
    	verifyCustomerAddress( AccNumber);
    	validateAccOverviewContents(userProfile);
    	validateESRcontents();
    	//verifyDebitandCredit(userProfile);
    	//verifyCQ5();
    }
    
    public void validateESRcontents(){
    	if(browser.isElementVisibleWithXpath(".//*[@id='esrpromo']/h3)")){
    	  if(browser.getTextByXpath(".//*[@id='esrpromo']/h3").equalsIgnoreCase("Energy Saver Report")){
    		if(browser.getTextByXpath(".//*[@id='ESR']/div[3]/a").contains("Complete Energy report")){
    			Report.updateTestLog("Energy Saver Report contents displayed", "FAIL");
    		}
    	  }
    	}
    	else{
    		Report.updateTestLog("Energy Saver Report contents not displayed as Expected", "PASS");
    	}
    }
    
    public void accountOverviewSubmitMeterRead(UserProfile userProfile){

    	String submitRead=browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.submitMeter"));
        if(submitRead.equals("Submit a meter reading and more"))
        {
        	Report.updateTestLog("Submit Meter Promo is avaialble", "PASS");
        }else
        {
        	Report.updateTestLog("Submit Meter Promo is not avaialble", "FAIL");
        }
    	
    }
    
    public void accountOverviewLogInUserValidationWTP(UserProfile userProfile){
    	//verifyingForgotEmailBreadCrumb();
    	Report.updateTestLog("Account Overview Page", "WARN");
    	if (ApplicationConfig.BRAND.equalsIgnoreCase("bg")){
    	verifyPageTitle("Account overview");
    	}
    	else{
    		verifyPageTitle("Sainsbury's Energy - Your Account Overview");	
    	}
    	Report.takeScreenshot("Account Overview Page", "WARN");
    	verifyingMyAccount(userProfile);
    	loggeduserVerification(userProfile.getUCRN());    	
   	    String AccNumber = userProfile.getAccNumber();
    	validateAccOverviewContents(userProfile);
    	//verifyCQ5();
    }
    
    public void loggeduserVerification(String email){
    	browser.wait(1000);
    	final String strLastName = new SiebelDataBase().getLastName(email);
    	String loggedInUserName = browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.UserId"));
    	String loggedInUserNameArray[] = loggedInUserName.split(" ");
    	if (loggedInUserNameArray[2].equalsIgnoreCase(strLastName)){
    		Report.updateTestLog("Expected Logged In User is displayed and the User Name Is: "+loggedInUserName, Pass_Str);
        }
        else{
        	Report.updateTestLog("Expected Logged In User "+strLastName+" was not displayed", Fail_Str);
        }
    }

// Verify the contents displayed in Account Overview Page 
    private void otherOfferCheck(String type){
    	String link1="";    	
    	browser.wait(2000);
    	if(browser.isElementVisibleWithXpath(AccPageProperties.getProperty("AccountOverviewPage.addAccountLink").replace("TYPE", type))){
    		link1=AccPageProperties.getProperty("AccountOverviewPage.addAccountLink").replace("TYPE", type);    	
    		if(link1.contains(type)){
    		Report.updateTestLog("Link for add "+type+" displayed", "PASS");}
    		else{
			Report.updateTestLog("Link for add "+type+" is not displayed", "FAIL");
		}    	
    	}else{
    		Report.updateTestLog("Cross sell section not displayed", "DONE");
    	}
    }
    
    public void crosscellVerification(){
    	String accTypeText=null;
    	Set<String> accountType=new HashSet<String>();
    	int accountCount = browser.getChildElementsCountByXpath(AccPageProperties.getProperty("AccountOverviewPage.accountCount"));  
    	for(int count=1;count<=accountCount;count++){
    		accTypeText = browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.AccountTypeID").replace("ACC_NUM", ""+count));
    		accountType.add(accTypeText.trim());
    	}
    	System.out.println(accountType);
    	if(accountType.contains("Gas") && accountType.contains("Electricity") && accountType.contains("Insurance & Repair")){
			otherOfferCheck(AccPageProperties.getProperty("AccountOverviewPage.crossCellEnergy"));
		}else
			if(accountType.contains("Gas") && accountType.contains("Electricity")){
    		otherOfferCheck(AccPageProperties.getProperty("AccountOverviewPage.crossCellIns"));
    	}else
    		if(accountType.contains("Gas")){    		
    		otherOfferCheck(AccPageProperties.getProperty("AccountOverviewPage.crossCellEle"));
    			otherOfferCheck(AccPageProperties.getProperty("AccountOverviewPage.crossCellIns"));
    			System.out.println("FI");
    	}else
    		if(accountType.contains("Electricity")){    	
    		otherOfferCheck(AccPageProperties.getProperty("AccountOverviewPage.crossCellGas"));
    		otherOfferCheck(AccPageProperties.getProperty("AccountOverviewPage.crossCellIns"));
    			System.out.println("FII");
    	}else 
    		if(accountType.contains("Energy")){    		
        		otherOfferCheck(AccPageProperties.getProperty("AccountOverviewPage.crossCellIns"));
        }else
        	if(!accountType.contains("Gas") && !accountType.contains("Electricity")){
        		otherOfferCheck(AccPageProperties.getProperty("AccountOverviewPage.crossCellEle"));
        		otherOfferCheck(AccPageProperties.getProperty("AccountOverviewPage.crossCellGas"));
        		System.out.println("FIII");
        }
    		

    	
    	
    }
    public void validateAccOverviewContents(UserProfile userProfile){
    	final String expectedEmailAdd = new OnlineDBConnector().getUserEmail(userProfile.getUCRN());
    	final String accountNum = new SiebelDataBase().getAccountNumber(userProfile.getUCRN());
    	/*String nectarText = browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.NectarTextID"));
    	//String accTypeText="";
    	if (browser.isElementVisibleWithXpath(AccPageProperties.getProperty("AccountOverviewPage.ManageAccID"))){
    	String manageAccText = browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.ManageAccID"));
    	//String optionText = browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.OptionTextID"));
    	//String readOurText = browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.ArticleTextID"));
    	//String tariffText = browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.TariffTextID"));
    	//String offerText = browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.OfferID"));
    	//String energySaverText = browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.EnergySaverID"));
    	
    	final String[] AccOverviewContents = {nectarText,  manageAccText};
    	    	for (int i=0; i < AccOverviewContents.length ; i++){
    		Report.updateTestLog("Displayed Accont Overview Text Is: "+AccOverviewContents[i], Pass_Str);    		
    	}
    	}
    	    	if(nectarText.toLowerCase().contains("Register".toLowerCase())){
        			verifyAndClickWithLinkText(nectarText, "Register link");
        			if(ApplicationConfig.BRAND.equalsIgnoreCase("bg")){
        				try {
    						Thread.sleep(4000);
    					} catch (InterruptedException e) {
    						e.printStackTrace();
    					}
        				verifyPageTitle("Your Nectar points - British Gas");
        				browser.browserBack();
        			} else {
        			verifyPageTitle("Sainsbury's Energy - Your Account Overview");
        			browser.browserBack();
        			}
        		}
        		if(nectarText.toLowerCase().contains("Your Nectar points".toLowerCase())){
        			if(ApplicationConfig.BRAND.equalsIgnoreCase("bg")){
        			verifyAndClickWithLinkText(nectarText, "Your Nectar points");
        			try {
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
        			verifyPageTitle("Your Nectar Points -British Gas");
        			
        			browser.browserBack();}
        		}
        		if(nectarText.toLowerCase().contains("Collect Nectar points".toLowerCase())){
        			if(ApplicationConfig.BRAND.equalsIgnoreCase("bg")){
        			verifyAndClickWithLinkText(nectarText, "Collect Nectar points");
        			try {
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
//        			verifyPageTitle("Collect");
        			browser.browserBack();}
        		}*/
    	int AccTable = browser.getChildElementsCountByXpath(AccPageProperties.getProperty("AccountOverviewPage.CountID"));
    	System.out.println(AccTable+1);
    	for (int i=1; i < AccTable; i++){
    		String AccName = browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.AccountName").replace("NUM", ""+i));
    		System.out.println(AccName);
        	Report.updateTestLog("Displayed Account Is: "+AccName, Pass_Str);
    	}
    	verifyAddMissingAccount();
    	//verifyAndClickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.MissingAccID"), "Missing Account Link");
    	//verifyAndInputByXpath(AccPageProperties.getProperty("AccountOverviewPage.CustRefNum"), "FirstName", "admin");
    	//verifyAndClickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.WhereCanIFindID"), "Missing Account Text");
//    	String toolTipText = browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.MyAccountID"));
//    	
//    	if (toolTipText.trim() == "customer reference"){
//    		Report.updateTestLog("Expected Text was displayed : "+toolTipText, Pass_Str);
//    	} else {
//    		Report.updateTestLog("Expected Text was not displayed: "+toolTipText, Fail_Str);
//    	}
    	
    	//verifyAndClickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.CloseButtonID"), "Missing Account close button");
    	/*if (ApplicationConfig.BRAND.equalsIgnoreCase("se")){
    	verifyAndClickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.CompleteEnergyID"), "Energy Savers report");
    	 verifyPageTitle("Energy Savers Report - Save Energy - British Gas");*/
    	//}
    	
    	//verifyAndClickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.LogOutID"), "Log Out");
    	   	
    }
    
  /*  public void verifyCQ5(){
    	verifyAndClickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.CQLoginId"), "Login");
    	verifyAndInputByXpath(AccPageProperties.getProperty("AccountOverviewPage.CQLoginName"), "FirstName", "admin");
        verifyAndInputByXpath(AccPageProperties.getProperty("AccountOverviewPage.CQLoginPwd"), "LastName","admin");
    	verifyAndClickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.CQOKButton"), "OK");
    	browser.wait(30000);
    	verifyAndClickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.CQEtc"), "Etc");
    	browser.wait(1000);
    	verifyAndClickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.CQCentricaTools"), "Centrica Tools");
    	browser.wait(1000);
    	//browser.doubleClick(AccPageProperties.getProperty("AccountOverviewPage.CQErrorMsgMaint"));
    	browser.wait(30000);
    	
    	verifyAndClickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.CQOKButton"), "OK");
    	
    	verifyAndInputByXpath(AccPageProperties.getProperty("AccountOverviewPage.CQUserId"), "FirstName", "admin");
        verifyAndInputByXpath(AccPageProperties.getProperty("AccountOverviewPage.CQPassword"), "LastName","admin");
    	verifyAndClickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.CQOK"), "OK");
    	int a = browser.getRowCountByXpath("//form[contains(@action,'errormessageoverrides')]//table");
    	int b = browser.getColCountByXpath("//form[contains(@action,'errormessageoverrides')]//table");
    	System.out.println(a);
    	System.out.println(b);
    	for (int i = 1; i < a; i++) {
    		String errorKey = browser.getTextByXpath("//form[contains(@action,'errormessageoverrides')]//table//tbody//tr["+i+"]//td[1]");
    		//System.out.println(errorKey);
    		if (errorKey.trim().equalsIgnoreCase("eshop.middleinitial.invalid")){
    			String errorKey1 = browser.getTextByXpath("//form[contains(@action,'errormessageoverrides')]//table//tbody//tr["+i+"]//td[2]");
    			System.out.println(errorKey1);
    			browser.inputByXpath("//form[contains(@action,'errormessageoverrides')]//table//tbody//tr["+i+"]//td[3]/textarea","thanks"); 
    		}
        
    }}*/
    
   
   
    public void verifyCustomerAddress(String accountNumber) {
        SiebelDataBase siebelDatabase = new SiebelDataBase();
        List<String> address = siebelDatabase.getAddress(accountNumber);
        System.out.println(address);
        String[] arrayaddress = (String[]) address.toArray(new String[0]);
        String houseno = arrayaddress[0];
        String addNum = arrayaddress[1];
        String addres = arrayaddress[2];
        String city = arrayaddress[3];
        String zipcode = arrayaddress[4];
        String number = "";
        String fulladdress = "";
        if (!(houseno==null)) {
            number = houseno;
        } else if (!(addNum==null)) {
            number = addNum;
        }

        String dbAddress= number + " " + addNum + " " + addres + " " + city + " " + zipcode;
       // if(ApplicationConfig.BRAND.equalsIgnoreCase("BG")){
        	String addressOneAdd = null;
            String addressTwoAdd = null;
        	int accountCount = browser.getChildElementsCountByXpath(AccPageProperties.getProperty("AccountOverviewPage.accountCount"));        	        	
        	String expectedAccountNum = accountNumber;
        		for (int i = 1; i <= accountCount; i++) {
        			String currentAccountNum ;
        			if(browser.isElementVisibleWithXpath(AccPageProperties.getProperty("AccountOverviewPage.accountNumber").replace("ACC_NUM", "" + i))){
        				currentAccountNum = browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.accountNumber").replace("ACC_NUM", "" + i)).trim();
        				if (currentAccountNum.equalsIgnoreCase(expectedAccountNum)) {
        					addressOneAdd = browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.addressLineOne").replace("ACC_NUM", "" + i));
        					addressTwoAdd = browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.addressLineTwo").replace("ACC_NUM", "" + i));
        					fulladdress = addressOneAdd + addressTwoAdd;        					
        				}
                 }else{
        				currentAccountNum = browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.accountNumberClose"));
        				if (currentAccountNum.equalsIgnoreCase(expectedAccountNum)) {
        					addressOneAdd = browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.addressLineOneClose"));
        					addressTwoAdd = browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.addressLineTwoClose"));
        					fulladdress = addressOneAdd + addressTwoAdd;        					
                 }
        			}        	        		
        				
        		}
        	if(fulladdress.contains(number) || fulladdress.contains(addres )|| fulladdress.contains(city) || fulladdress.contains(zipcode)){
         	   Report.updateTestLog("Address verification done with database successfull<br>"+
        	"Database address-<b>"+dbAddress+"</b><br>Page address-<b>"+fulladdress+"</b>", "PASS");
            }else{
         	   Report.updateTestLog("Address verification done with database is not successfull<br>"+
        	"Database address-<b>"+dbAddress+"</b><br>Page address-<b>"+fulladdress+"</b>", "FAIL");
            }
      //  }
//        else if(ApplicationConfig.BRAND.equalsIgnoreCase("Fusion")){
//        	fulladdress = browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.address"));
//        	System.out.println(fulladdress);
//        	if(fulladdress.contains(number) && fulladdress.contains(addres )&& fulladdress.contains(city) && fulladdress.contains(zipcode)){
//         	   Report.updateTestLog("Address verification done with database successfull<br>"+
//        	"Database address-<b>"+dbAddress+"</b><br>Page address-<b>"+fulladdress+"</b>", "PASS");
//            }else{
//         	   Report.updateTestLog("Address verification done with database is not successfull<br>"+
//        	"Database address-<b>"+dbAddress+"</b><br>Page address-<b>"+fulladdress+"</b>", "FAIL");
//            }
       // }
        }
  
    public void logout() {
    	verifyAndClickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.Logout"), "Log out");
    	if (ApplicationConfig.BRAND.equalsIgnoreCase("bg")){
    		
    	try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	verifyPageTitle("Login to Your Account - British Gas");}
    	//Report.takeScreenshot("After Log out", "WARN");
    }
    public void verifyAddMissingAccount(){
    	if (ApplicationConfig.BRAND.equalsIgnoreCase("bg")){
    	browser.clickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.MissingAccID"));
    	browser.wait(3000);
    	if(browser.isElementVisibleWithXpath(AccPageProperties.getProperty("AccountOverviewPage.MissingAccOverlay"))){
    		Report.updateTestLog("Add a Missing Account overlay displayed","PASS");	
    	}else{
    		Report.updateTestLog("Add a Missing Account overlay not displayed","FAIL");
    	}
    	browser.clickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.MissingAccOverlayclose"));    	
    }else {
    	browser.clickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.MissingAccID"));
    	browser.wait(3000);
    	if(browser.isElementVisibleWithXpath(AccPageProperties.getProperty("AccountOverviewPage.MissingAccOverlay"))){
    		Report.updateTestLog("Add a Missing Account overlay displayed","PASS");	
    	}else{
    		Report.updateTestLog("Add a Missing Account overlay not displayed","FAIL");
    	}
    	browser.clickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.SEMissingAccOverlayclose"));
    }
    }
    public void verifyBrowserback(){
    	browser.wait(5000);
       browser.browserBack();
      if (browser.isElementVisibleWithXpath(AccPageProperties.getProperty("AccountOverviewPage.MissingAccID"))){
            Report.updateTestLog("Account Overview page exists, Browser back working as expected","PASS");
        }else {
            Report.updateTestLog("Account Overview page not exists, page navigated to back page","FAIL");
      }
    }
    public void registerNectarPage() {
    	browser.clickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.nectarregisterlink"));
    	if (browser.isElementVisibleWithXpath(AccPageProperties.getProperty("AccountOverviewPage.NectarPage"))){
            Report.updateTestLog("Nectar register page exists","PASS");
        }else {
            Report.updateTestLog("Nectar register page not exists","FAIL");
      }
    }
    public void verifyandEnterAddMissingAccount(UserProfile userProfile){
    	if (ApplicationConfig.APP_BG_URL.equalsIgnoreCase("bg")){
    	browser.clickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.MissingAccID"));
    	browser.wait(3000);
    	if(browser.isElementVisibleWithXpath(AccPageProperties.getProperty("AccountOverviewPage.MissingAccOverlay"))){
    		Report.updateTestLog("Add a Missing Account overlay displayed","PASS");	
    	}else{
    		Report.updateTestLog("Add a Missing Account overlay not displayed","FAIL");
    	}
    	browser.input(AccPageProperties.getProperty("AccountOverviewPage.MissingAccount"),userProfile.getAccNumber());
    	browser.clickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.MissingAccOverlayclose"));    	
    }else {
    	browser.clickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.MissingAccID"));
    	browser.wait(3000);
    	if(browser.isElementVisibleWithXpath(AccPageProperties.getProperty("AccountOverviewPage.MissingAccOverlay"))){
    		Report.updateTestLog("Add a Missing Account overlay displayed","PASS");	
    	}else{
    		Report.updateTestLog("Add a Missing Account overlay not displayed","FAIL");
    	}
    	browser.clickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.SEMissingAccOverlayclose"));
    }
    }
    public void verifyDebitandCredit(UserProfile userProfile){
    	String accNum = userProfile.getAccNumber();
    	String baltype = browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.balancetype").replace("ACC_NUM", accNum));
    	String balAmt = browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.balanceAmt").replace("ACC_NUM", accNum));
    	if(browser.isTextPresent("closed")){
    	if (baltype.contains("debit")){    		
    		if(browser.isElementVisibleWithXpath(AccPageProperties.getProperty("AccountOverviewPage.MakePaymentLink").replace("ACC_NUM", accNum))){
    			String makepayment = browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.MakePaymentLink").replace("ACC_NUM", accNum));
    			Report.updateTestLog("Account Overview Page","WARN");
    			Report.updateTestLog("Make a payment link ::"+makepayment+" displayed for "+ baltype+ " with balance "+balAmt+" as expected", "PASS");
    			verifyAndClickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.MakePaymentLink").replace("ACC_NUM", accNum), "Make a Payment Link");
    			verifyPageTitle("Make a payment");
    			browser.browserBack();
    		}else{
    			Report.updateTestLog("Make a payment link not displayed for "+ baltype+ " as expected", "FAIL");
    		}    			
    	}
    	if(baltype.contains("credit")){
        		if(browser.isElementVisibleWithXpath(AccPageProperties.getProperty("AccountOverviewPage.MakePaymentLink").replace("ACC_NUM", accNum))){
        			String makepayment = browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.MakePaymentLink").replace("ACC_NUM", accNum));
        			Report.updateTestLog("Make a payment link ::"+makepayment+" displayed for "+ baltype, "FAIL");
        		}else{
        			Report.updateTestLog("Account Overview Page","WARN");
        			Report.updateTestLog("Make a payment link not displayed for "+ baltype+" with balance "+balAmt+" as expected", "PASS");
        		}
    		}
    	if(balAmt.contains("£0.00")){
    		if(browser.isElementVisibleWithXpath(AccPageProperties.getProperty("AccountOverviewPage.MakePaymentLink").replace("ACC_NUM", accNum))){
    			String makepayment = browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.MakePaymentLink").replace("ACC_NUM", accNum));
    			Report.updateTestLog("Make a payment link ::"+makepayment+" displayed for zero balance with "+ baltype, "FAIL");
    		}else{
        		Report.updateTestLog("Account Overview Page","WARN");
    			Report.updateTestLog("Make a payment link not displayed for zero balance "+ baltype+ " with balance "+balAmt+ " as expected", "PASS");
    		}
    	  }
    	} else {
    		Report.updateTestLog("Closed Account not Present in application","DONE");
    	}
    }
    
    public void navigateBackToMyAccount(){
    	if(browser.isElementVisibleWithXpath(AccPageProperties.getProperty("AccountOverviewPage.MakePaymentLink"))){
    		
    	}
    }

    /////////////Services Email//////
    public void verifyBenefitsOfPaperlessOverlay(){
    	if(browser.isElementVisibleWithXpath(AccPageProperties.getProperty("AccountOverviewPage.PaperlessOverlay"))){
    		verifyAndClickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.PaperlessOverlay"),"Paperless Overlay Close Button");
    		Report.updateTestLog("Paperless Overlay is Displayed to customer when Logged in for first Time", "PASS");
    	}
    	else{
    		Report.updateTestLog("Paperless Overlay is not Displayed for service customers when Logged in for first Time", "FAIL"); 		
    	}
    }
    

        
    
    /*--------------------------------------------------------------------SMB Phase 2-------------------------------------------------------------- */
    
    
    public void clickManageAccount(){
    	browser.wait(12000);
    	verifyAndClickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.ManageAccountLink"), "Manage Account");
    /*	verifyAndClickWithLinkText(" Manage account", "Manage Account Link");
    	browser.clickWithLinkText("                          Manage account                     ");*/
    }
    
    public void clickManageAccount1(){
    	browser.wait(10000);
    	verifyAndClickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.ManageAccountLink1"), "Manage Account 1");

    }
    public void clickManageAccount2(){
    	browser.wait(10000);
    	verifyAndClickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.ManageAccountLink2"), "Manage Account 2");

    }
    public void browserBack(){
    	browser.wait(10000);
    	browser.browserBack();

    }
        
    public void clickChangeBillDate(){
    	browser.wait(3000);
    	if(browser.isElementVisibleWithXpath(AccPageProperties.getProperty("AccountOverviewPage.ChangeBillDate"))){
    		verifyAndClickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.ChangeBillDate"), "Change Bill Date");
    	}if(browser.isElementVisibleWithXpath(AccPageProperties.getProperty("AccountOverviewPage.ChangeBillDate1"))){
    		verifyAndClickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.ChangeBillDate1"), "Change Bill Date");
    	}
    	
    	}           
    
    public void selectDateDropDown(CharSequence date){
    	
    	if(browser.isElementVisible(AccPageProperties.getProperty("AccountOverviewPage.Billdate"))){
    	//browser.input(AccPageProperties.getProperty("SMB.Billdate"),acquisition.getAccountName());
        //String dropbilldate=browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.Billdatevalue"));
    		String dropbilldate=browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.Billdatevalue").replace("+i+", date));
        System.out.println("drop date value: "+dropbilldate);
        browser.selectfromDropBox("id", AccPageProperties.getProperty("AccountOverviewPage.Billdate"),dropbilldate);
        
       // browser.selectfromDropBox("id", AccPageProperties.getProperty("AccountOverviewPage.Billdate").replace("i", date),dropbilldate);
        Report.updateTestLog("date Selected", "PASS");
    	}else{
    		 Report.updateTestLog("Date dropdown Object is not present", "FAIL");
    	}
    	orderDate=new OnlineDBConnector().DBsysdate();
    	System.out.println("DB Data - "+orderDate);
    } 
    
    
    public static void main(String[] args){
    /*	String dropbilldate=AccPageProperties.getProperty("AccountOverviewPage.Billdatevalue").replace("i", "3");*/
    	
    	System.out.println("Drop Date");
    }
    
    
    public void clickSubmitButton(){
    	verifyAndClick(AccPageProperties.getProperty("AccountOverviewPage.ChangeBillDateSubmitButton"), "Change Bill Date Submit");
    
    }
    
    public void validateChangeBillDate(){
    	if(browser.isElementVisible(AccPageProperties.getProperty("AccountOverviewPage.DateChangedMsg"))){
    		String actualMsg=browser.getText(AccPageProperties.getProperty("AccountOverviewPage.DateChangedMsg"));
    		String expectedmsg="You have changed your billing date";
    		System.out.println("Acutal Msg= "+actualMsg);
    		if(expectedmsg.equals(actualMsg)){
    			Report.updateTestLog("Date Has been changed - confirmation Msg exists: "+actualMsg, "PASS");
    			}else{
    		Report.updateTestLog("Date Has not been changed - confirmation Msg does not exists: "+actualMsg, "FAIL");
    			}
    	}
    	if(browser.isElementVisibleWithXpath(AccPageProperties.getProperty("AccountOverviewPage.DateChangedErrorMsg")))
    	{
    		String errorText=browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.DateChangedErrorMsg"));
    		System.out.println("Date Change error Message: "+errorText);
    		Report.updateTestLog("Date not changed as  the selected date and previous dates are same error Text- "+errorText, "FAIL");
    	}
    	if(browser.isElementVisibleWithXpath(AccPageProperties.getProperty("AccountOverviewPage.DateChanged3TimesErrorMsg"))){
    		String errorText=browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.DateChanged3TimesErrorMsg"));
    		System.out.println("Crossed the limit Date Change error Message: "+errorText);
    		Report.updateTestLog("Crossed the limit to change the date. Error Text- "+errorText, "FAIL");
    		}
    	 }
    
    
    public void validateChangeBillDate1(){
    	if(browser.isElementVisible(AccPageProperties.getProperty("AccountOverviewPage.DateChangedMsg"))){
    		String actualMsg=browser.getText(AccPageProperties.getProperty("AccountOverviewPage.DateChangedMsg"));
    		String expectedmsg="You have changed your billing date";
    		System.out.println("Acutal Msg= "+actualMsg);
    	/*	if(expectedmsg.equals(actualMsg)){
    			Report.updateTestLog("Date Has been changed - confirmation Msg exists: "+actualMsg, "PASS");
    			}else{
    		Report.updateTestLog("Date Has not been changed - confirmation Msg does not exists: "+actualMsg, "FAIL");
    			}*/
    	}
    	if(browser.isElementVisibleWithXpath(AccPageProperties.getProperty("AccountOverviewPage.DateChangedErrorMsg")))
    	{
    		String errorText=browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.DateChangedErrorMsg"));
    		System.out.println("Date Change error Message: "+errorText);
    		Report.updateTestLog("Date not changed as the selected date and previous dates are same error Text- "+errorText, "FAIL");
    	}
    	if(browser.isElementVisibleWithXpath(AccPageProperties.getProperty("AccountOverviewPage.DateChanged3TimesErrorMsg"))){
    		String errorText=browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.DateChanged3TimesErrorMsg"));
    		System.out.println("Crossed the limit Date Change error Message: "+errorText);
    		Report.updateTestLog("Crossed the limit to change the date. Error Text- "+errorText, "PASS");
    		}
    	 }
    
    public void clickReturnToAccSummary(){
    	if(browser.isElementVisibleWithXpath(AccPageProperties.getProperty("AccountOverviewPage.ReturnToAccSummaryLink"))){
    		verifyAndClickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.ReturnToAccSummaryLink"), "Return to Acc Summary Link");
    	}else{
    	Report.updateTestLog("Account Summary link is not present", "WARN");
    		}
    	
    }
    
        
    public void ChangebillDateDB(UserProfile userProfile){
    	 String ucrn=userProfile.getUCRN();
    String changeBillAuditDetails=new OnlineDBConnector().changebillDateDB(orderDate,ucrn);
   	System.out.println(changeBillAuditDetails);	   	
   	Report.updateTestLog("Audit Details for change Bill date "+changeBillAuditDetails, "DONE");
    }
       
    
    public void viewLatestStatement(){
    	verifyAndClickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.SeeLatestStatement"), "see Latest Statement Link");
    }
    
    public void validateFirstBillNot(){
    	if(browser.isElementVisibleWithXpath(AccPageProperties.getProperty("AccountOverviewPage.FirstBillPromo"))){
    		String text=browser.getTextByXpath(AccPageProperties.getProperty("AccountOverviewPage.FirstBillPromo"));
    		Report.updateTestLog("First Bill Promo Exixts: "+text, "PASS");
    		System.out.println("First Bill Promo Exixts: "+text);
    	}else{
    		Report.updateTestLog("First Bill Promo does not Exixts", "FAIL");
    	}
    }
    
    
    public void verifyFirstBillNotification(){
    	if(browser.isElementVisibleWithXpath(AccPageProperties.getProperty("AccountOverviewPage.FirstBillNotificationText"))){
    		Report.updateTestLog("First Bill Notification - that the first bill may be larger than usual exists", "PASS");
    	}else{
    		Report.updateTestLog("First Bill Notification does not exists", "FAIL");
    	}
    }
    
    public void clickBillHistory(){
    	verifyAndClickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.BillHistory"), "Bill History");
    }
    
    public void clickLatestBill(){
    	verifyAndClickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.LatestBill"), "Latest Bill");
    }
    
     
    public void clickOtherBill(){
    	verifyAndClickWithXpath(AccPageProperties.getProperty("AccountOverviewPage.NotLatestBill"), "Not Latest Bill");
    }
    
    public void openSMBUrl(){
    	browser.open(ApplicationConfig.APP_BG_URL+"/smartmonthlybilling");
    }
        
    public void selectAddress(){
    	browser.clickWithXpath(".//*[@id='supplyAddress']");
    	browser.clickWithXpath(".//*[@id='supplyAddress']/option[2]");
    	
    }
    

    
    	
    }	
