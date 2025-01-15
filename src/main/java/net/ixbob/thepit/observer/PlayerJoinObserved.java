package net.ixbob.thepit.observer;

public interface PlayerJoinObserved {

    void attachObserver(PlayerJoinObserver observer);

    void notifyObservers(PlayerJoinObservingData data);

}
