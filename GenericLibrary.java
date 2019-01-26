package com.common.utilities;

import java.util.Map.Entry;
import java.util.*;
import java.io.*;
import java.lang.reflect.Method;
import java.util.regex.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.openqa.selenium.*;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.ie.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.safari.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.interactions.*;
import org.openqa.selenium.support.ui.*;

public class GenericLibrary
{
	
	public static WebDriver driver = null;
	public static String strBrowser = "";
	public static String applicationURL = "";
	public static String strObject = "";
	public static String ExcelTimeStamp = "";
	public static StringBuilder verificationErrors;
	public static int DDiff;
	public static String strErrMsg_GenLib = null;
	public static String strCurrentTestCaseID = "";
	public static String strCurrentTestCaseName = "";
	public static String strCurrentTestCaseFormattedName = "";
	public static String strCurrentTestSet = "";
	public static LinkedHashMap<String, String>MapTestSuite = new LinkedHashMap<String, String>();
	//public static ArrayList<Entry<String, String>> lstTestSuite = new ArrayList<Entry<String, String>>();
	//ArrayList<String,String> lstTestSuite = new ArrayList<String,String>();
	public static HashMap<String, String> dicTestData = new HashMap<String, String>();
	
	public static HashMap<String, String> dicOR = new HashMap<String, String>();
	public static HashMap<String, String> dicCommon = new HashMap<String, String>();
	public static HashMap<String, String> dicReporting = new HashMap<String, String>();
	public static HashMap<String, String> dicConfig = new HashMap<String, String>();
	public static HashMap<String, String> dicDict = new HashMap<String, String>();
	public static HashMap<String, String> dicOutput = new HashMap<String, String>();
	public static HashMap<String, String> dicInput = new HashMap<String, String>();
	public static String[] sObject;
	//public static Microsoft.Office.Interop.Excel.Application myExcel;
	public static FileWriter oReportWriter;
	public static int iStepCount;
	public static Calendar sStartDate;
	public static int iPassStepCount;
	public static int iFailStepCount;
	//public static int intScriptCount = 1;
	public static String sCurrentDir;
	public static String sCurrentExePath;
	//public static int IntIteration;
	public static int iScriptCount = 1;
	public static class Action
	{
		//Page specific fields
		public static String Click = "click";
		public static String Exist = "exist";
		public static String Type = "type";
		public static String Select = "select";
		public static String isDisplayed = "isdisplayed";
		public static String isSelected = "isselected";
		public static String isEnabled = "isenabled";
		public static String MouseHover = "mousehover";
		public static String MouseHoverAndClickOnSecondElement = "mousehoverandclickonsecondelement";
		public static String ClickAtCenter = "clickatcenter";
		public static String WaitForElement = "waitforelement";
		public static String SelectCheckbox = "selectcheckbox";
		public static String DeSelectCheckbox = "deselectcheckbox";
		public static String ClickUsingJS = "clickusingjs";
		public static String ClickUsingBuilder = "clickusingbuilder";
		public static String Clear = "clear";
		public static String isNotDisplayed = "isnotdisplayed";
		public static String isNotSelected = "isnotselected";
		//Browser specific fields
		public static String WaitForPageToLoad = "waitforpagetoload";
		public static String Back = "back";
		public static String Navigate = "navigate";
		public static String Refresh = "refresh";
		public static String Maximize = "maximize";
		public static String OpenNewWindow = "opennewwindow";
		public static String SelectWindow = "selectwindow";
		public static String SelectOriginalWindow = "selectoriginalwindow";
		public static String WindowFocus = "windowfocus";
		public static String VerifyTitle = "verifytitle";
		public static String Close = "close";
		public static String SelectFrame = "selectframe";
		public static String Scroll = "scroll";
		public static String Forward = "forward";
		public static String GetURL = "geturl";
		public static String MoveToElementInList = "movetoelementinlist";
		public static String DeleteCookies = "deletecookies";
	}

	private static class Events
	{
		private GenericLibrary genericLibrary = new GenericLibrary();
		private WebElement element = null;
		public Events(WebElement element)
		{
			this.element = element;
		}
		
		//----------------------------------------------------------------------------------------//
		// Function Name : Click
		// Function Description : This function perform click operation on object passed to 'Element(string strObjectName)'
		// Input Variable : Optional (Timeout)
		// OutPut : true / false
		// example : 
		//---------------------------------------------------------------------------------------//
		public final boolean Click()
		{
			return Click(120);
		}

		public final boolean Click(int intTimeout)
		{
			boolean flag = false;
			try
			{
				element.click();
				if (!dicCommon.get("BrowserName").toLowerCase().equals("ie"))
				{
					genericLibrary.PerformAction("browser", Action.WaitForPageToLoad, (new Integer(intTimeout)).toString());
				}
				flag = true;
			}
			catch (RuntimeException e)
			{
				flag = false;
				strErrMsg_GenLib = e.getMessage();
			}
			return flag;
		}


		public final boolean ClickUsingJS()
		{
			return ClickUsingJS(120);
		}

		public final boolean ClickUsingJS(int Timeout)
		{
			boolean flag = false;
			try
			{
				((org.openqa.selenium.JavascriptExecutor)GenericLibrary.driver).executeScript("arguments[0].click();", element);
				flag = true;
			}
			catch (RuntimeException e)
			{
				strErrMsg_GenLib = e.getMessage();
			}
			return flag;
		}


		public final boolean Clear()
		{
			return Clear(120);
		}

		public final boolean Clear(int Timeout)
		{
			boolean flag = false;
			try
			{
				element.clear();
				if (!dicCommon.get("BrowserName").toLowerCase().equals("ie"))
				{
					genericLibrary.PerformAction("browser", Action.WaitForPageToLoad, (new Integer(Timeout)).toString());
				}
				flag = true;
			}
			catch (RuntimeException e)
			{
				flag = false;
				strErrMsg_GenLib = e.getMessage();
			}
			return flag;
		}


		public final boolean ClickUsingBuilder()
		{
			return ClickUsingBuilder(120);
		}

		public final boolean ClickUsingBuilder(int Timeout)
		{
			boolean flag = false;
			try
			{
				Actions builder = new Actions(driver);
				builder.click(element).build().perform();
				return true;
			}
			catch (RuntimeException e)
			{
				strErrMsg_GenLib = e.getMessage();
			}
			return flag;
		}


		public final boolean ClickAtCenter()
		{
			return ClickAtCenter(120);
		}

		public final boolean ClickAtCenter(int intTimeout)
		{
			boolean flag = false;
			try
			{
				//if (strErrMsg_GenLib != null)
				//    return false;

				GenericLibrary g1 = new GenericLibrary();
				g1.ExecuteJavascript("window.focus()");
				if (GenericLibrary.dicCommon.get("BrowserName").toLowerCase().contains("firefox"))
				{
					g1.PerformAction("browser", Action.Maximize);
					g1.PerformAction("browser", Action.WindowFocus);
					//Getting element location
					int elementXoffset = element.getLocation().x;
					int elementYoffset = element.getLocation().y;
					//Getting element width & heigth
					int elementWidth = element.getSize().width;
					int elementHeight = element.getSize().height;
					//Getting offset for element's mid position
					int elementMidXoffset = elementXoffset + (elementWidth / 2);
					int elementMidYoffset = elementYoffset + (elementHeight / 2);
					Actions action = new Actions(GenericLibrary.driver);
					action.moveByOffset(elementMidXoffset, elementMidYoffset).contextClick().build().perform();
				}
				else
				{
					Actions action = new Actions(GenericLibrary.driver);
					
					action.click(element).build().perform();
				}
				genericLibrary.PerformAction("browser", Action.WaitForPageToLoad, (new Integer(intTimeout)).toString());
				flag = true;
			}
			catch (RuntimeException e)
			{
				flag = false;
				strErrMsg_GenLib = e.getMessage();
			}
			return flag;
		}


		//----------------------------------------------------------------------------------------//
		// Function Name : MouseHoverAndClickOnSecondElement
		// Function Description : This function perform mouse hover operation from one element to second element and clicks on the second element
		// Input Variable : locator of second element to be clicked after hovering on the first element
		// OutPut : true / false
		// example : 
		//---------------------------------------------------------------------------------------//
		public final boolean MouseHoverAndClickOnSecondElement(String secondElement)
		{
			boolean flag = false;
			try
			{
				Actions builder = new Actions(GenericLibrary.driver);
				builder.doubleClick(element).build().perform();
				genericLibrary.PerformAction("browser", Action.WaitForPageToLoad, "5");
				flag = true;
			}
			catch (RuntimeException e)
			{
				flag = false;
				strErrMsg_GenLib = e.getMessage();
				System.out.println(strErrMsg_GenLib);
			}
			return flag;
		}
		
		//----------------------------------------------------------------------------------------//
		// Function Name : MouseHover
		// Function Description : This function perform mouse hover operation on object passed to 'Element(string strObjectName)'
		// Input Variable : 
		// OutPut : true / false
		// example : 
		//---------------------------------------------------------------------------------------//
		
		public final boolean MouseHover()
		{
			boolean flag = false;
			try
			{
				Actions builder = new Actions(GenericLibrary.driver);
				builder.moveToElement(element).build().perform();
				genericLibrary.PerformAction("browser", Action.WaitForPageToLoad, "5");
				flag = true;
			}
			catch (RuntimeException e)
			{
				flag = false;
				strErrMsg_GenLib = e.getMessage();
			}
			return flag;
		}


		//----------------------------------------------------------------------------------------//
		// Function Name : Exist
		// Function Description : This function checks existance of object passed to 'Element(string strObjectName)'
		// Input Variable : 
		// OutPut : true / false
		// example : 
		//---------------------------------------------------------------------------------------//
		public final boolean Exist(String strObject)
		{
			boolean flag = false;
			try
			{
				WebElement element = genericLibrary.GetElement(strObject);
				if (element != null)
				{
					flag = true;
				}
				else
				{
					throw new RuntimeException("Element is not present on page.");
				}
			}
			catch (RuntimeException e)
			{
				flag = false;
				strErrMsg_GenLib = e.getMessage();
			}
			return flag;
		}


		//----------------------------------------------------------------------------------------//
		// Function Name : Type
		// Function Description : This function types into input box object passed to 'Element(string strObjectName)'
		// Input Variable : String Keyword
		// OutPut : true / false
		// example : 
		//---------------------------------------------------------------------------------------//
		public final boolean Type(String strKeyword)
		{
			boolean flag = false;
			try
			{
				//if (strErrMsg_GenLib != null)
				//    return false;
				element.clear();
				element.sendKeys(strKeyword);
				flag = true;
			}
			catch (RuntimeException e)
			{
				flag = false;
				strErrMsg_GenLib = e.getMessage();
			}
			return flag;
		}
		//----------------------------------------------------------------------------------------//
		// Function Name : Select
		// Function Description : This function types into input box object passed to 'Element(string strObjectName)'
		// Input Variable : String List Value
		// OutPut : true / false
		// example : 
		//---------------------------------------------------------------------------------------//
		public final boolean Select(String strListValue)
		{
			boolean flag = false;
			try
			{
				try
				{
					Select select = new Select(element);
					//OpenQA.Selenium.Support.UI.SelectElement select = new OpenQA.Selenium.Support.UI.SelectElement(element);
					select.selectByVisibleText(strListValue);
				}
				catch (RuntimeException e)
				{
					element.click();
					element.findElement(By.xpath("//*[text()='" + strListValue + "']"));
				}

				flag = true;
			}
			catch (RuntimeException e)
			{
				flag = false;
				strErrMsg_GenLib = e.getMessage();
			}
			return flag;
		}


		//----------------------------------------------------------------------------------------//
		// Function Name : isVisible
		// Function Description : This function verifies the visibility of an object passed to 'Element(string strObjectName)'
		// Input Variable : 
		// OutPut : true / false
		// example : 
		//---------------------------------------------------------------------------------------//
		public final boolean isDisplayed(String strObject)
		{
			boolean flag = false;
			try
			{
				WebElement element = genericLibrary.GetElement(strObject);
				flag = element.isDisplayed();
			}
			catch (RuntimeException e)
			{
				flag = false;
				strErrMsg_GenLib = e.getMessage();
			}
			return flag;
		}

		/** 
		 Verifies that the element is not displayed on the page
		 
		 <!--Created by : Mandeep Kaur-->
		 <!--Last Modified : 17/2/2015 by Mandeep Kaur-->
		 @param strObject
		 @return 
		*/
		public final boolean isNotDisplayed(String strObject)
		{
			boolean flag = false, flag1 = false;
			try
			{
				WebElement element = genericLibrary.GetElement(strObject);
				flag = (element == null);
				flag1 = strErrMsg_GenLib.contains("is not present on page.");
				if (flag && !flag1) // If element is null but reason is other than 'is not present on page.'
				{
					throw new RuntimeException(strErrMsg_GenLib);
				}
				else if (!flag && !flag1) // If element is not null and reason is also not 'is not present on page.' then check the displayed property of the element
				{
					flag = (element.isDisplayed() == false);
					if (!flag)
					{
						throw new RuntimeException("Element is displayed on page.");
					}
				}
			}
			catch (RuntimeException e)
			{
				flag = false;
				strErrMsg_GenLib = e.getMessage();
			}
			return flag;
		}

		/** 
		 Verifies that a checkbox is not selected
		 
		 <!--Created by : Mandeep Kaur 16/2/2015-->
		 @return 
		*/
		public final boolean isNotSelected()
		{
			boolean flag = false;
			try
			{
				flag = !element.isSelected();
			}
			catch (RuntimeException e)
			{
			}
			return flag;
		}

		public final boolean isEnabled()
		{
			boolean flag = false;
			try
			{
				//if (strErrMsg_GenLib != null)
				//    return false;
				flag = element.isEnabled();
			}
			catch (RuntimeException e)
			{
				flag = false;
				strErrMsg_GenLib = e.getMessage();
			}
			return flag;
		}

		//----------------------------------------------------------------------------------------//
		// Function Name : isSelected
		// Function Description : This function verifies for selected radio or list object passed to 'Element(string strObjectName)'
		// Input Variable : 
		// OutPut : true / false
		// example : 
		//---------------------------------------------------------------------------------------//
		public final boolean isSelected()
		{
			boolean flag = false;
			strErrMsg_GenLib = "";
			try
			{
				//if (strErrMsg_GenLib != null)
				//    return false;
				flag = element.isSelected();
			}
			catch (RuntimeException e)
			{
				flag = false;
				strErrMsg_GenLib = e.getMessage();
			}
			return flag;
		}


		//----------------------------------------------------------------------------------------//
		// Function Name : getText
		// Function Description : This function gets the text over an object passed to 'Element(string strObjectName)'
		// Input Variable : 
		// OutPut : String captured text
		// example : element.getText()
		//---------------------------------------------------------------------------------------//
		public final String GetText()
		{
			String strElementText = "";
			strErrMsg_GenLib = "";
			try
			{
				//if (strErrMsg_GenLib != null)
				//    throw new Exception(strErrMsg_GenLib);

				strElementText = element.getText();

			}
			catch (RuntimeException e)
			{
				strElementText = "";
				strErrMsg_GenLib = e.getMessage();
			}
			return strElementText;
		}


		//----------------------------------------------------------------------------------------//
		// Function Name : getValue
		// Function Description : This function gets the value of an input box object passed
		// Input Variable : 
		// OutPut : String text - for input box, on/off - for checkbox or radio
		// example : 
		//---------------------------------------------------------------------------------------//
		public final String GetValue()
		{
			String strElementValue = "";
			try
			{
				//if (strErrMsg_GenLib != null)
				//    throw new Exception(strErrMsg_GenLib);

				strElementValue = element.getAttribute("value");

			}
			catch (RuntimeException e)
			{
				strElementValue = "";
				strErrMsg_GenLib = e.getMessage();
			}
			return strElementValue;
		}


		//----------------------------------------------------------------------------------------//
		// Function Name : WaitForElement
		// Function Description : This function waits for a particular element in a page until timeout is reached. 120 seconds is default
		// Input Variable : 
		// OutPut : true / false
		// example : 
		//---------------------------------------------------------------------------------------//

		public final boolean WaitForElement(String strObject)
		{
			return WaitForElement(strObject, 60);
		}

		public final boolean WaitForElement(String strObject, int iTimeout)
		{
			boolean flag = false;
			try
			{
				for (int i = 0; i <= iTimeout; i++)
				{
					if (isDisplayed(strObject))
					{

						flag = true;
						break;
					}
					else
					{
						Thread.sleep(Integer.parseInt(dicCommon.get("WaitTimeout")) * 1000);
					}
				}
				if (!flag)
				{
					strErrMsg_GenLib = "OBJECT '" + strObject + "' is not found. Waited for " + (new Integer(iTimeout)).toString() + " seconds.";
				}
			}
			catch (Exception e)
			{
				flag = false;
			}
			return flag;
		}

		public final boolean SelectCheckbox()
		{
			boolean flag = false;
			try
			{
				if (!element.isSelected())
				{
					element.click();
					flag = true;
				}
				else if (element.isSelected())
				{
					flag = true;
				}
				else
				{
					throw new RuntimeException("Could not select Checkbox.");
				}
			}
			catch (RuntimeException e)
			{
				flag = false;
				strErrMsg_GenLib = e.getMessage();
			}
			return flag;
		}

		public final boolean DeSelectCheckbox()
		{
			boolean flag = false;
			try
			{
				if (element.isSelected())
				{
					element.click();
					if (!element.isSelected())
					{
						flag = true;
					}
					else
					{
						throw new RuntimeException("Could not uncheck checkbox.");
					}
				}
				else if (!element.isSelected())
				{
					flag = true;
				}
				else
				{
					throw new RuntimeException("Could not get the check-status of checkbox");
				}
			}
			catch (RuntimeException e)
			{
				flag = false;
				strErrMsg_GenLib = e.getMessage();
			}
			return flag;
		}

		public final boolean MoveToElementInList(String strObjectName)
		{
			boolean flag = false;
			try
			{
				Actions builder = new Actions(driver);
				builder.moveToElement(GenericLibrary.driver.findElement(By.xpath(strObjectName))).build().perform();
				GenericLibrary.driver.findElement(By.xpath(strObjectName)).click();
				flag = true;
			}
			catch (RuntimeException e)
			{
				genericLibrary.writeToLog("MoveToElementInList -- " + e.getMessage());
			}
			return flag;
		}


		/** 
		 Method to convert a string to Selenium.By method.
		*/
		/*public final void convert()
		{
			try
			{
				String classname = "Id";
				Assembly a = Assembly.LoadFile("D:/MobileLabsWork/DeviceConnect/SVN1/Artifacts/WebDriver.dll");
				//Assembly a = Assembly.Load("OpenQA.Selenium.By");
				//Type t = a.GetType("OpenQA.Selenium.By." + classname);
				java.lang.Class t = a.GetType("OpenQA.Selenium.By");
				//object instance = Activator.CreateInstance(t,);
				java.lang.reflect.Method method = t.getMethod(classname);
				//method.Invoke(instance, null);
			}
			catch (RuntimeException e)
			{
				genericLibrary.writeToLog("convert() -- " + e.getMessage());
			}
		}*/
		
		
	}

	public static class Browser
	{
		private GenericLibrary genericLibrary = new GenericLibrary();


		public final boolean PerformActionOnBrowser(String strAction)
		{
			return PerformActionOnBrowser(strAction, "");
		}

		public final boolean PerformActionOnBrowser(String strAction, String strValue)
		{
			boolean flag = true;
			switch (strAction.toLowerCase())
			{
				case "waitforpagetoload":
					if (strValue.equals(""))
						strValue = "60";
					flag = WaitForPageToLoad(Integer.parseInt(strValue));
					break;
				case "back":
					flag = Back();
					break;
				case "forward":
					flag = Forward();
					break;
				case "navigate":
					flag = Navigate(strValue);
					break;
				case "refresh":
					flag = Refresh();
					break;
				case "maximize":
					flag = Maximize();
					break;
				case "opennewwindow":
					flag = OpenNewWindow(strValue);
					break;
				case "selectwindow":
					flag = SelectWindow(strValue);
					break;
				case "selectoriginalwindow":
					flag = SelectOriginalWindow();
					break;
				case "windowfocus":
					flag = WindowFocus();
					break;
				case "verifytitle":
					flag = VerifyTitle(strValue);
					break;
				case "close":
					flag = Close();
					break;
				case "selectframe":
					flag = SelectFrame(strValue);
					break;
				case "scroll":
					flag = Scroll(strValue);
					break;
				case "geturl":
					flag = GetURL();
					break;
				case "deletecookies":
					flag = DeleteCookies();
					break;

			}
			return flag;
		}

		//----------------------------------------------------------------------------------------//
		// Function Name : WaitForPageToLoad
		// Function Description : This function halts the execution commands until the browser is fully loaded or timer exceeds user timeout
		// Input Variable : Field name, for e.g. Category
		// OutPut : Cell value held by the column in that particular row
		// example : string strCategory = FetchDatafromFile(strFieldName)
		//---------------------------------------------------------------------------------------//
		private boolean WaitForPageToLoad(int intTimeOut)
		{
			String strTotalWaitTime = "";
			try
			{
			if (dicOutput.containsKey("WaitForPageToLoad"))
			{
				dicOutput.remove("WaitForPageToLoad");
			}
			long dtInitial = Calendar.getInstance().getTime().getTime();
			//LocalDateTime dtInitial = LocalDateTime.now();
			intTimeOut = intTimeOut * Integer.parseInt(dicCommon.get("WaitTimeout"));
			String strBrowserState = "";
			int intTime = 0;
			if (!dicCommon.get("BrowserName").toLowerCase().contains("safari"))
			{
			tagGotoHere:
				try
				{
					while (!((JavascriptExecutor)GenericLibrary.driver).executeScript("return navigator.onLine").toString().toLowerCase().equals("true") && intTime <= intTimeOut)
					{
						Thread.sleep(1000);
						intTime += 1;
					}
				}
				catch (RuntimeException e)
				{
					Thread.sleep(1000);
					intTime += 1;
					break tagGotoHere;
				}
			}
			Thread.sleep(2000);
			intTime = 0;
			while (!strBrowserState.toLowerCase().equals("complete") && intTime <= intTimeOut)
			{
				Thread.sleep(1000);
				strBrowserState = ((JavascriptExecutor)driver).executeScript("return window.document.readyState").toString();
				intTime += 1;
			}
			String strBrowserNavigatorState = "";
			strBrowserNavigatorState = ((JavascriptExecutor)driver).executeScript("return navigator.onLine").toString().toLowerCase();
			if (!strBrowserState.toLowerCase().equals("complete") || !strBrowserNavigatorState.equals("true"))
			{
				long dtFinal = Calendar.getInstance().getTime().getTime();
				int ts = (int)(dtFinal - dtInitial)/1000;
				//int ts = dtFinal.getSeconds() - dtInitial.getSeconds(); 
				//Date diff = dtFinal.  //dtInitial - dtFinal;
				//LocalDateTime dtFinal = LocalDateTime.now();
				//TimeSpan ts = dtFinal.Subtract(dtInitial);
				
				//strTotalWaitTime = (new Integer(java.lang.Math.round(ts.TotalSeconds))).toString();
				strTotalWaitTime = String.valueOf(ts);
				dicOutput.put("WaitForPageToLoad", strTotalWaitTime);
				return false;
			}
			}
			catch(Exception e)
			{
				genericLibrary.writeToLog(e.getStackTrace().toString());
			}
			return true;
		}


		//----------------------------------------------------------------------------------------//
		// Function Name : Back
		// Function Description : This function performs Browser back operation
		// Input Variable : 
		// OutPut : true / false
		// example : Browser.Back
		//---------------------------------------------------------------------------------------//
		private boolean Back()
		{
			boolean flag = false;
			try
			{
				driver.navigate().back();
				genericLibrary.PerformAction("browser", Action.WaitForPageToLoad);
				flag = true;
			}
			catch (RuntimeException e)
			{
				flag = false;
				strErrMsg_GenLib = e.getMessage();
			}
			return flag;
		}

		//----------------------------------------------------------------------------------------//
		// Function Name : Forward
		// Function Description : This function performs Browser forward operation
		// Input Variable : 
		// OutPut : true / false
		// example : Browser.Forward
		//---------------------------------------------------------------------------------------//
		private boolean Forward()
		{
			boolean flag = false;
			try
			{
				driver.navigate().forward();
				genericLibrary.PerformAction("browser", Action.WaitForPageToLoad);
				flag = true;
			}
			catch (RuntimeException e)
			{
				flag = false;
				strErrMsg_GenLib = e.getMessage();
			}
			return flag;
		}

		//----------------------------------------------------------------------------------------//
		// Function Name : Navigate
		// Function Description : This function navigates the opened browser window to the passed url
		// Input Variable : Page URL
		// OutPut : true / false
		// example : Browser.Navigate("http://www/google.com")
		//---------------------------------------------------------------------------------------//
		private boolean Navigate(String strURL)
		{
			boolean flag = false;
			try
			{
				driver.navigate().to(strURL);
				genericLibrary.PerformAction("browser", Action.WaitForPageToLoad);
				flag = true;
			}
			catch (RuntimeException e)
			{
				flag = false;
				strErrMsg_GenLib = e.getMessage();
			}
			return flag;
		}


		//----------------------------------------------------------------------------------------//
		// Function Name : Refresh
		// Function Description : This function performs Page Refresh operation
		// Input Variable : 
		// OutPut : true / false
		// example : Browser.Refresh
		//---------------------------------------------------------------------------------------//
		private boolean Refresh()
		{
			boolean flag = false;
			try
			{
				driver.navigate().refresh();
				genericLibrary.PerformAction("browser", Action.WaitForPageToLoad);
				flag = true;
			}
			catch (RuntimeException e)
			{
				flag = false;
				strErrMsg_GenLib = "Refresh() -- " + e.getMessage();
			}
			return flag;
		}


		//----------------------------------------------------------------------------------------//
		// Function Name : maximize
		// Function Description : This function maximizes the opened browser window
		// Input Variable : 
		// OutPut : true / false
		// example : Browser.Maximize()
		//---------------------------------------------------------------------------------------//
		private boolean Maximize()
		{
			boolean flag = false;
			try
			{
				driver.manage().window().maximize();
				flag = true;
			}
			catch (RuntimeException e)
			{
				flag = false;
				strErrMsg_GenLib = e.getMessage();
			}
			return flag;
		}
		//----------------------------------------------------------------------------------------//
		// Function Name : Scroll
		// Function Description : This function scrolls browser window
		// Input Variable : 
		// OutPut : true / false
		// example : Scroll("30")
		//---------------------------------------------------------------------------------------//

		private boolean Scroll(String scrollValue)
		{
			boolean flag = false;
			try
			{
				genericLibrary.ExecuteJavascript("$('html,body').scrollTop(" + scrollValue + ");");
			}
			catch (RuntimeException e)
			{
				flag = false;
				strErrMsg_GenLib = e.getMessage();
			}
			return flag;
		}

		//----------------------------------------------------------------------------------------//
		// Function Name : OpenNewWindow
		// Function Description : This function opens new browser window and navigates the page to the passed url
		// Input Variable : Page URL
		// OutPut : true / false
		// example : Browser.OpenNewWindow("http://www/google.com")
		//---------------------------------------------------------------------------------------//

		private boolean OpenNewWindow()
		{
			return OpenNewWindow("");
		}

		private boolean OpenNewWindow(String strBaseURL)
		{
			boolean flag = false;
			if (strBaseURL.equals(""))
			{
				strBaseURL = dicCommon.get("ApplicationURL");
			}
			try
			{
				//Opening new window through Javascript
				((JavascriptExecutor)GenericLibrary.driver).executeScript("window.open('" + strBaseURL + "','NewWindow')");
				GenericLibrary.driver.switchTo().window("NewWindow");
				genericLibrary.PerformAction("browser", Action.Maximize);
				genericLibrary.PerformAction("browser", Action.WaitForPageToLoad);
				flag = true;
			}
			catch (RuntimeException e)
			{
				flag = false;
				strErrMsg_GenLib = e.getMessage();
			}
			return flag;
		}


		//----------------------------------------------------------------------------------------//
		// Function Name : SelectWindow
		// Function Description : This function selects the browser window based on page title or creation time
		// Input Variable : Page Title or Creation time
		// OutPut : true / false
		// example : Browser.SelectWindow("Google") or Browser.SelectWindow("1")
		//---------------------------------------------------------------------------------------//
		private boolean SelectWindow(String strWindowTitleOrCreationTime)
		{
			boolean flag = false;
			/*try
			{
				try
				{
					int creationTime = Integer.parseInt(strWindowTitleOrCreationTime);
					//driver.switchTo().window(driver.getWindowHandles(creationTime));
					driver.switchTo().window(driver.getWindowHandle(). (creationTime));
					flag = true;
				}
				catch (RuntimeException ee)
				{
					driver.SwitchTo().Window(strWindowTitleOrCreationTime);
					flag = true;
				}

			}
			catch (RuntimeException e)
			{
				flag = false;
				strErrMsg_GenLib = e.getMessage();
			}*/
			return flag;
		}


		//----------------------------------------------------------------------------------------//
		// Function Name : SelectOriginalWindow
		// Function Description : This function selects the original browser window whoose creation time is 0
		// Input Variable : 
		// OutPut : true / false
		// example : Browser.SelectOriginalWindow()
		//---------------------------------------------------------------------------------------//
		private boolean SelectOriginalWindow()
		{
			boolean flag = false;
			try
			{
				flag = SelectWindow("0");
			}
			catch (RuntimeException e)
			{
				flag = false;
				strErrMsg_GenLib = e.getMessage();
			}
			return flag;
		}
		
		//----------------------------------------------------------------------------------------//
				// Function Name : WindowFocus
				// Function Description : This function focuses the selected browser window
				// Input Variable : 
				// OutPut : true / false
				// example : Browser.WindowFocus()
				//---------------------------------------------------------------------------------------//
				private boolean WindowFocus()
				{
					boolean flag = false;
					try
					{
						((JavascriptExecutor)driver).executeScript("window.focus()");
						flag = true;
					}
					catch (RuntimeException e)
					{
						flag = false;
						strErrMsg_GenLib = e.getMessage();
					}
					return flag;
				}


				//----------------------------------------------------------------------------------------//
				// Function Name : GetURL
				// Function Description : This function gets the page URL and saves it in GenericClass.dicOutput["url"]
				// Input Variable : 
				// OutPut : true / false
				// example : Browser.GetURL()
				//---------------------------------------------------------------------------------------//
				private boolean GetURL()
				{
					String strURL = "";
					boolean flag = false;
					try
					{
						strURL = driver.getCurrentUrl();
						if (!dicOutput.containsKey("URL"))
						{
							dicOutput.put("URL", strURL);
						}
						else
						{
							dicOutput.put("URL", strURL);
						}
						flag = true;
					}
					catch (RuntimeException e)
					{
						strURL = "";
						strErrMsg_GenLib = e.getMessage();
					}
					return flag;
				}


				//----------------------------------------------------------------------------------------//
				// Function Name : VerifyTitle
				// Function Description : This function compares the actual page title with the passed string
				// Input Variable : Page Title
				// OutPut : true / false
				// example : Browser.VerifyTitle("Google")
				//---------------------------------------------------------------------------------------//
				private boolean VerifyTitle(String strWindowTitle)
				{
					boolean flag = false;
					try
					{
						String strBrowserTitle = "";
						strBrowserTitle = driver.getTitle();
						if (strBrowserTitle.contains(strWindowTitle))
						{
							flag = true;
						}
						else
						{
							flag = false;
						}
					}
					catch (RuntimeException e)
					{
						flag = false;
						strErrMsg_GenLib = e.getMessage();
					}
					return flag;
				}


				//----------------------------------------------------------------------------------------//
				// Function Name : Close
				// Function Description : This function closes the page window.
				// Input Variable : 
				// OutPut : true / false
				// example : Browser.Close()
				//---------------------------------------------------------------------------------------//
				private boolean Close()
				{
					boolean flag = false;
					try
					{
						driver.close();
					}
					catch (RuntimeException e)
					{
						flag = false;
						strErrMsg_GenLib = e.getMessage();
					}
					return flag;
				}


				//----------------------------------------------------------------------------------------//
				// Function Name : GetPageSource
				// Function Description : This function gets the page source and saves it in GenericClass.dicOutput["PageSource"]
				// Input Variable : 
				// OutPut : true / false
				// example : Browser.GetPageSource()
				//---------------------------------------------------------------------------------------//
				private boolean GetPageSource()
				{
					boolean flag = false;
					try
					{
						if (!dicOutput.containsKey("PageSource"))
						{
							dicOutput.put("PageSource", "");
						}
						dicOutput.put("PageSource", driver.getPageSource());
						flag = true;
					}
					catch (RuntimeException e)
					{
						flag = false;
						strErrMsg_GenLib = e.getMessage();
					}
					return flag;
				}


				private boolean SelectFrame(String strFrameName)
				{
					boolean flag = false;
					try
					{
						driver.switchTo().frame(strFrameName);
						flag = true;
					}
					catch (RuntimeException e)
					{
						flag = false;
						strErrMsg_GenLib = e.getMessage();
					}
					return flag;
				}

				private boolean DeleteCookies()
				{
					boolean flag = false;
					strErrMsg_GenLib = "";
					try
					{
						driver.manage().deleteAllCookies();
						flag = true;
					}
					catch (RuntimeException e)
					{
						flag = false;
						strErrMsg_GenLib = "DeleteCookies() -- " + e.getStackTrace();
						genericLibrary.writeToLog(strErrMsg_GenLib);
					}
					return flag;
				}
	}
	
	/*public final boolean LaunchQTPScript(String strScriptName, String strStepDescription, String strExpectedResult)
	{
		boolean isEventSuccessful = false;
		try
		{
			String strScriptPath = dicConfig.get("QTP_Artifacts") + "\\TestScripts\\" + strScriptName;
			if (!(new java.io.File(strScriptPath)).isDirectory())
			{
				throw new RuntimeException("QTP script " + strScriptPath + " does not exist.");
			}
			try
			{
				KillObjectInstances("QTPro");
				DeleteFilesFromDirectory("C:\\Users\\" + Environment.UserName + "\\AppData\\Local\\Temp\\TrustTestResult", "folder");
				QuickTest.Application qtpApp = new QuickTest.Application();
				qtpApp.Launch();
				qtpApp.Visible = true;
				//qtpApp.TDConnection.Connect("http://10.1.11.100:8081/qcbin", "DEFAULT", "MobileLabsTest", "shadab", "", false);
				qtpApp.Open(strScriptPath);
				qtpApp.Test.Run();
				qtpApp.Quit();
				String strCmdText = "cd/ && C: && cd windows && cd system32 && taskkill /IM QTPro /f";
				ExecuteCommandAsync(strCmdText);
				KillObjectInstances("QTPro");
				//isEventSuccessful = true;
			}
			catch (RuntimeException e)
			{
				throw new RuntimeException("Error running QTP script " + strScriptName + "\n" + e.getMessage());
			}
			//Merging report file
			isEventSuccessful = InsetQTPReportIntoSeleniumReport(strStepDescription, strExpectedResult);
			if (!isEventSuccessful)
			{
				throw new RuntimeException(strErrMsg_GenLib);
			}
		}
		catch (RuntimeException e)
		{
			strErrMsg_GenLib = e.getMessage();
			isEventSuccessful = false;
		}
		return isEventSuccessful;
	}*/


	/*public final boolean WaitTillProcessStarts(String strProcessName)
	{
		return WaitTillProcessStarts(strProcessName, 30);
	}*/

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public bool WaitTillProcessStarts(string strProcessName, int iTimeout = 30)
	/*public final boolean WaitTillProcessStarts(String strProcessName, int iTimeout)
	{
		boolean blnProcessStarted = false;
		for (int i = 0; i < iTimeout; i++)
		{
			if (System.Diagnostics.Process.GetProcessesByName("QTPro").getLength() == 0)
			{
				Thread.sleep(1000);
				continue;
			}
			else
			{
				blnProcessStarted = true;
				break;
			}
		}
		return blnProcessStarted;
	}*/

	/*public final boolean InsetQTPReportIntoSeleniumReport(String strStepDescription, String strExpectedResult)
	{
		boolean isScriptFailed = false;
		try
		{
			String userName = Environment.UserName;
			String reportFilePath = "C:\\Users\\" + userName + "\\AppData\\Local\\Temp\\TrustTestResult";
			String[] arrFiles = Directory.GetFiles(reportFilePath, "*.html");
			if (arrFiles.length > 0)
			{

				ArrayList lst = new ArrayList(File.ReadAllLines(arrFiles[0]));
				lst.remove(0);
				isScriptFailed = tangible.DotNetToJavaStringHelper.join(",", lst.toArray(new Object[0])).Contains("class ='tdborder_1_Fail'");
				(new Reporter()).ReportStep("Calling QTP Script - " + strStepDescription, strExpectedResult, "Below are individual steps' details.", !isScriptFailed);
				GenericLibrary.oReportWriter.WriteLine("</table><table align='center' style='width:90%; margin-left:50px;border:1px solid black;'>");
				for (int iLine = 0; iLine < lst.size(); iLine++)
				{
					if (lst.get(iLine).toString().contains("</table>"))
					{
						break;
					}
					GenericLibrary.oReportWriter.WriteLine(lst.get(iLine));
				}
				GenericLibrary.oReportWriter.WriteLine("</table>");
				oReportWriter.WriteLine("<table cellSpacing='0' cellPadding='0' border='0' align='center' style='width:96%; margin-left:20px;'>");
				oReportWriter.WriteLine("<tr>" + " <td class='subheading2' width ='5%' align='center'>Steps</td>" + " <td class='subheading2'  width ='30%' align='center'>Description</td>" + " <td class='subheading2'  width ='30%' align='center'>Expected Result</td>" + " <td class='subheading2'  width ='30%' align='center'>Actual Result</td>" + " <td class='subheading2' width ='5%' align='center'>Step Status</td>" + " </tr>");
			}
			else
			{
				throw new RuntimeException("Report file is not present at " + reportFilePath);
			}
			if ((new File("C:\\Users\\" + Environment.UserName + "\\AppData\\Local\\Temp\\TrustTestResult")).isDirectory())
			{//*****
				for (String strFile : Directory.GetFiles("C:\\Users\\" + Environment.UserName + "\\AppData\\Local\\Temp\\TrustTestResult"))
				{
					try
					{
						String strFileName = strFile.substring(strFile.lastIndexOf("\\") + 1);
						if (!strFileName.contains(".html"))
						{
							File.Copy(strFile, GenericLibrary.dicConfig.get("ReportPath").toString() + "LastRun\\" + strFileName);
						}
					}
					catch (RuntimeException ee)
					{
					}
				}
			}
		}
		catch (RuntimeException e)
		{
			writeToLog("InsetQTPReportIntoSeleniumReport() -- " + e.getStackTrace());
		}
		return !isScriptFailed;
	}*/

	//***** Commented as this is not used in the code
	/*public final String RemoveSpecialCharactersFromString(String stringvalue)
	{
		String strCleanString = "";
		try
		{
			//Regex r = new Regex("(?:[^a-z0-9% ]|(?<=['\"])s)", RegexOptions.IgnoreCase | RegexOptions.CultureInvariant | RegexOptions.Compiled);
			strCleanString = stringvalue.replaceAll("(?:[^a-z0-9% ]|(?<=['\"])s)", "_");
			strCleanString = Microsoft.VisualBasic.Strings.Replace(Microsoft.VisualBasic.Strings.trim(strCleanString), " ", "");
			strCleanString = Microsoft.VisualBasic.Strings.Replace(Microsoft.VisualBasic.Strings.trim(strCleanString), ")", "");
			strCleanString = Microsoft.VisualBasic.Strings.Replace(Microsoft.VisualBasic.Strings.trim(strCleanString), "™", "");
		}
		catch (RuntimeException ee)
		{
			writeToLog(ee.getStackTrace().toString());
		}

		return strCleanString;
	}*/

	public final String ReplaceSpecialCharacterAndSpaces(String strText, String strChrToReplaceWith)
	{
		String strModifiedString = "";
		try
		{
			strModifiedString = strText.replaceAll("(?:[^a-zA-Z0-9%]|(?<=['\"])s)", strChrToReplaceWith); // Replaces all characters from strText which do not match the given Regex expression.
		}
		catch (RuntimeException ee)
		{
		}

		return strModifiedString;
	}

	public final void ConfigureALMExecutionContraints(String[] args)
	{
		dicConfig.put("isRunningThroughALM", "true");
		//Updating Browser Name
		dicCommon.put("BrowserName", args[0]);
		//Updating Test Suite
		UpdateTestSuite(args[1], args[2], args[3]);
	}

	/**
	 * Reads testSuite.xls (Main sheet) and puts testCaseID and testCase name of testCases with RUN = 'Y' in keyValue pair type of hashmap
	 * @author mandeepm
	 */
	public final void ReadTestSuite()
	{
		String RunValue = "";
		String TestCaseID = "";
		String TestCaseName = "";
		String fileName = "";
		int rows = 0;
		int cols = 0;
		xlsReader reader;
		ArrayList<String>lstAllSheetNames;
		
		String sTestSuitePath = GenericLibrary.dicConfig.get("TestSuitePath");
		String sTestSuiteName = GenericLibrary.dicCommon.get("TestSuiteName") + ".xlsx";
		try
		{	
			fileName = sTestSuitePath + "\\" + sTestSuiteName;
			reader = new xlsReader(fileName);
			lstAllSheetNames = reader.getAllSheetNames();
			for(String SheetName : lstAllSheetNames)
			{
			rows = reader.getRowCount(SheetName);
			cols = reader.getColumnCount(SheetName);
			for(int rowNum=2; rowNum<=rows; rowNum++)
			{
				RunValue = reader.getCellData(SheetName, "Run", rowNum);  //(SheetName, cols-1, i);
				if(RunValue.equals("Y"))
				{
					TestCaseID = ((int)(Float.parseFloat(reader.getCellData(SheetName, "TestCaseId", rowNum)))) + "" ; // This handles the case when TestCaseId is not prefixed with "'" and value returned is float type, this converts float to int and then again to String to match the requirement.
					TestCaseName = reader.getCellData(SheetName, "TestCaseName", rowNum); 
					MapTestSuite.put(TestCaseID + "_" +  TestCaseName, SheetName);
				}
			}
			}
		}
	catch (RuntimeException e)
	{
		writeToLog("ReadTestSuite() -- " + e.getStackTrace());
	}
}

	/*public final void ReadTestSuite()
	{
		String sTestSuitePath = GenericLibrary.dicConfig.get("TestSuitePath");
		String sTestSuiteName = GenericLibrary.dicCommon.get("TestSuiteName");
		//KillObjectInstances("EXCEL");
		OleDbConnection objcon = null;
		OleDbDataReader reader = null;
		OleDbCommand comm = null;
		try
		{
			objcon = ExcelDBConnection(sTestSuitePath, sTestSuiteName);
			objcon.Open();

			// Get All sheet names from testSuite file
			String[] AllSheetNames = GetExcelSheetNamesFromExcelFile(objcon);

			for (String sheet : AllSheetNames)
			{
				if (sheet.equals("Main$"))
				{
					String strTestCaseName;
					comm = new OleDbCommand("Select TestCaseId, TestCaseName from [" + sheet + "] where Run = 'Y'", objcon);
					reader = comm.ExecuteReader();
					while (reader.Read())
					{
						strTestCaseName = reader.GetValue(0).toString() + "_" + reader.GetValue(1).toString();
						lstTestSuite.add(new KeyValuePair<String, String>(strTestCaseName, sheet.replace("$", "")));
					}
				}
			}
		}
		catch (RuntimeException e)
		{
			writeToLog(" ReadTestSuite() -- " + e.getStackTrace());
		}
		reader.Dispose();
		objcon.Close();
	}*/

	/**
	 * For each row in the given sheet, it reads first Column's data as key and second column's data as value and adds them to the given HashMap.
	 * @author mandeepm
	 * @since 19/6/2015
	 * @param sWorkbookPath
	 * @param sWorbookName
	 * @param sWorkSheetName : At the moment it supports only .xlsx files
	 * @param dicdict
	 */
	//TODO OWN : Doing : check how it can be used to read .xls and .xlsx both ( make changes in xlsReader class. Do not user Linked HAsh Map because that would not enter identical keys (that may be there while reading OR)
	public final void GetItemValuesFromExcel(String sWorkbookPath, String sWorbookName, String sWorkSheetName, HashMap<String, String> dicdict)
	{
		int rowsCount = 0;
		String item = "";
		String value = "";
		try
		{
			xlsReader reader = new xlsReader(sWorkbookPath + "/" + sWorbookName);
			rowsCount = reader.getRowCount(sWorkSheetName);
			for(int RowNum = 3; RowNum <= rowsCount; RowNum++)
			{
				item = reader.getCellData(sWorkSheetName, 0, RowNum);
				value = reader.getCellData(sWorkSheetName, 1, RowNum);
				dicdict.put(item, value);
			}
			dicdict.put("TestSuiteName", dicdict.get("TestSuiteName") + "_" + dicdict.get("BrowserName"));
		}
		catch (RuntimeException e)
		{
			writeToLog("GetItemValuesFromExcel() -- " + e.getStackTrace());
		}
	}

	public final boolean ExecuteJavascript(String strJavascript)
	{
		boolean isEventSuccessful = false;
		try
		{
			((JavascriptExecutor)GenericLibrary.driver).executeScript(strJavascript);
			isEventSuccessful = true;
		}
		catch (RuntimeException e)
		{
			isEventSuccessful = false;
			strErrMsg_GenLib = e.getMessage();
		}
		return isEventSuccessful;
	}

	public final void TeardownTest()
	{
		try
		{
			if (GenericLibrary.driver != null)
			{
				//GenericLibrary.driver.Close();
				GenericLibrary.driver.quit();
				GenericLibrary.driver = null;
			}
		}
		catch (RuntimeException e)
		{
			//do nothing
		}
		finally
		{
		}
	}

	/*public final OleDbConnection ExcelDBConnection(String sExcelPath, String sExcelName)
	{
		if (!sExcelName.contains(".xls"))
		{
			sExcelName = sExcelName + ".xls";
		}
		OleDbConnection ole1 = null;
		try
		{
			ole1 = new OleDbConnection("Provider=Microsoft.ACE.OLEDB.12.0;Data Source=" + sExcelPath + sExcelName + ";Extended Properties=Excel 12.0");
		}
		catch (RuntimeException ee)
		{
			writeToLog("ExcelDBConnection() for -- '" + sExcelPath + "' and '" + sExcelName + "' " + ee.getStackTrace());
			System.out.println(ee.getMessage());
		}

		return ole1;
	}*/

	// Bring window to front
	/*public static native IntPtr FindWindow(String lpClassName, String lpWindowName);
	static
	{
		System.loadLibrary("USER32.DLL");
	}
	public static native boolean SetForegroundWindow(IntPtr hWnd);*/

	/**
	 * 
	 * @param title Title of the window to be brought to front
	 */
	//TODO OWN To be implemented yet.
	public static void bringToFront(String title)
	{
		/*try
		{
			// Get a handle to the Calculator application.
			IntPtr handle = FindWindow(null, title);

			// Verify that Calculator is a running process.
			if (IntPtr.OpEquality(handle, IntPtr.Zero))
			{
				return;
			}

			// Make Calculator the foreground application
			SetForegroundWindow(handle);
		}
		catch (Exception e)
		{
			
		}*/
	}

	public final void KillObjectInstances(String ProcessName)
	{
		try 
		{
			Runtime.getRuntime().exec("taskkill /F /IM " + ProcessName);
		}
		catch (IOException e)
		{
			writeToLog("KillObjectInstances--" + e.getStackTrace());
		}
		/*Process[] myAllProcesses = Process.getProcessesByName(ProcessName); //*****
		if (myAllProcesses.Count() != 0)
		{
			for (Process objProcess : myAllProcesses)
			{
				try
				{
					objProcess.Kill();
				}
				catch (java.lang.Exception e)
				{
				}
			}
		}
		myAllProcesses = null;*/
	}

	//TODO OWN Need to find out way to do this
	public final void CloseWindow(String ProcessName) //*****
	{
		 
		/*Process[] myAllProcesses = Process.GetProcessesByName(ProcessName);
		if (myAllProcesses.Count() != 0)
		{
			for (Process objProcess : myAllProcesses)
			{
				try
				{
					objProcess.CloseMainWindow();
				}
				catch (java.lang.Exception e)
				{
				}
			}
		}
		myAllProcesses = null;*/
	}

	public final boolean verifyProcessRunning(String ProcessName) //*****
	{
		boolean flag = false;
		/*try
		{
			Process[] myAllProcesses = Process.GetProcessesByName(ProcessName);
			if (myAllProcesses.Count() != 0)
			{
				flag = true;
			}
		}
		catch (RuntimeException e)
		{
			writeToLog("verifyProcessRunning" + e.getStackTrace());
		}*/
		String line;
		try {
		Process proc = Runtime.getRuntime().exec("wmic.exe");
		BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		OutputStreamWriter oStream = new OutputStreamWriter(proc.getOutputStream());
		oStream .write("process where name='explorer.exe'");
		oStream .flush();
		oStream .close();
		while ((line = input.readLine()) != null) {
		System.out.println(line);
		}
		input.close();
		} catch (IOException ioe) {
		ioe.printStackTrace();
		}
		return flag;
	}

	//Update the testSuite so as to change value of 'Run' for all testcases to 'N' and then change the value of 'Run' to 'Y' for only that testCase which needs to be run at the moment.
	// This function is used only when we are running the tests from ALM as it runs the tests one by one.
	// TODO OWN : Tarun doing this
	public final void UpdateTestSuite(String sTestSetName, String sTestCaseID, String sTestCaseName)
	{
		/*String sTestSuitePath = GenericLibrary.dicConfig.get("TestSuitePath");
		String sTestSuiteName = GenericLibrary.dicCommon.get("TestSuiteName");
		if ((new java.io.File(GenericLibrary.dicConfig.get("TestSuitePath") + sTestSuiteName + ".xls")).isFile()) // Run the code if the testSuite File exists.
		{
			OleDbConnection connection = ExcelDBConnection(sTestSuitePath, sTestSuiteName + ".xls");
			connection.Open();
			OleDbCommand objMyCom = connection.CreateCommand();
			for (String sTestSet : GetExcelSheetNamesFromExcelFile(connection))
			{
				if (!sTestSet.equals("Summary$")) // Do this for all sheets except Summary sheet
				{
					objMyCom.CommandText = "update [" + sTestSet + "] set  Run='N'";
					objMyCom.ExecuteNonQuery();
				}
			}
			objMyCom.CommandText = "update [" + sTestSetName + "$] set  Run='Y' where TestCaseName='" + sTestCaseName + "' and TestCaseId='" + sTestCaseID + "'";
			objMyCom.ExecuteNonQuery();
			connection.Close();
		}*/
	}

	// TODO OWN : Make it working for ALM executions find the path of exe
	public final void GetAppConfigData(int iLen)
	{
		sCurrentDir = "";
		String ProjectPath="";
		Enumeration allKeysEnum;
		String key = "";
		String value = "";
		
		try
		{
		GenericLibrary.dicDict.put("ReportMsgFail", "");
		GenericLibrary.dicReporting.put("StepStatus", "fail");
		if (iLen == 0 || iLen == 1)
		{
			ProjectPath = System.getProperty("user.dir");
			sCurrentDir = ProjectPath + "/src";
			//sCurrentDir = System.getenv(sCurrentDir).substring(0, System.getenv(sCurrentDir).toString().indexOf("deviceConnect_Automation"));
			//sCurrentDir = Environment.CurrentDirectory.substring(0, Environment.CurrentDirectory.toString().indexOf("deviceConnect_Automation"));
			//sCurrentExePath = Environment.CurrentDirectory.toString() + "\\";
		}
		else
		{
			sCurrentDir = System.getProperty("user.dir") + "/src";
			//sCurrentExePath = sCommandLine;  //***** Still need to find out the path of exe(first need to find where/if it is made)

		}
			Properties prop = new Properties();
		    String fileName = sCurrentDir + "/com/main/resources/App.config";
		    InputStream is = new FileInputStream(fileName);
	
		    prop.load(is);
		    allKeysEnum = prop.keys();
		    
		    while(allKeysEnum.hasMoreElements())
		    {
		    	key = (String)allKeysEnum.nextElement();
		    	value = prop.getProperty(key);
		    	if (key.equals("TestScriptPkg") || key.equals("frameworkconfig") || key.equals("ExcelWorkBookName") || key.equals("isRunningThroughALM") || key.equals("LibFiles") || key.equals("ProjectName"))
		    		GenericLibrary.dicConfig.put(key, value);
		    	else if(key.equals("Artifacts"))
		    		GenericLibrary.dicConfig.put(key, ProjectPath + "/" + value);
		    	else
		    		GenericLibrary.dicConfig.put(key, sCurrentDir + "\\" +value); // add full path of directories in the dicConfig map
		    }
		    if (!(new File(dicConfig.get("frameworkconfig"))).isFile())  // Figure out why this condition is there
			{
				dicConfig.put("frameworkconfig", dicConfig.get("DatasheetPath") + "Config.xls");
			}
		}
		catch(Exception e)
		{
			writeToLog("GetAppConfigData() -- " + e.getStackTrace());
		}
	}

	//TODO OWN : Not used anywhere at the moment
	public final void RunFileFromcmd(String FilePath)
	{
		/*ProcessStartInfo procStartInfo = new ProcessStartInfo("cmd", "/c " + FilePath);
		procStartInfo.UseShellExecute = false;
		// Do not create the black window.
		procStartInfo.CreateNoWindow = true;
		procStartInfo.Verb = "runas";
		// Now we create a process, assign its ProcessStartInfo and start it
		Process proc = new Process();
		proc.StartInfo = procStartInfo;
		proc.Start();*/
	}

	//***** Replaced with getAllSheetNames() written in xlsReaded.java
	/*public final String[] GetExcelSheetNamesFromExcelFile(OleDbConnection ole1)
	{

		System.Data.DataTable dt = null;
		//KillObjectInstances("EXCEL");
		try
		{
			// Get the data table containg the schema guid.
			dt = ole1.GetOleDbSchemaTable(OleDbSchemaGuid.Tables, null);

			if (dt == null)
			{
				return null;
			}

			String[] excelSheets = new String[dt.Rows.size()];
			int i = 0;

			// Add the sheet name to the string array.
			for (DataRow row : dt.Rows)
			{
				excelSheets[i] = row["TABLE_NAME"].toString();
				i++;
			}

			// Loop through all of the sheets if you want too...
			for (int j = 0; j < excelSheets.length; j++)
			{
				// Query each excel sheet.
			}

			return excelSheets;
		}
		catch (RuntimeException ex)
		{
			return null;
		}
		finally
		{
			if (dt != null)
			{
				dt.Dispose();
			}
		}
	}*/

	/**
	 * Reads the values of all columns in front of the required testCase name in testData.xlsx file and puts all column header - column values in dicTestData as keyValue pairs
	 * @author mandeepm
	 * @since 22/6/2015
	 */
	public final void ReadTestData()
	{String DataSheetPath = "";
	String SheetName = strCurrentTestSet;
	String ColumnName = "";
	String CellValue = "";	
	int RowNumber = -1;
	xlsReader objReader;
	
	try
	{
		DataSheetPath = dicConfig.get("DatasheetPath") + "\\TestData.xlsx";
		objReader = new  xlsReader(DataSheetPath);       //Creating xlsReader class's object to work with TestData file.
		RowNumber = objReader.getCellRowNum(SheetName, "TestCaseName", strCurrentTestCaseName); // Getting row number of the required testCase
		if(RowNumber != -1)  // Execute only if there is a row for the current testCase
		{
			int ColumnsCount = objReader.getColumnCount(SheetName); // Getting number of columns in the sheet.
			for(int i = 0; i<=ColumnsCount; i++)                    // for each column of the testCase(row) in the sheet, do the following: 
			{
				ColumnName = objReader.getCellData(SheetName, i, 1) ;                                   // Getting Column name of the i'th column in front of the testCase's row
				CellValue = objReader.getCellData(SheetName, i, RowNumber);                             // Getting value of the above column for the required testCase row
				GenericLibrary.dicTestData.put(ColumnName, CellValue);                                  // Putting the above key value pair in dicTestData to be used in the testScripts
				
			}
		}
	}
	catch (RuntimeException e)
	{
		writeToLog("ReadTestData() -- " + e.getStackTrace());
	}
		
		//***** Auto ported(C# to JAVA) implementation
		/*OleDbConnection oleDbCon = null;
		OleDbDataAdapter da = null;
		DataSet ds;
		try
		{
			oleDbCon = ExcelDBConnection(dicConfig.get("DatasheetPath"), "TestData" + ".xls");
			oleDbCon.Open();
			da = new OleDbDataAdapter("SELECT * FROM [" + strCurrentTestSet + "$] WHERE TestCaseName='" + strCurrentTestCaseName + "' AND TestCaseID='" + strCurrentTestCaseID + "'", oleDbCon);
			ds = new DataSet();
			da.Fill(ds);
			if (ds.Tables[0].Rows.size() == 1)
			{
				for (int i = 0; i < ds.Tables[0].Columns.size(); i++)
				{
					if (dicTestData.containsKey(ds.Tables[0].Columns[i].ColumnName.toString()))
					{
						dicTestData.put(ds.Tables[0].Columns[i].ColumnName.toString(), ds.Tables[0].Rows[0].ItemArray[i].toString());
					}
					else
					{
						dicTestData.put(ds.Tables[0].Columns[i].ColumnName.toString(), ds.Tables[0].Rows[0].ItemArray[i].toString());
					}
				}
			}
			else
			{
				writeToLog("ReadTestData() -- WARNING - No row found for test case " + strCurrentTestCaseName + " in datasheet.");
			}
			ds.Clear();
			da.Dispose();*/
	}


	public final void LaunchWebDriver(String strBrowser)
	{
		LaunchWebDriver(strBrowser, "");
	}

	public final void LaunchWebDriver()
	{
		LaunchWebDriver("", "");
	}

//ORIGINAL LINE: public void LaunchWebDriver(string strBrowser = "", string strURL = "")
	public final void LaunchWebDriver(String strBrowser, String strURL)
	{
		if (strBrowser.equals(""))
			strBrowser = dicCommon.get("BrowserName");
		if (strURL.equals(""))
			strURL = dicCommon.get("ApplicationURL");
		
		GenericLibrary.strBrowser = strBrowser;
		GenericLibrary.applicationURL = strURL;
		try
		{
			//Appending http
			if (!GenericLibrary.applicationURL.toLowerCase().startsWith("http"))
				GenericLibrary.applicationURL = "http://" + GenericLibrary.applicationURL;
			
			//Deleting Cookies
			DeleteCookies();
			//Launching browser
			LaunchBrowser(strBrowser);
			PerformAction("browser", Action.Maximize);
			driver.navigate().to(GenericLibrary.applicationURL);
			PerformAction("browser", Action.WaitForPageToLoad);
			GenericLibrary.verificationErrors = new StringBuilder();
			//Starting reporting
			//Reporter reporter = new Reporter();
			//reporter.NewScriptHeader();
		}
		catch (RuntimeException e)
		{
			writeToLog("LaunchWebDriver -- for " + strBrowser + " browser s" + e.getStackTrace());
			System.out.println(e.getMessage());
			TeardownTest();
		}
	}

	private void LaunchBrowser(String strBrowser)
	{
		try
		{
			switch (strBrowser.toString().toLowerCase())
			{
				//Passing respective driver path
				case "firefox":
					//FirefoxProfile firefoxProfile = new FirefoxProfile();
					GenericLibrary.driver = new FirefoxDriver();
					break;
				case "ie":
					KillObjectInstances("IEDriverServer");
					KillObjectInstances("iexplore");
					//GenericLibrary.driver = new InternetExplorerDriver(GenericLibrary.dicConfig.get("Artifacts"));
					driver = new InternetExplorerDriver(); //***** Check if we neeed to give the path of the driver
					break;
				case "chrome":
					//KillObjectInstances("chromedriver");
					//GenericLibrary.driver = new ChromeDriver(GenericLibrary.dicConfig.get("Artifacts"));
					System.setProperty("webdriver.chrome.driver", GenericLibrary.dicConfig.get("Artifacts") + "/chromedriver.exe");
					GenericLibrary.driver = new ChromeDriver();//webdriver.chrome.driver system property; //***** Check if we neeed to give the path of the driver
					break;
				case "safari":
					GenericLibrary.driver = new SafariDriver();
					break;
				case "opera":
					GenericLibrary.driver = new OperaDriver();
					break;
			}
		}
		catch (RuntimeException e)
		{
			writeToLog("LaunchBrowser() --" + e.getStackTrace());
			System.out.println("LaunchBrowser --" + e.getMessage());
			TeardownTest();
		}
	}

	/** 
	 Performs the given action on given object.
	 @since 16 July 2015 By Tarun Ahuja
	 @param strObjectName It may be the identification of object on which action is to be performed. generally it is the object name in the OR. It can also be xpath , css, name, id , etc of the object on which action is to be performed. 
	 <p>Give this value in double quotes always.</p>
	 @param strAction It is the action which needs to be performed on the object. eg. Action.Click
	 @param strValue In case of actions like "type" and "waitforelement", where some value is to be provided, this parameter is used.
	 @return Returns the result in form of bool value, "true" or "false" after executing the action on object specified.
	 <example>PerformAction("id=abcd", Action.Type, "akjdhfkalf");</example>
	*/

	public final boolean PerformAction(String strObjectName, String strAction)
	{
		return PerformAction(strObjectName, strAction, "");
	}

//ORIGINAL LINE: public bool PerformAction(string strObjectName, string strAction, string strValue = "")
	public final boolean PerformAction(String strObjectName, String strAction, String strValue)
	{
		boolean flag = false;
		try
		{
			WebElement element = null;
			if (strObjectName.toLowerCase().equals("browser"))
			{
				Browser objBrowser = new Browser();
				flag = objBrowser.PerformActionOnBrowser(strAction, strValue);
				return flag;
			}
			else
			{		
				element = GetElement(strObjectName);
				
				if (element == null)
				{
					//strErrMsg_GenLib = "Element " + strObjectName + " is not found on page";
					if ((!strAction.toLowerCase().equals("waitforelement")) && (!strAction.toLowerCase().equals("isnotdisplayed")))
						return false;
				}
				Events events = new Events(element);
				switch (strAction.toLowerCase())
				{
					case "click":
						flag = events.Click();
						break;
					case "exist":
						flag = true;
						break;
					case "type":
						flag = events.Type(strValue);
						break;
					case "select":
						flag = events.Select(strValue);
						break;
					case "isdisplayed":
						flag = events.isDisplayed(strObjectName);
						break;
					case "isselected":
						flag = events.isSelected();
						break;
					case "mousehover":
						flag = events.MouseHover();
						break;
					case "mousehoverandclickonsecondelement":
						flag = events.MouseHoverAndClickOnSecondElement(strValue);
						break;
					case "clickatcenter":
						flag = events.ClickAtCenter();
						break;
					case "waitforelement":
						if (strValue.equals(""))
							strValue = "60";
						flag = events.WaitForElement(strObjectName, Integer.parseInt(strValue));
						break;
					case "selectcheckbox":
						flag = events.SelectCheckbox();
						break;
					case "isenabled":
						flag = events.isEnabled();
						break;
					case "movetoelementinlist":
						flag = events.MoveToElementInList(strObjectName);
						break;
					case "clickusingjs":
						flag = events.ClickUsingJS();
						break;
					case "clickusingbuilder":
						flag = events.ClickUsingBuilder();
						break;
					case "clear":
						flag = events.Clear();
						break;
					case "isnotdisplayed":
						flag = events.isNotDisplayed(strObjectName);
						break;
					case "deselectcheckbox":
						flag = events.DeSelectCheckbox();
						break;
					case "isnotselected":
						flag = events.isNotSelected();
						break;
				}
			}
		}
		catch (RuntimeException e)
		{
			flag = false;
			strErrMsg_GenLib = e.getMessage();
		}
		return flag;
	}

	/** 
	 Finds the text or value of the object specified
	 @param strObjectName Identification of the object whose text/value is to be found.
	 @param strAction It takes values out of : "text" and "value" according to which property of the object is to be found out.
	 @return Returns the required object's text/value according to the requirement.
	*/
	public final String GetTextOrValue(String strObjectName, String strAction)
	{
		WebElement element = GetElement(strObjectName);
		String value = "";

		if (element != null)
		{
			Events events = new Events(element);
			switch (strAction.toLowerCase())
			{
				case "value":
					value = events.GetValue();

					break;
				case "text":
					value = events.GetText();

					break;
			}
		}
		else
		{
			strErrMsg_GenLib = "Element " + strObjectName + " is not found on page";
		}
		return value;
	}
	
	public final boolean GetStatusofCheckBox(String Xpath_checkbox)
	{
		boolean value = false;
		WebElement element = GetElement(Xpath_checkbox);
		if (element != null)
		{
			//Select select = new Select(element);
			value = element.findElement(By.xpath(Xpath_checkbox)).isSelected();
			System.out.println("status of current element is " + value);
		}
		return value;
	}
	
	
	

	/** 
	 It finds out any attribute of the object like "class", etc.
	 @param strObject Object property/objectName as given in the OR
	 @param strAttributeName Attribute of the object which needs to be found out. eg. "class", "id".
	 @param cssPropertyName In case where any css value is to be found out, give strAttributeName = "css" and in cssPropertyName, give the cssproperty whose value is to be found out.
	 @return Returns the desired attribute of the object if successful, else it returns empty string i.e. "".
	*/

	public final String getAttribute(String strObject, String strAttributeName)
	{
		return getAttribute(strObject, strAttributeName, "");
	}
	
//ORIGINAL LINE: public string getAttribute(string strObject, string strAttributeName, string cssPropertyName = "")
	public final String getAttribute(String strObject, String strAttributeName, String cssPropertyName)
	{
		String strAttributeValue = null;
		WebElement ele = GetElement(strObject);
		try
		{
			if (ele != null)
			{
				if (strAttributeName.toLowerCase().equals("css"))
				{
					try
					{
						strAttributeValue = ele.getCssValue(cssPropertyName.toLowerCase());
					}
					catch (RuntimeException e)
					{
						throw new RuntimeException("CSS property - " + cssPropertyName + " is not found for object - " + strObject);
					}
				}
				else
				{
					try
					{
						strAttributeValue = ele.getAttribute(strAttributeName.toLowerCase());
					}
					catch (RuntimeException e)
					{
						throw new RuntimeException("Attribute - " + strAttributeName + " is not found for object " + strObject);
					}
				}
			}
			else
			{
				throw new RuntimeException("Element " + strObject + " is not found on page.");
			}
		}
		catch (RuntimeException e)
		{
			strErrMsg_GenLib = e.getMessage();
			strAttributeValue = null;
		}
		return strAttributeValue;
	}

	/** 
	 Returns an object of type WebElement with the given locator
	 
	 @param strObject
	 @param parentElement
	 @return 
	*/

	//TODO OWN:Check if 'if' statement in original function can be modified as in some fucntions
	public final WebElement GetElement(String strObject)
	{
		return GetElement(strObject, null);
	}

	public final WebElement GetElement(String strObject, WebElement parentElement)
	{
		WebElement element = null;
		boolean enteredCase = false;
		String property = "", propertyValue = "", strObjectProperty = "";
		if (dicOR.containsKey(strObject))
			strObjectProperty = dicOR.get(strObject);
			
		else
			strObjectProperty = strObject;
		try
		{
			// If identification is in the format of XPATH, enter this if block
			if (strObjectProperty.substring(0, 2).equals("//") || strObjectProperty.substring(0, 3).equals("(//") || strObjectProperty.substring(0, 4).equals("((//"))
			{
				try
				{
					if (parentElement == null)
						element = driver.findElement(By.xpath(strObjectProperty));
					else
						element = parentElement.findElement(By.xpath(strObjectProperty));
				}
				catch (RuntimeException e)
				{
					throw new RuntimeException("Object " + strObjectProperty + " is not present on page." + e.getMessage());
				}

			}
			else // If identification is not in format of xpath, then enter this else block
			{
				try
				{ // Split the identification, if it does not contain '=' , then Split('=')[1] will throw an exception that locator(identification) is not in correct format
					property = strObjectProperty.split("[=]", -1)[0];
					propertyValue = strObjectProperty.split("[=]", -1)[1];
				}
				catch (RuntimeException e)
				{
					throw new RuntimeException("Locator '" + strObjectProperty + "' is not in proper format, Or it does not exist in the OR.");
				}
				try
				{ // If identification is not in format of xpath, then enter this block
					switch (property.toLowerCase())
					{
						case "id":
							enteredCase = true;
							if (parentElement == null)
								element = driver.findElement(By.id(propertyValue));
							else
								element = parentElement.findElement(By.id(propertyValue));
							break;
						case "class":
							enteredCase = true;
							if (parentElement == null)
								element = driver.findElement(By.className(propertyValue));
							else
								element = parentElement.findElement(By.className(propertyValue));
							break;
						case "name":
							enteredCase = true;
							if (parentElement == null)
								element = driver.findElement(By.name(propertyValue));
							else
								element = parentElement.findElement(By.name(propertyValue));
							break;
						case "css":
							enteredCase = true;
							if (parentElement == null)
								element = driver.findElement(By.cssSelector(propertyValue));
							else
								element = parentElement.findElement(By.cssSelector(propertyValue));
							break;
						case "link":
							enteredCase = true;
							if (parentElement == null)
								element = driver.findElement(By.linkText(propertyValue));
							else
								element = parentElement.findElement(By.linkText(propertyValue));
							break;
					}
					if (enteredCase == false) // If the locator is in neither in xpath format, nor in any of the cases in 'Switch', then throw exception and return null.
						throw new RuntimeException("Locator '" + strObjectProperty + "' is not in proper format.");
				}
				catch (RuntimeException e)
				{ // If element locator is in correct format but driver.findElement function could not find any element on that field, then throw an exception that object is not found on page.
					throw new RuntimeException("Object " + strObjectProperty + " is not present on page." + e.getMessage()); // !!!! Do not change the message
				} // !!! Msg linked to isNotDisplayed().
			}
		}
		catch (RuntimeException e)
		{
			element = null;
			strErrMsg_GenLib = e.getMessage();
		}
		return element;
	}

	/** 
	 Writes the message to Logs.txt file in Logs folder. 
	 It appends the text at the bottom of already added text.
	 
	@author mandeepm
	@since 19/6/2015
	 @param errorMessage Message that needs to be appended to the log file
	*/
	public final void writeToLog(String errorMessage)
	{
		File File;
		FileWriter FileWriter;
		BufferedWriter BufWriter;
		String LogString = "";
		try
		{
			String logFilePath = GenericLibrary.dicConfig.get("LogsPath") + "Log.txt";
			LogString = strCurrentTestCaseID + " - " + Calendar.getInstance() + " : " + errorMessage + "\r\n";
			File = new File(logFilePath);
			FileWriter = new FileWriter(File, true);
			BufWriter = new BufferedWriter(FileWriter);
			BufWriter.append(LogString);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	public final void CallTestScript()
	{
		/*try
		{
			String classname = "_" + strCurrentTestCaseID + "_" + strCurrentTestCaseFormattedName;
			System.out.println(classname);
			Assembly a = Assembly.Load("deviceConnect_Automation");
			java.lang.Class t = a.GetType("deviceConnect_Automation." + classname);
			Object instance = Activator.CreateInstance(t);
			java.lang.reflect.Method method = t.getMethod("testScript");
			method.Invoke(instance, null); //calling script
		}
		catch (RuntimeException e)
		{
			writeToLog("CallTestScript() -- " + e.getStackTrace());
		}*/
		try
		{
			//Step1 - Using string funClass to convert to class
			String classname = "_" + strCurrentTestCaseID + "_" + strCurrentTestCaseFormattedName;
			Class c = Class.forName(dicConfig.get("TestScriptPkg") + "." + classname);
			//Step2 - instantiate an object of the class above
			Object o = c.newInstance();
			//Prepare array of the arguments that your function accepts, lets say only one string here
			Class[] paramTypes = new Class[1];
			paramTypes[0]=String.class;
			String methodName = "testScript";
			//Instantiate an object of type method that returns you method name
			Method m = c.getMethod(methodName, new Class[]{});
			//invoke method with actual params
			m.invoke(o, null);
		}
		catch (Exception e)
		{
			writeToLog("CallTestScript() -- " + e.getStackTrace());
		}
	}

	//TODO OWN : To be implemented still
	public final boolean DeleteDownloadedFileWithExtension(String fileName, String fileExtn)
	{
		boolean flag = false;
		/*String strTempPath = Path.GetTempPath();
		String[] strPath = strTempPath.split("[\\\\]", -1);
		java.io.File di = new java.io.File(strPath[0] + "\\" + strPath[1] + "\\" + strPath[2] + "\\Downloads");
//C# TO JAVA CONVERTER TODO TASK: There is no Java equivalent to LINQ queries:
		java.io.File[] files = di.GetFiles("*." + fileExtn).Where(p -> p.Extension.equals("." + fileExtn) && p.Name.startsWith(fileName)).ToArray();
		if (files.length == 0)
		{
			return true;
		}
		else
		{
			for (java.io.File file : files)
			{
				try
				{
					file.Attributes = FileAttributes.Normal;
					(new java.io.File(file.getPath())).delete();
					flag = true;
				}
				catch (RuntimeException e)
				{
					writeToLog("DeleteDownloadedFileWithExtension() --" + e.getStackTrace());
					flag = false;
				}
			}
		}*/
		return flag;
	}

	/** 
	 Deletes the given file, folder or all files in the given folder.
	 <p>For deleting a single file, give full file path in FileNameWithPath and FileOrFolder = "file"</p>
	 <p>For deleting all files inside a folder, give full path of folder in FileNameWithPath and FileOrFolder="allfiles"</p>
	 <p>For deleting entire folder, give full path of folder in FileNameWithPath and FileOrFolder = "folder"</p>
	 
	 @param FileNameWithPath Full path of the file/folder that needs to be deleted.
	 @param FileOrFolder It takes three values: "file", "folder", "allfiles"
	 @return Bool value true or false indicating whether file/folder were deleted or not.
	*/

	//TODO OWN : To be implemented still
	public final boolean DeleteFilesFromDirectory(String FileNameWithPath)
	{
		return DeleteFilesFromDirectory(FileNameWithPath, "file");
	}

//ORIGINAL LINE: public bool DeleteFilesFromDirectory(string FileNameWithPath, string FileOrFolder = "file")
	public final boolean DeleteFilesFromDirectory(String FileNameWithPath, String FileOrFolder)
	{
		boolean flag = false;


		/*switch (FileOrFolder.toLowerCase().trim())
		{
			case "file":
				if ((new java.io.File(FileNameWithPath)).isFile())
				{
					try
					{
						(new java.io.File(FileNameWithPath)).delete();
						flag = true;
					}
					catch (RuntimeException ee)
					{
						flag = false;
					}
				}
				break;
			case "allfiles":
				String[] fileList = Directory.GetFiles(FileNameWithPath);
				for (String file : fileList)
				{
					try
					{
						(new java.io.File(file)).delete();
						flag = true;
					}
					catch (RuntimeException ee)
					{
						writeToLog("DeleteFilesFromdirectory() -- " + ee.getStackTrace());
					}
				}
				break;
			case "folder":
				if ((new java.io.File(FileNameWithPath)).isDirectory())
				{
					try
					{
						(new java.io.File(FileNameWithPath, true)).delete();
						flag = true;
					}
					catch (RuntimeException ee)
					{
						flag = false;
					}
				}
				break;
		}*/
		return flag;
	}

	//TODO OWN Check if this is even required
	public final void ExecuteCommandAsync(String command)
	{
		/*try
		{
			//Asynchronously start the Thread to process the Execute command request.
			Thread objThread = new Thread(new ParameterizedThreadStart(ExecuteCommandSync));
			//Make the thread as background thread.
			objThread.IsBackground = true;
			//Set the Priority of the thread.
			objThread.Priority = ThreadPriority.AboveNormal;
			//Start the thread.
			objThread.Start(command);
		}
		catch (ThreadStartException objException)
		{
			// Log the exception
		}
		catch (ThreadAbortException objException)
		{
			// Log the exception
		}
		catch (RuntimeException objException)
		{
			// Log the exception
		}*/
	}
	
	//TODO OWN Check if this is even required
	public final void ExecuteCommandSync(Object command)
	{
		/*try
		{
			// create the ProcessStartInfo using "cmd" as the program to be run,
			// and "/c " as the parameters.
			// Incidentally, /c tells cmd that we want it to execute the command that follows,
			// and then exit.
			System.Diagnostics.ProcessStartInfo procStartInfo = new System.Diagnostics.ProcessStartInfo("cmd", "/c " + command);

			// The following commands are needed to redirect the standard output.
			// This means that it will be redirected to the Process.StandardOutput StreamReader.
			procStartInfo.RedirectStandardOutput = true;
			procStartInfo.UseShellExecute = false;
			// Do not create the black window.
			procStartInfo.CreateNoWindow = true;
			// Now we create a process, assign its ProcessStartInfo and start it
			System.Diagnostics.Process proc = new System.Diagnostics.Process();
			proc.StartInfo = procStartInfo;
			proc.Start();
			// Get the output into a string
			String result = proc.StandardOutput.ReadToEnd();
			// Display the command output.
			System.out.println(result);
		}
		catch (RuntimeException e)
		{
			writeToLog("ExecuteCommandSync() -- " + e.getStackTrace());
		}*/
	}

	/** 
	 Deletes browser cookies
	 
	 @param strBrowserName
	*/
	//TODO OWN Check if it is actually required as this can be replaced with selenium deleteCookies method in LaunchWebDriver
	public final void DeleteCookies()
	{
		DeleteCookies("");
	}

	//ORIGINAL LINE: public void DeleteCookies(string strBrowserName = "")
	public final void DeleteCookies(String strBrowserName)
	{
		/*strErrMsg_GenLib = "";
		try
		{
			if (strBrowserName.equals(""))
				strBrowserName = dicCommon.get("BrowserName");
		}
		catch (RuntimeException e)
		{
			writeToLog("DeleteCookies() -- 'BrowserName' key not found in dicCommon");
		}

		try
		{
			Microsoft.VisualBasic.Devices.ComputerInfo computerInfo = new Microsoft.VisualBasic.Devices.ComputerInfo();
			String fullName = computerInfo.OSFullName;
			String userName = Environment.UserName;
			if (fullName.toLowerCase().contains("windows 7") || fullName.toLowerCase().contains("windows 8"))
			{
				if (strBrowserName.toLowerCase().equals("chrome"))
					DeleteFilesFromDirectory("C:\\Users\\" + userName + "\\AppData\\Local\\Google\\Chrome\\User Data", "folder");
				else if (strBrowserName.toLowerCase().equals("ie"))
				{
					String strCmd_DelCookies, str_DelHistory;
					strCmd_DelCookies = "cd/ && C: && cd windows && cd system32 && RunDll32.exe InetCpl.cpl,ClearMyTracksByProcess 2";
					str_DelHistory = "cd/ && C: && cd windows && cd system32 && RunDll32.exe InetCpl.cpl,ClearMyTracksByProcess 1";
					ExecuteCommandAsync(strCmd_DelCookies);
					ExecuteCommandAsync(str_DelHistory);
					DeleteFilesFromDirectory("C:\\Users\\" + userName + "\\AppData\\Local\\Microsoft\\Windows\\Cookies", "folder");
					DeleteFilesFromDirectory("C:\\Users\\" + userName + "\\AppData\\Local\\Temp\\Cookies", "allfiles");
				}
			}
		}
		catch (RuntimeException e)
		{
			strErrMsg_GenLib = "DeleteCookies() -- " + e.StackTrace;
			writeToLog("DeleteCookies() -- " + e.getStackTrace());
		}*/
	}

	
	/** 
	 Executes given action on browser pop ups.
	 
	 <!--Created By : Mandeep Kaur-->
	 @param strAction action to be performed. Takes values: accept,ok,yes, dismiss, no, cancel, "sendkeys", "gettext"
	 @param strValue It is to be given only in case when strAction is "sendkeys". It should be the value which is to be typed in the editable field (on pop up) which has focus.
	*/

	public final void ExecuteActionOnPopUp(String strAction)
	{
		ExecuteActionOnPopUp(strAction, "");
	}

	//ORIGINAL LINE: public void ExecuteActionOnPopUp(string strAction, string strValue = "")
	public final void ExecuteActionOnPopUp(String strAction, String strValue)
	{
		//@ verify Pop up is present or not.
		try
		{
			switch (strAction.toLowerCase().trim())
			{
				case "accept":
				case "ok":
				case "yes":
					driver.switchTo().alert().accept();
					break;
				case "dismiss":
				case "no":
				case "cancel":
					driver.switchTo().alert().dismiss();
					break;
				case "sendkeys":
					driver.switchTo().alert().sendKeys(strValue);
					break;
				case "gettext":
					dicOutput.put("strOutPut", driver.switchTo().alert().getText()); //*****
					break;
			}
		}
		catch (RuntimeException e)
		{
			writeToLog("ExecuteActionOnPopUp() --" + e.getStackTrace());
		}
	}
	/** 
	 Finds the number of elements with the given "xpath" property.
	 <p>Implements "findElements" method of webdriver.</p>
	 <!--Created By : Mandeep Kaur-->
	 
	 @param elementProperty It is "xpath" of the element for which count is to be found.
	 @return integer value for number of occurences of elements with this xpath 
	*/
	public final int getelementCount(String elementProperty)
	{
		int ElementsCount = 0;
		try
		{
			if (dicOR.containsKey(elementProperty))
			{
				elementProperty = dicOR.get(elementProperty); //Assign the value of dicOR'skey to the variable in case OR contains the provided key
			}

			ElementsCount = driver.findElements(By.xpath(elementProperty)).size();
			return ElementsCount;
		}
		catch (RuntimeException e)
		{
			writeToLog("Could not get element count for element with property : " + elementProperty + "_____ \n" + e.toString());
			return ElementsCount;
		}
	}

	/**
	 <!-- Modified By : Tarun Ahuja on 22 July,2015 -->
	 Get the List of elements.
	 <p>Implements "findElements" method of webdriver.</p>
	 <!--Created By : Mandeep Kaur-->
	 @param elementProperty It is "xpath" of the element for which count is to be found.
	 @return integer value for number of occurences of elements with this xpath 
	*/
	public final List<WebElement> getelementsList(String elementProperty)
	{
		List<WebElement> ElementsList = new ArrayList<WebElement>();
		try
		{
			if (dicOR.containsKey(elementProperty))
			{
				elementProperty = dicOR.get(elementProperty); //Assign the value of dicOR's key to the variable in case OR contains the provided key
			}

			ElementsList = driver.findElements(By.xpath(elementProperty));
 
			// = element.findElements(By.tagName("a")); /* Tarun Ahuja : Commented below code to make the function work in generic way*/

			/*ElementsList = driver.findElements(By.xpath(elementProperty));*/
		}
		catch (RuntimeException e)
		{
			writeToLog("getelementsList() -- Could not get elements for element with identification : " + elementProperty + "_____ \n" + e.getStackTrace());
		}
		return ElementsList;
	}

	/** 
	 Extracts int values from any string passed.
	 
	 <!--Created By : Mandeep Kaur-->
	 @param StringVariable The string value containing int values.
	 @return A string of all the int values present in the string passed.
	 <example> extractIntFromString("a1js3df78") returns value - "1378"</example>
	*/
	public final String extractIntFromString(String StringVariable)
	{
		String result = "";
		try
		{
			result = StringVariable.replaceAll("[^\\d]", "");
		}
		catch (RuntimeException e)
		{
			writeToLog("extractIntFromString() -- " + e.getStackTrace());
		}
		return result;
	}

	/** 
	 Gets the value of given key from the given dictionary
	 
	 <!--Created By : Mandeep Kaur-->
	 <!--Last Modified : 9 Dec, 2014-->
	 @param dictionary Dictionary where key is present
	 @param key Key for which value is to be obtained.
	 @return 
	*/
	public final String getValueFromDictionary(HashMap<String, String> dictionary, String key)
	{
		strErrMsg_GenLib = "";
		String result = "";
		try
		{
			result = dictionary.get(key);
		}
		catch (RuntimeException e)
		{
			strErrMsg_GenLib = e.getMessage();
		}
		return result;
	}

	/** 
	 Fetches the value of required key from given dictionary and then replaces string given in 'valToReplaces' with string in replaceWith
	 
	 <!--Created By : Mandeep Kaur-->
	 <!--Last Modified : 9 Dec, 2014-->
	 @param dictionary Dictionary from where value is to be fetched.
	 @param key key inside the dictionary whose value is needed.
	 @param valToReplace substring in the value to be replaced.
	 @param replaceWith value with which the substring needs to be replaced
	 @return Final value after replacing the substring.
	*/
	public final String getValueFromDictAndReplace(HashMap<String, String> dictionary, String key, String valToReplace, String replaceWith)
	{
		strErrMsg_GenLib = "";
		String Value = "", result = "";
		try
		{
			Value = dictionary.get(key);
			if (Value.contains(valToReplace))
			{
				result = Value.replace(valToReplace, replaceWith);
				if (result.contains(valToReplace))
				{
					throw new RuntimeException("Could not replace the required string: '" + valToReplace + "' with: '" + replaceWith + "' in: '" + Value + ".");
				}
			}
			else
			{
				throw new RuntimeException("Value obtained from dictionary does not contain substring to be replaced. Value obtained from dictionary : '" + Value + "'.");
			}
		}
		catch (RuntimeException e)
		{
			result = "";
			strErrMsg_GenLib = e.getMessage();
		}
		return result;
	}

	/** 
	 Add the given key and value pair to the given dictionary
	 
	 @param dictionary dictionary in which key is to be added.
	 @param key key needed to be added.
	 @param value Value of the key.
	 @return 
	*/
	public final boolean AddToDictionary(HashMap<String, String> dictionary, String key, String value)
	{
		boolean flag = false;
		strErrMsg_GenLib = "";
		try
		{
			if (dictionary.containsKey(key))
				dictionary.remove(key);
			dictionary.put(key, value);
			if (dictionary.containsKey(key))
				flag = true;
			else
				flag = false;
		}
		catch (RuntimeException e)
		{
			flag = false;
			strErrMsg_GenLib = e.getMessage();
			writeToLog("AddToDictionary() -- " + e.getMessage() + "\r\n" + e.getStackTrace());
		}
		return flag;
	}

	/** 
	 This function just returns the current date, time, timeformat and dateonly
	 
	 <!--Created By : Vinita Mahajan-->
	 @param item values can be : time,date,timeformat,dateonly
	 @return 
	*/
	public final String FetchDateTime(String item)
	{
		String Value = "";
		DateFormat dateFormat;
		Calendar objCalendar;
		try
		{
		switch (item.toString())
		{
			case "time":
				dateFormat = new SimpleDateFormat("yyyy/MM/dd H:mm");
				objCalendar = Calendar.getInstance();
				Value = dateFormat.format(objCalendar.getTime());
				break;
			case "date":
				dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				objCalendar = Calendar.getInstance();
				Value = dateFormat.format(objCalendar.getTime());
				break;
			case "timeformat": // TODO OWN : find way to find AM or PM from time 
				dateFormat = new SimpleDateFormat("tt");
				objCalendar = Calendar.getInstance();
				Value = dateFormat.format(objCalendar.getTime());
				break;
			case "dateonly":
				objCalendar = Calendar.getInstance();
				Value = objCalendar.getTime().getDate() + "";
				break;
		}
		}
		catch(Exception e)
		{
			strErrMsg_GenLib = e.getMessage();
			writeToLog("FetchDateTime()-- " + e.getStackTrace());
		}
		return Value;
	}
	
	public final String FetchDateTimeInSpecificFormat(String format)
	{
		String dateTime = "";
		try
		{
			DateFormat dateFormat = new SimpleDateFormat(format);
			Calendar objCalendar = Calendar.getInstance();
			dateTime = dateFormat.format(objCalendar.getTime());
		}
		catch(Exception e)
		{
			writeToLog("FetchDateTimeInSpecificFormat()-- " + e.getStackTrace());
		}
		return dateTime;
	}
	
	/**
	 * Gets a list of all the files included in the specified folder.
	 * @author mandeepm
	 * @since 15/6/2015
	 * @param folderPath Path of the folder for which list of included files needs to be fetched.
	 */
	public final void GetFilesListInFolder(String folderPath)
	{	
	File folder = new File(folderPath);
	File[] listOfFiles = folder.listFiles();

	    for (int i = 0; i < listOfFiles.length; i++) {
	      if (listOfFiles[i].isFile()) {
	        System.out.println("File " + listOfFiles[i].getName());
	      } else if (listOfFiles[i].isDirectory()) {
	        System.out.println("Directory " + listOfFiles[i].getName());
	      }
	    }
	}
	
	public final String GetSystemData()
	{
		String value = "";
		return value;
	}
	
}