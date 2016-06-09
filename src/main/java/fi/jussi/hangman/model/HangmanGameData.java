package fi.jussi.hangman.model;

import fi.jussi.hangman.util.Utils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by Jussi on 8.6.2016.
 */
public class HangmanGameData {

    public static final int MAX_WRONG_GUESSES = 10;
    private int WRONG_GUESSES_SO_FAR = 0;

    private String gameId = "";
    private String word = "";
    private String wordSoFar = "";
    private Map<Character, List<Integer>> wordCharMap = null;
    private Set<Character> guessedChars = null;

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

    public HangmanGameData(int wrongGuesses, String word, String wordSoFar,
                           Map<String, List<Integer>> wordCharMap,
                           List<String> guessedChars) {
        this.WRONG_GUESSES_SO_FAR = wrongGuesses;
        this.word = word;
        this.wordSoFar = wordSoFar;
        this.wordCharMap = Utils.mapStrToChr(wordCharMap);
        this.guessedChars = Utils.listToSet(guessedChars);
    }

    public static HangmanGameData fromDocument(Document doc) {
        String word = doc.getString("word");
        String wordSoFar = doc.getString("wordSoFar");
        int wrongGuesses = doc.getInteger("wrongGuesses");
        Map<String, List<Integer>> wordCharMap = (Map<String, List<Integer>>) doc.get("wordCharMap");
        List<String> guessedChars = (List<String>) doc.get("guessedChars");
        return new HangmanGameData(wrongGuesses, word, wordSoFar, wordCharMap, guessedChars);
    }

    public Document createDocument() {
        Document doc = new Document()
            .append("word", this.word)
            .append("wordSoFar", this.wordSoFar)
            .append("wrongGuesses", this.WRONG_GUESSES_SO_FAR)
            .append("wordCharMap", Utils.wordCharMapStrKey(this.wordCharMap))
            .append("guessedChars", this.guessedChars);
        if (this.gameId.length() > 0) {
            doc.append("_id", new ObjectId(this.gameId));
        }
        return doc;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
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

    public Map<Character, List<Integer>> getWordCharMap() {
        return wordCharMap;
    }

    public Set<Character> getGuessedChars() {
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
