package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class EvilHangman {
    public static void main(String[] args) throws EmptyDictionaryException, IOException, GuessAlreadyMadeException {
        EvilHangmanGame userHangmanGame = new EvilHangmanGame();

        //set up the variables and files and scanners for input for character
        String dictionaryString = args[0];
        String wordLengthString = args[1];
        String numGuessesString = args[2];
        File dictionary = new File(dictionaryString);
        int wordLength = Integer.parseInt(wordLengthString);
        int numGuesses = Integer.parseInt(numGuessesString);

        //initialize Game
        userHangmanGame.startGame(dictionary,wordLength);

        Set<String>usedGuesses = new TreeSet<>();

        Scanner inputScanner = new Scanner(System.in);

        //output to string and prompt for user input
        while(numGuesses != 0) {
            System.out.println("You have " + numGuesses + " guesses left");
            //System.out.println("Used letters: " + usedGuesses.toString());
            System.out.println("Used letters: " + userHangmanGame.getGuessedLetters().toString());
            System.out.println("Word: " + userHangmanGame.getCurrentWord());
            System.out.println("Enter a guess: ");
            String userGuess = inputScanner.next().toLowerCase();
            char userGuessChar = userGuess.charAt(0);

            //invalid guess
            while(userGuess.length() > 1 || userGuess.equals("\n") || !Character.isLetter(userGuessChar) || usedGuesses.contains(userGuess)){
                if (usedGuesses.contains(userGuess)){
                    System.out.println("Guess already made! Enter guess: ");
                    userGuess = inputScanner.next().toLowerCase();
                    userGuessChar = userGuess.charAt(0);
                    throw new GuessAlreadyMadeException("Guess already made! Enter guess: ");

                }
                else {
                    System.out.println("Invalid input! Enter guess: ");
                    userGuess = inputScanner.next().toLowerCase();
                    userGuessChar = userGuess.charAt(0);
                }
            }
            usedGuesses.add(userGuess);
            userHangmanGame.makeGuess(userGuessChar);
            numGuesses--;

            boolean status = wonGame(numGuesses,userHangmanGame.currentWord);
            if(status == true){
                System.out.println("You won!");
            }
            if(numGuesses == 0 && status == false){
                System.out.println("You lose!");
                System.out.println("The word was: " + userHangmanGame.getFirstWord());
            }

        }


        String userInput = inputScanner.nextLine();






        //write an interactive loop so that the user can actually play the game with input etc.
        // specs show how this can be implemented

    }
    public static boolean wonGame(int numGuesses, String currentString){
        for (int i = 0; i < currentString.length(); i++) {
            if(currentString.charAt(i)== '_'){
                return false;
            }

        }

        return true;
    }

}
