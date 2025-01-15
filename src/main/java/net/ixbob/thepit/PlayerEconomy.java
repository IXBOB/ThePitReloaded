package net.ixbob.thepit;

import net.ixbob.thepit.mongodb.MongoDBManager;
import net.ixbob.thepit.observer.PlayerEconomyUpdateObserved;
import net.ixbob.thepit.observer.PlayerEconomyUpdateObserver;
import net.ixbob.thepit.observer.PlayerEconomyUpdateObservingData;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlayerEconomy implements PlayerEconomyUpdateObserved {

    private final ArrayList<PlayerEconomyUpdateObserver> playerEcoUpdateObservers = new ArrayList<>();
    private final Player player;
    private double coinAmount;
    private double pointAmount;
    private double xpAmount;

    public PlayerEconomy(Player player, double coinAmount, double pointAmount, double xpAmount) {
        this.player = player;
        this.coinAmount = coinAmount;
        this.pointAmount = pointAmount;
        this.xpAmount = xpAmount;
    }

    private void onEconomyUpdate() {
        this.notifyObservers(new PlayerEconomyUpdateObservingData(this));
        MongoDBManager.getInstance().getPlayerEconomyCollection().updatePlayerEcoData(this);
    }

    @Override
    public void attachObserver(PlayerEconomyUpdateObserver observer) {
        this.playerEcoUpdateObservers.add(observer);
    }

    @Override
    public void notifyObservers(PlayerEconomyUpdateObservingData data) {
        for (PlayerEconomyUpdateObserver observer : this.playerEcoUpdateObservers) {
            observer.onNotified(data);
        }
    }

    public Player getPlayer() {
        return this.player;
    }

    public double getCoinAmount() {
        return coinAmount;
    }

    public void setCoinAmount(double coinAmount) {
        this.coinAmount = coinAmount;
        onEconomyUpdate();
    }

    public void addCoinAmount(double addCoinAmount) {
        this.setCoinAmount(this.coinAmount + addCoinAmount);
    }

    public double getPointAmount() {
        return pointAmount;
    }

    public void setPointAmount(double pointAmount) {
        this.pointAmount = pointAmount;
        onEconomyUpdate();
    }

    public void addPointAmount(double addPointAmount) {
        this.setPointAmount(this.pointAmount + addPointAmount);
    }

    public double getXpAmount() {
        return xpAmount;
    }

    public void setXpAmount(double xpAmount) {
        this.xpAmount = xpAmount;
        onEconomyUpdate();
    }

    public void addXpAmount(double addXpAmount) {
        this.setXpAmount(this.xpAmount + addXpAmount);
    }
}
