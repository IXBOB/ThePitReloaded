package net.ixbob.thepit;

import com.github.retrooper.packetevents.PacketEvents;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import net.ixbob.thepit.data.PitPlayerDataManager;
import net.ixbob.thepit.data.mongodb.MongoDBManager;
import net.ixbob.thepit.holder.ConfigHolder;
import net.ixbob.thepit.listener.EntityDamageByEntityListener;
import net.ixbob.thepit.listener.PlayerDeathListener;
import net.ixbob.thepit.listener.PlayerJoinListener;
import net.ixbob.thepit.listener.PlayerQuitListener;
import net.ixbob.thepit.scoreboard.PitScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
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

        saveDefaultConfig();

        // 检查配置文件版本是否过期
        if (!isConfigVersionValid()) {
            getLogger().warning("config.yml配置文件版本已过期，正在备份原有配置文件并生成新的默认配置文件");
            boolean isBackupSuccess = backupOldConfig();

            if (isBackupSuccess) {
                getLogger().info("原有配置文件已备份成功，正在生成新的默认配置文件...");
                saveResource("config.yml", true);
                getLogger().info("成功生成新的默认配置文件！");
                getLogger().info("服务器即将关闭，请配置新生成的config.yml文件后重新启动服务器");
            } else {
                getLogger().severe("无法备份原有配置文件，请检查文件占用情况后重新启动服务器");
            }

            // 无论配置是否成功，都必须先关闭服务器，以避免配置不匹配带来后续问题
            getServer().shutdown();
            return;
        }

        ConfigHolder.getInstance().reload();
        MongoDBManager.getInstance();
        PitPlayerDataManager.getInstance(); //加载类，确保观察者被实例化并激活观察者逻辑
        PitScoreboardManager.getInstance();
        registerListeners(
                EntityDamageByEntityListener.getInstance(),
                PlayerJoinListener.getInstance(),
                PlayerQuitListener.getInstance(),
                PlayerDeathListener.getInstance());
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

    private boolean isConfigVersionValid() {
        File configFile = new File(getDataFolder(), "config.yml");
        YamlConfiguration configYml = YamlConfiguration.loadConfiguration(configFile);
        int currentVersion = configYml.getInt("config-version", -1);
        return currentVersion == getBoundedDefaultConfigVersion();
    }

    /**
     * 备份旧的配置文件
     *
     * @return 备份操作是否成功
     */
    private boolean backupOldConfig() {
        File configFile = new File(getDataFolder(), "config.yml");
        String backupFileName = "config_backup_%s.yml".formatted(
                new SimpleDateFormat("yyyy-MM-dd-HH_mm_ss").format(new Date()));
        File backupFile = new File(getDataFolder(), backupFileName);

        try {
            Files.move(configFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            getLogger().info("原有配置文件已备份为" + backupFileName);
            return true;
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "备份配置文件时出错" + e.getMessage(), e);
            return false;
        }
    }

    /**
     * 获取JAR包中捆绑的默认配置文件的版本
     *
     * @return 捆绑的默认配置文件的版本
     */
    private int getBoundedDefaultConfigVersion() {
        InputStream defaultConfigStream = getResource("config.yml");
        if (defaultConfigStream == null) {
            getServer().shutdown();
            throw new RuntimeException("插件内部错误：无法获取JAR包中捆绑的默认配置文件config.yml");
        }
        @NotNull YamlConfiguration defaultConfigYml = YamlConfiguration.loadConfiguration(
                new InputStreamReader(defaultConfigStream, StandardCharsets.UTF_8));
        int defaultConfigVersion = defaultConfigYml.getInt("config-version", -1);
        if (defaultConfigVersion == -1) {
            getServer().shutdown();
            throw new RuntimeException("插件内部错误：捆绑的默认配置文件config.yml中未找到config-version项或其为-1");
        }
        return defaultConfigVersion;
    }
}
