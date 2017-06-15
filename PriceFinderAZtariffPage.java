package bg.framework.app.functional.page.reFactoring;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.NoSuchElementException;

import bg.framework.app.functional.action.common.HomePageAction;
import bg.framework.app.functional.entities.PriceFinder;
import bg.framework.app.functional.page.common.BasePage;
import bg.framework.app.functional.util.PropertyLoader;
import bg.framework.app.functional.util.Report;
import bg.framework.app.functional.util.RobotSendKeys;
import bg.framework.app.functional.util.TestDataHelper;
import static bg.framework.app.functional.entities.FunctionalCategory.*;

public class PriceFinderAZtariffPage extends BasePage
{
	private final static String FILE_NAME = "resources/ReFactoring/PriceFinderAZTariff.properties";
	private static Properties pageProperties = new PropertyLoader(FILE_NAME).load();
	/*
	 * Navigates to the All Tariff Link
	 */ 
	public void navigateToAllTariff() 
	{
		browser.wait(getWaitTime());
		verifyAndClickWithXpath(pageProperties.getProperty("PriceFinderAZtariff.gas&elec"), "Gas & Electricity");
		browser.wait(getWaitTime());
		verifyAndClickWithXpath(pageProperties.getProperty("PriceFinderAZtariff.ourtariff"), "Our Tariffs");
		browser.wait(getWaitTime());
		verifyAndClickWithXpath(pageProperties.getProperty("PriceFinderAZtariff.alltariff"),"All Tariffs");
	}
	/*
	 * Verifies whether the values in the screen matches with the Values in the PriceFinder2.XML
	 * If there are any changes in the TOU Tariff names in the screen, Need to Update the TOU Array.
	 */ 
	public void VerifyAZTariffPage() 
	{
		String ElecToU[]={"Off Peak Saver" , "Off Peak Saver 3 Rate" , "Smarter Energy December 2014" , "Smarter Energy July 2014"};
		String meter, payment, Region;
		String paymentType[] = {"QCC", "MDD", "PPM"};
		int reg=1, Register;
		browser.wait(2000);
		String postcode;
		List<String> fuelType = browser.getFromDropBox("id", pageProperties.getProperty("PriceFinderAZtariff.fuelType"));
		for (int fuelCount = 0; fuelCount < fuelType.size(); fuelCount++) 
		{
			browser.selectfromDropBox("id",pageProperties.getProperty("PriceFinderAZtariff.fuelType"), fuelType.get(fuelCount));
			List<String> tariffDetails = getMeterList(pageProperties.getProperty("PriceFinderAZtariff.tariff"));
			for (int tariffCount = 0; tariffCount <tariffDetails.size(); tariffCount++) 
			{
				String tariff = tariffDetails.get(tariffCount);
				browser.selectfromDropBox("id", pageProperties.getProperty("PriceFinderAZtariff.tariff"), tariff);
				for (int RegionCount = 0; RegionCount < getRegions().length; RegionCount++) 
				{
					postcode = getPostcode(RegionCount);
					Region= getRegions()[RegionCount];
					if (browser.isElementVisible(pageProperties.getProperty("PriceFinderAZtariff.postcode"))) 
					{
						browser.clearValue(pageProperties.getProperty("PriceFinderAZtariff.postcode"));
						browser.input(pageProperties.getProperty("PriceFinderAZtariff.postcode"),postcode);
					}
					if (browser.isElementVisibleWithXpath(pageProperties.getProperty("PriceFinderAZtariff.submit"))) 
					{
						browser.wait(getWaitTime());
						browser.clickWithXpath(pageProperties.getProperty("PriceFinderAZtariff.submit"));
						browser.wait(getWaitTime());
					}
					try
					{
						browser.clickWithLinkText("How did we calculate this?");
						browser.wait(getWaitTime());
						browser.clickWithXpath(pageProperties.getProperty("PriceFinderAZtariff.close"));
						browser.wait(getWaitTime());
						if (fuelType.get(fuelCount).equalsIgnoreCase("Gas"))
						{  
							meter="Gas";
							System.out.println("Meter is assigned as GAS");
						}
						else if(fuelType.get(fuelCount).equalsIgnoreCase("Electricity"))
						{
							if(Arrays.asList(ElecToU).contains(tariff))
							{
								meter="Elec ToU";
							}
							else
							{
								meter="Elec SR";
							}
						}
						else if(fuelType.get(fuelCount).equalsIgnoreCase("Economy 7"))
						{
							meter="Elec 2R";
						}
						else
						{
							meter="Null";
							Report.updateTestLog("MeterType is not Assigned - Wrong Fuel Type", "Fail");
						}
						for(int PaymentTCount = 0; PaymentTCount < paymentType.length; PaymentTCount++) 
						{
							if((meter=="Elec 2R")|| (meter=="Elec ToU"))
							{
								for(reg=1; reg<=3; reg++)
								{	
									Register=reg;
									payment = tariffDetails.get(tariffCount) + meter + Register + paymentType[PaymentTCount] + getRegions()[RegionCount];
									PriceFinder priceFinder = new TestDataHelper().getPFdata(payment);
									if(priceFinder==(null))
									{
										continue; 
									}
									else
									{
										if(paymentType[PaymentTCount].equals("QCC"))
										{
											if ((browser.isElementVisibleWithXpath(pageProperties.getProperty("PriceFinderAZtariff.ElecCashCardPath")))&& (!priceFinder.getSC().isEmpty())&&(!priceFinder.getT1().isEmpty())&&(!priceFinder.getT2().isEmpty())) 
											{
												verifyCashCard(tariffDetails, tariffCount, meter, priceFinder, Region, paymentType, PaymentTCount , reg);
											}
											else
											{
												Report.updateTestLog("There is a Mismatch with the Cash/Card Path and the Data in the XML", "Fail");
											}
										}
										if(paymentType[PaymentTCount].equals("MDD"))
										{
											if ((browser.isElementVisibleWithXpath(pageProperties.getProperty("PriceFinderAZtariff.ElecDirectDebitPath")))&& (!priceFinder.getSC().isEmpty())&&(!priceFinder.getT1().isEmpty())&&(!priceFinder.getT2().isEmpty())) 
											{
												verifyDirectDebit(tariffDetails, tariffCount, meter, priceFinder, Region, paymentType, PaymentTCount, reg);
											}
											else
											{
												Report.updateTestLog("There is a Mismatch with the Direct Debit Path and the Data in the XML", "Fail");
											}
										}
										if(paymentType[PaymentTCount].equals("PPM"))
										{
											if (browser.isElementVisibleWithXpath(pageProperties.getProperty("PriceFinderAZtariff.ElecPrepaymentPath")))
											{
												verifyPrePayment(tariffDetails, tariffCount, meter, priceFinder, Region, paymentType, PaymentTCount, reg);
											}
										}
									}
								}
							}
							if((meter.equals("Elec SR")))
							{
								Register=1;
								payment = tariffDetails.get(tariffCount) + meter + Register + paymentType[PaymentTCount] + getRegions()[RegionCount];
								PriceFinder priceFinder1 = new TestDataHelper().getPFdata(payment);
								if(priceFinder1==(null))
								{
									continue; 
								}
								else
								{
									if(paymentType[PaymentTCount].equals("QCC"))
									{
										if ((browser.isElementVisibleWithXpath(pageProperties.getProperty("PriceFinderAZtariff.ElecCashCardPath")))&& (!priceFinder1.getSC().isEmpty())&&(!priceFinder1.getT1().isEmpty())&&(!priceFinder1.getT2().isEmpty())) 
										{
											verifyCashCard(tariffDetails, tariffCount, meter, priceFinder1, Region, paymentType, PaymentTCount , Register);
										}
										else
										{
											Report.updateTestLog("There is a Mismatch with the Cash/Card Path and the Data in the XML", "Fail");
										}
									}
									if(paymentType[PaymentTCount].equals("MDD"))
									{
										if ((browser.isElementVisibleWithXpath(pageProperties.getProperty("PriceFinderAZtariff.ElecDirectDebitPath")))&& (!priceFinder1.getSC().isEmpty())&&(!priceFinder1.getT1().isEmpty())&&(!priceFinder1.getT2().isEmpty())) 
										{
											verifyDirectDebit(tariffDetails, tariffCount, meter, priceFinder1, Region, paymentType, PaymentTCount, Register);
										}
										else
										{
											Report.updateTestLog("There is a Mismatch with the Direct Debit Path and the Data in the XML", "Fail");
										}
									}
									if(paymentType[PaymentTCount].equals("PPM"))
									{
										if (browser.isElementVisibleWithXpath(pageProperties.getProperty("PriceFinderAZtariff.ElecPrepaymentPath")))
										{
											verifyPrePayment(tariffDetails, tariffCount, meter, priceFinder1, Region, paymentType, PaymentTCount, Register);
										}
									}
								}
							}
							if(meter.equals("Gas"))
							{
								Register=1;
								payment = tariffDetails.get(tariffCount) + meter + Register + paymentType[PaymentTCount] + getRegions()[RegionCount];
								PriceFinder priceFinder2 = new TestDataHelper().getPFdata(payment);
								if(priceFinder2==(null))
								{
									continue; 
								}
								else
								{
									if(paymentType[PaymentTCount].equals("QCC"))
									{
										if (browser.isElementVisibleWithXpath(pageProperties.getProperty("PriceFinderAZtariff.GasCashCardPath"))&& (!priceFinder2.getSC().isEmpty())&&(!priceFinder2.getT1().isEmpty())&&(!priceFinder2.getT2().isEmpty())) 
										{
											verifyCashCard(tariffDetails, tariffCount, meter, priceFinder2, Region, paymentType , PaymentTCount, Register);
										} 	
										else
										{
											Report.updateTestLog("There is a Mismatch with the Cash/Card Path and the Data in the XML", "Fail");
										}
									}
									if(paymentType[PaymentTCount].equals("MDD"))
									{
										if (browser.isElementVisibleWithXpath(pageProperties.getProperty("PriceFinderAZtariff.GasDirectDebitPath"))&& (!priceFinder2.getSC().isEmpty())&&(!priceFinder2.getT1().isEmpty())&&(!priceFinder2.getT2().isEmpty())) 
										{
											verifyDirectDebit(tariffDetails, tariffCount, meter, priceFinder2, Region, paymentType, PaymentTCount, Register);
										} 
										else
										{
											Report.updateTestLog("There is a Mismatch with the Direct Debit Path and the Data in the XML", "Fail");
										}
									}
									if(paymentType[PaymentTCount].equals("PPM"))
									{
										if(browser.isElementVisibleWithXpath(pageProperties.getProperty("PriceFinderAZtariff.GasPrepaymentPath"))) 
										{
											verifyPrePayment(tariffDetails, tariffCount, meter, priceFinder2, Region, paymentType, PaymentTCount, Register);
										}
									}
								}
							}
						}

					}
					catch(NoSuchElementException e)
					{
						System.out.println("The Tariff shows some ERROR");
						String error = browser.getTextByXpath(pageProperties.getProperty("PriceFinderAZtariff.Error"));
						Report.updateTestLog("The Tariff :" +tariff+ " shows ERROR - " + error+"." ,"WARN");
					}

				}
			}
		}
	}


	public String[] getRegions() 
	{
		String[] region;
		region = new String[14];
		region[0] = "Eastern";
		region[1] = "East Midlands";
		region[2] = "London";
		region[3] = "ManWeb";
		region[4] = "Midlands";
		region[5] = "Northern";
		region[6] = "Norweb";
		region[7] = "ScotHydro";
		region[8] = "ScotPower";
		region[9] = "Seeboard";
		region[10] = "Southern";
		region[11] = "Swalec";
		region[12] = "Sweb";
		region[13] = "Yorkshire";
		return region;
	}

	public void verifyCashCard(List<String> tariffDetails, int tariffCount, String meter, PriceFinder priceFinder, String Region, String[] paymentT, int PaymentTCount, int reg)
	{
		if (reg == 1)
		{
			Report.updateTestLog("Price Finder : Tariff :" + tariffDetails.get(tariffCount) + ", " +
					"Meter Type :" + meter + ", Payment Type : Cash/Card" + ", Region :" + Region, "PASS");
		}
		if(meter == ("Gas"))
		{		
			verifyStandingCharges("Gas", priceFinder, paymentT, PaymentTCount);
			verifyUnitRates("Gas", priceFinder, paymentT, PaymentTCount);
			verifyTier1Rates("Gas", priceFinder, paymentT, PaymentTCount);
			verifyTier2Rates("Gas", priceFinder, paymentT, PaymentTCount);
		}
		else if(meter==("Elec SR"))
		{	
			verifyStandingCharges("Elec", priceFinder, paymentT, PaymentTCount);
			verifyUnitRates("Elec", priceFinder, paymentT, PaymentTCount);
			verifyTier1Rates("Elec", priceFinder, paymentT, PaymentTCount);
			verifyTier2Rates("Elec", priceFinder, paymentT, PaymentTCount);
		}
		else if(meter == ("Elec 2R"))
		{		        
			verifyStandingChargesAndRegisterRatesforELec2RandTOU("Elec",priceFinder, paymentT, PaymentTCount, reg); 
			if (browser.isElementVisibleWithXpath(pageProperties.getProperty("PriceFinderAZtariff.ElecTier1Element"))) 
			{
				if(reg==1)
				{
					String t1Act = priceFinder.getT1();
					String tier1Act = ceilDouble(t1Act, "0.00");
					String t1Disp = browser.getTextByXpath(pageProperties.getProperty("PriceFinderAZtariff."+paymentT[PaymentTCount]+"ElecTier1"));
					String tier1Disp = ceilDouble(t1Disp, "0.00");
					if(tier1Disp.equalsIgnoreCase(tier1Act))
					{
						Report.updateTestLog("Tier 1 Verification Done :" + tier1Act, "PASS");
					} 
					else
					{
						Report.updateTestLog("Tier 1 Verification actual :" + tier1Act + ", displayed :" + tier1Disp, "FAIL");
					}
					String t2Act = priceFinder.getT2();
					String tier2Act = ceilDouble(t2Act, "0.00");
					String t2Disp = browser.getTextByXpath(pageProperties.getProperty("PriceFinderAZtariff."+paymentT[PaymentTCount]+"ElecReg"+ reg +"Tier2"));
					String tier2Disp = ceilDouble(t2Disp, "0.00");
					if(tier2Disp.equalsIgnoreCase(tier2Act))
					{
						Report.updateTestLog("Register "+ reg + "Verification Done :" + tier2Act, "PASS");
					}
					else
					{
						Report.updateTestLog("Register "+ reg + "Verification actual :" + tier2Act + ", displayed :" + tier2Disp, "FAIL");
					}
				}
				else
				{
					String t2Act = priceFinder.getT2();
					String tier2Act = ceilDouble(t2Act, "0.00");
					String t2Disp = browser.getTextByXpath(pageProperties.getProperty("PriceFinderAZtariff."+paymentT[PaymentTCount]+"ElecReg"+ reg +"Tier2"));
					String tier2Disp = ceilDouble(t2Disp, "0.00");
					if (tier2Disp.equalsIgnoreCase(tier2Act))
					{
						Report.updateTestLog("Register "+ reg + "Verification Done :" + tier2Act, "PASS");
					}
					else
					{
						Report.updateTestLog("Register "+ reg + "Verification actual :" + tier2Act + ", displayed :" + tier2Disp, "FAIL");
					}	
				}
			} 
		}
		else if(meter ==("Elec ToU"))
		{		
			verifyStandingChargesAndRegisterRatesforELec2RandTOU("Elec",priceFinder, paymentT, PaymentTCount, reg); 
		}
	}

	public void verifyDirectDebit(List<String> tariffDetails, int tariffCount, String meter, PriceFinder priceFinder, String Region, String[] paymentT, int PaymentTCount, int reg)
	{
		if(reg==1)
		{
			Report.updateTestLog("Price Finder : Tariff :" + tariffDetails.get(tariffCount) + ", " +
					"Meter Type :" + meter + ", Payment Type : Direct / Debit" + ", Region :" + Region, "PASS");
		}
		if(meter == ("Gas"))
		{		
			verifyStandingCharges("Gas", priceFinder, paymentT, PaymentTCount);
			verifyUnitRates("Gas", priceFinder, paymentT, PaymentTCount);
			verifyTier1Rates("Gas", priceFinder, paymentT, PaymentTCount);
			verifyTier2Rates("Gas", priceFinder, paymentT, PaymentTCount);
		}
		else if(meter==("Elec SR"))
		{	
			verifyStandingCharges("Elec", priceFinder, paymentT, PaymentTCount);
			verifyUnitRates("Elec", priceFinder, paymentT, PaymentTCount);
			verifyTier1Rates("Elec", priceFinder, paymentT, PaymentTCount);
			verifyTier2Rates("Elec", priceFinder, paymentT, PaymentTCount);
		}
		else if(meter == ("Elec 2R"))
		{		        
			verifyStandingChargesAndRegisterRatesforELec2RandTOU("Elec",priceFinder, paymentT, PaymentTCount, reg); 
			if (browser.isElementVisibleWithXpath(pageProperties.getProperty("PriceFinderAZtariff.ElecTier1Element"))) 
			{
				if(reg==1)
				{
					String t1Act = priceFinder.getT1();
					String tier1Act = ceilDouble(t1Act, "0.00");
					String t1Disp = browser.getTextByXpath(pageProperties.getProperty("PriceFinderAZtariff."+paymentT[PaymentTCount]+"ElecTier1"));
					String tier1Disp = ceilDouble(t1Disp, "0.00");
					if (tier1Disp.equalsIgnoreCase(tier1Act)) 
					{
						Report.updateTestLog("Tier 1 Verification Done :" + tier1Act, "PASS");
					} 
					else 
					{
						Report.updateTestLog("Tier 1 Verification actual :" + tier1Act + ", displayed :" + tier1Disp, "FAIL");
					}
					String t2Act = priceFinder.getT2();
					String tier2Act = ceilDouble(t2Act, "0.00");
					String t2Disp = browser.getTextByXpath(pageProperties.getProperty("PriceFinderAZtariff."+paymentT[PaymentTCount]+"ElecReg"+ reg +"Tier2"));
					String tier2Disp = ceilDouble(t2Disp, "0.00");
					if (tier2Disp.equalsIgnoreCase(tier2Act)) 
					{
						Report.updateTestLog("Register" +reg+ " Verification Done :" + tier2Act, "PASS");
					} 
					else 
					{
						Report.updateTestLog("Register" +reg+ " Verification actual :" + tier2Act + ", displayed :" + tier2Disp, "FAIL");
					}
				}
				else
				{
					String t2Act = priceFinder.getT2();
					String tier2Act = ceilDouble(t2Act, "0.00");
					String t2Disp = browser.getTextByXpath(pageProperties.getProperty("PriceFinderAZtariff."+paymentT[PaymentTCount]+"ElecReg"+ reg +"Tier2"));
					String tier2Disp = ceilDouble(t2Disp, "0.00");
					if (tier2Disp.equalsIgnoreCase(tier2Act)) 
					{
						Report.updateTestLog("Register" +reg+ " Verification Done :" + tier2Act, "PASS");
					} 
					else
					{
						Report.updateTestLog("Register" +reg+ " Verification actual :" + tier2Act + ", displayed :" + tier2Disp, "FAIL");
					}	
				}
			} 
		}
		else if(meter ==("Elec ToU"))
		{	       
			verifyStandingChargesAndRegisterRatesforELec2RandTOU("Elec",priceFinder, paymentT, PaymentTCount, reg);
		}
	}

	public void verifyPrePayment(List<String> tariffDetails, int tariffCount, String meter, PriceFinder priceFinder, String Region, String[] paymentT, int PaymentTCount, int reg) 
	{
		if(reg==1)
		{
			Report.updateTestLog("Price Finder : Tariff :" + tariffDetails.get(tariffCount) + ", " +
					"Meter Type :" + meter + ", Payment Type : Prepayment" + ", Region :" + Region, "PASS");
		}
		if(meter == ("Gas"))
		{		     
			if (browser.isElementVisibleWithXpath(pageProperties.getProperty("PriceFinderAZtariff.GasStandingChargesElement")))
			{
				String SC = priceFinder.getSC();
				String SCAct = ceilDouble(SC, "0.00");
				String SC1 = browser.getTextByXpath(pageProperties.getProperty("PriceFinderAZtariff."+paymentT[PaymentTCount]+"GasStandingCharges"));
				if(SC1.equals("NA") && SCAct.equals("0.00"))
				{
					Report.updateTestLog("Standing Charge Verification Done :" + SCAct, "PASS");
				}
				else
				{
					String SCDisp = ceilDouble(SC1, "0.00");
					if(SCDisp.equalsIgnoreCase(SCAct)) 
					{
						Report.updateTestLog("Standing Charge Verification Done :" + SCAct, "PASS");
					}
					else
					{
						Report.updateTestLog("Standing Charge Verification actual :" + SCAct + ", displayed :" + SCDisp, "FAIL");
					}
				}
			}
			if(browser.isElementVisibleWithXpath(pageProperties.getProperty("PriceFinderAZtariff.GasUnitRatesElement")))
			{
				String t1Act = priceFinder.getT1();
				String tier1Act = ceilDouble(t1Act, "0.00");
				String t1Disp = browser.getTextByXpath(pageProperties.getProperty("PriceFinderAZtariff."+paymentT[PaymentTCount]+"GasUnitRates"));
				if(t1Disp.equals("NA") && tier1Act.equals("0.00"))
				{
					Report.updateTestLog("Unit Rate Verification Done :" + tier1Act, "PASS");
				}
				else
				{
					String tier1Disp = ceilDouble(t1Disp, "0.00");
					if(tier1Disp.equalsIgnoreCase(tier1Act))
					{
						Report.updateTestLog("Unit Rate Verification Done :" + tier1Act, "PASS");
					} 
					else
					{
						Report.updateTestLog("Unit Rate Verification actual :" + tier1Act + ", displayed :" + tier1Disp, "FAIL");
					}
				}
			}
			if (browser.isElementVisibleWithXpath(pageProperties.getProperty("PriceFinderAZtariff.GasTier1Element")))
			{
				String t1Act = priceFinder.getT1();
				String tier1Act = ceilDouble(t1Act, "0.00");
				String t1Disp = browser.getTextByXpath(pageProperties.getProperty("PriceFinderAZtariff."+paymentT[PaymentTCount]+"GasTier1"));
				if(t1Disp.equals("NA") && tier1Act.equals("0.00"))
				{
					Report.updateTestLog("Tier 1 Verification Done :" + tier1Act, "PASS");
				}
				else
				{
					String tier1Disp = ceilDouble(t1Disp, "0.00");
					if (tier1Disp.equalsIgnoreCase(tier1Act))
					{
						Report.updateTestLog("Tier 1 Verification Done :" + tier1Act, "PASS");
					} 
					else 	
					{
						Report.updateTestLog("Tier 1 Verification actual :" + tier1Act + ", displayed :" + tier1Disp, "FAIL");
					}
				}
			}
			if (browser.isElementVisibleWithXpath(pageProperties.getProperty("PriceFinderAZtariff.GasTier2Element")))
			{
				String t2Act = priceFinder.getT2();
				String tier2Act = ceilDouble(t2Act, "0.00");
				String t2Disp = browser.getTextByXpath(pageProperties.getProperty("PriceFinderAZtariff."+paymentT[PaymentTCount]+"GasTier2"));
				if(t2Disp.equals("NA") && tier2Act.equals("0.00"))
				{
					Report.updateTestLog("Tier 2 Verification Done :" + tier2Act, "PASS");
				}
				else
				{
					String tier2Disp = ceilDouble(t2Disp, "0.00");
					if (tier2Disp.equalsIgnoreCase(tier2Act))
					{
						Report.updateTestLog("Tier 2 Verification Done :" + tier2Act, "PASS");
					}
					else 
					{
						Report.updateTestLog("Tier 2 Verification actual :" + tier2Act + ", displayed :" + tier2Disp, "FAIL");
					}
				}
			} 

		}
		if(meter==("Elec SR"))
		{		        
			if (browser.isElementVisibleWithXpath(pageProperties.getProperty("PriceFinderAZtariff.ElecStandingChargesElement")))
			{
				String SC = priceFinder.getSC();
				String SCAct = ceilDouble(SC, "0.00");
				String SC1 = browser.getTextByXpath(pageProperties.getProperty("PriceFinderAZtariff."+paymentT[PaymentTCount]+"ElecStandingCharges"));
				if(SC1.equals("NA") && SCAct.equals("0.00"))
				{
					Report.updateTestLog("Standing Charge Verification Done :" + SCAct, "PASS");
				}
				else
				{
					String SCDisp = ceilDouble(SC1, "0.00");
					if (SCDisp.equalsIgnoreCase(SCAct)) 
					{
						Report.updateTestLog("Standing Charge Verification Done :" + SCAct, "PASS");
					} 
					else
					{
						Report.updateTestLog("Standing Charge Verification actual :" + SCAct + ", displayed :" + SCDisp, "FAIL");
					}
				}
			}
			if (browser.isElementVisibleWithXpath(pageProperties.getProperty("PriceFinderAZtariff.ElecUnitRatesElement"))) 
			{
				String t1Act = priceFinder.getT1();
				String tier1Act = ceilDouble(t1Act, "0.00");
				String t1Disp = browser.getTextByXpath(pageProperties.getProperty("PriceFinderAZtariff."+paymentT[PaymentTCount]+"ElecUnitRates"));
				if(t1Disp.equals("NA") && tier1Act.equals("0.00"))
				{
					Report.updateTestLog("Unit Rate Verification Done :" + tier1Act, "PASS");
				}
				else
				{
					String tier1Disp = ceilDouble(t1Disp, "0.00");
					if (tier1Disp.equalsIgnoreCase(tier1Act))
					{
						Report.updateTestLog("Unit Rate Verification Done :" + tier1Act, "PASS");
					}
					else
					{
						Report.updateTestLog("Unit Rate Verification actual :" + tier1Act + ", displayed :" + tier1Disp, "FAIL");
					}
				}
			}
			if (browser.isElementVisibleWithXpath(pageProperties.getProperty("PriceFinderAZtariff.ElecTier1Element")))
			{
				String t1Act = priceFinder.getT1();
				String tier1Act = ceilDouble(t1Act, "0.00");
				String t1Disp = browser.getTextByXpath(pageProperties.getProperty("PriceFinderAZtariff."+paymentT[PaymentTCount]+"ElecTier1"));
				if(t1Disp.equals("NA") && tier1Act.equals("0.00"))
				{
					Report.updateTestLog("Tier 1 Verification Done :" + tier1Act, "PASS");
				}
				else
				{
					String tier1Disp = ceilDouble(t1Disp, "0.00");
					if (tier1Disp.equalsIgnoreCase(tier1Act))
					{
						Report.updateTestLog("Tier 1 Verification Done :" + tier1Act, "PASS");
					}
					else
					{
						Report.updateTestLog("Tier 1 Verification actual :" + tier1Act + ", displayed :" + tier1Disp, "FAIL");
					}
				}
			}
			if (browser.isElementVisibleWithXpath(pageProperties.getProperty("PriceFinderAZtariff.ElecTier2Element")))
			{
				String t2Act = priceFinder.getT2();
				String tier2Act = ceilDouble(t2Act, "0.00");
				String t2Disp = browser.getTextByXpath(pageProperties.getProperty("PriceFinderAZtariff."+paymentT[PaymentTCount]+"ElecTier2"));
				if(t2Disp.equals("NA") && tier2Act.equals("0.00"))
				{
					Report.updateTestLog("Tier 2 Verification Done :" + tier2Act, "PASS");
				}
				else
				{
					String tier2Disp = ceilDouble(t2Disp, "0.00");
					if (tier2Disp.equalsIgnoreCase(tier2Act))
					{
						Report.updateTestLog("Tier 2 Verification Done :" + tier2Act, "PASS");
					}
					else
					{
						Report.updateTestLog("Tier 2 Verification actual :" + tier2Act + ", displayed :" + tier2Disp, "FAIL");
					}
				}
			} 
		}
		if(meter == ("Elec 2R"))
		{		   
			if(browser.isElementVisibleWithXpath(pageProperties.getProperty("PriceFinderAZtariff.ElecStandingChargesElement")))
			{
				if(reg==1)
				{
					String SC = priceFinder.getSC();
					String SCAct = ceilDouble(SC, "0.00");
					String SC1 = browser.getTextByXpath(pageProperties.getProperty("PriceFinderAZtariff."+paymentT[PaymentTCount]+"ElecStandingCharges"));
					if(SC1.equals("NA") && SCAct.equals("0.00"))
					{
						Report.updateTestLog("Standing Charge Verification Done :" + SCAct, "PASS");
					}
					else
					{
						String SCDisp = ceilDouble(SC1, "0.00");
						if (SCDisp.equalsIgnoreCase(SCAct))
						{
							Report.updateTestLog("Standing Charge Verification Done :" + SCAct, "PASS");
						}
						else 
						{
							Report.updateTestLog("Standing Charge Verification actual :" + SCAct + ", displayed :" + SCDisp, "FAIL");
						}
					}
					String t1Act = priceFinder.getT1();
					String tier1Act = ceilDouble(t1Act, "0.00");
					String t1Disp = browser.getTextByXpath(pageProperties.getProperty("PriceFinderAZtariff."+paymentT[PaymentTCount]+"ElecReg"+reg+"Tier1"));
					if(t1Disp.equals("NA") && tier1Act.equals("0.00"))
					{
						Report.updateTestLog("Register "+ reg + "Verification Done :" + tier1Act, "PASS");
					}
					else
					{
						String tier1Disp = ceilDouble(t1Disp, "0.00");
						if (tier1Disp.equalsIgnoreCase(tier1Act))
						{
							Report.updateTestLog("Register "+ reg + " Verification Done :" + tier1Act, "PASS");
						} 
						else
						{
							Report.updateTestLog("Register "+ reg + " Verification actual :" + tier1Act + ", displayed :" + tier1Disp, "FAIL");
						}
					}
				}
				else
				{
					String t1Act = priceFinder.getT1();
					String tier1Act = ceilDouble(t1Act, "0.00");
					String t1Disp = browser.getTextByXpath(pageProperties.getProperty("PriceFinderAZtariff."+paymentT[PaymentTCount]+"ElecReg"+reg+"Tier1"));
					if(t1Disp.equals("NA") && tier1Act.equals("0.00"))
					{
						Report.updateTestLog("Register "+ reg + "Verification Done :" + tier1Act, "PASS");
					}
					else
					{
						String tier1Disp = ceilDouble(t1Disp, "0.00");
						if (tier1Disp.equalsIgnoreCase(tier1Act))
						{
							Report.updateTestLog("Register "+ reg + " Verification Done :" + tier1Act, "PASS");
						}
						else
						{
							Report.updateTestLog("Register "+ reg + " Verification actual :" + tier1Act + ", displayed :" + tier1Disp, "FAIL");
						}
					}
				}
			}
			if (browser.isElementVisibleWithXpath(pageProperties.getProperty("PriceFinderAZtariff.ElecTier1Element")))
			{
				if(reg==1)
				{
					String t1Act = priceFinder.getT1();
					String tier1Act = ceilDouble(t1Act, "0.00");
					String t1Disp = browser.getTextByXpath(pageProperties.getProperty("PriceFinderAZtariff."+paymentT[PaymentTCount]+"ElecTier1"));
					if(t1Disp.equals("NA") && tier1Act.equals("0.00"))
					{
						Report.updateTestLog("Tier1 Verification Done :" + tier1Act, "PASS");
					}
					else
					{
						String tier1Disp = ceilDouble(t1Disp, "0.00");
						if (tier1Disp.equalsIgnoreCase(tier1Act))
						{
							Report.updateTestLog("Tier 1 Verification Done :" + tier1Act, "PASS");
						} 
						else
						{
							Report.updateTestLog("Tier 1 Verification actual :" + tier1Act + ", displayed :" + tier1Disp, "FAIL");
						}
					}
					String t2Act = priceFinder.getT2();
					String tier2Act = ceilDouble(t2Act, "0.00");
					String t2Disp = browser.getTextByXpath(pageProperties.getProperty("PriceFinderAZtariff."+paymentT[PaymentTCount]+"ElecReg"+ reg +"Tier2"));
					if(t2Disp.equals("NA") && tier2Act.equals("0.00"))
					{
						Report.updateTestLog("Register" +reg+ " Tier Verification Done :" + tier2Act, "PASS");
					}
					else
					{
						String tier2Disp = ceilDouble(t2Disp, "0.00");
						if (tier2Disp.equalsIgnoreCase(tier2Act))
						{
							Report.updateTestLog("Register" +reg+ "Tier Verification Done :" + tier2Act, "PASS");
						} else
						{
							Report.updateTestLog("Register" +reg+ "Tier Verification actual :" + tier2Act + ", displayed :" + tier2Disp, "FAIL");
						}
					}
				}
				else
				{
					String t2Act = priceFinder.getT2();
					String tier2Act = ceilDouble(t2Act, "0.00");
					String t2Disp = browser.getTextByXpath(pageProperties.getProperty("PriceFinderAZtariff."+paymentT[PaymentTCount]+"ElecReg"+ reg +"Tier2"));
					if(t2Disp.equals("NA") && tier2Act.equals("0.00"))
					{
						Report.updateTestLog("Register" +reg+ " Tier Verification Done :" + tier2Act, "PASS");
					}
					else
					{
						String tier2Disp = ceilDouble(t2Disp, "0.00");
						if (tier2Disp.equalsIgnoreCase(tier2Act))
						{
							Report.updateTestLog("Register" +reg+ "Tier Verification Done :" + tier2Act, "PASS");
						}
						else 
						{
							Report.updateTestLog("Register" +reg+ "Tier Verification actual :" + tier2Act + ", displayed :" + tier2Disp, "FAIL");
						}
					}
				}
			} 
		}
		if(meter ==("Elec ToU"))
		{		
			if(browser.isElementVisibleWithXpath(pageProperties.getProperty("PriceFinderAZtariff.ElecStandingChargesElement")))
			{
				if(reg==1)
				{
					String SC = priceFinder.getSC();
					String SCAct = ceilDouble(SC, "0.00");
					String SC1 = browser.getTextByXpath(pageProperties.getProperty("PriceFinderAZtariff."+paymentT[PaymentTCount]+"ElecStandingCharges"));
					if(SC1.equals("NA") && SCAct.equals("0.00"))
					{
						Report.updateTestLog("Standing Charge Verification Done :" + SCAct, "PASS");
					}
					else
					{
						String SCDisp = ceilDouble(SC1, "0.00");
						if (SCDisp.equalsIgnoreCase(SCAct)) 
						{
							Report.updateTestLog("Standing Charge Verification Done :" + SCAct, "PASS");
						}
						else
						{
							Report.updateTestLog("Standing Charge Verification actual :" + SCAct + ", displayed :" + SCDisp, "FAIL");
						}
					}
					String t1Act = priceFinder.getT1();
					String tier1Act = ceilDouble(t1Act, "0.00");
					String t1Disp = browser.getTextByXpath(pageProperties.getProperty("PriceFinderAZtariff."+paymentT[PaymentTCount]+"ElecReg"+reg+"Tier1"));
					if(t1Disp.equals("NA") && tier1Act.equals("0.00"))
					{
						Report.updateTestLog("Register"+ reg+ " Verification Done :" + tier1Act, "PASS");
					}
					else
					{
						String tier1Disp = ceilDouble(t1Disp, "0.00");
						if (tier1Disp.equalsIgnoreCase(tier1Act))
						{
							Report.updateTestLog("Register "+ reg + " Verification Done :" + tier1Act, "PASS");
						}
						else 
						{
							Report.updateTestLog("Register "+ reg + " Verification actual :" + tier1Act + ", displayed :" + tier1Disp, "FAIL");
						}
					}
				}
				else
				{
					String t1Act = priceFinder.getT1();
					String tier1Act = ceilDouble(t1Act, "0.00");
					String t1Disp = browser.getTextByXpath(pageProperties.getProperty("PriceFinderAZtariff."+paymentT[PaymentTCount]+"ElecReg"+reg+"Tier1"));
					if(t1Disp.equals("NA") && tier1Act.equals("0.00"))
					{
						Report.updateTestLog("Register" + reg+" Verification Done :" + tier1Act, "PASS");
					}
					else
					{
						String tier1Disp = ceilDouble(t1Disp, "0.00");
						if (tier1Disp.equalsIgnoreCase(tier1Act))
						{
							Report.updateTestLog("Register "+ reg + " Verification Done :" + tier1Act, "PASS");
						}
						else
						{
							Report.updateTestLog("Register "+ reg + " Verification actual :" + tier1Act + ", displayed :" + tier1Disp, "FAIL");
						}
					}
				}
			}
		}
	}
	public void verifyStandingCharges(String Energy, PriceFinder priceFinder, String[] paymentT, int PaymentTCount) 
	{
		if (browser.isElementVisibleWithXpath(pageProperties.getProperty("PriceFinderAZtariff."+Energy+"StandingChargesElement"))) 
		{
			String SC = priceFinder.getSC();
			String SCAct = ceilDouble(SC, "0.00");
			String SC1 = browser.getTextByXpath(pageProperties.getProperty("PriceFinderAZtariff."+paymentT[PaymentTCount]+Energy+"StandingCharges"));
			String SCDisp = ceilDouble(SC1, "0.00");

			if (SCDisp.equalsIgnoreCase(SCAct)) 
			{
				Report.updateTestLog("Standing Charge Verification Done :" + SCAct, "PASS");
			} 
			else
			{
				Report.updateTestLog("Standing Charge Verification actual :" + SCAct + ", displayed :" + SCDisp, "FAIL");
			}
		}
	}
	public void verifyUnitRates(String Energy, PriceFinder priceFinder, String[] paymentT, int PaymentTCount) 
	{
		if (browser.isElementVisibleWithXpath(pageProperties.getProperty("PriceFinderAZtariff."+Energy+"UnitRatesElement")))
		{
			String t1Act = priceFinder.getT1();
			String tier1Act = ceilDouble(t1Act, "0.00");
			String t1Disp = browser.getTextByXpath(pageProperties.getProperty("PriceFinderAZtariff."+paymentT[PaymentTCount]+Energy+"UnitRates"));
			String tier1Disp = ceilDouble(t1Disp, "0.00");
			if (tier1Disp.equalsIgnoreCase(tier1Act))
			{
				Report.updateTestLog("Unit Rate Verification Done :" + tier1Act, "PASS");
			} 
			else
			{
				Report.updateTestLog("Unit Rate Verification actual :" + tier1Act + ", displayed :" + tier1Disp, "FAIL");
			}
		}
	}
	public void verifyTier1Rates(String Energy, PriceFinder priceFinder, String[] paymentT, int PaymentTCount) 
	{
		if (browser.isElementVisibleWithXpath(pageProperties.getProperty("PriceFinderAZtariff."+Energy+"Tier1Element"))) 
		{
			String t1Act = priceFinder.getT1();
			String tier1Act = ceilDouble(t1Act, "0.00");
			String t1Disp = browser.getTextByXpath(pageProperties.getProperty("PriceFinderAZtariff."+paymentT[PaymentTCount]+Energy+"Tier1"));
			String tier1Disp = ceilDouble(t1Disp, "0.00");

			if (tier1Disp.equalsIgnoreCase(tier1Act)) 
			{
				Report.updateTestLog("Tier 1 Verification Done :" + tier1Act, "PASS");
			} 
			else 
			{
				Report.updateTestLog("Tier 1 Verification actual :" + tier1Act + ", displayed :" + tier1Disp, "FAIL");
			}
		}
	}
	public void verifyTier2Rates(String Energy, PriceFinder priceFinder, String[] paymentT, int PaymentTCount) 
	{
		if (browser.isElementVisibleWithXpath(pageProperties.getProperty("PriceFinderAZtariff."+Energy+"Tier2Element")))
		{
			String t2Act = priceFinder.getT2();
			String tier2Act = ceilDouble(t2Act, "0.00");
			String t2Disp = browser.getTextByXpath(pageProperties.getProperty("PriceFinderAZtariff."+paymentT[PaymentTCount]+Energy+"Tier2"));
			String tier2Disp = ceilDouble(t2Disp, "0.00");
			if (tier2Disp.equalsIgnoreCase(tier2Act)) 
			{
				Report.updateTestLog("Tier 2 Verification Done :" + tier2Act, "PASS");
			} 
			else 
			{
				Report.updateTestLog("Tier 2 Verification actual :" + tier2Act + ", displayed :" + tier2Disp, "FAIL");
			}
		} 
	}
	public void verifyStandingChargesAndRegisterRatesforELec2RandTOU(String Energy, PriceFinder priceFinder, String[] paymentT, int PaymentTCount, int reg) 
	{
		if (browser.isElementVisibleWithXpath(pageProperties.getProperty("PriceFinderAZtariff.ElecStandingChargesElement")))
		{
			if(reg==1)
			{
				String SC = priceFinder.getSC();
				String SCAct = ceilDouble(SC, "0.00");
				String SC1 = browser.getTextByXpath(pageProperties.getProperty("PriceFinderAZtariff."+paymentT[PaymentTCount]+"ElecStandingCharges"));
				String SCDisp = ceilDouble(SC1, "0.00");
				if (SCDisp.equalsIgnoreCase(SCAct))
				{
					Report.updateTestLog("Standing Charge Verification Done :" + SCAct, "PASS");
				} else
				{
					Report.updateTestLog("Standing Charge Verification actual :" + SCAct + ", displayed :" + SCDisp, "FAIL");
				}
				String t1Act = priceFinder.getT1();
				String tier1Act = ceilDouble(t1Act, "0.00");
				String t1Disp = browser.getTextByXpath(pageProperties.getProperty("PriceFinderAZtariff."+paymentT[PaymentTCount]+"ElecReg"+reg+"Tier1"));
				String tier1Disp = ceilDouble(t1Disp, "0.00");
				if (tier1Disp.equalsIgnoreCase(tier1Act))
				{
					Report.updateTestLog("Register "+ reg + " Verification Done :" + tier1Act, "PASS");
				} else
				{
					Report.updateTestLog("Register "+ reg + " Verification actual :" + tier1Act + ", displayed :" + tier1Disp, "FAIL");
				}
			}
			else
			{
				String t1Act = priceFinder.getT1();
				String tier1Act = ceilDouble(t1Act, "0.00");
				String t1Disp = browser.getTextByXpath(pageProperties.getProperty("PriceFinderAZtariff."+paymentT[PaymentTCount]+"ElecReg"+reg+"Tier1"));
				String tier1Disp = ceilDouble(t1Disp, "0.00");
				if (tier1Disp.equalsIgnoreCase(tier1Act))
				{
					Report.updateTestLog("Register "+ reg + " Verification Done :" + tier1Act, "PASS");
				}
				else
				{
					Report.updateTestLog("Register "+ reg + " Verification actual :" + tier1Act + ", displayed :" + tier1Disp, "FAIL");
				}
			}
		}
	}
}
				 