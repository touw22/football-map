package com.touw.algorithm.games;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Game 
{
	public Calendar date;
	private String stadium;
	public String homeTeam;
	public String awayTeam;
	
	public static Game fromDateString(String dateStr) throws ParseException
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("E, d MMM");
		Calendar c = Calendar.getInstance();
		c.setTime(dateFormat.parse(dateStr));
		Game g = new Game();
		g.date = c;
		
		return g;
	}
	
	public String getWeekID()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMdd");
		
		if (getDOW().equals("Thursday"))
		{
			return dateFormat.format(date.getTime());
		}
		else if (getDOW().equals("Friday"))
		{
			Calendar thurs = Calendar.getInstance();
			thurs.setTime(date.getTime());
			thurs.add(Calendar.HOUR_OF_DAY, -24);
			return dateFormat.format(thurs.getTime());
		}
		else if (getDOW().equals("Saturday"))
		{
			Calendar thurs = Calendar.getInstance();
			thurs.setTime(date.getTime());
			thurs.add(Calendar.HOUR_OF_DAY, -48);
			return dateFormat.format(thurs.getTime());
		}
		else //Sunday
		{
			Calendar thurs = Calendar.getInstance();
			thurs.setTime(date.getTime());
			thurs.add(Calendar.HOUR_OF_DAY, -72);
			return dateFormat.format(thurs.getTime());
		}
	}
	
	public String getDateString()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("E, d MMM");
		return dateFormat.format(date.getTime());
	}
	
	public String getDOW()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE");
		return dateFormat.format(date.getTime());
	}

	public String getStadium() {
		return stadium;
	}

	public void setStadium(String stadium) {
		//All the stadiums that came in wrong...
		if (stadium.equals("Albertson's Stadium, Boise, ID"))
		{
			stadium = "1910 University Drive, Boise, ID 83706";
		}
		else if (stadium.equals("Foreman Field at S.B. Ballard Stadium"))
		{
			stadium = "Foreman Field, Norfolk, VA";
		}
		else if (stadium.equals("Stanford Stadium, Stanford, CA"))
		{
			stadium = "601 Nelson Rd, Stanford, CA 94305";
		}
		else if (stadium.equals("Bulldog Stadium, Fresno, CA"))
		{
			stadium = "1600 E Bulldog Ln, Fresno, CA 93710";
		}
		else if (stadium.equals("Rose Bowl, Los Angeles, CA"))
		{
			stadium = "1001 Rose Bowl Dr, Pasadena, CA 91103";
		}
		else if (stadium.equals("H.A. Chapman Stadium, Tulsa, OK"))
		{
			stadium = "3112 E 8th St, Tulsa, OK 74104";
		}
		else if (stadium.equals("Bright House Networks Stadium, Orlando, FL"))
		{
			stadium = "4465 Knights Victory Way, Orlando, FL 32816";
		}
		this.stadium = stadium;
	}

	@Override
	public String toString() {
		return getDateString() + ", " + awayTeam.trim() + " @ " + homeTeam.trim();// + " (" + stadium.trim() + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((awayTeam == null) ? 0 : awayTeam.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((homeTeam == null) ? 0 : homeTeam.hashCode());
		result = prime * result + ((stadium == null) ? 0 : stadium.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (awayTeam == null) {
			if (other.awayTeam != null)
				return false;
		} else if (!awayTeam.equals(other.awayTeam))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (homeTeam == null) {
			if (other.homeTeam != null)
				return false;
		} else if (!homeTeam.equals(other.homeTeam))
			return false;
		if (stadium == null) {
			if (other.stadium != null)
				return false;
		} else if (!stadium.equals(other.stadium))
			return false;
		return true;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
}
