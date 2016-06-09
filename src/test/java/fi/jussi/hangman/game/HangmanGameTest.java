package fi.jussi.hangman.game;

import fi.jussi.hangman.model.HangmanGameData;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Jussi on 1.6.2016.
 */
public class HangmanGameTest {

    private String word = "Hello";
    private HangmanGame hangmanGame = null;

    @Before
    public void setUp() {
        hangmanGame = new HangmanGame(word);
    }

    @Test
    public void testInitializeGame() {
        HangmanGameData data = hangmanGame.getGameData();
        assertEquals(data.getWordSoFar(), "_____");
        assertEquals(hangmanGame.getGameStatus(), GameStatus.IN_PROGRESS);
        assertEquals(data.getWord(), "HELLO");
        assertEquals(data.getGuessedChars().size(), 0);
    }

    @Test
    public void testAddCorrectCharacters() {
        HangmanGameData data = hangmanGame.getGameData();
        guessTest('h', GuessStatus.CORRECT);
        assertEquals(data.getWordSoFar(), "H____");

        guessTest('e', GuessStatus.CORRECT);
        assertEquals(data.getWordSoFar(), "HE___");

        guessTest('l', GuessStatus.CORRECT);
        assertEquals(data.getWordSoFar(), "HELL_");

        guessTest('o', GuessStatus.CORRECT);
        assertEquals(data.getWordSoFar(), "HELLO");

        assertEquals(hangmanGame.getGameStatus(), GameStatus.SUCCESSFUL);
    }

    @Test
    public void testAddIncorrectCharacters() {
        HangmanGameData data = hangmanGame.getGameData();
        guessTest('x', GuessStatus.INCORRECT);
        assertEquals(data.getGuessedChars().size(), 1);

        for (int i = 0; i < 20; i++) {
            guessTest('x', GuessStatus.ALREADY_GUESSED);
        }

        // Already guessed attempts do not increment the wrong guesses count
        assertEquals(hangmanGame.getGameStatus(), GameStatus.IN_PROGRESS);
        assertEquals(data.getGuessedChars().size(), 1);
        assertEquals(data.getWordSoFar(), "_____");

        guessTest('c', GuessStatus.INCORRECT);
        guessTest('z', GuessStatus.INCORRECT);
        guessTest('y', GuessStatus.INCORRECT);
        guessTest('p', GuessStatus.INCORRECT);
        guessTest('t', GuessStatus.INCORRECT);
        guessTest('q', GuessStatus.INCORRECT);
        guessTest('v', GuessStatus.INCORRECT);
        guessTest('m', GuessStatus.INCORRECT);
        guessTest('r', GuessStatus.INCORRECT);

        assertEquals(hangmanGame.getGameStatus(), GameStatus.FAILED);
        assertEquals(data.getGuessedChars().size(), 10);
        assertEquals(data.getWordSoFar(), "_____");
    }

    private void guessTest(char c, GuessStatus expected) {
        assertEquals(hangmanGame.guess(c), expected);
    }
}
