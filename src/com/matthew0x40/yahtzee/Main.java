package com.matthew0x40.yahtzee;

import com.matthew0x40.yahtzee.game.*;

/**
 * Main - program entry point.
 * Doesn't do much except calling the start
 * method in the relevant Game object.
 */
public class Main {
	public static void main(String[] args) {
		new Game1PlayerGUIController().start();
	}
	
}
