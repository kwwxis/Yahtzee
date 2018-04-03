package com.matthew0x40.yahtzee.models;

import static com.matthew0x40.yahtzee.Util.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Hand in Yahtzee
 */
public class Hand {
	private Settings s;
	
	// The current throw of this hand (starting at 0)
	public int current_throw = 0;
	
	// List of dice to keep in next toss
	protected ArrayList<Dice> keep;
	
	// List of dice to be discard in next toss
	protected ArrayList<Dice> discard;
	
	/**
	 * Create a new hand.
	 * 
	 * @param s Game settings
	 */
	public Hand(Settings s) {
		this.s = s;
		this.keep = new ArrayList<>(s.dice_per_hand);
		this.discard = new ArrayList<>(s.dice_per_hand);
	}
	
	/**
	 * Initialize with a specific first throw.
	 * 
	 * @param s Game settings
	 * @param discard first throw
	 */
	public Hand(Settings s, List<Dice> discard) {
		this.s = s;
		this.keep = new ArrayList<>(s.dice_per_hand);
		this.discard = new ArrayList<>(discard);
		current_throw++;
	}

	/**
	 * Initialize with a specific first throw.
	 * 
	 * @param s Game settings
	 * @param is first throw
	 */
	public Hand(Settings s, int...is) {
		this(s, Dice.asDiceList(is));
	}
	
	/**
	 * Returns the game settings of this hand
	 * @return
	 */
	public Settings getSettings() {
		return this.s;
	}
	
	/**
	 * Roll the dice we're not keeping.
	 * 
	 * @return <b>true</b> if success<br/>
	 *   <b>false</b> if failure<br/>
	 *   Failure can be caused by:
	 *   <ul>
	 *     <li>no rolls left</li>
	 *     <li>no dice to roll (all dice are being kept)</li>
	 *   </ul>
	 */
	public boolean roll() {
		if (this.rollsLeft() == 0) {
			return false;
		}
		
		int dice_to_roll = s.dice_per_hand - keep.size();
		if (dice_to_roll <= 0) {
			this.current_throw++;
			return false;
		}
		
		this.discard.clear();
		this.current_throw++;
		
		for (int i = 0; i < dice_to_roll; i++) {
			this.discard.add(Dice.roll(s.dice_sides));
		}
		return true;
	}
	
	/**
	 * @return the number of throws left.
	 */
	public int rollsLeft() {
		return s.throws_per_hand - this.current_throw; 
	}
	
	/**
	 * 
	 * @return list of all dice: keep + discard
	 */
	public List<Dice> getAll() {
		return Dice.sorted(list_merge(this.keep, this.discard));
	}
	
	/**
	 * 
	 * @return the list of Dice to be kept through the next toss
	 */
	public List<Dice> getKeep() {
		return this.keep;
	}
	
	/**
	 * 
	 * @return the list of Dice to be discared in the next toss
	 */
	public List<Dice> getDiscard() {
		return this.discard;
	}
	
	/** 
	 * Removes dice from dice_list that are in diff
	 * not in a HashSet way, but in an exchange way.<br/><br/>
	 * 
	 * For example, if dice_list contains: [5, 5, 3, 4, 4]<br/>
	 * And diff contains: [5, 4, 4, 4, 4]<br/><br/>
	 * After this operation, dice_list will contain: [5, 3]<br/>
	 * And returned will be: [5, 4, 4]<br/>
	 * 
	 * @param dice_list
	 * @param dice_num
	 * @return dice split from dice_list
	 */
	private ArrayList<Dice> split(ArrayList<Dice> dice_list, int[] diff) {
		List<Integer> diff_list = asList(diff);
		
		ArrayList<Dice> split	= new ArrayList<Dice>();
		ArrayList<Dice> retain	= new ArrayList<Dice>();
		
		for (int i = 0; i < dice_list.size(); i++) {
			Dice dice  = dice_list.get(i);
			int number = dice.number;
			
			if (diff_list.contains(number)) {
				diff_list.remove(Integer.valueOf(number));
				split.add(dice);
			} else {
				retain.add(dice);
			}
		}
		
		dice_list.clear();
		dice_list.addAll(retain);
		
		return split;
	}
	
	/**
	 * Keep the dice with the given dice numbers.<br/>
	 * See {@link #split(List, int...)} on how this works.
	 * 
	 * @param dice_to_keep
	 * @return number of dice actually moved from discard to kept
	 */
	public int keep(int... dice_to_keep) {
		if (this.discard.isEmpty()) {
			return 0;
		}
		
		ArrayList<Dice> kept = this.split(this.discard, dice_to_keep);
		this.keep.addAll(kept);
		
		return kept.size();
	}
	
	/**
	 * See {@link #keep(int...)}
	 */
	public int keep(Dice... dice_to_keep) {
		int[] real_dice_to_keep = new int[dice_to_keep.length];
		for (int i = 0; i < dice_to_keep.length; i++) {
			real_dice_to_keep[i] = dice_to_keep[i].number;
		}
		return this.keep(real_dice_to_keep);
	}
	
	/**
	 * Discard the dice with the given dice numbers.</br>
	 * See {@link #split(List, int...)} on how this works.
	 * 
	 * @param dice_to_discard
	 * @return number of dice actually moved from keep to discard.
	 */
	public int discard(int... dice_to_discard) {
		if (this.keep.isEmpty()) {
			return 0;
		}
		
		List<Dice> discarded = this.split(this.keep, dice_to_discard);
		this.discard.addAll(discarded);
		
		return discarded.size();
	}
	
	/**
	 * See {@link #discard(int...)}
	 */
	public int discard(Dice... dice_to_discard) {
		int[] real_dice_to_discard = new int[dice_to_discard.length];
		for (int i = 0; i < dice_to_discard.length; i++) {
			real_dice_to_discard[i] = dice_to_discard[i].number;
		}
		return this.discard(real_dice_to_discard);
	}
	
}
