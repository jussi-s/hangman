package fi.jussi.hangman.dao;

import fi.jussi.hangman.game.HangmanGame;
import fi.jussi.hangman.model.HangmanGameData;

import java.util.List;

/**
 * Created by Jussi on 9.6.2016.
 *
 * @author Jussi
 * Interface for CRUD operations across the Hangman Game.
 */
public interface HangmanGameDataDAO {

    /**
     * Stores the game data into the database.
     * @param data The game data (ID field does not have to be initialized).
     * @return The ID of the game, used to redirect the browser to the game page.
     */
    public String store(HangmanGameData data);

    /**
     * Retrieve a game by its ID.
     * @param id The ID of the game.
     * @return The game instance, used in the REST API.
     */
    public HangmanGame getGame(String id);

    /**
     * Checks if a game exists with the given ID.
     * @param id The ID of a game.
     * @return True if a game exists with the given ID, false if not.
     */
    public boolean exists(String id);

    /**
     * Get all games' data.
     * @return All Hangman Game instances as a list.
     */
    public List<HangmanGame> getAllGames();

    /**
     * Updates data for the given game. Used during guessing a character.
     * @param id The ID of the game.
     * @param data The updated data for the game.
     */
    public void updateGame(String id, HangmanGameData data);

    /**
     * Delete a game which matches the given ID.
     * @param id The ID of the game.
     * @return True if the game was deleted, false if not.
     */
    public boolean delete(String id);

}
