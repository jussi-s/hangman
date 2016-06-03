package fi.jussi.hangman.game;

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
        assertEquals(hangmanGame.getWordSoFar(), "_____");
        assertEquals(hangmanGame.getGameStatus(), GameStatus.IN_PROGRESS);
        assertEquals(hangmanGame.getWord(), "HELLO");
        assertEquals(hangmanGame.getGuessedChars().size(), 0);
    }

    @Test
    public void testAddCorrectCharacters() {
        guessTest('h', GuessStatus.CORRECT);
        assertEquals(hangmanGame.getWordSoFar(), "H____");

        guessTest('e', GuessStatus.CORRECT);
        assertEquals(hangmanGame.getWordSoFar(), "HE___");

        guessTest('l', GuessStatus.CORRECT);
        assertEquals(hangmanGame.getWordSoFar(), "HELL_");

        guessTest('o', GuessStatus.CORRECT);
        assertEquals(hangmanGame.getWordSoFar(), "HELLO");

        assertEquals(hangmanGame.getGameStatus(), GameStatus.SUCCESSFUL);
    }

    @Test
    public void testAddIncorrectCharacters() {
        guessTest('x', GuessStatus.INCORRECT);
        assertEquals(hangmanGame.getGuessedChars().size(), 1);

        for (int i = 0; i < 20; i++) {
            guessTest('x', GuessStatus.ALREADY_GUESSED);
        }

        // Already guessed attempts do not increment the wrong guesses count
        assertEquals(hangmanGame.getGameStatus(), GameStatus.IN_PROGRESS);
        assertEquals(hangmanGame.getGuessedChars().size(), 1);
        assertEquals(hangmanGame.getWordSoFar(), "_____");

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
        assertEquals(hangmanGame.getGuessedChars().size(), 10);
        assertEquals(hangmanGame.getWordSoFar(), "_____");
    }

    private void guessTest(char c, GuessStatus expected) {
        assertEquals(hangmanGame.guess(c), expected);
    }
}
