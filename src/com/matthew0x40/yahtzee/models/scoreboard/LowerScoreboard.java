package com.matthew0x40.yahtzee.models.scoreboard;

import static com.matthew0x40.yahtzee.Util.incrementOrOne;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.matthew0x40.yahtzee.models.Dice;
import com.matthew0x40.yahtzee.models.Hand;
import com.matthew0x40.yahtzee.models.Settings;

/**
 * Represents the Lower Scoreboard; i.e. X of Kind, Full House, Straights, Chance, and YAHTZEE
 */
public class LowerScoreboard extends Scoreboard {
	private static final List<String> required_scores = Arrays.asList("FULLHOUSE", "CHANCE", "YAHTZEE");
	public final Scoreboard kd;
	public final Scoreboard st;
	
	public LowerScoreboard(Settings s) {
		super(s);
		this.children.add(this.kd = new Kind(s));
		this.children.add(this.st = new Straight(s));
	}

	@Override
	public boolean isComplete() {
		boolean a = true; // required_scores.stream().allMatch(key -> this.hasScore(key) == true);
		for (String k : required_scores) {
			if (!this.hasScore(k)) {
				a = false;
				break;
			}
		}
		boolean b = kd.isComplete();
		boolean c = st.isComplete();
		
		/*if (a)
			System.out.println("LOWER SCOREBOARD IS COMPLETE");
		if (b)
			System.out.println("X OF A KIND SCOREBOARD IS COMPLETE");
		if (c)
			System.out.println("STRAIGHT SCOREBOARD IS COMPLETE");
		*/
		return a && b && c;
	}

	@Override
	public int getTotal() {
		int total = kd.getTotal() + st.getTotal();
		total += required_scores.stream().mapToInt(key -> getScore(key)).sum();
		return total;
	}

	@Override
	public List<ScoringOption> receive(Hand hand, List<ScoringOption> ret) {
		Map<Integer,Integer> number_count = new HashMap<>();
		
		for (int i = 1; i <= s.dice_sides; i++) {
			number_count.put(i, 0);
		}
		
		int total_sum = 0;
		for (Dice d : hand.getAll()) {
			total_sum += d.number;
			incrementOrOne(number_count, d.number);
		}
		
		// Check for X of Kinds
		ret.addAll(kd.receive(hand));
		
		// Check for Full House
		Integer k0 = groupOfAtLeast(number_count, 5); // count(die value) >= 5
		if (k0 != null) {
			ret.add(new ScoringOption(this, "Full House", "FULLHOUSE", s.score_for_fullhouse));
		} else {
			Integer k1 = groupOfAtLeast(number_count, 3); // count(die value) >= 3
			Integer k2 = groupOfAtLeast(number_count, 2, k1); // count (die value) >= 2
			if (k1 != null && k2 != null) {
				ret.add(new ScoringOption(this, "Full House", "FULLHOUSE", s.score_for_fullhouse));
			} else {
				ret.add(new ScoringOption(this, "Full House", "FULLHOUSE", 0));
			}
		}
		
		// Check for Straights
		ret.addAll(st.receive(hand));
		
		// Check for chance
		ret.add(new ScoringOption(this, "Chance", "CHANCE", total_sum));
		
		// Check for Yahtzee
		Integer ky = largestGroupOf(number_count, s.dice_per_hand);
		if (ky != null) {
			ret.add(new ScoringOption(this, "Yahtzee", "YAHTZEE", s.score_for_yahtzee));
		} else {
			ret.add(new ScoringOption(this, "Yahtzee", "YAHTZEE", 0));
		}
		
		return ret.stream().filter(o -> o.isAvailable()).collect(Collectors.toList());
	}
	
	protected static Integer largestGroupOf(Map<Integer,Integer> number_count, int x) {
		Integer key = null;
		int value = 0;
		for (Map.Entry<Integer, Integer> e : number_count.entrySet()) {
			if (e.getValue() == x) {
				int my_value = Dice.multiply(e.getKey(), e.getValue());
				if (my_value > value) {
					value = my_value;
					key = e.getKey();
				}
			}
		}
		return key;
	}

	protected static Integer groupOfAtLeast(Map<Integer,Integer> number_count, int x) {
		Integer key = null;
		for (Map.Entry<Integer, Integer> e : number_count.entrySet()) {
			if (e.getValue() >= x) {
				key = e.getKey();
			}
		}
		return key;
	}
	
	protected static Integer groupOfAtLeast(Map<Integer,Integer> number_count, int x, Integer not_key) {
		Integer key = null;
		for (Map.Entry<Integer, Integer> e : number_count.entrySet()) {
			if (e.getValue() >= x && !e.getKey().equals(not_key)) {
				key = e.getKey();
			}
		}
		return key;
	}
	
	@Override
	public List<ScoringOption> toScoreList() {
		List<ScoringOption> sl = new ArrayList<ScoringOption>();
		sl.addAll(kd.toScoreList());
		sl.add(new ScoringOption(this, "Full House", "FULLHOUSE", getScore("FULLHOUSE")));
		sl.addAll(st.toScoreList());
		sl.add(new ScoringOption(this, "Chance", "CHANCE", getScore("CHANCE")));
		sl.add(new ScoringOption(this, "Yahtzee", "YAHTZEE", getScore("YAHTZEE")));
		return sl;
	}
	
	// Scoreboard for X of Kind scores
	public static class Kind extends Scoreboard {
		public static final String KEY_NAME = "KIND";
		
		public Kind(Settings s) {
			super(s);
		}

		@Override
		public boolean isComplete() {
			for (int x = s.min_of_kind; x <= s.max_of_kind; x++) {
				if (!hasScore(formatKey(KEY_NAME, x))) {
					return false;
				}
			}
			return true;
		}

		@Override
		public int getTotal() {
			int total = 0;
			for (int x = s.min_of_kind; x <= s.max_of_kind; x++) {
				total += getScore(formatKey(KEY_NAME, x));
			}
			return total;
		}

		@Override
		public List<ScoringOption> receive(Hand hand, List<ScoringOption> ret) {
			Map<Integer,Integer> number_count = new HashMap<>();
			
			for (int i = 1; i <= s.dice_sides; i++) {
				number_count.put(i, 0);
			}
			
			int total_sum = 0;
			for (Dice d : hand.getAll()) {
				total_sum += d.number;
				incrementOrOne(number_count, d.number);
			}
			
			Map<Integer, ScoringOption> add_map = new HashMap<>();
			
			for (int x = s.max_of_kind; x >= s.min_of_kind; x--) {
				add_map.put(x, getScoringOption(x, 0));
			}
			
			for (int x = s.max_of_kind; x >= s.min_of_kind; x--) {
				boolean should_break = false;
				for (Map.Entry<Integer, Integer> e : number_count.entrySet()) {
					if (e.getValue() == x) {
						add_map.put(e.getValue(), getScoringOption(e.getValue(), total_sum));
						
						// add scoring options for everything below
						// (ex: if we got a 4 of kind, then we also got a 3 of kind)
						// and then we can break because we're check from max of kind down
						for (int i = e.getValue()-1; i >= s.min_of_kind; i--) {
							add_map.put(i, getScoringOption(i, total_sum));
						}
						
						should_break = true;
						break;
					}
				}
				if (should_break) {
					break;
				}
			}
			
			for (Map.Entry<Integer, ScoringOption> e : add_map.entrySet()) {
				ret.add(e.getValue());
			}
			
			return ret;
		}
		
		private ScoringOption getScoringOption(int x, int score) {
			return new ScoringOption(this, Dice.byNumber(x).name + " of a kind", formatKey(KEY_NAME, x), score);
		}

		@Override
		public List<ScoringOption> toScoreList() {
			List<ScoringOption> sl = new ArrayList<>();
			for (int x = s.min_of_kind; x <= s.max_of_kind; x++) {
				sl.add(getScoringOption(x, getScore(formatKey(KEY_NAME, x))));
			}
			return sl;
		}
		
	}
	
	// Scoreboard for Straight scores
	public static class Straight extends Scoreboard {
		public static final String KEY_NAME = "STRAIGHT";

		public Straight(Settings s) {
			super(s);
		}

		@Override
		public boolean isComplete() {
			for (int x = s.min_straight; x <= s.max_straight; x++) {
				if (!hasScore(formatKey(KEY_NAME, x))) {
					return false;
				}
			}
			return true;
		}

		@Override
		public int getTotal() {
			int total = 0;
			for (int x = s.min_straight; x <= s.max_straight; x++) {
				total += getScore(formatKey(KEY_NAME, x));
			}
			return total;
		}

		@Override
		public List<ScoringOption> receive(Hand hand, List<ScoringOption> ret) {
			// remove duplicates, sort, and then reverse
			List<Dice> reversed = new ArrayList<>(new HashSet<>(hand.getAll()));
			reversed = Dice.sorted(reversed);
			Collections.reverse(reversed);
			
			for (int x = s.min_straight; x <= s.max_straight; x++) {
				ScoringOption opt = getScoringOption(x, 0);
				
				if (reversed.size() >= x) {
					for (int i = 0; i <= (reversed.size() - x); i++) {
						int offset_max = reversed.get(i).number;
						boolean fail = false;
						
						for (int j = 0; j < x; j++) {
							if ((offset_max-j) != reversed.get(i+j).number) {
								fail = true;
								break;
							}
						}
						
						if (!fail) {
							opt = getScoringOption(x, null);
							break;
						}
					}
				}
				
				ret.add(opt);
			}
			
			return ret;
		}

		@Override
		public List<ScoringOption> toScoreList() {
			List<ScoringOption> sl = new ArrayList<>();
			for (int x = s.min_straight; x <= s.max_straight; x++) {
				sl.add(getScoringOption(x, getScore(formatKey(KEY_NAME, x))));
			}
			return sl;
		}
		
		/**
		 * Returns a ScoringOption for a straight of x
		 * 
		 * @param x
		 * @return
		 */
		private ScoringOption getScoringOption(int x, Integer score) {
			String name = null;
			
			if (x == 4) {
				name = "Small Straight";
				if (score == null)
					score = 30;
			} else if (x == 5) {
				name = "Large Straight";
				if (score == null)
					score = 40;
			} else {
				name = "Straight of " + x;
				if (score == null)
					score = 40;
			}
			
			return new ScoringOption(this, name, formatKey(KEY_NAME, x), score);
		}
		
	}
	
}
