package bg.framework.app.functional.page.reFactoring;

import java.util.Properties;

import com.sun.jna.platform.win32.Netapi32Util.User;

import bg.framework.app.functional.common.ApplicationConfig;
import bg.framework.app.functional.entities.UserProfile;
import bg.framework.app.functional.page.common.BasePage;
import bg.framework.app.functional.util.PropertyLoader;
import bg.framework.app.functional.util.Report;

public class PBFlagPage extends BasePage{
	private final static String FILE_NAME = "resources/ReFactoring/PBFlag.properties";
	private static Properties pageProperties = new PropertyLoader(FILE_NAME).load();
	private UserProfile userProfile;
	
	public PBFlagPage(){
		
	}
	
	public PBFlagPage(UserProfile userProfile) {
	this.userProfile=userProfile;
	}
			  
	    
	    public void clickCheckBox(){
	    	verifyAndClickWithXpath(pageProperties.getProperty("PBFlag.selectAccountBox").replace("ACCOUNTNUMBER",userProfile.getAccNumber()), "Check box for Pb Flag");
	    }
	    
	    public void clickContinueButton(){
	    	verifyAndClick(pageProperties.getProperty("PBFlag.continueButton"), "Continue");
	    }
	    
	    public void verifyPaperlessBillingSuccess(){
	    	verifyPageTitle("Paperless Billing Confirmation");
	    	verifyIsTextPresent("250");
	    	if(browser.getTextByXpath(pageProperties.getProperty("PBFlag.ConfirmationText")).contains("paperless billing")){
	    		Report.updateTestLog("Paperless billing signed up", "Pass");
	    	}
	    	else{
	    		Report.updateTestLog("Paperless billing not signed up", "Fail");
	    	}
	    }
	    
	    public void enterNectorCardNumber(){
	    	verifyAndInputById(pageProperties.getProperty("PBFlag.nectorCardNumber"), "Nector number", "11111111111");
	    }
	    
	    public void clickCardNumber(){
	    	verifyAndClick(pageProperties.getProperty("PBFlag.cardNumber"), "Nector card number");
	    }
	    
	    public void clickSignUp(){
	    	verifyAndClick(pageProperties.getProperty("PbFlag.cardSignup"), "Sign up radio button");
	    }
		public void clickNectorLater(){
			verifyAndClick(pageProperties.getProperty("PBFlag.nectorCard_later"), "nect later radio button");
		}
		public void clickNoThanks(){
			verifyAndClick(pageProperties.getProperty("PBFlag.noThanks"), "No thanks radio button");
		}
		public void clickTermsAndCondotion(){
			verifyAndClick(pageProperties.getProperty("PBFlag.termsAndCondition"), "Terms and condition check box");			
		}
		
		public void logout(){
			verifyAndClickWithXpath(pageProperties.getProperty("PBFlag.logout"), "Lou out link");
		}
}
