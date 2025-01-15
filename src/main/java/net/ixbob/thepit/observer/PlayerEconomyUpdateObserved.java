package net.ixbob.thepit.observer;

public interface PlayerEconomyUpdateObserved {

    void attachObserver(PlayerEconomyUpdateObserver observer);

    void notifyObservers(PlayerEconomyUpdateObservingData data);

}
