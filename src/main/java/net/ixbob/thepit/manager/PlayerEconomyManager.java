package net.ixbob.thepit.manager;

import net.ixbob.thepit.PlayerEconomy;
import net.ixbob.thepit.PlayerEconomyFactory;
import net.ixbob.thepit.listener.PlayerJoinListener;
import net.ixbob.thepit.listener.PlayerQuitListener;
import net.ixbob.thepit.observer.*;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerEconomyManager implements PlayerJoinObserver, PlayerQuitObserver {

    private static PlayerEconomyManager instance = new PlayerEconomyManager();

    HashMap<Player, PlayerEconomy> holdingEcoMap = new HashMap<>();

    private PlayerEconomyManager() {
        PlayerJoinListener.getInstance().attachObserver(this);
        PlayerQuitListener.getInstance().attachObserver(this);
    }

    public static PlayerEconomyManager getInstance() {
        if (instance == null) {
            instance = new PlayerEconomyManager();
        }
        return instance;
    }

    public PlayerEconomy getEconomy(Player player) {
        return holdingEcoMap.get(player);
    }

    @Override
    public void onNotified(PlayerJoinObservingData observedData) {
        Player player = observedData.player();
        PlayerEconomy playerEco = PlayerEconomyFactory.getEconomyFromDataBase(player);
        if (playerEco == null) {
            playerEco = PlayerEconomyFactory.getEconomyForNew(player);
        }
        this.holdingEcoMap.put(player, playerEco);
    }

    @Override
    public void onNotified(PlayerQuitObservingData observedData) {
        Player player = observedData.player();
        this.holdingEcoMap.remove(player);
    }
}
