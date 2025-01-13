package net.ixbob.thepit;

import net.ixbob.thepit.mongodb.MongoDBManager;
import net.ixbob.thepit.mongodb.collection.PlayerEconomyCollection;
import org.bson.Document;
import org.bukkit.entity.Player;

public class PlayerEconomy {

    private final Player player;
    private double coinAmount;
    private double pointAmount;
    private double xpAmount;

    private PlayerEconomy(Player player, double coinAmount, double pointAmount, double xpAmount) {
        this.player = player;
        this.coinAmount = coinAmount;
        this.pointAmount = pointAmount;
        this.xpAmount = xpAmount;
    }

    public static PlayerEconomy getEconomyForNew(Player player) {
        PlayerEconomy newEco = new PlayerEconomy(player, 1000, 0, 0);
        MongoDBManager.getInstance().getPlayerEconomyCollection().updatePlayerEcoData(newEco);
        return newEco;
    }

    public static PlayerEconomy getEconomyFromDataBase(Player player) {
        PlayerEconomyCollection dbCollection = MongoDBManager.getInstance().getPlayerEconomyCollection();
        Document savedEcoData = dbCollection.getPlayerEcoData(player);
        if (savedEcoData == null) {
            return null;
        }
        return new PlayerEconomy(player,
                savedEcoData.getDouble(PlayerEconomyCollection.FIELD_PLAYER_COIN_AMOUNT),
                savedEcoData.getDouble(PlayerEconomyCollection.FIELD_PLAYER_POINT_AMOUNT),
                savedEcoData.getDouble(PlayerEconomyCollection.FIELD_PLAYER_XP_AMOUNT));
    }

    public Player getPlayer() {
        return this.player;
    }

    public double getCoinAmount() {
        return coinAmount;
    }

    public void setCoinAmount(double coinAmount) {
        this.coinAmount = coinAmount;
        MongoDBManager.getInstance().getPlayerEconomyCollection().updatePlayerEcoData(this);
    }

    public double getPointAmount() {
        return pointAmount;
    }

    public void setPointAmount(double pointAmount) {
        this.pointAmount = pointAmount;
        MongoDBManager.getInstance().getPlayerEconomyCollection().updatePlayerEcoData(this);
    }

    public double getXpAmount() {
        return xpAmount;
    }

    public void setXpAmount(double xpAmount) {
        this.xpAmount = xpAmount;
        MongoDBManager.getInstance().getPlayerEconomyCollection().updatePlayerEcoData(this);
    }
}
