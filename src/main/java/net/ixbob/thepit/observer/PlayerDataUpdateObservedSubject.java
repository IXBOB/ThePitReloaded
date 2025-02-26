package net.ixbob.thepit.observer;

public interface PlayerDataUpdateObservedSubject {

    void attachObserver(PlayerDataUpdateObserver observer);

    void detachObserver(PlayerDataUpdateObserver observer);

    void notifyObservers(PlayerDataUpdateObservingData data);

}
