package com.matthew0x40.yahtzee.game;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.matthew0x40.yahtzee.models.Hand;
import com.matthew0x40.yahtzee.models.scoreboard.Scoreboard;
import com.matthew0x40.yahtzee.models.scoreboard.ScoreboardFactory;
import com.matthew0x40.yahtzee.models.Settings;

/**
 * This "Game" class provides an interface for the entire Yahtzee to abstract away most of the logic
 * in order to focus on the user interface in classes that extend this class.
 */
public abstract class Game {
	protected Map<String,Scoreboard> scoreboards = new HashMap<>();
	protected boolean running = true;
	protected final Settings s;
	
	/**
	 * Construct a Game object from a given settings and set of players
	 * 
	 * @param s Settings
	 * @param player_ids Unique list of player IDs
	 */
	public Game(Settings s, List<String> player_ids) {
		this.s = s;
		
		// Create a Scoreboard for each player
		for (String player_id : player_ids) {
			scoreboards.put(player_id, ScoreboardFactory.newScoreboard(s));
		}
		
		gameStart();
	}

	/**
	 * Construct a Game object from a given settings and set of players
	 * 
	 * @param s Settings
	 * @param player_ids Unique list of player IDs
	 */
	public Game(Settings s, String... player_ids) {
		this(s, Arrays.asList(player_ids));
	}
	
	/**
	 * This method loops over all (player => scoreboards) sets
	 * and executes the turn for each player.
	 *  
	 * @return
	 */
	public boolean nextTurn() {
		boolean game_complete = true;
		
		for (Map.Entry<String,Scoreboard> s : scoreboards.entrySet()) {
			String player_id = s.getKey();
			Scoreboard scoreboard = s.getValue();
			
			Hand hand = new Hand(this.s);
			while (hand.rollsLeft() > 0) {
				hand.roll();
				if (this.receive(player_id, scoreboard, hand) || !this.running) {
					break;
				}
			}
			
			if (!scoreboard.isComplete()) {
				game_complete = false;
			}
		}
		
		if (game_complete) {
			gameOver();
		}
		
		return !game_complete;
	}
	
	/**
	 * This method is called after each roll.
	 * 
	 * @param player_id The player who rolled
	 * @param scoreboard The player's scoreboard
	 * @param hand The hand
	 * @return should return true if the turn is over, false otherwsie
	 */
	public abstract boolean receive(String player_id, Scoreboard scoreboard, Hand hand);
	
	/**
	 * This method is called upon game start.
	 */
	public void gameStart() {
		
	}
	
	/**
	 * This method is called upon game end.
	 */
	public void gameOver() {
		
	}
	
	/**
	 * Starts the game.
	 */
	public void autoStart() {
		if (!scoreboards.isEmpty()) {
			while (this.running && nextTurn());
		}
		this.running = false;
	};
	
	/**
	 * Change the running state of the game.
	 * @param state
	 */
	public void setRunning(boolean state) {
		this.running = state;
	}
	
	/**
	 * Checks if the game is currently running.
	 * @return
	 */
	public boolean isRunning() {
		return this.running;
	}
	
}
