package net.ixbob.thepit;

import net.ixbob.thepit.manager.PitScoreBoardManager;
import net.ixbob.thepit.mongodb.MongoDBManager;
import net.ixbob.thepit.mongodb.collection.PlayerEconomyCollection;
import org.bson.Document;
import org.bukkit.entity.Player;

public class PlayerEconomyFactory {

    public static PlayerEconomy getEconomyForNew(Player player) {
        PlayerEconomy newEco = new PlayerEconomy(player, 1000, 0, 0);
        MongoDBManager.getInstance().getPlayerEconomyCollection().updatePlayerEcoData(newEco);
        attachObservers(newEco);
        return newEco;
    }

    public static PlayerEconomy getEconomyFromDataBase(Player player) {
        PlayerEconomyCollection dbCollection = MongoDBManager.getInstance().getPlayerEconomyCollection();
        Document savedEcoData = dbCollection.getPlayerEcoData(player);
        if (savedEcoData == null) {
            return null;
        }
        PlayerEconomy newEco = new PlayerEconomy(player,
                savedEcoData.getDouble(PlayerEconomyCollection.FIELD_PLAYER_COIN_AMOUNT),
                savedEcoData.getDouble(PlayerEconomyCollection.FIELD_PLAYER_POINT_AMOUNT),
                savedEcoData.getDouble(PlayerEconomyCollection.FIELD_PLAYER_XP_AMOUNT));
        attachObservers(newEco);
        return newEco;
    }

    private static void attachObservers(PlayerEconomy playerEconomy) {
        playerEconomy.attachObserver(MongoDBManager.getInstance().getPlayerEconomyCollection());
        playerEconomy.attachObserver(PitScoreBoardManager.getInstance());
    }

}
