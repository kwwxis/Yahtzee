package com.matthew0x40.yahtzee.game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.matthew0x40.yahtzee.Util;
import com.matthew0x40.yahtzee.models.Dice;
import com.matthew0x40.yahtzee.models.Hand;
import com.matthew0x40.yahtzee.models.scoreboard.Scoreboard;
import com.matthew0x40.yahtzee.models.scoreboard.ScoringOption;
import com.matthew0x40.yahtzee.models.Settings;

/**
 * A 1 Player game through the Console
 */
public class Game1PlayerConsole extends Game {
	private Scanner in;

	public Game1PlayerConsole() {
		super(getSettings(), "player");
        this.in = new Scanner(System.in);
	}
	
	public Game1PlayerConsole(Settings s) {
		super(s, "player");
        this.in = new Scanner(System.in);
	}

	@Override
	public boolean receive(String p, Scoreboard scoreboard, Hand hand) {
		hand.getDiscard().addAll(hand.getKeep());
		hand.getKeep().clear();
		
		System.out.println("Die Numbers Rolled: " + Dice.listAsString(hand.getDiscard()));
		
		if (hand.rollsLeft() == 0) {
			System.out.println("===================================");
			System.out.println("Here is your sorted hand: " + Dice.listAsString(hand.getAll()));
			
			List<ScoringOption> options = scoreboard.receive(hand);
			
			if (options.size() == 0) {
				return false;
			}
			
			for (int i = 0; i < options.size(); i++) {
				ScoringOption opt = options.get(i);
				System.out.println(Integer.toString(i + 1) + ": Score " + opt.value + " on the " + opt.display_name + " line");
			}
			
			System.out.println("Enter the # before the : of the option you want to score: ");
			int choice = askUntilPositiveNumber(this.in) - 1;
			while (choice < 0 || choice > options.size()-1) {
				System.out.println("Not an available choice! Try again: ");
				choice = askUntilPositiveNumber(this.in) - 1;
			}
			
			ScoringOption chosen_option = options.get(choice);
			chosen_option.realize();
			System.out.println("You scored " + chosen_option.value + " on the " + chosen_option.display_name + " line.");
			System.out.println("===================================");
			
			System.out.println("Here is your current scorecard: ");
			
			List<ScoringOption> scored = scoreboard.toScoreList();
			for (ScoringOption score : scored) {
				System.out.println(score.display_name + " : " + score.value);
			}
			
			System.out.println("===================================");
			
			return true;
		} else {
			while (true) {
				System.out.print("Enter the <Die Number> of each die you want to keep (separated by space): ");
				String s = this.in.nextLine().trim();
				
				if (s.isEmpty()) {
					break;
				} else {
					boolean fail = false;
					String[] sa = s.split(" ");
					ArrayList<Integer> keep_list = new ArrayList<>();
					
					for (String sn : sa) {
						try {
				            int n = Integer.parseInt(sn);
				            keep_list.add(n);
				        } catch(NumberFormatException nfe){
				            System.out.println("(Invalid Format! Try again)");
				            fail = true;
				        }
					}
					
					if (!fail) {
						hand.keep(Util.asIntegerArray(keep_list));
						return false;
					}
				}
			}
		}
		
		return false;
	}

	@Override
	public void gameOver() {
		System.out.println("===================================");
		System.out.print("Enter (y/n) to play again: ");
		if (this.in.next().trim().equals("y")) {
			System.out.println("===================================");
			System.out.println("Playing again..");
			new Game1PlayerConsole(this.s).autoStart();
		} else {
			System.out.println("===================================");
			System.out.println("Goodbye.");
		}
	}

	@Override
	public void gameStart() {
		
	}
	
	public static Settings getSettings() {
		File cfg = new File("yahtzeeConfig.txt");
		
		Settings s = Settings.defaultSettings();
	    int dice_sides = s.dice_sides;
	    int dice_per_hand = s.dice_per_hand;
	    int throws_per_hand = s.throws_per_hand;
		try (BufferedReader br = new BufferedReader(new FileReader(cfg))) {
		    String line;
		    int n = 1;
		    
		    while ((line = br.readLine()) != null) {
		    	if (n == 1) {
		    		dice_sides = Integer.parseInt(line.trim());
		    	} else if (n == 2) {
		    		dice_per_hand = Integer.parseInt(line.trim());
		    	} else if (n == 3) {
		    		throws_per_hand = Integer.parseInt(line.trim());
		    	}
		    	n++;
		    }
		    
		    s = new Settings(dice_sides, dice_per_hand, throws_per_hand);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("you are playing with " + s.dice_per_hand + " " + s.dice_sides + "-sided dice");
		System.out.println("you get "+s.throws_per_hand+" rolls per hand");
		System.out.println("Enter 'y' if you'd like to change these settings: ");
		
		Scanner sc = new Scanner(System.in);

		if (sc.nextLine().trim().equals("y")) {
			System.out.println("enter sides on die: ");
			dice_sides = askUntilPositiveNumber(sc);
			
			System.out.println("enter dice per hand: ");
			dice_per_hand = askUntilPositiveNumber(sc);
			
			System.out.println("enter # of rolls per hand: ");
			throws_per_hand = askUntilPositiveNumber(sc);
		    
		    s = new Settings(dice_sides, dice_per_hand, throws_per_hand);
			System.out.println("you are playing with " + s.dice_per_hand + " " + s.dice_sides + "-sided dice");
			System.out.println("you get "+s.throws_per_hand+" rolls per hand");
		    
		    FileWriter fooWriter = null;
			try {
				fooWriter = new FileWriter(cfg, false);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		    try {
				fooWriter.write(dice_sides+"\n"+dice_per_hand+"\n"+throws_per_hand);
			} catch (IOException e) {
				e.printStackTrace();
			}
		    try {
				fooWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return s;
	}
	
	public static int askUntilPositiveNumber(Scanner sc) {
		Integer i = null;
		while (true) {
			try {
				i = Integer.parseInt(sc.nextLine().trim());
				if (i < 1) {
					System.out.println("Not a positive number! Try again: ");
					continue;
				}
				break;
			} catch(NumberFormatException e) {
				System.out.println("Not a positive number! Try again: ");
			}
		}
		return i;
	}
	
}