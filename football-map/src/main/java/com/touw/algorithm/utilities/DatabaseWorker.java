package com.touw.algorithm.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.touw.algorithm.Leg;
import com.touw.algorithm.games.Game;

public class DatabaseWorker 
{
	Connection c = null;

	public DatabaseWorker()
	{
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:football-games.db");
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    //System.out.println("Opened database successfully");
	}
	
	public void close() throws SQLException
	{
		c.close();
	}
	
	public Map<Integer,List<Leg>> getWeeksLegs(String weekID) throws SQLException, ParseException
	{
		Map<Integer,List<Leg>> weeksLegs = new HashMap<Integer,List<Leg>>();
		Statement stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery( "select * from MATRIX where week = '" + weekID + "';" );
		while ( rs.next() ) {
			String  odateStr = rs.getString("ORIGINGAMETIME");
			String oaway = rs.getString("ORIGINAWAY").trim();
			String ohome = rs.getString("ORIGINHOME").trim();
			String ostadium = rs.getString("ORIGINADDRESS").trim();
			
			String  ddateStr = rs.getString("DESTINATIONGAMETIME");
			String daway = rs.getString("DESTINATIONAWAY").trim();
			String dhome = rs.getString("DESTINATIONHOME").trim();
			String dstadium = rs.getString("DESTINATIONADDRESS").trim();
			
			long duration = rs.getLong("DURATION");

			Game origin = Game.fromDateString(odateStr);
			origin.awayTeam = oaway;
			origin.homeTeam = ohome;
			origin.setStadium(ostadium);
			
			Game destination = Game.fromDateString(ddateStr);
			destination.awayTeam = daway;
			destination.homeTeam = dhome;
			destination.setStadium(dstadium);
			
			Leg leg = new Leg(origin, destination, duration);

			if (weeksLegs.containsKey(leg.legType))
			{
				weeksLegs.get(leg.legType).add(leg);
			}
			else
			{
				ArrayList<Leg> legs = new ArrayList<Leg>();
				legs.add(leg);
				weeksLegs.put(leg.legType, legs);
			}
		}
		
		rs.close();
		stmt.close();
		
		return weeksLegs;
	}
	
	public Map<String,List<Game>> getWeeksGames(String weekID) throws SQLException, ParseException
	{
		Map<String,List<Game>> weeksGames = new HashMap<String,List<Game>>();
		Statement stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery( "select DATE, AWAY, HOME, STADIUM from games where week = '" + weekID + "' and stadium NOT LIKE '%Honolulu%';" );
		while ( rs.next() ) {
			String  dateStr = rs.getString("DATE");
			String away = rs.getString("AWAY").trim();
			String home = rs.getString("HOME").trim();
			String stadium = rs.getString("STADIUM").trim();

			Game g = Game.fromDateString(dateStr);
			g.awayTeam = away;
			g.homeTeam = home;
			g.setStadium(stadium);

			if (weeksGames.containsKey(g.getDOW())) 
			{
				weeksGames.get(g.getDOW()).add(g);
			}
			else
			{
				ArrayList<Game> games = new ArrayList<Game>();
				games.add(g);
				weeksGames.put(g.getDOW(), games);
			}
		}
		rs.close();
		stmt.close();
		
		return weeksGames;
	}
	
	public void insertRoute(String weekId, Game origin, Game destination, long duration) throws SQLException
	{
		PreparedStatement prep = c.prepareStatement("INSERT INTO MATRIX " +
	                   "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);"); 
	    
	    prep.setString(2, weekId);
	    prep.setString(3, origin.getDOW());
	    prep.setString(4, origin.getDateString());
	    prep.setString(5, destination.getDOW());
	    prep.setString(6, destination.getDateString());
	    prep.setString(7, origin.getStadium());
	    prep.setString(8, origin.awayTeam);
	    prep.setString(9, origin.homeTeam);
	    prep.setString(10, destination.getStadium());
	    prep.setString(11, destination.awayTeam);
	    prep.setString(12, destination.homeTeam);
	    prep.setLong(13, duration);
	    prep.execute();
	    prep.close();
	    //c.commit();
	}
	
	public void insertGame(Game game) throws SQLException
	{
		PreparedStatement prep = c.prepareStatement("INSERT INTO GAMES " +
	                   "VALUES (?,?,?,?,?,?,?);"); 
	    
	    prep.setString(2, game.getWeekID());
	    prep.setString(3, game.getDateString());
	    prep.setString(4, game.getDOW());
	    prep.setString(5, game.awayTeam);
	    prep.setString(6, game.homeTeam);
	    prep.setString(7, game.getStadium());
	    
	    prep.execute();
	    prep.close();
	    //c.commit();
	}
	
	public void createGameTable() throws SQLException
	{
		Statement stmt = c.createStatement();
		String sql = "CREATE TABLE GAMES " +
	                   "(ID INTEGER PRIMARY KEY     AUTOINCREMENT," +
	                   " WEEK           TEXT    NOT NULL, " + 
	                   " DATE           TEXT    NOT NULL, " + 
	                   " DOW           TEXT    NOT NULL, " + 
	                   " AWAY           TEXT    NOT NULL, " + 
	                   " HOME           TEXT    NOT NULL, " + 
	                   " STADIUM           TEXT    NOT NULL)"; 
	      stmt.executeUpdate(sql);
	      stmt.close();
	      //c.commit();
	}
	
	public void createMatrixTable() throws SQLException
	{
		Statement stmt = c.createStatement();
		String sql = "CREATE TABLE MATRIX " +
	                   "(ID INTEGER PRIMARY KEY     AUTOINCREMENT," +
	                   " WEEK           TEXT    NOT NULL, " + 
	                   " ORIGINDOW           TEXT    NOT NULL, " + 
	                   " ORIGINGAMETIME           TEXT    NOT NULL, " + 
	                   " DESTINATIONDOW           TEXT    NOT NULL, " + 
	                   " DESTINATIONGAMETIME           TEXT    NOT NULL, " + 
	                   " ORIGINADDRESS        TEXT    NOT NULL, " + 
	                   " ORIGINAWAY        TEXT    NOT NULL, " + 
	                   " ORIGINHOME        TEXT    NOT NULL, " + 
	                   " DESTINATIONADDRESS        TEXT    NOT NULL, " + 
	                   " DESTINATIONAWAY        TEXT    NOT NULL, " + 
	                   " DESTINATIONHOME        TEXT    NOT NULL, " + 
	                   " DURATION         INTEGER    NOT NULL)"; 
	      stmt.executeUpdate(sql);
	      stmt.close();
	      //c.commit();
	}
	
	public void flushGameTable() throws SQLException
	{
		Statement stmt = c.createStatement();
		String sql = "DELETE FROM GAMES"; 
	    stmt.executeUpdate(sql);
	    stmt.close();
	    //c.commit();
	}
	
	public void flushMatrixTable(String weekId) throws SQLException
	{
		Statement stmt = c.createStatement();
		String sql = "DELETE FROM MATRIX where week = '" + weekId + "';"; 
	    stmt.executeUpdate(sql);
	    stmt.close();
	    //c.commit();
	}
	
	public void deleteMatrixTable() throws SQLException
	{
		Statement stmt = c.createStatement();
		String sql = "DROP TABLE MATRIX"; 
	    stmt.executeUpdate(sql);
	    stmt.close();
	    //c.commit();
	}
}
