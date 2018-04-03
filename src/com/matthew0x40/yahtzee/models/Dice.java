package com.matthew0x40.yahtzee.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/** Represents a six-sided die.
 *	See {@link #roll}
 */
public enum Dice {
	ONE("one", 1),
	TWO("two", 2),
	THREE("three", 3),
	FOUR("four", 4),
	FIVE("five", 5),
	SIX("six", 6),
	SEVEN("seven", 7),
	EIGHT("eight", 8),
	NINE("nine", 9),
	TEN("ten", 10),
	ELEVEN("eleven", 11),
	TWELVE("twelve", 12),
	THIRTEEN("thirteen", 13),
	FOURTEEN("fourteen", 14),
	FIFTEEN("fifteen", 15),
	SIXTEEN("sixteen", 16),
	SEVENTEEN("seventeen", 17),
	EIGHTEEN("eighteen", 18),
	NINETEEN("nineteen", 19),
	TWENTY("twenty", 20),
	TWENTYONE("twentyone", 21),
	TWENTYTWO("twentytwo", 22),
	TWENTYTHREE("twentythree", 23),
	TWENTYFOUR("twentyfour", 24),
	TWENTYFIVE("twentyfive", 25),
	TWENTYSIX("twentysix", 26),
	TWENTYSEVEN("twentyseven", 27),
	TWENTYEIGHT("twentyeight", 28),
	TWENTYNINE("twentynine", 29),
	THIRTY("thirty", 30)
	;
	
	public final String name;
	public final String plural_name;
	public final int number;
	
	Dice(String name, int number) {
		this.name = name;
		if (name.endsWith("x")) {
			this.plural_name = name + "es";
		} else {
			this.plural_name = name + "s";
		}
		this.number = number;
	}

	/**
	 * Get a Dice object from the given name
	 * @param number
	 */
	public static Dice byName(String name) {
		for (Dice d : Dice.values()) {
			if (name.equals(d.name) || name.equals(d.name+"s") || name.equals(d.name+"es")) {
				return d;
			}
		}
		return null;
	}
	
	/**
	 * Get a Dice object from the given number
	 * @param number
	 */
	public static Dice byNumber(int number) {
		for (Dice d : Dice.values()) {
			if (d.number == number) {
				return d;
			}
		}
		return null;
	}
	
	/**
	 * Roll a die.
	 * @return Random Dice between ONE and SIX
	 */
	public static Dice roll(int sides) {
		int randomNum = ThreadLocalRandom.current().nextInt(1, sides + 1);
		return byNumber(randomNum);
	}
	
	/**
	 * Returns the sum of the given die numbers
	 * @param dices
	 */
	public static int sum(Dice...dices) {
		int sum = 0;
		for (int i = 0; i < dices.length; i++) {
			sum += dices[i].number;
		}
		return sum;
	}

	/**
	 * Returns the sum of the given die numbers
	 * @param dices
	 */
	public static int sum(List<Dice> dices) {
		int sum = 0;
		for (int i = 0; i < dices.size(); i++) {
			sum += dices.get(i).number;
		}
		return sum;
	}

	/**
	 * Returns the number of the Dice object with its name matching 'd' multiplied by 'i'
	 * @param d
	 * @param i
	 */
	public static int multiply(String d, int i) {
		return Dice.byName(d).number * i;
	}

	/**
	 * Returns the number of 'd' multiplied by 'i'
	 * @param d
	 * @param i
	 */
	public static int multiply(Dice d, int i) {
		return d.number * i;
	}

	/**
	 * Returns the number of 'd' multiplied by 'i'
	 * @param d
	 * @param i
	 */
	public static int multiply(int d, int i) {
		return Dice.byNumber(d).number * i;
	}
	
	/**
	 * Returns a sorted version of the given list of Dice. Does
	 * not modify the passed in list.
	 * 
	 * @param list
	 */
	public static List<Dice> sorted(List<Dice> list) {
		ArrayList<Dice> sorted = new ArrayList<Dice>(list);
        Collections.sort(sorted, new DiceComparator());
        return sorted;
	}
	
	public static class DiceComparator implements Comparator<Dice> {
	    @Override
	    public int compare(Dice a, Dice b) {
	        return a.number < b.number ? -1 : a.number == b.number ? 0 : 1;
	    }
	}
	
	/**
	 * Like {@link #byNumber}, but for a list of numbers.
	 */
	public static List<Dice> asDiceList(int...is) {
		List<Dice> dice_list = new ArrayList<>();
		for (int i : is) {
			dice_list.add(Dice.byNumber(i));
		}
		return dice_list;
	}
	
	/**
	 * Returns a string of dice numbers based on the given dice 'list' concatenated by spaces
	 * @param list
	 * @return
	 */
	public static String listAsString(List<Dice> list) {
		return listAsString(list, " ");
	}
	
	/**
	 * Returns a string of dice numbers based on the given dice 'list' concatenated by 'sep'
	 * @param list
	 * @param sep
	 * @return
	 */
	public static String listAsString(List<Dice> list, String sep) {
		String s = "";
		for (int i = 0; i < list.size(); i++) {
			s += list.get(i).number;
			if (i != list.size()-1) {
				s += sep;
			}
		}
		return s;
	}
}
