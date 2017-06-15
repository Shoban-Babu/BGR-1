package bg.framework.app.functional.page.reFactoring;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import net.sf.saxon.event.Stripper;

import org.hamcrest.core.IsNull;
import org.openqa.selenium.By;

import bg.framework.app.functional.entities.Ddcps;
import bg.framework.app.functional.entities.UserProfile;
import bg.framework.app.functional.page.common.BasePage;
import bg.framework.app.functional.util.OnlineDBConnector;
import bg.framework.app.functional.util.PropertyLoader;
import bg.framework.app.functional.util.Report;
import bg.framework.app.functional.util.RobotSendKeys;
import bg.framework.app.functional.util.RunQTP;
import bg.framework.common.functional.UIDriver;
import bg.framework.common.functional.WebDriverProvider;

public class DDcpsRewritePage extends BasePage {
	private final static String File_Name= "resources/reFactoring/DdcpsRewrite.properties";
	private static Properties PageProperties =new PropertyLoader(File_Name).load();
	private Ddcps ddcps;
	private UserProfile userProfile;
	public static String accType;
	public DDcpsRewritePage()
	{
		
	}
	public DDcpsRewritePage(Ddcps ddcps )
	{
		this.ddcps=ddcps;
	}
	public DDcpsRewritePage(Ddcps ddcps ,UserProfile userProfile)
	{
		this.ddcps=ddcps;
		this.userProfile=userProfile;
	}
						//----------------------------DD DashBoard-----------------------------//
	
	
	public void navigateToManageDD()
	{
		verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.ManageDD"),"ManagaeDD");
		browser.wait(1000);
		verifyPageTitle("Manage your Direct Debit");
	}
	public void errorForAccessBlocked(ArrayList<String> errList)
	{
		String implErr=browser.getTextByXpath(PageProperties.getProperty("Ddcps.ErrorInDashboard"));
		if(errList.get(0).contains(implErr))
		{
			Report.updateTestLog("CQ5 validation: The error text for Access blocked is displayed in the application"
					,"PASS");
		}
		else if("error.payment.complete".contains(implErr))
		{
			Report.updateTestLog("CQ5 validation: The error text for Payment complete is displayed in the application"
					,"PASS");
		}
	}
	
	public void checkMissedPayment()
	{
		if(browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.paymentMissedBox")))
		{
			Report.updateTestLog("The Missed payment breakdown is displayed in the application","PASS"); 
			verifyIsTextPresent("if you miss another payment ","Error For Missed Payment");
		}
		
	}
	public void detailedPlanBreakDown()
	{
		verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.PlanBreakDownLink"),"PlanBreakDown");
		//verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.PlanBreakDown"), "PlanBreakDown");
		browser.wait(1000);
		verifyPageTitle("Detailed plan of breakdown");
		checkMissedPayment();
		if(browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.PaymentsBreakDown")))
		{
			Report.updateTestLog("The detailed plan breakdown is displayed in the application", "WARN");
		}
		else
		{
			Report.updateTestLog("The detailed plan breakdown is not displayed in the application", "FAIL");
		}
		if(browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.NextreviewDate")))
		{
			String NextReviewDate=browser.getTextByXpath(PageProperties.getProperty("Ddcps.NextreviewDate"));
			Report.updateTestLog("The Next Review Date is displayed in the application"+NextReviewDate, "PASS");
		}
		
		verifyIsElementVisibleWithXpath(PageProperties.getProperty("Ddcps.BankName"), "Bank Name");
		verifyIsElementVisibleWithXpath(PageProperties.getProperty("Ddcps.AccountHolder"), "Account Holder");
		verifyIsElementVisibleWithXpath(PageProperties.getProperty("Ddcps.SortCode"), "Sort Code");
		verifyIsElementVisibleWithXpath(PageProperties.getProperty("Ddcps.AccNumber"), "Account Number");
		
		if(browser.isTextPresent(PageProperties.getProperty("Ddcps.FlexingPaymentLink")))
		{
		/*verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.FlexingPaymentLink"), "FlexingPaymentLink");
		verifyPageTitle("British Gas - Customise your payment plan");
		browser.browserBack();*/
			Report.updateTestLog("The Flexi link is displayed in the application", "PASS");
		}
		browser.wait(1000);
		if(browser.isTextPresent(PageProperties.getProperty("Ddcps.AdjustAmount")))
		{
		/*verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.AdjustAmount"), "Adjust Monthly Payment link");
		verifyPageTitle("British Gas - DDCPS Adjust Monthly Payments");
		browser.browserBack();*/
			Report.updateTestLog("The Adjust monthly link is displayed in the application", "PASS");
		}
		browser.wait(1000);
		/*verifyAndClick(PageProperties.getProperty("Ddcps.YourPlan"),"your plan tool tip");
		verifyIsElementVisibleWithXpath(PageProperties.getProperty("Ddcps.yourPlanOverlay"),"Your Plan Overlay");
		verifyIsElementVisibleWithXpath(PageProperties.getProperty("Ddcps.ClosePlanOverlay"),"close");
		*/
		if(browser.isTextPresent(PageProperties.getProperty("Ddcps.ChangeBnkDtailsViaPBD")))
		{
		verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.ChangeBnkDtailsViaPBD"), "Change Bank Details");
		verifyPageTitle("Update bank details");
		browser.browserBack();
		}
		browser.wait(1000);
		//verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.BackToManageDDLink"), "BackToManageDD");
	}
	
	public void makeAPaymentManageDD(String mkpType,String recalc)
	{
		if(browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.CustomizedPlan")))
		{
			verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.CustomizedPlan"), "Customized Plan");
			verifyIsElementVisibleWithXpath(PageProperties.getProperty("Ddcps.CustomizedOverlay"), "Customized plan overlay");
		}
		String recomenddMKP=browser.getTextByXpath(PageProperties.getProperty("Ddcps.MKPRecommended"));
		String endYearBalance=browser.getTextByXpath(PageProperties.getProperty("Ddcps.EYBalance"));
		recomenddMKP=recomenddMKP.substring(1);
		double recomenddMKPAmt=Double.parseDouble(recomenddMKP);
		/*----Reset Functionality----*/
		ddcps.setMkpAmount(5);
		recalculateMkp(recalc);
		verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.ResetMKP"), "Reset MKP");
		if(browser.getTextByXpath(PageProperties.getProperty("Ddcps.EYBalAfterPay")).isEmpty())
		{
			Report.updateTestLog("After Reset the value is nulled", "PASS");
		}else{
			Report.updateTestLog("After Reset the value is not nulled", "FAIL");
		}
		if(endYearBalance.contains("Credit"))
		{
			endYearBalance=(endYearBalance.substring(1)).replace("in Credit", "").trim();
			double endYearBal=Double.parseDouble(endYearBalance);
			System.out.println("double value of EYB"+endYearBal);
			if(recomenddMKPAmt==(Double) 0.00)
			{
				Report.updateTestLog("The account has credit balance hence recommended amount is zero", "PASS");
				System.out.println("The zero recomened and contains credit");
			}
			else{Report.updateTestLog("The account has credit balance but the recommended amount is not zero", "FAIL");}
			ddcps.setMkpAmount(5);
			recalculateMkp(recalc);
			
			
		}
		else if(endYearBalance.contains("Debit"))
		{
			Report.updateTestLog("The End of year balance is in Debit", "PASS");
			String endYearBalance2=(endYearBalance.substring(1)).replace("in Debit", "").trim();
			
			System.out.println("The recomened value is displayed and contains debit, the value alone is "+endYearBalance2 + "The length is " +endYearBalance2.length());
			double endYearBal=Double.parseDouble(endYearBalance2);
			if(recomenddMKPAmt==endYearBal)
			{
				Report.updateTestLog("The Recommended amount and the End of year balance are same", "PASS");
			}
			else{
				Report.updateTestLog("The Recommended amount and the End of year balance are not same", "FAIL");
			}
			if(mkpType=="payRecommended"&& endYearBal<2500)
			{
					ddcps.setMkpAmount(endYearBal);
					System.out.println("The endyear Bal"+ddcps.getMkpAmount());
					recalculateMkp(recalc);
					if(browser.getTextByXpath(PageProperties.getProperty("Ddcps.EYBalAfterPay")).contains("Credit"))
					{
						Report.updateTestLog("After paying recommended amount the End of year balance is in credit", "PASS");
					}else{
						Report.updateTestLog("After paying recommended the End of year balance is not in credit", "FAIL");
					}
			}
			
			if(mkpType=="payRecommended"&& endYearBal>2500)
			{
					ddcps.setMkpAmount(2488);
					recalculateMkp(recalc);
					Report.updateTestLog("Since the amount is greater than tolerance partial amount is paid", "PASS");
			}
			if(mkpType=="paySimple")
			{
				ddcps.setMkpAmount(2);
				recalculateMkp(recalc);
			}
			if(mkpType=="payGreaterThanRec" && endYearBal<2490)
			{
				endYearBal=endYearBal+1;
				ddcps.setMkpAmount(endYearBal);
				if(browser.getTextByXpath(PageProperties.getProperty("Ddcps.EYBalAfterPay")).contains("Credit"))
				{
					Report.updateTestLog("After paying a greater amount the balance is in credit", "PASS");
				}else{
					Report.updateTestLog("After paying a greater amount the balance is not changed to credit", "FAIL");
				}
			}
			
		}
		//recalculateMkp();
		/*verifyAndInputById(PageProperties.getProperty("Ddcps.MKPayAmount"), "Amount", ddcps.getMkpAmount());
		verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.RecalculatePay"), "Recalculate");
		browser.wait(10000);*/
		verifyAndSelectDropDownBox(PageProperties.getProperty("Ddcps.MakePaymentCardType"),"Card Type",ddcps.getCardType());
		verifyAndInputById(PageProperties.getProperty("Ddcps.NameOnCard"),"NameOnCard",ddcps.getCardHolderName());
		
		verifyAndInputById(PageProperties.getProperty("Ddcps.CardNumber"),"CardNumber" ,ddcps.getCardNumber());
		verifyAndSelectDropDownBox(PageProperties.getProperty("Ddcps.CardStartMonth"),"CardStartMonth",ddcps.getCardStartMonth());
		verifyAndSelectDropDownBox(PageProperties.getProperty("Ddcps.CardStartYear"),"CardStartYear",ddcps.getCardStartYear());
		verifyAndSelectDropDownBox(PageProperties.getProperty("Ddcps.CardExpiryMonth"),"CardExpiryMonth",ddcps.getCardEndMonth());
		verifyAndSelectDropDownBox(PageProperties.getProperty("Ddcps.CardExpiryYear"),"CardExpiryYear",ddcps.getCardEndYear());
		verifyAndInputById(PageProperties.getProperty("Ddcps.CardSecurityNumber"),"CardSecurityNumber" , ddcps.getCardCVV());
		verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.SubmitToMakePayment"), "MakePayment");
		
		if(browser.isTextPresent("Your payment summary"))
		{
			Report.updateTestLog("The Bank Card details are given and the Payment Summary Page is displayed","PASS");
		}
		else
		{
			Report.updateTestLog("The Payment Summary Page is not displayed","FAIL");
		}
		verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.ManagePayComplete"), "PayNow");
		//verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.SubmitVisa"), "SubmitVisa");
		browser.wait(6000);
		if(browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.3dSecureMaestro")))
		{
			//TODO get the secure code
			browser.closeAlert();
		}
		
		//if(browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.3dSecureHtml")))
		if(browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.3dsecureiframe")))
		{
			UIDriver driver = WebDriverProvider.getCurrentDriver();
			driver.switchTo().frame(driver.findElement(By.xpath(PageProperties.getProperty("Ddcps.3dsecureiframe"))));
			if(browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.3dSecureHtml")))
			{
			verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.3dSecureHtml"),"Submit 3d secure");
			}
			else if(browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.3dSecureMaestro")))
			{
				verifyAndInputByXpath(PageProperties.getProperty("Ddcps.3dSecureMaestro"), "Maestro Secure Code", ddcps.getthreedSecure());
				verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.3dSecureMaeestrSubmit"), "Maestro Submit");
			}
			browser.closeAlert();
			browser.swtichToDefaultContent();
			
		}
		if(browser.isTextPresent("Your card verification"))
		{
			verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.3dSecureHtml"),"Submit 3d secure");
			browser.wait(2000);
			browser.closeAlert();
		}
		verifyIsTextPresent(PageProperties.getProperty("Ddcps.MKPsuccessText"), "Payment Success");
		verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.BackToManageDDLink"), "BackToManageDD");
		
		browser.wait(3000);
	}
	
	public void recalculateMkp(String recalc)
	{
		
		System.out.println("The amount is inside reclac"+""+Math.round(ddcps.getMkpAmount()));
		//verifyAndInputById(PageProperties.getProperty("Ddcps.MKPayAmount"), "Amount", ""+(Math.round(ddcps.getMkpAmount())+1));
		browser.executeScript("$('#amountToPay').val('1')");
		//browser.click(PageProperties.getProperty("Ddcps.MKPayAmount"));
		//browser.executeScript("");
		if(recalc!="withoutRecalc")
		{
			verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.RecalculatePay"), "Recalculate");
		}
		else if (recalc=="withoutRecalc")
		{
			Report.updateTestLog("The Recalculate button is not clicked", "PASS");
		}
		browser.wait(3000);
	}
	
												//Credit Refund//
	
	public void navigateToCreditRefund(UserProfile userProfile,ArrayList<String> errList)
	{
		verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.GetCreditRefundLink"), "CreditRefund");
		if(browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.SMROverlay"))
				&&!browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.SMRElecDayDial")))
		{
			Report.updateTestLog("The account is a smart meter type account, overlay with no dials displayed", "WARN");
			browser.clickWithLinkText("Cancel");
		}
		else if(browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.SMROverlay"))
				&&browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.SMRElecDayDial")))
		{
			Report.updateTestLog("The account is a smart meter type account, overlay with dials are displayed", "WARN");
			getMeterRead(userProfile);
		}
		else if(browser.isTextPresent(errList.get(3)))
		{
			Report.updateTestLog("The account has no credit balance to get Refund", "WARN");
		}
		else{
		verifyPageTitle("Get a credit refund");}	
		
	}
	
	public void navigateToSubmitMeterRead(UserProfile userProfile)
	{
		System.out.println("The acc num"+userProfile.getAccNumber());
		verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.SMRLink"), "Submit A Meter Read");
		verifyIsElementVisibleWithXpath(PageProperties.getProperty("Ddcps.SMROverlay"), "SMR overlay");
		if(browser.isElementVisibleWithXpath((PageProperties.getProperty("Ddcps.SMRAccountSelect")).replace("NUMBER",userProfile.getAccNumber())))
		{
			Report.updateTestLog("The account selector for Submitting meter reading is displayed", "PASS");
			if(browser.isElementVisibleWithXpath((PageProperties.getProperty("Ddcps.SMRElecDayDial")).replace("//input", "")))
			{
			getMeterRead(userProfile);
			}
		}
		else{
			Report.updateTestLog("The account selector for Submitting meter reading is not displayed chances of being a smart account", "FAIL");
			verifyIsElementVisibleWithXpath(PageProperties.getProperty("Ddcps.SMRErrForSmart"), "No dials Reason");
			browser.clickWithLinkText("Cancel");
		}
	}
	
	public void manageDDErrorForDebitCustomer()
	{
		verifyIsTextPresent("not in credit");
	}
	public void GARHigherThanToleranceErrmessage()
	{	/*if(browser.isTextPresent("the refund amount you've requested is too high")){
		Report.updateTestLog("The Error message for high than tolerance is displayed", "PASS");*/
		verifyIsTextPresent("too high", "Higher Than Tolerance");
	}

	public void manageDDmeterReadOverlay()
	{	
		if(browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.SmrOverlay")))
		{
		Report.updateTestLog("The meter Read overlay is displayed for the account", "PASS");
		String prevReadText1 = null;
		String prevReadText2 = null;
		if(browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.MeterPreRead1")))
		{
			prevReadText1 = browser.getTextByXpath(PageProperties.getProperty("Ddcps.MeterPreRead1"));
			int read1=Integer.parseInt(prevReadText1);
			read1=read1+1;
			String newMeterRead1=String.format("%05d",read1);
			String meterRead1[]=newMeterRead1.split("");
			for(int j=1;j<=5;j++)
			{	
				verifyAndInputByXpath((PageProperties.getProperty("Ddcps.Meterdial1")+"["+j+"]"), "meterRead", meterRead1[j]);
			}
		}
		if(browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.MeterPreRead2")))
		{
			prevReadText2 = browser.getTextByXpath(PageProperties.getProperty("Ddcps.MeterPreRead2"));
			int read2=Integer.parseInt(prevReadText2);
			read2=read2+1;
			String newMeterRead2=String.format("%05d",read2);
			String meterRead2[]=newMeterRead2.split("");
			for(int j=1;j<=5;j++)
			{	
				verifyAndInputByXpath((PageProperties.getProperty("Ddcps.Meterdial2")+"["+j+"]"), "meterRead", meterRead2[j]);
			}
		}
		/*if(browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.PreReadDualElec")))
		{
			String prevElecDualRead= browser.getTextByXpath(PageProperties.getProperty("Ddcps.PreReadDualElec"));
			int readElec=Integer.parseInt(prevElecDualRead);
			readElec=readElec+1;
			String newElecDualMeterRead=String.format("%05d",readElec);
			String meterReadDual[]=newElecDualMeterRead.split("");
			for(int i=1;i<=5;i++)
			{	
				browser.wait(1000);
				verifyAndInputByXpath((PageProperties.getProperty("Ddcps.DualElecDial")+"["+i+"]"), "meterReadElec", meterReadDual[i]);
			}
			
		}
		*/
		verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.SmrSubmit"), "Continue Submit meter read");
	}
	else{
		Report.updateTestLog("The meter Read overlay is not displayed for the account", "PASS");
	}
	}
	
	public void navigateToUpdateBankDetails()
	{
		verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.UpdateBankDetailsLink"),"Change Bank details");
		browser.wait(1000);
		verifyPageTitle("Update bank details");
	}
	
	
	public void navigateToFlexiPaymentPlan()
	{
		verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.FlexiPaymentPlan"),"Flex your payment plan");
		browser.wait(1000);
		if(browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.SMROverlay"))
				&&!browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.SMRElecDayDial")))
		{
			Report.updateTestLog("The account is a smart meter type account, overlay with no dials displayed", "WARN");
			browser.clickWithLinkText("Cancel");
		}
		else if(browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.SMROverlay"))
				&& browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.SMRElecDayDial")))
		{
			Report.updateTestLog("The account is a smart meter type account, overlay with dials are displayed", "WARN");
			getMeterRead(userProfile);
		}
		else{
		verifyPageTitle("Adjust monthly payment amount");}
		verifyPageTitle("Flex your payment plan");
	}
	
	public void navigateToMkp()
	{
		verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.MakeAPaymentLink"),"Make A Payment");
		browser.wait(3000);
		verifyPageTitle("Make a payment");
	}
	public void navigateToAdjustMonthly()
	{
		browser.wait(3000);
		verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.AdjustMonthlyPaymentLink"),"Adjust Monthly Payment");
		browser.wait(1000);
		if(browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.SMROverlay"))
				&&!browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.SMRElecDayDial")))
		{
			Report.updateTestLog("The account is a smart meter type account, overlay with no dials displayed", "WARN");
			browser.clickWithLinkText("Cancel");
		}
		else if(browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.SMROverlay"))
				&& browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.SMRElecDayDial")))
		{
			Report.updateTestLog("The account is a smart meter type account, overlay with dials are displayed", "WARN");
			getMeterRead(userProfile);
		}
		else{
		verifyPageTitle("Adjust monthly payment amount");}
	}
	public void verifyCancelOverlay(String journey)
	{
		verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.CancelLink"), "Cancel");
		verifyIsElementVisibleWithXpath(PageProperties.getProperty("Ddcps.CancelOveraly"), "Overlay content for"+journey);
		verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.CancelOverlayNo"), "No");
		verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.CancelLink"), "Cancel");
		verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.CancelOveralyYes"), "Yes");
		verifyPageTitle("Manage your Direct Debit");
		verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps."+journey), journey);
		
	}
	
	public void credRefundAmount(String refundRange)
	{
		if(browser.isElementVisibleWithXpath(PageProperties.getProperty("DdcpsGAR.PaymentBox")))
		{
			Report.updateTestLog("The Credit Refund page is displayed in the application", "PASS");
			browser.wait(1000);
			String maxTolerance=browser.getTextByXpath(PageProperties.getProperty("DdcpsGAR.MaxTolerance"));
			String recommendedRefund=(browser.getTextByXpath(PageProperties.getProperty("DdcpsGAR.RecommendedPay")));
			System.out.println("After replacement "+recommendedRefund);
			System.out.println("After replacement "+maxTolerance);
			recommendedRefund=recommendedRefund.substring(1);
			maxTolerance=maxTolerance.substring(5);
			double maxToleranceInt=Double.parseDouble(maxTolerance.replace(")", ""));
			double recommendedRefundInt=Double.parseDouble(recommendedRefund.replace("£", ""));
			System.out.println("The Maximum Tolerance is "+maxToleranceInt);
			System.out.println("The recommended amount is "+recommendedRefundInt);
			System.out.println("The less than tolerance  value is"+(maxToleranceInt-1));
			System.out.println("The GreaterThanTolerance  value is"+(maxToleranceInt+ 1));
			if(refundRange=="LessThanTolerance")
			{
				verifyAndInputByXpath(PageProperties.getProperty("DdcpsGAR.PaymentBox"), "Amount",""+(Math.round(maxToleranceInt-1)));
			}
			else if(refundRange=="SimpleRefund")
			{
				verifyAndInputByXpath(PageProperties.getProperty("DdcpsGAR.PaymentBox"), "Amount",""+Math.round(1.00));
			}
			else if(refundRange=="Recommended")
			{
				if(recommendedRefund=="£0.00")
				{
					Report.updateTestLog("The Recommended amount is Null so the Refund for that amount is not possible", "WARN");
				}
				else{
				verifyAndInputByXpath(PageProperties.getProperty("DdcpsGAR.PaymentBox"), "Amount",""+Math.round(recommendedRefundInt));
				}
			}
			else if(refundRange=="GreaterThanTolerance")
			{
				verifyAndInputByXpath(PageProperties.getProperty("DdcpsGAR.PaymentBox"), "Amount",""+(Math.round(maxToleranceInt+1)));
				
			}
			verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.RecalculateRefund"), "Recalculate");
			browser.wait(5000);
			GARHigherThanToleranceErrmessage();
			confirmCredRefund();
		}
		else{
			Report.updateTestLog("The Credit Refund page is not displayed in the application", "WARN");}
			//verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.BackToManageDDLink"), "BackToManageDD");		}
	}

	public void checkGARResetFunc()
	{
		if(browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.ResetGARefund")))
		{
			verifyAndInputByXpath(PageProperties.getProperty("Ddcps.RefundAmount"), "Amount",""+0.01);
			verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.RecalculateRefund"), "Recalculate");
			browser.wait(5000);
			verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.ResetGARefund"), "Reset GetARefund");
			verifyIsElementVisibleWithXpath(PageProperties.getProperty("Ddcps.InactiveRefundButton"), "Inactive Get Refund Button");
			System.out.println("The value is"+browser.getTextByXpath(PageProperties.getProperty("Ddcps.AfterRefundCredit")));
			if(browser.getTextByXpath(PageProperties.getProperty("Ddcps.AfterRefundCredit")).isEmpty())
			{
				Report.updateTestLog("The After refund credit value is Empty", "PASS");
			}else{
				Report.updateTestLog("The After refund credit value is Not Empty", "FAIL");
			}
			System.out.println("The value is"+browser.getTextByXpath(PageProperties.getProperty("Ddcps.AfterRefundAmount")));
			if(browser.getTextByXpath(PageProperties.getProperty("Ddcps.AfterRefundAmount")).isEmpty())
			{
				Report.updateTestLog("The After refund Payment amount is Empty", "PASS");
			}else{
			Report.updateTestLog("The After refund Payment amount is Not Empty", "FAIL");
			}
		}
		else{
			Report.updateTestLog("The reset link in GAR is not displayed ", "WARN");
		}
	}
	
	public void confirmCredRefund()
	{
		if(browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.RefundButton")))
		{
		verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.RefundButton"), "GetRefund");
		if(browser.isElementVisibleWithXpath(PageProperties.getProperty("DdcpsGAR.ConfirmOverlay")))
		{
			Report.updateTestLog("The Confirm Refund Overlay is displayed", "PASS");
			verifyAndClickWithXpath(PageProperties.getProperty("DDCPSGAR.ConfirmOverlaySubmit"), "ConfirmOverlay");
		}
		else{
			Report.updateTestLog("The Confirm Refund Overlay is not displayed", "FAIL");
		}
		browser.wait(1000);
		}
		//verifyPageTitle("Manage your Direct Debit");
		
		verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.BackToManageDDLink"), "BackToManageDD");
	}
	
	/*public void manageDDCreditRefund()
	{
		verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.GetCreditRefundLink"), "CreditRefund");
		manageDDmeterReadOverlay();
		if(browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.RefundAmount")))
		{
			Report.updateTestLog("The Credit Refund page is displayed in the application", "PASS");
			verifyAndInputByXpath(PageProperties.getProperty("Ddcps.RefundAmount"), "Amount",ddcps.getRefundAmount());
			browser.wait(1000);
			verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.RecalculateRefund"), "Recalculate");
			browser.wait(5000);
			
			checkGARResetFunc();
			
			verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.RefundButton"), "GetRefund");
			if(browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.ConfirmRefundOverlay")))
					{
				Report.updateTestLog("The Confirm Refund Overlay is displayed", "PASS");
				verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.ConfirmRefundOverlay"), "ConfirmOverlay");
					}
			else{
				Report.updateTestLog("The Confirm Refund Overlay is not displayed", "PASS");
			}
			browser.wait(1000);
			verifyPageTitle("British Gas - Manage Direct Debit Confirmation");
			
			verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.BackToManageDDLink"), "BackToManageDD");
		}
	}*/
	
	public void manageDDUpdatebankDetails()
	{
		verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.UpdateBankDetailsLink"), "UpdateBankDetails");
		//verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.UpdateBankDetails"), "UpdateBankDetails");
		inputBankDetailsWithoutTerms();
	}
	
	public void inputBankDetailsWithoutTerms()
	{
		browser.wait(4000);
		if(browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.UpdateFirstName")))
		{
			Report.updateTestLog("The Update Bank Details Page is displayed in the application","PASS");
			verifyAndInputByXpath(PageProperties.getProperty("Ddcps.UpdateFirstName"), "UpdateFirstName",ddcps.getUpdateFirstName());
			verifyAndInputByXpath(PageProperties.getProperty("Ddcps.UpdateLastName"), "UpdateLastName",ddcps.getUpdateLastName());
			
			/*verifyAndClick(PageProperties.getProperty("Ddcps.UpdateDateOfBirth"), "DateOfBirth");
			selectDataFromDropDownBox(ddcps.getUpdateDOB());
			verifyAndClick(PageProperties.getProperty("Ddcps.UpdateMonthOfBirth"), "MonthOfBirth");
			selectDataFromDropDownBox(ddcps.getUpdateMOB());
			verifyAndClick(PageProperties.getProperty("Ddcps.UpdateYearOfBirth"), "YearOfBirth");
			selectDataFromDropDownBox(ddcps.getUpdateYOB());*/
			
			verifyAndSelectDropDownBoxByXpath(PageProperties.getProperty("Ddcps.UpdateDateOfBirth"), "Day Of Birth", ddcps.getUpdateDOB());
			verifyAndSelectDropDownBoxByXpath(PageProperties.getProperty("Ddcps.UpdateMonthOfBirth"), "Month Of Birth", ddcps.getUpdateMOB());
			verifyAndSelectDropDownBoxByXpath(PageProperties.getProperty("Ddcps.UpdateYearOfBirth"), "Year Of Birth", ddcps.getUpdateYOB());
			
			verifyAndInputByXpath(PageProperties.getProperty("Ddcps.UpdateSortCode1"), "UpdateSortCode1",ddcps.getUpdateSortCode1());
			verifyAndInputByXpath(PageProperties.getProperty("Ddcps.UpdateSortCode2"), "UpdateSortCode2",ddcps.getUpdateSortCode2());
			verifyAndInputByXpath(PageProperties.getProperty("Ddcps.UpdateSortCode3"), "UpdateSortCode3",ddcps.getUpdateSortCode3());
			verifyAndInputByXpath(PageProperties.getProperty("Ddcps.UpdateBankAccountNumber"), "UpdateAccountNumber",ddcps.getUpdateAccountNumber());
			verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.UpdateDDTerms"),"Terms And conditions");
			verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.SubmitUpdate"), "SaveBankDetails");
			verifyIsElementVisibleWithXpath(PageProperties.getProperty("Ddcps.InvalidDataErr"), "Error message Third time");
			verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.BackToManageDDLink"), "BackToManageDD");
			browser.wait(3000);
			
		}
		else
		{
			Report.updateTestLog("The Update Bank Details Page is not displayed in the application","FAIL");
		}
	}
	
	public void inputBankDetails()
	{
		browser.wait(4000);
		
		if(browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.UpdateFirstName")))
		{
			Report.updateTestLog("The Update Bank Details Page is displayed in the application","PASS");
			verifyAndInputByXpath(PageProperties.getProperty("Ddcps.UpdateFirstName"), "UpdateFirstName",ddcps.getUpdateFirstName());
			verifyAndInputByXpath(PageProperties.getProperty("Ddcps.UpdateLastName"), "UpdateLastName",ddcps.getUpdateLastName());
			
			verifyAndSelectDropDownBoxByXpath(PageProperties.getProperty("Ddcps.UpdateDateOfBirth"), "Day Of Birth", ddcps.getUpdateDOB());
			verifyAndSelectDropDownBoxByXpath(PageProperties.getProperty("Ddcps.UpdateMonthOfBirth"), "Month Of Birth", ddcps.getUpdateMOB());
			verifyAndSelectDropDownBoxByXpath(PageProperties.getProperty("Ddcps.UpdateYearOfBirth"), "Year Of Birth", ddcps.getUpdateYOB());
			
			//verifyAndInputByXpath(PageProperties.getProperty("Ddcps.UpdateSortCode1"), "UpdateSortCode1",ddcps.getUpdateSortCode1());
			//verifyAndInputByXpath(PageProperties.getProperty("Ddcps.UpdateSortCode2"), "UpdateSortCode2",ddcps.getUpdateSortCode2());
			//verifyAndInputByXpath(PageProperties.getProperty("Ddcps.UpdateSortCode3"), "UpdateSortCode3",ddcps.getUpdateSortCode3());
			//verifyAndInputByXpath(PageProperties.getProperty("Ddcps.UpdateBankAccountNumber"),"UpdateAccountNumber",ddcps.getUpdateAccountNumber());
			browser.executeScript("$('#sortCode1').val('"+ddcps.getUpdateSortCode1()+"')");
			browser.executeScript("$('#sortCode2').val('"+ddcps.getUpdateSortCode2()+"')");
			browser.executeScript("$('#sortCode3').val('"+ddcps.getUpdateSortCode3()+"')");
			String JS="$('#bankAccountNumber').val('"+ddcps.getUpdateAccountNumber()+"')";
			browser.executeScript(JS);
			
			verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.UpdateDDTerms"),"DDGuarenteeTerms");
			verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.SubmitUpdate"), "SaveBankDetails");
			updateConfirmaionOverlay();
			verifyPageTitle("Change bank details confirmation");
			updateBDConfirmationPage();
			verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.BackToManageDDLink"), "BackToManageDD");
			browser.wait(3000);
			
		}
		else
		{
			Report.updateTestLog("The Update Bank Details Page is not displayed in the application","FAIL");
		}
	}
	
	public void updateConfirmaionOverlay()
	{
		verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.ConfirmationOverlayYes"),"Save Changes");
	}
	public void updateBDConfirmationPage()
	{
		verifyIsTextPresent(PageProperties.getProperty("Ddcps.UpdateDoneText"),"The Bank details are updated and the confirmation page is displayed " );
		/*if(browser.isTextPresent(PageProperties.getProperty("Ddcps.ManageDDLink")))
		{
			verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.ManageDDLink"), "Manage Direct Debit");
			browser.wait(1000);
			browser.browserBack();
		}else{
			Report.updateTestLog("The link "+PageProperties.getProperty("Ddcps.ManageDDLink")+"is not displayed", "FAIL");
		}
		if(browser.isTextPresent(PageProperties.getProperty("Ddcps.ViewAccSummary")))
		{
			verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.ViewAccSummary"), "View Account Summary");
			browser.wait(1000);
			browser.browserBack();
		}else{
			Report.updateTestLog("The link "+PageProperties.getProperty("Ddcps.ViewAccSummary")+" is not displayed", "FAIL");
		}
		if(browser.isTextPresent(PageProperties.getProperty("Ddcps.ViewRecentBill")))
		{
			verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.ViewRecentBill"), "View Recent bill");
			browser.wait(1000);
			browser.browserBack();
		}else{
			Report.updateTestLog("The link "+PageProperties.getProperty("Ddcps.ViewRecentBill")+" is not displayed", "FAIL");
		}*/
	}
	
	
	public void verifyUpdatedDetails()
	{
		browser.wait(5000);
		verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.PlanBreakDownLink"),"PlanBreakDown");
		browser.wait(1000);
		verifyPageTitle("Detailed plan of breakdown");
		if(browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.PaymentsBreakDown")))
		{
			Report.updateTestLog("The detailed plan breakdown is displayed in the application", "PASS");
		}
		else
		{
			Report.updateTestLog("The detailed plan breakdown is not displayed in the application", "FAIL");
		}
		if(browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.AccNumber")))
		{
			String hiddenSortcode=browser.getTextByXpath(PageProperties.getProperty("Ddcps.SortCode"));
			System.out.println("THe sort code displayed in the application is "+hiddenSortcode);
			if(browser.getTextByXpath(PageProperties.getProperty("Ddcps.SortCode")).contains("XX-XX-XX"))
				{
			Report.updateTestLog("The Sort Code is displayed and it is hidden","PASS");
				}
			else{
			Report.updateTestLog("The hidden Sort Code is not displayed","FAIL");
			}
			verifyIsElementVisibleWithXpath(PageProperties.getProperty("Ddcps.AccNumber"), "Account Number");
			String accholdrName=browser.getTextByXpath(PageProperties.getProperty("Ddcps.AccountHolder"));
			System.out.println("The account holder name displayed in the app is"+accholdrName);
			System.out.println("The combined first last name is "+ddcps.getBankFirstName()+" "+ddcps.getBankLastName() );
			if(browser.getTextByXpath(PageProperties.getProperty("Ddcps.AccountHolder")).contains(ddcps.getBankFirstName()+" "+ddcps.getBankLastName()))		{
			Report.updateTestLog("The Account holder name is displayed and it is updated","PASS");
			}
			else{
			Report.updateTestLog("The updated Account holder name is not displayed","FAIL");
			}
		}
		else{
			Report.updateTestLog("The bank details are not displayed in the application", "FAIL");
		}
		verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.BackToManageDDLink"), "BackToManageDD");
		browser.wait(3000);
	}
	
	public void updateBDViaPlanBreakdown()
	{
		browser.wait(5000);
		verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.PlanBreakDownLink"),"PlanBreakDown");
		browser.wait(1000);
		verifyPageTitle("Detailed plan of breakdown");
		if(browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.PaymentsBreakDown")))
		{
			Report.updateTestLog("The detailed plan breakdown is displayed in the application", "PASS");
		}
		else
		{
			Report.updateTestLog("The detailed plan breakdown is not displayed in the application", "FAIL");
		}
		verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.ChangeBnkDtailsViaPBD"),"Change Bank Details from plan breakdown");
		/*if(browser.getTextByXpath(PageProperties.getProperty("Ddcps.CustRefNumber")).contains(userProfile.getAccNumber()))
		{
			Report.updateTestLog("The Customer Reference Number is displayed in the application", "PASS");
		}
		else{
			Report.updateTestLog("The Customer Reference Number is not displayed in the application", "FAIL");
		}*/
		inputBankDetails();
	}
	
	public void manageDDFlexiPaymentPlan()
	{
		verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.FlexiPaymentPlan"), "FlexiPaymentPlan");
		String Amount=null;
		int i=1;
		do
		{
			if(browser.isElementVisibleWithXpath("//div["+i+"]"+PageProperties.getProperty("DdcpsFlexi.UnpaidAmountMonth")))
			{
				Amount=browser.getAttributeByXpath("//div["+i+"]"+PageProperties.getProperty("DdcpsFlexi.UnpaidAmountMonth"),  "value");
				if(Amount!="0")
				{
					verifyAndClick(PageProperties.getProperty("DdcpsFlexi.PayBreak1"), "PayBreak1");
					selectDataFromDropDownBox(i);
					browser.wait(1000);
				}
			}
			i++;
		}while(Amount=="0"||Amount==null);
		
		do
		{
			if(browser.isElementVisibleWithXpath("//div["+i+"]"+PageProperties.getProperty("DdcpsFlexi.UnpaidAmountMonth")))
			{
				Amount=browser.getAttributeByXpath("//div["+i+"]"+PageProperties.getProperty("DdcpsFlexi.UnpaidAmountMonth"),  "value");
				if(Amount!="0")
				{
				verifyAndClick(PageProperties.getProperty("DdcpsFlexi.PayBreak2"), "PayBreak2");
				selectDataFromDropDownBox(i);
					browser.wait(1000);
				}}
			i++;
		}while(Amount=="0");
		
		
		verifyIsElementVisibleById(PageProperties.getProperty("DdcpsFlexi.PlanChangeTable"), "Plan Change Overview");
		verifyAndClick(PageProperties.getProperty("DdcpsFlexi.FlexiTerms"), "FlexiTerms");
		verifyAndClickWithXpath(PageProperties.getProperty("DdcpsFlexi.ChangePlan"), "ChangePlan");
		
		if(browser.isElementVisible(PageProperties.getProperty("DdcpsFlexi.Overlay")))
				{
			verifyAndClickWithXpath(PageProperties.getProperty("DdcpsFlexi.SaveChange"), "Save");
				}
		verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.BackToManageDDLink"), "BackToManageDD");
		browser.wait(3000);
	}
	
	
	public void nextInstallmentDetailsVerification()
	{
		verifyIsElementVisibleWithXpath(PageProperties.getProperty("Ddcps.NextInsDay"), "Next installment Day");
		verifyIsElementVisibleWithXpath(PageProperties.getProperty("Ddcps.NextInsMonth"), "Next Installment Month");
		verifyIsElementVisibleWithXpath(PageProperties.getProperty("Ddcps.NextInsAmount"), "Next Installment Amount");
		verifyIsElementVisibleWithXpath(PageProperties.getProperty("Ddcps.NextInsFullDate"), "Next Installment Full Date");
		verifyIsElementVisibleWithXpath(PageProperties.getProperty("Ddcps.NextInsBalAmt"), "Account Balance");
		verifyIsTextPresent(PageProperties.getProperty("Ddcps.detailedbreakdownLink"), "BreakDown Link");
	}
	
	public void invalidDetailsErrMessageValidation(ArrayList<String> errList)
	{
		String CBDError1,CBDExceedsError;
		verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.UpdateBankDetailsLink"),"Change Bank details");
		browser.wait(1000);
		verifyPageTitle("Update bank details");
		for(int i=1;i<5;i++)
		{
			incorrectData();
			CBDError1=browser.getTextByXpath(PageProperties.getProperty("Ddcps.ErrorForCBD"));
			if(i==4)
			{
				new RegistrationPage().verifyErrorMsg(CBDError1,"validation.attempt.exceeds","CBD Try Exceeds" );
				}
			else
			{
				//new RegistrationPage().verifyErrorMsg(CBDError1, errList.get(3),"Invalid Direct Debit Details" );
				new RegistrationPage().verifyErrorMsg(CBDError1, errList.get(2),"Invalid Direct Debit Details" );
			}
			
		}
		incorrectData();
		CBDExceedsError=browser.getTextByXpath(PageProperties.getProperty("Ddcps.ErrorForCBD"));
		new RegistrationPage().verifyErrorMsg(CBDExceedsError, errList.get(2),"Change Bank details Exceeds Limit");
	}
	
	public void incorrectData()
	{
		verifyAndInputByXpath(PageProperties.getProperty("Ddcps.UpdateFirstName"), "UpdateFirstName",ddcps.getUpdateFirstName());
		verifyAndInputByXpath(PageProperties.getProperty("Ddcps.UpdateLastName"), "UpdateLastName",ddcps.getUpdateLastName());
		verifyAndSelectDropDownBoxByXpath(PageProperties.getProperty("Ddcps.UpdateDateOfBirth"), "Day Of Birth", ddcps.getUpdateDOB());
		verifyAndSelectDropDownBoxByXpath(PageProperties.getProperty("Ddcps.UpdateMonthOfBirth"), "Month Of Birth", ddcps.getUpdateMOB());
		verifyAndSelectDropDownBoxByXpath(PageProperties.getProperty("Ddcps.UpdateYearOfBirth"), "Year Of Birth", ddcps.getUpdateYOB());
		//verifyAndInputByXpath(PageProperties.getProperty("Ddcps.UpdateSortCode1"), "UpdateSortCode1",ddcps.getUpdateSortCode1());
		//verifyAndInputByXpath(PageProperties.getProperty("Ddcps.UpdateSortCode2"), "UpdateSortCode2",ddcps.getUpdateSortCode2());
		//verifyAndInputByXpath(PageProperties.getProperty("Ddcps.UpdateSortCode3"), "UpdateSortCode3",ddcps.getUpdateSortCode3());
		//verifyAndInputByXpath(PageProperties.getProperty("Ddcps.UpdateBankAccountNumber"), "UpdateAccountNumber","01234568");
		browser.executeScript("$('#sortCode1').val('"+ddcps.getUpdateSortCode1()+"')");
		browser.executeScript("$('#sortCode2').val('"+ddcps.getUpdateSortCode2()+"')");
		browser.executeScript("$('#sortCode3').val('"+ddcps.getUpdateSortCode3()+"')");
		String JS="$('#bankAccountNumber').val('01234568')";
		browser.executeScript(JS);
		browser.wait(1000);
		if(!browser.isSelected(PageProperties.getProperty("Ddcps.UpdateDDTerms")))
		{
		verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.UpdateDDTerms"),"DDGuarenteeTerms");
		}
		verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.SubmitUpdate"), "SaveBankDetails");
	}
	
	public void getMeterRead(UserProfile userProfile)
	{
		System.out.println("account number is "+userProfile.getAccNumber());
		if(browser.isElementVisibleWithXpath((PageProperties.getProperty("Ddcps.SMRAccountSelect")).replace("NUMBER",userProfile.getAccNumber())))
		{
			Report.updateTestLog("The account selector for Submitting meter reading is displayed", "PASS");
			if(!browser.isSelected((PageProperties.getProperty("Ddcps.SMRAccountSelect")).replace("NUMBER",userProfile.getAccNumber())))
			{
			verifyAndClickWithXpath((PageProperties.getProperty("Ddcps.SMRAccountSelect")).replace("NUMBER",userProfile.getAccNumber()), "Account Selector");
			}
		}
		
		 String estMeterRead1=null,estMeterRead2=null,estMeterRead3=null,estMeterRead4=null;
		 System.out.println("RUN QTP");
         RunQTP runQTP = new RunQTP();
         runQTP.runQTP("BGRegression\\src\\bg\\framework\\app\\functional\\util\\vbsScripts\\GetEstimated.vbs",userProfile.getAccNumber());
         browser.wait(15000);
         File file1 = new File("C:\\SAPData\\estimatedmeterread.txt");
         FileReader fr = null;
		try {
			fr = new FileReader(file1);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);
		try {
			estMeterRead1=(br.readLine()).replace("GasMeter Read1=", "");
			estMeterRead2=(br.readLine()).replace("Gas Meter Read2=", "");
			estMeterRead3=(br.readLine()).replace("ElecMeter Read1=", "");
			estMeterRead4=(br.readLine()).replace("Elec Meter Read2=", "");
			System.out.println("aLL THE METER READS"+estMeterRead1+estMeterRead2+estMeterRead3+estMeterRead4);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(estMeterRead1.isEmpty() && estMeterRead2.isEmpty() 
				&& estMeterRead3.isEmpty() && estMeterRead4.isEmpty())
		{
			estMeterRead1=browser.getTextByXpath(PageProperties.getProperty("Ddcps.SMRPrevReading1"));

		}
         if(browser.isElementVisibleWithXpath((PageProperties.getProperty("Ddcps.SMRElecDayDial")).replace("//input", "")))
         {
        	 Report.updateTestLog("The First dial is displayed", "PASS");
        	 if(!estMeterRead1.isEmpty()&& estMeterRead3.isEmpty())
        	 {
        		 System.out.println("Retrieved Gas meter read from SAP");
        		 enterMeterRead(PageProperties.getProperty("Ddcps.SMRElecDayDial"), estMeterRead1);
        	 }
        	/* if(estMeterRead1.isEmpty()&&!estMeterRead3.isEmpty())
        	 {
        		 System.out.println("Retrieved Electricity meter read from SAP");
        		 enterMeterRead(PageProperties.getProperty("Ddcps.SMRElecDayDial"), estMeterRead3);
        		 
        	 }*/
        	 if(estMeterRead1.isEmpty()&&!estMeterRead3.isEmpty())
        	 {
        		 System.out.println("Retrieved Electricity meter read from SAP");
        		 enterMeterRead(PageProperties.getProperty("Ddcps.SMRElecDayDial"), estMeterRead3);
        		 
        	 }
        	 if(!estMeterRead1.isEmpty()&&!estMeterRead3.isEmpty())
        	 {
        		 System.out.println("Retrieved Gas and Electricity meter read  from SAP");
        		 enterMeterRead(PageProperties.getProperty("Ddcps.SMRElecDayDial"), estMeterRead1);
        	 }
         }
         if(browser.isElementVisibleWithXpath((PageProperties.getProperty("Ddcps.SMRElecNightDial")).replace("//input", "")))
        		 
         {
        	 Report.updateTestLog("The Second dial is displayed", "PASS");
        	 if(!estMeterRead1.isEmpty()&& !estMeterRead3.isEmpty())
        	 {
        		 enterMeterRead(PageProperties.getProperty("Ddcps.SMRElecNightDial"), estMeterRead3);
        	 }
        	 /*if(!estMeterRead4.isEmpty()&& estMeterRead1.isEmpty())
        	 {
        		 System.out.println("Retrieved Electricity dual rate meter read  from SAP");
        		 enterMeterRead(PageProperties.getProperty("Ddcps.SMRElecNightDial"), estMeterRead4);
        	 }*/
        	 if(!estMeterRead4.isEmpty()&& estMeterRead1.isEmpty())
        	 {
        		 System.out.println("Retrieved Electricity dual rate meter read  from SAP");
        		 enterMeterRead(PageProperties.getProperty("Ddcps.SMRElecNightDial"), estMeterRead4);
        	 }
         }
         if(browser.isElementVisibleWithXpath((PageProperties.getProperty("Ddcps.SMRThirdDial")).replace("//input", "")))
         {
        	 Report.updateTestLog("The third dial is displayed", "PASS");
        	 if(!estMeterRead4.isEmpty())
        	 {
        		 System.out.println("Retrieved Electricity dual rate meter read  from SAP");
        		 enterMeterRead(PageProperties.getProperty("Ddcps.SMRThirdDial"), estMeterRead4);
        	 }
         }
         
         verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.SMROverlayContinue"),"Submit Overlay");
         browser.wait(10000);
         if (browser.isElementVisibleWithXpath("//*[@id='ddcps-meter-read-overlay']") && (!estMeterRead4.isEmpty()))
         {
        	 System.out.println("SMR overlay is displayed after error value");
        	 if(browser.isElementVisibleWithXpath((PageProperties.getProperty("Ddcps.SMRThirdDial")).replace("//input", "")))
             {
            	 if(!estMeterRead3.isEmpty())
            	 {
            		 System.out.println("Retrieved JIE7 meter read  from SAP and swapping");
            		 enterMeterRead(PageProperties.getProperty("Ddcps.SMRThirdDial"), estMeterRead3);
            		 enterMeterRead(PageProperties.getProperty("Ddcps.SMRElecNightDial"), estMeterRead4);
            		 enterMeterRead(PageProperties.getProperty("Ddcps.SMRElecDayDial"), estMeterRead1);
            	 }
             }
         
         
        	 if(browser.isElementVisibleWithXpath((PageProperties.getProperty("Ddcps.SMRElecNightDial")).replace("//input", ""))
        			 && !browser.isElementVisibleWithXpath((PageProperties.getProperty("Ddcps.SMRThirdDial")).replace("//input", ""))
        			 )
        	 {
        		 System.out.println("Retrieved E7 meter read  from SAP and swapping");
        		 enterMeterRead(PageProperties.getProperty("Ddcps.SMRElecNightDial"), estMeterRead3);
        		 enterMeterRead(PageProperties.getProperty("Ddcps.SMRElecDayDial"), estMeterRead4);
        	 }
        	 verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.SMROverlayContinue"),"Submit Overlay");
         }
         else{
        	 System.out.println("NO");
         }
         
	}
	
	public void enterMeterRead(String dialXpath,String meterRead)
	{
		int read=Integer.parseInt(meterRead);
		int dialSize=browser.getChildElementsCountByXpath(dialXpath);
		String newMeterRead=String.format("%0"+dialSize+"d",read);
		String meterReadSplit[]=newMeterRead.split("");
		for(int i=1;i<=dialSize;i++)
 		{
 			System.out.println("The meter read value "+meterReadSplit[i]);
 			verifyAndInputByXpath((dialXpath+"["+i+"]"), "Meter Read Input "+i,meterReadSplit[i]);
 		}
	}
	
	
	
	public void flexPaymentPlan()
	{
		ArrayList<String> payBreakMonth1List = new ArrayList<String>();   
		ArrayList<String> payBreakMonth2List = new ArrayList<String>();   
    	if(browser.isElementVisible(PageProperties.getProperty("DdcpsFlexi.PayBreak1")))
    	{
    		payBreakMonth1List=(browser.getFromDropBox("id", PageProperties.getProperty("DdcpsFlexi.PayBreak1")));
    		int sizeOfDDBox1=payBreakMonth1List.size();
    		System.out.println("The payment break sizes of dd boxes are"+sizeOfDDBox1+"other is "+sizeOfDDBox1);
        	
        	for (int i=1;i<sizeOfDDBox1;i++)
        	{
        		System.out.println("The array list is "+payBreakMonth1List.get(i));
        		verifyAndSelectDropDownBox(PageProperties.getProperty("DdcpsFlexi.PayBreak1"), "Payment break option 1", payBreakMonth1List.get(i));
        		verifyIsElementVisibleWithXpath((PageProperties.getProperty("DdcpsFlexi.DisabledMonthBox")).replace("BREAKDATE", (payBreakMonth1List.get(i)).replace(" ",""))
        				,"Disabled Payment Break Amount");
        	}
    	}
    	if(browser.isElementVisible(PageProperties.getProperty("DdcpsFlexi.PayBreak2")))
    	{
    		payBreakMonth2List=(browser.getFromDropBox("id", PageProperties.getProperty("DdcpsFlexi.PayBreak2")));
    		int sizeOfDDBox2=payBreakMonth2List.size();
    		System.out.println("The payment break sizes of dd boxes are"+sizeOfDDBox2+"other is "+sizeOfDDBox2);
    		for (int i=1;i<sizeOfDDBox2;i++)
        	{
        		System.out.println("The array list is "+payBreakMonth2List.get(i));
        		verifyAndSelectDropDownBox(PageProperties.getProperty("DdcpsFlexi.PayBreak2"), "Payment break option 1", payBreakMonth2List.get(i));
        		verifyIsElementVisibleWithXpath((PageProperties.getProperty("DdcpsFlexi.DisabledMonthBox")).replace("BREAKDATE", (payBreakMonth2List.get(i)).replace(" ",""))
        				, "Disabled Payment Break Amount");
        	}
    	}
    	
    	
    	String minFlexAmount=browser.getTextByXpath(PageProperties.getProperty("DdcpsFlexi.MinPayFlexi"));
    	String maxFlexAmount=browser.getTextByXpath(PageProperties.getProperty("DdcpsFlexi.MaxPayFlexi"));
    	String payCurrentTotal=browser.getTextByXpath(PageProperties.getProperty("DdcpsFlexi.PaymentCurrentTotal"));
    	String payMustTotal=browser.getTextByXpath(PageProperties.getProperty("DdcpsFlexi.MustTotal"));
    	String diffInTotal=browser.getTextByXpath(PageProperties.getProperty("DdcpsFlexi.DifferenceAmt"));
    	System.out.println("Amount details in order"+minFlexAmount+maxFlexAmount+payCurrentTotal+payMustTotal+diffInTotal);
    	if(payCurrentTotal.trim()==payMustTotal.trim())
    	{
    		Report.updateTestLog("The payment Current total and payment must total are same", "PASS");
    	}
    	else{
    		Report.updateTestLog("The payment Current total and payment must total are not same", "FAIL");
    	}
    	verifyAndClickWithLinkText("Reset", "Reset link");
    	
    	
    	verifyAndSelectDropDownBox(PageProperties.getProperty("DdcpsFlexi.PayBreak1"), "Payment break option 1", payBreakMonth1List.get(1));
    	verifyAndSelectDropDownBox(PageProperties.getProperty("DdcpsFlexi.PayBreak2"), "Payment break option 2", payBreakMonth2List.get(2));
    	
    	verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.CancelLink"), "Cancel");
		verifyIsElementVisibleWithXpath(PageProperties.getProperty("Ddcps.CancelOverlayContent"), "Overlay content");
		verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.CancelOverlayNo"), "No");
		
    	if(payCurrentTotal.trim()==payMustTotal.trim())
    	{
    		Report.updateTestLog("The payment Current total and payment must total are same", "PASS");
    	}
    	else{
    		Report.updateTestLog("The payment Current total and payment must total are not same", "FAIL");
    	}
    	verifyAndClick(PageProperties.getProperty("DdcpsFlexi.FlexiTerms"), "Terms And Conditions");
    	if(browser.isElementVisible(PageProperties.getProperty("DdcpsFlexi.PaperLessCheckbox")))
    	{
    	verifyAndClick(PageProperties.getProperty("DdcpsFlexi.PaperLessCheckbox"), "Paper less billing");
    	}
    	else{
    		Report.updateTestLog("The Paper less Check box is not present in the application", "PASS");
    	}
    	verifyAndClick(PageProperties.getProperty("DdcpsFlexi.ChangePlan"), "Flex Submit Button");
    	verifyIsElementVisibleWithXpath(PageProperties.getProperty("DdcpsFlexi.Overlay"), "Flexi Confirmation Overlay");
    	verifyAndClickWithXpath(PageProperties.getProperty("DdcpsFlexi.SaveChange"), "Submit Overlay");
	}
	
	public void adjustMonthly(String ampType)
	{
		if(browser.isElementVisible(PageProperties.getProperty("Ddcps.AMPPaymentBox")))
		{
			System.out.println("Payment box is visible");
		}
		
		if(browser.isElementVisible(PageProperties.getProperty("Ddcps.AMPPaymentBox")))
		{
			
			String recommendedAMP=(browser.getTextByXpath(PageProperties.getProperty("Ddcps.AMPRecommended"))).substring(1);
			String beforeEYB=browser.getTextByXpath(PageProperties.getProperty("Ddcps.AMPEYBalance"));
			String beforeMonthlyPay=browser.getTextByXpath(PageProperties.getProperty("Ddcps.AMPMonthlyPayBfor"));
			String minAMPAmount=browser.getTextByXpath(PageProperties.getProperty("Ddcps.AMPMinAmount"));
			minAMPAmount=((minAMPAmount.replace("(minimum ","")).substring(1)).replace(")", "");
			double minAmountDoub =Double.parseDouble(minAMPAmount);
			double recommendedAmt=Double.parseDouble(recommendedAMP);
			System.out.println("The min amt"+minAMPAmount);
			System.out.println("The rec"+recommendedAMP+"beforeEYB"+beforeEYB+"beforeMOnthly"+beforeMonthlyPay);
			
			//Reset Functionality
			if (browser.isElementVisibleWithXpath(PageProperties.getProperty("Ddcps.AMPResetLink")))
			{
				//verifyAndInputById(PageProperties.getProperty("Ddcps.AMPPaymentBox"),"Adjust monthly amount",""+((recommendedAmt)-1));
				browser.executeScript("$('#amountToPay').val('"+(recommendedAmt-1)+"')");
				browser.executeScript("$('#reCalculate').removeAttr('disabled')");
				verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.AMPRecalculate"), "Recalculate");
				browser.wait(3000);
				verifyIsElementVisibleWithXpath(PageProperties.getProperty("Ddcps.AMPEYBalAfterPay"), 
				"AfterPaymentAmount");
				verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.AMPResetLink"), "Reset Link");
				if(browser.getAttribute(PageProperties.getProperty("Ddcps.AMPPaymentBox"), "value").isEmpty())
				{
					Report.updateTestLog("The recalculate text box is reset", "PASS");
				}
				else{
					Report.updateTestLog("The recalculate text box is not reset", "FAIL");
				}
			}
			
			//Error For Lesser than Minimum 
			//verifyAndInputById(PageProperties.getProperty("Ddcps.AMPPaymentBox"),"Adjust monthly amount lesser than minimum",""+((minAmountDoub)-1));
			browser.executeScript("$('#amountToPay').val('"+((minAmountDoub)-1)+"')");
			browser.executeScript("$('#reCalculate').removeAttr('disabled')");
			verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.AMPRecalculate"), "Recalculate");
			verifyIsElementVisibleWithXpath(PageProperties.getProperty("Ddcps.AMPErrorForLowToler"),"Error for lower Tolerance");
			//TODO CQ5
			
			if(ampType=="Recommended"||ampType=="All")
			{
				//verifyAndInputById(PageProperties.getProperty("Ddcps.AMPPaymentBox"),"Adjust monthly amount",""+((recommendedAmt)-1));
				browser.executeScript("$('#amountToPay').val('"+((recommendedAmt)-1)+"')");
				browser.executeScript("$('#reCalculate').removeAttr('disabled')");
				verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.AMPRecalculate"), "Recalculate");
				browser.wait(3000);
				verifyIsElementVisibleWithXpath(PageProperties.getProperty("Ddcps.AMPEYBalAfterPay"),"AfterPaymentAmount");
				verifyIsElementVisibleWithXpath(PageProperties.getProperty("Ddcps.AMPMonthlyPayAftr"),"Monthly payment After Recalculate");
			}
			
			if(ampType=="GreaterThanMin"||ampType=="All")
			{
			//Greater Than Minimum
			//verifyAndInputById(PageProperties.getProperty("Ddcps.AMPPaymentBox"),"Adjust monthly amount more than minimum",""+((minAmountDoub)+1));
			browser.executeScript("$('#amountToPay').val('"+((minAmountDoub)+1)+"')");
			browser.executeScript("$('#reCalculate').removeAttr('disabled')");
			verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.AMPRecalculate"), "Recalculate");
			browser.wait(4000);
			verifyIsElementVisibleWithXpath(PageProperties.getProperty("Ddcps.AMPEYBalAfterPay"),"Year End Balance After Recaculte");
			verifyIsElementVisibleWithXpath(PageProperties.getProperty("Ddcps.AMPMonthlyPayAftr"),"Monthly payment After Recalculate");
				if((browser.getTextByXpath(PageProperties.getProperty("Ddcps.AMPEYBalAfterPay"))).contains("Debt")
					&& (browser.getTextByXpath(PageProperties.getProperty("Ddcps.AMPEYBalance"))).contains("Debt"))
				{
					verifyIsElementVisibleWithXpath(PageProperties.getProperty("Ddcps.AMPConfirmOnRecalc"), "Confirmation for lesser than recommended");
				}
			}
			
			
			
			verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.AMPSubmit"), "Submit Adjust monthly amount");
			verifyIsElementVisibleWithXpath(PageProperties.getProperty("Ddcps.AMPConfirmOverlay"), "Adjust MOnthly confirmation overlay");
			verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.AMPSubmitConfirmOverlay"), "Submit Overlay");
			verifyIsTextPresent("successfully");
			verifyAndClickWithLinkText(PageProperties.getProperty("Ddcps.BackToManageDDLink"), "BackToManageDD");
			browser.wait(3000);
		}	
	
	else{
		Report.updateTestLog("The Adjust Monthly Page is not getting displayed", "FAIL");
	}
	}
	
	
	public void smartRefreshLink()
	{
		verifyAndClickWithLinkText(PageProperties.getProperty("DdcpsSmart.RefreshLink"), "Refresh Link");
		browser.wait(1000);
		verifyPageTitle("Manage your Direct Debit");
		verifyIsElementVisibleWithXpath(PageProperties.getProperty("DdcpsSmart.MeterRead"),"Meter Reading From MDUS");
	}

	public void selectDataFromDropDownBox(int dropDownValue)
	{
		for(int value=1;value<=dropDownValue;value++) 
		{
			RobotSendKeys.downArrow();
			RobotSendKeys.typeenter();
		}
	}
	
	public void mkpErrValidation(ArrayList<String> errList)
	{
		String recomenddMKP=browser.getTextByXpath(PageProperties.getProperty("Ddcps.MKPRecommended"));
		String endYearBalance=browser.getTextByXpath(PageProperties.getProperty("Ddcps.EYBalance"));
		recomenddMKP=recomenddMKP.substring(1);
		double recomenddMKPAmt=Double.parseDouble(recomenddMKP);
		/*----Reset Functionality----*/
		ddcps.setMkpAmount(1);
		recalculateMkp("withoutRecalc");
		verifyAndClickWithXpath(PageProperties.getProperty("Ddcps.SubmitToMakePayment"), "MakePayment");
		new RegistrationPage().verifyErrorMsg(browser.getTextByXpath(PageProperties.getProperty("Ddcps.MinAmt")),(errList.get(5)).replace("&pound;","£"),"Min Amount Error" );
		new RegistrationPage().verifyErrorMsg(browser.getTextByXpath(PageProperties.getProperty("Ddcps.CardTypeErr")),errList.get(6),"Care Type Error" );
		new RegistrationPage().verifyErrorMsg(browser.getTextByXpath(PageProperties.getProperty("Ddcps.CardNameErr")),errList.get(7),"Name on Card Error" );
		new RegistrationPage().verifyErrorMsg(browser.getTextByXpath(PageProperties.getProperty("Ddcps.CardNumberErr")),errList.get(8),"Card Number Error" );
		new RegistrationPage().verifyErrorMsg(browser.getTextByXpath(PageProperties.getProperty("Ddcps.CardExpiry")),errList.get(9),"Expiry Date Error" );
		new RegistrationPage().verifyErrorMsg(browser.getTextByXpath(PageProperties.getProperty("Ddcps.CvvErr")),errList.get(10),"Cvv Invalid Error" );
		
	}
	
	public void mkpTryExceeds(UserProfile userProfile,ArrayList<String> errList)
	{
		String output= new OnlineDBConnector().changePaymentTryCount(userProfile);
		browser.wait(3000);
		browser.open("https://10.224.70.111/CPS/MakeAPayment/LandingView/");
		//navigateToMkp();
		//new RegistrationPage().verifyErrorMsg(browser.getTextByXpath(PageProperties.getProperty("Ddcps.mkpTryExceedErr")),errList.get(12),"Make a Payment Try Exceeds" );
		System.out.println("the er"+browser.getTextByXpath(PageProperties.getProperty("Ddcps.mkpTryExceedErr")));
	}
	
	public void ddPayComplete(ArrayList<String> errList)
	{
		verifyIsTextPresent(errList.get(11),"Payment Complete");
		
		
	}

}
