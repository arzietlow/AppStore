///////////////////////////////////////////////////////////////////////////////
//
//Title:            AppStore
//Files:            App, AppRating, AppScoreComparator, AppStore, AppStoreDB
//					User
//Semester:         CS 367 Fall 2015
//
//Author:           Andrew Zietlow
//Email:            arzietlow@wisc.edu
//CS Login:         azietlow
//Lecturer's Name: 	Jim Skrentny
//Lab Section:      Lecture 1
//
//Pair Partner:     N/A
//
//External Help: None
////////////////////////////80 columns wide //////////////////////////////////

import java.time.Instant;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;


/**
 * Creates and utilizes an AppStore object to represent and process information. 
 * The user and app information is read from four input text files and then 
 * the program continues to process a set of user commands until terminated.
 * 
 * <p>Bugs: None known
 *
 * @author Andrew Zietlow (partially)
 */
public class AppStore {

	private static AppStoreDB appStoreDB = new AppStoreDB();
	private static User appUser = null;
	private static Scanner scanner = null;

	public static void main(String args[]) {
		if (args.length < 4) {			
			System.err.println("Bad invocation! Correct usage: "
					+ "java AppStore <UserDataFile> <CategoryListFile> "
					+ "<AppDataFile> <AppActivityFile>");
			System.exit(1);
		}

		boolean didInitialize = 
				initializeFromInputFiles(args[0], args[1], args[2], args[3]);

		if(!didInitialize) {
			System.err.println("Failed to initialize the application!");
			System.exit(1);
		}

		System.out.println("Welcome to the App Store!\n"
				+ "Start by browsing the top free and the top paid apps "
				+ "today on the App Store.\n"
				+ "Login to download or upload your favorite apps.\n");

		processUserCommands();
	}

	/**
	 * Reads input from each of the four necessary files to piece together
	 * all essential components of the AppStore database. Each file is in a 
	 * very particular configuration that is crucial to the way it is read.
	 *
	 * @param userDataFile: filename, contains components of User objects
	 * @param categoryListFile: filename, contains the valid list of categories
	 * @param appDataFile: filename, contains components of App objects
	 * @param appActivityFile: filename, contains actions used on App objects
	 * 
	 * @return true if all files have been found and read successfully
	 */
	private static boolean initializeFromInputFiles(String userDataFile, String 
			categoryListFile, String appDataFile, String appActivityFile) {

		//Initializing data structures using UserData.txt
		File userFile = new File(userDataFile);
		Scanner scnr = null;
		try {
			scnr = new Scanner(userFile);
		} catch (FileNotFoundException e) {
			System.out.println("File " + userFile + " not found");
			System.exit(1);
		}
		while (scnr.hasNext()) {
			String newUser = scnr.nextLine();
			String[] tokens = newUser.split("[,]");
			appStoreDB.addUser(tokens[0], tokens[1], tokens[2], tokens[3],
					tokens[4], tokens[5]);
			//Splits line up at commas into 6 separate strings
		}
		scnr.close();

		//Initializing data structures using CategoryList.txt
		File catFile = new File(categoryListFile);
		Scanner catScan = null;
		try {
			catScan = new Scanner(catFile);
		} catch (FileNotFoundException e) {
			System.out.println("File " + catFile + " not found");
			System.exit(1);
		}
		catScan.useDelimiter(",");
		while (catScan.hasNext()) {
			String category = catScan.nextLine();
			appStoreDB.addCategory(category);
		}
		catScan.close();

		//Initializing data structures using AppData.txt
		File appFile = new File(appDataFile);
		Scanner appScan = null;
		try {
			appScan = new Scanner(appFile);
		} catch (FileNotFoundException e) {
			System.out.println("File " + appFile + " not found");
			System.exit(1);
		}
		while (appScan.hasNext()) {
			String newApp = appScan.nextLine();
			String[] tokens = newApp.split("[,]");
			User developer = appStoreDB.findUserByEmail(tokens[0]);
			double price = Double.parseDouble(tokens[4]);
			long uploadTimeStamp = Long.parseLong(tokens[5]);
			appStoreDB.uploadApp(developer, tokens[1], tokens[2], tokens[3],
					price, uploadTimeStamp);
		}
		appScan.close();

		//Initializing data structures using AppActivity.txt
		File appFile2 = new File(appActivityFile);
		Scanner appActiv = null;
		try {
			appActiv = new Scanner(appFile2);
		} catch (FileNotFoundException e1) {
			System.out.println("File " + appFile2 + " not found");
			System.exit(1);
		}
		while (appActiv.hasNext()) {
			String newLog = appActiv.nextLine();
			String[] tokens = newLog.split("[,]");
			User user = appStoreDB.findUserByEmail(tokens[1]);
			App app = appStoreDB.findAppByAppId(tokens[2]);
			if (tokens[0].equals("r")) {
				short rating = Short.parseShort(tokens[3]);
				app.rate(user,  rating);
			}
			else {
				app.download(user);
				user.download(app);
			}
		}
		appActiv.close();
		return true; //Only gets to this point if the files have been found
	}				 

	private static void processUserCommands() {
		scanner = new Scanner(System.in);
		String command = null;		
		do {
			if (appUser == null) {
				System.out.print("[anonymous@AppStore]$ ");
			} else {
				System.out.print("[" + appUser.getEmail().toLowerCase() 
						+ "@AppStore]$ ");
			}
			command = scanner.next();
			switch(command.toLowerCase()) {
			case "l":
				processLoginCommand();
				break;

			case "x": 
				processLogoutCommand();
				break;

			case "s":
				processSubscribeCommand();
				break;

			case "v":
				processViewCommand();
				break;

			case "d":
				processDownloadCommand();
				break;

			case "r":
				processRateCommand();
				break;

			case "u":
				processUploadCommand();
				break;

			case "p":
				processProfileViewCommand();
				break;								

			case "q":
				System.out.println("Quit");
				break;
			default:
				System.out.println("Unrecognized Command!");
				break;
			}
		} while (!command.equalsIgnoreCase("q"));
		scanner.close();
	}


	private static void processLoginCommand() {
		if (appUser != null) {
			System.out.println("You are already logged in!");
		} else {
			String email = scanner.next();
			String password = scanner.next();
			appUser = appStoreDB.loginUser(email, password);
			if (appUser == null) {
				System.out.println("Wrong username / password");
			}
		}
	}

	private static void processLogoutCommand() {
		if (appUser == null) {
			System.out.println("You are already logged out!");
		} else {
			appUser = null;
			System.out.println("You have been logged out.");
		}
	}

	private static void processSubscribeCommand() {
		if (appUser == null) {
			System.out.println("You need to log in "
					+ "to perform this action!");
		} else {
			if (appUser.isDeveloper()) {
				System.out.println("You are already a developer!");
			} else {
				appUser.subscribeAsDeveloper();
				System.out.println("You have been promoted as developer");
			}
		}
	}

	private static void processViewCommand() {
		String restOfLine = scanner.nextLine();
		Scanner in = new Scanner(restOfLine);
		String subCommand = in.next();
		int count;
		String category;
		switch(subCommand.toLowerCase()) {
		case "categories":
			System.out.println("Displaying list of categories...");
			List<String> categories = appStoreDB.getCategories();
			count = 1;
			for (String categoryName : categories) {
				System.out.println(count++ + ". " + categoryName);
			}
			break;
		case "recent":				
			category = null;
			if (in.hasNext()) {
				category = in.next();
			} 
			displayAppList(appStoreDB.getMostRecentApps(category));				
			break;
		case "free":
			category = null;
			if (in.hasNext()) {
				category = in.next();
			}
			displayAppList(appStoreDB.getTopFreeApps(category));
			break;
		case "paid":
			category = null;
			if (in.hasNext()) {
				category = in.next();
			}
			displayAppList(appStoreDB.getTopPaidApps(category));
			break;
		case "app":
			String appId = in.next();
			App app = appStoreDB.findAppByAppId(appId);
			if (app == null) {
				System.out.println("No such app found with the given app id!");
			} else {
				displayAppDetails(app);
			}
			break;
		default: 
			System.out.println("Unrecognized Command!");
		}
		in.close();
	}

	private static void processDownloadCommand() {
		if (appUser == null) {
			System.out.println("You need to log in "
					+ "to perform this action!");
		} else {
			String appId = scanner.next();
			App app = appStoreDB.findAppByAppId(appId);
			if (app == null) {
				System.out.println("No such app with the given id exists. "
						+ "Download command failed!");
			} else {
				try {
					appStoreDB.downloadApp(appUser, app);
					System.out.println("Downloaded App " + app.getAppName());
				} catch (Exception e) {				
					System.out.println("Something went wrong. "
							+ "Download command failed!");
				}
			}
		}

	}

	private static void processRateCommand() {
		if (appUser == null) {
			System.out.println("You need to log in "
					+ "to perform this action!");
		} else {
			String appId = scanner.next();
			App app = appStoreDB.findAppByAppId(appId);
			if (app == null) {
				System.out.println("No such app with the given id exists. "
						+ "Rating command failed!");
			} else {
				try {
					short rating = scanner.nextShort();
					appStoreDB.rateApp(appUser, app, rating);
					System.out.println("Rated app " + app.getAppName());
				} catch (Exception e) {
					System.out.println("Something went wrong. "
							+ "Rating command failed!");
				}
			}
		}

	}

	private static void processUploadCommand() {
		if (appUser == null) {
			System.out.println("You need to log in "
					+ "to perform this action!");
		} else {
			String appName = scanner.next();
			String appId = scanner.next();
			String category = scanner.next();
			double price = scanner.nextDouble();
			long uploadTimestamp = Instant.now().toEpochMilli();
			try {
				appStoreDB.uploadApp(appUser, appId, appName, category, 
						price, uploadTimestamp);
			} catch (Exception e) {
				System.out.println("Something went wrong. "
						+ "Upload command failed!");
			}
		}
	}

	private static void processProfileViewCommand() {		
		String restOfLine = scanner.nextLine();
		Scanner in = new Scanner(restOfLine);
		String email = null;
		if (in.hasNext()) {
			email = in.next();
		}
		if (email != null) {
			displayUserDetails(appStoreDB.findUserByEmail(email));
		} else {
			displayUserDetails(appUser);
		}
		in.close();

	}

	private static void displayAppList(List<App> apps) {
		if (apps.size() == 0) {
			System.out.println("No apps to display!");
		} else {
			int count = 1;
			for(App app : apps) {
				System.out.println(count++ + ". " 
						+ "App: " + app.getAppName() + "\t" 
						+ "Id: " + app.getAppId() + "\t" 
						+ "Developer: " + app.getDeveloper().getEmail());
			}	
		}
	}

	private static void displayAppDetails(App app) {
		if (app == null) {
			System.out.println("App not found!");
		} else {
			System.out.println("App name: " + app.getAppName());
			System.out.println("App id: " + app.getAppId());
			System.out.println("Category: " + app.getCategory());
			System.out.println("Developer Name: " 
					+ app.getDeveloper().getFirstName() + " " 
					+ app.getDeveloper().getLastName());
			System.out.println("Developer Email: " 
					+ app.getDeveloper().getEmail());
			System.out.println("Total downloads: " + app.getTotalDownloads());
			System.out.println("Average Rating: " + app.getAverageRating());

			// show revenue from app if the logged-in user is the app developer
			if (appUser != null && 
					appUser.getEmail()
					.equalsIgnoreCase(app.getDeveloper().getEmail())) {
				System.out.println("Your Revenue from this app: $" 
						+ app.getRevenueForApp());
			}

		}		
	}

	private static void displayUserDetails(User user) {		
		if (user == null) {
			System.out.println("User not found!");
		} else {
			System.out.println("User name: " + user.getFirstName() + " "
					+ user.getLastName());
			System.out.println("User email: " + user.getEmail());
			System.out.println("User country: " + user.getCountry());

			// print the list of downloaded apps
			System.out.println("List of downloaded apps: ");			
			List<App> downloadedApps = user.getAllDownloadedApps();
			displayAppList(downloadedApps);

			// print the list of uploaded app
			System.out.println("List of uploaded apps: ");
			List<App> uploadedApps = user.getAllUploadedApps();
			displayAppList(uploadedApps);

			// show the revenue earned, if current user is developer
			if (appUser != null 
					&& user.getEmail().equalsIgnoreCase(appUser.getEmail()) 
					&& appUser.isDeveloper()) {
				double totalRevenue = 0.0;
				for (App app : uploadedApps) {
					totalRevenue += app.getRevenueForApp();
				}
				System.out.println("Your total earnings: $" + totalRevenue);
			}

		}
	}
}
