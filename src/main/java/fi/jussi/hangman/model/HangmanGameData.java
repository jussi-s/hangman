package fi.jussi.hangman.model;

import fi.jussi.hangman.util.Utils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by Jussi on 8.6.2016.
 *
 * @author Jussi
 * This class contains the hangman game data.
 */
public class HangmanGameData {

    public static final int MAX_WRONG_GUESSES = 10;
    private int WRONG_GUESSES_SO_FAR = 0;

    private String gameId = "";
    private String word = "";
    private String wordSoFar = "";
    private Map<Character, List<Integer>> wordCharMap = null;
    private Set<Character> guessedChars = null;

    /**
     * Constructs a new game data instance based on a word.
     * @param word The word to be used in the game.
     */
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

    /**
     * Constructs a new game data instance. This method is used when retrieving
     * data from the MongoDB database, as the wordCharMap and guessedChars work
     * with different data structures in MongoDB.
     * @param wrongGuesses Number of wrong guesses in the game.
     * @param word The word for the game.
     * @param wordSoFar The word so far.
     * @param wordCharMap Characters of the word, mapped to indices.
     * @param guessedChars Characters already guessed.
     */
    public HangmanGameData(int wrongGuesses, String word, String wordSoFar,
                           Map<String, List<Integer>> wordCharMap,
                           List<String> guessedChars) {
        this.WRONG_GUESSES_SO_FAR = wrongGuesses;
        this.word = word;
        this.wordSoFar = wordSoFar;
        this.wordCharMap = Utils.mapStrToChr(wordCharMap);
        this.guessedChars = Utils.listToSet(guessedChars);
    }

    /**
     * Creates a new game data instance from a MongoDB/BSON document.
     * @param doc The document instance.
     * @return The game data.
     */
    public static HangmanGameData fromDocument(Document doc) {
        String word = doc.getString("word");
        String wordSoFar = doc.getString("wordSoFar");
        int wrongGuesses = doc.getInteger("wrongGuesses");
        Map<String, List<Integer>> wordCharMap = (Map<String, List<Integer>>) doc.get("wordCharMap");
        List<String> guessedChars = (List<String>) doc.get("guessedChars");
        return new HangmanGameData(wrongGuesses, word, wordSoFar, wordCharMap, guessedChars);
    }

    /**
     * Creates a new BSON document of the game data. Used with the MongoDB DAO.
     * @return The game data as a BSON document
     */
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

    /**
     * Retrieves the game ID.
     * @return The game ID.
     */
    public String getGameId() {
        return gameId;
    }

    /**
     * Sets the game ID.
     * @param gameId The game ID to be set.
     */
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    /**
     * Retrieves the amount of wrong guesses.
     * @return The amount of wrong guesses.
     */
    public int getWrongGuesses() {
        return WRONG_GUESSES_SO_FAR;
    }

    /**
     * Retrieves the word used in the game.
     * @return The word as a String.
     */
    public String getWord() {
        return word;
    }

    /**
     * Retrieves the word so far.
     * @return The word so far.
     */
    public String getWordSoFar() {
        return wordSoFar;
    }

    /**
     * Retrieves the character/indices map for the current word of the game.
     * @return The character/indices map.
     */
    public Map<Character, List<Integer>> getWordCharMap() {
        return wordCharMap;
    }

    /**
     * Retrieves the characters that are already guessed in the game.
     * @return The already guessed characters.
     */
    public Set<Character> getGuessedChars() {
        return guessedChars;
    }

    /**
     * Sets the word so far.
     * @param wordSoFar The desired word so far.
     */
    public void setWordSoFar(String wordSoFar) {
        this.wordSoFar = wordSoFar;
    }

    /**
     * Increment the amount of wrong guesses in the game.
     */
    public void incrWrongGuesses() {
        this.WRONG_GUESSES_SO_FAR++;
    }

    /**
     * Add a new guessed character.
     * @param c The character that was guessed.
     */
    public void addGuessedChar(Character c) {
        this.guessedChars.add(c);
    }
}
