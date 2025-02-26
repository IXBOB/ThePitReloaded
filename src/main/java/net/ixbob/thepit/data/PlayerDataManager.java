package net.ixbob.thepit.data;

import net.ixbob.thepit.listener.PlayerJoinListener;
import net.ixbob.thepit.listener.PlayerQuitListener;
import net.ixbob.thepit.observer.PlayerJoinObserver;
import net.ixbob.thepit.observer.PlayerJoinObservingData;
import net.ixbob.thepit.observer.PlayerQuitObserver;
import net.ixbob.thepit.observer.PlayerQuitObservingData;
import net.ixbob.thepit.util.SingletonUtil;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PlayerDataManager implements PlayerJoinObserver, PlayerQuitObserver {

    private static final Supplier<PlayerDataManager> INSTANCE = SingletonUtil.createSingletonEager(PlayerDataManager::new);

    Set<PlayerData> holdingData = new HashSet<>();

    private PlayerDataManager() {
        PlayerJoinListener.getInstance().attachObserver(this);
        PlayerQuitListener.getInstance().attachObserver(this);
    }

    public static PlayerDataManager getInstance() {
        return INSTANCE.get();
    }

    public PlayerData getData(Player player) {
        Optional<PlayerData> getResult = holdingData.stream().filter(data ->
                data.getPlayer().equals(player)).findFirst();
        return getResult.orElse(null);
    }

    @Override
    public void onNotified(PlayerJoinObservingData observedData) {
        Player player = observedData.player();
        PlayerData playerData = PlayerData.getDataFromDataBase(player);
        if (playerData == null) {
            playerData = PlayerData.getDataForNew(player);
        }
        holdingData.add(playerData);
    }

    @Override
    public void onNotified(PlayerQuitObservingData observedData) {
        holdingData.removeAll(
                holdingData.stream().filter(data ->
                        data.getPlayer().equals(observedData.player())
                ).collect(Collectors.toSet()));
    }
}
