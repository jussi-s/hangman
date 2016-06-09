package fi.jussi.hangman.dao;

import fi.jussi.hangman.game.HangmanGame;
import fi.jussi.hangman.model.HangmanGameData;

import java.util.List;

/**
 * Created by Jussi on 9.6.2016.
 */
public interface HangmanGameDataDAO {

    public String store(HangmanGameData data);

    public HangmanGame getGame(String id);

    public boolean exists(String id);

    public List<HangmanGame> getAllGames();

    public void updateGame(String id, HangmanGameData data);

    public boolean delete(String id);

}
