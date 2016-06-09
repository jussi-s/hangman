package fi.jussi.hangman.game;

/**
 * Created by Jussi on 1.6.2016.
 *
 * @author Jussi
 * This enum provides the status for a character guess.
 */
public enum GuessStatus {
    CORRECT,
    INCORRECT,
    GAME_COMPLETE,
    ALREADY_GUESSED
}
