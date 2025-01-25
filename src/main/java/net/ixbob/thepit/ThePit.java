package net.ixbob.thepit;

import com.github.retrooper.packetevents.PacketEvents;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import net.ixbob.thepit.economy.PlayerEconomyManager;
import net.ixbob.thepit.holder.ConfigHolder;
import net.ixbob.thepit.listener.PlayerDeathListener;
import net.ixbob.thepit.listener.PlayerJoinListener;
import net.ixbob.thepit.listener.PlayerQuitListener;
import net.ixbob.thepit.scoreboard.PitScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class ThePit extends JavaPlugin {

    private static ThePit instance;
    private static MongoClient mongoClient;
    private static MongoDatabase usingDataBase;

    public ThePit() {
        instance = this;
    }

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        PacketEvents.getAPI().init();
        saveResource("config.yml", false);
        ConfigHolder.getInstance().reload();
        PlayerEconomyManager.getInstance(); //加载类，确保观察者被实例化并激活观察者逻辑
        PitScoreboardManager.getInstance();
        registerListeners(
                PlayerJoinListener.getInstance(),
                PlayerQuitListener.getInstance(),
                new PlayerDeathListener());
        getLogger().log(Level.INFO, "Plugin successfully enabled");
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
    }

    public static ThePit getInstance() {
        return instance;
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
    }
}
