package com.touw.algorithm.games;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public class GameParserDriver {

	public static void main(String[] args) throws ParseException, SQLException 
	{
		GameParser gp = new GameParser();
		try {
			gp.parseFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
