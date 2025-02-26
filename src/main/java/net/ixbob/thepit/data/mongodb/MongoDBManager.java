package net.ixbob.thepit.data.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import net.ixbob.thepit.holder.ConfigHolder;
import net.ixbob.thepit.data.mongodb.collection.PlayerDataCollection;
import net.ixbob.thepit.data.mongodb.collection.RegisteredPlayerCollection;
import net.ixbob.thepit.util.SingletonUtil;

import java.util.function.Supplier;

public class MongoDBManager {

    private static final Supplier<MongoDBManager> instance = SingletonUtil.createSingletonEager(MongoDBManager::new);

    private static final String DB_URI = "mongodb://%s:%s";
    private final MongoClient mongoClient;
    private final MongoDatabase usingDataBase;
    private final RegisteredPlayerCollection registeredPlayerCollection;
    private final PlayerDataCollection playerDataCollection;

    private MongoDBManager() {
        final ConfigHolder configHolder = ConfigHolder.getInstance();
        if (configHolder.getIsMongoDBEnabled()) {
            mongoClient = MongoClients.create(String.format(DB_URI,
                    configHolder.getMongoDBHost(), configHolder.getMongoDBPort()));
            usingDataBase = mongoClient.getDatabase("ThePitReloaded");
            registeredPlayerCollection = new RegisteredPlayerCollection(
                    usingDataBase.getCollection("registeredPlayer"));
            playerDataCollection = new PlayerDataCollection(
                    usingDataBase.getCollection("playerData"));
        }
        else {
            throw new RuntimeException("MongoDB cannot be disabled!");
        }
    }

    public static MongoDBManager getInstance() {
        return instance.get();
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

    public PlayerDataCollection getPlayerDataCollection() {
        return playerDataCollection;
    }
}
