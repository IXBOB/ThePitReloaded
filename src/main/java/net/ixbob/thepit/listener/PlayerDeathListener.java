package net.ixbob.thepit.listener;

import net.ixbob.thepit.PlayerEconomy;
import net.ixbob.thepit.manager.PlayerEconomyManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

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
        PlayerEconomy killerEco = PlayerEconomyManager.getInstance().getEconomy(killer);
        killerEco.addCoinAmount(addCoin);
        killerEco.addXpAmount(addExp);
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
