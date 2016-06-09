package fi.jussi.hangman.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import fi.jussi.hangman.game.HangmanGame;
import fi.jussi.hangman.model.HangmanGameData;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jussi on 9.6.2016.
 *
 * @author Jussi
 * A MongoDB implementation for the HangmanGameDataDAO interface.
 */
public class HangmanGameDataMongoDAO implements HangmanGameDataDAO {

    private static HangmanGameDataMongoDAO daoObjSingleton = null;

    private static final String DBNAME = "hangman";
    private static final String COLLECTION = "games";

    private static MongoClient mongoClient;
    private static MongoDatabase mongoDatabase;

    /**
     * Create a DAO instance, initialize the Mongo variables once so that
     * there is only one connection pool.
     */
    public HangmanGameDataMongoDAO() {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");
        DBConnectionProperties dbConnProps = (DBConnectionProperties) context.getBean("dbProps");
        if (mongoClient == null) {
            mongoClient = new MongoClient(dbConnProps.getHost(), dbConnProps.getPort());
        }
        if (mongoDatabase == null) {
            mongoDatabase = mongoClient.getDatabase(DBNAME);
        }
    }


    /**
     * Stores the game data as ObjectId is generated.
     * @param data The game data (ID field does not have to be initialized).
     * @return The ObjectId as a String.
     */
    @Override
    public String store(HangmanGameData data) {
        Document doc = data.createDocument();
        ObjectId objId = ObjectId.get();
        doc.append("_id", objId);
        mongoDatabase.getCollection(COLLECTION).insertOne(doc);
        return objId.toString();
    }

    /**
     * Retrieve a game by its ID.
     * @param id The ID of the game.
     * @return The game instance, used in the REST API.
     */
    @Override
    public HangmanGame getGame(String id) {
        FindIterable<Document> res = mongoDatabase.getCollection(COLLECTION)
                                                  .find(new BasicDBObject("_id", new ObjectId(id)));
        Document doc = res.first();
        HangmanGameData data = HangmanGameData.fromDocument(doc);
        data.setGameId(id);
        return new HangmanGame(data);
    }

    /**
     * Checks if a game exists with the given ID.
     * @param id The ID of a game.
     * @return True if a game exists with the given ID, false if not.
     */
    @Override
    public boolean exists(String id) {
        FindIterable<Document> res = mongoDatabase.getCollection(COLLECTION)
                .find(new BasicDBObject("_id", new ObjectId(id)));
        if (res.first() != null)
            return true;
        else
            return false;
    }

    /**
     * Get all games' data.
     * @return All Hangman Game instances as a list.
     */
    @Override
    public List<HangmanGame> getAllGames() {
        FindIterable<Document> res = mongoDatabase.getCollection(COLLECTION).find();
        MongoCursor<Document> iterator = res.iterator();
        List<HangmanGame> games = new ArrayList<>();
        while (iterator.hasNext()) {
            Document doc = iterator.next();
            HangmanGameData gameData = HangmanGameData.fromDocument(doc);
            gameData.setGameId(doc.get("_id").toString());
            HangmanGame game = new HangmanGame(gameData);
            games.add(game);
        }
        return games;
    }

    /**
     * Updates data for the given game. Used during guessing a character.
     * @param id The ID of the game.
     * @param data The updated data for the game.
     */
    @Override
    public void updateGame(String id, HangmanGameData data) {
        data.setGameId(id);
        mongoDatabase.getCollection(COLLECTION).findOneAndReplace(
                new BasicDBObject("_id", new ObjectId(id)),
                data.createDocument());
    }

    /**
     * Delete a game which matches the given ID.
     * @param id The ID of the game.
     * @return True if the game was deleted, false if not.
     */
    @Override
    public boolean delete(String id) {
        BasicDBObject idObj = new BasicDBObject("_id", new ObjectId(id));
        DeleteResult res = mongoDatabase.getCollection(COLLECTION).deleteOne(idObj);
        return res.wasAcknowledged();
    }
}
