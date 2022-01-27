package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class EvilHangmanGame implements IEvilHangmanGame {
    private Set<String> dicSet;
    public StringBuilder currentWord = new StringBuilder();
    private Boolean letterInWord;
    private int lengthofWord;
    public int letterCount;
    private Map<String, Set<String>> partition;
    public SortedSet<Character> guessedLetters = new TreeSet<Character>();
    //The map needs to keep track of the words in the partition


    public EvilHangmanGame() {
        partition = new HashMap<>();
        lengthofWord = 0;
        dicSet = new HashSet<String>();
        letterInWord = false;
    }

    public Boolean getLetterInWord() {
        return letterInWord;
    }

    public Set getSet () {
        return dicSet;
    }

    public boolean winGame () {
        /*for (Map.Entry<String,Set<String>> mapSet : partition.entrySet()) {
            /*if (!mapSet.getKey().contains("-")) {
                return true;
            }*/

            for (int i = 0; i < lengthofWord; i++) {
                if (currentWord.charAt(i) == '-') {
                    return false;
                }
            }
        return true;
    }

    public String getWord () {
        return dicSet.iterator().next();
    }

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        Boolean testBool = false;
        lengthofWord = wordLength;
        int count = 0;
        dicSet.clear();
        Scanner sc = new Scanner(dictionary);
        if (!(dictionary.exists())) {
            throw new IOException();
        }
        if (dictionary.length() == 0) {
            throw new EmptyDictionaryException();
        }
        if (wordLength == 1) {
            throw new EmptyDictionaryException();
        }
        while (sc.hasNext()) {
            count++;
            String nextWord = sc.next();
            if (nextWord.length() == wordLength) {
                testBool = true;
                dicSet.add(nextWord);
            }
        }
        if (testBool != true) {
            throw new EmptyDictionaryException();
        }
        if (dicSet.isEmpty()){
           throw new EmptyDictionaryException();
        }
        for (int i = 0; i < lengthofWord; i++) {
            currentWord.append("-");
        }
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        guess = Character.toLowerCase(guess);
        if (getGuessedLetters().contains(guess)) {
            throw new GuessAlreadyMadeException();
        }
        letterCount = 0;
        partition.clear(); //clears the map for the further guesses
        guessedLetters.add(guess);
        Set<String> set1 = new HashSet<>(); //New subset
        StringBuilder subsetKey = new StringBuilder("");
        //partition set
        StringBuilder emptyString = new StringBuilder("");
        for (int i = 0; i < lengthofWord; i++) {
            emptyString.append("-");
        }
        for (String dictionarySet : dicSet) { //Iterates through the set
            //dictionarySet is the current word
            subsetKey = new StringBuilder("");
            for (int i = 0; i < lengthofWord; i++) { //iterates through each letter of the word in the set
                //create a subsetKey
                if (dictionarySet.charAt(i) == guess) {
                    subsetKey.append(guess);
                } else {
                    subsetKey.append("-");
                }
            }
            if (partition.containsKey(subsetKey.toString())) {
                partition.get(subsetKey.toString()).add(dictionarySet);
            } else {
                partition.put(subsetKey.toString(), new HashSet<>());
                partition.get(subsetKey.toString()).add(dictionarySet);
            }
        }
        String bestKey = subsetKey.toString();
        boolean sizeChange = false;
        boolean letterChange = false;
        boolean isEmpty = false;
        int mapsetcount1 = 0;
        int bestsetcount2 = 0;
        for (Map.Entry<String, Set<String>> mapSet : partition.entrySet()) {
            mapsetcount1 = 0;
            bestsetcount2 = 0;
            if (partition.get(bestKey).size() > mapSet.getValue().size()) {
                sizeChange = true;
            }
            if (mapSet.getValue().size() > partition.get(bestKey).size()) {
                bestKey = mapSet.getKey();
                sizeChange = true;
            }
            if (sizeChange == false) {
                for (int i = 0; i < lengthofWord; i++) { //determines which set has the least guessed letters
                    if (mapSet.getKey().charAt(i) == '-') {
                        mapsetcount1++;
                    }
                    if (bestKey.charAt(i) == '-') {
                        bestsetcount2++;
                    }
                }
                if (mapsetcount1 > bestsetcount2) {
                    bestKey = mapSet.getKey();
                    letterChange = true;
                }
            }
            if (bestKey.equals(emptyString.toString())) {
                isEmpty = true;
            }
            if (letterChange == false && sizeChange == false && isEmpty == false && (mapsetcount1 == bestsetcount2)) {
                for (int i = lengthofWord - 1; i >= 0; i--) {
                    if ((mapSet.getKey().charAt(i) == guess) && (bestKey.charAt(i) != guess)) {
                        bestKey = mapSet.getKey();
                        break;
                    } else if ((mapSet.getKey().charAt(i) != guess) && (bestKey.charAt(i) == guess)) {
                        break;
                    }
                }
            }
        }
        if (bestKey.equals(emptyString.toString())) {
            letterInWord = false;
        } else {
            letterInWord = true;
        }
        set1 = partition.get(bestKey);
        dicSet = set1;
        for (int i = 0; i < lengthofWord; i++) { //determines which set has the least guessed letters
            if (bestKey.charAt(i) != '-') {
                currentWord.replace(i,i+1, String.valueOf(bestKey.charAt(i)));
                letterCount++;
            }
        }
        return set1;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedLetters;
    }

    public void isGuessedAlready(char letter) throws GuessAlreadyMadeException {
        if (getGuessedLetters().contains(letter)) {
            throw new GuessAlreadyMadeException();
        }
    }
}
