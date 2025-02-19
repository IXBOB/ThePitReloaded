package net.ixbob.thepit.holder;

import net.ixbob.thepit.ThePit;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class ConfigHolder {

    private static final ConfigHolder INSTANCE = new ConfigHolder();

    public static ConfigHolder getInstance() {
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    public void reload() {
        FileConfiguration config = ThePit.getInstance().getConfig();
        isMongoDBEnabled = config.getBoolean("database.mongodb.enabled");
        mongoDBHost = config.getString("database.mongodb.host");
        mongoDBPort = config.getInt("database.mongodb.port");
        worldId = config.getInt("game.world-id");
        worldUID = Bukkit.getWorlds().get(worldId).getUID();
        enablePvpZonePoses = (List<LinkedHashMap<String, List<Number>>>) config.getList("game.enable-pvp-zone");
    }

    private boolean isMongoDBEnabled;
    private String mongoDBHost;
    private int mongoDBPort;
    private int worldId;
    private UUID worldUID;
    private List<LinkedHashMap<String, List<Number>>> enablePvpZonePoses;

    public boolean getIsMongoDBEnabled() {
        return this.isMongoDBEnabled;
    }

    public String getMongoDBHost() {
        return this.mongoDBHost;
    }

    public int getMongoDBPort() {
        return this.mongoDBPort;
    }

    public int getWorldId() {
        return this.worldId;
    }

    public UUID getWorldUID() {
        return this.worldUID;
    }

    public List<LinkedHashMap<String, List<Number>>> getEnablePvpZonePoses() {
        return this.enablePvpZonePoses;
    }

}
