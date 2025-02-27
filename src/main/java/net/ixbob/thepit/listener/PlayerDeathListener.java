package net.ixbob.thepit.listener;

import net.ixbob.thepit.data.PitPlayerData;
import net.ixbob.thepit.data.PitPlayerDataManager;
import net.ixbob.thepit.util.SingletonUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.function.Supplier;

public class PlayerDeathListener implements Listener {
    private static final Supplier<PlayerDeathListener> instance = SingletonUtil.createSingletonLazy(PlayerDeathListener::new);

    private PlayerDeathListener() {
    }

    public static PlayerDeathListener getInstance() {
        return instance.get();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setCancelled(true);
        event.setKeepInventory(true);
        event.setKeepLevel(true);
        event.setShouldDropExperience(false);

        Player deathPlayer = event.getPlayer();
        Player killer = deathPlayer.getKiller();
        assert killer != null;

        double addCoin = 12.0;
        double addExp = 20.0;
        PitPlayerData killerData = PitPlayerDataManager.getInstance().getData(killer);
        killerData.addCoinAmount(addCoin);
        killerData.addXpAmount(addExp);
        deathPlayer.sendMessage(
                Component.text("你被玩家 ", NamedTextColor.RED)
                        .append(Component.text(killer.getName(), NamedTextColor.GRAY))
                        .append(Component.text(" 击杀了!", NamedTextColor.RED)));
        killer.sendMessage(
                Component.text("击杀玩家! ")
                        .color(NamedTextColor.GREEN)
                        .decoration(TextDecoration.BOLD, true)
                        .append(Component.text(deathPlayer.getName())
                                .color(NamedTextColor.GRAY)
                                .decoration(TextDecoration.BOLD, false)));
    }
}
