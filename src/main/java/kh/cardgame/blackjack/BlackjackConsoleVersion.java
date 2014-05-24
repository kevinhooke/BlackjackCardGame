package kh.cardgame.blackjack;

import kh.cardgame.io.Output;
import kh.cardgame.io.impl.SystemOutput;

/**
 * Command line version of Blackjack.
 * 
 * Uses SystemOutput version of Output to direct output to System.out.
 * 
 * @author Kevin Hooke
 *
 */
public class BlackjackConsoleVersion {

	public static void main(String[] args) {
		Output systemOutput = new SystemOutput();
		BlackjackGame game = new BlackjackGame();
		game.setOutput(systemOutput);
		game.startGame();
	}

}
