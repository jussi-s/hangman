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
 */
public class HangmanGameDataMongoDAO implements HangmanGameDataDAO {

    private static HangmanGameDataMongoDAO daoObjSingleton = null;

    private static final String DBNAME = "hangman";
    private static final String COLLECTION = "games";

    private static MongoClient mongoClient;
    private static MongoDatabase mongoDatabase;

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

    @Override
    public String store(HangmanGameData data) {
        Document doc = data.createDocument();
        ObjectId objId = ObjectId.get();
        doc.append("_id", objId);
        mongoDatabase.getCollection(COLLECTION).insertOne(doc);
        return objId.toString();
    }

    @Override
    public HangmanGame getGame(String id) {
        FindIterable<Document> res = mongoDatabase.getCollection(COLLECTION)
                                                  .find(new BasicDBObject("_id", new ObjectId(id)));
        Document doc = res.first();
        HangmanGameData data = HangmanGameData.fromDocument(doc);
        data.setGameId(id);
        return new HangmanGame(data);
    }

    @Override
    public boolean exists(String id) {
        FindIterable<Document> res = mongoDatabase.getCollection(COLLECTION)
                .find(new BasicDBObject("_id", new ObjectId(id)));
        if (res.first() != null)
            return true;
        else
            return false;
    }

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

    @Override
    public void updateGame(String id, HangmanGameData data) {
        data.setGameId(id);
        mongoDatabase.getCollection(COLLECTION).findOneAndReplace(
                new BasicDBObject("_id", new ObjectId(id)),
                data.createDocument());
    }

    @Override
    public boolean delete(String id) {
        BasicDBObject idObj = new BasicDBObject("_id", new ObjectId(id));
        DeleteResult res = mongoDatabase.getCollection(COLLECTION).deleteOne(idObj);
        return res.wasAcknowledged();
    }
}
