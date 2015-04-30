package com.touw.algorithm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TreeSet;

import com.touw.algorithm.utilities.MapsHelper;

public class AlgorithmDriver 
{
	public static void main(String[] args) 
	{
		//0917
		SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2015);
		c.set(Calendar.MONTH, 8);
		c.set(Calendar.DAY_OF_MONTH, 17);
		int weekNumber = 3;
		
		while(c.get(Calendar.MONTH) != 11)
		{
			String weekId = sdf.format(c.getTime());
			try {
				
				//runGetDistances(args[0], weekId);
			
				runAlgorithm(weekId, weekNumber);
				//runAlgorithm("1022");
				weekNumber++;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			c.add(Calendar.DATE, 7);
		}
	}
	
	public static void runGetDistances(String apiKey, String weekId) throws Exception
	{
		MapsHelper helper = new MapsHelper(apiKey);
		helper.populateDistancesForWeek(weekId);
	}
	
	public static void runAlgorithm(String weekId, int weekNumber) throws Exception
	{
		BruteForceAlgorithm bfa = new BruteForceAlgorithm();
		TreeSet<Route> routes = bfa.buildRoutes(weekId);
		bfa.printTop(routes, 5, weekNumber);
	}
}
