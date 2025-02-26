package net.ixbob.thepit.data;

import net.ixbob.thepit.data.mongodb.MongoDBManager;
import net.ixbob.thepit.data.mongodb.collection.PlayerDataCollection;
import net.ixbob.thepit.observer.PlayerDataUpdateObservedSubject;
import net.ixbob.thepit.observer.PlayerDataUpdateObserver;
import net.ixbob.thepit.observer.PlayerDataUpdateObservingData;
import net.ixbob.thepit.scoreboard.PitScoreboardManager;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlayerData implements PlayerDataUpdateObservedSubject {

    private final ArrayList<PlayerDataUpdateObserver> playerEcoUpdateObservers = new ArrayList<>();
    private final Player player;
    private double coinAmount;
    private double pointAmount;
    private double xpAmount;
    private int masteryLevel;
    private int reputationAmount;

    public static PlayerData getDataForNew(Player player) {
        PlayerData newData = new PlayerData(player, 1000, 0, 0, 0, 0);
        MongoDBManager.getInstance().getPlayerDataCollection().updatePlayerData(newData);
        attachObservers(newData);
        return newData;
    }

    public static PlayerData getDataFromDataBase(Player player) {
        PlayerDataCollection dbCollection = MongoDBManager.getInstance().getPlayerDataCollection();
        Document savedData = dbCollection.getPlayerData(player);
        if (savedData == null) {
            return null;
        }
        PlayerData newEco = new PlayerData(player,
                savedData.getDouble(PlayerDataCollection.FIELD_PLAYER_COIN_AMOUNT),
                savedData.getDouble(PlayerDataCollection.FIELD_PLAYER_POINT_AMOUNT),
                savedData.getDouble(PlayerDataCollection.FIELD_PLAYER_XP_AMOUNT),
                savedData.getInteger(PlayerDataCollection.FIELD_PLAYER_MASTERY_LEVEL),
                savedData.getInteger(PlayerDataCollection.FIELD_PLAYER_REPUTATION_AMOUNT));
        attachObservers(newEco);
        return newEco;
    }

    private static void attachObservers(PlayerData playerData) {
        playerData.attachObserver(MongoDBManager.getInstance().getPlayerDataCollection());
        playerData.attachObserver(PitScoreboardManager.getInstance());
    }

    public PlayerData(Player player, double coinAmount, double pointAmount, double xpAmount, int masteryLevel, int reputationAmount) {
        this.player = player;
        this.coinAmount = coinAmount;
        this.pointAmount = pointAmount;
        this.xpAmount = xpAmount;
        this.masteryLevel = masteryLevel;
        this.reputationAmount = reputationAmount;
    }

    private void onDataUpdate() {
        this.notifyObservers(new PlayerDataUpdateObservingData(this));
        MongoDBManager.getInstance().getPlayerDataCollection().updatePlayerData(this);
    }

    public Player getPlayer() {
        return this.player;
    }

    public double getCoinAmount() {
        return coinAmount;
    }

    public void setCoinAmount(double coinAmount) {
        this.coinAmount = coinAmount;
        onDataUpdate();
    }

    public void addCoinAmount(double addCoinAmount) {
        this.setCoinAmount(this.coinAmount + addCoinAmount);
    }

    public double getPointAmount() {
        return pointAmount;
    }

    public void setPointAmount(double pointAmount) {
        this.pointAmount = pointAmount;
        onDataUpdate();
    }

    public void addPointAmount(double addPointAmount) {
        this.setPointAmount(this.pointAmount + addPointAmount);
    }

    public double getXpAmount() {
        return xpAmount;
    }

    public void setXpAmount(double xpAmount) {
        this.xpAmount = xpAmount;
        onDataUpdate();
    }

    public void addXpAmount(double addXpAmount) {
        this.setXpAmount(this.xpAmount + addXpAmount);
    }

    public int getMasteryLevel() {
        return masteryLevel;
    }

    public void setMasteryLevel(int masteryLevel) {
        this.masteryLevel = masteryLevel;
        onDataUpdate();
    }

    public void addMasteryLevel(int addMasteryLevel) {
        this.setMasteryLevel(this.masteryLevel + addMasteryLevel);
    }

    public int getReputationAmount() {
        return reputationAmount;
    }

    public void setReputationAmount(int reputationAmount) {
        this.reputationAmount = reputationAmount;
        onDataUpdate();
    }

    public void addReputationAmount(int addReputationAmount) {
        this.setReputationAmount(this.reputationAmount + addReputationAmount);
    }

    @Override
    public void attachObserver(PlayerDataUpdateObserver observer) {
        this.playerEcoUpdateObservers.add(observer);
    }

    @Override
    public void detachObserver(PlayerDataUpdateObserver observer) {
        this.playerEcoUpdateObservers.remove(observer);
    }

    @Override
    public void notifyObservers(PlayerDataUpdateObservingData data) {
        playerEcoUpdateObservers.forEach(observer -> observer.onNotified(data));
    }
}
