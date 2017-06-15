package bg.framework.app.functional.page.reFactoring;

import bg.framework.app.functional.common.ApplicationConfig;
import bg.framework.app.functional.entities.UserProfile;
import bg.framework.app.functional.page.common.BasePage;
import bg.framework.app.functional.page.common.GlobalErrorMessages;
import bg.framework.app.functional.util.OnlineDBConnector;
import bg.framework.app.functional.util.PropertyLoader;
import bg.framework.app.functional.util.Report;
import bg.framework.app.functional.util.SiebelDataBase;

import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: !athimook
 * Date: 13/03/12
 * Time: 09:54
 * To change this template use File | Settings | File Templates.
 */
public class ESSCallBackPage extends BasePage {

    private final static String FILE_NAME = "resources/ReFactoring/ESSCallBack.Properties";
    private static Properties pageProperties = new PropertyLoader(FILE_NAME).load();
    private static String orderDate = new OnlineDBConnector().DBsysdate();
    final String  Pass_Str = "PASS";
    final String  Fail_Str = "FAIL";
    public ESSCallBackPage() {

    }
    public void navigateToRequestCallBackPage(){

        //verifyAndClickWithXpath(pageProperties.getProperty("EssCallBack.RequestCallBackLink"),"Request a Call Back Link");
        //verifyIsTextPresent(pageProperties.getProperty("EssCallBack.RequestPage"),"Request a call back");
        if (ApplicationConfig.BRAND.equalsIgnoreCase("BG")) {
    		browser.open(ApplicationConfig.APP_BG_URL+"/products-and-services/energy-saving/energy-savers-store.html");
			}else {
             browser.open(ApplicationConfig.APP_FUSION_URL+"/save-and-create/home-insulation.html");
        }
    }
    public void navigateToContinueShoppingPage(){
    	
    	try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        verifyAndClickWithLinkText(pageProperties.getProperty("EssCallBack.ContinueShopping"),"Continue Shopping in Confirmation Page");
        getWaitTime();
        browser.wait(3000);
        verifyIsTextPresent(pageProperties.getProperty("EssCallBack.ProductServices"),"Navigated to Products and Services Page");
    }
    public void navigateToSaveCreatePage(){
    	try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        verifyAndClickWithLinkText(pageProperties.getProperty("EssCallBack.ReturnLink"),"Return to Save and Create Link in Confirmation Page");
        getWaitTime();
        browser.wait(3000);
        verifyIsTextPresent(pageProperties.getProperty("EssCallBack.SaveCreate"),"Navigated to Save and Create Page");
    }
    public void logOut(){
    	try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        verifyAndClickWithLinkText(pageProperties.getProperty("EssCallBack.Logout"),"Log out Link");
        getWaitTime();
        browser.wait(3000);
    }
    public void selectSingleProduct(){
     for (int i=1;i<=6;i++){
     String strProduct = pageProperties.getProperty("EssCallBack.chk"+i+"txt");
     String proId = pageProperties.getProperty("EssCallBack.chk"+i);
     verifyAndSelectCheckBoxByID(proId,strProduct);
     validateESSCallBackAnonyPage();
     getWaitTime();
     verifyIsTextPresent(pageProperties.getProperty("EssCallBack.Thankyoupage"),"Thanks, we'll be calling you soon");
     String strVerify = strProduct.toLowerCase();
     verifyIsTextPresent(pageProperties.getProperty("EssCallBack.chk"+i+"txt"),strVerify);
     if (i == 1 || i == 2){
    	 verifyCallBackLoftLeadType();
     } else {
    	 verifyCallBackOtherLeadType();
     }
     if (ApplicationConfig.BRAND.equalsIgnoreCase("bg")) {
            browser.open(ApplicationConfig.APP_BG_URL+"/products-and-services/energy-savers/request-a-callback.htm");}
         else{
         browser.open(ApplicationConfig.APP_FUSION_URL+"/products-and-services/energy-savers/request-a-callback.htm");
         }
     }
    }

    public void selectSESingleProduct(){
     for (int i=1;i<=5;i++){
     String strProduct = pageProperties.getProperty("EssCallBack.SEchk"+i+"txt");
     String proId = pageProperties.getProperty("EssCallBack.SEchk"+i);
     verifyAndSelectCheckBoxByID(proId,strProduct);
     validateESSCallBackAnonyPage();
     getWaitTime();
     verifyIsTextPresent(pageProperties.getProperty("EssCallBack.Thankyoupage"),"Thanks, we'll be calling you soon");
     String strVerify = strProduct.toLowerCase();
     verifyIsTextPresent(pageProperties.getProperty("EssCallBack.SEchk"+i+"txt"),strVerify);
     if (ApplicationConfig.BRAND.equalsIgnoreCase("bg")) {
            browser.open(ApplicationConfig.APP_BG_URL);}
         else{
         browser.open(ApplicationConfig.APP_FUSION_URL);
         }
     }
    }

    public void selectMultipleProduct(){
        verifyAndSelectCheckBoxByID(pageProperties.getProperty("EssCallBack.chk1"),"Loft insulation");
        verifyAndSelectCheckBoxByID(pageProperties.getProperty("EssCallBack.chk2"),"Cavity wall insulation");
        verifyAndSelectCheckBoxByID(pageProperties.getProperty("EssCallBack.chk3"),"A-rated boiler & controls");
        verifyAndSelectCheckBoxByID(pageProperties.getProperty("EssCallBack.chk5"),"Solar PV");
//        verifyAndSelectCheckBoxByID(pageProperties.getProperty("EssCallBack.chk7"),"Home Energy Assessment");
    }

    public void selectSEMultipleProduct(){
        verifyAndSelectCheckBoxByID(pageProperties.getProperty("EssCallBack.SEchk1"),"Loft insulation");
        verifyAndSelectCheckBoxByID(pageProperties.getProperty("EssCallBack.SEchk2"),"Cavity wall insulation");
        verifyAndSelectCheckBoxByID(pageProperties.getProperty("EssCallBack.SEchk3"),"A-rated boiler & controls");
        verifyAndSelectCheckBoxByID(pageProperties.getProperty("EssCallBack.SEchk4"),"Solar PV");
//        verifyAndSelectCheckBoxByID(pageProperties.getProperty("EssCallBack.SEchk5"),"Home Energy Assessment");
    }

    public void verifyEssCallSEConfirmationPage(){
        verifyIsTextPresent(pageProperties.getProperty("EssCallBack.Thankyoupage"),"Thanks, we'll be calling you soon");
        verifyIsTextPresent(pageProperties.getProperty("EssCallBack.SEchk2txt"),"Cavity wall insulation");
        verifyIsTextPresent(pageProperties.getProperty("EssCallBack.SEchk3txt"),"A-rated boiler & controls");
        verifyIsTextPresent(pageProperties.getProperty("EssCallBack.SEchk4txt"),"Solar PV");
//        verifyIsTextPresent(pageProperties.getProperty("EssCallBack.SEchk5txt"),"Home Energy Assessment");
    }
    // verify ESS call Back page exists and OAM user can request for query
    public void validateESSCallBackPage(UserProfile userProfile) {

        String firstName = new SiebelDataBase().getFirstName(userProfile.getUCRN());
        final String lastName = new SiebelDataBase().getLastName(userProfile.getUCRN());
        final String strTitle = new SiebelDataBase().getTitle(userProfile.getUCRN());
        final String telPhoneNum = new SiebelDataBase().getTelephoneNumber(userProfile.getUCRN());
        //System.out.println(telPhoneNum);
        final String EmailId = new OnlineDBConnector().getUserEmail(userProfile.getUCRN());
        String Name = strTitle +" "+ firstName +" "+ lastName;
        String strtel = browser.getAttribute(pageProperties.getProperty("EssCallBack.OAMPhone"),"value");
        if (strtel.trim().length() > 0){
            Report.updateTestLog("Telephone number Pre Populated :"+ strtel,Pass_Str);
            } else {
            Report.updateTestLog("Telephone number not Pre Populated",Fail_Str);
        }
        String stFname = browser.getAttribute(pageProperties.getProperty("EssCallBack.Name"),"value");
        if (stFname.trim().length() > 0){
            Report.updateTestLog("Title, First Name and Last Name Pre Populated :"+stFname,Pass_Str);
            } else {
            Report.updateTestLog("Title, First Name and Last Name not Pre Populated",Fail_Str);
        }
        String strEmail = browser.getAttribute(pageProperties.getProperty("EssCallBack.OAMEmailId"),"value");
        if (strEmail.trim().length() > 0){
            Report.updateTestLog("Email Address Pre Populated :"+strEmail,Pass_Str);
            } else {
            Report.updateTestLog("Email Address not Pre Populated",Fail_Str);
        }
        verifyAndInputById(pageProperties.getProperty("EssCallBack.PostCode"), "Post Code", "al7 4hd");
        verifyAndClick(pageProperties.getProperty("EssCallBack.RequestCallBackButton"), "In ESS Call Back page, Request Call Back Button");
        getWaitTime();
        browser.wait(3000);
        }
    public void verifyEssCallConfirmationPage(){
        verifyIsTextPresent(pageProperties.getProperty("EssCallBack.Thankyoupage"),"Thanks, we'll be calling you soon");
        verifyIsTextPresent(pageProperties.getProperty("EssCallBack.chk1txt"),"Loft insulation");
        verifyIsTextPresent(pageProperties.getProperty("EssCallBack.chk2txt"),"Cavity wall insulation");
        verifyIsTextPresent(pageProperties.getProperty("EssCallBack.chk3txt"),"A-rated boiler & controls");
        verifyIsTextPresent(pageProperties.getProperty("EssCallBack.chk5txt"),"Solar PV");
//        verifyIsTextPresent(pageProperties.getProperty("EssCallBack.chk7txt"),"Home Energy Assessment");
    }
    public void verifySingleEssCallConfirmationPage(){
        verifyIsTextPresent(pageProperties.getProperty("EssCallBack.Thankyoupage"),"Thanks, we'll be calling you soon");
        verifyIsTextPresent(pageProperties.getProperty("EssCallBack.chk1txt"),"Loft insulation");
    }
     // verify ESS call Back page exists and OAM user can request for query
    public void validateESSCallBackAnonyPage() {

        verifyAndInputById(pageProperties.getProperty("EssCallBack.TelephoneNumber"), "Telephone Number","0123456789");
        verifyAndSelectDropDownBox(pageProperties.getProperty("EssCallBack.Title"), "Title", "Mr");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.FirstName"), "First Name", "Digital");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.SurName"), "Last Name", "selenium");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.EmailId"), "EmailId", "abc@bgdigitaltest.co.uk");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.PostCode"), "Post Code", "al7 4hd");
        verifyAndClick(pageProperties.getProperty("EssCallBack.RequestCallBackButton"), "In ESS Call Back page, Request Call Back Button");
        getWaitTime();
        browser.wait(3000);
    }
    public void verifyCallBackLoftLeadType(){
        String leadData=new OnlineDBConnector().getLoftLeadData(orderDate);
    	System.out.println(leadData);    	
    	Report.updateTestLog("Lead Data Generated is "+leadData, "PASS");        
    }
    public void verifyCallBackOtherLeadType(){
        String leadData=new OnlineDBConnector().getOtherLeadData(orderDate);
    	System.out.println(leadData);    	
    	Report.updateTestLog("Lead Data Generated is "+leadData, "PASS");        
    }
    
    //verify blank field errors in ESS call Back  page
    private void verifyBlankFieldsError() {
        verifyAndClick(pageProperties.getProperty("EssCallBack.RequestCallBackButton"), "In ESS Call Back page, Submit Button");
        browser.wait(2000);
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ErrorMSGHeader);
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ErrorMSGSubHeader);
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ESSProductError);
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ESSTelephoneError);
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ESSTitleError);
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ESSFirstnameError);
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ESSLastnameError);
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ESSEmailIdError);
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ESSPostCodeTypeError);
    }
    private void validateProductListError(){
        verifyAndInputById(pageProperties.getProperty("EssCallBack.TelephoneNumber"), "Telephone Number","0123456789");
        verifyAndSelectDropDownBox(pageProperties.getProperty("EssCallBack.Title"), "Title", "Mr");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.FirstName"), "First Name", "Digital");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.SurName"), "Last Name", "selenium");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.EmailId"), "EmailId", "abc@bgdigitaltest.co.uk");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.PostCode"), "Post Code", "al7 4hd");
        verifyAndClick(pageProperties.getProperty("EssCallBack.RequestCallBackButton"), "In ESS Call Back page, Request Call Back Button");
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ErrorMSGHeader);
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ErrorMSGSubHeader);
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ESSProductError);
    }

    //verify error message for Telephone Number field with different invalid values
    private void validateTelePhoneError( ) {
        String[] getTelephoneNumber;
        getTelephoneNumber = new String[4];
        getTelephoneNumber[0] = "1234567";    //InvalidPhone
        getTelephoneNumber[1] = "abc%^&de";   //InvalidPhone
        getTelephoneNumber[2] = "";   //InvalidPhone
        getTelephoneNumber[3] = "12345 qwe";   //InvalidPhone
        for (int i = 0; i < 4; i++) {
        verifyAndSelectCheckBoxByID(pageProperties.getProperty("EssCallBack.chk1"),"Loft insulation");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.TelephoneNumber"), "Telephone Number",getTelephoneNumber[i]);
        verifyAndSelectDropDownBox(pageProperties.getProperty("EssCallBack.Title"), "Title", "Mr");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.FirstName"), "First Name", "Digital");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.SurName"), "Last Name", "selenium");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.EmailId"), "EmailId", "abc@bgdigitaltest.co.uk");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.PostCode"), "Post Code", "al7 4hd");
        verifyAndClick(pageProperties.getProperty("EssCallBack.RequestCallBackButton"), "In ESS Call Back page, Request Call Back Button");
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ErrorMSGHeader);
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ErrorMSGSubHeader);
        browser.wait(2000);
        if (i==2){
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ESSTelephoneError);
        }else {
               verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ESSTelephoneTypeError);
        }
        }
    }

    private void validateTitleError(){
        verifyAndSelectCheckBoxByID(pageProperties.getProperty("EssCallBack.chk1"),"Loft insulation");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.TelephoneNumber"), "Telephone Number","0123456789");
        verifyAndSelectDropDownBox(pageProperties.getProperty("EssCallBack.Title"), "Title", "Please select");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.FirstName"), "First Name", "Digital");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.SurName"), "Last Name", "selenium");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.EmailId"), "EmailId", "abc@bgdigitaltest.co.uk");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.PostCode"), "Post Code", "al7 4hd");
        verifyAndClick(pageProperties.getProperty("EssCallBack.RequestCallBackButton"), "In ESS Call Back page, Request Call Back Button");
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ErrorMSGHeader);
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ErrorMSGSubHeader);
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ESSTitleError);
    }

    private void validateFirstNameError( ) {
        String[] getFirstName;
        getFirstName = new String[4];
        getFirstName[0] = "1234567";    //InvalidFirstNameError
        getFirstName[1] = "abc%^&de";   //InvalidFirstNameError
        getFirstName[2] = "";   //InvalidFirstNameError
        getFirstName[3] = "12345 qwe";   //InvalidFirstNameError
        for (int i = 0; i < 4; i++) {
        verifyAndSelectCheckBoxByID(pageProperties.getProperty("EssCallBack.chk1"),"Loft insulation");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.TelephoneNumber"), "Telephone Number","0123456789");
        verifyAndSelectDropDownBox(pageProperties.getProperty("EssCallBack.Title"), "Title", "Mr");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.FirstName"), "First Name", getFirstName[i]);
        verifyAndInputById(pageProperties.getProperty("EssCallBack.SurName"), "Last Name", "selenium");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.EmailId"), "EmailId", "abc@bgdigitaltest.co.uk");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.PostCode"), "Post Code", "al7 4hd");
        verifyAndClick(pageProperties.getProperty("EssCallBack.RequestCallBackButton"), "In ESS Call Back page, Request Call Back Button");
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ErrorMSGHeader);
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ErrorMSGSubHeader);
        browser.wait(2000);
        if (i==2){
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ESSFirstnameError);
        }else {
               verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ESSFirstnameTypeError);
        }
        }
    }

    private void validateLastNameError( ) {
        String[] getLastName;
        getLastName = new String[4];
        getLastName[0] = "1234567";    //InvalidLastName
        getLastName[1] = "abc%^&de";   //InvalidLastName
        getLastName[2] = "";   //InvalidLastName
        getLastName[3] = "12345 qwe";   //InvalidLastName
        for (int i = 0; i < 4; i++) {
        verifyAndSelectCheckBoxByID(pageProperties.getProperty("EssCallBack.chk1"),"Loft insulation");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.TelephoneNumber"), "Telephone Number","0123456789");
        verifyAndSelectDropDownBox(pageProperties.getProperty("EssCallBack.Title"), "Title", "Mr");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.FirstName"), "First Name", "centrica");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.SurName"), "Last Name", getLastName[i]);
        verifyAndInputById(pageProperties.getProperty("EssCallBack.EmailId"), "EmailId", "abc@bgdigitaltest.co.uk");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.PostCode"), "Post Code", "al7 4hd");
        verifyAndClick(pageProperties.getProperty("EssCallBack.RequestCallBackButton"), "In ESS Call Back page, Request Call Back Button");
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ErrorMSGHeader);
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ErrorMSGSubHeader);
        browser.wait(2000);
        if (i==2){
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ESSLastnameError);
        }else {
               verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ESSLastnameTypeError);
        }
        }
    }

    private void validateEmailaddressError( ) {
        String[] getEmail;
        getEmail = new String[4];
        getEmail[0] = "1234567";    //InvalidEmailID
        getEmail[1] = "abc%^&de@#$";   //InvalidEmailID
        getEmail[2] = "";   //InvalidEmailID
        getEmail[3] = "kathir@dfds@dfdf";   //InvalidEmailID
        for (int i = 0; i < 4; i++) {
        verifyAndSelectCheckBoxByID(pageProperties.getProperty("EssCallBack.chk1"),"Loft insulation");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.TelephoneNumber"), "Telephone Number","0123456789");
        verifyAndSelectDropDownBox(pageProperties.getProperty("EssCallBack.Title"), "Title", "Mr");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.FirstName"), "First Name", "centrica");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.SurName"), "Last Name", "Automation");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.EmailId"), "EmailId", getEmail[i]);
        verifyAndInputById(pageProperties.getProperty("EssCallBack.PostCode"), "Post Code", "al7 4hd");
        verifyAndClick(pageProperties.getProperty("EssCallBack.RequestCallBackButton"), "In ESS Call Back page, Request Call Back Button");
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ErrorMSGHeader);
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ErrorMSGSubHeader);
        browser.wait(2000);
        if (i==2){
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ESSEmailIdError);
        }else {
               verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ESSEmailTypeError);
        }
        }
    }

    private void validatePostCodeError( ) {
        String[] getPostcode;
        getPostcode = new String[4];
        getPostcode[0] = "1234567";    //InvalidPostCode
        getPostcode[1] = "abc%^&de@#$";   //InvalidPostCode
        getPostcode[2] = "";   //InvalidPostCode
        getPostcode[3] = "@#ahl32";   //InvalidPostCode
        for (int i = 0; i < 4; i++) {
        verifyAndSelectCheckBoxByID(pageProperties.getProperty("EssCallBack.chk1"),"Loft insulation");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.TelephoneNumber"), "Telephone Number","0123456789");
        verifyAndSelectDropDownBox(pageProperties.getProperty("EssCallBack.Title"), "Title", "Mr");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.FirstName"), "First Name", "centrica");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.SurName"), "Last Name", "Automation");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.EmailId"), "EmailId", "abc@bgdigitaltest.co.uk");
        verifyAndInputById(pageProperties.getProperty("EssCallBack.PostCode"), "Post Code", getPostcode[i]);
        verifyAndClick(pageProperties.getProperty("EssCallBack.RequestCallBackButton"), "In ESS Call Back page, Request Call Back Button");
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ErrorMSGHeader);
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ErrorMSGSubHeader);
        browser.wait(2000);
        if (i==2){
        verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ESSPostcodeError);
        }else {
               verifyIsTextPresent(GlobalErrorMessages.ReFactoring_ESSPostCodeTypeError);
        }
        }
    }
    //Method to validate all the error messages
    public void validateErrorMessages() {
        verifyBlankFieldsError();
        validateProductListError();
        validateTelePhoneError();
        validateTitleError();
        validateFirstNameError();
        validateLastNameError();
        validateEmailaddressError();
        validatePostCodeError();
    }
    // Validate the back option functionality

    public void verifyBrowserBackbutton(){
        browser.browserBack();
        getWaitTime();
        browser.wait(5000);
        if (browser.isElementVisible(pageProperties.getProperty("EssCallBack.RequestCallBackButton"))){
           Report.updateTestLog("Browser Back Button is not working as expected",Fail_Str);
        } else {
            Report.updateTestLog("Browser Back Button is working as expected",Pass_Str);
        }
    }
    public void navigateToEnergySaversStore(){
    	verifyAndClickWithXpath(pageProperties.getProperty("EssCallBack.ESSLink"),"Energy Savers Store ");
    }
    
    public void navigateToLHSRequestCallBack(){
    	getWaitTime();
    	if (ApplicationConfig.BRAND.equalsIgnoreCase("bg")) {
    	browser.clickWithXpath(pageProperties.getProperty("EssCallBack.BGRequestCallLink"));
    }
    	else{
    		browser.clickWithXpath(pageProperties.getProperty("EssCallBack.FusionRequestCallLink"));	
    	}
    }
    public void navigateToSELHSRequestCallBack(){
    	verifyAndClickWithXpath(pageProperties.getProperty("EssCallBack.SERequestCallbackLink"),"LHS Request a call back in SE Site ");
    }
    public void navigateToBottomRequestCallBack(){
    	verifyAndClickWithXpath(pageProperties.getProperty("EssCallBack.EssBottomLink"),"Bottom Request a call back link");
    }
    public void navigateToHeatingESSLink(){
    	verifyAndClickWithLinkText(pageProperties.getProperty("EssCallBack.HeatingESSLink"),"Energy Savers Store -> Heating Link");
    }
    public void navigateToStandbypowersaversLink(){
    	verifyAndClickWithXpath(pageProperties.getProperty("EssCallBack.StandbypowersaversLink"),"Energy Savers Store -> Standby powersavers Link");
    }
    public void navigateToGreenGadgets(){
    	verifyAndClickWithXpath(pageProperties.getProperty("EssCallBack.GreenGadgets"),"Energy Savers Store -> Green Gadgets Link");
    }
}


