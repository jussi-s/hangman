package fi.jussi.hangman.game;

import fi.jussi.hangman.model.HangmanGameData;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by Jussi on 1.6.2016.
 *
 * @author Jussi
 * This class contains the business logic for the Hangman Game - guessing characters.
 */
public class HangmanGame {

    private HangmanGameData gameData = null;

    /**
     * Constructs a new game instance based on a word. Used when a new game is
     * created on the front-end web form.
     * @param word The word for the new game.
     */
    public HangmanGame(String word) {
        this.gameData = new HangmanGameData(word);
    }

    /**
     * Constructs a new game instance based on existing game data.
     * @param data The game data.
     */
    public HangmanGame(HangmanGameData data) {
        this.gameData = data;
    }

    /**
     * Guess a character by checking if the provided character exists in the
     * wordCharMap. Game must be in progress and the character provided must
     * be a new one (not already guessed).
     * @param c The character to be guessed.
     * @return Status of the guess (INCORRECT, CORRECT, GAME_COMPLETE or ALREADY_GUESSED).
     */
    public GuessStatus guess(Character c) {
        c = Character.toUpperCase(c);
        StringBuilder current = new StringBuilder(gameData.getWordSoFar());
        GuessStatus status;
        if (this.getGameStatus() == GameStatus.FAILED || this.getGameStatus() == GameStatus.SUCCESSFUL) {
            return GuessStatus.GAME_COMPLETE;
        }
        if (gameData.getGuessedChars().contains(c)) {
            return GuessStatus.ALREADY_GUESSED;
        } else if (gameData.getWordCharMap().containsKey(c)) {
            for (Integer index : gameData.getWordCharMap().get(c)) {
                current.setCharAt(index, c);
            }
            gameData.setWordSoFar(current.toString());
            status = GuessStatus.CORRECT;
        } else {
            gameData.incrWrongGuesses();
            status = GuessStatus.INCORRECT;
        }
        gameData.addGuessedChar(c);
        return status;
    }

    /**
     * Retrieves the status of the game.
     * @return The current game status as the GameStatus enum defines.
     */
    public GameStatus getGameStatus() {
        if (gameData.getWordSoFar().equals(gameData.getWord())) {
            return GameStatus.SUCCESSFUL;
        } else if (gameData.getWrongGuesses() == gameData.MAX_WRONG_GUESSES) {
            return GameStatus.FAILED;
        } else {
            return GameStatus.IN_PROGRESS;
        }
    }

    /**
     * Retrieves the game status as JSON. Used for the front-end.
     * @return The status as JSON.
     * @throws JSONException
     */
    public JSONObject getStatusJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("word", gameData.getWord());
        json.put("wordSoFar", gameData.getWordSoFar());
        json.put("gameStatus", getGameStatus().toString());
        json.put("wrongGuesses", gameData.getWrongGuesses());
        return json;
    }

    /**
     * Retrieves the game data.
     * @return The game data.
     */
    public HangmanGameData getGameData() {
        return this.gameData;
    }

}
