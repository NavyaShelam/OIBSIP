import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class NumberGuessingGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    
    public enum GameState {
        READY, PLAYING, WON, LOST
    }
    
    private NumberGuessingGame game;
    private GameState currentState = GameState.READY;
    
    // Theme Colors
    private final Color bgColor = new Color(15, 23, 42); // #0F172A
    private final Color cardColor = new Color(30, 41, 59); // #1E293B
    private final Color textColor = new Color(248, 250, 252); // #F8FAFC
    private final Color subTextColor = new Color(148, 163, 184); // #94A3B8
    private final Color accentColor = new Color(139, 92, 246); // #8B5CF6
    
    // UI Components
    private JComboBox<String> difficultyCombo;
    private JLabel rangeLabel;
    private JLabel maxAttemptLabel;
    private JLabel attemptLabel;
    private JLabel attemptDotsLabel;
    
    private JLabel currentRoundLabel;
    private JLabel totalRoundsLabel;
    private JLabel roundsWonLabel;
    private JLabel roundsLostLabel;
    
    private JTextField guessInput;
    private ModernButton guessButton;
    private JLabel resultLabel;
    
    private ModernButton startGameButton;
    private ModernButton newGameButton;
    private ModernButton playAgainButton;
    private ModernButton exitButton;
    
    private JTextArea historyArea;
    
    private int completedRounds = 0;
    private int roundsLost = 0;

    public NumberGuessingGUI() {
        game = new NumberGuessingGame();
        
        setTitle("Number Guessing Game");
        setMinimumSize(new Dimension(850, 650));
        setSize(950, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main wrapper
        JPanel wrapper = new JPanel(new BorderLayout(15, 15));
        wrapper.setBackground(bgColor);
        wrapper.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setContentPane(wrapper);

        initComponents();
        
        game.setDifficulty((String) difficultyCombo.getSelectedItem());
        changeState(GameState.READY);
    }
    
    private Border createCustomBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(subTextColor, 1, true),
            title, TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12), subTextColor
        );
        return border;
    }

    private void initComponents() {
        // --- HEADER ---
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(bgColor);
        
        JLabel titleLabel = new JLabel("🎯 NUMBER GUESSING GAME");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(textColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subTitleLabel = new JLabel("Think smart. Guess faster.");
        subTitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subTitleLabel.setForeground(accentColor);
        subTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subTitleLabel);
        headerPanel.add(Box.createVerticalStrut(15));
        
        add(headerPanel, BorderLayout.NORTH);

        // --- CENTER MAIN PANEL ---
        JPanel mainContentPanel = new JPanel(new BorderLayout(20, 20));
        mainContentPanel.setOpaque(false);
        
        // TOP SECTION: Status and Score
        JPanel topSection = new JPanel(new GridLayout(1, 2, 20, 0));
        topSection.setOpaque(false);
        
        // Game Status Card
        JPanel statusCard = new JPanel(new BorderLayout(10, 10));
        statusCard.setBackground(cardColor);
        statusCard.setBorder(BorderFactory.createCompoundBorder(
            createCustomBorder("GAME STATUS"),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        difficultyPanel.setOpaque(false);
        JLabel diffLabel = new JLabel("Difficulty: ");
        diffLabel.setForeground(textColor);
        diffLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        String[] difficulties = {"Easy", "Medium", "Hard"};
        difficultyCombo = new JComboBox<>(difficulties);
        difficultyCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        difficultyCombo.setSelectedItem("Medium"); // Default
        difficultyCombo.addActionListener(e -> {
            if (currentState == GameState.READY) {
                game.setDifficulty((String) difficultyCombo.getSelectedItem());
                updateStatusLabels();
            }
        });
        difficultyPanel.add(diffLabel);
        difficultyPanel.add(difficultyCombo);
        
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPanel.setOpaque(false);
        rangeLabel = createStyledLabel("", 16);
        maxAttemptLabel = createStyledLabel("", 16);
        attemptLabel = createStyledLabel("Attempt: 0 / 7", 16);
        infoPanel.add(rangeLabel);
        infoPanel.add(maxAttemptLabel);
        infoPanel.add(attemptLabel);
        
        statusCard.add(difficultyPanel, BorderLayout.NORTH);
        statusCard.add(infoPanel, BorderLayout.CENTER);
        
        // Score Board Card
        JPanel scoreCard = new JPanel(new GridLayout(2, 2, 10, 10));
        scoreCard.setBackground(cardColor);
        scoreCard.setBorder(BorderFactory.createCompoundBorder(
            createCustomBorder("SCORE BOARD"),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        currentRoundLabel = createScoreLabel("Current Round", "0");
        totalRoundsLabel = createScoreLabel("Total Played", "0");
        roundsWonLabel = createScoreLabel("Rounds Won", "0");
        roundsLostLabel = createScoreLabel("Rounds Lost", "0");
        
        scoreCard.add(currentRoundLabel);
        scoreCard.add(roundsWonLabel);
        scoreCard.add(roundsLostLabel);
        scoreCard.add(totalRoundsLabel);
        
        topSection.add(statusCard);
        topSection.add(scoreCard);
        
        mainContentPanel.add(topSection, BorderLayout.NORTH);
        
        // MIDDLE SECTION: Guess Input Area
        JPanel guessCard = new JPanel();
        guessCard.setLayout(new BoxLayout(guessCard, BoxLayout.Y_AXIS));
        guessCard.setBackground(cardColor);
        guessCard.setBorder(BorderFactory.createCompoundBorder(
            createCustomBorder("ENTER YOUR GUESS"),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JPanel inputWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        inputWrapper.setOpaque(false);
        
        guessInput = new JTextField();
        guessInput.setPreferredSize(new Dimension(300, 50)); // 50px high
        guessInput.setFont(new Font("Segoe UI", Font.BOLD, 22));
        guessInput.setHorizontalAlignment(JTextField.CENTER);
        guessInput.setBackground(bgColor);
        guessInput.setForeground(textColor);
        guessInput.setCaretColor(textColor);
        guessInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(subTextColor, 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        guessButton = new ModernButton("GUESS", ModernButton.Style.PRIMARY);
        guessButton.setPreferredSize(new Dimension(130, 50));
        
        inputWrapper.add(guessInput);
        inputWrapper.add(guessButton);
        
        attemptDotsLabel = new JLabel("○ ○ ○ ○ ○ ○ ○", SwingConstants.CENTER);
        attemptDotsLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        attemptDotsLabel.setForeground(accentColor);
        attemptDotsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        resultLabel = new JLabel("Welcome! Select difficulty and Start Game.", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        resultLabel.setForeground(textColor);
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        guessCard.add(inputWrapper);
        guessCard.add(Box.createVerticalStrut(15));
        guessCard.add(attemptDotsLabel);
        guessCard.add(Box.createVerticalStrut(15));
        guessCard.add(resultLabel);
        
        mainContentPanel.add(guessCard, BorderLayout.CENTER);
        add(mainContentPanel, BorderLayout.CENTER);
        
        // --- RIGHT PANEL: Round History ---
        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        historyArea.setBackground(bgColor);
        historyArea.setForeground(textColor);
        historyArea.setMargin(new Insets(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(historyArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setBackground(bgColor);
        
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(cardColor);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(0, 0, 0, 0),
            createCustomBorder("ROUND HISTORY")
        ));
        rightPanel.setPreferredSize(new Dimension(250, 0));
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(rightPanel, BorderLayout.EAST);
        
        // --- BOTTOM PANEL: Controls ---
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlPanel.setOpaque(false);
        
        startGameButton = new ModernButton("START GAME", ModernButton.Style.SUCCESS);
        startGameButton.setPreferredSize(new Dimension(160, 45));
        
        playAgainButton = new ModernButton("PLAY AGAIN", ModernButton.Style.PRIMARY);
        playAgainButton.setPreferredSize(new Dimension(160, 45));
        
        newGameButton = new ModernButton("NEW GAME", ModernButton.Style.SECONDARY);
        newGameButton.setPreferredSize(new Dimension(140, 45));
        
        exitButton = new ModernButton("EXIT", ModernButton.Style.DANGER);
        exitButton.setPreferredSize(new Dimension(120, 45));
        
        controlPanel.add(startGameButton);
        controlPanel.add(playAgainButton);
        controlPanel.add(newGameButton);
        controlPanel.add(exitButton);
        
        add(controlPanel, BorderLayout.SOUTH);

        // --- Action Listeners ---
        ActionListener guessAction = e -> handleGuess();
        guessButton.addActionListener(guessAction);
        guessInput.addActionListener(guessAction);
        
        startGameButton.addActionListener(e -> startGame());
        playAgainButton.addActionListener(e -> startGame());
        
        newGameButton.addActionListener(e -> {
            game.resetSession();
            completedRounds = 0;
            roundsLost = 0;
            historyArea.setText("");
            changeState(GameState.READY);
        });

        exitButton.addActionListener(e -> System.exit(0));
    }
    
    private JLabel createStyledLabel(String text, int size) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, size));
        label.setForeground(textColor);
        return label;
    }
    
    private JLabel createScoreLabel(String title, String initialValue) {
        JLabel label = new JLabel("<html><center><span style='color:#94A3B8;'>" + title + "</span><br><b style='font-size:18px;'>" + initialValue + "</b></center></html>", SwingConstants.CENTER);
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(subTextColor, 1, true),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        label.setOpaque(true);
        label.setBackground(bgColor);
        label.setForeground(textColor);
        return label;
    }
    
    private void updateScoreLabel(JLabel label, String title, String value) {
        label.setText("<html><center><span style='color:#94A3B8;'>" + title + "</span><br><b style='font-size:18px;'>" + value + "</b></center></html>");
    }
    
    private void changeState(GameState newState) {
        this.currentState = newState;
        
        switch (newState) {
            case READY:
                difficultyCombo.setEnabled(true);
                startGameButton.setVisible(true);
                playAgainButton.setVisible(false);
                guessInput.setEnabled(false);
                guessButton.setEnabled(false);
                
                guessInput.setText("");
                resultLabel.setText("Select difficulty and click START GAME.");
                resultLabel.setForeground(textColor);
                game.setDifficulty((String) difficultyCombo.getSelectedItem());
                updateStatusLabels();
                updateAttemptDots(0, game.getMaxAttempts());
                updateScoreBoard();
                break;
                
            case PLAYING:
                difficultyCombo.setEnabled(false);
                startGameButton.setVisible(false);
                playAgainButton.setVisible(false);
                guessInput.setEnabled(true);
                guessButton.setEnabled(true);
                
                guessInput.setText("");
                guessInput.requestFocusInWindow();
                resultLabel.setText("I'm thinking of a number. Can you guess it?");
                resultLabel.setForeground(textColor);
                break;
                
            case WON:
            case LOST:
                difficultyCombo.setEnabled(false); 
                startGameButton.setVisible(false);
                playAgainButton.setVisible(true);
                guessInput.setEnabled(false);
                guessButton.setEnabled(false);
                updateScoreBoard();
                break;
        }
    }

    private void startGame() {
        game.setDifficulty((String) difficultyCombo.getSelectedItem());
        game.startNewRound();
        changeState(GameState.PLAYING);
        updateStatusLabels();
        updateAttemptDots(0, game.getMaxAttempts());
        updateScoreBoard();
    }

    private void updateStatusLabels() {
        rangeLabel.setText("Range: 1 \u2013 " + game.getMaxRange());
        maxAttemptLabel.setText("Max Attempts: " + game.getMaxAttempts());
        if (currentState == GameState.READY) {
            attemptLabel.setText("Attempt: 0 / " + game.getMaxAttempts());
        } else {
            attemptLabel.setText("Attempt: " + game.getAttemptsTaken() + " / " + game.getMaxAttempts());
        }
    }
    
    private void updateAttemptDots(int taken, int max) {
        StringBuilder dots = new StringBuilder();
        for (int i = 0; i < max; i++) {
            if (i < taken) {
                dots.append("● ");
            } else {
                dots.append("○ ");
            }
        }
        attemptDotsLabel.setText(dots.toString().trim());
    }
    
    private void updateScoreBoard() {
        // If ready, show 0 for current round since we haven't started.
        int currentRnd = currentState == GameState.READY ? 0 : game.getRoundsPlayed();
        updateScoreLabel(currentRoundLabel, "Current Round", String.valueOf(currentRnd));
        updateScoreLabel(totalRoundsLabel, "Total Played", String.valueOf(completedRounds));
        updateScoreLabel(roundsWonLabel, "Rounds Won", String.valueOf(game.getSuccessfulRounds()));
        updateScoreLabel(roundsLostLabel, "Rounds Lost", String.valueOf(roundsLost));
    }

    private void handleGuess() {
        if (currentState != GameState.PLAYING) return;
        
        String inputText = guessInput.getText().trim();
        if (inputText.isEmpty()) {
            showTempResult("⚠ Please enter a number.", new Color(245, 158, 11)); // Warning
            return;
        }

        int guess;
        try {
            guess = Integer.parseInt(inputText);
        } catch (NumberFormatException ex) {
            showTempResult("⚠ Please enter a valid number.", new Color(245, 158, 11));
            guessInput.setText("");
            return;
        }

        if (guess < 1 || guess > game.getMaxRange()) {
            showTempResult("⚠ Enter a number between 1 and " + game.getMaxRange() + ".", new Color(245, 158, 11));
            guessInput.setText("");
            return;
        }

        game.incrementAttempts();
        updateStatusLabels();
        updateAttemptDots(game.getAttemptsTaken(), game.getMaxAttempts());
        
        String result = game.checkGuess(guess);
        
        if (result.equals("Correct!")) {
            resultLabel.setText("✓ CORRECT! You guessed it in " + game.getAttemptsTaken() + " attempts.");
            resultLabel.setForeground(new Color(34, 197, 94)); // Success
            game.recordSuccess();
            completedRounds++;
            historyArea.append("✓ Round " + game.getRoundsPlayed() + " \u2014 guessed in " + game.getAttemptsTaken() + " attempts\n");
            changeState(GameState.WON);
        } else {
            resultLabel.setText(result.equals("Too High!") ? "↓ Too High! Try a lower number." : "↑ Too Low! Try a higher number.");
            resultLabel.setForeground(new Color(239, 68, 68)); // Danger
            
            if (game.hasReachedMaxAttempts()) {
                resultLabel.setText("✕ You Lost! The number was " + game.getCurrentNumber() + ".");
                roundsLost++;
                completedRounds++;
                historyArea.append("✕ Round " + game.getRoundsPlayed() + " \u2014 Lost\n");
                changeState(GameState.LOST);
            }
        }
        
        guessInput.setText("");
        if (currentState == GameState.PLAYING) {
            guessInput.requestFocusInWindow();
        }
    }
    
    private void showTempResult(String message, Color color) {
        resultLabel.setText(message);
        resultLabel.setForeground(color);
    }
}
