import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NumberGuessingGUI extends JFrame {
    private NumberGuessingGame game;
    
    // UI Components
    private JComboBox<String> difficultyCombo;
    private JLabel rangeLabel;
    private JLabel maxAttemptLabel;
    private JLabel attemptLabel;
    
    private JLabel currentRoundLabel;
    private JLabel totalRoundsLabel;
    private JLabel roundsWonLabel;
    private JLabel roundsLostLabel;
    
    private JTextField guessInput;
    private JButton guessButton;
    private JLabel resultLabel;
    
    private JButton newGameButton;
    private JButton playAgainButton;
    private JButton exitButton;
    
    private JTextArea historyArea;
    
    private int completedRounds = 0;
    private int roundsLost = 0;

    public NumberGuessingGUI() {
        game = new NumberGuessingGame();
        
        setTitle("Number Guessing Game");
        setMinimumSize(new Dimension(750, 550));
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center window
        setLayout(new BorderLayout(10, 10));

        initComponents();
        
        // Initial setup
        game.setDifficulty((String) difficultyCombo.getSelectedItem());
        startNewRoundUI();
    }

    private void initComponents() {
        // --- HEADER ---
        JLabel headerLabel = new JLabel("NUMBER GUESSING GAME", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(headerLabel, BorderLayout.NORTH);

        // --- CENTER MAIN PANEL ---
        JPanel mainContentPanel = new JPanel(new BorderLayout(15, 15));
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // TOP SECTION: Difficulty and Game Status
        JPanel topSection = new JPanel(new BorderLayout());
        
        JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        difficultyPanel.add(new JLabel("Difficulty: "));
        String[] difficulties = {"Easy", "Medium", "Hard"};
        difficultyCombo = new JComboBox<>(difficulties);
        difficultyCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        difficultyCombo.addActionListener(e -> {
            game.setDifficulty((String) difficultyCombo.getSelectedItem());
            updateStatusLabels();
        });
        difficultyPanel.add(difficultyCombo);
        topSection.add(difficultyPanel, BorderLayout.NORTH);
        
        JPanel statusPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        statusPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "GAME STATUS", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 12)));
        rangeLabel = new JLabel("", SwingConstants.CENTER);
        maxAttemptLabel = new JLabel("", SwingConstants.CENTER);
        attemptLabel = new JLabel("", SwingConstants.CENTER);
        rangeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        maxAttemptLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        attemptLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        statusPanel.add(rangeLabel);
        statusPanel.add(maxAttemptLabel);
        statusPanel.add(attemptLabel);
        topSection.add(statusPanel, BorderLayout.CENTER);
        
        mainContentPanel.add(topSection, BorderLayout.NORTH);
        
        // MIDDLE SECTION: Guess Input Area
        JPanel guessSection = new JPanel();
        guessSection.setLayout(new BoxLayout(guessSection, BoxLayout.Y_AXIS));
        guessSection.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "ENTER YOUR GUESS", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 12)));
        
        JPanel inputWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        
        guessInput = new JTextField();
        guessInput.setPreferredSize(new Dimension(250, 50));
        guessInput.setFont(new Font("Arial", Font.BOLD, 20));
        guessInput.setHorizontalAlignment(JTextField.CENTER);
        guessInput.setToolTipText("Enter a number...");
        
        guessButton = new JButton("GUESS");
        guessButton.setPreferredSize(new Dimension(130, 50));
        guessButton.setFont(new Font("Arial", Font.BOLD, 18));
        
        inputWrapper.add(guessInput);
        inputWrapper.add(guessButton);
        
        resultLabel = new JLabel("Welcome! Start guessing.", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        guessSection.add(Box.createVerticalStrut(20));
        guessSection.add(inputWrapper);
        guessSection.add(Box.createVerticalStrut(10));
        guessSection.add(resultLabel);
        guessSection.add(Box.createVerticalStrut(20));
        
        mainContentPanel.add(guessSection, BorderLayout.CENTER);
        
        // BOTTOM SECTION: Score Tracking
        JPanel scoreSection = new JPanel(new GridLayout(1, 4, 15, 0));
        scoreSection.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "SCORE", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 12)));
        
        currentRoundLabel = createScoreLabel("Current Round", "1");
        totalRoundsLabel = createScoreLabel("Total Played", "0");
        roundsWonLabel = createScoreLabel("Rounds Won", "0");
        roundsLostLabel = createScoreLabel("Rounds Lost", "0");
        
        scoreSection.add(currentRoundLabel);
        scoreSection.add(totalRoundsLabel);
        scoreSection.add(roundsWonLabel);
        scoreSection.add(roundsLostLabel);
        
        mainContentPanel.add(scoreSection, BorderLayout.SOUTH);
        
        add(mainContentPanel, BorderLayout.CENTER);
        
        // --- RIGHT PANEL: Round History ---
        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Arial", Font.PLAIN, 14));
        historyArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(historyArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "ROUND HISTORY", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 12)));
        scrollPane.setPreferredSize(new Dimension(250, 0));
        
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 20));
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(rightPanel, BorderLayout.EAST);
        
        // --- BOTTOM PANEL: Controls ---
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        
        newGameButton = createControlButton("NEW GAME");
        playAgainButton = createControlButton("PLAY AGAIN");
        exitButton = createControlButton("EXIT");
        
        controlPanel.add(newGameButton);
        controlPanel.add(playAgainButton);
        controlPanel.add(exitButton);
        
        add(controlPanel, BorderLayout.SOUTH);

        // --- Action Listeners ---
        ActionListener guessAction = e -> handleGuess();
        guessButton.addActionListener(guessAction);
        guessInput.addActionListener(guessAction);
        
        newGameButton.addActionListener(e -> {
            game = new NumberGuessingGame();
            game.setDifficulty((String) difficultyCombo.getSelectedItem());
            completedRounds = 0;
            roundsLost = 0;
            historyArea.setText("");
            startNewRoundUI();
        });

        playAgainButton.addActionListener(e -> startNewRoundUI());
        exitButton.addActionListener(e -> System.exit(0));
    }
    
    private JLabel createScoreLabel(String title, String initialValue) {
        JLabel label = new JLabel("<html><center>" + title + "<br><b style='font-size:16px;'>" + initialValue + "</b></center></html>", SwingConstants.CENTER);
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(10, 5, 10, 5)
        ));
        label.setOpaque(true);
        label.setBackground(new Color(245, 245, 245));
        return label;
    }
    
    private void updateScoreLabel(JLabel label, String title, String value) {
        label.setText("<html><center>" + title + "<br><b style='font-size:16px;'>" + value + "</b></center></html>");
    }
    
    private JButton createControlButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(140, 45));
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        return btn;
    }

    private void startNewRoundUI() {
        game.startNewRound();
        guessInput.setText("");
        guessInput.setEditable(true);
        guessButton.setEnabled(true);
        playAgainButton.setEnabled(false);
        difficultyCombo.setEnabled(false);
        
        resultLabel.setText("New round started! Enter a number between 1 and " + game.getMaxRange() + ".");
        resultLabel.setForeground(Color.BLACK);
        
        updateStatusLabels();
        updateScoreBoard();
        guessInput.requestFocusInWindow();
    }

    private void updateStatusLabels() {
        rangeLabel.setText("Range: 1\u2013" + game.getMaxRange());
        maxAttemptLabel.setText("Max Attempts: " + game.getMaxAttempts());
        attemptLabel.setText("Attempt: " + game.getAttemptsTaken() + " / " + game.getMaxAttempts());
    }
    
    private void updateScoreBoard() {
        updateScoreLabel(currentRoundLabel, "Current Round", String.valueOf(game.getRoundsPlayed()));
        updateScoreLabel(totalRoundsLabel, "Total Played", String.valueOf(completedRounds));
        updateScoreLabel(roundsWonLabel, "Rounds Won", String.valueOf(game.getSuccessfulRounds()));
        updateScoreLabel(roundsLostLabel, "Rounds Lost", String.valueOf(roundsLost));
    }

    private void handleGuess() {
        String inputText = guessInput.getText().trim();
        if (inputText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a number.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int guess;
        try {
            guess = Integer.parseInt(inputText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            guessInput.setText("");
            return;
        }

        if (guess < 1 || guess > game.getMaxRange()) {
            JOptionPane.showMessageDialog(this, "Enter a number between 1 and " + game.getMaxRange() + ".", "Out of Range", JOptionPane.WARNING_MESSAGE);
            guessInput.setText("");
            return;
        }

        // Valid guess
        game.incrementAttempts();
        updateStatusLabels();
        
        String result = game.checkGuess(guess);
        
        if (result.equals("Correct!")) {
            resultLabel.setText("Correct! You guessed it in " + game.getAttemptsTaken() + " attempts!");
            resultLabel.setForeground(new Color(0, 153, 0));
            game.recordSuccess();
            completedRounds++;
            historyArea.append("Round " + game.getRoundsPlayed() + " \u2014 guessed in " + game.getAttemptsTaken() + " attempts\n");
            endRoundUI();
        } else {
            resultLabel.setText(result);
            resultLabel.setForeground(Color.RED);
            
            if (game.hasReachedMaxAttempts()) {
                resultLabel.setText("You Lost! The number was " + game.getCurrentNumber() + ".");
                roundsLost++;
                completedRounds++;
                historyArea.append("Round " + game.getRoundsPlayed() + " \u2014 Lost\n");
                endRoundUI();
            }
        }
        
        guessInput.setText("");
        guessInput.requestFocusInWindow();
    }
    
    private void endRoundUI() {
        guessInput.setEditable(false);
        guessButton.setEnabled(false);
        playAgainButton.setEnabled(true);
        difficultyCombo.setEnabled(true);
        updateScoreBoard();
    }
}
