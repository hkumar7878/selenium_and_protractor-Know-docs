package com.common.utilities;

import java.awt.List;
import java.util.ArrayList;

import com.Reporting.Reporter;
import com.common.utilities.GenericLibrary.Action;

public class ScriptFuncLibrary extends ApplicationLibrary
{
	// Objects of different classes :
	private GenericLibrary genericLibrary = new GenericLibrary();
	private Reporter reporter = new Reporter();

	// Variables for reporting
	boolean isEventSuccessful;
	protected String strStepDescription, strExpectedResult, strActualResult, labelsXpath = "";

	/** 
	 Navigates the logged in user to reservations index page. It adds a step in the HTML report.
	 
	 <!--Created by : Mandeep Kaur-->
	 <!--Last updated : 9/2/2015 by Mandeep Kaur-->
	 @param isEventSuccessful
	*/
	public final boolean GoToReservationsPage()
	{
		strStepDescription = "Navigate to reservations index page.";
		strExpectedResult = "Reservations index page should be displayed.";
		strActualResult = "";
		isEventSuccessful = false;
		try
		{
			labelsXpath = getValueFromDictionary(dicOR, "eleReservationIndexPgLabels");
		}
		catch (RuntimeException e)
		{
			isEventSuccessful = false;
		}

		if (isEventSuccessful)
		{
			isEventSuccessful = navigateToNavBarPages("Reservations", "eleReservationsHeader");
			if (isEventSuccessful)
			{
				isEventSuccessful = PerformAction(labelsXpath.replace("__LABEL__", "Devices"), Action.isDisplayed);
				if (isEventSuccessful)
				{
					strActualResult = "'Devices' element is displayed successfully on Reservation Index Page";
				}
				else
				{
					strActualResult = "'Devices' element is not displayed successfully on Reservation Index Page";
				}
			}
			else
			{
				strActualResult = "SelectFromMenu-- " + strErrMsg_AppLib + "";
			}
		}
		else
		{
			strActualResult = "Could not get value of variable 'eleReservationIndexPgLabels' from dictionary 'dicOR'.";
		}
		reporter.ReportStep(strStepDescription, strExpectedResult, strActualResult, isEventSuccessful);
		return isEventSuccessful;
	}

	/** 
	 <!--Created by : Mandeep Kaur-->
	 
	 @param isEventSuccessful
	*/
	public final boolean GoToCreateReservationPage()
	{
		strStepDescription = "Click on Çreate'button to go to Create Reservation page.";
		strExpectedResult = "Create Reservation page should be opened.";
		isEventSuccessful = false;
		strActualResult = "";
		isEventSuccessful = PerformAction("btnCreateReservation", Action.Click);
		if (isEventSuccessful)
		{
			isEventSuccessful = PerformAction("eleCreateRsrvtnHeader", Action.WaitForElement, "10");
			if (isEventSuccessful)
			{
				strActualResult = "Create Reservation page displayed successfully after clicking on Çreate button.";
			}
			else
			{
				strActualResult = "Create Reservation page not displayed after clicking on Çreate button.";
			}
		}
		else
		{
			strActualResult = "Could not click on Çreate button on Reservations page.";
		}
		reporter.ReportStep(strStepDescription, strExpectedResult, strActualResult, isEventSuccessful);
		return isEventSuccessful;
	}

	/** 
	 Navigates the logged in user to devices index page. It adds a step in the HTML report.
	 
	 <!--Created by : Mandeep Kaur-->
	 <!--Last updated : 9/2/2015 by Mandeep Kaur-->
	 @param isEventSuccessful
	*/
	public final boolean GoToDevicesPage()
	{
		strStepDescription = "Go to devices index page.";
		strExpectedResult = "Devices page should be displayed.";
		strActualResult = "";
		isEventSuccessful = false;
		isEventSuccessful = navigateToNavBarPages("Devices", "eleDevicesTab_Devices");
		if (isEventSuccessful)
		{
			strActualResult = "Devices index page opened successfully.";
		}
		else
		{
			strActualResult = strErrMsg_AppLib;
		}
		reporter.ReportStep(strStepDescription, strExpectedResult, strActualResult, isEventSuccessful);
		return isEventSuccessful;
	}

	/** 
	 Navigates the logged in user to Applications index page and adds a step in the HTML report.
	 
	 <!--Created by : Mandeep Kaur-->
	 <!--Last updated : 9/2/2015 by Mandeep Kaur-->
	 @param isEventSuccessful
	*/
	public final boolean GoToApplicationsPage()
	{
		strStepDescription = "Go to applications index page.";
		strExpectedResult = "Applications page should be displayed.";
		strActualResult = "";
		isEventSuccessful = false;
		isEventSuccessful = navigateToNavBarPages("Applications", "eleApplicationsHeader");
		if (isEventSuccessful)
		{
			strActualResult = "Applications index page opened successfully.";
		}
		else
		{
			strActualResult = strErrMsg_AppLib;
		}
		reporter.ReportStep(strStepDescription, strExpectedResult, strActualResult, isEventSuccessful);
		return isEventSuccessful;
	}

	/** 
	 Navigates the logged in user to System page and adds a step in the HTML report.
	 
	 <!--Created by : Mandeep Kaur-->
	 <!--Last updated : 9/2/2015 by Mandeep Kaur-->
	 @param isEventSuccessful
	*/
	public final boolean GoToSystemPage()
	{
		strStepDescription = "Go to system index page.";
		strExpectedResult = "System page should be displayed.";
		strActualResult = "";
		isEventSuccessful = false;
		isEventSuccessful = navigateToNavBarPages("Devices", "eleDevicesTab_Devices");
		if (isEventSuccessful)
		{
			strActualResult = "System page opened successfully.";
		}
		else
		{
			strActualResult = strErrMsg_AppLib;
		}
		reporter.ReportStep(strStepDescription, strExpectedResult, strActualResult, isEventSuccessful);
		return isEventSuccessful;
	}

	
	/** 
	 Navigates the logged in user to detail page of the first device displayed on Devices index page.It adds a step in the HTML report.
	 This function returns the Object array which contains the Boolean and String values. Boolean needs to be added first and then String
	 
	 <!--Created by: Tarun Ahuja-->
	 @param isEventSuccessful and DeviceName
	*/
	
	public final Object[] GoTofirstDeviceDetailsPage()
	{
		strStepDescription = "Go to device details page of first displayed device.";
		strExpectedResult = "Device details page should be opened.";
		strActualResult = "";
		isEventSuccessful = false;
		String deviceName = "";
		Object[] values = new Object[2];
		isEventSuccessful = SelectDevice("first");
		deviceName = getValueFromDictionary(dicOutput, "selectedDeviceName");
		
		if (isEventSuccessful)
		{
			strActualResult = "Navigated to device details page of first displayed device : '" + deviceName;
		}
		else
		{
			strActualResult = strErrMsg_AppLib + "\r\n Device name : '" + deviceName + "'. ";
		}
		values[0] = isEventSuccessful;
		values[1]= deviceName;
		reporter.ReportStep(strStepDescription, strExpectedResult, strActualResult, isEventSuccessful);
		return values;
	}
	
	
	

	/** 
	 Connects the first device displayed on the devices index page using CLI and adds a step in the HTML report.
	 
	 <!--Created by : Mandeep Kaur-->
	 @param isEventSuccessful Variable in which the boolean result of function execution is to be stored.
	 @param selectedDevice Variable in which the name of the connected device is to be stored.
	 @param platform Platform of the device to be connected.
	 @param userName Username with which the device needs to be connected.
	 @param password Password of the user.
	*/

	public final Object[] connectFirstDevice_CLI(String platform, String userName)
	{
		return  connectFirstDevice_CLI(platform, userName, "");
	}

	public final Object[] connectFirstDevice_CLI(String platform)
	{
		return connectFirstDevice_CLI(platform, "", "");
	}

	public final Object[] connectFirstDevice_CLI()
	{
		return connectFirstDevice_CLI("android", "", "");
	}
	
	
	/* Created by : Tarun Ahuja
	   Returning array Object with Boolean and String Value	 
	 */
	public final Object[] connectFirstDevice_CLI(String platform, String userName, String password)
	{
		strActualResult = "";
		isEventSuccessful = false;
		String executedCommand = "";
		strStepDescription = "Connect to first displayed device.";
		strExpectedResult = "First displayed device should be connect.";
		
		Object[] CLIResult = ExecuteCLICommand("connect", platform, userName, password);
		isEventSuccessful = (boolean) CLIResult[0];
		String selectedDevice = (String) CLIResult[1];
		executedCommand = getValueFromDictionary(dicOutput, "executedCommand");
		if (isEventSuccessful)
		{
			strActualResult = "Device : '" + selectedDevice + "' connected successfully.";
		}
		else
		{
			strActualResult = strErrMsg_AppLib + "\r\n Command executed : '" + executedCommand + "'.";
		}
		reporter.ReportStep(strStepDescription, strExpectedResult, strActualResult, isEventSuccessful);
		return new Object[] {isEventSuccessful,selectedDevice } ; 
	}

	/** 
	 Connects the specified device using CLI and adds a step in the HTML report.
	 
	 <!--Created by : Mandeep Kaur-->
	 @param isEventSuccessful Variable in which the boolean result of function execution is to be stored.
	 @param selectedDevice Variable in which the name of the connected device is to be stored.
	 @param platform Platform of the device to be connected.
	 @param userName Username with which the device needs to be connected.
	 @param password Password of the user.
	 @param device Name of the device to be connected. It should be provided and not left empty.
	*/

	public final boolean ConnectSpecificDevice_CLI(  String platform, String userName, String password)
	{
		return isEventSuccessful= ConnectSpecificDevice_CLI( platform, userName, password, "");
	}

	public final boolean ConnectSpecificDevice_CLI(  String platform, String userName)
	{
		return isEventSuccessful=ConnectSpecificDevice_CLI( platform, userName, "", "");
	}

	public final boolean ConnectSpecificDevice_CLI(  String platform)
	{
		return isEventSuccessful=ConnectSpecificDevice_CLI( platform, "", "", "");
	}

	public final boolean ConnectSpecificDevice_CLI( )
	{
		return isEventSuccessful=ConnectSpecificDevice_CLI( "android", "", "", "");
	}
	
	public final boolean ConnectSpecificDevice_CLI(  String platform, String userName, String password, String device)
	{
		strStepDescription = "Connect to device : '" + device + "'.";
		strExpectedResult = "Device should get connected.";
		strActualResult = "";
		isEventSuccessful = false;
		String selectedDevice = "";
		String executedCommand = "";

		Object [] CLIResult = ExecuteCLICommand("connect", platform, userName, password);
		isEventSuccessful = (boolean) CLIResult[0];
		selectedDevice = (String) CLIResult[1];
		executedCommand = getValueFromDictionary(dicOutput, "executedCommand");
		if (isEventSuccessful)
		{
			strActualResult = "Device : '" + selectedDevice + "' connected successfully.";
		}
		else
		{
			strActualResult = strErrMsg_AppLib + "\r\n Command executed : '" + executedCommand + "'.";
		}
		reporter.ReportStep(strStepDescription, strExpectedResult, strActualResult, isEventSuccessful);
		return isEventSuccessful;
	}

	/** 
	 This function is for Releasing Device. It adds a step in the HTML report.
	 <!--Created By : Vinita Mahajan-->
	 
	 @param isEventSuccessful out isEventSuccessful
	 @param devicename Devicename to be released
	*/
	public final boolean ReleaseDevice(String devicename)
	{
		strStepDescription = "Release device : '" + devicename + "'.";
		strExpectedResult = "Device should be released.";
		String selectedDevice = "", executedCommand = "";
		strActualResult = "";
		isEventSuccessful = false;
		Object[] CLIResult = ExecuteCLICommand("release", "", "", "", devicename);
		isEventSuccessful = (boolean) CLIResult[0];
		selectedDevice = (String) CLIResult[1];
		executedCommand = getValueFromDictionary(dicOutput, "executedCommand");
		if (isEventSuccessful)
		{
			strActualResult = "Device : '" + selectedDevice + "' released successfully.";
		}
		else
		{
			strActualResult = strErrMsg_AppLib = "\r\n Command executed : '" + executedCommand + "'.";
		}
		reporter.ReportStep(strStepDescription, strExpectedResult, strActualResult, isEventSuccessful);
		return isEventSuccessful;
	}

	/** 
	 This function is for disabling a specific Device by DeviceName. It adds a step in the HTML report.
	 <!--Created By : Vinita Mahajan-->
	 
	 @param isEventSuccessful out isEventSuccessful
	 @param devicename Devicename to be released
	*/
	public final boolean DisableSpecificDeviceCLI(  String devicename)
	{
		strStepDescription = "Disable device : '" + devicename + "'.";
		strExpectedResult = "Device should be disabled.";
		String selectedDevice = "", executedCommand = "";
		strActualResult = "";
		Object[] CLIResult = ExecuteCLICommand("disable", "", "", "", devicename);
		isEventSuccessful = (boolean) CLIResult[0];
		selectedDevice = (String) CLIResult[1];
		executedCommand = getValueFromDictionary(dicOutput, "executedCommand");
		if (isEventSuccessful)
		{
			strActualResult = "Device : '" + selectedDevice + "' disabled successfully.";
		}
		else
		{
			strActualResult = strErrMsg_AppLib = "\r\n Command executed : '" + executedCommand + "'.";
		}
		reporter.ReportStep(strStepDescription, strExpectedResult, strActualResult, isEventSuccessful);
		return isEventSuccessful;
	}

	/** 
	 This function is for disabling first available device. It adds a step in the HTML report.
	 <!--Created By : Vinita Mahajan-->
	 
	 @param isEventSuccessful out isEventSuccessful
	 @param devicename out devicename of the device which is disabled
	*/
	public final Object[] DisableDeviceCLI()
	{
		strStepDescription = "Disbale first available device.";
		strExpectedResult = "Device should be disabled.";
		String executedCommand = "";
		strActualResult = "";
		
		Object[] CLIResult = ExecuteCLICommand("disable");
		isEventSuccessful = (boolean) CLIResult[0];
		String devicename = (String) CLIResult[1];
		executedCommand = getValueFromDictionary(dicOutput, "executedCommand");
		if (isEventSuccessful)
		{
			strActualResult = "Device : '" + devicename + "' disbaled successfully.";
		}
		else
		{
			strActualResult = strErrMsg_AppLib = "\r\n Command executed : '" + executedCommand + "'.";
		}
		reporter.ReportStep(strStepDescription, strExpectedResult, strActualResult, isEventSuccessful);
		return new Object[] {isEventSuccessful,devicename } ;
	}

	/** 
	 This function is for enabling a specific Device by DeviceName. It adds a step in the HTML report.
	 <!--Created By : Vinita Mahajan-->
	 
	 @param isEventSuccessful out isEventSuccessful
	 @param devicename Devicename to be enabled - should be disabled prior
	*/
	public final boolean EnableDevice(  String devicename)
	{
		strStepDescription = "Enable device : '" + devicename + "'.";
		strExpectedResult = "Device should be enabled.";
		String selectedDevice = "", executedCommand = "";
		strActualResult = "";
//		Object CLIResult = ExecuteCLICommand("enable", "", "", "", devicename);
		
		Object[] CLIResult = ExecuteCLICommand("enable", "", "", "", devicename);
		
		isEventSuccessful = (boolean) CLIResult[0];
		selectedDevice = (String) CLIResult[1];
		executedCommand = getValueFromDictionary(dicOutput, "executedCommand");
		if (isEventSuccessful)
		{
			strActualResult = "Device : '" + selectedDevice + "' enabled successfully.";
		}
		else
		{
			strActualResult = strErrMsg_AppLib = "\r\n Command executed : '" + executedCommand + "'.";
		}
		reporter.ReportStep(strStepDescription, strExpectedResult, strActualResult, isEventSuccessful);
		return isEventSuccessful;
	}

	/** 
	 This function is for login to DC. It adds a step in the HTML report.
	 <!--Created By : Vinita Mahajan-->
	 
	 @param isEventSuccessful out isEventSuccessful
	 @param UserName Username for login : if not specified then takes the default logged in value specified in TestData sheet
	 @param Password Password for login
	 @param NavigatetoDevices Navigated to devices check
	*/

	public final boolean Login(  String UserName, String Password)
	{
		return isEventSuccessful= Login( UserName, Password, true);
	}

	public final boolean Login(  String UserName)
	{
		return isEventSuccessful=Login( UserName, "", true);
	}

	public final boolean Login( )
	{
		return isEventSuccessful=Login( "", "", true);
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public void Login(out bool isEventSuccessful, string UserName = "", string Password = "", bool NavigatetoDevices = true)
	public final boolean Login(  String UserName, String Password, boolean NavigatetoDevices)
	{
		strActualResult = "";
		isEventSuccessful = false;
		if (UserName.isEmpty())
		{
			UserName = getValueFromDictionary(dicCommon, "EmailAddress");
			Password = getValueFromDictionary(dicCommon, "Password");
			strStepDescription = "Login to deviceConnect with " + UserName + " and verify Devices page.";
			strExpectedResult = "User " + UserName + " should be logged in and navigated to Devices page.";
		}
		else
		{
			strStepDescription = "Login to deviceConnect with " + UserName + " and verify Devices page.";
			strExpectedResult = "User : " + UserName + " should be logged in and navigated to Devices page.";
		}
		isEventSuccessful = LoginToDC(UserName, Password, NavigatetoDevices);
		if (isEventSuccessful)
		{
			strActualResult = "User : " + UserName + " is logged in to DC.";
		}
		else
		{
			strActualResult = strErrMsg_AppLib + "User : " + UserName + " is not able to login to DC.";
		}
		reporter.ReportStep(strStepDescription, strExpectedResult, strActualResult, isEventSuccessful);
		return isEventSuccessful;
	}

	/** 
	 Logs out the logged in User from dC application. It adds a step in the HTML report.
	 <!--Created By : Vinita Mahajan-->
	 
	 @param isEventSuccessful out isEventSuccessful
	*/
	public final boolean LogoutDC()
	{
		strStepDescription = "Logout from deviceConnect web UI.";
		strExpectedResult = "User is logged out of deviceConnect and login page is displayed.";
		isEventSuccessful = false;
		strActualResult = "";
		isEventSuccessful = Logout();
		if (isEventSuccessful)
		{
			strActualResult = "Logout is successful.";
		}
		else
		{
			strActualResult = strErrMsg_AppLib + "Not able to logout.";
		}
		reporter.ReportStep(strStepDescription, strExpectedResult, strActualResult, isEventSuccessful);
		return isEventSuccessful;
	}

	/** 
	 Navigates the logged in user to the Users index page and adds a step in the HTML report.
	 
	 <!--Created by : Mandeep Kaur-->
	 <!--Last updated : 9/2/2015 by Mandeep Kaur-->
	 @param isEventSuccessful Variable in which the boolean result of function execution is to be stored.
	*/
	public final boolean GoToUsersPage( )
	{
		strStepDescription = "Go to 'Users' page";
		strExpectedResult = "'Users page should be opened.";
		isEventSuccessful = false;
		strActualResult = "";
		isEventSuccessful = navigateToNavBarPages("Users", "eleUsersHeader");
		if (isEventSuccessful)
		{
			isEventSuccessful = PerformAction("btnCreateUser", Action.isDisplayed);
			if (isEventSuccessful)
			{
				strActualResult = "Users page is opened.";
			}
			else
			{
				strActualResult = "Users page is not opened.";
			}
		}
		else
		{
			strActualResult = "selectFromMenu()-- " + strErrMsg_AppLib;
		}
		reporter.ReportStep(strStepDescription, strExpectedResult, strActualResult, isEventSuccessful);
		return isEventSuccessful;
	}

	/** 
	 Clicks on 'Create User' button and verifies that create user page is open. It adds a step in the HTML report.
	 
	 <!--Created by : Mandeep Kaur-->
	 @param isEventSuccessful
	*/
	public final boolean GoToCreateUserPage( )
	{
		strStepDescription = "Click on'create' button and verify Create user page is opened.";
		strErrMsg_AppLib = "Create user page should be opened.";
		isEventSuccessful = false;
		strActualResult = "";
		isEventSuccessful = PerformAction("btnCreateUser", Action.Click);
		if (isEventSuccessful)
		{
			isEventSuccessful = PerformAction("inpFirstNameCreateUser", Action.WaitForElement, "5");
			if (isEventSuccessful)
			{
				strActualResult = "Create user page displayed successfully after clicking on 'create user' button on 'Users' page.";
			}
			else
			{
			strActualResult = "create user page not displayed after clicking on 'Create user' button.";
			}
		}
		else
		{
			strActualResult = "Could not click on 'Create user' button.";
		}
		reporter.ReportStep(strStepDescription, strExpectedResult, strActualResult, isEventSuccessful);
		return isEventSuccessful;
	}

	/** 
	 Navigates the logged in user to details page of the given user by clicking on 'Edit' button and verifies that user details page is opened. It adds a step in HTML report.
	 
	 <!--Created by : Mandeep Kaur-->
	 @param isEventSuccessful
	 @param strUserID Email ID of the user whose details page is to be opened.
	*/
	public final boolean GoToSpecificUserDetailsPage(  String strUserID)
	{
		strStepDescription = "Go to device details page of the user : '" + strUserID + "'.";
		strStepDescription = "User details page should be opened.";
		isEventSuccessful = false;
		strActualResult = "";
		String editBtnXpath = getValueFromDictionary(dicOR, "btnEditUser_GridView");
		editBtnXpath = editBtnXpath.replace("__EMAILID__", strUserID);
		isEventSuccessful = PerformAction(editBtnXpath, Action.Click);
		if (isEventSuccessful)
		{
			isEventSuccessful = PerformAction("eleUserEditHeader", Action.WaitForElement, "20");
			if (isEventSuccessful)
			{
				strActualResult = "User details page of user : " + strUserID + " is displayed successfully.";
			}
			else
			{
				strActualResult = "User details page of user : " + strUserID + " is not displayed ";
			}
		}
		else
		{
			strActualResult = "Could not click on 'Edit' button for user : '" + strUserID + "'. \r\n Locator used : '" + editBtnXpath + "'.";
		}
		reporter.ReportStep(strStepDescription, strErrMsg_AppLib, strActualResult, isEventSuccessful);
		return isEventSuccessful;
	}

	/** 
	 Navigates the logged in user to details page of first displayed application and verifies that the correct details page is opened. It adds a step in the HTML report.
	 
	 <!--Created by : Tarun Ahuja-->
	 @param isEventSuccessful
	 @param selectedApp Variable in which the name of application selected by code is to be stored.
	*/
	public final Object[] GoToFirstAppDetailsPage()
	{
		strStepDescription = "Go to details page of first application displayed on Applications index page.";
		strExpectedResult = "Application details page of first application should be opened.";
		isEventSuccessful = false;
		strActualResult = "";
		Object[] values = new Object[2];
		isEventSuccessful = SelectApplication("first");
		String selectedApp = getValueFromDictionary(dicOutput, "selectedAppName");
		if (isEventSuccessful)
		{
			strActualResult = "Application details page of application : '" + selectedApp + "' is opened successfully.";
		}
		else
		{
			strActualResult = strErrMsg_AppLib + "\r\n Application name : '" + selectedApp + "'.";
		}
		
		values[0] = isEventSuccessful;
		values[1]= selectedApp;
		reporter.ReportStep(strStepDescription, strExpectedResult, strActualResult, isEventSuccessful);
		return values;
	}

	/** 
	 Navigates the logged in user to details page of given application and verifies that the correct details page is opened. It adds a step in the HTML report.
	 
	 <!--Created by : Mandeep Kaur-->
	 @param isEventSuccessful
	 @param appName Name of the application(as displayed on applications index page) whose details page is to be opened.
	*/
	public final boolean GoToSpecificAppDetailsPage(  String appName)
	{
		strStepDescription = "Go to details page of '" + appName + "'.";
		strExpectedResult = "Application details page of the given application should be opened.";
		isEventSuccessful = false;
		strActualResult = "";
		String selectedApp = "";
		isEventSuccessful = SelectApplication("appname", appName);
		selectedApp = getValueFromDictionary(dicOutput, "selectedAppName");
		if (isEventSuccessful)
		{
			strActualResult = "Application details page of application : '" + selectedApp + "' is opened successfully.";
		}
		else
		{
			strActualResult = strErrMsg_AppLib + "\r\n Application name : '" + selectedApp + "'.";
		}
		reporter.ReportStep(strStepDescription, strExpectedResult, strActualResult, isEventSuccessful);
		return isEventSuccessful;
	}

	/** 
	 This function is to get the device details as per the index. It adds a step in the HTML report.
	 <!--Created By : Vinita Mahajan-->
	 
	 @param isEventSuccessful out isEventSuccessful
	 @param detailValue Details Value : Exoected
	 @param index Index of the device whose value to be fetched
	 @param detailName Details Name to be fetched
	 @param view In which View to verify
	 @param status Status of the device whose detail is to be verified
	*/

	public final Object[] GetDeviceDetails( int index, String detailName, String view)
	{
		return GetDeviceDetails(index, detailName, view, "");  
	}

	public final Object[] GetDeviceDetails(int index, String detailName)
	{
		return GetDeviceDetails(index, detailName, "", "");
	}

//C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//ORIGINAL LINE: public void GetDeviceDetails(out bool isEventSuccessful, out string detailValue, int index, string detailName, string view = "", string status = "")
	
	/**
	  <!--Created by: Tarun Ahuja-->
	 @param isEventSuccessful and detailValue
	 */
	public final Object[] GetDeviceDetails(int index, String detailName, String view, String status)
	{
		strStepDescription = "Get the details : '" + detailName + "'.";
		strExpectedResult = "User should be able to get the details : '" + detailName + "'.";
		isEventSuccessful = false;
		Object[] values = new Object[2];
		String detailValue = GetDeviceDetailInGridAndListView(index, detailName, view, status);
		if (!(detailValue.isEmpty()))
		{
			strActualResult = "Not able to get the details value.";
			isEventSuccessful = true;
		}
		else
		{
			strActualResult = strErrMsg_AppLib + "\r\n Not able to get the Detail Name : '" + detailName + "'.";
			isEventSuccessful = false;
		}
		values[0]= isEventSuccessful;
		values[1]=detailValue;
		reporter.ReportStep(strStepDescription,strExpectedResult,strActualResult,isEventSuccessful);
		return values;
	}

	/** 
	 This function is to get the device details as per the deviceName specified. It adds a step in the HTML report.
	 <!--Created By : Vinita Mahajan-->
	 
	 @param isEventSuccessful out isEventSuccessful
	 @param deviceName Get the details as per the devicename specified
	*/
	public final boolean GoToSpecificDeviceDetailsPage(  String deviceName)
	{
		strStepDescription = "Go to device details page of " + deviceName + " device.";
		strExpectedResult = "Device details page for " + deviceName + " should be opened.";
		strActualResult = "";
		isEventSuccessful = false;
		isEventSuccessful = SelectDevice("devicename",deviceName);
		deviceName = getValueFromDictionary(dicOutput, "selectedDeviceName");
		if (isEventSuccessful)
		{
			strActualResult = "Navigated to device details page of : '" + deviceName;
		}
		else
		{
			strActualResult = strErrMsg_AppLib + "\r\n Device name : '" + deviceName + "'. ";
		}
		reporter.ReportStep(strStepDescription, strExpectedResult, strActualResult, isEventSuccessful);
		return isEventSuccessful;
	}

	/** 
	 This function verifies the given device detail name and its value(if given) on device details page. It adds a step in the HTML report.
	 <!--Created By : Vinita Mahajan-->
	 
	 @param isEventSuccessful
	 @param detailName Detail Name to be verified
	 @param detailValue The Expected Value to be verified or else it checks the existence
	*/

	public final boolean VerifyonDeviceDetails(  String detailName)
	{
		return isEventSuccessful= VerifyonDeviceDetails(detailName, "");
	}
	
	public final boolean VerifyonDeviceDetails(  String detailName, String detailValue)
	{
		strStepDescription = "Verify the detail : " + detailName + " is displayed on device details page.";
		strExpectedResult = "Detail : " + detailName + " should be displayed on device details page.";
		strActualResult = "";
		isEventSuccessful = false;
		isEventSuccessful = VerifyOnDeviceDetailsPage(detailName, detailValue);
		if (detailName.equals("Battery Status"))
		{
			detailValue = getValueFromDictionary(dicOutput, "BatteryStatusText");
		}
		if (isEventSuccessful)
		{
			strActualResult = detailName + " - " + detailValue + " : is displayed for the device.";
		}
		else
		{
			strActualResult = strErrMsg_AppLib + "\r\n Detail : " + detailName + detailValue + "is not displayed.";
		}
		reporter.ReportStep(strStepDescription, strExpectedResult, strActualResult, isEventSuccessful);
		return isEventSuccessful;
	}

	/** 
	 Clicks on List view icon. It does not verify anything. It adds a step in the HTML report.
	 
	 <!--Created by : Mandeep Kaur-->
	 @param isEventSuccessful
	*/
	public final boolean clickListViewIcon( )
	{
		strStepDescription = "Click on 'List View' icon.";
		strExpectedResult = "User should be able to click on 'List view' icon.";
		strActualResult = "";
		isEventSuccessful = false;
		isEventSuccessful = PerformAction("lnkListView", Action.Click);
		if (isEventSuccessful)
		{
			strActualResult = "Successfully clicked on 'List' view icon.";
		}
		else
		{
			strActualResult = "Could not click on 'List' view icon.";
		}
		reporter.ReportStep(strStepDescription, strExpectedResult, strActualResult, isEventSuccessful);
		return isEventSuccessful;
	}

	/** 
	 Clicks on Card view icon. It does not verify anything. It adds a step in the HTML report.
	 
	 <!--Created by : Mandeep Kaur-->
	 @param isEventSuccessful
	*/
	public final boolean clickCardViewIcon( )
	{
		strStepDescription = "Click on 'Card View' icon.";
		strExpectedResult = "User should be able to click on 'Card view' icon.";
		strActualResult = "";
		isEventSuccessful = false;
		isEventSuccessful = PerformAction("lnkGridView", Action.Click);
		if (isEventSuccessful)
		{
			strActualResult = "Successfully clicked on 'Card' view icon.";
		}
		else
		{
			strActualResult = "Could not click on 'Card' view icon.";
		}
		reporter.ReportStep(strStepDescription, strExpectedResult, strActualResult, isEventSuccessful);
		return isEventSuccessful;
	}

	/** 
	 Selects the checkbox(s) for platform(s) given as parameter in array and unchecks all other platform checkboxes on Devices index page. It adds a step in the HTML report.
	 
	 <!-- Created by : Mandeep Kaur -->
	 <!-- Last updated : 2/6/2015 by Mandeep Kaur -->
	 @param isEventSuccessful
	 @param devicePlatform Comma separated values of platforms to be selected. eg. "Android,iOS"
	 @param checkOrUncheckAll If the value is specified "uncheckall" then it unchecks all the Platforms checkboxes.
	*/

	public final boolean selectPlatform(String devicePlatform)
	{
		return isEventSuccessful= selectPlatform(devicePlatform, "checkall");
	}
	
	public final boolean selectPlatform(  String devicePlatform, String checkOrUncheckAll)
	{
		//string Platforms = ArrayToString(devicePlatform);
		strStepDescription = "Select platform(s) checkboxes : " + devicePlatform;
		strExpectedResult = "Checkbox for platform(s) : " + devicePlatform + " should be checked.";
		strActualResult = "";
		isEventSuccessful = false;
		if (checkOrUncheckAll.equals("uncheckall"))
		{
			isEventSuccessful = selectPlatform_DI("Inmjskdjflksdjfl"); //unselects all the checkboxes as no Platform starts with Inmjskdjflksdjfl
		}
		else
		{
			isEventSuccessful = selectPlatform_DI(devicePlatform);
		}
		if (isEventSuccessful)
		{
			strActualResult = "Checkboxes for platforms : " + devicePlatform + " selected successfully.";
		}
		else
		{
			strActualResult = strErrMsg_AppLib;
		}
		reporter.ReportStep(strStepDescription, strExpectedResult, strActualResult, isEventSuccessful);
		return isEventSuccessful;
	}

	/** 
	 Selects the checkbox(s) for platform(s) given as parameter in array and unchecks all other platform checkboxes on Devices index page. It adds a step in the HTML report.
	 
	 <!-- Created by : Mandeep Kaur -->
	 <!-- Last updated : 2/6/2015 by Mandeep Kaur -->
	 @param isEventSuccessful
	 @param deviceStatus Comma separated platforms that need to be selected. eg. "Available,Offline"
	 @param checkOrUncheckAll If the value is specified "uncheckall" then it unchecks all the status checkboxes.
	*/

	/*public final boolean selectStatus(String deviceStatus)
	{
		return isEventSuccessful= selectStatus(deviceStatus, "checkall");
	}*/

	/*public final boolean selectStatus(String deviceStatus, String checkOrUncheckAll)*/
	public final boolean selectStatus(String deviceStatus)
	{
		//string status = ArrayToString(deviceStatus);
		strStepDescription = "Select status(es) checkboxes : " + deviceStatus;
		strExpectedResult = deviceStatus + " checkbox should be selected.";
		strActualResult = "";
		isEventSuccessful = false;
		isEventSuccessful = selectStatus_DI(deviceStatus);
		if (isEventSuccessful)
		{
			strActualResult = "Checkboxes for status(es) : " + deviceStatus + " selected successfully.";
		}
		else
		{
			strActualResult = strErrMsg_AppLib;
		}

		reporter.ReportStep(strStepDescription, strExpectedResult, strActualResult, isEventSuccessful);
		return isEventSuccessful;
	}

	/** 
	 Selects the checkbox in front of the first displayed device. It adds report step to the HTMl report.
	 
	 <!--Created by : Tarun Ahuja-->
	 <!--Last updated : 11/2/2015 by Mandeep Kaur-->
	 @param isEventSuccessful
	 @param selectedDeviceName Variable in which the name of selected(i.e. First displayed) device needs to be stored.
	*/
	public final Object[] selectFirstDeviceChk_DI()
	{
		strStepDescription = "Select checkbox of first displayed device.";
		strExpectedResult = "Checkbox in front of the first displayed device should be selected.";
		strActualResult = "";
		String selectedDeviceName = "";
		isEventSuccessful = false;
		Object[] values = new Object[2];
		isEventSuccessful = PerformAction("chkDeviceName_Devices", Action.SelectCheckbox);
		if (isEventSuccessful)
		{
			selectedDeviceName = GetTextOrValue(dicOR.get("eleDeviceName_ListView").replace("__INDEX__", "1"), "text");
			if (selectedDeviceName == null || selectedDeviceName == " ")
			{
				isEventSuccessful = false;
				strActualResult = "Could not get name of selected device.";
			}
			else
			{
				strActualResult = "Checkbox in front of device '" + selectedDeviceName + "' selected successfully.";
			}
		}
		else
		{
			strActualResult = "Could not select checkbox in front of first displayed device.";
		}
		values[0] = isEventSuccessful;
		values[1] = selectedDeviceName;
		reporter.ReportStep(strStepDescription, strExpectedResult, strActualResult, isEventSuccessful);
		return values;
	}

	/** 
	 Clicks on the select all checkbox on devices index page, verifies that checkboxes in front of all displayed devices are also selected and adds names of the .
	 
	 <!--Created by : Mandeep Kaur-->
	 <!--Last updated : 2/6/2015 by Mandeep Kaur-->
	 @param isEventSuccessful
	*/
	public final Object[] selectAllDevicesCheckbox_DI()
	{
		strStepDescription = "Select the select all checkbox on devices index page.";
		strExpectedResult = "Select All checkbox should get selected.";
		ArrayList devicesSelected = new ArrayList();
		strActualResult = "";
		isEventSuccessful = false;
		//Pair<Boolean, ArrayList<String>> returnedPairvalues;
		try{

			Object[] objresult = selectAllCheckboxAndVerify_DI(); // PerformAction("chkSelectAll_Devices", Action.SelectCheckbox);
			isEventSuccessful = (boolean) objresult[0];
			devicesSelected = (ArrayList) objresult[1];
			if (isEventSuccessful)
			{
				strActualResult = "Select all checkbox selected successfully.";
			}
			else
			{
				strActualResult = "Could not select the select all checkbox." ;
			}

		}catch(Exception e)
		{	
			isEventSuccessful=false;
			strActualResult = "Error occured at Line Number " +e.getStackTrace()[0].getLineNumber() + "\r\n" + e.getMessage();
		}
		reporter.ReportStep(strStepDescription, strExpectedResult, strActualResult, isEventSuccessful);
		return new Object[] {isEventSuccessful,devicesSelected}; 
	}

	/** 
	 Verifies that no element with given xpath has the given text.
	 
	 <!--Created by : Mandeep Kaur 2/6/2015-->
	 @param isEventSuccessful Bool variable in which result of execution is to be stored.
	 @param elementXpath Common xpath of the elements whose text is to be checked.
	 @param text Text on the elements that needs to be verified.
	 <example>eg. VerifyNoelementWithText(out isEventSuccessful, "//button", "Add");</example>
	*/
	public final boolean VerifyNoelementWithGivenText(  String elementXpath, String text)
	{
		strStepDescription = "Verify that no element with xpath '" + elementXpath + "' has text '" + text + "'";
		strExpectedResult = "None of the elements matching locator '" + elementXpath + "' should have the given text.";
		strActualResult = "";
		isEventSuccessful = false;
		isEventSuccessful = verifyElementWithTextNotPresentOnPage(elementXpath, text);
		if (isEventSuccessful)
		{
			strActualResult = "No element with xpath '" + elementXpath + "' has text '" + text + "'";
		}
		else
		{
			strActualResult = strErrMsg_AppLib;
		}
		reporter.ReportStep(strStepDescription, strExpectedResult, strActualResult, isEventSuccessful);
		return isEventSuccessful;
	}
	
	
	/** 
	 Refresh and verifies the page refresh.
	 
	 <!--Created by : Tarun Ahuja 7/206/2015-->
	 @param isEventSuccessful Bool variable in which result of execution is to be stored.
	**/
	public final boolean RefreshPage()
	{
		strStepDescription = "Refresh and verify the page refresh is completed'";
		strExpectedResult = "Page should be refreshed";
		strActualResult = "";
		isEventSuccessful = false;
		isEventSuccessful = PerformAction("Browser", Action.Refresh);
		if (isEventSuccessful)
		{
			strActualResult = "Page is successfully refreshed";
		}
		else
		{
			strActualResult = "Error occured while refreshing the page";
		}
		reporter.ReportStep(strStepDescription, strExpectedResult, strActualResult, isEventSuccessful);
		return isEventSuccessful;
	}
}