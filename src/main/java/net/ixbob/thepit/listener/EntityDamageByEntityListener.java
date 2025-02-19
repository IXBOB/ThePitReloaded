package net.ixbob.thepit.listener;

import net.ixbob.thepit.pvp.PvpZoneManager;
import net.ixbob.thepit.util.SingletonUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class EntityDamageByEntityListener implements Listener {
    private static final Supplier<EntityDamageByEntityListener> instance = SingletonUtil.createSingletonLazy(EntityDamageByEntityListener::new);

    public static EntityDamageByEntityListener getInstance() {
        return instance.get();
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        @NotNull Entity damager = event.getDamager();
        if (damager instanceof Player
                && !PvpZoneManager.getInstance().getPosIsInPvpZone(damager.getLocation())) {
            damager.sendMessage(Component.text("非PVP区域！").color(NamedTextColor.RED));
            event.setCancelled(true);
        }
    }
}
