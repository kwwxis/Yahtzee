package com.matthew0x40.yahtzee.models.scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.matthew0x40.yahtzee.Util.*;

import com.matthew0x40.yahtzee.models.Hand;
import com.matthew0x40.yahtzee.models.Settings;

/**
 * Represents a Scoreboard node. A Scoreboard can be made up of more Scoreboard nodes.
 * 
 * Use ScoreboardFactory to create a new blank Scoreboard for gameplay.
 * 
 * A Scoreboard should be responsible for all of its direct children. The isComplete, getTotal, receive, and toScoreList
 * functions need to account for their children (if any) as currently the RootScoreboard does not do that automatically
 */
public abstract class Scoreboard {
	protected Settings s;
	protected ArrayList<Scoreboard> children = new ArrayList<Scoreboard>();
	protected HashMap<String, Integer> internal = new HashMap<String,Integer>();
	
	public Scoreboard(Settings s) {
		this.s = s;
	}
	
	/**
	 * Returns true if this scoreboard and all its children Scoreboards report that they are complete.
	 */
	public abstract boolean isComplete();
	
	/**
	 * Returns the total score as the addition of the score of this scoreboard and all scores of all children Scoreboards.
	 */
	public abstract int getTotal();
	
	protected abstract List<ScoringOption> receive(Hand hand, List<ScoringOption> ret);
	
	/**
	 * Returns a list of ScoringOptions given the provided hand. Use ScoringOption.realize() on the
	 * score you want to score with.
	 * 
	 * @param hand
	 * @return
	 */
	public final List<ScoringOption> receive(Hand hand) {
		List<ScoringOption> ret = new ArrayList<>();
		return receive(hand, ret);
	}
	
	/**
	 * Returns the list of all current scores in this scoreboard that have been scored.
	 * @return
	 */
	public abstract List<ScoringOption> toScoreList();
	
	// These functions are for putting a score in this scoreboard
	
	protected void putScore(String key, int value) {
		this.internal.put(formatKey(key, null), value);
	}
	protected void putScore(String key, int key_type, int value) {
		this.internal.put(formatKey(key, Integer.toString(key_type)), value);
	}
	protected void putScore(String key, String key_type, int value) {
		this.internal.put(formatKey(key, key_type), value);
	}

	// These functions are for retrieving a score from this scoreboard.
	// Only pertains this scoreboard, not any children.
	
	protected int getScore(String key) {
		return (int) ifNull(this.internal.get(formatKey(key, null)), Integer.valueOf(0));
	}
	protected int getScore(String key, int key_type) {
		return (int) ifNull(this.internal.get(formatKey(key, Integer.toString(key_type))), Integer.valueOf(0));
	}
	protected int getScore(String key, String key_type) {
		return (int) ifNull(this.internal.get(formatKey(key, key_type)), Integer.valueOf(0));
	}
	
	// These functions are for checking if a score exists in this scoreboard.
	// Only pertains to this scoreboard, not any children.
	
	protected boolean hasScore(String key) {
		return this.internal.containsKey(formatKey(key, null));
	}
	protected boolean hasScore(String key, int key_type) {
		return this.internal.containsKey(formatKey(key, Integer.toString(key_type)));
	}
	protected boolean hasScore(String key, String key_type) {
		return this.internal.containsKey(formatKey(key, key_type));
	}
	
	/**
	 * Format a key that has a key type for the hasScore, getScore and putScore functions
	 * @param key For example, "UPPER_SCORE"
	 * @param key_type For example, "5"
	 * @return
	 */
	public static final String formatKey(String key, String key_type) {
		return key + "__" + (key_type == null ? "" : key_type);
	}
	
	/**
	 * Format a key that has a key type for the hasScore, getScore and putScore functions
	 * @param key For example, "UPPER_SCORE"
	 * @param key_type For example, 5
	 * @return
	 */
	public static final String formatKey(String key, int key_type) {
		return key + "__" + Integer.toString(key_type);
	}
}
