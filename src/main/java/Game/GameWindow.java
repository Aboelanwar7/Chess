package Game;

import Board.BoardSetup;
import Pieces.piece.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GameWindow extends JPanel {
    private BoardSetup board;
    private JLabel backgroundLabel;
    private JButton exitGameButton;
    private JLabel sidePanel;

    // Timer variables
    private JLabel whiteTimerLabel;
    private JLabel blackTimerLabel;
    public JLabel blackDiedPieces;
    public JLabel whiteDiedPieces;
    private Timer whiteTimer;
    private Timer blackTimer;
    private int whiteTimeRemaining; // Time in seconds for white
    private int blackTimeRemaining; // Time in seconds for black
    private boolean isWhiteTurn = true; // To track the current player's turn

    public GameWindow(CardLayout cardLayout, JPanel mainPanel) {
        this.board = new BoardSetup(this);
        System.out.println(this.getSize());


        backgroundLabel = new JLabel();
        this.setLayout(null);

        JLabel bottomLabel = new JLabel();
        bottomLabel.setBounds(25, 380, 300, 300);
        bottomLabel.setIcon(new ImageIcon("src\\main\\resources\\wooden-sign__2_-removebg-preview.png"));
        bottomLabel.setOpaque(false);
        FuturisticButton restartGameButton = new FuturisticButton("Restart Game");
        exitGameButton = new FuturisticButton("Exit Game");
        bottomLabel.add(restartGameButton);
        bottomLabel.add(exitGameButton);


        // Set up the chess board, shifted to the left
        board.setBounds(330, 25, 720, 720); // Shifted to the left to make room for the side panel
        backgroundLabel.add(board);


        // Create side panel for the Exit Game button and timers on the left
        sidePanel = new JLabel();
        sidePanel.setLayout(null);
        sidePanel.setBounds(20, 0, 200, 300); // Left side panel, within the background bounds
        sidePanel.setIcon(new ImageIcon("src\\main\\resources\\wooden-sign-hanging-from-rope__1_-removebg-preview (1).png"));
        sidePanel.setOpaque(false);

        // Add Exit Game button

        exitGameButton.setBounds(50, 480, 140, 30); // Button in the side panel
        exitGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show a confirmation dialog when the Exit Game button is clicked
                int result = JOptionPane.showConfirmDialog(
                        GameWindow.this,
                        "Are you sure you want to exit the game?",
                        "Exit Game",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE

                );

                if (result == JOptionPane.YES_OPTION) {
                    resetBoard(); // Reset the board to its initial state
                    cardLayout.show(mainPanel, "MainMenu");
                }
            }
        });

        restartGameButton.setBounds(50, 430, 140, 30);
        restartGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show a confirmation dialog when the Restart Game button is clicked
                int result = JOptionPane.showConfirmDialog(
                        GameWindow.this,
                        "Are you sure you want to restart the game?",
                        "Restart Game",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE

                );

                if (result == JOptionPane.YES_OPTION) {
                    resetBoard(); // Reset the board to its initial state
                    restartTimers();
                }
            }
        });
//        sidePanel.add(restartGameButton);


        blackDiedPieces = new JLabel();
        blackDiedPieces.setLayout(new GridLayout(2, 2));
//        blackDiedPieces.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        blackDiedPieces.setBounds(1070, 25, 400, 100);
        blackDiedPieces.setBackground(new Color(139, 69, 19));
        blackDiedPieces.setIcon(new ImageIcon("src\\sources\\kl.jpg"));
        blackDiedPieces.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));


        whiteDiedPieces = new JLabel();
        whiteDiedPieces.setLayout(new GridLayout(2, 2));
//        whiteDiedPieces.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        whiteDiedPieces.setBounds(1070, 635, 400, 100);
//        whiteDiedPieces.setBackground(new Color(222, 184, 135));

        whiteDiedPieces.setIcon(new ImageIcon("src\\sources\\kl.jpg"));
        whiteDiedPieces.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));


        this.add(blackDiedPieces);
        this.add(whiteDiedPieces);

        this.add(restartGameButton);
        this.add(exitGameButton);
        this.add(bottomLabel);


        // White player timer
        whiteTimerLabel = new JLabel("White: 00:00");
        whiteTimerLabel.setBounds(25, 230, 160, 30);
        whiteTimerLabel.setFont(new Font("MV Boli", Font.BOLD, 20));
        whiteTimerLabel.setForeground(Color.WHITE);
        whiteTimerLabel.setBackground(new Color(255, 255, 255, 0));

        whiteTimerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sidePanel.add(whiteTimerLabel);

        // Black player timer
        blackTimerLabel = new JLabel("Black: 00:00");
        blackTimerLabel.setBounds(25, 190, 160, 30);
        blackTimerLabel.setFont(new Font("MV Boli", Font.BOLD, 20));
        blackTimerLabel.setForeground(Color.BLACK);
        blackTimerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sidePanel.add(blackTimerLabel);

        // Set background properties
        backgroundLabel.setBounds(0, 0, 1500, 800);
        backgroundLabel.setIcon(new ImageIcon("src\\main\\resources\\ww.jpg"));

        // Add components to the main panel
        this.add(sidePanel);
        this.add(backgroundLabel);

        // Set up board logic (turn switch handling)
        board.setOnTurnSwitch(this::switchTurn);
    }

    // Show the pop-up window to set the timer duration before the game starts
    public void showTimerSetupDialog() {
        // Ask the user for the time in minutes
        String input = JOptionPane.showInputDialog(
                this,
                "Set timer (in minutes) for each player:",
                "Timer Setup",
                JOptionPane.QUESTION_MESSAGE
        );

        if (input != null && !input.isEmpty()) {
            try {
                int minutes = Integer.parseInt(input);
                // Convert minutes to seconds and set it
                whiteTimeRemaining = blackTimeRemaining = minutes * 60;

                // Initialize the timers with the user-defined time
                initializeTimers();

            } catch (NumberFormatException e) {
                // Show error and re-open the dialog if input is invalid
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number of minutes.", "Error", JOptionPane.ERROR_MESSAGE);
                showTimerSetupDialog(); // Retry timer setup
            }
        } else {
            // Default time if no input is provided
            whiteTimeRemaining = blackTimeRemaining = 600; // 10 minutes default
            initializeTimers();
        }
    }

    // Initialize timers for both players
    private void initializeTimers() {
        whiteTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                whiteTimeRemaining--;
                updateTimerLabel(whiteTimerLabel, whiteTimeRemaining);

                if (whiteTimeRemaining <= 0) {
                    whiteTimer.stop();
                    blackTimer.stop();
                    endGame("Black wins! White ran out of time.");
                }
            }
        });

        blackTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                blackTimeRemaining--;
                updateTimerLabel(blackTimerLabel, blackTimeRemaining);

                if (blackTimeRemaining <= 0) {
                    whiteTimer.stop();
                    blackTimer.stop();
                    endGame("White wins! Black ran out of time.");
                }
            }
        });

        // Start the white timer by default
        updateTimerLabel(whiteTimerLabel, whiteTimeRemaining);
        updateTimerLabel(blackTimerLabel, blackTimeRemaining);
        whiteTimer.start();
    }

    // Helper method to format and update the timer label
    private void updateTimerLabel(JLabel timerLabel, int timeRemaining) {
        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;
        timerLabel.setText(String.format("%s: %02d:%02d",
                timerLabel.getText().contains("White") ? "White" : "Black", minutes, seconds));
    }

    // Switch timer between players
    public void switchTurn() {
        if (isWhiteTurn) {
            whiteTimer.stop();  // Stop white's timer
            blackTimer.start();  // Start black's timer
        } else {
            blackTimer.stop();  // Stop black's timer
            whiteTimer.start();  // Start white's timer
        }
        isWhiteTurn = !isWhiteTurn;  // Toggle the turn
    }

    // Method to reset the chess board
    private void resetBoard() {
        this.board = new BoardSetup(this);
        board.setBounds(330, 25, 720, 720);
        this.isWhiteTurn = true;
        board.setOnTurnSwitch(this::switchTurn);
        backgroundLabel.removeAll();
        backgroundLabel.add(board);
        backgroundLabel.revalidate();
        backgroundLabel.repaint();
        whiteDiedPieces.removeAll();
        whiteDiedPieces.revalidate();
        whiteDiedPieces.repaint();
        blackDiedPieces.removeAll();
        blackDiedPieces.revalidate();
        blackDiedPieces.repaint();


        // Reset timers to default or user-defined value

    }

    public void restartTimers() {
        whiteTimer.stop();
        blackTimer.stop();
        this.showTimerSetupDialog();
    }

    // End the game when time runs out
    public void endGame(String message) {
        JOptionPane.showMessageDialog(this, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);

//        resetBoard();
    }

    public String showPromotionDialog() {
        String[] options = {"Queen", "Rook", "Bishop", "Knight"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Choose the piece to promote your pawn to:",
                "Pawn Promotion",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        // Return the chosen piece based on the selected option
        if (choice == JOptionPane.CLOSED_OPTION) {
            return "Queen";  // Default to Queen if the dialog is closed
        }

        return options[choice];
    }

    public void promotePawn(int index, boolean isWhite) {
        String promotionChoice = showPromotionDialog();
        Piece newPiece;

        switch (promotionChoice.toLowerCase()) {
            case "rook":
                newPiece = new Rook(isWhite);
                break;
            case "bishop":
                newPiece = new Bishop(isWhite);
                break;
            case "knight":
                newPiece = new Knight(isWhite);
                break;
            case "queen":
            default:
                newPiece = new Queen(isWhite);
                break;
        }

        // Replace the pawn with the new piece in the squares array
        board.getSquares()[index].setPiece(newPiece);

        // Update GameBoard array to reflect the new piece's type
        int pieceType = 0; // Assign a corresponding integer type for each piece (e.g., Queen = 5)
        if (newPiece instanceof Queen) {
            pieceType = isWhite ? 9 : -9;
        } else if (newPiece instanceof Rook) {
            pieceType = isWhite ? 4 : -4;
        } else if (newPiece instanceof Bishop) {
            pieceType = isWhite ? 3 : -3;
        } else if (newPiece instanceof Knight) {
            pieceType = isWhite ? 2 : -2;
        }

        board.getGameBoard()[index] = pieceType;  // Update gameBoard with the new piece

        board.updateBoard();  // Update the board UI to reflect the change
    }


    public void stopTimers() {
        whiteTimer.stop();
        blackTimer.stop();
    }

    public void updateWhiteDiedPieces(Piece piece) {
        JLabel pieceLabel = new JLabel();
        pieceLabel.setIcon(new ImageIcon(piece.getIconPath()));
        whiteDiedPieces.add(pieceLabel);
        whiteDiedPieces.revalidate();
        whiteDiedPieces.repaint();
    }

    public void updateBlackDiedPieces(Piece piece) {
        JLabel pieceLabel = new JLabel();
        pieceLabel.setIcon(new ImageIcon(piece.getIconPath()));
        blackDiedPieces.add(pieceLabel);
        blackDiedPieces.revalidate();
        blackDiedPieces.repaint();
    }


    void displayBoard() {
        this.setVisible(true);
    }

    void setBoard(BoardSetup board) {
        this.board = board;
    }

    BoardSetup getBoard() {
        return this.board;
    }


}
