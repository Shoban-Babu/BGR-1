package bg.framework.app.functional.page.reFactoring;

import bg.framework.app.functional.entities.UserProfile;
import bg.framework.app.functional.page.common.BasePage;
import bg.framework.app.functional.page.common.GlobalErrorMessages;
import bg.framework.app.functional.util.OnlineDBConnector;
import bg.framework.app.functional.util.PropertyLoader;
import bg.framework.app.functional.util.Report;

import java.util.Properties;

/**
 Class Name:  ChangeEmailPage
 Description: To verify Change Email Journey
 */
public class ChangeEmailPage extends BasePage {

    private final static String FILE_NAME = "resources/reFactoring/ChangeEmailAddressReFactoring.properties";
    private static Properties pageProperties = new PropertyLoader(FILE_NAME).load();

    final String  Pass_Str = "PASS";
    final String  Fail_Str = "FAIL";

    public ChangeEmailPage() {
        super();
    }

    // verify change email address page exists and user can change the email address
    public void validateChangeEmailAddressJourney(UserProfile userProfile) {
        final String expectedEmailAdd = new OnlineDBConnector().getUserEmail(userProfile.getUCRN());
        String strNewEmailAddress = "cea" + expectedEmailAdd;
        verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.OldEmailTextBox"), "Old Email ID Text Box",expectedEmailAdd);
        verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.PasswordTextBox"), "Password Text Box", userProfile.getPassword());
        verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.NewEmailAddress"), "New Email Address Text Box", strNewEmailAddress);
        verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.ConfirmNewEmailAddress"), "Confirm New Email Address Text Box", strNewEmailAddress);
        verifyAndClick(pageProperties.getProperty("ChangeEmailAddress.ContinueButton"), "Continue Button");
        browser.wait(getWaitTime());
        browser.wait(10000);
        if (browser.isTextPresent(pageProperties.getProperty("ChangeEmailAddress.ConfirmationMessage"))) {
            Report.updateTestLog("Email Update confirmation message '" + pageProperties.getProperty("ChangeEmailAddress.ConfirmationMessage1") + "'" + strNewEmailAddress + " exist in the confirmation screen", Pass_Str);
            if (browser.isTextPresent(pageProperties.getProperty("ChangeEmailAddress.ConfirmationMessage2"))) {
                Report.updateTestLog("Email Update confirmation message '" + pageProperties.getProperty("ChangeEmailAddress.ConfirmationMessage2") + "' exist in the confirmation screen", Pass_Str);
                if (browser.isTextPresent(strNewEmailAddress)) {
                    Report.updateTestLog("New Email Address '" + strNewEmailAddress + "' displayed in the confirmation screen", Pass_Str);
                } else {
                    Report.updateTestLog("New Email Address '" + strNewEmailAddress + "' is not displayed in the confirmation screen",Fail_Str);
                }
            } else {
                Report.updateTestLog("Email Update confirmation message '" + pageProperties.getProperty("ChangeEmailAddress.ConfirmationMessage2") + "' does not exist in the confirmation screen", Fail_Str);
            }

            if (browser.isTextPresent(pageProperties.getProperty("ChangeEmailAddress.ConfirmationMessage3"))) {
                Report.updateTestLog("Email Update confirmation message '" + pageProperties.getProperty("ChangeEmailAddress.ConfirmationMessage3") + "' exist in the confirmation screen", Pass_Str);
            } else {
                Report.updateTestLog("Email Update confirmation message '" + pageProperties.getProperty("ChangeEmailAddress.ConfirmationMessage3") + "' does not exist in the confirmation screen", Fail_Str);
            }
        } else {
            Report.updateTestLog("Change Email Address Journey Confirmation Message not found", Fail_Str);
        }
        getUserEmail(userProfile);
        new OnlineDBConnector().updateUserEmail(userProfile.getUCRN(),expectedEmailAdd);
    }
    //verify blank field errors in change email address page
    private void verifyBlankFieldsError() {
        verifyAndClick(pageProperties.getProperty("ChangeEmailAddress.ContinueButton"), "In Change Email Address page, Continue Button");
        browser.wait(2000);
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ErrorMSGHeader);
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ErrorMSGSubHeader);
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_OldEmailAddress);
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_OldPassword);
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_NewEmailAddress);
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_RetypeNewEmailAddress);
    }
    //verify error message when an WTP Customer trying to change the email address
    public void verifyWTPAccountError(UserProfile userProfile) {

        String strNewEmailAddress = "cea" + userProfile.getEmail();
        verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.OldEmailTextBox"), "Old Email ID Text Box", userProfile.getEmail());
        verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.PasswordTextBox"), "Password Text Box", userProfile.getPassword());
        verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.NewEmailAddress"), "New Email Address Text Box", strNewEmailAddress);
        verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.ConfirmNewEmailAddress"), "Confirm New Email Address Text Box", strNewEmailAddress);
        verifyAndClick(pageProperties.getProperty("ChangeEmailAddress.ContinueButton"), "Continue Button");
        browser.wait(getWaitTime());

        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_WTP_Error);
    }

    //verify error message when an WTP Customer trying to change the email address
    public void verifySSOAccountError(UserProfile userProfile) {
        final String expectedEmailAdd = new OnlineDBConnector().getUserEmail(userProfile.getUCRN());
        String strNewEmailAddress = "cea" + expectedEmailAdd;
        verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.OldEmailTextBox"), "Old Email ID Text Box",expectedEmailAdd);
        verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.PasswordTextBox"), "Password Text Box", userProfile.getPassword());
        verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.NewEmailAddress"), "New Email Address Text Box", strNewEmailAddress);
        verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.ConfirmNewEmailAddress"), "Confirm New Email Address Text Box", strNewEmailAddress);
        verifyAndClick(pageProperties.getProperty("ChangeEmailAddress.ContinueButton"), "Continue Button");
        browser.wait(getWaitTime());

        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_SSOAccountError);
    }

    //verify error message when an PrePayment Customer trying to change the email address
    public void verifyPrePaymentAccountError(UserProfile userProfile) {
        final String expectedEmailAdd = new OnlineDBConnector().getUserEmail(userProfile.getUCRN());
        String strNewEmailAddress = "cea" + expectedEmailAdd;
        verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.OldEmailTextBox"), "Old Email ID Text Box", expectedEmailAdd);
        verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.PasswordTextBox"), "Password Text Box", userProfile.getPassword());
        verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.NewEmailAddress"), "New Email Address Text Box", strNewEmailAddress);
        verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.ConfirmNewEmailAddress"), "Confirm New Email Address Text Box", strNewEmailAddress);
        verifyAndClick(pageProperties.getProperty("ChangeEmailAddress.ContinueButton"), "Continue Button");
        browser.wait(getWaitTime());

        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_PrePayment_Error);
    }
    //verify error message when an Locked account customer trying to change the email address
    public void verifyLockedAccountError(UserProfile userProfile) {
        final String expectedEmailAdd = new OnlineDBConnector().getUserEmail(userProfile.getUCRN());
        String strNewEmailAddress = "cea" + expectedEmailAdd;
        verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.OldEmailTextBox"), "Old Email ID Text Box", expectedEmailAdd);
        verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.PasswordTextBox"), "Password Text Box", userProfile.getPassword());
        verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.NewEmailAddress"), "New Email Address Text Box", strNewEmailAddress);
        verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.ConfirmNewEmailAddress"), "Confirm New Email Address Text Box", strNewEmailAddress);
        verifyAndClick(pageProperties.getProperty("ChangeEmailAddress.ContinueButton"), "Continue Button");
        browser.wait(getWaitTime());

        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_LockedAccountError);
    }
    //verify old email address field with different invalid values
    public void validateOldEmailError(UserProfile userProfile) {
        String strNewEmailAddress = "cea" + userProfile.getEmail();
        String[] getOldEmailAddress;
        getOldEmailAddress = new String[4];
        getOldEmailAddress[0] = "1234567";    //InvalidEmailID
        getOldEmailAddress[1] = "abc%^&de";   //InvalidEmailID
        getOldEmailAddress[2] = "";   //InvalidEmailID
        getOldEmailAddress[3] = "1234567@tessco.co.uk";   //InvalidEmailID
        for (int i = 0; i < 4; i++) {

            verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.OldEmailTextBox"), "Old Email Text Box", getOldEmailAddress[i]);
            verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.PasswordTextBox"), "Password Text Box", userProfile.getPassword());
            verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.NewEmailAddress"), "New Email Address Text Box", strNewEmailAddress);
            verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.ConfirmNewEmailAddress"), "Confirm New Email Address Text Box", strNewEmailAddress);
            verifyAndClick(pageProperties.getProperty("ChangeEmailAddress.ContinueButton"), "Continue Button");
            browser.wait(2000);
            if (i == 3) {
                verifyIsTextPresent(GlobalErrorMessages.NewBlue_ErrorMSG_EmailIDFieldEmailNotPresentInDB);
            } else {
                verifyIsTextPresent(GlobalErrorMessages.ReFactoring_OldEmailAddress);
            }
        }
    }
    //validate password field error message with different invalid values
    private void validatePasswordError(UserProfile userProfile) {
        String strNewEmailAddress = "cea" + userProfile.getEmail();
        String[] getPassword;
        getPassword = new String[4];
        getPassword[0] = "12";    //InvalidEmailID
        getPassword[1] = "%^&de";   //InvalidEmailID
        getPassword[2] = "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq";   //InvalidEmailID
        getPassword[3] = "";
        for (int i = 0; i < 4; i++) {
            verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.OldEmailTextBox"), "Old Email Text Box", userProfile.getEmail());
            verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.PasswordTextBox"), "Password Text Box", getPassword[i]);
            verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.NewEmailAddress"), "New Email Address Text Box", strNewEmailAddress);
            verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.ConfirmNewEmailAddress"), "Confirm New Email Address Text Box", strNewEmailAddress);
            verifyAndClick(pageProperties.getProperty("ChangeEmailAddress.ContinueButton"), "Continue Button");
            browser.wait(2000);
            if (i == 3) {
                verifyIsTextPresent(GlobalErrorMessages.ReFactoring_OldPassword);
            } else {
                verifyIsTextPresent(GlobalErrorMessages.ReFactoring_PasswordtypeError);
            }
        }
    }
    //verify error message for new email address field with different invalid values
    private void validateNewEmailAddressError(UserProfile userProfile) {
        String strNewEmailAddress = "cea" + userProfile.getEmail();
        String[] getNewEmailAddress;
        getNewEmailAddress = new String[4];
        getNewEmailAddress[0] = "1234567";    //InvalidEmailID
        getNewEmailAddress[1] = "abc%^&de";   //InvalidEmailID
        getNewEmailAddress[2] = "";   //InvalidEmailID
        getNewEmailAddress[3] = "12345 qwe";   //InvalidEmailID
        for (int i = 0; i < 4; i++) {
            verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.OldEmailTextBox"), "Old Email Text Box", userProfile.getEmail());
            verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.PasswordTextBox"), "Password Text Box", userProfile.getPassword());
            verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.NewEmailAddress"), "New Email Address Text Box", getNewEmailAddress[i]);
            verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.ConfirmNewEmailAddress"), "Confirm New Email Address Text Box", getNewEmailAddress[i]);
            verifyAndClick(pageProperties.getProperty("ChangeEmailAddress.ContinueButton"), "Continue Button");
            browser.wait(2000);
            verifyIsTextPresent(GlobalErrorMessages.ReFactoring_NewEmailAddress);
        }
    }
    //verify new confirm email address field with different invalid values
    private void validateConfirmNewEmailAddressError(UserProfile userProfile) {
        verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.OldEmailTextBox"), "Old Email Text Box", userProfile.getEmail());
        verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.PasswordTextBox"), "Password Text Box", userProfile.getPassword());
        verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.NewEmailAddress"), "New Email Address Text Box", "abc@bgdigitaltest.co.uk");
        verifyAndInputById(pageProperties.getProperty("ChangeEmailAddress.ConfirmNewEmailAddress"), "Confirm New Email Address Text Box", "abc123@bgdigitaltest.co.uk");
        verifyAndClick(pageProperties.getProperty("ChangeEmailAddress.ContinueButton"), "Continue Button");
        browser.wait(2000);
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_NewEmailHeader);
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_NewEmailError);
    }
    //Method to validate all the error messages
    public void validateErrorMessages(UserProfile userProfile) {
        verifyBlankFieldsError();
        validateOldEmailError(userProfile);
        validatePasswordError(userProfile);
        validateNewEmailAddressError(userProfile);
        validateConfirmNewEmailAddressError(userProfile);
    }
    //Method to get the Updated Email from DB
    public void getUserEmail(UserProfile userProfile) {
        String emailDB = new OnlineDBConnector().getUserEmail(userProfile.getUCRN());
        String oldEmail = "cea" + userProfile.getEmail();
        if (emailDB.equalsIgnoreCase(oldEmail)) {
            Report.updateTestLog("Email Id Updated in DB  :" + emailDB + " Ucrn  :" + userProfile.getUCRN(), Pass_Str);
        }
    }

    // Validate the back option functionality

    public void verifyBrowserBackbutton(){
        browser.browserBack();
        getWaitTime();
        browser.wait(5000);
        if (browser.isElementVisible(pageProperties.getProperty("ChangeEmailAddress.ContinueButton"))){
           Report.updateTestLog("Browser Back Button is not working as expected",Fail_Str);
        } else {
            Report.updateTestLog("Browser Back Button is working as expected",Pass_Str);
        }
    }
}

