package net.ixbob.thepit.data;

import net.ixbob.thepit.data.mongodb.MongoDBManager;
import net.ixbob.thepit.data.mongodb.collection.PitPlayerDataCollection;
import net.ixbob.thepit.observer.PlayerDataUpdateObservedSubject;
import net.ixbob.thepit.observer.PlayerDataUpdateObserver;
import net.ixbob.thepit.observer.PlayerDataUpdateObservingData;
import net.ixbob.thepit.scoreboard.PitScoreboardManager;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PitPlayerData implements PlayerDataUpdateObservedSubject {

    private final ArrayList<PlayerDataUpdateObserver> playerEcoUpdateObservers = new ArrayList<>();
    private final Player player;
    private double coinAmount;
    private double pointAmount;
    private double xpAmount;
    private int masteryLevel;
    private int reputationAmount;

    public static PitPlayerData getDataForNew(Player player) {
        PitPlayerData newData = new PitPlayerData(player, 1000, 0, 0, 0, 0);
        MongoDBManager.getInstance().getPlayerDataCollection().update(newData);
        attachObservers(newData);
        return newData;
    }

    public static PitPlayerData getDataFromDataBase(Player player) {
        PitPlayerDataCollection dbCollection = MongoDBManager.getInstance().getPlayerDataCollection();
        Document savedData = dbCollection.get(player);
        if (savedData == null) {
            return null;
        }
        PitPlayerData newEco = new PitPlayerData(player,
                savedData.getDouble(PitPlayerDataCollection.FIELD_PLAYER_COIN_AMOUNT),
                savedData.getDouble(PitPlayerDataCollection.FIELD_PLAYER_POINT_AMOUNT),
                savedData.getDouble(PitPlayerDataCollection.FIELD_PLAYER_XP_AMOUNT),
                savedData.getInteger(PitPlayerDataCollection.FIELD_PLAYER_MASTERY_LEVEL),
                savedData.getInteger(PitPlayerDataCollection.FIELD_PLAYER_REPUTATION_AMOUNT));
        attachObservers(newEco);
        return newEco;
    }

    private static void attachObservers(PitPlayerData pitPlayerData) {
        pitPlayerData.attachObserver(MongoDBManager.getInstance().getPlayerDataCollection());
        pitPlayerData.attachObserver(PitScoreboardManager.getInstance());
    }

    public PitPlayerData(Player player, double coinAmount, double pointAmount, double xpAmount, int masteryLevel, int reputationAmount) {
        this.player = player;
        this.coinAmount = coinAmount;
        this.pointAmount = pointAmount;
        this.xpAmount = xpAmount;
        this.masteryLevel = masteryLevel;
        this.reputationAmount = reputationAmount;
    }

    private void onDataUpdate() {
        this.notifyObservers(new PlayerDataUpdateObservingData(this));
        MongoDBManager.getInstance().getPlayerDataCollection().update(this);
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
