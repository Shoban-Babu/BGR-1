package bg.framework.app.functional.page.reFactoring;

import bg.framework.app.functional.common.ApplicationConfig;
import bg.framework.app.functional.entities.UserProfile;
import bg.framework.app.functional.page.common.BasePage;
import bg.framework.app.functional.page.common.GlobalErrorMessages;
import bg.framework.app.functional.util.OnlineDBConnector;
import bg.framework.app.functional.util.PropertyLoader;
import bg.framework.app.functional.util.Report;
import bg.framework.app.functional.util.SiebelDataBase;

import java.util.ArrayList;
import java.util.Properties;

public class ForgotEmailPage extends BasePage {

    private final static String FILE_NAME = "resources/ReFactoring/ForgotEmail.Properties";
    private static Properties pageProperties = new PropertyLoader(FILE_NAME).load();
    private final static String LOGIN_FILE_NAME = "resources/common/LoginPage.Properties";
    private static Properties loginProperties = new PropertyLoader(LOGIN_FILE_NAME).load();
    final String  Pass_Str = "PASS";
    final String  Fail_Str = "FAIL";
    private static String bname="";
    
    public ForgotEmailPage() {
        super();
    }

// Verify whether Forgot Email Link exists and if it exists click on the Forgot Email Link and check whether it navigates to the appropriate page    

    public void validateForgotEmail(final UserProfile userProfile) {
        final String EmailAddressText = browser.getTextByXpath(loginProperties.getProperty("LoginPage.RequiredFieldID"));
        if (EmailAddressText.trim().toLowerCase().contains("retrieve your email address")) {
            Report.updateTestLog("Displayed Email Address Text Is  :" + EmailAddressText, Pass_Str);
        } else {
            Report.updateTestLog("Expected Email Address Text Was Not Displayed ",Fail_Str);
        }
    }

// Verify whether appropriate fields are displayed in the Forgot Email Page     

    public void validateForgotEmailFields(final UserProfile userProfile) {
        int flag = 0;

        final String customerRefNum = browser.getTextByXpath(pageProperties.getProperty("ForgotEmailPage.CustomerReferenceNumberID"));
        final String whereCanIFind = browser.getTextByXpath(pageProperties.getProperty("ForgotEmailPage.WhereCanIFindID"));
        final String title = browser.getTextByXpath(pageProperties.getProperty("ForgotEmailPage.TitleID"));
        final String firstName = browser.getTextByXpath(pageProperties.getProperty("ForgotEmailPage.FirstNameID"));
        final String surName = browser.getTextByXpath(pageProperties.getProperty("ForgotEmailPage.SurNameID"));
        final String getEmailAddress = browser.getAttributeByXpath(pageProperties.getProperty("ForgotEmailPage.GetEmailAddressID"), "title");
        final String cancel = browser.getTextByXpath(pageProperties.getProperty("ForgotEmailPage.CancelID"));

        final String[] fields = {customerRefNum, whereCanIFind, title, firstName, surName, getEmailAddress, cancel};
        final String[] names = {"customer", "where can i", "title", "first", "last", "get email", "cancel"};

        for (int i = 0; i < names.length; i++) {
            if (fields[i].trim().toLowerCase().contains(names[i].trim())) {
                flag = flag + 1;
            } else {
                Report.updateTestLog("Expected Field Name Was Not Displayed " + fields[i], Fail_Str);
            }
        }

        if (flag == 6) {
            Report.updateTestLog("Expected Field Names was displayed", Pass_Str);
        }
    }

    // Verifying the values in the Bread Crumb in Forgot Email Page

    public void verifyingForgotEmailBreadCrumb() {
        final String strBreadCrumbText = browser.getTextByXpath(pageProperties.getProperty("ForgotEmailPage.BreadCrumbID"));
        Report.updateTestLog("Displayed Field Name Is  :" + strBreadCrumbText,Pass_Str);
    }

    // Validating the contents displayed in Where Can I Find Option Dialog Box and close it

    public void validateForgotEmailWhereCanIFind() {
        if (browser.isElementVisibleWithXpath(pageProperties.getProperty("ForgotEmailPage.WhereCanIFindID"))) {
            verifyAndClickWithXpath(pageProperties.getProperty("ForgotEmailPage.WhereCanIFindID"), "WhereCanIFindID");
            //verifyIsTextPresent(pageProperties.getProperty("ForgotEmailPage.CustomerReferenceText"));
            if (browser.isElementVisibleWithXpath(pageProperties.getProperty("ForgotEmailPage.CloseButtonID"))) {
                verifyAndClickWithXpath(pageProperties.getProperty("ForgotEmailPage.CloseButtonID"), "Close Button");
            }
        }
    }

    // Validating the values displayed in the Title List Box

    public void validateForgotEmailTitle() {
        Integer Flg = 0;
        final String[] titlevalues = {"Please select", "Mr", "Mrs", "Ms", "Dr", "Miss", "Sir", "Reverend", "Dame", "Lady","Professor"};
        final ArrayList<String> titleTxtArray = browser.getFromDropBox("id", pageProperties.getProperty("ForgotEmailPage.SelectID"));
        for (int i = 0; i < titlevalues.length; i++) {
            if (titleTxtArray.get(i).trim().equalsIgnoreCase(titlevalues[i].trim())) {
                Flg = Flg + 1;
            } else {
                Report.updateTestLog("Expected Title Text Was Not Displayed ", Fail_Str);
            }
            if (Flg == 12) {
                Report.updateTestLog("Expected Title Text Was Displayed", Pass_Str);
            }

        }

    }

// Validating Enter Email Address option by comparing the Email Address displayed in the application against the Email Address fetched from the database

    public void validateEnterEmail(final UserProfile userProfile) {
        final String firstName = new SiebelDataBase().getFirstName(userProfile.getUCRN());
        final String lastName = new SiebelDataBase().getLastName(userProfile.getUCRN());
        final String strTitle = new SiebelDataBase().getTitle(userProfile.getUCRN());
        final String accountNum = userProfile.getAccNumber();
        final String expectedEmailAdd = new OnlineDBConnector().getUserEmail(userProfile.getUCRN());

        enterForgotEmailData(accountNum, strTitle, firstName, lastName);
        if (ApplicationConfig.BRAND.equalsIgnoreCase("bg")){
        if (browser.isElementVisibleWithXpath(pageProperties.getProperty("ForgotEmailPage.AccountLoginID"))) {
        	final String DisplayedEmailAdd = browser.getTextByXpath(pageProperties.getProperty("ForgotEmailPage.DisplayedEmailAddressID"));
        	Report.takeScreenshot("Forgot Email Confirmation Page", "WARN");
            if (DisplayedEmailAdd.trim().contains(expectedEmailAdd.trim())) {
                Report.updateTestLog("Expected DB Email ID : "+expectedEmailAdd+"  Displayed Email Address Is  :" + DisplayedEmailAdd,Pass_Str);
            } else {
                Report.updateTestLog("Expected Email Address "+expectedEmailAdd+" Was Not Displayed", Fail_Str);
            }
            verifyAndClickWithXpath(pageProperties.getProperty("ForgotEmailPage.AccountLoginID"),"Login to your account Link");
        }
        }else{
        	if (browser.isElementVisibleWithXpath(pageProperties.getProperty("ForgotEmailPage.SEAccountLoginID"))) {
            	final String DisplayedEmailAdd = browser.getTextByXpath(pageProperties.getProperty("ForgotEmailPage.DisplayedEmailAddressID"));
            	Report.takeScreenshot("Forgot Email Confirmation Page", "WARN");
                if (DisplayedEmailAdd.trim().contains(expectedEmailAdd.trim())) {
                    Report.updateTestLog("Expected DB Email ID : "+expectedEmailAdd+"  Displayed Email Address Is  :" + DisplayedEmailAdd,Pass_Str);
                } else {
                    Report.updateTestLog("Expected Email Address "+expectedEmailAdd+" Was Not Displayed", Fail_Str);
                }
                verifyAndClickWithXpath(pageProperties.getProperty("ForgotEmailPage.SEAccountLoginID"),"Login to your account Link");
            
        }
        	

        }

        //verifyIsElementVisibleWithXpath(loginProperties.getProperty("LoginPage.HeaderID"), "Login Page");
        String titleText = browser.getTitle();
        if (titleText.trim().contains("Login")) {
            Report.updateTestLog("Login Page displayed :" + titleText,Pass_Str);
        } else {
            Report.updateTestLog("Login Page displayed", "DONE");
        }
    }

    // Validating the Error Message by providing different invalid input combinations for Account Number

    public void accountNumberErrorMsgValidation(final UserProfile userProfile) {
        final String firstName = new SiebelDataBase().getFirstName(userProfile.getUCRN());
        final String lastName = new SiebelDataBase().getLastName(userProfile.getUCRN());
        final String strTitle = new SiebelDataBase().getTitle(userProfile.getUCRN());
        final String[] customerNum = {"", "850000000000", "8500000000", "000000000000", "85aaac234"};
        for (int i = 0; i < customerNum.length; i++) {
            if (i == 0) {
                enterForgotEmailData("", "Please select", "", "");
                errorMessageComparison(GlobalErrorMessages.ReFactoring_CUSTOMER_Reference_ERROR);
            } else if (i > 0 & i < 4) {
                enterForgotEmailData(customerNum[i], strTitle, firstName, lastName);
                errorMessageComparison(GlobalErrorMessages.ReFactoring_ReferenceNumberInline_ERROR);
            } else if (i == 4) {
                enterForgotEmailData(customerNum[i], strTitle, firstName, lastName);
                errorMessageComparison(GlobalErrorMessages.ReFactoring_ReferenceNumber_ERROR);
            }
        }

    }

    // Validating the Error Message by providing different invalid input combinations for First Name

    public void firstNameErrorMsgValidation(final UserProfile userProfile) {
        final String strAccountNum = new SiebelDataBase().getAccountNumber(userProfile.getUCRN());
        final String strLastName = new SiebelDataBase().getLastName(userProfile.getUCRN());
        final String strTitle = new SiebelDataBase().getTitle(userProfile.getUCRN());
        final String[] firstName = {"", "aa23", ".*a", "occupier"};

        for (int i = 0; i < firstName.length; i++) {
            if (i == 0) {
                enterForgotEmailData(strAccountNum, strTitle, "", strLastName);
                errorMessageComparison(GlobalErrorMessages.ReFactoring_FIRSTNAME_ERROR);
            } else if (i > 0 & i < 3) {
                enterForgotEmailData(strAccountNum, strTitle, firstName[i], strLastName);
                errorMessageComparison(GlobalErrorMessages.ReFactoring_FIRSTNAMECondition_ERROR);
            } else if (i == 3) {
                enterForgotEmailData(strAccountNum, strTitle, firstName[i], strLastName);
                errorMessageComparison(GlobalErrorMessages.ReFactoring_NAME_InLineERROR);
            }
        }

    }

// Validating the Error Message by providing different invalid input combinations for Last Name and finally
// select your account for navigation back to the Log In Page

    public void lastNameErrorMsgValidation(final UserProfile userProfile) {
        final String strAccountNum = new SiebelDataBase().getAccountNumber(userProfile.getUCRN());
        final String strFirstName = new SiebelDataBase().getFirstName(userProfile.getUCRN());
        final String strTitle = new SiebelDataBase().getTitle(userProfile.getUCRN());
        final String[] lastName = {"", "aa23", ".*a", "occupier"};

        for (int i = 0; i < lastName.length; i++) {
            if (i == 0) {
                enterForgotEmailData(strAccountNum, strTitle, strFirstName, "");
                errorMessageComparison(GlobalErrorMessages.ReFactoring_LASTNAME_ERROR);
            } else if (i > 0 & i < 3) {
                enterForgotEmailData(strAccountNum, strTitle, strFirstName, lastName[i]);
                final String strCopyRights = browser.getTextByXpath(pageProperties.getProperty("ForgotEmailPage.CopyRights"));
                if (strCopyRights.trim().contains("British")) {
                    errorMessageComparison(GlobalErrorMessages.ReFactoring_LASTNAMECondition_InLineERROR);
                } else {
                    errorMessageComparison(GlobalErrorMessages.ReFactoring_LASTNAMECondition_ERROR);
                }
            } else if (i == 3) {
                enterForgotEmailData(strAccountNum, strTitle, strFirstName, lastName[i]);
                errorMessageComparison(GlobalErrorMessages.ReFactoring_NAME_InLineERROR);
            }
        }
        selectYourAccount();
    }

// Error Message Validation in Forgot Email Page by by changing the BG Account Number to Sainsbury 
// and validate the Account Number in BG Site by selecting get Email Address Option and once the
// validation is done revert back the changes       

    public void validateDBConversionErrorMessage(final UserProfile userProfile) {
        final String strAccountNum = new SiebelDataBase().getAccountNumber(userProfile.getUCRN());
        final String strFirstName = new SiebelDataBase().getFirstName(userProfile.getUCRN());
        final String strLastName = new SiebelDataBase().getLastName(userProfile.getUCRN());
        final String strTitle = new SiebelDataBase().getTitle(userProfile.getUCRN());
        final String strCopyRights = browser.getTextByXpath(pageProperties.getProperty("ForgotEmailPage.CopyRights"));

        if (strCopyRights.contains("British Gas")) {
            Report.updateTestLog("Db updated to SE", Pass_Str);
            updateToSEConversionDB(userProfile);

        } else if (strCopyRights.contains("Sainsbury's Energy")) {
            Report.updateTestLog("Db updated to BG", Pass_Str);
            updateToBGConversionDB(userProfile);
        }

        enterForgotEmailData(strAccountNum, strTitle, strFirstName, strLastName);

        if (browser.isElementVisibleWithXpath(pageProperties.getProperty("ForgotEmailPage.AccountLoginID"))) {
            //verifyErrorMessage(strActualErrorMessageValidationText,userProfile.getErrorMessage4());
            Report.updateTestLog("Expected Error Message was not displayed", "FAIL");
        } else {
        	errorMessageComparison(GlobalErrorMessages.ReFactoring_ErrorMSGHeader);
        }

        if (strCopyRights.trim().contains("British")) {
            Report.updateTestLog("Db updated to BG", Pass_Str);
            updateToBGConversionDB(userProfile);
            
        } else {
            Report.updateTestLog("Db updated to SE", Pass_Str);
            updateToSEConversionDB(userProfile);
        }
    }

// Enter BG Account Number in Sainsbury and try retrieving the email address. Error message will be displayed.

    public void updateToBGConversionDB(final UserProfile userProfile) {
        bname=new OnlineDBConnector().getCustomerID(userProfile.getUCRN());
        new OnlineDBConnector().changeBrandName(bname, "BG");
    }

// Revert the Changed Account Number from BG to Sainsbury

    public void updateToSEConversionDB(final UserProfile userProfile) {
    	bname=new OnlineDBConnector().getCustomerID(userProfile.getUCRN());
    	new OnlineDBConnector().changeBrandName(bname, "SE");
    }


// Verify Email Address is displayed after locking the account and once the validation is done 
// Revert back the changes to Unlocked state

    public void verifyAccountLockValidation(final UserProfile userProfile) {
        new OnlineDBConnector().dbAccountlock(userProfile.getUCRN());
        validateEnterEmail(userProfile);
        verifyAndClickWithXpath(loginProperties.getProperty("LoginPage.ForgotEmailID"), "Forgot Email");
        browser.wait(1000);
        new OnlineDBConnector().dbAccountunlock(userProfile.getUCRN());
    }


// 	Verify Account Number with Time Period(Less/Greater than 6 Months)and status in Active state       

    public void verifyActiveAccount(final UserProfile userProfile) {
        final Integer activeVal = 5;
        final Integer secondActiveVal = 7;
        final String strAccountNum = new SiebelDataBase().getAccountNumber(userProfile.getUCRN());
        new SiebelDataBase().setAccountStatus(strAccountNum, activeVal);
        validateEnterEmail(userProfile);
        verifyAndClickWithXpath(loginProperties.getProperty("LoginPage.ForgotEmailID"), "Forgot Email");
        new SiebelDataBase().setAccountStatus(strAccountNum, secondActiveVal);
        validateEnterEmail(userProfile);
    }

//  Verify Account Number with Time Period(Less/Greater than 6 Months)and status in Inactive state       

    public void verifyInactiveAccount(final UserProfile userProfile) {
        final Integer inactiveVal = 5;
        final Integer secondInactiveVal = 7;
        final String strAccountNum = new SiebelDataBase().getAccountNumber(userProfile.getUCRN());
        new SiebelDataBase().setStatus(strAccountNum, inactiveVal, "Inactive");
        validateEnterEmail(userProfile);
        verifyAndClickWithXpath(loginProperties.getProperty("LoginPage.ForgotEmailID"), "Forgot Email");
        new SiebelDataBase().setStatus(strAccountNum, secondInactiveVal, "Inactive");
        validateEnterEmail(userProfile);
    }

// Enter the appropriate details in the fields displayed in Forgot Email Page        

    public void enterForgotEmailData(final String CustomerNumber, final String CustomerTitle, final String FirstName, final String SurName) {
        verifyAndInputByXpath(pageProperties.getProperty("ForgotEmailPage.CustomerReferenceNumberFieldID"), "CustomerReferenceNumber", CustomerNumber);
        verifyAndSelectDropDownBox(pageProperties.getProperty("ForgotEmailPage.SelectID"), "Title", CustomerTitle);
        verifyAndInputByXpath(pageProperties.getProperty("ForgotEmailPage.FirstNameFieldID"), "FirstName", FirstName);
        verifyAndInputByXpath(pageProperties.getProperty("ForgotEmailPage.SurNameFieldID"), "LastName", SurName);
        Report.takeScreenshot("Forgot Email Address Page","WARN");
        verifyAndClickWithXpath(pageProperties.getProperty("ForgotEmailPage.GetEmailAddressID"), "GetEmailAddress");
    }

// Clear the CustomerReferenceNumber,FirstName,LastName fields displayed in Forgot Email Page       

    public void selectYourAccount() {
        verifyAndClickWithXpath(pageProperties.getProperty("ForgotEmailPage.YourAccountID"), "Your Account");
    }

// Capture the error message displayed and verify the actual and error message displayed by making a call to the verifyErrorMessage method   

    public void errorMessageComparison(final String expectedErrorMsg) {
        final String displayedErrorMsg = browser.getTextByXpath(pageProperties.getProperty("ForgotEmailPage.ErrorMessageValidationID"));
        verifyErrorMessage(displayedErrorMsg, expectedErrorMsg);
    }

// Validation for Inappropriate data for the fields displayed in Forgot Email Compare the error message displayed in the 
// application against the Expected data  	   

    public void verifyErrorMessage(final String displayedErrorMsg, final String expectedErrorMsg) {
        if (displayedErrorMsg.trim().contains(expectedErrorMsg.trim())) {
            Report.updateTestLog("Displayed Error Message Validation Is  :" + displayedErrorMsg, Pass_Str);
        } else {
            Report.updateTestLog("Expected Error Message Validation Was Not Displayed", Fail_Str);
        }
    }


}
 


