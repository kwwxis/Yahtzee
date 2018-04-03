package com.matthew0x40.yahtzee.models.scoreboard;

import static com.matthew0x40.yahtzee.Util.addOrSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.matthew0x40.yahtzee.models.Dice;
import com.matthew0x40.yahtzee.models.Hand;
import com.matthew0x40.yahtzee.models.Settings;

/**
 * Represents the upper scoreboard; i.e. ones, twos, threes, ...
 */
public class UpperScoreboard extends Scoreboard {
	public static final String KEY_NAME = "UPPER_SCORE";
	
	public UpperScoreboard(Settings s) {
		super(s);
	}

	@Override
	public boolean isComplete() {
		for (int i = 1; i <= s.dice_sides; i++) {
			if (!this.hasScore(formatKey(KEY_NAME, i))) {
				return false;
			}
		}
		//System.out.println("UPPER SCOREBOARD IS COMPLETE");
		return true;
	}
	
	@Override
	public List<ScoringOption> receive(Hand hand, List<ScoringOption> ret) {
		Map<Integer,Integer> number_count = new HashMap<>(); // Dice # -> Score
		
		// initialize each dice side to 0
		for (int i = 1; i <= s.dice_sides; i++) {
			number_count.put(i, 0);
		}
		
		// populate scores
		for (Dice d : hand.getAll()) {
			addOrSet(number_count, d.number, d.number);
		}
		
		// populate scoring options
		for (int i = 1; i <= s.dice_sides; i++) {
			ret.add(getScoringOption(i, number_count.get(i)));
		}
		
		// filter out unavailable options and return
		return ret.stream().filter(o -> o.isAvailable()).collect(Collectors.toList());
	}

	@Override
	public int getTotal() {
		int score = 0;
		for (int i = 1; i <= s.dice_sides; i++) {
			score += this.getScore(KEY_NAME, i);
		}
		return score;
	}

	@Override
	public List<ScoringOption> toScoreList() {
		List<ScoringOption> sl = new ArrayList<>();
		for (int i = 1; i <= s.dice_sides; i++) {
			sl.add(getScoringOption(i, getScore(formatKey(KEY_NAME, i))));
		}
		return sl;
	}
	
	private ScoringOption getScoringOption(int i, int score) {
		return new ScoringOption(this, Dice.byNumber(i).plural_name, formatKey(KEY_NAME, i), score);
	}
	
}
