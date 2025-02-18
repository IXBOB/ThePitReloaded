package net.ixbob.thepit.listener;

import net.ixbob.thepit.mongodb.MongoDBManager;
import net.ixbob.thepit.observer.PlayerJoinObserved;
import net.ixbob.thepit.observer.PlayerJoinObserver;
import net.ixbob.thepit.observer.PlayerJoinObservingData;
import net.ixbob.thepit.util.SingletonUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.function.Supplier;

public class PlayerJoinListener implements Listener, PlayerJoinObserved {

    private static final Supplier<PlayerJoinListener> instance = SingletonUtil.createSingleton(PlayerJoinListener::new);
    ArrayList<PlayerJoinObserver> playerJoinObservers = new ArrayList<>();

    private PlayerJoinListener() {}

    public static PlayerJoinListener getInstance() {
        return instance.get();
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
    public void attachObserver(PlayerJoinObserver observer) {
        this.playerJoinObservers.add(observer);
    }

    @Override
    public void notifyObservers(PlayerJoinObservingData data) {
        for (PlayerJoinObserver observer : this.playerJoinObservers) {
            observer.onNotified(data);
        }
    }
}
