import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

/**
 * This program simulates the Tic-tac-toe game with two game modes: Single-player & Multi-player.
 * For the single-player mode, an AI was implemented using the Minimax Algorithm, so the AI is unbeatable.
 * In addition, the Alpha-beta pruning algorithm was implemented to decrease the number of evaluated nodes by the Minimax algorithm.
 * @author Kenny Phan
 */
public class TicTacToe extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	JButton singleplayer;
	JButton multiplayer;
	JLabel title;
	JLabel description;
	JButton reset_button = new JButton();
	JButton exit_button = new JButton();
	JPanel top_panel = new JPanel();
	JPanel slots_panel;
	JButton[] slots = new JButton[9];
	private static String[] board = new String[9];
	private boolean player1_turn;
	private boolean one_player_game;
	private boolean two_player_game;
	private boolean game_done;
	
	public TicTacToe() {
		this.setTitle("Tic-Tac-Toe");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(800, 800);
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		this.getContentPane().setBackground(Color.black);
		this.setResizable(false);
		
		startMenu();
		
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		// Initialize the board game
		for (int i = 0; i < board.length; i++) {
			board[i] = "";
		}
		new TicTacToe();
	}
	
	public void startMenu() {
		one_player_game = false;
		two_player_game = false;
		
		title = new JLabel("Tic-Tac-Toe");
		title.setBounds(185, 30, 700, 100);
		title.setFont(new Font("Comic Sans", Font.PLAIN, 80));
		title.setForeground(Color.yellow);		
		
		singleplayer = new JButton("Player VS AI");
		singleplayer.setBounds(290, 250, 200, 70);
		singleplayer.setFocusable(false);
		singleplayer.addActionListener(this);
		
		multiplayer = new JButton("Player vs Player");
		multiplayer.setBounds(290, 400, 200, 70);
		multiplayer.setFocusable(false);
		multiplayer.addActionListener(this);

		this.add(title);
		this.add(singleplayer);
		this.add(multiplayer);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// One player game
		if (e.getSource() == singleplayer) {
			one_player_game = true;
			onePlayerGame();
		}
		// Two players game
		else if (e.getSource() == multiplayer) {
			two_player_game = true;
			twoPlayersGame();
		}
		else if (e.getSource() == reset_button) {
			for(int i = 0; i < slots.length; i++) {
				slots[i].setText("");
				slots[i].setBackground(Color.white);
				slots[i].setEnabled(true);
				description.setText(null);
				board[i] = "";
			}
			
			if (one_player_game) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
				game_done = false;
				player1_turn = true;
				description.setText("Tic-Tac-Toe");
			}
			
			else if (two_player_game) {
				firstTurn();
			}

		} 
		else if (e.getSource() == exit_button) {
			reset_button.setVisible(false);
			exit_button.setVisible(false);
			description.setVisible(false);
			top_panel.setVisible(false);
			slots_panel.setVisible(false);
			
			this.setLayout(null);
			
			startMenu();

		}
		
		if (one_player_game) {
			for(int i = 0; i < 9; i++) {
				if (e.getSource() == slots[i]) {
					if (player1_turn) {
						if (slots[i].getText() == "") {
							slots[i].setForeground(new Color(255, 0, 0));
							slots[i].setText("X");
							board[i] = "X";
							player1_turn = false;

						}
					} 
				}
			}
			check();
			if (!game_done) {
				AI_play();
			}
		}
		else if (two_player_game) {
			for(int i = 0; i < 9; i++) {
				if (e.getSource() == slots[i]) {
					if (player1_turn) {
						if (slots[i].getText() == "") {
							slots[i].setForeground(new Color(255, 0, 0));
							slots[i].setText("X");
							board[i] = "X";
							player1_turn = false;
							description.setText("O turn");
							check();
						}
					} else {
						if (slots[i].getText() == "") {
							slots[i].setForeground(new Color(0, 0, 255));
							slots[i].setText("O");
							board[i] = "O";
							player1_turn = true;
							description.setText("X turn");
							check();
						}
					}
				}
			}
		}
	}
	
	private void onePlayerGame() {
		gameLayout();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		game_done = false;
		player1_turn = true;
	}

	private void twoPlayersGame() {
		gameLayout();
		firstTurn();
	}
	
	public void AI_play() {
		if (player1_turn == false) {			
			int pos = bestMove();
			
			slots[pos].setForeground(new Color(0, 0, 255));
			slots[pos].setText("O");
			board[pos] = "O";
			check();
			
			player1_turn = true;
		}
	}

	public void gameLayout() {
		title.setVisible(false);
		singleplayer.setVisible(false);
		multiplayer.setVisible(false);
		
		this.setLayout(new BorderLayout());
		
		description = new JLabel("Tic-Tac-Toe");
		description.setForeground(Color.white);
		description.setFont(new Font("Comic Sans", Font.BOLD, 55));
		description.setHorizontalAlignment(JLabel.CENTER);
		
		top_panel.add(description);
		top_panel.setBounds(0, 0, 800, 120);
		top_panel.setBackground(Color.black);
		
		reset_button.setText("Reset Game");
		reset_button.setBounds(20, 15, 125, 50);
		reset_button.setBackground(Color.orange);
		reset_button.setFont(new Font(null, Font.ITALIC, 15));
		reset_button.addActionListener(this);
		reset_button.setFocusable(false);
		
		exit_button.setText("Go to Main Menu");
		exit_button.setBounds(600, 15, 175, 50);
		exit_button.setBackground(Color.orange);
		exit_button.setFont(new Font(null, Font.ITALIC, 15));
		exit_button.addActionListener(this);
		exit_button.setFocusable(false);
		
		slots_panel = new JPanel();
		slots_panel.setLayout(new GridLayout(3, 3));
		slots_panel.setBackground(Color.black);
		
		for (int i = 0; i < 9; i++) {
			slots[i] = new JButton();
			//slots[i].setBackground(Color.white);
			slots_panel.add(slots[i]);
			slots[i].setFont(new Font("MV Boli", Font.BOLD, 120));
			slots[i].setFocusable(false);
			slots[i].addActionListener(this);
		}
		
		description.setVisible(true);
		reset_button.setVisible(true);
		exit_button.setVisible(true);
		top_panel.setVisible(true);
		slots_panel.setVisible(true);
		
		this.add(reset_button);
		this.add(exit_button);
		this.add(top_panel, BorderLayout.NORTH);
		this.add(slots_panel);
		
	}

	/**
	 * Automatically dictates whether Player1 or Player2 starts
	 */
	private void firstTurn() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Random random = new Random();
		if (random.nextInt(2) == 0) {
			player1_turn = true;
			description.setText("X turn");
		} else {
			player1_turn = false;
			description.setText("O turn");
		}
	}
	
	/**
	 * Checks the different winning combinations
	 * @return Returns true if there is a winner; otherwise, false
	 */
	public boolean check() {
		//Check X win conditions
		if ((slots[0].getText() == "X") && 
			(slots[1].getText() == "X") && 
			(slots[2].getText() == "X")) {
			xWins(0,1,2);
			return true;
		}
		else if ((slots[3].getText() == "X") && 
			(slots[4].getText() == "X") && 
			(slots[5].getText() == "X")) {
			xWins(3,4,5);
			return true;
			}
		else if ((slots[6].getText() == "X") && 
			(slots[7].getText() == "X") && 
			(slots[8].getText() == "X")) {
			xWins(6,7,8);
			return true;
			}
		else if ((slots[0].getText() == "X") && 
			(slots[3].getText() == "X") && 
			(slots[6].getText() == "X")) {
			xWins(0,3,6);
			return true;
			}
		else if ((slots[1].getText() == "X") && 
			(slots[4].getText() == "X") && 
			(slots[7].getText() == "X")) {
			xWins(1,4,7);
			return true;
			}
		else if ((slots[2].getText() == "X") && 
			(slots[5].getText() == "X") && 
			(slots[8].getText() == "X")) {
			xWins(2,5,8);
			return true;
			}
		else if ((slots[0].getText() == "X") && 
			(slots[4].getText() == "X") && 
			(slots[8].getText() == "X")) {
			xWins(0,4,8);
			return true;
			}
		else if ((slots[2].getText() == "X") && 
			(slots[4].getText() == "X") && 
			(slots[6].getText() == "X")) {
			xWins(2,4,6);
			return true;
			}
		
		//Check O win conditions
		else if ((slots[0].getText() == "O") && 
				(slots[1].getText() == "O") && 
				(slots[2].getText() == "O")) {
				oWins(0,1,2);
				return true;

			}
		else if ((slots[3].getText() == "O") && 
			(slots[4].getText() == "O") && 
			(slots[5].getText() == "O")) {
			oWins(3,4,5);
			return true;

			}
		else if ((slots[6].getText() == "O") && 
			(slots[7].getText() == "O") && 
			(slots[8].getText() == "O")) {
			oWins(6,7,8);
			return true;

			}
		else if ((slots[0].getText() == "O") && 
			(slots[3].getText() == "O") && 
			(slots[6].getText() == "O")) {
			oWins(0,3,6);
			return true;

			}
		else if ((slots[1].getText() == "O") && 
			(slots[4].getText() == "O") && 
			(slots[7].getText() == "O")) {
			oWins(1,4,7);
			return true;

			}
		
		else if ((slots[2].getText() == "O") && 
			(slots[5].getText() == "O") && 
			(slots[8].getText() == "O")) {
			oWins(2,5,8);
			return true;

			}
		else if ((slots[0].getText() == "O") && 
			(slots[4].getText() == "O") && 
			(slots[8].getText() == "O")) {
			oWins(0,4,8);
			return true;

			}
		else if ((slots[2].getText() == "O") && 
			(slots[4].getText() == "O") && 
			(slots[6].getText() == "O")) {
			oWins(2,4,6);
			return true;

			}
		else {
			int checkTie = 0;
			for (JButton i : slots) {
				if (i.getText() == "O" || i.getText() == "X") {
					checkTie++;
				}
				if (checkTie == 9) {
					tie();
					return true;
				}
			}
		}
		return false;
	}

	public void xWins(int a, int b, int c) {
		slots[a].setBackground(Color.green);
		slots[b].setBackground(Color.green);
		slots[c].setBackground(Color.green);
		
		for(int i = 0 ; i < 9; i++) {
			slots[i].setEnabled(false);
		}
		description.setText("X wins");
		game_done = true;
	}
	
	public void oWins(int a, int b, int c) {
		slots[a].setBackground(Color.green);
		slots[b].setBackground(Color.green);
		slots[c].setBackground(Color.green);
		
		for(int i = 0 ; i < 9; i++) {
			slots[i].setEnabled(false);
		}
		description.setText("O wins");
		game_done = true;
	}
	
	public void tie() {
		description.setText("Tie!");
		game_done = true;
	}
	
	/**
	 * The best possible move for the AI
	 * @return Returns an integer representing the best slot
	 */
	public int bestMove() {
		int bestScore = Integer.MIN_VALUE;
		int bestMove = -1;
		for(int i = 0; i < 9; i++) {
			// Check if slot is available
			if (board[i] == "") {
				board[i] = "O";
				int score = minimax(board, 0, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
				board[i] = "";
				if (score > bestScore) {
					bestScore = score;
					bestMove = i;
				}
			}
		}
		board[bestMove] = "O";
		return bestMove;
	}

	/**
	 * @param board The board game
	 * @param depth The depth in the tree node
	 * @param isMaximizing True if the AI is maximizing; otherwise, false if the player is minimizing
	 * @param alpha Negative infinity
	 * @param beta Positive infinity
	 * @return Returns a score for a subtree 
	 */
	public int minimax(String[] board, int depth, boolean isMaximizing, int alpha, int beta) {
		// Stopping cases
		if (hasPlayerWon("X")) {
			return -1;
		}
		if (hasPlayerWon("O")) {
			return 1;
		}
		if (getAvailableSlots(board) == 0) {
			return 0;
		}		

		// Maximizing for the AI
		if (isMaximizing) {
			int bestScore = Integer.MIN_VALUE;
			for (int i = 0; i < board.length; i++) {
				if (board[i] == "") {
					board[i] = "O";
					int score = minimax(board, depth + 1, false, alpha, beta);
					board[i] = "";
					bestScore = Math.max(score, bestScore);
					alpha = Math.max(alpha, bestScore);
					if (beta <= alpha) {
						break;
					}
				}

			}
			return bestScore;
		}
		//Minimizing for the Player
		else {
			int bestScore = Integer.MAX_VALUE;
			for (int i = 0; i < board.length; i++) {
				if (board[i] == "") {
					board[i] = "X";
					int score = minimax(board, depth + 1, true, alpha, beta);
					board[i] = "";
					bestScore = Math.min(score, bestScore);
					beta = Math.min(beta, bestScore);
					if (beta <= alpha) {
						break;
					}
				}

			}
			return bestScore;
		}
	}
	
	/**
	 * Searches the number of available slots in the board.
	 * @param board The board game.
	 * @return Returns the number of available slots.
	 */
	public int getAvailableSlots(String[] board) {
		int num = 0;
		for (int i = 0; i < board.length; i++) {
			if (board[i] == "") {
				num++;
			}
		}
		return num;
	}

	/**
	 * Searches if a player has won.
	 * @param player The "X" player or "O" player.
	 * @return Returns true if a player has won, otherwise, false.
	 */
	public boolean hasPlayerWon(String player) {
		// Check diagonals
		if ((board[0] == board[4] && board[0] == board[8] && board[0].equals(player)) || (board[2] == board[4] && board[2] == board[6] && board[2].equals(player)) ) {
			return true;
		}
		// Check horizontal
		if ((board[0] == board[1] && board[0] == board[2] && board[0].equals(player)) || (board[3] == board[4] && board[3] == board[5] && board[3].equals(player)) || (board[6] == board[7] && board[6] == board[8] && board[6].equals(player))) {
			return true;
		}
		// Check verticals
		if ((board[0] == board[3] && board[0] == board[6] && board[0].equals(player)) || (board[1] == board[4] && board[1] == board[7] && board[1].equals(player)) || (board[2] == board[5] && board[2] == board[8] && board[2].equals(player))) {
			return true;
		}
		return false;
	}
	
}
