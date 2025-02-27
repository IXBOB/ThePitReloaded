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

public class PitPlayerDataManager implements PlayerJoinObserver, PlayerQuitObserver {

    private static final Supplier<PitPlayerDataManager> INSTANCE = SingletonUtil.createSingletonEager(PitPlayerDataManager::new);

    Set<PitPlayerData> holdingData = new HashSet<>();

    private PitPlayerDataManager() {
        PlayerJoinListener.getInstance().attachObserver(this);
        PlayerQuitListener.getInstance().attachObserver(this);
    }

    public static PitPlayerDataManager getInstance() {
        return INSTANCE.get();
    }

    public PitPlayerData getData(Player player) {
        Optional<PitPlayerData> getResult = holdingData.stream().filter(data ->
                data.getPlayer().equals(player)).findFirst();
        return getResult.orElse(null);
    }

    @Override
    public void onNotified(PlayerJoinObservingData observedData) {
        Player player = observedData.player();
        PitPlayerData pitPlayerData = PitPlayerData.getDataFromDataBase(player);
        if (pitPlayerData == null) {
            pitPlayerData = PitPlayerData.getDataForNew(player);
        }
        holdingData.add(pitPlayerData);
    }

    @Override
    public void onNotified(PlayerQuitObservingData observedData) {
        holdingData.removeAll(
                holdingData.stream().filter(data ->
                        data.getPlayer().equals(observedData.player())
                ).collect(Collectors.toSet()));
    }
}
