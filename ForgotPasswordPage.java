package bg.framework.app.functional.page.reFactoring;

import bg.framework.app.functional.common.ApplicationConfig;
import bg.framework.app.functional.entities.UserProfile;
import bg.framework.app.functional.page.common.BasePage;
import bg.framework.app.functional.page.common.GlobalErrorMessages;
import bg.framework.app.functional.page.common.LegacyHomePage;
import bg.framework.app.functional.page.common.LegacyLoginPage;
import bg.framework.app.functional.util.OnlineDBConnector;
import bg.framework.app.functional.util.PropertyLoader;
import bg.framework.app.functional.util.Report;

import java.util.Properties;


/**
 ClassName: ForgotPasswordPage
 Description: To verify Forgotten Password journey
 */
public class ForgotPasswordPage extends BasePage {

    private final static String FILE_NAME = "resources/selfServe/ForgottenPassword.properties";
    private static Properties pageProperties = new PropertyLoader(FILE_NAME).load();
    private static String orderDate;
    final String  Pass_Str = "PASS";
    final String  Fail_Str = "FAIL";
// Verify whether Forgot Password link exists and check whether user is able to successfully navigate using temporary password once after
// forgot password link is selected
    
    public void validateForgottenPasswordJourney(UserProfile userProfile) {
        browser.wait(1000);
        verifyIsTextPresent(pageProperties.getProperty("ForgottenPassword.Header"));
        enterEmailAddress(userProfile,userProfile.getEmail());

        //Setting up the user's temp password as temp12345
        new OnlineDBConnector().updateTempPassword(userProfile.getUCRN());
        enterPwd(userProfile);
        enterTemporaryPwd("temp12345","password12");
        verifyIsTextPresent(pageProperties.getProperty("ForgottenPassword.ConfirmationMessage"));
        verifyAndClickWithLinkText(pageProperties.getProperty("ForgottenPassword.ContinueToYourAccount"),"Go back to Login ");
        verifyIsElementVisibleById("logoutMsg", "Home Page");
    }

 // Validating the Error Message by providing different invalid input combinations for Forgot Password field   
    
    public void verifyBlankFieldsError(UserProfile userProfile) {
        verifyAndClickWithXpath(pageProperties.getProperty("ForgottenPassword.ContinueButton"), "Continue Button");
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ErrorMSGHeader);
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ErrorMSGSubHeader);
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_EMAIL_ERROR);
    }

// Validating forgot password email address field
    
    public void validateEmailAddressField(UserProfile userProfile) {
    	final String[] getEmailID = {"abcdefghtest","�$%$%^&&","123456789","abc1234","kartic@bgdigitaltest.co.uk"};
 	   	for (int i = 0; i < getEmailID.length; i++) {
 	   		enterEmailAddress(userProfile,getEmailID[i]);
            if (i == 4) {
                verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ErrorMSGHeader);
                verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ErrorMSG_ForgottenPasswordIncorrectEmail);
            } else {
                verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ErrorMSGHeader);
                verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ErrorMSGSubHeader);
                verifyIsTextPresent(GlobalErrorMessages.ReFactoring_EMAIL_ERROR);
            }
        }
        verifyAndClickWithLinkText(pageProperties.getProperty("ForgottenPassword.Cancel"), "Cancel Link");
        browser.wait(getWaitTime());
        browser.wait(1000);
    }

 // Validating forgot password new pwd field
    
    public void validateNewPasswordField(UserProfile userProfile) {
    	enterEmailAddress(userProfile,userProfile.getEmail());
        final String dbPwd = new OnlineDBConnector().getPassword(userProfile.getUCRN());
        String pwd = "password12";
        new OnlineDBConnector().updateOneTimePassword(userProfile.getUCRN(),dbPwd);
        browser.wait(1000);
        String emailAddress = userProfile.getEmail();
        if (ApplicationConfig.BRAND.equalsIgnoreCase("BG")) {
    		browser.open(ApplicationConfig.APP_BG_URL
			        + "/Password-Reset/Password-Temporary-Entry/"+emailAddress+"/1/"+pwd+"/");
			} else {
			browser.open(ApplicationConfig.APP_FUSION_URL
			        + "/Password-Reset/Password-Temporary-Entry/"+emailAddress+"/2/"+pwd+"/");
			}
    	browser.wait(getWaitTime());

        final String[] getPassWord = {"123","�$%&&","1a2b3c","abc",""};
        for (int i = 0; i < getPassWord.length; i++) {
            enterTemporaryPwd("temp12345",getPassWord[i]);
            browser.wait(getWaitTime());
            browser.wait(1000);
            if (i == 4) {
                verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ErrorMSGHeader);
                verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ErrorMSGSubHeader);
                verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ForgotPwdOldPassword);
                verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ForgotPwdRetypePassword);
            } else {
                verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ErrorMSGHeader);
                verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ErrorMSGSubHeader);
                verifyIsTextPresent(GlobalErrorMessages.ReFactoring_FgetPasswordtypeError);
            }
        }
    }

 // Validating forgot password Temp Pwd field
    
    public void validateTempPasswordField(UserProfile userProfile) {
    	final String[] arrTempPassWord = {"123","�$%&&","1a2b3c","abc",""};
    	for (int i = 0; i < arrTempPassWord.length; i++) {
           enterTemporaryPwd(arrTempPassWord[i],"password12");
            browser.wait(getWaitTime());
            browser.wait(1000);
            if (i == 4) {
                verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ErrorMSGHeader);
                verifyIsElementVisibleWithXpath(pageProperties.getProperty("ForgottenPassword.FieldErrorMessage"), "Temporary Password field validation for the value '" + arrTempPassWord[i] + "'");
            } else {
                verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ErrorMSGHeader);
                verifyIsTextPresent(GlobalErrorMessages.ReFactoring_FgetPasswordtypeError);
            }
        }
    }

 // Validating forgot password error message text 
    
    public void validatePasswordFieldErrorMsg(UserProfile userProfile) {
        verifyAndInputById(pageProperties.getProperty("ForgottenPassword.EmailIDField"), "Customer Email Address", userProfile.getEmail());
        verifyAndClickWithXpath(pageProperties.getProperty("ForgottenPassword.ContinueButton"), "Continue Button");
        browser.wait(getWaitTime());
        verifyAndClickWithLinkText(pageProperties.getProperty("LoginPage.PasswordID"), "Password Link");
        verifyIsTextPresent(pageProperties.getProperty("ForgottenPassword.Header"));
        
       }

 // Entering the appropriate Email address to reset the password
    
    public void enterEmailAddress(UserProfile userProfile,String emailAddress) {
    	if (browser.isElementVisible(pageProperties.getProperty("ForgottenPassword.EmailIDField"))){
    	verifyAndInputById(pageProperties.getProperty("ForgottenPassword.EmailIDField"), "Customer Email Address", emailAddress);
        verifyAndClickWithXpath(pageProperties.getProperty("ForgottenPassword.ContinueButton"), "Continue Button");
        browser.wait(1000);
        Report.updateTestLog("Email is triggered to reset your Password ",Pass_Str);
    	}
      }

// Retrieving the Email address and Password for the appropriate UCRN from database and reset to a new password for the retrieved email address
// and check whether the pwd has been reset successfully
    
    public void navigateToOneTimePasswordPage(UserProfile userProfile) {
    	final String emailAddress = new OnlineDBConnector().getUserEmail(userProfile.getUCRN());
    	final String dbPwd = new OnlineDBConnector().getPassword(userProfile.getUCRN());
    	final String pwd = "password12";
    	browser.wait(1000);
    	enterEmailAddress(userProfile,emailAddress);
    	new OnlineDBConnector().updateOneTimePassword(userProfile.getUCRN(),dbPwd);
    	    	
    	if (ApplicationConfig.BRAND.equalsIgnoreCase("BG")) {
    		browser.open(ApplicationConfig.APP_BG_URL
			        + "/Password-Reset/Password-Temporary-Entry/"+emailAddress+"/1/"+pwd+"/");

		} else {
			browser.open(ApplicationConfig.APP_FUSION_URL
			        + "/Password-Reset/Password-Temporary-Entry/"+emailAddress+"/2/"+pwd+"/");
		}
    	browser.wait(getWaitTime());
		enterTemporaryPwd("","password8");
        verifyBrowserBackbutton();
        new OnlineDBConnector().updateOneTimePassword(userProfile.getUCRN(),dbPwd);
        browser.wait(1000);
        verifyIsTextPresent(pageProperties.getProperty("ForgottenPassword.PasswordResetSuccess"));
        verifyAndClickWithXpath(pageProperties.getProperty("ForgottenPassword.AccountLoginID"),"AccountLogin");
        browser.wait(getWaitTime());

	}

// Enter the pwd for the appropriate Email Address

    public void enterPwd(UserProfile userProfile){
    	verifyAndInputById("login-email", "Login ID", userProfile.getEmail());
        verifyAndInputById("login-password", "Temp Password", "temp12345");
        verifyAndClickWithClass("btn-input", "Login Button");
        browser.wait(getWaitTime());
    }

// Entering a new pwd for the appropriate email address and check whether it has been reset successfully
    
    public void enterTemporaryPwd(String tempPwd,String newPwd){
    	verifyAndInputByXpath(pageProperties.getProperty("ForgottenPassword.NewPassword"), "New Password", newPwd);
        verifyAndInputById(pageProperties.getProperty("ForgottenPassword.ConfirmPassword"), "ReEnter New Password", newPwd);
        verifyAndClick(pageProperties.getProperty("ForgottenPassword.PasswordSubmitButton"), "Submit Temporary Password Button");
        browser.wait(getWaitTime());
        browser.wait(1000);
        //verifyIsTextPresent(pageProperties.getProperty("ForgottenPassword.PasswordResetConfirmation"));

    }
    
// Select Direct debit option from the mega menu located in home page and select the forgot password location link located in the page
    
    public void selectDirectDebit(UserProfile userProfile) {

    	if (ApplicationConfig.BRAND.equalsIgnoreCase("BG")) {
    		browser.open(ApplicationConfig.APP_BG_URL +"/content/britishgas/youraccount/discover/you-are-in-control-with-direct-debit.html");
    	} else {
			browser.open(ApplicationConfig.APP_FUSION_URL+"/content/britishgas/youraccount/discover/you-are-in-control-with-direct-debit.html");
			
		}	
    	browser.wait(5000);
    	//verifyAndClickWithXpath(pageProperties.getProperty("ForgottenPassword.DirectDebitSignUp"), "Direct Debit SignIn");
    	browser.clickWithLinkText(pageProperties.getProperty("ForgottenPassword.DirectDebitText"));
    	browser.wait(1000);
    	//browser.clickWithXpath(pageProperties.getProperty("ForgottenPassword.PasswordReset"));
    	browser.clickWithLinkText(pageProperties.getProperty("ForgottenPassword.PasswordResetText"));
        //browser.wait(1000);
    }
    
// Validating the error messages for Forgot Password option
    
	public void validateErrorMessages(UserProfile userProfile) {
        verifyBlankFieldsError(userProfile);
    }

// Validate the back option functionality
	
    public void verifyBrowserBackbutton(){
        browser.browserBack();
       /* getWaitTime();
        if (browser.isElementVisible(pageProperties.getProperty("ForgottenPassword.PasswordSubmitButton"))){
           Report.updateTestLog("Browser Back Button is not working as expected",Fail_Str);
        } else {
            Report.updateTestLog("Browser Back Button is working as expected",Pass_Str);
        }*/
    }

    public void enterOnlineEmailAddress(UserProfile userProfile) {
    	final String expectedEmailAdd = new OnlineDBConnector().getUserEmail(userProfile.getUCRN());
    	verifyAndInputById(pageProperties.getProperty("ForgottenPassword.EmailIDField"), "Customer Email Address", expectedEmailAdd);
        verifyAndClickWithXpath(pageProperties.getProperty("ForgottenPassword.ContinueButton"), "Continue Button");
        browser.wait(1000);
        Report.updateTestLog("Email is triggered to reset your Password ",Pass_Str);

        if (ApplicationConfig.BRAND.equalsIgnoreCase("BG")) {
			browser.open(ApplicationConfig.APP_BG_URL);
		} else {
			browser.open(ApplicationConfig.APP_FUSION_URL);
		}
      }
    public void verifyAuditEntry(UserProfile userProfile){
    	OnlineDBConnector onlineDB = new OnlineDBConnector();
    	final String emailAddress = onlineDB.getUserEmail(userProfile.getUCRN());
    	onlineDB.verifyAudit(emailAddress);
    }
    public void verifyLastPasswordPage(UserProfile userProfile) {
    	final String emailAddress = new OnlineDBConnector().getUserEmail(userProfile.getUCRN());    	
    	final String[] getPassWord = {"password12","password12","password12","password12","password12","password12","password12","password12"
    			,"password12","password12","password12","password12"};
        for (int i = 0; i < getPassWord.length; i++) {
        final String dbPwd = new OnlineDBConnector().getPassword(userProfile.getUCRN());
    	final String pwd = getPassWord[i];
    	Report.updateTestLog("Reset Password Iteration "+i, "DONE");
    	LegacyHomePage legacyHomePage = new LegacyHomePage();
        legacyHomePage.navigateToLoginPage();
        legacyHomePage.navigateToForgottenPassword();
    	browser.wait(1000);
    	enterEmailAddress(userProfile,emailAddress);
    	new OnlineDBConnector().updateOneTimePassword(userProfile.getUCRN(),dbPwd);
    	    	
    	if (ApplicationConfig.BRAND.equalsIgnoreCase("BG")) {
    		browser.open(ApplicationConfig.APP_BG_URL
			        + "/Password-Reset/Password-Temporary-Entry/"+emailAddress+"/1/"+pwd+"/");
		} else {
			browser.open(ApplicationConfig.APP_FUSION_URL
			        + "/Password-Reset/Password-Temporary-Entry/"+emailAddress+"/2/"+pwd+"/");
		}
    	browser.wait(getWaitTime());
		enterTemporaryPwd("",getPassWord[i]);
        //verifyBrowserBackbutton();
        new OnlineDBConnector().updateOneTimePassword(userProfile.getUCRN(),dbPwd);
        verifyIsTextPresent(pageProperties.getProperty("ForgottenPassword.PasswordResetSuccess"));
        verifyAndClickWithXpath(pageProperties.getProperty("ForgottenPassword.AccountLoginID"),"AccountLogin");
        browser.wait(getWaitTime());
        verifyAuditEntry(userProfile);
        LegacyLoginPage legacyLoginPage = new LegacyLoginPage();
        legacyLoginPage.loginUser(userProfile);
        legacyLoginPage.logOut();
        }

	}
}
