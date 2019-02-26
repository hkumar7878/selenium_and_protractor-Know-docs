package com.common.utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;

import net.sourceforge.htmlunit.corejs.javascript.regexp.SubString;

import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.*;
//import Microsoft.VisualBasic.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.*;

import com.sun.jna.platform.win32.WinUser.FLASHWINFO;
//import ICSharpCode.SharpZipLib.Zip.*;

public class ApplicationLibrary extends GenericLibrary
{
	public static String strErrMsg_AppLib = "";
	private GenericLibrary genericLibrary = new GenericLibrary();

	public final boolean ClickUsingJSAndVerifyPage(String strelement, String objectToBeVerified)
	{
		boolean isEventSuccessful = false;
		strErrMsg_AppLib = "";
		try
		{
			isEventSuccessful = PerformAction(strelement, Action.ClickUsingJS);
			if (isEventSuccessful)
			{
				isEventSuccessful = PerformAction(objectToBeVerified, Action.WaitForElement, "5");
				if (!isEventSuccessful)
				{
					strErrMsg_AppLib = "Not navigated to destination after clicking on ";
				}
			}
			else
			{
				strErrMsg_AppLib = "Could not click on ";
			}
		}
		catch (RuntimeException e)
		{
			strErrMsg_AppLib = "ClickUsingJSAndVerifyPage--" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
		}
		return isEventSuccessful;
	}

	/** 
	 sets the filter from the drowpdown eg. dropdown = "status", filter = "Available", whichPage = "devices" will set the filter to show only available devices
	 <p>It will not affect any other filter ie. in the above example, if the platform filter is set to iOS this function will show available iOS devices</p>
	 <p>***** NOTE : STRING IN THE OPTION PARAMETER SHOULD BE IN THE CORRECT CASE AS IS SHOWN IN THE WEB UI.</p>
	 <p>ALSO, IT DOES NOT CHECK IF THE FILTER IS APPLIED PROPERLY OR NOT for APPLICATIONS filter</p>
	 
	 <!--Created By : Mandeep Mann-->
	 @param dropdown Takes values : "status" for status dropdown and "platform" for platform dropdown.
	 @param option for "status" filter - takes values: "Available", "In Use", "Disabled", "Reserved", "Offline", "All" 
	 <p> *NOTE : 'All' works in case of Applications page ONLY</p>
	 @param whichPage Page on which verification is to be done. Takes values : "Devices" and "Applications".
	 @param gridOrListView View for which verification is to be done . Takes values : "list" and "grid".
	 @param only It is to be passed only when user wants to uncheck all other selected options in the multiselect dropdown selected.
	 <p>It takes values : 'yes' if only one option should be selected and all others should be unchecked.</p>
	 <p>'no' if the already selected values are not to be unchecked.</p>
	 @return 
	*/

	public final boolean SelectFromFilterDropdowns(String dropdown, String option, String whichPage, String gridOrListView)
	{
		return SelectFromFilterDropdowns(dropdown, option, whichPage, gridOrListView, "No");
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public bool SelectFromFilterDropdowns(string dropdown, string option, string whichPage, string gridOrListView, string uncheckOtherOptions = "No")
	public final boolean SelectFromFilterDropdowns(String dropdown, String option, String whichPage, String gridOrListView, String uncheckOtherOptions)
	{
		boolean flag = false;
		String dropdownForXpath = "";
		String droptionOptionXpath = "";
		String dropdownButtonXpath = "";
		String dropdownButtonText = "";
		String dropdownArrowXpath = "";
		strErrMsg_AppLib = "";

		//// modifying to correct case
		if (dropdown.toLowerCase().equals("status"))
		{
			dropdown = "Status";
		}
		else if (dropdown.toLowerCase().equals("platform"))
		{
			dropdown = "Platform";
		}
		else if (dropdown.toLowerCase().equals("used by"))
		{
			dropdown = "Used By";
		}
		else if (dropdown.toLowerCase().equals("devices"))
		{
			dropdown = "Devices";
		}
		else if (dropdown.toLowerCase().equals("users"))
		{
			dropdown = "Users";
		}

		// Modifying the string 'dropdownForXpath' to be used in finding xpath of any option in a particular dropdown.
		if (dropdown.toLowerCase().equals("devices"))
		{
			dropdownForXpath = "device";
		}
		else if (dropdown.toLowerCase().equals("used by"))
		{
			dropdownForXpath = "user";
		}
		else
		{
			dropdownForXpath = dropdown;
		}

		//Modifying options to correct case
		if (option.toLowerCase().equals("available"))
		{
			option = "Available";
		}
		else if (option.toLowerCase().equals("in use"))
		{
			option = "In Use";
		}
		else if (option.toLowerCase().equals("reserved"))
		{
			option = "Reserved";
		}
		else if (option.toLowerCase().equals("disabled"))
		{
			option = "Disabled";
		}
		else if (option.toLowerCase().equals("offline"))
		{
			option = "Offline";
		}
		else if (option.toLowerCase().equals("android"))
		{
			option = "Android";
		}
		else if (option.toLowerCase().equals("ios"))
		{
			option = "iOS";
		}
		else if (option.toLowerCase().equals("all"))
		{
			option = "All";
		}
		/**
		*/

		// Getting identifications for filter and the respective dropdown option to be selected.
		dropdownButtonXpath = dicOR.get("eleDropdownFilter").replace("__FILTER__", dropdown);
		dropdownArrowXpath = dicOR.get("eledropdownArrow").replace("__FILTER__", dropdown);
		droptionOptionXpath = dicOR.get("eleDropdownFilter").replace("__OPTION__", option).replace("__FILTER__", dropdownForXpath.toLowerCase());

		// This piece of code is specifically for Applications page. remove this when improvement for changing the dropdown on applications page is done.
		if (whichPage.toLowerCase().equals("applications"))
		{
			flag = PerformAction(dicOR.get("elePlatformDropdown_appsPage"), Action.ClickAtCenter);
			if (flag)
			{
				String temp = dicOR.get("elePlatformOptionAndroid_Applications");
				flag = PerformAction(dicOR.get("elePlatformOption_appsPage").replace("__OPTION__", temp), Action.ClickUsingJS);
				
				if (!flag)
				{
					strErrMsg_AppLib = "Could not click on " + dropdownForXpath + " filter.";
				}
			}
			else
			{
				strErrMsg_AppLib = "Unable to click on Platform dropdown.";
			}
		}
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		//This block is for all other dropdowns as they are multiselect.
		else
		{
			if (uncheckOtherOptions.toLowerCase().equals("yes")) // If only parameter is 'yes', --
			{
				flag = UncheckAllOptionsInDropdown(dropdown); // -- then uncheck all the selected options in the drodown and as this function updated the strErrMsg_Applib, it is not required to assign value to strErrMsg_appLib again if this function fails.
			}
			else
			{
				flag = true; // If only is 'no', then there is no need to uncheck the selected options, then mark flag as true.
			}

			if (flag)
			{
				flag = PerformAction(dropdownArrowXpath, Action.Click);
				if (flag)
				{
					//Thread.Sleep("1000");
					// Clicking on the required option in dropdown
					flag = PerformAction(droptionOptionXpath, Action.WaitForElement, "5");
					if (flag)
					{
						flag = PerformAction(droptionOptionXpath, Action.Click);
						if (flag)
						{
							flag = PerformAction(dropdownArrowXpath, Action.Click);
							if (!flag)

							{
								strErrMsg_AppLib = "Could not click on dropdown button to collapse the " + dropdown + " filter.";
							}
						}
						else
						{
							strErrMsg_AppLib = "Could not click on the selected filter from " + dropdown + " dropdown.";
							return flag;
						}
					}
					else
					{
						strErrMsg_AppLib = dropdown + " dropdown not opened after user clicked on the dropdown button.";
					}
				}
				else
				{
					strErrMsg_AppLib = "Could not click on " + dropdown + " filter.";
				}
			}
			//else  //****** No need of else as the strErrMsg_AppLib is modified as when the UncheckAllOptionsInDropdown function is called.
		}

		// Verifying if this filter is applied properly
		if (flag)
		{
			switch (whichPage.toLowerCase())
			{
				case "devices":
					switch (uncheckOtherOptions.toLowerCase())
					{
						case "yes":
							if (dropdown.toLowerCase().equals("status"))
							{
								flag = VerifyDeviceDetailsInGridAndListView("devicestatus", option, gridOrListView);
							}
							else if (dropdown.toLowerCase().equals("platform") && !option.toLowerCase().equals("all"))
							{
								flag = VerifyDeviceDetailsInGridAndListView("deviceplatform", option, gridOrListView);
							}
							else if (dropdown.toLowerCase().equals("used by") && !option.toLowerCase().equals("all"))
							{
								flag = VerifyDeviceDetailsInGridAndListView("deviceUser", option, gridOrListView);
							}
							break;
						case "no":
							dropdownButtonText = GetTextOrValue(dropdownButtonXpath, "text");
							if (dropdownButtonText.contains(option))
							{
								flag = true;
							}
							else
							{
								flag = false;
								strErrMsg_AppLib = "Could not select '" + option + "' from '" + dropdown + "' filter.";
							}
							break;
						//case "applications":
						//    if (!option.ToLower().Equals("all"))
						//        flag = VerifyAppDetailsInListView("platform", option);
						//    break;
					}
					break;
				case "applications":
					if (!option.toLowerCase().equals("all"))
					{
						flag = VerifyAppDetailsInListView("platform", option);
					}
					break;
			}
		}

		//If there are no devices related to the given filter, then 'true' should be returned.
		if (strErrMsg_AppLib.contains("deviceConnect currently has no configured"))
		{
			flag = true;
		}
		return flag;
	}

	/** 
	 This function unchecks all the checked options for the given dropdown.
	 <p>**NOTE : It does not work for Platform dropdown on Applications page right now as it is not a multiselect dropdown.</p>
	 
	 <!--Created By : Hitesh Ghai-->
	 @param dropdownName Dropdown (label) name for which all the options are to be unchecked.
	 @return True or False
	*/
	public final List<String> GetSelectedOptionsInDropdown(String dropdownName) //**** put option , string optionToLeaveSelected="" so that it becomes more efficient and unchecks all options except this option so that the scripts work faster . also put option that of "" is passed then uncheck all the options
	{
		List<String> SelectedOptions = new ArrayList<String>();
		boolean flag = false;
		int optionsCount = 0;
		String errorIndices = "";
		strErrMsg_AppLib = "";
		try
		{
			flag = PerformAction((dicOR.get("eleDropdownFilter").replace("__FILTER__", dropdownName)), Action.isDisplayed);
			if (flag)
			{
				flag = PerformAction((dicOR.get("eleDropdownFilter").replace("__FILTER__", dropdownName)), Action.Click);
				if (flag)
				{
					if ((optionsCount = getelementCount(dicOR.get("eleAllDropdownOptions").replace("__FILTER__", dropdownName))) != 0)
					{
						for (int i = 1; i <= optionsCount; i++)
						{
							flag = PerformAction(dicOR.get("eleCheckMarkDropdownOption").replace("__FILTER__", dropdownName).replace("__INDEX__", (new Integer(i)).toString()), Action.isDisplayed);
							if (flag)
							{

								SelectedOptions.add(GetTextOrValue(dicOR.get("eleAllDropdownOptions").replace("__FILTER__", dropdownName) + "[" + i + "]", "text"));
								//flag = PerformAction(dicOR["eleAllDropdownOptions"].replace("__FILTER__", dropdownName) + "[" + i + "]", Action.Click);
								if (!flag)
								{
									errorIndices = errorIndices + i + ",";
								}
							}
						}
						if (!strErrMsg_AppLib.equals(""))
						{
							throw new RuntimeException("Could not click on options at indices :" + errorIndices + "for " + dropdownName + " dropdown.");
						}
					}
					else
					{
						throw new RuntimeException("No options are there under the selected dropdown OR dropdown list is not open.");
					}

					flag = PerformAction((dicOR.get("eleDropdownFilter").replace("__FILTER__", dropdownName)), Action.Click); //Click on the dropdown again so as the options close
					if (!flag)
					{
						throw new RuntimeException("Could not click on " + dropdownName + " dropdown to close.");
					}
					if (!GetTextOrValue(dicOR.get("eleDropdownFilter").replace("__FILTER__", dropdownName), "text").equals("All"))
					{
						writeToLog("Text displayed on the dropdown is not 'All' which means all options may not have been unchecked.");
					}
					//******Insert a verification if the options are hidden after click or not. They should hide after the above step
				}
				else
				{
					throw new RuntimeException("Could not click on dropdown : " + dropdownName);
				}
			}
			else
			{
				throw new RuntimeException("Dropdown " + dropdownName + "not displayed on the opened page.");
			}
		}
		catch (RuntimeException e)
		{
			//flag = false;                
			strErrMsg_AppLib = "GetSelectedOptionsInDropdown--" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
		}
		return SelectedOptions;
	}

	/** 
	 This function unchecks all the checked options for the given dropdown and closes the dropdown by clicking on the top nav bar where there are no clickable controls.
	 <p>**NOTE : It does not work for Platform dropdown on Applications page right now as it is not a multiselect dropdown.</p>
	 
	 <!--Created By : Mandeep Mann-->
	 @param dropdownName Dropdown (label) name for which all the options are to be unchecked.
	 @return True or False
	*/
	public final boolean UncheckAllOptionsInDropdown(String dropdownName) //**** put option , string optionToLeaveSelected="" so that it becomes more efficient and unchecks all options except this option so that the scripts work faster . also put option that of "" is passed then uncheck all the options
	{
		boolean flag = false, loopEntered = false;
		int optionsCount = 0;
		String errorIndices = "";
		strErrMsg_AppLib = "";
		try
		{
			flag = PerformAction((dicOR.get("eleDropdownFilter").replace("__FILTER__", dropdownName)), Action.isDisplayed);
			if (flag)
			{
				flag = PerformAction((dicOR.get("eleDropdownFilter").replace("__FILTER__", dropdownName)), Action.Click);
				if (flag)
				{
					if ((optionsCount = getelementCount(dicOR.get("eleAllDropdownOptions").replace("__FILTER__", dropdownName))) != 0)
					{
						for (int i = 1; i <= optionsCount; i++)
						{
							loopEntered = true;
							flag = PerformAction(dicOR.get("eleCheckMarkDropdownOption").replace("__FILTER__", dropdownName).replace("__INDEX__", (new Integer(i)).toString()), Action.isDisplayed);
							if (flag)
							{
								flag = PerformAction(dicOR.get("eleCheckMarkDropdownOption").replace("__FILTER__", dropdownName).replace("__INDEX__", (new Integer(i)).toString()), Action.Click);
								//flag = PerformAction(dicOR["eleAllDropdownOptions"].replace("__FILTER__", dropdownName) + "[" + i + "]", Action.Click);
								flag = !PerformAction(dicOR.get("eleCheckMarkDropdownOption").replace("__FILTER__", dropdownName).replace("__INDEX__", (new Integer(i)).toString()), Action.isDisplayed);
								if (!flag)
								{
									errorIndices = errorIndices + i + ",";
								}
							}
						}
						if ((!errorIndices.equals("")) || (loopEntered == false))
						{
							throw new RuntimeException("Could not click on options at indices :" + errorIndices + "for " + dropdownName + " dropdown.");
						}
					}
					else
					{
						throw new RuntimeException("No options are there under the selected dropdown OR dropdown list is not open.");
					}

					//Close the dropdown if it is open and verify that the dropdown options are hidden.
					if (PerformAction(dicOR.get("eleAllDropdownOptions").replace("__FILTER__", dropdownName), Action.isDisplayed))
					{
						flag = PerformAction((dicOR.get("eleDropdownFilter").replace("__FILTER__", dropdownName)), Action.Click); //Click on the dropdown again so as the options close
						if (flag)
						{
							//Code to check if the options are hidden or not after clicking on dropdown again.
							flag = !PerformAction(dicOR.get("eleAllDropdownOptions").replace("__FILTER__", dropdownName), Action.isDisplayed);
							if (!flag)
							{
								throw new RuntimeException("Dropdown not closed after clicking on the dropdown again.");
							}
						}
						else
						{
							throw new RuntimeException("Could not click on " + dropdownName + " dropdown to close.");
						}
					}
					if (!GetTextOrValue(dicOR.get("eleDropdownFilter").replace("__FILTER__", dropdownName), "text").equals("All"))
					{
						throw new RuntimeException("UncheckAllOptionsInDropdown -- Text displayed on the dropdown is not 'All' which means all options may not have been unchecked.");
					}
				}
				else
				{
					throw new RuntimeException("Could not click on dropdown : " + dropdownName);
				}
			}
			else
			{
				throw new RuntimeException("Dropdown " + dropdownName + "not displayed on the opened page.");
			}
		}
		catch (RuntimeException e)
		{
			flag = false;
			strErrMsg_AppLib = "UncheckAllOptionsInDropdown--" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
		}
		return flag;
	}

	// ######*********Obsolete function

	public final boolean SelectFilter(String strFilter)
	{
		return SelectFilter(strFilter, "all");
	}

	public final boolean SelectFilter()
	{
		return SelectFilter("NoFilter", "all");
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public bool SelectFilter(string strFilter = "NoFilter", string strResetFilter = "all")
	public final boolean SelectFilter(String strFilter, String strResetFilter)
	{
		boolean isEventSuccessful = false;
		try
		{
			PerformAction("browser", Action.Refresh);
			switch (strResetFilter.toLowerCase())
			{
				case "all":
					selectPlatform("all");
					if (PerformAction("chkAvailable", Action.isSelected))
					{
						PerformAction("chkAvailable", Action.Click);
					}
					if (PerformAction("chkInUse", Action.isSelected))
					{
						PerformAction("chkInUse", Action.Click);
					}
					break;
				case "available":
					if (PerformAction("chkAvailable", Action.isSelected))
					{
						PerformAction("chkAvailable", Action.Click);
					}
					break;
				case "inuse":
					if (PerformAction("chkInUse", Action.isSelected))
					{
						PerformAction("chkInUse", Action.Click);
					}
					break;
				case "platform":
					selectPlatform("all");
					break;
				case "none":
					break;
			}
			switch (strFilter.toLowerCase())
			{

				case "available":
					isEventSuccessful = PerformAction("chkAvailable", Action.Click);
					if (!isEventSuccessful)
					{
						throw new RuntimeException("Could not select filter 'Available'.");
					}
					break;
				case "inuse":
					isEventSuccessful = PerformAction("chkInUse", Action.Click);
					if (!isEventSuccessful)
					{
						throw new RuntimeException("Could not select filter 'Available'.");
					}
					break;
				case "platform":
					isEventSuccessful = selectPlatform("all");
					if (!isEventSuccessful)
					{
						throw new RuntimeException(strErrMsg_AppLib);
					}
					break;
				case "nofilter":
					isEventSuccessful = true;
					break;
			}
		}
		catch (RuntimeException e)
		{
			isEventSuccessful = false;
			strErrMsg_AppLib = "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
		}
		return isEventSuccessful;
	}



	/** 
	 This function performs various activities on Users page . eg. 
	 
	 @param strUser
	 @param strAction
	 @param strObject
	 @param strView
	 @return 
	*/

	public final boolean PerformActionsInUsersPage(String strUser, String strAction, String strObject)
	{
		return PerformActionsInUsersPage(strUser, strAction, strObject, "card");
	}

	public final boolean PerformActionsInUsersPage(String strUser, String strAction)
	{
		return PerformActionsInUsersPage(strUser, strAction, "", "card");
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public bool PerformActionsInUsersPage(string strUser, string strAction, string strObject = "", string strView = "card")
	public final boolean PerformActionsInUsersPage(String strUser, String strAction, String strObject, String strView)
	{
		boolean isEventSuccessful = false;
		String strErr = "";
		strErrMsg_AppLib = "";
		int ActiveUsersCount = 0;
		try
		{
			StopAutoRefresh(strView);
			int intRange = 0;
			if (strUser.contains("@") || strUser.startsWith("USER:") || strUser.startsWith("EMAIL:"))
			{
				String xPath = "";
				if (strUser.contains("@"))
				{
					xPath = "//*[text()='" + strUser + "']/../.." + strObject;
				}
				else if (strUser.startsWith("USER:") || strUser.startsWith("EMAIL:"))
				{
					xPath = "//*[text()='" + strUser.split("[:]", -1)[1] + "']/../../.." + strObject;
				}
				if ((dicCommon.get("BrowserName").toLowerCase().equals("ie")) && strAction.toLowerCase().equals("click"))
				{
					isEventSuccessful = PerformAction(xPath, Action.ClickUsingJS);
				}
				else
				{
					isEventSuccessful = PerformAction(xPath, strAction);
				}
				if (!isEventSuccessful)
				{
					throw new RuntimeException("Could not perform action : '" + strAction + "' on object : '" + strObject + "' for user : '" + strUser);
				}
			}
			else
			{
				if (strUser.toLowerCase().equals("first"))
				{
					intRange = 1;
				}
				else if (strUser.toLowerCase().equals("all"))
				{
					if (strView.toLowerCase().equals("card"))
					{
						intRange = driver.findElements(By.className("user-card")).size();
						if (strAction.toLowerCase().equals("count"))
						{
							if (dicOutput.containsKey("UsersCount"))
							{
								dicOutput.remove("UsersCount");
							}
							dicOutput.put("UsersCount", (new Integer(intRange)).toString());
							return true;
						}
					}
					else
					{
						intRange = driver.findElements(By.xpath("//tbody/tr")).size();
						if (strAction.toLowerCase().equals("count"))
						{
							if (dicOutput.containsKey("UsersCount"))
							{
								dicOutput.remove("UsersCount");
							}
							dicOutput.put("UsersCount", (new Integer(intRange)).toString());
							return true;
						}
					}

				}

				for (int i = 1; i < intRange; i++)
				{
					String xPathUsersHolder = "";
					if (strView.toLowerCase().equals("card"))
					{
						xPathUsersHolder = "//li[" + i + "]/div";
					}
					else
					{
						xPathUsersHolder = "//tbody/tr[" + i + "]/td";
					}
					switch (strAction.toLowerCase())
					{
						case "exist":
							isEventSuccessful = PerformAction(xPathUsersHolder + strObject, Action.Exist);
							if (!isEventSuccessful)
							{
								strErr = strErr + (new Integer(i)).toString() + ", ";
							}
							break;

						case "getallactiveusernames":
							String userId = null;
							if (GetTextOrValue(xPathUsersHolder + "//p[1]//span", "text").contains("Active"))
							{
								ActiveUsersCount++;
								userId = GetTextOrValue(xPathUsersHolder + "//p[2]//span", "text");
								if (userId == null)
								{
									throw new RuntimeException("Could not get the userID from Users Page.");
								}
								try
								{
									dicOutput.put("userId" + ActiveUsersCount, userId);
									isEventSuccessful = true;
								}
								catch (RuntimeException e)
								{
									throw new RuntimeException("Could not put userID in dicOutput dictionary.");
								}
							}
							break;
					}
				}
				dicOutput.put("ActiveUsersCount", (new Integer(ActiveUsersCount)).toString());
			}


			if (strErr.equals(""))
			{
				isEventSuccessful = true;
			}
			else
			{
				throw new RuntimeException("Action " + strAction + " failed for object " + strObject + " for user(s) " + strErr);
			}
		}
		catch (RuntimeException e)
		{
			dicOutput.put("ActiveUsersCount", "0");
			isEventSuccessful = false;
			strErrMsg_AppLib = "PerformActionsInUsersPage--" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
		}
		return isEventSuccessful;
	}


	// blnCallQTPScript variable is false in case user only needs to click on app name in app list that appears after clicking on Connect button
	/** 
	 Connects to a device of given platform with given application.
	 
	 @param strPlatform Platform of the device which is required to be connected.
	 @param strApplication Name of the application to be connected with. Provide exact name that appears on 'Launch application' dialog, eg. 'Phone Lookup'
	 @param whichViewer Defines which viewer to connect the device with : desktop or webviewer.
	 @return 
	*/

	/*public final boolean ConnectDevice(String strPlatform, String strApplication, boolean blnCallQTPScript)
	{
		return ConnectDevice(strPlatform, strApplication, blnCallQTPScript, "desktop");
	}
*/
	/*public final boolean ConnectDevice(String strPlatform, String strApplication)
	{
		return ConnectDevice(strPlatform, strApplication, true, "desktop");
	}*/

	/*public final boolean ConnectDevice(String strPlatform)
	{
		return ConnectDevice(strPlatform, "", true, "desktop");
	}*/

	/*public final boolean ConnectDevice()
	{
		return ConnectDevice("iOS", "", true, "desktop");
	}*/

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public bool ConnectDevice(string strPlatform = "iOS", string strApplication = "", bool blnCallQTPScript = true, string whichViewer = "desktop")
	/*public final boolean ConnectDevice(String strPlatform, String strApplication, boolean blnCallQTPScript, String whichViewer)
	{
		strErrMsg_AppLib = "";
		String strStepDescription = "Connect a device and launch an application to it.";
		String strExpectedResult = "Selected application should be launched on Ai Display.";
		if (strPlatform.toLowerCase().equals("ios"))
		{
			strPlatform = "iOS";
		}

		boolean isEventSuccessful = false;
		//PerformAction("browser", Action.Refresh);
		String xpathApplication = "";
		String deviceSelected = "";
		if (strApplication.equals(""))
		{
			xpathApplication = dicOR.get("eleAppListItem");
		}
		else
		{
			xpathApplication = "//a[contains(text(),'" + strApplication + "')]";
		}
		try
		{
			if (!PerformAction("eleDevicesHeader", Action.isDisplayed))
			{
				if (!selectFromMenu("Devices", "eleDevicesHeader"))
				{
					throw new RuntimeException("On selecting 'Devices' menu, 'Devices' page is not opened.");
				}
			}
			//if (SelectFilter("Available"))
			if (SelectFromFilterDropdowns("status", "Available", "devices", "grid"))
			{
				if (!SelectFromFilterDropdowns("platform", strPlatform, "devices", "grid"))
				{
					throw new RuntimeException(strErrMsg_AppLib);
				}
			}
			else
			{
				throw new RuntimeException(strErrMsg_AppLib);
			}
			if (SelectDevice("first", "connect", "grid"))
			{
				if (dicOutput.containsKey("selectedDeviceName"))
				{
					deviceSelected = dicOutput.get("selectedDeviceName");
				}
				Thread.sleep(5000);
				if (PerformAction(xpathApplication, Action.WaitForElement, "30"))
				{
					//KillObjectInstances("MobileLabs.deviceviewer");
					if (PerformAction(xpathApplication, Action.Click))
					{
						isEventSuccessful = PerformAction("eleNotificationRightBottom", Action.WaitForElement, "120");
						if (isEventSuccessful)
						{
							String notificationText = GetTextOrValue("eleNotificationRightBottom", "text");
							isEventSuccessful = notificationText.contains("Application installed successfully.");
							if (!isEventSuccessful)
							{
								throw new RuntimeException("Notification - 'Application install successful' is not displayed. Following notification is displayed - '" + notificationText + "'.");
							}
						}
						else
						{
							throw new RuntimeException("No success notification displayed on devices page.");
						}
					}
					else
					{
						throw new RuntimeException("Could not click on application " + strApplication);
					}
				}
				else
				{
					throw new RuntimeException("Application list is not displayed on clicking on connect button.");
				}
			}
			else
			{
				throw new RuntimeException(strErrMsg_AppLib);
			}
			if (blnCallQTPScript)
			{
				if (genericLibrary.LaunchQTPScript("LaunchApplication", strStepDescription + " <br> Device name : '" + deviceSelected + "'.", strExpectedResult))
				{
					isEventSuccessful = true;
				}
				else
				{
					throw new RuntimeException(strErrMsg_GenLib);
				}
			}
		}
		catch (RuntimeException e)
		{
			isEventSuccessful = false;
			strErrMsg_AppLib = "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
			if (blnCallQTPScript)
			{
				//(new Reporter()).ReportStep("Connect to device.", "Application should gets launched in device.", strErrMsg_AppLib, "Fail");
				writeToLog("ConnectDevice -- " + e.getMessage());
			}
		}
		//SelectFilter();
		SelectFromFilterDropdowns("status", "All", "devices", "grid");
		SelectFromFilterDropdowns("platform", "All", "devices", "grid");
		//PerformAction("browser", Action.Refresh);
		return isEventSuccessful;
	}*/


	public final boolean StopAutoRefresh()
	{
		return StopAutoRefresh("grid");
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public bool StopAutoRefresh(string strView = "grid")
	public final boolean StopAutoRefresh(String strView)
	{
		//bool flag = false;
		//string strJavascript = "";
		//if (strView.ToLower() == "grid")
		//    strJavascript = "document.getElementsByClassName('cards-layout')[0].setAttribute('data-bind','')";
		//else
		//    strJavascript = "document.getElementsByTagName('tbody')[0].setAttribute('data-bind','')";
		//try
		//{
		//    genericLibrary.ExecuteJavascript(strJavascript);
		//    flag = true;
		//}
		//catch (Exception e)
		//{
		//    flag = false;
		//    strErrMsg_AppLib = e.Message;
		//}
		return true;
	}


	/** 
	 This function creates a User with specified parameters
	 If emailID is not appended with "@deviceconnect.com", function will append the current date & time and create a new emailID accordingly.
	 If emailID is simple "newuser", then it will append, if emailID is "newuser@deviceconnect.com" then it won't append the current date time.
	 The newly created User with emailID with current date and time is saved in the output dictionary named as EmailID
	 
	 <!--Modified By : Vinita Mahajan-->
	 @param firstName First Name of the User
	 @param lastName Last Name of the User
	 @param emailID email ID with which to perform Login
	 @param password
	 @param userType Role : admin, testuser - testuser is by default created if not specified
	 @param enabled This indicated whether the User created should be active : enabled or inactive : disbaled
	 @return This function returns boolean value.
	*/

	public final boolean createUser(String firstName, String lastName, String emailID, String password, String userType)
	{
		return createUser(firstName, lastName, emailID, password, userType, true);
	}

	public final boolean createUser(String firstName, String lastName, String emailID, String password)
	{
		return createUser(firstName, lastName, emailID, password, "testuser", true);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public bool createUser(string firstName, string lastName, string emailID, string password, string userType = "testuser", bool enabled = true)
	public final boolean createUser(String firstName, String lastName, String emailID, String password, String userType, boolean enabled)
	{
		boolean flag = false;
		strErrMsg_AppLib = "";
		//default value of emailID
		try
		{
			if (dicOutput.containsKey("EmailID"))
			{
				dicOutput.remove("EmailID");
			}

			if (firstName.equals(""))
			{
				firstName = "selenium";
			}
			if (lastName.equals(""))
			{
				lastName = "user";
			}
			if (password.equals(""))
			{
				password = "deviceconnect";
			}
			if (userType.equals(""))
			{
				userType = "testuser";
			}

			if (emailID.equals("")) //If email ID is not given by user, then assign it value and put to dictionary
			{
				emailID = FetchDateTimeInSpecificFormat("MMddyy_hhmmss") + "@deviceconnect.com";
				dicOutput.put("EmailID", emailID);
			}


			if (!(emailID.contains("@deviceconnect.com"))) //If email ID is not in correct format, then correct it and put the value in dicOutput
			{
				emailID = emailID + FetchDateTimeInSpecificFormat("MMddyy_hhmmss") + "@deviceconnect.com";
				dicOutput.put("EmailID", emailID);
			}


			if (PerformAction("lnkLogout", Action.isDisplayed))
			{
				flag = PerformAction("btnMenu", Action.Click);
			}
			if (!PerformAction("inpFirstNameCreateUser", Action.isDisplayed))
			{
				flag = PerformAction("btnCreateUser", Action.Click);
			}
			else
			{
				flag = true;
			}
			if (flag)
			{
				flag = PerformAction("inpFirstNameCreateUser", Action.WaitForElement);
				if (flag)
				{
					//PerformAction("txtFirstName", Action.Click);
					flag = PerformAction("inpFirstNameCreateUser", Action.Type, firstName);
					if (!flag)
					{
						throw new RuntimeException("Could not type in First Name field");
					}

					//PerformAction("txtLastName", Action.Click);
					flag = PerformAction("inpLastNameCreateUser", Action.Type, lastName);
					if (!flag)
					{
						throw new RuntimeException("Could not type in Last Name field");
					}

					//PerformAction("txtLogin", Action.Click);
					flag = PerformAction("txtLogin", Action.Type, emailID);
					if (!flag)
					{
						throw new RuntimeException("Could not type in Login field");
					}

					//PerformAction("txtPassword", Action.Click);
					flag = PerformAction("txtPassword", Action.Type, password);
					if (!flag)
					{
						throw new RuntimeException("Could not type in Password field");
					}

					//PerformAction("txtConfirmPassword", Action.Click);
					flag = PerformAction("txtConfirmPassword", Action.Type, password);
					if (!flag)
					{
						throw new RuntimeException("Could not type in Confirm Password field");
					}

					// in case admin type user is to be created, then click  on dropdown , select 'Admin' and 
					if (userType.toLowerCase().equals("admin"))
					{
						flag = PerformAction("btnTesterDropdown_CreateUserPage", Action.Click);
						if (flag)
						{
							flag = PerformAction("eleAdminOption_RoleDropdown", Action.WaitForElement, "10");
							if (flag)
							{
								flag = PerformAction("eleAdminOption_RoleDropdown", Action.Click);
								if (flag)
								{
									//Verify Admin is selected.
									flag = GetTextOrValue("btnTesterDropdown_CreateUserPage", "text").equals("Admin");
									if (!flag)
									{
										throw new RuntimeException("Admin option is not selected.");
									}
								}
								else
								{
									throw new RuntimeException("Could not click on 'Admin' option on dropdown.");
								}
							}
							else
							{
								throw new RuntimeException("Dropdown not displayed after clicking on 'Tester/Admin' dropdown.");
							}
						}
						else
						{
							throw new RuntimeException("Could not click on admin/tester dropdown.");
						}
					}

					// now select if the user is to be disabled as by default it is enabled
					if (enabled == false)
					{
						flag = PerformAction("chkActive_CreateUser", Action.Click);
						if (flag)
						{
							flag = !PerformAction("chkActive_CreateUser", Action.isSelected);
							if (!flag)
							{
								throw new RuntimeException("Checkbox is not unchecked.");
							}
						}
						else
						{
							throw new RuntimeException("Could not click on Checkbox for active.");
						}
					}

					PerformAction("browser", Action.Scroll, "0");
					flag = PerformAction("btnSave", Action.Click);
					if (!flag)
					{
						throw new RuntimeException("Could not click on 'Save' button");
					}

					flag = PerformAction("eleNotificationRightBottom", Action.WaitForElement);
					if (flag)
					{
						String text = GetTextOrValue("eleNotificationRightBottom", "text");
						flag = text.contains("User created.");
						if (!flag)
						{
							throw new RuntimeException("'Notification does not read : 'User updated.' but : " + text); // **text used in script 252
						}
					}
					else
					{
						throw new RuntimeException("'User updated.' notification did not appear on the page.");
					}

				}
				else
				{
					throw new RuntimeException("Create User page not displayed after clicking on 'Create User' button");
				}
			}
			else
			{
				throw new RuntimeException("Could not click on 'Create User' button ");
			}
		}
		catch (RuntimeException e)
		{
			strErrMsg_AppLib = "createUser---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
			flag = false;
		}
		return flag;
	}


	/** 
	 This Function fetches all the details of all devices
	 
	 <!--Modified By : Vinita  Mahajan-->
	 @param strDetailName
	 @param strView
	 @return 
	*/

	public final ArrayList<String> GetAllDevicesDetails(String strDetailName)
	{
		return GetAllDevicesDetails(strDetailName, "grid");
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public List<string> GetAllDevicesDetails(string strDetailName, string strView = "grid")
	public final ArrayList<String> GetAllDevicesDetails(String strDetailName, String strView)
	{
		strErrMsg_AppLib = "";
		String xpathDevicesHolder;
		ArrayList<String> lstDevicesDetails = new ArrayList<String>();
		if (strView.toLowerCase().equals("list"))
		{
			xpathDevicesHolder = GenericLibrary.dicOR.get("eleDevicesHolderListView");
		}
		else
		{
			xpathDevicesHolder = GenericLibrary.dicOR.get("eleDevicesHolderGridView");
		}
		try
		{
			if (PerformAction("eleDevicesHeader", Action.WaitForElement))
			{
				//Verifying devices are displayed
				if (GetTextOrValue("class=message", "text").contains("deviceConnect currently has no configured devices or your filter produced no results."))
				{
					throw new RuntimeException("deviceConnect currently has no configured devices or your filter produced no results.");
				}
				StopAutoRefresh(strView);
				int noOfDevices = getelementCount(xpathDevicesHolder);
				//int noOfDevices = driver.FindElements(By.XPath(xpathDevicesHolder)).Count;
				for (int i = 1; i <= noOfDevices; i++)
				{
					String strValue = GetDeviceDetailInGridAndListView(i, strDetailName);
					if (!strValue.equals(""))
					{
						lstDevicesDetails.add(strValue);
					}
					else
					{
						throw new RuntimeException(strErrMsg_AppLib);
					}
				}
			}
			else
			{
				throw new RuntimeException("Devices page is not displayed");
			}
		}
		catch (RuntimeException e)
		{
			strErrMsg_AppLib = "GetAllDevicesDetails---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
			lstDevicesDetails.clear();
		}
		return lstDevicesDetails;
	}


	public final boolean RebootDevice(String strAction, boolean gotoDeviceDetails)
	{
		return RebootDevice(strAction, gotoDeviceDetails, "all");
	}

	public final boolean RebootDevice(String strAction)
	{
		return RebootDevice(strAction, false, "all");
	}

	public final boolean RebootDevice()
	{
		return RebootDevice("reboot", false, "all");
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public bool RebootDevice(string strAction = "reboot", bool gotoDeviceDetails = false, string strPlatform = "all")
	public final boolean RebootDevice(String strAction, boolean gotoDeviceDetails, String strPlatform)
	{
		strErrMsg_AppLib = "";
		boolean isEventSuccessful = false;
		try
		{
			if (gotoDeviceDetails)
			{
				//PerformAction(dicOR["chkAvailable"], Action.Click);
				SelectFromFilterDropdowns("status", "Available", "devices", "grid");
				if (SelectFromFilterDropdowns("platform", strPlatform, "device details", "grid"))
				{
					if (!SelectDevice("first", "devicedetails"))
					{
						throw new RuntimeException(strErrMsg_AppLib);
					}
				}
				else
				{
					throw new RuntimeException(strErrMsg_AppLib);
				}
			}
			switch (strAction.toLowerCase())
			{
				case "reboot":
					if (PerformAction("btnReboot", Action.isDisplayed))
					{
						if (PerformAction("btnReboot", Action.isDisplayed))
						{
							if (PerformAction("btnReboot", Action.Click))
							{
								//Verifying confirmation popup is opened.
								isEventSuccessful = PerformAction("eleConfirmReboot", Action.WaitForElement);
								if (isEventSuccessful)
								{
									isEventSuccessful = PerformAction("btnContinue", Action.Click);
									if (isEventSuccessful)
									{
										//Verify device status is changed to 'Offline'.
										//isEventSuccessful = WaitTillDeviceStatusIsChanged(30,"Offline");
										isEventSuccessful = PerformAction("//span[text()='Offline']", Action.WaitForElement, "60");
										if (!isEventSuccessful)
										{
											throw new RuntimeException("Device status is not changed to 'Offline'.");
										}
									}
									else
									{
										throw new RuntimeException("Could not click on 'Continue' button on 'Confirm Reboot' popup.");
									}
								}
								else
								{
									throw new RuntimeException("'Confirm Reboot' popup is not opened.");
								}
							}
							else
							{
								throw new RuntimeException("Could not click on reboot button.");
							}
						}
						else
						{
							throw new RuntimeException("Reboot button is not enabled.");
						}
					}
					else
					{
						throw new RuntimeException("Reboot button is not displayed.");
					}
					break;

				case "verifyrebootdisable":
					if (!PerformAction("btnReboot", Action.isEnabled))
					{
						isEventSuccessful = true;
					}
					else
					{
						throw new RuntimeException("Reboot button is enabled.");
					}
					break;
			}

		}
		catch (RuntimeException e)
		{
			isEventSuccessful = false;
			strErrMsg_AppLib = "RebootDevice---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
		}
		return isEventSuccessful;
	}

	/** 
	 Verifies that delete button is visible for all apps displayed on Applications index page.
	 
	 <!--Created By : Mandeep Mann-->
	 @return True if all apps have this button enabled and False if any one of them does not have either 'Install' button enabled or 'Delete' button accessible.
	*/

	public final boolean VerifybtnDeleteOnApplicationsPage()
	{
		return VerifybtnDeleteOnApplicationsPage(true);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public bool VerifybtnDeleteOnApplicationsPage(bool chkDisplayed = true)
	public final boolean VerifybtnDeleteOnApplicationsPage(boolean chkDisplayed)
	{
		boolean flag = false;
		boolean clickable = false; // displayed = false;
		int AppsCount = 0;
		String errorIndices = "";
		boolean strVerification = false;
		strErrMsg_AppLib = "";
		AppsCount = getelementCount(dicOR.get("eleAppTableRows"));
		String deletebtnXPath = getValueFromDictionary(dicOR, "eleDeleteOption_AppPage");

		if (AppsCount != 0)
		{
			for (int i = 1; i <= AppsCount; i++)
			{
				clickable = PerformAction(dicOR.get("eleInstallAppDropdown") + "[" + i + "]", Action.Click);
				// Conditional verification according to whether it is required to check that delete button is displayed or it is hidden
				if (chkDisplayed)
				{
					strVerification = PerformAction(deletebtnXPath + "[" + i + "]", Action.isDisplayed);
				}
				else
				{
					strVerification = !PerformAction(deletebtnXPath + "[" + i + "]", Action.isDisplayed);
				}
				if (!strVerification || !clickable)
				{
					errorIndices = errorIndices + ", " + i;
				}
			}
		}
		else
		{
			strErrMsg_AppLib = "VerifybtnDeleteOnApplicationsPage--" + "Applications list is empty or is not found on page.";
			return false;
		}

		// Check if there is error message . If no error is appended to errorIndices it means everything went alright function is pass.
		if (errorIndices.equals(""))
		{
			flag = true;
		}
		else
		{
			if (chkDisplayed)
			{
				strErrMsg_AppLib = "VerifybtnDeleteOnApplicationsPage---" + "Delete button is not visible for application at these indices: " + errorIndices;
			}
			else
			{
				strErrMsg_AppLib = "VerifybtnDeleteOnApplicationsPage---" + "Delete button is visible for applications at these indices: " + errorIndices;
			}
		}
		return flag;
	}
	
	 
    //Verifies that the value of 'Provisioned' matches the value passed, for each device under 'Compatible Devices' section on app details page.
    //
    //<!--Created By : Mandeep Mann-->
    //@return True if all devices have the given status for provisioned column.
    //
    public final boolean VerifyProvisionedValue_AppDetails(String value)
    {
        boolean flag = false;
        int RowsCount = 0;
        String errorIndices = "";
        String strProvisionedValue = "";
        strErrMsg_AppLib = "";
        RowsCount = getelementCount(dicOR.get("eleDeviceListNameCol"));

        if (RowsCount != 0)
        {
            for (int i = 2; i <= RowsCount + 1; i++)
            {
                strProvisionedValue = GetTextOrValue(dicOR.get("ProvisionedValue_Appdetails").replace("__INDEX__", (new Integer(i)).toString()), "text");
                if ( ! strProvisionedValue.trim().equals(value))
                {
                    errorIndices = errorIndices + ", " + i;
                }
            }
        }
        else
        {
            if (PerformAction("CompatibleDevWarning_appDetails", Action.isDisplayed))
            {
                strErrMsg_AppLib = "VerifyProvisionedValue_AppDetails---" + "There are no compatible devices for the application.";
            }
            else
            {
                strErrMsg_AppLib = "VerifyProvisionedValue_AppDetails---" + "Compatible devices list is empty or is not found on page.";
            }
            return false;
        }

        // Check if there is error message . Empty errorIndices means everything is as expected
        if (errorIndices.equals(""))
        {
            flag = true;
        }
        else
        {
            strErrMsg_AppLib = "VerifyProvisionedValue_AppDetails---" + "Provisioned value is not '" + value + "' for devices at indices : " + errorIndices;
        }
        return flag;
    }

    //public bool ReleaseDevice(string userName, string Platform = "All")
    //{

    //    bool flag = false;
    //    try
    //    {


    //        // go to devices page if it is not already displayed.
    //        if (!PerformAction("eleDevicesHeader", Action.isDisplayed))
    //        {

    //            if (!selectFromMenu("Devices", "eleDevicesHeader"))
    //                throw new Exception("On selecting 'Devices' menu, 'Devices' page is not opened.");
    //        }
    //        else


    //            PerformAction("browser", Action.Refresh);
    //        flag = PerformAction("chkInUse", Action.SelectCheckbox);
    //        if (flag)
    //        {


    //            flag = PerformAction("btnSelect", Action.Click);
    //            if (flag)
    //            {


    //                flag = PerformAction("//a[text()='" + userName + "']", Action.Click);
    //                if (flag)
    //                {


    //                    flag = selectPlatform(Platform);
    //                    if (flag || strErrMsg_AppLib.contains("deviceConnect currently has no configured devices or your filter produced no results."))
    //                    {

    //                        if (strErrMsg_AppLib.contains("deviceConnect currently has no configured devices or your filter produced no results."))
    //                        {

    //                            PerformAction("browser", Action.Refresh);
    //                            CloseWindow("MobileLabs.deviceviewer");
    //                            return true;
    //                        }
    //                        else
    //                        {




    //                            flag = SelectDevice("first");
    //                            if (flag)
    //                            {


    //                                flag = PerformAction("btnRelease", Action.Click);
    //                                if (flag)
    //                                {


    //                                    if (PerformAction("eleNotificationRightBottom", Action.WaitForElement, "10"))
    //                                    {

    //                                        flag = GetTextOrValue("eleNotificationRightBottom", "text").contains("Device released");
    //                                        if (!flag)

    //                                            throw new Exception("Device not released as 'Device released' notification not displayed");
    //                                    }
    //                                    else


    //                                        throw new Exception("'Device released' notification not displayed even after waiting for 10 seconds.");
    //                                }
    //                                else


    //                                    throw new Exception("Could not click on 'Release' button.");
    //                            }
    //                            else


    //                                throw new Exception(strErrMsg_AppLib);
    //                        }
    //                    }
    //                    else



    //                        throw new Exception(strErrMsg_AppLib);
    //                }
    //                else


    //                    throw new Exception("Could not select the username from dropdown.");
    //            }
    //            else


    //                throw new Exception("Coud not click on 'Select' button.");
    //        }
    //        else


    //            throw new Exception("Could not select 'In Use' checkbox");

    //    }

    //    catch (Exception e)
    //    {

    //        flag = false;
    //        strErrMsg_AppLib = e.Message;
    //        selectFromMenu("Devices", "eleDevicesHeader");
    //        return flag;
    //    }

    //    flag = ReleaseDevice(userName, Platform);
    //    return flag;
    //}

     
    //Logs in the given user to dC. If no username is specified, it logs in using the defult admin.
    //
    //<!--Last Modified : 9/2/2015 by Mandeep Kaur-->
    //@param strEmailAddress Email Address of user to be logged in.
    //@param strPassword Password of the user.
    //@param GoToDevicesPage Give 'true if user needs to be on Devices page just after login.
    //@return 
    //

    public final boolean LoginToDC(String strEmailAddress, String strPassword)
    {
        return LoginToDC(strEmailAddress, strPassword, true);
    }

    public final boolean LoginToDC(String strEmailAddress)
    {
        return LoginToDC(strEmailAddress, "", true);
    }

    public final boolean LoginToDC()
    {
        return LoginToDC("", "", true);
    }

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public bool LoginToDC(string strEmailAddress = "", string strPassword = "", bool GoToDevicesPage = true)
    public final boolean LoginToDC(String strEmailAddress, String strPassword, boolean GoToDevicesPage)
    {
        strErrMsg_AppLib = "";
        if (strEmailAddress.equals(""))
        {
            strEmailAddress = GenericLibrary.dicCommon.get("EmailAddress");
        }
        if (strPassword.equals(""))
        {
            strPassword = GenericLibrary.dicCommon.get("Password");
        }
        boolean isEventSuccessful = false;
        try
        {
            //Check if browser is not already opened
            if (GenericLibrary.driver == null)
            {
                genericLibrary.LaunchWebDriver();
            }
            //Verify login page is opened.
            if (PerformAction("btnLogin", Action.isDisplayed))
            {
                if (!PerformAction("inpEmailAddress", Action.Type, strEmailAddress))
                {
                    throw new RuntimeException("Could not enter Email Address " + strEmailAddress + " in correponding field.");
                }
                if (!PerformAction("inpPassword", Action.Type, strPassword))
                {
                    throw new RuntimeException("Could not enter Email Address " + strPassword + " in correponding field.");
                }
                if (PerformAction("btnLogin", Action.Click))
                {
                    PerformAction("browser", Action.WaitForPageToLoad);
                    isEventSuccessful = PerformAction("btnMenu", Action.WaitForElement);
                    if (isEventSuccessful)
                    {
                        if (GoToDevicesPage)
                        {
                            if (!PerformAction("eleDevicesTab_Devices", Action.isDisplayed)) // Checking if the 'Devices side tab is displayed to amke sure that the user is on Devices page.
                            {
                                //isEventSuccessful = selectFromMenu("Devices", dicOR["eleDevicesHeader"]);
                                isEventSuccessful = navigateToNavBarPages("Devices", "eleDevicesTab_Devices");
                                if (!isEventSuccessful)
                                {
                                    throw new RuntimeException("Could not click on 'Devices' on top nav bar.");
                                }
                            }
                        }
                    }
                    else
                    {
                        throw new RuntimeException("User " + strEmailAddress + " is not logged in on clicking on 'Login' button.");
                    }
                }
                else
                {
                    throw new RuntimeException("Could not click on 'Login' button.");
                }

            }
            else
            {
                throw new RuntimeException("Login page is not opened.");
            }

        }
        catch (RuntimeException e)
        {
            strErrMsg_AppLib = "LoginToDC--" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
            isEventSuccessful = false;
        }
        return isEventSuccessful;
    }

     
    //It opens the device details page of the either first displayed device or the first device that matches the verison or deviceName supplied.
    //<p>It also puts the name of selected device in 'dicOutput["selectedDeviceName"]'</p>
    //
    //<!--Modified By : Mandeep Mann-->
    //@param strDeviceOption It takes three values : 'first', 'version'or 'name'. If user wants to select a device with specific version, he can provide 'version' in this parameter.
    //@param strActionOrValue <p>If strDeviceOption = "first" then it takes values = '1'</p>
    //<p>If strDeviceOption = "version", then its value should be the full OS version, eg. "Android 3.2"</p>
    //<p>If strDeviceOption = "devicename", then its value should be the full name of device as displayed on devices page IN CORRECT CASE. </p>
    //
    //@param strView It is the devices index view open at the moment, i.e. grid or list.
    //@return True or False
    //

    public final boolean SelectDevice(String strDeviceOption, String strValue)
    {
        return SelectDevice(strDeviceOption, strValue, "list");
    }

    public final boolean SelectDevice(String strDeviceOption)
    {
        return SelectDevice(strDeviceOption, "1", "list");
    }

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public bool SelectDevice(string strDeviceOption, string strValue = "1", string strView = "grid")
    public final boolean SelectDevice(String strDeviceOption, String strValue, String strView)
    {
        boolean isEventSuccessful = false;
        String strDeviceName = "", xpathDevicesHolder = "", deviceNameLink = "";
        strErrMsg_AppLib = "";
        //WebElement element = null, childElement= null;
        try
        {
            if (PerformAction("eleDevicesTab_Devices", Action.WaitForElement))
            {
                //Verifying some devices are displayed for the applied filter
                if (GetTextOrValue("class=message", "text").contains("deviceConnect currently has no configured devices or your filter produced no results."))
                {
                    throw new RuntimeException("deviceConnect currently has no configured devices or your filter produced no results.");
                }

                //Get xpath for devices in card/list page
                if (strView.toLowerCase().equals("list"))
                {
                    xpathDevicesHolder = dicOR.get("eleDevicesHolderListView");
                }
                else
                {
                    xpathDevicesHolder = dicOR.get("eleDevicesHolderGridView");
                }

                // cases for opening device details page of a device with given specification i.e. strDeviceOption
                switch (strDeviceOption.toLowerCase())
                {
                    case "first":
                        //First put element xpath from OR in variable , according to sView and browser, element property is taken differently in case of IE
                        if (strView.toLowerCase().equals("list"))
                        {
                            deviceNameLink = dicOR.get("eleDeviceName_ListView").replace("__INDEX__", "1");
                        }
                        else if (dicCommon.get("BrowserName").toLowerCase().equals("ie"))
                        {
                            deviceNameLink = "css=.card-detail-link";
                        }
                        else
                        {
                            deviceNameLink = dicOR.get("eleDeviceName_CardsView").replace("__INDEX__", "1");
                        }
                        break;

                    case "version":
                        if (strView.toLowerCase().equals("list"))
                        {
                            deviceNameLink = "(" + xpathDevicesHolder + "/td[@title='" + strValue + "'])[1]/../td[2]/a";
                        }
                        else
                        {
                            deviceNameLink = "(" + xpathDevicesHolder + "/div/p[@title='" + strValue + "'])[1]/../div/a";
                        }
                        break;

                    case "devicename":
                        if (strView.toLowerCase().equals("list"))
                        {
                            deviceNameLink = "//td[@title='" + strValue + "']/a";
                        }
                        else
                        {
                            deviceNameLink = "//a[@class='card-detail-link' and text()='" + strValue + "']";
                        }
                        break;
                }

                //Get device name and put it to dicOutput
                strDeviceName = GetTextOrValue(deviceNameLink, "text");
                try
                {
                    if (dicOutput.containsKey("selectedDeviceName"))
                    {
                        dicOutput.remove("selectedDeviceName");
                    }
                    dicOutput.put("selectedDeviceName", strDeviceName);
                }
                catch (RuntimeException e)
                {
                    writeToLog("SelectDevice -- Unable to put devicename to dicOutput dictionary." + e.getStackTrace());
                }

                //Click on device name and verify correct device details page is opened.
                if ( ! strDeviceName.equals(""))
                {
                    isEventSuccessful = PerformAction(deviceNameLink, Action.Click);
                    if (isEventSuccessful)
                    {
                        isEventSuccessful = PerformAction("eleDeviceNameinDeviceDetailsHeader", Action.WaitForElement);
                        if (isEventSuccessful)
                        {
                            isEventSuccessful = GetTextOrValue("eleDeviceNameinDeviceDetailsHeader", "text").equals(strDeviceName);
                            
                            if (!isEventSuccessful)
                            {
                                throw new RuntimeException("Correct Device details page is not opened for device: " + strDeviceName + " in " + strView + " view.");
                            }
                        }
                        else
                        {
                            throw new RuntimeException("Device details page not opened after clicking on device name link for device : " + strDeviceName + " in " + strView + " view.");
                        }
                    }
                    else
                    {
                        throw new RuntimeException("Could not click  on device name link (cards view on devices page) for device : " + strDeviceName + " in " + strView + " view.");
                    }
                }
                else
                {
                    throw new RuntimeException("Could not find any device with the given parameters.");
                }
            }
            else
            {
                throw new RuntimeException("Devices page is not displayed");
            }
        }
        catch (RuntimeException e)
        {
            isEventSuccessful = false;
            strErrMsg_AppLib = "SelectDevice---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return isEventSuccessful;
    }

     
    //Clicks on Connect button (on devices card/list view) for the first device displayed OR first device that matches the given option.
    //<p>It also verifies if 'Launch Application' dialog is opened."</para>
    //
    //@param strDeviceOption It takes values : "first", "version" and "deviceName".
    //@param strValue It takes value "1" for 'first', exact value for 'version', or exact name in correct case for 'deviceName' 
    //@param strView Devices page view open at the moment. i.e. 'grid' or 'list'
    //@return True or false
    //

    public final boolean OpenLaunchAppDialog(String strDeviceOption, String strValue)
    {
        return OpenLaunchAppDialog(strDeviceOption, strValue, "grid");
    }

    public final boolean OpenLaunchAppDialog(String strDeviceOption)
    {
        return OpenLaunchAppDialog(strDeviceOption, "", "list");
    }

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public bool OpenLaunchAppDialog(string strDeviceOption, string strValue = "", string strView = "grid")
    public final boolean OpenLaunchAppDialog(String strDeviceOption, String strValue, String strView)
    {
        boolean isEventSuccessful = false;
        String xpathDevicesHolder = "", connectBtn = "";
        strErrMsg_AppLib = "";
        try
        {
            if (PerformAction("eleDevicesTab_Devices", Action.WaitForElement))
            {
                //Verifying some devices are displayed for the applied filter
                if (GetTextOrValue("class=message", "text").contains("deviceConnect currently has no configured devices or your filter produced no results."))
                {
                    throw new RuntimeException("deviceConnect currently has no configured devices or your filter produced no results.");
                }

                //Get xpath for devices in card/list page
                if (strView.toLowerCase().equals("list"))
                {
                    xpathDevicesHolder = dicOR.get("eleDevicesHolderListView");
                }
                else
                {
                    xpathDevicesHolder = dicOR.get("eleDevicesHolderGridView");
                }

                // cases for opening device details page of a device with given specification i.e. strDeviceOption
                switch (strDeviceOption.toLowerCase())
                {
                    case "first":
                        //First put element xpath from OR in variable , according to sView and browser, element property is taken differently in case of IE
                        if (strView.toLowerCase().equals("list"))
                        {
                            connectBtn = dicOR.get("btnConnect_ListView") + "[1]";
                        }
                        //else if (dicCommon["BrowserName"].ToLower().Equals("ie"))
                        //    connectBtn = "css=.card-detail-link";
                        else
                        {
                            connectBtn = dicOR.get("btnConnectGridView").replace("__INDEX__", "1");
                        }
                        break;

                    case "version":
                        if (strView.toLowerCase().equals("list"))
                        {
                            connectBtn = "(//td[@title='" + strValue + "'])[1]/following-sibling::td[2]/div/button[1]";
                        }
                        else
                        {
                            connectBtn = "//p[@title='" + strValue + "']/following-sibling::div/div/button[1]";
                        }
                        break;

                    case "devicename":
                        if (strView.toLowerCase().equals("list"))
                        {
                            connectBtn = "//td[@title='" + strValue + "']/a/../following-sibling::td[5]/div/button[1]";
                        }
                        else
                        {
                            connectBtn = "//a[@class='card-detail-link' and text()='" + strValue + "']/../following-sibling::div/div/button[1]";
                        }
                        break;
                }

                //Click on the connect button and check if launch application dialog is opened
                isEventSuccessful = PerformAction(connectBtn, Action.WaitForElement, "2");
                if (isEventSuccessful)
                {
                    isEventSuccessful = PerformAction(connectBtn, Action.Click);
                    if (isEventSuccessful)
                    {
                        isEventSuccessful = PerformAction("appConnectList", Action.WaitForElement, "10");
                        if (!isEventSuccessful)
                        {
                            throw new RuntimeException("App list not displayed after clicking on 'Connect' button for device with options: " + strDeviceOption + " " + strValue + ".");
                        }
                    }
                    else
                    {
                        throw new RuntimeException("Unable to click on 'Connect' button for device with options: " + strDeviceOption + " " + strValue + ".");
                    }
                }
                else
                {
                    throw new RuntimeException("Unable to find 'Connect' button for device with options: " + strDeviceOption + " " + strValue + ".");
                }
            }
            else
            {
                throw new RuntimeException("Devices page is not displayed");
            }
        }
        catch (RuntimeException e)
        {
            isEventSuccessful = false;
            strErrMsg_AppLib = "OpenLaunchAppDialog---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return isEventSuccessful;
    }

     
    //This function returns true if the text on details page matches the Value passed as parameter. Also, it returns true if actual state of Item(Button) matches the 'Enabled' parameter value.
    //<p><I>Parameter "Enabled" takes two values : "true" or "false"  , Value = null if only existence of label is to be verified.</I></p> 
    //<p>Pre-Requisite : Need to click on Show Details Link before calling the function</p>
    //
    //<!--Modified By : Vinita Mahajan-->
    //@param Item In case some text value is to be verified, Item is name displayed on the details page eg. Serial Number
    //Otherwise, it is the object(Button) whose state is to be verified to be Enabled or Disabled 
    //ItemTextOnPage : Battert Status
    //@param Value In case of text verification, it is the text to be verified against the Item, otherwise its value should be "" .
    //@param Enabled In case of verifying state of button, "true" or "false" should be passed, otherwise, it can be left.
    //@return true or false
    //

    public final boolean VerifyOnDeviceDetailsPage(String ItemTextOnPage)
    {
        return VerifyOnDeviceDetailsPage(ItemTextOnPage, "");
    }

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public bool VerifyOnDeviceDetailsPage(String ItemTextOnPage, String Value = "")
    public final boolean VerifyOnDeviceDetailsPage(String ItemTextOnPage, String Value)
    {
        strErrMsg_AppLib = "";
        boolean flag = false;
        String SendItemParentIdentification = dicOR.get("elelabel_anypage").replace("__VALUE__", ItemTextOnPage);
        String actualValue = "";
        String ValueExpected = "";

        //Verifying devices are displayed
        try
        {
            if (GetTextOrValue("class=message", "text").contains("deviceConnect currently has no configured devices or your filter produced no results."))
            {
                strErrMsg_AppLib = "VerifyOnDeviceDetailsPage---" + "deviceConnect currently has no configured devices or your filter produced no results.";
                return false;
            }
            switch (ItemTextOnPage)
            {
                case "Name":
                case "Model Name":
                case "Platform":
                    if (!(Value.isEmpty())) //  to verify that the value in front of label is same as which is passed in th function
                    {
                        if (ItemTextOnPage.equals("Name"))
                        {
                            actualValue = GetTextOrValue(dicOR.get("deviceName_detailsPage"), "text");
                            
                        }
                        else
                        {
                            actualValue = GetTextOrValue(dicOR.get("eleDeviceModel"), "text");
                        }
                    }
                    else
                    {
                        flag = !GetTextOrValue("deviceName_detailsPage", "text").equals("");
                    }
                    break;

                case "Slot #":
                case "Idle timeout":
                    SendItemParentIdentification = "(" + SendItemParentIdentification + ")[1]";
                    flag = PerformAction(SendItemParentIdentification, Action.isDisplayed);
                    if (flag)
                    {
                        String send = "(" + SendItemParentIdentification + "//..//span)[3]";
                        flag = PerformAction(send, Action.Exist);
                        if (flag)
                        {
                            if (!(Value.isEmpty())) //  to verify that the value in front of label is same as which is passed in th function
                            {
                                actualValue = GetTextOrValue("(" + SendItemParentIdentification + "//..//span)[3]", "text");
                            }
                            else
                            {
                                flag = !GetTextOrValue("" + SendItemParentIdentification + "/following::span", "text").equals("");
                            }
                        }
                        else
                        {
                            strErrMsg_AppLib = "Object Label : " + ItemTextOnPage + "'s Value is not displayed.";
                        }
                    }
                    else
                    {
                        strErrMsg_AppLib = "Object Label : " + ItemTextOnPage + " is not displayed.";
                    }
                    break;

                case "Vendor Name":
                case "Status":
                case "Offline Since":
                case "In Use Since":
                case "Online Since":
                case "Model #":
                case "Serial Number":
                case "Disk Usage":
                case "Next Reservation":
                    SendItemParentIdentification = "//label[text()='" + ItemTextOnPage + "']";
                    flag = PerformAction(SendItemParentIdentification, Action.isDisplayed);
                    if (flag)
                    {
                        flag = PerformAction("" + SendItemParentIdentification + "//..//span", Action.isDisplayed);
                        if (flag)
                        {
                            if (!Value.isEmpty()) //  to verify that the value in front of label is same as which is passed in th function
                            {
                                actualValue = GetTextOrValue("" + SendItemParentIdentification + "/following-sibling::span", "text");
                            }
                            else
                            {
                                flag = !GetTextOrValue("" + SendItemParentIdentification + "/following-sibling::span", "text").equals("");
                            }
                        }
                        else
                        {
                            strErrMsg_AppLib = "Object Label : " + ItemTextOnPage + "'s Value is not displayed.";
                        }
                    }
                    else
                    {
                        strErrMsg_AppLib = "Object Label : " + ItemTextOnPage + " is not displayed.";
                    }
                    break;

                case "Battery Status":
                    if (!(Value.isEmpty()))
                    {
                       //TODO OWN: to be implemented - implement GetBatteryStatus() first
                        /*Object StatusValues = GetBatteryStatus();
                        if (StatusValues.Item1 == true && !(StatusValues.Item2))
                        {
                            actualValue = StatusValues.Item2;
                            if (dicOutput.containsKey("BatteryStatusText"))
                            {
                                dicOutput.remove("BatteryStatusText");
                            }
                            dicOutput.put("BatteryStatusText", StatusValues.Item2);
                        }
                        else
                        {
                            strErrMsg_AppLib = strErrMsg_AppLib + "Object Value : " + ItemTextOnPage + "'s Value is not displayed.";
                        }*/
                    }
                    else
                    {
                    	//TODO OWN: to be implemented - implement GetBatteryStatus() first
                        /*Object StatusValues = GetBatteryStatus();
                        if (StatusValues.Item1 == false || (StatusValues.Item2).isEmpty())
                        {
                            strErrMsg_AppLib = strErrMsg_AppLib + "Object Value : " + ItemTextOnPage + "'s Value is not displayed.";
                        }
                        if (dicOutput.containsKey("BatteryStatusText"))
                        {
                            dicOutput.remove("BatteryStatusText");
                        }
                        dicOutput.put("BatteryStatusText", (StatusValues.Item2).isEmpty());
                        return StatusValues.Item1;*/
                    }
                    break;
            }
            //----------------------------------------------------------------------------------------------------------//

            // COMPARING ACTUAL VALUE TO THE EXPECTED VALUE
            if (Value.contains("||"))
            {
                if (Value.contains(actualValue))
                {
                    flag = true;
                }
                else
                {
                    strErrMsg_AppLib = "Actual value of " + ItemTextOnPage + " on device details page is: '" + actualValue + "' and not '" + Value + "'.";
                    flag = false;
                }
            }
            if (Value.startsWith("CONTAINS__"))
            {
                ValueExpected = Value.split("CONTAINS__")[1];
                if (actualValue.contains(ValueExpected))
                {
                    flag = true;
                }
                else
                {
                    strErrMsg_AppLib = "Actual value of " + ItemTextOnPage + " on device details page is: '" + actualValue + "' and not '" + ValueExpected + "'.";
                    flag = false;
                }
            }
            else
            {
                if (actualValue.equals(Value))
                {
                    flag = true;
                }
                else
                {
                    strErrMsg_AppLib = "Actual value of " + ItemTextOnPage + " on device details page is: '" + actualValue + "' and not '" + Value + "'.";
                    flag = false;
                }
            }
            if (!flag)
            {
                strErrMsg_AppLib = "Value for '" + ItemTextOnPage + "' is empty.";
            }
            //----------------------------------------------------------------------------------------------------------//


            //if (Value == "")  // to verify that the value in front of the label is not blank on details page.
            //{
            //    if (ItemTextOnPage == ("Name"))
            //    {
            //        flag = !GetTextOrValue("deviceName_detailsPage", "text").Equals("");
            //    }
            //    else
            //    {
            //        flag = !GetTextOrValue("" + SendItemParentIdentification + "/following::span", "text").Equals("");
            //    }
        }
        catch (RuntimeException e)
        {
            strErrMsg_AppLib = "VerifyOnDeviceDetailsPage---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
            flag = false;
        }
        return flag;
    }

     
    //Returns the detail of device at given index in the given view(grid/list) as a string.
    //<p>*Note : Index is ignored in case we need to find devices count.</p>
    //
    //<!--Modified By : Vinita Mahajan-->
    //@param index index of the device for which detail is to be captured.
    //@param DetailName Detail which is to be found out. 
    //<p>Permitted values for DetailName - devicescount, devicename, devicereservation, devicemodel, deviceplatform, devicestatus</p>
    //@param sView View for which the detail is to be found out : take values - "list" or "grid".
    //@param status This parameter is only for finding the devices count of devices with a specific status like offline, disabled, available, In Use, pass these for status but not reserved.
    //@return It returns the required detail of device(s) in the form of string.
    //

    public final String GetDeviceDetailInGridAndListView(int index, String DetailToBeFound, String sView)
    {
        return GetDeviceDetailInGridAndListView(index, DetailToBeFound, sView, "all");
    }

    public final String GetDeviceDetailInGridAndListView(int index, String DetailToBeFound)
    {
        return GetDeviceDetailInGridAndListView(index, DetailToBeFound, "grid", "all");
    }

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public string GetDeviceDetailInGridAndListView(int index, string DetailToBeFound, string sView = "grid", string status = "all")
    public final String GetDeviceDetailInGridAndListView(int index, String DetailToBeFound, String sView, String status)
    {
        String DetailValue = "";
        strErrMsg_AppLib = "";
        WebElement element;
        String xpathDevicesHolder;
        boolean flag = false;

        if (sView.toLowerCase().equals("list"))
        {
            xpathDevicesHolder = dicOR.get("eleDevicesHolderListView");
        }
        else
        {
            xpathDevicesHolder = dicOR.get("eleDevicesHolderGridView");
        }

        //////// to find number of devices with a specific status in any view, list or grid
        if (DetailToBeFound.toLowerCase().contains("devicescount"))
        {
            int devicesCountTotal = 0;
            int devicesCount = 0;
            try
            {
                if (PerformAction("eleDevicesHeader", Action.WaitForElement))
                {
                    if (GetTextOrValue("class=message", "text").contains("deviceConnect currently has no configured devices or your filter produced no results."))
                    {
                        throw new RuntimeException("deviceConnect currently has no configured devices or your filter produced no results.");
                    }
                    StopAutoRefresh(sView);

                    //**Finding number of devices in grid or list view
                    devicesCountTotal = getelementCount(xpathDevicesHolder); //GenericLibrary.driver.FindElements(By.XPath(xpathDevicesHolder)).Count;
                    if (status.equals("all"))
                    {
                        return (new Integer(devicesCountTotal)).toString();
                    }
                    else
                    {
                        ////flag = SelectFromFilterDropdowns("status", status, "devices", sView); //### check if it itself handles the view selected
                        //devicesCount = getelementCount(xpathDevicesHolder);
                        //return devicesCount.ToString();
                        if (SelectFromFilterDropdowns("status", status, "devices", sView))
                        {
                            for (int i = 1; i <= devicesCountTotal; i++)
                            {
                                if (sView.toLowerCase().equals("grid"))
                                {
                                    if (GetTextOrValue(dicOR.get("eleDeviceStatus_CardView").replace("__INDEX__", (new Integer(i)).toString()), "text").toLowerCase().startsWith(status.toLowerCase()))
                                    {
                                        devicesCount++;
                                    }
                                }
                                else if (sView.toLowerCase().equals("list"))
                                {
                                    //if (SelectFromFilterDropdowns("status", status, "devices", sView))
                                    //{
                                    if (GetTextOrValue(dicOR.get("eleDeviceStatus_ListView").replace("__INDEX__", (new Integer(i)).toString()), "text").toLowerCase().startsWith(status.toLowerCase()))
                                    {
                                        devicesCount++;
                                    }
                                    //}
                                    //else
                                    //    throw new Exception("Status not selected.");
                                }
                            }
                        }
                        else
                        {
                            throw new RuntimeException("Status not selected.");
                        }
                        return (new Integer(devicesCount)).toString();
                    }
                }
                else
                {
                    throw new RuntimeException("Filters not selected");
                }
            }
            catch (RuntimeException e)
            {
                strErrMsg_AppLib = "GetDeviceDetailInGridAndListView---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
                return "0";
            }
        }

        // cases in which we need to find details of devices other than devicescount.
        else
        {
            try
            {
                if (PerformAction("eleDevicesHeader", Action.WaitForElement))
                {
                    if (GetTextOrValue("class=message", "text").contains("deviceConnect currently has no configured devices or your filter produced no results."))
                    {
                        throw new RuntimeException("No devices are displayed with status - 'In Use'");
                    }
                    StopAutoRefresh(sView);
                    element = genericLibrary.GetElement(xpathDevicesHolder + "[" + index + "]");
                    if (element != null)
                    {
                        switch (DetailToBeFound.toLowerCase())
                        {
                            case "devicename":
                                if (sView.toLowerCase().equals("list"))
                                {
                                    if (PerformAction(dicOR.get("eleDeviceName_ListView").replace("__INDEX__", (new Integer(index)).toString()), Action.Exist))
                                    {
                                        DetailValue = GetTextOrValue(dicOR.get("eleDeviceName_ListView").replace("__INDEX__", (new Integer(index)).toString()), "text");
                                    }
                                    else
                                    {
                                        strErrMsg_AppLib = DetailToBeFound + " is not displayed on devices page";
                                    }
                                }
                                else
                                {
                                    if (PerformAction(dicOR.get("eleDeviceName_CardsView").replace("__INDEX__", (new Integer(index)).toString()), Action.Exist))
                                    {
                                        DetailValue = GetTextOrValue(dicOR.get("eleDeviceName_CardsView").replace("__INDEX__", (new Integer(index)).toString()), "text");
                                    }
                                    else
                                    {
                                        strErrMsg_AppLib = DetailToBeFound + " is not displayed on devices page";
                                    }
                                }
                                break;
                            case "devicereservation":
                                if (sView.toLowerCase().equals("list"))
                                {
                                    if (PerformAction(dicOR.get("eleDeviceReservation_ListView").replace("__INDEX__", (new Integer(index)).toString()), Action.Exist))
                                    {
                                        DetailValue = GetTextOrValue(dicOR.get("eleDeviceReservation_ListView").replace("__INDEX__", (new Integer(index)).toString()), "text");
                                    }
                                    else
                                    {
                                        strErrMsg_AppLib = DetailToBeFound + " is not displayed on devices page";
                                    }
                                }
                                else
                                {
                                    if (PerformAction(dicOR.get("eleDeviceReservation_CardView").replace("__INDEX__", (new Integer(index)).toString()), Action.Exist))
                                    {
                                        DetailValue = GetTextOrValue(dicOR.get("eleDeviceReservation_CardView").replace("__INDEX__", (new Integer(index)).toString()), "text");
                                    }
                                    else
                                    {
                                        strErrMsg_AppLib = DetailToBeFound + " is not displayed on devices page";
                                    }
                                }
                                break;
                            case "devicemodel":
                                if (sView.toLowerCase().equals("list"))
                                {
                                    if (PerformAction(dicOR.get("eleDeviceModel_ListView").replace("__INDEX__", (new Integer(index)).toString()), Action.Exist))
                                    {
                                        DetailValue = GetTextOrValue(dicOR.get("eleDeviceModel_ListView").replace("__INDEX__", (new Integer(index)).toString()), "text");
                                    }
                                    else
                                    {
                                        strErrMsg_AppLib = DetailToBeFound + " is not displayed on devices page";
                                    }
                                }
                                else
                                {
                                    if (PerformAction(dicOR.get("eleDeviceModel_CardView").replace("__INDEX__", (new Integer(index)).toString()), Action.Exist))
                                    {
                                        DetailValue = GetTextOrValue(dicOR.get("eleDeviceModel_CardView").replace("__INDEX__", (new Integer(index)).toString()), "text");
                                    }
                                    else
                                    {
                                        strErrMsg_AppLib = DetailToBeFound + " is not displayed on devices page";
                                    }
                                }
                                break;
                            case "deviceplatform":
                                if (sView.toLowerCase().equals("list"))
                                {
                                    if (PerformAction(dicOR.get("eleDevicePlatform_ListView").replace("__INDEX__", (new Integer(index)).toString()), Action.Exist))
                                    {
                                        DetailValue = GetTextOrValue(dicOR.get("eleDevicePlatform_ListView").replace("__INDEX__", (new Integer(index)).toString()), "text");
                                    }
                                    else
                                    {
                                        strErrMsg_AppLib = DetailToBeFound + " is not displayed on devices page";
                                    }
                                }
                                else
                                {
                                    if (PerformAction(dicOR.get("eleDevicePlatform_CardView").replace("__INDEX__", (new Integer(index)).toString()), Action.Exist))
                                    {
                                        DetailValue = GetTextOrValue(dicOR.get("eleDevicePlatform_CardView").replace("__INDEX__", (new Integer(index)).toString()), "text");
                                    }
                                    else
                                    {
                                        strErrMsg_AppLib = DetailToBeFound + " is not displayed on devices page";
                                    }
                                }
                                break;
                            case "devicestatus":
                                if (sView.toLowerCase().equals("list"))
                                {
                                    if (PerformAction(dicOR.get("eleDeviceStatus_ListView").replace("__INDEX__", (new Integer(index)).toString()), Action.Exist))
                                    {
                                        DetailValue = GetTextOrValue(dicOR.get("eleDeviceStatus_ListView").replace("__INDEX__", (new Integer(index)).toString()), "text");
                                    }
                                    else
                                    {
                                        strErrMsg_AppLib = DetailToBeFound + " is not displayed on devices page";
                                    }
                                }
                                else
                                {
                                    if (PerformAction(dicOR.get("eleDeviceStatus_CardView").replace("__INDEX__", (new Integer(index)).toString()), Action.Exist))
                                    {
                                        DetailValue = GetTextOrValue(dicOR.get("eleDeviceStatus_CardView").replace("__INDEX__", (new Integer(index)).toString()), "text");
                                    }
                                    else
                                    {
                                        strErrMsg_AppLib = DetailToBeFound + " is not displayed on devices page";
                                    }
                                }
                                break;
                        }
                    }
                    else
                    {
                        throw new RuntimeException("Element " + xpathDevicesHolder + "[" + index + "] is not found in page.");
                    }
                }
                else
                {
                    throw new RuntimeException("Devices page is not displayed");
                }
            }
            catch (RuntimeException e)
            {
                strErrMsg_AppLib = "GetDeviceDetailInGridAndListView---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
            }
            return DetailValue;
        }
    }

    // Old implementation
    //public string GetDeviceDetailInGridAndListView(int index, string DetailName, string sView = "grid", string status = "all")
    //{
    //    string DetailValue = "";
    //    strErrMsg_AppLib = "";
    //    WebElement element;
    //    string xpathDevicesHolder;


    //    if (sView.ToLower() == "list")
    //        xpathDevicesHolder = "//table[contains(@class,'table')]//tbody/tr";

    //    else
    //        xpathDevicesHolder = "//ul[@class='cards-layout']/li";


    //    //////// to find number of devices with a specific status in any view, list or grid
    //    if (DetailName.ToLower().contains("devicescount"))
    //    {
    //        int devicesCountTotal = 0;
    //        int devicesCount = 0;
    //        try
    //        {
    //            if (PerformAction("eleDevicesHeader", Action.WaitForElement))
    //            {
    //                if (GetTextOrValue("class=message", "text").contains("deviceConnect currently has no configured devices or your filter produced no results."))
    //                    throw new Exception("deviceConnect currently has no configured devices or your filter produced no results.");
    //                StopAutoRefresh(sView);

    //                //**Finding number of devices in grid or list view
    //                //if (sView.ToLower().Equals("grid"))
    //                    devicesCountTotal = GenericLibrary.driver.FindElements(By.XPath(xpathDevicesHolder)).Count;
    //                //else if (sView.ToLower().Equals("list"))
    //                //    devicesCountTotal = GenericLibrary.driver.FindElements(By.XPath(xpathDevicesHolder)).Count - 1;

    //                if (!status.Equals("all"))


    //                {
    //                    for (int i = 1; i <= devicesCountTotal; i++)
    //                    {
    //                        if (sView.ToLower().Equals("grid"))
    //                        {
    //                            if ((GetTextOrValue(xpathDevicesHolder + "[" + i + "]//div[@class='location spec']", "text")).ToLower().Equals(status.ToLower()))
    //                                devicesCount++;
    //                        }
    //                        else if (sView.ToLower().Equals("list"))
    //                        {
    //                            if ((GetTextOrValue(xpathDevicesHolder + "[" + i + "]/td[1]", "text")).ToLower().Equals(status))
    //                                devicesCount++;
    //                        }
    //                    }


    //                    return devicesCount.ToString();
    //                }
    //                else
    //                    return devicesCountTotal.ToString();



















    //            }
    //            else
    //                throw new Exception("Devices page is not displayed");
    //        }
    //        catch (Exception e)
    //        {
    //            strErrMsg_AppLib = e.Message;
    //            return "0";
    //        }

    //    }


    //    else
    //    {
    //        try
    //        {
    //            if (PerformAction("eleDevicesHeader", Action.WaitForElement))
    //            {
    //                if (GetTextOrValue("class=message", "text").contains("deviceConnect currently has no configured devices or your filter produced no results."))
    //                    throw new Exception("No devices are displayed with status - 'In Use'");
    //                StopAutoRefresh(sView);
    //                element = genericLibrary.GetElement(xpathDevicesHolder + "[" + index + "]");
    //                if (element != null)
    //                {
    //                    switch (DetailName.ToLower())
    //                    {
    //                        case "devicename":
    //                            if (sView.ToLower() == "list")
    //                            {
    //                                if (PerformAction(xpathDevicesHolder + "[" + index + "]/td[2]/a", Action.Exist))
    //                                    //ul[@class='cards-layout']/li[1]/div/div/a
    //                                    DetailValue = GetTextOrValue(xpathDevicesHolder + "[" + index + "]/td[2]/a", "text");


    //                                else
    //                                    strErrMsg_AppLib = DetailName + " is not displayed on devices page";
    //                            }
    //                            else
    //                            {
    //                                if (PerformAction(xpathDevicesHolder + "[" + index + "]/div/div", Action.Exist))
    //                                    DetailValue = GetTextOrValue(xpathDevicesHolder + "[" + index + "]/div/div/a", "text");


    //                                else
    //                                    strErrMsg_AppLib = DetailName + " is not displayed on devices page";
    //                            }
    //                            break;
    //                        case "devicemodel":
    //                            if (sView.ToLower() == "list")
    //                            {
    //                                if (PerformAction(xpathDevicesHolder + "[" + index + "]/td[3]", Action.Exist))
    //                                    DetailValue = GetTextOrValue(xpathDevicesHolder + "[" + index + "]/td[3]", "text");


    //                                else
    //                                    strErrMsg_AppLib = DetailName + " is not displayed on devices page";



    //                            }
    //                            else
    //                            {
    //                                if (PerformAction(xpathDevicesHolder + "[" + index + "]//div[@class='hardware spec']", Action.Exist))
    //                                    DetailValue = GetTextOrValue(xpathDevicesHolder + "[" + index + "]//div[@class='hardware spec']", "text");


    //                                else
    //                                    strErrMsg_AppLib = DetailName + " is not displayed on devices page";
    //                            }
    //                            break;
    //                        case "deviceplatform":
    //                            if (sView.ToLower() == "list")
    //                            {
    //                                if (PerformAction(xpathDevicesHolder + "[" + index + "]/td[4]", Action.Exist))
    //                                    DetailValue = GetTextOrValue(xpathDevicesHolder + "[" + index + "]/td[4]", "text");


    //                                else
    //                                    strErrMsg_AppLib = DetailName + " is not displayed on devices page";
    //                            }
    //                            else
    //                            {
    //                                if (PerformAction(xpathDevicesHolder + "[" + index + "]//div[@class='platform spec']", Action.Exist))
    //                                    DetailValue = GetTextOrValue(xpathDevicesHolder + "[" + index + "]//div[@class='platform spec']", "text");


    //                                else
    //                                    strErrMsg_AppLib = DetailName + " is not displayed on devices page";
    //                            }
    //                            break;
    //                        case "devicestatus":
    //                            if (sView.ToLower() == "list")
    //                            {
    //                                if (PerformAction(xpathDevicesHolder + "[" + index + "]/td[1]", Action.Exist))
    //                                    DetailValue = GetTextOrValue(xpathDevicesHolder + "[" + index + "]/td[1]", "text");


    //                                else
    //                                    strErrMsg_AppLib = DetailName + " is not displayed on devices page";
    //                            }
    //                            else
    //                            {
    //                                if (PerformAction(xpathDevicesHolder + "[" + index + "]//div[@class='location spec']", Action.Exist))
    //                                    DetailValue = GetTextOrValue(xpathDevicesHolder + "[" + index + "]//div[@class='location spec']", "text");


    //                                else
    //                                    strErrMsg_AppLib = DetailName + " is not displayed on devices page";
    //                            }
    //                            break;
    //                    }
    //                }
    //                else
    //                    throw new Exception("Element " + xpathDevicesHolder + "[" + index + "] is not found in page.");
    //            }
    //            else
    //                throw new Exception("Devices page is not displayed");
    //        }
    //        catch (Exception e)
    //        {
    //            strErrMsg_AppLib = e.Message;
    //        }
    //        return DetailValue;
    //    }
    //}

	 
    //Verifies on devices page that the sVerificationObjectName has the same value/text as sVerificationObjectValue in grid/list view
    //
    //<!--Modified By : Vinita Mahajan-->
    //<!--Last updated : 10/2/2015 by Mandeep Kaur , Modifying Connect case, changed loop scope to i<noOfDevices because table header is also getting in the fetched list-->
    //@param sVerificationObjectName It takes values: connect, statusicon, devicename, devicemodel, deviceplatform, devicestatus, existence of status
    //<p>***NOTE : Use "devicestatus" instead of "statusicon" when it is required to verify the status of the device in text</p>
    //<p>Use "statusicon" in case it is required to verify the status icon</p>
    //@param sVerificationObjectValue It takes values : enable, disable(for sVerificationObjectName = connect), "link" (for devicename), any value (for devicemodel), any value (for deviceplatform), Available, Disabled, Offline, In Use (for existenceofstatus)
    //@param sView It is the view for which verification is to be done. It takes values : "grid", "list", "both"
    //@return Returns true or false if the verification passes or fails respectively.
    //

    public final boolean VerifyDeviceDetailsInGridAndListView(String sVerificationObjectName, String sVerificationObjectValue)
    {
        return VerifyDeviceDetailsInGridAndListView(sVerificationObjectName, sVerificationObjectValue, "grid");
    }

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public bool VerifyDeviceDetailsInGridAndListView(string sVerificationObjectName, string sVerificationObjectValue, string sView = "grid")
    public final boolean VerifyDeviceDetailsInGridAndListView(String sVerificationObjectName, String sVerificationObjectValue, String sView)
    {
        //StopAutoRefresh(sView);
        String hrefValue = "";
        strErrMsg_AppLib = "";
        boolean isEventSuccessful = false, loopEntered = false;
        WebElement element, childElement = null;
        String xpathDevicesHolder = "", ElementProperty = "", strDeviceName = "", classname = "", text = "", srcAttribute = "";

        try
        {
            if (PerformAction("eleDevicesTab_Devices", Action.WaitForElement))
            {
                if (sView.toLowerCase().equals("list"))
                {
                    xpathDevicesHolder = dicOR.get("eleDevicesHolderListView");
                }
                else if (sView.toLowerCase().equals("grid"))
                {
                    xpathDevicesHolder = dicOR.get("eleDevicesHolderGridView");
                }

                // verification in case user wants to verify on both grid and list view
                else if (sView.toLowerCase().equals("both"))
                {
                    String strErr = "";
                    //First check for grid view
                    isEventSuccessful = VerifyDeviceDetailsInGridAndListView(sVerificationObjectName, sVerificationObjectValue, "grid");
                    if (!isEventSuccessful)
                    {
                        strErr = strErrMsg_AppLib;
                    }
                    // now open list view and verify for list view
                    isEventSuccessful = PerformAction("lnkListView", Action.Click);
                    if (isEventSuccessful)
                    {
                        isEventSuccessful = VerifyDeviceDetailsInGridAndListView(sVerificationObjectName, sVerificationObjectValue, "list");
                        if (!isEventSuccessful)
                        {
                            strErr = strErr + strErrMsg_AppLib;
                        }
                    }
                    else
                    {
                        strErr = strErrMsg_AppLib + "Could not click on icon for List view.";
                    }
                    if (strErr.equals(""))
                    {
                        return true;
                    }
                    else
                    {
                        strErrMsg_AppLib = strErr;
                        return false;
                    }
                }
                /////////////////## Verification for "both" ends. ##///////////////////////

                ///#################### Verification for Specifically 'Grid' or 'list' view ###################//

                //Verifying devices are displayed
                if (PerformAction("eleNoDevicesWarning_Devices", Action.isDisplayed))
                {
                    throw new RuntimeException("deviceConnect currently has no configured devices or your filter produced no results.");
                }
                //StopAutoRefresh(sView);
                int noOfDevices = getelementCount(xpathDevicesHolder) - 1; // Get number of devices' rows . '-1' because it counts the header also.
                
                for (int i = 1; i <= noOfDevices; i++)
                {
                    loopEntered = true;
                    element = GetElement(xpathDevicesHolder + "[" + i + "]");
                    if (element != null)
                    {
                        if (sView.toLowerCase().equals("list"))
                        {
                            strDeviceName = GetTextOrValue(dicOR.get("eleDeviceName_ListView").replace("__INDEX__", (new Integer(i)).toString()), "text");
                        }
                        else
                        {
                            strDeviceName = GetTextOrValue(dicOR.get("eleDeviceName_CardsView").replace("__INDEX__", (new Integer(i)).toString()), "text");
                        }

                        // Now verify the value of the element required using different cases
                        switch (sVerificationObjectName.toLowerCase())
                        {
                            case "connect": // Verification for checking if Connect button is enabled or disabled

                                //Getting the xpath of connect button for i'th device in list/grid view
                                //if (sView.ToLower() == "list")
                                //    ElementProperty = dicOR["btnConnect_ListView"] + "[" + i + "]";
                                //else
                                //    ElementProperty = dicOR["btnConnectGridView"].Replace("__INDEX__", i.ToString());

                                //childElement = GetElement(ElementProperty);
                                //if (childElement != null && PerformAction(ElementProperty, Action.isDisplayed))
                                //{
                                //classname = getAttribute(ElementProperty, "class");// childElement.GetAttribute("class");
                                // checking button is not disabled when it should be enabled.
                                if (sVerificationObjectValue.toLowerCase().contains("enable") && !(PerformAction(dicOR.get("btnConnectDisabled_ListView") + "[" + i + "]", Action.isNotDisplayed) && PerformAction(dicOR.get("btnConnect_ListView") + "[" + i + "]", Action.isDisplayed))) // If we need to check that the connect button is enabled and disabled connect button is displayed, then the condition is fail for that device
                                {
                                    strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
                                }
                                //checking button is not enabled when it should be disabled.
                                else if (sVerificationObjectValue.toLowerCase().contains("disable") && (!PerformAction(dicOR.get("btnConnectDisabled_ListView") + "[" + i + "]", Action.isDisplayed))) // If we need to check that the connect button is disabled and disabled connect button is not displayed, then the condition is fail for that device
                                {
                                    strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
                                }
                                //}
                                //else
                                //    strErrMsg_AppLib = strErrMsg_AppLib + "; Connect button is not found for device " + i.ToString() + " in devices list view.";
                                break;

                            case "statusicon":
                                if (sView.toLowerCase().equals("list"))
                                {
                                    ElementProperty = dicOR.get("eleStatusIcon_ListView").replace("__INDEX__", (new Integer(i + 1)).toString());
                                    srcAttribute = getAttribute(ElementProperty, "class");
                                }
                                else
                                {
                                    ElementProperty = dicOR.get("eleStatusIcon_CardView").replace("__INDEX__", (new Integer(i + 1)).toString());
                                    srcAttribute = getAttribute(ElementProperty, "class");
                                }
                                if (PerformAction(ElementProperty, Action.isDisplayed))
                                {
                                    if (sVerificationObjectValue.toLowerCase().contains("available") || sVerificationObjectValue.toLowerCase().contains("green"))
                                    {
                                        if (!srcAttribute.contains("dc-icon-device-status-available status-icon"))
                                        {
                                            strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
                                        }
                                    }
                                    else if (sVerificationObjectValue.toLowerCase().contains("offline") || sVerificationObjectValue.toLowerCase().contains("grey"))
                                    {
                                        if (!srcAttribute.contains("dc-icon-device-status-offline status-icon"))
                                        {
                                            strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
                                        }
                                    }
                                    else if (sVerificationObjectValue.toLowerCase().contains("in use") || sVerificationObjectValue.toLowerCase().contains("red"))
                                    {
                                        if (!srcAttribute.contains("dc-icon-device-status-in-use status-icon"))
                                        {
                                            strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
                                        }
                                    }
                                    else if (sVerificationObjectValue.toLowerCase().contains("disabled") || sVerificationObjectValue.toLowerCase().contains("red-cross"))
                                    {
                                        if (!srcAttribute.contains("dc-icon-device-status-disabled status-icon"))
                                        {
                                            strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
                                        }
                                    }
                                }
                                else
                                {
                                    strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", "; // Here it puts name of device if Status icon is not displayed(But this case is not displaye in the HTML report.
                                }
                                break;

                            case "devicename":
                                if (sVerificationObjectValue.toLowerCase().contains("link"))
                                {
                                    if (sView.toLowerCase().equals("list"))
                                    {
                                        hrefValue = getAttribute(dicOR.get("eleDeviceName_ListView").replace("__INDEX__", (new Integer(i)).toString()), "href");
                                        if ( ! hrefValue.equals(""))
                                        {
                                            if (!GetElement(dicOR.get("eleDeviceName_ListView").replace("__INDEX__", (new Integer(i)).toString())).isEnabled())
                                            {
                                                strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
                                            }
                                        }
                                    }
                                    else
                                    {
                                        if (!GetElement(dicOR.get("eleDeviceName_CardsView").replace("__INDEX__", (new Integer(i)).toString())).isEnabled())
                                        {
                                            strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
                                        }
                                    }
                                }
                                break;

                            case "devicemodel":
                                if (sView.toLowerCase().equals("list"))
                                {
                                    if (!PerformAction(dicOR.get("eleDeviceModel_ListView").replace("__INDEX__", (new Integer(i + 1)).toString()), Action.isDisplayed))
                                    {
                                        strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
                                    }
                                }
                                else
                                {
                                    if (!PerformAction(dicOR.get("eleDeviceModel_CardView").replace("__INDEX__", (new Integer(i)).toString()), Action.isDisplayed))
                                    {
                                        strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
                                    }
                                }
                                break;

                            case "deviceplatform":
                                if (sView.toLowerCase().equals("list"))
                                {
                                    childElement = GetElement(ElementProperty = (dicOR.get("eleDevicePlatform_ListView").replace("__INDEX__", (new Integer(i + 1)).toString())));
                                }
                                else
                                {
                                    childElement = GetElement(ElementProperty = (dicOR.get("eleDevicePlatform_CardView").replace("__INDEX__", (new Integer(i)).toString())));
                                }
                                if (i < noOfDevices) // this check is applied because only for this case 'deviceplatform', "__INDEX__" needs to be replaced with (i+1) instead of i. so if this check is not there then error will be thrown.
                                {
                                    if (PerformAction(ElementProperty, Action.isDisplayed))
                                    {
                                        text = GetTextOrValue(ElementProperty, "text");
                                        if ( ! sVerificationObjectValue.equals("") && !text.toLowerCase().contains(sVerificationObjectValue.toLowerCase()))
                                        {
                                            strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
                                        }
                                    }
                                    else
                                    {
                                        strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + "(Not displayed), ";
                                    }
                                }
                                break;

                            case "devicestatus":
                                if (sView.toLowerCase().equals("list"))
                                {
                                    if (!GetElement(dicOR.get("eleDeviceStatus_ListView") + "[" + i + "]").getText().toLowerCase().startsWith(sVerificationObjectValue.toLowerCase()))
                                    {
                                        strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
                                    }
                                }
                                else
                                {
                                    if (!(GetElement(dicOR.get("eleDeviceStatus_CardView").replace("__INDEX__", (new Integer(i)).toString())).getText().toLowerCase().startsWith(sVerificationObjectValue.toLowerCase())))
                                    {
                                        strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
                                    }
                                }
                                break;

                            case "deviceuser":
                                if (sView.toLowerCase().equals("list"))
                                {
                                    if (!GetElement(dicOR.get("eleDeviceStatus_ListView").replace("__INDEX__", (new Integer(i)).toString())).getText().toLowerCase().endsWith("(" + sVerificationObjectValue.toLowerCase() + ")"))
                                    {
                                        strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
                                    }
                                }
                                else
                                {
                                    if (!(GetElement(dicOR.get("eleDeviceStatus_CardView").replace("__INDEX__", (new Integer(i)).toString())).getText().toLowerCase().endsWith("(" + sVerificationObjectValue.toLowerCase() + ")")))
                                    {
                                        strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
                                    }
                                }
                                break;


                            case "devicereservation":
                                if (sView.toLowerCase().equals("list"))
                                {
                                    if (sVerificationObjectValue.toLowerCase().equals("present"))
                                    {
                                        if ((GetElement(dicOR.get("eleDeviceReservation_ListView").replace("__INDEX__", (new Integer(i)).toString())).getText().toLowerCase()).equals("-")) //StartsWith(sVerificationObjectValue.ToLower())))
                                        {
                                            strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
                                        }
                                    }
                                    else
                                    {
                                        if ( ! (GetElement(dicOR.get("eleDeviceReservation_ListView").replace("__INDEX__", (new Integer(i)).toString())).getText().toLowerCase()).equals("-")) //StartsWith(sVerificationObjectValue.ToLower())))
                                        {
                                            strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
                                        }
                                    }
                                }
                                else
                                {
                                    if (sVerificationObjectValue.toLowerCase().equals("present"))
                                    {
                                        if ((GetElement(dicOR.get("eleDeviceReservation_CardView").replace("__INDEX__", (new Integer(i)).toString())).getText().toLowerCase()).equals("-"))
                                        {
                                            strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
                                        }
                                    }
                                    else
                                    {
                                        if ( ! (GetElement(dicOR.get("eleDeviceReservation_CardView").replace("__INDEX__", (new Integer(i)).toString())).getText().toLowerCase()).equals("-"))
                                        {
                                            strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
                                        }
                                    }
                                }
                                break;

                            case "existenceofstatus": // This case checks if the given status's device exists on the UI or not, the moment it finds one, case breaks and returns "True"
                                isEventSuccessful = false;
                                strErrMsg_AppLib = "";
                                if (sView.toLowerCase().equals("list"))
                                {
                                    if (GetTextOrValue(dicOR.get("eleDeviceStatus_ListView").replace("__INDEX__", (new Integer(i)).toString()), "text").toLowerCase().startsWith(sVerificationObjectValue.toLowerCase()))
                                    {
                                        return true;
                                    }
                                    //if (!driver.findElement(By.xpath(xpathDevicesHolder + "[" + i + "]/td[1]")).getText().ToLower().contains(sVerificationObjectValue))
                                    //    return true;
                                }
                                else
                                {
                                    if (GetTextOrValue(dicOR.get("eleDeviceStatus_CardView").replace("__INDEX__", (new Integer(i)).toString()), "text").toLowerCase().startsWith(sVerificationObjectValue.toLowerCase()))
                                    {
                                        return true;
                                    }
                                    //if (!driver.FindElement(By.XPath(xpathDevicesHolder + "[" + i + "]//div[@class='location spec']")).getText().ToLower().contains(sVerificationObjectValue))
                                    //    return true;
                                }
                                if (!isEventSuccessful)
                                {
                                    strErrMsg_AppLib = "'" + sVerificationObjectValue + "' is not present on  the UI.";
                                }
                                break;

                            case "existenceofplatform": // This case checks if the given platform device exists on the UI or not, the moment it finds one, case breaks and returns "True"
                                isEventSuccessful = false;
                                strErrMsg_AppLib = "";
                                if (sView.toLowerCase().equals("list"))
                                {
                                    if (GetTextOrValue(dicOR.get("eleDevicePlatform_ListView").replace("__INDEX__", (new Integer(i)).toString()), "text").toLowerCase().startsWith(sVerificationObjectValue.toLowerCase()))
                                    {
                                        return true;
                                    }
                                    //if (!driver.FindElement(By.XPath(xpathDevicesHolder + "[" + i + "]/td[1]")).getText().ToLower().contains(sVerificationObjectValue))
                                    //    return true;
                                }
                                else
                                {
                                    if (GetTextOrValue(dicOR.get("eleDevicePlatform_CardView").replace("__INDEX__", (new Integer(i)).toString()), "text").toLowerCase().startsWith(sVerificationObjectValue.toLowerCase()))
                                    {
                                        return true;
                                    }
                                    //if (!driver.FindElement(By.XPath(xpathDevicesHolder + "[" + i + "]//div[@class='location spec']")).getText().ToLower().contains(sVerificationObjectValue))
                                    //    return true;
                                }
                                if (!isEventSuccessful)
                                {
                                    strErrMsg_AppLib = "'" + sVerificationObjectValue + "' is not present on  the UI.";
                                }
                                break;
                        }
                    }
                    else
                    {
                        throw new RuntimeException("Element " + xpathDevicesHolder + "[" + i + "] is not found in page.");
                    }
                }
                // Final check if the validatio was pass or not.
                if (loopEntered) // If loop is entered then check if the error message is empty otherwise throw error. If loop is not entered then throw error that no devices match the xpath
                {
                    if (strErrMsg_AppLib.equals(""))
                    {
                        isEventSuccessful = true;
                    }
                    else
                    {
                        throw new RuntimeException("Following devices' " + sVerificationObjectName + " is not displayed or is not correct: <br/>" + sVerificationObjectValue + " - " + strErrMsg_AppLib);
                    }
                }
                else
                {
                    throw new RuntimeException("No devices found matching the xpath : '" + xpathDevicesHolder + "'.");
                }
            }
            else // if devices page is not displayed then throw exception and return with flag set to false.
            {
                isEventSuccessful = false;
                strErrMsg_AppLib = "VerifyDeviceDetailsInGridAndListView--" + "Devices page is not displayed";
                return isEventSuccessful;
            }
        }
        catch (RuntimeException e)
        {
            isEventSuccessful = false;
            strErrMsg_AppLib = "VerifyDeviceDetailsInGridAndListView--" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return isEventSuccessful;
    }

    //Old implementation
    //public bool VerifyDeviceDetailsInGridAndListView(string sVerificationObjectName, string sVerificationObjectValue, string sView = "grid")
    //{
    //        //StopAutoRefresh(sView);
    //        strErrMsg_AppLib = "";
    //        bool isEventSuccessful = false;
    //        WebElement element, childElement = null;
    //        string xpathDevicesHolder = "", ElementProperty = "", strDeviceName = "", classname = "", text = "", srcAttribute= "";
    //        if (PerformAction("eleDevicesHeader", Action.WaitForElement))
    //        {
    //            if (sView.ToLower() == "list")
    //                xpathDevicesHolder = "//table[contains(@class,'table')]//tbody/tr";
    //            else if (sView.ToLower() == "grid")
    //                xpathDevicesHolder = "//ul[@class='cards-layout']/li";
    //            else if (sView.ToLower() == "both")
    //            {
    //                string strErr = "";
    //                isEventSuccessful = VerifyDeviceDetailsInGridAndListView(sVerificationObjectName, sVerificationObjectValue, "grid");
    //                if (!isEventSuccessful)
    //                    strErr = strErrMsg_AppLib;
    //                isEventSuccessful = PerformAction("lnkListView", Action.Click);
    //                if (isEventSuccessful)
    //                {
    //                    isEventSuccessful = VerifyDeviceDetailsInGridAndListView(sVerificationObjectName, sVerificationObjectValue, "list");
    //                    if (!isEventSuccessful)
    //                        strErr = strErr + strErrMsg_AppLib;
    //                }
    //                else
    //                {
    //                    strErr = strErrMsg_AppLib + "Could not click on icon for List view.";
    //                }
    //                if (strErr == "")
    //                    return true;
    //                else
    //                {
    //                    strErrMsg_AppLib = strErr;
    //                    return false;
    //                }
    //            }
    //            try
    //            {
    //                //Verifying devices are displayed
    //                if (GetTextOrValue("class=message", "text").contains("deviceConnect currently has no configured devices or your filter produced no results."))
    //                    throw new Exception("deviceConnect currently has no configured devices or your filter produced no results.");
    //                //StopAutoRefresh(sView);
    //                int noOfDevices = driver.FindElements(By.XPath(xpathDevicesHolder)).Count;
    //                //if (sView.ToLower() == "list")
    //                //    noOfDevices = noOfDevices - 1; // ** one <tr>tag(row) is extra only in list view, so actual number of devices is 1 less than the tr tag count.
    //                for (int i = 1; i <= noOfDevices; i++)
    //                {
    //                    element = GetElement(xpathDevicesHolder + "[" + i + "]");
    //                    if (element != null)
    //                    {
    //                        if (sView.ToLower() == "list")
    //                            strDeviceName = GetTextOrValue(dicOR["eleDeviceName_ListView"].Replace("__INDEX__", i.ToString()), "text");
    //                        else   //ul[@class='cards-layout']/li[1]//div/div/a
    //                            strDeviceName = GetTextOrValue(dicOR["eleDeviceName_CardsView"].Replace("__INDEX__", i.ToString()), "text");
    //                        //string strDeviceName = genericLibrary.GetElement(".//h4/a", element).getText();
    //                        switch (sVerificationObjectName.ToLower())
    //                        {
    //                            case "connect":
    //                                //Getting the connect button for i'th device
    //                                ElementProperty = dicOR["btnConnectGridView"].Replace("__INDEX__", i.ToString());
    //                                childElement = GetElement(ElementProperty);
    //                                if (childElement != null)
    //                                {
    //                                    classname = getAttribute(ElementProperty, "class");// childElement.GetAttribute("class");
    //                                    // checking button is not disabled when it should be enabled.
    //                                    if(sVerificationObjectValue.ToLower().contains("enable") && !((classname == "connect-btn btn btn-info btn-sm") ||(classname == "connect-btn btn btn-info btn-sm ")))
    //                                        strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
    //                                    //checking button is not enabled when it should be disabled.
    //                                    else if(sVerificationObjectValue.ToLower().contains("disable") && classname != "connect-btn btn btn-info btn-sm disabled")
    //                                        strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
    //                                }
    //                                else
    //                                    strErrMsg_AppLib = strErrMsg_AppLib + "; Connect button is not found for device " + i.ToString() + " in devices card view.";
    //                                break;

    //                            case "status":
    //                                string srcAttribute = getAttribute(dicOR["eleStatusIcon_CardView"].Replace("__INDEX__", "1"), "class");
    //                                if (sVerificationObjectValue.ToLower().contains("available") || sVerificationObjectValue.ToLower().contains("green"))
    //                                {
    //                                    if (!srcAttribute.contains("dc-icon-device-status-available"))
    //                                        strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
    //                                }
    //                                else if (sVerificationObjectValue.ToLower().contains("offline") || sVerificationObjectValue.ToLower().contains("grey"))
    //                                {
    //                                    if (!srcAttribute.contains("dc-icon-device-status-offline"))
    //                                        strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
    //                                }
    //                                else if (sVerificationObjectValue.ToLower().contains("inuse") || sVerificationObjectValue.ToLower().contains("red"))
    //                                {
    //                                    if (!srcAttribute.contains("dc-icon-device-status-in-use"))
    //                                        strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
    //                                }
    //                                else if (sVerificationObjectValue.ToLower().contains("disabled") || sVerificationObjectValue.ToLower().contains("red-cross"))
    //                                {
    //                                    if (!srcAttribute.contains("dc-icon-device-status-disabled"))
    //                                        strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
    //                                }
    //                                break;

    //                            case "devicename":
    //                                if (sVerificationObjectValue.ToLower().contains("link"))
    //                                {
    //                                    if (sView.ToLower() == "list")
    //                                    {
    //                                        if (!GetElement(dicOR["eleDeviceName_ListView"].Replace("__INDEX__", i.ToString())).Enabled)
    //                                            strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
    //                                    }
    //                                    else
    //                                    {
    //                                        if (!GetElement(dicOR["eleDeviceName_CardsView"].Replace("__INDEX__", i.ToString())).Enabled)
    //                                            strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";

    //                                    }
    //                                }
    //                                break;
    //                            case "devicemodel":
    //                                if (sView.ToLower() == "list")
    //                                {
    //                                    if (!PerformAction(dicOR["eleDeviceModel_ListView"].Replace("__INDEX__", i.ToString()), Action.isDisplayed))
    //                                        strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
    //                                }
    //                                else
    //                                {
    //                                    if(!PerformAction(dicOR["eleDeviceModel_CardView"].Replace("__INDEX__", i.ToString()), Action.isDisplayed))
    //                                        strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
    //                                }
    //                                break;
    //                            case "deviceplatform":
    //                                if (sView.ToLower() == "list")
    //                                    childElement = GetElement(ElementProperty = (dicOR["eleDevicePlatform_ListView"].Replace("__INDEX__", i.ToString())));
    //                                else
    //                                    childElement = GetElement(ElementProperty = (dicOR["eleDevicePlatform_CardView"].Replace("__INDEX__", i.ToString())));

    //                                if (PerformAction(ElementProperty, Action.isDisplayed))
    //                                {
    //                                    text = GetTextOrValue(ElementProperty, "text");
    //                                    if(sVerificationObjectValue != "" && !text.ToLower().contains(sVerificationObjectValue.ToLower()))
    //                                        strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
    //                                }
    //                                else
    //                                    strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + "(Not displayed), ";
    //                                break;

    //                            case "devicestatus":
    //                                if (sView.ToLower() == "list")
    //                                {
    //                                    if (!GetElement(dicOR["eleDeviceStatus_ListView"].Replace("__INDEX__", i.ToString())).getText().ToLower().contains(sVerificationObjectValue))
    //                                        strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
    //                                }
    //                                else
    //                                {
    //                                    if (!GetElement(dicOR["eleDeviceStatus_CardView"].Replace("__INDEX__", i.ToString())).getText().ToLower().contains(sVerificationObjectValue))
    //                                        strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
    //                                }
    //                                break;
    //                            case "existenceofstatus": // This case checks if the given status's device exists on the UI or not, the moment it finds one, case breaks and returns "True"
    //                                isEventSuccessful = false;
    //                                if (sView.ToLower() == "list")
    //                                {
    //                                    if (GetElement(dicOR["eleDeviceStatus_ListView"].Replace("__INDEX__", i.ToString())).getText().ToLower().contains(sVerificationObjectValue))
    //                                        return true;
    //                                    //if (!driver.FindElement(By.XPath(xpathDevicesHolder + "[" + i + "]/td[1]")).getText().ToLower().contains(sVerificationObjectValue))
    //                                    //    return true;
    //                                }
    //                                else
    //                                {
    //                                    if (GetElement(dicOR["eleDeviceStatus_CardView"].Replace("__INDEX__", i.ToString())).Text.ToLower().contains(sVerificationObjectValue))
    //                                        return true;
    //                                    //if (!driver.FindElement(By.XPath(xpathDevicesHolder + "[" + i + "]//div[@class='location spec']")).getText().ToLower().contains(sVerificationObjectValue))
    //                                    //    return true;
    //                                }
    //                                if (!isEventSuccessful)
    //                                    strErrMsg_AppLib = "'" + sVerificationObjectValue + "' is not present on  the UI.";                                    
    //                                    break;
    //                        }
    //                    }
    //                    else
    //                        throw new Exception("Element " + xpathDevicesHolder + "[" + i + "] is not found in page.");
    //                }
    //                if (strErrMsg_AppLib == "")
    //                    isEventSuccessful = true;
    //                else
    //                    throw new Exception("Following devices' " + sVerificationObjectName + " is not displayed or is not correct: <br/>" + sVerificationObjectValue + " - " + strErrMsg_AppLib);
    //            }
    //            catch (Exception e)
    //            {
    //                isEventSuccessful = false;
    //                strErrMsg_AppLib = e.Message;
    //            }
    //            return isEventSuccessful;
    //        }
    //        else
    //        {
    //            isEventSuccessful = false;
    //            strErrMsg_AppLib = "Devices page is not displayed";
    //            return isEventSuccessful;
    //        }
    //}

     
    //It gives the list of uploaded apps for any OS version passed.
    //*NOTE : It only gives those application name that are displayed on applications page and not the builds.
    //
    //@param platform Platform for which uploaded applications are to be fetched, i.e. 'iOS', 'Android' or 'All'(for getting all apps listed on applications index page.)
    //@return List of all applications matching the OS platform.
    //
    public final ArrayList<String> getPlatformSpecificAppsLists(String platform)
    {
        ArrayList<String> appList = new ArrayList<String>();
        int rowCount = 0;
        String OS = "";
        String TableRows_Xpath = "eleAppTableRows";
        strErrMsg_AppLib = "";
        try
        {
            if (platform.toLowerCase().equals("android"))
            {
                OS = "Android";
            }
            else if (platform.toLowerCase().equals("ios"))
            {
                OS = "iOS";
            }
            else if (platform.toLowerCase().equals("all"))
            {
                OS = "All";
            }

            if (PerformAction("eleApplicationsHeader", Action.isDisplayed)) // check if applications page is displayed
            {
                TableRows_Xpath = dicOR.get("eleAppTableRows");
                if (!GetTextOrValue("class=message", "text").contains("deviceConnect currently has no configured applications.")) //Check if there are applications uploaded to system.
                {
                    rowCount = getelementCount(TableRows_Xpath);

                    for (int i = 1; i <= rowCount; i++)
                    {
                        if (platform.toLowerCase().equals("all"))
                        {
                            appList.add(GetTextOrValue(TableRows_Xpath + "[" + i + "]//td[1]/a", "text")); //If All apps are required, then put all the apps irrespective of the
                        }
                        else if (GetTextOrValue(TableRows_Xpath + "[" + i + "]//td[2]", "text").equals(OS)) //If OS of current row matches the required OS
                        {
                            appList.add(GetTextOrValue(TableRows_Xpath + "[" + i + "]//td[1]/a", "text")); // then put it to the appList
                        }
                    }
                }
                else
                {
                    throw new RuntimeException("deviceConnect currently has no configured applications.");
                }
            }
            else
            {
                throw new RuntimeException("Applications page not displayed.");
            }
        }
        catch (RuntimeException e)
        {
            strErrMsg_AppLib = "getPlatformSpecificAppsLists---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
            writeToLog("getPlatformSpecificAppsLists -- " + strErrMsg_AppLib + "\r\n            " + e.getStackTrace());
        }
        return appList;
    }

    public final boolean verifyAppsList(ArrayList<String> appList)
    {
        boolean flag = false;
        try
        { // in case no applications are uploaded.
            if (PerformAction("eleNoAppWarning", Action.isDisplayed))
            {
                if (appList.isEmpty())
                {
                    flag = true;
                }
            }
            // in case applications list is displayed
            else if (PerformAction("eleLaunchApplicationHeader", Action.isDisplayed))
            {
                int appCount = GenericLibrary.driver.findElements(By.xpath("//tr[starts-with(@class,'app-list-item')]/td[1]")).size(); // get count of list elements
                for (int i = 1; i <= appCount; i++)
                {
                    flag = false;
                    for (int j = 0; j < appList.size(); j++)
                    {
                        if ((GenericLibrary.driver.findElement(By.xpath("//tr[starts-with(@class,'app-list-item')][" + i + "]/td[1]")).getText()).contains(appList.get(j)))
                        {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag)
                    {
                        strErrMsg_AppLib = "Application is not as per the platform selected.";
                    }
                }
            }
            return flag;
        }
        catch (RuntimeException e)
        {
            strErrMsg_AppLib = "verifyAppsList---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
            return false;
        }
    }

    public final ArrayList<String> getApplistOnLaunchAppPage()
    {
        ArrayList<String> appList = new ArrayList<String>();
        strErrMsg_AppLib = "";
        try
        {
            int appCount = GenericLibrary.driver.findElements(By.xpath("//tr[starts-with(@class,'app-list-item')]/td[1]")).size();
            for (int i = 1; i <= appCount; i++)
            {
                appList.add(GenericLibrary.driver.findElement(By.xpath("//tr[starts-with(@class,'app-list-item')][" + i + "]/td[1]")).getText());
            }

        }
        catch (RuntimeException e)
        {
            strErrMsg_AppLib = "getApplistOnLaunchAppPage---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
            appList.clear();
        }
        return appList;
    }

    public final boolean Logout()
    {
        boolean flag = false; //PerformAction("lnkLogout", Action.WaitForElement
        strErrMsg_AppLib = "";
        try
        {
            flag = PerformAction("lnkLogout", Action.isDisplayed);
            if (!flag)
            {
                flag = PerformAction("btnMenu", Action.Click);
                if (!flag)
                {
                    strErrMsg_AppLib = "Menu button does not exist on the page.";
                }
            }
            if (flag)
            {
                flag = PerformAction("lnkLogout", Action.Click);
                if (flag)
                {
                    PerformAction("btnNo", Action.WaitForElement, "5");
                    if (PerformAction("btnNo", Action.Exist)) // click on confirmation button if it appears.
                    {
                        if (dicCommon.get("BrowserName").toLowerCase().contains("ie"))
                        {
                            PerformAction("btnNo", Action.ClickAtCenter);
                        }
                        else
                        {
                            PerformAction("btnNo", Action.Click);
                        }
                    }
                    flag = PerformAction("inpEmailAddress", Action.WaitForElement, "30");
                    if (!flag)

                    {
                        strErrMsg_AppLib = "Login page not displayed properly after clicking on Logout link.";
                    }
                }
                else
                {
                    strErrMsg_AppLib = "Logout link does not exist in the 'Menu' button's dropdown.";
                }
            }
        }
        catch (RuntimeException e)
        {
            flag = false;
            strErrMsg_AppLib = "Logout---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return flag;
    }

     
    //This function is used to select any value from the 'Menu'dropdown on any page
    //<p>*NOTE :  In case of 'Download Mobile Labs Trust' and 'Download deviceConnect CLI', it only clicks on the option but does not verify anything.</p>
    //<p>put putitional steps in script for verification</p>
    //
    //@param menuItemText Option inside the 'Menu' dropdown that needs to be selected.It should be in exact case as it appears in the dropdown.
    //@param expectedPageElement Identification of some unique element of the destination page.
    //<example>selectFromMenu("Logout", "btnLogin")</example>
    //@return True or false
    //
    public final boolean selectFromMenu(String menuItemText, String expectedPageElement)
    {
        boolean flag = false;
        strErrMsg_AppLib = "";
        try
        {
            flag = PerformAction("lnkDevicesMenu", Action.isDisplayed);
            if (!flag)
            {
                if (!dicCommon.get("BrowserName").contains("IE"))
                {
                    flag = PerformAction("btnMenu", Action.Click);
                }
                else
                {
                    flag = PerformAction("btnMenu", Action.ClickAtCenter);
                }

                if (!flag)
                {
                    strErrMsg_AppLib = "Could not click on 'Menu' button.";
                }
            }
            if (flag)
            {
                //Click on the required option under 'Menu' dropdown
                String element = "//a[text()='" + menuItemText + "']";
                flag = PerformAction(element, Action.Click);
                if (flag)
                {
                    // Case when some zip file is downloaded after clicking on an option in 'Menu' dropdown
                    if (menuItemText.equals("Download Mobile Labs Trust") || menuItemText.equals("Download deviceConnect CLI"))
                    {
                         //*******put verification code when the function for verifying the downloaded zip file is created.
                        //
                    }
                    //case for which user is navigated to some page, this block verifies if correct page is opened.
                    else
                    {
                        flag = PerformAction(expectedPageElement, Action.WaitForElement);
                        if (!flag)
                        {
                            strErrMsg_AppLib = menuItemText + " page not displayed after clicking on " + menuItemText + "link.";
                        }
                    }
                }
                else
                {
                    strErrMsg_AppLib = menuItemText + " link does not exist in the 'Menu' button's dropdown.";
                }
            }
        }
        catch (RuntimeException e)
        {
            flag = false;
            strErrMsg_AppLib = "selectFromMenu---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return flag;
    }

    public final boolean returnToDevicesPage()
    {
        boolean flag = false;
        strErrMsg_AppLib = "";
        try
        {
            flag = PerformAction("lnkDevicesMenu", Action.isDisplayed);
            if (!flag)
            {
                flag = PerformAction("btnMenu", Action.Click);
                if (!flag)
                {
                    strErrMsg_AppLib = "Menu button does not exist on the page.";
                }
            }
            if (flag)
            {
                flag = PerformAction("lnkDevicesMenu", Action.Click);
                if (flag)
                {
                    flag = PerformAction("eleDevicesHeader", Action.WaitForElement);
                    if (!flag)
                    {
                        strErrMsg_AppLib = "Devices page not displayed after clicking on Devices link.";
                    }
                }
                else
                {
                    strErrMsg_AppLib = "Devices link does not exist in the 'Menu' button's dropdown.";
                }
            }
        }
        catch (RuntimeException e)
        {
            flag = false;
            strErrMsg_AppLib = "returnToDevicesPage---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return flag;
    }

    public boolean selectPlatform(String Platform)
    {
        boolean flag = false;
        strErrMsg_AppLib = "";
        try
        {
            PerformAction("browser", Action.WaitForPageToLoad);
            if (GenericLibrary.dicCommon.get("BrowserName").toLowerCase().equals("ie"))
            {
                switch (Platform.toLowerCase().trim())
                {
                    case "all":
                        flag = PerformAction("lnkAll", Action.ClickAtCenter);
                        break;
                    case "android":
                        flag = PerformAction("lnkAndroid", Action.ClickAtCenter);
                        break;
                    case "ios":
                        PerformAction("browser", Action.Scroll, "30");
                        flag = PerformAction("lnkiOS", Action.ClickAtCenter);
                        break;
                }
            }
            else
            {
                switch (Platform.toLowerCase().trim())
                {
                    case "all":
                        flag = PerformAction("lnkAll", Action.Click);
                        break;
                    case "android":
                        flag = PerformAction("lnkAndroid", Action.Click);
                        break;
                    case "ios":
                        PerformAction("browser", Action.Scroll, "30");
                        try
                        {
                            driver.findElement(By.xpath("//a[text()='iOS']")).click();
                            PerformAction("browser", Action.WaitForPageToLoad, "120");
                            flag = true;
                        }
                        catch (RuntimeException e)
                        {
                            //
                        }
                        break;
                }
            }
            if (!flag)
            {
                throw new RuntimeException("Could not select platform - " + Platform);
            }
            PerformAction("browser", Action.WaitForPageToLoad);
            //Verifying devices are displayed
            if (GetTextOrValue("class=message", "text").contains("deviceConnect currently has no configured devices or your filter produced no results."))
            {
                throw new RuntimeException("deviceConnect currently has no configured devices or your filter produced no results.");
            }
            if (flag && Platform.toLowerCase().equals("all"))
            {
                flag = getAttribute("lnkAll", "class").equals("platform-filter active");
                if (!flag)
                {
                    throw new RuntimeException("Could not select platform - 'All'.");
                }
            }
            else if (flag)
            {
                flag = VerifyDeviceDetailsInGridAndListView("deviceplatform", Platform.toLowerCase());
            }
        }
        catch (RuntimeException e)
        {
            flag = false;
            strErrMsg_AppLib = "selectPlatform---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return flag;
    }

    public final boolean VerifyLogoutPopup()
    {
        strErrMsg_AppLib = "";
        boolean flag = false;
        try
        {

            flag = PerformAction("lnkLogout", Action.isDisplayed);
            if (!flag)
            {
                flag = PerformAction("btnMenu", Action.Click);
                if (!flag)
                {
                    strErrMsg_AppLib = "Menu button does not exist on the page.";
                    return flag;
                }

            }
            if (flag)
            {
                flag = PerformAction("lnkLogout", Action.Click);
                if (flag)
                {
                    if (!PerformAction("btnNo", Action.Exist)) // click on confirmation button if it appears.
                    {
                        strErrMsg_AppLib = strErrMsg_AppLib + "No button ,";
                    }
                    if (!PerformAction("btnYes", Action.Exist))
                    {
                        strErrMsg_AppLib = strErrMsg_AppLib + "Yes button ,";
                    }
                    if (!PerformAction("btnCancel", Action.Exist))
                    {
                        strErrMsg_AppLib = strErrMsg_AppLib + "Cancel button ,";
                    }
                    if (!GetTextOrValue("eleWarningOrConfirmationPopUpBody", "text").toLowerCase().contains("you currently have one or more active device sessions. do you want to release your current device sessions?"))
                    {
                        strErrMsg_AppLib = strErrMsg_AppLib + "Logout message - 'You currently have one or more active device sessions. Do you want to release your current device sessions?' ";
                    }

                    if ( ! strErrMsg_AppLib.equals(""))
                    {
                        strErrMsg_AppLib = "Following are not displayed on logout pop up - " + strErrMsg_AppLib;
                    }
                    else
                    {
                        flag = true;
                    }

                }
                else
                {
                    strErrMsg_AppLib = "Logout link does not exist in the 'Menu' button's dropdown.";
                }
            }
        }
        catch (RuntimeException e)
        {
            flag = false;
            strErrMsg_AppLib = "VerifyLogoutPopup---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return flag;
    }

    public final boolean DeleteFile(String strFileName)
    {
        strErrMsg_AppLib = "";
        boolean flag = false;
        int intCount = 0, i = 1;
        String strText = "";
        try
        {
            intCount = driver.findElements(By.xpath("//table[@class='table data-grid tablesorter']/tbody/tr")).size();
            if (intCount == 0)
            {
                strErrMsg_AppLib = "No data displayed in the table.";
                throw new RuntimeException(strErrMsg_AppLib);
            }
            else
            {
                for (i = 0; i <= intCount; i++)
                {
                    if (strFileName.toLowerCase().contains(".ipa") || strFileName.toLowerCase().contains(".apk"))
                    {
                        strText = GetTextOrValue("//table[@class='table data-grid']//tbody/tr[" + (new Integer(i)).toString() + "]/td[2]", "text");
                        if (strText.toLowerCase().equals(strFileName.toLowerCase()))
                        {
                            break;
                        }
                    }
                    else
                    {
                        strText = GetTextOrValue("//table[@class='table data-grid tablesorter']/tbody/tr[1]/td[1]/a[1]", "text");
                        if (strText.toLowerCase().equals(strFileName.toLowerCase()))
                        {
                            break;
                        }
                    }
                }

                if (i > intCount)
                {
                    strErrMsg_AppLib = "Application - '" + strFileName + " not found on page";
                    return flag;
                }
                flag = PerformAction("//table[@class='table data-grid tablesorter']/tbody/tr[" + (new Integer(i)).toString() + "]/td[5]//..//button[contains(@class,'btn btn-sm btn-info dropdown-toggle')]", Action.Click);
                if (flag)
                {
                    flag = PerformAction("//table[@class='table data-grid tablesorter']/tbody/tr[1]/td[5]//ul//..//a[contains(text(),'Delete')]", Action.Click);
                    if (flag)
                    {
                        flag = PerformAction("btnContinue_Disable", Action.Click);
                        if (flag)
                        {
                            PerformAction("eleNotificationRightBottom", Action.WaitForElement, "10");
                            if (GetTextOrValue("eleNotificationRightBottom", "text").contains("was successfully deleted."))
                            {
                                flag = true;
                            }
                            else
                            {
                                strErrMsg_AppLib = GetTextOrValue("eleNotificationRightBottom", "text");
                                flag = false;
                            }
                        }
                        else
                        {
                            strErrMsg_AppLib = "Unable to click on continue button of Delete application";
                        }
                    }
                }
                else
                {
                    strErrMsg_AppLib = "Unable to click on Delete button for application - " + strFileName;
                }

            }
        }
        catch (RuntimeException e)
        {
            flag = false;
            strErrMsg_AppLib = "DeleteFile---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return flag;
    }
	
	     
    //<p>PRE-Requisite : IWebDriver.ipa and Webdriver.apk should be present at DeviceConnectCLI folder</p>         
    //<p>This Function is for usage of CLI commands</p>
    //<p>This Function will perform Upload,Connect, Disable, Enable, Release on Android / iOS device using CLI</p>         
    //<p>Default CLIOption is Connect Command</p>                  
    //
    //<!--Created By : Vinita Mahajan-->
    //<!--Last modified : 10/2/2015 by Mandeep Kaur-->
    //@param CLIOption OPTIONAL : default value is connect - This parameter accepts values : connect, upload, release, disable, enable
    //@param platform OPTIONAL : default value is android
    //@param UserName OPTIONAL : default value is dicCommon["EmailAddress"] 
    //@param Password OPTIONAL : default value is dicCommon["Password"]
    //@param deviceName OPTIONAL - but in case of enable CLIOPTION, devicename is mandatory
    //@return 
    //

    public final Object[] ExecuteCLICommand(String CLIOption, String platform, String UserName, String Password)
    {
        return ExecuteCLICommand(CLIOption, platform, UserName, Password, "");
    }

    public final Object[] ExecuteCLICommand(String CLIOption, String platform, String UserName)
    {
        return ExecuteCLICommand(CLIOption, platform, UserName, "", "");
    }

    public final Object[] ExecuteCLICommand(String CLIOption, String platform)
    {
        return ExecuteCLICommand(CLIOption, platform, "", "", "");
    }

    public final Object[] ExecuteCLICommand(String CLIOption)
    {
        return ExecuteCLICommand(CLIOption, "android", "", "", "");
    }

    public final Object[] ExecuteCLICommand()
    {
        return ExecuteCLICommand("connect", "android", "", "", "");
    }
    //TODO OWN : To be implemented
    public final Object[] ExecuteCLICommand(String CLIOption, String platform, String UserName, String Password, String deviceName)
    {
        String CLI_Command = dicConfig.get("Artifacts") + "deviceConnectCLI\\";
        strErrMsg_AppLib = "";
        String FileName = "", command = "", deviceSelected = "", appName = "";
        boolean isEventSuccessful = false;
        
        Object[] returnValue = new Object[2]; 
        /*ProcessStartInfo ProcessInfo;
        Process process;
        try
        {
            if (!(new java.io.File(CLI_Command)).isDirectory())
            {
                strErrMsg_AppLib = "CLI Executable " + CLI_Command + " does not exist.";
                throw new RuntimeException(strErrMsg_AppLib);
            }

            if (UserName.equals(""))
            {
                UserName = dicCommon.get("EmailAddress");
            }
            if (Password.equals(""))
            {
                Password = dicCommon.get("Password");
            }
            if (platform.toLowerCase().equals("android"))
            {
                FileName = "\"" + CLI_Command + "android-server-2.38.0.apk" + "\"";
                appName = "\"" + "deviceControl" + "\"";
            }
            else if (platform.toLowerCase().equals("ios"))
            {
                FileName = "\"" + CLI_Command + "iWebDriver.ipa" + "\"";
                appName = "\"" + "deviceControl iOS5" + "\"";
            }

            if (deviceName.equals(""))
            {
                isEventSuccessful = PerformAction("eleDevicesTab_Devices", Action.isDisplayed);
                if (!isEventSuccessful)
                {
                    //if (!selectFromMenu("Devices", "eleDevicesHeader"))
                    if (!navigateToNavBarPages("Devices", "eleDevicesTab_Devices"))
                    {
                        throw new RuntimeException("On selecting 'Devices' menu, 'Devices' page is not opened.");
                    }
                }
                else
                {
                    if (selectStatus_DI("Available"))
                    {
                        if (!selectPlatform_DI(platform))
                        {
                            throw new RuntimeException(strErrMsg_AppLib);
                        }
                        if (PerformAction("eleNoDevicesWarning_Devices", Action.isDisplayed))
                        {
                            throw new RuntimeException("No devices displayed.");
                        }
                        deviceSelected = GetDeviceDetailInGridAndListView(1, "devicename");
                        deviceSelected = "\"" + deviceSelected + "\"";
                    }
                    else
                    {
                        isEventSuccessful = false;
                        throw new RuntimeException(strErrMsg_AppLib);
                    }
                }
            }
            else
            {
                deviceSelected = "\"" + deviceName + "\"";
            }

            switch (CLIOption.toLowerCase())
            {
                case "upload":
                    command = CLI_Command + "MobileLabs.DeviceConnect.Cli.exe " + dicCommon.get("ApplicationURL") + " " + UserName + " " + Password + " -upload" + FileName;
                    break;

                case "connect":
                    KillObjectInstances("MobileLabs.deviceViewer");
                    command = CLI_Command + "MobileLabs.DeviceConnect.Cli.exe " + dicCommon.get("ApplicationURL") + " " + UserName + " " + Password + " -device " + deviceSelected + " -run " + appName;
                    break;

                case "install":
                    command = CLI_Command + "MobileLabs.DeviceConnect.Cli.exe " + dicCommon.get("ApplicationURL") + " " + UserName + " " + Password + " -device " + deviceSelected + " -install " + appName;
                    break;

                case "disable":
                    command = CLI_Command + "MobileLabs.DeviceConnect.Cli.exe " + dicCommon.get("ApplicationURL") + " " + UserName + " " + Password + " -device " + deviceSelected + " -disable";
                    break;

                case "enable":
                    command = CLI_Command + "MobileLabs.DeviceConnect.Cli.exe " + dicCommon.get("ApplicationURL") + " " + UserName + " " + Password + " -enable " + deviceSelected;
                    break;

                case "release":
                    command = CLI_Command + "MobileLabs.DeviceConnect.Cli.exe " + dicCommon.get("ApplicationURL") + " " + UserName + " " + Password + " -device " + deviceSelected + " -release";
                    break;

                default:
                    command = CLI_Command + "MobileLabs.DeviceConnect.Cli.exe " + dicCommon.get("ApplicationURL") + " " + dicCommon.get("EmailAddress") + " " + dicCommon.get("Password") + " -device " + deviceSelected + " -retain -install \"deviceControl\" -autoconnect \"deviceControl\"";
                    break;
            }

            AddToDictionary(dicOutput, "executedCommand", command);

            //CLI_Command = CLI_Command + "MobileLabs.DeviceConnect.Cli.exe " + command;
            ProcessStartInfo tempVar = new ProcessStartInfo("cmd.exe", "/c " + command);
            tempVar.setUseShellExecute(false);
            tempVar.setRedirectStandardError(true);
            tempVar.setRedirectStandardOutput(true);
            tempVar.setRedirectStandardInput(true);
            ProcessInfo = tempVar;
            // *** Redirect the output ***
            process = Process.Start(ProcessInfo);

            // *** Read the streams ***
            if (process != null)
            {
                String output = process.StandardOutput.ReadToEnd();
                String error = process.StandardError.ReadToEnd();

                System.out.println("Reading the command : " + (output.isEmpty() ? "PASSED" : output));
                if (error.equals(""))
                {
                    if (CLIOption.equals("connect") || CLIOption.equals(""))
                    {
                        //Console.WriteLine("ERROR : " + error);
                        PerformAction("", Action.WaitForElement, "30");
//C# TO JAVA CONVERTER TODO TASK: There is no equivalent to implicit typing in Java:
                        var myAllProcesses = Process.GetProcessesByName("MobileLabs.deviceViewer");
                        if (myAllProcesses.Any())
                            isEventSuccessful = true;
                        else
                        {
                            isEventSuccessful = false;
                            strErrMsg_AppLib = "MobileLabs.deviceViewer.exe is not found." + "Device Name - " + deviceSelected;
                            throw new RuntimeException(strErrMsg_AppLib);
                        }
                    }
                    else
                        isEventSuccessful = true;
                }
                else
                {
                    strErrMsg_AppLib = "Error while executing the command : " + error + "\n" + "DeviceViewer has not opened." + "Device Used - " + deviceSelected;
                    isEventSuccessful = false;
                }
                process.close();
            }
        }
        catch (RuntimeException e)
        {
            isEventSuccessful = false;
            strErrMsg_AppLib = "ExecuteCLICommand---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }*/
//        return Pair.of(isEventSuccessful, deviceSelected.replace("\"", ""));  // Replacement for tuple in java
        return returnValue;
    }

     
    //<p>This Function returns the detail value for selected device by device display name</p>
    //<p>Permitted values for detailName - devicename, devicereservation, devicemodel, deviceplatform, devicestatus</p>
    //
    //<!--Created By : Vinita Mahajan-->
    //@param deviceName Display Name of the device
    //@param detailName Detail Name which User wants to get for particular device
    //@param strView From which View to verify and fetch the value
    //@return 
    //

    public final String GetSingleDeviceDetails(String deviceName, String detailName)
    {
        return GetSingleDeviceDetails(deviceName, detailName, "grid");
    }

    public final String GetSingleDeviceDetails(String deviceName, String detailName, String strView)
    {
        strErrMsg_AppLib = "";
        String strErr = "";
        WebElement element;
        boolean isEventSuccessful = false;
        String xpathDevicesHolder = "";
        String DevicesDetail = "";
        try
        {
            if (PerformAction("eleDevicesHeader", Action.WaitForElement))
            {
                if (strView.toLowerCase().equals("list"))
                {
                    xpathDevicesHolder = GenericLibrary.dicOR.get("eleDeviceName_ListView");
                }
                else if (strView.toLowerCase().equals("grid"))
                {
                    xpathDevicesHolder = GenericLibrary.dicOR.get("eleDeviceName_CardsView");
                }

                else if (strView.toLowerCase().equals("both"))
                {
                    //First check for grid view
                    DevicesDetail = GetSingleDeviceDetails(deviceName, detailName, "grid");
                    if (DevicesDetail.equals(""))
                    {
                        strErr = strErrMsg_AppLib;
                    }
                    // now open list view and verify for list view
                    isEventSuccessful = PerformAction("lnkListView", Action.Click);
                    if (isEventSuccessful)
                    {
                        DevicesDetail = GetSingleDeviceDetails(deviceName, detailName, "list");
                        if (DevicesDetail.equals(""))
                        {
                            strErr = strErr + strErrMsg_AppLib;
                        }
                    }
                    else
                    {
                        strErr = strErrMsg_AppLib + "Could not click on icon for List view.";
                    }
                    if (strErr.equals(""))
                    {
                        return DevicesDetail;
                    }
                    else
                    {
                        strErrMsg_AppLib = strErr;
                        return DevicesDetail;
                    }
                }


                //Verifying devices are displayed
                if (GetTextOrValue("class=message", "text").contains("deviceConnect currently has no configured devices or your filter produced no results."))
                {
                    throw new RuntimeException("deviceConnect currently has no configured devices or your filter produced no results.");
                }
                StopAutoRefresh(strView);
                int noOfDevices = getelementCount(xpathDevicesHolder.replace("[__INDEX__]", ""));
                for (int i = 1; i <= noOfDevices; i++)
                {
                    element = genericLibrary.GetElement(xpathDevicesHolder.replace("__INDEX__", (new Integer(i)).toString()));
                    if (deviceName.equals(element.getText()))
                    {
                        String strValue = GetDeviceDetailInGridAndListView(i, detailName, strView);
                        if ( ! strValue.equals(""))
                        {
                            DevicesDetail = strValue;
                        }
                        else
                        {
                            throw new RuntimeException(strErrMsg_AppLib);
                        }
                    }
                }
            }
            else
            {
                throw new RuntimeException("Devices page is not displayed");
            }
        }
        catch (RuntimeException e)
        {
            DevicesDetail = "";
            strErrMsg_AppLib = "GetSingleDeviceDetails---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return DevicesDetail;
    }

    //
    //This is only for verifying the Mobile Labs Trust and DeviceConnect CLI zip Folder
    //This works for only zip files which dont have any folders
    // 
    //\<!--Created By : Vinita Mahajan-->
    //\@param zipFilePath Downloaded path of the Zip file
    //\@param filesToBeVerified Verify all the Files under the zip folder : as a string array
    //@return 
    // TODO OWN : To be implemented
    public final boolean VerifyDownloadedZip(String zipFilePath, String[] filesToBeVerified)
    {
        boolean flag = false;
        strErrMsg_AppLib = "";
        String fileExtn = "zip";
        String fileName = "MobileLabsTrust";
        String fileNameIE = "";
        String[] ItemsNotFound = new String[] { };
        List<String> FailList = null;
        try
        {
        	
        	/* This code can be utilized
        	File zip = new File(zipFilePath);
        	ZipFile zip1 = new ZipFile(zipFilePath);
        	zip1.getInputStream(entry)
        	*/
        	
            /*String strTempPath = Path.GetTempPath();
            String[] strPath = strTempPath.split("[\\\\]", -1);
            if (dicCommon.get("BrowserName").equals("IE"))
            {
                fileName = fileNameIE;
            }
            File zip = new File(strPath[0] + "\\" + strPath[1] + "\\" + strPath[2] + "\\Downloads");
            File[] files = di.GetFiles().OrderByDescending(p -> p.CreationTime).ToArray();
            for (File file : files)
            {
                file.Attributes = FileAttributes.Normal;
                zipFilePath = file.getPath();
                break;
            }

            ZipInputStream zip = new ZipInputStream(File.OpenRead(zipFilePath));

            ZipEntry item;
            int i = 0, j = 0;
            for (j = 0; j < filesToBeVerified.length; j++)
            {
                while ((item = zip.GetNextEntry()) != null)
                {
                    if (item.Name.startsWith(filesToBeVerified[j]))
                    {
                        ItemsNotFound[j] = filesToBeVerified[j];
                        i++;
                        break;
                    }
                }
            }

            // This saves the values in a List which are not found in the Itemstobeverified
            List list1 = filesToBeVerified.ToList(), list2 = ItemsNotFound.ToList();
            for (int x = 0; x < list1.size(); x++)
            {
                if (!(list1.contains(list2.get(x))))
                {
                    FailList.put(list2.get(x).toString() + ", ");
                }
            }
            if (i == j)
            {
                flag = true;
            }
            else
            {
                strErrMsg_AppLib = "All Items are not found" + FailList.toString();
            }
            if ( ! strErrMsg_AppLib.equals(""))
            {
                flag = false;
                strErrMsg_AppLib = "VerifyDownloadedZip-- Following files are extra in the downloaded zip folder: " + strErrMsg_AppLib;
                //writeToLog("VerifyDownloadedZip-- " + strErrMsg_AppLib);
            }*/
        }
        catch (RuntimeException e)
        {
            flag = false;
            strErrMsg_AppLib = "VerifyDownloadedZip---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
            writeToLog("VerifyDownloadedZip --" + e.getStackTrace());
        }
        return flag;
    }

     
    //This Function verified that the Devices Multifunction dropdown options are present
    //
    //<!--Created By : Vinita Mahajan-->
    //@param objectNameToVerify List of string to verify under devices dropdown
    //@param status Status of the device on which the verification is to be made : available, disabled, offline, inuse
    //@param index OPTIONAL - Index of the device on the devices page : Pass 1 if want to verify for first device on page - Default value is 1
    //@param sView OPTIONAL - grid, list, both : By default it takes grid view
    //@return 
    //

    public final boolean VerifyDeviceOptions(ArrayList<String> objectNameToVerify, String status, int index)
    {
        return VerifyDeviceOptions(objectNameToVerify, status, index, "grid");
    }

    public final boolean VerifyDeviceOptions(ArrayList<String> objectNameToVerify, String status)
    {
        return VerifyDeviceOptions(objectNameToVerify, status, 1, "grid");
    }

    public final boolean VerifyDeviceOptions(ArrayList<String> objectNameToVerify, String status, int index, String sView)
    {
        strErrMsg_AppLib = "";
        boolean isEventSuccessful = false;
        WebElement element;
        List<WebElement> DropdownObjects = null; //List of WebElements for connect button DropDown
        List<String> DropdownValues = new ArrayList<String>(); // List of Text present on Connect button dropdown elements
        String DropDownListxpath = "";
        String xpathDevicesHolder = "", strDeviceName = "";

        if (PerformAction("eleDevicesHeader", Action.WaitForElement))
        {
            if (sView.toLowerCase().equals("list"))
            {
                xpathDevicesHolder = dicOR.get("eleDevicesHolderListView");
                DropDownListxpath = "(" + xpathDevicesHolder + "/td[7]//..//following-sibling::ul[@class='dropdown-menu'])";
            }
            else if (sView.toLowerCase().equals("grid"))
            {
                xpathDevicesHolder = dicOR.get("eleDevicesHolderGridView");
                DropDownListxpath = "(" + xpathDevicesHolder + "//..//following-sibling::ul[@class='dropdown-menu'])";
            }

            // verification in case user wants to verify on both grid and list view
            else if (sView.toLowerCase().equals("both"))
            {
                String strErr = "";
                //First check for grid view
                //isEventSuccessful = VerifyPerformActionsOnDevicesPage(objectNameToVerify, sVerificationObjectValue,status,UserRole,index, "grid");
                isEventSuccessful = VerifyDeviceOptions(objectNameToVerify, status, index, "grid");
                if (!isEventSuccessful)
                {
                    strErr = strErrMsg_AppLib;
                }
                // now open list view and verify for list view
                isEventSuccessful = PerformAction("lnkListView", Action.Click);
                if (isEventSuccessful)
                {
                    //isEventSuccessful = VerifyPerformActionsOnDevicesPage(objectNameToVerify, sVerificationObjectValue,status,UserRole,index,"list");
                    isEventSuccessful = VerifyDeviceOptions(objectNameToVerify, status, index, "list");
                    if (!isEventSuccessful)
                    {
                        strErr = strErr + strErrMsg_AppLib;
                    }
                }
                else
                {
                    strErr = strErrMsg_AppLib + "Could not click on icon for List view.";
                }
                if (strErr.equals(""))
                {
                    return true;
                }
                else
                {
                    strErrMsg_AppLib = strErr;
                    return false;
                }
            }

            ///#################### Verification for Specifically 'Grid' or 'list' view ###################//
            try
            {
                //Verifying devices are displayed
                if (GetTextOrValue("class=message", "text").contains("deviceConnect currently has no configured devices or your filter produced no results."))
                {
                    throw new RuntimeException("deviceConnect currently has no configured devices or your filter produced no results.");
                }
                //StopAutoRefresh(sView);
                int noOfDevices = getelementCount(xpathDevicesHolder); // Get number of devices' rows/cards displayed in given view
                for (int i = 1; i < noOfDevices; i++)
                {
                    if (i == index)
                    {
                        //element = GetElement(xpathDevicesHolder + "[" + i + "]" + "//..//following-sibling::button[contains(@class,'btn btn-info dropdown-toggle')]");
                        if (sView.toLowerCase().equals("list"))
                        {
                            element = GetElement(xpathDevicesHolder + "[" + i + "]" + "/td[7]//..//following-sibling::button[contains(@class,'btn btn-info dropdown-toggle')]");
                            strDeviceName = GetTextOrValue(dicOR.get("eleDeviceName_ListView").replace("__INDEX__", (new Integer(i)).toString()), "text");
                        }
                        else
                        {
                            element = GetElement("(" + xpathDevicesHolder + "//..//following-sibling::button[contains(@class,'btn btn-info dropdown-toggle')])" + "[" + i + "]");
                            strDeviceName = GetTextOrValue(dicOR.get("eleDeviceName_CardsView").replace("__INDEX__", (new Integer(i)).toString()), "text");
                        }
                        if (element != null)
                        {
                            // Now verify the value of the element required using different cases
                            switch (status.toLowerCase())
                            {
                                case "available":
                                case "inuse":
                                    if (sView.toLowerCase().equals("list"))
                                    {
                                        isEventSuccessful = PerformAction(xpathDevicesHolder + "[" + i + "]" + "/td[7]//..//following-sibling::button[contains(@class,'btn btn-info dropdown-toggle')]", Action.Click);
                                    }
                                    else
                                    {
                                        isEventSuccessful = PerformAction("(" + xpathDevicesHolder + "//..//following-sibling::button[contains(@class,'btn btn-info dropdown-toggle')])" + "[" + i + "]", Action.Click);
                                    }
                                    if (isEventSuccessful)
                                    {
                                        DropdownObjects = getelementsList(DropDownListxpath + "[" + i + "]" + "/li/a");
                                        for (int j = 0; j < DropdownObjects.size(); j++)
                                        {
                                            DropdownValues.add(DropdownObjects.get(j).getText());
                                        }
                                        if (DropdownValues.equals(objectNameToVerify)) //.SequenceEqual(objectNameToVerify)
                                        {
                                            isEventSuccessful = true;
                                        }
                                        else
                                        {
                                            isEventSuccessful = false;
                                            strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
                                        }
                                    }
                                    break;

                                case "disabled":
                                case "offline":
                                    if (sView.toLowerCase().equals("list"))
                                    {
                                        isEventSuccessful = PerformAction(xpathDevicesHolder + "[" + i + "]" + "/td[7]//..//following-sibling::button[contains(@class,'btn btn-info dropdown-toggle')]", Action.Click);
                                    }
                                    else
                                    {
                                        isEventSuccessful = PerformAction("(" + xpathDevicesHolder + "//..//following-sibling::button[contains(@class,'btn btn-info dropdown-toggle')])" + "[" + i + "]", Action.Click);
                                    }
                                    if (isEventSuccessful)
                                    {
                                        DropdownObjects = getelementsList(DropDownListxpath + "[" + i + "]" + "/li/a");
                                        for (int j = 0; j < DropdownObjects.size(); j++)
                                        {
                                            DropdownValues.add(DropdownObjects.get(j).getText());
                                        }
                                        if (DropdownValues.equals(objectNameToVerify))
                                        {
                                            isEventSuccessful = true;
                                        }
                                        else
                                        {
                                            isEventSuccessful = false;
                                            strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
                                        }
                                    }
                                    else
                                    {
                                        DropdownObjects = getelementsList(DropDownListxpath + "[" + i + "]" + "/li/a");
                                        if (DropdownObjects == null)
                                        {
                                            isEventSuccessful = true;
                                        }
                                        else
                                        {
                                            isEventSuccessful = false;
                                            strErrMsg_AppLib = strErrMsg_AppLib + strDeviceName + ", ";
                                        }
                                    }
                                    break;
                            }
                        }
                        else
                        {
                            throw new RuntimeException("Connect Button Dropdown Element " + xpathDevicesHolder + "[" + i + "] is not found in page.");
                        }
                    }
                }
                if (strErrMsg_AppLib.equals(""))
                {
                    isEventSuccessful = true;
                }
                else
                {
                    throw new RuntimeException("DropDown Values for device " + strDeviceName + "is not displayed or is not correct: <br/>" + " - " + strErrMsg_AppLib);
                }
            }
            catch (RuntimeException e)
            {
                isEventSuccessful = false;
                strErrMsg_AppLib = "VerifyDeviceOptions---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
            }
            return isEventSuccessful;
        }
        else
        {
            isEventSuccessful = false;
            strErrMsg_AppLib = "VerifyDeviceOptions---" + "Devices page is not displayed";
            return isEventSuccessful;
        }
    }

     
    // It verifies if all the apps displayed are of the given platform OR no cell is empty inside the applications page.
    //
    //<!--Modified By : Mandeep Mann-->
    //@param sVerificationObjectName It takes values "platform" and "cellvalues"
    //@param sVerificationObjectValue For "platform", it accepts values : "Android" and "iOS" and verifies that only the apps with given OS platform are displayed.
    //<p>For "cellvalues", it requires no value and verifies that no cell is empty for any application displayed.</p>
    //@param sView
    //@return 
    //

    public final boolean VerifyAppDetailsInListView(String sVerificationObjectName)
    {
        return VerifyAppDetailsInListView(sVerificationObjectName, "");
    }

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public bool VerifyAppDetailsInListView(string sVerificationObjectName, string sVerificationObjectValue = "")
    public final boolean VerifyAppDetailsInListView(String sVerificationObjectName, String sVerificationObjectValue)
    {
        boolean flag = false;
        String TableRows_Xpath = "";
        int rowCount = 0;
        int columnsCount = 0;
        String errorColumnIndex = "", errorRows = "";
        strErrMsg_AppLib = "";

        //Modifying ot correct case : 
        if (sVerificationObjectValue.toLowerCase().equals("android"))
        {
            sVerificationObjectValue = "Android";
        }
        else if (sVerificationObjectValue.toLowerCase().equals("ios"))
        {
            sVerificationObjectValue = "iOS";
        }

        try
        {
            if (PerformAction("eleApplicationsHeader", Action.isDisplayed)) // check if applications page is displayed
            {
                flag = true;
            }
            else
            {
                throw new RuntimeException("Applications page not displayed.");
            }

            // If no apps are displayed at the moment then return true and exit. Also, write to logs file that there were no applications
            if (GetTextOrValue("class=message", "text").contains("deviceConnect currently has no configured applications.")) //Check if there are applications uploaded to system.
            {
                throw new RuntimeException("deviceConnect currently has no configured applications.");
            }

            //If applications page is open, then proceed further and verify that for all apps, the sVerificationObjectName has sVerificationObjectValue as value
            TableRows_Xpath = dicOR.get("eleAppTableRows");
            rowCount = getelementCount(TableRows_Xpath); // Count of all the rows displayed at the moment;
            if (rowCount != 0)
            {
                switch (sVerificationObjectName)
                {
                    case "platform":
                        for (int i = 1; i <= rowCount; i++)
                        {
                            if (!GetTextOrValue(TableRows_Xpath + "[" + i + "]//td[2]", "text").equals(sVerificationObjectValue))
                            {
                                errorRows = errorRows + i + ",";
                            }
                        }
                        if ( ! errorRows.equals("")) //Throw exception if there are some rows with wrong platform.
                        {
                            throw new RuntimeException("Platform is not correct for apps at row number  " + errorRows);
                        }
                        break;

                    case "cellvalues":
                        for (int i = 1; i <= rowCount; i++) //For each row, --
                        {
                            errorColumnIndex = "";
                            columnsCount = getelementCount(TableRows_Xpath + "[" + i + "]//td"); //--Get the number of columns.
                            if (columnsCount != 0) // If column count is 0, then put to error message that no columns exist for this particular row
                            {
                                for (int j = 1; j <= columnsCount; j++) // Iterate over all the columns of this row--
                                {
                                    if ((GetTextOrValue(TableRows_Xpath + "[" + i + "]//td[" + j + "]", "text").isEmpty())) //--and verify if any cell is empty or white space
                                    {
                                        errorColumnIndex = errorColumnIndex + j + ",";
                                    }
                                }
                                if ( ! errorColumnIndex.equals("")) //Check how to replace __AND__ with new line operator to display new line in report.
                                {
                                    strErrMsg_AppLib = strErrMsg_AppLib + " _ AND_ Column numbers  " + errorColumnIndex + " are empty for app at row number: " + i;
                                }
                            }
                            else
                            {
                                strErrMsg_AppLib = strErrMsg_AppLib + "_AND_ No columns exist for row number  " + i;
                            }
                        }
                        if ( ! strErrMsg_AppLib.equals(""))
                        {
                            throw new RuntimeException(strErrMsg_AppLib);
                        }
                        break;
                }
            }
            else
            {
                throw new RuntimeException("No rows found on reservations page.");
            }
        }
        catch (RuntimeException e)
        {
            flag = false;
            strErrMsg_AppLib = "VerifyAppDetailsInListView---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
            //writeToLog(strErrMsg_AppLib);
        }
        
        System.out.println("Verified all values in all columns ... there are no empty values");
        return flag;
    }

     
    //This method will fetch and return the list of drop down options
    //
    //<!--Created By : Hitesh Ghai-->
    //@param dropdownName
    //@return 
    //
    public final List<String> getDropDownOptions(String dropdownName)
    {
        strErrMsg_AppLib = "";
        List<String> optionsFound = new ArrayList<String>();
        List<WebElement> DropdownObjects = new ArrayList<WebElement>();
        try
        {
            switch (dropdownName)
            {
                case "menu":
                case "Menu":
                    if (PerformAction("btnMenu", Action.Click))
                    {
                        DropdownObjects = getelementsList(dicOR.get("eleMenubtnOptions"));
                    }
                    else
                    {
                        throw new RuntimeException("Could not click on 'Menu' button.");
                    }
                    break;

                default:
                    if (PerformAction(dicOR.get("eleDropdownFilter").replace("__FILTER__", dropdownName), Action.Click))
                    {
                        DropdownObjects = getelementsList(dicOR.get("eleAllDropdownOptions").replace("__FILTER__", dropdownName));
                    }
                    else
                    {
                        throw new RuntimeException("Could not click on '" + dropdownName + "' filter.");
                    }
                    break;
            }
            for (WebElement optionSelect : DropdownObjects)
            {
                optionsFound.add(optionSelect.getText());
            }
        }
        catch (RuntimeException e)
        {
            strErrMsg_AppLib = "getDropDownOptions---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
            //writeToLog(strErrMsg_AppLib);
        }
        return optionsFound;
    }

     
    //Returns all elements in a one-dimensional list to a string containing &lt;&lt;&gt;&gt; delimiter separated values in string form.
    //
    //<!--Created By : Mandeep Mann-->
    //@param list list of type List&lt;object&gt;
    //@return String with comma separated elements(in string form)
    //
    public final String ListToString(List<Object> list)
    {
        String elementsString = "";
        strErrMsg_AppLib = "";
        try
        {
            for (Object ele : list)
            {
                elementsString = elementsString + "<<>>" + ele.toString();
            }
        }
        catch (RuntimeException e)
        {
            strErrMsg_AppLib = "ListToString -- " + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return elementsString;
    }

     
    //Converts the given array of string values to single string containing all array values separated by ','
    //
    //<!-- Created by : Mandeep Kaur -->
    //<!-- Last modified : 9/2/2015 by Mandeep Kaur -->
    //@param arr String array that needs to be converted to single string.
    //@return Single string containing all array values each separated by ',' .
    //
    public final String ArrayToString(String[] arr)
    {
        String elementsString = "";
        strErrMsg_AppLib = "";
        try
        {
            for (String str : arr)
            {
                elementsString = elementsString + "," + str;
            }
        }
        catch (RuntimeException e)
        {
            strErrMsg_AppLib = "ArrayToString -- " + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return elementsString;
    }

     
    //This function verifies the field value on Users page in Grid/List View
    //
    //<!--Created By : Vinita Mahajan-->
    //@param sVerificationObjectName Fields on Users card/list to verify : statusicon,firstname,lastname,emailid,userrole,edit
    //status icon: green, grey, firstname : exepcted value,lastname : exepceted value, emailid : exepcetd value,userrole, edit button checks only the existence
    //@param sVerificationObjectValue Value which is expected
    //@param sView View in which to be verified
    //@return 
    //

    public final boolean VerifynUsersPage(String sVerificationObjectName, String sVerificationObjectValue)
    {
        return VerifynUsersPage(sVerificationObjectName, sVerificationObjectValue, "grid");
    }

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public bool VerifynUsersPage(string sVerificationObjectName, string sVerificationObjectValue, string sView = "grid")
    public final boolean VerifynUsersPage(String sVerificationObjectName, String sVerificationObjectValue, String sView)
    {
        strErrMsg_AppLib = "";
        String[] arrSplitString;
        boolean isEventSuccessful = false;
        WebElement element;
        String xpathDevicesHolder = "", strUserName = "", text = "", srcAttribute = "";

        try
        {
            if (PerformAction("eleUsersHeader", Action.isDisplayed))
            {
                if (sView.toLowerCase().equals("list"))
                {
                    xpathDevicesHolder = dicOR.get("eleUsersHolderListView");
                }
                else if (sView.toLowerCase().equals("grid"))
                {
                    xpathDevicesHolder = dicOR.get("eleUsersHolderGridView");
                }

                // verification in case user wants to verify on both grid and list view
                else if (sView.toLowerCase().equals("both"))
                {
                    String strErr = "";
                    //First check for grid view
                    isEventSuccessful = VerifynUsersPage(sVerificationObjectName, sVerificationObjectValue, "grid");
                    if (!isEventSuccessful)
                    {
                        strErr = strErrMsg_AppLib;
                    }
                    // now open list view and verify for list view
                    isEventSuccessful = PerformAction("lnkListView", Action.Click);
                    if (isEventSuccessful)
                    {
                        isEventSuccessful = VerifynUsersPage(sVerificationObjectName, sVerificationObjectValue, "list");
                        if (!isEventSuccessful)
                        {
                            strErr = strErr + strErrMsg_AppLib;
                        }
                    }
                    else
                    {
                        strErr = strErrMsg_AppLib + "Could not click on icon for List view.";
                    }
                    if (strErr.equals(""))
                    {
                        return true;
                    }
                    else
                    {
                        strErrMsg_AppLib = strErr;
                        return false;
                    }
                }
                /////////////////## Verification for "both" ends. ##///////////////////////

                ///#################### Verification for Specifically 'Grid' or 'list' view ###################//

                int noOfDevices = getelementCount(xpathDevicesHolder); // Get number of devices' rows/cards displayed in given view
                for (int i = 1; i <= noOfDevices; i++)
                {
                    element = GetElement(xpathDevicesHolder + "[" + i + "]");
                    if (element != null)
                    {
                        if (sView.toLowerCase().equals("list"))
                        {
                            strUserName = GetTextOrValue(dicOR.get("eleFirstNameUsers_ListView").replace("__INDEX__", (new Integer(i)).toString()), "text");
                        }
                        else
                        {
                            strUserName = GetTextOrValue(dicOR.get("eleFirstLastName_GridView").replace("__INDEX__", (new Integer(i)).toString()), "text");
                        }

                        // Now verify the value of the element required using different cases
                        switch (sVerificationObjectName.toLowerCase())
                        {
                            case "statusiconandtext":
                                if (sView.toLowerCase().equals("list"))
                                {
                                    srcAttribute = getAttribute(dicOR.get("eleStatusIconUsers_ListView").replace("__INDEX__", (new Integer(i)).toString()), "class");
                                    if ((GetTextOrValue(dicOR.get("eleUserStatus_ListView").replace("__INDEX__", (new Integer(i)).toString()), "text").equals("Active")))
                                    {
                                        if (!srcAttribute.contains("dc-icon-device-status-available"))
                                        {
                                            strErrMsg_AppLib = strErrMsg_AppLib + strUserName + ", ";
                                        }
                                    }
                                    else if ((GetTextOrValue(dicOR.get("eleUserStatus_ListView").replace("__INDEX__", (new Integer(i)).toString()), "text").equals("Inactive")))
                                    {
                                        if (!srcAttribute.contains("dc-icon-device-status-offline"))
                                        {
                                            strErrMsg_AppLib = strErrMsg_AppLib + strUserName + ", ";
                                        }
                                    }
                                }
                                else
                                {
                                    srcAttribute = getAttribute(dicOR.get("eleStatusIconUsers_GridView").replace("__INDEX__", (new Integer(i)).toString()), "class");
                                    if ((GetTextOrValue(dicOR.get("eleUserStatus_GridView").replace("__INDEX__", (new Integer(i)).toString()), "text").equals("Active")))
                                    {
                                        if (!srcAttribute.contains("dc-icon-device-status-available"))
                                        {
                                            strErrMsg_AppLib = strErrMsg_AppLib + strUserName + ", ";
                                        }
                                    }
                                    else if ((GetTextOrValue(dicOR.get("eleUserStatus_GridView").replace("__INDEX__", (new Integer(i)).toString()), "text").equals("Inactive")))
                                    {
                                        if (!srcAttribute.contains("dc-icon-device-status-offline"))
                                        {
                                            strErrMsg_AppLib = strErrMsg_AppLib + strUserName + ", ";
                                        }
                                    }
                                }
                                if ( ! sVerificationObjectValue.equals(""))
                                {
                                    return true;
                                }
                                break;

                            case "statusicon":
                                if (sView.toLowerCase().equals("list"))
                                {
                                    srcAttribute = getAttribute(dicOR.get("eleUserStatus_ListView").replace("__INDEX__", "1"), "class");
                                }
                                else
                                {
                                    srcAttribute = getAttribute(dicOR.get("eleUserStatus_GridView").replace("__INDEX__", "1"), "class");
                                }
                                if (sVerificationObjectValue.toLowerCase().contains("available") || sVerificationObjectValue.toLowerCase().contains("green"))
                                {
                                    if (!srcAttribute.contains("dc-icon-device-status-available"))
                                    {
                                        strErrMsg_AppLib = strErrMsg_AppLib + strUserName;
                                    }
                                }
                                else if (sVerificationObjectValue.toLowerCase().contains("offline") || sVerificationObjectValue.toLowerCase().contains("grey"))
                                {
                                    if (!srcAttribute.contains("dc-icon-device-status-offline"))
                                    {
                                        strErrMsg_AppLib = strErrMsg_AppLib + strUserName;
                                    }
                                }
                                break;

                            case "firstname":
                                if (sView.toLowerCase().equals("list"))
                                {
                                    text = GetElement(dicOR.get("eleFirstNameUsers_ListView").replace("__INDEX__", (new Integer(i)).toString())).getText().toLowerCase();
                                    if (!text.isEmpty())
                                    {
                                        if ( ! sVerificationObjectValue.toLowerCase().equals("exist"))
                                        {
                                            if (text.startsWith(sVerificationObjectValue.toLowerCase()))
                                            {
                                                strErrMsg_AppLib = "";
                                                isEventSuccessful = true;
                                                return isEventSuccessful;
                                            }
                                            else
                                            {
                                                strErrMsg_AppLib = strErrMsg_AppLib + strUserName + ", ";
                                            }
                                        }
                                        else
                                        {
                                            isEventSuccessful = PerformAction(dicOR.get("eleFirstNameUsers_ListView").replace("__INDEX__", (new Integer(i)).toString()), Action.isDisplayed);
                                        }
                                    }
                                }
                                else
                                {
                                    text = GetElement(dicOR.get("eleFirstLastName_GridView").replace("__INDEX__", (new Integer(i)).toString())).getText().toLowerCase().split(" ", 2)[0];
                                    if (!text.isEmpty())
                                    {
                                        if ( ! sVerificationObjectValue.toLowerCase().equals("exist"))
                                        {
                                            if (text.startsWith(sVerificationObjectValue.toLowerCase()))
                                            {
                                                strErrMsg_AppLib = "";
                                                isEventSuccessful = true;
                                                return isEventSuccessful;
                                            }
                                            else
                                            {
                                                strErrMsg_AppLib = strErrMsg_AppLib + strUserName + ", ";
                                            }
                                        }
                                        else
                                        {
                                            isEventSuccessful = PerformAction(dicOR.get("eleFirstLastName_GridView").replace("__INDEX__", (new Integer(i)).toString()), Action.isDisplayed);
                                        }
                                    }
                                }
                                break;

                            case "lastname":
                                if (sView.toLowerCase().equals("list"))
                                {
                                    text = GetElement(dicOR.get("eleLastNameUsers_ListView").replace("__INDEX__", (new Integer(i)).toString())).getText().toLowerCase();
                                    if (!text.isEmpty())
                                    {
                                        if ( ! sVerificationObjectValue.toLowerCase().equals("exist"))
                                        {
                                            if (text.startsWith(sVerificationObjectValue.toLowerCase()))
                                            {
                                                strErrMsg_AppLib = "";
                                                isEventSuccessful = true;
                                                return isEventSuccessful;
                                            }
                                            else
                                            {
                                                strErrMsg_AppLib = strErrMsg_AppLib + strUserName + ", ";
                                            }
                                        }
                                        else
                                        {
                                            isEventSuccessful = PerformAction(dicOR.get("eleLastNameUsers_ListView").replace("__INDEX__", (new Integer(i)).toString()), Action.isDisplayed);
                                        }
                                    }
                                }
                                else
                                {
                                	arrSplitString = GetElement(dicOR.get("eleFirstLastName_GridView").replace("__INDEX__", Integer.toString(i))).getText().toLowerCase().split(" ", 0); // Splits the string by empty space character. split(" ", 0) specifies that the 
                                    text = arrSplitString[arrSplitString.length - 1]; // Gives the last value split from the above string.
                                    if (!text.isEmpty())
                                    {
                                        if ( ! sVerificationObjectValue.toLowerCase().equals("exist"))
                                        {
                                            if (text.startsWith(sVerificationObjectValue.toLowerCase()))
                                            {
                                                strErrMsg_AppLib = "";
                                                isEventSuccessful = true;
                                                return isEventSuccessful;
                                            }
                                            else
                                            {
                                                strErrMsg_AppLib = strErrMsg_AppLib + strUserName + ", ";
                                            }
                                        }
                                        else
                                        {
                                            isEventSuccessful = PerformAction(dicOR.get("eleFirstLastName_GridView").replace("__INDEX__", (new Integer(i)).toString()), Action.isDisplayed);
                                        }
                                    }
                                }
                                break;

                            case "emailid":
                                if (sView.toLowerCase().equals("list"))
                                {
                                    text = GetElement(dicOR.get("eleEmailIDUsers_ListView").replace("__INDEX__", (new Integer(i)).toString())).getText().toLowerCase();
                                    if (!text.isEmpty())
                                    {
                                        if ( ! sVerificationObjectValue.toLowerCase().equals("exist"))
                                        {
                                            if (text.startsWith(sVerificationObjectValue.toLowerCase()))
                                            {
                                                strErrMsg_AppLib = "";
                                                isEventSuccessful = true;
                                                return isEventSuccessful;
                                            }
                                            else
                                            {
                                                strErrMsg_AppLib = strErrMsg_AppLib + strUserName + ", ";
                                            }
                                            //if (!text.StartsWith(sVerificationObjectValue.ToLower()))
                                            //    strErrMsg_AppLib = strErrMsg_AppLib + strUserName + ", ";
                                        }
                                        else
                                        {
                                            isEventSuccessful = PerformAction(dicOR.get("eleEmailIDUsers_ListView").replace("__INDEX__", (new Integer(i)).toString()), Action.isDisplayed);
                                        }
                                    }
                                }
                                else
                                {
                                    text = GetElement(dicOR.get("eleEmailIDUsers_GridView").replace("__INDEX__", (new Integer(i)).toString())).getText().toLowerCase();
                                    if (!text.isEmpty())
                                    {
                                        if ( ! sVerificationObjectValue.toLowerCase().equals("exist"))
                                        {
                                            if (text.startsWith(sVerificationObjectValue.toLowerCase()))
                                            {
                                                strErrMsg_AppLib = "";
                                                isEventSuccessful = true;
                                                return isEventSuccessful;
                                            }
                                            else
                                            {
                                                strErrMsg_AppLib = strErrMsg_AppLib + strUserName + ", ";
                                            }
                                        }
                                        else
                                        {
                                            isEventSuccessful = PerformAction(dicOR.get("eleEmailIDUsers_GridView").replace("__INDEX__", (new Integer(i)).toString()), Action.isDisplayed);
                                        }
                                    }
                                }

                                if (!isEventSuccessful)
                                {
                                    strErrMsg_AppLib = "'" + sVerificationObjectValue + "' is not present on  the UI.";
                                }
                                break;

                            case "userrole":
                                if (sView.toLowerCase().equals("list"))
                                {
                                    isEventSuccessful = PerformAction(dicOR.get("eleRoleUsers_ListView").replace("__INDEX__", (new Integer(i)).toString()), Action.isDisplayed);
                                    if (!isEventSuccessful)
                                    {
                                        strErrMsg_AppLib = strErrMsg_AppLib + strUserName + ", ";
                                    }
                                }
                                else
                                {
                                    isEventSuccessful = PerformAction(dicOR.get("eleRoleUsers_GridView").replace("__INDEX__", (new Integer(i)).toString()), Action.isDisplayed);
                                    if (!isEventSuccessful)
                                    {
                                        strErrMsg_AppLib = strErrMsg_AppLib + strUserName + ", ";
                                    }
                                }
                                break;

                            case "edit":
                                if (sView.toLowerCase().equals("list"))
                                {
                                    isEventSuccessful = PerformAction(dicOR.get("btnEdit_ListView").replace("__INDEX__", (new Integer(i)).toString()), Action.isDisplayed);
                                    if (!isEventSuccessful)
                                    {
                                        strErrMsg_AppLib = strErrMsg_AppLib + strUserName + ", ";
                                    }
                                }
                                else
                                {
                                    isEventSuccessful = PerformAction(dicOR.get("btnEdit_CardView").replace("__INDEX__", (new Integer(i)).toString()), Action.isDisplayed);
                                    if (!isEventSuccessful)
                                    {
                                        strErrMsg_AppLib = strErrMsg_AppLib + strUserName + ", ";
                                    }
                                }
                                break;

                            case "existenceofstatus": // This case checks if the given status's User exists on the UI or not, the moment it finds one, case breaks and returns "True"
                                isEventSuccessful = false;
                                strErrMsg_AppLib = "";
                                if (sView.toLowerCase().equals("list"))
                                {
                                    if (GetTextOrValue(dicOR.get("eleUserStatus_ListView").replace("__INDEX__", (new Integer(i)).toString()), "text").toLowerCase().startsWith(sVerificationObjectValue.toLowerCase()))
                                    {
                                        return true;
                                    }
                                    //if (!driver.FindElement(By.XPath(xpathDevicesHolder + "[" + i + "]/td[1]")).getText().ToLower().contains(sVerificationObjectValue))
                                    //    return true;
                                }
                                else
                                {
                                    if (GetTextOrValue(dicOR.get("eleUserStatus_GridView").replace("__INDEX__", (new Integer(i)).toString()), "text").toLowerCase().startsWith(sVerificationObjectValue.toLowerCase()))
                                    {
                                        return true;
                                    }
                                    //if (!driver.FindElement(By.XPath(xpathDevicesHolder + "[" + i + "]//div[@class='location spec']")).getText().ToLower().contains(sVerificationObjectValue))
                                    //    return true;
                                }
                                if (!isEventSuccessful)
                                {
                                    strErrMsg_AppLib = "'" + sVerificationObjectValue + "' is not present on  the UI.";
                                }
                                break;
                        }
                    }
                    else
                    {
                        throw new RuntimeException("Element " + xpathDevicesHolder + "[" + i + "] is not found on page.");
                    }
                }
                if (strErrMsg_AppLib.equals(""))
                {
                    isEventSuccessful = true;
                }
                else
                {
                    throw new RuntimeException("Following Users' " + sVerificationObjectName + " is/are not displayed or is/are not correct: <br/>" + sVerificationObjectValue + " - " + strErrMsg_AppLib);
                }

            }
            else
            {
                isEventSuccessful = false;
                strErrMsg_AppLib = "Users page is not displayed";
                return isEventSuccessful;
            }
        }
        catch (RuntimeException e)
        {
            isEventSuccessful = false;
            strErrMsg_AppLib = "VerifynUsersPage---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return isEventSuccessful;
    }
	
	 
    //Open details page of any application according to the options given
    //
    //<!--Created By : Mandeep Mann-->
    //@param strAppOption It takes values : first and appname
    //@param strValue For strAppOption= first, it should be "1"; For strAppOption=appname, it should be the name of application as displayed on the applications table.
    //@return True or False
    //

    public final boolean SelectApplication(String strAppOption)
    {
        return SelectApplication(strAppOption, "1");
    }

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public bool SelectApplication(string strAppOption, string strValue = "1")
    public final boolean SelectApplication(String strAppOption, String strValue)
    {
        boolean flag = false;
        String strAppName = "", xpathAppsHolder = "", AppNameLink = "";
        strErrMsg_AppLib = "";
        //WebElement element = null, childElement= null;

        try
        {
            if (PerformAction("eleApplicationsHeader", Action.WaitForElement))
            {
                //Verifying some apps are displayed for the applied filter
                if (GetTextOrValue("class=message", "text").contains("deviceConnect currently has no configured applications.")) //Check if there are applications uploaded to system.
                {
                    throw new RuntimeException("deviceConnect currently has no configured applications.");
                }

                // cases for opening details page of an Application with given specification i.e. strAppOption
                switch (strAppOption)
                {
                    case "first":
                        AppNameLink = dicOR.get("eleAppNameAppTable").replace("__APP_INDEX__", "1");
                        //else if (dicCommon["BrowserName"].ToLower().Equals("ie"))
                        //    AppNameLink = "css=.card-detail-link";
                        break;

                    case "appame":
                        AppNameLink = dicOR.get("eleAppName_AppsPage").replace("__APPNAME__", strValue);
                        break;
                }

                //Get app name and put it to dicOutput
                strAppName = GetTextOrValue(AppNameLink, "text");
                if (!AddToDictionary(dicOutput, "selectedAppName", strAppName))
                {
                    throw new RuntimeException("SelectApplication -- Unable to put appName to dicOutput dictionary.");
                }

                //Click on app name and verify correct app details page is opened.
                if ( ! strAppName.equals(""))
                {
                    flag = PerformAction(AppNameLink, Action.Click);
                    if (flag)
                    {
                        flag = PerformAction("eleAppNameDisplay", Action.WaitForElement);
                        if (flag)
                        {
                            flag = GetTextOrValue("eleAppNameDisplay", "text").equals(strAppName);
                            if (!flag)
                            {
                                throw new RuntimeException("Correct App details page is not opened for app: " + strAppName + ".");
                            }
                        }
                        else
                        {
                            throw new RuntimeException("app details page not opened after clicking on app name link for app : " + strAppName + ".");
                        }
                    }
                    else
                    {
                        throw new RuntimeException("Could not click  on app name link (cards view on apps page) for app : " + strAppName + " view.");
                    }
                }
                else
                {
                    throw new RuntimeException("Could not find any app with the given parameters.");
                }
            }
            else
            {
                throw new RuntimeException("apps page is not displayed");
            }
        }
        catch (RuntimeException e)
        {
            flag = false;
            strErrMsg_AppLib = "SelectApplication---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return flag;
    }

     
    //This function verifies All Builds section
    //
    //<!--Created By : Mandeep Mann-->
    //@param sVerificationObjectName
    //@param sVerificationObjectValue
    //@return 
    //

    public final boolean verifyOnAllBuildsSection(String sVerificationObjectName)
    {
        return verifyOnAllBuildsSection(sVerificationObjectName, "");
    }

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public bool verifyOnAllBuildsSection(string sVerificationObjectName, string sVerificationObjectValue = "")
    public final boolean verifyOnAllBuildsSection(String sVerificationObjectName, String sVerificationObjectValue)
    {
        boolean flag = false;
        String TableRows_Xpath = "";
        int rowCount = 0;
        int columnsCount = 0;
        String errorColumnIndex = "", errorRows = "";
        strErrMsg_AppLib = "";

        try
        {
            if (PerformAction(dicOR.get("eleEmbeddedTableHeaders").replace("__HEADER__", "All Builds"), Action.isDisplayed)) // check if applications page is displayed
            {
                flag = true;
            }
            else
            {
                throw new RuntimeException("All Builds section is not displayed.");
            }

            //// If no apps are displayed at the moment then return true and exit. Also, write to logs file that there were no applications
            //if (GetTextOrValue("class=message", "text").contains("deviceConnect currently has no configured applications."))   //Check if there are applications uploaded to system.                
            //    throw new Exception("deviceConnect currently has no configured applications.");

            //If applications page is open, then proceed further and verify that for all apps, the sVerificationObjectName has sVerificationObjectValue as value
            TableRows_Xpath = dicOR.get("eleAllBuildsRows_AllBuilds");
            rowCount = getelementCount(TableRows_Xpath); // Count of all the rows displayed at the moment;
            if (rowCount != 0)
            {
                switch (sVerificationObjectName)
                {
                    case "cellvalues":
                        for (int i = 1; i <= rowCount; i++) //For each row, --
                        {
                            errorColumnIndex = "";
                            columnsCount = getelementCount(TableRows_Xpath + "[" + i + "]//td"); //--Get the number of columns.
                            if (columnsCount != 0) // If column count is 0, then put to error message that no columns exist for this particular row
                            {
                                for (int j = 1; j <= columnsCount; j++) // Iterate over all the columns of this row--
                                {
                                    if ((GetTextOrValue(TableRows_Xpath + "[" + i + "]//td[" + j + "]", "text")).isEmpty()) //--and verify if any cell is empty or white space
                                    {
                                        errorColumnIndex = errorColumnIndex + j + ",";
                                    }
                                }
                                if ( ! errorColumnIndex.equals("")) //Check how to replace __AND__ with new line operator to display new line in report.
                                {
                                    strErrMsg_AppLib = strErrMsg_AppLib + " _ AND_ Column numbers  " + errorColumnIndex + " are empty for app at row number: " + i;
                                }
                            }
                            else
                            {
                                strErrMsg_AppLib = strErrMsg_AppLib + "_AND_ No columns exist for row number  " + i;
                            }
                        }
                        if ( ! strErrMsg_AppLib.equals(""))
                        {
                            throw new RuntimeException(strErrMsg_AppLib);
                        }
                        break;

                    case "deletebutton":
                        //for (int i = 1; i <= rowCount; i++)                                           //For each row, --
                        //{

                        //    columnsCount = getelementCount(TableRows_Xpath + "[" + i + "]//td");      //--Get the number of columns.
                        //    if (columnsCount != 0)                                                    // If column count is 0, then put to error message that no columns exist for this particular row
                        //    {
                        for (int j = 1; j <= rowCount; j++) // Iterate over all the columns of this row--
                        {
                            errorColumnIndex = "";
                            flag = PerformAction(dicOR.get("eleInstallAppDropdown") + "[" + j + "]", Action.Click);
                            if (flag)
                            {
                                if (sVerificationObjectValue.toLowerCase().equals("disabled"))
                                {
                                    flag = !PerformAction(dicOR.get("eleDeleteOption_AppPage") + "[" + j + "]", Action.isDisplayed);
                                }
                                else if (sVerificationObjectValue.toLowerCase().equals("enabled"))
                                {
                                    flag = PerformAction(dicOR.get("eleDeleteOption_AppPage") + "[" + j + "]", Action.isDisplayed);
                                }

                                if (!flag)
                                {
                                    errorColumnIndex = errorColumnIndex + ", " + j;
                                }
                            }
                            else
                            {
                                errorColumnIndex = errorColumnIndex + ", " + j;
                            }
                        }

                        if ( ! errorColumnIndex.equals(""))
                        {
                            strErrMsg_AppLib = "Delete button is not " + sVerificationObjectValue + " for row numbers : " + errorColumnIndex;
                        }
                        //    }
                        //    else
                        //        strErrMsg_AppLib ="No columns exist for row number  " + i;
                        //}
                        if ( ! strErrMsg_AppLib.equals(""))
                        {
                            throw new RuntimeException(strErrMsg_AppLib);
                        }
                        break;
                }
            }
            else
            {
                throw new RuntimeException("No rows found on All Builds section under application details page.");
            }
        }
        catch (RuntimeException e)
        {
            flag = false;
            strErrMsg_AppLib = "verifyOnAllBuildsSection---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
            //writeToLog(strErrMsg_AppLib);
        }
        return flag;
    }

     
    //This function verifies the data under specified column is sorted correctly or not : ascending/descending
    //The columns containing any special character other than '-' is not handled.
    //
    //<!--Created By : Vinita Mahajan-->
    //@param TableContainerXpath Exact Identification of Table container on whose columns you need to perform sorting.
    //@param columnIndex Index of the columns which need to be sorted
    //@param SortByASCorDESC Expected type of sorting - Ascending : By Default Value, 'descending' value
    //@param NumericComparison If the Column values contains numeric values
    //@param rowElements name of element in OR containing xpath of the column elements whose sorting needs to be verified.
    //@return Returns boolean value : Data is sorted correctly or not
    //

    public final boolean VerifySortingAsc(String TableContainerXpath, int columnIndex, String SortByASCorDESC, String NumericComparison)
    {
        return VerifySortingAsc(TableContainerXpath, columnIndex, SortByASCorDESC, NumericComparison, "");
    }

    public final boolean VerifySortingAsc(String TableContainerXpath, int columnIndex, String SortByASCorDESC)
    {
        return VerifySortingAsc(TableContainerXpath, columnIndex, SortByASCorDESC, "no", "");
    }

    public final boolean VerifySortingAsc(String TableContainerXpath, int columnIndex)
    {
        return VerifySortingAsc(TableContainerXpath, columnIndex, "ascending", "no", "");
    }

    // TODO OWN : to be implemented (Vinita)
    public final boolean VerifySortingAsc(String TableContainerXpath, int columnIndex, String SortByASCorDESC, String NumericComparison, String rowElements)
    {
        List<WebElement> TableStatus = new ArrayList<WebElement>();
        List<String> ListDisplayed = new ArrayList<String>();
        List<String> ListSorted = new ArrayList<String>();
        List<String> ListSorted_int = new ArrayList<String>();
        boolean flag = false;
        strErrMsg_AppLib = "";

        try
        {
        	/*
            if (rowElements.equals(""))
            {
                TableStatus = driver.findElements(By.xpath(TableContainerXpath + "//tr"));
                for (int i = 1; i < TableStatus.size(); i++)
                {
                    ListDisplayed.add(driver.findElement(By.xpath(TableContainerXpath + "//tr[" + (new Integer(i)).toString() + "]/td[" + (new Integer(columnIndex)).toString() + "]")).getText());
                }
            }
            else
            {
                TableStatus = getelementsList(dicOR.get(rowElements));
                for (int i = 1; i < TableStatus.size(); i++)
                {
                    ListDisplayed.add(TableStatus.get(i).getText());
                }
            }

            if (SortByASCorDESC.toLowerCase().equals("") || SortByASCorDESC.toLowerCase().equals("ascending"))
            {
                if (NumericComparison.toLowerCase().equals("yes"))
                {
                    for (String listdis : ListDisplayed)
                    {
                        if (listdis.equals("-"))
                        {
                            ListSorted_int.add(listdis.replace("-", "0"));
                        }
                        else
                        {
                            ListSorted_int.add(listdis);
                        }
                    }
//C# TO JAVA CONVERTER TODO TASK: There is no Java equivalent to LINQ queries:
                    ListSorted = ListSorted_int.OrderBy(q -> Integer.parseInt(q)).<String>ToList();
                }
                else
                {
//C# TO JAVA CONVERTER TODO TASK: There is no Java equivalent to LINQ queries:
                    ListSorted = ListDisplayed.OrderBy(q -> q).ToList();
                }

                if (ListSorted.SequenceEqual(ListSorted_int))
                {
                    flag = true;
                }
                else
                {
                    strErrMsg_AppLib = "List is not sorted correctly.";
                    flag = false;
                }

            }
            else if (SortByASCorDESC.toLowerCase().equals("descending"))
            {
                if (NumericComparison.toLowerCase().equals("yes"))
                {
                    for (String listdis : ListDisplayed)
                    {
                        if (listdis.equals("-"))
                        {
                            ListSorted_int.add(listdis.replace("-", "0"));
                        }
                        else
                        {
                            ListSorted_int.add(listdis);
                        }
                    }
//C# TO JAVA CONVERTER TODO TASK: There is no Java equivalent to LINQ queries:
                    ListSorted = ListSorted_int.OrderByDescending(q -> Integer.parseInt(q)).<String>ToList();
                }
                else
                {
//C# TO JAVA CONVERTER TODO TASK: There is no Java equivalent to LINQ queries:
                    ListSorted = ListDisplayed.OrderByDescending(q -> q).ToList();
                }

                if (ListSorted.SequenceEqual(ListSorted_int))
                {
                    flag = true;
                }
                else
                {
                    strErrMsg_AppLib = "List is not sorted correctly.";
                    flag = false;
                }
            }
            if (NumericComparison.equals("no"))
            {
                if (ListSorted.SequenceEqual(ListDisplayed))
                {
                    flag = true;
                }
                else
                {
                    strErrMsg_AppLib = "List is not sorted correctly.";
                    flag = false;
                }
            }*/
        }
        catch (RuntimeException e)
        {
            flag = false;
            strErrMsg_AppLib = "VerifySortingAsc---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return flag;
    }
     
    //Verifies if there is warning message displayed on any page like applications, devices, reservations index page.
    //
    //<example>isEventSuccessful = !VerifyNoRowsWarningOnTable() && strErrMsg_AppLib.Equals("No warning message displayed on table.");
    //<p>This example verifies that there is not warning message on applications index page.</p></example>
    //@return True if correct warning is displayed, otherwise False
    //
    public final boolean VerifyNoRowsWarningOnTable()
    {
        boolean flag = false;
        strErrMsg_AppLib = "";
        String warningMessage = "";

        try
        {
            flag = PerformAction("class=message", Action.isDisplayed);
            if (flag)
            {
                warningMessage = GetTextOrValue("class=message", "text");
                flag = warningMessage.startsWith("deviceConnect currently has no configured");
                if (!flag)
                {
                    throw new RuntimeException("Warning message is incorrect. It is : '" + warningMessage + "'. ");
                }
            }
            else if (PerformAction("class=message", Action.isNotDisplayed))
            {
                strErrMsg_AppLib = "No warning message displayed on table.";
            }
        }
        catch (RuntimeException e)
        {
            strErrMsg_AppLib = "VerifyNoRowsWarningOnTable---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return flag;
    }

    /**
     *  This functions returns the battery status of the device. [Battery status text]
     *  This function is internally called in VerifyOnDeviceDetailsPage() - Value cannot be null, expected value need to be used along with contains__
     * @return Returns boolean and string value
     * @author vinitam
     */
    // TODO OWN : To be implemented (Find equivalent for Tuple return type)
    /*public final Pair<Boolean, String> GetBatteryStatus()
    {
        boolean flag = false;
        strErrMsg_AppLib = "";
        String batteryStatus = "";

        try
        {
            if (PerformAction(dicOR.get("deviceName_detailsPage"), Action.isDisplayed)) // check if device details page is displayed
            {
                flag = PerformAction("eleBatteryStatusIcon", Action.MouseHover);
                if (flag)
                {
                    flag = PerformAction("eleBatteryStatusValue", Action.isDisplayed);
                    if (flag)
                    {
                        batteryStatus = GetTextOrValue("eleBatteryStatusValue", "text");
                        if (batteryStatus.isEmpty())
                        {
                            flag = false;
                        }
                    }
                    else
                    {
                        strErrMsg_AppLib = "Battery Status tooltip is not displayed.";
                    }
                }
                else
                {
                    strErrMsg_AppLib = "Not able to perform MouseHover on Battery Icon.";
                }
            }
            else
            {
                throw new RuntimeException("Device Details page not displayed.");
            }
            //driver.FindElement(By.XPath("//div[@class='tooltip fade bottom in']"));
        }
        catch (RuntimeException e)
        {
            flag = false;
            strErrMsg_AppLib = "GetBatteryStatus---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return Pair(flag, batteryStatus);//Pair<boolean.class[flag], String.class[batteryStatus]>; //Pair<Boolean, String>(flag, batteryStatus);
    	}*/

     
    //This function creates reservation of type - Weekly
    //NOT COMPLETE YET 
    //<!--Created By : Vinita Mahajan-->
    //
    //@param weekday /
    //@param deviceName
    //@param everyWeekValue
    //@return 
    //

    public final boolean RSVD_CreateReservationWeekly(String[] weekday, String startdate, String enddate, String startTimeFormat, String endTimeFormat, String startTime, String endTime, String deviceName, String everyWeekValue)
    {
        return RSVD_CreateReservationWeekly(weekday, startdate, enddate, startTimeFormat, endTimeFormat, startTime, endTime, deviceName, everyWeekValue, false);
    }

    public final boolean RSVD_CreateReservationWeekly(String[] weekday, String startdate, String enddate, String startTimeFormat, String endTimeFormat, String startTime, String endTime, String deviceName)
    {
        return RSVD_CreateReservationWeekly(weekday, startdate, enddate, startTimeFormat, endTimeFormat, startTime, endTime, deviceName, "1", false);
    }

    public final boolean RSVD_CreateReservationWeekly(String[] weekday, String startdate, String enddate, String startTimeFormat, String endTimeFormat, String startTime, String endTime)
    {
        return RSVD_CreateReservationWeekly(weekday, startdate, enddate, startTimeFormat, endTimeFormat, startTime, endTime, "1", "1", false);
    }

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public bool RSVD_CreateReservationWeekly(string[] weekday, string startdate, string enddate, string startTimeFormat, string endTimeFormat, string startTime, string endTime, string deviceName = "1", string everyWeekValue = "1", bool SelectFromCalendar = false)
    public final boolean RSVD_CreateReservationWeekly(String[] weekday, String startdate, String enddate, String startTimeFormat, String endTimeFormat, String startTime, String endTime, String deviceName, String everyWeekValue, boolean SelectFromCalendar)
    //, DateAndTime startdate, DateAndTime enddate, Tuple<string, string> startTime, Tuple<string, string> endTime, string deviceName = "1")
    {
        if (weekday == null)
        {
            throw new IllegalArgumentException("weekday");
        }
        boolean flag = false;
        strErrMsg_AppLib = "";
        String xPath, xPathReplaced;
        try
        {
            if (PerformAction("eleReservationsHeader", Action.isDisplayed))
            {
                flag = PerformAction("btnCreateReservation", Action.Click);
                if (flag)
                {
                    flag = PerformAction("eleCreateRsrvtnHeader", Action.isDisplayed);
                    if (flag)
                    {
                        flag = PerformAction("drpDevice_CreateReservation", Action.Click);
                        if (flag)
                        {
                            flag = PerformAction("drpDevicesValues_CreateReservation", Action.Click);
                            if (flag)
                            {
                                flag = RSVD_SelectReservationType("Weekly");
                                if (flag)
                                {
                                    flag = PerformAction("eleEveryWeekInput_CreateReservation", Action.Type, everyWeekValue);
                                    if (flag)
                                    {
                                        flag = RSVD_SelectWeekDay(weekday);
                                        if (flag)
                                        {
                                            flag = RSVD_SelectReservationDate(startdate, enddate, "Current", "Current", SelectFromCalendar);
                                            if (flag)
                                            {
                                                flag = RSVD_SelectReservationTime(startTimeFormat, endTimeFormat, startTime, endTime);
                                                if (!flag)
                                                {
                                                    strErrMsg_AppLib = "Not able to select the start time and end time.";
                                                }
                                            }
                                        }
                                        else
                                        {
                                            strErrMsg_AppLib = "Not able to select the weekday/s.";
                                        }
                                    }
                                    else
                                    {
                                        strErrMsg_AppLib = "Not able to input value in Every n Weeks field.";
                                    }
                                }
                                else
                                {
                                    strErrMsg_AppLib = strErrMsg_AppLib + "Not able to select Weekly from Repeats dropdown.";
                                }
                            }
                        }
                    }
                    else
                    {
                        strErrMsg_AppLib = "New Reservation page is not displayed.";
                    }
                }
                else
                {
                    strErrMsg_AppLib = "Not able to click on Create Reservation button.";
                }
            }
            else
            {
                throw new RuntimeException("Reservation Page is not displayed.");
            }
        }
        catch (RuntimeException e)
        {
            flag = false;
            strErrMsg_AppLib = "RSVD_CreateReservationWeekly---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return flag;
    }

     
    //This function creates reservation of type - Never
    //<!--Created By : Mandeep Kaur-->
    //
    //@param deviceName
    //@return 
    //

    public final boolean RSVD_CreateReservationNever(String deviceName, String startDate, String startTime, String endDate)
    {
        return RSVD_CreateReservationNever(deviceName, startDate, startTime, endDate, "");
    }

    public final boolean RSVD_CreateReservationNever(String deviceName, String startDate, String startTime)
    {
        return RSVD_CreateReservationNever(deviceName, startDate, startTime, "", "");
    }

    public final boolean RSVD_CreateReservationNever(String deviceName, String startDate)
    {
        return RSVD_CreateReservationNever(deviceName, startDate, "", "", "");
    }

    public final boolean RSVD_CreateReservationNever(String deviceName)
    {
        return RSVD_CreateReservationNever(deviceName, "", "", "", "");
    }

    public final boolean RSVD_CreateReservationNever()
    {
        return RSVD_CreateReservationNever("1", "", "", "", "");
    }

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public bool RSVD_CreateReservationNever(string deviceName = "1", string startDate = "", string startTime = "", string endDate = "", string endTime = "")
    public final boolean RSVD_CreateReservationNever(String deviceName, String startDate, String startTime, String endDate, String endTime)
    {
        boolean flag = false;
        strErrMsg_AppLib = "";
        try
        {
            if (PerformAction("eleReservationsHeader", Action.isDisplayed)) // check if usr is on the reservations page , otherwise skip the next steps
            {
                flag = PerformAction("btnCreateReservation", Action.Click); // Click on 'Create' reservation button
                if (flag)
                {
                    flag = PerformAction("eleCreateRsrvtnHeader", Action.isDisplayed); //Check if user is navigated to create reservation page, otherwise throw exception and escape the function
                    if (flag)
                    {
                        flag = RSVD_SelectDevice(deviceName); // Select the first device in 'Devices' dropdown.
                        if (flag)
                        {
                            //flag = RSVD_SelectReservationDateTime(startDate, startTime, endDate,  endTime);
                            //if (flag)
                            //{ 

                            //}
                            //else
                            //    throw new Exception(strErrMsg_AppLib);
                        }
                        else
                        {
                            throw new RuntimeException(strErrMsg_AppLib);
                        }
                    }
                    else
                    {
                        throw new RuntimeException("New Reservation page is not displayed.");
                    }
                }
                else
                {
                    throw new RuntimeException("Not able to click on Create Reservation button.");
                }
            }
            else
            {
                throw new RuntimeException("Reservation Page is not displayed.");
            }
        }
        catch (RuntimeException e)
        {
            flag = false;
            strErrMsg_AppLib = "RSVD_CreateReservationWeekly---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return flag;
    }

     
    //This function creates reservation of type - Never
    //<!--Created By : Mandeep Kaur-->
    //
    //@param deviceName
    //@return 
    //

    public final boolean RSVD_CreateReservationDaily()
    {
        return RSVD_CreateReservationDaily("1");
    }

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public bool RSVD_CreateReservationDaily(string deviceName = "1")
    public final boolean RSVD_CreateReservationDaily(String deviceName)
    {
        boolean flag = false;
        strErrMsg_AppLib = "";
        try
        {
            if (PerformAction("eleReservationsHeader", Action.isDisplayed)) // check if usr is on the reservations page , otherwise skip the next steps
            {
                flag = PerformAction("btnCreateReservation", Action.Click); // Click on 'Create' reservation button
                if (flag)
                {
                    flag = PerformAction("eleCreateRsrvtnHeader", Action.isDisplayed); //Check if user is navigated to create reservation page, otherwise throw exception and escape the function
                    if (flag)
                    {
                        flag = RSVD_SelectDevice(deviceName); // Select the first device in 'Devices' dropdown.
                        if (flag)
                        {
                            //flag = 
                        }
                        else
                        {
                            throw new RuntimeException(strErrMsg_AppLib);
                        }
                    }
                    else
                    {
                        throw new RuntimeException("New Reservation page is not displayed.");
                    }
                }
                else
                {
                    throw new RuntimeException("Not able to click on Create Reservation button.");
                }
            }
            else
            {
                throw new RuntimeException("Reservation Page is not displayed.");
            }
        }
        catch (RuntimeException e)
        {
            flag = false;
            strErrMsg_AppLib = "RSVD_CreateReservationWeekly---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return flag;
    }

     
    //This function Selects the Device from the Devices dropdown on Create New Reservation page
    //This also verifies that the selected deviceName is displayed on the device dropdown after selection
    //<!--Created By : Vinita Mahajan-->
    //
    //@param deviceName deviceName = Name of the device OR deviceName not specified then selects the first device from dropdown.
    //@return Returns true if able to select device from devices dropdown
    //

    public final boolean RSVD_SelectDevice()
    {
        return RSVD_SelectDevice("1");
    }

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public bool RSVD_SelectDevice(string deviceName = "1")
    public final boolean RSVD_SelectDevice(String deviceName)
    {
        boolean flag = false;
        String XPath;
        String XPathValue;
        strErrMsg_AppLib = "";
        String dName;
        try
        {
            if (PerformAction("eleDeviceDrpArrow_CreateReservation", Action.WaitForElement, "2"))
            {
                if (PerformAction("eleDeviceDrpArrow_CreateReservation", Action.Click))
                {
                    XPath = getValueFromDictionary(dicOR, "eleDevicesOption_CreateReservation");
                    XPathValue = XPath.replace("__DEVICENAME__", deviceName.equals("1") ? "[1]" : deviceName);
                    flag = PerformAction(XPathValue, Action.Click);
                    if (flag)
                    {
                        dName = GetTextOrValue("drpDevice_CreateReservation", "text");
                        if (dName.isEmpty())
                        {
                            flag = false;
                            strErrMsg_AppLib = "Selected Device Name is not displayed on Devices dropdown.";
                        }
                        else
                        {
                            AddToDictionary(dicOutput, "ReservedDeviceName", dName);
                        }
                    }
                    else
                    {
                        strErrMsg_AppLib = "Not Able to select the Device : " + deviceName + " from Devices dropdown.";
                    }
                }
                else
                {
                    strErrMsg_AppLib = "Not able to click on Devices dropdown.";
                }
            }
            else
            {
                strErrMsg_AppLib = "Devices dropdown is not displayed.";
            }
        }
        catch (RuntimeException e)
        {
            flag = false;
            strErrMsg_AppLib = "RSVD_SelectDevice---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return flag;
    }

     
    //
    // This function Selects Reservation Type from Repeats dropdown on Create New Reservation page
    // This also verifies that the selected Reservation Type is displayed on the Repeats dropdown after selection
    //<!--Created By : Vinita Mahajan-->
    //
    //@param reservationType Reservation Type by default accepts Never if not specified else need to specified exactlty as per UI
    //@return 
    //

    public final boolean RSVD_SelectReservationType()
    {
        return RSVD_SelectReservationType("Never");
    }

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public bool RSVD_SelectReservationType(string reservationType = "Never")
    public final boolean RSVD_SelectReservationType(String reservationType)
    {
        boolean flag = false;
        String XPath;
        String XPathValue;
        strErrMsg_AppLib = "";
        String type;
        try
        {
            if (PerformAction("eleRepeatsDrpArrow_CreateReservation", Action.WaitForElement, "2"))
            {
                if (PerformAction("eleRepeatsDrpArrow_CreateReservation", Action.Click))
                {
                    XPath = getValueFromDictionary(dicOR, "eleRepeatsOption_CreateReservation");
                    XPathValue = XPath.replace("__TYPE__", reservationType);
                    flag = PerformAction(XPathValue, Action.Click);
                    if (flag)
                    {
                        type = GetTextOrValue("drpRepeats_CreateReservation", "text");
                        if (type.isEmpty())
                        {
                            flag = false;
                            strErrMsg_AppLib = "Selected Reservation Type is not displayed on Repeats dropdown.";
                        }
                        else
                        {
                            AddToDictionary(dicOutput, "ReserveType", reservationType);
                        }
                    }
                    else
                    {
                        strErrMsg_AppLib = "Not Able to select the Reservation Type : " + reservationType + " from Repeats dropdown.";
                    }
                }
                else
                {
                    strErrMsg_AppLib = "Not able to click on Repeats dropdown.";
                }
            }
            else
            {
                strErrMsg_AppLib = "Repeats dropdown is not displayed.";
            }
        }
        catch (RuntimeException e)
        {
            flag = false;
            strErrMsg_AppLib = "RSVD_SelectReservationType---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return flag;
    }

    //button[contains(@class,'weekly-day-option') and @data-value='SU']
     
    //This function selects the week days for creating reservation of type weekly
    //<!--Created By : Vinita Mahajan-->
    //
    //@param weekDay Value for weekday array should be : MO,TU,WE,TH,FR,SA
    //@return 
    //
    public final boolean RSVD_SelectWeekDay(String[] weekDay)
    {
        boolean flag = false;
        strErrMsg_AppLib = "";
        String WeekDayXPath, ClassName;
        String weekDaysAlreadySelected = "";
        try
        {
            for (int i = 1; i <= weekDay.length; i++)
            {
                WeekDayXPath = getValueFromDictAndReplace(dicOR, "btnWeekDay_CreateReservation", "__WEEKDAY__", weekDay[i]);
                if (WeekDayXPath.isEmpty())
                {
                    ClassName = getAttribute(WeekDayXPath, "class");
                    if (!ClassName.isEmpty())
                    {
                        if (!ClassName.contains("active"))
                        {
                            flag = PerformAction(WeekDayXPath, Action.Click);
                            if (!flag)
                            {
                                strErrMsg_AppLib = "Not able to select the button for WeekDay : " + weekDay[i];
                            }
                        }
                        else
                        {
                            weekDaysAlreadySelected = weekDaysAlreadySelected + weekDay[i];
                        }
                    }
                    else
                    {
                        throw new RuntimeException("Not able to get the ClassName for the Weekday XPath or is returned as null.");
                    }
                }
                else
                {
                    throw new RuntimeException("Not able to replace the WeekDay Value in WeekDayXPath.");
                }
            }
        }
        catch (RuntimeException e)
        {
            flag = false;
            strErrMsg_AppLib = "RSVD_SelectWeekDay---" + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return flag;
    }

     
    //This function sets the Reservation Time. Like Say : 2:00 PM or 3:30 AM
    //<!--Created By : Vinita Mahajan-->
    //
    //@param startTimeFormat This is the value AM or PM for start time
    //@param endTimeFormat This is the value for AM or PM for end time
    //@param startTime Start Time to be selected from the Start Time dropdown to be set to
    //@param endTime End Time to be selected from the End Time dropdown to be set to
    //@return Returns Flag boolean value
    //
    public final boolean RSVD_SelectReservationTime(String startTimeFormat, String endTimeFormat, String startTime, String endTime)
    {
        boolean flag = false;
        strErrMsg_AppLib = "";
        int startTimeValue = 0;
        int endTimeValue = 0;
        String startTimeValueXPath = "";
        String endTimeValueXPath = "";

        try
        {
            if (PerformAction("eleProposedWarning_CreateReservation", Action.isDisplayed))
            {
                startTime = FetchDateTime("time");
                startTimeValue = Integer.parseInt(startTime) + 1;
                startTime = (new Integer(startTimeValue)).toString();
                endTime = FetchDateTime("time");
                endTimeValue = Integer.parseInt(endTime) + 2;
                endTime = (new Integer(endTimeValue)).toString();
            }

            if (PerformAction("eleStartTimeDrpArrow_CreateReservation", Action.Click))
            {
                if (!startTime.isEmpty())
                {
                    startTimeValueXPath = getValueFromDictAndReplace(dicOR, "eleStartTime_CreateReservation", "__TIME__", startTime);
                    flag = PerformAction(startTimeValueXPath, Action.Click);
                    if (flag)
                    {
                        if (!startTimeFormat.isEmpty())
                        {
                            String element = getValueFromDictAndReplace(dicOR, "rbtnstartTimeFormat_CreateReservation", "__FORMAT__", startTimeFormat);
                            flag = PerformAction(element, Action.Click);
                            if (!flag)
                            {
                                strErrMsg_AppLib = "Not able to select the AM/PM radio button : " + startTimeFormat;
                            }
                        }
                        else
                        {
                            strErrMsg_AppLib = "startTimeFormat is null or empty.";
                        }
                    }
                    else
                    {
                        strErrMsg_AppLib = "Not able to select the desired time value from Start Time dropdown.";
                    }
                }
                else
                {
                    strErrMsg_AppLib = "StartTime is null or empty.";
                }
            }
            else
            {
                strErrMsg_AppLib = "Not able to open the Start Time Dropdown.";
            }

            if (PerformAction("eleEndTimeDrpArrow_CreateReservation", Action.Click))
            {
                if (!endTime.isEmpty())
                {
                    endTimeValueXPath = getValueFromDictAndReplace(dicOR, "eleEndTime_CreateReservation", "__TIME__", endTime);
                    flag = PerformAction(endTimeValueXPath, Action.Click);
                    if (flag)
                    {
                        if (!startTimeFormat.isEmpty())
                        {
                            String element = getValueFromDictAndReplace(dicOR, "rbtnendTimeFormat_CreateReservation", "__FORMAT__", endTimeFormat);
                            flag = PerformAction(element, Action.Click);
                            if (!flag)
                            {
                                strErrMsg_AppLib = "Not able to select the AM/PM radio button : " + endTimeFormat;
                            }
                        }
                        else
                        {
                            strErrMsg_AppLib = "endTimeFormat is null or empty.";
                        }
                    }
                    else
                    {
                        strErrMsg_AppLib = "Not able to select the desired time value from End Time dropdown.";
                    }
                }
                else
                {
                    strErrMsg_AppLib = "EndTime is null or empty.";
                }
            }
            else
            {
                strErrMsg_AppLib = "Not able to open the End Time Dropdown.";
            }
        }
        catch (RuntimeException e)
        {
            flag = false;
            strErrMsg_AppLib = "RSVD_SelectReservationTime--- " + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return flag;
    }


     
    //This function sets the start date and end date on Create Reservation page
    //This function can set the values in the textbox as well as select from the calendar
    //
    //<!--Created By : Vinita Mahajan-->
    //@param startdate startdate : Value will be only a date. Say For Example : Only 22 OR 24, if the iscalendar = true 
    //@param enddate endate : Value will be only a date. Say For Example : Only 22 OR 24, if the iscalendar = true. If the isCalendar value is false then need to send the value in MM/dd/yyyy
    //@param startMonth By Default the value will be Current : as for other months not implemented yet
    //@param endMonth By Default the value will be Current : as for other months not implemented yet
    //@param isCalendar By Default the value will be False : if User wants to select a date explicitly from calendar for the current month then need to set this to True
    //@return returns boolean value flag
    //

    public final boolean RSVD_SelectReservationDate(String startdate, String enddate, String startMonth, String endMonth)
    {
        return RSVD_SelectReservationDate(startdate, enddate, startMonth, endMonth, false);
    }

    public final boolean RSVD_SelectReservationDate(String startdate, String enddate, String startMonth)
    {
        return RSVD_SelectReservationDate(startdate, enddate, startMonth, "Current", false);
    }

    public final boolean RSVD_SelectReservationDate(String startdate, String enddate)
    {
        return RSVD_SelectReservationDate(startdate, enddate, "Current", "Current", false);
    }

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public bool RSVD_SelectReservationDate(string startdate, string enddate, string startMonth = "Current", string endMonth = "Current", bool isCalendar = false)
    public final boolean RSVD_SelectReservationDate(String startdate, String enddate, String startMonth, String endMonth, boolean isCalendar)
    {
        boolean flag = false;
        strErrMsg_AppLib = "";
        String startDateXPath = "", endDateXPath = "";

        try
        {
            if (isCalendar)
            {
                String startDateCalendar = getValueFromDictionary(dicOR, "eleStartDateCalendar_CreateReservation");
                if (!startDateCalendar.isEmpty())
                {
                    flag = PerformAction(startDateCalendar, Action.Click);
                    if (flag)
                    {
                        if ( ! startMonth.equals("Current"))
                        {
                            //Need to implement a fucntion and call here
                        }
                        startDateXPath = getValueFromDictAndReplace(dicOR, "btnDatetoSelectCalendar_Reseravation", "__DATE__", startdate);
                        if (!startDateXPath.isEmpty())
                        {
                            flag = PerformAction(startDateXPath, Action.Click);
                            if (flag)
                            {
                                String endDateCalendar = getValueFromDictionary(dicOR, "eleEndDateCalendar_CreateReservation");
                                if (!endDateCalendar.isEmpty())
                                {
                                    flag = PerformAction(endDateCalendar, Action.Click);
                                    if (flag)
                                    {
                                        if (endMonth.equals("Current"))
                                        {
                                            endDateXPath = getValueFromDictAndReplace(dicOR, "btnDatetoSelectCalendar_Reseravation", "__DATE__", enddate);
                                            if (!endDateXPath.isEmpty())
                                            {
                                                flag = PerformAction(endDateXPath, Action.Click);
                                                if (!flag)
                                                {
                                                    strErrMsg_AppLib = "Not able to select end date from the calendar.";
                                                }
                                            }
                                        }
                                        //else
                                        //{
                                        //    //Need to implement a function and call here
                                        //}
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        strErrMsg_AppLib = "Not able to click on startDateCalendar.";
                    }
                }
                else
                {
                    throw new RuntimeException("Not able to get the value of : 'eleStartDateCalendar_CreateReservation' from OR");
                }
            }
            else if ( ! startdate.equals("") && ! enddate.equals("")) //Note this date will be set only for current month
            {
                flag = PerformAction("txtStartDate_CreateReservation", startdate, Action.Type);
                if (flag)
                {
                    flag = PerformAction("txtEndDate_CreateReservation", enddate, Action.Type);
                }
            }
        }
        catch (RuntimeException e)
        {
            flag = false;
            strErrMsg_AppLib = "RSVD_SelectReservationDate--- " + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return flag;
    }

     
    //Function to check the checkboxes given in array passed and uncheck all other checkboxes of that kind.
    //
    //<!-- Created by : Mandeep Kaur -->
    //<!-- Last modified : 6/2/2015 by Mandeep Kaur-->
    //@param checkboxesToBeChecked Array of names of the checkboxes that need to be checked.
    //@param xpathOfChkBoxesInOR Key to the locator of the checkboxes in OR.xls
    //@return True or False
    //<example>selectCheckboxes_DI(new string[]{"Android"}, "chkPlatform_Device") : This will make the Android checkbox to be checked and iOS to be Unchecked.</example>
    //
    private boolean selectCheckboxes_DI(String[] checkboxesToBeChecked, String xpathOfChkBoxesInOR)
    {
        boolean flag = false, shouldBeChecked = false;
        strErrMsg_AppLib = "";
        String Xpath_checkboxGeneral = "", Xpath_checkbox = "", currentCheckboxText = "";
        String errorIndex = "";
        int chkBoxCount = 0;
        try
        {
            Xpath_checkboxGeneral = getValueFromDictionary(dicOR, xpathOfChkBoxesInOR); //Getting xpath of the checkboxes from Or
            chkBoxCount = getelementCount(Xpath_checkboxGeneral); //getting number of checkboxes of the same type (eg. all checkboxes under platform
            for (int i = 1; i <= chkBoxCount; i++)
            {
                flag = false; // Set flag to false for every iteration
                Xpath_checkbox = Xpath_checkboxGeneral + "[" + i + "]"; // Getting xpath of each checkbox by puting index to the general xpath
                currentCheckboxText = GetTextOrValue(Xpath_checkbox + "/..", "text"); //Getting text in front of the checkbox, i.e. text of the parent of the checkbox that has text associated with the checkbox
 
    			for(String chkName : checkboxesToBeChecked)
    			{
    				/*shouldBeChecked = p.matcher(chkName).matches();*/
    				shouldBeChecked = currentCheckboxText.startsWith(chkName);
    				if(shouldBeChecked)
    					break;
    			}
                    if (shouldBeChecked)
                    {
                    flag = PerformAction(Xpath_checkbox, Action.SelectCheckbox); //Check or leave checked if the checkbox text is passed int he function
                    //break;
                    }
                    else
                    {
                    flag = PerformAction(Xpath_checkbox, Action.DeSelectCheckbox); //Uncheck the checkbox if it is not there in the array passed to the function
                    //break;
                    }

    			
                //put checkbox index to list if there is any error while checking or unchecking the checkbox
                if (!flag)
                    errorIndex = errorIndex + ", " + i;
            }
            // If errorIndex is not empty then function is not pass and so, throw an exception including the errorIndex string containing the checkbox indices where check/uncheck could not be performed
            if ( ! errorIndex.equals(""))
                throw new RuntimeException("Could not check/uncheck checkboxes at indices : " + errorIndex);
       }
        catch (RuntimeException e)
        {
            flag = false;
            strErrMsg_AppLib = "selectPlatform_DI--- " + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return flag;
    }

     
    //Checks the checkboxes for the given platform(s), unchecks the ones not provided. Also, it verifies if the platform filter was applied properly or not
    //<p>If there are more than one values passed as input to function, then it does not verify that devices of only those platforms are present.</p>
    //
    //<!-- Created by : Mandeep Kaur -->
    //<!-- Last modified : 9/2/2015 by Mandeep Kaur, changed input parameter type from string array to string.-->
    //@param platform Comma separated names of the status(s) to be selected (eg. "Android,iOS" . Provide values in exact case.
    //@return True or False
    //
    public final boolean selectPlatform_DI(String platforms)
    {
        boolean flag = false;
        strErrMsg_AppLib = "";
        try
        {
            String[] platformsArray = platforms.split("[,]", -1);
            flag = PerformAction("chkPlatform_Devices", Action.WaitForElement, "5");
            if (flag)
            {
                flag = selectCheckboxes_DI(platformsArray, "chkPlatform_Devices"); // Call function to select the required platform checkboxes.
                if (flag && (platformsArray.length == 1)) // Verify that displayed devices are of given platform only when a single platform needs to be selected.
                {
                    flag = PerformAction("eleNoDevicesWarning_Devices", Action.WaitForElement, "5"); // If warning message showing that there are no devices matching the filter applied, then the function is pass.
                    if (!flag) // If some devices are displayed, then check that devices of only the given platform are displayed.
                    {
                        flag = VerifyDeviceDetailsInGridAndListView("deviceplatform", platformsArray[0], "list");
                        if (!flag)
                        {
                            throw new RuntimeException(strErrMsg_AppLib);
                        }
                    }
                }
                else if (!flag) // selectPlatform function failed then throw error.
                {
                    throw new RuntimeException(strErrMsg_AppLib);
                }
            }
            else
            {
                throw new RuntimeException("Platform checkboxes are not displayed on the page.");
            }
        }
        catch (RuntimeException e)
        {
            flag = false;
            strErrMsg_AppLib = "selectPlatform_DI--- " + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return flag;
    }
	
	 
    //Checks the checkboxes for the given status(s), unchecks the ones not provided. Also, it verifies if the status filter was applied properly or not
    //<p>If there are more than one values passed as input to function, then it does not verify that devices of only those statuses are present.</p>
    //
    //<!-- Created by : Mandeep Kaur -->
    //<!-- Last modified : 3/6/2015 by Mandeep Kaur, changed input parameter type from string array to string.-->
    //@param status Comma separated names of the status(s) to be selected (eg. "Available,Offline" . Provide values in exact case.
    //@return True or False
    //
    public final boolean selectStatus_DI(String status)
    {
        boolean flag = false;
        strErrMsg_AppLib = "";
        try
        {
            String[] statusesArray = status.split("[,]", -1);
           flag = PerformAction("chkStatus_Devices", Action.WaitForElement, "5");
            if (flag)
            {
                flag = selectCheckboxes_DI(statusesArray, "chkStatus_Devices");
                if (flag)
                {
                    status = status.replace("Disabled", "Disabled,Connected");
                    flag = VerifyMultipleValuesOfProperty_DI("Status", status);
                    if (!flag)
                    {
                        throw new RuntimeException(strErrMsg_AppLib);
                    }
                }
                else
                {
                    throw new RuntimeException(strErrMsg_AppLib);
                }
                //if (flag && (status.Length == 1)) // Verify that displayed devices are of given status only when a single platform needs to be selected.
                //{
                //    flag = PerformAction("eleNoDevicesWarning", Action.WaitForElement, "5"); // If warning message showing that there are no devices matching the filter applied, then the function is pass.
                //    if (!flag) // If some devices are displayed, then check that devices of only the given status are displayed.
                //    {
                //        flag = VerifyDeviceDetailsInGridAndListView("devicestatus", statusesArray[0], "list");
                //        if (!flag)
                //            throw new Exception(strErrMsg_AppLib);
                //    }
                //}
                //else if (!flag)  // selectPlatform function failed then throw error.
                //    throw new Exception(strErrMsg_AppLib);
            }
            else
            {
                throw new RuntimeException("Status checkboxes are not displayed on the page.");
            }
        }
        catch (RuntimeException e)
        {
            flag = false;
            strErrMsg_AppLib = "selectStatus_DI--- " + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return flag;
    }

     
    //Clicks on the required tab on top nav bar and verifies if the expected page element is loaded or not.
    //
    //@param TabName Name of page to be navigated to : "Devices", "Applications", "Reservations", "Users", "System"
    //@param expectedPageElement Locator of the 
    //@return 
    //
    protected final boolean navigateToNavBarPages(String TabName, String expectedPageElement)
    {
        boolean flag = false;
        strErrMsg_AppLib = "";
        try
        {
            flag = PerformAction(dicOR.get("eleTopNavTab").replace("__TABNAME__", TabName), Action.ClickUsingJS);
            if (flag)
            {
                flag = PerformAction(expectedPageElement, Action.WaitForElement);
                if (!flag)
                {
                    throw new RuntimeException("'" + TabName + "' page not loaded.");
                }
            }
            else
            {
                throw new RuntimeException("Could not click on '" + TabName + "' tab.");
            }
        }
        catch (RuntimeException e)
        {
            flag = false;
            strErrMsg_AppLib = "navigateToNavBarPages--- " + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return flag;
    }

     
    //Selects the select all checkbox and verifies that checkboxes in front of all the devices are also selected.
    //
    //<!--Created by : Mandeep Kaur-->
    //<!--Last updated : 12/2/2015 by Mandeep Kaur-->
    //@param devicesSelected List in which the names all selected devices are to be stored for further usage.
    //@return True or false
    //
    public final Object[] selectAllCheckboxAndVerify_DI()
    {
        boolean flag = false;
        strErrMsg_AppLib = "";
        //int devicesCount = 0;
        ArrayList<String> devicesSelected = new ArrayList<String>();
        //string strErrorIndex = "", strErrorIndexName = "", deviceName = "";
        Object[] values = new Object[2];
        try
        {
            //Select the select all checkbox
            flag = PerformAction("chkSelectAll_Devices", Action.SelectCheckbox);
            if (flag) // If it is selected then get the names of all the devices which are displayed and verify that the checkbox in front of each device got selected.
            {
            	Object[] objresult =  VerifyAllCheckedOrUnchecked_DI(Action.isSelected);
    			flag = (boolean) objresult[0];
    			devicesSelected = (ArrayList<String>) objresult[1];  // Warning not suppressed because the runtime will always return ArrayList of StringType
                if (!flag)
                    throw new RuntimeException(strErrMsg_AppLib);
            }
            else
                throw new RuntimeException("Could not select the select all checkbox.");
            values[0]= flag;
            values[1] = devicesSelected; 
        }
        catch (RuntimeException e)
        {
            flag = false;
            values[0]= flag;
            values[1] = devicesSelected; 
            strErrMsg_AppLib = "selectAllCheckboxAndVerify_DI--- " + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return values;
        
    }

    /** 
    //Verifies if all checkboxes in front of all the displayed devices are checked or not.
    //
    //@author mandeepm
    //@since 23/6/2015
    //@param devicesSelected List of string type in which names of all displayed de
    //@param checkStatusToVerify It takes values : Action.isSelected if it needs to be verfied that all checkboxes are selected or Action.isNotSelected to check that all checkboxes are not selected. 
    //@return True or False
    */
    public final  Object[] VerifyAllCheckedOrUnchecked_DI(String checkStatusToVerify)
    {
        boolean flag = false;
        strErrMsg_AppLib = "";
        int devicesCount = 0;
        ArrayList<String> devicesSelected = new ArrayList<String>();
        String strErrorIndex = "", strErrorIndexName = "", deviceName = "";
        try
        {
            devicesCount = getelementCount("eleDevicesHolderListView") - 1;
            if (devicesCount > 0) // If number of rows are obtained then check if the check-status of all the checkboxes match the given check status
            {
                for (int i = 1; i <= devicesCount; i++)
                {
                    if (!PerformAction(dicOR.get("chkDeviceName_Devices") + "[" + i + "]", checkStatusToVerify)) // If the checkbox is not selected/deselected then put the index to errorVariable
                    {
                        strErrorIndex = strErrorIndex + ", " + i;
                    }
                    deviceName = GetTextOrValue(dicOR.get("eleDeviceName_ListView").replace("__INDEX__", (new Integer(i)).toString()), "text");
                    devicesSelected.add(deviceName);
                    if (deviceName.equals(""))
                    {
                        strErrorIndexName = strErrorIndexName + ", " + i;
                    }
                }
                if ( ! strErrorIndex.equals(""))
                {
                    throw new RuntimeException("Checkbox is not in correct checked-state for device at index(s) : '" + strErrorIndex + "'.");
                }
                if ( ! strErrorIndexName.equals(""))
                {
                    throw new RuntimeException("Could not get name of device at index(s) : '" + strErrorIndexName + "'.");
                }
                flag = true;
            }
            else
            {
                throw new RuntimeException(strErrMsg_AppLib);
            }
        }
        catch (RuntimeException e)
        {
            flag = false;
            strErrMsg_AppLib = "VerifyAllCheckedOrUnchecked_DI--- " + " + " + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return new Object[] {flag,devicesSelected};
    }

     
    //Verifies that none of the element with xpath elementXpath has text elementText>
    //
    //<!--Created by : Mandeep Kaur 17/2/2015-->
    //@param elementXpath xpath of elements whose text needs to be verified.
    //@param elementText Text that needs to be verified
    //@return 
    //
    protected final boolean verifyElementWithTextNotPresentOnPage(String elementXpath, String elementText)
    {
        boolean flag = false;
        strErrMsg_AppLib = "";
        int elementsCount = 0;
        try
        {
            elementsCount = getelementCount(elementXpath); //Get number of elements with given xpath
            if (elementsCount != 0)
            {
                for (int i = 1; i <= elementsCount; i++)
                {
                    if (elementText.equals(GetTextOrValue(elementXpath + "[" + i + "]", "text")))
                    {
                        throw new RuntimeException("Element with text '" + elementText + "' exists on page. Element xpath is : '" + elementXpath + "[" + i + "]'");
                    }
                }
                flag = true; // Set flag to True if there is no element with such text.
            }
            else
            {
                throw new RuntimeException("Could not get number of elements with xpath '" + elementXpath + "'");
            }
        }
        catch (RuntimeException e)
        {
            flag = false;
            strErrMsg_AppLib = "VerifyAllCheckedOrUnchecked_DI--- " + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return flag;
    }

     
    //It verifies that devices with only the given statuses or platforms are displayed on devices index page list view.
    //<!--Created by : Mandeep Kaur 18/2/2015-->
    //<!--Last modified : 13/03/2015 by Mandeep Kaur-->
    //@param property What aspect to be verified : Status or Platform
    //@param strRequiredValues Value of statuses or platforms that need to be verified.
    //<example>eg. </example>
    //@return VerifyMultiplePlatformOrStatus_DI("Status", "Available,Offline"); -- It verifies that only those devices are displayed whose status is either Available or Offline.
    // Possible values for device status : Available, Offline, removed, In Use (<username>), Reserved (<username>), In Use (<username>), by reservation
    // Possible values for device platform :
    // 
    public final boolean VerifyMultipleValuesOfProperty_DI(String property, String strRequiredValues)
    {
        boolean flag = false, isValueCorrrect = false, loopEntered = false;
        strErrMsg_AppLib = "";
        int devicesCount = 0;
        String propertyValue = "", strErrorIndex = "";
        List<String> lstdeviceProperty = new ArrayList<String>();
        String[] arrRequiredValues;
        try
        {
            // Return true if no devices are displayed on devices index page
            if (PerformAction("eleNoDevicesWarning_Devices", Action.isDisplayed))
            {
                return true;
            }

            // If there are devices then get number of devices displayed on the devices index page
            devicesCount = getelementCount("eleDevicesHolderListView") - 1; // getting number of displayed devices. (-1 because this functions counts the header row also.)
            if (devicesCount <= 0)
                throw new RuntimeException("Could not get the number of devices."); // throw exception if number of displayed devices could not be fetched.

            // Switch to the property that needs to be verified and put the property value of all devices to the list lstdeviceProperty
            switch (property.toLowerCase())
            {
                case "status":
                    for (int i = 1; i <= devicesCount; i++) // For each device, get status or platform value and put in variable 'deviceStatusOrplatform'
                    {
                        propertyValue = GetTextOrValue(dicOR.get("eleDeviceStatus_ListView") + "[" + i + "]", "text"); //Getting status value for each devices displayed.
                        if (propertyValue.equals(""))
                            throw new RuntimeException("Could not get the status of device at index '" + i + "'.");
                        else
                            lstdeviceProperty.add(propertyValue);
                    }
                    break;
                case "platform":
                    for (int i = 1; i <= devicesCount; i++) // For each device, get status or platform value and put in variable 'deviceStatusOrplatform'
                    {
                        propertyValue = GetTextOrValue(dicOR.get("eleDevicePlatform_ListView").replace("__INDEX__", (new Integer(i + 1)).toString()), "text");
                        if (propertyValue.equals(""))
                            throw new RuntimeException("Could not get the platform of device at index '" + (i + 1) + "'.");
                        else
                            lstdeviceProperty.add(propertyValue);
                    }
                    break;
            }

            // Now that the required property of all devices is added to the list lstdeviceProperty, compare each value to the required value(ValuesToCheck)
            arrRequiredValues = strRequiredValues.split(",");
            for (int i = 0; i < lstdeviceProperty.size(); i++)
            {
            	loopEntered = true;
            	isValueCorrrect = false;
                //Check if lstdeviceProperty[i] starts with any of the values in arrRequiredValues
            	//i.e. check if the value obtained for each device is one of the required values.
            	
            	for(String propertyVal : arrRequiredValues)
            	{
            		if(propertyVal.startsWith(lstdeviceProperty.get(i))) // If the current property value matches with any of the required values, then set variable to true and break the loop for this value
            		{
            			isValueCorrrect = true;
            			break;
            		}
            	}
            	if(!isValueCorrrect)
            		strErrorIndex = strErrorIndex + ", " + i;
            	
                //if(!arrRequiredValues.Any(x=> lstdeviceProperty.get(i).startsWith(x)))
                //   strErrorIndex = strErrorIndex + ", " + i;
            }
            if(!strErrorIndex.equals("") || !loopEntered)  // throw exception if any device's property is not in the required values OR the loop is not entered at all.
                throw new RuntimeException("'" + property + "' of device at index(es) '" + strErrorIndex + "' is not any of these : '" + strRequiredValues + "'.");
            else
                flag = true;
        }
        catch (RuntimeException e)
        {
            flag = false;
            strErrMsg_AppLib = "VerifyAllCheckedOrUnchecked_DI--- " + "Exception at line number : '" + e.getStackTrace()[0].getLineNumber() + "'.; " + e.getMessage();
        }
        return flag;
    }
}