package Game;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;  // Import BasicButtonUI
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame implements ActionListener {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    FuturisticButton button1;
    FuturisticButton button2;
    FuturisticButton button3;
    FuturisticButton button4;
    private final Image backgroundImage = new ImageIcon("src\\sources\\modernMenu.png").getImage(); // Use the uploaded image

    public MainMenu() {
        // Set up the main frame
        Dimension sz = new Dimension(1000, 800);
        this.setSize(sz);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null); // Center the window
        // CardLayout to switch between main menu and chess game
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create main menu panel with the background image
        JPanel menuPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        menuPanel.setLayout(new BorderLayout()); // Set layout for buttons to be placed at the top

        // Create a panel for the buttons and set it to flow horizontally
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Center buttons with horizontal space
        buttonPanel.setOpaque(false); // Make sure the button panel is transparent

        // Create buttons
        button1 = new FuturisticButton("1 Player");
        button2 = new FuturisticButton("2 Players");
        button3 = new FuturisticButton("Online");
        button4 = new FuturisticButton("Exit");

        // Add action listeners to buttons
        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);
        button4.addActionListener(this);

        // Add buttons to the button panel
        buttonPanel.add(button1);
        buttonPanel.add(button2);
        buttonPanel.add(button3);
        buttonPanel.add(button4);

        // Add the button panel to the top of the menu panel
        menuPanel.add(buttonPanel, BorderLayout.PAGE_START); // Place buttons at the top

        // Add panels to the main panel
        GameWindow chessPanel = new GameWindow(cardLayout, mainPanel); // Pass cardLayout and mainPanel
        mainPanel.add(menuPanel, "MainMenu");
        mainPanel.add(chessPanel, "ChessGame");

        this.add(mainPanel);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button2) {
            // Switch to ChessGame panel and show the timer setup dialog
            cardLayout.show(mainPanel, "ChessGame");

            // Show the timer setup dialog when switching to the game
            GameWindow chessGame = (GameWindow) mainPanel.getComponent(1); // Get the GameWindow component
            chessGame.showTimerSetupDialog(); // Call the method to show the timer setup dialog
        }
        if (e.getSource() == button4) {
            System.exit(0); // Close the application
        }
    }
}

// Custom FuturisticButton class with hover effect
class FuturisticButton extends JButton {
    private boolean hover = false;

    public FuturisticButton(String text) {
        super(text);
        setUI(new BasicButtonUI());
        setPreferredSize(new Dimension(150, 40));  // Adjusted size
        setFont(new Font("Arial", Font.BOLD, 16));
        setFocusPainted(false);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorder(BorderFactory.createEmptyBorder());

        // Hover effect
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                hover = true;
                setBorder(BorderFactory.createLineBorder(Color.CYAN, 2)); // Glowing cyan effect
                repaint();
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                hover = false;
                setBorder(BorderFactory.createEmptyBorder());
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Button gradient (similar to the theme)
        Color color1 = hover ? new Color(0, 255, 255) : new Color(0, 128, 255);  // Lighter on hover
        Color color2 = hover ? new Color(0, 255, 255, 100) : new Color(0, 128, 255, 100);  // Light cyan glow

        GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
        g2d.setPaint(gp);

        // Draw an oval (rounded button)
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);

        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw rounded border
        g2d.setColor(new Color(0, 128, 255));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 50, 50);
    }
}
