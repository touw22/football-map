package com.touw.algorithm;

import java.math.BigDecimal;

public class Route implements Comparable<Route>
{
	//private List<Leg> legs;
	private int fridaycount = 0;
	private int saturdaycount = 0;
	private int sundaycount = 0;
	private Leg FridayLeg = null;
	private Leg SaturdayLeg = null;
	private Leg SundayLeg = null;
	private long fitness = 0;
	private int index = 0;
	
	public Route(int index)
	{
		this.index = index;
	}
	
	public void clear()
	{
		this.fridaycount = 0;
		this.saturdaycount = 0;
		this.sundaycount = 0;
		this.fitness = 0;
	}
	
	public void addLeg(Leg leg) throws Exception
	{
		switch(leg.legType)
		{
		case Leg.TYPE_FRIDAY_LEG:
			fridaycount++;
			if (fridaycount > 1) throw new Exception("You can't have more than one Thursday to Friday Leg");
			FridayLeg = leg;
			fitness += leg.duration;
			break;
		case Leg.TYPE_SATURDAY_LEG:
			saturdaycount++;
			if (saturdaycount > 1) throw new Exception("You can't have more than one Friday to Saturday Leg");
			SaturdayLeg = leg;
			fitness += leg.duration * 2;
			break;
		case Leg.TYPE_SUNDAY_LEG:
			sundaycount++;
			if (sundaycount > 1) throw new Exception("You can't have more than one Saturday to Sunday Leg");
			SundayLeg = leg;
			break;
		}
		
	}

	public long getFitness() {
		return fitness;
	}

	public int compareTo(Route o) 
	{
		return new Long(this.fitness).compareTo(new Long(o.fitness));
	}

	@Override
	public String toString() 
	{
		String games = "";
		games += FridayLeg.origin.toString() + "\n";
		games += "Hours: " + new BigDecimal((double)FridayLeg.duration * 0.000277778).setScale(1, BigDecimal.ROUND_HALF_UP) + "\n";
		games += FridayLeg.destination.toString() + "\n";
		//games += SaturdayLeg.origin.toString() + "\n";
		games += "Hours: " + new BigDecimal((double)SaturdayLeg.duration * 0.000277778).setScale(1, BigDecimal.ROUND_HALF_UP) + "\n";
		games += SaturdayLeg.destination.toString() + "\n";
		
		String out = "Total Hours: " + new BigDecimal((double)this.fitness * 0.000277778).setScale(1, BigDecimal.ROUND_HALF_UP) + "\n" +
				"\n" + games;
		
		return out;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
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
		Route other = (Route) obj;
		if (index != other.index)
			return false;
		return true;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	
}
