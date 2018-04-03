package com.matthew0x40.yahtzee.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JComboBox;

import com.matthew0x40.yahtzee.models.Dice;
import com.matthew0x40.yahtzee.models.Hand;
import com.matthew0x40.yahtzee.models.Settings;
import com.matthew0x40.yahtzee.models.scoreboard.Scoreboard;
import com.matthew0x40.yahtzee.models.scoreboard.ScoreboardFactory;
import com.matthew0x40.yahtzee.models.scoreboard.ScoringOption;

/**
 * This class controls the game, it handles interaction between
 * the models and the GUIManager object (view).
 */
public class Game1PlayerGUIController {
	protected GUIManager mgr;
	protected Settings s = Settings.defaultSettings();
	
	protected Scoreboard scoreboard = null;
	protected Hand current_hand = null;
	
	/**
	 * This method starts the game.
	 * Usage: <code>new Game1PlayerGUIController().start()</code>
	 */
	public void start() {
		mgr = new GUIManager(this);
		
		// adds action listener for the sides setting
		mgr.sides_chooser.addActionListener(new ActionListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
		        String selected = (String) ((JComboBox<String>) e.getSource()).getSelectedItem();
		        int value = Integer.parseInt(selected.split(" ")[0]);
		        
		        s.dice_sides = value;
		        Game1PlayerGUIController.this.restart();
			}
			
		});
		
		// adds action listener for the dice number setting
		mgr.dice_chooser.addActionListener(new ActionListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
		        String selected = (String) ((JComboBox<String>) e.getSource()).getSelectedItem();
		        int value = Integer.parseInt(selected.split(" ")[0]);
		        
		        s.dice_per_hand = value;
		        Game1PlayerGUIController.this.restart();
			}
			
		});
		
		// action listener for clicking the roll button
		mgr.rollbutton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				nextRoll();
			}
			
		});
		
		// action listener for the show scoreboard button
		mgr.sb_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (scoreboard == null) {
					return;
				}
				
				mgr.showScoreboardWindow(scoreboard);
			}
			
		});
		
		// reset
		restart();
		
		// show the window
		mgr.show();
	}
	
	/**
	 * Handles creating a hand (if needed) and rolling it for each roll.
	 */
	protected void nextRoll() {
		if (this.current_hand == null) {
			this.current_hand = new Hand(this.s);
		}
		
		this.current_hand.roll();
		this.receiveRoll(this.current_hand);
	}
	
	/**
	 * Restarts the game. This method is called from the start method,
	 * whenever a setting is changed, and from the "Replay" button
	 * when a game finishes
	 */
	protected void restart() {
		// reset game
		this.scoreboard = ScoreboardFactory.newScoreboard(this.s);
		this.current_hand = null;
		
		// reset gui
		mgr.rollbutton.setEnabled(true);
		mgr.resetScoreOpts(this.s);
		mgr.resetDicePanel(this.s);
	}
	
	/**
	 * Set a dice to be kept. This method
	 * is called from GUIManager when the
	 * user clicks on a die in the Discard row.
	 * 
	 * @param d
	 */
	protected void moveToKeep(Dice d) {
		if (current_hand != null) {
			current_hand.keep(d);
		}
	}
	
	/**
	 * Set a dice to be discarded. This method
	 * is called from the GUIManager when the
	 * user clicks on a die in the Keep row.
	 * 
	 * @param d
	 */
	protected void moveToDiscard(Dice d) {
		if (current_hand != null) {
			current_hand.discard(d);
		}
	}
	
	/**
	 * This method is called from nextRoll(), which is called from the roll
	 * button ActionListener.
	 * 
	 * This method talks with the GUIManager to handle populating the GUI
	 * after each roll.
	 * 
	 * @param hand
	 */
	protected void receiveRoll(Hand hand) {
		// this was the last roll, disable button
		if (hand.rollsLeft() == 0) {
			mgr.rollbutton.setEnabled(false);
		}
		
		// set dice in GUI
		if (hand.current_throw == 1) {
			// this was the first roll
			for (int i = 0; i < mgr.discard.size(); i++) {
				mgr.discard.get(i).d = hand.getDiscard().get(i);
				mgr.discard.get(i).repaint();
			}
			
			for (int i = 0; i < mgr.keep.size(); i++) {
				mgr.keep.get(i).d = null;
				mgr.keep.get(i).repaint();
			}
		} else {
			for (int i = 0, index = 0; i < mgr.discard.size(); i++) {
				GUIManager.MyButton agent = mgr.discard.get(i);
				
				if (agent.d == null) {
					continue;
				}
				
				agent.d = hand.getDiscard().get(index);
				agent.repaint();
				
				index++;
			}
			
			for (int i = 0, index = 0; i < mgr.keep.size(); i++) {
				GUIManager.MyButton agent = mgr.keep.get(i);
				
				if (agent.d == null) {
					continue;
				}
				
				agent.d = hand.getKeep().get(index);
				agent.repaint();
				
				index++;
			}
		}
		
		// pass scoring options to the gui manager
		List<ScoringOption> options = scoreboard.receive(hand);
		mgr.showOptions(options);
	}
	
	/**
	 * This method is called from GUIManager when the user
	 * clicks on which scoringoption they want to choose
	 * @param opt
	 */
	protected void receiveScoringOption(ScoringOption opt) {
		// reset stuff
		mgr.resetScoreOpts(s);
		mgr.clearDicePanel();
		
		// reenable roll button (if was disabled at all)
		mgr.rollbutton.setEnabled(true);
		
		// set to null so next roll will start a new hand
		this.current_hand = null;
		
		// score the option
		opt.realize();
		
		if (scoreboard.isComplete()) {
			mgr.showGameOver();
			mgr.rollbutton.setEnabled(false);
		}
	}
}
