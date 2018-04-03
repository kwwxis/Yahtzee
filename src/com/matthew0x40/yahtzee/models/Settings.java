package com.matthew0x40.yahtzee.models;

/**
 * Holds Game Settings.
 */
public class Settings {
	public int dice_sides = 6;
	public int dice_per_hand = 5;
	public int throws_per_hand = 3;
	public int min_of_kind = 3;
	public int max_of_kind = 4;
	public int min_straight = 4;
	public int max_straight = 5;
	public int score_for_fullhouse = 25;
	public int score_for_yahtzee = 50;
	
	/**
	 * Constructs a Settings object with the default settings.
	 */
	public Settings() {}
	
	/**
	 * Constructs a Settings object with the given parameters set to their relative property.
	 * All properties remain default.
	 * 
	 * @param dice_sides
	 * @param dice_per_hand
	 * @param throws_per_hand
	 */
	public Settings(int dice_sides, int dice_per_hand, int throws_per_hand) {
		this.dice_sides = dice_sides;
		this.dice_per_hand = dice_per_hand;
		this.throws_per_hand = throws_per_hand;
	}

	/**
	 * Returns a new Settings object with the default settings.
	 */
	public static Settings defaultSettings() {
		return new Settings();
	}

}
