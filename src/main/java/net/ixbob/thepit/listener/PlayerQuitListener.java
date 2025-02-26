package net.ixbob.thepit.listener;

import net.ixbob.thepit.observer.PlayerQuitObservedSubject;
import net.ixbob.thepit.observer.PlayerQuitObserver;
import net.ixbob.thepit.observer.PlayerQuitObservingData;
import net.ixbob.thepit.util.SingletonUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.function.Supplier;

public class PlayerQuitListener implements Listener, PlayerQuitObservedSubject {

    private static final Supplier<PlayerQuitListener> instance = SingletonUtil.createSingletonLazy(PlayerQuitListener::new);
    private final ArrayList<PlayerQuitObserver> playerQuitObservers = new ArrayList<>();

    private PlayerQuitListener() {}

    public static PlayerQuitListener getInstance() {
        return instance.get();
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        Player player = event.getPlayer();
        TextComponent quitMsg = Component.text("[", NamedTextColor.GRAY)
                .append(Component.text("-", NamedTextColor.RED))
                .append(Component.text("]", NamedTextColor.GRAY))
                .append(Component.text(" "))
                .append(player.displayName()
                        .color(NamedTextColor.GRAY));
        this.notifyObservers(new PlayerQuitObservingData(player));
        event.quitMessage(quitMsg);
    }

    @Override
    public void attachObserver(PlayerQuitObserver observer) {
        this.playerQuitObservers.add(observer);
    }

    @Override
    public void notifyObservers(PlayerQuitObservingData data) {
        for (PlayerQuitObserver observer : this.playerQuitObservers) {
            observer.onNotified(data);
        }
    }
}
