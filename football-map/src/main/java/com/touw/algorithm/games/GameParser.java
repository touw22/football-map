package com.touw.algorithm.games;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import com.opencsv.CSVReader;
import com.touw.algorithm.utilities.DatabaseWorker;

public class GameParser 
{
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("EEEEMMM.dd");

	public void parseFile() throws IOException, ParseException, SQLException
	{
		DatabaseWorker worker = new DatabaseWorker();
		//worker.createGameTable(); //you should do this the first time
		worker.flushGameTable();
		
		InputStream stream = this.getClass().getClassLoader().getResourceAsStream("fbssitemap.csv");
		CSVReader reader = new CSVReader(new InputStreamReader(stream));
		String [] nextLine;
	     while ((nextLine = reader.readNext()) != null) 
	     {
	    	 //ignore away games, ignore header row, ignore open dates
	        if (!nextLine[3].startsWith("at ") && !nextLine[2].equals("Date") && !nextLine[3].equals("Open Date"))
	        {
	        	Calendar c = Calendar.getInstance();
	        	String standard = nextLine[2].replaceAll("\\s","").replaceAll("\\n", "").replaceAll("Thurs.", "Thursday").replaceAll("Sept.", "Sep.").replaceAll("Wednes.", "Wednesday").replaceAll("Thursdayay", "Thursday").replaceAll("May", "May.");
				c.setTime(dateFormat.parse(standard));

	        	Game g = new Game();
	        	g.date = c;
	        	
	        	//only want Thursday through Sunday games
	        	if (g.getDOW().equals("Thursday") || g.getDOW().equals("Friday") || g.getDOW().equals("Saturday") || g.getDOW().equals("Sunday"))
	        	{
		        	g.homeTeam = nextLine[0];
	
		        	String[] stadiumParts = nextLine[3].split("\n");
		        	g.awayTeam = stadiumParts[0];
		        	g.setStadium(stadiumParts[1]);
		        	
		        	worker.insertGame(g);
		        	System.out.println(g.toString());
	        	}
	        }
	     }
        reader.close();
        
        worker.close();
	}

}
