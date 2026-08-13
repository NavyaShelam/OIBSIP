import java.util.Random;
import java.io.Serializable;

public final class NumberGuessingGame implements Serializable {
    private static final long serialVersionUID = 1L;
    private int currentNumber;
    private int attemptsTaken;
    private int maxAttempts;
    private int maxRange;
    
    private int roundsPlayed;
    private int successfulRounds;
    
    private Random random;

    public NumberGuessingGame() {
        this.random = new Random();
        this.roundsPlayed = 0;
        this.successfulRounds = 0;
        setDifficulty("Easy"); // Default fallback
    }

    public void setDifficulty(String difficulty) {
        if (difficulty.equals("Easy")) {
            this.maxRange = 50;
            this.maxAttempts = 10;
        } else if (difficulty.equals("Medium")) {
            this.maxRange = 100;
            this.maxAttempts = 7;
        } else if (difficulty.equals("Hard")) {
            this.maxRange = 200;
            this.maxAttempts = 5;
        }
    }

    public void startNewRound() {
        this.currentNumber = random.nextInt(maxRange) + 1;
        this.attemptsTaken = 0;
        this.roundsPlayed++;
    }

    public void recordSuccess() {
        this.successfulRounds++;
    }

    public void incrementAttempts() {
        this.attemptsTaken++;
    }

    public boolean hasReachedMaxAttempts() {
        return attemptsTaken >= maxAttempts;
    }

    public String checkGuess(int guess) {
        if (guess < currentNumber) {
            return "Too Low!";
        } else if (guess > currentNumber) {
            return "Too High!";
        } else {
            return "Correct!";
        }
    }

    public void resetSession() {
        this.roundsPlayed = 0;
        this.successfulRounds = 0;
    }

    // Getters
    public int getAttemptsTaken() { return attemptsTaken; }
    public int getMaxAttempts() { return maxAttempts; }
    public int getMaxRange() { return maxRange; }
    public int getRoundsPlayed() { return roundsPlayed; }
    public int getSuccessfulRounds() { return successfulRounds; }
    public int getCurrentNumber() { return currentNumber; }
}
