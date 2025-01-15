package net.ixbob.thepit.observer;

public interface PlayerQuitObserved {

    void attachObserver(PlayerQuitObserver observer);

    void notifyObservers(PlayerQuitObservingData data);

}
