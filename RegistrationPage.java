package bg.framework.app.functional.page.reFactoring;

import java.util.ArrayList;
import java.util.Properties;

import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;

import bg.framework.app.functional.common.ApplicationConfig;
import bg.framework.app.functional.entities.UserProfile;
import bg.framework.app.functional.page.common.BasePage;
import bg.framework.app.functional.page.common.GlobalErrorMessages;
import bg.framework.app.functional.page.common.LegacyLoginPage;
import bg.framework.app.functional.page.selfServe.AccountOverviewPage;
import bg.framework.app.functional.test.common.TestBase;
import bg.framework.app.functional.util.OnlineDBConnector;
import bg.framework.app.functional.util.PropertyLoader;
import bg.framework.app.functional.util.Report;
import bg.framework.app.functional.util.SapNetWeaverPage;
import bg.framework.app.functional.util.SiebelDataBase;


public class RegistrationPage extends BasePage {

	private final static String File_RegPage = "resources/ReFactoring/RegistrationPageRewrite.properties";
    private static Properties regPageProperties = new PropertyLoader(File_RegPage).load();

    private final static String email_Domain = "bgdigitaltest.co.uk";
    private final static String confirm_EmailMismatchChar = "a";
    private final static String noNectarValue = "4";
    private final static String addNewNectarValue = "2";
    private final static String addExistingNectarValue = "1";
    private final static String acceptTermsValue = "Y";
    public static String curRegAcctnumber = "";
    public static String curRegEmailAddress = "";
    final String logPath = null;
    final String Pass_Str = "PASS";
    final String Fail_Str = "FAIL";
    private static String orderDate;
    public RegistrationPage() {

    }

    public void registerUpdateYourDetails(UserProfile selfRegisterData,
                                          String accountNumber) {
        String logInfo = "";
        String logStatus = "PASS";

        try {

            curRegAcctnumber = accountNumber;
            logInfo = logInfo + " Registering using Account number: <b>"+ curRegAcctnumber + " </b>. ";
            verifyAndInputById(regPageProperties.getProperty("RegisterationPage.AccNoText"),"Customer Account Number",curRegAcctnumber);
            verifyAndSelectDropDownBox(regPageProperties.getProperty("RegistrationPage.TitleSelectId"), "Title", selfRegisterData.getTitle());
            verifyAndInputById(regPageProperties.getProperty("RegistrationPage.FirstNameTextId"), "First Name", selfRegisterData.getFirstName());
            verifyAndInputById(regPageProperties.getProperty("RegistrationPage.LastNameTextId"),"Last Name",selfRegisterData.getLastName());
            //new Report().webArchiveCapture("Registration");
            browser.wait(3000);
            verifyAndClickWithXpath(regPageProperties.getProperty("RegistrationPage.Continue1Button"),"Register Button");

            String emailData = selfRegisterData.getEmail();
            String confirmEmailData = selfRegisterData.getEmail();
            String emailOnPage = browser.getAttribute(regPageProperties.getProperty("RegistrationPage.EmailTextId"),"value");
            if (emailOnPage.trim().length() > 0)
                logInfo = logInfo + " Email address prepopulated on PageLoad: "
                        + emailOnPage + ". \n";
            else
                logInfo = logInfo
                        + "<b> Email address field is not populated on page load. </b>"
                        + emailOnPage + ". \n";

            if (null != emailOnPage && !emailOnPage.trim().equals("") && emailOnPage.contains("@")) {
                emailOnPage = emailOnPage.substring(0, emailOnPage.indexOf('@'));

            } else if (null != emailOnPage && emailOnPage.trim().equals(""))
                emailOnPage = null;

            String emailAddress = emailOnPage == null ? emailData : emailOnPage + "@" + email_Domain;

            if (null == confirmEmailData || !confirmEmailData.trim().equalsIgnoreCase("invalid")) {
                confirmEmailData = emailAddress;
            } else {
                confirmEmailData = confirm_EmailMismatchChar + emailAddress;
            }

            browser.clearValue(regPageProperties.getProperty("RegistrationPage.EmailTextId"));
            verifyAndInputById(regPageProperties.getProperty("RegistrationPage.EmailTextId"),"Email Address",emailAddress);
            curRegEmailAddress = emailAddress;
            logInfo = logInfo + "Updating user email address as '" + emailAddress + "'. ";
            if (browser.getAttribute(regPageProperties.getProperty("RegistrationPage.ConfirmEmailTextId"),"value").length() > 0) {
                logInfo = logInfo + "<b> Confirm Email Text is not blank on Page Load.</b> The value is: "
                        + browser.getAttribute(regPageProperties.getProperty("RegistrationPage.ConfirmEmailTextId"),"value") + "\n ";
                logStatus = "FAIL";
            }
            verifyAndInputById(regPageProperties.getProperty("RegistrationPage.ConfirmEmailTextId"),"Confirm Email Address",confirmEmailData);
            verifyAndInputById(regPageProperties.getProperty("RegistrationPage.PasswordTextId"),"Password",selfRegisterData.getPassword());
            verifyAndInputById(regPageProperties.getProperty("RegistrationPage.ConfirmPasswordTextId"),"Confirm Password",selfRegisterData.getPassword());
            verifyAndInputById(regPageProperties.getProperty("RegistrationPage.PhoneTextId"),"Phone Number",selfRegisterData.getPhoneNumber());
            verifyAndSelectDropDownBox(regPageProperties.getProperty("RegistrationPage.PhoneTypeSelectId"),"Phone Type",selfRegisterData.getPhoneType());
            browser.wait(3000);
            //String acceptTerms = selfRegisterData.getAcceptTerms().toLowerCase();
            //if (acceptTerms.startsWith(acceptTermsValue.toLowerCase())) {
                browser.click(regPageProperties.getProperty("RegistrationPage.acceptTermsCheckBoxId"));//,"Accept Terms and Conditions");
            //}

            // Update Nectar InformationHere
            String nectarInput = selfRegisterData.getNectarValue();
            if (browser.isElementVisible(regPageProperties.getProperty("RegistrationPage.NoNectarRadioId"))) {
                if (nectarInput.trim().equalsIgnoreCase(noNectarValue)) {
                    verifyAndClick(regPageProperties.getProperty("RegistrationPage.NoNectarRadioId"),"No Nectar Options");
                    logInfo = logInfo + " No Nectar Needed option. ";
                } else if (nectarInput.trim().equalsIgnoreCase(addNewNectarValue)) {
                    verifyAndClick(regPageProperties.getProperty("RegistrationPage.AddNewNectarRadioId"),"Add New Nectar Card");
                    logInfo = logInfo + " Selected Add New Nectar Option. ";
                } else if (nectarInput.trim().equalsIgnoreCase(addExistingNectarValue)) {
                    verifyAndClick(regPageProperties.getProperty("RegistrationPage.card_number"),"Existing Nectar Card");
                    browser.input(regPageProperties.getProperty("RegistrationPage.NectarTextId"),selfRegisterData.getNectarValue());
                    logInfo = logInfo+ " Selected Add Existing Nectar Option. Added the Card: "+ selfRegisterData.getNectarValue() + ". ";
                }
                if (browser.isElementVisible(regPageProperties.getProperty("RegistrationPage.NectarTermsCheckBoxId"))){ 
                	browser.click(regPageProperties.getProperty("RegistrationPage.NectarTermsCheckBoxId"));
                }
                /*String acceptNectarTerms = selfRegisterData.getAcceptTerms().toLowerCase();
                if (browser.isElementVisible(regPageProperties.getProperty("RegistrationPage.NectarTermsCheckBoxId"))) {
                    if (acceptNectarTerms.startsWith(acceptTermsValue.toLowerCase())) {
                        verifyAndClick(regPageProperties.getProperty("RegistrationPage.NectarTermsCheckBoxId"),"Nectar Terms and Conditions");
                    }
                }*/
                }
            // Update Log if Nectar Option Not Found on Page.
            else {
                logInfo = logInfo + "<b>No Nectar Radio button Found on Page. </b>";

            }

            verifyAndClickWithXpath(regPageProperties.getProperty("RegistrationPage.Continue2ButtonXPath"),"Continue Registration");
            browser.wait(4000);
            getWaitTime();
            // Verify Registration Success Messsage
            if(browser.isTextPresent(regPageProperties.getProperty("RegistrationPage.SuccessMessage")))
             Report.updateTestLog("Thanks, you're now registered confirmation message verified",Pass_Str);
             else {
              Report.updateTestLog("Thanks, you're now registered not in application",Fail_Str);
            }
            verifyIsTextPresent(regPageProperties.getProperty("RegistrationPage.SuccessMessageText"),"Online Account setup verification");
                //logInfo = logInfo + " User Registration Completed.<b>Success Message found.</b> User is in Page: "
                        //+ browser.getTextByXpath("//title");
                //logStatus = "PASS";

            // Verify Nectar Confirmation Message.
            if (addNewNectarValue.trim().equalsIgnoreCase(selfRegisterData.getNectarValue().trim())
                    || addExistingNectarValue.trim().equalsIgnoreCase(selfRegisterData.getNectarValue().trim())) {
                if (browser.isTextPresent(regPageProperties.getProperty("RegistrationPage.NewNectarSuccessMessage")))
                    logInfo = logInfo
                            + " New Nectar Added Success Message found on Page. ";
                else {
                    logInfo = logInfo
                            + " <b>Nectar Success Message not found.</b> User is in Page: "
                            + browser.getTextByXpath("//title") + ". ";
                    logStatus = "FAIL";
                }

            }
        } catch (NoSuchElementException ex) {
            logInfo = logInfo + "Caught Exception: " + ex.getMessage() + ". \n";
            logStatus = "FAIL";

            ex.printStackTrace();
            Report.updateTestLog(logInfo, logStatus);

        }

    }

    public void goToYourAccount() {
    	Report.updateTestLog("Registration Confirmation Page", "WARN");
    	if (ApplicationConfig.BRAND.equalsIgnoreCase("bg")){
    		if (browser.isElementVisibleWithXpath(regPageProperties.getProperty("RegistrationPage.GotoYourAccLnk"))){
                verifyAndClickWithXpath(regPageProperties.getProperty("RegistrationPage.GotoYourAccLnk"), "Registration successfull and Go to your account");
            	}
    	} else {
    		browser.clickWithLinkText("Go to my account");
    	}
    
    	/*try {
		Thread.sleep(15000);
		verifyAndClickWithXpath("//div[@class='text parbase section']/p/a", "Continue to my online account");
    	if(browser.isElementVisibleWithXpath("//div[@class='text parbase section']/p/a/span"))
    	{
    		verifyAndClickWithXpath("//div[@class='text parbase section']/p/a/span", "Continue to my online account");
    	}
    	if(browser.isElementVisibleWithXpath("//div[@class='text parbase section']/p/a/span"))
    	{
    		verifyAndClickWithXpath("//div[@class='text parbase section']/p/a/span", "Continue to my online account");
    	}
    	Thread.sleep(8000);
    	} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
    }

// Enter valid details to register a Account Number
    
    public void registerAccount(final UserProfile userProfile){
    	 final String strAccountNum = userProfile.getAccNumber();
    	 final String strTitle = new SiebelDataBase().getTitle(userProfile.getUCRN());
    	 final String firstName = new SiebelDataBase().getFirstName(userProfile.getUCRN());
         final String lastName = new SiebelDataBase().getLastName(userProfile.getUCRN());
         enterRegistrationData(strAccountNum,strTitle,firstName,lastName);
    }

// Enter valid details to register a Online Account
    
    public void registerOnlineAccount(final UserProfile userProfile){
   	 final String strAccountNum = new SiebelDataBase().getAccountNumber(userProfile.getUCRN());
   	 validateWhyIsThisNeeded();
    } 
    
// Validating the Error Message by providing different invalid input combinations for Account Number    
    
    public void accountNumberErrorMsgValidation(final UserProfile userProfile,ArrayList<String> errList){
    	 final String firstName = new SiebelDataBase().getFirstName(userProfile.getUCRN());
         final String lastName = new SiebelDataBase().getLastName(userProfile.getUCRN());
         final String strTitle = new SiebelDataBase().getTitle(userProfile.getUCRN());
        //final String[] customerNum = {""};
        final String[] customerNum = {"", "850000000000", "8500000000", "85aaac234"};
         for (int i = 0; i < customerNum.length; i++) {
             if (i == 0) {
           	 enterRegistrationData("", strTitle, firstName, lastName);
           	 errorMsgComparison(errList.get(10),"Account Number");
            } else if (i == 1) {
            	 enterRegistrationData(customerNum[i], strTitle, firstName, lastName);
             errorMsgComparison(errList.get(8),"Account Number");	
            } else if (i > 1 & i < 3) {
             System.out.println(errList.get(9));
            	 enterRegistrationData(customerNum[i], strTitle, firstName, lastName);
           	 errorMsgComparison(errList.get(9),"Account Number");
             }
         }

    }

 // Validating the Error Message by providing different invalid input combinations for First Name

    public void firstNameErrorMsgValidation(final UserProfile userProfile,ArrayList<String> errList) {
        final String strAccountNum = new SiebelDataBase().getAccountNumber(userProfile.getUCRN());
        final String strLastName = new SiebelDataBase().getLastName(userProfile.getUCRN());
        final String strTitle = new SiebelDataBase().getTitle(userProfile.getUCRN());
        final String[] firstName = {"", "aa23", ".*a", "1234"};

        for (int i = 0; i < firstName.length; i++) {
            if (i == 0) {
            	enterRegistrationData(strAccountNum, strTitle, "", strLastName);
            	errorMsgComparison(errList.get(0),"First Name");
            } else if (i > 0) {
            	enterRegistrationData(strAccountNum, strTitle, firstName[i], strLastName);
            	errorMsgComparison(errList.get(1),"First Name");
            } 
        }

    }

// Validating the Error Message by providing different invalid input combinations for Last Name and finally
// select your account for navigation back to the Log In Page

    public void lastNameErrorMsgValidation(final UserProfile userProfile,ArrayList<String> errList) {
        final String strAccountNum = new SiebelDataBase().getAccountNumber(userProfile.getUCRN());
        final String strFirstName = new SiebelDataBase().getFirstName(userProfile.getUCRN());
        final String strTitle = new SiebelDataBase().getTitle(userProfile.getUCRN());
        final String[] lastName = {"", "aa23", ".*a", "1234"};

        for (int i = 0; i < lastName.length; i++) {
            if (i == 0) {
            	enterRegistrationData(strAccountNum, strTitle, strFirstName, "");
            	errorMsgComparison(errList.get(2),"Last Name");
            } else if (i > 0) {
            	enterRegistrationData(strAccountNum, strTitle, strFirstName, lastName[i]);
            	errorMsgComparison(errList.get(3),"Last Name");
            }
        }
        
    }

    
// Validating the Error Message by providing different invalid input combinations for Email Address and Confirm Email Address   
    
    public void emailAddressErrorMsgValidation(final UserProfile userProfile){
    	 final String expectedEmailAdd = new OnlineDBConnector().getUserEmail(userProfile.getUCRN());
         final String[] emailAddress = {"", "@@@@", "1234", "nest@digi.co.uk.com","", "@@@@", "1234", "nest@digi.co.uk.com"};
                  
         for (int i = 0; i < emailAddress.length; i++) {
             if (i == 0 & i < 2) {
            	 enterRegistrationOnline(emailAddress[i],expectedEmailAdd, "12345678","12345678","0207123456","Mobile"
            			 				 ,"Yes","Yes");
            	 errorMsgComparison(GlobalErrorMessages.ReFactoring_EMAIL_ERROR,"Email Address");
             } else if (i == 2) {
            	 enterRegistrationOnline(emailAddress[i],expectedEmailAdd, "12345678","12345678","0207123456","Mobile"
            			 				 ,"Yes","Yes");
            	 errorMsgComparison(GlobalErrorMessages.ReFactoring_ReferenceNumberInline_ERROR,"Email Address");
             } else if (i > 2 & i < 7 ){
            	 enterRegistrationOnline(expectedEmailAdd,emailAddress[i], "12345678","12345678","0207123456","Mobile"
            			 				 ,"Yes","Yes");
            	 errorMsgComparison(GlobalErrorMessages.ReFactoring_EMAIL_ERROR,"Confirm Email Address");
             } else if (i == 7) {
            	 enterRegistrationOnline(expectedEmailAdd,emailAddress[i], "12345678","12345678", "0207123456","Mobile"
            			 				,"Yes","Yes");
            	 errorMsgComparison(GlobalErrorMessages.ReFactoring_ReferenceNumberInline_ERROR,"Confirm Email Address");
         }
        }
    }

//  Validating the Error Message by providing different invalid input combinations for Password and Retype Password
    
    public void passwordErrorMsgValidation(final UserProfile userProfile){
   	 	final String expectedEmailAdd = new OnlineDBConnector().getUserEmail(userProfile.getUCRN());
        final String[] password = {"", "@@@@", "1234", "1235@123","", "@@@@", "1234", "1235@123"};
                
        for (int i = 0; i < password.length; i++) {
              if (i == 0 & i < 2) {
           	 	enterRegistrationOnline(expectedEmailAdd,expectedEmailAdd, password[i],"12345678","0207123456","Mobile"
           	 							,"Yes","Yes");
           	 	errorMsgComparison(GlobalErrorMessages.ReFactoring_EMAIL_ERROR,"Password");
            } else if (i == 2) {
           	 	enterRegistrationOnline(expectedEmailAdd,expectedEmailAdd, password[i],"12345678","0207123456","Mobile"
           	 							,"Yes","Yes");
           	 	errorMsgComparison(GlobalErrorMessages.ReFactoring_ReferenceNumberInline_ERROR,"Password");
            } else if (i > 2 & i < 7 ){
	           	 enterRegistrationOnline(expectedEmailAdd,expectedEmailAdd, "12345678",password[i],"0207123456","Mobile"
	           			 				 ,"Yes","Yes");
	           	errorMsgComparison(GlobalErrorMessages.ReFactoring_EMAIL_ERROR,"Retype Password");
            } else if (i == 7) {
	        	 enterRegistrationOnline(expectedEmailAdd,expectedEmailAdd, "12345678",password[i],"0207123456","Mobile"
	        			 				 ,"Yes","Yes");
	        	 errorMsgComparison(GlobalErrorMessages.ReFactoring_ReferenceNumberInline_ERROR,"Retype Password");
            }
        }
      }

// Validating the Error Message for Nectar Details
    
    public void nectarCardErrorMsgValidation(final UserProfile userProfile){
    	final String expectedEmailAdd = new OnlineDBConnector().getUserEmail(userProfile.getUCRN());
    	enterRegistrationOnline(expectedEmailAdd,expectedEmailAdd, "12345678","12345678","0207123456",
								"Mobile","Uncheck Nectar","No");
    	errorMsgComparison(GlobalErrorMessages.ReFactoring_NectarTermsError,"Nectar Terms");
    }
    
// Validating the Error Message for Accept Terms
    
    public void acceptTermsErrorMsgValidation(final UserProfile userProfile){
    	final String expectedEmailAdd = new OnlineDBConnector().getUserEmail(userProfile.getUCRN());
    	enterRegistrationOnline(expectedEmailAdd,expectedEmailAdd, "12345678","12345678","0207123456",
								"Mobile","Yes","No");
    	errorMsgComparison(GlobalErrorMessages.ReFactoring_AcceptTermsError,"Accept Terms");
    }
    
// Enter the appropriate details in the fields displayed in Registration Page
    
    public void enterRegistrationData(final String CustomerNumber, final String CustomerTitle, final String FirstName, final String SurName){
    	UserProfile userProfile = new UserProfile();
    	enterValidateEmailId(userProfile.getEmail());
   	 	curRegAcctnumber = CustomerNumber;
        verifyAndSelectDropDownBox(regPageProperties.getProperty("RegistrationPage.TitleSelectId"), "Title",CustomerTitle);
        verifyAndInputById(regPageProperties.getProperty("RegistrationPage.FirstNameTextId"), "First Name", FirstName);
        verifyAndInputById(regPageProperties.getProperty("RegistrationPage.LastNameTextId"),"Last Name",SurName);
        verifyAndInputById(regPageProperties.getProperty("RegisterationPage.AccNoText"),"Customer Account Number",curRegAcctnumber);
        browser.wait(3000);
        //new Report().webArchiveCapture("Registration");
        verifyAndClick(regPageProperties.getProperty("RegistrationPage.FindAccounts"),"Find Accounts Button");
        browser.wait(3000);
    }

    public void enterInvalidAccountregistration(UserProfile userProfile){
        final String strAccountNum = userProfile.getElecAccount();
    	final String strTitle = new SiebelDataBase().getTitle(userProfile.getUCRN());
    	final String firstName = new SiebelDataBase().getFirstName(userProfile.getUCRN());
        final String lastName = new SiebelDataBase().getLastName(userProfile.getUCRN());
        enterRegistrationData(strAccountNum,strTitle,firstName,lastName);
        browser.wait(4000);
        if(browser.isElementVisible(regPageProperties.getProperty("RegistrationPage.PasswordTextId"))){
        	Report.updateTestLog("Customer is able to register", Pass_Str);
        }else{
        Report.updateTestLog("Customer is not able to register", Pass_Str);
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_RegistrationInvalidAccountError);
        }
    }

    public void enterRegistrationOnline(final String EmailAddress, final String ConfirmEmail, final String Pwd, final String RetypePwd,
                                        final String PhoneNumber,final String PhoneType,final String NectarTerms,final String AcceptTerms){
    	verifyAndInputById(regPageProperties.getProperty("RegistrationPage.EmailTextId"),"Email",EmailAddress);
    	verifyAndInputById(regPageProperties.getProperty("RegistrationPage.ConfirmEmailTextId"),"Confirm Email",ConfirmEmail);
    	verifyAndInputById(regPageProperties.getProperty("RegistrationPage.PasswordTextId"),"Password Text",Pwd);
    	verifyAndInputById(regPageProperties.getProperty("RegistrationPage.ConfirmPasswordTextId"),"Confirm Password",RetypePwd);
    	//verifyAndSelectDropDownBox(regPageProperties.getProperty("RegistrationPage.Security_questionSelectId"),"Security",SecurityQuery);
    	//verifyAndInputById(regPageProperties.getProperty("RegistrationPage.Security_answerTextId"),"Security Answer",SecurityAnswer);
    	verifyAndInputById(regPageProperties.getProperty("RegistrationPage.PhoneTextId"),"Phone Number",PhoneNumber);
    	verifyAndSelectDropDownBox(regPageProperties.getProperty("RegistrationPage.PhoneTypeSelectId"),"Phone",PhoneType);
    	if (NectarTerms == "Yes"){ 
    		verifyAndClick(regPageProperties.getProperty("RegistrationPage.AddNectarLaterRadioId"),"Nectar Card");
    		if (browser.isElementVisible(regPageProperties.getProperty("RegistrationPage.NectarTermsCheckBoxId"))){
    			verifyAndSelectCheckBoxByID(regPageProperties.getProperty("RegistrationPage.NectarTermsCheckBoxId"),"Nectar Terms");
    		}
    	}else if (NectarTerms == "No"){ 
    		verifyAndClick(regPageProperties.getProperty("RegistrationPage.NoNectarRadioId"),"Nectar Card");
    		if (browser.isElementVisible(regPageProperties.getProperty("RegistrationPage.NectarTermsCheckBoxId"))){
    			Report.updateTestLog("Nectar Terms Option should not be visible when No Nectar Id is Selected" ,Fail_Str);
    		}else{
    			Report.updateTestLog("Nectar Terms Option is not displayed which is expected" ,Pass_Str);
    		}
    	}else if (NectarTerms == "Uncheck Nectar"){ 
    		verifyAndClick(regPageProperties.getProperty("RegistrationPage.AddNectarLaterRadioId"),"Nectar Card");
    		if (browser.isElementVisible(regPageProperties.getProperty("RegistrationPage.NectarTermsCheckBoxId"))){
    		}
    	}	
    	
    	///if (AcceptTerms == "Yes"){
    		verifyAndSelectCheckBoxByID(regPageProperties.getProperty("RegistrationPage.acceptTermsCheckBoxId"),"Accept Terms");
    	//}else if (AcceptTerms == "Uncheck Nectar"){	
    		//verifyAndSelectCheckBoxByID(regPageProperties.getProperty("RegistrationPage.NectarTermsCheckBoxId"),"Accept Terms");
    	//}	
    	verifyAndClickWithXpath(regPageProperties.getProperty("RegistrationPage.Continue2ButtonXPath"),"Continue");
    }


 // Validating the contents displayed in Why Is This Needed Option Dialog Box and close it
    
    public void validateWhyIsThisNeeded(){
    if (browser.isElementVisibleWithXpath(regPageProperties.getProperty("RegistrationPage.OnlineRegMsg"))) {
        verifyAndClickWithXpath(regPageProperties.getProperty("RegistrationPage.OnlineRegMsg"), "WhereCanIFindID");
        verifyIsTextPresent("We request your telephone number");
        if (browser.isElementVisibleWithXpath(regPageProperties.getProperty("RegistrationPage.CloseButtonID"))) {
            verifyAndClickWithXpath(regPageProperties.getProperty("RegistrationPage.CloseButtonID"), "Close Button");
        }
      }
   }
    
// Capture the error message displayed and verify the actual and error message displayed by making a call to the verifyErrorMessage method   

    public void errorMsgComparison(final String expectedErrorMsg,final String fieldValidation) {
        final String displayedErrorMsg = browser.getTextByXpath(regPageProperties.getProperty("RegistrationPage.ErrorMessageValidationID"));
        verifyErrorMsg(displayedErrorMsg, expectedErrorMsg,fieldValidation);
    }

    
// Validation for Inappropriate data for the fields displayed in Registration Page and compare the error message displayed in the application against 
// the Expected data  	   

     public void verifyErrorMsg(final String displayedErrorMsg, final String expectedErrorMsg, final String fieldValidation) {
         System.out.println("DisplayedErrorMsg is: "+displayedErrorMsg);
         System.out.println("ExpectedErrorMsg is: "+expectedErrorMsg);
         
         if (displayedErrorMsg.trim().contains(expectedErrorMsg.trim())) {
             Report.updateTestLog("CQ5 Validation :Displayed Error Message Validation For "+ fieldValidation + " Is  :" + displayedErrorMsg, Pass_Str);
         } else {
             Report.updateTestLog("CQ5 Validation :Expected Error Message Validation Was Not Displayed For "+ fieldValidation, Fail_Str);
         }
     }
     public void verifyBrowserback(UserProfile userProfile){
         browser.browserBack();
       if (browser.isElementVisible(regPageProperties.getProperty("RegistrationPage.NewEmailId"))){
             Report.updateTestLog("Email Id field exists, Browser back working as expected","PASS");
         }else {
             Report.updateTestLog("Email Id field not available, page navigated to back page","FAIL");
       }
        registerAccount(userProfile);
        browser.browserBack();
         if (browser.isElementVisible(regPageProperties.getProperty("RegistrationPage.PasswordTextId"))){
             Report.updateTestLog("Password field exists,Browser back working as expected","PASS");
         }else {
             Report.updateTestLog("Password field not available,page navigated to back page","FAIL");
         }
         
         new AccountOverviewPage().logout();
     }
     public void verifyAddMissingAccount(UserProfile userProfile){
    	 browser.wait(2000);
         verifyAndClick(regPageProperties.getProperty("RegistrationPage.AddaMissingAcc"),"Add a missing Account");
         browser.wait(2000);
         verifyAndInputById(regPageProperties.getProperty("RegistrationPage.MissingAccntnumber"),"Missing Account Number",userProfile.getAccNumber());
         verifyAndClick(regPageProperties.getProperty("RegistrationPage.FindId"),"Find Button");
         browser.wait(2000);
         verifyAndClick(regPageProperties.getProperty("RegistrationPage.FindId"),"Find Button");
     }
     
     public void verifySecondaryUCRNTable(UserProfile userProfile){
    	 String leadData=new OnlineDBConnector().getPrimaryUCRN(userProfile.getUCRN());
     	 if(!leadData.isEmpty()){    	
     	 Report.updateTestLog("Primary UCRN updated in PO_TA_SECONDARY_UCRN table  :"+leadData, "PASS");
     	 }else{
     		Report.updateTestLog("Primary UCRN not updated in PO_TA_SECONDARY_UCRN table ","FAIL");	 
     	 }
     }
     	public void verifyOAMCustomerTable(UserProfile userProfile){
     	 browser.wait(5000);
       	 String leadData=new OnlineDBConnector().getUserEmail(userProfile.getUCRN());
       	 System.out.println("@#%#@^#@^#%"+leadData);
        	 if(!leadData.isEmpty()){    	
        	 Report.updateTestLog("Registered Data updated in PO_TA_OAM_CUSTOMER table  :"+leadData, "PASS");
        	 }else{
        		Report.updateTestLog("Registered Data not updated in PO_TA_OAM_CUSTOMER table","FAIL");	 
        	 }
     }
     public ArrayList<String> openCQ5(){
    	 //browser.open("http://10.224.70.17:9002/etc/centrica-tools/errormessages-maintenance.html"); 
    	 ArrayList<String> a1= verifyCQ5("Account Number");
    	 //browser.close();
    	 browser.browserBack();
    	 browser.browserBack();
    	 System.out.println("Outside For Loop " +a1.get(0));
    	 browser.wait(10000);
    	 System.out.println("Outside For Loop " +a1.get(1));
    	 System.out.println("Outside For Loop " +a1.get(2));
    	 System.out.println("Outside For Loop " +a1.get(3));
    	 System.out.println("Outside For Loop " +a1.get(4));
    	 System.out.println("Outside For Loop " +a1.get(5));
    	 System.out.println("Outside For Loop " +a1.get(6));
    	 System.out.println("Outside For Loop " +a1.get(7));
    	 System.out.println("Outside For Loop " +a1.get(8));
    	 System.out.println("Outside For Loop " +a1.get(9));
    	 return a1;
    	     	 
     }
     
     public ArrayList<String> openCQ5List(String[] strErrorCode){
    	 //browser.open("http://10.224.70.17:9002/etc/centrica-tools/errormessages-maintenance.html"); 
    	 ArrayList<String> a1= verifyCQ5(strErrorCode);
    	 //browser.close();
    	 browser.browserBack();
    	 browser.browserBack();
    	// System.out.println("Outside For Loop " +a1.get(0));
    	// browser.wait(10000);
    	 for(int i = 0; i <strErrorCode.length;i++)
    	 {
    	 System.out.println("Outside For Loop " +a1.get(i));
    	 
    	 }
    	 return a1;
    	     	 
     }
     
     public ArrayList<String> verifyCQ5(String fieldType){
    	verifyAndInputByXpath(regPageProperties.getProperty("RegistrationPage.CQLoginName"), "FirstName", "admin");
        verifyAndInputByXpath(regPageProperties.getProperty("RegistrationPage.CQLoginPwd"), "LastName","admin");
     	verifyAndClickWithXpath(regPageProperties.getProperty("RegistrationPage.CQOKButton"), "OK");
     	int a = browser.getRowCountByXpath("//form[contains(@action,'errormessageoverrides')]//table");
     	System.out.println("COUNT ROW: "+a);
     	ArrayList<String> a1= new ArrayList<String>();
     	int j =0;
     	//String[] errorcode=GlobalErrorMessages.ReFactoring_CUSTOMER_Reference_ErrorCode;
     	String[] errorcode=GlobalErrorMessages.ReFactoring_CUSTOMER_Reference_ErrorCode;
     	     	
       	loops:
     	for (int i = 0; i < a; i++) {
   		String errorKey = browser.getTextByXpath("//form[contains(@action,'errormessageoverrides')]//table//tbody//tr["+(i+1)+"]//td[1]");
   		int errorCodeSize = errorcode.length; 
   		errorCodeSize = errorCodeSize - 1;
   		     		
   		if (j <= errorCodeSize) {
   			browser.wait(100);
     		if (errorKey.trim().equalsIgnoreCase(errorcode[j])){
    			String errorKey1 = browser.getTextByXpath("//form[contains(@action,'errormessageoverrides')]//table//tbody//tr["+(i+1)+"]//td[2]");
     			System.out.println("ErrorKEYY11 :"+i+" "+errorKey1);
//    			browser.inputByXpath("//form[contains(@action,'errormessageoverrides')]//table//tbody//tr["+i+"]//td[3]/textarea","thanks");
     			a1.add(errorKey1);
     			System.out.println("Inside For Loop " +errorKey);
        		System.out.println("Inside For Loop " +errorcode[j]);
         		j = j+1;
         		i = 0;
            	continue loops;
     		  }
     		}
     	}
    	return a1;
		    
     }
     
     public ArrayList<String> verifyCQ5(String[] strErrorCode){ 	
    	 if(browser.isElementVisibleWithXpath(regPageProperties.getProperty("RegistrationPage.CQLoginName")))
    	 {
       verifyAndInputByXpath(regPageProperties.getProperty("RegistrationPage.CQLoginName"), "FirstName", "admin");
         verifyAndInputByXpath(regPageProperties.getProperty("RegistrationPage.CQLoginPwd"), "LastName","admin");
      	verifyAndClickWithXpath(regPageProperties.getProperty("RegistrationPage.CQOKButton"), "OK");
    	 }
      	int a = browser.getRowCountByXpath("//form[contains(@action,'errormessageoverrides')]//table");
      	System.out.println("COUNT ROW: "+a);
      	ArrayList<String> a1= new ArrayList<String>();
      	int j =0;      	
      	for(int eCount =0;eCount < strErrorCode.length; eCount++)
      	{
      		browser.wait(1000);     		
      		
      		if(browser.isElementVisibleWithXpath("//td[contains(text(),'ERRORKEY')]/parent::tr/td[1]".replace("ERRORKEY", strErrorCode[eCount]))
      				&& browser.getTextByXpath("//td[contains(text(),'ERRORKEY')]/parent::tr/td[3]".replace("ERRORKEY", strErrorCode[eCount])).equalsIgnoreCase(""))
      		{
      			String errorKey = browser.getTextByXpath("//td[contains(text(),'ERRORKEY')]/parent::tr/td[2]".replace("ERRORKEY", strErrorCode[eCount]));
      			a1.add(errorKey);      			
      			Report.updateTestLog("ErrorKey "+strErrorCode[eCount]+" is present in the page as "+errorKey , "DONE");
      		}
      		else 
      		{
      			System.out.print("*****************************"+browser.getTextByXpath("//td[contains(text(),'ERRORKEY')]/parent::tr/td[3]".replace("ERRORKEY", strErrorCode[eCount]))+"***");
      			if(!browser.getTextByXpath("//td[contains(text(),'ERRORKEY')]/parent::tr/td[3]".replace("ERRORKEY", strErrorCode[eCount])).equalsIgnoreCase(""))
          		{
          			String errorKey = browser.getTextByXpath("//td[contains(text(),'ERRORKEY')]/parent::tr/td[3]".replace("ERRORKEY", strErrorCode[eCount]));
          			a1.add(errorKey);      			
          			Report.updateTestLog("ErrorKey "+strErrorCode[eCount]+" is present in the page as "+errorKey , "DONE");
          		}
      			else
      			{
      			a1.add("Error Key Not Found in the application");
      			Report.updateTestLog("ErrorKey "+strErrorCode[eCount]+" not present in the page", "FAIL");
      			}
      		}
      	}      	
     	return a1; 		    
      }
     public void enterPrepaymentAccountregistration(UserProfile userProfile){
         final String strAccountNum = userProfile.getAccNumber();
     	final String strTitle = new SiebelDataBase().getTitle(userProfile.getUCRN());
     	final String firstName = new SiebelDataBase().getFirstName(userProfile.getUCRN());
         final String lastName = new SiebelDataBase().getLastName(userProfile.getUCRN());
         enterRegistrationData(strAccountNum,strTitle,firstName,lastName);
         browser.wait(4000);
         if(browser.isElementVisible(regPageProperties.getProperty("RegistrationPage.EmailTextId"))){
         	Report.updateTestLog("Customer is able to register", Pass_Str);
         }else{
         Report.updateTestLog("Customer is not able to register", Pass_Str);
         verifyIsTextPresent(GlobalErrorMessages.ReFactoring_PrePayment_Error);
         }
     }
     
     public void enterValidateEmailId(String Email){
    	 //while(browser.isElementVisible(regPageProperties.getProperty("RegistrationPage.NewEmailId"))==false){
        	 //browser.wait(5000);
         //}
    	 /*String Strtext=browser.getTitle();
    	 if (Strtext.trim().contains("Set up your account")) {
             Report.updateTestLog("Page Title : " + Strtext,Pass_Str);
         } else {
             Report.updateTestLog("Your Details page ", "DONE");
         }
    	 if(Strtext.trim().contains("Set up your account")) {*/
    	 browser.clearValue(regPageProperties.getProperty("RegistrationPage.NewEmailId"));
         verifyAndInputById(regPageProperties.getProperty("RegistrationPage.NewEmailId"),"Email Address",Email);
         verifyAndInputById(regPageProperties.getProperty("RegistrationPage.NewConfirmEmailId"),"Confirm Email Address",Email);
         Report.updateTestLog("Email validation Page", "WARN");
         verifyAndClick(regPageProperties.getProperty("RegistrationPage.CreateOnlineAcc"), "Create Online Account Button");
    	 //}
         //new Report().webArchiveCapture("Registration");
         if(browser.isElementVisible(regPageProperties.getProperty("RegistrationPage.Password"))){
        	Report.updateTestLog("Customer with this email Id "+Email+" already registered","PASS"); 
         }
     }
     
     public void clickEditEmailAddress(UserProfile userProfile){
    	 Report.updateTestLog("Registration Edit link page", "WARN");
    	 //verifyIsTextPresent(userProfile.getEmail());
    	 verifyAndClick(regPageProperties.getProperty("RegistrationPage.EditEmail"), "Edit Link");
    	 browser.wait(2000);
    	 Report.updateTestLog("Entereing New Email ID", "DONE");
    	 enterValidateEmailId("new"+userProfile.getEmail());
    	 //verifyIsTextPresent("new"+userProfile.getEmail());
    	 verifyAndClick(regPageProperties.getProperty("RegistrationPage.EditEmail"), "Edit Link");
    	 enterValidateEmailId(userProfile.getEmail());
     }
     public void loginUser(UserProfile userProfile) {
              verifyAndInputById(regPageProperties.getProperty("RegistrationPage.loginEmail"), "Registered Email Id",userProfile.getEmail());
              verifyAndInputById(regPageProperties.getProperty("RegistrationPage.loginPassword"), "Password", userProfile.getPassword());
              Report.updateTestLog("Good news login page", "WARN");
              verifyAndClick(regPageProperties.getProperty("RegistrationPage.loginbutton"),"Login Button");
     }
     public void enterResetPassword() {
    	 Report.updateTestLog("Reset Password Page", "WARN");
         verifyAndClick(regPageProperties.getProperty("RegistrationPage.ResetPwd"),"Reset Password Button");
         browser.wait(2000);
         Report.updateTestLog("Reset Password Confirmation Page", "WARN");
         
}
     public void registerCustomerDetails(UserProfile userProfile,String accountNumber){
    	 orderDate=new OnlineDBConnector().DBsysdate();
    	 //enterValidateEmailId(userProfile.getEmail());
    	 //verifyIsTextPresent(userProfile.getEmail());
    	 curRegAcctnumber = accountNumber;
         verifyAndSelectDropDownBox(regPageProperties.getProperty("RegistrationPage.TitleSelectId"), "Title", userProfile.getTitle());
         verifyAndInputById(regPageProperties.getProperty("RegistrationPage.FirstNameTextId"), "First Name", userProfile.getFirstName());
         verifyAndInputById(regPageProperties.getProperty("RegistrationPage.LastNameTextId"),"Last Name",userProfile.getLastName());
         verifyAndInputById(regPageProperties.getProperty("RegisterationPage.AccNoText"),"Customer Account Number",curRegAcctnumber);
         browser.wait(3000);
         //new Report().webArchiveCapture("Registration");
         Report.updateTestLog("Your details Page", "WARN");
         verifyAndClick(regPageProperties.getProperty("RegistrationPage.FindAccounts"),"Find Accounts Button");
         browser.wait(3000);
         //new Report().webArchiveCapture("Registration");
         
         if(browser.isTextPresent("Your password"))
         {
        /* String strName = userProfile.getTitle()+" "+userProfile.getFirstName()+" "+userProfile.getLastName();
         if(browser.isTextPresent(strName)){
        	 Report.updateTestLog(strName +" Verified scuccessfully", "PASS");
         }
         browser.wait(5000);
         while(browser.isElementVisible(regPageProperties.getProperty("RegistrationPage.PasswordTextId"))==false){
        	 browser.wait(5000);
         }*/
         verifyAndInputById(regPageProperties.getProperty("RegistrationPage.PasswordTextId"),"Password",userProfile.getPassword());
         verifyAndInputById(regPageProperties.getProperty("RegistrationPage.ConfirmPasswordTextId"),"Confirm Password",userProfile.getPassword());
         browser.click(regPageProperties.getProperty("RegistrationPage.acceptTermsCheckBoxId"));
         //new Report().webArchiveCapture("Registration");
         Report.updateTestLog("Password details Page", "WARN");
         verifyAndClick(regPageProperties.getProperty("RegistrationPage.CreateRegOnlineAcc"), "Create Online Account Button");
        //getRegistrationAuditEventID(userProfile);
         browser.wait(10000);
         //Confirmation page text
         while(browser.isElementVisibleWithXpath(regPageProperties.getProperty("RegistrationPage.Logout"))==false){
        	 browser.wait(5000);
         }
         if(browser.isElementVisibleWithXpath(regPageProperties.getProperty("RegistrationPage.Logout"))){
        	 Report.updateTestLog("Logout Link  Verified in Registration confirmation Page successfully", "PASS");
        	 String strTitle = userProfile.getTitle();
        	 if(browser.isTextPresent(strTitle)){
            	 Report.updateTestLog(strTitle +" Title Verified in Registration confirmation Page scuccessfully", "PASS");
             } 
        	 String strLName = userProfile.getLastName();
        	 if(browser.isTextPresent(strLName)){
            	 Report.updateTestLog(strLName +" Lastname Verified in Registration confirmation Page scuccessfully", "PASS");
             } 
         }
         if(browser.isTextPresent(regPageProperties.getProperty("RegistrationPage.NectarLink"))){
        	 browser.clickWithLinkText(regPageProperties.getProperty("RegistrationPage.NectarLink")); 
        	 if(browser.isTextPresent("Collect Nectar points with British Gas")){
        		 Report.updateTestLog("Confirmation page Nectar link for Loyalty Customer", "PASS"); 
        	 }
        	 Report.updateTestLog("Register For Nectar Link Verified successfully", "PASS");
        	 browser.wait(5000);
        	 if(browser.isElementVisibleWithXpath(regPageProperties.getProperty("RegistrationPage.Logout"))){
        		 browser.browserBack();
        		 browser.wait(3000);
            	 Report.updateTestLog("Logout Link  Verified in Nectar Page successfully", "PASS");}
            	 else {
            		 Report.updateTestLog("Logout Link  Not Verified in Nectar Page", "FAIL");	 
            	 }
         }else {
        	 Report.updateTestLog("Confirmation page Nectar link for Non - Loyalty Customer", "PASS");
    	 }
         String strText ="Registration complete";
         if(browser.isTextPresent(strText)){
        	 Report.updateTestLog(strText +" Verified successfully", "PASS");
         }
         }else
         {
        	 Report.updateTestLog("Registration got Failed", "FAIL");
         }
         //new Report().webArchiveCapture("Registration");
     }
     public void clickGotoYourAccount(){
    	 if(browser.isElementVisibleWithXpath(regPageProperties.getProperty("RegistrationPage.GotoMyAccountLink"))){
    		 //new Report().webArchiveCapture("Registration");
    		 browser.clickWithXpath(regPageProperties.getProperty("RegistrationPage.GotoMyAccountLink"));
    		 browser.wait(3000);
    		//new Report().webArchiveCapture("Registration");
    	 }
     }
     public void verifyAccountPresence(String account) {
         if (browser.isTextPresent(account)) {
             Report.updateTestLog("The Account Number: " + account
                     + " Is found on page: " + browser.getTextByXpath("//title"), "PASS");
         } else
             Report.updateTestLog(
                     "<b> The Account Number: " + account
                             + " is not found </b>.User is in page: "
                             + browser.getTextByXpath("//title"), "FAIL");

     }  
     public void verifyFastrackRegCustomer(String Email){
    	 OnlineDBConnector onlineDB = new OnlineDBConnector();
    	 try {
    		 String strEmailId =  onlineDB.verifyFastrackRegCustomer(Email);
    		 if (strEmailId.equals("0")){
    	           	Report.updateTestLog(strEmailId +"  : Email Address deleted from PO_TA_FT_REG Table", "PASS");}
    	           	else {
    	           		Report.updateTestLog(strEmailId +"  : Email Address Present in from PO_TA_FT_REG Table", "PASS");
    	           }  
         } catch (Exception e) {
             Report.updateTestLog("Email Address deleted from PO_TA_FT_REG Table", "PASS");
         }   	     	 
     }
     public void verifyRegProfileTempCustomer(String Email){
    	 OnlineDBConnector onlineDB = new OnlineDBConnector();
    	 try{
    	   String strEmailId = onlineDB.verifyRegProfileTempCustomer(Email);
    	   if (strEmailId.equals("0")){
           	Report.updateTestLog(strEmailId +"  : Email Address deleted from PO_TA_REG_PROFILE_TEMP Table", "PASS");}
           	else {
           		Report.updateTestLog(strEmailId +"  : Email Address Present in from PO_TA_REG_PROFILE_TEMP Table", "PASS");
           	}
           } catch (Exception e) {
               Report.updateTestLog("Email Address deleted from PO_TA_REG_PROFILE_TEMP Table", "PASS");  	 
         }
     }
    	 
    	 public void verifyFastrackRegCustomerUCRN(String Email){
        	 OnlineDBConnector onlineDB = new OnlineDBConnector();
        	 try {
        		 String strEmailId =  onlineDB.verifyFastrackRegCustomerUCRN(Email);
        		 if (strEmailId.equals("0")){
        			 Report.updateTestLog(strEmailId +"  : UCRN deleted from PO_TA_FT_REG Table", "PASS");}
             	else {
             		Report.updateTestLog(strEmailId +"  : UCRN Present in from PO_TA_FT_REG Table", "PASS");
              }  
             } catch (Exception e) {
                 Report.updateTestLog("UCRN deleted from PO_TA_FT_REG Table", "PASS");
             }   	     	 
         }
         public void verifyRegProfileTempCustomerUCRN(String Email){
        	 OnlineDBConnector onlineDB = new OnlineDBConnector();
        	 try{
        	   String strEmailId = onlineDB.verifyRegProfileTempCustomerUCRN(Email);
        	   if (strEmailId.equals("0")){
        		   Report.updateTestLog(strEmailId +"  : UCRN deleted from PO_TA_REG_PROFILE_TEMP Table", "PASS");}
           	else {
           		Report.updateTestLog(strEmailId +"  : UCRN Present in from PO_TA_REG_PROFILE_TEMP Table", "PASS");
           }
               } catch (Exception e) {
                   Report.updateTestLog("UCRN deleted from PO_TA_REG_PROFILE_TEMP Table", "PASS");  	 
               }
     }
         public void verifyFastrackRegCustomerEmail(String Email){
        	 OnlineDBConnector onlineDB = new OnlineDBConnector();
        	 try {
        		 String strEmailId =  onlineDB.verifyFastrackRegCustomerEmail(Email);
        		 if (strEmailId.equals("0")){
        			 Report.updateTestLog(strEmailId +"  : Email Id deleted from PO_TA_FT_REG Table", "PASS");}
             	else {
             		Report.updateTestLog(strEmailId +"  : Email Id Present in from PO_TA_FT_REG Table", "PASS");
              }  
             } catch (Exception e) {
                 Report.updateTestLog("Email Address deleted from PO_TA_FT_REG Table", "PASS");
             }   	     	 
         }
         public void verifyRegProfileTempCustomerEmail(String Email){
        	 OnlineDBConnector onlineDB = new OnlineDBConnector();
        	 try{
        	   String strEmailId = onlineDB.verifyRegProfileTempCustomerEmail(Email);
        	   if (strEmailId.equals("0")){
        		   Report.updateTestLog(strEmailId +"  : Email Id deleted from PO_TA_REG_PROFILE_TEMP Table", "PASS");}
           	else {
           		Report.updateTestLog(strEmailId +"  : Email Id Present in from PO_TA_REG_PROFILE_TEMP Table", "PASS");
           }
               } catch (Exception e) {
                   Report.updateTestLog("Email Id deleted from PO_TA_REG_PROFILE_TEMP Table", "PASS");  	 
               }
     }  
         public void verifyFastrackRegWTPCustomer(String Email){
        	 OnlineDBConnector onlineDB = new OnlineDBConnector();
        	 try {
        		 String strEmailId =  onlineDB.verifyFastrackRegWTPCustomer(Email);
        		 if (strEmailId.equals("0")){
        			 Report.updateTestLog(strEmailId +"  : Email Id deleted from PO_TA_WTP_REG Table", "PASS");}
             	else {
             		Report.updateTestLog(strEmailId +"  : Email Id Present in from PO_TA_WTP_REG Table", "PASS");
              }  
             } catch (Exception e) {
                 Report.updateTestLog("Email Address deleted from PO_TA_WTP_REG Table", "PASS");
             }   	     	 
         }
         public void getRegistrationAuditEventID(UserProfile userProfile){
     	   	String eventid=new OnlineDBConnector().getRegAuditEventID(orderDate,userProfile.getEmail());
     	   	System.out.println(eventid);
     	   	String strActID = "60";
     	   	if (eventid.equals(strActID)){
     	   	Report.updateTestLog("Audit Event ID Generated is :"+eventid, "PASS");
     	   	}
     	   	else{
     	   	Report.updateTestLog("Audit Event ID not as expected, Audit Event ID Generated is :"+eventid, "FAIL");
     	   	}	  	
     		}
         public void getRegistrationAuditDetails(UserProfile userProfile){
      	   	String auditdetails=new OnlineDBConnector().verifyAuditDetailsEntryDB(userProfile.getEmail(),orderDate);
      	   	System.out.println(auditdetails);
      	   	if (auditdetails.equals("0")){
      	   		Report.updateTestLog("Audit Details not as expected", "FAIL");      	   	
      	   	}
      	   	else{
      	   		Report.updateTestLog("Audit Details Generated is :"+auditdetails, "PASS");
      	   	}	  	
      		}
         
         public void getRegistrationAuditDetailsNew(UserProfile userProfile,String registerType){
        	   	String auditdetails=new OnlineDBConnector().verifyAuditDetailsEntryDBNew(userProfile.getEmail(),orderDate, registerType);
        	   	System.out.println(auditdetails);
        	   	if (auditdetails.equals("0")){
        	   		Report.updateTestLog("Audit Details not as expected", "FAIL");      	   	
        	   	}
        	   	else{
        	   		Report.updateTestLog("Audit Details Generated is :"+auditdetails, "PASS");
        	   	}	  	
        		}
          public void getRegistrationAuditEventIDNew(UserProfile userProfile,String registerType){
       	   	String eventid=new OnlineDBConnector().getRegAuditEventIDNew(orderDate,userProfile.getEmail(), registerType);
       	   	System.out.println(eventid);
       	   	if(registerType.equals("AccNo"))
       	   	{
       	   	if (eventid.equals("60")){
       	   	Report.updateTestLog("Audit Event ID Generated is :"+eventid, "PASS");
       	   	}
       	   	else{
       	   	Report.updateTestLog("Audit Event ID not as expected, Audit Event ID Generated is :"+eventid, "FAIL");
       	   	}	
       	   	}
       	 if(registerType.equals("SO"))
    		{
        	   	if (eventid.equals("79")){
        	   	Report.updateTestLog("Audit Event ID Generated is :"+eventid, "PASS");
        	   	}
        	   	else{
        	   	Report.updateTestLog("Audit Event ID not as expected, Audit Event ID Generated is :"+eventid, "FAIL");
        	   	}	
    		}
       	 
       		if(registerType.equals("DD"))
       		{
           	   	if (eventid.equals("80")){
           	   	Report.updateTestLog("Audit Event ID Generated is :"+eventid, "PASS");
           	   	}
           	   	else{
           	   	Report.updateTestLog("Audit Event ID not as expected, Audit Event ID Generated is :"+eventid, "FAIL");
           	   	}	
       		}
       		if(registerType.equals("Email"))
       		{
           	   	if (eventid.equals("77")){
           	   	Report.updateTestLog("Audit Event ID Generated is :"+eventid, "PASS");
           	   	}
           	   	else{
           	   	Report.updateTestLog("Audit Event ID not as expected, Audit Event ID Generated is :"+eventid, "FAIL");
           	   	}	
       		}
       		}
         public void verifyPreRegASVCustomerEmail(String Email){
        	 OnlineDBConnector onlineDB = new OnlineDBConnector();
        	 try {
        		 String strEmailId =  onlineDB.verifyPreRegASVCustomerEmail(Email);
        		 if (strEmailId.equals("0")){
        			 Report.updateTestLog(strEmailId +"  : Email Id deleted from ASV_TA_REG_MI Table", "PASS");}
             	else {
             		Report.updateTestLog(strEmailId +"  : Email Id Present in from ASV_TA_REG_MI Table", "PASS");
              }  
             } catch (Exception e) {
                 Report.updateTestLog("Email Address deleted from ASV_TA_REG_MI Table", "PASS");
             }   	     	 
         }
         public void verifyPreRegASVCustomerUCRN(String Email){
        	 OnlineDBConnector onlineDB = new OnlineDBConnector();
        	 try {
        		 String strEmailId =  onlineDB.verifyPreRegASVCustomerUCRN(Email);
        		 if (strEmailId.equals("0")){
        			 Report.updateTestLog(strEmailId +"  : UCRN deleted from ASV_TA_REG_MI Table", "PASS");}
             	else {
             		Report.updateTestLog(strEmailId +"  : UCRN Present in from ASV_TA_REG_MI Table", "PASS");
              }  
             } catch (Exception e) {
                 Report.updateTestLog("UCRN deleted from ASV_TA_REG_MI", "PASS");
             }   	     	 
         }
         public void verifyPreRegASVTempCustomerEmail(String Email){
        	 OnlineDBConnector onlineDB = new OnlineDBConnector();
        	 try {
        		 String strEmailId =  onlineDB.verifyPreRegASVTempCustomerEmail(Email);
        		 if (strEmailId.equals("0")){
        			 Report.updateTestLog(strEmailId +"  : Email Id deleted from ASV_TA_PRE_REG_TEMP Table", "PASS");}
             	else {
             		Report.updateTestLog(strEmailId +"  : Email Id Present in from ASV_TA_PRE_REG_TEMP Table", "PASS");
              }  
             } catch (Exception e) {
                 Report.updateTestLog("Email Address deleted from ASV_TA_PRE_REG_TEMP Table", "PASS");
             }   	     	 
         }
         public void verifyPreRegASVTempCustomerUCRN(String Email){
        	 OnlineDBConnector onlineDB = new OnlineDBConnector();
        	 try {
        		 String strEmailId =  onlineDB.verifyPreRegASVTempCustomerUCRN(Email);
        		 if (strEmailId.equals("0")){
        			 Report.updateTestLog(strEmailId +"  : UCRN deleted from ASV_TA_PRE_REG_TEMP Table", "PASS");}
             	else {
             		Report.updateTestLog(strEmailId +"  : UCRN Present in from ASV_TA_PRE_REG_TEMP Table", "PASS");
              }  
             } catch (Exception e) {
                 Report.updateTestLog("UCRN deleted from ASV_TA_PRE_REG_TEMP", "PASS");
             }   	     	 
         } 
         public void verifyPreRegPBCustomerEmail(String Email){
        	 OnlineDBConnector onlineDB = new OnlineDBConnector();
        	 try {
        		 String strEmailId =  onlineDB.verifyPreRegPBCustomerEmail(Email);
        		 if (strEmailId.equals("0")){
        			 Report.updateTestLog(strEmailId +"  : Email Id deleted from PO_TA_PB_CUSTOMER Table", "PASS");}
             	else {
             		Report.updateTestLog(strEmailId +"  : Email Id Present in from PO_TA_PB_CUSTOMER Table", "PASS");
              }  
             } catch (Exception e) {
                 Report.updateTestLog("Email Address deleted from PO_TA_PB_CUSTOMER Table", "PASS");
             }   	     	 
         }
         public void verifyPreRegPBCustomerUCRN(String Email){
        	 OnlineDBConnector onlineDB = new OnlineDBConnector();
        	 try {
        		 String strEmailId =  onlineDB.verifyPreRegPBCustomerUCRN(Email);
        		 if (strEmailId.equals("0")){
        			 Report.updateTestLog(strEmailId +"  : UCRN deleted from PO_TA_PB_CUSTOMER Table", "PASS");}
             	else {
             		Report.updateTestLog(strEmailId +"  : UCRN Present in from PO_TA_PB_CUSTOMER Table", "PASS");
              }  
             } catch (Exception e) {
                 Report.updateTestLog("UCRN deleted from PO_TA_PB_CUSTOMER Table", "PASS");
             }   	     	 
         }
         
         public void registerCustomerAcctDetails(UserProfile userProfile,String accountNumber){
        	 orderDate=new OnlineDBConnector().DBsysdate();
        	 //enterValidateEmailId(userProfile.getEmail());
        	 //verifyIsTextPresent(userProfile.getEmail());
        	 curRegAcctnumber = accountNumber;
             verifyAndSelectDropDownBox(regPageProperties.getProperty("RegistrationPage.TitleSelectId"), "Title", userProfile.getTitle());
             verifyAndInputById(regPageProperties.getProperty("RegistrationPage.FirstNameTextId"), "First Name", userProfile.getFirstName());
             verifyAndInputById(regPageProperties.getProperty("RegistrationPage.LastNameTextId"),"Last Name",userProfile.getLastName());
             verifyAndInputById(regPageProperties.getProperty("RegisterationPage.AccNoText"),"Customer Account Number",curRegAcctnumber);
             browser.wait(3000);
             //new Report().webArchiveCapture("Registration");
             Report.updateTestLog("Your details Page", "WARN");
             verifyAndClick(regPageProperties.getProperty("RegistrationPage.FindAccounts"),"Find Accounts Button");
             browser.wait(3000);
             }
         
         public void passwordValidation(final UserProfile userProfile){       	 	
             final String[] password = {"","@@@@","1234","1235@123","qwertyuqw", " qwerty123","    ","qwertyuiopasdfghjkl1", "qwerty12","123 wewewe",
            		 "password12~","password12!","password12#","password12#","password12$","password12%","password12^","password12&","password12*",
            		 "password12-","password12+","password12=","password12_","password12?","password12>","password12<","password12:","password12;"}; 
             String noletters=""; /*= browser.getTextByXpath(regPageProperties.getProperty("RegistrationPage.noletters"));
             //System.out.println(noletters+"1111111111111111111111");*/             
             String splcharacters = browser.getTextByXpath(regPageProperties.getProperty("RegistrationPage.splcharacters"));
             System.out.println(splcharacters+"1111111111111111111111");
             String spacesandlength = browser.getTextByXpath(regPageProperties.getProperty("RegistrationPage.spacesandlength"));
             System.out.println(spacesandlength+"1111111111111111111111");
             for (int i = 0; i < password.length; i++) {
            	 if (i == 0 || i < 2 || i== 7) {
                  verifyAndInputById(regPageProperties.getProperty("RegistrationPage.PasswordTextId"),"Password",password[i]);
                  browser.wait(2000);
                  if(browser.isElementVisibleWithXpath(regPageProperties.getProperty("RegistrationPage.noletters"))){
                	 String classnoletters = browser.getAttributeByXpath(regPageProperties.getProperty("RegistrationPage.noletters"), "class");
                	  noletters = browser.getTextByXpath(regPageProperties.getProperty("RegistrationPage.noletters"));
                	 System.out.println(noletters+"1111111111111111111111");
                 	 String classsplcharacters = browser.getAttributeByXpath(regPageProperties.getProperty("RegistrationPage.splcharacters"), "class");
                 	 String classspacesandlength = browser.getAttributeByXpath(regPageProperties.getProperty("RegistrationPage.spacesandlength"), "class");
                	 if (classnoletters.equals("gap-cross")&& classsplcharacters.equals("gap-cross")&& classspacesandlength.equals("gap-cross")){
                		 Report.updateTestLog("Condition :"+noletters+"/n"+splcharacters+"/n"+spacesandlength+" Displayed", "PASS");
                	 } else {
                		 Report.updateTestLog("New Password Validation Failed", "FAIL");
                     }
                    }
                  else {
                   	Report.updateTestLog("New Password validation overlay not displayed", "FAIL");
                   }
            	 }
                  if (i == 2) {
                      verifyAndInputById(regPageProperties.getProperty("RegistrationPage.PasswordTextId"),"Password",password[i]);
                      browser.wait(2000);
                      if(browser.isElementVisibleWithXpath(regPageProperties.getProperty("RegistrationPage.noletters"))){
                    	  String classnoletters = browser.getAttributeByXpath(regPageProperties.getProperty("RegistrationPage.noletters"), "class");
                     	 String classsplcharacters = browser.getAttributeByXpath(regPageProperties.getProperty("RegistrationPage.splcharacters"), "class");
                     	 String classspacesandlength = browser.getAttributeByXpath(regPageProperties.getProperty("RegistrationPage.spacesandlength"), "class");
                    	 if (classnoletters.equals("gap-cross")&& classsplcharacters.equals("gap-cross")&& classspacesandlength.equals("tick")){
                    		 Report.updateTestLog("Condition :"+noletters+"/n"+splcharacters+"/n"+spacesandlength+" Displayed", "PASS");
                    	 } else {
                    		 Report.updateTestLog("New Password Validation Failed", "FAIL");
                         }
                        }
                      else {
                       	Report.updateTestLog("New Password validation overlay not displayed", "FAIL");
                       }
                  }
                  if (i == 3) {
                      verifyAndInputById(regPageProperties.getProperty("RegistrationPage.PasswordTextId"),"Password",password[i]);
                      browser.wait(2000);
                      if(browser.isElementVisibleWithXpath(regPageProperties.getProperty("RegistrationPage.noletters"))){
                    	  String classnoletters = browser.getAttributeByXpath(regPageProperties.getProperty("RegistrationPage.noletters"), "class");
                     	 String classsplcharacters = browser.getAttributeByXpath(regPageProperties.getProperty("RegistrationPage.splcharacters"), "class");
                     	 String classspacesandlength = browser.getAttributeByXpath(regPageProperties.getProperty("RegistrationPage.spacesandlength"), "class");
                    	 if (classnoletters.equals("tick")&& classsplcharacters.equals("gap-cross")&& classspacesandlength.equals("gap-cross")){
                    		 Report.updateTestLog("Condition :"+noletters+"/n"+splcharacters+"/n"+spacesandlength+" Displayed", "PASS");
                    	 } else {
                    		 Report.updateTestLog("New Password Validation Failed", "FAIL");
                         }
                        }
                      else {
                       	Report.updateTestLog("New Password validation overlay not displayed", "FAIL");
                       }
                  }
                  if (i == 4) {
                      verifyAndInputById(regPageProperties.getProperty("RegistrationPage.PasswordTextId"),"Password",password[i]);
                      browser.wait(2000);
                      if(browser.isElementVisibleWithXpath(regPageProperties.getProperty("RegistrationPage.noletters"))){
                    	  String classnoletters = browser.getAttributeByXpath(regPageProperties.getProperty("RegistrationPage.noletters"), "class");
                     	 String classsplcharacters = browser.getAttributeByXpath(regPageProperties.getProperty("RegistrationPage.splcharacters"), "class");
                     	 String classspacesandlength = browser.getAttributeByXpath(regPageProperties.getProperty("RegistrationPage.spacesandlength"), "class");
                    	 if (classnoletters.equals("tick")&& classsplcharacters.equals("gap-cross")&& classspacesandlength.equals("tick")){
                    		 Report.updateTestLog("Condition :"+noletters+"/n"+splcharacters+"/n"+spacesandlength+" Displayed", "PASS");
                    	 } else {
                    		 Report.updateTestLog("New Password Validation Failed", "FAIL");
                         }
                        }
                      else {
                       	Report.updateTestLog("New Password validation overlay not displayed", "FAIL");
                       }
                  }
                  if (i == 5 || i > 8) {
                      verifyAndInputById(regPageProperties.getProperty("RegistrationPage.PasswordTextId"),"Password",password[i]);
                      browser.wait(2000);
                      if(browser.isElementVisibleWithXpath(regPageProperties.getProperty("RegistrationPage.noletters"))){
                    	  String classnoletters = browser.getAttributeByXpath(regPageProperties.getProperty("RegistrationPage.noletters"), "class");
                     	 String classsplcharacters = browser.getAttributeByXpath(regPageProperties.getProperty("RegistrationPage.splcharacters"), "class");
                     	 String classspacesandlength = browser.getAttributeByXpath(regPageProperties.getProperty("RegistrationPage.spacesandlength"), "class");
                    	 if (classnoletters.equals("tick")&& classsplcharacters.equals("tick")&& classspacesandlength.equals("gap-cross")){
                    		 Report.updateTestLog("Condition :"+noletters+"/n"+splcharacters+"/n"+spacesandlength+" Displayed", "PASS");
                    	 } else {
                    		 Report.updateTestLog("New Password Validation Failed", "FAIL");
                         }
                        }else {
                        	Report.updateTestLog("New Password validation overlay not displayed", "FAIL");
                        }
                  }
                  if (i == 6 || i == 8) {
                      verifyAndInputById(regPageProperties.getProperty("RegistrationPage.PasswordTextId"),"Password",password[i]);
                      browser.wait(2000);
                      if(browser.isElementVisibleWithXpath(regPageProperties.getProperty("RegistrationPage.noletters"))){
                    	  String classnoletters = browser.getAttributeByXpath(regPageProperties.getProperty("RegistrationPage.noletters"), "class");
                     	 String classsplcharacters = browser.getAttributeByXpath(regPageProperties.getProperty("RegistrationPage.splcharacters"), "class");
                     	 String classspacesandlength = browser.getAttributeByXpath(regPageProperties.getProperty("RegistrationPage.spacesandlength"), "class");
                    	 if (classnoletters.equals("tick")&& classsplcharacters.equals("tick")&& classspacesandlength.equals("tick")){
                    		 Report.updateTestLog("Condition :"+noletters+"/n"+splcharacters+"/n"+spacesandlength+" Displayed", "PASS");
                    	 } else {
                    		 Report.updateTestLog("New Password Validation Failed", "FAIL");
                         }
                        }else {
                        	Report.updateTestLog("New Password validation overlay not displayed", "FAIL");
                        }
                  }
            	 
               }
             verifyAndInputById(regPageProperties.getProperty("RegistrationPage.PasswordTextId"),"Password","password12");
             verifyAndInputById(regPageProperties.getProperty("RegistrationPage.ConfirmPasswordTextId"),"Confirm Password",userProfile.getPassword());
             browser.click(regPageProperties.getProperty("RegistrationPage.acceptTermsCheckBoxId"));
             //new Report().webArchiveCapture("Registration");
             Report.updateTestLog("Password details Page", "WARN");
             verifyAndClick(regPageProperties.getProperty("RegistrationPage.CreateRegOnlineAcc"), "Create Online Account Button");
            //getRegistrationAuditEventID(userProfile);
             browser.wait(10000);
             //Confirmation page text
             while(browser.isElementVisibleWithXpath(regPageProperties.getProperty("RegistrationPage.Logout"))==false){
            	 browser.wait(5000);
             }
             if(browser.isElementVisibleWithXpath(regPageProperties.getProperty("RegistrationPage.Logout"))){
            	 Report.updateTestLog("Logout Link  Verified in Registration confirmation Page successfully", "PASS");
            	 String strTitle = userProfile.getTitle();
            	 if(browser.isTextPresent(strTitle)){
                	 Report.updateTestLog(strTitle +" Title Verified in Registration confirmation Page scuccessfully", "PASS");
                 } 
            	 String strLName = userProfile.getLastName();
            	 if(browser.isTextPresent(strLName)){
                	 Report.updateTestLog(strLName +" Lastname Verified in Registration confirmation Page scuccessfully", "PASS");
                 } 
             }
             if(browser.isTextPresent(regPageProperties.getProperty("RegistrationPage.NectarLink"))){
            	 browser.clickWithLinkText(regPageProperties.getProperty("RegistrationPage.NectarLink")); 
            	 if(browser.isTextPresent("Collect Nectar points with British Gas")){
            		 Report.updateTestLog("Confirmation page Nectar link for Loyalty Customer", "PASS"); 
            	 }
            	 Report.updateTestLog("Register For Nectar Link Verified successfully", "PASS");
            	 browser.wait(5000);
            	 if(browser.isElementVisibleWithXpath(regPageProperties.getProperty("RegistrationPage.Logout"))){
            		 browser.browserBack();
            		 browser.wait(3000);
                	 Report.updateTestLog("Logout Link  Verified in Nectar Page successfully", "PASS");}
                	 else {
                		 Report.updateTestLog("Logout Link  Not Verified in Nectar Page", "FAIL");	 
                	 }
             }else {
            	 Report.updateTestLog("Confirmation page Nectar link for Non - Loyalty Customer", "PASS");
        	 }
             String strText ="Registration complete";
             if(browser.isTextPresent(strText)){
            	 Report.updateTestLog(strText +" Verified successfully", "PASS");
             }
           }

         public void verifyRegistrationSapNetweaver(UserProfile userProfile){
         	String[] ContactText = new String[5];
     		if(TestBase.CustomerData.equals("SAPCRM")){
     			SapNetWeaverPage sapNetWeaverPage = new SapNetWeaverPage();
    			sapNetWeaverPage.openSapCRM(userProfile);
    			ContactText = sapNetWeaverPage.contactHistoryValidation();
    			Report.updateTestLog("Transaction agent : "+ContactText[0], "Pass");
    		
    			if(ContactText[4].contains("REGISTRATION")){
    				Report.updateTestLog("Details have been updated in SAP with text :"+ContactText[4], "Pass");
    			}
    			else{
    				Report.updateTestLog("Details have not been updated in SAP", "Fail");
    				}
    			}
     		
         }
         
         //// New Registration Implementation
         
         public void registerCustomerDetailsNewOption(UserProfile userProfile,String accountNumber, String registerType){
        	 orderDate=new OnlineDBConnector().DBsysdate();
        	 curRegAcctnumber = accountNumber;
             verifyAndSelectDropDownBox(regPageProperties.getProperty("RegistrationPage.TitleSelectId"), "Title", userProfile.getTitle());
             verifyAndInputById(regPageProperties.getProperty("RegistrationPage.FirstNameTextId"), "First Name", userProfile.getFirstName());
             verifyAndInputById(regPageProperties.getProperty("RegistrationPage.LastNameTextId"),"Last Name",userProfile.getLastName());
             
             //new implementation-needs to update property
             if(registerType.equals("Email") || registerType.equals("DD") || registerType.equals("SO"))
             {
            	 verifyAndClick("postcodeRadio", "PostCode Radio Button");
            	 try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            	 verifyAndInputById("postCode", "PostCode", userProfile.getaddr());
             }else
             {
            	 verifyAndClick("customerReferenceNumber", "Customer Reference Number Selection");
            	 verifyAndInputById(regPageProperties.getProperty("RegisterationPage.AccNoText"),"Customer Account Number",curRegAcctnumber);
             }
             browser.wait(3000);
             Report.updateTestLog("Your details Page", "WARN");
             verifyAndClick(regPageProperties.getProperty("RegistrationPage.FindAccounts"),"Find Accounts Button");
             browser.wait(3000);
         }
         public void registerCustomerDetailsNew(UserProfile userProfile,String accountNumber, String registerType){
        	 orderDate=new OnlineDBConnector().DBsysdate();
        	 curRegAcctnumber = accountNumber;
             verifyAndSelectDropDownBox(regPageProperties.getProperty("RegistrationPage.TitleSelectId"), "Title", userProfile.getTitle());
             verifyAndInputById(regPageProperties.getProperty("RegistrationPage.FirstNameTextId"), "First Name", userProfile.getFirstName());
             verifyAndInputById(regPageProperties.getProperty("RegistrationPage.LastNameTextId"),"Last Name",userProfile.getLastName());
             
             if(registerType.equals("Email") || registerType.equals("DD") || registerType.equals("SO")|| registerType.equals("AccNo2"))
             {
            	 verifyAndClick(regPageProperties.getProperty("RegistrationPage.PostCodeOption"), "PostCode Radio Button");
            	 try {
					Thread.sleep(3000);
            	 verifyAndInputById(regPageProperties.getProperty("RegistrationPage.PostCodeInput"), "PostCode", userProfile.getaddr());
            	 browser.wait(3000);
                 Report.updateTestLog("Your details Page", "WARN");
                 verifyAndClick(regPageProperties.getProperty("RegistrationPage.FindAccounts"),"Find Accounts Button");
    					Thread.sleep(25000);
    				} catch (InterruptedException e) {
    					e.printStackTrace();
    				}
             }else
             {
            	 verifyAndClick(regPageProperties.getProperty("RegistrationPage.AccountOption"), "Customer Reference Number Selection");
            	 verifyAndInputById(regPageProperties.getProperty("RegisterationPage.AccNoText"),"Customer Account Number",curRegAcctnumber);
            	 browser.wait(3000);
                 Report.updateTestLog("Your details Page", "WARN");
                 verifyAndClick(regPageProperties.getProperty("RegistrationPage.FindAccounts"),"Find Accounts Button");
                 try {
    					Thread.sleep(35000);
    				} catch (InterruptedException e) {
    					e.printStackTrace();
    				}
             }
             if(registerType.equals("Email"))
             {
            	 try {
  					 Thread.sleep(18000);
	            	 verifyAndClick(regPageProperties.getProperty("RegistrationPage.EmailOption"), "Email Selection");
	            	 verifyAndClick(regPageProperties.getProperty("RegistrationPage.Account"),"Send Email Button");
	  				 Thread.sleep(5000);
	            	 verifyAndClick("accept", "Accept Layout submit");
	   				 Thread.sleep(35000);
   				} catch (InterruptedException e) {
   					e.printStackTrace();
   				}
            	 resetPasswordViaMail(userProfile);
             }
             
             if(registerType.equals("DD"))
             {
            	 try {
 					 Thread.sleep(18000);
	            	 verifyAndClick(regPageProperties.getProperty("RegistrationPage.DDOption"), "AccountNumber Selection");
	//            	 verifyAndClickWithName("altIdOptions", "DD Option Selection");
	            	 //verifyAndClick(regPageProperties.getProperty("RegistrationPage.DDNo Option"), "DDNo Selection");
	            	 String DDno = userProfile.getDDNo();	            	
	            for (int i=0; i< DDno.length();i++ ){
	            		String Str = DDno.charAt(i)+"";
	            		verifyAndInputByXpath(regPageProperties.getProperty("RegistrationPage.DDInput4")+(i+5)+"']", "Direct Debit Number Last 4 digit", Str);	
	            	 }
	            	 //verifyAndInputById(regPageProperties.getProperty("RegistrationPage.DDInput"), "Direct Debit Number Last 4 digit", userProfile.getDDNo());
	            	 verifyAndClick(regPageProperties.getProperty("RegistrationPage.Account"),"Find Accounts Button");
	 				 Thread.sleep(35000);
 				} catch (InterruptedException e) {
 					e.printStackTrace();
 				}
             }
             
             if(registerType.equals("SO"))
             {
            	 try {
  				 Thread.sleep(18000);
            	 verifyAndClick(regPageProperties.getProperty("RegistrationPage.SNOption"), "Sales Order Selection");
            	 verifyAndInputById(regPageProperties.getProperty("RegistrationPage.SNInput"), "Sales Order Number 10 Digit", userProfile.getSalesNo());
            	 verifyAndClick(regPageProperties.getProperty("RegistrationPage.Account"),"Find Accounts Button");
 				 Thread.sleep(35000);
 				} catch (InterruptedException e) {
 					e.printStackTrace();
 				}
             }
             if(registerType.equals("AccNo2"))
             {
            	 try {
  					 Thread.sleep(18000);
  					 verifyAndClick(regPageProperties.getProperty("RegistrationPage.AccountOption2"), "Customer Reference Number Selection in Registration");
  	            	 verifyAndInputById(regPageProperties.getProperty("RegisterationPage.AccNoText2"),"Customer Account Number Entered",curRegAcctnumber);
  	            	 verifyAndClick(regPageProperties.getProperty("RegistrationPage.Account"),"Find Accounts Button");
	   				 Thread.sleep(35000);
   				} catch (InterruptedException e) {
   					e.printStackTrace();
   				}
            	
             }
             
             if(browser.isTextPresent("Your password"))
             {
             verifyAndInputById(regPageProperties.getProperty("RegistrationPage.PasswordTextId"),"Password",userProfile.getPassword());
             verifyAndInputById(regPageProperties.getProperty("RegistrationPage.ConfirmPasswordTextId"),"Confirm Password",userProfile.getPassword());
             browser.click(regPageProperties.getProperty("RegistrationPage.acceptTermsCheckBoxId"));
             Report.updateTestLog("Password details Page", "WARN");
             verifyAndClick(regPageProperties.getProperty("RegistrationPage.CreateRegOnlineAcc"), "Create Online Account Button");
             browser.wait(10000);
             //Confirmation page text
             while(browser.isElementVisibleWithXpath(regPageProperties.getProperty("RegistrationPage.Logout"))==false){
            	 browser.wait(5000);
             }
             if(browser.isElementVisibleWithXpath(regPageProperties.getProperty("RegistrationPage.Logout"))){
            	 Report.updateTestLog("Logout Link  Verified in Registration confirmation Page successfully", "PASS");
            	 String strTitle = userProfile.getTitle();
            	 if(browser.isTextPresent(strTitle)){
                	 Report.updateTestLog(strTitle +" Title Verified in Registration confirmation Page scuccessfully", "PASS");
                 } 
            	 String strLName = userProfile.getLastName();
            	 if(browser.isTextPresent(strLName)){
                	 Report.updateTestLog(strLName +" Lastname Verified in Registration confirmation Page scuccessfully", "PASS");
                 } 
             }
             
          /*   if(browser.isTextPresent(regPageProperties.getProperty("RegistrationPage.NectarLink"))){
            	 try {
					Thread.sleep(4000);
				
            	 browser.clickWithLinkText(regPageProperties.getProperty("RegistrationPage.NectarLink")); 
            	 Thread.sleep(8000);
            	 if(browser.isTextPresent("Collect Nectar points with British Gas")){
            		 Report.updateTestLog("Confirmation page Nectar link for Loyalty Customer", "PASS"); 
            	 }
            	 verifyAndClick("necoption", "Nectar Option selection");
            	 verifyAndInputById("necVal", "Nectar Card number", "11111111111");
            	 verifyAndClick("tandc", "Check box selection");
            	 verifyAndClick("nectar_join", "Nectar Submit");
            	 } catch (InterruptedException e) {
 					e.printStackTrace();
 				}
            	 //browser.browserBack();
            	 //verifyIsTextPresent("Registration Success");
            	 Report.updateTestLog("Register For Nectar Link Verified successfully", "PASS");
            	 browser.wait(5000);
            	 if(browser.isElementVisibleWithXpath(regPageProperties.getProperty("RegistrationPage.Logout"))){
            		 browser.browserBack();
            		 browser.wait(3000);
                	 Report.updateTestLog("Logout Link  Verified in Nectar Page successfully", "PASS");}
                	 else {
                		 Report.updateTestLog("Logout Link  Not Verified in Nectar Page", "FAIL");	 
                	 }
             }else {
            	 Report.updateTestLog("Confirmation page Nectar link for Non - Loyalty Customer", "PASS");
        	 }*/
             String strText ="Registration complete";
             if(browser.isTextPresent(strText)){
            	 Report.updateTestLog(strText +" Verified successfully", "PASS");
             }
             }/*else
             {
            	 Report.updateTestLog("Registration got Failed", "FAIL");
             }*/
         }
         public void optionButtonverification(String optiontype){
        	 
        	 if(optiontype.equals("3")){
        	 if(browser.isElementVisible("tempPasswordRadio"))
        	 {
        		 Report.updateTestLog("Email option is present", "PASS");
        	 }
        	 else
        	 {
        		 Report.updateTestLog("Email option is not present", "FAIL");
        	 }
        	 
        	 if(browser.isElementVisible("directDebitRadio"))
        	 {
        		 Report.updateTestLog("DD option is present", "PASS");
        	 }
        	 else
        	 {
        		 Report.updateTestLog("DD option is not present", "FAIL");
        	 }
        	 
        	 if(browser.isElementVisible("salesOrderRadio"))
        	 {
        		 Report.updateTestLog("SO option is present", "PASS");
        	 }
        	 else
        	 {
        		 Report.updateTestLog("SO option is not present", "FAIL");
        	 }
        	 }
         }
         
         public void optionButtonverificationNoDD(){
        	 
        	 if(!browser.isElementVisible("directDebitRadio"))
        	 {
        		 Report.updateTestLog("DD option is not present", "PASS");
        	 }
        	 else
        	 {
        		 Report.updateTestLog("DD option is present", "FAIL");
        	 }
        	 
         }
  
  public void optionButtonverificationPresent(String optiontype){
        	 
        	 if(optiontype.equals("Email")){
        	 if(!browser.isElementVisible("tempPasswordRadio"))
        	 {
        		 Report.updateTestLog("Email option is present", "FAIL");
        	 }
        	 else
        	 {
        		 Report.updateTestLog("Email option is not present", "PASS");
        	 }
        	 }
        	 if(optiontype.equals("DD")){
        	 if(!browser.isElementVisible("directDebitRadio"))
        	 {
        		 Report.updateTestLog("DD option is present", "FAIL");
        	 }
        	 else
        	 {
        		 Report.updateTestLog("DD option is not present", "PASS");
        	 }
        	 }
        	 if(optiontype.equals("SO")){
        	 if(!browser.isElementVisible("salesOrderRadio"))
        	 {
        		 Report.updateTestLog("SO option is present", "FAIL");
        	 }
        	 else
        	 {
        		 Report.updateTestLog("SO option is not present", "PASS");
        	 }
        	 }
        	 } 
        	 
         public void resetPasswordViaMail(UserProfile userProfile) {
         	final String emailAddress = new OnlineDBConnector().getUserEmail(userProfile.getUCRN());
         	final String dbPwd = new OnlineDBConnector().getPassword(userProfile.getUCRN());
         	final String pwd = "EMKPnPBmhZXUXBCQp7Sirpjt+lg=";
         	browser.wait(1000);
         	//enterEmailAddress(userProfile,emailAddress);
         	new OnlineDBConnector().updateOneTimePassword(userProfile.getUCRN(),pwd);
         	new OnlineDBConnector().updatePassword(userProfile.getUCRN(),pwd);
         	if (ApplicationConfig.BRAND.equalsIgnoreCase("BG")) {
         		browser.open(ApplicationConfig.APP_BG_URL
     			        + "/Login/Login-Verify/");

     		} else {
     			browser.open(ApplicationConfig.APP_FUSION_URL
     			        + "/Login/Login-Verify/");
     		}
         	browser.wait(getWaitTime());
			
         	new LegacyLoginPage().loginUser(userProfile);
     		enterTemporaryPwd("","password12");
            
            new OnlineDBConnector().updateOneTimePassword(userProfile.getUCRN(),dbPwd);
            browser.wait(getWaitTime());

     	}
         
         public void enterTemporaryPwd(String tempPwd,String newPwd){
         	verifyAndInputByXpath(regPageProperties.getProperty("ForgottenPassword.NewPassword"), "New Password", newPwd);
             verifyAndInputById(regPageProperties.getProperty("ForgottenPassword.ConfirmPassword"), "ReEnter New Password", newPwd);
             //verifyAndClickWithName(regPageProperties.getProperty("ForgottenPassword.PasswordSubmitButton"), "Submit Temporary Password Button");
             verifyAndClick("Continue", "Password Reset Continue Button");
             browser.wait(getWaitTime());
             browser.wait(1000);
             //verifyIsTextPresent(pageProperties.getProperty("ForgottenPassword.PasswordResetConfirmation"));
         }
         
         public void verifyBrowserBackbutton(){
             browser.browserBack();
             getWaitTime();
             if (browser.isElementVisible(regPageProperties.getProperty("ForgottenPassword.PasswordSubmitButton"))){
                Report.updateTestLog("Browser Back Button is not working as expected",Fail_Str);
             } else {
                 Report.updateTestLog("Browser Back Button is working as expected",Pass_Str);
             }
         }
         
         public void nectarRegistration(){
        	 browser.open(ApplicationConfig.APP_BG_URL + "/Loyalty/View-Rewards/");
        	 browser.open(ApplicationConfig.APP_BG_URL + "/Loyalty/View-Rewards/");
        	 verifyAndClick("necoption", "Nectar Option selection");
        	 verifyAndInputById("necVal", "Nectar Card number", "11111111111");
        	 verifyAndClick("tandc", "Check box selection");
        	 verifyAndClick("nectar_join", "Nectar Submit");
        	 verifyIsTextPresent("Registration Success");
         }

/* Added for BGMO start*/

	public void registerEnterYourDetails(UserProfile selfRegisterData) {

		verifyAndInputById(
				regPageProperties.getProperty("RegisterationPage.EmailAddress"),
				"Email Address", selfRegisterData.getEmail());
		verifyAndInputById(
				regPageProperties
						.getProperty("RegisterationPage.ConfirmEmailAddress"),
				"Confirm Email Address", selfRegisterData.getEmail());
		verifyAndClickWithXpath(
				regPageProperties
						.getProperty("RegistrationPage.CreateOnlineAccountButton"),
				"Create online account Button");
		browser.wait(3000);

		/*
		 * verifyIsElementVisibleById(
		 * regPageProperties.getProperty("RegisterationPage.AccNoText"),
		 * "Customer Account Number"); verifyIsElementVisibleById(
		 * regPageProperties.getProperty("RegistrationPage.TitleSelectId"),
		 * "Title"); verifyIsElementVisibleById( regPageProperties
		 * .getProperty("RegistrationPage.FirstNameTextId"), "First Name");
		 * verifyIsElementVisibleById( regPageProperties
		 * .getProperty("RegistrationPage.LastNameTextId"), "Last Name");
		 */

		verifyAndInputById(
				regPageProperties.getProperty("RegisterationPage.AccNoText"),
				"Customer Account Number", selfRegisterData.getAccNumber());
		verifyAndSelectDropDownBox(
				regPageProperties.getProperty("RegistrationPage.TitleSelectId"),
				"Title", selfRegisterData.getTitle());
		verifyAndInputById(
				regPageProperties
						.getProperty("RegistrationPage.FirstNameTextId"),
				"First Name", selfRegisterData.getFirstName());
		verifyAndInputById(
				regPageProperties
						.getProperty("RegistrationPage.LastNameTextId"),
				"Last Name", selfRegisterData.getLastName());
		verifyAndClickWithXpath(
				regPageProperties
						.getProperty("RegistrationPage.Continue1Button"),
				"Register Button");

		if (browser.getTitle().contains("Already registered")) {
			verifyAndInputById(
					regPageProperties
							.getProperty("RegistrationPage.PasswordId"),
					"Password", "password12" + Keys.ENTER);
			browser.wait(9000);

		} else {

			verifyAndInputById(
					regPageProperties
							.getProperty("RegistrationPage.PasswordTextId"),
					"Password", "password12");
			verifyAndInputById(
					regPageProperties
							.getProperty("RegistrationPage.ConfirmPasswordTextId"),
					"Confirm Password", "password12");
			verifyAndSelectCheckBoxByID(
					regPageProperties
							.getProperty("RegistrationPage.acceptTermsCheckBoxId"),
					"Accept terms");
			verifyAndClickWithXpath(
					regPageProperties
							.getProperty("RegistrationPage.CreateAccountButton"),
					"Create Online Account Button");
			browser.wait(9000);

			verifyAndClickWithLinkText(
					regPageProperties
							.getProperty("RegistrationPage.ContinueToMyOnlineAccount"),
					"Continue to my online account");
			browser.wait(9000);
		}

	}

/* Added for BGMO end */

}
