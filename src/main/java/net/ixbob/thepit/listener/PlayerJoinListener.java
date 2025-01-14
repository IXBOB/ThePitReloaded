package net.ixbob.thepit.listener;

import net.ixbob.thepit.mongodb.MongoDBManager;
import net.ixbob.thepit.observer.PlayerJoinObservedObject;
import net.ixbob.thepit.observer.PlayerJoinObserverObject;
import net.ixbob.thepit.observer.PlayerJoinObservingData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;

public class PlayerJoinListener implements Listener, PlayerJoinObservedObject {

    private static final PlayerJoinListener INSTANCE = new PlayerJoinListener();
    ArrayList<PlayerJoinObserverObject> playerJoinObservers = new ArrayList<>();

    private PlayerJoinListener() {
    }

    public static PlayerJoinListener getInstance() {
        return INSTANCE;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        TextComponent joinMsg = Component.text("[", NamedTextColor.GRAY)
                .append(Component.text("+", NamedTextColor.GREEN))
                .append(Component.text("]", NamedTextColor.GRAY))
                .append(Component.text(" "))
                .append(player.displayName()
                        .color(NamedTextColor.GRAY));
        MongoDBManager.getInstance().getRegisteredPlayerCollection().registerIfFirstJoin(player);
        this.notifyObservers(new PlayerJoinObservingData(player));
        event.joinMessage(joinMsg);
    }

    @Override
    public void attachObserver(PlayerJoinObserverObject observer) {
        this.playerJoinObservers.add(observer);
    }

    @Override
    public void notifyObservers(PlayerJoinObservingData data) {
        for (PlayerJoinObserverObject observer : this.playerJoinObservers) {
            observer.onNotified(data);
        }
    }
}
