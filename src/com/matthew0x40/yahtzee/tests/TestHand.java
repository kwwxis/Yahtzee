package com.matthew0x40.yahtzee.tests;

import org.junit.*;

import com.matthew0x40.yahtzee.models.Dice;
import com.matthew0x40.yahtzee.models.Hand;
import com.matthew0x40.yahtzee.models.Settings;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

public class TestHand {
	
	@Test
	public void testHandRoll() {
		Hand hand = new Hand(Settings.defaultSettings());
		
		assertEquals(hand.getSettings().throws_per_hand, hand.rollsLeft());
		int throws_left = hand.getSettings().throws_per_hand;
		
		do {
			hand.roll();
			assertEquals(hand.getSettings().dice_per_hand, hand.getDiscard().size());
			assertEquals(--throws_left, hand.rollsLeft());
		} while(hand.rollsLeft() > 0);
		
		assertEquals(0, hand.getKeep().size());
		
		List<Dice> list = hand.getAll();
		assertEquals(hand.getSettings().dice_per_hand, list.size());
		
		hand.keep(list.get(0));
		
		assertEquals(hand.getSettings().dice_per_hand, hand.getAll().size());
		assertEquals(hand.getSettings().dice_per_hand - 1, hand.getDiscard().size());
		assertEquals(1, hand.getKeep().size());
		
		hand.discard(hand.getKeep().get(0));
		
		assertEquals(hand.getSettings().dice_per_hand, hand.getAll().size());
		assertEquals(hand.getSettings().dice_per_hand, hand.getDiscard().size());
		assertEquals(0, hand.getKeep().size());
	}
	
	@Test
	public void testHandKeep() {
		Hand hand = new Hand(Settings.defaultSettings(), Arrays.asList(Dice.FIVE, Dice.FIVE, Dice.THREE, Dice.FOUR, Dice.FOUR));
		hand.keep(5, 4, 4, 4, 4);
		
		List<Dice> discard = Dice.sorted(hand.getDiscard());
		List<Dice> keep = Dice.sorted(hand.getKeep());
		
		assertEquals(2, discard.size()); // [3,5]
		assertEquals(3, keep.size()); // [4,4,5]
		
		assertEquals(Dice.THREE, discard.get(0));
		assertEquals(Dice.FIVE, discard.get(1));
		
		assertEquals(Dice.FOUR, keep.get(0));
		assertEquals(Dice.FOUR, keep.get(1));
		assertEquals(Dice.FIVE, keep.get(2));
	}
	
}
