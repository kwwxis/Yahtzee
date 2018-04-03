package com.matthew0x40.yahtzee.models.scoreboard;

import com.matthew0x40.yahtzee.models.Settings;

/**
 * The factory class for generating new scoreboards
 */
public class ScoreboardFactory {
	
	/**
	 * Create a new, blank scoreboard
	 * 
	 * @param s the settings to use
	 * @return the new scoreboard
	 */
	public static Scoreboard newScoreboard(Settings s) {
		Scoreboard board = new RootScoreboard(s);
		
		addBoard(board, UpperScoreboard.class);
		addBoard(board, LowerScoreboard.class);
		
		return board;
	}
	
	protected final static void addBoard(Scoreboard base, Class<? extends Scoreboard> child_class) {
		try {
			base.children.add(child_class.getConstructor(Settings.class).newInstance(base.s));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
