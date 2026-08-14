Number Guessing Game
Oasis Infobyte Java Development Internship – Task 2
Project Title
Number Guessing Game

Internship Domain
Java Development

Task Number
Task 2

1. Objective
The objective of this project is to build a console or GUI-based Number Guessing Game using Java. The computer generates a random number, and the user attempts to guess it. After each guess, the system provides hints ("Too High!", "Too Low!", or "Correct!") until the correct number is guessed or the maximum attempts are reached.

This project demonstrates the use of Java basics, loops, conditionals, random number generation, user input handling, and optional GUI development with Swing.

2. Project Description
The Number Guessing Game is a simple yet interactive Java application that challenges the user to guess a randomly generated number.

The application provides:

Random number generation at the start of each round

User input via console (Scanner) or GUI (JTextField)

Feedback after each guess ("Too High!", "Too Low!", "Correct!")

Attempt counter visible throughout the game

Maximum attempt limit (default: 7 attempts)

"You Lost!" message if the user fails within the attempt limit, revealing the correct number

"Play Again" option after each round

Score tracking across multiple rounds

(Bonus) Difficulty levels:

Easy: Range 1–50, 10 attempts

Medium: Range 1–100, 7 attempts

Hard: Range 1–200, 5 attempts

3. Features
Random Number Generation
System generates a random number at the start of each round.

Range depends on difficulty level.

User Input
Console version: Scanner for input.

GUI version: JTextField for input.

Feedback System
Displays "Too High!", "Too Low!", or "Correct!" after each guess.

Attempt Counter
Tracks number of guesses made.

Visible to the user during gameplay.

Attempt Limit
Default: 7 attempts (Medium difficulty).

Game ends with "You Lost!" if limit is reached.

Play Again Option
User can restart the game after each round.

Score Tracking
Displays summary: "Round X — guessed in Y attempts".

Difficulty Levels (Bonus)
Easy: 1–50, 10 attempts

Medium: 1–100, 7 attempts

Hard: 1–200, 5 attempts

4. Technologies Used
Java

java.util.Random

Scanner (console input)

Java Swing (optional GUI)

Loops and conditionals

Object-Oriented Programming (optional for structuring)

5. Project Structure
text
JavaDevelopment-Task2-NumberGuessingGame/
│
├── src/
│   ├── Main.java
│   │
│   ├── game/
│   │   ├── NumberGuessingGame.java
│   │   ├── DifficultyLevel.java
│   │   └── ScoreTracker.java
│   │
│   ├── ui/
│   │   ├── ConsoleGame.java
│   │   └── GameFrame.java   (GUI version)
│   │
│   └── util/
│       └── GameUtils.java
│
├── bin/
├── .vscode/
├── run.bat
└── README.md
6. Main Components
Game Logic
NumberGuessingGame.java – Core game mechanics (random number, attempts, feedback).

DifficultyLevel.java – Defines ranges and attempt limits.

ScoreTracker.java – Tracks scores across rounds.

UI
ConsoleGame.java – Console-based implementation.

GameFrame.java – Swing-based GUI implementation.

Utilities
GameUtils.java – Helper methods (e.g., replay prompt, input validation).

7. Steps Performed
Designed the game structure using Java.

Implemented random number generation.

Added user input handling (console/GUI).

Implemented feedback system ("Too High!", "Too Low!", "Correct!").

Added attempt counter and maximum attempt limit.

Implemented "Play Again" option.

Added score tracking across rounds.

(Bonus) Implemented difficulty levels.

Created batch file for compiling and running the application.

8. How to Run the Project
Prerequisites
Ensure Java JDK is installed and configured.

Check Java version:

bash
java -version
Check compiler:

bash
javac -version
Method 1 – Using run.bat
On Windows:

Open the project folder.

Double-click run.bat.

The project will compile and run automatically.

Method 2 – Using Command Prompt
bash
javac -d bin src\game\*.java src\ui\*.java src\util\*.java src\Main.java
Then run:

bash
java -cp bin Main
9. Application Flow
text
Start Application
       ↓
   Select Difficulty
       ↓
   Generate Random Number
       ↓
   User Guess Input
       ↓
Feedback: Too High / Too Low / Correct
       ↓
   Attempt Counter
       ↓
Win / Lose
       ↓
   Play Again Option
       ↓
   Score Tracking
10. Outcome
The project successfully implements a functional Number Guessing Game in Java. It demonstrates:

Random number generation

User input handling

Loops and conditionals

Attempt tracking

Replay functionality

Score tracking

Optional GUI development with Swing



11. Conclusion
The Number Guessing Game was developed as part of the Oasis Infobyte Java Development Internship. The project provided practical experience in Java basics, random number generation, user input handling, loops, conditionals, and optional GUI development with Swing. It demonstrates how simple logic can be combined with user interaction to create an engaging game.