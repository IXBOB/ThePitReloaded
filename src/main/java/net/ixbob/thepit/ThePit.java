package net.ixbob.thepit;

import org.bukkit.plugin.java.JavaPlugin;

public class ThePit extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Hello World!");
    }

    @Override
    public void onDisable() {
        System.out.println("Goodbye World!");
    }
}
