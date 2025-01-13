package net.ixbob.thepit.observer;

public interface PlayerJoinObservedObject {

    void attachObserver(PlayerJoinObserverObject observer);

    void detachObserver(PlayerJoinObserverObject observer);

    void notifyObservers(PlayerJoinObservingData data);

}
