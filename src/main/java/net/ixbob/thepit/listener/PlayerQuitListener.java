package net.ixbob.thepit.listener;

import net.ixbob.thepit.observer.PlayerQuitObservedObject;
import net.ixbob.thepit.observer.PlayerQuitObserverObject;
import net.ixbob.thepit.observer.PlayerQuitObservingData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class PlayerQuitListener implements Listener, PlayerQuitObservedObject {

    private static final PlayerQuitListener INSTANCE = new PlayerQuitListener();
    ArrayList<PlayerQuitObserverObject> playerQuitObservers = new ArrayList<>();

    private PlayerQuitListener() {
    }

    public static PlayerQuitListener getInstance() {
        return INSTANCE;
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
    public void attachObserver(PlayerQuitObserverObject observer) {
        this.playerQuitObservers.add(observer);
    }

    @Override
    public void detachObserver(PlayerQuitObserverObject observer) {
        this.playerQuitObservers.remove(observer);
    }

    @Override
    public void notifyObservers(PlayerQuitObservingData data) {
        for (PlayerQuitObserverObject observer : this.playerQuitObservers) {
            observer.onNotified(data);
        }
    }
}