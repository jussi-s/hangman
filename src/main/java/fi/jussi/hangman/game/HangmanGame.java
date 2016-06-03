package fi.jussi.hangman.game;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by Jussi on 1.6.2016.
 */
public class HangmanGame {

    private static int MAX_WRONG_GUESSES = 10;
    private int WRONG_GUESSES_SO_FAR = 0;

    private String word = "";
    private String wordSoFar = "";
    private TreeMap<Character, List<Integer>> wordCharMap = null;
    private TreeSet<Character> guessedChars = null;

    public HangmanGame(String word) {
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

    public GuessStatus guess(Character c) {
        c = Character.toUpperCase(c);
        StringBuilder current = new StringBuilder(wordSoFar);
        GuessStatus status;
        if (this.getGameStatus() == GameStatus.FAILED || this.getGameStatus() == GameStatus.SUCCESSFUL) {
            return GuessStatus.GAME_COMPLETE;
        }
        if (guessedChars.contains(c)) {
            return GuessStatus.ALREADY_GUESSED;
        } else if (wordCharMap.containsKey(c)) {
            for (Integer index : wordCharMap.get(c)) {
                current.setCharAt(index, c);
            }
            this.wordSoFar = current.toString();
            status = GuessStatus.CORRECT;
        } else {
            WRONG_GUESSES_SO_FAR++;
            status = GuessStatus.INCORRECT;
        }
        guessedChars.add(c);
        return status;
    }

    public GameStatus getGameStatus() {
        if (this.wordSoFar.equals(this.word)) {
            return GameStatus.SUCCESSFUL;
        } else if (WRONG_GUESSES_SO_FAR == MAX_WRONG_GUESSES) {
            return GameStatus.FAILED;
        } else {
            return GameStatus.IN_PROGRESS;
        }
    }

    public JSONObject getStatusJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("word", this.word);
        json.put("wordSoFar", this.wordSoFar);
        json.put("gameStatus", this.getGameStatus().toString());
        json.put("wrongGuesses", this.WRONG_GUESSES_SO_FAR);
        return json;
    }

    public String getWord() {
        return word;
    }

    public String getWordSoFar() {
        return wordSoFar;
    }

    public TreeSet<Character> getGuessedChars() {
        return guessedChars;
    }

}
