package com.matthew0x40.yahtzee.models.scoreboard;

/**
 * Represents a scoring option.
 */
public class ScoringOption {
	
	private final Scoreboard scoreboard;
	public final String display_name;
	public final String key;
	public final int value;
	
	public ScoringOption(Scoreboard sb, String display_name, String key, int value) {
		this.scoreboard = sb;
		this.display_name = display_name;
		this.key = key;
		this.value = value;
	}
	
	/**
	 * Returns the scoreboard that this ScoringOption was generated from upon receiving a hand.
	 */
	public Scoreboard getScoreboard() {
		return this.scoreboard;
	}
	
	/**
	 * Score the Scoreboard using this ScoringOption
	 */
	public void realize() {
		this.scoreboard.putScore(this.key, this.value);
	}
	
	/**
	 * Returns true if the Scoreboard has already been scored with the key of this ScoringOption
	 * @return
	 */
	public boolean isRealized() {
		return this.scoreboard.hasScore(this.key);
	}
	
	/**
	 * Returns true if the Scoreboard has not been scored with the key of this ScoringOption
	 * @return
	 */
	public boolean isAvailable() {
		return !this.isRealized();
	}
	
	@Override
	public String toString() {
		return display_name + " ["+key+"] : " + Integer.toString(this.value);
	}
	
}
