///////////////////////////////////////////////////////////////////////////////
//                  
// Main Class File:  AppStore.java
// File:             User.java
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

import java.util.List;
import java.util.ArrayList;

/**
 * This class is used to create User objects that contain relevant information 
 * about each registered User of the Appstore. The Appstore utilizes this class 
 * to modify App objects and to process certain commands.
 *
 * <p>Bugs: None known
 *
 * @author Andrew Zietlow
 */
public class User {

	private String email; //a user's email address (login ID)
	private String password; //a user's password
	private String firstName; //a user's first name
	private String lastName; //a user's last name
	private String country; //a user's country of origin
	private String type; //"user" or "developer" - determines upload privileges
	private List<App> uploaded; //the list of apps a user has uploaded
	private List<App> downloaded; //the list of apps a user has downloaded

	//Constructs a new User object
	public User(String email, String password, String firstName,
			String lastName, String country, String type)
					throws IllegalArgumentException {
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.country = country;
		this.type = type;
		this.uploaded = new ArrayList<App>();
		this.downloaded = new ArrayList<App>();
	}

	/**
	 * Getter for a particular user object's email value
	 * @return an email address
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Takes in a password string and compares it to the user in question's 
	 * actual password.
	 * @param testPassword: the attempted password value to compare with
	 * @return True if the entered password matches the user's stored password,
	 * or false if the passwords do not match.
	 */
	public boolean verifyPassword(String testPassword) {
		if (testPassword.equals(this.password)) return true;
		else return false;
	}

	/**
	 * Getter for a particular user object's firstName value
	 * @return a first name
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * Getter for a particular user object's lastName value
	 * @return a last name
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * Getter for a particular user object's country value
	 * @return a name or abbreviation of a country
	 */
	public String getCountry() {
		return this.country;
	}

	/**
	 * Used for determining whether or not a user has the ability to upload apps
	 * @return true if the user is registered as a developer, or false if not
	 */
	public boolean isDeveloper() {
		if (this.type.equals("developer")) return true;
		else return false;
	}

	/**
	 * Registers a user as a developer for the purpose of uploading apps
	 */
	public void subscribeAsDeveloper() {
		this.type = "developer";
	}

	/**
	 * Adds the given App object to this particular user object's list of 
	 * downloaded apps
	 * @param app: the App object that has been downloaded
	 */
	public void download(App app) { 
		this.downloaded.add(app);
	}

	/**
	 * Allows a user to add a new App object to the AppStore if they have not
	 * already added the app before
	 * @param app: the App object being uploaded to the AppStore
	 */
	public void upload(App app) {
		boolean hasUsed = false;
		for (int i = 0; i < this.uploaded.size(); i++){
			App temp = this.uploaded.get(i);
			if (temp == app) hasUsed = true; 
		}
		if (!hasUsed) this.uploaded.add(app);
	}

	/**
	 * Getter for the list of App objects that a particular user has downloaded
	 * @return a list of App objects
	 */
	public List<App> getAllDownloadedApps() {
		return this.downloaded;
	}

	/**
	 * Getter for the list of App objects that a particular user has uploaded
	 * @return a list of App objects
	 */
	public List<App> getAllUploadedApps() {
		return this.uploaded;
	}
}

