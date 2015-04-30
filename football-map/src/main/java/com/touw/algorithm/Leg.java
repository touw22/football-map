package com.touw.algorithm;

import com.touw.algorithm.games.Game;

public class Leg
{
	public Game origin;
	public Game destination;
	public long duration;
	public int legType;
	
	public static final int TYPE_FRIDAY_LEG = 0;
	public static final int TYPE_SATURDAY_LEG = 1;
	public static final int TYPE_SUNDAY_LEG = 2;
	
	public Leg(Game origin, Game destination, long duration)
	{
		this.origin = origin;
		this.destination = destination;
		this.duration = duration;
		
		if (origin.getDOW().equals("Thursday"))
		{
			this.legType = Leg.TYPE_FRIDAY_LEG;
		}
		else if (origin.getDOW().equals("Friday"))
		{
			this.legType = Leg.TYPE_SATURDAY_LEG;
		}
		else if (origin.getDOW().equals("Saturday"))
		{
			this.legType = Leg.TYPE_SUNDAY_LEG;
		}
		else
		{
			System.out.println("Invalid day: " + origin.getDOW());
		}
	}

	@Override
	public String toString() {
		return "Leg [origin=" + origin + ", destination=" + destination
				+ ", duration=" + duration + ", legType=" + legType + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + (int) (duration ^ (duration >>> 32));
		result = prime * result + legType;
		result = prime * result + ((origin == null) ? 0 : origin.hashCode());
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
		Leg other = (Leg) obj;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (duration != other.duration)
			return false;
		if (legType != other.legType)
			return false;
		if (origin == null) {
			if (other.origin != null)
				return false;
		} else if (!origin.equals(other.origin))
			return false;
		return true;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
}
