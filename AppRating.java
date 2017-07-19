///////////////////////////////////////////////////////////////////////////////
//                  
// Main Class File:  AppStore.java
// File:             AppRating.java
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

/**
 *The AppRating class constructs AppRating objects that are useful for the
 *recording of Appstore activity that has taken place. It is an organizational
 *structure.
 *
 * <p>Bugs: This class is not implemented (I somehow never noticed it here)
 *
 * @author Andrew Zietlow
 */
public class AppRating {	
	private App app; //The app that has been rated
	private User user; //The user who has rated "app"
	private short rating; //The rating "user" applied to "app"
	
	//Constructor for a new AppRating object
	public AppRating(App app, User user, short rating) {
		this.app = app;
		this.user = user;
		this.rating = rating;
	}
	
	/**
	 * The getter for an AppRating's App object
	 * @return the particular App object relative to this AppRating object
	 */
	public App getApp() {
		return this.app;
	}
	
	/**
	 * The getter for an AppRating's User object
	 * @return the particular User object relative to this AppRating object
	 */
	public User getUser() {
		return this.user;
	}
	
	/**
	 * The getter for the rating assigned to a particular App/User combination
	 * @return the particular rating that relates the two objects
	 */
	public short getRating() {
		return this.rating;
	}
}

