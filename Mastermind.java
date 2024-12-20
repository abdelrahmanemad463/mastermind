package mastermind;

import java.util.Scanner;
import java.util.Random;

class Mastermind {
    private static final int CODE_LENGTH = 4;
    private static final String[] VALID_COLORS = {"Red", "Blue", "Green", "Yellow", "Orange", "Purple"};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Mastermind!");
        System.out.println("Choose your game mode:");
        System.out.println("1. Play against the computer");
        System.out.println("2. Play against another player");
        System.out.print("Enter your choice (1 or 2): ");

        int gameMode = getChoice(scanner, 1, 2);
        String[] secretCode;

        if (gameMode == 1) {
            secretCode = generateRandomCode();
            System.out.println("The computer has generated a secret code.");
        } else {
            secretCode = getValidCode(scanner, "Player 1: Enter your secret code (e.g., Red Blue Green Yellow): ");
        }

        System.out.print("Player 2, how many attempts would you like to have? ");
        int maxAttempts = getPositiveInteger(scanner);

        System.out.println("\nPlayer 2: Try to guess the secret code!");
        System.out.println("Possible colors: Red, Blue, Green, Yellow, Orange, Purple.");
        System.out.println("You have " + maxAttempts + " attempts.");

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            System.out.println("Attempt " + attempt + ": Enter your guess (e.g., Red Blue Green Yellow):");
            String[] guess = getValidCode(scanner, "Player 2: Enter your guess (e.g., Red Blue Green Yellow): ");

            int[] feedback = evaluateGuess(secretCode, guess);
            System.out.println("Correct position: " + feedback[0]);
            System.out.println("Correct color (wrong position): " + feedback[1]);

            if (feedback[0] == CODE_LENGTH) {
                System.out.println("Congratulations! You cracked the code!");
                return;
            }
        }

        System.out.println("Game Over! You ran out of attempts.");
        System.out.print("The correct code was: ");
        for (String color : secretCode) {
            System.out.print(color + " ");
        }
        System.out.println();
    }

    private static String[] generateRandomCode() {
        Random random = new Random();
        String[] code = new String[CODE_LENGTH];
        for (int i = 0; i < CODE_LENGTH; i++) {
            code[i] = VALID_COLORS[random.nextInt(VALID_COLORS.length)];
        }
        return code;
    }

    private static String[] getValidCode(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            String[] code = input.split("\\s+");

            if (code.length != CODE_LENGTH) {
                System.out.println("Invalid input! Please enter exactly " + CODE_LENGTH + " colors.");
                continue;
            }

            if (!areValidColors(code)) {
                System.out.println("Invalid input! Only these colors are allowed: Red, Blue, Green, Yellow, Orange, Purple.");
                continue;
            }

            return code;
        }
    }

    private static boolean areValidColors(String[] guess) {
        for (String color : guess) {
            boolean isValid = false;
            for (String validColor : VALID_COLORS) {
                if (color.equalsIgnoreCase(validColor)) {
                    isValid = true;
                    break;
                }
            }
            if (!isValid) {
                return false;
            }
        }
        return true;
    }

    private static int[] evaluateGuess(String[] code, String[] guess) {
        int correctPosition = 0;
        int correctColorWrongPosition = 0;

        boolean[] codeUsed = new boolean[code.length];
        boolean[] guessUsed = new boolean[guess.length];

        for (int i = 0; i < code.length; i++) {
            if (guess[i].equalsIgnoreCase(code[i])) {
                correctPosition++;
                codeUsed[i] = true;
                guessUsed[i] = true;
            }
        }

        for (int i = 0; i < guess.length; i++) {
            if (!guessUsed[i]) {
                for (int j = 0; j < code.length; j++) {
                    if (!codeUsed[j] && guess[i].equalsIgnoreCase(code[j])) {
                        correctColorWrongPosition++;
                        codeUsed[j] = true;
                        break;
                    }
                }
            }
        }

        return new int[]{correctPosition, correctColorWrongPosition};
    }

    private static int getChoice(Scanner scanner, int min, int max) {
        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= min && choice <= max) {
                    return choice;
                }
                System.out.print("Invalid choice. Enter a number between " + min + " and " + max + ": ");
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    private static int getPositiveInteger(Scanner scanner) {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine());
                if (value > 0) {
                    return value;
                }
                System.out.print("Please enter a positive number: ");
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }
}
