package net.ixbob.thepit.holder;

import net.ixbob.thepit.ThePit;

public class ConfigHolder {

    private static final ConfigHolder INSTANCE = new ConfigHolder();

    public static ConfigHolder getInstance() {
        return INSTANCE;
    }

    public void reload() {
        isMongoDBEnabled = ThePit.getInstance().getConfig().getBoolean("database.mongodb.enabled");
        mongoDBHost = ThePit.getInstance().getConfig().getString("database.mongodb.host");
        mongoDBPort = ThePit.getInstance().getConfig().getInt("database.mongodb.port");
    }

    private boolean isMongoDBEnabled;
    private String mongoDBHost;
    private int mongoDBPort;

    public boolean getIsMongoDBEnabled() {
        return this.isMongoDBEnabled;
    }

    public String getMongoDBHost() {
        return this.mongoDBHost;
    }

    public int getMongoDBPort() {
        return this.mongoDBPort;
    }

}
