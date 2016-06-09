package fi.jussi.hangman.rest;

import fi.jussi.hangman.dao.HangmanGameDataDAO;
import fi.jussi.hangman.dao.HangmanGameDataMongoDAO;
import fi.jussi.hangman.game.GuessStatus;
import fi.jussi.hangman.game.HangmanGame;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Jussi on 2.6.2016.
 *
 * @author Jussi
 * The REST API for the Hangman Game.
 */
@Path("game")
public class HangmanGameResource {

    HangmanGameDataDAO dao = new HangmanGameDataMongoDAO();

    /**
     * Creates a new game and returns its ID in the response.
     * @param word The word for the game.
     * @return Response containing the game ID.
     * @throws JSONException
     */
    @POST
    @Path("/start")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response newGame(@FormParam("wordForGame") String word) throws JSONException {
        word = word.toUpperCase();
        HangmanGame hangmanGame = new HangmanGame(word);
        JSONObject res = new JSONObject();
        String id = dao.store(hangmanGame.getGameData());
        res.put("game", id);
        return Response.status(200).entity(res.toString()).build();
    }

    /**
     * Guess a character for the given game.
     * @param game The game ID.
     * @param ch The character to guess.
     * @return Response object containing the status of the guess.
     * @throws JSONException
     */
    @POST
    @Path("/guess")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response guessCharacter(@FormParam("game") String game, @FormParam("character") String ch) throws JSONException {
        Character c = ch.charAt(0);
        JSONObject res = new JSONObject();

        if (dao.exists(game)) {
            HangmanGame current = dao.getGame(game);
            GuessStatus gStatus = current.guess(c);
            dao.updateGame(game, current.getGameData());
            res.put("GUESS", gStatus.toString());
        } else {
            res.put("ERRORS", "Game " + game + " not found.");
        }

        return Response.status(200).entity(res.toString()).build();
    }

    /**
     * Retrieves the status for all games.
     * @return Response object containing the status of all games in the JSON payload.
     * @throws JSONException
     */
    @GET
    @Path("status")
    @Produces("application/json")
    public Response gamesStatus() throws JSONException {
        JSONObject res = new JSONObject();

        for (HangmanGame game : dao.getAllGames()) {
            JSONObject gameStatusJson = game.getStatusJson();
            String id = game.getGameData().getGameId();
            gameStatusJson.put("id", id);
            res.put(id, gameStatusJson);
        }

        return Response.status(200).entity(res.toString()).build();
    }

    /**
     * Retrieves the status for a given game. This method is polled from the front-end
     * to synchronize the game status.
     * @param id The ID of the game.
     * @return Response object containing the status of the game.
     * @throws JSONException
     */
    @GET
    @Path("status/{id}")
    @Produces("application/json")
    public Response statusForGame(@PathParam("id") String id) throws JSONException {
        JSONObject res = new JSONObject();

        if (dao.exists(id)) {
            HangmanGame game = dao.getGame(id);
            res = game.getStatusJson();
        } else {
            res.put("ERRORS", "Game " + id + " does not exist.");
        }

        return Response.status(200).entity(res.toString()).build();
    }

}
