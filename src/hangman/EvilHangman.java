package hangman;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class EvilHangman {

    public static void main(String[] args) {
        char validLetters[] = {'a', 'b', 'c', 'd', 'f', 'q', 'w', 'e','r', 't', 'y', 'u', 'i', 'o', 'p','s', 'g', 'h', 'j', 'k', 'l', 'z','x','v','n','m'};


        File dictionaryFile = new File(args[0]);
        int wordLength = Integer.parseInt(args[1]);
        int numberofGuesses = Integer.parseInt(args[2]);
        EvilHangmanGame eg = new EvilHangmanGame();
        try {
            eg.startGame(dictionaryFile, wordLength);
        } catch (IOException e) {
            System.out.print("Error reading file");
            System.exit(0);
        } catch (EmptyDictionaryException e) {
            System.out.print("Empty dictionary");
            System.exit(0);
        }
        char guess = '0';
        Scanner sc=new Scanner(System.in);
        int count = 0;
        while (numberofGuesses > 0) {
            System.out.print("\nYou have ");
            System.out.print(numberofGuesses);
            System.out.print(" left\n");
            System.out.print("Used letters: ");
            System.out.print(eg.guessedLetters);
            System.out.print("");
            System.out.print("Word: ");
            if (count == 0) {
                for (int i = 0; i < wordLength; i++) {
                    System.out.print("-");
                }
            } else {
                System.out.print(eg.currentWord.toString());
            }
            boolean temp = false;
            System.out.print("Enter guess: \n");
            guess = sc.next().charAt(0);
            count++;
            try {
                for (int i = 0; i < 26; i++) {
                    if (guess == validLetters[i]) {
                        temp = true;
                        break;
                    }
                }
                if (temp == false) {
                    System.out.print("Invalid character! \n");
                    continue;
                }
                eg.makeGuess(guess);
                if (eg.getLetterInWord() == false && temp == true) {
                    numberofGuesses--;
                    System.out.print("Sorry, there are no ");
                    System.out.print(guess);
                }
                else if (temp == true){
                    System.out.print("Yes, there is ");
                    System.out.print(eg.letterCount);
                    System.out.print(" ");
                    System.out.print(guess);
                }
            } catch (GuessAlreadyMadeException e) {
                System.out.print("You already guessed that letter!");
            }
            if (eg.winGame() == true) {
                System.out.print("\nGood job! You won the game! The word was: ");
                System.out.print(eg.getWord());
                return;
            }
            if (numberofGuesses == 0) {
                    System.out.print("\nSorry, you lost! The word was: ");
                    System.out.print(eg.getWord());
                    return;
            }
        }
    }
}
