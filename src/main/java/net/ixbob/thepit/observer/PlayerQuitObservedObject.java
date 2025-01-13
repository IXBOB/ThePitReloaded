package net.ixbob.thepit.observer;

public interface PlayerQuitObservedObject {

    void attachObserver(PlayerQuitObserverObject observer);

    void detachObserver(PlayerQuitObserverObject observer);

    void notifyObservers(PlayerQuitObservingData data);

}
