///////////////////////////////////////////////////////////////////////////////
//                  
// Main Class File:  AppStore.java
// File:             App.java
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

/**
 * The App class is used for the creation of App objects, which store the 
 * relevant information associated with each App that has been registered 
 * in the Appstore. This class also plays a direct role in a user's commands 
 * and in comparing/sorting app objects.
 * 
 * <p>Bugs: None known
 *
 * @author Andrew Zietlow
 */
public class App implements Comparable<App> {

	private User developer; //The User object who created this App object
	private String appId;   //The unique keyword given to this App object
	private String appName; //The title given to this App object
	private String category;//The category to which this App object belongs
	private double price;   //The price one will pay to download this App
	private long uploadTimestamp;//The age indicator assigned to this App
	private long downloads; //The number of times this App has been downloaded
	private long rateTally; //All rates added together (for average rating)
	private double revenue; //The total amount of money paid for this App
	private long raters; //Number of Users who have rated (for average rating)
	private double rating;  //The average rating value assigned to this App
	private ArrayList<User> rateList;//The list of Users who have rated this

	public App(User developer, String appId, String appName, String category,
			double price, long uploadTimestamp) throws IllegalArgumentException 
	{
		this.developer = developer;
		this.appId = appId;
		this.appName = appName;
		this.category = category;
		this.price = price;
		this.uploadTimestamp = uploadTimestamp;
		this.rateList = new ArrayList<User>();
	}

	/**
	 * The getter for an app object's developer's email address
	 * @return the particular app's developer's email address
	 */
	public User getDeveloper() {
		return this.developer;
	}

	/**
	 * The getter for an app object's unique identifying keyword in the Appstore
	 * @return the particular app's identifying keyword
	 */
	public String getAppId() {
		return this.appId;
	}

	/**
	 * The getter for an app object's proper title
	 * @return the particular app's name
	 */
	public String getAppName() {
		return this.appName;
	}

	/**
	 * The getter for an app object's category variable
	 * @return the particular app's category
	 */
	public String getCategory() {
		return this.category;
	}

	/**
	 * The getter for an app object's price
	 * @return the particular app's price
	 */
	public double getPrice() {
		return this.price;
	}

	/**
	 * The getter for an app object's relative time of uploading
	 * @return the miliseconds since Unix that the app was uploaded
	 */
	public long getUploadTimestamp() {
		return this.uploadTimestamp;
	}

	/**
	 * The method to increase an app's number of downloads. We assume each user
	 * will only try to download an app one time.
	 */
	public void download(User user) {
		this.downloads++;
	}

	/**
	 * Allows a user who has not already rated the app in question
	 * to assign their rating.
	 *
	 * @param User: the User object this rating is coming from
	 * @param rating: the rating (1-5) this user has assigned to the app
	 */
	public void rate(User user, short rating) throws IllegalArgumentException {
		boolean hasRate = false; //Flag for when user has already rated this App
		ArrayList<User> temp = this.rateList;
		for (int i = 0; i < temp.size(); i++)
		{
			User tempUser = temp.get(i);
			if (tempUser == user) hasRate = true;
		}
		if (hasRate == false)
		{
			this.rateTally += rating;
			this.raters++;
			this.rateList.add(user);
		}
		else throw new IllegalArgumentException("User has already rated this app!");
	}

	/**
	 * The getter for an app object's number of downloads
	 * @return the number of times this app has been downloaded
	 */
	public long getTotalDownloads() {
		return this.downloads;
	}

	/**
	 * The getter for an app object's overall average rating
	 * @return the average score an app has been assigned by all rated users
	 */
	public double getAverageRating() {
		if (this.raters != 0) {
			this.rating = (this.rateTally / this.raters);
		}
		return this.rating;
	}

	/**
	 * The getter for an app object's total revenue from its downloads
	 * @return the total amount of money people have paid for this app
	 */
	public double getRevenueForApp() {
		revenue = this.downloads * this.price;
		return this.revenue;
	}

	/**
	 * The getter for an app object's "score" 
	 * @return the value determined by the app's average rating and downloads
	 */
	public double getAppScore() {
		if (this.downloads == 0) {
			return 0.0;
		}
		else return (this.getAverageRating() * Math.log(1 + this.downloads));
	}

	/**
	 * Compares one app object to another for the purpose of sorting based on
	 * each apps' respective timestamp value. Used for determining which app
	 * has been uploaded most recently.
	 *
	 * @param otherApp: the app object that will be compared
	 * @return The indicator representing one of each possible case in the app
	 * comparison. Positive if this app is older than otherApp, negative if
	 * this app is younger than otherApp, and zero if they were uploaded at the
	 * same time.
	 */
	@Override
	public int compareTo(App otherApp) {
		if (this.uploadTimestamp > otherApp.uploadTimestamp) {
			return -1;
		}
		if (this.uploadTimestamp == otherApp.uploadTimestamp) {
			return 0;
		}
		else return 1;
	}
}

