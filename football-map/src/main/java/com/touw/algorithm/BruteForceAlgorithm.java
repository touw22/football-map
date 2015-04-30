package com.touw.algorithm;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.touw.algorithm.utilities.DatabaseWorker;

public class BruteForceAlgorithm 
{
	public TreeSet<Route> buildRoutes(String weekID) throws Exception
	{
		DatabaseWorker worker = new DatabaseWorker();
		Map<Integer,List<Leg>> legs = worker.getWeeksLegs(weekID);
		
		List<Leg> fridays = legs.get(Leg.TYPE_FRIDAY_LEG);
		List<Leg> saturdays = legs.get(Leg.TYPE_SATURDAY_LEG);
		//List<Leg> sundays = legs.get(Leg.TYPE_SUNDAY_LEG);
		
		int count = 0;
		TreeSet<Route> allRoutes = new TreeSet<Route>();
		for (Leg f : fridays)
		{
			for (Leg s : saturdays)
			{
				Route r = new Route(count);
				r.addLeg(f);
				if (s.origin.equals(f.destination))
				{
					r.addLeg(s);
					allRoutes.add(r);
					count++;
				}
			}
		}
		
		worker.close();
		return allRoutes;
	}
	
	public void printTop(TreeSet<Route> routes, int topAmount, int weekNumber)
	{
		
		System.out.println("\n\n|||||||||||||||||||||||||||| Week: " + weekNumber + " ||||||||||||||||||||||||||||");
		System.out.println("-------------------------- Top Option:");
		int times = 0;
		for (Route r : routes)
		{
			if (times >= topAmount) break;
			System.out.println(r.toString());
			if (times + 1 != topAmount) 
			{
				System.out.println("-------------------------- Alternate:");
			}
			
			times++;
		}
	}

}
