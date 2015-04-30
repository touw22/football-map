package com.touw.algorithm.utilities;


import java.util.List;
import java.util.Map;

import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;
import com.touw.algorithm.games.Game;

public class MapsHelper 
{
	private GeoApiContext context = null;
	
	public MapsHelper(String yourApiKey)
	{
		this.context = new GeoApiContext().setApiKey(yourApiKey);
	}
	
	public void populateDistancesForWeek(String weekId) throws Exception
	{
		DatabaseWorker worker = new DatabaseWorker();
		//worker.deleteMatrixTable();
		//worker.createMatrixTable(); //you should do this the first time
		worker.flushMatrixTable(weekId);
		Map<String,List<Game>> weeksGames = worker.getWeeksGames(weekId);

		List<Game> thursdays = weeksGames.get("Thursday");
		List<Game> fridays = weeksGames.get("Friday");
		List<Game> saturdays = weeksGames.get("Saturday");
		List<Game> sundays = weeksGames.get("Sunday");

		//Thursday to Friday
		if(thursdays != null && fridays != null)
		{
			for (Game tGame : thursdays)
			{
				for (Game fGame : fridays)
				{
					long duration = getDuration(tGame.getStadium(), fGame.getStadium());
					//System.out.println((double)duration * 0.000277778);
					if (duration != -1) worker.insertRoute(weekId, tGame, fGame, duration);
				}
			}
		}

		//Friday to Saturday
		if(fridays != null && saturdays != null)
		{
			for (Game fGame : fridays)
			{
				for (Game sGame : saturdays)
				{
					long duration = getDuration(fGame.getStadium(), sGame.getStadium());
					//System.out.println((double)duration * 0.000277778);
					if (duration != -1) worker.insertRoute(weekId, fGame, sGame, duration);
				}
			}
		}

		//Saturday to Sunday
		if(saturdays != null && sundays != null)
		{
			for (Game sGame : saturdays)
			{
				for (Game nflGame : sundays)
				{
					long duration = getDuration(sGame.getStadium(), nflGame.getStadium());
					//System.out.println((double)duration * 0.000277778);
					if (duration != -1) worker.insertRoute(weekId, sGame, nflGame, duration);
				}
			}
		}

		worker.close();
	}
	
	public long getDuration(String origin, String destination) throws Exception
	{
		String[] singleOrigin = { origin };
		String[] singleDestination = { destination };
		DistanceMatrixApiRequest req = DistanceMatrixApi.getDistanceMatrix(context, singleOrigin, singleDestination);
		req.mode(TravelMode.DRIVING);
		req.language("English");
		req.units(Unit.METRIC);
		DistanceMatrix matrix = req.await();
		DistanceMatrixRow row = matrix.rows[0];
		DistanceMatrixElement element = row.elements[0];
		if (element.duration == null)
		{
			System.out.println(origin + " to " + destination);
			//This happens when you try to drive from Aloha Stadium, lol
			return -1;
		}
		else
		{
			return element.duration.inSeconds;
		}
	}

}
