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
		
		System.out.println("How many computer players? (enter number of players, like 1)");
		String players = this.getPlayerInput();
		this.playerCount = Integer.parseInt(players);
		this.dealToPlayers();

		this.gameLoop();
	}

	private void dealToPlayers() {
		System.out.println("Dealing to you...");
		this.player.addToHand(this.dealer.deal(1));
		this.player.addToHand(this.dealer.deal(1));

		for(int i=1; i <= this.playerCount; i++){
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
		
		//show hands
		int winningScore = 0;
		Player winner = null;
		System.out.println("\nPlayers hands: ");
		System.out.println("Player: ");
		this.player.getHand().printHand();
		winner = this.player;
		int playerScore = this.player.getHand().getValueOfCardsInHand();
		if(playerScore < 22){
			winningScore = playerScore;
		}
		
		for(Player computerPlayer : this.computerPlayers){
			System.out.println("\nPlayer: " + computerPlayer.getPlayerName());
			computerPlayer.getHand().printHand();
			int currentScore = computerPlayer.getHand().getValueOfCardsInHand();
			//TODO handle draw
			if(currentScore < 22 && currentScore > winningScore) {
				winningScore = currentScore;
				winner = computerPlayer;
			}
		}
		
		System.out.println("Winner is: " + winner.getPlayerName() + ", score: " + winningScore);

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
				System.out.println("Player: " + currentPlayer.getPlayerName() + " is bust!\n");
				//currentPlayer.getHand().printHand();
				play = false;
			} else {
				GameMove move = this.getNextMove(currentPlayer);
				if (move == GameMove.hit) {
					System.out.println("Player: " + currentPlayer.getPlayerName() + " says hit!\n");
					currentPlayer.addToHand(this.dealer.deal(1));
				} else if (move == GameMove.stick) {
					System.out.println("Player: " + currentPlayer.getPlayerName() + " says stick\n");
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
			move = calculateNextMoveForComputerPlayer(currentPlayer);

		}
		return move;

	}

	private GameMove calculateNextMoveForComputerPlayer(Player currentPlayer) {
		GameMove move = null;
		int currentScore = currentPlayer.getHand().getValueOfCardsInHand();
		if(currentScore == 21) {
			move = GameMove.stick;
		}
		else {
			Random r = new Random();
			int nextMove = 1; //default = stick
			
			if(currentScore < 12){
				move = GameMove.hit; // if <= 11 100% chance of hitting
			}
			else {
				if(currentScore < 14){
					nextMove = r.nextInt(1); // 0 or 1 = 50/50 chance of hitting
				} else if(currentScore < 16){
					nextMove = r.nextInt(3); // 1 in 4 chance of hitting
				}
				else if(currentScore < 18){
					nextMove = r.nextInt(7); // 1 in 8 chance of hitting
				}
				
				if(nextMove == 0) {
					move = GameMove.hit;
				}
				else {
					move = GameMove.stick;
				}
			}
		}
		return move;
	}

	private String getPlayerInput() {
		Scanner scan = new Scanner(System.in);
		String text = scan.nextLine();
		//scan.close();
		return text;
	}
}
