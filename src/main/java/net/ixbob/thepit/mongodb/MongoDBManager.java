package net.ixbob.thepit.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import net.ixbob.thepit.holder.ConfigHolder;
import net.ixbob.thepit.mongodb.collection.PlayerEconomyCollection;
import net.ixbob.thepit.mongodb.collection.RegisteredPlayerCollection;
import net.ixbob.thepit.util.SingletonUtil;

import java.util.function.Supplier;

public class MongoDBManager {

    private static final Supplier<MongoDBManager> instance = SingletonUtil.createSingletonEager(MongoDBManager::new);

    private static final String DB_URI = "mongodb://%s:%s";
    private final MongoClient mongoClient;
    private final MongoDatabase usingDataBase;
    private final RegisteredPlayerCollection registeredPlayerCollection;
    private final PlayerEconomyCollection playerEconomyCollection;

    private MongoDBManager() {
        final ConfigHolder configHolder = ConfigHolder.getInstance();
        if (configHolder.getIsMongoDBEnabled()) {
            mongoClient = MongoClients.create(String.format(DB_URI,
                    configHolder.getMongoDBHost(), configHolder.getMongoDBPort()));
            usingDataBase = mongoClient.getDatabase("ThePitReloaded");
            registeredPlayerCollection = new RegisteredPlayerCollection(
                    usingDataBase.getCollection("registeredPlayer"));
            playerEconomyCollection = new PlayerEconomyCollection(
                    usingDataBase.getCollection("playerEconomy"));
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

    public PlayerEconomyCollection getPlayerEconomyCollection() {
        return playerEconomyCollection;
    }
}
