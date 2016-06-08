package fi.jussi.hangman.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by Jussi on 8.6.2016.
 */
public class HangmanGameData {

    public static final int MAX_WRONG_GUESSES = 10;
    private int WRONG_GUESSES_SO_FAR = 0;

    private String word = "";
    private String wordSoFar = "";
    private TreeMap<Character, List<Integer>> wordCharMap = null;
    private TreeSet<Character> guessedChars = null;

    public HangmanGameData(String word) {
        word = word.toUpperCase();
        this.word = word;
        this.wordSoFar = new String(new char[word.length()]).replace("\0", "_");
        wordCharMap = new TreeMap<>();
        guessedChars = new TreeSet<>();

        for (int i = 0; i < word.length(); i++) {
            Character c = word.charAt(i);
            List<Integer> indices = null;
            if (wordCharMap.containsKey(c)) {
                indices = wordCharMap.get(c);
            } else {
                indices = new ArrayList<>();
            }
            indices.add(i);
            wordCharMap.put(c, indices);
        }
    }

    public int getWrongGuesses() {
        return WRONG_GUESSES_SO_FAR;
    }

    public String getWord() {
        return word;
    }

    public String getWordSoFar() {
        return wordSoFar;
    }

    public TreeMap<Character, List<Integer>> getWordCharMap() {
        return wordCharMap;
    }

    public TreeSet<Character> getGuessedChars() {
        return guessedChars;
    }

    public void setWordSoFar(String wordSoFar) {
        this.wordSoFar = wordSoFar;
    }

    public void incrWrongGuesses() {
        this.WRONG_GUESSES_SO_FAR++;
    }

    public void addGuessedChar(Character c) {
        this.guessedChars.add(c);
    }
}
