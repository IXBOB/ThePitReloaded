package net.ixbob.thepit.holder;

import net.ixbob.thepit.PlayerEconomy;
import net.ixbob.thepit.listener.PlayerJoinListener;
import net.ixbob.thepit.listener.PlayerQuitListener;
import net.ixbob.thepit.observer.PlayerJoinObserverObject;
import net.ixbob.thepit.observer.PlayerJoinObservingData;
import net.ixbob.thepit.observer.PlayerQuitObserverObject;
import net.ixbob.thepit.observer.PlayerQuitObservingData;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerEconomyHolder implements PlayerJoinObserverObject, PlayerQuitObserverObject {

    private static final PlayerEconomyHolder INSTANCE = new PlayerEconomyHolder();

    HashMap<Player, PlayerEconomy> holdingEcoMap = new HashMap<>();

    private PlayerEconomyHolder() {
        PlayerJoinListener.getInstance().attachObserver(this);
        PlayerQuitListener.getInstance().attachObserver(this);
    }

    public static PlayerEconomyHolder getInstance() {
        return INSTANCE;
    }

    public PlayerEconomy getEconomy(Player player) {
        return holdingEcoMap.get(player);
    }

    @Override
    public void onNotified(PlayerJoinObservingData observedData) {
        Player player = observedData.player();
        PlayerEconomy playerEco = PlayerEconomy.getEconomyFromDataBase(player);
        if (playerEco == null) {
            playerEco = PlayerEconomy.getEconomyForNew(player);
        }
        this.holdingEcoMap.put(player, playerEco);
        System.out.println(this.holdingEcoMap);
    }

    @Override
    public void onNotified(PlayerQuitObservingData observedData) {
        Player player = observedData.player();
        this.holdingEcoMap.remove(player);
        System.out.println(this.holdingEcoMap);
    }
}
