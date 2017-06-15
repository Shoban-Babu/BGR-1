package bg.framework.app.functional.page.reFactoring;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import bg.framework.app.functional.action.selfServe.AccountSummaryAction;
import bg.framework.app.functional.page.common.BasePage;
import bg.framework.app.functional.util.PropertyLoader;
import bg.framework.app.functional.util.Report;


public class AccountSummaryPage extends BasePage{
	private final static String File_AccPage = "resources/ReFactoring/AccountSummary.properties";
    private static Properties pageProperties = new PropertyLoader(File_AccPage).load();
    
    public void clickSeeYourStatementLink(){
    	//browser.open("https://10.224.70.83/Your_Account/Update-PbFlag/");
    	browser.wait(2000);
    	//String link=browser.getTextByXpath(pageProperties.getProperty("AccountSummary.seeYourStatementLink").replace("NUMBER",""+1));
		//verifyAndClickWithXpath(pageProperties.getProperty("AccountSummary.seeYourStatementLink").replace("NUMBER", ""+1), link);
    	browser.clickWithXpath(pageProperties.getProperty("AccountSummary.BillingLink"));
    	browser.wait(2000);
    	browser.clickWithXpath(pageProperties.getProperty("AccountSummary.ViewBillLink"));
		//verifyPageTitle("View bill/statement");
    }
    public void clickViewAllAccounts(){
    	new AccountSummaryAction().allAccountOverviewPageAction();
    }
    public void clickGoPaperLessLink(){
    	verifyAndClickWithXpath(pageProperties.getProperty("AccountSummary.goPaperLessLink"), "Go Paperless link");
    	verifyPageTitle("Sign up for paperless billing");
    }
    
    public void verifyServiceNotDue(){
    	verifyIsTextPresent("Your annual service is not due");
    }
    
    public void verifyServiceIsDue(){
    	verifyIsTextPresent("Your annual service is due");
    }
        
    public void pbFlagLinkNonExistverification(){
    	if(browser.isElementVisibleWithXpath(pageProperties.getProperty("AccountSummary.goPaperLessLink"))){
    		Report.updateTestLog("Go Paperless link exist in page", "FAIL");
    	}else{
    		Report.updateTestLog("Go Paperless link not exist in page", "PASS");
    	}
    }
    
    public void crossCellVerification(){    	
    	ArrayList<String> accountList = new ArrayList<String>();   
    	Set<String> accountType=new HashSet<String>();
    	if(browser.isElementVisible(pageProperties.getProperty("AccountSummary.differentAccountBox"))){
    		 accountList=(browser.getFromDropBox("id", pageProperties.getProperty("AccountSummary.differentAccountBox"))); 
    	for(String account:accountList){
    	accountType.add(account.substring(0, account.indexOf('-')).trim());
    	}    	
    	}else{    		
    		accountType.add(browser.getTextByXpath(pageProperties.getProperty("AccountSummary.product")));
    	}
    	    	    
    	System.out.println("MAPS---> "+accountType);
    	if(accountType.contains("Gas") && accountType.contains("Electricity") && accountType.contains("Insurance & Repair")){
    		otherOfferCheck(pageProperties.getProperty("AccountSummary.energy"));    		
    	}else if(accountType.contains("Gas") && accountType.contains("Electricity")){
    		otherOfferCheck(pageProperties.getProperty("AccountSummary.homecare"));    		
    	}else if(accountType.contains("Electricity")){
    		otherOfferCheck(pageProperties.getProperty("AccountSummary.gas"));    		
    	}else if(accountType.contains("Gas")){
    		otherOfferCheck(pageProperties.getProperty("AccountSummary.electricity"));    		
    	}else if(accountType.contains("Energy") ){
    		otherOfferCheck(pageProperties.getProperty("AccountSummary.energy"));
    	}else if(!accountType.contains("Gas") && !accountType.contains("Electricity")){
    		otherOfferCheck(pageProperties.getProperty("AccountSummary.electricity"));
    		otherOfferCheck(pageProperties.getProperty("AccountSummary.gas"));
    	}

    }
    private void otherOfferCheck(String type){    	      	
    	browser.wait(2000);
    	boolean fail=false;   
    	ArrayList<String> crossSell=new ArrayList<String>();
    	if(type.equalsIgnoreCase("Electricity")){
    		if(browser.isElementVisibleWithXpath(pageProperties.getProperty("AccountSummary.crossCellLink1"))){
        		crossSell.add(browser.getTextByXpath(pageProperties.getProperty("AccountSummary.crossCellLink1")));    		
        	}
    	}else if(type.equalsIgnoreCase("Gas")){
    				if(browser.isElementVisibleWithXpath(pageProperties.getProperty("AccountSummary.crossCellLink2"))){
    					crossSell.add(browser.getTextByXpath(pageProperties.getProperty("AccountSummary.crossCellLink2")));    		
    				}
    	}else if(type.equalsIgnoreCase("HomeCare")){
    				if(browser.isElementVisibleWithXpath(pageProperties.getProperty("AccountSummary.crossCellLink3"))){
    					crossSell.add(browser.getTextByXpath(pageProperties.getProperty("AccountSummary.crossCellLink3")));    		
    				}
    	}else if(type.equalsIgnoreCase("Boiler")){
    				if(browser.isElementVisibleWithXpath(pageProperties.getProperty("AccountSummary.crossCellLink4"))){
    					crossSell.add(browser.getTextByXpath(pageProperties.getProperty("AccountSummary.crossCellLink4")));    		
    				}
    	}else{
    		Report.updateTestLog("Cross sell not displayed", "FAIL");
    	}
    	if(!crossSell.isEmpty()){
    		for(String crossSellText:crossSell){
    			if(crossSellText.toLowerCase().contains(type.toLowerCase())){
    				Report.updateTestLog("Cross sell section "+type+" displayed", "PASS"); 
    				fail=false;
    				break;
    				}    		
    		else{
    				fail=true;
    }
    		}
    		if(fail){
    			Report.updateTestLog("Expecteed cross sell section not displayed", "FAIL"); 
    		}
    	}
    
    	/*if(browser.isElementVisibleWithXpath(pageProperties.getProperty("AccountSummary.crossCellLink3"))){
    		crossSell.add(browser.getTextByXpath(pageProperties.getProperty("AccountSummary.crossCellLink3")));    		
    	}
    	if(browser.isElementVisibleWithXpath(pageProperties.getProperty("AccountSummary.crossCellLink3"))){
    		crossSell.add(browser.getTextByXpath(pageProperties.getProperty("AccountSummary.crossCellLink3")));
    	}
    	if(type.equalsIgnoreCase("find out more")){
    		if(browser.isElementVisibleWithXpath(pageProperties.getProperty("AccountSummary.crossCellLink3")))
    		System.out.println(browser.getTextByXpath(pageProperties.getProperty("AccountSummary.crossCellLink3")));
    		System.out.println(browser.getTextByXpath(pageProperties.getProperty("AccountSummary.crossCellLink3")).length());
    		if(browser.getTextByXpath(pageProperties.getProperty("AccountSummary.offerSection3")).length()==0 ||
    				browser.getTextByXpath(pageProperties.getProperty("AccountSummary.offerSection3")).toLowerCase().contains("new boiler")){
    			Report.updateTestLog("Customer has all accounts  <br><b>"+
    					browser.getTextByXpath(pageProperties.getProperty("AccountSummary.offerSection3")), "PASS");
    		}else{
    			Report.updateTestLog("customer owned offer displayed <br><b>"+
    					browser.getTextByXpath(pageProperties.getProperty("AccountSummary.offerSection3")), "FAIL");
    		}
    	}*/
    	
    }
    
    public void verifySSOAccount(){
    verifyIsElementVisibleWithXpath(pageProperties.getProperty("AccountSummary.conFirming"), "Confirming your order link");
    verifyIsElementVisibleWithXpath(pageProperties.getProperty("AccountSummary.energySuplyDate"), "We confirm your energy supply date");
    verifyIsElementVisibleWithXpath(pageProperties.getProperty("AccountSummary.meterReading"), "We ask you for your opening meter reading ");
    verifyIsElementVisibleWithXpath(pageProperties.getProperty("AccountSummary.setupComplete"), "Account set up is complete ");
    }
    
    public void verifyAccountSummaryPage(String accountNumber){
    	AccountSummaryAction accoutnSummaryPage=new AccountSummaryAction();
    	accoutnSummaryPage.verifyAccountSummaryPageAction(accountNumber);
    }
    
    public void logout(){
    	if (browser.isElementVisibleWithXpath(pageProperties.getProperty("AccountSummary.Logout"))){
    	verifyAndClickWithXpath(pageProperties.getProperty("AccountSummary.Logout"), "Log out link");
    	//verifyIsTextPresent("You're now logged out of your account");
    	//verifyPageTitle("Login to Your Account - British Gas");
    	}
    }
    
    public void verifyingLinksAccountSummaryPage(){
    	int count=browser.getChildElementsCountByXpath(pageProperties.getProperty("AccountSummary.accountSummaryLink").replace("[LINK]", ""));
    	System.out.println(count);
    	//browser.clickWithXpath("//div[@class='left-nav oamlefthandnav']/ul/li/ul/ul/li[1]");
    	String linkName[]={"Billing","Payments","Submit a meter reading","Energy usage","Nectar points","Update your details","Moving home",
    	"Your messages","Discover your account"};
    	for(int i=1;i<=count;i++){
    		String link=browser.getTextByXpath(pageProperties.getProperty("AccountSummary.accountSummaryLink").replace("LINK", ""+i));
    		String xpath=pageProperties.getProperty("AccountSummary.accountSummaryLink1").replace("LINK", ""+i);
    		System.out.println("LINK___ >: "+link +" -"+xpath);
    		//browser.clickWithXpath(xpath);
    		verifyAndClickWithXpath(pageProperties.getProperty("AccountSummary.accountSummaryLink1").replace("LINK", ""+i), link);
    		verifyIsTextPresent(linkName[i-1]);
    		//verifyAndClickWithXpath(pageProperties.getProperty("AccountSummary.linkName").replace("LINK", link), link);
    		browser.browserBack();
    		}
    	
    }
    
    public void navigaelinkPaperlesslink(){
    	try{
    	verifyAndClickWithXpath(pageProperties.getProperty("PBFlag.ManageAccount"), "ManageAccountLink");
    	Thread.sleep(5000);
    	verifyAndClickWithXpath(pageProperties.getProperty("PBFlag.BillingLink"), "BillingLink");
    	Thread.sleep(2000);
    	verifyAndClickWithXpath(pageProperties.getProperty("PBFlage.Viewbill"), "ViewBillLink");
    	Thread.sleep(9000);
    	
    	String status=browser.getTextByXpath(pageProperties.getProperty("PBFlag.PaperlessStatus"));
    	
    	if(status.contains("Yes"))
    	{
    		Report.updateTestLog("Paper Billing ststau is YES- Please take the valid Account status as NO","FAIL");
    	}else
    	{
    		verifyAndClickWithXpath(pageProperties.getProperty("PBFlag.Paperlesslink"), "PaperlessStatusLink");
    		Thread.sleep(2000);
    		verifyAndClick(pageProperties.getProperty("PBFlage.NectarSelection"), "NectarSelection");
    		verifyAndInputById(pageProperties.getProperty("PBFlag.NectarNumber"), "Nectar Card Number", "11111111111");
//    		verifyAndClickWithXpath(pageProperties.getProperty("PBFlag.NectarCancel"), "CancelLink");
    		verifyAndClick(pageProperties.getProperty("PBFlag.Continue"), "Continue button");
    	}
    	}catch(Exception e)
    	{
    		System.out.println("Error: "+e);
    	}
    }
public void verifyAccountSummaryPageTOU(String accountNumber) {
		AccountSummaryAction accoutnSummaryPage=new AccountSummaryAction();
    	accoutnSummaryPage.verifyAccountSummaryPageTOU(accountNumber);
		
	}
}
