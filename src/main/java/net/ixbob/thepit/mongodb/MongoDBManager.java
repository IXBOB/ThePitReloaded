package net.ixbob.thepit.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import net.ixbob.thepit.mongodb.collection.PlayerEconomyCollection;
import net.ixbob.thepit.mongodb.collection.RegisteredPlayerCollection;

public class MongoDBManager {

    private static final String DB_URI = "mongodb://localhost:27017";

    private static final MongoDBManager instance = new MongoDBManager();
    private final MongoClient mongoClient;
    private final MongoDatabase usingDataBase;
    private final RegisteredPlayerCollection registeredPlayerCollection;
    private final PlayerEconomyCollection playerEconomyCollection;

    private MongoDBManager() {
        mongoClient = MongoClients.create(DB_URI);
        usingDataBase = mongoClient.getDatabase("ThePitReloaded");
        registeredPlayerCollection = new RegisteredPlayerCollection(usingDataBase.getCollection("registeredPlayer"));
        playerEconomyCollection = new PlayerEconomyCollection(usingDataBase.getCollection("playerEconomy"));
    }

    public static MongoDBManager getInstance() {
        return instance;
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public MongoDatabase getUsingDataBase() {
        return usingDataBase;
    }

    public RegisteredPlayerCollection getRegisteredPlayerCollection() {
        return registeredPlayerCollection;
    }

    public PlayerEconomyCollection getPlayerEconomyCollection() {
        return playerEconomyCollection;
    }
}
