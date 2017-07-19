///////////////////////////////////////////////////////////////////////////////
//                  
// Main Class File:  AppStore.java
// File:             AppStoreComparator.java
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

import java.util.Comparator;

/**
 * The AppScoreComparator class implements the comparator utility to be used
 * in the sorting of app objects based on their "AppScore" variable.
 *
 * <p>Bugs: None known
 *
 * @author Andrew Zietlow
 */
public class AppScoreComparator implements Comparator<App> {

	/**
	 * Compares two app objects at a time to determine which of the two contains
	 * the highest appScore value. This method is used in sorting a list of 
	 * apps by placing the highest-scoring apps first.
	 *
	 * @param app1: the first of two apps being compared
	 * @param app2: the second of two apps being compared
	 * @return The indicator representing one of each possible case in the app
	 * comparison. Positive if app1's score is lower than app2's, negative if 
	 * app1's score is greater than app2's, and zero if they have the same 
	 * AppScore value.
	 */
	@Override
	public int compare(App app1, App app2) {
		if (app1.getAppScore() > app2.getAppScore()) return -1; 
		else if (app1.getAppScore() < app2.getAppScore()) return 1; 
		else return 0; 
	}

}

