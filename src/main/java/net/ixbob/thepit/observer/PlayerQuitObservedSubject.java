package net.ixbob.thepit.observer;

public interface PlayerQuitObservedSubject {

    void attachObserver(PlayerQuitObserver observer);

    void notifyObservers(PlayerQuitObservingData data);

}
