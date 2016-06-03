package fi.jussi.hangman.rest;

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
 */
@Path("game")
public class HangmanGameResource {

    @Context
    ServletContext servletContext;

    private String gamesAttribute = "games";

    @POST
    @Path("/start")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response newGame(@FormParam("wordForGame") String word) throws JSONException {
        word = word.toUpperCase();
        HangmanGame hangmanGame = new HangmanGame(word);
        JSONObject res = new JSONObject();

        if (servletContext.getAttribute(gamesAttribute) == null) {
            servletContext.setAttribute(gamesAttribute, new LinkedHashMap<String, HangmanGame>());
        }

        LinkedHashMap<String, HangmanGame> gamesMap = (LinkedHashMap<String, HangmanGame>)
                servletContext.getAttribute(gamesAttribute);

        if (containsGame(word, gamesMap)) {
            res.put("ERRORS", "Game already exists.");
        } else {
            String next = String.valueOf((gamesMap.size() + 1));
            gamesMap.put(next, hangmanGame);
            servletContext.setAttribute(gamesAttribute, gamesMap);
            res.put("game", next);
            res.put("status", "CREATED");
        }

        return Response.status(200).entity(res.toString()).build();
    }

    @POST
    @Path("/guess")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response guessCharacter(@FormParam("game") String game, @FormParam("character") String ch) throws JSONException {
        String gameKey = game;
        Character c = ch.charAt(0);

        JSONObject res = new JSONObject();
        HashMap<String, HangmanGame> gamesMap = null;

        if (servletContext.getAttribute(gamesAttribute) == null) {
            res.put("ERRORS", "No games started.");
        } else {
            gamesMap = (HashMap<String, HangmanGame>)
                    servletContext.getAttribute(gamesAttribute);
        }

        if (gamesMap != null && gamesMap.containsKey(gameKey)) {
            HangmanGame current = gamesMap.get(gameKey);
            GuessStatus gStatus = current.guess(c);
            res.put("GUESS", gStatus.toString());
        } else {
            res.put("ERRORS", "Game " + gameKey + " not found.");
        }

        return Response.status(200).entity(res.toString()).build();
    }

    @GET
    @Path("status")
    @Produces("application/json")
    public Response gamesStatus() throws JSONException {
        JSONObject res = new JSONObject();

        if (servletContext.getAttribute(gamesAttribute) != null) {
            HashMap<String, HangmanGame> gamesMap = (HashMap<String, HangmanGame>)
                    servletContext.getAttribute(gamesAttribute);

            for (Map.Entry<String, HangmanGame> ent : gamesMap.entrySet()) {
                HangmanGame game = ent.getValue();
                JSONObject gameStatusJson = game.getStatusJson();
                gameStatusJson.put("id", ent.getKey());
                res.put(ent.getKey(), gameStatusJson);
            }
        }

        return Response.status(200).entity(res.toString()).build();
    }

    @GET
    @Path("status/{id}")
    @Produces("application/json")
    public Response statusForGame(@PathParam("id") String id) throws JSONException {
        JSONObject res = new JSONObject();
        String gameKey = id;

        if (servletContext.getAttribute(gamesAttribute) != null) {
            HashMap<String, HangmanGame> gamesMap = (HashMap<String, HangmanGame>)
                    servletContext.getAttribute(gamesAttribute);
            if (gamesMap.containsKey(id)) {
                HangmanGame game = gamesMap.get(id);
                res = game.getStatusJson();
            } else {
                res.put("ERRORS", "Game " + gameKey + " does not exist.");
            }
        } else {
            res.put("ERRORS", "There are currently no games being played.");
        }

        return Response.status(200).entity(res.toString()).build();
    }

    private boolean containsGame(String word, HashMap<String, HangmanGame> gamesMap) {
        boolean found = false;
        for (Map.Entry<String, HangmanGame> ent : gamesMap.entrySet()) {
            try {
                if (ent.getValue().getStatusJson().get(word) != null) {
                    found = true;
                }
            } catch (JSONException exp) {
                // Not found, could append to a log
            }
        }
        return found;
    }
}
