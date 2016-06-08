package fi.jussi.hangman.game;

import fi.jussi.hangman.model.HangmanGameData;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by Jussi on 1.6.2016.
 */
public class HangmanGame {

    private HangmanGameData gameData = null;

    public HangmanGame(String word) {
        this.gameData = new HangmanGameData(word);
    }

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

    public GameStatus getGameStatus() {
        if (gameData.getWordSoFar().equals(gameData.getWord())) {
            return GameStatus.SUCCESSFUL;
        } else if (gameData.getWrongGuesses() == gameData.MAX_WRONG_GUESSES) {
            return GameStatus.FAILED;
        } else {
            return GameStatus.IN_PROGRESS;
        }
    }

    public JSONObject getStatusJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("word", gameData.getWord());
        json.put("wordSoFar", gameData.getWordSoFar());
        json.put("gameStatus", getGameStatus().toString());
        json.put("wrongGuesses", gameData.getWrongGuesses());
        return json;
    }

    public HangmanGameData getGameData() {
        return this.gameData;
    }

}
