package kh.cardgame.blackjack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import kh.cardgame.common.Dealer;
import kh.cardgame.common.GameMove;
import kh.cardgame.common.Player;

/**
 * Simple implementation of the card game Blackjack.
 * 
 * @author Kevin Hooke
 */
public class BlackjackGame {

	private Dealer dealer;
	private Player player;
	private int score;
	private List<Player> computerPlayers;
	private int playerCount;

	public static void main(String[] args) {
		BlackjackGame game = new BlackjackGame();
		game.startGame();
	}

	public BlackjackGame() {
		this.dealer = new Dealer();
		this.player = new Player(Player.PlayerType.human, "Player 1");
		this.computerPlayers = new ArrayList<>();
	}

	public void startGame() {
		
		System.out.println("How many computer players? (enter number like 1");
		String players = this.getPlayerInput();
		this.playerCount = Integer.parseInt(players);
		this.dealToPlayers();

		this.gameLoop();
	}

	private void dealToPlayers() {
		System.out.println("Dealing to you...");
		this.player.addToHand(this.dealer.deal(1));
		this.player.addToHand(this.dealer.deal(1));

		for(int i=0; i<this.playerCount; i++){
			System.out.println("Dealing to player " + i + " ...");
			Player computerPlayer = new Player(Player.PlayerType.computer, "Computer player " + i);
			computerPlayer.addToHand(this.dealer.deal(1));
			computerPlayer.addToHand(this.dealer.deal(1));

			this.computerPlayers.add(computerPlayer);			
		}
		
		
	}

	/**
	 * Main game loop. Each player takes a turn asking for another card or sticking
	 * until they hold. Keep playing until everyone sticks or has bust.
	 */
	private void gameLoop() {
		
		//human player goes first, followed by computer players
		Player currentPlayer = this.player;
		this.takeTurn(currentPlayer);
		
		for(Player computerPlayer : this.computerPlayers){
			this.takeTurn(computerPlayer);
		}
	}

	/**
	 * Perform turn logic. If currentPlayer is human player, print
	 * current hand and ask for turn input, otherwise execute computer turn.
	 * 
	 * @param currentPlayer
	 */
	private void takeTurn(Player currentPlayer) {
		boolean play = true;
		int handValue = 0;
		
		while (play) {
			handValue = currentPlayer.getHand().getValueOfCardsInHand();
			
			if(currentPlayer.getPlayerType() == Player.PlayerType.human){
				currentPlayer.getHand().printHand();				
				System.out.println("Current hand value: " + handValue);
			}
			
			if (handValue > 21) {
				System.out.println("Player: " + currentPlayer.getPlayerName() + " is bust!");
				currentPlayer.getHand().printHand();
				play = false;
			} else {
				GameMove move = this.getNextMove(currentPlayer);
				if (move == GameMove.hit) {
					currentPlayer.addToHand(this.dealer.deal(1));
				} else if (move == GameMove.stick) {
					System.out.println("Player: " + currentPlayer.getPlayerName() + " says stick");
					play = false;
				}
			}

		}
	}

	private GameMove getNextMove(Player currentPlayer) {
		GameMove move = null;
		
		if(currentPlayer.getPlayerType() == Player.PlayerType.human){
			System.out.println("Hit (h) or stick (s) ?");
			String text = getPlayerInput();
	
			if (text.equals("h")) {
				move = GameMove.hit;
			} else if (text.equals("s")) {
				move = GameMove.stick;
			}
		}
		else{
			move = calculateNextMoveForComputerpPlayer(currentPlayer);
			System.out.println("Player: " + currentPlayer.getPlayerName() + " says: " + move.toString());
		
		}
		return move;

	}

	private GameMove calculateNextMoveForComputerpPlayer(Player currentPlayer) {
		GameMove move = null;
		
		//for simplicity 50/50 for now
		Random r = new Random();
		int nextMove = r.nextInt(1); // 0 or 1
		
		if(nextMove == 0)
		{
			move = GameMove.hit;
		}
		else if(nextMove == 1){
			move = GameMove.stick;
		}
		
		return move;
	}

	private String getPlayerInput() {
		Scanner scan = new Scanner(System.in);
		String text = scan.nextLine();
		return text;
	}
}
