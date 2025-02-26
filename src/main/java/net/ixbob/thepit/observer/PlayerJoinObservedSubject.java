package net.ixbob.thepit.observer;

public interface PlayerJoinObservedSubject {

    void attachObserver(PlayerJoinObserver observer);

    void notifyObservers(PlayerJoinObservingData data);

}
