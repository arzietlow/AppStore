///////////////////////////////////////////////////////////////////////////////
//                  
// Main Class File:  AppStore.java
// File:             AppStoreDB.java
// Semester:         CS 367 Fall 2015
//
// Author:           Andrew Zietlow arzietlow@wisc.edu
// CS Login:         azietlow
// Lecturer's Name:  Jim Skrentny
// Lab Section:      Lecture 1
//
//
// Pair Partner:     N/A
//
// External Help:   None
//////////////////////////// 80 columns wide //////////////////////////////////

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class defines a single AppStoreDB object that is needed to contain all
 * the information on Users and Apps as read from the AppStore class. This 
 * class also plays a role in organizating that information into specifically
 * defined lists.
 *
 * <p>Bugs: User input is not checked in "uploadApp" method
 *
 * @author Andrew Zietlow
 */
public class AppStoreDB {

	private List<String> categories; //Range of categories for all App objects
	private List<User> userList; //The list of all users in the store
	private List<App> appList; //The list of all registered apps in the store
	private List<App> freeAppList; //The list of all app objects with 0 price
	private List<App> paidAppList; //The list of all app objects with >0 price
	private List<App> recentAppList; //all app objects ordered by upload time

	//Constructs a new AppStore Database object
	public AppStoreDB() {
		this.userList = new ArrayList<User>();
		this.appList = new ArrayList<App>();
		this.categories = new ArrayList<String>();
		this.freeAppList = new ArrayList<App>();
		this.paidAppList = new ArrayList<App>();
		this.recentAppList = new ArrayList<App>();
	}

	/**
	 * Takes in input and adds it together to form a new User object, but
	 * only adds the resulting object to the database provided it does not
	 * already exist.
	 *
	 * @param email: the email address associated with a new User object
	 * @param password: the password value associated with a new User object
	 * @param firstName: the firstName value associated with a new User object
	 * @param lastname: the lastName value associated with a new User object
	 * @param country: the country balue associated with a new User object
	 * @param type: the value used to determine if this user can upload apps
	 * @return a new User object constructed from file input (AppStore class)
	 */
	public User addUser(String email, String password, String firstName,
			String lastName, String country, String type)
					throws IllegalArgumentException {
		User temp = new User(email, password, firstName,
				lastName, country, type);
		boolean onList = false;
		if (userList.size() != 0) {
			for (int i = 0; i < userList.size(); i++) {
				if (userList.get(i) == temp) onList = true;
			}
		}
		if (onList == false) {
			this.userList.add(temp);
			return temp;
		}
		else throw new IllegalArgumentException("User already registered");
	}

	/**
	 * Adds a new category to the list of valid App categories
	 */
	public void addCategory(String category) {
		categories.add(category);
	}

	/**
	 * Getter for the list of valid categories currently allowed in the store
	 * @return a list of category types
	 */
	public List<String> getCategories() {	
		return categories;
	}

	/**
	 * Looks through the current database to find a User object that matches
	 * the given email ID value. If not found, returns null rather than an 
	 * error.
	 *
	 * @param email: the unique value used to match a search with a result
	 * @return a User object
	 */
	public User findUserByEmail(String email) {
		boolean hasFound = false;
		User user = userList.get(0);
		for (int i = 0; i < userList.size(); i++) {
			User temp = userList.get(i);
			if (temp.getEmail().equals(email)) {
				hasFound = true;
				user = userList.get(i);
			}
			if (hasFound == true) break;
		}
		if (!hasFound) return null;
		else return user;
	}

	/**
	 * Looks through the current database to find an App object that matches
	 * the given appID value. If not found, returns null rather than an error.
	 *
	 * @param appId: the unique value used to match a search with a result
	 * @return an App object
	 */
	public App findAppByAppId(String appId) {
		boolean hasFound = false;
		App app = appList.get(0);
		for (int i = 0; i < appList.size(); i++) {
			App temp = appList.get(i);
			if (temp.getAppId().equals(appId)) {
				hasFound = true;
				app = appList.get(i);
			}
			if (hasFound == true) break;
		}
		if (!hasFound) return null;
		else return app;
	}

	/**
	 * Allows a user to log in to the Appstore for the purpose of making 
	 * changes to their respective User object as well as altering App objects.
	 * Only works if the given parameters BOTH match a user object.
	 *
	 * @param email: the given email login ID for the particular User object
	 * @param password: the corresponding password value for that User object
	 * @return the newly logged-in user
	 */
	public User loginUser(String email, String password) {
		User user = userList.get(0);
		boolean hasFound = false;
		for (int i = 0; i < userList.size(); i++) {
			User temp = userList.get(i);
			if (temp.getEmail().equals(email)){
				if (temp.verifyPassword(password)) {
					hasFound = true;
					user = userList.get(i);
				}
			}
		}
		if (!hasFound) return null;
		else return user;
	}

	/**
	 * Allows a User object to submit their own App object to the Appstore. Also
	 * adds the App object to the User's list of uploaded Apps.
	 *
	 * @param uploader: the particular User object's email parameter
	 * @param appId: the unique ID given to the new App object
	 * @param appName: the name of the new App object
	 * @param category: the category to which the new App object belongs
	 * @param price: the price it will cost to download the new App object
	 * @param timestamp: the number of miliseconds since ~1970 at upload time
	 * @return the newly-added App object
	 */
	public App uploadApp(User uploader, String appId, String appName, 
			String category, double price, 
			long timestamp) throws IllegalArgumentException {
		boolean onList = false;
		App temp = new App(uploader, appId, appName,
				category, price, timestamp);
		for (int i = 0; i < appList.size(); i++) {
			if (appList.get(i) == temp) onList = true;
		}
		if (onList == false) {
			this.appList.add(temp);
			uploader.upload(temp);
			return temp;
		}
		else throw new IllegalArgumentException("App already registered");
	}

	/**
	 * Adds a given App object to a particular User object's list of downloaded
	 * Apps. Also increases the App object's number of downloads. This is 
	 * assuming the app has not been downloaded previously.
	 *
	 * @param user: the User object to which the download is attributed
	 * @param app: the App object to which the download is also attributed
	 */
	public void downloadApp(User user, App app) {
		if (!hasUserDownloadedApp(user, app)) {
			app.download(user);
			user.download(app);
		}
		else throw new IllegalArgumentException();
	}

	/**
	 * Adds a given App object to a particular User object's list of rated Apps.
	 * Also increases the App object's rate values. This is assuming the app 
	 * has not been rated previously, but has been downloaded.
	 *
	 * @param user: the User object to which the rating is attributed
	 * @param app: the App object to which the rating is also attributed
	 * @param rating: the numerical value of the rating given to the App
	 */
	public void rateApp(User user, App app, short rating) {
		if (hasUserDownloadedApp(user, app)) {
			if ((rating <= 5) && (rating >= 0)) {
				app.rate(user, rating);
			}
		}
		else throw new IllegalArgumentException();
	}

	/**
	 * Used to determine whether a given User object has already downloaded a 
	 * given App object in the past.
	 *
	 * @param user: the User object being checked for download history
	 * @param app: the App object being looked for in the User's downloaded list
	 * @return True if the app has been downloaded before, false if it hasn't
	 */
	public boolean hasUserDownloadedApp(User user, App app) { 
		boolean hasDownload = false;
		App temp = app;
		for (int i = 0; i < user.getAllDownloadedApps().size(); i++) {
			if (temp == user.getAllDownloadedApps().get(i)) hasDownload = true;
		}
		return hasDownload;
	}

	/**
	 * Sorts the list of all free apps in the AppStore based on the given 
	 * category, AppScore values, and timestamp values. This sorted list is 
	 * then stored in a new, separate list.
	 *
	 * @param category: the category of App objects to be listed. if empty, all
	 * App objects will be listed
	 * @return a list of App objects
	 */
	public List<App> getTopFreeApps(String category) {
		App[] appArray = new App[appList.size()];
		this.freeAppList = new ArrayList<App>();
		boolean hasCategory = true;
		int position = 0;
		if (category == null) hasCategory = false;
		Iterator<App> itr = appList.iterator();

		//Builds array of apps based on the app list
		while (itr.hasNext()) {
			App currApp = itr.next();
			for (int i = 0; i < appArray.length; i++) {
				appArray[position] = currApp;
			}
			position++;
		}

		//Sorts array of apps based on AppScore
		boolean hasSwapped = false;
		int leftIndex = 0;
		int rightIndex = 1;
		App leftApp = appArray[leftIndex];
		App rightApp = appArray[rightIndex];

		do {
			hasSwapped = false;
			leftIndex = 0;
			rightIndex = 1;
			for (int i = 0; i < appArray.length; i++) {
				leftApp = appArray[leftIndex];
				rightApp = appArray[rightIndex];
				if (leftApp.getAppScore() > rightApp.getAppScore()) {
					hasSwapped = true;
					App temp = rightApp;
					appArray[rightIndex] = leftApp;
					appArray[leftIndex] = temp;
				}
				else if ((leftApp.getAppScore() == rightApp.getAppScore()) &&
						(leftApp.getUploadTimestamp() < 
								rightApp.getUploadTimestamp())) {
					hasSwapped = true;
					App temp = rightApp;
					appArray[rightIndex] = leftApp;
					appArray[leftIndex] = temp;
				}
				if (leftIndex < appArray.length - 1) leftIndex++;
				if (rightIndex < appArray.length - 1) rightIndex++;
			}
		} while(hasSwapped);

		//Filters valid app members of app array into list
		for (int i = appArray.length - 1; i > -1; i--) {
			if ((!hasCategory) && (appArray[i].getPrice() == 0)) {
				freeAppList.add(appArray[i]);
			}
			else {
				if ((appArray[i].getCategory().equalsIgnoreCase(category)) &&
						(appArray[i].getPrice() == 0)) {
					freeAppList.add(appArray[i]);
				}
			}
		}
		return freeAppList;
	}

	/**
	 * Sorts the list of all paid apps in the AppStore based on the given 
	 * category, AppScore values, and timestamp values. This sorted list is 
	 * then stored in a new, separate list.
	 *
	 * @param category: the category of App objects to be listed. if empty, all
	 * App objects will be listed
	 * @return a list of App objects
	 */
	public List<App> getTopPaidApps(String category) {
		App[] appArray = new App[appList.size()];
		this.paidAppList = new ArrayList<App>();
		boolean hasCategory = true;
		int position = 0;
		if (category == null) hasCategory = false;
		Iterator<App> itr = appList.iterator();

		//Builds array of apps based on appData
		while (itr.hasNext()) {
			App currApp = itr.next();
			for (int i = 0; i < appArray.length; i++) {
				appArray[position] = currApp;
			}
			position++;
		}

		boolean hasSwapped = false;
		int leftIndex = 0;
		int rightIndex = 1;
		App leftApp = appArray[leftIndex];
		App rightApp = appArray[rightIndex];
		//Sorts array of apps based on AppScore and UploadTimeStamp
		do {
			hasSwapped = false;
			leftIndex = 0;
			rightIndex = 1;
			for (int i = 0; i < appArray.length; i++) {
				leftApp = appArray[leftIndex];
				rightApp = appArray[rightIndex];
				if (leftApp.getAppScore() > rightApp.getAppScore()) {
					hasSwapped = true;
					App temp = rightApp;
					appArray[rightIndex] = leftApp;
					appArray[leftIndex] = temp;
				}
				else if ((leftApp.getAppScore() == rightApp.getAppScore()) &&
						(leftApp.getUploadTimestamp() < 
								rightApp.getUploadTimestamp())) {
					hasSwapped = true;
					App temp = rightApp;
					appArray[rightIndex] = leftApp;
					appArray[leftIndex] = temp;
				}
				if (leftIndex < appArray.length - 1) leftIndex++;
				if (rightIndex < appArray.length - 1) rightIndex++;
			}
		} while(hasSwapped);

		//Filters valid app members of app array into list
		for (int i = appArray.length - 1; i > -1; i--) {
			if ((!hasCategory) && (appArray[i].getPrice() != 0)) {
				paidAppList.add(appArray[i]);
			}
			else {
				if ((appArray[i].getCategory().equalsIgnoreCase(category)) &&
						(appArray[i].getPrice() != 0)) {
					paidAppList.add(appArray[i]);
				}
			}
		}
		return paidAppList;
	}

	//Returns the list of apps sorted from newest to oldest
	/**
	 * Sorts the list of all apps in the AppStore based on the given category
	 * and timestamp values. This sorted list is then stored 
	 * in a new, separate list.
	 *
	 * @param category: the category of App objects to be listed. if empty, all
	 * App objects will be listed
	 * @return a list of App objects
	 */
	public List<App> getMostRecentApps(String category) {
		App[] appArray = new App[appList.size()];
		this.recentAppList = new ArrayList<App>();
		boolean hasCategory = true;
		int position = 0;
		if (category == null) hasCategory = false;
		Iterator<App> itr = appList.iterator();

		//Builds array of apps based on appData
		while (itr.hasNext()) {
			App currApp = itr.next();
			for (int i = 0; i < appArray.length; i++) {
				appArray[position] = currApp;
			}
			position++;
		}

		//Sorts array of apps based on UploadTimeStamp
		boolean hasSwapped = false;
		int leftIndex = 0;
		int rightIndex = 1;
		App leftApp = appArray[leftIndex];
		App rightApp = appArray[rightIndex];

		//bubble sort
		do {
			hasSwapped = false;
			leftIndex = 0;
			rightIndex = 1;
			for (int i = 0; i < appArray.length; i++) {
				leftApp = appArray[leftIndex];
				rightApp = appArray[rightIndex];
				if (leftApp.compareTo(rightApp) == 1) { 
					hasSwapped = true;
					App temp = rightApp;
					appArray[rightIndex] = leftApp;
					appArray[leftIndex] = temp;
				}
				if (leftIndex < appArray.length - 1) leftIndex++;
				if (rightIndex < appArray.length - 1) rightIndex++;
			}
		} while(hasSwapped);

		//Filters valid app members of app array into list
		for (int i = 0; i < appList.size(); i++) {
			if (!hasCategory) {
				recentAppList.add(appArray[i]);
			}
			else {
				if (appArray[i].getCategory().equalsIgnoreCase(category)) {
					recentAppList.add(appArray[i]);
				}
			}
		}
		return recentAppList;
	}
}

