package net.ixbob.thepit;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import net.ixbob.thepit.holder.PlayerEconomyHolder;
import net.ixbob.thepit.listener.PlayerJoinListener;
import net.ixbob.thepit.listener.PlayerQuitListener;
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
    public void onEnable() {
        registerListeners(
                PlayerJoinListener.getInstance(),
                PlayerQuitListener.getInstance());
        PlayerEconomyHolder playerEcoHolder = PlayerEconomyHolder.getInstance(); //加载类，确保实例化
        getLogger().log(Level.INFO, "Plugin successfully enabled");
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
