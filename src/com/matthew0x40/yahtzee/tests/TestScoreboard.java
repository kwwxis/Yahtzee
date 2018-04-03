package com.matthew0x40.yahtzee.tests;

import org.junit.*;

import com.matthew0x40.yahtzee.models.Hand;
import com.matthew0x40.yahtzee.models.Settings;
import com.matthew0x40.yahtzee.models.scoreboard.Scoreboard;
import com.matthew0x40.yahtzee.models.scoreboard.ScoreboardFactory;
import com.matthew0x40.yahtzee.models.scoreboard.ScoringOption;
import com.matthew0x40.yahtzee.models.scoreboard.UpperScoreboard;

import static org.junit.Assert.*;

import java.util.List;

public class TestScoreboard {
	
	@Test
	public void testScoreboard() {
		Scoreboard board = ScoreboardFactory.newScoreboard(Settings.defaultSettings());
		
		// HAND #1
		// --------------------------------------------------
		List<ScoringOption> s1 = board.receive(new Hand(Settings.defaultSettings(), 5,5,4,4,4));
		assertEquals(5 + 5 + 4 + 4 + 4, get(s1, "CHANCE"));
		
		assertFalse(hasNumber(s1, 1));
		assertFalse(hasNumber(s1, 2));
		assertFalse(hasNumber(s1, 3));
		assertTrue(hasNumber(s1, 4));
		assertTrue(hasNumber(s1, 5));
		
		assertEquals(5 * 2, getNumber(s1, 5));
		assertEquals(4 * 3, getNumber(s1, 4));
		
		assertTrue(has(s1, "CHANCE"));
		assertTrue(has(s1, "FULLHOUSE"));
		assertTrue(has(s1, "KIND__3"));
		assertFalse(has(s1, "KIND__4"));
		assertFalse(has(s1, "STRAIGHT__4"));
		assertFalse(has(s1, "STRAIGHT__5"));
		assertFalse(has(s1, "YAHTZEE"));
		
		/* To run this part of the test that is commented out, change Scoreboard.getScore and Scoreboard.hasScore to public
		 * Note that uncommenting this will cause 'assertTrue(hasNumber(s2, 1))' to fail which is expected.
		ScoringOption sample = s1.get(0);
		assertTrue(s1.get(0).isAvailable());
		assertFalse(sample.getScoreboard().hasScore(sample.key));
		sample.realize();
		assertFalse(sample.isAvailable());
		assertTrue(sample.getScoreboard().hasScore(sample.key));
		assertEquals(sample.value, sample.getScoreboard().getScore(sample.key));
		*/
		
		// HAND #2
		// --------------------------------------------------
		List<ScoringOption> s2 = board.receive(new Hand(Settings.defaultSettings(), 1,2,3,4,5));
		
		assertTrue(hasNumber(s2, 1));
		assertTrue(hasNumber(s2, 2));
		assertTrue(hasNumber(s2, 3));
		assertTrue(hasNumber(s2, 4));
		assertTrue(hasNumber(s2, 5));
		
		assertTrue(has(s2, "CHANCE"));
		assertFalse(has(s2, "FULLHOUSE"));
		assertFalse(has(s2, "KIND__3"));
		assertFalse(has(s2, "KIND__4"));
		assertTrue(has(s2, "STRAIGHT__4"));
		assertTrue(has(s2, "STRAIGHT__5"));
		assertFalse(has(s2, "YAHTZEE"));

		// HAND #3
		// --------------------------------------------------
		List<ScoringOption> s3 = board.receive(new Hand(Settings.defaultSettings(), 1,2,4,3,4));
		
		assertTrue(hasNumber(s3, 1));
		assertTrue(hasNumber(s3, 2));
		assertTrue(hasNumber(s3, 3));
		assertTrue(hasNumber(s3, 4));
		assertFalse(hasNumber(s3, 5));

		assertTrue(has(s3, "CHANCE"));
		assertFalse(has(s3, "FULLHOUSE"));
		assertFalse(has(s3, "KIND__3"));
		assertFalse(has(s3, "KIND__4"));
		assertTrue(has(s3, "STRAIGHT__4"));
		assertFalse(has(s3, "STRAIGHT__5"));
		assertFalse(has(s3, "YAHTZEE"));
		
		// HAND #4
		// --------------------------------------------------
		assertTrue(has(board.receive(new Hand(Settings.defaultSettings(), 1,1,1,1,1)), "YAHTZEE"));
		assertTrue(has(board.receive(new Hand(Settings.defaultSettings(), 2,2,2,2,2)), "YAHTZEE"));
		assertTrue(has(board.receive(new Hand(Settings.defaultSettings(), 3,3,3,3,3)), "YAHTZEE"));
		assertTrue(has(board.receive(new Hand(Settings.defaultSettings(), 4,4,4,4,4)), "YAHTZEE"));
		assertTrue(has(board.receive(new Hand(Settings.defaultSettings(), 5,5,5,5,5)), "YAHTZEE"));
	}

	public int get(List<ScoringOption> list, String key) {
		for (ScoringOption o : list) {
			if (o.key.equals(key)) {
				return o.value;
			}
		}
		return 0;
	}
	
	public boolean hasNumber(List<ScoringOption> list, int x) {
		for (ScoringOption o : list) {
			if (o.key.equals(Scoreboard.formatKey(UpperScoreboard.KEY_NAME, x)) && o.value != 0) {
				return true;
			}
		}
		return false;
	}

	public int getNumber(List<ScoringOption> list, int x) {
		for (ScoringOption o : list) {
			if (o.key.equals(Scoreboard.formatKey(UpperScoreboard.KEY_NAME, x))) {
				return o.value;
			}
		}
		return 0;
	}
	
	public boolean has(List<ScoringOption> list, String key) {
		for (ScoringOption o : list) {
			if (o.key.equals(key) && o.value != 0) {
				return true;
			}
		}
		return false;
	}
	
	public int sum(int...is) {
		int sum = 0;
		for (int i : is) {
			sum += i;
		}
		return sum;
	}
	
}
